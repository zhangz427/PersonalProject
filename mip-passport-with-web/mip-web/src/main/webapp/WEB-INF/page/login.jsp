<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<jsp:include page="head.jsp" />

<body id="mainframe">
<header>
    <div class="s-topbar">
        <div class="s-return"><a href="javascript:history.go(-1)" class="s-bg2">返回</a></div>
        <h1>登录</h1>
    </div>
</header>
<div class="s-screenWrap-2 s-mh250">

    <form  id="frm_login" method="post" action="${homeModule}/passport/login.action">
        <ol class="s-boxCon s-login">
            <li class="s-boxLine">
                <span class="s-bg s-userName"></span>
                <span class="s-block"><input type="text" style="background:none;" class="s-input2 s-inputH46 " value="${username}" name="username" id="username" placeholder="请输入您的邮箱，用户名，手机号"></span>
            </li>
            <li class="s-boxLine">
                <span class="s-bg s-password"></span>
                <span class="s-block"><input type="password" style="background:none;" class="s-input2 s-inputH46" value="${password}" name="password" id="password" placeholder="请输入您的密码"></span>
            </li>
            <li class="s-boxLine">
                <span class="s-bg s-password"></span>

                <jsp:include page="identifyCode.jsp" >
                    <jsp:param name="codeType" value="0" />
                </jsp:include>

            </li>
        </ol>
        <div class="m-error" style="color:red">${loginResult}</div>
        <p class="s-ptb8"><span class="s-lvBorder s-block"><a class="s-lvBtn s-Btn34" href="javascript:void(-1)" onclick="submitFrm()">登录</a></span></p>

        <input type="hidden" name="mp" value="" />

    </form>
    <div class="s-lr s-p">
        <div class="sl"><a href="${homeModule}/passport/toNoPassLogin.action"  id="id-forgetPwd" class="s-color7 s-de-un">短信快捷登录</a></div>
        <div class="sr"><a href="${homeModule}/passport/toForgetPwd.action"  id="id-reg" class="s-color7 s-de-un">忘记密码</a></div>
    </div>
    <p class="s-301"><span class="s-block"><a href="${homeModule}/union/unionLoginList.action" id="id-unionLogin" class="s-color7 s-f16">用合作网站账号登录&gt;</a></span></p>


</div>
<script type="text/javascript" language="javascript">



    function submitFrm(){

        if(!(checkUsername() && checkPassword())) return;
        if(!handleFormDataAndCheck("frm_login","m-error")){
            return ;
        }
        $('#frm_login').submit();
    }


    function checkUsername(){
        var username = $("#username").val().trim();
        if(username == null || username == ""|| username==undefined){
            $(".m-error").html("\u8d26\u6237\u540d\u4e0d\u80fd\u4e3a\u7a7a");
            $(".m-error").show();
            return false;
        }
        return true;
    }

    function checkPassword(){
        var password = $("#password").val().trim();
        if(password == null || password == ""|| password==undefined){
            $(".m-error").html("\u5bc6\u7801\u4e0d\u80fd\u4e3a\u7a7a");
            $(".m-error").show();
            return false;
        }
        return true;
    }
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