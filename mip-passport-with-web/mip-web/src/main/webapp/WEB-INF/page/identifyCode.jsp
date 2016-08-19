<!--图片校验-->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${param.codeType eq 0}">
        <p name="codeP">
                    <span>
                    <input name="_identifyingCode" class="s-input2 s-i-code-input"  codeType="${param.codeType}" placeholder="请输入验证码" style="ime-mode:disabled" >
                    <span class="s-i-code"><img onclick="flushCode(this,'0');" src="${homeModule}/passport/generateIdentifyCode.action?r=<%=Math.random()%>&codeType=0" width="63" height="25" alt=""></span>
                    </span>
        </p>

    </c:when>

    <c:when test="${param.codeType eq 11}">
        <li class="s-boxLine">
            <span class="s-bg s-password"></span><p name="codeP"><span>
            <input type="text" id="identifyingCode" name="_identifyingCode"  codeType="${param.codeType}" class="s-input2 s-i-code-input clearInput" size="11" maxlength="6" placeholder="请输入验证码" />
            <a class="fr-clear fr-clear-code clearA"  href="javascript:;"><img src="${homeModule}/images/sso/icon_06.png" /> </a>
            <span class="s-i-code s-i-code-long"><img name="codeImg" id="identifycode_img" onclick="flushCode(this,'${param.codeType}');" src="${homeModule}/passport/generateIdentifyCode.action?r=<%=Math.random()%>&codeType=${param.codeType}" width="63" height="25" alt=""/></span></span></p>
        </li>

    </c:when>

    <c:otherwise>
        <p class="s-inputbox" name="codeP"><span class="s-labelH36">验证码：</span> <span class="s-inputBorder s-block">
         <input name="_identifyingCode" class="s-input s-inputH36"  codeType="${param.codeType}"
                placeholder="请输入验证码" style="ime-mode:disabled" >
                <span class="s-i-code"><img onclick="flushCode(this,'${param.codeType}');" src="${homeModule}/passport/generateIdentifyCode.action?r=<%=Math.random()%>&codeType=${param.codeType}" width="63" height="25" alt=""></span>
        </span></p>
    </c:otherwise>
</c:choose>


<!--图片校验结束-->
<script type="text/javascript">
    function flushCode(obj,code){
        $(obj).attr("src", "${homeModule}/passport/generateIdentifyCode.action?" + "codeType="+code+"&r=" + Math.random());
    }
    function reFlushCode(){
        $("p[name='codeP'] :visible").find("img").each(function(){
            $(this).click();
        });
    }

    //图形验证码格式校验
    function checkIdentifyingCode(errClass){
        var identifyingCode = $("p[name='codeP'] :visible").find("input[name='_identifyingCode']").eq(0).val();
        if (identifyingCode == null || identifyingCode == "" || identifyingCode == undefined || identifyingCode.length!=4) {
            $("."+errClass).html("请输入4位有效验证码");
            $("."+errClass).show();
            return false;
        }
        return true;
    }

    function handlePostJsonAndCheck(postJson,errClass){
        if(!checkIdentifyingCode(errClass)){
            return null;
        }
        var obj = $("p[name='codeP'] :visible").find("input[name='_identifyingCode']").eq(0);
        var identifyingCode =obj.val();
        var codeType =obj.attr("codeType");
        postJson.codeType=codeType;
        postJson.identifyingCode=identifyingCode;
        return postJson;
    }
    //errClass   前台页面校验图片验证码，展示错误
    function handleFormDataAndCheck(formId,errClass){
        if(!checkIdentifyingCode(errClass)){
            return false;
        }
        var obj = $("p[name='codeP'] :visible").find("input[name='_identifyingCode']").eq(0);
        var identifyingCode =obj.val();
        var codeType =obj.attr("codeType");

        $("#"+formId).append("<input type='hidden' name='identifyingCode' value='"+identifyingCode+"' />");
        $("#"+formId).append("<input type='hidden' name='codeType' value='"+codeType+"' />");
        return true;
    }


</script>





