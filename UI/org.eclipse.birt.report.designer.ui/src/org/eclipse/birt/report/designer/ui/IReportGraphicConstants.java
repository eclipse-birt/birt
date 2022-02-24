/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * This class defines constants for looking up resources that are available only
 * within the Report Designer UI.
 */
public interface IReportGraphicConstants {
	// Palette large icons
	public final static String LARGE = "LARGE"; //$NON-NLS-1$

	// common icons
	public final static String ICON_NEW_REPORT = "New report"; //$NON-NLS-1$

	public final static String ICON_NEW_TEMPLATE = "New template"; //$NON-NLS-1$

	public final static String ICON_NEW_LIBRARY = "New library"; //$NON-NLS-1$

	public final static String ICON_NEW_FOLDER = "New folder"; //$NON-NLS-1$

	public final static String ICON_REPORT_FILE = "Report"; //$NON-NLS-1$

	public final static String ICON_TEMPLATE_FILE = "Template"; //$NON-NLS-1$

	public final static String ICON_LIBRARY_FILE = "Library"; //$NON-NLS-1$

	public final static String ICON_DOCUMENT_FILE = "Document"; //$NON-NLS-1$

	public final static String ICON_REPORT_LOCK = "Report Lock"; //$NON-NLS-1$

	public final static String ICON_LOCK_MENU = "Report Menu"; //$NON-NLS-1$

	public final static String ICON_QUIK_EDIT = "Quick edit"; //$NON-NLS-1$

	public final static String ICON_REPORT_PERSPECTIVE = "Report perspective"; //$NON-NLS-1$

	public final static String ICON_REPORT_PROJECT = "Report project"; //$NON-NLS-1$

	// element icons, most of them are same as model defined for convenience
	public final static String ICON_ELEMENT_CELL = ReportDesignConstants.CELL_ELEMENT;

	public final static String ICON_ELEMENT_DATA = ReportDesignConstants.DATA_ITEM;

	public final static String ICON_ELEMENT_DATA_LARGE = ICON_ELEMENT_DATA + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_DATA_SET = ReportDesignConstants.DATA_SET_ELEMENT;

	public final static String ICON_ELEMENT_DATA_SOURCE = ReportDesignConstants.DATA_SOURCE_ELEMENT;

	public final static String ICON_ELEMENT_EXTENDED_ITEM = ReportDesignConstants.EXTENDED_ITEM;

	public final static String ICON_ELEMENT_ODA_DATA_SET = ReportDesignConstants.ODA_DATA_SET;

	public final static String ICON_ELEMENT_ODA_DATA_SOURCE = ReportDesignConstants.ODA_DATA_SOURCE;

	public final static String ICON_ELEMENT_DERIVED_DATA_SET = ReportDesignConstants.DERIVED_DATA_SET;

	public final static String ICON_ELEMENT_SCRIPT_DATA_SET = ReportDesignConstants.SCRIPT_DATA_SET;

	public final static String ICON_ELEMENT_SCRIPT_DATA_SOURCE = ReportDesignConstants.SCRIPT_DATA_SOURCE;

	// ReportDesignConstants.DATAMART_DATA_SET,
	// ReportDesignConstants.DATAMART_DATA_SOURCE
	public final static String ICON_ELEMENT_DATAMART_DATA_SET = "DataMartDataSet"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_DATAMART_DATA_SOURCE = "DataMartDataSource"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_JOINT_DATA_SET = ReportDesignConstants.JOINT_DATA_SET;

	public final static String ICON_ELEMENT_GRID = ReportDesignConstants.GRID_ITEM;

	public final static String ICON_ELEMENT_GRID_LARGE = ICON_ELEMENT_GRID + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_GROUP = "Group"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_IMAGE = ReportDesignConstants.IMAGE_ITEM;

	public final static String ICON_ELEMENT_IMAGE_LARGE = ICON_ELEMENT_IMAGE + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_LABEL = ReportDesignConstants.LABEL_ITEM;

	public final static String ICON_ELEMENT_LABEL_LARGE = ICON_ELEMENT_LABEL + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_LINE = ReportDesignConstants.LINE_ITEM;

	public final static String ICON_ELEMENT_LIST = ReportDesignConstants.LIST_ITEM;

	public final static String ICON_ELEMENT_LIST_LARGE = ICON_ELEMENT_LIST + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_LIST_GROUP = ReportDesignConstants.LIST_GROUP_ELEMENT;

	public final static String ICON_ELEMNET_MASTERPAGE = ReportDesignConstants.MASTER_PAGE_ELEMENT;

	public final static String ICON_ELEMNET_GRAPHICMASTERPAGE = ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT;

	public final static String ICON_ELEMENT_PARAMETER = "Parameter"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_PARAMETER_GROUP = ReportDesignConstants.PARAMETER_GROUP_ELEMENT;

	public final static String ICON_ELEMENT_CASCADING_PARAMETER_GROUP = ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT;

	public final static String ICON_ELEMENT_ROW = ReportDesignConstants.ROW_ELEMENT;

	public final static String ICON_ELEMENT_COLUMN = ReportDesignConstants.COLUMN_ELEMENT;

	public final static String ICON_ELEMENT_SCALAR_PARAMETER = ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;

	public final static String ICON_ELEMNET_SIMPLE_MASTERPAGE = ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT;

	public final static String ICON_ELEMENT_STYLE = ReportDesignConstants.STYLE_ELEMENT;

	public final static String ICON_ELEMENT_TABLE = ReportDesignConstants.TABLE_ITEM;

	public final static String ICON_ELEMENT_TABLE_LARGE = ICON_ELEMENT_TABLE + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_TABLE_GROUP = ReportDesignConstants.TABLE_GROUP_ELEMENT;

	public final static String ICON_ELEMENT_TEXT = ReportDesignConstants.TEXT_ITEM;

	public final static String ICON_ELEMENT_TEXT_LARGE = ICON_ELEMENT_TEXT + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_TEXTDATA = ReportDesignConstants.TEXT_DATA_ITEM;

	public final static String ICON_ELEMENT_TEXTDATA_LARGE = ICON_ELEMENT_TEXTDATA + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_ELEMENT_LIBRARY = ReportDesignConstants.LIBRARY_ELEMENT;

	public final static String ICON_ELEMENT_LIBRARY_REFERENCED = "Library Referenced"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_THEME = ReportDesignConstants.THEME_ITEM;

	public final static String ICON_ELEMENT_CSS_STYLE_SHEET = "ReportDesignConstants.CSS_STYLE_SHEET"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_TEMPLATEITEM = ReportDesignConstants.TEMPLATE_REPORT_ITEM;

	public final static String ICON_ELEMENT_VARIABLE = ReportDesignConstants.VARIABLE_ELEMENT;

	public final static String ICON_ELEMENT_VARIABLE_REPORT = ReportDesignConstants.VARIABLE_ELEMENT + "report"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_VARIABLE_PAGE = ReportDesignConstants.VARIABLE_ELEMENT + "page"; //$NON-NLS-1$

	// Library report item icons
	public final static String LINK = "LINK"; //$NON-NLS-1$

	public final static String ICON_ELEMENT_CSS_STYLE_SHEET_LINK = ICON_ELEMENT_CSS_STYLE_SHEET + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_STYLE_LINK = ICON_ELEMENT_STYLE + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_DATA_LINK = ICON_ELEMENT_DATA + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_DATA_SET_LINK = ICON_ELEMENT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_DATA_SOURCE_LINK = ICON_ELEMENT_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_GRID_LINK = ICON_ELEMENT_GRID + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_IMAGE_LINK = ICON_ELEMENT_IMAGE + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_LABEL_LINK = ICON_ELEMENT_LABEL + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_LIST_LINK = ICON_ELEMENT_LIST + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_TABLE_LINK = ICON_ELEMENT_TABLE + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_TEXT_LINK = ICON_ELEMENT_TEXT + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_TEXTDATA_LINK = ICON_ELEMENT_TEXTDATA + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_ODA_DATA_SET_LINK = ReportDesignConstants.ODA_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_DERIVED_DATA_SET_LINK = ReportDesignConstants.DERIVED_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_ODA_DATA_SOURCE_LINK = ReportDesignConstants.ODA_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_SCRIPT_DATA_SET_LINK = ReportDesignConstants.SCRIPT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_ELEMENT_SCRIPT_DATA_SOURCE_LINK = ReportDesignConstants.SCRIPT_DATA_SOURCE + "_" //$NON-NLS-1$
			+ LINK;

	// ReportDesignConstants.DATAMART_DATA_SET,
	// ReportDesignConstants.DATAMART_DATA_SOURCE
	public final static String ICON_ELEMENT_DATAMART_DATA_SET_LINK = ICON_ELEMENT_DATAMART_DATA_SET + "_" + LINK; //$NON-NLS-1$

	public final static String ICON_ELEMENT_DATAMART_DATA_SOURCE_LINK = ICON_ELEMENT_DATAMART_DATA_SOURCE + "_" + LINK; //$NON-NLS-1$

	public final static String ICON_ELEMENT_JOINT_DATA_SET_LINK = ReportDesignConstants.JOINT_DATA_SET + "_" //$NON-NLS-1$
			+ LINK;
	public final static String ICON_SCALAR_PARAMETER_ELEMENT_LINK = ReportDesignConstants.SCALAR_PARAMETER_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_CASCADING_PARAMETER_GROUP_ELEMENT_LINK = ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT
			+ "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_PARAMETER_GROUP_ELEMENT_LINK = ReportDesignConstants.PARAMETER_GROUP_ELEMENT + "_" //$NON-NLS-1$
			+ LINK;

	public final static String ICON_SIMPLE_MASTER_PAGE_ELEMENT_LINK = ReportDesignConstants.SIMPLE_MASTER_PAGE_ELEMENT
			+ "_" //$NON-NLS-1$
			+ LINK;

	// outline view icons
	public final static String ICON_NODE_BODY = "Body"; //$NON-NLS-1$

	public final static String ICON_NODE_MASTERPAGES = "Master Pages"; //$NON-NLS-1$

	public final static String ICON_NODE_STYLES = "Styles"; //$NON-NLS-1$

	public final static String ICON_NODE_HEADER = "Header"; //$NON-NLS-1$

	public final static String ICON_NODE_DETAILS = "Details"; //$NON-NLS-1$

	public final static String ICON_NODE_FOOTER = "Footer"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUPS = "Groups"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUP_HEADER = "Group Header"; //$NON-NLS-1$

	public final static String ICON_NODE_GROUP_FOOTER = "Group Footer"; //$NON-NLS-1$

	public final static String ICON_NODE_LIBRARIES = "Libraries"; //$NON-NLS-1$

	public final static String ICON_NODE_THEMES = "Themes"; //$NON-NLS-1$

	public final static String ICON_NODE_IMAGES = "Images"; //$NON-NLS-1$

	// icons for layout
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

	// missing image icon
	public final static String ICON_MISSING_IMG = "Missing image"; //$NON-NLS-1$

	// add image constants for data explore
	public final static String ICON_DATA_EXPLORER_VIEW = "Data explorer view"; //$NON-NLS-1$

	public final static String ICON_NODE_DATA_SETS = "Data Sets"; //$NON-NLS-1$

	public final static String ICON_NODE_DATA_SOURCES = "Data Sources"; //$NON-NLS-1$

	public final static String ICON_NODE_PARAMETERS = "Parameters"; //$NON-NLS-1$

	public final static String ICON_NODE_VARIABLES = "Variables"; //$NON-NLS-1$

	public final static String ICON_DATA_COLUMN = "DataColumn"; //$NON-NLS-1$

	public final static String ICON_INHERIT_COLUMN = "InheritColumn"; //$NON-NLS-1$

	public final static String ICON_ALPHABETIC_SORT = "AlphabeticSort"; //$NON-NLS-1$
	// auto text icon
	public final static String ICON_AUTOTEXT = "AutoText"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_LARGE = ICON_AUTOTEXT + "_" + LARGE; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_PAGE = "Page"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_DATE = "Date"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CREATEDON = "Created on"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CREATEDBY = "Created by"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_FILENAME = "Filename"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_LAST_PRINTED = "Last printed"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_PAGEXOFY = "Page X of Y"; //$NON-NLS-1$

	public final static String ICON_AUTOTEXT_AUTHOR_PAGE_DATE = "AuthorPageDate";//$NON-NLS-1$

	public final static String ICON_AUTOTEXT_CONFIDENTIAL_PAGE = "ConfidentalPage";//$NON-NLS-1$

	// expression builder icons

	public final static String ICON_EXPRESSION_BUILDER = "Expression Builder";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_DATA_TABLE = "DataTable";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_OPERATOR = "Operator";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_GLOBAL = "Global";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_CONSTRUCTOP = "Constructor";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_METHOD = "Method";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_STATIC_METHOD = "Static Method";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_MEMBER = "Member";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_STATIC_MEMBER = "Static Member";//$NON-NLS-1$

	public final static String ICON_EXPRESSION_VALIDATE = "Validate Expression";//$NON-NLS-1$

	// data wizards
	public final static String ICON_WIZARD_DATASOURCE = "DataSourceBasePage";//$NON-NLS-1$

	public final static String ICON_WIZARD_DATASET = "DataSetBasePage";//$NON-NLS-1$

	public final static String ICON_WIZARDPAGE_DATASETSELECTION = "DataSetSelectionPage";//$NON-NLS-1$

	public final static String ICON_HISTORYTOOLBAR_BACKWARDDISABLED = "BackwardDisabled";//$NON-NLS-1$

	public final static String ICON_HISTORYTOOLBAR_BACKWARDENABLED = "BackwardEnabled";//$NON-NLS-1$

	public final static String ICON_HISTORYTOOLBAR_FORWARDDISABLED = "ForwardDisabled";//$NON-NLS-1$

	public final static String ICON_HISTORYTOOLBAR_FORWARDENABLED = "ForwardEnabled";//$NON-NLS-1$

	// attribute icon constants
	public final static String DIS = "DIS";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_FONT_WIDTH = StyleHandle.FONT_WEIGHT_PROP;

	public final static String ICON_ATTRIBUTE_FONT_STYLE = StyleHandle.FONT_STYLE_PROP;

	public final static String ICON_ATTRIBUTE_TEXT_UNDERLINE = StyleHandle.TEXT_UNDERLINE_PROP;

	public final static String ICON_ATTRIBUTE_TEXT_LINE_THROUGH = StyleHandle.TEXT_LINE_THROUGH_PROP;

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

	public final static String ICON_ATTRIBUTE_TOP_MARGIN = MasterPageHandle.TOP_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_BOTTOM_MARGIN = MasterPageHandle.BOTTOM_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_LEFT_MARGIN = MasterPageHandle.LEFT_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_RIGHT_MARGIN = MasterPageHandle.RIGHT_MARGIN_PROP;

	public final static String ICON_ATTRIBUTE_ONE_COLUMN = "Master Page One Column";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_TWO_COLUMNS = "Master Page Two Column";//$NON-NLS-1$

	public final static String ICON_ATTRIBUTE_THTREE_COLUMNS = "Master Page Three Column";//$NON-NLS-1$

	// Preview editer icons
	public final static String ICON_PREVIEW_PARAMETERS = "PreviewParameters"; //$NON-NLS-1$
	public final static String ICON_PREVIEW_PARAMETERS_HIDE = "PreviewParametersHide"; //$NON-NLS-1$

	public final static String ICON_PREVIEW_REFRESH = "RreviewRefresh";//$NON-NLS-1$

	public final static String ICON_REFRESH = "Refresh";//$NON-NLS-1$

	public final static String ICON_REFRESH_DISABLE = "DisableRefresh";//$NON-NLS-1$

	public final static String ICON_TOGGLE_BREADCRUMB = "ToggleBreadcrumb";//$NON-NLS-1$

	public final static String ICON_TOGGLE_BREADCRUMB_DISABLE = "DisableToggleBreadcrumb";//$NON-NLS-1$

	// Parameter dialog icon
	public final static String ICON_DEFAULT = "Default"; //$NON-NLS-1$

	public final static String ICON_DEFAULT_NOT = "NotDefault"; //$NON-NLS-1$

	public static final String ICON_DATAEDIT_DLG_TITLE_BANNER = "org.eclipse.birt.report.designer.property"; //$NON-NLS-1$

	// Open file flag image
	public static final String ICON_OPEN_FILE = "Open file";//$NON-NLS-1$

	public static final String ICON_ENABLE_RESTORE_PROPERTIES = "Enable Resotre Properties"; //$NON-NLS-1$

	public static final String ICON_DISABLE_RESTORE_PROPERTIES = "Disable Restore Properties"; //$NON-NLS-1$

	public static final String ICON_ENABLE_EXPRESSION_BUILDERS = "Enable Expression Builder"; //$NON-NLS-1$

	public static final String ICON_DISABLE_EXPRESSION_BUILDERS = "Disable Expression Builder"; //$NON-NLS-1$

	public static final String ICON_ENABLE_EXPRESSION_CONSTANT = "Enable Expression Constant"; //$NON-NLS-1$

	public static final String ICON_DISABLE_EXPRESSION_CONSTANT = "Disable Expression Constant"; //$NON-NLS-1$

	public static final String ICON_ENABLE_EXPRESSION_JAVASCRIPT = "Enable Expression Javascript"; //$NON-NLS-1$

	public static final String ICON_DISABLE_EXPRESSION_JAVASCRIPT = "Disable Expression Javasciprt"; //$NON-NLS-1$

	// Template preview image
	public static final String ICON_TEMPLATE_NO_PREVIEW = "no_preview";//$NON-NLS-1$

	public static final String ICON_SCRIPTS_NODE = "Scripts Node"; //$NON-NLS-1$

	public final static String ICON_CHECKED = "Checked";//$NON-NLS-1$
	public final static String ICON_UNCHECKED = "UnChecked";//$NON-NLS-1$

	public final static String[] IMAGE_FILTER_NAMES = { Messages.getString("IReportGraphicConstants.ImageType.All"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Bmp"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Jpg"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Tif"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Gif"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Png"), //$NON-NLS-1$
			Messages.getString("IReportGraphicConstants.ImageType.Icon") //$NON-NLS-1$
	};

	public final static String[] IMAGE_FILTER_EXTS = { "*.bmp;*.jpg;*.jpeg;*.gif;*.tif;*.png;*.ico", //$NON-NLS-1$
			"*.bmp", //$NON-NLS-1$
			"*.jpg;*.jpeg;", //$NON-NLS-1$
			"*.tif", //$NON-NLS-1$
			"*.gif", //$NON-NLS-1$
			"*.png", //$NON-NLS-1$
			"*.ico" //$NON-NLS-1$
	};

	public static final String REPORT_KEY_WORD = "report"; //$NON-NLS-1$

	// Quick tools aggregation icon
	public static final String ICON_ELEMENT_AGGREGATION = "aggregation";//$NON-NLS-1$

	public static final String ICON_ELEMENT_AGGREGATION_LARGE = ICON_ELEMENT_AGGREGATION + "_" + LARGE; //$NON-NLS-1$

	public static final String ICON_ELEMENT_TIMEPERIOD = "relativetime";//$NON-NLS-1$

	public final static String ICON_ELEMENT_TIMEPERIOD_LARGE = ICON_ELEMENT_TIMEPERIOD + "_" + LARGE; //$NON-NLS-1$

	public static final String REPORT_LAYOUT_PROPERTY = "layout";//$NON-NLS-1$

	// Level attribute icon
	public static final String ICON_LEVEL_ATTRI = "levelAttribute";//$NON-NLS-1$

	public final static String ICON_TOOL_FILTER = "resource filter"; //$NON-NLS-1$

	public final static String ICON_VIEW_MENU = "view menu"; //$NON-NLS-1$

	public static final String ICON_ENABLE_EXPORT = "export_enable"; //$NON-NLS-1$

	public static final String ICON_ENABLE_IMPORT = "import_enable"; //$NON-NLS-1$

	public static final String ICON_DISABLE_EXPORT = "export_disable"; //$NON-NLS-1$

	public static final String ICON_DISABLE_IMPORT = "import_edisable"; //$NON-NLS-1$

	// Script icons
	public final static String ICON_SCRIPT_ERROR = "Script Error";//$NON-NLS-1$

	public final static String ICON_SCRIPT_NOERROR = "Script NoError";//$NON-NLS-1$

	public final static String ICON_SCRIPT_RESET = "Script Reset"; //$NON-NLS-1$

	public final static String ICON_SCRIPT_HELP = "Script Help"; //$NON-NLS-1$

	public static final String ICON_REPORT_PROJECT_OVER = "Report Project Over";//$NON-NLS-1$
	public static final String ICON_REPORT_LOCAL_LIBRARY_OVER = "Report Local Library Over";//$NON-NLS-1$
	public static final String ICON_REPORT_LIBRARY_OVER = "Report Library Over";//$NON-NLS-1$

	/** The width of the vertical ruler. */
	public final static int VERTICAL_RULER_WIDTH = 12;

	// Annotation type
	public final static String ANNOTATION_ERROR = "org.eclipse.ui.workbench.texteditor.error"; //$NON-NLS-1$

	public static final String ICON_SCRIPTS_METHOD_NODE = "Script Method Node"; //$NON-NLS-1$

	public static final String ICON_TOOL_CALENDAR = "Calendar"; //$NON-NLS-1$

	public static final String ICON_STATUS_ERROR = "Error"; //$NON-NLS-1$

	// bidi_hcg BiDi-specific property
	public static final String REPORT_BIDIORIENTATION_PROPERTY = "bidiLayoutOrientation";//$NON-NLS-1$

	public static final String ICON_GROUP_SORT = "GroupSort"; //$NON-NLS-1$

	public static final String ICON_LOCAL_PROPERTIES = "LocalProperties"; //$NON-NLS-1$

	public static final String ICON_STYLE_MODIFIED = "StyleModified"; //$NON-NLS-1$

	public static final String ICON_STYLE_DEFAULT = "StyleDefault"; //$NON-NLS-1$

	public static final String ICON_STYLE_RESOTRE = "StyleRestore"; //$NON-NLS-1$

	public static final String ICON_LAYOUT_AUTO = "LayoutAuto"; //$NON-NLS-1$

	public static final String ICON_LAYOUT_FIXED = "LayoutFixed"; //$NON-NLS-1$

	public static final String ICON_LAYOUT_PREFERENCE = "LayoutPreference"; //$NON-NLS-1$

	// add for classpath node
	public static final String ICON_NODE_VARIABLE = "VariableNodel"; //$NON-NLS-1$
	public static final String ICON_NODE_EXTJAR = "ExtJar"; //$NON-NLS-1$
	public static final String ICON_NODE_EXTFOL = "ExtFol"; //$NON-NLS-1$
	public static final String ICON_NODE_JAR = "JAR"; //$NON-NLS-1$
	public static final String ICON_NODE_FOL = "FOL"; //$NON-NLS-1$

	// copy/paste format actions
	public static final String ICON_COPY_FORMAT = "CopyFormat"; //$NON-NLS-1$
	public static final String ICON_PASTE_FORMAT = "PasteFormat"; //$NON-NLS-1$
}
