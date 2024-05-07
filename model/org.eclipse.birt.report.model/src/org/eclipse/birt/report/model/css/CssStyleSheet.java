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

package org.eclipse.birt.report.model.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyleElement;

/**
 * This class represents one include style sheet of the module.
 *
 */

public final class CssStyleSheet implements Cloneable {

	/**
	 * All the external styles the style sheet contains.
	 */

	protected LinkedHashMap<String, CssStyle> styles = new LinkedHashMap<>();

	/**
	 * All the collected warnings during the loading.
	 */

	private ArrayList<StyleSheetParserException> warnings = new ArrayList<>();

	/**
	 * The map of the all the style name that is not supported for the given
	 * StyleSheetParserException.
	 */

	private HashMap<String, StyleSheetParserException> unsupportedStyles = new HashMap<>();

	/**
	 * All the errors for each style. Key is the name of the style and value is all
	 * the StyleSheetParserException that is fired when loading and parsing this
	 * style.
	 */

	private HashMap<String, List<StyleSheetParserException>> warningsForStyles = new HashMap<>();

	/**
	 * The error handler for the CSS parser.
	 */

	private CssErrorHandler errorHandler = null;

	/**
	 * Css style url resource file name
	 */

	private String fileName = null;

	private String externalCssURI = null;

	/**
	 * Get the external CSS URI
	 *
	 * @return Return the external CSS URI
	 */
	public String getExternalCssURI() {
		return externalCssURI;
	}

	/**
	 * Set the external CSS URI
	 *
	 * @param externalCssURI external CSS URI
	 */
	public void setExternalCssURI(String externalCssURI) {
		this.externalCssURI = externalCssURI;
	}

	/**
	 * Is external CSS used
	 *
	 * @return true, if external CSS is used
	 */
	public boolean isUseExternalCss() {
		return useExternalCss;
	}

	/**
	 * Set the flag of external CSS use
	 *
	 * @param useExternalCss set the flag of external CSS use
	 */
	public void setUseExternalCss(boolean useExternalCss) {
		this.useExternalCss = useExternalCss;
	}

	private boolean useExternalCss = false;

	/**
	 * Container of CssStyleSheet
	 */

	private DesignElement container = null;

	/**
	 * Return a handle to deal with the style sheet.
	 *
	 * @param module the module of the style sheet
	 * @return handle to deal with the style sheet
	 */

	public CssStyleSheetHandle handle(Module module) {
		return new CssStyleSheetHandle((ModuleHandle) module.getHandle(module), this);
	}

	/**
	 * Gets the style with the given name.
	 *
	 * @param name the style name to find
	 * @return the style with the given name if found, otherwise null
	 */

	public StyleElement findStyle(String name) {
		return styles.get(name);
	}

	/**
	 * Adds a style into the style sheet.
	 *
	 * @param style the style to add
	 */

	public void addStyle(DesignElement style) {
		assert styles.get(style.getName()) == null;
		assert style instanceof CssStyle;
		styles.put(style.getName(), (CssStyle) style);
	}

	/**
	 * Removes a style into the style sheet.
	 *
	 * @param name the name of the style
	 */

	public void removeStyle(String name) {
		assert styles.get(name) != null;
		styles.remove(name);
	}

	/**
	 * Gets all the styles in the style sheet. Each one in the list is instance of
	 * <code>StyleElement</code>.
	 *
	 * @return all the styles in the style sheet
	 */

	public List<CssStyle> getStyles() {
		return new ArrayList<>(styles.values());
	}

	/**
	 * Adds a style sheet parser exception into the warning list.
	 *
	 * @param warnings the warning list to add
	 */

	public void addWarning(List<StyleSheetParserException> warnings) {
		this.warnings.addAll(warnings);
	}

	/**
	 * Gets the warning list during the loading.
	 *
	 * @return the warning list
	 */

	public List<StyleSheetParserException> getWarnings() {
		return warnings;
	}

	/**
	 * Adds an unsupported style exception to the list.
	 *
	 * @param styleName the style name that is not supported
	 * @param e         the exception that is caused by the unsupported style
	 */

	public void addUnsupportedStyle(String styleName, StyleSheetParserException e) {
		unsupportedStyles.put(styleName, e);
	}

	/**
	 * Returns all the unsupported style names.
	 *
	 * @return the list of the unsupported style name
	 */

	public List<String> getUnsupportedStyle() {
		List<String> styles = new ArrayList<>(this.unsupportedStyles.keySet());
		return styles;
	}

	/**
	 * Adds the error list of the given style to the hash map. Key is the style
	 * name, content is the error list of the style.
	 *
	 * @param styleName the style name
	 * @param errors    the error list
	 */

	public void addWarnings(String styleName, List<StyleSheetParserException> errors) {
		this.warningsForStyles.put(styleName, errors);
	}

	/**
	 * Gets the error list of the given style.
	 *
	 * @param styleName the style name
	 * @return the error list of the given style, otherwise null
	 */

	public List<StyleSheetParserException> getWarnings(String styleName) {
		return this.warningsForStyles.get(styleName);
	}

	/**
	 * Gets the error handler for the css parser.
	 *
	 * @return Returns the errorHandler.
	 */

	public CssErrorHandler getErrorHandler() {
		return errorHandler;
	}

	/**
	 * Sets the error handler for the css parser.
	 *
	 * @param errorHandler The errorHandler to set.
	 */

	public void setErrorHandler(CssErrorHandler errorHandler) {
		this.errorHandler = errorHandler;
	}

	/**
	 * Gets css file name
	 *
	 * @return css file name
	 */

	public String getFileName() {
		return fileName;
	}

	/**
	 * Sets css file name
	 *
	 * @param fileName
	 */

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets container element. The container is the element that imports this style
	 * sheet. It can be either report design or library theme.
	 *
	 * @return the container of this style sheet.
	 */

	public DesignElement getContainer() {
		return container;
	}

	/**
	 * Sets container element.
	 *
	 * @param container
	 */

	public void setContainer(DesignElement container) {
		this.container = container;
		List<CssStyle> tmpStyles = getStyles();
		for (int i = 0; i < tmpStyles.size(); i++) {
			CssStyle tmpStyle = tmpStyles.get(i);
			tmpStyle.setContainer(container);
		}
	}

	@Override
	public CssStyleSheet clone() throws CloneNotSupportedException {
		CssStyleSheet cssStyleSheet = (CssStyleSheet) super.clone();
		// clone CssStyle
		LinkedHashMap<String, CssStyle> newStyles = new LinkedHashMap<>();
		if (this.styles.size() > 0) {
			Set<String> keySet = this.styles.keySet();
			for (String key : keySet) {
				CssStyle cloneCssStyle = (CssStyle) this.styles.get(key).clone();
				newStyles.put(key, cloneCssStyle);
			}
		}
		cssStyleSheet.styles = newStyles;
		return cssStyleSheet;
	}
}
