layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;


    /**
     * 监听表单submit事件
     *  form.on('submit(按钮元素lay-filter属性值)',function (data) {

     * });
     */
    form.on('submit(addOrUpdateSaleChance)', function (data) {
        //提交数据的加载层
        var index = layer.msg("数据加载中，请稍后...", {
            icon: 16, //图标
            time: false, //不关闭
            shade: 0.8 // 设置遮罩的透明度
        });

        //发送ajax请求
        var url = ctx + "/sale_chance/add"; //添加操作

        // 通过营销机会的id来判断当前需要执行添加操作还是修改操作
        //如果营销机会id为空则表示添加操作，id不为空则表示执行更新操作
        //通过获取隐藏域中的id
        var saleChanceId = $("[name='id']").val();
        //判断id是否为空
        if (saleChanceId != null && saleChanceId != '') {
            //更新操作
            url = ctx + "/sale_chance/update";
        }

        $.post(url, data.field, function (result) {
            //判断操作是否执行成功  200=成功
            if (result.code == 200) {
                //成功
                //提示成功
                layer.msg("操作成功", {icon: 6})
                //关闭加载层
                layer.close(index);
                //关闭弹出层
                layer.closeAll("iframe");
                //刷新父窗口,重新加载数据
                parent.location.reload();
            } else {
                //失败
                layer.msg(result.msg, {icon: 5})
            }
        });

        //阻止表单提交
        return false;
    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        //当你在iframe页面关闭自身时
        var index = parent.layer.getFrameIndex(window.name);//先得到当前iframe层的索引
        parent.layer.close(index);//再执行关闭
    });

    /**
     * 加载指派人下拉框
     */
    $.ajax({
        type: "get",
        url: ctx + "/user/queryAllSales",
        data: {},
        success: function (data) {
            //判断返回的数据是否为空
            if (data != null) {

                //获取隐藏域中设置的指派人id
                var assignManId = $("#assignManId").val();
                //遍历返回的数据
                for (var i = 0; i < data.length; i++) {
                    var opt = "";
                    //如果循环得到的id与隐藏域中id相等则表示选中
                    if (assignManId == data[i].id){
                        //设置下拉选项选中
                        opt = "<option value='" + data[i].id + "' selected>"+data[i].uname+"</option>";
                    }else {
                        //设置下拉选项
                        opt = "<option value='" + data[i].id + "'>"+data[i].uname+"</option>";
                    }
                    //将下拉项设置到下拉框中
                    $("#assignMan").append(opt);
                }
            }
            //渲染下拉框元素
            layui.form.render("select");
        }
    });


});