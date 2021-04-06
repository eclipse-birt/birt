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

package org.eclipse.birt.chart.device.swt;

import org.eclipse.birt.chart.device.swt.i18n.Messages;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Display;

import com.ibm.icu.util.ULocale;

/**
 * R31Enhance
 */
final class R31Enhance {

	private static final boolean R31_AVAILABLE;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.extension/swt"); //$NON-NLS-1$

	static {
		// Check if the advanced graphics is present.
		GC gc = new GC(Display.getDefault());
		gc.setAdvanced(true);
		R31_AVAILABLE = gc.getAdvanced();
		gc.dispose();

		if (R31_AVAILABLE) {
			logger.log(ILogger.INFORMATION, Messages.getString("R31Enhance.info.advanced.enabled", //$NON-NLS-1$
					ULocale.getDefault()));
		} else {
			logger.log(ILogger.INFORMATION, Messages.getString("R31Enhance.info.advanced.disabled", //$NON-NLS-1$
					ULocale.getDefault()));
		}
	}

	private R31Enhance() {
	}

	/**
	 * Returns if r3.1 new feature available.
	 * 
	 * @return
	 */
	public static boolean isR31Available() {
		return R31_AVAILABLE;
	}

	/**
	 * Equivalent to <code><b>GC.setAdvanced()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param value
	 */
	static void setAdvanced(GC gc, boolean value, Region clipping) {
		if (R31_AVAILABLE) {
			gc.setAdvanced(value);

			// setAdvanced will clean the clipping info, restore it here.
			gc.setClipping(clipping);
		}
	}

	/**
	 * Equivalent to <code><b>GC.setAlpha()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param value
	 */
	static void setAlpha(GC gc, int value) {
		if (R31_AVAILABLE) {
			gc.setAlpha(value);
		}
	}

	/**
	 * Equivalent to <code><b>GC.setAntialias()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param value
	 */
	static void setAntialias(GC gc, int value) {
		if (R31_AVAILABLE) {
			gc.setAntialias(value);
		}
	}

	/**
	 * Equivalent to <code><b>GC.setTextAntialias()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param value
	 */
	static void setTextAntialias(GC gc, int value) {
		if (R31_AVAILABLE) {
			gc.setTextAntialias(value);
		}
	}

	/**
	 * Equivalent to <code><b>new Transform()</b></code> in r3.1.
	 * 
	 * @param param
	 * @return
	 */
	static Object newTransform(Device param) {
		if (R31_AVAILABLE) {
			return new Transform(param);
		}

		return null;
	}

	/**
	 * Equivalent to <code><b>GC.setTransform()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param value
	 */
	static void setTransform(GC gc, Object value) {
		if (R31_AVAILABLE) {
			gc.setTransform((Transform) value);
		}
	}

	/**
	 * Equivalent to <code><b>Transform.translate()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param transObject
	 * @param v1
	 * @param v2
	 */
	static void translate(GC gc, Object transform, float v1, float v2) {
		if (R31_AVAILABLE) {
			((Transform) transform).translate(v1, v2);
		}
	}

	/**
	 * Equivalent to <code><b>Transform.rotate()</b></code> in r3.1.
	 * 
	 * @param gc
	 * @param transform
	 * @param value
	 */
	static void rotate(GC gc, Object transform, float value) {
		if (R31_AVAILABLE) {
			((Transform) transform).rotate(value);
		}
	}

	/**
	 * Equivalent to <code><b>Transform.dispose()</b></code> in r3.1.
	 * 
	 * @param transform
	 */
	static void disposeTransform(Object transform) {
		if (R31_AVAILABLE) {
			((Transform) transform).dispose();
		}
	}

	/**
	 * Convenient method to enable alpha effect on GC if r31 available.
	 * 
	 * @param gc
	 * @param cd
	 * @param useOpaque
	 */
	static void setAlpha(GC gc, ColorDefinition cd) {
		if (R31_AVAILABLE) {
			if (cd != null && cd.isSetTransparency()) {
				setAlpha(gc, cd.getTransparency());
			} else {
				setAlpha(gc, 255);
			}
		}
	}

	/**
	 * Convenient method to enable alpha effect on GC if r31 available.
	 * 
	 * @param gc
	 * @param cd
	 * @param useOpaque
	 */
	static void setAlpha(GC gc, Gradient g) {
		if (R31_AVAILABLE) {
			if (g != null && g.isSetTransparency()) {
				setAlpha(gc, g.getTransparency());
			} else {
				setAlpha(gc, 255);
			}
		}
	}

}
