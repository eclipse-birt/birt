<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/chart.tld" prefix="chart"%>
<%@page import="org.eclipse.birt.chart.viewer.sample.SampleHelper"%>
<%@page import="com.ibm.icu.util.ULocale"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sample web page of BIRT Chart</title>
</head>
<body>
<p>Read <a href="notes.html">notes</a> here if you can't preview following charts in some servers.</p>
<table border="1">
<tr><td align="center">Sample chart</td><td align="center">Properties</td></tr>
<tr>
<td>
<chart:renderChart width="600" height="300" model="SampleBar.chart"
	data="<%=SampleHelper.createSampleHeaderEvaluator() %>"
	runtimeContext="<%=SampleHelper.createSampleRuntimeContext(ULocale.ENGLISH) %>">
</chart:renderChart>
</td>
<td valign="top">
Chart type: Tube<br>
Output format: PNG<br>
Model type: XML file<br>
Data type: Evaluator<br>
Style processor: NONE<br>
Interactivity: Tooltips and Hyperlinks<br>
(Click the tube to see the detail data)
</td>
</tr>
<tr>
<td>
<chart:renderChart width="600" height="300" output="svg"
	model="<%=SampleHelper.createSampleRuntimeChart() %>"
	styleProcessor="<%=SampleHelper.getSampleStyleProcessor() %>">
</chart:renderChart>
</td>
<td valign="top">
Chart type: Difference<br>
Output format: SVG<br>
Model type: Java instance<br>
Data type: Built-in data in Java<br>
Style processor: Yes<br>
Interactivity: Highlight series
</td>
</tr>
<tr>
<td>
<chart:renderChart width="600" height="300" output="pdf"
	model="SamplePie.chart">
</chart:renderChart>
</td>
<td valign="top">
Chart type: Pie<br>
Output format: PDF<br>
Model type: XML file<br>
Data type: Sample data<br>
Style processor: NONE<br>
Interactivity: NONE
</td>
</tr>
</table>
</body>
</html>