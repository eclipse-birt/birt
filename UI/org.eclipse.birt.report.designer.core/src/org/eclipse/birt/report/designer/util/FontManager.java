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

package org.eclipse.birt.report.designer.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * Manages font resouces.
 */

public class FontManager {

	/**
	 * This map stores font name - Font pairs, used to quickly lookup a Font of a
	 * predefined font.
	 */
	public static Font getFont(String family, int size, int style) {
		Font font = null;

		if (size < 0) {
			size = 0;
		}

		String key = family + Integer.toString(size) + Integer.toString(style);
		if (JFaceResources.getFontRegistry().hasValueFor(key)) {
			font = JFaceResources.getFontRegistry().get(key);
		} else {
			JFaceResources.getFontRegistry().put(key, new FontData[] { new FontData(family, size, style) });
			font = JFaceResources.getFontRegistry().get(key);
		}
		return font;
	}

	/**
	 * Gets font by FontData, the font will be cached and disposed automatically.
	 */
	public static Font getFont(FontData fd) {
		if (fd == null) {
			return null;
		}

		Font font = null;

		String key = fd.toString();

		if (JFaceResources.getFontRegistry().hasValueFor(key)) {
			font = JFaceResources.getFontRegistry().get(key);
		} else {
			JFaceResources.getFontRegistry().put(key, new FontData[] { fd });
			font = JFaceResources.getFontRegistry().get(key);
		}
		return font;
	}

}