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
				 org.eclipse.birt.report.resource.ResourceConstants,
				 org.eclipse.birt.report.resource.BirtResources"  %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<%-----------------------------------------------------------------------------
	Exception dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR>
		<TD CLASS="birtviewer_exception_dialog">
			<TABLE CELLSPACING="2" CELLPADDING="2">
				<TR>
					<TD VALIGN="top"><IMG style="width:36px; height:36px;" src="birt/images/Error.png" /></TD>
					
					<TD>
					
						<TABLE CELLSPACING="2" CELLPADDING="4" CLASS="birtviewer_exception_dialog_container" >
							<TR>
								<TD>
								<DIV ID="faultStringContainer" CLASS="birtviewer_exception_dialog_message">
									<B><SPAN ID='faultstring'></SPAN><B>
								</DIV>
								</TD>
							</TR>
							<TR>
								<TD>
									<DIV ID="showTraceLabel" CLASS="birtviewer_exception_dialog_label"><%= BirtResources.getMessage( ResourceConstants.EXCEPTION_DIALOG_SHOW_STACK_TRACE ) %></DIV>
									<DIV ID="hideTraceLabel" CLASS="birtviewer_exception_dialog_label" style="display:none"><%= BirtResources.getMessage( ResourceConstants.EXCEPTION_DIALOG_HIDE_STACK_TRACE ) %></DIV>
									<div id="copyTraceToClipBoard" CLASS="birtviewer_exception_dialog_copy_label"><img style="height:16px;margin-right:4px;" src="birt/images/DataCopy.png" title="<%= BirtResources.getMessage( ResourceConstants.ERROR_STACK_TRACE_COPY ) %>">
									<em id="copyTraceLabel"><%= BirtResources.getMessage( ResourceConstants.ERROR_STACK_TRACE_COPY ) %></em></img></div>
								</TD>
							</TR>
							<TR>
								<TD>
									<DIV ID="exceptionTraceContainer" STYLE="display:none">
										<TABLE WIDTH="100%">
											<TR>
												<TD>
													<%= 
														BirtResources.getMessage( ResourceConstants.EXCEPTION_DIALOG_STACK_TRACE )
													%><BR>
												</TD>
											</TR>
											<TR>
												<TD>
													<DIV CLASS="birtviewer_exception_dialog_detail">
														<SPAN ID='faultdetail'></SPAN>
													</DIV>
												</TD>
											</TR>											
										</TABLE>
									</DIV>
								</TD>
							</TR>	
						</TABLE>
					
					</TD>
					
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>