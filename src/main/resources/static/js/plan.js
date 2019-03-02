$(function(){
    layui.use("layer", function () {
        var layer = layui.layer;  //layer初始化
    });
    //分页
    $('.page').pagination({
        totalData:$("#totalPage").val(),
        current:$("#currentpage").val(),
        mode: 'fixed',
        callback:function(){
            //针对费超管用户获取不到当前
            var uId = "";
            if(typeof($("#selectUser").val()) != "undefined"){
                uId = $("#selectUser").val();
            }else{
                uId = $("#currentUserId").val();
            }
            window.location.href = "index?adtype="+$("#choose_type").val()+"&userId="+uId+"&currentpage="+$(".page").children(".active").text();
        }
    });
    //$("#plancreatename").bind("click",getUser());
});
// var plan = function() {
    function addOredit(action, pid) {
        $("#planlist").hide();
        if (action == "add") {
            $("#planedit").show();
            $(".planDeal").show();
            $(".action").text("新建计划");
        } else {
            $.ajax({
                url: "getPlan",
                type: "post",
                data: {planId: pid},
                dataType: "json",
                success: function (data) {
                    console.log(data.Data);
                    if (data.Data != null) {
                        $("#planedit").show();
                        $(".planDeal").show();
                        $("#plancreatename").css("border","0px");
                        $("#planid").val(data.Data.planId);
                        $("#plancreateid").val(data.Data.planCreateId);
                        $("#plancreatename").val(data.Data.planCreateName);
                        $("#plancreatename").unbind();
                        $("#plantitle").val(data.Data.planTitle);
                        $("#planbtype").val(data.Data.planBtype);
                        $("#planstype").val(data.Data.planStype);
                        $("#planprice").val(data.Data.planPrice);
                        $("#planlogo").parent("p").show();
                        $("#planlogo").text(data.Data.planLogo);
                        $("#planmaxprice").val(data.Data.planMaxprice);
                    }
                }
            });
        }
    }

    function delPlan(planId) {
        layer.confirm("确定删除该计划吗？",{
            btn: ['确定','取消'] //按钮
        }, function() {
            window.location.href = "delete?planid=" + planId;
        });
    }

    function queryPlanByTypeOrUser() {
        var userId = $("#selectUser").val();
        if(typeof(userId) == "undefined"){
            userId = $("#currentUserId").val();
        }
        window.location.href = "index?planstype="+$("#choose_type").val()+"&userId="+userId;
    }
    function getUser(){
        $("#user").show();
    }

    function selectUser(obj){
        $("#user").hide();
        $("#plancreateid").val($(obj).attr("code"));
        $("#plancreatename").val($(obj).children().text());
    }
    function paramValue(){
        if($("#plantitle").val() == null || $("#plantitle").val() == ""){
            layer.msg("计划名称不能为空！",{icon:2,time:1500});
            return false;
        }
        /*if($("#plantitle").val().length > 20){
            alert("计划名称长度不能超过20个字！");
            return false;
        }*/

        var flag = checkFloat();
        if(flag == false){
            return false;
        }

        if($("#planprice").val() < 0.1){
            layer.msg("广告金额不得低于0.1元每条！",{icon:2,time:1500});
            return false;
        }
        if($("#planmaxprice").val()> 100000000){
            layer.msg("广告金额不得高于100000000元每天！",{icon:2,time:1500});
            return false;
        }
        return true;
    }

    //验证金额的格式
    function checkFloat() {
        var patrn = /^\d+(\.\d+)?$/;
        var result = true;
        if (!patrn.exec($("#planprice").val())) {
            alert("请输入正确的金额格式（小数或者整数）！");
            result = false;
        }
        return result;
    }
// }