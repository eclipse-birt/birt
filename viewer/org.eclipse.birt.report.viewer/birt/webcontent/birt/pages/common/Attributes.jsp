<%@ page import="org.eclipse.birt.report.utility.ParameterAccessor" %>

<%-- Map Java attributes to Javascript constants --%>
<script type="text/javascript">
// <![CDATA[	
	// Request attributes
	if ( !Constants.request )
	{
		Constants.request = {};
	}
	Constants.request.format = '<%= ParameterAccessor.getFormat(request) %>';
	Constants.request.rtl = <%= ParameterAccessor.isRtl( request ) %>;
// ]]>
</script>
