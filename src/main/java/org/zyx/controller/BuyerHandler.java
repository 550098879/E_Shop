package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.VO.DataVO;
import org.zyx.VO.MapVO;
import org.zyx.entity.*;
import org.zyx.enums.AddressStatus;
import org.zyx.enums.OrderStatus;
import org.zyx.enums.RegStatus;
import org.zyx.mapper.AddressMapper;
import org.zyx.mapper.BuyerMapper;
import org.zyx.mapper.GoodCarMapper;
import org.zyx.mapper.StoreMapper;
import org.zyx.utils.ConnectTencentCloud;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/buyer")
public class BuyerHandler {

    @Autowired
    private BuyerMapper buyerMapper;
    @Autowired
    private ConnectTencentCloud cloud;
    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private GoodCarMapper goodCarMapper;

    @GetMapping("/findAll")
    public DataVO<Buyer> findAll(int page, int limit) {
        DataVO<Buyer> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(buyerMapper.selectCount(null));
        dataVO.setMsg("买家数据");
        //分页查询
        Page<Buyer> page1 = new Page<>(page, limit);
        List<Buyer> list = buyerMapper.selectPage(page1, null).getRecords();
        List<Buyer> buyerList = new ArrayList<>();
        for(Buyer buyer : list){
            buyer.setFace(cloud.getUrl(buyer.getFace().substring(buyer.getFace().lastIndexOf('/'))));
            buyerList.add(buyer);
        }
        dataVO.setData(buyerList);
        return dataVO;
    }


    /**
     * 处理注册逻辑
     *
     * @return
     */
    @PostMapping("/reg")
    public int reg(Buyer buyer) {

        buyer.setBalance(new BigDecimal(0));
        buyer.setCreateTime(LocalDateTime.now());
        buyer.setFace("/images/默认头像.png");//设置默认头像

        QueryWrapper<Buyer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", buyer.getAccount());
        //判断账户是否已存在
        if (buyerMapper.selectList(queryWrapper).size() > 0) {
            return RegStatus.ACCOUNT_EXIST.getType();
        }
        //添加用户数据
        if (buyerMapper.insert(buyer) == 0) {
            return RegStatus.REG_FAILED.getType();
        }

        return RegStatus.REG_SUCCEED.getType();
    }

    @PostMapping("/login")
    public boolean login(Buyer buyer, HttpSession session, HttpServletResponse response,
                         @RequestParam(name = "rem", required = false, defaultValue = "no") String rem) {
        QueryWrapper<Buyer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", buyer.getAccount());
        queryWrapper.eq("psw", buyer.getPsw());
        buyer = buyerMapper.selectOne(queryWrapper);
        //判断是否登录成功
        if (buyer == null) {
            return false;
        }
        //修改buyer的头像为远程头像
        buyer.setFace(cloud.getUrl(buyer.getFace().substring(buyer.getFace().lastIndexOf('/'))));
        session.setAttribute("shopCartCount",
                goodCarMapper.selectCount(new QueryWrapper<GoodCar>().eq("buyer_id",buyer.getBuyerId())));
        session.setAttribute("buyer", buyer);
        if ("on".equals(rem)) {
            //判断出是否记住密码,将密码放入Cookie中
            Cookie account = new Cookie("account", buyer.getAccount());
            account.setPath("/");
            response.addCookie(account);
            Cookie psw = new Cookie("psw", buyer.getPsw());
            psw.setPath("/");
            response.addCookie(psw);
        }else{
            response.addCookie(new Cookie("account", ""));
            response.addCookie(new Cookie("psw", ""));
        }
        return true;
    }

    //退出登录
    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session, ModelAndView modelAndView) {

        session.removeAttribute("buyer");
        modelAndView.setViewName("login");
        return modelAndView;
    }


    /**
     * 更新买家信息
     *
     * @param buyer
     * @return
     */
    @PostMapping("/updateById")
    public boolean updateById(Buyer buyer,HttpSession session) {
        if (buyerMapper.updateById(buyer) > 0) {
            //更新session:
            Buyer buyer1 = (Buyer) session.getAttribute("buyer");
            String face = buyer1.getFace();
            buyer = buyerMapper.selectById(buyer.getBuyerId());
            buyer.setFace(face);
            session.setAttribute("buyer",buyer);

            return true;
        }
        return false;
    }


    //修改头像
    @PostMapping("/upload")
    public MapVO upload(MultipartFile file, HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("file");
        InputStream inputStream = multipartFile.getInputStream();
        String fileKey = UUID.randomUUID().toString() + multipartFile.getOriginalFilename();
        System.out.println(fileKey);
        //图片上传云服务
        cloud.uploadStream(inputStream, fileKey, multipartFile.getSize());

        MapVO picVO = new MapVO();
        picVO.setCode(0);
        picVO.setMsg("头像");
        Map map = new HashMap();
        map.put("src", "/buyer/img/" + fileKey);
        picVO.setData(map);

        //新增更新用户头像,并修改session
        Buyer buyer = (Buyer) request.getSession().getAttribute("buyer");
        buyer.setFace(map.get("src").toString());
        buyerMapper.updateById(buyer);//修改数据库中的文件路径
        buyer.setFace(cloud.getUrl(fileKey));//修改session中的文件路径
        request.getSession().setAttribute("buyer", buyer);

        return picVO;
    }

    //将图片请求重定向到服务器的访问路径
    @GetMapping("/img/{fileKey}")
    public String getImage(@PathVariable("fileKey") String fileKey, HttpSession session) throws MalformedURLException {
        String url = cloud.getUrl(fileKey);
        return url;
    }


    //充值
    @Transactional  //设置事务锁,避免冲突
    @PostMapping("/updateBalance")
    public BigDecimal updateBalance(BigDecimal money, HttpSession session) {

        Buyer buyer = (Buyer) session.getAttribute("buyer");
        Buyer updBuyer = buyerMapper.selectById(buyer.getBuyerId());//从数据库查询数据,保证数据的准确性

        //判断数据准确性,保证数据库安全
        if(money.compareTo(BigDecimal.ZERO) == -1){
            System.out.println("充值金额不能为负数");
            return new BigDecimal(-1);
        }
        updBuyer.setBalance(buyer.getBalance().add(money));
        buyerMapper.updateById(updBuyer);//更新数据库
        buyer.setBalance(updBuyer.getBalance());
        session.setAttribute("buyer",buyer);//设置session中的值

        //添加针对充值表记录
        Store store = new Store();
        store.setBuyerId(buyer.getBuyerId());
        store.setStoreTime(LocalDateTime.now());
        store.setAmount(money);
        storeMapper.insert(store);
        return buyer.getBalance();
    }

    @GetMapping("/findStoreHistory")
    public DataVO<Store> findStoreHistory(int page,int limit,HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        DataVO<Store> dataVO = new DataVO();

        dataVO.setCode(0);
        dataVO.setMsg("新增订单");
        QueryWrapper<Store> queryWrapper = new QueryWrapper<Store>().eq("buyer_id", buyer.getBuyerId());
        dataVO.setCount(storeMapper.selectCount(queryWrapper));
        dataVO.setData(storeMapper.selectPage(new Page<>(page, limit), queryWrapper).getRecords());
        return dataVO;
    }

    //管理员所需充值记录
    @GetMapping("/findStoreHistoryAll")
    public DataVO<Store> findStoreHistoryAll(int page,int limit,Integer buyerId,HttpSession session){
        DataVO<Store> dataVO = new DataVO();
        dataVO.setCode(0);
        dataVO.setMsg("买家充值记录");
        QueryWrapper<Store> queryWrapper = new QueryWrapper<>();
        if(buyerId != null){
            queryWrapper.eq("buyer_id",buyerId);
        }

        dataVO.setCount(storeMapper.selectCount(queryWrapper));
        dataVO.setData(storeMapper.selectPage(new Page<>(page, limit), queryWrapper).getRecords());
        return dataVO;
    }



    /**
     * 添加收货地址
     * @param address
     * @return
     */
    @PostMapping("/addReceiving")
    public Boolean addReceiving(Address address){
        addressMapper.insert(address);
        //判断是否设置为默认地址
        if(address.getStatus() == AddressStatus.START_USE.getType()){
            addressMapper.disableOther(address.getAddressId(),address.getBuyerId());
        }


        return true;
    }

    @GetMapping("/getAddress")
    public DataVO getAddress(HttpSession session,int page, int limit){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id",buyer.getBuyerId());

        DataVO<Address> addressVO = new DataVO<>();
        addressVO.setCode(0);
        addressVO.setCount(addressMapper.selectCount(queryWrapper));
        addressVO.setMsg("地址信息");
        addressVO.setData(addressMapper.selectPage(new Page<>(page,limit),queryWrapper).getRecords());

        return addressVO;
    }

    @GetMapping("/setDefault")
    public boolean setDefault(int addressId,HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        int buyerId = buyer.getBuyerId();
        addressMapper.setDefault(addressId);//设置默认
        addressMapper.disableOther(addressId,buyerId);//禁用其他

        return true;
    }


    @GetMapping("/deleteAddressById")
    public boolean deleteAddressById(int addressId,HttpSession session){
        if(addressMapper.deleteById(addressId) == 0 ){
            return false;
        }
        return true;
    }

    @GetMapping("/findDefaultAddress")
    public String findDefaultAddress(HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if(buyer == null){
            return null;
        }
        int buyerId = buyer.getBuyerId();
        QueryWrapper<Address> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("buyer_id",buyerId);
        queryWrapper.eq("status",AddressStatus.START_USE.getType());
        Address address = addressMapper.selectOne(queryWrapper);
        if(address == null){
            return null;
        }
        return address.getAddress();
    }


    /**
     * 验证支付密码
     */
    @PostMapping("/verifyPayPsw")
    public Boolean verifyPayPsw(String payPsw,HttpSession session){
        Buyer buyer = (Buyer) session.getAttribute("buyer");
        if(buyer.getPayPsw().equals(payPsw)){
            return true;
        }
        return false;
    }


}
