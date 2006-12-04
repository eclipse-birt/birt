<?xml version="1.0" encoding="UTF-8"?><!-- DWXMLSource="TESTS-SmokeTests.xml" -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<xsl:output version="1.0" encoding="UTF-8" indent="no" omit-xml-declaration="no" media-type="text/html"/>
	<xsl:template match="/">
		<xsl:for-each select="testsuite">
			<html>
				<head>
					<title/>
					<style type="text/css"><![CDATA[							
      body {
      	font:normal 68% verdana,arial,helvetica;
      	color:#000000;
      }
      
      table{
      }
      
      table.details  tr th {
       	font-weight: bold;
      	text-align:left;
      	background:#a6caf0;
      }
  
      table.details tr td{
      	background:#eeeee0;
      }
      
      table.details{
		  width:100%;
      }

      td.odd{
		  background:#eeeee0;
      }
    
      td.even{
		  background=#a6caf0;
      }
      
	 .Errors {
		 font-weight:bold; color: red;
	 }
     
     .Failures {
		 font-weight:bold; color: purple;
	 } 
      
      
      ]]></style>
				</head>
				<body>
					<h1>
						<span style="font-family:Verdana; font-size:h1; ">
							<xsl:value-of select="@name"/>
						</span> Smoke Test Result
								</h1>
					<br/>
					<h2>Summary<h2/>
						<p>
							<table cellspacing="2" cellpadding="5"  border="0" width="100%" class="details">
										<tr>
										<th>Tests</th>
										<th>Failures</th>
										<th>Success rate</th>
										<th/>
									</tr>
									<tr class="Failures">
										<td>
											<xsl:value-of select="@tests"/>
										</td>
										<td>
											<xsl:value-of select="@failures"/>
										</td>
										<td>
											<xsl:value-of select="(@tests  -  @failures) div  @tests  *  100"/>%</td>
										<td/>
									</tr>
									<tr>
										<td/>
										<td/>
										<td/>
										<td/>
									</tr>
							</table>
							<br/>
						</p>
						<br/>
					</h2>
					<h3>Test Cases</h3>
					<table cellspacing="2" cellpadding="5"  border="0" width="100%" class="details">
						<thead>
							<tr>
								<th>Name</th>
								<th>Status</th>
								<th>Errors</th>
								<th>Engine internal errors</th>
							</tr>
						</thead>
						<tbody>
							<xsl:for-each select="testcase">
								<xsl:if test="string-length(  @errors )  != 0">
									<tr class="Errors">
										<td>
											<xsl:value-of select="@name"/>
										</td>
										<td>Failure</td>
										<td><xsl:value-of select="@errors"/></td>
										<td><xsl:value-of select="@internalErrors"/></td>
									</tr>
								</xsl:if>	
								<xsl:if test="string-length(  @errors ) = 0">
									<tr>
										<td>
											<xsl:value-of select="@name"/>
										</td>
										<td>Success</td>
										<td><xsl:value-of select="@errors"/></td>
										<td><xsl:value-of select="@internalErrors"/></td>
									</tr>
								</xsl:if>	
							</xsl:for-each>
						</tbody>
					</table>
					<br/>
					<a href="#top">Back to top</a>	
				</body>
			</html>
			<br/>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
<!-- Stylus Studio meta-information - (c) 2004-2006. Progress Software Corporation. All rights reserved.
<metaInformation>
<scenarios ><scenario default="yes" name="Scenario1" userelativepaths="yes" externalpreview="no" url="TESTS&#x2D;SmokeTests.xml" htmlbaseurl="" outputurl="" processortype="internal" useresolver="yes" profilemode="0" profiledepth="" profilelength="" urlprofilexml="" commandline="" additionalpath="" additionalclasspath="" postprocessortype="none" postprocesscommandline="" postprocessadditionalpath="" postprocessgeneratedext="" validateoutput="no" validator="internal" customvalidator=""/></scenarios><MapperMetaTag><MapperInfo srcSchemaPathIsRelative="yes" srcSchemaInterpretAsXML="no" destSchemaPath="" destSchemaRoot="" destSchemaPathIsRelative="yes" destSchemaInterpretAsXML="no"/><MapperBlockPosition></MapperBlockPosition><TemplateContext></TemplateContext><MapperFilter side="source"></MapperFilter></MapperMetaTag>
</metaInformation>
-->