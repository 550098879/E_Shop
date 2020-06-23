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
- MySQL的 decimal 数据类型,需要手动设置精度 decimal(小数点前保留位数,小数点后保留位数);
- Cookie失效的原因: 未设置path属性,导致客户端无法找到cookie? cookie.setPath("/"); cookie.setMaxAge("2400");
- BigDecimal 数据类型的add方法会返回一个BigDecimal 对象,所以需要:bigDecimal = bigDecimal.add(new BigDecimal(123));
- 事务回滚: TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 


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
- 想要使用checkbox 的change事件,需要将整个表格放到 <div class="layui-form"> 或 form 表单中,使用form.on("switch(filter)",function(data){}");
```html
<!--表格-->
<div class="layui-form">
    <table id="receiving" lay-filter="receiving"></table>
</div>
<script type="text/html" id="statusTpl">
    <input type="checkbox" lay-skin="switch" lay-text="默认|未使用"
           lay-filter="changeDefault"
           {{ d.status== "1" ? "checked" : "" }} />
</script>

<script>
//渲染表格
table.render({
            id: "testCheck",
            elem: "#receiving",
            url: "/buyer/getAddress",
            cols: [
                [{field: "addressId", width: 80, title: "ID", sort: !0},
                    {field: "status", width: 120, title: "正在使用", templet: "#statusTpl", filter: true},
                    ]
            ],
            page: !0,
            limit: 10,
            // height: "full-350",
            text: "对不起，加载出现异常！"
        });
//获取当前行的id值
        form.on('switch(changeDefault)', function (obj) {
                   console.log(obj);
                   var data = $(obj.elem);
                   //遍历父级tr，取第一个，然后查找第一个td，取值
                   var id = data.parents('tr').first().find('td').eq(0).text();
                   console.log(id);
                    //后续执行相应操作
         });

</script>
```
- layui 自定义表单组件:
```html
    <div class="layui-form">
        <button class="layui-btn" lay-submit="lay-buyer-submit" lay-filter="lay-buyer-submit" >注册</button>
    </div>
<!--lay-submit="" 这个属性是必须的-->
    <script>        
    var form = layui.form;
    //监听提交
    form.on('submit(lay-buyer-submit)', function(data){
        data.field;//获取表单中的所有数据(json格式)
    });
    </script>   

```
- 想要在非layui.use();中使用layui的组件时,只需要引入 layui.all.js 文件即可
```html
<script src="/layui/layui/layui.all.js"></script>
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

- LayUI模板引擎导入数据
```html
<div id="orderDetails"></div>

<script type="text/html" id="demo">
    {{# layui.each(d.data,function(index,item){}}
    <!--导入对应样式数据-->
    <div style="width: 100%;height:120px; ">
        <div class="layui-col-md4" align="center"><img src="{{item.goodsVO.cover}}" style="width: 80%;"></div>
        <div class="layui-col-md4">
            <ul>
                <li>商品名: {{item.goodsVO.goods.goodsName}}</li>
                <li>分类: <span>{{item.goodsVO.superName}}</span> <span>{{item.goodsVO.typeName}}</span></li>
                <li>单价: {{item.goodsVO.goods.price}}</li>
                <li>数量: {{item.goodCar.num}}</li>

            </ul>
        </div>
        <div class="layui-col-md4">
            <button>已发货</button>
            <h3>总计:小计: {{item.goodsVO.goods.price * item.goodCar.num}}</h3>
        </div>
    </div>
    {{# });}}
</script>

<script>
     var html = demo.innerHTML,
     orderDetails = document.getElementById('orderDetails');
    $.get("/shop/getShopCartInfo", {}, function (res) {
        orderDetails.innerHTML = mm.renderHtml(html, res)
        element.render();
    });
</script>

```
- LayUI 筛选表格数据(点击修改url并重载)
```javascript
table.reload("tableId" ,{url: newUrl, page:{curr: 1}, where:{}});
```

### js技巧
-  window.location.href = document.referrer ; 回到前一页并刷新该页
- a标签未添加href属性时,在onclick事件中进行跳转会回到当前页面,先执行onclick事件,后执行href跳转
- isNaN() 函数用于检查其参数是否是非数字值。如isNaN("test");返回的是true，而isNaN("123")，则返回false；

### 前端ajax请求发送json对象以及服务端获取
```javascript
//js
$.ajax({
    url: "/shop/clearing",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=UTF-8",
    data: JSON.stringify(carIdList),
    success: function () {
        layer.msg("订单结算成功");
    },

});

```
```java
//java
@PostMapping("/clearing")  //map可以换成对应实体类的list集合
public void clearing(@RequestBody Map carIdList, HttpSession session){
    carIdList.forEach((k,v)->System.out.println("key : " + k + " value : " + v));
}

```

- 部署服务器后需要放行端口
- 项目部署语句 : nohup java -jar E_Shop-1.0-SNAPSHOT.jar >logger.txt &