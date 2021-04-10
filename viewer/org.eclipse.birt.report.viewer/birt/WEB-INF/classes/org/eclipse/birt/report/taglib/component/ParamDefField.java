/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib.component;

import java.io.Serializable;

/**
 * Specifies the html control of report parameter.
 * <p>
 * There are the following parameter attributes:
 * <ol>
 * <li>id</li>
 * <li>name</li>
 * <li>pattern</li>
 * <li>value</li>
 * <li>displayText</li>
 * <li>isLocale</li>
 * <li>title</li>
 * <li>cssClass</li>
 * <li>style</li>
 * </ol>
 */
public class ParamDefField implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6722568811793756664L;

	private String id;
	private String name;
	private String pattern;
	private Object value;
	private String displayText;
	private String isLocale;
	private String title;
	private String cssClass;
	private String style;

	/**
	 * validate parameter
	 * 
	 * @return
	 */
	public boolean validate() {
		return id != null && id.trim().length() > 0 && name != null && name.trim().length() > 0 ? true : false;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the displayText
	 */
	public String getDisplayText() {
		return displayText;
	}

	/**
	 * @param displayText the displayText to set
	 */
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	/**
	 * @return the isLocale
	 */
	public String getIsLocale() {
		return isLocale;
	}

	/**
	 * @param isLocale the isLocale to set
	 */
	public void setIsLocale(String isLocale) {
		this.isLocale = isLocale;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the cssClass
	 */
	public String getCssClass() {
		return cssClass;
	}

	/**
	 * @param cssClass the cssClass to set
	 */
	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	/**
	 * @return the style
	 */
	public String getStyle() {
		return style;
	}

	/**
	 * @param style the style to set
	 */
	public void setStyle(String style) {
		this.style = style;
	}

}
