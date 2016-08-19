<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<jsp:include page="head.jsp" />

<body id="mainframe">
<a name="s-top" id="s-top"></a>
<header>
    <div class="s-topbar">
        <h1>重置登录密码成功</h1>
    </div>
</header>
<div class="s-screenWrap">
    <div class="s-info1">
        <div class="s-404page">
            <p>
                重置登录密码成功<br/><span class="s-color6" id="id-countTime">5</span>秒后自动<a class="s-color6" href="${mobileHomeModule}/my/home.action">跳转</a>
            </p>
        </div>
    </div>
</div>
<script type="text/javascript" language="javascript">
    var endTime;
    $(function(){
        var lineLink = location.href;
        var descContent = '全球美食尽在中粮我买网';
        var shareTitle = '中粮我买网';
        loadInfo.init(lineLink,descContent,shareTitle);
        endTime =  new Date().getTime()/1000 + 6;
        timer();
    })

    var timer = function () {
        var a = new Date().getTime() / 1000;
        var b = Math.floor(endTime - a);
        $("#id-countTime").text(b);
        if (b > 0) {
            setTimeout("timer()", 1000);
        } else {
            window.location.href = '${mobileHomeModule}/my/home.action';
        }
    };
</script>
<jsp:include page="bottom.jsp" />
</body>
</html>