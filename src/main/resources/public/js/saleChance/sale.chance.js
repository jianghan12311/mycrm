layui.use(['table', 'layer'], function () {
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var tableIns = table.render({

        id: 'saleChanceTable'
        // 容器元素的id属性值
        , elem: '#saleChanceList'
        //容器的高度 full-差值
        , height: 'full-125'
        //单元格的最小宽度
        , cellMinWidth: 95
        //访问数据的url (后台数据的接口)
        , url: ctx + '/sale_chance/list' //数据接口
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
            , {field: 'chanceSource', title: '机会来源', align: 'center'}
            , {field: 'customerName', title: '客户名称', align: 'center'}
            , {field: 'cgjl', title: '成功几率', align: 'center'}
            , {field: 'overview', title: '概要', align: 'center'}
            , {field: 'linkMan', title: '联系人', align: 'center'}
            , {field: 'linkPhone', title: '联系号码', align: 'center'}
            , {field: 'description', title: '描述', align: 'center'}
            , {field: 'createMan', title: '创建人', align: 'center'}
            , {field: 'uname', title: '分配人', align: 'center'}
            , {field: 'assignTime', title: '分配时间', align: 'center'}
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '更新时间', align: 'center'}
            , {field: 'state', title: '分配状态', align: 'center', templet: function (data) {
                    //调用函数返回格式化结果
                    return formatState(data.state);
                }
            }
            , {field: 'devResult', title: '开发状态', align: 'center', templet: function (data) {
                    //调用函数返回格式化结果
                    return formatDevResult(data.devResult);
                }
            }
            , {title: '操作', templet: '#saleChanceListBar', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });


    /**
     * 格式化分配状态值
     * 0 = 未分配   1 = 已分配   其他未知
     * @param state
     */
    function formatState(state) {
        if (state == 0) {
            return "<div style='color: cornflowerblue'>未分配</div>";
        } else if (state == 1) {
            return "<div style='color: green'>已分配</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

    /**
     * 格式化开发状态
     * 0 = 未开发   1 = 开发中  2 = 开发成功  3 = 开发失败  其它未知
     * @param devResult
     */
    function formatDevResult(devResult) {
        if (devResult == 0) {
            return "<div style='color: cornflowerblue'>未开发</div>";
        } else if (devResult == 1) {
            return "<div style='color: orange'>开发中</div>";
        } else if (devResult == 2) {
            return "<div style='color: green'>开发成功</div>";
        } else if (devResult == 3) {
            return "<div style='color: cyan'>开发失败</div>";
        } else {
            return "<div style='color: red'>未知</div>";
        }
    }

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
                customerName: $("[name='customerName']").val() //客户名称
                , createMan: $("[name='createMan']").val() //创建人
                , state: $("#state").val() //状态
            }
            , page: {
                curr: 1 //重新从第一页开始
            }
        });
    });

    /**
     * 监听表格的头部工具栏事件
     * 格式：
     * table.on('toolbar(数据表格的lay-filter)',function (data) {
     *
     *  })
     */
    table.on('toolbar(saleChances)', function (data) {
        //判断对应的事件类型
        if (data.event == "add") {
            //添加操作
            openSaleChanceDialog();
        } else if (data.event == "del") {
            //删除操作
            deleteSaleChance(data);
        }
    });

    /**
     * 删除多条营销机会
     * @param data
     */
    function deleteSaleChance(data) {
        //获取数据表格选中行的数据  checkStatus("数据表格的id属性");
        var checkStatus = table.checkStatus("saleChanceTable");

        //获取所有被选中的记录对应的数据
        var saleChanceData = checkStatus.data;

        //判断用户是否选择了记录
        if (saleChanceData.length < 1) {
            layer.msg("请至少选择一条记录删除", {icon: 5});
            return;
        }

        //询问是否确认删除
        layer.confirm('确定要删除选中的数据吗?', {icon: 3, title: '系统提示'}, function (index) {
            //关闭确认框
            layer.close(index);

            var ids = "ids=";
            //循环选中行记录的数据
            for (var i = 0; i < saleChanceData.length; i++) {
                if (i < saleChanceData.length - 1) {
                    ids = ids + saleChanceData[i].id + "&ids="
                }else {
                    ids = ids + saleChanceData[i].id
                }
            }
            console.log(ids)
            //发送ajax请求，执行删除多条营销机会
            $.ajax({
                type:"post",
                url:ctx +"/sale_chance/delete",
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
     * 打开添加营销机会数据的窗口
     * 如果营销机会id为空，则为添加操作
     * 如果营销机会id不为空，则更新操作
     */
    function openSaleChanceDialog(saleChanceId) {

        var title = "<h3>添加营销机会</h3>";
        var url = ctx + "/sale_chance/toSaleChancePage";

        //判断营销机会id是否为空
        if (saleChanceId != null && saleChanceId != '') {
            //更新操作
            title = "<h3>更新营销机会</h3>";
            //请求地址传递营销机会id
            url += '?saleChanceId=' + saleChanceId;
        }
        //iframe层
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //对应的宽高
            area: ['500px', '620px'],
            //url地址
            content: url,
            //可以最大化与最小化
            maxmin: true
        });
    }

    /**
     * 行工具栏监听事件
     *
     *  table.on('tool(数据表格中的lay-filter属性值)',function (data) {

     *  });
     */
    table.on('tool(saleChances)', function (data) {
        //判断类型
        if (data.event == "edit") {  //编辑操作
            //得到营销机会的id
            var saleChanceId = data.data.id;
            //打开修改营销机会数据的窗口
            openSaleChanceDialog(saleChanceId);
        } else if (data.event == "del") { //删除操作
            //弹出确认框询问用户确认删除
            layer.confirm('确定要删除该记录吗?', {icon: 3, title: "系统提示"}, function (index) {
                //关闭确认框
                layer.close(index);

                //发送ajax请求删除记录
                $.ajax({
                    type: "post",
                    url: ctx + "/sale_chance/delete",
                    data: {
                        ids: data.data.id
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

});










