layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;



    /**
     * 加载数据表格
     */
    var tableIns = table.render({

        id: 'userTable'
        // 容器元素的id属性值
        , elem: '#userList'
        //容器的高度 full-差值
        , height: 'full-125'
        //单元格的最小宽度
        , cellMinWidth: 95
        //访问数据的url (后台数据的接口)
        , url: ctx + '/user/list' //数据接口
        //开启分页
        , page: true
        //默认每页显示的数量
        , limit: 10
        //每页页数的可选项
        , limits: [10, 20, 30, 40, 50]
        //开启头部工具栏区域
        , toolbar: '#toolbarDemo'
        //表头
        , cols: [[
            //field:要求filed属性值与返回的数据中对应的属性字段名一致
            //title:设置列的标题
            //sort:是否允许排序（默认：false）
            //fixed:固定列
            {type: 'checkbox', fixed: 'center'}
            , {field: 'id', title: '编号', sort: true, fixed: 'left'}
            , {field: 'userName', title: '用户名称', align: 'center'}
            , {field: 'trueName', title: '真实姓名', align: 'center'}
            , {field: 'email', title: '用户邮箱', align: 'center'}
            , {field: 'phone', title: '用户号码', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '更新时间', align: 'center'}
            , {title: '操作', templet: '#userListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });



    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {

        /**
         * 表格重载
         * 多条件查询
         */
        tableIns.reload({
            //设置需要传递给后端的参数
            where: { //设定异步数据接口的额外参数，任意设
                // 通过文本框、下拉框的值，设置传递的参数
                userName: $("[name='userName']").val() //用户名称
                , email: $("[name='email']").val() //邮箱
                , phone: $("[name='phone']").val() //手机号
            }
            , page: {
                curr: 1 //重新从第一页开始
            }
        });
    });


    /**
     * 监听头部工具栏
     */
    table.on('toolbar(users)',function (data) {

        if (data.event == "add"){ //添加

            //打开添加或修改用户的对话框
            openAddOrUpdateUserDiaLog();

        }else if (data.event == "del"){ //删除

            //获取被选中的数据的信息
            var checkStatus = table.checkStatus(data.config.id);

            //删除多个用户
            deleteUsers(checkStatus.data);
        }
    });

    /**
     * 删除多条用户记录
     * @param data
     */
    function deleteUsers(data) {
        //判断用户是否选择了要删除的记录
        if (data.length == 0){
            layer.msg("请选择要删除的记录",{icon:5});
            return;
        }
        //询问是否确认删除
        layer.confirm('确定要删除选中的数据吗?', {icon: 3, title: '系统提示'}, function (index) {
            //关闭确认框
            layer.close(index);

            var ids = "ids=";
            //循环选中行记录的数据
            for (var i = 0; i < data.length; i++) {
                if (i < data.length - 1) {
                    ids = ids + data[i].id + "&ids="
                }else {
                    ids = ids + data[i].id
                }
            }
            //发送ajax请求，执行删除多个用户
            $.ajax({
                type:"post",
                url:ctx +"/user/delete",
                data:ids,
                success:function (result) {
                    //判断删除结果
                    if (result.code == 200) {
                        //提示成功
                        layer.msg("删除成功", {icon: 6});
                        //刷新表格
                        tableIns.reload();
                    } else {
                        //提示失败
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

    /**
     * 打开添加或修改用户的对话框
     */
    function openAddOrUpdateUserDiaLog(id) {

        //判断id是否为空，如果为空则为添加操作，否则是修改操作
        var title = "<h3>添加用户</h3>";
        var url = ctx + "/user/toAddOrUpdateUserPage";

        if (id != null && id != ''){
            title = "<h3>更新用户</h3>";
            url += "?id=" + id;  //传递主键查询数据
        }

        //iframe层
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //对应的宽高
            area: ['650px', '400px'],
            //url地址
            content: url,
            //可以最大化与最小化
            maxmin: true
        });
    }

    /**
     * 监听行工具栏
     */
    table.on('tool(users)',function (data) {

        if (data.event == "edit"){ //添加

            //打开添加或修改用户的对话框
            openAddOrUpdateUserDiaLog(data.data.id);

        }else if (data.event == "del"){//删除
            //删除单条用户记录
            deleteUser(data.data.id);

        }
    });

    /**
     * 删除单条用户记录
     */
    function deleteUser(id) {

        //弹出确认框询问用户确认删除
        layer.confirm('确定要删除该记录吗?', {icon: 3, title: "系统提示"}, function (index) {
            //关闭确认框
            layer.close(index);

            //发送ajax请求删除记录
            $.ajax({
                type: "post",
                url: ctx + "/user/delete",
                data: {
                    ids: id
                },
                success: function (result) {
                    //判断删除结果
                    if (result.code == 200) {
                        //提示成功
                        layer.msg("删除成功", {icon: 6});
                        //刷新表格
                        tableIns.reload();
                    } else {
                        //提示失败
                        layer.msg(result.msg, {icon: 5});
                    }
                }
            });
        });
    }

});