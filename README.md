##  E-SHOP 宠物商城

### 注意事项  
- Thymeleaf暂时与SpringBoot3.0不兼容,需要降低版本
- Thymeleaf会解析LayUI的[[...]]符号,需要将[ [] ]分开
- LayUI自带jQuery,需要使用$符号时,可以定义 var $ = layui.$; 
- Druid版本1.1.14 与MyBatisPlus对于时间类型存在兼容问题,需要提高Druid的版本
- 新增本地的静态资源时,出现无法加载的情况可以考虑ReBuild Project(重构) 
- 使用layui的同事,想要自己写js代码(使用jQuery),则需要在layui.js之前导入jQuery 
- js中使用 + 加法运算符出现字符串拼接的现象,需要将字符串使用Number(str)转换为数字
- 同一个Controller之间的请求调用,可以直接调用方法,设置相同的返回值即可



### SQL 语句
- 多表关联查询
```sql
    select g1.type_id,g1.type_name,u.login_name,g2.type_name
        from goods_type g1, goods_type g2 ,users u
            where u.userId=g1.userId and u.userId=g2.userId
                and g1.super_id= g2.type_id

    select * from goods g , goods_pic p where g.good_id = p.goods_id and p.pic_type = 0

```



### LayUI使用心得
- render:表格,表单渲染
- on:事件绑定
- js中数据丢失时,可以尝试定义一个额外的变量来实现数据传递
- layui 自定义表单组件:
```html
    <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
        <button class="layui-btn" lay-submit="lay-buyer-submit" lay-filter="lay-buyer-submit" >注册</button>
    </div>
    <script>        
    var form = layui.form;
    //监听提交
    form.on('submit(lay-buyer-submit)', function(data){
        data.field;//获取表单中的所有数据(json格式)
    });
    </script>   

```
### layUI的弹出窗使用  

- 使用隐藏div,基于同一个界面,使用layer.open();打开该div于弹出窗
```html
    <div style="display:none;" id="test">内容</div>
    <script>
        layui.config({
            base: '../layui/static/js/util/' //你存放新模块的目录，注意，不是layui的模块目录
          }).use(['mm'],function(){
              var  mm = layui.mm;//下拉框依赖模块
               
              $("#id").click(function(){
                layer.open({
                            type: 1,
                            title: "测试",
                            content: $("#test"),
                            area: ['600px', '600px'],
                            btn: ['确定', '取消'],
                            });
                });

        });
    </script>

```


### js技巧
-  window.location.href = document.referrer ; 回到前一页并刷新该页
