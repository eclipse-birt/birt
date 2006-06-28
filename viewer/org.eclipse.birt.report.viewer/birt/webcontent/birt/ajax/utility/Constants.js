/******************************************************************************
 *	Copyright (c) 2004 Actuate Corporation and others.
 *	All rights reserved. This program and the accompanying materials 
 *	are made available under the terms of the Eclipse Public License v1.0
 *	which accompanies this distribution, and is available at
 *		http://www.eclipse.org/legal/epl-v10.html
 *	
 *	Contributors:
 *		Actuate Corporation - Initial implementation.
 *****************************************************************************/
 
var Constants = {

	/** id of document managed BirtReportDocument*/
	documentId: "Document",
	
	/** element is a UI component managed by BirtReportBase */
	reportBase: "ReportBase",
	
	/** element is a table managed by BirtReportTable */
	reportTable: "ReportTable",
	
	/** element is a chart managed by BirtReportChart */
	reportChart: "ReportChart",
	
	/** element is a document managed BirtReportDocument */
	isDocument: "isDocument",
	
	/** contains number of selected column if there is one */
	selColNum: "SelColNum",

	/** contains number of selected column if there is one */
	activeIds: "activeIds",
	activeIdTypes: "activeIdTypes",
	
	// Report object types.
	Document : "Document",
	Table : "Table",
	Chart : "Chart",
	Label : "Label",
	Table_T : "Table_T",	// template of table type
	Chart_T : "Chart_T",	// template of chart type
	Label_T : "Label_T",	// template of label type
	
	/** 
	event.returnvalue indicating that event has already been handled
	as a selection
	*/
	select: "select",
	
	/**
	event.returnvalue indicated that contextmenu has already been handled
	*/
	context: "context",
	
	error : { },
	
	type: {UpdateRequest: "UpdateRequest"},
	operation: "Operation",
	target: "Target",
	operator: {show: "Show", hide: "Hide", sort: "Sort"}
}