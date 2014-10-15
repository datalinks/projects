<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Rss Tool - delete confirmation</title>
</head>
<body>




		You sure you like to delete file: 
		${selectedFileName}
		
	<form:form method="post" modelAttribute="deleteModel" action="deleterssfileConfirm" >
		<form:hidden path="fileName" value="${selectedFileName}" />
        <form:radiobutton path="doDelete" value="true" />YES 
		<form:radiobutton path="doDelete" value="false" />NO
       	<input type="submit" value="Submit"/>                	
	</form:form>
		
	
	

      
	   
</body>
</html>