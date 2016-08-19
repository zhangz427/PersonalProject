<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp" />

<body id="mainframe">
<header>
    <div class="s-topbar">
        <div class="s-return"><a class="s-bg2" href="javascript:history.go(-1)">返回</a></div>
        <h1>忘记密码</h1>
    </div>
</header>
<div class="s-screenWrap-2">
    <c:if test="${findByMobile eq 'true'}">
        <div class="s-tab1"><a id="id-findByMobile" class="ack" href="#">手机找回</a><a id="id-findByEmail"href="#">邮箱找回</a></div>
    </c:if>
    <c:if test="${findByMobile ne 'true'}">
        <div class="s-tab1"><a id="id-findByMobile" href="#">手机找回</a><a class="ack" id="id-findByEmail"href="#">邮箱找回</a></div>
    </c:if>
</div>
<!-- 通过手机号找回 -->
<div class="s-screenWrap-2 s-mh250" id="id-findByMobileSection" <c:if test="${findByMobile ne 'true'}">style="display: none" </c:if>>

    <form id="frm_forgetPwdMobile" method="post" action="${homeModule}/passport/forgetPwdMobile.action">
        <div>
            <p class="s-inputbox">
                <span class="s-labelH36">手机号：</span>
                <span class="s-inputBorder s-block">
                    <input type="tel" class="s-input s-inputH36" value="${mobileNumber}" name="mobileNumber" id="rMobile" placeholder="请输入手机号" style="ime-mode:disabled" onblur="checkMobile()">
                </span>
            </p>
            <jsp:include page="identifyCode.jsp" >
                <jsp:param name="codeType" value="2" />
            </jsp:include>
            <p class="s-inputbox s-204">
                <span class="s-labelH36">
                    手机验证码：
                </span>
                <span class="s-inputBorder s-block">
                    <input type="tel" class="s-input s-inputH36" value="${authCode}" name="authCode" id="authCode" placeholder="请输入手机验证码" onblur="checkAuthCode()">
                </span>
                <span class="s-jvBorder s-block s-r">
                    <a class="s-jvBtn s-Btn34" href="javascript:void(0);" id="validBtn" onclick="sValid()">获取验证码</a>
                </span>
                <span class="s-baiBorder s-block s-r" id="timerCode" style="display:none">
                    <a href="javascript:void(0)" class="s-baiBtn s-Btn34 s-color5" id="rValidBtn">有效(60s)</a>
                </span>
            </p>
        </div>
        <div class="m-error-mobile" style="color:red">${forgetPwdResultMobile}</div>
        <p><span class="s-lvBorder s-block"><a onclick="submitFrmMobile()" class="s-lvBtn s-Btn34">提交</a></span></p>
    </form>
</div>
<!-- 通过邮箱找回 -->
<div class="s-screenWrap-2 s-mh250" id="id-findByEmailSection" <c:if test="${findByMobile eq 'true'}">style="display: none" </c:if> >

    <form id="frm_forgetPwd" method="post" action="${homeModule}/passport/forgetPwdEmail.action">
        <p class="s-inputbox s-h35">
            <span class="s-labelH36">邮箱地址：</span>
            <span class="s-inputBorder s-block"><input type="text" class="s-input s-inputH36" name="email" id="id-email"
                                                       placeholder="请输入您注册时填写的地址" value="${email}"></span>
        </p>
        <jsp:include page="identifyCode.jsp" >
            <jsp:param name="codeType" value="3" />
        </jsp:include>
        <div class="m-error" style="color:red">${forgetPwdResultMail}</div>
        <p><span class="s-lvBorder s-block"><a onclick="submitFrm()" class="s-lvBtn s-Btn34">提交</a></span></p>
    </form>
    <p class="s-title">温馨提示：</p>
    <p class="s-pb5 s-color4">请正确填写您的注册的电子邮箱地址，系统将把您的密码发送到您的注册邮箱，如果该邮箱地址错误，或者您已经忘记注册时的邮箱地址，我们将无法为您找回密码，建议您重新注册一个帐号。</p>

</div>
<script type="text/javascript" language="javascript">
    $(function(){
        $("#id-findByMobile").click(function () {
            $(this).addClass("ack");
            $("#id-findByEmail").removeClass("ack");
            $("#id-findByMobileSection").show();
            $("#id-findByEmailSection").hide();
        });
        $("#id-findByEmail").click(function () {
            $(this).addClass("ack");
            $("#id-findByMobile").removeClass("ack");
            $("#id-findByEmailSection").show();
            $("#id-findByMobileSection").hide();
        });
    })
    //邮箱找回表单提交
    function submitFrm() {
        if (!checkEmail()) return;
        if(!handleFormDataAndCheck("frm_forgetPwd","m-error")){
            return ;
        }
        $("#frm_forgetPwd").submit();
    }
    //手机找回表单提交
    function submitFrmMobile() {

        if(!(checkMobile() && checkAuthCode())) {
            return;
        }else{
            $('#frm_forgetPwdMobile').submit();
        }

    }
    function checkEmail() {
        var username = $("#id-email").val().trim();
        var myreg = /^([\w-]+[_|\_|\.]?)*[\w-]+@([\w-]+[_|\_|\.]?)*[\w-]+\.[a-zA-Z]{2,3}$/;
        if (username == "") {
            $(".m-error").html("邮箱不能为空");
            $(".m-error").show();
            return false;
        }
        if (!myreg.test(username)) {
            $(".m-error").html("请输入有效的邮箱地址");
            $(".m-error").show();
            return false;
        }
        return true;
    }
    $('.s-input').focus(function(){
        $(".m-error").hide();
        $(".m-error-mobile").hide();
    });

    <c:if test="${successResult eq 'success'}">
    $(function(){
        alert("您的申请已成功提交，系统已经给您的邮箱发送了通知信。");
    });
    </c:if>

    function checkAuthCode(){
        var authCode = $('#authCode').val();
        if(!/\d{6}/.exec(authCode) || authCode.length != 6 || authCode == null || authCode.trim() == ""){
            $(".m-error-mobile").html("请输入6位数字验证码");
            $(".m-error-mobile").show();
            return false;
        }
        return true;
    }
    var distantTime = 60;
    var timer = function () {
        distantTime --;
        $("#validBtn").hide();
        $("#rValidBtn").html("有效("+ distantTime + ")秒");
        $("#timerCode").show();
        if (distantTime > 0) {
            setTimeout("timer()", 1000)
        } else {
            distantTime = 60;
            $("#timerCode").hide();
            $('#validBtn').html("重新发送");
            $("#validBtn").show();
        }
    };

    function checkMobile(){
        var mobile = $('#rMobile').val();
        if (!/^1[345678]\d{9}$/.exec(mobile) || mobile == null || mobile.trim() == "") {
            $(".m-error-mobile").html("请输入11位有效手机号");
            $(".m-error-mobile").show();
            return false;
        }
        return true;
    }
    function sValid(){
        if(!(checkMobile())) return;
        var mobile = $('#rMobile').val();
        var postJson = {
            "mobileNumber":mobile
        };
        postJson = handlePostJsonAndCheck(postJson,"m-error-mobile");
        if(postJson == null ) return ; //校验方法在引入文件中
        jQuery.ajax({
            type:'POST',
            url:'${homeModule}/passport/sendFindPwdCode.action',
            async:false,
            cache:false,
            data:postJson,
            dataType:'text',
            success:function (data) {
                if (data == 'success') {
                    timer();
                } else {
                    reFlushCode();
                    $(".m-error-mobile").html(data);
                    $(".m-error-mobile").show();
                }
            },
            error:function (xhr, type) {
            }
        })
    }
</script>
<jsp:include page="bottom.jsp" />
</body>
</html>