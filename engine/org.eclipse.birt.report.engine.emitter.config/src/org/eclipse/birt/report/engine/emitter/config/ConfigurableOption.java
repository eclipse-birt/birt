/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.config;

/**
 * This class is a representation of configurable option for emitter.
 */
public final class ConfigurableOption implements IConfigurableOption {

	/** The option name. */
	private final String name;

	/** The data type. */
	private DataType dataType = DataType.STRING;

	/** The option type. */
	private DisplayType displayType = DisplayType.TEXT;

	/** The default value. */
	private Object defaultValue;

	/** The choice value list. */
	private IOptionValue[] choices;

	/** The display name. */
	private String displayName;

	/** The option description. */
	private String description;

	/** The tool tip. */
	private String toolTip;

	private boolean enabled = true;

	private String category;

	/**
	 * Constructs a configurable option with the specified name.
	 * 
	 * @param name the option name.
	 */
	public ConfigurableOption(String name) {
		assert name != null;

		this.name = name;
	}

	public ConfigurableOption(String name, DataType dataType, DisplayType displayType, Object defaultValue) {
		this(name);

		setDataType(dataType);
		setDisplayType(displayType);
		setDefaultValue(defaultValue);
	}

	/**
	 * Constructs a configurable option with the all specified params.
	 * 
	 * @param name         the option name.
	 * @param dataType     the data type.
	 * @param displayType  the option type.
	 * @param defaultValue the default value.
	 * @param choices      the chioces list.
	 * @param displayName  the display name.
	 * @param description  the option description.
	 * @param toolTip      the tool tip.
	 */
	public ConfigurableOption(String name, DataType dataType, DisplayType displayType, Object defaultValue,
			IOptionValue[] choices, String displayName, String description, String toolTip) {
		this(name, dataType, displayType, defaultValue);

		setChoices(choices);
		setDescription(description);
		setDisplayName(displayName);
		setToolTip(toolTip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IConfigurableOption#getDataType
	 * ()
	 */
	public DataType getDataType() {
		return dataType;
	}

	/**
	 * Sets data type
	 * 
	 * @param dataType the data type to set.
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IConfigurableOption#
	 * getOptionType()
	 */
	public DisplayType getDisplayType() {
		return displayType;
	}

	/**
	 * Sets option type.
	 * 
	 * @param displayType the option type to set.
	 */
	public void setDisplayType(DisplayType displayType) {
		this.displayType = displayType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IConfigurableOption#
	 * getDefaultValue()
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets default value.
	 * 
	 * @param defaultValue the default value to set.
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IConfigurableOption#getChoices
	 * ()
	 */
	public IOptionValue[] getChoices() {
		return choices;
	}

	/**
	 * Sets choice values
	 * 
	 * @param choices the value list to set.
	 */
	public void setChoices(IOptionValue[] choices) {
		this.choices = choices;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IConfigurableOption#
	 * getDisplayName()
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets display name.
	 * 
	 * @param displayName the display name to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.emitter.config.IConfigurableOption#
	 * getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets option description.
	 * 
	 * @param description the description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IConfigurableOption#getToolTip
	 * ()
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Sets tool tip.
	 * 
	 * @param toolTip the tool tip to set.
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.emitter.config.IConfigurableOption#getName ()
	 */
	public String getName() {
		return name;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
