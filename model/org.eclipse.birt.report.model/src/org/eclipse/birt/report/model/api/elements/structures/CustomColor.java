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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.List;

import org.eclipse.birt.report.model.api.CustomColorHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Represents an custom color in the report's color palette.The use-defined
 * color has a unique name and a valid RGB value for render.
 * <p>
 * This is a managed object, meaning that all changes should be made though the
 * command layer so that they can be undone and redone. Each custom color has
 * the following properties:
 *
 * <p>
 * <dl>
 * <dt><strong>Name </strong></dt>
 * <dd>a custom color has a unique and required name, so the user can use the
 * color name to identify a custom color.</dd>
 *
 * <dt><strong>color </strong></dt>
 * <dd>The color value of the color, it is preserved as the user typed in.</dd>
 *
 * <dt><strong>Resource Key </strong></dt>
 * <dd>a custom color has an optional display name ID to localize the display
 * name.</dd>
 * </dl>
 *
 */

public class CustomColor extends ReferencableStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String CUSTOM_COLOR_STRUCT = "CustomColor"; //$NON-NLS-1$

	/**
	 * Name of the color name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the color value member.
	 */

	public static final String COLOR_MEMBER = "color"; //$NON-NLS-1$

	/**
	 * Name of the display name member.
	 */

	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the display name ID member.
	 */

	public static final String DISPLAY_NAME_ID_MEMBER = "displayNameID"; //$NON-NLS-1$

	/**
	 * The internal color name such as "periwinkle".
	 */

	protected String name;

	/**
	 * The color value of the color, it is preserved as the user typed in.
	 */

	protected String color;

	/**
	 * The display name for the color.
	 */

	protected String displayName;

	/**
	 * The message ID for the display name. Allows the display name to be localized.
	 */

	protected String displayNameID;

	/**
	 * Default constructor.
	 */

	public CustomColor() {
	}

	/**
	 * Constructs the custom color with a required name and RGB value.
	 *
	 * @param theName the internal color name
	 * @param value   the color value
	 */

	public CustomColor(String theName, String value) {
		name = theName;
		color = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.core.IStructure#getStructName()
	 */

	@Override
	public String getStructName() {
		return CUSTOM_COLOR_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String valueName) {
		if (valueName.equals(NAME_MEMBER)) {
			return name;
		}
		if (valueName.equals(COLOR_MEMBER)) {
			return color;
		}
		if (valueName.equals(DISPLAY_NAME_MEMBER)) {
			return displayName;
		}
		if (valueName.equals(DISPLAY_NAME_ID_MEMBER)) {
			return displayNameID;
		}
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String valueName, Object value) {
		if (valueName.equals(NAME_MEMBER)) {
			name = (String) value;
		} else if (valueName.equals(COLOR_MEMBER)) {
			color = (String) value;
		} else if (valueName.equals(DISPLAY_NAME_MEMBER)) {
			displayName = (String) value;
		} else if (valueName.equals(DISPLAY_NAME_ID_MEMBER)) {
			displayNameID = (String) value;
		} else {
			assert false;
		}
	}

	/**
	 * Returns the color value as an integer RGB value. If the color value is of a
	 * valid color representation, then return its numeric RGB value as integer.
	 * Otherwise, return <code>-1</code> indicates that the value is not valid.
	 * <p>
	 * The color value can contain any of the valid color representations:
	 * <ul>
	 * <li>A decimal number: An integer with a valid RGB color value.</li>
	 * <li>A hexadecimal number in Java or HTML style: "0xRRGGBB, #RRGGBB or
	 * #RGB</li>
	 * <li>CSS absolute: RGB(r,g,b)</li>
	 * <li>CSS relative: RGB(r%,g%,b%)</li>
	 * <li>One of the css color name: red, green, black.</li>
	 * </ul>
	 *
	 *
	 * @return the color value as an integer RGB value. Return <code>-1</code> if
	 *         the color value is not valid or the color value is <code>null</code>.
	 * @see ColorUtil#parseColor(String)
	 */

	public int getRGB() {
		if (color == null) {
			return -1;
		}

		// Use the validation logic of color property type.

		try {
			// design is null, do not need to validate from color pallete.

			return ColorUtil.parseColor(color);
		} catch (NumberFormatException e) {
			return -1;
		}

	}

	/**
	 * Returns the internal color name.
	 *
	 * @return the internal color name
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Returns the color value as what the user has input.
	 *
	 * @return the color value as what the user has input.
	 */

	public String getColor() {
		return (String) getProperty(null, COLOR_MEMBER);
	}

	/**
	 * Returns the color display name message ID.
	 *
	 * @return the display name message ID
	 */

	public String getDisplayNameID() {
		return (String) getProperty(null, DISPLAY_NAME_ID_MEMBER);
	}

	/**
	 * Returns the color display name.
	 *
	 * @return the display name
	 */

	public String getDisplayName() {
		return (String) getProperty(null, DISPLAY_NAME_MEMBER);
	}

	/**
	 * Return the display name of the color that can be showed to user. If the
	 * custom translations has defined a translation for <code>displayNameID</code>,
	 * then the localized text for the current locale is returned; Otherwise return
	 * the <code>displayName</code> property if is not null or blank; Else, return
	 * the internal name of the color.
	 *
	 * @param module the module
	 * @return the display name of the color
	 */

	public String getDisplayName(Module module) {
		String value = null;

		// 1. use the displayNameID to find the translation.

		Translation translation = module.findTranslation(displayNameID, module.getLocale().toString());

		if (translation != null) {
			value = translation.getText();
			if (!StringUtil.isBlank(value)) {
				return value;
			}
		}

		// 2. use the display name.

		value = getDisplayName();
		if (!StringUtil.isBlank(value)) {
			return value;
		}

		// 3. return the internal name, its required.

		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	@Override
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new CustomColorHandle(valueHandle, index);
	}

	/**
	 * Sets the internal color name.
	 *
	 * @param name the internal color name
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the color display name.
	 *
	 * @param displayName the display name
	 */

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets the color value.
	 *
	 * @param colorValue the color value to be set.
	 *
	 */

	public void setColor(String colorValue) {
		this.color = colorValue;
	}

	/**
	 * Sets the color display name ID.
	 *
	 * @param displayNameID the display name ID to set
	 */

	public void setDisplayNameID(String displayNameID) {
		this.displayNameID = displayNameID;
	}

	/**
	 * Validates this structure. The following are the rules:
	 * <ul>
	 * <li>The color name is required.</li>
	 * <li>The color value can not be the same as a CSS color name.</li>
	 * </ul>
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#validate(Module,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public List validate(Module module, DesignElement element) {
		List list = super.validate(module, element);

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		IChoiceSet colors = MetaDataDictionary.getInstance().getChoiceSet(ColorPropertyType.COLORS_CHOICE_SET);
		if (colors.contains(name)) {
			list.add(new SemanticError(element, new String[] { name },
					SemanticError.DESIGN_EXCEPTION_INVALID_CUSTOM_COLOR_NAME));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.core.ReferencableStructure#
	 * isReferencableProperty(java.lang.String)
	 */

	@Override
	public boolean isReferencableProperty(String memberName) {
		return NAME_MEMBER.equalsIgnoreCase(memberName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.Structure#getReferencableProperty()
	 */

	@Override
	public String getReferencableProperty() {
		return name;
	}

}
