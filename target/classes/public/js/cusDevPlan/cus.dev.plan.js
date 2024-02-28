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
        //访问数据的url (后台数据的接口) 设置flag参数表示查询的是客户开发数据
        , url: ctx + '/sale_chance/list?flag=1' //数据接口
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
            , {field: 'createDate', title: '创建时间', align: 'center'}
            , {field: 'updateDate', title: '更新时间', align: 'center'}
            , {
                field: 'devResult', title: '开发状态', align: 'center', templet: function (data) {
                    //调用函数返回格式化结果
                    return formatDevResult(data.devResult);
                }
            }
            , {title: '操作', templet: '#op', fixed: 'right', align: 'center', minWidth: 150}
        ]]
    });


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
                , devResult: $("#devResult").val() //开发状态
            }
            , page: {
                curr: 1 //重新从第一页开始
            }
        });
    });


    /**
     * 行工具栏的监听
     */
    table.on('tool(saleChances)', function (data) {
        //判断类型
        if (data.event == "dev") {  //开发

            //打开计划项开发与详情页面
            openCusDevPlanDiaLog('计划项数据开发', data.data.id);

        } else if (data.event == "info") {  //详情
            //打开计划项开发与详情页面
            openCusDevPlanDiaLog('计划项数据维护', data.data.id);
        }
    });

    /**
     * 打开计划项开发或详情页面
     * @param title
     * @param id
     */
    function openCusDevPlanDiaLog(title, id) {

        //iframe层
        layui.layer.open({
            //弹出层类型
            type: 2,
            //标题
            title: title,
            //对应的宽高
            area: ['750px', '550px'],
            //url地址
            content: ctx + "/cus_dev_plan/toCusDevPlanPage?id=" + id,
            //可以最大化与最小化
            maxmin: true
        });

    }

});
