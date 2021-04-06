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

package org.eclipse.birt.report.model.core.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.CaseInsensitiveNameSpace;
import org.eclipse.birt.report.model.css.CssNameManager;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * The special case for the styles.
 */
public class StyleNameContext extends AbstractModuleNameContext {

	/**
	 * Map of the styles that can be resolved by calling resolve(String).
	 */

	private Map<String, StyleElement> cachedStyles = null;

	/**
	 * Cached default TOC style map. If it is <code>null</code>, the value has not
	 * been initialized. Otherwise, it is safter to use this value.
	 */

	private Map<String, StyleElement> cachedTOCStyles = null;

	/**
	 * Constructs one style element name space.
	 * 
	 * @param module the attached module
	 */

	StyleNameContext(Module module) {
		super(module, Module.STYLE_NAME_SPACE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.core.namespace.AbstractNameContext#
	 * initNameSpace()
	 */
	protected void initNameSpace() {
		this.namespace = new CaseInsensitiveNameSpace();
	}

	/**
	 * Returns all elements in the module this module namespace is associated and
	 * those in the included modules. For the style name scope, the depth of the
	 * library is ignored.
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.IModuleNameScope#getElements(int)
	 */

	public List<DesignElement> getElements(int level) {
		Theme theme = module.getTheme(module);

		Map<String, StyleElement> elements = new LinkedHashMap<String, StyleElement>();

		if (theme == null && module instanceof Library)
			return Collections.emptyList();

		if (theme != null) {
			List<StyleElement> tmpStyles = theme.getAllStyles();
			elements.putAll(addAllStyles(tmpStyles));
		}

		if (module instanceof Library)
			return new ArrayList<DesignElement>(elements.values());

		// find in css file

		List<CssStyle> csses = CssNameManager.getStyles((ICssStyleSheetOperation) module);
		elements.putAll(addAllStyles(csses));

		// find all styles in report design.

		NameSpace ns = module.getNameHelper().getNameSpace(nameSpaceID);
		List<DesignElement> styles = ns.getElements();
		elements.putAll(addAllStyles(styles));

		return new ArrayList<DesignElement>(elements.values());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IModuleNameSpace#resolve(org.eclipse
	 * .birt.report.model.core.DesignElement)
	 */

	private ElementRefValue resolve(DesignElement element) {
		return new ElementRefValue(null, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IModuleNameSpace#resolve(java.lang
	 * .String)
	 */

	private ElementRefValue resolve(String elementName) {
		String name = elementName == null ? null : elementName.toLowerCase();
		if (module.isCached()) {
			DesignElement style = cachedStyles.get(name);

			// firstly, find it in the cached style list

			if (style != null)
				return new ElementRefValue(null, style);

			style = cachedTOCStyles.get(name);
			if (style != null)
				return new ElementRefValue(null, style);

			return new ElementRefValue(null, elementName);
		}

		// this name is not cached, so find it directly

		Theme theme = module.getTheme(module);

		if (theme == null && module instanceof Library)
			return new ElementRefValue(null, elementName);

		// find the style first in the report design first.

		DesignElement target = null;

		if (module instanceof ReportDesign) {
			NameSpace ns = module.getNameHelper().getNameSpace(nameSpaceID);
			target = ns.getElement(name);

			if (target != null) {
				return new ElementRefValue(null, target);
			}

			// find in css file

			List<CssStyle> csses = CssNameManager.getStyles((ICssStyleSheetOperation) module);
			for (int i = 0; csses != null && i < csses.size(); ++i) {
				CssStyle s = csses.get(i);
				// style name is case-insensitive
				if (name.equalsIgnoreCase(s.getFullName())) {
					return new ElementRefValue(null, s);
				}
			}
		}

		// find the style in the library.

		DesignElement libraryStyle = null;
		if (theme != null) {
			libraryStyle = theme.findStyle(name);
		}

		if (libraryStyle != null) {
			return new ElementRefValue(null, libraryStyle);
		}

		// find style in toc default style

		buildTOCStyles();

		if (cachedTOCStyles != null) {
			Style tmpStyle = (Style) cachedTOCStyles.get(name);
			if (tmpStyle != null)
				return new ElementRefValue(null, tmpStyle);
		}

		// if the style is not find, return a unresolved element reference
		// value.

		return new ElementRefValue(null, elementName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#resolve(org
	 * .eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn,
	 * org.eclipse.birt.report.model.metadata.ElementDefn)
	 */
	public ElementRefValue resolve(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		return resolve(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#resolve(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn,
	 * org.eclipse.birt.report.model.metadata.ElementDefn)
	 */
	public ElementRefValue resolve(DesignElement focus, String elementName, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		return resolve(elementName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#findElement
	 * (java.lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */

	public DesignElement findElement(String elementName, IElementDefn elementDefn) {
		ElementRefValue refValue = resolve(elementName);
		return refValue == null ? null : refValue.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.AbstractModuleNameContext
	 * #cacheValues()
	 */

	public void cacheValues() {
		// build resolved styles.

		cachedStyles = addAllStyles(getElements(IAccessControl.ARBITARY_LEVEL));

		// build TOC styles

		buildTOCStyles();
	}

	/**
	 * 
	 */

	private void buildTOCStyles() {
		if (cachedTOCStyles == null) {
			// TOC styles are fixed. Always use the same set.

			List<DesignElement> defaultTocStyle = module.getSession().getDefaultTOCStyleValue();

			cachedTOCStyles = addAllStyles(defaultTocStyle);
		}
	}

	/**
	 * @param styleMap
	 * @param styleList
	 */

	private Map<String, StyleElement> addAllStyles(List<? extends DesignElement> styleList) {
		Map<String, StyleElement> tmpMap = new LinkedHashMap<String, StyleElement>();
		if (styleList != null) {
			for (int i = 0; i < styleList.size(); i++) {
				DesignElement style = styleList.get(i);
				// style name is case-insensitive
				tmpMap.put(style.getName().toLowerCase(), (StyleElement) style);
			}
		}
		return tmpMap;
	}
}
