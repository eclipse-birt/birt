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

package org.eclipse.birt.report.model.api.elements;

/**
 * Defines constants for design element name.
 *
 * @see DesignChoiceConstants
 */

interface IReportDesignConstants {

	String CELL_ELEMENT = "Cell"; //$NON-NLS-1$
	String COLUMN_ELEMENT = "Column"; //$NON-NLS-1$
	String DATA_ITEM = "Data"; //$NON-NLS-1$
	String DATA_SET_ELEMENT = "DataSet"; //$NON-NLS-1$
	String SIMPLE_DATA_SET_ELEMENT = "SimpleDataSet"; //$NON-NLS-1$
	String DATA_SOURCE_ELEMENT = "DataSource"; //$NON-NLS-1$
	String DESIGN_ELEMENT = "DesignElement"; //$NON-NLS-1$
	String ODA_DATA_SET = "OdaDataSet"; //$NON-NLS-1$
	String ODA_DATA_SOURCE = "OdaDataSource"; //$NON-NLS-1$
	String DERIVED_DATA_SET = "DerivedDataSet"; //$NON-NLS-1$
	String EXTENDED_ITEM = "ExtendedItem"; //$NON-NLS-1$
	String FREE_FORM_ITEM = "FreeForm"; //$NON-NLS-1$
	String GRAPHIC_MASTER_PAGE_ELEMENT = "GraphicMasterPage"; //$NON-NLS-1$
	String GRID_ITEM = "Grid"; //$NON-NLS-1$
	String GROUP_ELEMENT = "ListingGroup"; //$NON-NLS-1$
	String IMAGE_ITEM = "Image"; //$NON-NLS-1$
	String LABEL_ITEM = "Label"; //$NON-NLS-1$
	String LIBRARY_ELEMENT = "Library"; //$NON-NLS-1$
	String LINE_ITEM = "Line"; //$NON-NLS-1$
	String LIST_GROUP_ELEMENT = "ListGroup"; //$NON-NLS-1$
	String LIST_ITEM = "List"; //$NON-NLS-1$
	String LISTING_ITEM = "Listing"; //$NON-NLS-1$
	String MASTER_PAGE_ELEMENT = "MasterPage"; //$NON-NLS-1$
	String MODULE_ELEMENT = "Module"; //$NON-NLS-1$
	String PARAMETER_GROUP_ELEMENT = "ParameterGroup"; //$NON-NLS-1$
	String CASCADING_PARAMETER_GROUP_ELEMENT = "CascadingParameterGroup"; //$NON-NLS-1$
	String RECTANGLE_ITEM = "Rectangle"; //$NON-NLS-1$
	String REPORT_DESIGN_ELEMENT = "ReportDesign"; //$NON-NLS-1$
	String REPORT_ITEM = "ReportItem"; //$NON-NLS-1$
	String ROW_ELEMENT = "Row"; //$NON-NLS-1$
	String SCALAR_PARAMETER_ELEMENT = "ScalarParameter"; //$NON-NLS-1$
	String DYNAMIC_FILTER_PARAMETER_ELEMENT = "DynamicFilterParameter"; //$NON-NLS-1$
	String SCRIPT_DATA_SET = "ScriptDataSet"; //$NON-NLS-1$
	String SCRIPT_DATA_SOURCE = "ScriptDataSource"; //$NON-NLS-1$
	String SIMPLE_MASTER_PAGE_ELEMENT = "SimpleMasterPage"; //$NON-NLS-1$
	String STYLE_ELEMENT = "Style"; //$NON-NLS-1$
	String TABLE_GROUP_ELEMENT = "TableGroup"; //$NON-NLS-1$
	String TABLE_ITEM = "Table"; //$NON-NLS-1$
	String TEXT_ITEM = "Text"; //$NON-NLS-1$
	String TEXT_DATA_ITEM = "TextData"; //$NON-NLS-1$
	String THEME_ITEM = "Theme"; //$NON-NLS-1$
	String TEMPLATE_PARAMETER_DEFINITION = "TemplateParameterDefinition"; //$NON-NLS-1$
	String TEMPLATE_ELEMENT = "TemplateElement"; //$NON-NLS-1$
	String TEMPLATE_REPORT_ITEM = "TemplateReportItem"; //$NON-NLS-1$
	String TEMPLATE_DATA_SET = "TemplateDataSet"; //$NON-NLS-1$
	String JOINT_DATA_SET = "JointDataSet"; //$NON-NLS-1$
	String AUTOTEXT_ITEM = "AutoText";//$NON-NLS-1$
	String CUBE_ELEMENT = "Cube"; //$NON-NLS-1$
	String DIMENSION_ELEMENT = "Dimension"; //$NON-NLS-1$
	String HIERARCHY_ELEMENT = "Hierarchy"; //$NON-NLS-1$
	String LEVEL_ELEMENT = "Level"; //$NON-NLS-1$
	String MEASURE_ELEMENT = "Measure"; //$NON-NLS-1$
	String MEASURE_GROUP_ELEMENT = "MeasureGroup"; //$NON-NLS-1$
	String ACCESS_CONTROL = "AccessControl"; //$NON-NLS-1$
	String VALUE_ACCESS_CONTROL = "ValueAccessControl"; //$NON-NLS-1$
	String TABULAR_CUBE_ELEMENT = "TabularCube"; //$NON-NLS-1$
	String TABULAR_DIMENSION_ELEMENT = "TabularDimension"; //$NON-NLS-1$
	String TABULAR_HIERARCHY_ELEMENT = "TabularHierarchy"; //$NON-NLS-1$
	String TABULAR_LEVEL_ELEMENT = "TabularLevel"; //$NON-NLS-1$
	String TABULAR_MEASURE_ELEMENT = "TabularMeasure"; //$NON-NLS-1$
	String TABULAR_MEASURE_GROUP_ELEMENT = "TabularMeasureGroup"; //$NON-NLS-1$
	String ODA_CUBE_ELEMENT = "OdaCube"; //$NON-NLS-1$
	String ODA_DIMENSION_ELEMENT = "OdaDimension"; //$NON-NLS-1$
	String ODA_HIERARCHY_ELEMENT = "OdaHierarchy"; //$NON-NLS-1$
	String ODA_LEVEL_ELEMENT = "OdaLevel"; //$NON-NLS-1$
	String ODA_MEASURE_ELEMENT = "OdaMeasure"; //$NON-NLS-1$
	String ODA_MEASURE_GROUP_ELEMENT = "OdaMeasureGroup"; //$NON-NLS-1$
	String MEMBER_VALUE_ELEMENT = "MemberValue"; //$NON-NLS-1$
	String SORT_ELEMENT = "SortElement"; //$NON-NLS-1$
	String FILTER_CONDITION_ELEMENT = "FilterConditionElement"; //$NON-NLS-1$
	String MULTI_VIEWS = "MultiViews"; //$NON-NLS-1$
	String VARIABLE_ELEMENT = "VariableElement"; //$NON-NLS-1$
	String DATA_GROUP_ELEMENT = "DataGroup"; //$NON-NLS-1$
	String DERIVED_DATA_SET_ELEMENT = "DerivedDataSet"; //$NON-NLS-1$
	String REPORT_ITEM_THEME_ELEMENT = "ReportItemTheme"; //$NON-NLS-1$

	/**
	 * @deprecated by {@link #TEXT_DATA_ITEM}
	 */
	@Deprecated
	String MULTI_LINE_DATA_ITEM = TEXT_DATA_ITEM;

}
