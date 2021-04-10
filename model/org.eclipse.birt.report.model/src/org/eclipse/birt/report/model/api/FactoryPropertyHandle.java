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

package org.eclipse.birt.report.model.api;

import java.math.BigDecimal;

import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * A specialized handle for use when creating the Factory data structures. This
 * handle caches the element definition and property value. It uses specialized
 * property resolution rules:
 * <p>
 * <ul>
 * <li>A property value is either a style property or a non-style property.</li>
 * <li>A non-style property is set if this element, or any of its ancestor
 * elements, provide a value. It is also considered set if the system provides a
 * default value.</li>
 * <li>A property value is considered set only if it is set in the private style
 * of this element or an ancestor element; it is not considered set if it is
 * inherited from a shared style.</li>
 * </ul>
 * <p>
 * The various "get" methods also have special meanings: they convert property
 * values to the format needed by the Factory.
 * 
 */

public class FactoryPropertyHandle extends ElementDetailHandle {

	/**
	 * Handle to the design element.
	 */

	protected ElementPropertyDefn propDefn;

	/**
	 * The cached property value.
	 */

	protected Object value = null;

	/**
	 * Constructs a factory property handle with the given
	 * <code>DesignElementHandle</code> and the definition of the property.
	 * 
	 * @param element handle to the design element. It provides the Factory context.
	 * @param prop    the definition of the property
	 */

	public FactoryPropertyHandle(DesignElementHandle element, ElementPropertyDefn prop) {
		super(element);
		propDefn = prop;
		value = element.getElement().getFactoryProperty(element.getModule(), propDefn);
	}

	/**
	 * Returns the cached factory property value.
	 * 
	 * @return the cached factory property value.
	 * @see org.eclipse.birt.report.model.core.DesignElement#getFactoryProperty(Module,
	 *      ElementPropertyDefn)
	 */

	public Object getValue() {
		return ModelUtil.wrapPropertyValue(getModule(), propDefn, value);
	}

	/**
	 * Tests whether this is a style property.
	 * 
	 * @return <code>true</code> if this is a style property, otherwise
	 *         <code>false</code>.
	 */

	public boolean isStyleProperty() {
		return propDefn.isStyleProperty();
	}

	/**
	 * Tests whether this property value is set for this element. It is set if it is
	 * defined on this element or any of its parents, or in the element's private
	 * style. It is considered unset if it is set on a shared style.
	 * 
	 * @return <code>true</code> if the value is set, <code>false</code> if it is
	 *         not set
	 */

	public boolean isSet() {
		return value != null;
	}

	/**
	 * Returns the property value as an integer.
	 * 
	 * @return The value as an integer. Returns 0 if the value cannot be converted
	 *         to an integer.
	 */

	public int getIntValue() {
		return propDefn.getIntValue(getModule(), value);
	}

	/**
	 * Returns the value as a non-localized string.
	 * 
	 * @return The value as a non-localized string.
	 */

	public String getStringValue() {
		if (value instanceof FormatValue) {
			FormatValue formatValue = (FormatValue) value;
			return formatValue.getPattern();
		}

		return propDefn.getStringValue(getModule(), value);
	}

	/**
	 * Returns the value as a double.
	 * 
	 * @return The value as a double. Returns 0 if the value cannot be converted to
	 *         a double.
	 */

	public double getFloatValue() {
		return propDefn.getFloatValue(getModule(), value);
	}

	/**
	 * Returns the value as a number (BigDecimal).
	 * 
	 * @return The value as a number. Returns <code>null</code> if the value cannot
	 *         be converted to a number.
	 */

	public BigDecimal getNumberValue() {
		return propDefn.getNumberValue(getModule(), value);
	}

	/**
	 * Returns the value as a Boolean.
	 * <p>
	 * Note: This method returns false if the value is unset. It DOES NOT return the
	 * default value for the property. Call <code>isSet</code> before calling this
	 * method to determine if the property is set.
	 * 
	 * @return the value as a boolean. Returns <code>false</code> if the value
	 *         cannot be converted to a boolean, or if the value is not set.
	 */

	public boolean getBooleanValue() {
		return propDefn.getBooleanValue(getModule(), value);
	}

	/**
	 * Gets the CSS color value. This is either a CSS (pre-defined) color name or an
	 * RGB value encoded in CSS format: rgb(r,g,b). Returns <code>null</code> if the
	 * property is not set.
	 * 
	 * @return the color value as a string
	 */

	public String getColorValue() {
		PropertyType type = propDefn.getType();

		// The property type of color with extended choice doesn't exist. So we
		// do not
		// need get value from property definition.

		if (type.getTypeCode() == IPropertyType.COLOR_TYPE)
			return ((ColorPropertyType) type).toCssColor(getModule(), value);
		return null;
	}
}