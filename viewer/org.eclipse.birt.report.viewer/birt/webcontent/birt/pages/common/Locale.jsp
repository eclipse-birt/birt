<%@ page import="org.eclipse.birt.report.resource.BirtResources" %>

<%-- Map Java resource messages to Javascript constants --%>
<script type="text/javascript">
// <![CDATA[	
	// Error msgs
	Constants.error.invalidPageRange = '<%= BirtResources.getEscapedMessage( "birt.viewer.dialog.page.error.invalidpagerange" )%>';
	Constants.error.parameterRequired = '<%= BirtResources.getEscapedMessage( "birt.viewer.error.parameterrequired" )%>';
	Constants.error.parameterNotAllowBlank = '<%= BirtResources.getEscapedMessage( "birt.viewer.error.parameternotallowblank" )%>';
	Constants.error.parameterNotSelected = '<%= BirtResources.getEscapedMessage( "birt.viewer.error.parameternotselected" )%>';
	Constants.error.invalidPageNumber = '<%= BirtResources.getEscapedMessage( "birt.viewer.navbar.error.blankpagenum" )%>';
	Constants.error.unknownError = '<%= BirtResources.getEscapedMessage( "birt.viewer.error.unknownerror" )%>';
	Constants.error.generateReportFirst = '<%= BirtResources.getEscapedMessage( "birt.viewer.error.generatereportfirst" )%>';
	Constants.error.printPreviewAlreadyOpen = '<%= BirtResources.getEscapedMessage( "birt.viewer.dialog.print.printpreviewalreadyopen" )%>';
	Constants.error.confirmCancelTask = '<%= BirtResources.getEscapedMessage( "birt.viewer.progressbar.confirmcanceltask" )%>';
// ]]>
</script>
