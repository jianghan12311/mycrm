layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);



    form.on('submit(login)',function (data) {

        console.log(data.field)

        $.ajax({
            type:"post",
            url:ctx + "/user/login",
            data:{
                userName:data.field.username,
                userPwd:data.field.password
            },
            success:function (result) { //result是回调函数，用来接收后端返回的数据
                console.log(result);

                //判断是否登录成功，如果code=200则表示登录成功，否则失败
                if(result.code == 200){
                    //登录成功
                    //设置用户是登录状态
                    layer.msg("登录成功",function () {

                        //判断用户是否选择记住密码
                        if($("#rememberMe").prop("checked")){
                            //选中，设置cookie7天生效
                            //将用户信息设置到cookie中
                            $.cookie("userIdStr",result.result.userIdStr,{expires:7});
                            $.cookie("userName",result.result.userName,{expires:7});
                            $.cookie("trueName",result.result.trueName,{expires:7});
                        }else {
                            //将用户信息设置到cookie中
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }
                        //登录成功后跳转到首页
                        window.location.href = ctx + "/main";
                    })

                }else {
                    //登录失败
                    layer.msg(result.msg, {icon:5})
                }
            }
        });

        return false;

    });

    
});