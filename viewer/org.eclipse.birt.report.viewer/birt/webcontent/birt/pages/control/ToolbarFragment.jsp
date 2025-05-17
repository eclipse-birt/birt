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
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.resource.BirtResources,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.servlet.ViewerServlet" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Toolbar fragment
-----------------------------------------------------------------------------%>
<TR 
	<%
		if( attributeBean.isShowToolbar( ) )
		{
	%>
		HEIGHT="20px"
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
	<TD COLSPAN='2'>
		<DIV ID="toolbar">
			<TABLE CELLSPACING="1px" CELLPADDING="1px" WIDTH="100%" CLASS="birtviewer_toolbar">
				<TR><TD></TD></TR>
				<TR>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='toc' SRC="birt/images/ReportToc.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.toc" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='parameter' SRC="birt/images/ReportParameters.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>"	
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='export' SRC="birt/images/ReportCsv.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.export" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='exportReport' SRC="birt/images/ReportExport.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='print' SRC="birt/images/Print.png"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<%
					if( ParameterAccessor.isSupportedPrintOnServer )
					{
					%>					
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT TYPE="image" NAME='printServer' SRC="birt/images/PrintServer.png"
								TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver" )%>"
								ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.printserver" )%>" CLASS="birtviewer_clickable icon_main_functions">
					</TD>
					<%
					}
					%>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
						<INPUT id="previewLayoutButton" TYPE="image" NAME="previewLayout" SRC="birt/images/PreviewPageLayout.png" VALUE="page"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							PAGE_TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							PAGE_ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.page" )%>"
							HTML_TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.html" )%>"
							HTML_ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.preview.layout.html" )%>"
							CLASS="birtviewer_clickable icon_main_functions">
					</TD>
						<%
						if( ParameterAccessor.isSupportedInfoDialog )
						{
						%>
						<TD WIDTH="6px"/>
						<TD WIDTH="15px">
							<input id="info-open-dialog" type="image" name='infoDialog' src="birt/images/Info.png"
									title="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.info" )%>"
									alt="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.info" )%>"
									class="icon_main_size"
									onclick="displayDialog('block')" >
							<div id="birt-info-dialog-layer" class="info_dialog_layer" style="display:none"> 
							<div id="birt-info-dialog" class="info_dialog_frame info_dialog_light">
								<div id="modal-content" class="modal-content">
									<div id="birt-info-group-frame" class="info_group_frame info_group_light"><span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.title" )%></span><span onclick="displayDialog('none')" style="text-align:left;" id="info_dialog_close" class="info_dialog_close dialogCloseBtn"></span></div>
									<div class="doc_chapter_title"><span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.title" )%></span></div>
									<div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.fonts/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.font" )%></span>
											</a>
											<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.fonts/fontsConfig.xml" target="blank">
												<span style="font-weight: bold;margin-left: 4px;font-size: 10pt;">&lt;/&gt;</span>
												<span style="font-size: 10pt;margin-left: 2px;"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.font.setup" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.font.details" )%></li></ul>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/data/org.eclipse.birt.report.engine.script.javascript/src/org/eclipse/birt/report/engine/javascript/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.js" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.js.details" )%></li></ul>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/data/org.eclipse.birt.report.data.oda.jdbc/src/org/eclipse/birt/report/data/oda/jdbc/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.jdbc" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.config.jdbc.details" )%></li></ul>
										</div>
									</div>
									<hr class="info_line_common">
									<div class="doc_chapter_title"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.title" )%></div>
									<div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/engine/uk.co.spudsoft.birt.emitters.excel/src/uk/co/spudsoft/birt/emitters/excel/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.xlsx" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.xlsx.details" )%></li></ul>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.emitter.wpml/src/org/eclipse/birt/report/engine/emitter/wpml/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.docx" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.docx.details" )%></li></ul>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.emitter.pdf/src/org/eclipse/birt/report/engine/emitter/pdf/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.pdf" )%></span>
											</a>
											<ul><li class="doc_li_item"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.emitter.pdf.details" )%></li></ul>
										</div>
									</div>
									<hr class="info_line_common">
									<div class="doc_chapter_title"><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.project.title" )%></div>
									<div>
										<div class="doc_content_slot">
											<a href="https://eclipse-birt.github.io/birt-website/" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.homepage" )%></span>
											</a>
										</div>
										<div class="doc_content_slot">
											<a href="https://download.eclipse.org/birt/updates/release/latest/" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.download" )%></span>
											</a>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.github" )%></span>
											</a>
										</div>
										<div class="doc_content_slot">
											<a href="https://github.com/eclipse-birt/birt/blob/master/README.md" target="blank">
												<img src="../webcontent/birt/images/Link_Icon.png" alt="Link" class="doc_link_image">
												<span><%= BirtResources.getHtmlMessage( "birt.viewer.dialog.info.birt.version" )%></span>
											</a>
										</div>
									</div>
									<hr class="info_line_button">
								</div>
							</div>
							</div>
							<script>
								// When the user clicks the button, open the info dialog 
								var infoDialog = document.getElementById("birt-info-dialog-layer");
								function displayDialog(mode) {
									if (infoDialog) {
										infoDialog.style.display = mode;
									}
								}

								// When the user clicks anywhere outside of the infoDialog, close it
								window.onclick = function(event) {
									if (infoDialog!== null && event.target == infoDialog) {
										infoDialog.style.display = "none";
									}
								}
							</script>
						</TD>
						<%
						}
						%>
					<TD ALIGN='right'>
						<div class="navbar-toggle-right">
						   <div id="toggle-button-frame" class="toggle-frame" onclick="birtToolbarTheme.toggleSwitch('toggleButton')">
						      <div class="toggle-track" tabindex="-1">
						         <div class="toggle-track-dark"><span class="toggle-icon-left">🌜</span></div>
						         <div class="toggle-track-light"><span class="toggle-icon-right">🌞</span></div>
						         <div id="toggle-track-icon" class="toggle-track-icon"></div>
						      </div>
						      <input id="toggle-track-checkbox" type="checkbox" class="toggle-track-checkbox" aria-label="Switch between dark and light mode">
						   </div>
						</div>
						<script>
							var birtToolbarTheme = new BirtToolbarTheme();
							birtToolbarTheme.initToggle();
						</script>
					</TD>
					<TD WIDTH="6px"/>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>
