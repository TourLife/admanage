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
            //针对非超管用户获取不到当前
            var planId = "";
            if(typeof($("#selectPlan").val()) != "undefined"){
                planId = $("#selectPlan").val();
            }
            window.location.href = "index?adtype="+$("#choose_type").val()+"&planId="+planId+"&currentpage="+$(".page").children(".active").text();
        }
    });
});

function queryPlanByTypeOrPlan() {
    window.location.href = "index?adtype="+$("#choose_type").val()+"&planId="+$("#selectPlan").val();
}

function addOredit(action, adid) {
    $("#addlist").hide();
    if (action == "add") {
        $("#addedit").show();
        $(".action").text("新建广告");
    } else {
        $.ajax({
            url: "getAd",
            type: "post",
            data: {adId: adid},
            dataType: "json",
            success: function (data) {
                console.log(data.Data);
                if (data.Data != null) {
                    $("#addedit").show();
                    $("#adid").val(data.Data.adId);
                    $("#pid").val(data.Data.planId);
                    $("#adtitle").val(data.Data.adTitle);
                    $("#plantitle").val(data.Data.planTitle);
                    $("#adtype").val(data.Data.adType);
                    $("#adlogo").parent("p").show();
                    $("#adlogo").text(data.Data.adLogo);
                    $("#status").val(data.Data.status);
                }
            }
        });
    }
}

function delAd(adId){
    layer.confirm("确定删除该广告吗？",{
        btn: ['确定','取消'] //按钮
    }, function(){
        window.location.href = "delete?adId="+adId;
    });
}

function getPlan(){
    $("#plans").show();
}

function selectPlan(obj){
    $("#plans").hide();
    $("#pid").val($(obj).attr("code"));
    $("#plantitle").val($(obj).children().text());
}

function paramValue(){
    if($("#adtitle").val() == null || $("#adtitle").val() == ""){
        alert("广告名称不能为空！");
        return false;
    }
    if($("#plantitle").val() == null || $("#plantitle").val() == ""){
        alert("所属计划不能为空！");
        return false;
    }
    if($("#adtype").val() == "请选择" || $("#adtype").val() == ""){
        alert("请选择一个广告类型！");
        return false;
    }
    if($("#logo").val() == "" || $("#logo").val() == null){
        if($("#adlogo").text() == "" || $("#adlogo").text() == null) {
            alert("广告logo不能为空！");
            return false;
        }
    }
    return true;
}