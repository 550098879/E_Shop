package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.VO.DataVO;
import org.zyx.VO.MapVO;
import org.zyx.entity.Buyer;
import org.zyx.entity.Store;
import org.zyx.entity.Users;
import org.zyx.enums.RegStatus;
import org.zyx.mapper.BuyerMapper;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/buyer")
public class BuyerHandler {

    @Autowired
    private BuyerMapper buyerMapper;
    @Autowired
    private ConnectTencentCloud cloud;
    @Autowired
    private StoreMapper storeMapper;


    @GetMapping("/findAll")
    public DataVO<Buyer> findAll(int page, int limit) {
        DataVO<Buyer> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(buyerMapper.selectCount(null));
        dataVO.setMsg("买家数据");
        //分页查询
        Page<Buyer> page1 = new Page<>(page, limit);
        dataVO.setData(buyerMapper.selectPage(page1, null).getRecords());
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

        session.setAttribute("buyer", buyer);
        if ("on".equals(rem)) {
            //判断出是否记住密码,将密码放入Cookie中
            System.out.println("记住密码");
            response.addCookie(new Cookie("account", buyer.getAccount()));
            response.addCookie(new Cookie("psw", buyer.getPsw()));
        } else {
            //移除cookie
            System.out.println("忘记密码");
            Cookie account = new Cookie("account", "");
            account.setMaxAge(0);
            response.addCookie(account);
            Cookie psw = new Cookie("psw", "");
            psw.setMaxAge(0);
            response.addCookie(psw);
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
    public boolean updateById(Buyer buyer) {
        System.out.println(buyer);
        if (buyerMapper.updateById(buyer) > 0) {
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

        //添加针对订单表记录
        Store store = new Store();
        store.setBuyerId(buyer.getBuyerId());
        store.setStoreTime(LocalDateTime.now());
        store.setAmount(money);
        storeMapper.insert(store);

        return buyer.getBalance();
    }


}
