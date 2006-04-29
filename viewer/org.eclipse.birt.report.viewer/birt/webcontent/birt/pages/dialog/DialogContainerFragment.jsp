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
				 org.eclipse.birt.report.IBirtConstants,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.resource.BirtResources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Dialog container fragment, shared by all standard dialogs.
-----------------------------------------------------------------------------%>
<div id="<%= fragment.getClientId( ) %>" class="dialogBorder" style="display:none;position:absolute;z-index:220">
	<iframe id="<%= fragment.getClientId( ) %>iframe"  name="<%= fragment.getClientId( ) %>iframe" style="z-index:-1; display: none; left:0px; top:0px;
					 background-color: #ff0000; opacity: .0; filter: alpha(opacity = 0); position: absolute;" frameBorder="0" scrolling="no">
	</iframe>	
	<div id="<%= fragment.getClientId( ) %>dialogTitleBar" class="dialogTitleBar dTitleBar">
		<div class="dTitleTextContainer">
			<table style="width: 100%; height: 100%;">
				<tr>
					<td class="dialogTitleText dTitleText">
						<%= fragment.getClientName( ) %>
					</td>
				</tr>
			</table>
		</div>
		<div class="dialogCloseBtnContainer dCloseBtnContainer">
			<table style="width: 100%; height: 100%; border-collapse: collapse">
				<tr>
					<td>
						<div id="<%= fragment.getClientId( ) %>dialogCloseBtn" class="dialogCloseBtn dCloseBtn"/>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<!-- overflow is set as workaround for Mozilla bug https://bugzilla.mozilla.org/show_bug.cgi?id=167801 -->		
	<div  class="dialogBackground dBackground" style="overflow: auto;"> 
		<div class="dialogContentContainers" id="<%= fragment.getClientId( ) %>dialogContentContainer">
			<%
				if ( fragment != null )
				{
					fragment.callBack( request, response );
				}
			%>
		</div>
		<div class="dialogBtnBarContainer">
			<div>
				<div class="dBtnBarDividerTop">
				</div>
				<div class="dBtnBarDividerBottom">
				</div>
			</div>
			<div class="dialogBtnBar">
				<table style="width: 100%; height: 100%;">
					<tr>
						<td class="dBtnBarCell">
							<%	
								String buttonName = "ok";								
								if ( IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase( request.getServletPath() ) 
								 	|| ( IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase( request.getServletPath() ) 
										&& ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( attributeBean.getFormat() ) ) )
								{
									buttonName = "okRun";
								}
							%>
						
							<input type='button' name='<%=buttonName%>' value="<%= BirtResources.getString( "birt.viewer.dialog.ok" )%>" class="dialogBtnActive"></input>
							<input type='button' name='cancel' value="<%= BirtResources.getString( "birt.viewer.dialog.cancel" )%>" class="dialogBtnActive"></input>
						</td>
					</tr>
				</table>				
			</div>
		</div>
	</div>
</div>