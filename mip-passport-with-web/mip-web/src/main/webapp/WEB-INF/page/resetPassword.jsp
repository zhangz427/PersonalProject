<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp" />

<body id="mainframe">
<header>
    <div class="s-topbar">
        <div class="s-return"><a href="javascript:history.go(-1)" class="s-bg2">返回</a></div>
        <h1>重置密码</h1>
    </div>
</header>
<div class="s-screenWrap-2">

    <form id="frm_register" method="post" action="${homeModule}/passport/resetPassword.action">
        <p class="s-inputbox"><span class="s-labelH36">登录密码：</span> <span class="s-inputBorder s-block">
    <input type="password" class="s-input s-inputH36" value="" name="password" id="password"
           placeholder="密码长度必须为8-16个字符">
    </span></p>

        <p class="s-inputbox"><span class="s-labelH36">确认密码：</span> <span class="s-inputBorder s-block">
    <input type="password" class="s-input s-inputH36" value="" name="conirmpw" id="conirmpw"
           placeholder="请再次输入登录密码">
    </span></p>
        <div class="m-error" style="color:red">${forgetPwdResultMobile}</div>

        <br></br><br></br><br></br>

        <p><span class="s-lvBorder s-block"><a href="javascript:void(0)" class="s-lvBtn s-Btn34" id="submitFrom"
                                               onclick="submitFrm()">提交</a></span></p>
        <input type="hidden" name="mobile" id="registerMobile" value="${mobileNumber}" />
    </form>
</div>

<script type="text/javascript" language="javascript">

    function submitFrm() {
        if (!(checkAllPassword() && checkPwdConsistency())) return;
        $('#submitFrom').attr('onclick','return false');;
        $('#frm_register').submit();
    }

    function checkAllPassword() {
        var password = $("#password").val().trim();
//        var myreg1 = /[0-9]{2,}/;
//        var myreg2 = /[a-zA-Z]{2,}/;
//        var myreg3 = password.length;
        if (password == "") {
            $(".m-error").html("密码不能为空");
            $(".m-error").show();
            return false;
        }
//        if (myreg3 > 16 || myreg3 < 8) {
//            $(".m-error").html("密码长度必须为8-16个字符(至少两个数字和两个字母)");
//            $(".m-error").show();
//            return false;
//        }
        return true;
    }

    function checkPwdConsistency() {
        var pass1 = document.getElementById("password");
        var pass2 = document.getElementById("conirmpw");

        if (pass1.value != pass2.value){
            $(".m-error").html("密码与确认密码不一致");
            $(".m-error").show();
            return false;
        }
        return true;
    }

    $('.s-input').focus(function () {
        $(".m-error").hide();
    });

    $(function(){
        var lineLink = location.href;
        var descContent = '全球美食尽在中粮我买网';
        var shareTitle = '中粮我买网';
        loadInfo.init(lineLink,descContent,shareTitle);
    })
</script>
<jsp:include page="bottom.jsp" />
</body>
</html>