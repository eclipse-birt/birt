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
	Export data dialog fragment
-----------------------------------------------------------------------------%>
<TABLE ID="simpleExportDialogBody" CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
			<%= BirtResources.getString( "birt.viewer.dialog.exportdata.resultsets" )%>
		</TD>
	</TR>
	<TR>
		<TD COLSPAN="3">
			<SELECT ID="resultsets" CLASS="birtviewer_exportdata_dialog_single_select">
			</SELECT>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD VALIGN="top">
			<TABLE STYLE="font-size:8pt;">
				<TR><TD>
					<%= BirtResources.getString( "birt.viewer.dialog.exportdata.availablecolumn" )%>
				</TD></TR>
				<TR><TD>
					<SELECT ID="availableColumnSelect" SIZE="10" CLASS="birtviewer_exportdata_dialog_select">
					</SELECT>
				</TD></TR>
			</TABLE>
		</TD>
		<TD VALIGN="middle">
			<TABLE HEIGHT="100%">
				<TR><TD>
					<TABLE VALIGN="middle">
						<TR><TD>
							<INPUT TYPE="image" NAME="Addall" SRC="birt/images/AddAll.gif" CLASS="birtviewer_exportdata_dialog_button">
						</TD></TR>
						<TR height="2px"><TD></TD></TR>
						<TR><TD>
							<INPUT TYPE="image" NAME="Add" SRC="birt/images/Add.gif" CLASS="birtviewer_exportdata_dialog_button">
						</TD></TR>
						<TR height="2px"><TD></TD></TR>
						<TR><TD>
							<INPUT TYPE="image" NAME="Remove" SRC="birt/images/Remove_disabled.gif" CLASS="birtviewer_exportdata_dialog_button">
						</TD></TR>
						<TR height="2px"><TD></TD></TR>
						<TR><TD>
							<INPUT TYPE="image" NAME="Removeall" SRC="birt/images/RemoveAll_disabled.gif" CLASS="birtviewer_exportdata_dialog_button">
						</TD></TR>
					</TABLE>
				</TD></TR>
			</TABLE>
		</TD>
		<TD >
			<TABLE STYLE="font-size:8pt;">
				<TR><TD>
					<%= BirtResources.getString( "birt.viewer.dialog.exportdata.selectedcolumn" )%>
				</TD></TR>
				<TR><TD>
					<SELECT ID="selectedColumnSelect" SIZE="10" CLASS="birtviewer_exportdata_dialog_select">
					</SELECT>
				</TD></TR>
			</TABLE>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD COLSPAN="3" STYLE="font-size:7pt">
			<%= BirtResources.getString( "birt.viewer.dialog.exportdata.format" )%>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>