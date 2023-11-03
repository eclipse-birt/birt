<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v2.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-2.0.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page session="false" buffer="none"%>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.resource.BirtResources"%>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<%
	String[] supportedFormats = ParameterAccessor.supportedFormats;
%>
<%-----------------------------------------------------------------------------
	Export report dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
			<table>
				<tr>
					<td>
						<label for="exportFormat"><%=BirtResources.getMessage( "birt.viewer.dialog.export.format" )%></label>
						<select	id="exportFormat" name="format" class="birtviewer_exportreport_dialog_select">
							<%
								ParameterAccessor.sortSupportedFormatsByDisplayName(supportedFormats);
								
								for ( int i = 0; i < supportedFormats.length; i++ )
								{
									if ( !ParameterAccessor.PARAM_FORMAT_HTML.equalsIgnoreCase( supportedFormats[i] ) )
									{
							%>
										<OPTION VALUE="<%= supportedFormats[i] %>"><%= ParameterAccessor.getOutputFormatLabel( supportedFormats[i] ) %></OPTION>
							<%
									}
								}
							%>
						</select>
					</td>
					<td STYLE="padding-left:5px">	
						<div id="exportExcelSingleSheet">
							<input type="checkbox" id="exportExcelSingleSheetCheckbox"/>
							<label id="exportExcelSingleSheetLabel" for="exportExcelSingleSheetCheckbox"><%=BirtResources.getMessage( "birt.viewer.dialog.export.spudsoft.excel.single.sheet" )%></label>
						</div>
					</td>
				</tr>
			</table>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR HEIGHT="5px"><TD><HR/></TD></TR>
	<TR>
		<TD>
		<label for="exportPages"><%=BirtResources.getMessage( "birt.viewer.dialog.page" )%></label>
		</TD>
	</TR>
	<TR>
		<TD>
			<DIV ID="exportPageSetting">
				<TABLE>
					<TR>
						<TD>
							<INPUT TYPE="radio" ID="exportPageAll" NAME="exportPages" CHECKED/>
							<label for="exportPageAll"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.page.all" )%></label>
						</TD>
						<TD STYLE="padding-left:5px">	
							<INPUT TYPE="radio" ID="exportPageCurrent" NAME="exportPages"/>
							<label for="exportPageCurrent"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.page.current" )%></label>
						</TD>	
						<TD STYLE="padding-left:5px">
							<INPUT TYPE="radio" ID="exportPageRange" NAME="exportPages"/>
							<label for="exportPageRange"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.page.range" )%></label>
							<INPUT TYPE="text" CLASS="birtviewer_exportreport_dialog_input" ID="exportPageRange_input" DISABLED="true"/>
						</TD>
					</TR>		
				</TABLE>
			</DIV>
		</TD>
	</TR>
	<TR>
		<TD>&nbsp;&nbsp;<%=BirtResources.getHtmlMessage( "birt.viewer.dialog.page.range.description" )%></TD>
	</TR>
	<TR HEIGHT="5px"><TD><HR/></TD></TR>
	<TR>
		<TD>
			<DIV ID="exportFitSetting">
				<TABLE>
					<TR>
						<TD>
							<label for="exportFitToAuto"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.export.pdf.fitto" )%></label>
						</TD>
					</TR>
					<TR>
						<TD>
							<INPUT TYPE="radio" ID="exportFitToAuto" NAME="exportFit" CHECKED/>
							<label for="exportFitToAuto"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.export.pdf.fittoauto" )%></label>
						</TD>
						<TD>
							<INPUT TYPE="radio" ID="exportFitToActual" NAME="exportFit"/>
							<label for="exportFitToActual"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.export.pdf.fittoactual" )%></label>
						</TD>
						<TD STYLE="padding-left:5px">	
							<INPUT TYPE="radio" ID="exportFitToWhole" NAME="exportFit"/>
							<label for="exportFitToWhole"><%=BirtResources.getHtmlMessage( "birt.viewer.dialog.export.pdf.fittowhole" )%></label>
						</TD>
					</TR>
				</TABLE>			
			</DIV>			
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>
