/***********************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.util;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.w3c.dom.css.CSSValue;

/**
 * Provides convenience methods for text alignment resolution
 * 
 */
public class BidiAlignmentResolver {
	public static String getDefaultAlignment(boolean rtl) {
		if (rtl)
			return CSSConstants.CSS_RIGHT_VALUE;

		return CSSConstants.CSS_LEFT_VALUE;
	}

	public static String getDefaultAlignment(String direction) {
		return getDefaultAlignment(DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(direction));
	}

	public static String resolveAlignmentForDesigner(String alignment, String direction, boolean mirrored) {
		if (alignment == null || CSSConstants.CSS_JUSTIFY_VALUE.equals(alignment)) {
			alignment = getDefaultAlignment(direction);
		}
		if (!mirrored) {
			return alignment;
		}
		if (CSSConstants.CSS_RIGHT_VALUE.equals(alignment)) {
			return CSSConstants.CSS_LEFT_VALUE;
		}
		if (CSSConstants.CSS_LEFT_VALUE.equals(alignment)) {
			return CSSConstants.CSS_RIGHT_VALUE;
		}
		return alignment;
	}

	public static boolean isRightAligned(IContent content, String align, boolean lastLine) {
		return CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase(align) || (content != null && content.isDirectionRTL()
				&& (null == align || lastLine && CSSConstants.CSS_JUSTIFY_VALUE.equalsIgnoreCase(align)));
	}

	public static boolean isRightAligned(IContent content, CSSValue align, boolean lastLine) {
		return isRightAligned(content, align != null ? align.getCssText() : null, lastLine);
	}

}
