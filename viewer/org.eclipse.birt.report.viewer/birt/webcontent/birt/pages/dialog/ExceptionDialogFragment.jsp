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
		<TD style="border-style:solid;border-color:#cccccc;background-color:#ffffef;border-width:1px">
			<TABLE CELLSPACING="2" CELLPADDING="2">
				<TR>
					<TD VALIGN="top"><IMG SRC="birt/images/Error.gif" /></TD>
					
					<TD>
					
						<TABLE CELLSPACING="2" CELLPADDING="4" style="border-left-style:solid;border-left-width:1px;border-left-color:#cccccc" >
							<TR>
								<TD>
									<B><SPAN ID='faultstring'></SPAN><B>
								</TD>
							</TR>
						<!--	
							<TR>
								<TD>
									<%= 
										BirtResources.getMessage( ResourceConstants.EXCEPTION_DIALOG_STACK_TRACE )
									%><BR>
								</TD>
							</TR>
							<TR>
								<TD>
									<DIV style="padding:2px;width:100%;overflow:auto;height:150px;border-top-color:#cccccc;border-top-style:solid;border-top-width:1px;font-size:8pt">
										<SPAN ID='faultdetail'></SPAN>
									</DIV>
								</TD>
							</TR>
						-->
						</TABLE>
					
					</TD>
					
				</TR>
			</TABLE>
		</TD>
	</TR>
</TABLE>