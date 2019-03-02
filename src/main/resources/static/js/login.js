/**
 * Created by baoxu2 on 2018/10/10.
 * Author xy
 */
$(function () {
    layui.use("layer", function () {
        var layer = layui.layer;  //layer初始化
    });
    $(document).keyup(function (e) {//捕获文档对象的按键弹起事件
        if (e.keyCode == 13) {//按键信息对象以参数的形式传递进来了
            login();
        }
    });
});
var login = function () {
    var loginname = $("#username").val();
    var psw = $("#password").val();
    if (loginname == null || loginname == "") {
        return layer.msg("请输入账户名!",{icon: 2,time:2000});
    }
    if (psw == null  || psw == "") {
        return layer.msg("请输入密码!",{icon: 2,time:2000});
    }
    //临时处理jsessionid问题
    var hasJs = path.split(";");
    if(hasJs){
        path = hasJs[0];
    }
    $.ajax({
        url: path+"user/login",
        type: 'post',
        dataType: "json",
        data: {
            loginName: loginname,
            userpassword: psw
        },
        success: function (data) {
            if (data["Data"] != null) {
                window.location.href = path+"user/index";
            } else {
                layer.msg("账号或者密码错误!", {icon: 2,time:2000});
            }
        }
    });
}