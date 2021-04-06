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

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;

/**
 * Simplifies working with the font family property. The font family can be for
 * either a element property or a structure member.
 * <p>
 * 
 * Values of a font family can be a list of font names, a string, a CSS
 * (pre-defined) string. The CSS values are defined in
 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}. For
 * example, the font family allows values in these formats:
 * <ul>
 * <li>"serif, recursive, Times New Roman, Arial" ( a list of font names
 * separated by commas )
 * <li>Song ( a string )
 * <li>cursive, sans-serif ( a CSS constant )
 * </ul>
 * <p>
 * CSS has the following predefined font names:
 * <ul>
 * <li>serif</li>
 * <li>sans-serif</li>
 * <li>cursive</li>
 * <li>fantasy</li>
 * <li>monospace</li>
 * </ul>
 * 
 * Please note that for a list of font names, blanks are allowed between two
 * neighboring font names.
 * <p>
 * The application generally does not create font handles directly. It uses the
 * method in <code>DesignElementHandle</code> to get a color handle like:
 * 
 * <pre>
 * 
 * DesignElementHandle elementHandle = element.handle();
 * FontHandle fontHandle = elementHandle.getFontProperty(Style.FONT_FAMILY_PROP);
 * 
 * </pre>
 * 
 */

public class FontHandle extends ComplexValueHandle {

	/**
	 * Constructs a font family handle for an element property.
	 * 
	 * @param handle the element handle
	 */

	public FontHandle(DesignElementHandle handle) {
		super(handle, handle.getElement().getPropertyDefn(IStyleModel.FONT_FAMILY_PROP));

		assert propDefn != null;
	}

	/**
	 * Constructs a font family handle for a structure member.
	 * 
	 * @param handle  the element handle
	 * @param context the context to the structure member.
	 * 
	 */

	public FontHandle(DesignElementHandle handle, StructureContext context) {
		super(handle, context);

		propDefn = getElement().getPropertyDefn(IStyleModel.HIGHLIGHT_RULES_PROP);
		assert propDefn != null;
	}

	/**
	 * Constructs a font family handle for a structure member.
	 * 
	 * @param handle  the element handle
	 * @param context the context to the structure member
	 * @deprecated
	 * 
	 */

	public FontHandle(DesignElementHandle handle, MemberRef context) {
		super(handle, context);

		propDefn = getElement().getPropertyDefn(IStyleModel.HIGHLIGHT_RULES_PROP);
		assert propDefn != null;
	}

	/**
	 * Returns an array containing all of font names in the correct order. Names are
	 * separated by commas in the property value, and are slit into an array by this
	 * method.
	 * 
	 * @return an array containing font names or <code>null</code> if the value of
	 *         the font family property is not set.
	 */

	public String[] getFontFamilies() {
		String lists = getStringValue();

		if (lists == null)
			return null;

		String[] names = lists.split(","); //$NON-NLS-1$
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].trim();
		}
		return names;
	}

	/**
	 * Returns an array containing CSS (pre-defined) font names. Each item in the
	 * array is a CSS constant defined in the
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}.
	 * 
	 * @return an array containing CSS (pre-defined) font names.
	 */

	public IChoice[] getCSSFontFamilies() {

		IChoiceSet choiceSet = propDefn.getChoices();
		if (choiceSet == null) {
			return null;
		}

		IChoice[] choices = choiceSet.getChoices();
		return choices;
	}

}