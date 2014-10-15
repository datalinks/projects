<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Rss Tool - file navigation</title>
</head>
<body>



<form:form method="post" modelAttribute="rssFileModel" action="rssfile" >

        <form:select path="rssFilez">
            <form:options items="${rssFilez}" itemValue="fileName" itemLabel="fileName"/>
        </form:select>
        Delete file: 
        <form:radiobutton path="doDelete" value="true" />Delete 
		<form:radiobutton path="doDelete" value="false" />Edit
                
        <input type="submit" value="Submit"/>                	
        <a href="./createRssFile">Create new RSS file</a>
        
</form:form>

</body>
</html>