var app = {
    browser: {},
    url: "http://m.womai.com/",
    ios: {
        native: "womaiapp://m.womai.com/0index.shtml",
        remote: "https://itunes.apple.com/cn/app/zhong-liang-wo-mai-wang/id528910047?mt=8"
    },
    android: {
        native: "womaiapp://m.womai.com/0index.shtml",
        remote: "http://upload.m.womai.com/app/android/womai.apk"
    },
    t1: 100,
    t2: 200,
    ifr: '',
    timeout: 300,
    targetUrl : '',
    system: function () {
        var ua = navigator.userAgent,
        //android = ua.match(/(Android)?[\s\/]+([\d.]+)?/),
            android = ua.indexOf('Android') > -1 ? true : false,
        //ipad = ua.match(/(iPad).*OS\s([\d_]+)/),
            ipad = ua.indexOf('iPad') > -1 ? true : false,
        //ipod = ua.match(/(iPod)(.*OS\s([\d_]+))?/),
            ipod = ua.indexOf('iPod') > -1 ? true : false,
        //iphone = !ipad && ua.match(/(iPhone\sOS)\s([\d_]+)/),
            iphone = !ipad && ua.indexOf('iPhone') > -1 ? true : false,
            weixin = ua.indexOf('MicroMessenger') > -1 ? true : false,
            windowsPhone = ua.indexOf('Windows Phone') > -1 ? true : false,
            weiboTecent = ua.indexOf('TXMicroBlog') > -1 ? true : false,
            weiboXinlang = ua.indexOf('Weibo') > -1 ? true : false,
            isChrome = ua.indexOf('Chrome') > -1 ? true : false,
            isSafari = ua.indexOf('Safari') > -1 ? true : false,
            isUC = ua.indexOf('UCBrowser') > -1 ? true : false,
            isOpera = ua.indexOf('OPR/') > -1  || ua.indexOf('OPiOS/') > -1 ? true : false,
            verOS = iphone || ipad || ipod ? ua.match(/(OS)\s*(\d)/)[2] : '';
        //alert(/(MicroMessenger\/)([5-9]{1}|[1-9][0-9]+)./.test(ua));
       
        if (android) {
            this.browser['browser'] = 'anroid';
            if (weiboTecent || weiboXinlang) {
                this.browser['weibo_sys'] = 'weibo';
            }
            if (isChrome) {
                this.browser['chrome'] = 'chrome';
            }
            if (isUC) {
                this.browser['uc'] = 'uc';
            }
        }
        if (ipad || ipod || iphone) {
            this.browser['browser'] = 'ios';
            if (weiboTecent || weiboXinlang) {
                this.browser['weibo_sys'] = 'weibo';
            }
            if (isSafari) {
                this.browser['safari'] = 'safari';
                this.browser['verOS'] = verOS;
            }
        }
        if (isOpera) {
            this.browser['opera'] = 'opera';
        }
        if (windowsPhone) {
            this.browser['browser'] = 'windowsPhone';
        }
        if (weixin) {
            this.browser['browser'] = 'weixin';
            if (android) {
                this.browser['weixin_sys'] = 'anroid';
            }
            if (ipad || ipod || iphone) {
                this.browser['weixin_sys'] = 'ios';
            }
            if (windowsPhone) {
                this.browser['weixin_sys'] = 'windowsPhone';
            }
        }

        return this.browser;
    },
    createIframe: function () {
        if (!this.ifr) {
            var iframe = document.createElement("iframe");
            iframe.style.cssText = "display:none;width:0px;height:0px;";
            document.body.appendChild(iframe);
            this.ifr = iframe;
        }
        return this.ifr;
    },
    createChromeOpen: function () {
        return "intent://m.womai.com/0index.shtml#Intent;scheme=womaiapp;package=com.womai;end";
    },
    setOpenAppUrl: function (targetUrl) {
        if ('chrome' == app.browser['chrome']) {//chrome及android自带浏览器
            if ('anroid' == app.browser['browser']) {
                window.location.href = targetUrl ? targetUrl : app.android.native;
//                this.createIframe().src = this.createChromeOpen();
            } else {
                window.location.href =  targetUrl ? targetUrl :app.android.native;
            }
        } else {
            app.createIframe().src = targetUrl ? targetUrl : app.android.native;
        }
    },
    downloadUrl: function () {
        window.location.href = app.url + 'download_backup.shtml';
    },
    openApp: function (targetUrl) {
        var _t = Date.now();
        app.setOpenAppUrl(targetUrl);
        setTimeout(function () {
           if(!targetUrl){
              app.downloadUrl();
           }
        }, 10);
    },
    appRedirect: function (CpsKey) {
        app.system();
        if(/([0-9]{1,3})p([0-9]+)\.shtml/.test(location.href) || /([0-9]{1,3})s([0-9]+)\.shtml/.test(location.href) || location.href.indexOf('/product/detail.action') > -1){
            app.targetUrl = CpsKey ? location.search ? location.href.replace(/http/g,'womaiapp') + '&' + CpsKey : location.href.replace(/http/g,'womaiapp') + '?' + CpsKey : '';
        }
//            if('weibo' != app.browser['weibo_sys']){
        if ('anroid' == app.browser['browser']) {
            // $('.s-layer a.s-downBtn').attr('href',app.android.remote);
            try {
                if (/(download_backup).shtml/.test(window.location.href)) {
                    window.location.href = app.android.remote;
                    return false;
                }
                if ('uc' == app.browser['uc']) {
                    app.openApp(app.targetUrl);
                } else {
                    window.location.href = app.targetUrl ? app.targetUrl :app.android.native;
                    setTimeout(function () {
                        if(!app.targetUrl){
                             window.location.href = app.url + 'download_backup.shtml';
                        }
                    }, 10);
                }
//                      window.location.href = 'download_backup.shtml';
                e.preventDefault();
                e.stopPropagation();
            } catch (e) {
            }
        }
        if ('weibo' != app.browser['weibo_sys']) {
            if ('ios' == app.browser['browser']) {
                if (parseInt(app.browser['verOS']) >= 9) {
                    try {
                        location.href = app.targetUrl ? app.targetUrl : app.ios.native;
                        setTimeout(function () {
                            if(!app.targetUrl) {
                                window.location.href = app.url + 'download_backup.shtml';
                            }
                        }, 2000);
                    } catch (e) {
                    }
                } else {
                    try {
                        var iframe = document.createElement("iframe");
                        iframe.style.cssText = "display:none;width:0px;height:0px;";
                        document.body.appendChild(iframe);
                        iframe.src = app.targetUrl ? app.targetUrl : app.ios.native;
                        setTimeout(function () {
                            if(!app.targetUrl) {
                                window.location.href = app.ios.remote;
                            }
                        }, app.t2);

                    } catch (e) {
                    }
                }
            }
        }
//            }
    },
    appDownAction: function (){
        var w_url = window.location.href;
        if (/(download)\w*.shtml/.test(w_url)) {
            if ($(window).height() - 254 > 152) {
                $('.container').css({'height': $(window).height() + 'px'});
                $('.dl-footer').css({'position': 'absolute', 'bottom': 0, 'left': '50%', 'margin-left': '-50%', 'width': '100%'});
            }
            if ($(window).height() >= 396) {
                var h = $('.dl-footer').offset().top - 30;
                $('.container h4').css({'position': 'absolute', 'top': h + 'px', 'left': '50%', 'margin-left': '-50%', 'padding': 0});
            }
            if ('anroid' == app.browser['browser']) {
//                $('.dl-bg .link-anroid').css('display', 'inline-block');

//                $('.dl-bg .dl-link img').attr('src', '../images/download_06_2.png');
//                $('.dl-footer .dl-footer-right img').attr('src', '../images/download_09_2.png');
                $('.dl-bg .link-ios').click(function () {
                    return false;
                });
                if (/(download_backup).shtml/.test(w_url)) {
                    $('.dl-bg .link-anroid').attr('href', app.android.remote);
                }
                if (/(download).shtml/.test(w_url)) {
                    $('.container .link-anroid').click(function (e) {

                        if ('weibo' == app.browser['weibo_sys']) {
                            $('.dl-bg .container').css('display', 'none');
                            $('.dl-bg .dl-weixin-bg').css('display', 'block');
                            window.location.href = app.url + 'weixinRedirect.shtml';
                            return false;
                        }
                        try {
//                            app.openApp();
//                             window.location.href = app.android.remote;
                            window.location.href = app.android.native;
                            setTimeout(function () {
                                window.location.href = app.url + 'download_backup.shtml';
//                                     window.localStorage.setItem('parents','window.location.href = app.android.native;');
                            }, 10);
                            e.preventDefault();
                            e.stopPropagation();
                        } catch (e) {
                        }
                    });
                }
            }
            if ('ios' == app.browser['browser']) {

                $('.dl-bg .link-anroid').click(function () {
                    return false;
                });
                if (/(download_backup).shtml/.test(w_url)) {
                    $('.dl-bg .link-ios').attr('href', app.ios.remote);
                }
                $('.container .link-ios').click(function () {
                    if ('weibo' == app.browser['weibo_sys']) {
                        $('.dl-bg .container').css('display', 'none');
                        $('.dl-bg .dl-weixin-bg').css('display', 'block');
                        window.location.href = app.url + 'weixinRedirect.shtml';
                        return false;
                    }
                    try {
                        window.location.href = app.ios.native;
                        setTimeout(function () {
                            window.location.href = app.ios.remote;

                        }, this.t2);

                    } catch (e) {
                    }
                });
            }
            if ('windowsPhone' == this.browser['browser']) {
                window.location.href = '';
            }
            if ('weixin' == this.browser['browser']) {
                if ('ios' == this.browser['weixin_sys']) {
                    $('.container .link-ios').click(function () {
                        $('.dl-bg .container').css('display', 'none');
                        $('.dl-bg .dl-weixin-bg').css('display', 'block');
                        window.location.href = app.url + 'weixinRedirect.shtml';
                    });
                }
                if ('windowsPhone' == this.browser['weixin_sys']) {
                    //$('.container .link-ios').tap(function(){
                    window.location.href = app.url;
                    //});
                }
                if ('anroid' == this.browser['weixin_sys']) {
                    $('.dl-bg .container .link-ios').css('display', 'none');
                    $('.dl-bg .dl-link img').attr('src', '../images/download_06_2.png');
                    $('.dl-footer .dl-footer-right img').attr('src', '../images/download_09_2.png');
                    $('.dl-bg .container .link-anroid').css('display', 'inline-block').click(function () {
                        window.location.href = app.url + 'weixinRedirect.shtml';
                        $('.dl-bg .container').css('display', 'none');
                        $('.dl-bg .dl-weixin-bg').css('display', 'block');
                    });
                }
            }
        }
    },
    getCookieName: function (cookie_name) {
        var c = document.cookie, c_index = c.indexOf(cookie_name);
        if (c_index > -1) {
            c_index += cookie_name.length + 1;
            var c_end = c.indexOf(';', c_index);
            if (c_end == -1) {
                c_end = c.length;
            }
            var cookie_value = unescape(c.substring(c_index, c_end));
            return cookie_value;
        }

    }
};
$(function () {
    app.system();
    app.appDownAction();
    window.addEventListener('orientationchange', function () {
        setTimeout(function () {
            app.appDownAction();
        }, 300);
    }, false);

    var w_url = window.location.href, isView = app.getCookieName('sinopec');
    var html, c1 = window.sessionStorage.getItem('isDisplay') == null ? 1 : window.sessionStorage.getItem('isDisplay');
//    if (/\w+.womai.com\/(index.shtml)*(\?mid=\d*)*$/.test(w_url) || w_url.indexOf('?redirect=false') > -1 || w_url.indexOf('http://m.womai.com/?utm_source') > -1 || w_url.indexOf('getIndex.action') > -1) {
//        html = '<div class="s-index"><img src="' + app.url + 'images/bg-index_01.png" class="index_bg"/><img src="' + app.url + 'images/bg-index_02.png" class="index_bg"/><img src="' + app.url + 'images/bg-index_03.png" class="index_bg"/><img src="' + app.url + 'images/bg-index_04.png" class="index_bg"/><div class="s-index-right"><img src="' + app.url + 'images/banner-index_09.png" width="113px" height="50px"><a class="s-index-downBtn"><img src="' + app.url + 'images/banner-index_13.png" width="112px" height="21px"/> </a></div></div></div><a class="s-index-close" href="javascript:;"><em><img src="' + app.url + 'images/close-btn_03.png" width="11px" height="11px"/></em></a>';
//        if ('windowsPhone' != app.browser['browser'] && undefined == app.browser['weibo_sys'] && 'weixin' != app.browser['browser'] && isView == undefined && 'opera' != app.browser['opera']) {
//
//            if (c1 == 1) {
//                $('body').prepend(html);
//                var left1 = $('.index_bg').offset().left + 320 - 28;
//                $('.s-index-close').css('left', left1 + 'px');
//            }
//        }
//        ;
//    }
//    ;
    //商品详情页
//    if ((/([0-9]{1,3})p([0-9]+)\.shtml/.test(w_url) || w_url.indexOf('/product/detail.action') > -1) && 'opera' != app.browser['opera'] && '168321' != app.getCookieName('promotionId') && !(/0p550268.shtml/.test(w_url)) && !(/0p544199.shtml/.test(w_url)) && !(/0p550283.shtml/.test(w_url)) && !(/0p577227.shtml/.test(w_url)) && !(/0p596278.shtml/.test(w_url))) {//商品详情页
//        html = '<div class="s-product-container s-product-relative" id="s-product-container"><div class="wrapper"><div><span><img src="http://m.womai.com/images/s-bg-pop-mai1.png" width="23px" height="23px"/></span><b>手机专享 购年货好礼<br/>送59元福袋</b></div><a class="s-downBtn"><img src="http://m.womai.com/images/banner-s-down_03.png" width="56px" height="15px"/> </a></div></div><div class="s-layer-close-wrap s-close-product"><a class="s-layer-close" href="javascript:;" id="s-close-p-link"><em><img src="http://m.womai.com/images/close-btn_03.png" width="11px" height="11px"/></em></a></div>';
//        if ('windowsPhone' != app.browser['browser'] && undefined == app.browser['weibo_sys'] && 'weixin' != app.browser['browser'] && isView == undefined) {
//            var t3 = new Date().getTime();
//            if (t3 - window.localStorage.getItem('closeProductTime2') >= 45000 || window.localStorage.getItem('closeProductTime2') == null) {
//                $('body').prepend(html);
//            }
//        }
//        $(window).scroll(function () {
//            if ($(window).scrollTop() > 0) {
//                $('#s-product-container').removeClass('s-product-relative').addClass('s-product-fixed');
//            } else {
//                $('#s-product-container').removeClass('s-product-fixed').addClass('s-product-relative');
//            }
//        });
//
//        $('.sr a:eq(1)').click(function () {
//            setTimeout(function () {
//                $(window).scrollTop(0);
//                $('#s-product-container').removeClass('s-product-fixed').addClass('s-product-relative');
//            }, 100);
//        });
//    }
//    ;

    //列表页
//    if (/([0-9]{1,3})[bc]([\-0-9]+)\.shtml/.test(w_url) || w_url.indexOf('/product/common/list.action') > -1 || /([0-9]{1,3})k(.*?)\.shtml$/.test(w_url) || w_url.indexOf('login.action') > -1 || (w_url.indexOf('/my/home.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/onlineCheckout.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/order/checkout.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/my/orders.action') > -1 && $('#frm_login').css('display') == 'block') || w_url.indexOf('mobileRegister.action') > -1 || w_url.indexOf('submitorder.action') > -1 || (w_url.indexOf('present/checkProductNum.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('my/gifts.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('my/gifts.action') > -1 && $('#frm_login').css('display') == 'block')) {// 商品列表页 登录页 订单成功页
//        if ('opera' != app.browser['opera']  && '168321' != app.getCookieName('promotionId')) {
//            html = '<div class="s-layer" id="s-list-layer"><div class="wrapper"><div><span><img src="http://m.womai.com/images/s-bg-pop-mai1.png" width="23px" height="23px"/></span><b>手机专享 购年货好礼<br/>送59元福袋</b></div><a class="s-downBtn"><img src="http://m.womai.com/images/banner-s-down_03.png" width="56px" height="15px"/> </a></div></div><div class="s-layer-close-wrap" id="s-layer-list-wrap"><a class="s-layer-close" id="s-layer-list-close" href="javascript:;"><em><img src="http://m.womai.com/images/close-btn_03.png" width="11px" height="11px"/></em></a></div>';
//            if ('windowsPhone' != app.browser['browser'] && undefined == app.browser['weibo_sys'] && 'weixin' != app.browser['browser'] && isView == undefined) {
//                if (w_url.indexOf('login.action') > -1 || (w_url.indexOf('/my/home.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/order/checkout.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/my/orders.action') > -1 && $('#frm_login').css('display') == 'block') || w_url.indexOf('mobileRegister.action') > -1 || w_url.indexOf('submitorder.action') > -1) {
//                    window.localStorage.setItem('closeListTime1', 0);
//                }
//                var t21 = new Date().getTime();
//                if (t21 - window.localStorage.getItem('closeListTime1') >= 45000 || window.localStorage.getItem('closeListTime1') == null) {
//                    $("body").append(html);
//                    $('#s-list-layer,#s-layer-list-wrap').css('display', 'block');
//                }
//            }
//            ;
//        };
//    }
//    ;

    //活动列表页
//    if (/([0-9]{1,3})s([0-9]+)\.shtml/.test(w_url) && 'opera' != app.browser['opera'] && '168321' != app.getCookieName('promotionId') && !(/0s3302.shtml/.test(w_url)) && !(/0s3295.shtml/.test(w_url))) {
//        html = '<div class="s-layer" id="s-activity-layer"><div class="wrapper"><div><span><img src="http://m.womai.com/images/s-bg-pop-mai1.png" width="23px" height="23px"/></span><b>手机专享 购年货好礼<br/>送59元福袋</b></div><a class="s-downBtn"><img src="http://m.womai.com/images/banner-s-down_03.png" width="56px" height="15px"/> </a></div></div><div class="s-layer-close-wrap" id="s-layer-activity-wrap"><a class="s-layer-close" id="s-activity-close" href="javascript:;"><em><img src="http://m.womai.com/images/close-btn_03.png" width="11px" height="11px"/></em></a></div>';
//        if ('windowsPhone' != app.browser['browser'] && undefined == app.browser['weibo_sys'] && 'weixin' != app.browser['browser'] && isView == undefined) {
//            if (w_url.indexOf('login.action') > -1 || (w_url.indexOf('/my/home.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/order/checkout.action') > -1 && $('#frm_login').css('display') == 'block') || (w_url.indexOf('/my/orders.action') > -1 && $('#frm_login').css('display') == 'block') || w_url.indexOf('mobileRegister.action') > -1 || w_url.indexOf('submitorder.action') > -1) {
//                window.localStorage.setItem('closeActivityTime1', 0);
//            }
//            var t5 = new Date().getTime();
//            if (t5 - window.localStorage.getItem('closeActivityTime1') >= 45000 || window.localStorage.getItem('closeActivityTime1') == null) {
//                $("body").append(html);
//                $('#s-activity-layer,#s-layer-activity-wrap').css('display', 'block');
//            }
//      };
//    }
//    ;


    if (html != '') {
        if ('anroid' == app.browser['browser']) {
            $('.s-layer,.s-index,.s-product-container').live('click', function (event) {
                app.appRedirect();
            });
        }
        ;
        if ('ios' == app.browser['browser']) {
            $('.s-index-downBtn,.s-downBtn').click(function (event) {
                var e = event || window.event, e_t = e.srcElement || e.target;
                app.appRedirect();
                e.preventDefault();
                e.stopPropagation();
            });
        }
        ;
        //关闭按钮
        $("#s-layer-list-close").click(function () {//列表页
            $('#s-list-layer,#s-layer-list-wrap').css('display', 'none');
            if (/([0-9]{1,3})[bc]([\-0-9]+)\.shtml$/.test(w_url) || w_url.indexOf('/product/common/list.action') > -1 || /([0-9]{1,3})k(.*?)\.shtml$/.test(w_url)) {// 普通/活动商品列表
                var t11 = new Date().getTime();
                window.localStorage.setItem('closeListTime1', t11);
            }
            ;
        });
        //活动页
        $("#s-activity-close").click(function () {
            $('#s-activity-layer,#s-layer-activity-wrap').css('display', 'none');
            if (/([0-9]{1,3})s([0-9]+)\.shtml$/.test(w_url)) {
                var t6 = new Date().getTime();
                window.localStorage.setItem('closeActivityTime1', t6);
            }
        });

        //商品详情页
        $("#s-close-p-link").click(function () {
            $('.s-product-container,.s-layer-close-wrap').css('display', 'none');
            if (/([0-9]{1,3})p([0-9]+)\.shtml$/.test(w_url) || w_url.indexOf('/product/detail.action') > -1) {
                var t4 = new Date().getTime();
                window.localStorage.setItem('closeProductTime2', t4);
            }
        });

        $('.s-index-close').click(function () {
            $('.s-index,.s-index-close').css('display', 'none');
            window.sessionStorage.setItem('isDisplay', 0);
        });
    }
    ;

});

