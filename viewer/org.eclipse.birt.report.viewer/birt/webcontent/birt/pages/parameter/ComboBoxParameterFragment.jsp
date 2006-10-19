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
				 org.eclipse.birt.report.IBirtConstants,
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
	String encodedParameterName = ParameterAccessor.htmlEncode( parameterBean.getName( ) );
	String paramValue = parameterBean.getValue( );
	String defaultValue = parameterBean.getDefaultValue( );
%>
<TR>
	<TD NOWRAP>
		<IMG SRC="birt/images/parameter.gif" ALT="<%= parameterBean.getDisplayName( ) %>" TITLE="<%= parameterBean.getToolTip( ) %>"/>
	</TD>
	<TD NOWRAP>
		<FONT TITLE="<%= parameterBean.getToolTip( ) %>"><LABEL FOR="<%= encodedParameterName + "_selection"%>"><%= parameterBean.getDisplayName( ) %>:</LABEL></FONT>
		<%-- is required --%>
		<%
		if ( parameterBean.isRequired( ) )
		{
		%>
			<FONT COLOR="red"><LABEL FOR="<%= encodedParameterName + "_selection"%>">*</LABEL></FONT>
		<%
		}
		%>
	</TD>
</TR>
<TR>
	<TD NOWRAP></TD>
	<TD NOWRAP WIDTH="100%">
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_value" %>"
			NAME="<%= encodedParameterName %>"
			VALUE="<%= ParameterAccessor.htmlEncode( paramValue )%>">
<%
	if (parameterBean.allowNull())
	{
%>
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_hidden" %>"
			NAME="<%= ParameterAccessor.PARAM_ISNULL %>"
			VALUE="<%= ( paramValue == null )? encodedParameterName : "" %>">
<%
	}
	
	boolean CHECKED = true;
		
	if ( parameterBean.allowNewValues( ) ) // TODO: Editable
	{
		CHECKED = parameterBean.isValueInList( ) 
				  || paramValue == null
				  || ( !parameterBean.isValueInList( ) && defaultValue != null && defaultValue.equals( paramValue ) );		
%>
		<LABEL FOR="<%= encodedParameterName + "_radio_selection" %>" CLASS="birtviewer_hidden_label">Select</LABEL>
		<INPUT TYPE="RADIO"
			NAME="<%= encodedParameterName + "_radios" %>" 
			ID="<%= encodedParameterName + "_radio_selection" %>" 
			VALUE="<%= encodedParameterName %>"
			<%= CHECKED ? "CHECKED" : "" %> >
<%
	}
%>
		<SELECT ID="<%= encodedParameterName + "_selection"%>"
			TITLE="<%= parameterBean.getToolTip( ) %>"
			CLASS="birtviewer_parameter_dialog_Select" 
			<%= !CHECKED ? "DISABLED='true'" : "" %> >
<%
	if ( parameterBean.getSelectionList( ) != null )
	{
		if( parameterBean.isCascade( ) )
		{
%>
		<OPTION></OPTION>
<%
		}
		
		if ( defaultValue != null && !parameterBean.isDefaultValueInList( ) ) // Add default value in Combo Box
		{
%>
			<OPTION VALUE="<%= ParameterAccessor.htmlEncode( defaultValue ) %>" 
				<%= CHECKED && !parameterBean.isValueInList( ) ? "SELECTED" : "" %> > <%= ParameterAccessor.htmlEncode( defaultValue ) %></OPTION>
<%	
		}
		
		for ( int i = 0; i < parameterBean.getSelectionList( ).size( ); i++ )
		{
			String label = ( String ) parameterBean.getSelectionList( ).get( i );
			String value = ( String ) parameterBean.getSelectionTable( ).get( label );

			if ( paramValue != null && paramValue.equals( value ) )
			{
%>
			<OPTION VALUE="<%= ParameterAccessor.htmlEncode( value ) %>" SELECTED><%= ParameterAccessor.htmlEncode( label ) %></OPTION>
<%
			}
			else
			{
%>
			<OPTION VALUE="<%= ParameterAccessor.htmlEncode( value ) %>"><%= ParameterAccessor.htmlEncode( label ) %></OPTION>
<%
			}
		}
	}
	if ( parameterBean.allowNull( ) )
	{
%>
		<OPTION VALUE="" <%= ( paramValue == null )? "SELECTED" : ""%> >Null Value</OPTION>
<%
	}
%>
		</SELECT>
<%
	if ( parameterBean.allowNewValues( ) ) // TODO: editable.
	{
%>
		<BR>
		<LABEL FOR="<%= encodedParameterName + "_radio_input" %>" CLASS="birtviewer_hidden_label">Input</LABEL>
		<INPUT TYPE="RADIO"
			NAME="<%= encodedParameterName + "_radios" %>" 
			ID="<%= encodedParameterName + "_radio_input"%>" 
			VALUE="<%= encodedParameterName %>"
			<%= !CHECKED ? "CHECKED" : "" %> >
			
		<LABEL FOR="<%= encodedParameterName + "_input" %>" CLASS="birtviewer_hidden_label">Input text</LABEL>
		<INPUT CLASS="BirtViewer_parameter_dialog_Input"
			TYPE="<%= parameterBean.isValueConcealed( )? "PASSWORD" : "TEXT" %>"
			TITLE="<%= parameterBean.getToolTip( ) %>"
			ID="<%= encodedParameterName + "_input"%>"
			<%= !CHECKED && paramValue != null ? "VALUE=\"" + ParameterAccessor.htmlEncode( paramValue ) + "\"": "" %> 
			<%= CHECKED ? "DISABLED='true'" : "" %>	>
	<%
		if ( !parameterBean.allowBlank( ) )
		{
	%>
			<INPUT TYPE="HIDDEN"
				ID="isNotBlank"  
				NAME="<%= encodedParameterName %>"
				VALUE = "true">
	<%
		}
	}

	if ( parameterBean.isCascade( ) )
	{
	%>
		<INPUT TYPE="HIDDEN" NAME="<%=IBirtConstants.IS_CASCADE%>" VALUE="true"/>
	<%
	}
	%>
	</TD>
</TR>