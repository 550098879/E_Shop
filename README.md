##  E-SHOP 宠物商城

### 注意事项  
- Thymeleaf暂时与SpringBoot3.0不兼容,需要降低版本
- Thymeleaf会解析LayUI的[[...]]符号,需要将[ [] ]分开
- LayUI自带jQuery,需要使用$符号时,可以定义 var $ = layui.$; 
- Druid版本1.1.14 与MyBatisPlus对于时间类型存在兼容问题,需要提高Druid的版本
