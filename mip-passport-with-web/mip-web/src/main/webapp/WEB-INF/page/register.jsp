<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<jsp:include page="head.jsp" />

<body id="mainframe">
<header>
    <div class="s-topbar">
        <div class="s-return"><a href="javascript:history.go(-1)" class="s-bg2">返回</a></div>
        <h1>注册</h1>
    </div>
</header>
<div class="s-screenWrap-2 s-mh250">

    <form id="frm_register" method="post" action="${homeModule}/passport/fastRegister.action">
        <p class="s-inputbox"><span class="s-labelH36">手机号：</span> <span class="s-inputBorder s-block">
    <input type="tel" class="s-input s-inputH36" value="${mobileNumber}" name="mobileNumber" id="rMobile"
           placeholder="请输入手机号" style="ime-mode:disabled" onblur="checkMobile()">
    </span></p>

    <jsp:include page="identifyCode.jsp" >
        <jsp:param name="codeType" value="1" />
    </jsp:include>

        <p class="s-inputbox s-204">
            <span class="s-labelH36">
                手机验证码：
            </span>
            <span class="s-inputBorder s-block">
                <input type="tel" class="s-input s-inputH36" value="${authCode}" name="authCode" id="authCode" placeholder="请输入验证码" onblur="checkAuthCode()">
            </span>
            <span class="s-jvBorder s-block s-r">
                <a class="s-jvBtn s-Btn34" href="javascript:void(0);" id="validBtn" onclick="sValid()">获取验证码</a>
            </span>
            <span class="s-baiBorder s-block s-r" id="timerCode" style="display:none">
                <a href="javascript:void(0)" class="s-baiBtn s-Btn34 s-color5" id="rValidBtn">有效(60s)</a>
            </span>
        </p>

        <div class="m-error" style="color:red">${registerResult}</div>

        <br>手机注册更方便，无需账号绑定。</br>
        已绑定我买网账号手机号码不支持注册、绑定。
        <br>手机号可直接登录我买网。</br>
        <br></br>

        <p><span class="s-lvBorder s-block"><a href="javascript:void(0)" class="s-lvBtn s-Btn34"
                                               onclick="submitFrm()">下一步</a></span></p>
        <input type="hidden" name="mp" value="" />
    </form>
</div>
<script type="text/javascript" language="javascript">
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
            $(".m-error").html("请输入11位有效手机号");
            $(".m-error").show();
            return false;
        }
        return true;
    }
    function checkAuthCode(){
        var authCode = $('#authCode').val();
        if(!/\d{6}/.exec(authCode) || authCode.length != 6 || authCode == null || authCode.trim() == ""){
            $(".m-error").html("请输入6位数字验证码");
            $(".m-error").show();
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
        postJson = handlePostJsonAndCheck(postJson,"m-error");
        if(postJson == null ) return ; //校验方法在引入文件中
        jQuery.ajax({
            type:'POST',
            url:'${homeModule}/passport/sendRegisterCode.action',
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

    function submitFrm() {
        if(!(checkMobile() && checkAuthCode())) {
            return;
        }else{
            $('#frm_register').submit();
        }
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