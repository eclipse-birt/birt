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

package org.eclipse.birt.report.model.parser;

/**
 *
 */

interface ParserSchemaConstants {

	/* Tags for properties */

	int PROPERTY_TAG = DesignSchemaConstants.PROPERTY_TAG.toLowerCase().hashCode();
	int LIST_PROPERTY_TAG = DesignSchemaConstants.LIST_PROPERTY_TAG.toLowerCase().toLowerCase().hashCode();

	int EXPRESSION_TAG = DesignSchemaConstants.EXPRESSION_TAG.toLowerCase().hashCode();
	int XML_PROPERTY_TAG = DesignSchemaConstants.XML_PROPERTY_TAG.toLowerCase().hashCode();

	int STRUCTURE_TAG = DesignSchemaConstants.STRUCTURE_TAG.toLowerCase().hashCode();
	int METHOD_TAG = DesignSchemaConstants.METHOD_TAG.toLowerCase().hashCode();

	int TEXT_PROPERTY_TAG = DesignSchemaConstants.TEXT_PROPERTY_TAG.toLowerCase().hashCode();
	int HTML_PROPERTY_TAG = DesignSchemaConstants.HTML_PROPERTY_TAG.toLowerCase().hashCode();

	int ENCRYPTED_PROPERTY_TAG = DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG.toLowerCase().hashCode();
	int SIMPLE_PROPERTY_LIST_TAG = DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG.toLowerCase().hashCode();
	int EX_PROPERTY_TAG = DesignSchemaConstants.EX_PROPERTY_TAG.toLowerCase().hashCode();

	/* Tags for elements */

	int TEXT_TAG = DesignSchemaConstants.TEXT_TAG.toLowerCase().hashCode();
	int AUTO_TEXT_TAG = DesignSchemaConstants.AUTO_TEXT_TAG.toLowerCase().hashCode();

	int LABEL_TAG = DesignSchemaConstants.LABEL_TAG.toLowerCase().hashCode();
	int DATA_TAG = DesignSchemaConstants.DATA_TAG.toLowerCase().hashCode();

	int LIST_TAG = DesignSchemaConstants.LIST_TAG.toLowerCase().hashCode();
	int TABLE_TAG = DesignSchemaConstants.TABLE_TAG.toLowerCase().hashCode();

	int FREE_FORM_TAG = DesignSchemaConstants.FREE_FORM_TAG.toLowerCase().hashCode();
	int GRID_TAG = DesignSchemaConstants.GRID_TAG.toLowerCase().hashCode();

	int EXTENDED_ITEM_TAG = DesignSchemaConstants.EXTENDED_ITEM_TAG.toLowerCase().hashCode();
	int MULTI_LINE_DATA_TAG = DesignSchemaConstants.MULTI_LINE_DATA_TAG.toLowerCase().hashCode();

	int TEXT_DATA_TAG = DesignSchemaConstants.TEXT_DATA_TAG.toLowerCase().hashCode();

	int TEMPLATE_REPORT_ITEM_TAG = DesignSchemaConstants.TEMPLATE_REPORT_ITEM_TAG.toLowerCase().hashCode();

	int IMAGE_TAG = DesignSchemaConstants.IMAGE_TAG.toLowerCase().hashCode();

	int LIBRARY_TAG = DesignSchemaConstants.LIBRARY_TAG.toLowerCase().hashCode();

	int REPORT_TAG = DesignSchemaConstants.REPORT_TAG.toLowerCase().hashCode();

	int TOC_TAG = DesignSchemaConstants.TOC_TAG.toLowerCase().hashCode();

	int STYLE_TAG = DesignSchemaConstants.STYLE_TAG.toLowerCase().hashCode();
	int THEME_TAG = DesignSchemaConstants.THEME_TAG.toLowerCase().hashCode();
	int REPORT_ITEM_THEME_TAG = DesignSchemaConstants.REPORT_ITEM_THEME_TAG.toLowerCase().hashCode();

	int CELL_TAG = DesignSchemaConstants.CELL_TAG.toLowerCase().hashCode();

	int ROW_TAG = DesignSchemaConstants.ROW_TAG.toLowerCase().hashCode();

	int PARAMETER_GROUP_TAG = DesignSchemaConstants.PARAMETER_GROUP_TAG.toLowerCase().hashCode();
	int SCALAR_PARAMETER_TAG = DesignSchemaConstants.SCALAR_PARAMETER_TAG.toLowerCase().hashCode();
	int DYNAMIC_FILTER_PARAMETER_TAG = DesignSchemaConstants.DYNAMIC_FILTER_PARAMETER_TAG.toLowerCase().hashCode();
	int FILTER_PARAMETER_TAG = DesignSchemaConstants.FILTER_PARAMETER_TAG.toLowerCase().hashCode();
	int LIST_PARAMETER_TAG = DesignSchemaConstants.LIST_PARAMETER_TAG.toLowerCase().hashCode();
	int TABLE_PARAMETER_TAG = DesignSchemaConstants.TABLE_PARAMETER_TAG.toLowerCase().hashCode();

	int TEMPLATE_DATA_SET_TAG = DesignSchemaConstants.TEMPLATE_DATA_SET_TAG.toLowerCase().hashCode();
	int JOINT_DATA_SET_TAG = DesignSchemaConstants.JOINT_DATA_SET_TAG.toLowerCase().hashCode();
	int ODA_DATA_SET_TAG = DesignSchemaConstants.ODA_DATA_SET_TAG.toLowerCase().hashCode();
	int SCRIPT_DATA_SET_TAG = DesignSchemaConstants.SCRIPT_DATA_SET_TAG.toLowerCase().hashCode();
	int DERIVED_DATA_SET_TAG = DesignSchemaConstants.DERIVED_DATA_SET_TAG.toLowerCase().hashCode();
	int EXTENDED_DATA_SET_TAG = "extended-data-set".toLowerCase().hashCode();

	int SCRIPT_DATA_SOURCE_TAG = DesignSchemaConstants.SCRIPT_DATA_SOURCE_TAG.toLowerCase().hashCode();
	int ODA_DATA_SOURCE_TAG = DesignSchemaConstants.ODA_DATA_SOURCE_TAG.toLowerCase().hashCode();
	int EXTENDED_DATA_SOURCE_TAG = "extended-data-source".toLowerCase().hashCode();

	int GRAPHIC_MASTER_PAGE_TAG = DesignSchemaConstants.GRAPHIC_MASTER_PAGE_TAG.toLowerCase().hashCode();
	int SIMPLE_MASTER_PAGE_TAG = DesignSchemaConstants.SIMPLE_MASTER_PAGE_TAG.toLowerCase().hashCode();
	int PAGE_SEQUENCE_TAG = DesignSchemaConstants.PAGE_SEQUENCE_TAG.toLowerCase().hashCode();

	int LINE_TAG = DesignSchemaConstants.LINE_TAG.toLowerCase().hashCode();
	int RECTANGLE_TAG = DesignSchemaConstants.RECTANGLE_TAG.toLowerCase().hashCode();

	/* Tags for slot */

	int CONTENTS_TAG = DesignSchemaConstants.CONTENTS_TAG.toLowerCase().hashCode();

	int HEADER_TAG = DesignSchemaConstants.HEADER_TAG.toLowerCase().hashCode();
	int GROUP_TAG = DesignSchemaConstants.GROUP_TAG.toLowerCase().hashCode();
	int DETAIL_TAG = DesignSchemaConstants.DETAIL_TAG.toLowerCase().hashCode();
	int FOOTER_TAG = DesignSchemaConstants.FOOTER_TAG.toLowerCase().hashCode();

	int COLUMN_TAG = DesignSchemaConstants.COLUMN_TAG.toLowerCase().hashCode();

	int STYLES_TAG = DesignSchemaConstants.STYLES_TAG.toLowerCase().hashCode();

	int PAGE_HEADER_TAG = DesignSchemaConstants.PAGE_HEADER_TAG.toLowerCase().hashCode();
	int PAGE_FOOTER_TAG = DesignSchemaConstants.PAGE_FOOTER_TAG.toLowerCase().hashCode();

	int PARAMETERS_TAG = DesignSchemaConstants.PARAMETERS_TAG.toLowerCase().hashCode();
	int CASCADING_PARAMETER_GROUP_TAG = DesignSchemaConstants.CASCADING_PARAMETER_GROUP_TAG.toLowerCase().hashCode();

	int DATA_SOURCES_TAG = DesignSchemaConstants.DATA_SOURCES_TAG.toLowerCase().hashCode();
	int DATA_SETS_TAG = DesignSchemaConstants.DATA_SETS_TAG.toLowerCase().hashCode();
	int PAGE_SETUP_TAG = DesignSchemaConstants.PAGE_SETUP_TAG.toLowerCase().hashCode();
	int COMPONENTS_TAG = DesignSchemaConstants.COMPONENTS_TAG.toLowerCase().hashCode();
	int BODY_TAG = DesignSchemaConstants.BODY_TAG.toLowerCase().hashCode();
	int SCRATCH_PAD_TAG = DesignSchemaConstants.SCRATCH_PAD_TAG.toLowerCase().hashCode();
	int TEMPLATE_PARAMETER_DEFINITIONS_TAG = DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG.toLowerCase()
			.hashCode();
	int TEMPLATE_PARAMETER_DEFINITION_TAG = DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITION_TAG.toLowerCase()
			.hashCode();

	/* Tags for unsupported */

	int INCLUDE_TAG = DesignSchemaConstants.AUTO_TEXT_TAG.toLowerCase().hashCode();
	int BROWSER_CONTROL_TAG = DesignSchemaConstants.BROWSER_CONTROL_TAG.toLowerCase().hashCode();

	/* Tags for attributes */

	int NAME_ATTRIB = DesignSchemaConstants.NAME_ATTRIB.toLowerCase().hashCode();
	int VALUE_TAG = DesignSchemaConstants.VALUE_TAG.toLowerCase().hashCode();

	int REF_ENTRY_TAG = DesignSchemaConstants.REF_ENTRY_TAG.toLowerCase().hashCode();
}
