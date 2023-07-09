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
	public static final int TEXT_BOX = 0;
	public static final int LIST_BOX = 1;
	public static final int RADIO_BUTTON = 2;
	public static final int CHECK_BOX = 3;

	public static final int AUTO = 0;
	public static final int LEFT = 1;
	public static final int CENTER = 2;
	public static final int RIGHT = 3;

	public static final int TYPE_ANY = 0;
	public static final int TYPE_STRING = 1;
	public static final int TYPE_FLOAT = 2;
	public static final int TYPE_DECIMAL = 3;
	public static final int TYPE_DATE_TIME = 4;
	public static final int TYPE_BOOLEAN = 5;
	public static final int TYPE_INTEGER = 6;
	public static final int TYPE_DATE = 7;
	public static final int TYPE_TIME = 8;

	public static final int SELECTION_LIST_NONE = 0;
	public static final int SELECTION_LIST_DYNAMIC = 1;
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

	private Collection selectionList;

	public ParameterDefinition(long id, String name, String category, String pattern, String displayFormat,
			String displayName,
			String helpText, String promptText, int dataType, String valueExpr, int controlType, boolean hidden,
			boolean allowNull, boolean allowBlank, boolean isRequired, boolean mustMatch, boolean concealValue,
			boolean distinct, boolean isMultiValue, ParameterGroupDefinition group, Collection selectionList) {
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

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public String getPattern() {
		return pattern;
	}

	public String getDisplayFormat() {
		return displayFormat;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getHelpText() {
		return helpText;
	}

	public String getPromptText() {
		return promptText;
	}

	public int getDataType() {
		return dataType;
	}

	public String getValueExpr() {
		return valueExpr;
	}

	public int getControlType() {
		return controlType;
	}

	public boolean isHidden() {
		return hidden;
	}

	/**
	 * @deprecated
	 * @return
	 */
	@Deprecated
	public boolean allowNull() {
		return allowNull;
	}

	/**
	 * @deprecated
	 * @return
	 */
	@Deprecated
	public boolean allowBlank() {
		return allowBlank;
	}

	public boolean isRequired() {
		return isRequired;
	}

	public boolean mustMatch() {
		return mustMatch;
	}

	public boolean concealValue() {
		return concealValue;
	}

	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * @return the isMultiValue
	 */
	public boolean isMultiValue() {
		return isMultiValue;
	}

	public ParameterGroupDefinition getGroup() {
		return group;
	}

	public Collection getSelectionList() {
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
