<%@ page import="org.eclipse.birt.report.resource.BirtResources" %>

<%-- Map Java resource messages to Javascript constants --%>
<script type="text/javascript">
// <![CDATA[	
	// Error msgs
	Constants.error.invalidPageRange = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.dialog.page.error.invalidpagerange" )%>';
	Constants.error.parameterRequired = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameterrequired" )%>';
	Constants.error.parameterNotAllowBlank = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameternotallowblank" )%>';
	Constants.error.parameterNotSelected = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.parameternotselected" )%>';
	Constants.error.invalidPageNumber = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.navbar.error.blankpagenum" )%>';
	Constants.error.invalidCopiesNumber = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.copiesnumber" )%>';
	Constants.error.unknownError = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.unknownerror" )%>';
	Constants.error.generateReportFirst = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.generatereportfirst" )%>';
	Constants.error.columnRequired = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.columnrequired" )%>';
	Constants.error.printPreviewAlreadyOpen = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.dialog.print.printpreviewalreadyopen" )%>';
	Constants.error.confirmCancelTask = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.progressbar.confirmcanceltask" )%>';
	Constants.error.copyTrace = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.stacktrace.copy" ) %>';
	Constants.error.traceCopied = '<%= BirtResources.getJavaScriptMessage( "birt.viewer.error.stacktrace.copy.done" ) %>';
// ]]>
</script>
