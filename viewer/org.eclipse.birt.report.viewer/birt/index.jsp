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
				width:				280px;
			}
			.li {
				padding:			4px;
			}
			
			.li-label {
				display:			inline-block;
				min-width:			148px;
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
		</style>
	<%
		String viewerVersion = "4.18.0";
		String engineVersion = "4.18.0";
		String qualifierVersion = "4.18.0.qualifier";

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
						<div style="background-color:#4682b4; color:white; padding:8px 8px 8px 16px; font-weight:bold;">Thank you for your choosing BIRT (Business Intelligence Reporting Tool).</div>
						<div>
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
						<div class="info_separator"></div>
					</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>