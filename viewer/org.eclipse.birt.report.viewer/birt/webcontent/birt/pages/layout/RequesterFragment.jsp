<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.resource.BirtResources,
 				 org.eclipse.birt.report.presentation.aggregation.IFragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>PARAMETER SELECTION PAGE</TITLE>
		<BASE href="<%= request.getScheme( ) +"://" + request.getServerName( ) + ":" + request.getServerPort( ) + request.getContextPath( ) + "/webcontent/frameset" %>" >
		
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; CHARSET=utf-8">
		<LINK REL="stylesheet" HREF="birt/styles/style.css" TYPE="text/css">
		<LINK HREF="birt/styles/dialogbase.css" media="screen" rel="stylesheet" type="text/css"/>			

		<SCRIPT SRC="birt/ajax/utility/Debug.js" type="text/javascript"></script>
		<script src="birt/ajax/lib/prototype.js" type="text/javascript"></script>
		
		<!-- Mask -->
		<script src="birt/ajax/core/Mask.js" type="text/javascript"></script>
		<script src="birt/ajax/utility/BrowserUtility.js" type="text/javascript"></script>
		
		<!-- Drag and Drop -->
		<script src="birt/ajax/core/BirtDndManager.js" type="text/javascript"></script>
		
		<script src="birt/ajax/utility/Constants.js" type="text/javascript"></script>
		<script src="birt/ajax/utility/BirtUtility.js" type="text/javascript"></script>
		
		<script src="birt/ajax/core/BirtEventDispatcher.js" type="text/javascript"></script>
		<script src="birt/ajax/core/BirtEvent.js" type="text/javascript"></script>
		
		<script src="birt/ajax/mh/BirtBaseResponseHandler.js" type="text/javascript"></script>
		<script src="birt/ajax/mh/BirtGetUpdatedObjectsResponseHandler.js" type="text/javascript"></script>

		<script src="birt/ajax/ui/app/AbstractUIComponent.js" type="text/javascript"></script>
		<script src="birt/ajax/ui/app/BirtProgressBar.js" type="text/javascript"></script>

 		<script src="birt/ajax/ui/report/AbstractReportComponent.js" type="text/javascript"></script>
 		<script src="birt/ajax/ui/report/AbstractBaseReportDocument.js" type="text/javascript"></script>
		<script src="birt/ajax/ui/report/BirtReportDocument.js" type="text/javascript"></script>

		<script src="birt/ajax/ui/dialog/AbstractBaseDialog.js" type="text/javascript"></script>
		<script src="birt/ajax/ui/dialog/AbstractParameterDialog.js" type="text/javascript"></script>
		<script src="birt/ajax/ui/dialog/BirtParameterDialog.js" type="text/javascript"></script>
		
		<SCRIPT SRC="birt/ajax/utility/BirtPosition.js" type="text/javascript"></script>

		<SCRIPT SRC="birt/ajax/core/BirtCommunicationManager.js" type="text/javascript"></script>
		<SCRIPT SRC="birt/ajax/core/BirtSoapRequest.js" type="text/javascript"></script>
		<SCRIPT SRC="birt/ajax/core/BirtSoapResponse.js" type="text/javascript"></script>
				
	</HEAD>

	<BODY CLASS="BirtViewer_Body" onload="Javascript:init()" LEFTMARGIN='0px' STYLE='overflow:hidden'>
		<!-- Header section -->
		<TABLE ID='layout' CELLSPACING='0' CELLPADDING='0' STYLE='width:100%;height:100%'>
		<%
			if ( fragment != null )
			{
				fragment.callBack( request, response );
			}
		%>
		</TABLE>
	</BODY>
	
	<script type="text/javascript">
	// <![CDATA[
		var hasSVGSupport = false;
		var useVBMethod = false;
		if ( navigator.mimeTypes != null && navigator.mimeTypes.length > 0 )
		{
		    if ( navigator.mimeTypes["image/svg+xml"] != null )
		    {
		        hasSVGSupport = true;
		    }
		}
		else
		{
		    useVBMethod = true;
		}
		
	// ]]>
	</script>
	
	<SCRIPT TYPE="text/vbscript">
		On Error Resume Next
		If useVBMethod = true Then
		    hasSVGSupport = IsObject(CreateObject("Adobe.SVGCtl"))
		End If
	</SCRIPT>

	<SCRIPT TYPE="text/javascript">
		var birtUtility = new BirtUtility( );
		var Mask =  new Mask( false );
		var BrowserUtility = new BrowserUtility( );
		var DragDrop = new BirtDndManager( );

		var birtProgressBar = new BirtProgressBar( 'progressBar' );
		var birtReportDocument = new BirtReportDocument( "Document" );
		
		<%
		if ( "/parameter".equalsIgnoreCase( request.getServletPath( ) ) )
		{
		%>
			var birtParameterDialog = new BirtParameterDialog( 'parameterDialog', 'parameter' );
		<%
		}
		else
		{
		%>
			var birtParameterDialog = new BirtParameterDialog( 'parameterDialog', 'run' );
		<%
		}
		%>
		
		function init( )
		{				
			birtParameterDialog.__cb_bind( );
		}
		
	</SCRIPT>
</HTML>

