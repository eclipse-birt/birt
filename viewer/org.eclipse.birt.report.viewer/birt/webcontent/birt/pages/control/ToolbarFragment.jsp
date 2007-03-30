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
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.resource.BirtResources,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.servlet.ViewerServlet" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%
	String pdfUrl = request.getContextPath( ) + "/run?"
		+ ParameterAccessor.getEncodedQueryString( request, ParameterAccessor.PARAM_FORMAT,	ParameterAccessor.PARAM_FORMAT_PDF );
	String ivUrl = request.getContextPath( ) + "/iv?"
		+ ParameterAccessor.getEncodedQueryString( request, null, null );
%>

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
					   <INPUT TYPE="image" NAME='toc' SRC="birt/images/Toc.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.toc" )%>"
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.toc" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='parameter' SRC="birt/images/Report_parameters.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.parameter" )%>"	
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.parameter" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='export' SRC="birt/images/Export.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.export" )%>"
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.export" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='exportReport' SRC="birt/images/ExportReport.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.exportreport" )%>"
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.exportreport" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='print' SRC="birt/images/Print.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.print" )%>"
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.print" )%>" CLASS="birtviewer_clickable">
					</TD>
					<%
					if( ParameterAccessor.isSupportedPrintOnServer )
					{
					%>					
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='printServer' SRC="birt/images/PrintServer.gif"
					   		TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.printserver" )%>"
					   		ALT="<%= BirtResources.getMessage( "birt.viewer.toolbar.printserver" )%>" CLASS="birtviewer_clickable">
					</TD>
					<%
					}
					%>										
					<TD ALIGN='right'>
					<%
					if ( !ViewerServlet.isOpenSource( ) )
					{
					%>
						<A HREF="<%= ivUrl %>">
							<IMG SRC='birt/images/Interactive_viewer.gif'
								CLASS='birtviewer_clickable'
								TITLE="<%= BirtResources.getMessage( "birt.viewer.toolbar.enableiv" )%>" />
						</A>
					<%
					}
					%>
					</TD>
					<TD WIDTH="6px"/>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>
