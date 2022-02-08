/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.metadata;

/**
 * Meta-data information for a system-defined property. System defined
 * properties can be <em>intrinsic</em>, that is, they are stored as member
 * variables in the Java class.
 * <p>
 * System properties also can be implicitly defined. If an element has a style,
 * then the element implicitly has all the properties defined on the style.
 * Since some style properties do not apply to some elements, filtering is used
 * to define which style properties do apply. Only applicable properties appear
 * as (implicit) properties of the element. This is handled at the meta-data
 * level so it does not need to be computed for each property operation.
 * 
 */

public class SystemPropertyDefn extends ElementPropertyDefn {
	/**
	 * Whether this is a style property. True for any property defined by a style
	 * for use by elements. Also true for properties copied onto an element from a
	 * style.
	 */

	protected boolean styleProperty = false;

	/**
	 * Whether this is a bidi property.
	 */

	protected boolean isBidiProperty = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#isStyleProperty()
	 */

	public boolean isStyleProperty() {
		return styleProperty;
	}

	/**
	 * Sets the style property attribute.
	 * 
	 * @param flag true if this is a property on a style, false otherwise
	 */

	void setStyleProperty(boolean flag) {
		styleProperty = flag;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */

	public int getValueType() {
		return SYSTEM_PROPERTY;
	}

	/**
	 * Sets the style property attribute.
	 * 
	 * @param flag true if this is a property on a style, false otherwise
	 */

	void setBidiProperty(boolean flag) {
		isBidiProperty = flag;
	}

	/**
	 * Indicates whether the property is associated with a bidi definition.
	 * 
	 * @return Whether the property is defined with Bidi for the purpose of being
	 *         used by elements.
	 */

	boolean isBidiProperty() {
		return isBidiProperty;
	}

}
