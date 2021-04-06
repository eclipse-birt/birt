/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - modification of Batik's SystemColorSupport.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine;

import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.RGBColorValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;

/**
 * This class provides support for AWT system colors.
 *
 */
public class SystemColorSupport implements CSSConstants {

	/**
	 * Returns the Value corresponding to the given system color.
	 */
	public static CSSValue getSystemColor(String ident) {
		ident = ident.toLowerCase();
		SystemColor sc = (SystemColor) factories.get(ident);
		return new RGBColorValue(new FloatValue(CSSPrimitiveValue.CSS_NUMBER, sc.getRed()),
				new FloatValue(CSSPrimitiveValue.CSS_NUMBER, sc.getGreen()),
				new FloatValue(CSSPrimitiveValue.CSS_NUMBER, sc.getBlue()));
	}

	/**
	 * The color factories.
	 */
	protected final static Map factories = new HashMap();
	static {
		factories.put(CSS_ACTIVEBORDER_VALUE, SystemColor.windowBorder);
		factories.put(CSS_ACTIVECAPTION_VALUE, SystemColor.activeCaption);
		factories.put(CSS_APPWORKSPACE_VALUE, SystemColor.desktop);
		factories.put(CSS_BACKGROUND_VALUE, SystemColor.desktop);
		factories.put(CSS_BUTTONFACE_VALUE, SystemColor.control);
		factories.put(CSS_BUTTONHIGHLIGHT_VALUE, SystemColor.controlLtHighlight);
		factories.put(CSS_BUTTONSHADOW_VALUE, SystemColor.controlDkShadow);
		factories.put(CSS_BUTTONTEXT_VALUE, SystemColor.controlText);
		factories.put(CSS_CAPTIONTEXT_VALUE, SystemColor.activeCaptionText);
		factories.put(CSS_GRAYTEXT_VALUE, SystemColor.textInactiveText);
		factories.put(CSS_HIGHLIGHT_VALUE, SystemColor.textHighlight);
		factories.put(CSS_HIGHLIGHTTEXT_VALUE, SystemColor.textHighlightText);
		factories.put(CSS_INACTIVEBORDER_VALUE, SystemColor.windowBorder);
		factories.put(CSS_INACTIVECAPTION_VALUE, SystemColor.inactiveCaption);
		factories.put(CSS_INACTIVECAPTIONTEXT_VALUE, SystemColor.inactiveCaptionText);
		factories.put(CSS_INFOBACKGROUND_VALUE, SystemColor.info);
		factories.put(CSS_INFOTEXT_VALUE, SystemColor.infoText);
		factories.put(CSS_MENU_VALUE, SystemColor.menu);
		factories.put(CSS_MENUTEXT_VALUE, SystemColor.menuText);
		factories.put(CSS_SCROLLBAR_VALUE, SystemColor.scrollbar);
		factories.put(CSS_THREEDDARKSHADOW_VALUE, SystemColor.controlDkShadow);
		factories.put(CSS_THREEDFACE_VALUE, SystemColor.control);
		factories.put(CSS_THREEDHIGHLIGHT_VALUE, SystemColor.controlHighlight);
		factories.put(CSS_THREEDLIGHTSHADOW_VALUE, SystemColor.controlLtHighlight);
		factories.put(CSS_THREEDSHADOW_VALUE, SystemColor.controlShadow);
		factories.put(CSS_WINDOW_VALUE, SystemColor.window);
		factories.put(CSS_WINDOWFRAME_VALUE, SystemColor.windowBorder);
		factories.put(CSS_WINDOWTEXT_VALUE, SystemColor.windowText);
	}

	/**
	 * This class does not need to be instantiated.
	 */
	protected SystemColorSupport() {
	}
}
