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
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.resource.BirtResources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Navigation bar fragment
-----------------------------------------------------------------------------%>
<TR 
	<%
		if( attributeBean.isShowNavigationbar( ) )
		{
	%>
		HEIGHT="25px"
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
	<TD>
		<DIV id="navigationBar">
			<TABLE CELLSPACING="0" CELLPADDING="0" WIDTH="100%" HEIGHT="25px" CLASS="birtviewer_navbar">
				<TR><TD></TD></TR>
				<TR>
					<TD WIDTH="6px"/>
					<TD>
						<B>
						<%
							if ( attributeBean.getBookmark( ) != null )
							{
						%>
							<%= 
								BirtResources.getMessage( "birt.viewer.navbar.prompt.one" )
							%>&nbsp;
							<SPAN ID='pageNumber'></SPAN>&nbsp;
							<%= BirtResources.getMessage( "birt.viewer.navbar.prompt.two" )%>&nbsp;
							<SPAN ID='totalPage'></SPAN>
						<%
							}
							else
							{
						%>
							<%= BirtResources.getMessage( "birt.viewer.navbar.prompt.one" )%>&nbsp;
							<SPAN ID='pageNumber'><%= ""+attributeBean.getReportPage( ) %></SPAN>&nbsp;
							<%= BirtResources.getMessage( "birt.viewer.navbar.prompt.two" )%>&nbsp;
							<SPAN ID='totalPage'></SPAN>
						<%
							}
						%>
						</B>
					</TD>
					
					<TD WIDTH="15px">
						<INPUT TYPE="image" SRC="birt/images/FirstPage_disabled.gif" NAME='first'
							ALT="<%= BirtResources.getMessage( "birt.viewer.navbar.first" )%>" 
							TITLE="<%= BirtResources.getMessage( "birt.viewer.navbar.first" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" SRC="birt/images/PreviousPage_disabled.gif" NAME='previous' 
							ALT="<%= BirtResources.getMessage( "birt.viewer.navbar.previous" )%>" 
							TITLE="<%= BirtResources.getMessage( "birt.viewer.navbar.previous" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" SRC="birt/images/NextPage_disabled.gif" NAME='next'
						    ALT="<%= BirtResources.getMessage( "birt.viewer.navbar.next" )%>" 
							TITLE="<%= BirtResources.getMessage( "birt.viewer.navbar.next" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" SRC="birt/images/LastPage_disabled.gif" NAME='last'
						    ALT="<%= BirtResources.getMessage( "birt.viewer.navbar.last" )%>"
							TITLE="<%= BirtResources.getMessage( "birt.viewer.navbar.last" )%>" CLASS="birtviewer_clickable">
					</TD>
					
					<TD WIDTH="8px"/>
					
					<TD ALIGN="right" WIDTH="80px"><LABEL for="gotoPage"><b><%= BirtResources.getMessage( "birt.viewer.navbar.lable.goto" )%></b></LABEL></TD>
					<TD WIDTH="2px"/>
					<TD ALIGN="right" WIDTH="50px">
						<INPUT ID='gotoPage' TYPE='text' VALUE='' MAXLENGTH="8" SIZE='5' CLASS="birtviewer_navbar_input">
					</TD>
					<TD WIDTH="4px"/>
					<TD ALIGN="right" WIDTH="10px">
						<INPUT TYPE="image" SRC="birt/images/Go.gif" NAME='goto'
						    ALT="<%= BirtResources.getMessage( "birt.viewer.navbar.goto" )%>" 
							TITLE="<%= BirtResources.getMessage( "birt.viewer.navbar.goto" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>

<script type="text/javascript"> 
// <![CDATA[
	// Error msgs
	Constants.error.invalidPageNumber = '<%= BirtResources.getMessage( "birt.viewer.navbar.error.blankpagenum" )%>';
// ]]>
</script>