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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * This class represents a style mapping rule: a translation of a database value
 * into a set of display values. A map might translate DB status codes (O, S, P)
 * into user-visible strings (Open, Shipped, Paid). The mapping is driven of of
 * a mapping test expression defined on the style. This class extends the
 * <code>StyleRule</code> class, see that class for additional details.
 * 
 */

public class MapRule extends StyleRule {

	/**
	 * Name of the member variable that represents the non-localized text to display
	 * when the rule "fires.".
	 */

	public static final String DISPLAY_MEMBER = "display"; //$NON-NLS-1$

	/**
	 * Name of the member variable that represents the message ID for the localized
	 * text to display when the rule "fires.".
	 */

	public static final String DISPLAY_ID_MEMBER = "displayID"; //$NON-NLS-1$

	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "MapRule"; //$NON-NLS-1$

	/**
	 * The non-localized text to display when the rule "fires.".
	 */

	protected String display = null;

	/**
	 * The message ID for the localized text to display when the rule "fires.".
	 */

	protected String displayKey = null;

	/**
	 * Default constructor.
	 */

	public MapRule() {
	}

	/**
	 * Constructs the map rule with an operator and arguments, message id if the
	 * display value is to be localized and display text if the display value is not
	 * to be localized.
	 * 
	 * @param op        operator. One of the internal choice values identified in
	 *                  the meta-data dictionary
	 * @param v1        the comparison value expressions for operators that take one
	 *                  or two arguments (equals, like, between)
	 * @param v2        the second comparison value for operators that take two
	 *                  arguments (between)
	 * @param testExpre the expression to check
	 * @param id        the message id if the display value is to be localized
	 * @param disp      the display text if the value is not localized
	 */

	public MapRule(String op, String v1, String v2, String testExpre, String id, String disp) {
		super(op, v1, v2, testExpre);
		displayKey = id;
		display = disp;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.PropertyStructure#getIntrinsicProperty(
	 * java.lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (DISPLAY_MEMBER.equals(propName))
			return display;
		else if (DISPLAY_ID_MEMBER.equals(propName))
			return displayKey;

		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.PropertyStructure#setIntrinsicProperty(
	 * java.lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (DISPLAY_MEMBER.equals(propName))
			display = (String) value;
		else if (DISPLAY_ID_MEMBER.equals(propName))
			displayKey = (String) value;
		else
			super.setIntrinsicProperty(propName, value);
	}

	/**
	 * Returns the message ID for the text.
	 * 
	 * @return the message ID for the display text
	 */

	public String getDisplayKey() {
		return (String) getProperty(null, DISPLAY_ID_MEMBER);
	}

	/**
	 * Set the message ID for the text, the text is to be displayed when this rule
	 * applies.
	 * 
	 * @param displayKey the message ID for the text.
	 */

	public void setDisplayKey(String displayKey) {
		setProperty(DISPLAY_ID_MEMBER, displayKey);
	}

	/**
	 * Returns the non-localized display text.
	 * 
	 * @return the non-localized display text
	 */

	public String getDisplay() {
		return (String) getProperty(null, DISPLAY_MEMBER);
	}

	/**
	 * Set the non-localized display text, the text is to be displayed when this
	 * rule applies.
	 * 
	 * @param text the non-localized display text
	 */

	public void setDisplay(String text) {
		setProperty(DISPLAY_MEMBER, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getDefn()
	 */

	public IStructureDefn getDefn() {
		return MetaDataDictionary.getInstance().getStructure(STRUCTURE_NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new MapRuleHandle(valueHandle, index);
	}
}
