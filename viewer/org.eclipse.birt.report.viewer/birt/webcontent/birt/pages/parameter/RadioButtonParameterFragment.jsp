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
<%@ page import="org.eclipse.birt.report.context.ScalarParameterBean,
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.utility.ParameterAccessor" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Radio button parameter control
-----------------------------------------------------------------------------%>
<%
	ScalarParameterBean parameterBean = ( ScalarParameterBean ) attributeBean.getParameterBean( );
	String encodedParameterName = ParameterAccessor.htmlEncode( parameterBean.getName( ) );
%>
<TR>
	<TD NOWRAP>
		<IMG SRC="birt/images/parameter.gif" ALT="<%= parameterBean.getDisplayName( ) %>" TITLE="<%= parameterBean.getToolTip( ) %>"/>
	</TD>
	<TD NOWRAP>
		<FONT TITLE="<%= parameterBean.getToolTip( ) %>"><%= parameterBean.getDisplayName( ) %>:</FONT>
		<%-- is required --%>
		<%
		if ( parameterBean.isRequired( ) )
		{
		%>
			<FONT COLOR="red">*</FONT>
		<%
		}
		%>
	</TD>
</TR>
<TR>
	<TD NOWRAP></TD>
	<%-- Parameter Name--%>

	<TD NOWRAP WIDTH="100%">
	<%-- Parameter control --%>
<%
	if (parameterBean.getSelectionList( ) != null)
	{
%>
	<INPUT TYPE="HIDDEN"
		ID="<%= encodedParameterName + "_hidden" %>"
		NAME="<%= ParameterAccessor.PARAM_ISNULL %>"
		VALUE="<%= (parameterBean.getValue( ) == null)? encodedParameterName : "" %>">
<%
		for ( int i = 0; i < parameterBean.getSelectionList( ).size( ); i++ )
		{
			String label = ( String ) parameterBean.getSelectionList( ).get( i );
			String value = ( String ) parameterBean.getSelectionTable( ).get( label );
%>
	<INPUT TYPE="RADIO"
		NAME="<%= encodedParameterName %>"
		ID="<%= encodedParameterName + i %>" 
		TITLE="<%= parameterBean.getToolTip( ) %>"
		VALUE="<%= ParameterAccessor.htmlEncode( value ) %>"
		<%= (parameterBean.getValue( ) != null && parameterBean.getValue( ).equalsIgnoreCase( value ) )? "CHECKED" : "" %>>
		<LABEL ID="<%= (encodedParameterName + i) + "_label" %>" FOR="<%= encodedParameterName + i %>"><%= label %></LABEL>
	</INPUT>
	<BR>
<%
		}
		if ( parameterBean.allowNull( ) )
		{
%>
	<INPUT TYPE="RADIO"
		NAME="<%= encodedParameterName %>"
		ID="<%= encodedParameterName + "_null" %>" 
		TITLE="<%= parameterBean.getToolTip( ) %>"
		VALUE=""
		<%= ( parameterBean.getValue( ) == null )? "CHECKED" : "" %>>
		<LABEL FOR="<%= encodedParameterName + "_null" %>">Null Value</LABEL>
	</INPUT>
	<BR>
<%
		}
	}
%>
	</TD>
</TR>