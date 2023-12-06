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
<%@ page import="java.util.Iterator,
				 java.util.Collection,
 				 org.eclipse.birt.report.utility.ParameterAccessor,
 				 org.eclipse.birt.report.context.ParameterGroupBean,
				 org.eclipse.birt.report.context.BaseAttributeBean,
 				 org.eclipse.birt.report.presentation.aggregation.IFragment" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragments" type="java.util.Collection" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Content fragment
-----------------------------------------------------------------------------%>
<%
	ParameterGroupBean parameterGroupBean = ( ParameterGroupBean ) attributeBean.getParameterBean( );
%>
<TR><TD HEIGHT="24px" COLSPAN="2"></TD></TR>
<%
	if ( parameterGroupBean.getDisplayName( ) != null )
	{
%>
<TR>
	<TD NOWRAP>
		<IMG class="icon_parameter_group" SRC="birt/images/ParameterGroup.png" ALT="<%= parameterGroupBean.getDisplayName( ) %>" TITLE="<%= parameterGroupBean.getToolTip( ) %>"/>
	</TD>
	<TD NOWRAP>
		<SPAN style="font-weight:bold;" TITLE="<%= parameterGroupBean.getToolTip( ) %>"><%= parameterGroupBean.getDisplayName( ) %></SPAN>
	</TD>
</TR>
<%
	}
%>
<TR>
<%
	if ( parameterGroupBean.getDisplayName( ) != null )
	{
%>	
	<TD NOWRAP></TD>
	<TD NOWRAP>
<%
	}
	else
	{
%>	
	<TD COLSPAN="2" NOWRAP>
<%
	}
%>	
		<TABLE CLASS="birtviewer_parameter_dialog_Label">
		<%
			if ( fragments != null )
			{
				Iterator childIterator = fragments.iterator( );
				while ( childIterator.hasNext( ) )
				{
				    IFragment subfragment = ( IFragment ) childIterator.next( );
					if ( subfragment != null )
					{
						subfragment.service( request, response );
					}
				}
			}
		%>
		</TABLE>
	</TD>
</TR>