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
	Combo box parameter control
-----------------------------------------------------------------------------%>
<%
	ScalarParameterBean parameterBean = ( ScalarParameterBean ) attributeBean.getParameterBean( );
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
	<TD NOWRAP WIDTH="100%">
		<INPUT TYPE="HIDDEN"
			ID="<%= parameterBean.getName( ) + "_value" %>"
			NAME="<%= parameterBean.getName( ) %>"
			VALUE="<%= ParameterAccessor.htmlEncode( parameterBean.getValue( ) )%>">
<%
	if (parameterBean.allowNull())
	{
%>
		<INPUT TYPE="HIDDEN"
			ID="<%= parameterBean.getName( ) + "_hidden" %>"
			NAME="<%= ParameterAccessor.PARAM_ISNULL %>"
			VALUE="<%= ( parameterBean.getValue( ) == null )? parameterBean.getName( ) : "" %>">
<%
	}
	
	boolean CHECKED = false;
	if ( parameterBean.allowNewValues( ) ) // TODO: Editable
	{
%>
		<INPUT TYPE="RADIO"
			ID="<%= parameterBean.getName( ) + "_radio_selection" %>"
			VALUE="<%= parameterBean.getName( ) %>"
			<%= ( parameterBean.isValueInList( ) || ( parameterBean.allowNull( ) && parameterBean.getValue( ) == null) )? "CHECKED" : "" %>>

			<% CHECKED = ( parameterBean.isValueInList( ) || ( parameterBean.allowNull( ) && parameterBean.getValue( ) == null) ); %>
<%
	}
%>
		<SELECT ID="<%= parameterBean.getName( ) + "_selection"%>"
			TITLE="<%= parameterBean.getToolTip( ) %>"
			CLASS="birtviewer_parameter_dialog_Select"
			>
<%
	if ( parameterBean.getSelectionList( ) != null )
	{
%>
	<OPTION></OPTION>
<%	
		for ( int i = 0; i < parameterBean.getSelectionList( ).size( ); i++ )
		{
			String label = ( String ) parameterBean.getSelectionList( ).get( i );
			String value = ( String ) parameterBean.getSelectionTable( ).get( label );
			if (parameterBean.getValue( ) != null && parameterBean.getValue( ).equalsIgnoreCase( value ) )
			{
%>
			<OPTION VALUE="<%= ParameterAccessor.htmlEncode( value ) %>" SELECTED><%= label %></OPTION>
<%
			}
			else
			{
%>
			<OPTION VALUE="<%= ParameterAccessor.htmlEncode( value ) %>"><%= label %></OPTION>
<%
			}
		}
	}
	if ( parameterBean.allowNull( ) )
	{
%>
			<OPTION VALUE="" <%= ( parameterBean.getValue( ) == null )? "SELECTED" : ""%>>Null Value</OPTION>
<%
	}
%>
		</SELECT>
<%
	if ( parameterBean.allowNewValues( ) ) // TODO: editable.
	{
%>
		<BR>
		<INPUT TYPE="RADIO"
			ID="<%= parameterBean.getName( ) + "_radio_input"%>"
			VALUE="<%= parameterBean.getName( ) %>"
			<%= ( !parameterBean.isValueInList( ) && parameterBean.getValue( ) != null  || !CHECKED )? "CHECKED" : "" %>>
		<INPUT CLASS="BirtViewer_parameter_dialog_Input"
			TYPE="<%= parameterBean.isValueConcealed( )? "PASSWORD" : "TEXT" %>"
			TITLE="<%= parameterBean.getToolTip( ) %>"
			ID="<%= parameterBean.getName( ) + "_input"%>"
			<%= ( !parameterBean.isValueInList( ) && parameterBean.getValue( ) != null )? "VALUE=\"" + ParameterAccessor.htmlEncode( parameterBean.getValue( ) ) + "\"": "" %>
			>
	<%
		if ( !parameterBean.allowBlank( ) )
		{
	%>
			<INPUT TYPE="HIDDEN"
				ID="<%= parameterBean.getName( ) + "_notblank" %>" 
				NAME="<%= parameterBean.getName( ) %>"
				VALUE = "true">
	<%
		}
	%>     			
<%
	}
%>
	</TD>
</TR>