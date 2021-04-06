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

package org.eclipse.birt.report.model.api.css;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;

/**
 * Represents an include style sheet in the module. A style sheet is used for
 * the user to load an external ".css" style file in the module.
 * 
 * @see org.eclipse.birt.report.model.css.CssStyleSheet
 */

public class CssStyleSheetHandle extends ElementDetailHandle {

	/**
	 * The translation message.
	 */

	protected CssStyleSheet styleSheet = null;

	/**
	 * Constructs a handle for an style sheet.
	 * 
	 * @param moduleHandle a handle to a module
	 * @param styleSheet   the style sheet to be handled
	 */

	public CssStyleSheetHandle(ModuleHandle moduleHandle, CssStyleSheet styleSheet) {
		super(moduleHandle);

		assert styleSheet != null;
		this.styleSheet = styleSheet;
	}

	/**
	 * Gets the style sheet.
	 * 
	 * @return the style sheet
	 */

	public CssStyleSheet getStyleSheet() {
		return styleSheet;
	}

	/**
	 * Returns an iterator over the styles of this style sheet. Useful only for
	 * style sheet. Returns a list of all the styles that use this style sheet.
	 * 
	 * @return an iterator over the styles of this style sheet. Each item returned
	 *         by the iterator's <code>getNext( )</code> method is of type
	 *         {@link SharedStyleHandle}.
	 */

	public Iterator getStyleIterator() {
		return new StyleIterator(this);
	}

	/**
	 * Gets a style handle with the given name in the style sheet.
	 * 
	 * @param name the name of the style to find
	 * @return the style handle with the given name in the style sheet, otherwise
	 *         <code>null</code>
	 */

	public SharedStyleHandle findStyle(String name) {
		StyleElement style = styleSheet.findStyle(name);
		if (style == null)
			return null;
		return (SharedStyleHandle) style.getHandle(getModule());
	}

	/**
	 * Returns all the unsupported style names.
	 * 
	 * @return the list of the unsupported style name
	 */

	public List getUnsupportedStyles() {
		return styleSheet.getUnsupportedStyle();
	}

	/**
	 * Gets the warning list of the given style. Each one in the list is instance of
	 * <code>StyleSheetParserException</code>.
	 * 
	 * @param styleName the style name
	 * @return the warning list of the given style, otherwise null
	 * @see org.eclipse.birt.report.model.api.css.StyleSheetParserException
	 */

	public List getWarnings(String styleName) {
		return styleSheet.getWarnings(styleName);
	}

	/**
	 * Gets the message list for the parser errors.
	 * 
	 * @return the message list for the parser errors
	 */

	public List getParserErrors() {
		return styleSheet.getErrorHandler().getParserErrors();
	}

	/**
	 * Gets the message list for the parser fatal errors.
	 * 
	 * @return the message list for the parser fatal errors
	 */

	public List getParserFatalErrors() {
		return styleSheet.getErrorHandler().getParserFatalErrors();
	}

	/**
	 * Gets the message list for the parser warnings.
	 * 
	 * @return the message list for the parser warnings
	 */

	public List getParserWarnings() {
		return styleSheet.getErrorHandler().getParserWarnings();
	}

	/**
	 * Gets css file name
	 * 
	 * @return css file name
	 */

	public String getFileName() {
		return styleSheet.getFileName();
	}

	public String getExternalCssURI() {
		return styleSheet.getExternalCssURI();
	}

	public boolean isUseExternalCss() {
		return styleSheet.isUseExternalCss();
	}

	/**
	 * Gets container handle.
	 * 
	 * @return
	 */

	public DesignElementHandle getContainerHandle() {
		DesignElement tmpElement = styleSheet.getContainer();
		if (tmpElement == null)
			return null;
		return tmpElement.getHandle(elementHandle.getModule());
	}
}
