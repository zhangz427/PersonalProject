<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<jsp:include page="head.jsp" />

<body id="mainframe">
<header>
    <div class="s-topbar">
        <div class="s-return"><a href="javascript:history.go(-1)" class="s-bg2">返回</a></div>
        <h1>短信快捷登录</h1>
    </div>
</header>
<div class="s-screenWrap-2 s-mh250">

    <form  id="frm_login" method="post" action="${homeModule}/passport/noPassLogin.action">
        <ol class="s-boxCon s-login s-loginfree">
            <li class="s-boxLine">
                <span class="s-bg s-userName s-phone"></span>
                <span class="s-block"><input type="tel" style="background:none;" class="s-input2 s-inputH46 clearInput" value="${mobileNumber}" name="mobileNumber" id="rMobile" placeholder="请输入您的手机号"></span>
                <a class="fr-clear clearA" href="javascript:;"><img src="${homeModule}/images/sso/icon_06.png"/> </a>
            </li>

            <!--图片校验-->
            <jsp:include page="identifyCode.jsp" >
                <jsp:param name="codeType" value="11" />
            </jsp:include>
            <!--图片校验结束-->


            <li class="s-boxLine">
                <span class="s-bg s-password s-phoneinfo"></span>
                <span class="s-block"><input type="tel" style="background:none;" class="s-input2 s-inputH46 clearInput" value="${authCode}" name="authCode" id="authCode" placeholder="请输入短信验证码" onblur="checkAuthCode();" /></span>
                <a class="fr-clear f-clear-info clearA" href="javascript:;"><img src="${homeModule}/images/sso/icon_06.png"/> </a>
            <span class="s-i-code s-i-info"><a id="validBtn" class="fr-info-link" href="javascript:;" onclick="sValid()">获取短信验证码</a>
                                                 </span>
            </li>
        </ol>
        <div class="m-error" style="color:red">${noPassLoginResult}</div>
        <p class="f-tip">温馨提示</p>
        <p class="f-rule">未注册我买网账号的手机号，登录时将自动注册且代表已同意<a href="${mobileHomeModule}/help/serviceRule.shtml">《用户服务协议》</a></p>
        <p class="s-ptb8"><span class="s-lvBorder s-block"><a class="s-lvBtn s-Btn34" href="javascript:void(-1)" onclick="submitFrm()">登录</a></span></p>

        <input type="hidden" name="mp" value="" />

    </form>
    <p class="s-301"><span class="s-block"><a href="${homeModule}/union/unionLoginList.action" id="id-unionLogin" class="s-color7 s-f16">用合作网站账号登录&gt;</a></span></p>
</div>
<script type="text/javascript" language="javascript">
    //非空验证
    $(document).ready(function(){
        $(".clearA").click(function(){
            $(this).parents("li").find("input.clearInput").val("");
            $(this).parents("li").find("input.clearInput").focus();
        });
    });

    //发送手机验证码
    function sValid(){
        if(!(checkMobile())) return;
        var mobile = $('#rMobile').val();
        var postJson = {
            "mobileNumber":mobile
        };
        postJson = handlePostJsonAndCheck(postJson,"m-error");
        if(postJson == null ) return ; //校验方法在引入文件中
        jQuery.ajax({
            type:'POST',
            url:'${homeModule}/passport/noPassLoginSendCode.action',
            async:false,
            cache:false,
            data:postJson,
            dataType:'text',
            success:function (data) {
                if (data == 'success') {
                    timer();
                }else {
                    reFlushCode();
                    $(".m-error").html(data);
                    $(".m-error").show();
                }
            },
            error:function (xhr, type) {
            }
        })
    }
    //登录
    function submitFrm() {
        if(!(checkMobile() && checkAuthCode())) {
            return;
        }else{
            $('#frm_login').submit();
        }
    }
    //校验验证码
    function checkAuthCode(){
        var authCode = $('#authCode').val();
        if(!/\d{6}/.exec(authCode) || authCode.length != 6 || authCode == null || authCode.trim() == ""){
            $(".m-error").html("请输入6位数字验证码");
            $(".m-error").show();
            return false;
        }
        return true;
    }
    //检查手机
    function checkMobile(){
        var mobile = $('#rMobile').val();
        if (!/^1[345678]\d{9}$/.exec(mobile) || mobile == null || mobile.trim() == "") {
            $(".m-error").html("请输入11位有效手机号");
            $(".m-error").show();
            return false;
        }
        return true;
    }
    var distantTime = 60;
    var timer = function () {
        distantTime --;
        var validBtn =  $("#validBtn");
        validBtn.attr("class","fr-info-again");
        validBtn.attr("onclick","");
        validBtn.html("重新获取("+ distantTime + "s)");
        if (distantTime > 0) {
            setTimeout("timer()", 1000)
        } else {
            distantTime = 60;
            validBtn.attr("class","fr-info-link")
            validBtn.html("重新发送");
            validBtn.attr("onclick","sValid()");
        }
    };


    //清空错误提示
    $('.s-input2').focus(function(){
        $(".m-error").hide();
    });

    $(function(){
        var lineLink = location.href;
        var shareTitle = '中粮我买网';
        var descContent = '全球美食尽在中粮我买网';
        loadInfo.init(lineLink,descContent,shareTitle);

    });
</script>
<jsp:include page="bottom.jsp" />
</body>
</html>