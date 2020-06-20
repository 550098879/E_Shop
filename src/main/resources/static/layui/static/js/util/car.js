/*
*@Name: 母婴商城
*@Author: xuzhiwen
*@Copyright:layui.com
*/

layui.define(['layer'], function (exports) {
    var layer = layui.layer;

    var car = {
        init: function () {
            var uls = document.getElementById('list-cont').getElementsByTagName('ul');//每一行
            var checkInputs = document.getElementsByClassName('check'); // 所有勾选框
            var checkAll = document.getElementsByClassName('check-all'); //全选框
            var SelectedPieces = document.getElementsByClassName('Selected-pieces')[0];//总件数
            var piecesTotal = document.getElementsByClassName('pieces-total')[0];//总价
            var batchdeletion = document.getElementsByClassName('batch-deletion')[0]//批量删除按钮

            //自定义所需参数
            var clearing = document.getElementById("clearing");//结算按钮
            var carIdList = {};//存储需要结算的id值
            var address = $("#address").text();

            //计算
            function getTotal() {
                var seleted = 0, price = 0;
                for (var i = 0; i < uls.length; i++) {
                    if (uls[i].getElementsByTagName('input')[0].checked) {
                        seleted += parseInt(uls[i].getElementsByClassName('Quantity-input')[0].value);
                        price += parseFloat(uls[i].getElementsByClassName('sum')[0].innerHTML);
                    }
                }
                SelectedPieces.innerHTML = seleted;
                piecesTotal.innerHTML = '￥' + price.toFixed(2);
            }

            function fn1() {
                alert(1)
            }

            // 小计
            function getSubTotal(ul) {
                var unitprice = parseFloat(ul.getElementsByClassName('th-su')[0].innerHTML);
                var count = parseInt(ul.getElementsByClassName('Quantity-input')[0].value);
                var SubTotal = parseFloat(unitprice * count)
                ul.getElementsByClassName('sum')[0].innerHTML = SubTotal.toFixed(2);
            }

            for (var i = 0; i < checkInputs.length; i++) {
                checkInputs[i].onclick = function () {
                    if (this.className === 'check-all check') {
                        for (var j = 0; j < checkInputs.length; j++) {
                            checkInputs[j].checked = this.checked;
                        }
                    }
                    if (this.checked == false) {
                        for (var k = 0; k < checkAll.length; k++) {
                            checkAll[k].checked = false;
                        }
                    }
                    getTotal()
                }
            }

            for (var i = 0; i < uls.length; i++) {
                uls[i].onclick = function (e) {
                    e = e || window.event;
                    var el = e.srcElement;
                    var cls = el.className;
                    var input = this.getElementsByClassName('Quantity-input')[0];
                    var less = this.getElementsByClassName('less')[0];
                    var val = parseInt(input.value);
                    var that = this;
                    switch (cls) {
                        case 'add layui-btn':
                            input.value = val + 1;
                            //发出ajax请求添加商品数量
                            getSubTotal(this)
                            break;
                        case 'less layui-btn':
                            if (val > 1) {
                                input.value = val - 1;
                                //发出ajax请求减少商品数量

                            }
                            getSubTotal(this)
                            break;
                        case 'dele-btn':
                            layer.confirm('你确定要删除这项商品吗?', {
                                yes: function (index, layero) {
                                    //执行ajax请求,删除该条信息
                                    // var id = layero.parents('tr').first().find('td').eq(0).text();
                                    var carId = that.childNodes[3].defaultValue;
                                    $.get("/shop/deleteById", {"carId": carId,}, function (res) {
                                        if (res) {
                                            layer.alert("订单项删除成功");
                                            getTotal()
                                        }
                                    });
                                    layer.close(index);
                                    that.parentNode.removeChild(that);
                                }
                            })
                            break;
                    }
                    getTotal()
                }
            }
            batchdeletion.onclick = function () {
                if (SelectedPieces.innerHTML != 0) {
                    layer.confirm('你确定要删除选中项吗?', {
                        yes: function (index, layero) {

                            layer.close(index)
                            for (var i = 0; i < uls.length; i++) {
                                var input = uls[i].getElementsByTagName('input')[0];
                                if (input.checked) {
                                    $.get("/shop/deleteById", {"carId": input.id,});
                                    uls[i].parentNode.removeChild(uls[i]);
                                    i--;
                                }
                            }
                            getTotal()
                        }

                    })
                } else {
                    layer.msg('请选择商品')
                }

            }
            //结算按钮事件
            clearing.onclick = function () {
                if (SelectedPieces.innerHTML != 0) {
                    //查看购物车商品详情
                    layer.open({
                        type: 1,
                        title: "购物车详情",
                        content: $("#list-cont"),
                        area: ['1200px', '700px'],
                        btn: ['购买', '取消'],
                        yes: function(index,layero){
                            layer.close(index);
                            layer.prompt({formType: 1, title: "请输入支付密码:"}, function (payPsw, index) {
                                //发送ajax请求,判断支付密码是否正确
                                $.post("/buyer/verifyPayPsw", {"payPsw": payPsw,}, function (res) {
                                    if (res) {
                                        layer.close(index)
                                        for (var i = 0; i < uls.length; i++) {
                                            var input = uls[i].getElementsByTagName('input')[0];
                                            if (input.checked) {
                                                carIdList[input.id] = input.id;//取出购物车id
                                                uls[i].parentNode.removeChild(uls[i]);//还是要移除商品项,否则会死循环
                                                i--;
                                            }
                                        }
                                        //获取到全部的被选中的商品id后,发送ajax请求,完成订单详情页面,及结算信息
                                        $.ajax({
                                            url: "/shop/clearing",
                                            type: "POST",
                                            dataType: "json",
                                            contentType: "application/json;charset=UTF-8",
                                            data: JSON.stringify(carIdList),
                                            success: function (res) {
                                                if (res == 3) {
                                                    layer.msg("订单结算成功");
                                                } else if (res == 2) {
                                                    layer.confirm("订单结算失败", function () {
                                                        location.reload();
                                                    });
                                                } else if (res == 1) {
                                                    layer.confirm("商品库存不足", function () {
                                                        location.reload();
                                                    });

                                                } else if (res == 0) {
                                                    layer.confirm("账户余额不足,请前往充值后购买", function () {
                                                        location.href = "/information";
                                                    });
                                                }else if (res == 4) {
                                                    layer.confirm("请先设置默认地址", function () {
                                                        location.href = "/information";
                                                    });
                                                }
                                            },

                                        });
                                        getTotal()

                                    } else {
                                        layer.msg("支付密码错误,请重试");
                                        layer.close(index);
                                    }
                                });
                            })
                        }
                    });

                    //输入支付密码

                } else {
                    layer.msg('请选择商品')
                }

            }
            checkAll[0].checked = true;
            checkAll[0].onclick();
        }

    }
    exports('car', car)
});


