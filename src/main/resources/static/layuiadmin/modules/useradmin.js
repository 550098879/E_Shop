/** layuiAdmin.std-v1.0.0 LPPL License By http://www.layui.com/admin/ */
;layui.define(["table", "form"], function (e) {
    var t = layui.$, i = layui.table;
    layui.form;
    i.render({
        //管理员表
        elem: "#LAY-user-manage",
        url: '/users/findAll',
        cols: [[{type: "checkbox", fixed: "left"},
            {field: "userId", width: 100, title: "ID", sort: !0},
            {field: "loginName", title: "用户名", width: 200}, // templet: "#imgTpl" 加载图片
            {field: "status", title: "状态" , width: 200 ,templet:function(data){
                    return data.status == 0 ? "在线" : "离线";
                }},
            {field: "userRole", title: "职位" , width: 200,templet:function(data){
                    return data.userRole == 0 ? "普通管理员" : "超级管理员";
                }},
            {
            title: "操作",
            width: 200,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-webuser"
        }]],
        page: !0,
        limit: 10,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            if(t != 123){
                layer.alert("口令错误");
                return;
            }
            layer.close(i), layer.confirm("确定删除此管理员?", function (t) {
                var $ = layui.jquery;
                $.get("/users/deleteById",{"userId": e.data.userId,})
                e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            var userId = e.data.userId;
            layer.open({
                type: 2,
                title: "编辑用户",
                content: "../../../views/user/user/userform.html",
                maxmin: !0,
                area: ["450px", "300px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-front-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        var $ = layui.jquery;
                        $.get("/users/updateById",{"userId":userId,"loginName":t.field.loginName,"psw":t.field.psw,})
                        i.reload("LAY-user-manage"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), i.render({
        //买家表
        elem: "#LAY-user-back-manage",
        url: "/buyer/findAll",

        cols: [[{type: "checkbox", fixed: "left"},
            {field: "buyerId", width: 80, title: "买家ID", sort: !0}, {
            field: "buyerName",
            title: "买家昵称"
        }, {field: "account", title: "账号"}, {field: "balance", title: "余额"}, {
            field: "payPsw",
            title: "支付密码", templet: function(data){
                    return data.payPsw == null ? "新用户" : data.payPsw ;
                }
        }, {field: "face", title: "头像", sort: !0 ,templet:"#imgTpl"}, {
            field: "createTime",
            title: "创建时间",
            minWidth: 80,
        }, {title: "操作", width: 150, align: "center", fixed: "right", toolbar: "#table-useradmin-admin"}]],
        text: "对不起，加载出现异常！",
        page: !0,
        limit: 10,
        height: "full-220",
    }), i.on("tool(LAY-user-back-manage)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            layer.close(i), layer.confirm("确定删除此用户？", function (t) {
                console.log(e), e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑管理员",
                content: "../../../views/user/administrators/adminform.html",
                area: ["420px", "420px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-back-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        i.reload("LAY-user-front-submit"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), i.render({
        elem: "#LAY-user-back-role",
        url: layui.setter.base + "json/useradmin/role.js",
        cols: [[{type: "checkbox", fixed: "left"}, {field: "id", width: 80, title: "ID", sort: !0}, {
            field: "rolename",
            title: "角色名"
        }, {field: "limits", title: "拥有权限"}, {field: "descr", title: "具体描述"}, {
            title: "操作",
            width: 150,
            align: "center",
            fixed: "right",
            toolbar: "#table-useradmin-admin"
        }]],
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-user-back-role)", function (e) {
        e.data;
        if ("del" === e.event) layer.confirm("确定删除此角色？", function (t) {
            e.del(), layer.close(t)
        }); else if ("edit" === e.event) {
            t(e.tr);
            layer.open({
                type: 2,
                title: "编辑角色",
                content: "../../../views/user/administrators/roleform.html",
                area: ["500px", "480px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],
                        r = t.find("iframe").contents().find("#LAY-user-role-submit");
                    l.layui.form.on("submit(LAY-user-role-submit)", function (t) {
                        t.field;
                        i.reload("LAY-user-back-role"), layer.close(e)
                    }), r.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),i.render({
        //类别表
        elem: "#LAY-goods-type",
        url: '/goods/findAllType',
        cols: [[{type: "checkbox", fixed: "left"},
            {field: "typeId", width: 100, title: "类型ID", sort: !0},
            {field: "typeName", title: "类别名", width: 200}, // templet: "#imgTpl" 加载图片
            {field: "userName", title: "创建者" , width: 200 },
            {field: "superName", title: "上级类别" , width: 200 },
            {
                title: "操作",
                width: 200,
                align: "center",
                fixed: "right",
                toolbar: "#table-type-handle"
            }
        ]],
        page: !0,
        limit: 10,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-goods-type)", function (e) {
        e.data;
        if ("del" === e.event) layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            if(t != 123){
                layer.alert("口令错误");
                return;
            }
            layer.close(i), layer.confirm("确定删除此管理员?", function (t) {
                var $ = layui.jquery;
                $.get("/users/deleteById",{"userId": e.data.userId,})
                e.del(), layer.close(t)
            })
        }); else if ("edit" === e.event) {
            t(e.tr);
            var userId = e.data.userId;
            layer.open({
                type: 2,
                title: "编辑用户",
                content: "../../../views/user/user/userform.html",
                maxmin: !0,
                area: ["450px", "300px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e], r = "LAY-user-front-submit",
                        n = t.find("iframe").contents().find("#" + r);
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;
                        var $ = layui.jquery;
                        $.get("/users/updateById",{"userId":userId,"loginName":t.field.loginName,"psw":t.field.psw,})
                        i.reload("LAY-user-manage"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }),  i.render({
        //商品列表
        elem: "#LAY-goods-list",
        url: '/goods/findAllGood',
        cols: [[{type: "checkbox", fixed: "left"},
            {field: "goodId", width: 100, title: "商品ID", sort: !0 ,templet:function(data){return data.goods.goodId;}},
            {field: "goodsName", title: "商品名", width: 120 ,templet:function(data){return data.goods.goodsName;}}, // templet: "#imgTpl" 加载图片
            {field: "typeName", title: "类别" , width: 100 },
            {field: "superName", title: "上级类别" , width: 100 },
            {field: "price", title: "单价" , width: 100 , sort: !0 ,templet:function(data){return data.goods.price;}},
            {field: "stock", title: "库存" , width: 100 , sort: !0 ,templet:function(data){return data.goods.stock;}},
            {field: "status", title: "状态" , width: 120 ,templet:function(data){
                    return data.goods.status == 0 ? "未上架" : "已上架";
                }},
            {field: "cover" , title: "封面" , width: 120 ,templet:"#coverImg"},
            {field: "remark", title: "备注" , width: 120 ,templet:function(data){return data.goods.remark;}},
            {
                title: "操作",
                width: 200,
                align: "center",
                fixed: "right",
                toolbar: "#table-list-handle"
            }
        ]],
        page: !0,
        limit: 10,
        height: "full-220",
        text: "对不起，加载出现异常！"
    }), i.on("tool(LAY-goods-list)", function (e) {
        e.data;
        if ("del" === e.event){ layer.prompt({formType: 1, title: "敏感操作，请验证口令"}, function (t, i) {
            if(t != 123){
                layer.alert("口令错误");
                return;
            }
            layer.close(i), layer.confirm("确定删除此商品?", function (t) {
                var $ = layui.jquery;
                console.log(e.data);
                $.get("/goods/deleteById",{"goodId": e.data.goods.goodId})
                e.del(), layer.close(t)
            })
        });} else if ("edit" === e.event) {
            t(e.tr);
            var data = e.data;
            layer.open({
                type: 2,
                title: "编辑商品",
                content: "/views/template/addgood.html",
                maxmin: !0,
                area: ["650px", "600px"],
                btn: ["确定", "取消"],
                yes: function (e, t) {
                    var l = window["layui-layer-iframe" + e],  //子窗口
                        r = "LAY-good-add-submit",             //提交按钮id
                        n = t.find("iframe").contents().find("#" + r);//提交按钮


                    //获取form表单的提交事件
                    l.layui.form.on("submit(" + r + ")", function (t) {
                        t.field;

                        console.log(t.field);
                        var $ = layui.jquery;
                        $.post("/goods/updateById/"+data.goods.goodId,t.field)

                        i.reload("LAY-goods-list"), layer.close(e)
                    }), n.trigger("click")
                },
                success: function (e, t) {
                }
            })
        }
    }), e("useradmin", {})
});