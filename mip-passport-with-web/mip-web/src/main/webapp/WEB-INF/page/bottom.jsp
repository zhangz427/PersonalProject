<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<footer class="syncsite-city-h">
    <div class="s-footera s-lr">
        <div class="sl">
            <a href="${homeModule}/passport/checkLogin.action?redirectUrl=${mobileHomeModule}/my/home.action">登录</a> | <a href="${homeModule}/passport/toRegister.action">注册</a>
        </div>
        <div class="sr">
            <a href="${mobileHomeModule}/my/feedBack.action">反馈</a> |
            <a href="#s-top">回顶部</a>
        </div>
    </div>
    <div class="s-footerb">
        <span class="s-baiBorder s-inblock"><a href="${mobileHomeModule}/index.shtml" class="s-baiBtn s-Btn29">首页</a></span>&nbsp;
        <span class="s-baiBorder s-inblock"><a href="${mobileHomeModule}/category/list.action" class="s-baiBtn s-Btn29">分类</a></span>&nbsp;
        <span class="s-baiBorder s-inblock"><a href="${mobileHomeModule}/cart/cartGet.action" class="s-baiBtn s-Btn29">购物车</a></span>&nbsp;
        <span class="s-baiBorder s-inblock"><a href="${mobileHomeModule}/my/home.action" class="s-baiBtn s-Btn29">我的小买</a></span>
    </div>
    <div class="s-footerc">
        <p class="s-color7"><a target="_blank" class="s-color7" href="https://itunes.apple.com/cn/app/zhong-liang-wo-mai-wang/id528910047?mt=8">&nbsp;下载iphone客户端</a>&#12288;|&#12288;<a target="_blank" class="s-color7" href="http://upload.m.womai.com/app/android/womai.apk">下载android客户端</a></p>
        <p class="s-color7"><a class="s-color7" href="${mobileHomeModule}">触屏版</a>&#12288;|&#12288;<a class="s-color7" href="http://www.womai.com?redirect=false">电脑版</a></p>
        <p>客服电话 <a href="tel:400-005-5678" class="s-color7">400-005-5678</a></p>
        <div style="width:300px;margin:0 auto; padding:5px 0;">
            <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=11010102000458" style="display:inline-block;text-decoration:none;height:20px;line-height:20px;"><img src="${homeModule}/images/icpno.png" style="float:left;"/><p style="float:left;height:20px;line-height:20px;margin: 0px 0px 0px 5px; color:#939393;">京公网安备 11010102000458号</p></a>
        </div>
    </div>
</footer>
<script type="text/javascript">
    //通用GA埋点js
    var GA_PageGroup = GA_PageGroup || "";
    var GA_UserId = '${commUserId}';
    var _runJaExtra=_runJaExtra|| "";
    //是不是在外部执行ja
    if(!_runJaExtra){
        gaTracker('create', 'UA-10228579-1', 'auto','Truemetrics', {'userId': '${commUserId}'});
        gaTracker('Truemetrics.require', 'linkid', 'linkid.js');
        gaTracker('Truemetrics.require', 'displayfeatures');
        gaTracker('Truemetrics.require', 'ec');
        gaTracker('Truemetrics.set', 'contentGroup1', GA_PageGroup);
        gaTracker(function() {
            var clientID = gaTracker.getByName('Truemetrics').get('clientId');
            gaTracker('Truemetrics.set', 'dimension1', clientID); });
        gaTracker('Truemetrics.set', 'dimension2', '${commUserId}');

        gaTracker('Truemetrics.set', 'transport', 'beacon');
        gaTracker('Truemetrics.set', 'forceSSL', true);
        gaTracker('Truemetrics.send', 'pageview');
    }
</script>
<script type="text/javascript">
    //Vizury埋点
    (function() {
        try {
            var viz = document.createElement("script");
            viz.type = "text/javascript";
            viz.async = true;
            viz.src = ("https:" == document.location.protocol ?"https://cn-tags.vizury.com" : "http://cn-tags.vizury.com")+ "/analyze/pixel.php?account_id=VIZVRM2960";

            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(viz, s);
            viz.onload = function() {
                try {
                    pixel.parse();
                } catch (i) {
                }
            };
            viz.onreadystatechange = function() {
                if (viz.readyState == "complete" || viz.readyState == "loaded") {
                    try {
                        pixel.parse();
                    } catch (i) {
                    }
                }
            };
        } catch (i) {
        }
    })();
    //百度统计
    var _hmt = _hmt || [];
    (function() {
        var hm = document.createElement("script");
        hm.src = "//hm.baidu.com/hm.js?3d400e618922947d47d0b95855f8b230";
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    })();
    //sem
    var _fxcmd = _fxcmd || [];
    _fxcmd.sid = 'dad82a7fe513d4091944229540d582e1';
    (function () {
        var _pzfx = document['createElement']('script');
        _pzfx.type = 'text/javascript';
        _pzfx.async = true;
        _pzfx.src = '//static.w3t.cn/fx/1/1/fx.js';
        var sc = document.getElementsByTagName('script')[0];
        sc.parentNode.insertBefore(_pzfx, sc);
    })();
</script>
<script type="text/javascript">
    (function(){
        var bp = document.createElement('script');
        var curProtocol = window.location.protocol.split(':')[0];
        if (curProtocol === 'https') {
            bp.src = 'https://zz.bdstatic.com/linksubmit/push.js';
        }
        else {
            bp.src = 'http://push.zhanzhang.baidu.com/push.js';
        }
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(bp, s);
    })();
</script>
