var pkHttpsBaseURL="https://mpassport.womai.com/piwik/";
var pkBaseURL="http://tracking.m.womaiapp.com/piwik/";
document.write(unescape("%3Cscript src='" + pkHttpsBaseURL + "piwik.js' type='text/javascript'%3E%3C/script%3E"));
$(function(){
    try {
        var piwikTracker = Piwik.getTracker(pkBaseURL + "piwik.php", 1);
        piwikTracker.trackPageView();
        piwikTracker.enableLinkTracking();
    } catch( err ) {}
});