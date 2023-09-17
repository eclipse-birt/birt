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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.DimensionPropertyType;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Simplifies working with dimension properties. A dimension property consists
 * of a measure and a dimension. This handle helps assemble and disassemble
 * dimension property values. The dimension property itself can be either a
 * top-level element property, or the member of a property structure.
 * <p>
 * Note that this handle cannot translate a dimension property into a physical
 * dimension. BIRT uses the CSS dimension system and requires a CSS User Agent
 * (UA) to compute the physical layout of a report given a report design. These
 * calculations often require context (to compute relative dimensions) and
 * knowledge of item contents to compute the sizes of items that expand to fit
 * their content.
 * <p>
 * This handle works with individual dimensions, the application-provided UA
 * uses these properties (and information about the overall report design) to
 * produce physical, absolute dimensions.
 * <p>
 * The application generally does not create dimension handles directly. It uses
 * the method in <code>DesignElementHandle</code> to get a dimension handle. For
 * example:
 *
 * <pre>
 * DesignElementHandle elementHandle = element.handle( );
 * &lt;p&gt;
 * DimensionHandle dimensionHandle = elementHandle
 * 		.getDimensionProperty( Style.FONT_SIZE_PROP );
 * </pre>
 *
 * <p>
 * The value of the dimension can be a standard format such as 1pt, 100% etc.
 * This kind of value represents a standard dimension, or it can be a CSS
 * (predefined) value such as XX-SMALL, X-SMALL. The CSS values are defined in
 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}.
 *
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public class DimensionHandle extends ComplexValueHandle {

	/**
	 * Constructs a dimension handle for a member of a structure. This member must
	 * be a dimension type.
	 *
	 * @param element the design element handle
	 * @param context the context for the member property
	 */

	public DimensionHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
		assert context.getPropDefn().getType() instanceof DimensionPropertyType;
	}

	/**
	 * Constructs a dimension handle for a member of a structure. This member must
	 * be a dimension type.
	 *
	 * @param element the design element handle
	 * @param context the context for the member property
	 * @deprecated
	 */

	@Deprecated
	public DimensionHandle(DesignElementHandle element, MemberRef context) {
		super(element, context);
		assert context.getPropDefn().getType() instanceof DimensionPropertyType;
	}

	/**
	 * Constructs a dimension handle for a element property. This property must be a
	 * dimension type.
	 *
	 * @param element     handle to the element that defined the property.
	 * @param thePropDefn definition of the dimension property.
	 */

	public DimensionHandle(DesignElementHandle element, ElementPropertyDefn thePropDefn) {
		super(element, thePropDefn);
		assert thePropDefn.getType() instanceof DimensionPropertyType;
	}

	/**
	 * Determines if the dimension is given by a standard format or by a pre-defined
	 * constant.
	 *
	 * @return <code>true</code> if the dimension is given by an pre-defined
	 *         constant <code>false</code> if the dimension is given by a standard
	 *         dimension.
	 */

	public boolean isKeyword() {
		IChoiceSet choiceSet = propDefn.getChoices();
		if (choiceSet != null && choiceSet.contains(getStringValue())) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the numeric measure part of the dimension. For example, if the
	 * dimension value is "2.3cm", the measure is 2.3.
	 *
	 * @return the numeric measure of the dimension, return <code>0.0</code> if the
	 *         dimension from an choice.
	 */

	public double getMeasure() {
		if (isKeyword()) {
			// Map a pre-defined choice to a zero value.
			return 0.0;
		}

		DimensionValue value = (DimensionValue) getValue();
		if (value != null) {
			return value.getMeasure();
		}

		return 0.0;
	}

	/**
	 * Returns an array of allowed units. The set of allowed units depends on
	 * context, not all properties allow all units.
	 *
	 * @return an array of allowed unit suffixes. Each suffix is a string.
	 */

	public IChoice[] getAllowedUnits() {
		if (memberContext == null) {
			return propDefn.getAllowedUnits().getChoices();
		}

		return memberContext.getPropDefn().getAllowedUnits().getChoices();
	}

	/**
	 * Returns the code for the units portion of the dimension. For example, if the
	 * dimension value is "2.3cm", then the unit is "cm".
	 *
	 * @return the units portion of the dimension. Return
	 *         <code>DimensionValue.DEFAULT_UNIT</code> if the dimension is a
	 *         predefined constant.
	 */

	public String getUnits() {
		if (isKeyword()) {
			return DimensionValue.DEFAULT_UNIT;
		}

		DimensionValue value = (DimensionValue) getValue();

		if (value != null) {
			return value.getUnits();
		}
		return null;
	}

	/**
	 * Sets the value of a dimension in default units. The default unit may be
	 * defined by the property in BIRT or the application unit defined in the design
	 * session.
	 *
	 * @param value the new value in application units.
	 * @throws SemanticException if the property is locked
	 */

	public void setAbsolute(double value) throws SemanticException {
		setValue(Double.valueOf(value));
	}

	/**
	 * Returns the absolute dimension value with the following units.
	 * <ul>
	 * <li>UNITS_IN
	 * <li>UNITS_CM
	 * <li>UNITS_MM
	 * <li>UNITS_PT
	 * <li>UNITS_PC
	 * <li>UNITS_PX
	 * </ul>
	 *
	 * This method tries to get the absolute value for absolute font size constants
	 * with {@link IAbsoluteFontSizeValueProvider}. Only the value of CSS property
	 * of the element which is not style is handled here. <code>null</code> is
	 * returned if this dimension is not CSS style property.
	 *
	 * <p>
	 * CSS 2.1 specification has the following statements:
	 * <p>
	 * Child elements do not inherit the relative values specified for their parent;
	 * they inherit the computed values.
	 * <p>
	 * Example(s):
	 * <p>
	 * In the following rules, the computed 'text-indent' value of "h1" elements
	 * will be 36px, not 45px, if "h1" is a child of the "body" element.
	 * <p>
	 * body { font-size: 12px; text-indent: 3em; /* i.e., 36px}}
	 * <p>
	 * h1 { font-size: 15px }
	 * <p>
	 *
	 * So when computing the value of text-indent, with this method, the value of
	 * font-size is retrieved from body, instead of h1.
	 *
	 * @return the absolute dimension value.
	 */

	public DimensionValue getAbsoluteValue() {
		// The original value, which might be relative, absolute or predefined
		// choice.

		Object value = getValue();

		if (value instanceof String) {
			// Only font size has string constant, which are relative or
			// absolute font size constants.

			if (IStyleModel.FONT_SIZE_PROP.equals(getDefn().getName())) {
				FontSizeValueHandler fontSizeValueHandle = new FontSizeValueHandler(this);
				return fontSizeValueHandle.convertFontSizeConstant((String) value);
			}
		} else {
			assert value instanceof DimensionValue || value == null;

			DimensionValue dimensionValue = (DimensionValue) value;

			// If the value is absolute value.

			if (dimensionValue != null && CSSLengthValueHandler.isAbsoluteUnit(dimensionValue.getUnits())) {
				return dimensionValue;
			}

			// Only the relative value of CSS property in the non-style
			// element is handled here.

			if (!propDefn.isStyleProperty() || getElement().isStyle()) {
				return null;
			}

			// Font size has its specific algorithm for getting absolute value.

			if (IStyleModel.FONT_SIZE_PROP.equalsIgnoreCase(getPropertyDefn().getName())) {
				FontSizeValueHandler fontSizeValueHandler = new FontSizeValueHandler(this);
				return fontSizeValueHandler.getAbsoluteValueForFontSize(dimensionValue);
			}

			ComputedValueHandler computedValueHandle = new ComputedValueHandler(this);
			return computedValueHandle.getAbsoluteValueForLength(dimensionValue);
		}

		assert false;
		return null;
	}

	/**
	 * Gets the default unit of the property.
	 *
	 * @return the default unit
	 */

	public String getDefaultUnit() {
		ElementPropertyDefn defn = (ElementPropertyDefn) getPropertyDefn();
		if (defn.getTypeCode() == IPropertyType.DIMENSION_TYPE) {
			String unit = defn.getDefaultUnit();
			if (!StringUtil.isBlank(unit)) {
				return unit;
			}

			unit = getModule().getUnits();
			if (!StringUtil.isBlank(unit)) {
				return unit;
			}

			if (getModule().getSession() != null) {
				return getModule().getSession().getUnits();
			}
		}
		return DimensionValue.DEFAULT_UNIT;
	}
}
