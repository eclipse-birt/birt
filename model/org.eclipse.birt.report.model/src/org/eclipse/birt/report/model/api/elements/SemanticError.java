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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * The class provides the error code and the element with semantic error. The
 * semantic error has two levels: error and warning. The default level is error.
 */

public class SemanticError extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */

	private static final long serialVersionUID = -285983593958407463L;

	/**
	 * Error code indicating the table has inconsistent column count. The column
	 * count should match the maximum cell count in header, detail, and foot slots.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT = MessageConstants.SEMANTIC_ERROR_INCONSISTENT_TABLE_COL_COUNT;

	/**
	 * Error code indicating the table has inconsistent column count because of drop
	 * effects of some cells. The column count should match the maximum cell count
	 * in header, detail, and foot slots.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT_WITH_DROP = MessageConstants.SEMANTIC_ERROR_INCONSISTENT_TABLE_COL_COUNT_COZ_DROP;

	/**
	 * Error code indicating the grid has inconsistent column count. The column
	 * count should match the maximum cell count in rows.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_GRID_COL_COUNT = MessageConstants.SEMANTIC_ERROR_INCONSISTENT_GRID_COL_COUNT;

	/**
	 * Error code indicating the table has overlapping cells. Cell is forbidden to
	 * overlap other cells.
	 */

	public static final String DESIGN_EXCEPTION_OVERLAPPING_CELLS = MessageConstants.SEMANTIC_ERROR_OVERLAPPING_CELLS;

	/**
	 * Error code indicating the table has a conflict among dropping cells in group
	 * header of the table.
	 */

	public static final String DESIGN_EXCEPTION_INCONSITENT_DROP_HEADINGS = MessageConstants.SEMANTIC_ERROR_INCONSISTENT_DROP_HEADINGS;

	/**
	 * Error code indicating the master page size is invalid. The size should be
	 * positive.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_INVALID_PAGE_SIZE;

	/**
	 * Error code indicating the page size is missing when page type is custom.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_MISSING_PAGE_SIZE;

	/**
	 * Error code indicating page size can not be specified if page type is not
	 * custom.
	 */

	public static final String DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE = MessageConstants.SEMANTIC_ERROR_CANNOT_SPECIFY_PAGE_SIZE;

	/**
	 * Error code indicating the page margin is larger than the whole page.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PAGE_MARGINS = MessageConstants.SEMANTIC_ERROR_INVALID_PAGE_MARGINS;

	/**
	 * Error code indicating the report has no master page.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_MASTER_PAGE = MessageConstants.SEMANTIC_ERROR_MISSING_MASTER_PAGE;

	/**
	 * Error code indicating the columns span outside the page content.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_MULTI_COLUMN = MessageConstants.SEMANTIC_ERROR_INVALID_MULTI_COLUMN;

	/**
	 * Error code indicating the element referred is not found.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_ELEMENT_REF = MessageConstants.SEMANTIC_ERROR_INVALID_ELEMENT_REF;

	/**
	 * Error code indicating the element referred should not be the container or
	 * content of the element.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF = MessageConstants.SEMANTIC_ERROR_INVALID_DATA_BINDING_REF;

	/**
	 * Error code indicating the structure referred is not found.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_STRUCTURE_REF = MessageConstants.SEMANTIC_ERROR_INVALID_STRUCTURE_REF;

	/**
	 * Error code indicating the JDBC select data set has no SQL statement.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_SQL_STMT = MessageConstants.SEMANTIC_ERROR_MISSING_SQL_STMT;

	/**
	 * Error code indicating List or Table can not access any data set.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_DATA_SET = MessageConstants.SEMANTIC_ERROR_MISSING_DATA_SET;

	/**
	 * Error code indicating the image file is not found.
	 *
	 * @deprecated
	 */

	@Deprecated
	public static final String DESIGN_EXCEPTION_IMAGE_FILE_NOT_EXIST = MessageConstants.SEMANTIC_ERROR_IMAGE_FILE_NOT_EXIST;

	/**
	 * Error code indicating the property name of property mask does not be defined
	 * on the element.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PROPERTY_NAME = MessageConstants.SEMANTIC_ERROR_INVALID_PROPERTY_NAME;

	/**
	 * Error code indicating the element is not supported, but implemented in this
	 * release.
	 */

	public static final String DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT = MessageConstants.SEMANTIC_ERROR_UNSUPPORTED_ELEMENT;

	/**
	 * Error code indicating the result set has no result set column.
	 */

	public static final String DESIGN_EXCEPTION_AT_LEAST_ONE_COLUMN = MessageConstants.SEMANTIC_ERROR_AT_LEAST_ONE_COLUMN;

	/**
	 * Error code indicating the table/list has duplicate group name.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME = MessageConstants.SEMANTIC_ERROR_DUPLICATE_GROUP_NAME;

	/**
	 * Error code indicating the custom color name is the same as CSS standard color
	 * name.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME = MessageConstants.SEMANTIC_ERROR_INVALID_CUSTOM_COLOR_NAME;

	/**
	 * Error code indicating the custom color name is duplicate.
	 */

	public static final String DESIGN_EXCEPTION_DUPLICATE_CUSTOM_COLOR_NAME = MessageConstants.SEMANTIC_ERROR_DUPLICATE_CUSTOM_COLOR_NAME;

	/**
	 * The extension name for the extended item is not found in our meta.
	 */

	public static final String DESIGN_EXCEPTION_EXTENSION_NOT_FOUND = MessageConstants.SEMANTIC_ERROR_EXTENSION_NOT_FOUND;

	/**
	 * The extension name for the extended item is not defined.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_EXTENSION = MessageConstants.SEMANTIC_ERROR_MISSING_EXTENSION;

	/**
	 * Error code indicating to copy one property is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_PROPERTY_COPY_FORBIDDEN;

	/**
	 * Error code indicating to copy one row is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_ROW_COPY_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_ROW_COPY_FORBIDDEN;

	/**
	 * Error code indicating to paste one row is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_ROW_PASTE_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_ROW_PASTE_FORBIDDEN;

	/**
	 * Error code indicating to inert and paste one row is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_ROW_INSERTANDPASTE_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_ROW_INSERTANDPASTE_FORBIDDEN;

	/**
	 * Error code indicating to shift one row is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_ROW_INSERT_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_ROW_INSERT_FORBIDDEN;

	/**
	 * Error code indicating to paste one row is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_ROW_SHIFT_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_ROW_SHIFT_FORBIDDEN;

	/**
	 * Error code indicating to copy one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_COLUMN_COPY_FORBIDDEN;

	/**
	 * Error code indicating to paste one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_COLUMN_PASTE_FORBIDDEN;

	/**
	 * Error code indicating to paste one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT = MessageConstants.SEMANTIC_ERROR_COLUMN_PASTE_DIFFERENT_LAYOUT;

	/**
	 * Error code indicating to insert one column is forbidden.
	 */

	public static final String DESIGN_EXCEPTION_COLUMN_INSERT_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_COLUMN_INSERT_FORBIDDEN;

	/**
	 * Error code indicating the a cascading parameter is not typed as "dynamic".
	 */

	public static final String DESIGN_EXCEPTION_INVALID_SCALAR_PARAMETER_TYPE = MessageConstants.SEMANTIC_ERROR_INVALID_SCALAR_PARAMETER_TYPE;

	/**
	 * Error code indicating template parameter definitions have no "type"
	 * information.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_TEMPLATE_PARAMETER_TYPE = MessageConstants.SEMANTIC_ERROR_MISSING_TEMPLATE_PARAMETER_TYPE;

	/**
	 * Error code indicating the structure referred is not found.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_LIBRARY_REFERENCE = MessageConstants.SEMANTIC_ERROR_INVALID_LIBRARY_REFERENCE;

	/**
	 * Error code indicating default element or value element of template parameter
	 * definition is incompatible the "allowedType" property defined or the element
	 * types of default element and value element are not the same.
	 */

	public static final String DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_PARAMETER_TYPE = MessageConstants.SEMANTIC_ERROR_INCONSISTENT_TEMPLATE_PARAMETER_TYPE;

	/**
	 * The property binding refers a non-existing element.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_PROPERTY_BINDING_ID = MessageConstants.SEMANTIC_ERROR_INVALID_PROPERTY_BINDING_ID;

	/**
	 * The element is not in the design tree, so it is forbidden to set the property
	 * binding.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_BINDING_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_PROPERTY_BINDING_FORBIDDEN;

	/**
	 * The master page can not contain table/list in any level.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT = MessageConstants.SEMANTIC_ERROR_INVALID_MASTER_PAGE_CONTEXT_CONTAINMENT;

	/**
	 * Data set is not added into this data set.
	 */

	public static final String DESIGN_EXCEPTION_DATA_SET_MISSED_IN_JOINT_DATA_SET = MessageConstants.SEMANTIC_ERROR_DATA_SET_MISSED_IN_JOINT_DATA_SET;

	/**
	 * Column name of the data item has no corresponding data bindging.
	 */

	public static final String DESIGN_EXCEPTION_MISSING_COLUMN_BINDING = MessageConstants.SEMANTIC_ERROR_MISSING_COLUMN_BINDING;

	/**
	 * The element reference would create a cycle: a extends b extends a.
	 */

	public static final String DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE = MessageConstants.SEMANTIC_ERROR_CIRCULAR_ELEMENT_REFERNECE;

	/**
	 * The simple list property has no definition in the element.
	 */

	public static final String DESIGN_EXCEPTION_INCONSISTENT_DATA_GROUP = MessageConstants.SEMANTIC_ERROR_EXCEPTION_INCONSISTENT_DATA_GROUP;

	public static final String DESIGN_EXCEPTION_INVALID_MANIFEST = MessageConstants.SEMANTIC_ERROR_INVALID_MANIFEST;

	/**
	 * Image reference type is expression, but not both type expression and value
	 * expression are present in the design file.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE;

	/**
	 * Image URL value is empty.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_IMAGE_URL_VALUE = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_IMAGE_URL_VALUE;

	/**
	 * Image Name is empty.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_IMAGE_NAME_VALUE = MessageConstants.DESIGN_PARSER_EXCEPTION_INVALID_IMAGE_NAME_VALUE;

	/**
	 * Empty list property should not have value.
	 */
	public static final String DESIGN_EXCEPTION_VALUE_FORBIDDEN = MessageConstants.SEMANTIC_ERROR_VALUE_FORBIDDEN;

	/**
	 * Tries to compute table's width with no column defined.
	 */
	public static final String DESIGN_EXCEPTION_TABLE_NO_COLUMN_FOUND = MessageConstants.SEMANTIC_ERROR_TABLE_NO_COLUMN_FOUND;

	/**
	 * Tries to compute table's width which has columns with no width defined.
	 */
	public static final String DESIGN_EXCEPTION_TABLE_COLUMN_WITH_NO_WIDTH = MessageConstants.SEMANTIC_ERROR_TABLE_COLUMN_WITH_NO_WIDTH;

	/**
	 * Tries to compute table's width which contains multiple columns in both
	 * absolute unit and relative unit other than percentage.
	 */
	public static final String DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_UNIT_TYPE = MessageConstants.SEMANTIC_ERROR_TABLE_COLUMN_INCONSISTENT_UNIT_TYPE;

	/**
	 * Tries to compute table's width which contains multiple columns in different
	 * relative unit.
	 */
	public static final String DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT = MessageConstants.SEMANTIC_ERROR_TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT;

	/**
	 * Tries to compute table's width which the sum of columns's width is greater
	 * than or equal to 100%.
	 */
	public static final String DESIGN_EXCEPTION_TABLE_COLUMN_ILLEGAL_PERCENTAGE = MessageConstants.SEMANTIC_ERROR_TABLE_COLUMN_ILLEGAL_PERCENTAGE;

	/**
	 * The possible value cannot be specified because of other property values.
	 */

	public static final String DESIGN_EXCEPTION_CANNOT_SPECIFY_VALUE = MessageConstants.SEMANTIC_ERROR_CANNOT_SPECIFY_VALUE;

	/**
	 * Error code indicating that the cube or data set can not be specified for the
	 * multiview.
	 */

	public static final String DESIGN_EXCEPTION_CANNOT_SPECIFY_DATA_OBJECT = MessageConstants.SEMANTIC_ERROR_CANNOT_SPECIFY_DATA_OBJECT;

	/**
	 * The constant for the semantic error.
	 */

	public static final int ERROR = 0;

	/**
	 * The constant for the semantic warning.
	 */

	public static final int WARNING = 1;

	/**
	 * The level for the semantic error. Can be error or warning.
	 */

	private int errorLevel = ERROR;

	/**
	 * Constructs a SemanticError with the default serious level.
	 *
	 * @param element the element causing this semantic error
	 * @param errCode the semantic error code
	 */

	public SemanticError(DesignElement element, String errCode) {
		super(element, errCode);
	}

	/**
	 * Constructs a SemanticError with the default serious level.
	 *
	 * @param element the element causing this semantic error
	 * @param values  value array used for error message
	 * @param errCode the semantic error code
	 */

	public SemanticError(DesignElement element, String[] values, String errCode) {
		super(element, values, errCode);
	}

	/**
	 * Constructs a SemanticError with the specified level.
	 *
	 * @param element the element causing this semantic error
	 * @param errCode the semantic error code
	 * @param level   the level of the error. Can be <code>ERROR</code> or
	 *                <code>WARNING</code>.
	 */

	public SemanticError(DesignElement element, String errCode, int level) {
		super(element, errCode);
		errorLevel = level;
	}

	/**
	 * Constructs a SemanticError with the specified level.
	 *
	 * @param element the element causing this semantic error
	 * @param values  value array used for error message
	 * @param errCode the semantic error code
	 * @param level   the level of the error. Can be <code>ERROR</code> or
	 *                <code>WARNING</code>.
	 */

	public SemanticError(DesignElement element, String[] values, String errCode, int level) {
		super(element, values, errCode);
		errorLevel = level;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT
				|| sResourceKey == DESIGN_EXCEPTION_MISSING_EXTENSION) {
			return ModelMessages.getMessage(sResourceKey, new String[] { element.getElementName() });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_ELEMENT_REF
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_STRUCTURE_REF) {
			assert oaMessageArguments != null;

			return ModelMessages.getMessage(sResourceKey, new String[] { element.getIdentifier(),
					(String) oaMessageArguments[0], (String) oaMessageArguments[1] });
		} else if (sResourceKey == DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME
				|| sResourceKey == DESIGN_EXCEPTION_EXTENSION_NOT_FOUND
				|| sResourceKey == DESIGN_EXCEPTION_MISSING_COLUMN_BINDING) {
			assert oaMessageArguments != null;

			return ModelMessages.getMessage(sResourceKey,
					new String[] { element.getIdentifier(), (String) oaMessageArguments[0] });
		} else if (sResourceKey == DESIGN_EXCEPTION_IMAGE_FILE_NOT_EXIST
				|| sResourceKey == DESIGN_EXCEPTION_INVALID_PROPERTY_NAME
				|| sResourceKey == DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE) {
			assert oaMessageArguments != null;

			return ModelMessages.getMessage(sResourceKey,
					new String[] { (String) oaMessageArguments[0], element.getIdentifier() });
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME
				|| sResourceKey == DESIGN_EXCEPTION_DUPLICATE_CUSTOM_COLOR_NAME
				|| sResourceKey == DESIGN_EXCEPTION_PROPERTY_COPY_FORBIDDEN) {
			assert oaMessageArguments != null;

			return ModelMessages.getMessage(sResourceKey, new String[] { (String) oaMessageArguments[0] });
		} else if (sResourceKey == DESIGN_EXCEPTION_OVERLAPPING_CELLS) {
			assert oaMessageArguments != null;
			if (oaMessageArguments[1] == null || StringUtil.isBlank(oaMessageArguments[1].toString())) {
				return ModelMessages.getMessage(sResourceKey, new String[] { (String) oaMessageArguments[0] });
			}
			return ModelMessages.getMessage(sResourceKey,
					new String[] { (String) oaMessageArguments[0] + " \"" + (String) oaMessageArguments[1] + "\"" }); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (sResourceKey == DESIGN_EXCEPTION_MISSING_DATA_SET) {
			String name = element.getFullName();
			if (StringUtil.isBlank(name)) {
				return ModelMessages.getMessage(sResourceKey, new String[] { "The " + element.getElementName() }); //$NON-NLS-1$
			}
			return ModelMessages.getMessage(sResourceKey,
					new String[] { "The " + element.getElementName() + " \"" + name + "\"" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		} else if (sResourceKey == DESIGN_EXCEPTION_INVALID_PROPERTY_BINDING_ID) {
			assert oaMessageArguments.length == 1;
			return ModelMessages.getMessage(sResourceKey,
					new String[] { element.getIdentifier(), (String) oaMessageArguments[0] });
		} else if (sResourceKey == DESIGN_EXCEPTION_CANNOT_SPECIFY_VALUE) {
			int newArgsLen = 0;

			if (oaMessageArguments != null) {
				newArgsLen = oaMessageArguments.length;
			}

			String[] newArgs = new String[newArgsLen + 1];
			newArgs[0] = element.getIdentifier();
			System.arraycopy(oaMessageArguments, 0, newArgs, 1, newArgsLen);

			return ModelMessages.getMessage(sResourceKey, newArgs);
		} else if (oaMessageArguments != null) {
			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		}

		return ModelMessages.getMessage(sResourceKey, new String[] { element.getIdentifier() });
	}

	/**
	 * Returns the level of the error. The level can be <code>ERROR</code> or
	 * <code>WARNING</code>.
	 *
	 * @return the level of the error
	 */

	public int getErrorLevel() {
		return errorLevel;
	}
}
