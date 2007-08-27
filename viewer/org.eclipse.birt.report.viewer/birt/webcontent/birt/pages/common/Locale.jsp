<%@ page import="org.eclipse.birt.report.resource.BirtResources" %>

<%-- Map Java resource messages to Javascript constants --%>
<script type="text/javascript">
// <![CDATA[	
	// Error msgs
	Constants.error.invalidPageRange = '<%= BirtResources.getMessage( "birt.viewer.dialog.page.error.invalidpagerange" )%>';
	Constants.error.parameterRequired = '<%= BirtResources.getMessage( "birt.viewer.error.parameterrequired" )%>';
	Constants.error.invalidPageNumber = '<%= BirtResources.getMessage( "birt.viewer.navbar.error.blankpagenum" )%>';
	Constants.error.unknownError = '<%= BirtResources.getMessage( "birt.viewer.error.unknownerror" )%>';
	Constants.error.generateReportFirst = '<%= BirtResources.getMessage( "birt.viewer.error.generatereportfirst" )%>';
	Constants.error.printPreviewAlreadyOpen = '<%= BirtResources.getMessage( "birt.viewer.dialog.print.printpreviewalreadyopen" )%>';	
// ]]>
</script>
