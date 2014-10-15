<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
    <title>Rss Tool - file navigation</title>
</head>
<body>

    <c:if test="${not empty param.login_error}">
      <div class="errors">
        <p>
          security_login_unsuccessful
          <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
          .
        </p>
      </div>
    </c:if>
    <c:if test="${empty param.login_error}">
      <p>
        security_login_message
      </p>
    </c:if>
    <spring:url value="/resources/j_spring_security_check" var="form_url" />
    <form name="f" action="${fn:escapeXml(form_url)}" method="POST">
      <div>
        <label for="j_username">
          security_login_form_name
        </label>
        <input id="j_username" type='text' name='j_username' style="width:150px" />
        user
        <script type="text/javascript">
          <c:set var="sec_name_msg">
            <spring:escapeBody javaScriptEscape="true">${name_msg}</spring:escapeBody>
          </c:set>
          Spring.addDecoration(new Spring.ElementDecoration({elementId : "j_username", widgetType : "dijit.form.ValidationTextBox", widgetAttrs : {promptMessage: "${sec_name_msg}", required : true}})); 
        </script>
      </div>
      <br />
      <div>
        <label for="j_password">
          password
        </label>
        <input id="j_password" type='password' name='j_password' style="width:150px" />
       xxx
        <script type="text/javascript">
          <c:set var="sec_pwd_msg">
            <spring:escapeBody javaScriptEscape="true">${pwd_msg}</spring:escapeBody>
          </c:set>
          Spring.addDecoration(new Spring.ElementDecoration({elementId : "j_password", widgetType : "dijit.form.ValidationTextBox", widgetAttrs : {promptMessage: "${sec_pwd_msg}", required : true}})); 
        </script>
      </div>
      <br />
      <div class="submit">
        <script type="text/javascript">Spring.addDecoration(new Spring.ValidateAllDecoration({elementId:'proceed', event:'onclick'}));</script>
        button
        <input id="proceed" type="submit" value="${fn:escapeXml(submit_label)}" />
        reset
        <input id="reset" type="reset" value="${fn:escapeXml(reset_label)}" />
      </div>
    </form>
</div>

