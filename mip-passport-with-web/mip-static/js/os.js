/*
 */
function getOS() {
    var sUserAgent = navigator.userAgent;
    if(sUserAgent!=""){
        sUserAgent = sUserAgent.toLowerCase();
    }
    var isWin = (navigator.platform === "Win32") || (navigator.platform === "Windows");
    var isMac = (navigator.platform === "Mac68K") || (navigator.platform === "MacPPC") || (navigator.platform === "Macintosh") || (navigator.platform === "MacIntel");
    var bIsIpad = (sUserAgent.indexOf("ipad") > -1);
    var bIsIphoneOs = (sUserAgent.indexOf("iphone") > -1);
    var isUnix = (navigator.platform === "X11") && !isWin && !isMac;
    var isLinux = (String(navigator.platform).indexOf("Linux") > -1);
    var bIsAndroid = (sUserAgent.indexOf("android") > -1);
    var bIsCE = (sUserAgent.indexOf("windows ce") > -1);
    var bIsWM = (sUserAgent.indexOf("windows") > -1) && ((sUserAgent.indexOf("phone") > -1) || (sUserAgent.indexOf("mobile") > -1));

    if (bIsIpad || bIsIphoneOs){
        return "1";//ios
    }
    if (isLinux) {
        if (bIsAndroid){
            return "2";//Android
        }else{
            return "3";//Linux
        }
    }
    if (isMac){
        return "4";//Mac
    }
    if (isUnix){
        return "5";//Unix
    }

    if(bIsCE || bIsWM){
        return '6';//wm
    }
    if (isWin) {
        var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;
        if (isWin2K)
            return "7";//Win2000
        var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 ||
            sUserAgent.indexOf("Windows XP") > -1;
        if (isWinXP)
            return "7";//WinXP
        var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;
        if (isWin2003)
            return "7";//Win2003
        var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
        if (isWinVista)
            return "7";//WinVista
        var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
        if (isWin7)
            return "7";//Win7
        var isWin8 = sUserAgent.indexOf("Windows NT 6.2") > -1 || sUserAgent.indexOf("Windows 8") > -1;
        if (isWin8)
            return "7";//Win8
    }
    return "9";//other
}