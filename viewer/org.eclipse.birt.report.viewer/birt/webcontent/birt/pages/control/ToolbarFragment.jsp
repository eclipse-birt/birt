<%-----------------------------------------------------------------------------
	Copyright (c) 2004, 2025 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v2.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-2.0.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.resource.BirtResources,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.servlet.ViewerServlet" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Toolbar fragment
-----------------------------------------------------------------------------%>
<TR 
	<%
		if( attributeBean.isShowToolbar( ) )
		{
	%>
		HEIGHT="20px"
	<%
		}
		else
		{
	%>
		style="display:none"
	<%
		}
	%>	
>
	<TD COLSPAN='2'>
		<DIV ID="toolbar">
			<TABLE CELLSPACING="1px" CELLPADDING="1px" WIDTH="100%" CLASS="birtviewer_toolbar">
				<TR><TD></TD></TR>
				<TR>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='toc' SRC="birt/images/ReportToc.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='parameter' SRC="birt/images/ReportParameters.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>"	
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='export' SRC="birt/images/ReportCsv.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='exportReport' SRC="birt/images/ReportExport.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='print' SRC="birt/images/Print.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<%
					if( ParameterAccessor.isSupportedPrintOnServer )
					{
					%>					
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='printServer' SRC="birt/images/PrintServer.png"
								TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver" )%>"
								ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<%
					}
					%>										
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT id="previewLayoutButton" TYPE="image" NAME="previewLayout" SRC="birt/images/PreviewPageLayout.png" VALUE="page"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							PAGE_TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							PAGE_ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							HTML_TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.html" )%>"
							HTML_ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.html" )%>"
							CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD ALIGN='right'>
						<div class="navbar-toggle-right">
						   <div id="toggle-button-frame" class="toggle-frame" onclick="birtToolbarTheme.toggleSwitch('toggleButton')">
						      <div class="toggle-track" tabindex="-1">
						         <div class="toggle-track-dark"><span class="toggle-icon-left">🌜</span></div>
						         <div class="toggle-track-light"><span class="toggle-icon-right">🌞</span></div>
						         <div id="toggle-track-icon" class="toggle-track-icon"></div>
						      </div>
						      <input id="toggle-track-checkbox" type="checkbox" class="toggle-track-checkbox" aria-label="Switch between dark and light mode">
						   </div>
						</div>
						<script>
							var birtToolbarTheme = new BirtToolbarTheme();
							birtToolbarTheme.initToggle();
						</script>
					</TD>
					<TD WIDTH="6px"/>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>
