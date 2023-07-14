/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.Collection;

/**
 * Viewer representation of a parameter definition
 *
 */
public class ParameterDefinition {

	// These are copied from IScalarParameterDefinition...

	/** property: text box key */
	public static final int TEXT_BOX = 0;
	/** property: list box key */
	public static final int LIST_BOX = 1;
	/** property: radio button */
	public static final int RADIO_BUTTON = 2;
	/** property: check box key */
	public static final int CHECK_BOX = 3;

	/** property: text alignment auto */
	public static final int AUTO = 0;
	/** property: text alignment left */
	public static final int LEFT = 1;
	/** property: text alignment center */
	public static final int CENTER = 2;
	/** property: text alignment right */
	public static final int RIGHT = 3;

	/** property: parameter data type any */
	public static final int TYPE_ANY = 0;
	/** property: parameter data type string */
	public static final int TYPE_STRING = 1;
	/** property: parameter data type float */
	public static final int TYPE_FLOAT = 2;
	/** property: parameter data type decimal */
	public static final int TYPE_DECIMAL = 3;
	/** property: parameter data type date time */
	public static final int TYPE_DATE_TIME = 4;
	/** property: parameter data type boolean */
	public static final int TYPE_BOOLEAN = 5;
	/** property: parameter data type integer */
	public static final int TYPE_INTEGER = 6;
	/** property: parameter data type date */
	public static final int TYPE_DATE = 7;
	/** property: parameter data type time */
	public static final int TYPE_TIME = 8;

	/** property: parameter selection list none */
	public static final int SELECTION_LIST_NONE = 0;
	/** property: parameter selection list dynamic */
	public static final int SELECTION_LIST_DYNAMIC = 1;
	/** property: parameter selection list static */
	public static final int SELECTION_LIST_STATIC = 2;

	private long id;

	private String name;

	private String category;

	private String pattern;

	private String displayFormat;

	private String displayName;

	private String helpText;

	private String promptText;

	private int dataType;

	private String valueExpr;

	private int controlType;

	private boolean hidden;

	private boolean allowNull;

	private boolean allowBlank;

	private boolean isRequired;

	private boolean mustMatch;

	private boolean concealValue;

	private boolean distinct;

	private boolean isMultiValue;

	private ParameterGroupDefinition group;

	private Collection<?> selectionList;

	/**
	 * Constructor to define the parameter object
	 *
	 * @param id            parameter id
	 * @param name          parameter name
	 * @param category      parameter category
	 * @param pattern       parameter patter
	 * @param displayFormat parameter display format
	 * @param displayName   parameter display name
	 * @param helpText      parameter help text
	 * @param promptText    parameter prompt text
	 * @param dataType      parameter data type
	 * @param valueExpr     parameter value expression
	 * @param controlType   parameter control type
	 * @param hidden        parameter hidden property
	 * @param allowNull     parameter allow null value
	 * @param allowBlank    parameter allow blank
	 * @param isRequired    parameter is required
	 * @param mustMatch     parameter must be match
	 * @param concealValue  parameter conceal value
	 * @param distinct      parameter distinct
	 * @param isMultiValue  parameter can have multiple values
	 * @param group         parameter group
	 * @param selectionList parameter selection list
	 */
	public ParameterDefinition(long id, String name, String category, String pattern, String displayFormat,
			String displayName,
			String helpText, String promptText, int dataType, String valueExpr, int controlType, boolean hidden,
			boolean allowNull, boolean allowBlank, boolean isRequired, boolean mustMatch, boolean concealValue,
			boolean distinct, boolean isMultiValue, ParameterGroupDefinition group, Collection<?> selectionList) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.pattern = pattern;
		this.displayFormat = displayFormat;
		this.displayName = displayName;
		this.helpText = helpText;
		this.promptText = promptText;
		this.dataType = dataType;
		this.valueExpr = valueExpr;
		this.controlType = controlType;
		this.hidden = hidden;
		this.allowNull = allowNull;
		this.allowBlank = allowBlank;
		this.isRequired = isRequired;
		this.mustMatch = mustMatch;
		this.concealValue = concealValue;
		this.distinct = distinct;
		this.isMultiValue = isMultiValue;
		this.group = group;
		this.selectionList = selectionList;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Get the parameter name
	 *
	 * @return Return the parameter name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the parameter category
	 *
	 * @return Return the parameter category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Get the parameter pattern
	 *
	 * @return Return the parameter pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Get the parameter display format
	 *
	 * @return Return the parameter display format
	 */
	public String getDisplayFormat() {
		return displayFormat;
	}

	/**
	 * Get the parameter display name
	 *
	 * @return Return the parameter display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Get the parameter help text
	 *
	 * @return Return the parameter help text
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * Get the parameter prompt text
	 *
	 * @return Return the parameter prompt text
	 */
	public String getPromptText() {
		return promptText;
	}

	/**
	 * Get the parameter data type
	 *
	 * @return Return the parameter data type
	 */
	public int getDataType() {
		return dataType;
	}

	/**
	 * Get the parameter value expression
	 *
	 * @return Return the parameter value expression
	 */
	public String getValueExpr() {
		return valueExpr;
	}

	/**
	 * Get the parameter control type
	 *
	 * @return Return the parameter control type
	 */
	public int getControlType() {
		return controlType;
	}

	/**
	 * Get the parameter is hidden
	 *
	 * @return Return the parameter hidden flag
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Get the allow null value flag
	 *
	 * @deprecated
	 * @return Return the allow null value flag
	 */
	@Deprecated
	public boolean allowNull() {
		return allowNull;
	}

	/**
	 * Get the allow blank value flag
	 *
	 * @deprecated
	 * @return Return the allow blank value flag
	 */
	@Deprecated
	public boolean allowBlank() {
		return allowBlank;
	}

	/**
	 * Get the required flag
	 *
	 * @return Return the required flag
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * Get the must match flag
	 *
	 * @return Return the must match flag
	 */
	public boolean mustMatch() {
		return mustMatch;
	}

	/**
	 * Get the parameter conceal value
	 *
	 * @return Return the parameter conceal value
	 */
	public boolean concealValue() {
		return concealValue;
	}

	/**
	 * Get the parameter distinct flag
	 *
	 * @return Return the parameter distinct flag
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * Is parameter with multiple value flag
	 *
	 * @return the isMultiValue
	 */
	public boolean isMultiValue() {
		return isMultiValue;
	}

	/**
	 * Get the parameter group
	 *
	 * @return Return the parameter group
	 */
	public ParameterGroupDefinition getGroup() {
		return group;
	}

	/**
	 * Get the parameter selection list
	 *
	 * @return Return the parameter selecton list
	 */
	public Collection<?> getSelectionList() {
		return selectionList;
	}

	@Override
	public boolean equals(Object obj) {
		if (name == null || !(obj instanceof ParameterDefinition)) {
			return false;
		}
		ParameterDefinition other = (ParameterDefinition) obj;
		return name.equals(other.getName());
	}

	@Override
	public int hashCode() {
		if (name == null) {
			return 0;
		}
		return name.hashCode();
	}

}
