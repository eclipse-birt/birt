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
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;

/**
 * Represents the handle of custom color. The developer can define a set of
 * custom color names as part of the design. The developer can then reference
 * these names within properties.
 * <p>
 * Every custom color has three parts: a display name, an internal name and a
 * color value.
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a custom color has a unique and required name, so the user can use the
 * color name to identify a custom color.</dd>
 * <dt><strong>color </strong></dt>
 * <dd>The color value of the color, it is preserved as the user typed in.</dd>
 * <dt><strong>Resource Key </strong></dt>
 * <dd>a custom color has an optional display name ID to localize the display
 * name.
 * </dl>
 *
 * <p>
 * The color value can contain any of the valid color representations:
 * <ul>
 * <li>INT_FORMAT: An integer with a valid RGB color value.</li>
 * <li>HTML_FORMAT: ( #RRGGBB or #RGB )</li>
 * <li>JAVA_FORMAT: ( 0xRRGGBB )</li>
 * <li>CSS_ABSOLUTE_FORMAT: ( RGB(r,g,b) )</li>
 * <li>CSS_RELATIVE_FORMAT: ( RGB(r%,g%,b%) )</li>
 * </ul>
 * <p>
 * If the color value is in one of the format, then it is meaningful to retrieve
 * it as integer RGB value. Otherwise the color value is treat just as what the
 * user has entered, and can not be converted into an RGB value.
 * <p>
 * Use {@link ColorUtil#format(int, int)}and
 * {@link ColorUtil#format(String, int)}to do the formatting work on a color
 * value.
 *
 * @see ColorUtil
 */

public class CustomColorHandle extends StructureHandle {

	/**
	 * Constructs the handle of custom color.
	 *
	 * @param valueHandle the value handle for custom color list of one property
	 * @param index       the position of this custom color in the list
	 */

	public CustomColorHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the internal display name.
	 *
	 * @return the internal display name
	 */

	public String getDisplayName() {
		return getStringProperty(CustomColor.DISPLAY_NAME_MEMBER);
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the display name to set
	 */

	public void setDisplayName(String displayName) {
		setPropertySilently(CustomColor.DISPLAY_NAME_MEMBER, displayName);
	}

	/**
	 * Returns the resource key for display name.
	 *
	 * @return the resource key for display name.
	 */

	public String getDisplayNameID() {
		return getStringProperty(CustomColor.DISPLAY_NAME_ID_MEMBER);
	}

	/**
	 * Sets the resource key for display name.
	 *
	 * @param displayNameID the resource key for display name
	 */

	public void setDisplayNameID(String displayNameID) {
		setPropertySilently(CustomColor.DISPLAY_NAME_ID_MEMBER, displayNameID);
	}

	/**
	 * Returns the custom color name.
	 *
	 * @return the custom color name
	 */

	public String getName() {
		return getStringProperty(CustomColor.NAME_MEMBER);
	}

	/**
	 * Returns the color value as what the user has input.
	 *
	 * @return the color value as what the user has input.
	 */

	public String getColor() {
		return getStringProperty(CustomColor.COLOR_MEMBER);
	}

	/**
	 * Sets the color value.
	 *
	 * @param colorValue the color value to be set.
	 * @throws SemanticException value required exception
	 *
	 */

	public void setColor(String colorValue) throws SemanticException {
		setProperty(CustomColor.COLOR_MEMBER, colorValue);
	}

	/**
	 * Sets the custom color name.
	 *
	 * @param name the custom color name to set
	 * @throws SemanticException value required exception
	 */

	public void setName(String name) throws SemanticException {
		setProperty(CustomColor.NAME_MEMBER, name);
	}

	/**
	 * Returns the color value as an integer RGB value. If the color value is of a
	 * valid color representation, then return its numeric RGB value as integer.
	 * Otherwise, return <code>-1</code> indicates that the value is not valid.
	 * <p>
	 * The color value can contain any of the valid color representations:
	 * <ul>
	 * <li>INT_FORMAT: An integer with a valid RGB color value.</li>
	 * <li>HTML_FORMAT: ( #RRGGBB or #RGB )</li>
	 * <li>JAVA_FORMAT: ( 0xRRGGBB )</li>
	 * <li>CSS_ABSOLUTE_FORMAT: ( RGB(r,g,b) )</li>
	 * <li>CSS_RELATIVE_FORMAT: ( RGB(r%,g%,b%) )</li>
	 * </ul>
	 *
	 *
	 * @return the color value as an integer RGB value. Return <code>-1</code> if
	 *         the color value is not valid or the color value is <code>null</code>.
	 */

	public int getRGB() {
		return ((CustomColor) getStructure()).getRGB();
	}

	/**
	 * Returns the qualified name of this element. The qualified name is the name of
	 * this element if this element is in module user is editing.
	 *
	 * @return the qualified name of thie element.
	 */

	public String getQualifiedName() {

		if (getName() == null) {
			return null;
		}

		Module module = getModule();
		if (module instanceof Library) {
			String namespace = ((Library) module).getNamespace();
			return StringUtil.buildQualifiedReference(namespace, getName());
		}

		return getName();
	}

}
