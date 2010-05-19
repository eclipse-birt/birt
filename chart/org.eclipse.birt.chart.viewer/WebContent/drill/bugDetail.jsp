<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="/chart.tld" prefix="chart"%>
<%@page import="org.eclipse.birt.chart.viewer.sample.SampleHelper"%>
<%
	String component = request.getParameter( "component" );
	if ( component == null )
	{
		component = "Chart";
	}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=component%> Detail</title>
</head>
<body>
<br>
<chart:renderChart width="600" height="400" output="svg"
	model='<%=SampleHelper.createSampleDesignTimeChart("Bug details of " + component + " component","Year","Number") %>'
	data="<%=SampleHelper.createSampleDetailsEvaluator(component) %>">
</chart:renderChart>

<br>
</body>
</html>