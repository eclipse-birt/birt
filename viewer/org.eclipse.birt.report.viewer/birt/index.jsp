<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>Eclipse BIRT Home</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
		<LINK href="styles/iv/index.css" type=text/css rel=stylesheet>
		<LINK href="http://www.eclipse.org/images/eclipse.ico" type=image/x-icon rel="shortcut icon">
		<STYLE>
			.warningMessage { color:red; }
		</STYLE>
	<%
		String javaVersion = System.getProperty("java.version");
		String viewerVersion = "4.8.0";
		String engineVersion = "4.8.0";
	%>
	</HEAD>
	<BODY>
		<!-- Page banner -->
		<TABLE class=banner-area cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR>
					<TD width=115><a href="http://www.eclipse.org/">
						<IMG src="webcontent/birt/images/EclipseBannerPic.jpg" alt="Eclipse Logo" width="115" height="50" border=0></a>
					</TD>
					<TD>
						<IMG src="webcontent/birt/images/gradient.jpg" alt="gradient banner" width="300" height="50" border=0>
					</TD>
					<TD vAlign=center align=right width=250>
						<a class="birt" href="http://www.eclipse.org/birt">
							<!-- Temporary BIRT header -->
							<SPAN style="PADDING-RIGHT: 10px; FONT-WEIGHT: bold; FONT-SIZE: 20px; COLOR: #e8e8ff; FONT-FAMILY: arial, sans-serif">
								BIRT
							</SPAN>
						</a> 
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<!-- Table with menu & content -->
		<TABLE cellSpacing=0 cols=2 cellPadding=0 border=0 width="100%">
			<TBODY>
				<TR>
					<TD class=content style="PADDING-RIGHT: 10px; PADDING-LEFT: 10px; PADDING-BOTTOM: 10px; PADDING-TOP: 10px" >
						<!-- Page title -->
						<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
							<TBODY>
								<TR>
									<TD vAlign=top>
										<span class="indextop">BIRT viewer has been installed.</span><p>&nbsp;
									</TD>
									<TD class=jump style="PADDING-LEFT: 10px" align=right rowSpan=2>
										<IMG src="webcontent/birt/images/Idea.jpg" alt="Idea" width="120" height="86">
									</TD>
								</TR>
								<TR>
									<TD>&nbsp;</TD>
								</TR>
							</TBODY>
						</TABLE>
						<!-- Content area -->
						<p>Thank you for your choosing BIRT (Business Intelligence Reporting Tool).</p>
						<p>Viewer Version : <%= viewerVersion %></p>
						<p>Engine Version: <%= engineVersion %></p>
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
						<p>JRE version: <%= javaVersionMessage  %></p>
						<p><a href="<%= request.getContextPath( ) + "/frameset?__report=test.rptdesign&sample=my+parameter" %>">View Example</a>
					</TD>
				</TR>
			</TBODY>
		</TABLE>
	</BODY>
</HTML>
