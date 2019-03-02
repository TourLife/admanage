$(function(){
    var flag = false;
    layui.use("layer", function () {
        var layer = layui.layer;  //layer初始化
    });
    $(".do_speed").click(function(){
       $(this).removeAttr("readonly");
        if($("#startBtn").text() == "暂停") {
            countDown();
        }
    });
    //更改消耗速率
    $(".do_speed").blur(function(){
        var reg = /^([0-9])*(\.\d{1,2})*$/;
        if($(".do_speed").val() <= 0 || !reg.test($(this).val())){
            $(".do_speed").val(1.0);
        }else{
            $(".do_speed").val($(this).val());
        }
        $(this).attr("readonly",true);
    });
    //开始or停止
    $("#startBtn").click(function(){
        if($(".blananceMoney").text() > 0){
            updateAcount(true);
        }else{
            layer.msg("余额不足，请先充值！",{icon:2,time:1000})
        }
    });
    if($("#isStart").val() == 1){
        updateAcount(true);
    }
});
function updateAcount(cammnd){
    var spendSpeed = $(".do_speed").val();
    var blananceMoney = $(".blananceMoney").text();
    var useMoney = $(".useMoney").text();
    var isstart = $("#isStart").val();
    if($("#startBtn").text() == "开始" && cammnd == true){
        isstart = 1;
    }
    if($("#startBtn").text() == "暂停"){
        isstart = 0;
    }
    if(cammnd == true){
        blananceMoney = $("#blanance").val();
    }
    //临时处理jsessionid问题
    var hasJs = path.split(";");
    if(hasJs){
        path = hasJs[0];
    }
    $.ajax({
        url:path+"userAcount/updateUserAcount",
        type:"post",
        data:{
            "spendspeed":spendSpeed,
            "usemoney":useMoney,
            "blanance":blananceMoney,
            "isStart":isstart,
            "isUpdate":cammnd,
            "userId":$("#selectUser").val(),
            "userAcountId":$("#userAcountId").val()
        },
        dataType:"json",
        success:function(data){
            if(cammnd == true){
                countDown();
            }else{
                $(".useMoney").text(0);
                var res = data.Data;
                $("#userAcountId").val(res.id);
            }
        }
    });
}
//模拟投资实时消费金额
function countDown(){
    var blananceMoney = $(".blananceMoney").text();
    var speed = $(".do_speed").val();
    var timer = null;
    if($("#startBtn").text() == "开始"){
        $("#startBtn").text("暂停");
    }else{
        $("#startBtn").text("开始");
    }
    timer=setInterval(function(){
        if($(".blananceMoney").text() <= 0|| $("#startBtn").text() == "开始"){
            clearInterval(timer);
            return ;
        }
        var blananceMoney = accSub($(".blananceMoney").text() , speed);
        var useMoney = accAdd($(".useMoney").text() , speed);
        if(blananceMoney <= 0){
            blananceMoney = 0;
            useMoney = $("#blanance").val();
        }
        $(".blananceMoney").text((blananceMoney));
        $(".useMoney").text((useMoney));
        if(blananceMoney <= 0) {
            updateAcount(true);
        }

    },1000);
}

function submitForm(){

    var m = $('#imoney').val();
    if(isNumber(m)){
        if($("#startBtn").text() == "暂停") {
            countDown();

        }
        if (!m) {
            layer.msg("请填写充值金额",{icon:2,time:1000});
        }else if (m < 10) {
            layer.msg("充值金额不能小于10元",{icon:2,time:1000});
        }else if (m > 100000000) {
            layer.msg("充值金额不能大于100000000元",{icon:2,time:1000});
        }else{
            $("#blanance").val(m);
            $(".blananceMoney").text($("#imoney").val());
            updateAcount(false);
        }
        //关闭弹窗
        box._hide();
    }else{
        layer.msg("请输入正确的数字",{icon:2,time:1500});
    }
}

function selectUser() {
    window.location.href = "index?userId="+$("#selectUser").val();
}
function accAdd(arg1, arg2) {
    var r1, r2, m, n;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    m = Math.pow(1, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
    n = (r1 >= r2) ? r1 : r2;
    return ((arg1 * m + arg2 * m) / m).toFixed(n);
}

function accSub(arg1, arg2) {
    var r1, r2, m, n;
    try {
        r1 = arg1.toString().split(".")[1].length;
    }
    catch (e) {
        r1 = 0;
    }
    try {
        r2 = arg2.toString().split(".")[1].length;
    }
    catch (e) {
        r2 = 0;
    }
    m = Math.pow(1, Math.max(r1, r2)); //last modify by deeka //动态控制精度长度
    n = (r1 >= r2) ? r1 : r2;
    return ((arg1 * m - arg2 * m) / m).toFixed(2);
}

function isNumber(val){

    var regPos = /^\d+(\.\d+)?$/; //非负浮点数
    var regNeg = /^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$/; //负浮点数
    if(regPos.test(val) || regNeg.test(val)){
        return true;
    }else{
        return false;
    }

}
