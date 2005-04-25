/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.Style;

/**
 * This class defines constants for looking up resources that are available only
 * within the Report Designer UI.
 */
public interface IReportGraphicConstants
{

	// common icons
	public final static String ICON_NEW_REPORT = "New report"; //$NON-NLS-1$

	public final static String ICON_REPORT_FILE = "Report"; //$NON-NLS-1$

	public final static String ICON_QUIK_EDIT = "Quick edit"; //$NON-NLS-1$

	public final static String ICON_REPORT_PERSPECTIVE = "Report perspective"; //$NON-NLS-1$

	public final static String ICON_REPORT_PROJECT = "Report project"; //$NON-NLS-1$

	//element icons, most of them are same as model defined for convenience
	public final static String ICON_ELEMENT_CELL = ReportDesignConstants.CELL_ELEMENT;

	public final static String ICON_ELEMENT_DATA = ReportDesignConstants.DATA_ITEM;

	public final static String ICON_ELEMENT_DATA_SET = ReportDesignConstants.DATA_SET_ELEMENT;

	public final static String ICON_ELEMENT_DATA_SOURCE = ReportDesignConstants.DATA_SOURCE_ELEMENT;

	public final static String ICON_ELEMENT_EXTENDED_ITEM = ReportDesignConstants.EXTENDED_ITEM;

	public final static String ICON_ELEMENT_ODA_DATA_SET = ReportDesignConstants.ODA_DATA_SET;

	public final static String ICON_ELEMENT_ODA_DATA_SOURCE = ReportDesignConstants.ODA_DATA_SOURCE;
	
	public final static String ICON_ELEMENT_SCRIPT_DATA_SET = ReportDesignConstants.SCRIPT_DATA_SET;

	public final static String ICON_ELEMENT_SCRIPT_DATA_SOURCE = ReportDesignConstants.SCRIPT_DATA_SOURCE;

	public final static String ICON_ELEMENT_GRID = ReportDesignConstants.GRID_ITEM;

	public final static String ICON_ELEMENT_GROUP = "Group"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_IMAGE = ReportDesignConstants.IMAGE_ITEM;

	public final static String ICON_ELEMENT_LABEL = ReportDesignConstants.LABEL_ITEM;

	public final static String ICON_ELEMENT_LINE = ReportDesignConstants.LINE_ITEM;

	public final static String ICON_ELEMENT_LIST = ReportDesignConstants.LIST_ITEM;

	public final static String ICON_ELEMENT_LIST_GROUP = ReportDesignConstants.LIST_GROUP_ELEMENT;

	public final static String ICON_ELEMNET_MASTERPAGE = ReportDesignConstants.MASTER_PAGE_ELEMENT;

	public final static String ICON_ELEMENT_PARAMETER = "Parameter"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_PARAMETER_GROUP = ReportDesignConstants.PARAMETER_GROUP_ELEMENT;

	public final static String ICON_ELEMENT_ROW = ReportDesignConstants.ROW_ELEMENT;

	public final static String ICON_ELEMENT_SCALAR_PARAMETER = ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;

	public final static String ICON_ELEMNET_SIMPLE_MASTERPAGE = ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;

	public final static String ICON_ELEMENT_STYLE = ReportDesignConstants.STYLE_ELEMENT;

	public final static String ICON_ELEMENT_TABLE = ReportDesignConstants.TABLE_ITEM;

	public final static String ICON_ELEMENT_TABLE_GROUP = ReportDesignConstants.TABLE_GROUP_ELEMENT;

	public final static String ICON_ELEMENT_TEXT = ReportDesignConstants.TEXT_ITEM;

	//outline view icons
	public final static String ICON_NODE_BODY = "Body"; //$NON-NLS-1$

	public final static String ICON_NODE_MASTERPAGES = "Master Pages"; //$NON-NLS-1$

	public final static String ICON_NODE_STYLES = "Styles"; //$NON-NLS-1$

	public final static String ICON_NODE_HEADER = "Header"; //$NON-NLS-1$

	public final static String ICON_NODE_DETAILS = "Details"; //$NON-NLS-1$

	public final static String ICON_NODE_FOOTER = "Footer"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUPS = "Groups"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUP_HEADER = "Group Header"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUP_FOOTER = "Group Footer"; //$NON-NLS-1$

	//icons for layout
	public final static String ICON_LAYOUT_NORMAL = "Layout Normal"; //$NON-NLS-1$

	public final static String ICON_LAYOUT_MASTERPAGE = "Layout Master Page"; //$NON-NLS-1$

	public final static String ICON_LAYOUT_RULER = "Layout Ruler"; //$NON-NLS-1$

	// add image constants for border
	public final static String ICON_BORDER_ALL = "All borders"; //$NON-NLS-1$

	public final static String ICON_BORDER_BOTTOM = "Bottom border"; //$NON-NLS-1$

	public final static String ICON_BORDER_LEFT = "Left border"; //$NON-NLS-1$

	public final static String ICON_BORDER_NOBORDER = "No border"; //$NON-NLS-1$

	public final static String ICON_BORDER_RIGHT = "Right border"; //$NON-NLS-1$

	public final static String ICON_BORDER_TOP = "Top border"; //$NON-NLS-1$

	// add image constants for chart
	public final static String ICON_CHART = "Chart icon"; //$NON-NLS-1$

	// missing image icon
	public final static String ICON_MISSING_IMG = "Missing image"; //$NON-NLS-1$

	// add image constants for data explore
	public final static String ICON_DATA_EXPLORER_VIEW = "Data explorer view"; //$NON-NLS-1$

	public final static String ICON_NODE_DATA_SETS = "Data Sets"; //$NON-NLS-1$

	public final static String ICON_NODE_DATA_SOURCES = "Data Sources"; //$NON-NLS-1$

	public final static String ICON_NODE_PARAMETERS = "Parameters"; //$NON-NLS-1$

	public final static String ICON_DATA_COLUMN = "DataColumn"; //$NON-NLS-1$

	//auto text icon
	public final static String ICON_AUTOTEXT = "AutoText"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_PAGE = "Page"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_DATE = "Date"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CREATEDON = "Created on"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CREATEDBY = "Created by"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_FILENAME = "Filename"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_LAST_PRINTED = "Last printed"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_PAGEXOFY = "Page X of Y"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_AUTHOR_PAGE_DATE = "AuthorPageDate";//$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CONFIDENTIAL_PAGE = "ConfidentalPage";//$NON-NLS-1$

	//expression builder icons
	public final static String ICON_EXPRESSION_FUNCTION = "Function";//$NON-NLS-1$	

	public final static String ICON_EXPRESSION_DATA_TABLE = "DataTable";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_OPERATOR = "Operator";//$NON-NLS-1$

	public final static String ICON_DEFINED_EXPRESSION = "Expression";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_CONSTANT = "Constant";//$NON-NLS-1$

	//data wizards
	public final static String ICON_WIZARD_DATASOURCE = "DataSourceBasePage";//$NON-NLS-1$

	public final static String ICON_WIZARD_DATASET = "DataSetBasePage";//$NON-NLS-1$

	public final static String ICON_WIZARDPAGE_DATASETSELECTION = "DataSetSelectionPage";//$NON-NLS-1$

	//attribute icon constants
	public final static String DIS = "DIS";//$NON-NLS-1$	

	public final static String ICON_ATTRIBUTE_FONT_WIDTH = Style.FONT_WEIGHT_PROP;

	public final static String ICON_ATTRIBUTE_FONT_STYLE = Style.FONT_STYLE_PROP;

	public final static String ICON_ATTRIBUTE_TEXT_UNDERLINE = Style.TEXT_UNDERLINE_PROP;

	public final static String ICON_ATTRIBUTE_TEXT_LINE_THROUGH = Style.TEXT_LINE_THROUGH_PROP;

	public final static String ICON_ATTRIBUTE_BORDER_NONE = "BORDER_NONE";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_BORDER_FRAME = "BORDER_FRAME";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_BORDER_LEFT = "BORDER_LEFT";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_BORDER_RIGHT = "BORDER_RIGHT";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_BORDER_TOP = "BORDER_TOP";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_BORDER_BOTTOM = "BORDER_BOTTOM";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_TEXT_ALIGN_CENTER = DesignChoiceConstants.TEXT_ALIGN_CENTER;

	public final static String ICON_ATTRIBUTE_TEXT_ALIGN_JUSTIFY = DesignChoiceConstants.TEXT_ALIGN_JUSTIFY;

	public final static String ICON_ATTRIBUTE_TEXT_ALIGN_LEFT = DesignChoiceConstants.TEXT_ALIGN_LEFT;

	public final static String ICON_ATTRIBUTE_TEXT_ALIGN_RIGHT = DesignChoiceConstants.TEXT_ALIGN_RIGHT;

	public final static String ICON_ATTRIBUTE_TOP_MARGIN = MasterPage.TOP_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_BOTTOM_MARGIN = MasterPage.BOTTOM_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_LEFT_MARGIN = MasterPage.LEFT_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_RIGHT_MARGIN = MasterPage.RIGHT_MARGIN_PROP;

	//Preview editer icons
	public final static String ICON_PREVIEW_PARAMETERS = "PreviewParameters";//$NON-NLS-1$	

	public final static String ICON_PREVIEW_REFRESH = "Refresh";//$NON-NLS-1$
}