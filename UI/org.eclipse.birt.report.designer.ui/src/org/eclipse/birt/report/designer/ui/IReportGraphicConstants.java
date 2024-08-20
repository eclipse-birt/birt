/*******************************************************************************
 * Copyright (c) 2004, 2024 Actuate Corporation and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui;

import static org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel.BOTTOM_MARGIN_PROP;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * This class defines constants for looking up resources that are available only
 * within the Report Designer UI.
 */
public interface IReportGraphicConstants {

	// Palette large icons
	/** property: large icons */
	String LARGE = "LARGE"; //$NON-NLS-1$

	// common icons

	/** icon property: new report */
	String ICON_NEW_REPORT = "New report"; //$NON-NLS-1$

	/** icon property: new template */
	String ICON_NEW_TEMPLATE = "New template"; //$NON-NLS-1$

	/** icon property: new library */
	String ICON_NEW_LIBRARY = "New library"; //$NON-NLS-1$

	/** icon property: new folder */
	String ICON_NEW_FOLDER = "New folder"; //$NON-NLS-1$

	/** icon property: report */
	String ICON_REPORT_FILE = "Report"; //$NON-NLS-1$

	/** icon property: template */
	String ICON_TEMPLATE_FILE = "Template"; //$NON-NLS-1$

	/** icon property: library */
	String ICON_LIBRARY_FILE = "Library"; //$NON-NLS-1$

	/** icon property: document */
	String ICON_DOCUMENT_FILE = "Document"; //$NON-NLS-1$

	/** icon property: report lock */
	String ICON_REPORT_LOCK = "Report Lock"; //$NON-NLS-1$

	/** icon property: report menu */
	String ICON_LOCK_MENU = "Report Menu"; //$NON-NLS-1$

	/** icon property: quick edit */
	String ICON_QUIK_EDIT = "Quick edit"; //$NON-NLS-1$

	/** icon property: report perspective */
	String ICON_REPORT_PERSPECTIVE = "Report perspective"; //$NON-NLS-1$

	/** icon property: report project */
	String ICON_REPORT_PROJECT = "Report project"; //$NON-NLS-1$


	// element icons, most of them are same as model defined for convenience
	/** icon property: cell element */
	String ICON_ELEMENT_CELL = ReportDesignConstants.CELL_ELEMENT;

	/** icon property: data element */
	String ICON_ELEMENT_DATA = ReportDesignConstants.DATA_ITEM;

	/** icon property: data large element */
	String ICON_ELEMENT_DATA_LARGE = ICON_ELEMENT_DATA + "_" + LARGE; //$NON-NLS-1$

	/** icon property: data set element */
	String ICON_ELEMENT_DATA_SET = ReportDesignConstants.DATA_SET_ELEMENT;

	/** icon property: data source element */
	String ICON_ELEMENT_DATA_SOURCE = ReportDesignConstants.DATA_SOURCE_ELEMENT;

	/** icon property: extended item element */
	String ICON_ELEMENT_EXTENDED_ITEM = ReportDesignConstants.EXTENDED_ITEM;

	/** icon property: oda data set element */
	String ICON_ELEMENT_ODA_DATA_SET = ReportDesignConstants.ODA_DATA_SET;

	/** icon property: oda data source element */
	String ICON_ELEMENT_ODA_DATA_SOURCE = ReportDesignConstants.ODA_DATA_SOURCE;

	/** icon property: derived data set element */
	String ICON_ELEMENT_DERIVED_DATA_SET = ReportDesignConstants.DERIVED_DATA_SET;

	/** icon property: scripted data set element */
	String ICON_ELEMENT_SCRIPT_DATA_SET = ReportDesignConstants.SCRIPT_DATA_SET;

	/** icon property: scripted data source element */
	String ICON_ELEMENT_SCRIPT_DATA_SOURCE = ReportDesignConstants.SCRIPT_DATA_SOURCE;


	// ReportDesignConstants.DATAMART_DATA_SET,
	// ReportDesignConstants.DATAMART_DATA_SOURCE
	/** icon property: datamart data set element */
	String ICON_ELEMENT_DATAMART_DATA_SET = "DataMartDataSet"; //$NON-NLS-1$

	/** icon property: datamart data source element */
	String ICON_ELEMENT_DATAMART_DATA_SOURCE = "DataMartDataSource"; //$NON-NLS-1$

	/** icon property: joint data set element */
	String ICON_ELEMENT_JOINT_DATA_SET = ReportDesignConstants.JOINT_DATA_SET;

	/** icon property: grid element */
	String ICON_ELEMENT_GRID = ReportDesignConstants.GRID_ITEM;

	/** icon property: grid large element */
	String ICON_ELEMENT_GRID_LARGE = ICON_ELEMENT_GRID + "_" + LARGE; //$NON-NLS-1$

	/** icon property: group element */
	String ICON_ELEMENT_GROUP = "Group"; //$NON-NLS-1$

	/** icon property: image element */
	String ICON_ELEMENT_IMAGE = ReportDesignConstants.IMAGE_ITEM;

	/** icon property: image large element */
	String ICON_ELEMENT_IMAGE_LARGE = ICON_ELEMENT_IMAGE + "_" + LARGE; //$NON-NLS-1$

	/** icon property: label element */
	String ICON_ELEMENT_LABEL = ReportDesignConstants.LABEL_ITEM;

	/** icon property: label large element */
	String ICON_ELEMENT_LABEL_LARGE = ICON_ELEMENT_LABEL + "_" + LARGE; //$NON-NLS-1$

	/** icon property: line element */
	String ICON_ELEMENT_LINE = ReportDesignConstants.LINE_ITEM;

	/** icon property: list element */
	String ICON_ELEMENT_LIST = ReportDesignConstants.LIST_ITEM;

	/** icon property: list large element */
	String ICON_ELEMENT_LIST_LARGE = ICON_ELEMENT_LIST + "_" + LARGE; //$NON-NLS-1$

	/** icon property: list group element */
	String ICON_ELEMENT_LIST_GROUP = ReportDesignConstants.LIST_GROUP_ELEMENT;

	/** icon property: masterpage element */
	String ICON_ELEMNET_MASTERPAGE = ReportDesignConstants.MASTER_PAGE_ELEMENT;

	/** icon property: graphic masterpage element */
	String ICON_ELEMNET_GRAPHICMASTERPAGE = ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT;

	/** icon property: parameter element */
	String ICON_ELEMENT_PARAMETER = "Parameter"; //$NON-NLS-1$

	/** icon property: parameter group element */
	String ICON_ELEMENT_PARAMETER_GROUP = ReportDesignConstants.PARAMETER_GROUP_ELEMENT;

	/** icon property: cascading parameter group element */
	String ICON_ELEMENT_CASCADING_PARAMETER_GROUP = ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT;

	/** icon property: row element */
	String ICON_ELEMENT_ROW = ReportDesignConstants.ROW_ELEMENT;

	/** icon property: column element */
	String ICON_ELEMENT_COLUMN = ReportDesignConstants.COLUMN_ELEMENT;

	/** icon property: scalar parameter element */
	String ICON_ELEMENT_SCALAR_PARAMETER = ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;

	/** icon property: simple masterpage element */
	String ICON_ELEMNET_SIMPLE_MASTERPAGE = ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;

	/** icon property: style element */
	String ICON_ELEMENT_STYLE = ReportDesignConstants.STYLE_ELEMENT;

	/** icon property: table element */
	String ICON_ELEMENT_TABLE = ReportDesignConstants.TABLE_ITEM;

	/** icon property: table large element */
	String ICON_ELEMENT_TABLE_LARGE = ICON_ELEMENT_TABLE + "_" + LARGE; //$NON-NLS-1$

	/** icon property: table group element */
	String ICON_ELEMENT_TABLE_GROUP = ReportDesignConstants.TABLE_GROUP_ELEMENT;

	/** icon property: text element */
	String ICON_ELEMENT_TEXT = ReportDesignConstants.TEXT_ITEM;

	/** icon property: text large element */
	String ICON_ELEMENT_TEXT_LARGE = ICON_ELEMENT_TEXT + "_" + LARGE; //$NON-NLS-1$

	/** icon property: textdata element */
	String ICON_ELEMENT_TEXTDATA = ReportDesignConstants.TEXT_DATA_ITEM;

	/** icon property: textdata large element */
	String ICON_ELEMENT_TEXTDATA_LARGE = ICON_ELEMENT_TEXTDATA + "_" + LARGE; //$NON-NLS-1$

	/** icon property: library element */
	String ICON_ELEMENT_LIBRARY = ReportDesignConstants.LIBRARY_ELEMENT;

	/** icon property: library referenced element */
	String ICON_ELEMENT_LIBRARY_REFERENCED = "Library Referenced"; //$NON-NLS-1$

	/** icon property: theme element */
	String ICON_ELEMENT_THEME = ReportDesignConstants.THEME_ITEM;

	/** icon property: css style sheet element */
	String ICON_ELEMENT_CSS_STYLE_SHEET = "ReportDesignConstants.CSS_STYLE_SHEET"; //$NON-NLS-1$

	/** icon property: templateitem element */
	String ICON_ELEMENT_TEMPLATEITEM = ReportDesignConstants.TEMPLATE_REPORT_ITEM;

	/** icon property: variable element */
	String ICON_ELEMENT_VARIABLE = ReportDesignConstants.VARIABLE_ELEMENT;

	/** icon property: variable report element */
	String ICON_ELEMENT_VARIABLE_REPORT = ReportDesignConstants.VARIABLE_ELEMENT + "report"; //$NON-NLS-1$

	/** icon property: variable page element */
	String ICON_ELEMENT_VARIABLE_PAGE = ReportDesignConstants.VARIABLE_ELEMENT + "page"; //$NON-NLS-1$

	// Library report item icons
	/** icon property: link element */
	String LINK = "LINK"; //$NON-NLS-1$

	/** icon property: css style sheet link element */
	String ICON_ELEMENT_CSS_STYLE_SHEET_LINK = ICON_ELEMENT_CSS_STYLE_SHEET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: style link element */
	String ICON_ELEMENT_STYLE_LINK = ICON_ELEMENT_STYLE + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: data link element */
	String ICON_ELEMENT_DATA_LINK = ICON_ELEMENT_DATA + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: data set link element */
	String ICON_ELEMENT_DATA_SET_LINK = ICON_ELEMENT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: data source link element */
	String ICON_ELEMENT_DATA_SOURCE_LINK = ICON_ELEMENT_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: grid link element */
	String ICON_ELEMENT_GRID_LINK = ICON_ELEMENT_GRID + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: image link element */
	String ICON_ELEMENT_IMAGE_LINK = ICON_ELEMENT_IMAGE + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: label link element */
	String ICON_ELEMENT_LABEL_LINK = ICON_ELEMENT_LABEL + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: list link element */
	String ICON_ELEMENT_LIST_LINK = ICON_ELEMENT_LIST + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: table link element */
	String ICON_ELEMENT_TABLE_LINK = ICON_ELEMENT_TABLE + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: text link element */
	String ICON_ELEMENT_TEXT_LINK = ICON_ELEMENT_TEXT + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: textdata link element */
	String ICON_ELEMENT_TEXTDATA_LINK = ICON_ELEMENT_TEXTDATA + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: oda data set link element */
	String ICON_ELEMENT_ODA_DATA_SET_LINK = ReportDesignConstants.ODA_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: derived data set link element */
	String ICON_ELEMENT_DERIVED_DATA_SET_LINK = ReportDesignConstants.DERIVED_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: oda data source link element */
	String ICON_ELEMENT_ODA_DATA_SOURCE_LINK = ReportDesignConstants.ODA_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: script data set link element */
	String ICON_ELEMENT_SCRIPT_DATA_SET_LINK = ReportDesignConstants.SCRIPT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: script data source link element */
	String ICON_ELEMENT_SCRIPT_DATA_SOURCE_LINK = ReportDesignConstants.SCRIPT_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	// ReportDesignConstants.DATAMART_DATA_SET,
	// ReportDesignConstants.DATAMART_DATA_SOURCE
	/** icon property: datamart data set link element */
	String ICON_ELEMENT_DATAMART_DATA_SET_LINK = ICON_ELEMENT_DATAMART_DATA_SET + "_" + LINK; //$NON-NLS-1$

	/** icon property: datamart data source link element */
	String ICON_ELEMENT_DATAMART_DATA_SOURCE_LINK = ICON_ELEMENT_DATAMART_DATA_SOURCE + "_" + LINK; //$NON-NLS-1$

	/** icon property: joint data set link element */
	String ICON_ELEMENT_JOINT_DATA_SET_LINK = ReportDesignConstants.JOINT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: scalar parameter element link */
	String ICON_SCALAR_PARAMETER_ELEMENT_LINK = ReportDesignConstants.SCALAR_PARAMETER_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: cascading parameter group element link */
	String ICON_CASCADING_PARAMETER_GROUP_ELEMENT_LINK = ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: parameter group element link */
	String ICON_PARAMETER_GROUP_ELEMENT_LINK = ReportDesignConstants.PARAMETER_GROUP_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	/** icon property: master page element link element */
	String ICON_SIMPLE_MASTER_PAGE_ELEMENT_LINK = ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	// outline view icons
	/** icon property: dy element */
	String ICON_NODE_BODY = "Body"; //$NON-NLS-1$

	/** icon property: master pages element */
	String ICON_NODE_MASTERPAGES = "Master Pages"; //$NON-NLS-1$

	/** icon property: styles element */
	String ICON_NODE_STYLES = "Styles"; //$NON-NLS-1$

	/** icon property: header element */
	String ICON_NODE_HEADER = "Header"; //$NON-NLS-1$

	/** icon property: details element */
	String ICON_NODE_DETAILS = "Details"; //$NON-NLS-1$

	/** icon property: footer element */
	String ICON_NODE_FOOTER = "Footer"; //$NON-NLS-1$

	/** icon property: groups element */
	String ICON_NODE_GROUPS = "Groups"; //$NON-NLS-1$

	/** icon property: group header element */
	String ICON_NODE_GROUP_HEADER = "Group Header"; //$NON-NLS-1$

	/** icon property: group footer element */
	String ICON_NODE_GROUP_FOOTER = "Group Footer"; //$NON-NLS-1$

	/** icon property: libraries element */
	String ICON_NODE_LIBRARIES = "Libraries"; //$NON-NLS-1$

	/** icon property: themes element */
	String ICON_NODE_THEMES = "Themes"; //$NON-NLS-1$

	/** icon property: images element */
	String ICON_NODE_IMAGES = "Images"; //$NON-NLS-1$

	// icons for layout
	/** icon property: layout normal element */
	String ICON_LAYOUT_NORMAL = "Layout Normal"; //$NON-NLS-1$

	/** icon property: layout master page element */
	String ICON_LAYOUT_MASTERPAGE = "Layout Master Page"; //$NON-NLS-1$

	/** icon property: layout ruler element */
	String ICON_LAYOUT_RULER = "Layout Ruler"; //$NON-NLS-1$

	// add image constants for border
	/** icon property: border all */
	String ICON_BORDER_ALL = "All borders"; //$NON-NLS-1$

	/** icon property: border bottom */
	String ICON_BORDER_BOTTOM = "Bottom border"; //$NON-NLS-1$

	/** icon property: border left */
	String ICON_BORDER_LEFT = "Left border"; //$NON-NLS-1$

	/** icon property: border noborder */
	String ICON_BORDER_NOBORDER = "No border"; //$NON-NLS-1$

	/** icon property: border right */
	String ICON_BORDER_RIGHT = "Right border"; //$NON-NLS-1$

	/** icon property: border top */
	String ICON_BORDER_TOP = "Top border"; //$NON-NLS-1$

	/** icon property: border diagonal */
	String ICON_BORDER_DIAGONAL = "Diagonal border"; //$NON-NLS-1$

	/** icon property: border antidiagonal */
	String ICON_BORDER_ANTIDIAGONAL = "Antidiagonal border"; //$NON-NLS-1$

	// missing image icon
	/** icon property: missing img */
	String ICON_MISSING_IMG = "Missing image"; //$NON-NLS-1$

	// add image constants for data explore
	/** icon property: data explorer view */
	String ICON_DATA_EXPLORER_VIEW = "Data explorer view"; //$NON-NLS-1$

	/** icon property: node data sets */
	String ICON_NODE_DATA_SETS = "Data Sets"; //$NON-NLS-1$

	/** icon property: node data sources */
	String ICON_NODE_DATA_SOURCES = "Data Sources"; //$NON-NLS-1$

	/** icon property: node parameters */
	String ICON_NODE_PARAMETERS = "Parameters"; //$NON-NLS-1$

	/** icon property: node variables */
	String ICON_NODE_VARIABLES = "Variables"; //$NON-NLS-1$

	/** icon property: data column */
	String ICON_DATA_COLUMN = "DataColumn"; //$NON-NLS-1$

	/** icon property: inherit column */
	String ICON_INHERIT_COLUMN = "InheritColumn"; //$NON-NLS-1$

	// auto text icon
	/** icon property: alphabetic sort */
	String ICON_ALPHABETIC_SORT = "AlphabeticSort"; //$NON-NLS-1$

	/** icon property: alphabetic sort ascending */
	String ICON_ALPHABETIC_SORT_ASCENDING = "AlphabeticSortAscending"; //$NON-NLS-1$

	/** icon property: alphabetic sort descending */
	String ICON_ALPHABETIC_SORT_DESCENDING = "AlphabeticSortDescending"; //$NON-NLS-1$

	/** icon property: autotext */
	String ICON_AUTOTEXT = "AutoText"; //$NON-NLS-1$

	/** icon property: autotext large */
	String ICON_AUTOTEXT_LARGE = ICON_AUTOTEXT + "_" + LARGE; //$NON-NLS-1$

	/** icon property: autotext page */
	String ICON_AUTOTEXT_PAGE = "Page"; //$NON-NLS-1$

	/** icon property: autotext date */
	String ICON_AUTOTEXT_DATE = "Date"; //$NON-NLS-1$

	/** icon property: autotext createdon */
	String ICON_AUTOTEXT_CREATEDON = "Created on"; //$NON-NLS-1$

	/** icon property: autotext createdby */
	String ICON_AUTOTEXT_CREATEDBY = "Created by"; //$NON-NLS-1$

	/** icon property: autotext filename */
	String ICON_AUTOTEXT_FILENAME = "Filename"; //$NON-NLS-1$

	/** icon property: autotext last printed */
	String ICON_AUTOTEXT_LAST_PRINTED = "Last printed"; //$NON-NLS-1$

	/** icon property: autotext pagexofy */
	String ICON_AUTOTEXT_PAGEXOFY = "Page X of Y"; //$NON-NLS-1$

	/** icon property: autotext author page date */
	String ICON_AUTOTEXT_AUTHOR_PAGE_DATE = "AuthorPageDate";//$NON-NLS-1$

	/** icon property: autotext confidential page */
	String ICON_AUTOTEXT_CONFIDENTIAL_PAGE = "ConfidentalPage";//$NON-NLS-1$

	// expression builder icons

	/** icon property: expression builder */
	String ICON_EXPRESSION_BUILDER = "Expression Builder";//$NON-NLS-1$

	/** icon property: expression data table */
	String ICON_EXPRESSION_DATA_TABLE = "DataTable";//$NON-NLS-1$

	/** icon property: expression operator */
	String ICON_EXPRESSION_OPERATOR = "Operator";//$NON-NLS-1$

	/** icon property: expression global */
	String ICON_EXPRESSION_GLOBAL = "Global";//$NON-NLS-1$

	/** icon property: expression constructop */
	String ICON_EXPRESSION_CONSTRUCTOP = "Constructor";//$NON-NLS-1$

	/** icon property: expression method */
	String ICON_EXPRESSION_METHOD = "Method";//$NON-NLS-1$

	/** icon property: expression static method */
	String ICON_EXPRESSION_STATIC_METHOD = "Static Method";//$NON-NLS-1$

	/** icon property: expression member */
	String ICON_EXPRESSION_MEMBER = "Member";//$NON-NLS-1$

	/** icon property: expression static member */
	String ICON_EXPRESSION_STATIC_MEMBER = "Static Member";//$NON-NLS-1$

	/** icon property: expression validate */
	String ICON_EXPRESSION_VALIDATE = "Validate Expression";//$NON-NLS-1$

	// data wizards
	/** icon property: wizard datasource */
	String ICON_WIZARD_DATASOURCE = "DataSourceBasePage";//$NON-NLS-1$

	/** icon property: wizard dataset */
	String ICON_WIZARD_DATASET = "DataSetBasePage";//$NON-NLS-1$

	/** icon property: wizard page dataset selection */
	String ICON_WIZARDPAGE_DATASETSELECTION = "DataSetSelectionPage";//$NON-NLS-1$

	/** icon property: history toolbar backward disabled */
	String ICON_HISTORYTOOLBAR_BACKWARDDISABLED = "BackwardDisabled";//$NON-NLS-1$

	/** icon property: history toolbar backward enabled */
	String ICON_HISTORYTOOLBAR_BACKWARDENABLED = "BackwardEnabled";//$NON-NLS-1$

	/** icon property: history toolbar forward disabled */
	String ICON_HISTORYTOOLBAR_FORWARDDISABLED = "ForwardDisabled";//$NON-NLS-1$

	/** icon property: history toolbar forward enabled */
	String ICON_HISTORYTOOLBAR_FORWARDENABLED = "ForwardEnabled";//$NON-NLS-1$

	// attribute icon constants
	/** icon property: dis */
	String DIS = "DIS";//$NON-NLS-1$

	/** icon property: attribute font width */
	String ICON_ATTRIBUTE_FONT_WIDTH = IStyleModel.FONT_WEIGHT_PROP;

	/** icon property: attribute font style */
	String ICON_ATTRIBUTE_FONT_STYLE = IStyleModel.FONT_STYLE_PROP;

	/** icon property: attribute text underline */
	String ICON_ATTRIBUTE_TEXT_UNDERLINE = IStyleModel.TEXT_UNDERLINE_PROP;

	/** icon property: attribute text line through */
	String ICON_ATTRIBUTE_TEXT_LINE_THROUGH = IStyleModel.TEXT_LINE_THROUGH_PROP;

	/** icon property: attribute border none */
	String ICON_ATTRIBUTE_BORDER_NONE = "BORDER_NONE";//$NON-NLS-1$

	/** icon property: attribute border frame */
	String ICON_ATTRIBUTE_BORDER_FRAME = "BORDER_FRAME";//$NON-NLS-1$

	/** icon property: attribute border left */
	String ICON_ATTRIBUTE_BORDER_LEFT = "BORDER_LEFT";//$NON-NLS-1$

	/** icon property: attribute border right */
	String ICON_ATTRIBUTE_BORDER_RIGHT = "BORDER_RIGHT";//$NON-NLS-1$

	/** icon property: attribute border top */
	String ICON_ATTRIBUTE_BORDER_TOP = "BORDER_TOP";//$NON-NLS-1$

	/** icon property: attribute border bottom */
	String ICON_ATTRIBUTE_BORDER_BOTTOM = "BORDER_BOTTOM";//$NON-NLS-1$

	/** icon property: attribute border diagonal */
	String ICON_ATTRIBUTE_BORDER_DIAGONAL = "BORDER_DIAGONAL";//$NON-NLS-1$

	/** icon property: attribute border antidiagonal */
	String ICON_ATTRIBUTE_BORDER_ANTIDIAGONAL = "BORDER_ANTIDIAGONAL";//$NON-NLS-1$

	/** icon property: attribute text align center */
	String ICON_ATTRIBUTE_TEXT_ALIGN_CENTER = DesignChoiceConstants.TEXT_ALIGN_CENTER;

	/** icon property: attribute text align justify */
	String ICON_ATTRIBUTE_TEXT_ALIGN_JUSTIFY = DesignChoiceConstants.TEXT_ALIGN_JUSTIFY;

	/** icon property: attribute text align left */
	String ICON_ATTRIBUTE_TEXT_ALIGN_LEFT = DesignChoiceConstants.TEXT_ALIGN_LEFT;

	/** icon property: attribute text align right */
	String ICON_ATTRIBUTE_TEXT_ALIGN_RIGHT = DesignChoiceConstants.TEXT_ALIGN_RIGHT;

	/** icon property: attribute top margin */
	String ICON_ATTRIBUTE_TOP_MARGIN = IMasterPageModel.TOP_MARGIN_PROP;

	/** icon property: attribute bottom margin */
	String ICON_ATTRIBUTE_BOTTOM_MARGIN = BOTTOM_MARGIN_PROP;

	/** icon property: attribute left margin */
	String ICON_ATTRIBUTE_LEFT_MARGIN = IMasterPageModel.LEFT_MARGIN_PROP;

	/** icon property: attribute right margin */
	String ICON_ATTRIBUTE_RIGHT_MARGIN = IMasterPageModel.RIGHT_MARGIN_PROP;

	/** icon property: attribute one column */
	String ICON_ATTRIBUTE_ONE_COLUMN = "Master Page One Column";//$NON-NLS-1$

	/** icon property: attribute two columns */
	String ICON_ATTRIBUTE_TWO_COLUMNS = "Master Page Two Column";//$NON-NLS-1$

	/** icon property: attribute thtree columns */
	String ICON_ATTRIBUTE_THTREE_COLUMNS = "Master Page Three Column";//$NON-NLS-1$

	// Preview editer icons
	/** icon property: preview parameters */
	String ICON_PREVIEW_PARAMETERS = "PreviewParameters"; //$NON-NLS-1$

	/** icon property: preview parameters hide */
	String ICON_PREVIEW_PARAMETERS_HIDE = "PreviewParametersHide"; //$NON-NLS-1$

	/** icon property: preview refresh */
	String ICON_PREVIEW_REFRESH = "RreviewRefresh";//$NON-NLS-1$

	/** icon property: refresh */
	String ICON_REFRESH = "Refresh";//$NON-NLS-1$

	/** icon property: refresh disable */
	String ICON_REFRESH_DISABLE = "DisableRefresh";//$NON-NLS-1$

	/** icon property: toggle breadcrumb */
	String ICON_TOGGLE_BREADCRUMB = "ToggleBreadcrumb";//$NON-NLS-1$

	/** icon property: toggle breadcrumb disable */
	String ICON_TOGGLE_BREADCRUMB_DISABLE = "DisableToggleBreadcrumb";//$NON-NLS-1$

	// Parameter dialog icon
	/** icon property: default */
	String ICON_DEFAULT = "Default"; //$NON-NLS-1$

	/** icon property: default not */
	String ICON_DEFAULT_NOT = "NotDefault"; //$NON-NLS-1$

	/** icon property: dataedit dlg title banner */
	String ICON_DATAEDIT_DLG_TITLE_BANNER = "org.eclipse.birt.report.designer.property"; //$NON-NLS-1$

	// Open file flag image
	/** icon property: open file */
	String ICON_OPEN_FILE = "Open file";//$NON-NLS-1$

	/** icon property: enable restore properties */
	String ICON_ENABLE_RESTORE_PROPERTIES = "Enable Resotre Properties"; //$NON-NLS-1$

	/** icon property: disable restore properties */
	String ICON_DISABLE_RESTORE_PROPERTIES = "Disable Restore Properties"; //$NON-NLS-1$

	/** icon property: enable expression builders */
	String ICON_ENABLE_EXPRESSION_BUILDERS = "Enable Expression Builder"; //$NON-NLS-1$

	/** icon property: disable expression builders */
	String ICON_DISABLE_EXPRESSION_BUILDERS = "Disable Expression Builder"; //$NON-NLS-1$

	/** icon property: enable expression constant */
	String ICON_ENABLE_EXPRESSION_CONSTANT = "Enable Expression Constant"; //$NON-NLS-1$

	/** icon property: disable expression constant */
	String ICON_DISABLE_EXPRESSION_CONSTANT = "Disable Expression Constant"; //$NON-NLS-1$

	/** icon property: enable expression javascript */
	String ICON_ENABLE_EXPRESSION_JAVASCRIPT = "Enable Expression Javascript"; //$NON-NLS-1$

	/** icon property: disable expression javascript */
	String ICON_DISABLE_EXPRESSION_JAVASCRIPT = "Disable Expression Javasciprt"; //$NON-NLS-1$

	// Template preview image
	/** icon property: template no preview */
	String ICON_TEMPLATE_NO_PREVIEW = "no_preview";//$NON-NLS-1$

	/** icon property: scripts node */
	String ICON_SCRIPTS_NODE = "Scripts Node"; //$NON-NLS-1$

	/** icon property: checked */
	String ICON_CHECKED = "Checked";//$NON-NLS-1$

	/** icon property: unchecked */
	String ICON_UNCHECKED = "UnChecked";//$NON-NLS-1$

	/** property: image filter names */
	String[] IMAGE_FILTER_NAMES = { Messages.getString("IReportGraphicConstants.ImageType.All"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Bmp"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Jpg"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Tif"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Gif"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Png"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Icon") //$NON-NLS-1$
	};

	/** property: image filter extensions */
	String[] IMAGE_FILTER_EXTS = { "*.bmp;*.jpg;*.jpeg;*.gif;*.tif;*.png;*.ico", //$NON-NLS-1$
			"*.bmp", //$NON-NLS-1$
			"*.jpg;*.jpeg;", //$NON-NLS-1$
			"*.tif", //$NON-NLS-1$
			"*.gif", //$NON-NLS-1$
			"*.png", //$NON-NLS-1$
			"*.ico" //$NON-NLS-1$
	};

	/** icon property: t key word */
	String REPORT_KEY_WORD = "report"; //$NON-NLS-1$


	// Quick tools aggregation icon
	/** icon property: element aggregation */
	String ICON_ELEMENT_AGGREGATION = "aggregation";//$NON-NLS-1$

	/** icon property: element aggregation large */
	String ICON_ELEMENT_AGGREGATION_LARGE = ICON_ELEMENT_AGGREGATION + "_" + LARGE; //$NON-NLS-1$

	/** icon property: element timeperiod */
	String ICON_ELEMENT_TIMEPERIOD = "relativetime";//$NON-NLS-1$

	/** icon property: element timeperiod large */
	String ICON_ELEMENT_TIMEPERIOD_LARGE = ICON_ELEMENT_TIMEPERIOD + "_" + LARGE; //$NON-NLS-1$

	/** icon property: t layout property */
	String REPORT_LAYOUT_PROPERTY = "layout";//$NON-NLS-1$

	// Level attribute icon
	/** icon property: level attri */
	String ICON_LEVEL_ATTRI = "levelAttribute";//$NON-NLS-1$

	/** icon property: tool filter */
	String ICON_TOOL_FILTER = "resource filter"; //$NON-NLS-1$

	/** icon property: view menu */
	String ICON_VIEW_MENU = "view menu"; //$NON-NLS-1$

	/** icon property: enable export */
	String ICON_ENABLE_EXPORT = "export_enable"; //$NON-NLS-1$

	/** icon property: enable import */
	String ICON_ENABLE_IMPORT = "import_enable"; //$NON-NLS-1$

	/** icon property: disable export */
	String ICON_DISABLE_EXPORT = "export_disable"; //$NON-NLS-1$

	/** icon property: disable import */
	String ICON_DISABLE_IMPORT = "import_edisable"; //$NON-NLS-1$

	// Script icons
	/** icon property: script error */
	String ICON_SCRIPT_ERROR = "Script Error";//$NON-NLS-1$

	/** icon property: script noerror */
	String ICON_SCRIPT_NOERROR = "Script NoError";//$NON-NLS-1$

	/** icon property: script reset */
	String ICON_SCRIPT_RESET = "Script Reset"; //$NON-NLS-1$

	/** icon property: script help */
	String ICON_SCRIPT_HELP = "Script Help"; //$NON-NLS-1$

	/** icon property: report project over */
	String ICON_REPORT_PROJECT_OVER = "Report Project Over";//$NON-NLS-1$

	/** icon property: report local library over */
	String ICON_REPORT_LOCAL_LIBRARY_OVER = "Report Local Library Over";//$NON-NLS-1$

	/** icon property: report library over */
	String ICON_REPORT_LIBRARY_OVER = "Report Library Over";//$NON-NLS-1$

	/** The width of the vertical ruler. */
	int VERTICAL_RULER_WIDTH = 12;


	// Annotation type
	/** icon property: ation error */
	String ANNOTATION_ERROR = "org.eclipse.ui.workbench.texteditor.error"; //$NON-NLS-1$

	/** icon property: scripts method node */
	String ICON_SCRIPTS_METHOD_NODE = "Script Method Node"; //$NON-NLS-1$

	/** icon property: tool calendar */
	String ICON_TOOL_CALENDAR = "Calendar"; //$NON-NLS-1$

	/** icon property: status error */
	String ICON_STATUS_ERROR = "Error"; //$NON-NLS-1$

	// bidi_hcg BiDi-specific property
	/** icon property: t bidiorientation property */
	String REPORT_BIDIORIENTATION_PROPERTY = "bidiLayoutOrientation";//$NON-NLS-1$

	/** icon property: group sort */
	String ICON_GROUP_SORT = "GroupSort"; //$NON-NLS-1$

	/** icon property: local properties */
	String ICON_LOCAL_PROPERTIES = "LocalProperties"; //$NON-NLS-1$

	/** icon property: style modified */
	String ICON_STYLE_MODIFIED = "StyleModified"; //$NON-NLS-1$

	/** icon property: style default */
	String ICON_STYLE_DEFAULT = "StyleDefault"; //$NON-NLS-1$

	/** icon property: style resotre */
	String ICON_STYLE_RESOTRE = "StyleRestore"; //$NON-NLS-1$

	/** icon property: layout auto */
	String ICON_LAYOUT_AUTO = "LayoutAuto"; //$NON-NLS-1$

	/** icon property: layout fixed */
	String ICON_LAYOUT_FIXED = "LayoutFixed"; //$NON-NLS-1$

	/** icon property: layout preference */
	String ICON_LAYOUT_PREFERENCE = "LayoutPreference"; //$NON-NLS-1$

	// add for classpath node
	/** icon property: node variable */
	String ICON_NODE_VARIABLE = "VariableNodel"; //$NON-NLS-1$

	/** icon property: node extjar */
	String ICON_NODE_EXTJAR = "ExtJar"; //$NON-NLS-1$

	/** icon property: node extfol */
	String ICON_NODE_EXTFOL = "ExtFol"; //$NON-NLS-1$

	/** icon property: node jar */
	String ICON_NODE_JAR = "JAR"; //$NON-NLS-1$

	/** icon property: node fol */
	String ICON_NODE_FOL = "FOL"; //$NON-NLS-1$

	// copy/paste format actions
	/** icon property: copy format */
	String ICON_COPY_FORMAT = "CopyFormat"; //$NON-NLS-1$

	/** icon property: paste format */
	String ICON_PASTE_FORMAT = "PasteFormat"; //$NON-NLS-1$

	/** icon property: attribute text hyperlink style none */
	String ICON_ATTRIBUTE_TEXT_HYPERLINK_STYLE = IStyleModel.TEXT_HYPERLINK_STYLE_PROP;
}
