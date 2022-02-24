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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetHandleAdapter;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;

/**
 * Represents a abstract theme in the library. Each theme contains some number
 * of styles. It can be a normal theme or a report item theme.
 * 
 * 
 * @see org.eclipse.birt.report.model.elements.Theme
 */

public abstract class AbstractThemeHandle extends ReportElementHandle implements IAbstractThemeModel {

	/**
	 * Constructs the handle for a theme with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AbstractThemeHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the styles slot of row. Through SlotHandle, each style can be
	 * obtained.
	 * 
	 * @return the handle to the style slot
	 * 
	 * @see SlotHandle
	 */

	public final SlotHandle getStyles() {
		return getSlot(STYLES_SLOT);
	}

	/**
	 * Gets all styles in theme,include css file.
	 * 
	 * @return all styles.each item is <code>StyleHandle</code>
	 */

	public final List getAllStyles() {
		AbstractTheme theme = (AbstractTheme) getElement();
		List styles = new ArrayList();
		List styleList = theme.getAllStyles();
		Iterator iter = styleList.iterator();
		while (iter.hasNext()) {
			StyleElement style = (StyleElement) iter.next();
			styles.add(style.getHandle(module));
		}
		return styles;
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param name the style name
	 * @return the corresponding style
	 */

	public final StyleHandle findStyle(String name) {
		AbstractTheme theme = (AbstractTheme) getElement();
		StyleElement style = theme.findStyle(name);
		if (style != null)
			return (StyleHandle) style.getHandle(module);

		return null;
	}

	/**
	 * Makes the unique style name in the given theme. The return name is based on
	 * <code>name</code>.
	 * 
	 * @param name the style name
	 * @return the new unique style name
	 */

	final String makeUniqueStyleName(String name) {
		assert this != null;

		SlotHandle styles = getStyles();
		Set set = new HashSet();
		for (int i = 0; i < styles.getCount(); i++) {
			StyleHandle style = (StyleHandle) styles.get(i);
			set.add(style.getName());
		}

		// Should different from css file name

		PropertyHandle propHandle = getPropertyHandle(IAbstractThemeModel.CSSES_PROP);
		if (propHandle != null) {
			Iterator iterator = propHandle.iterator();
			while (iterator.hasNext()) {
				IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle) iterator.next();
				set.add(handle.getFileName());
			}
		}

		// Add a numeric suffix that makes the name unique.

		int index = 0;
		String baseName = name;
		while (set.contains(name)) {
			name = baseName + ++index;
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.DesignElementHandle#getDisplayLabel
	 * (int)
	 */

	public final String getDisplayLabel(int level) {

		String displayLabel = super.getDisplayLabel(level);

		Module rootModule = getModule();
		if (rootModule instanceof Library)
			displayLabel = StringUtil.buildQualifiedReference(((Library) rootModule).getNamespace(), displayLabel);

		return displayLabel;

	}

	/**
	 * Returns the iterator over all included css style sheets. Each one is the
	 * instance of <code>IncludedCssStyleSheetHandle</code>
	 * 
	 * @return the iterator over all included css style sheets.
	 */

	public final Iterator includeCssesIterator() {
		PropertyHandle propHandle = getPropertyHandle(CSSES_PROP);
		return propHandle == null ? Collections.emptyList().iterator() : propHandle.iterator();
	}

	/**
	 * Gets all css styles sheet
	 * 
	 * @return each item is <code>CssStyleSheetHandle</code>
	 */

	public List getAllCssStyleSheets() {
		AbstractTheme theme = (AbstractTheme) getElement();
		List allStyles = new ArrayList();
		List csses = theme.getCsses();
		for (int i = 0; csses != null && i < csses.size(); ++i) {
			CssStyleSheet sheet = (CssStyleSheet) csses.get(i);
			allStyles.add(sheet.handle(getModule()));
		}
		return allStyles;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(org.eclipse
	 *      .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void addCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(sheetHandle);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(org.eclipse
	 *      .birt.report.model.api.elements.structures.IncludedCssStyleSheet)
	 */

	public void addCss(IncludedCssStyleSheet cssStruct) throws SemanticException {
		if (cssStruct == null)
			return;

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(cssStruct);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#addCssByFileName(java.lang
	 *      .String)
	 */
	public void addCss(String fileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(fileName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#addCssByProerties(java.
	 * lang .String)
	 */
	public void addCssByProerties(String fileName, String externalCssURI, boolean isUseExternalCss)
			throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCssbyProperties(fileName, externalCssURI, isUseExternalCss);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#dropCss(org.eclipse
	 *      .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void dropCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.dropCss(sheetHandle);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#canDropCssStyleSheet
	 *      (org.eclipse.birt.report.model.api.css.CssStyleSheetHandle)
	 */
	public boolean canDropCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canDropCssStyleSheet(sheetHandle);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#canAddCssStyleSheet
	 *      (org.eclipse.birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public boolean canAddCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(sheetHandle);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#canAddCssStyleSheet
	 *      (java.lang.String)
	 */
	public boolean canAddCssStyleSheet(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(fileName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#canAddCssStyleSheetByProperties
	 *      (java.lang.String)
	 */
	public boolean canAddCssStyleSheetByProperties(String fileName, String externalCssURI, boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheetByProperties(fileName, externalCssURI, useExternalCss);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.AbstractThemeHandle#reloadCss(org.eclipse
	 * .birt.report.model.api.css.CssStyleSheetHandle)
	 */

	public void reloadCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.reloadCss(sheetHandle);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 *      findCssStyleSheetHandleByName(java.lang.String)
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByName(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByFileName(fileName);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 *      findCssStyleSheetHandleByProperties(java.lang.String, java.lang.String,
	 *      java.lang.Boolean)
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByProperties(String fileName, String externalCssURI,
			boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 *      findIncludedCssStyleSheetHandleByName(java.lang.String)
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByName(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByFileName(fileName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#
	 *      findIncludedCssStyleSheetHandleByProperties(java.lang.String,
	 *      java.lang.String, java.lang.Boolean)
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByProperties(String fileName,
			String externalCssURI, boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#renameCss(org.eclipse
	 *      .birt.report.model.api.IncludedCssStyleSheetHandle, java.lang.String)
	 */
	public void renameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCss(handle, newFileName);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#renameCssByProperties(org.eclipse
	 *      .birt.report.model.api.IncludedCssStyleSheetHandle, java.lang.String,
	 *      java.lang.String, java.lang.Boolean)
	 */
	public void renameCssByProperties(IncludedCssStyleSheetHandle handle, String newFileName, String externalCssURI,
			boolean useExternalCss) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCssByProperties(handle, newFileName, externalCssURI, useExternalCss);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @deprecated
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#canRenameCss(org
	 *      .eclipse.birt.report.model.api.IncludedCssStyleSheetHandle,
	 *      java.lang.String)
	 */
	public boolean canRenameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCss(handle, newFileName);
	}

	/**
	 * 
	 * @param handle
	 * @param newFileName
	 * @param externalCssURI
	 * @param useExternalCss
	 * @return
	 * @throws SemanticException
	 */
	public boolean canRenameCssByProperties(IncludedCssStyleSheetHandle handle, String newFileName,
			String externalCssURI, boolean useExternalCss) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCssByProperties(handle, newFileName, externalCssURI, useExternalCss);
	}

}
