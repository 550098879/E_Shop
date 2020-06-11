##  E-SHOP 宠物商城

### 注意事项  
- Thymeleaf暂时与SpringBoot3.0不兼容,需要降低版本
- Thymeleaf会解析LayUI的[[...]]符号,需要将[ [] ]分开
- LayUI自带jQuery,需要使用$符号时,可以定义 var $ = layui.$; 
- Druid版本1.1.14 与MyBatisPlus对于时间类型存在兼容问题,需要提高Druid的版本
- 新增本地的静态资源时,出现无法加载的情况可以考虑ReBuild Project(重构) 
- 使用layui的同事,想要自己写js代码(使用jQuery),则需要在layui.js之前导入jQuery 
- js中使用 + 加法运算符出现字符串拼接的现象,需要将字符串使用Number(str)转换为数字




### SQL 语句
- 多表关联查询
```sql
    select g1.type_id,g1.type_name,u.login_name,g2.type_name
        from goods_type g1, goods_type g2 ,users u
            where u.userId=g1.userId and u.userId=g2.userId
                and g1.super_id= g2.type_id
```



### LayUI使用心得
- render:表格,表单渲染
- on:事件绑定


