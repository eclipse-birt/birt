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
Bar chart (PDF):
<br>
<chart:renderChart width="400" height="300" output="pdf"
	model="<%=SampleHelper.createSampleChart() %>">
</chart:renderChart>
<br>
<br>
Pie chart (SVG):
<br>
<chart:renderChart width="400" height="300" output="svg"
	model="<%=session.getServletContext().getRealPath("SamplePie.chart")%>">
</chart:renderChart>
<br>
<br>
Bar chart with tooltips, data evaluator, style processor and runtime
context (PNG):
<br>
<chart:renderChart width="400" height="300"
	model="<%=session.getServletContext().getRealPath("SampleBar.chart")%>"
	data="<%=SampleHelper.createSampleEvaluator() %>"
	styleProcessor="<%=SampleHelper.getSampleStyleProcessor() %>"
	runtimeContext="<%=SampleHelper.createSampleRuntimeContext(ULocale.ENGLISH) %>">
</chart:renderChart>
</body>
</html>
