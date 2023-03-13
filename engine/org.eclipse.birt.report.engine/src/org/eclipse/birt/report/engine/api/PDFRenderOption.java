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

package org.eclipse.birt.report.engine.api;

/**
 * Defines render options for emitters
 */
public class PDFRenderOption extends RenderOption implements IPDFRenderOption {

	/**
	 * dummy constructor
	 */
	public PDFRenderOption() {
		super();
	}

	/**
	 * Constructor.
	 *
	 * @param options
	 */
	public PDFRenderOption(IRenderOption options) {
		super(options);
	}

	/**
	 * Set flag indicates if the font needs to be embedded.
	 *
	 * @param isEmbededFont
	 */
	@Override
	public void setEmbededFont(boolean isEmbededFont) {
		setOption(IS_EMBEDDED_FONT, Boolean.valueOf(isEmbededFont));
	}

	/**
	 *
	 * @return true if font is embedded
	 */
	@Override
	public boolean isEmbededFont() {
		return getBooleanOption(IS_EMBEDDED_FONT, false);
	}

	/**
	 * @deprecated
	 * @return the user-defined font directory
	 */
	@Deprecated
	@Override
	public String getFontDirectory() {
		return getStringOption(FONT_DIRECTORY);
	}

	/**
	 * @deprecated
	 * @param fontDirectory the user-defined font directory
	 */
	@Deprecated
	@Override
	public void setFontDirectory(String fontDirectory) {
		setOption(FONT_DIRECTORY, fontDirectory);
	}
}
