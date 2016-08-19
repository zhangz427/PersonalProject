<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>中粮我买网</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="format-detection" content="telephone=no">
    <link href="/images/favicon.ico" rel="shortcut icon">
    <!--<link href="/css/s-base.css" rel="stylesheet" type="text/css" />-->
    <style type="text/css">
            /*type_select*/
        body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote,th,td{ margin:0; padding:0;}
        body{ font:14px/150% 'Microsoft YaHei',Helvetica,Tahoma,sans-serif; color:#333; -webkit-text-size-adjust:none; background-color:#eee; min-width:320px;}
        h1,h2,h3,h4,h5,h6,em,stone{ font-size:14px; font-weight:normal;}
        ol,ul { list-style:none;}
        input,img,select{ vertical-align:middle;}
        img,fieldset{ border:0;}
        em,address,i{ font-style:normal;}
        input[type="submit"],input[type="button"]{-webkit-appearance:none;}
        input{outline: none;}
        a{ color:#333; text-decoration:none; font-size:14px;}

            /* bg */
        .s-bg2{ background-image:url(../images/s-bg2.png); background-repeat:no-repeat; background-size:60px 700px;}

            /* s-topbar */
        .s-topbar{ width:100%; height:43px; border-bottom:1px solid #b5b5b5; position:relative; background-color:#FFF;}
        .s-topbar .s-return{ position:absolute; top:12px; left:8px;}
        .s-topbar .s-return a{ display:block; font-size:16px; line-height:22px; width:50px; height:20px; overflow:hidden; background-position:-44px -157px; padding-left:17px;}
        .s-topbar .s-topbarR{ position:absolute; top:7px; right:8px; padding-top:0;}
        .s-topbar .s-topbarR a, .s-topbar .s-topbarR input{ display:block; font-size:14px; line-height:26px; width:42px; height:28px; overflow:hidden; padding:0 6px; width:auto; color:#333;}
        .s-topbar h1{ font-size:18px; font-weight:normal; height:43px; line-height:43px; text-align: center; text-overflow:ellipsis; vertical-align:bottom; white-space:nowrap;}

        .m-screenWrap-link{ min-width:270px; padding:0 25px 20px 25px;}
        .m-conlink .m-iconlink .m-bg, .m-conlink .m-arrowR{ background-image:url(../images/img-icon-loginlink.png); background-repeat:no-repeat; background-size:40px 150px;}
        .m-conlink .m-inputBox{ border-bottom:1px solid #d9d9d9; height:50px; position:relative;}
        .m-conlink .m-clicka{ display:block; font-size:14px; width:100%; height:50px; line-height:50px;}
        .m-conlink .m-clicka:hover{ text-decoration:none; background-color:#f5f5f5;}
        .m-conlink .m-clicka span{ display:inline-block; padding-left:70px;}
        .m-conlink .m-iconlink .m-bg{ width:31px; height:31px; padding:0; position:absolute; left:25px; top:10px;}
        .m-conlink .m-iconlinka{ background-position:0 0;}
        .m-conlink .m-iconlinkb{ background-position:0 -33px;}
        .m-conlink .m-iconlinkc{ background-position:0 -66px;}
        .m-conlink .m-arrowR{ display:block; width:10px; height:15px; position:absolute; top:19px; right:17px; background-position:0 -120px;}
    </style>
</head>
<body style="background-color:#eeeeee;">
<header>
    <div class="s-topbar">
        <div class="s-return"><a href="javascript: history.go(-1);" class="s-bg2">返回</a></div>
        <h1>第三方联合登录</h1>
    </div>
</header>
<div class="m-screenWrap-link">
    <ol class="m-conlink">
        <li class="m-inputBox"><a class="m-clicka m-iconlink" href="/union/qq/login.action"><span class="m-bg m-iconlinka"></span><span>QQ登录</span></a><span class="m-arrowR"></span></li>
        <li class="m-inputBox"><a class="m-clicka m-iconlink" href="/union/sina/login.action"><span class="m-bg m-iconlinkb"></span><span>新浪微博登录</span></a><span class="m-arrowR"></span></li>
        <li class="m-inputBox"><a class="m-clicka m-iconlink" href="/union/alipay/login.action"><span class="m-bg m-iconlinkc"></span><span>支付宝登录</span></a><span class="m-arrowR"></span></li>
    </ol>
</div>
<script type="text/javascript"  language="javascript">
    var url = location.search;
    if (url.indexOf("?") != -1 && url.indexOf("msg") != -1 && url.indexOf("error") != -1) {
        alert("登录服务器异常,请重新登录!");
    }
</script>
</body>
</html>