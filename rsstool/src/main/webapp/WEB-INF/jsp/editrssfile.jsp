<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
    <title>Rss Tool - edit file</title>
</head>
<body>


	<form:form method="post" modelAttribute="editModel" action="editFile" >
	
	<div>
	    <div style="display: table-row;" >
	    	<div style="display: table-cell;">FileName</div> 
	    	<div style="display: table-cell;"><form:input path="fileName" readonly="readonly" onfocus="this.blur()" /></div> 
		</div>	    	
	</div>

	<div>
		&nbsp;
		&nbsp;
	</div>

	<div>
	    <div style="display: table-row;" >
	    	<div style="display: table-cell;">Channel</div> 
		</div>
	    <div style="display: table-row;" >
	    	<div style="display: table-cell;">&nbsp;</div> 
		</div>
		
	    <div style="display: table-row;">
	        <div style="display: table-cell;">Title</div>
	        <div style="display: table-cell;"><form:input path="channel.title" /></div>
	    </div>
	    <div style="display: table-row;">
	        <div style="display: table-cell;">Link</div>
	        <div style="display: table-cell;"><form:input path="channel.link"/></div> This is the link you subscribe to
	    </div>
	    <div style="display: table-row;">
	        <div style="display: table-cell;">Description</div>
	        <div style="width: 200px; height: 40px;"><form:textarea path="channel.description" /></div>
	    </div>	    
	</div>
	
	<div>
		&nbsp;
		&nbsp;
	</div>
		
<div>
	<div>
	    <div style="display: table-row;" >
	    	<div style="display: table-cell;">Item(s)</div> 
		</div>
	    <div style="display: table-row;" >
	    	<div style="display: table-cell;">&nbsp;</div> 
		</div>
		

			<div style="display: table-row;">
	        		<div style="display: table-cell;">Title</div>
	        		<div style="display: table-cell;">Link</div>
	        		<div style="display: table-cell;">Description</div>
			</div>
			<c:forEach items="${selectedItems}" var="item" varStatus="status">
				<div style="display: table-row;">
	        		<div style="display: table-cell;"><form:input path="rssItems[${status.index}].title" /></div>
	        		<div style="display: table-cell;"><form:input path="rssItems[${status.index}].link" /></div>
	        		<div style="display: table-cell;"><form:input path="rssItems[${status.index}].description" /></div>
	    		</div>	
			</c:forEach>

	</div>
	<div style="display: table-row;">
       		<div style="display: table-cell;"><input type="submit" class="button" name="save" value="Save" /></div>
       		<div style="display: table-cell;"><input type="submit" class="button" name="add_item" value="Add Item" /></div>
       		<div style="display: table-cell;"><input type="submit" class="button" name="cancel" value="Cancel" /></div>       		
	</div>
		
	</form:form>	
	
<div>
      
	   
</body>
</html>