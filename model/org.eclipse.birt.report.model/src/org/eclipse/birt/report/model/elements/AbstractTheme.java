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

package org.eclipse.birt.report.model.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.NamePropertyType;
import org.eclipse.birt.report.model.util.StyleUtil;

/**
 * This class represents a theme in the library.
 * 
 */

public abstract class AbstractTheme extends ReferenceableElement
		implements IAbstractThemeModel, ICssStyleSheetOperation {

	protected List<String> cachedStyleNames = new ArrayList<String>();

	protected ICssStyleSheetOperation operation = null;

	/**
	 * Constructor.
	 */

	public AbstractTheme() {
		super();
		initSlots();
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName the element name
	 */

	public AbstractTheme(String theName) {
		super(theName);
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public final ContainerSlot getSlot(int slot) {
		assert (slot == STYLES_SLOT);
		return slots[STYLES_SLOT];
	}

	/**
	 * Gets all styles including styles in css file.
	 * 
	 * @return all styles
	 */

	public final List<StyleElement> getAllStyles() {
		List styleList = new ArrayList<StyleElement>();

		// add style in css file
		styleList.addAll(CssNameManager.getStyles(this));

		// add style in theme slot
		List<DesignElement> styles = slots[STYLES_SLOT].getContents();
		if (!styleList.isEmpty()) {
			for (DesignElement style : styles) {
				String name = ((StyleElement) style).getFullName();
				int pos = StyleUtil.getStylePosition(styleList, name);
				if (pos != -1)
					styleList.remove(pos);
			}
		}
		styleList.addAll(styles);
		return styleList;
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param styleName the style name
	 * @return the corresponding style
	 */

	public final StyleElement findStyle(String styleName) {
		if (styleName == null)
			return null;
		List<StyleElement> styles = getAllStyles();
		for (int i = 0; i < styles.size(); ++i) {
			StyleElement style = styles.get(i);
			// style name is case-insensitive
			if (styleName.equalsIgnoreCase(style.getFullName())) {
				return style;
			}
		}
		return null;
	}

	/**
	 * Makes a unique name for this element.
	 * 
	 * @param element
	 */

	public final void makeUniqueName(DesignElement element) {
		if (element == null)
			return;

		if (!(element instanceof StyleElement))
			return;

		// if style is on the tree already, return
		if (element.getRoot() != null)
			return;

		String name = StringUtil.trimString(element.getName());

		// replace all the illegal chars with '_'
		name = NamePropertyType.validateName(name);

		if (name == null)
			name = ModelMessages.getMessage("New." //$NON-NLS-1$
					+ element.getDefn().getName()).trim();

		List<DesignElement> styles = slots[STYLES_SLOT].getContents();
		List<String> ns = new ArrayList<String>(styles.size());
		// style name is case-insensitive
		for (int i = 0; i < styles.size(); i++)
			ns.add(styles.get(i).getName().toLowerCase());

		int index = 0;
		String baseName = name;

		assert name != null;
		// style name is case-insensitive
		String lowerCaseName = name.toLowerCase();
		while (cachedStyleNames.contains(lowerCaseName) || ns.contains(lowerCaseName)) {
			name = baseName + ++index;
			lowerCaseName = name.toLowerCase();
		}

		// set the unique name and add the element to the name manager
		element.setName(name.trim());
		cachedStyleNames.add(lowerCaseName);
	}

	/**
	 * Remove some cached name.
	 * 
	 * @param name the name of style element.
	 */

	public final void dropCachedName(String name) {
		assert name != null;
		name = name.toLowerCase();
		cachedStyleNames.remove(name);
	}

	/**
	 * Drops the given css from css list.
	 * 
	 * @param css the css to drop
	 * @return the position of the css to drop
	 */

	public int dropCss(CssStyleSheet css) {
		if (operation == null)
			return -1;
		return operation.dropCss(css);
	}

	/**
	 * Adds the given css to css list.
	 * 
	 * @param css the css to insert
	 */

	public void addCss(CssStyleSheet css) {
		if (operation == null)
			operation = new CssStyleSheetAdapter();
		operation.addCss(css);
	}

	/**
	 * Insert the given css to the given position
	 * 
	 * @param css
	 * @param index
	 */

	public void insertCss(CssStyleSheet css, int index) {
		if (operation == null)
			operation = new CssStyleSheetAdapter();
		operation.insertCss(css, index);
	}

	/**
	 * Returns only csses this module includes directly.
	 * 
	 * @return list of csses. each item is <code>CssStyleSheet</code>
	 */

	public List<CssStyleSheet> getCsses() {
		if (operation == null)
			return Collections.emptyList();
		return operation.getCsses();
	}

}
