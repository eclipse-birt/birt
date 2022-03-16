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
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;

/**
 * Represents a included css style sheet in report design and theme.
 *
 */

public class IncludedCssStyleSheetHandle extends StructureHandle {

	/**
	 * Constructs the handle of the included css style sheet.
	 *
	 * @param valueHandle the value handle for the included css style sheet list of
	 *                    one property
	 * @param index       the position of this included css style sheet in the list
	 */

	public IncludedCssStyleSheetHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Gets the file name of the include css style sheet.
	 *
	 * @return the file name of the include css style sheet
	 */

	public String getFileName() {
		return getStringProperty(IncludedCssStyleSheet.FILE_NAME_MEMBER);
	}

	/**
	 * Sets the file name of the included css style sheet..
	 *
	 * @param fileName the file name.
	 * @throws SemanticException
	 */
	public void setFileName(String fileName) throws SemanticException {
		setProperty(IncludedCssStyleSheet.FILE_NAME_MEMBER, fileName);
	}

	/**
	 * Gets the URI of the external CSS.
	 *
	 * @return the URI of the external CSS
	 */
	public String getExternalCssURI() {
		return getStringProperty(IncludedCssStyleSheet.EXTERNAL_CSS_URI_MEMBER);
	}

	/**
	 * Sets the URI of the external CSS.
	 *
	 * @param externalCssURI the URI of the external CSS.
	 * @throws SemanticException
	 */
	public void setExternalCssURI(String externalCssURI) throws SemanticException {
		setProperty(IncludedCssStyleSheet.EXTERNAL_CSS_URI_MEMBER, externalCssURI);
	}

	public void setUseExternalCss(boolean useExternalCss) throws SemanticException {
		setProperty(IncludedCssStyleSheet.USE_EXTERNAL_CSS, useExternalCss);
	}

	public boolean isUseExternalCss() {
		Object value = getProperty(IncludedCssStyleSheet.USE_EXTERNAL_CSS);
		if (value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				return true;
			}
		}
		return false;
	}

}
