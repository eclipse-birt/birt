<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/chart.tld" prefix="chart"%>
<%@page import="org.eclipse.birt.chart.viewer.sample.SampleHelper"%>
<%@page import="com.ibm.icu.util.ULocale"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sample web page of BIRT chart</title>
</head>
<body>
<p>Read <a href="notes.html">notes</a> here if you can't preview following charts in WebLogic or WebSphere container.</p>

1. Bar chart with Tooltips and Hyperlinks (PNG):
<br>
<chart:renderChart width="600" height="400" model="SampleBar.chart"
	data="<%=SampleHelper.createSampleEvaluator() %>"
	runtimeContext="<%=SampleHelper.createSampleRuntimeContext(ULocale.ENGLISH) %>">
</chart:renderChart>
<br>
2. Difference chart (SVG):
<br>
<chart:renderChart width="400" height="300" output="svg"
	model="<%=SampleHelper.createSampleChart() %>"
	styleProcessor="<%=SampleHelper.getSampleStyleProcessor() %>">
</chart:renderChart>
<br>
3. Pie chart (PDF):
<br>
<chart:renderChart width="400" height="300" output="pdf"
	model="SamplePie.chart">
</chart:renderChart>
<br>
</body>
</html>