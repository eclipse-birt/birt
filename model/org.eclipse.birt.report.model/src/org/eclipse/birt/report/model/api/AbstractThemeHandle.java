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

	public final List<DesignElementHandle> getAllStyles() {
		AbstractTheme theme = (AbstractTheme) getElement();
		List<DesignElementHandle> styles = new ArrayList<DesignElementHandle>();
		List<StyleElement> styleList = theme.getAllStyles();
		Iterator<StyleElement> iter = styleList.iterator();
		while (iter.hasNext()) {
			StyleElement style = iter.next();
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
		if (style != null) {
			return (StyleHandle) style.getHandle(module);
		}

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
		Set<String> set = new HashSet<String>();
		for (int i = 0; i < styles.getCount(); i++) {
			StyleHandle style = (StyleHandle) styles.get(i);
			set.add(style.getName());
		}

		// Should different from css file name

		PropertyHandle propHandle = getPropertyHandle(IAbstractThemeModel.CSSES_PROP);
		if (propHandle != null) {
			Iterator<?> iterator = propHandle.iterator();
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

	@Override
	public final String getDisplayLabel(int level) {

		String displayLabel = super.getDisplayLabel(level);

		Module rootModule = getModule();
		if (rootModule instanceof Library) {
			displayLabel = StringUtil.buildQualifiedReference(((Library) rootModule).getNamespace(), displayLabel);
		}

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
	public List<CssStyleSheetHandle> getAllCssStyleSheets() {
		AbstractTheme theme = (AbstractTheme) getElement();
		List<CssStyleSheetHandle> allStyles = new ArrayList<CssStyleSheetHandle>();
		List<CssStyleSheet> csses = theme.getCsses();
		for (int i = 0; csses != null && i < csses.size(); ++i) {
			CssStyleSheet sheet = csses.get(i);
			allStyles.add(sheet.handle(getModule()));
		}
		return allStyles;
	}

	/**
	 * (non-Javadoc)
	 *
	 * @param sheetHandle
	 * @throws SemanticException
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
	 * @param cssStruct
	 * @throws SemanticException
	 *
	 * @see org.eclipse.birt.report.model.api.AbstractThemeHandle#addCss(org.eclipse
	 *      .birt.report.model.api.elements.structures.IncludedCssStyleSheet)
	 */
	public void addCss(IncludedCssStyleSheet cssStruct) throws SemanticException {
		if (cssStruct == null) {
			return;
		}

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(cssStruct);
	}

	/**
	 * Add css based on file
	 *
	 * @param fileName file name of the css style sheet
	 * @throws SemanticException
	 *
	 * @deprecated
	 */
	@Deprecated
	public void addCss(String fileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCss(fileName);
	}

	/**
	 * Add css based on properties
	 *
	 * @param fileName         file name of the css file
	 * @param externalCssURI   external css uri
	 * @param isUseExternalCss use external css
	 * @throws SemanticException
	 */
	public void addCssByProerties(String fileName, String externalCssURI, boolean isUseExternalCss)
			throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.addCssbyProperties(fileName, externalCssURI, isUseExternalCss);
	}

	/**
	 * Check if the css style sheet can be dropped based on sheet handle
	 *
	 * @param sheetHandle
	 * @throws SemanticException
	 */
	public void dropCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.dropCss(sheetHandle);
	}

	/**
	 * Check if the css style sheet can be dropped based on sheet handle
	 *
	 * @param sheetHandle sheet handle to be validated
	 * @return Return the value of drop option of css style sheet
	 */
	public boolean canDropCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canDropCssStyleSheet(sheetHandle);
	}

	/**
	 * Check if the css style sheet can be added based on sheet handle
	 *
	 * @param sheetHandle sheet handle to be validated
	 * @return Return the validation result of the add option
	 */
	public boolean canAddCssStyleSheet(CssStyleSheetHandle sheetHandle) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(sheetHandle);
	}

	/**
	 * Check if the css style sheet can be added based on file name
	 *
	 * @param fileName file name of the css style sheet
	 * @return Return the validation result of the add option
	 *
	 * @deprecated
	 */
	@Deprecated
	public boolean canAddCssStyleSheet(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheet(fileName);
	}

	/**
	 * Check if the css style sheet can be added based on properties
	 *
	 * @param fileName       file name of the css file
	 * @param externalCssURI external css uri
	 * @param useExternalCss use external css
	 * @return Give the value of add option of css sytle sheet
	 */
	public boolean canAddCssStyleSheetByProperties(String fileName, String externalCssURI, boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canAddCssStyleSheetByProperties(fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Reload the css style
	 *
	 * @param sheetHandle css style sheet handle to be reloaded
	 * @throws SemanticException
	 */
	public void reloadCss(CssStyleSheetHandle sheetHandle) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.reloadCss(sheetHandle);
	}

	/**
	 * Find the css style sheet handle based on file name
	 *
	 * @param fileName file name of the css style sheet
	 * @return Return the css style sheet handle
	 *
	 * @deprecated
	 */
	@Deprecated
	public CssStyleSheetHandle findCssStyleSheetHandleByName(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByFileName(fileName);

	}

	/**
	 * Find the css style sheet handle based on properties
	 *
	 * @param fileName       file name of the css
	 * @param externalCssURI external css uri
	 * @param useExternalCss use external css
	 * @return Return the css style sheet handle
	 */
	public CssStyleSheetHandle findCssStyleSheetHandleByProperties(String fileName, String externalCssURI,
			boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);

	}

	/**
	 * Find the included css style sheet handle based on file name
	 *
	 * @param fileName file name of the css
	 * @return Return the css style sheet handle
	 *
	 * @deprecated
	 */
	@Deprecated
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByName(String fileName) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByFileName(fileName);
	}

	/**
	 * Find the included css style sheet handle based on properties
	 *
	 * @param fileName       file name of the css
	 * @param externalCssURI external css uri
	 * @param useExternalCss use external css
	 * @return Return the css style sheet handle
	 */
	public IncludedCssStyleSheetHandle findIncludedCssStyleSheetHandleByProperties(String fileName,
			String externalCssURI, boolean useExternalCss) {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.findIncludedCssStyleSheetHandleByProperties(fileName, externalCssURI, useExternalCss);
	}

	/**
	 * Rename css properties based on file name
	 *
	 * @param handle      css sytle sheet handle
	 * @param newFileName new file name
	 * @throws SemanticException
	 *
	 * @deprecated
	 */
	@Deprecated
	public void renameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCss(handle, newFileName);
	}

	/**
	 * Rename css properties based on properties
	 *
	 * @param handle         css style sheet handle
	 * @param newFileName    new file name
	 * @param externalCssURI external css uri
	 * @param useExternalCss use external css
	 * @throws SemanticException
	 *
	 */
	public void renameCssByProperties(IncludedCssStyleSheetHandle handle, String newFileName, String externalCssURI,
			boolean useExternalCss) throws SemanticException {

		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		adapter.renameCssByProperties(handle, newFileName, externalCssURI, useExternalCss);
	}

	/**
	 * Verify if the css could be renamed by file name
	 *
	 * @param handle      css style sheet handle
	 * @param newFileName new file name
	 * @return Verification result of renaming
	 * @throws SemanticException
	 * @deprecated
	 */
	@Deprecated
	public boolean canRenameCss(IncludedCssStyleSheetHandle handle, String newFileName) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCss(handle, newFileName);
	}

	/**
	 * Verify if the css could be renamed by property
	 *
	 * @param handle         css style sheet handle
	 * @param newFileName    new file name
	 * @param externalCssURI external css uri
	 * @param useExternalCss use external css
	 * @return Verification result of renaming
	 * @throws SemanticException
	 */
	public boolean canRenameCssByProperties(IncludedCssStyleSheetHandle handle, String newFileName,
			String externalCssURI, boolean useExternalCss) throws SemanticException {
		CssStyleSheetHandleAdapter adapter = new CssStyleSheetHandleAdapter(module, getElement());
		return adapter.canRenameCssByProperties(handle, newFileName, externalCssURI, useExternalCss);
	}

}
