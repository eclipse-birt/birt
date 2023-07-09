<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v2.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-2.0.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.context.ScalarParameterBean,
				 org.eclipse.birt.report.service.api.ParameterDefinition"
				 %>

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
		<IMG class="icon_parameter" SRC="birt/images/Parameter.png" ALT="<%= parameterBean.getDisplayName( ) %>" TITLE="<%= parameterBean.getToolTip( ) %>"/>
	</TD>
	<TD NOWRAP>
		<SPAN TITLE="<%= parameterBean.getToolTip( ) %>"><LABEL FOR="<%= encodedParameterName %>"><%= parameterBean.getDisplayName( ) %>:</LABEL></SPAN>
		<%-- is required --%>
		<%
		if ( parameterBean.isRequired( ) )
		{
		%>
			<SPAN style="color:red;"><LABEL FOR="<%= encodedParameterName %>">*</LABEL></SPAN>
		<%
		}
		%>
	</TD>
</TR>
<TR>
	<TD NOWRAP></TD>
	<TD NOWRAP WIDTH="100%">
		<INPUT TYPE="HIDDEN" ID="control_type" VALUE="text">
		<INPUT TYPE="HIDDEN" ID="data_type" VALUE="<%="" + parameterBean.getParameter( ).getDataType( ) %>">
		<INPUT CLASS="BirtViewer_parameter_dialog_Input" 
			category="<%= parameterBean.getParameter().getCategory()%>"
			pattern="<%= parameterBean.getParameter().getPattern()%>"
			
			<%
				if (parameterBean.getParameter().getCategory().equals("Date Picker")					) {
			%>
			TYPE="date"
			<%
				} else if (		parameterBean.getParameter().getDataType() == ParameterDefinition.TYPE_DATE_TIME
							&&	parameterBean.getParameter().getCategory().startsWith("Date Picker")	) {
			%>
			TYPE="datetime-local"
				<% if (parameterBean.getParameter().getCategory().contains("Medium Time")) { %>
					step="1"
				<%} %>
			<%
				} else if (		parameterBean.getParameter().getDataType() == ParameterDefinition.TYPE_TIME
							&&	parameterBean.getParameter().getCategory().startsWith("Time Picker")	) {
			%>
			TYPE="time"
				<% if (parameterBean.getParameter().getCategory().contains("Medium Time")) { %>
					step="1"
				<%} %>
			<%
				} else if ( parameterBean.isValueConcealed() ) { 
			%>
			TYPE="password" 
			<%
				} else {
			%>
			TYPE="text"
			<%
				}
			%>
			NAME="<%= encodedParameterName %>"
			ID="<%= encodedParameterName %>" 
			TITLE="<%= parameterBean.getToolTip( ) %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getDisplayText( ) == null ) ? "" : parameterBean.getDisplayText( ) ) %>" 
			<%= ( !parameterBean.isRequired( ) && parameterBean.getValue( ) == null )? "DISABLED='true'" : "" %>
			<%= parameterBean.isRequired( ) ? "aria-required='true'" : "" %>
            >
		<%
		if ( !parameterBean.isRequired( ) )
		{
		%>
		<INPUT TYPE="CHECKBOX"
			ID="<%= encodedParameterName + "_checkbox"%>"
			VALUE="<%= encodedParameterName %>"
			<%= ( parameterBean.getValue( ) == null )? "CHECKED" : "" %>
			REQUIRED=true
			>
		<LABEL FOR="<%= encodedParameterName + "_checkbox"%>" CLASS="birtviewer_label">Null Value</LABEL>
		<%
		}
		%>

		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_value" %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getValue( ) == null )? "" : parameterBean.getValue( ) ) %>"
			>		
		<INPUT TYPE="HIDDEN"
			ID="<%= encodedParameterName + "_displayText" %>"
			VALUE="<%= ParameterAccessor.htmlEncode( ( parameterBean.getDisplayText( ) == null )? "" : parameterBean.getDisplayText( ) ) %>"
			>
		<INPUT TYPE="HIDDEN" ID="isRequired" 
			VALUE = "<%= parameterBean.isRequired( )? "true": "false" %>"
			>
	</TD>
</TR>