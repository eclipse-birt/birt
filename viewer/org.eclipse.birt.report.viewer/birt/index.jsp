<%@ page import="java.io.File" %>
<!DOCTYPE HTML>
<html>
	<head>
		<title>Eclipse BIRT Home</title>
		<meta http-equiv=Content-Type content="text/html; charset=iso-8859-1">
		<link href="styles/iv/index.css" type=text/css rel=stylesheet>
		<link href="https://eclipse-birt.github.io/birt-website/img/BIRT-Icon.ico"  type=image/x-icon rel="shortcut icon">
		<style>
			.warningMessage {
				color:				red; 
			}
			
			.main_font {
				font-family:		Arial, sans-serif; 
			}
			
			.info_group {
				margin-top:			16px;
				padding:			6px 8px 6px 20px;
				background-color:	#ddd;
				color:				#5c5c5c;
				font-weight:		bold;
				font-size:			11pt;
			}

			.info_large {
				width:				264px;
			}
			
			.li {
				padding:			4px;
			}
			
			.li-label {
				display:			inline-block;
				min-width:			264px;
			}

			.li-value {
				display:			inline-block;
				min-width:			132px;
				font-size:			12pt;
			}
			
			.installed_message {
				border:				1px solid gray;
				border-radius:		2px;
				font-weight:		bold;
				font-size:			14pt;
				color:				#5c5c5c;
				background-color:	white;
				margin:				16px 0px 24px 0px;
				padding:			16px;

			}
			
			.info_separator {
				background-color:	#4682b4;
				height:				2px;
			}

			ul {
				margin-top:			4px;
				margin-bottom:		4px;
			}

			ul li, p {
				font-size:			10.5pt;
			}
			
			.ul-beside {
				display:			inline-block;
				margin-left:		32px;
			}
			.doc-li-item {
				margin-top	: 8px;
				font-size	: 0.95em;
			}
			.doc-chapter-title {
				font-weight	: bold;
				margin		: 24px 0px 0px 20px;
				font-size	: 0.9em;
			}
			.doc-content-slot {
				margin			: 12px 0px 0px 36px;
				text-decoration	: none;
				font-size		: 0.9em;
			}
			.link-img {
				margin			: 0px 4px 0px 0px;
				vertical-align	: middle;
				height			: 12px;
			}
			a:link {
				text-decoration: none;
				color: #06c;
			}
			a:visited {
				text-decoration: none;
				color: #06c;
			}
			a:hover {
				text-decoration: none;
				color: #06c;
			}
			a:active {
				text-decoration: none;
				color: #06c;
			}

		</style>
	<%
		String viewerVersion = "4.20.0";
		String engineVersion = "4.20.0";
		String qualifierVersion = "4.19.0.qualifier";

		String javaVersion	= System.getProperty("java.version");
		String javaVendor	= System.getProperty("java.vendor");
		String javaHome		= System.getProperty("java.home");

		String osName		= System.getProperty("os.name");
		String osVersion	= System.getProperty("os.version");
		String osArch		= System.getProperty("os.arch");

		String directoryApplicationServer			= "application server, directory";
		String workingDirectoryApplicationServer	= "working directory, application server";
		String workingDirectoryApplicationServlet	= "working directory, servlet/viewer";

		String runtimeVersion = "Runtime undefined";
		
		try {
			directoryApplicationServer			= new File(".").getCanonicalPath();
			workingDirectoryApplicationServer	= this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath().toString().replaceAll("/", "\\" + File.separator).substring(1);
			workingDirectoryApplicationServlet	= getServletConfig().getServletContext().getRealPath("/");

			String osgiPath = workingDirectoryApplicationServlet + "/WEB-INF/platform/plugins";
			File osgiDir = new File(osgiPath);
			if (osgiDir != null && osgiDir.isDirectory()) {
				runtimeVersion = "OSGi Runtime";
			} else {
				runtimeVersion = "Standard Runtime";
			}
			if (!qualifierVersion.contains("qualifier")) {
				runtimeVersion += ", Qualifier: " + qualifierVersion;
			}
		} catch (Exception e) {}
		
%>
	</head>
	<body class="main_font">
		<!-- Page banner -->
		<table class="main_font" style="width:100%; border:0; background-image:url('webcontent/birt/images/Breadcrumbs-large-bg.jpg'); background-size:100%; padding-left:12px;">
			<tbody>
				<tr>
					<td width=120>
						<a href="http://www.eclipse.org/">
							<img src="webcontent/birt/images/EclipseBannerPic.jpg" alt="Eclipse Logo" style="margin-left:-14px; vertical-align:middle; width:120px; border:0;">
						</a>
					</td>
					<td>
						<span style="margin-left:20px; font-weight:bold; font-size:16pt; color:white;">BIRT Viewer Installation</span>
					</td>
					<td vAlign=center align=right width=250>
						<a class="birt" href="http://www.eclipse.org/birt" target="blank">
							<!-- Temporary BIRT header -->
							<div style="text-align:right; padding-right:10px; font-weight:bold; font-size:20px; color:#e8e8ff;">
								<img src="webcontent/birt/images/BIRT-Logo.png" alt="BIRT Logo" style="margin-right:12px; height:48px; margin-top:8px; margin-bottom:8px; background-color: white;border: 4px solid white; border-radius: 4px;">
							</div>
						</a> 
					</td>
				</tr>
			</tbody>
		</table>
		<!-- table with menu & content -->
		<table class="main_font" style="padding:0px; width:100%; border:0;">
			<tbody>
				<tr>
					<td class="content" style="padding:10px;" >
						<!-- Page title -->
						<div class="installed_message"><img src="webcontent/birt/images/BIRT-Icon-large.png" alt="BIRT Logo" style="vertical-align:middle; border:0; margin-right:12px; height:48px;">BIRT viewer has been installed.</div>
						<!-- Content area -->
						<div style="background-color:#4682b4; color:white; padding:8px 8px 8px 16px; font-weight:bold;">
							<span style="display:inline-block;">Thank you for your choosing BIRT (Business Intelligence Reporting Tool)</span>
							<ul class="ul-beside">
								<li class="li"><span class="li-value"><a  style="text-decoration:none; color:white;" href="https://eclipse-birt.github.io/birt-website/" target="blank">BIRT Homepage</a></span></li>
							</ul>
							<ul class="ul-beside">
								<li class="li"><span class="li-value"><a  style="text-decoration:none; color:white;" href="https://download.eclipse.org/birt/updates/release/latest/" target="blank">BIRT Download Page</a></span></li>
							</ul>
							<ul class="ul-beside">
								<li class="li"><span class="li-value"><a  style="text-decoration:none; color:white;" href="https://github.com/eclipse-birt/birt" target="blank">BIRT Project at GitHub</a></span></li>
							</ul>
						</div>
						<div style="float:left;width:75%;">
							<%
								String javaVersionMessage = javaVersion;
								
								// check Java version
								String[] versionParts = javaVersion.split("\\.");
								int majorVersion = 0;
								int minorVersion = 0;
								try
								{
									majorVersion = Integer.parseInt(versionParts[0]);		
									minorVersion = Integer.parseInt(versionParts[1]);
									if ( majorVersion < 1 || ( majorVersion == 1 && minorVersion < 5 ) )
									{
										javaVersionMessage = "<span class=\"warningMessage\">" + javaVersion + " (WARNING: BIRT " + viewerVersion + " only supports JRE versions >= 1.5)</span>";
									}
								}
								catch (NumberFormatException e)
								{
								
								}
							%>
							<div class="info_group">BIRT details</div>
							<ul>
								<li class="li"><span class="li-label">Viewer Version:</span><span><%= viewerVersion %></span></li>
								<li class="li"><span class="li-label">Engine Version:</span><span><%= engineVersion %></span></li>
								<li class="li"><span class="li-label">BIRT Runtime:</span><span><%= runtimeVersion %></span></li>
							</ul>
							<div class="info_separator"></div>
							<p>
								<a href="<%= request.getContextPath( ) + "/frameset?__report=example_simple.rptdesign&paramString=my+parameter&paramInteger=2&paramList=2" %>" target="blank">
									<img src="webcontent/birt/images/Link_Icon.png" alt="Link" style="margin-left:24px; margin-right:8px; vertical-align:middle; height:16px;">
									<span>View Example 01: Integrated simple report</span>
								</a>
							</p>
							<p>
								<a href="<%= request.getContextPath( ) + "/frameset?__report=example_chart.rptdesign&paramCust=130" %>" target="blank">
									<img src="webcontent/birt/images/Link_Icon.png" alt="Link" style="margin-left:24px; margin-right:8px; vertical-align:middle; height:16px;">
									<span>View Example 02: Integrated chart report</span>
								</a>
							</p>
							<div class="info_separator"></div>
							<div class="info_group">OS details</div>
							<ul>
								<li class="li"><span class="li-label">OS Name:</span><span><%= osName  %></span></li>
								<li class="li"><span class="li-label">OS Version:</span><span><%= osVersion  %></span></li>
								<li class="li"><span class="li-label">OS Architecture:</span><span><%= osArch  %></span></li>
							</ul>
							<div class="info_group">Java environment</div>
							<ul>
								<li class="li"><span class="li-label">JRE Version:</span><span><%= javaVersionMessage  %></span></li>
								<li class="li"><span class="li-label">JRE Vendor:</span><span><%= javaVendor  %></span></li>
								<li class="li"><span class="li-label">Java Home:</span><span><%= javaHome  %></span></li>
							</ul>
							<div class="info_group">Applicaton Server</div>
							<ul>
							    <li class="li"><span class="li-label info_large">Applicaton Server:</span><span><%= application.getServerInfo() %></span></li> 
								<li class="li"><span class="li-label info_large">Application Server, Directory:</span><span><%= directoryApplicationServer %></span></li>
								<li class="li"><span class="li-label info_large">Application Server, Working Directory:</span><span><%= workingDirectoryApplicationServer %></span></li>
								<li class="li"><span class="li-label info_large">Web Application, Working Directory:</span><span><%= workingDirectoryApplicationServlet %></span></li>
							</ul>
						</div>
						<div style="float:left;width:0.5%;">&nbsp;</div>
						<div style="float:left; width:24.5%;">
							<div class="info_group">BIRT documentation</div>
							<div class="doc-chapter-title">Global BIRT configuration</div>
							<div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.fonts/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>Font configuration</span>
									</a>
									<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.fonts/fontsConfig.xml" target="blank">
										<span style="font-weight: bold;margin-left: 4px;font-size: 10pt;">&lt;/&gt;</span>
										<span style="font-size: 10pt;margin-left: 2px;">Setup-XML</span>
									</a>
									<ul><li class="doc-li-item">handling of external fonts, mapping and kerning &amp; ligatures</li></ul>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/data/org.eclipse.birt.report.engine.script.javascript/src/org/eclipse/birt/report/engine/javascript/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>JavaScript configuration</span>
									</a>
									<ul><li class="doc-li-item">JS version setup and security configuration</li></ul>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/data/org.eclipse.birt.report.data.oda.jdbc/src/org/eclipse/birt/report/data/oda/jdbc/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>JDBC driver configuration</span>
									</a>
									<ul><li class="doc-li-item">configuration of validation roles for JDBC driver</li></ul>
								</div>
							</div>
							<div class="doc-chapter-title">Output emitter configuration</div>
							<div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/engine/uk.co.spudsoft.birt.emitters.excel/src/uk/co/spudsoft/birt/emitters/excel/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>Microsoft Excel, XLSX (Spudsoft)</span>
									</a>
									<ul><li class="doc-li-item">setup of additional Excel output functions</li></ul>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.emitter.wpml/src/org/eclipse/birt/report/engine/emitter/wpml/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>Microsoft Word, DOCX</span>
									</a>
									<ul><li class="doc-li-item">setup of additional Word output details</li></ul>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/engine/org.eclipse.birt.report.engine.emitter.pdf/src/org/eclipse/birt/report/engine/emitter/pdf/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>Adobe PDF</span>
									</a>
									<ul><li class="doc-li-item">setup of additional PDF output details</li></ul>
								</div>
							</div>
							<div  class="doc-chapter-title">BIRT project</div>
							<div>
								<div class="doc-content-slot">
									<a href="https://eclipse-birt.github.io/birt-website/" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>BIRT Homepage</span>
									</a>
								</div>
								<div class="doc-content-slot">
									<a href="https://download.eclipse.org/birt/updates/release/latest/" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>BIRT Download Site</span>
									</a>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>BIRT Project at Github</span>
									</a>
								</div>
								<div class="doc-content-slot">
									<a href="https://github.com/eclipse-birt/birt/blob/master/README.md" target="blank">
										<img src="webcontent/birt/images/Link_Icon.png" alt="Link" class="link-img">
										<span>BIRT Version & Contribution</span>
									</a>
								</div>
							</div>
						</div>
						<div class="info_separator"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>