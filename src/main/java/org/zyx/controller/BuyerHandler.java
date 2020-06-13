package org.zyx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.zyx.VO.DataVO;
import org.zyx.entity.Buyer;
import org.zyx.entity.Users;
import org.zyx.enums.RegStatus;
import org.zyx.mapper.BuyerMapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/buyer")
public class BuyerHandler {

    @Autowired
    private BuyerMapper buyerMapper;

    @GetMapping("/findAll")
    public DataVO<Buyer> findAll(int page,int limit){
        DataVO<Buyer> dataVO = new DataVO();
        dataVO.setCode(0); //返回的code为0,成功
        dataVO.setCount(buyerMapper.selectCount(null));
        dataVO.setMsg("买家数据");
        //分页查询
        Page<Buyer> page1 =  new Page<>(page, limit);
        dataVO.setData(buyerMapper.selectPage(page1,null).getRecords());
        return dataVO;
    }


    /**
     * 处理注册逻辑
     * @return
     */
    @PostMapping("/reg")
    public int reg(Buyer buyer){

        buyer.setBalance(new BigDecimal(0));
        buyer.setCreateTime(LocalDateTime.now());
        buyer.setFace("/images/默认头像.png");//设置默认头像

        QueryWrapper<Buyer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",buyer.getAccount());
        //判断账户是否已存在
        if(buyerMapper.selectList(queryWrapper).size() > 0){
            return RegStatus.ACCOUNT_EXIST.getType();
        }
        //添加用户数据
        if(buyerMapper.insert(buyer) == 0){
            return RegStatus.REG_FAILED.getType();
        }

        return RegStatus.REG_SUCCEED.getType();
    }

    @PostMapping("/login")
    public boolean login(Buyer buyer, HttpSession session , HttpServletResponse response,
                         @RequestParam(name = "rem", required=false, defaultValue="no") String rem){
        QueryWrapper<Buyer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",buyer.getAccount());
        queryWrapper.eq("psw",buyer.getPsw());
        buyer = buyerMapper.selectOne(queryWrapper);
        //判断是否登录成功
        if(buyer == null){
            return false;
        }
        session.setAttribute("buyer",buyer);
        if("on".equals(rem)){
            //判断出是否记住密码,将密码放入Cookie中
            System.out.println("记住密码");
            response.addCookie(new Cookie("account",buyer.getAccount()));
            response.addCookie(new Cookie("psw",buyer.getPsw()));
        }else{
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

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session,ModelAndView modelAndView){

        session.removeAttribute("buyer");
        modelAndView.setViewName("login");
        return modelAndView;
    }


}
