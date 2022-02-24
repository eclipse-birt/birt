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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Handle to work with a color property. Works with element properties and
 * structure members.
 * <p>
 * The application generally does not create color handles directly. It uses the
 * method in other handle to get a color handle like:
 * <p>
 * A color property may be in the following format:
 * <ul>
 * <li>A hexadecimal number in Java or HTML format: "#rrggbb", "#rgb" or
 * "0xRRGGBB"</li>
 * <li>A decimal number: "16711680"</li>
 * <li>A CSS color name: "Red", "Green".</li>
 * <li>A localized CSS color name.</li>
 * <li>CSS absolute or relative format: {rgb(r,g,b)} or {rgb(r%,g%,b%)}</li>
 * <li>A custom defined color.</li>
 * </ul>
 * 
 * <pre>
 * 
 * ColorHandle colorHandle = styleHandle.getColor();
 * </pre>
 * 
 * <p>
 * Use {@link ColorUtil#format(int, int)}and
 * {@link ColorUtil#format(String, int)}to do the formatting work on a color
 * value.
 * 
 * @see ColorUtil
 * @see ColorPropertyType
 */

public class ColorHandle extends ComplexValueHandle {

	/**
	 * Cached meta information about color property.
	 */

	private static final ColorPropertyType type = (ColorPropertyType) MetaDataDictionary.getInstance()
			.getPropertyType(IPropertyType.COLOR_TYPE);

	/**
	 * Constructs a color handle for a member in a structure. This member must be a
	 * color type.
	 * 
	 * @param element the design element handle
	 * @param context the context for the member property
	 */

	public ColorHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
		assert context.getPropDefn().getType() instanceof ColorPropertyType;
	}

	/**
	 * Constructs a color handle for a member in a structure. This member must be a
	 * color type.
	 * 
	 * @param element the design element handle
	 * @param context the context for the member property
	 * @deprecated
	 */

	public ColorHandle(DesignElementHandle element, MemberRef context) {
		super(element, context);
		assert context.getPropDefn().getType() instanceof ColorPropertyType;
	}

	/**
	 * Constructs a color handle for a element property. This property must be a
	 * color type.
	 * 
	 * @param element     handle to the element that defined the property.
	 * @param thePropDefn definition of the color property.
	 */

	public ColorHandle(DesignElementHandle element, ElementPropertyDefn thePropDefn) {
		super(element, thePropDefn);

		assert thePropDefn.getType() instanceof ColorPropertyType;
	}

	/**
	 * Returns the color value as an integer RGB value. Return <code>-1</code> if
	 * color value is not set( value is <code>null</code>) or the <code>value</code>
	 * is not a valid internal value for a color.
	 * 
	 * @return the color value as an integer RGB value
	 * @see ColorPropertyType#toInteger(Module, Object)
	 */

	public int getRGB() {
		return type.toInteger(getModule(), getValue());
	}

	/**
	 * Sets a color with a given integer RGB value.
	 * 
	 * @param rgbValue rgb color value.
	 * @throws SemanticException if the rgb value is invalid.
	 */

	public void setRGB(int rgbValue) throws SemanticException {
		setValue(Integer.valueOf(rgbValue));
	}

	/**
	 * Returns a CSS-compatible color value. It is a CSS-defined color name like
	 * "red", or a CSS absolute RGB value like RGB(255,0,0).
	 * 
	 * @return a CSS-compatible color value
	 * @see ColorPropertyType#toCSSCompatibleColor(ReportDesign, Object)
	 */

	public String getCssValue() {
		return type.toCSSCompatibleColor(getModule(), getValue());
	}

	/**
	 * Returns a list containing all the names of both standard(CSS) colors or user
	 * defined colors. The color names are localized.
	 * 
	 * @return a list of localized color names, including both standard(CSS) colors
	 *         and user defined colors.
	 * 
	 */

	public List getColors() {
		List retList = getCSSColors();

		List colors = getModule().getListProperty(getModule(), IModuleModel.COLOR_PALETTE_PROP);
		if (colors == null)
			return retList;

		for (int i = 0; i < colors.size(); i++) {
			CustomColor customColor = (CustomColor) colors.get(i);
			retList.add(customColor.getDisplayName(getModule()));
		}

		return retList;
	}

	/**
	 * Returns a list containing standard(CSS) color names. The names are localized.
	 * 
	 * @return a list of localized CSS color names.
	 * 
	 */

	public List getCSSColors() {
		ArrayList retList = new ArrayList();

		IChoice[] colors = type.getChoices().getChoices();
		for (int i = 0; i < colors.length; i++) {
			retList.add(colors[i].getDisplayName());
		}

		return retList;
	}
}
