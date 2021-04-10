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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.util.VersionUtil;

/**
 * This class holds the set of design XML schema element and attribute names.
 * 
 */

interface IDesignSchemaConstants {

	/**
	 * The version of report design.
	 */

	public final static String REPORT_VERSION = "3.2.23"; //$NON-NLS-1$

	/**
	 * The number representation for the current version string.
	 */

	final static int REPORT_VERSION_NUMBER = VersionUtil.VERSION_3_2_23;

	static final String ACCESS_CONTROL_TAG = "access-control"; //$NON-NLS-1$
	static final String AUTO_TEXT_TAG = "auto-text"; //$NON-NLS-1$
	static final String BACKGROUND_ATTACHMENT_ATTRIB = "background-attachment"; //$NON-NLS-1$
	static final String BACKGROUND_COLOR_ATTRIB = "background-color"; //$NON-NLS-1$
	static final String BACKGROUND_IMAGE_ATTRIB = "background-image"; //$NON-NLS-1$
	static final String BACKGROUND_POSITION_X_ATTRIB = "background-position-x"; //$NON-NLS-1$
	static final String BACKGROUND_POSITION_Y_ATTRIB = "background-position-y"; //$NON-NLS-1$
	static final String BACKGROUND_REPEAT_ATTRIB = "background-repeat"; //$NON-NLS-1$
	static final String BASE_ID_ATTRIB = "baseId"; //$NON-NLS-1$
	static final String BODY_TAG = "body"; //$NON-NLS-1$
	static final String BORDER_BOTTOM_COLOR_ATTRIB = "border-bottom-color"; //$NON-NLS-1$
	static final String BORDER_BOTTOM_STYLE_ATTRIB = "border-bottom-style"; //$NON-NLS-1$
	static final String BORDER_BOTTOM_WIDTH_ATTRIB = "border-bottom-width"; //$NON-NLS-1$
	static final String BORDER_LEFT_COLOR_ATTRIB = "border-left-color"; //$NON-NLS-1$
	static final String BORDER_LEFT_STYLE_ATTRIB = "border-left-style"; //$NON-NLS-1$
	static final String BORDER_LEFT_WIDTH_ATTRIB = "border-left-width"; //$NON-NLS-1$
	static final String BORDER_RIGHT_COLOR_ATTRIB = "border-right-color"; //$NON-NLS-1$
	static final String BORDER_RIGHT_STYLE_ATTRIB = "border-right-style"; //$NON-NLS-1$
	static final String BORDER_RIGHT_WIDTH_ATTRIB = "border-right-width"; //$NON-NLS-1$
	static final String BORDER_TOP_COLOR_ATTRIB = "border-top-color"; //$NON-NLS-1$
	static final String BORDER_TOP_STYLE_ATTRIB = "border-top-style"; //$NON-NLS-1$
	static final String BORDER_TOP_WIDTH_ATTRIB = "border-top-width"; //$NON-NLS-1$
	static final String BROWSER_CONTROL_TAG = "browser-control"; //$NON-NLS-1$
	static final String CAN_SHRINK_ATTRIB = "can-shrink"; //$NON-NLS-1$
	static final String CELL_TAG = "cell"; //$NON-NLS-1$
	static final String CHOICE_TAG = "choice"; //$NON-NLS-1$
	static final String COLOR_ATTRIB = "color"; //$NON-NLS-1$
	static final String COLUMN_HEADER_TAG = "column-header"; //$NON-NLS-1$
	static final String COLUMN_HINT_TAG = "column-hint"; //$NON-NLS-1$
	static final String COLUMN_HINTS_TAG = "column-hints"; //$NON-NLS-1$
	static final String COLUMN_TAG = "column"; //$NON-NLS-1$
	static final String COMPONENTS_TAG = "components"; //$NON-NLS-1$
	static final String CONTENTS_TAG = "contents"; //$NON-NLS-1$
	static final String CUBE_TAG = "cube"; //$NON-NLS-1$
	static final String CUBES_TAG = "cubes"; //$NON-NLS-1$
	static final String CUSTOM_COLOR_TAG = "custom-color"; //$NON-NLS-1$
	static final String DATA_GROUP_TAG = "data-group"; //$NON-NLS-1$
	static final String DATA_SETS_TAG = "data-sets"; //$NON-NLS-1$
	static final String DATA_SOURCES_TAG = "data-sources"; //$NON-NLS-1$
	static final String DATA_TAG = "data"; //$NON-NLS-1$
	static final String DEFAULT_VALUE_TAG = "default-value"; //$NON-NLS-1$
	static final String DEFAULT_TAG = "default"; //$NON-NLS-1$
	String DERIVED_DATA_SET_TAG = "derived-data-set"; //$NON-NLS-1$
	static final String DETAIL_TAG = "detail"; //$NON-NLS-1$
	static final String DIMENSION_TAG = "dimension"; //$NON-NLS-1$
	static final String ENCRYPTION_ID_ATTRIB = "encryptionID"; //$NON-NLS-1$
	static final String ENCRYPTED_PROPERTY_TAG = "encrypted-property"; //$NON-NLS-1$
	static final String EX_PROPERTY_TAG = "ex-property"; //$NON-NLS-1$
	static final String EXPRESSION_TAG = "expression"; //$NON-NLS-1$
	static final String EXTENDED_ITEM_TAG = "extended-item"; //$NON-NLS-1$
	static final String EXTENDS_ATTRIB = "extends"; //$NON-NLS-1$
	static final String EXTENSION_ATTRIB = "extension"; //$NON-NLS-1$
	static final String EXTENSION_NAME_ATTRIB = "extensionName"; //$NON-NLS-1$
	static final String EXTENSION_ID_ATTRIB = "extensionID"; //$NON-NLS-1$
	static final String EXTENSION_VERSION_ATTRIB = "extensionVersion"; //$NON-NLS-1$
	static final String FILTER_PARAMETER_TAG = "filter-parameter"; //$NON-NLS-1$
	static final String FONT_FAMILY_ATTRIB = "font-family"; //$NON-NLS-1$
	static final String FONT_SIZE_ATTRIB = "font-size"; //$NON-NLS-1$
	static final String FONT_STYLE_ATTRIB = "font-style"; //$NON-NLS-1$
	static final String FONT_VARIANT_ATTRIB = "font-variant"; //$NON-NLS-1$
	static final String FONT_WEIGHT_ATTRIB = "font-weight"; //$NON-NLS-1$
	static final String FOOTER_TAG = "footer"; //$NON-NLS-1$
	static final String FREE_FORM_TAG = "free-form"; //$NON-NLS-1$
	static final String GRAPHIC_MASTER_PAGE_TAG = "graphic-master-page"; //$NON-NLS-1$
	static final String GRID_TAG = "grid"; //$NON-NLS-1$
	static final String GROUP_TAG = "group"; //$NON-NLS-1$
	static final String HEADER_TAG = "header"; //$NON-NLS-1$
	static final String HEADLINE_TAG = "headline"; //$NON-NLS-1$
	static final String HIERARCHY_TAG = "hierarchy"; //$NON-NLS-1$
	static final String HTML_PROPERTY_TAG = "html-property"; //$NON-NLS-1$
	static final String ID_ATTRIB = "id"; //$NON-NLS-1$
	static final String IMAGE_TAG = "image"; //$NON-NLS-1$
	static final String IMPORT_TAG = "import"; //$NON-NLS-1$
	static final String INCLUDE_LIBRARY_TAG = "include-library"; //$NON-NLS-1$
	static final String INCLUDE_SCRIPT_TAG = "include-script"; //$NON-NLS-1$
	static final String INCLUDE_TAG = "include"; //$NON-NLS-1$
	static final String INCLUDES_TAG = "includes"; //$NON-NLS-1$
	static final String IS_NULL_ATTRIB = "isNull"; //$NON-NLS-1$
	static final String IS_EMPTY_ATTRIB = "isEmpty"; //$NON-NLS-1$
	static final String JOINT_DATA_SET_TAG = "joint-data-set"; //$NON-NLS-1$
	static final String KEY_ATTRIB = "key"; //$NON-NLS-1$
	static final String LABEL_TAG = "label"; //$NON-NLS-1$
	static final String LEVEL_TAG = "level"; //$NON-NLS-1$
	static final String LIBRARY_ATTRIB = "library"; //$NON-NLS-1$
	static final String LIBRARY_TAG = "library"; //$NON-NLS-1$
	static final String LINE_TAG = "line"; //$NON-NLS-1$
	static final String LIST_PARAMETER_TAG = "list-parameter"; //$NON-NLS-1$
	static final String LIST_PROPERTY_TAG = "list-property"; //$NON-NLS-1$
	static final String LIST_TAG = "list"; //$NON-NLS-1$
	static final String LOCALE_ATTRIB = "locale"; //$NON-NLS-1$
	static final String MARGIN_BOTTOM_ATTRIB = "margin-bottom"; //$NON-NLS-1$
	static final String MARGIN_LEFT_ATTRIB = "margin-left"; //$NON-NLS-1$
	static final String MARGIN_RIGHT_ATTRIB = "margin-right"; //$NON-NLS-1$
	static final String MARGIN_TOP_ATTRIB = "margin-top"; //$NON-NLS-1$
	static final String MARGINS_TAG = "margins"; //$NON-NLS-1$
	static final String MASK_ATTRIB = "mask"; //$NON-NLS-1$
	static final String MEASURE_TAG = "measure"; //$NON-NLS-1$
	static final String MEASURE_GROUP_TAG = "measure-group"; //$NON-NLS-1$
	static final String MEMBER_VALUE_TAG = "member-value"; //$NON-NLS-1$
	static final String SORT_ELEMENT_TAG = "sort-element"; //$NON-NLS-1$
	static final String FILTER_CONDITION_TAG = "filter-condition-element"; //$NON-NLS-1$
	static final String MULTI_VIEWS_TAG = "multi-views"; //$NON-NLS-1$
	static final String METHOD_TAG = "method"; //$NON-NLS-1$
	static final String NAME_ATTRIB = "name"; //$NON-NLS-1$
	static final String NUMBER_ALIGN_ATTRIB = "number-align"; //$NON-NLS-1$
	static final String NUMBER_FORMAT_ATTRIB = "number-format"; //$NON-NLS-1$
	static final String ODA_CUBE_TAG = "oda-cube"; //$NON-NLS-1$
	static final String ODA_DATA_SET_TAG = "oda-data-set"; //$NON-NLS-1$
	static final String ODA_DATA_SOURCE_TAG = "oda-data-source"; //$NON-NLS-1$
	static final String ODA_DIMENSION_TAG = "oda-dimension"; //$NON-NLS-1$
	static final String ODA_HIERARCHY_TAG = "oda-hierarchy"; //$NON-NLS-1$
	static final String ODA_LEVEL_TAG = "oda-level"; //$NON-NLS-1$
	static final String ODA_MEASURE_TAG = "oda-measure"; //$NON-NLS-1$
	static final String ODA_MEASURE_GROUP_TAG = "oda-measure-group"; //$NON-NLS-1$
	static final String OVERRIDDEN_VALUES_TAG = "overridden-values"; //$NON-NLS-1$
	static final String PADDING_BOTTOM_ATTRIB = "padding-bottom"; //$NON-NLS-1$
	static final String PADDING_LEFT_ATTRIB = "padding-left"; //$NON-NLS-1$
	static final String PADDING_RIGHT_ATTRIB = "padding-right"; //$NON-NLS-1$
	static final String PADDING_TOP_ATTRIB = "padding-top"; //$NON-NLS-1$
	static final String PAGE_BREAK_AFTER_ATTRIB = "page-break-after"; //$NON-NLS-1$
	static final String PAGE_BREAK_BEFORE_ATTRIB = "page-break-before"; //$NON-NLS-1$
	static final String PAGE_FOOTER_TAG = "page-footer"; //$NON-NLS-1$
	static final String PAGE_HEADER_TAG = "page-header"; //$NON-NLS-1$
	static final String PAGE_SEQUENCE_TAG = "page-sequence"; //$NON-NLS-1$
	static final String PAGE_SETUP_TAG = "page-setup"; //$NON-NLS-1$
	static final String PARAMETER_GROUP_TAG = "parameter-group"; //$NON-NLS-1$
	static final String CASCADING_PARAMETER_GROUP_TAG = "cascading-parameter-group"; //$NON-NLS-1$
	static final String PARAMETER_TAG = "parameter"; //$NON-NLS-1$
	static final String PARAMETERS_TAG = "parameters"; //$NON-NLS-1$
	static final String PROPERTY_DEFN_TAG = "property-defn"; //$NON-NLS-1$
	static final String PROPERTY_MASK_TAG = "property-mask"; //$NON-NLS-1$
	static final String PROPERTY_TAG = "property"; //$NON-NLS-1$
	static final String PROPERTY_VALUE_TAG = "property-value"; //$NON-NLS-1$
	static final String RECTANGLE_TAG = "rectangle"; //$NON-NLS-1$
	static final String REF_ENTRY_TAG = "ref-entry"; //$NON-NLS-1$
	static final String REPORT_ITEMS_TAG = "report-items"; //$NON-NLS-1$
	static final String REPORT_ITEM_THEME_TAG = "report-item-theme"; //$NON-NLS-1$
	static final String REPORT_TAG = "report"; //$NON-NLS-1$
	static final String RESOURCE_KEY_ATTRIB = "resource-key"; //$NON-NLS-1$
	static final String RESOURCE_TAG = "resource"; //$NON-NLS-1$
	static final String ROW_TAG = "row"; //$NON-NLS-1$
	static final String SCALAR_PARAMETER_TAG = "scalar-parameter"; //$NON-NLS-1$
	static final String DYNAMIC_FILTER_PARAMETER_TAG = "dynamic-filter-parameter"; //$NON-NLS-1$
	static final String SCRATCH_PAD_TAG = "scratch-pad"; //$NON-NLS-1$
	static final String SCRIPT_DATA_SET_TAG = "script-data-set"; //$NON-NLS-1$
	static final String SCRIPT_DATA_SOURCE_TAG = "script-data-source"; //$NON-NLS-1$
	static final String SECTION_DISPLAY_ATTRIB = "display"; //$NON-NLS-1$
	static final String SECTION_MASTER_PAGE_ATTRIB = "master-page"; //$NON-NLS-1$
	static final String SECTION_PAGE_BREAK_AFTER_ATTRIB = "page-break-after"; //$NON-NLS-1$
	static final String SECTION_PAGE_BREAK_BEFORE_ATTRIB = "page-break-before"; //$NON-NLS-1$
	static final String SECTION_PAGE_BREAK_INSIDE_ATTRIB = "page-break-inside"; //$NON-NLS-1$
	static final String SECTION_SHOW_LF_BLANK_ATTRIB = "show-if-blank"; //$NON-NLS-1$
	static final String SIMPLE_MASTER_PAGE_TAG = "simple-master-page"; //$NON-NLS-1$
	static final String SIMPLE_PROPERTY_LIST_TAG = "simple-property-list"; //$NON-NLS-1$
	static final String STRING_FORMAT_ATTRIB = "string-format"; //$NON-NLS-1$
	static final String STRUCTURE_TAG = "structure"; //$NON-NLS-1$
	static final String STYLE_TAG = "style"; //$NON-NLS-1$
	static final String STYLES_TAG = "styles"; //$NON-NLS-1$
	static final String TABLE_PARAMETER_TAG = "table-parameter"; //$NON-NLS-1$
	static final String TABLE_TAG = "table"; //$NON-NLS-1$
	static final String TABULAR_CUBE_TAG = "tabular-cube"; //$NON-NLS-1$
	static final String TABULAR_DIMENSION_TAG = "tabular-dimension"; //$NON-NLS-1$
	static final String TABULAR_HIERARCHY_TAG = "tabular-hierarchy"; //$NON-NLS-1$
	static final String TABULAR_LEVEL_TAG = "tabular-level"; //$NON-NLS-1$
	static final String TABULAR_MEASURE_TAG = "tabular-measure"; //$NON-NLS-1$
	static final String TABULAR_MEASURE_GROUP_TAG = "tabular-measure-group"; //$NON-NLS-1$
	static final String TEMPLATE_TAG = "template"; //$NON-NLS-1$
	static final String TEMPLATE_PARAMETER_DEFINITION_TAG = "template-parameter-definition"; //$NON-NLS-1$
	static final String TEMPLATE_PARAMETER_DEFINITIONS_TAG = "template-parameter-definitions"; //$NON-NLS-1$
	static final String TEMPLATE_REPORT_ITEM_TAG = "template-report-item"; //$NON-NLS-1$
	static final String TEMPLATE_DATA_SET_TAG = "template-data-set"; //$NON-NLS-1$
	static final String TEXT_ALIGN_ATTRIB = "text-align"; //$NON-NLS-1$
	static final String TEXT_INDENT_ATTRIB = "text-indent"; //$NON-NLS-1$
	static final String TEXT_DATA_TAG = "text-data"; //$NON-NLS-1$
	static final String TEXT_LETTER_SPACING_ATTRIB = "letter-spacing"; //$NON-NLS-1$
	static final String TEXT_LINE_HEIGHT_ATTRIB = "line-height"; //$NON-NLS-1$
	static final String TEXT_LINE_THROUGH_ATTRIB = "text-line-through"; //$NON-NLS-1$
	static final String TEXT_ORPHANS_ATTRIB = "orphans"; //$NON-NLS-1$
	static final String TEXT_OVERLINE_ATTRIB = "text-overline"; //$NON-NLS-1$
	static final String TEXT_PROPERTY_TAG = "text-property"; //$NON-NLS-1$
	static final String TEXT_TAG = "text"; //$NON-NLS-1$
	static final String TEXT_TRANSFORM_ATTRIB = "text-transform"; //$NON-NLS-1$
	static final String TEXT_UNDERLINE_ATTRIB = "text-underline"; //$NON-NLS-1$
	static final String TEXT_VERTICAL_ALIGN_ATTRIB = "vertical-align"; //$NON-NLS-1$
	static final String TEXT_WHITE_SPACE_ATTRIB = "white-space"; //$NON-NLS-1$
	static final String TEXT_WIDOWS_ATTRIB = "widows"; //$NON-NLS-1$
	static final String TEXT_WORD_SPACING_ATTRIB = "word-spacing"; //$NON-NLS-1$
	static final String THEME_TAG = "theme"; //$NON-NLS-1$
	static final String THEMES_TAG = "themes"; //$NON-NLS-1$
	static final String TOC_TAG = "toc"; //$NON-NLS-1$
	static final String TRANSLATION_TAG = "translation"; //$NON-NLS-1$
	static final String TRANSLATIONS_TAG = "translations"; //$NON-NLS-1$
	static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$
	static final String TYPE_TAG = "type"; //$NON-NLS-1$
	static final String VALUE_ACCESS_CONTROL_TAG = "value-access-control"; //$NON-NLS-1$
	static final String VALUE_TAG = "value"; //$NON-NLS-1$
	static final String VARIABLE_ELEMENT_TAG = "variable-element"; //$NON-NLS-1$
	static final String VERTICAL_ALIGN_ATTRIB = "vertical-align"; //$NON-NLS-1$
	static final String VERSION_ATTRIB = "version"; //$NON-NLS-1$
	static final String VIEW_ACTION_ATTRIB = "viewAction"; //$NON-NLS-1$
	static final String XML_PROPERTY_TAG = "xml-property"; //$NON-NLS-1$
	static final String XMLNS_ATTRIB = "xmlns"; //$NON-NLS-1$
	/**
	 * @deprecated by the {@link #TEXT_DATA_TAG}
	 */

	static final String MULTI_LINE_DATA_TAG = "multi-line-data"; //$NON-NLS-1$

}
