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
				 org.eclipse.birt.report.resource.BirtResources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<%-----------------------------------------------------------------------------
	Export report dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD><INPUT TYPE='radio' NAME="exportoption" VALUE="printoption1" CHECKED></TD>
		<TD><%= 
				BirtResources.getMessage( "birt.viewer.dialog.export.all" )
			%>
		</TD>
	</TR>
	<TR>
		<TD></TD>
		<TD><%= BirtResources.getMessage( "birt.viewer.dialog.export.all.detail" )%></TD>
	</TR>
	<TR>
		<TD><INPUT TYPE='radio' NAME="exportoption" VALUE="printoption2"></TD>
		<TD><%= BirtResources.getMessage( "birt.viewer.dialog.export.modified" )%></TD>
	</TR>
	<TR>
		<TD></TD>
		<TD><%= BirtResources.getMessage( "birt.viewer.dialog.export.modified.detail" )%></TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD></TD>
		<TD>
			<%= BirtResources.getMessage( "birt.viewer.dialog.export.format" )%>
			<SELECT NAME='format' STYLE='width:150px;height:25px;font-size:8pt'>
				<OPTION><%= BirtResources.getMessage( "birt.viewer.dialog.export.format.pdf" )%>
				<OPTION><%= BirtResources.getMessage( "birt.viewer.dialog.export.format.excel" )%>
			</SELECT>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>