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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the ducplicat style name in the theme.
 * 
 * <h3>Rule</h3> The rule is that one theme element doesn't allow duplicate
 * style name to appear in this element.
 * 
 * <h3>Applicability</h3> This validator is only applied to <code>Theme</code>.
 * 
 */

public class ThemeStyleNameValidator extends AbstractElementValidator {

	private static ThemeStyleNameValidator instance = new ThemeStyleNameValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static ThemeStyleNameValidator getInstance() {
		return instance;
	}

	/**
	 * Gets all styles in theme with the same name.
	 * 
	 * @param themeHandle
	 * @param styleName
	 * @return
	 */

	private List<StyleHandle> getSameNameStyles(AbstractThemeHandle themeHandle, String styleName) {
		SlotHandle slot = themeHandle.getSlot(IAbstractThemeModel.STYLES_SLOT);
		Iterator<DesignElementHandle> iterator = slot.iterator();
		List<StyleHandle> sameNameList = new ArrayList<StyleHandle>();

		while (iterator.hasNext()) {
			StyleHandle style = (StyleHandle) iterator.next();
			if (style.getName().equalsIgnoreCase(styleName)) {
				sameNameList.add(style);
			}
		}
		return sameNameList;
	}

	/**
	 * Validates whether the style with the given name can be added into the given
	 * theme element.
	 * 
	 * @param theme     the theme element
	 * @param styleName name of the style to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAddingStyle(AbstractThemeHandle theme, String styleName) {
		List<SemanticException> list = new ArrayList<SemanticException>();

		if (getSameNameStyles(theme, styleName).size() > 0)
			list.add(new NameException(theme.getElement(), styleName, NameException.DESIGN_EXCEPTION_DUPLICATE));

		return list;
	}

	/**
	 * Validates whether the style can be renamed to the given name.
	 * 
	 * @param theme     the theme element
	 * @param style     the style to rename
	 * @param styleName the new name of the style to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForRenamingStyle(AbstractThemeHandle theme, StyleHandle style,
			String styleName) {
		// Specially deal with style name in theme.

		List<SemanticException> list = new ArrayList<SemanticException>();

		List<StyleHandle> sameNameStyles = getSameNameStyles(theme, styleName);

		// style is in theme slot, so if duplicate there must be two style
		// instance with same style name
		if (sameNameStyles.size() > 1) {
			list.add(new NameException(theme.getElement(), styleName, NameException.DESIGN_EXCEPTION_DUPLICATE));
		}
		// style is not in theme slot, just check name is unique or not
		else if (sameNameStyles.size() == 1) {
			StyleHandle tempStyle = sameNameStyles.get(0);
			if (tempStyle != style)
				list.add(new NameException(theme.getElement(), styleName, NameException.DESIGN_EXCEPTION_DUPLICATE));
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.validators.AbstractElementValidator#validate
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof AbstractTheme))
			return Collections.emptyList();

		AbstractTheme theme = (AbstractTheme) element;
		ContainerSlot slot = theme.getSlot(IAbstractThemeModel.STYLES_SLOT);

		List<SemanticException> list = new ArrayList<SemanticException>();
		Set<String> set = new HashSet<String>();

		for (Iterator<DesignElement> iter = slot.iterator(); iter.hasNext();) {
			StyleElement style = (StyleElement) iter.next();
			String styleName = style.getFullName().toLowerCase();

			if (!set.contains(styleName))
				set.add(styleName);
			else
				list.add(new NameException(theme, styleName, NameException.DESIGN_EXCEPTION_DUPLICATE));
		}
		return list;
	}
}
