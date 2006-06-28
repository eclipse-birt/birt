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
<%@ page import="org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.context.ScalarParameterBean" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Text box parameter control
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
		<FONT TITLE="<%= parameterBean.getToolTip( ) %>"><LABEL FOR="<%= encodedParameterName %>"><%= parameterBean.getDisplayName( ) %>:</LABEL></FONT>
		<%-- is required --%>
		<%
		if ( parameterBean.isRequired( ) )
		{
		%>
			<FONT COLOR="red"><LABEL FOR="<%= encodedParameterName %>">*</LABEL></FONT>
		<%
		}
		%>
	</TD>
</TR>
<TR>
	<TD NOWRAP></TD>
	<TD NOWRAP WIDTH="100%">
<%
	if ( parameterBean.allowNull( ) )
	{
%>
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_hidden" %>"
			NAME="<%= ParameterAccessor.PARAM_ISNULL %>"
			VALUE="<%= (parameterBean.getValue( ) == null)? encodedParameterName : "" %>">
		
		<LABEL FOR="<%= encodedParameterName + "_radio_notnull" %>" CLASS="birtviewer_hidden_label">Input text</LABEL>	
		<INPUT TYPE="RADIO"
			ID="<%= encodedParameterName + "_radio_notnull" %>"
			VALUE="<%= encodedParameterName %>"
			<%= (parameterBean.getValue( ) != null)? "CHECKED" : "" %>>
<%
	}
%>

<%
	if ( !parameterBean.allowNull( ) && !parameterBean.allowBlank( ) )
	{
%>
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_default" %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getDefaultValue( ) == null )? "" : parameterBean.getDefaultValue( ) ) %>"
			>
<%
	}
%>
		<INPUT CLASS="BirtViewer_parameter_dialog_Input"
			TYPE="<%= parameterBean.isValueConcealed( )? "PASSWORD" : "TEXT" %>"
			NAME="<%= encodedParameterName %>"
			ID="<%= encodedParameterName %>" 
			TITLE="<%= parameterBean.getToolTip( ) %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getValue( ) == null )? "" : parameterBean.getValue( ) ) %>" 
			<%= ( parameterBean.allowNull( ) && parameterBean.getValue( ) == null )? "DISABLED='true'" : "" %>
            >

<%
	if ( parameterBean.allowNull( ) && !parameterBean.allowBlank( ) )
	{
%>
		<INPUT TYPE="HIDDEN"
			ID="isNotBlank" 
			NAME="<%= encodedParameterName %>"
			VALUE = "true">
<%
	}
%>            

<%
	if ( parameterBean.allowNull( ) )
	{
%>
		<BR>
		<LABEL FOR="<%= encodedParameterName + "_radio_null" %>" CLASS="birtviewer_hidden_label">Null Value</LABEL>	
		<INPUT TYPE="RADIO"
			ID="<%= encodedParameterName + "_radio_null"%>"
			VALUE="<%= encodedParameterName %>"
			<%= ( parameterBean.getValue( ) == null )? "CHECKED" : "" %>> Null Value
<%
	}
%>
	</TD>
</TR>