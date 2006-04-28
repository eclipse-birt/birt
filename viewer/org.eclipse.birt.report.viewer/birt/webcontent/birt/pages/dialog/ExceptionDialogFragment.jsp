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
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />

<%-----------------------------------------------------------------------------
	Exception dialog fragment
-----------------------------------------------------------------------------%>
<TABLE CELLSPACING="2" CELLPADDING="2" CLASS="birtviewer_dialog_body">
	<TR HEIGHT="5px"><TD></TD></TR>
	<TR>
		<TD>
			<TABLE CELLSPACING="0" CELLPADDING="0" style="font:verdana;font-size:8pt">
				<TR>
					<TD VALIGN="top"><IMG SRC="birt/images/Error.gif" /></TD>
					<TD WIDTH="20px"></TD>
					<TD>
						<B><FONT STYLE='color:red'><SPAN ID='faultstring'></SPAN></FONT><B>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR>
		<TD>
			<TABLE CELLSPACING="0" CELLPADDING="0" style="font:verdana;font-size:8pt">
				<TR>
					<TD COLSPAN=3>
						<B>Stack Trace:</B><BR>
						<DIV style="height:100%;overflow:auto;height:200px;border-style:inset;border-width:1px">
							<FONT STYLE='color:red'><SPAN ID='faultdetail'></SPAN></FONT>
						</DIV>
					</TD>
				</TR>
			</TABLE>
		</TD>
	</TR>
	<TR HEIGHT="5px"><TD></TD></TR>
</TABLE>