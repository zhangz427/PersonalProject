var loadInfo = {
    init : function(lineLink,descContent,shareTitle){
    var imgUrl = 'https://mpassport.womai.com/images/share-logo.png';
//    var imgUrl = "$!homeModule.getTarget('/images/share-logo.png')";
//    var lineLink = location.href;
//    var descContent = "全球美食尽在中粮我买网";
//    var shareTitle = '全球美食尽在中粮我买网';
        var appid = '';

        function shareFriend() {
            WeixinJSBridge.invoke('sendAppMessage',{
                "appid": appid,
                "img_url": imgUrl,
                "img_width": "200",
                "img_height": "200",
                "link": lineLink,
                "desc": descContent,
                "title": shareTitle
            }, function(res) {
                //_report('send_msg', res.err_msg);
            })
        }
        function shareTimeline() {
            WeixinJSBridge.invoke('shareTimeline',{
                "img_url": imgUrl,
                "img_width": "200",
                "img_height": "200",
                "link": lineLink,
                "desc": descContent,
                "title": descContent
            }, function(res) {
                //_report('timeline', res.err_msg);
            });
        }
        function shareWeibo() {
            WeixinJSBridge.invoke('shareWeibo',{
                "content": descContent+' '+lineLink,
                "url": lineLink
            }, function(res) {
                //_report('weibo', res.err_msg);
            });
        }
// 当微信内置浏览器完成内部初始化后会触发WeixinJSBridgeReady事件。
        document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
            // 发送给好友
            WeixinJSBridge.on('menu:share:appmessage', function(argv){
                shareFriend();
            });
            // 分享到朋友圈
            WeixinJSBridge.on('menu:share:timeline', function(argv){
                shareTimeline();
            });
            // 分享到微博
            WeixinJSBridge.on('menu:share:weibo', function(argv){
                shareWeibo();
            });
        }, false);
    }
}
