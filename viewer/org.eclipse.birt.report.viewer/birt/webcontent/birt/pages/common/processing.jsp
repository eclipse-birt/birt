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
<%@ page import="org.eclipse.birt.report.resource.BirtResources" %>

<%-----------------------------------------------------------------------------
	Progress page
-----------------------------------------------------------------------------%>
<%
	boolean rtl = false;
	String rtlParam = request.getParameter("__rtl");
	if ( rtlParam != null )
	{
		rtl = Boolean.getBoolean(rtlParam);
	}
%>
<!DOCTYPE html>
<HTML>
	<HEAD>
		<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
		<LINK REL="stylesheet" HREF="<%= request.getContextPath( ) + "/webcontent/birt/styles/style.css" %>" TYPE="text/css">
	</HEAD>
	<BODY">
		<DIV ID="progressBar" ALIGN="center">
			<TABLE WIDTH="250px" CLASS="birtviewer_progresspage" CELLSPACING="10px">
				<TR>
					<TD ALIGN="center">
						<B>
							<%= BirtResources.getMessage( "birt.viewer.progressbar.prompt" )%>
						</B>
					</TD>
				</TR>
				<TR>
					<TD ALIGN="center">
						<IMG SRC="<%= request.getContextPath( ) + "/webcontent/birt/images/" + (rtl?"Loading_rtl":"Loading") + ".gif" %>" ALT="Progress Bar Image"/>
					</TD>
				</TR>
			</TABLE>
		</DIV>
	</BODY>
</HTML>		