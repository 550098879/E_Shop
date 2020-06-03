function adminManage(){
   // $("#demo").empty();
    $("#headline").text("admin管理");
    layui.use('element', function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 470
            , url: '/users/findAll' //数据接口
            , page: true //开启分页
            , cols: [
                [ //表头
                    {field: 'userId', title: 'ID', width: 80, sort: true, fixed: 'left'}
                    , {field: 'loginName', title: '用户名', width: 80}    //sort: true  ,排序
                    , {
                    field: 'status', title: '状态', width: 80, templet: function (data) {
                        return data.status == 0 ? "离线" : "在线";
                    }
                }
                    , {
                    field: 'userRole', title: '职位', width: 180, templet: function (data) {
                        return data.userRole == 0 ? "普通管理员" : "顶级管理员";
                    }
                }
                    ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:130}
                ]
            ]
        });

        table.on('tool(test)', function(obj){
            var data = obj.data;
            if(obj.event === 'update'){
                window.location.href="/menu/findById/"+data.id;
            }if(obj.event === 'del'){
                layer.confirm("确定要删除吗?",function(index){
                    window.location.href="/menu/deleteById/"+data.id;
                    layer.close(index);
                });

            }
        });


    });
}

function goodsManage(){
   //$("#demo").empty();
    $("#headline").text("商品管理");
    layui.use('element', function () {
        var table = layui.table;
        table.render({
            elem: '#demo'
            , height: 470
            , url: '/users/findAll' //数据接口
            , page: true //开启分页
            , cols: [
                [ //表头
                        {field: 'userId', title: 'ID', width: 80, sort: true, fixed: 'left'}
                    ,{fixed: 'right', title:'操作', toolbar: '#barDemo', width:130}
                ]
            ]
        });
    });
}
