package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.UsesSunMisc;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.VO.DataVO;
import org.zyx.VO.GoodsVO;
import org.zyx.VO.MapVO;
import org.zyx.VO.TypeVO;
import org.zyx.entity.Goods;
import org.zyx.entity.GoodsPic;
import org.zyx.entity.GoodsType;
import org.zyx.enums.GoodPicType;
import org.zyx.enums.GoodsStatus;
import org.zyx.mapper.GoodsMapper;
import org.zyx.mapper.GoodsPicMapper;
import org.zyx.mapper.GoodsTypeMapper;
import org.zyx.service.GoodsService;
import org.zyx.service.GoodsTypeService;
import org.zyx.utils.ConnectTencentCloud;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/goods")
public class GoodsHandler {

    @Autowired
    private GoodsPicMapper goodsPicMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsTypeMapper goodsTypeMapper;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ConnectTencentCloud cloud;
    @Autowired
    private GoodsTypeService goodsTypeService;

    @GetMapping("/findAllType")
    public DataVO<TypeVO> findAllType(int page, int limit){
        DataVO<TypeVO> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(goodsTypeMapper.selectCount(null));
        dataVO.setMsg("商品类型数据");
        //分页查询
        dataVO.setData(goodsTypeMapper.findAll((page-1)*limit,limit));
        return dataVO;
    }

    @GetMapping("/findAllGood")
    public DataVO<GoodsVO> findAllGood(int page, int limit, HttpSession session,
                                       @RequestParam(name = "type_id" ,required = false ,defaultValue = "-1") int type_id){
        DataVO<GoodsVO> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功

        dataVO.setMsg("商品数据");
        //分页查询
        if(type_id == -1){
            List<GoodsVO> allGoods = goodsService.findAllGoods(page, limit);
            dataVO.setCount(goodsMapper.selectCount(null));
            dataVO.setData(allGoods);
        }else{
            QueryWrapper<Goods> wrapper = new QueryWrapper<>();
            wrapper.eq("type_id",type_id);
            wrapper.eq("status", GoodsStatus.PUT_AWAY.getType());
            dataVO.setCount(goodsMapper.selectCount(wrapper));
            List<GoodsVO> allGoods = goodsService.findByTypeId(type_id,page,limit);
            dataVO.setData(allGoods);
        }

        return dataVO;
    }

    @GetMapping("/findSuper")
    public List<GoodsType> findSuper(){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("super_id",0);
        return goodsTypeMapper.selectList(wrapper);
    }

    @GetMapping("/findChildType")
    public List<GoodsType> findChildType(int superId){
        QueryWrapper wrapper = new QueryWrapper();

        wrapper.eq("super_id",superId);
        return goodsTypeMapper.selectList(wrapper);
    }

    @PostMapping("/upload")
    public MapVO upload(MultipartFile file, HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile= multipartRequest.getFile("file");
        InputStream inputStream = multipartFile.getInputStream();
        String fileKey = UUID.randomUUID().toString()+multipartFile.getOriginalFilename();
        System.out.println(fileKey);
        //图片上传云服务
        cloud.uploadStream(inputStream,fileKey,multipartFile.getSize());

        MapVO picVO = new MapVO();
        picVO.setCode(0);
        picVO.setMsg("图片");
        Map map = new HashMap();
        map.put("src","/goods/img/"+fileKey);
        picVO.setData(map);
        return picVO;
    }

    //将图片请求重定向到服务器的访问路径
    @GetMapping("/img/{fileKey}")
    public String getImage (@PathVariable("fileKey") String fileKey, HttpServletResponse response) throws MalformedURLException {
        String url = cloud.getUrl(fileKey);
        return url;
        //通过流的方式将图片显示到前端,有点麻烦,且有所延迟
//        URL fileUrl = new URL(url);
//        BufferedInputStream fis = null;
//        OutputStream os = null;
//        try {
//            fis =  new BufferedInputStream(fileUrl.openStream());
//            os = response.getOutputStream();
//            int count = 0;
//            byte[] buffer = new byte[1024 * 8];
//            while ((count = fis.read(buffer)) != -1) {
//                os.write(buffer, 0, count);
//                os.flush();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fis.close();
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }


    @PostMapping("/addGood")
    public String addGood(Goods goods,String cover){
        //先添加商品
        System.out.println(goods);
        System.out.println(cover);

        if(goods.getGoodId() == 0){
            if(goodsMapper.selectOne(new QueryWrapper<Goods>().eq("goods_name",goods.getGoodsName())) != null){
                return "商品重名了,请重试";
            }
            goods.setGoodId(null);
            if(goodsMapper.insert(goods) == 0){
                return "商品添加失败";
            }
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("goods_name",goods.getGoodsName());
            goods = goodsMapper.selectOne(queryWrapper);
            //添加封面
            GoodsPic goodsPic = new GoodsPic();
            goodsPic.setGoodsId(goods.getGoodId());
            goodsPic.setPicPath(cover);
            goodsPic.setPicType(GoodPicType.cover.getType());
            goodsPicMapper.insert(goodsPic);
        }else{
            goodsMapper.updateById(goods);//更新商品
            QueryWrapper<GoodsPic> wrapper = new QueryWrapper<GoodsPic>();
            wrapper.eq("goods_id", goods.getGoodId());
            wrapper.eq("pic_type", GoodPicType.cover.getType());
            GoodsPic goodsPic = goodsPicMapper.selectOne(wrapper);
            goodsPic.setPicPath(cover);
            goodsPicMapper.updateById(goodsPic);//更新图片
            return "商品更新成功";
        }

        return "商品添加成功";
    }

    @GetMapping("/deleteById")
    public void  deleteById(int goodId){
        //存储桶移除图片
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("pic_type", GoodPicType.cover.getType());//查找封面图
        wrapper.eq("goods_id",goodId);
        String picPath = goodsPicMapper.selectOne(wrapper).getPicPath();
        String key = picPath.substring(picPath.lastIndexOf('/'));
        cloud.deleteObjectRequest(key);
        goodsMapper.deleteById(goodId);//数据库移除商品
    }

    @PostMapping("/updateById/{Id}")
    public void updateById(@PathVariable("Id") int Id,Goods goods){
        goods.setGoodId(Id);
        System.out.println(goods);
        goodsMapper.updateById(goods);
    }

    @GetMapping("/findById/{goodId}")
    public ModelAndView findById(@PathVariable("goodId") int goodId,ModelAndView modelAndView){

        //后续添加异常处理,若没有该商品id
        GoodsVO goodsVO = goodsService.findById(goodId);
        if(goodsVO == null){
            modelAndView.setViewName("commodity");
            return modelAndView;
        }
        int typeId = goodsVO.getGoods().getTypeId();

        List<GoodsVO> likeGoods = goodsService.findByTypeId(typeId, 1, 3);
        likeGoods.remove(goodsVO);//移除当前商品
        modelAndView.addObject("good",goodsVO);
        modelAndView.addObject("likeGoods",likeGoods);
        modelAndView.setViewName("details");
        return modelAndView;
    }

    @GetMapping("/findBySearch")
    public ModelAndView findBySearch(String title,ModelAndView modelAndView,HttpSession session){
        List<Goods> goodsList = goodsMapper.selectList(new QueryWrapper<Goods>().like("goods_name", title));
        List<GoodsVO> searchGoods = goodsService.findByGoodList(goodsList);
        session.setAttribute("searchGoods",searchGoods);
        session.setAttribute("title",title);
        modelAndView.addObject("allTypeList",goodsTypeService.getAllType());
        modelAndView.setViewName("findBySearch");
        return modelAndView;
    }

}
