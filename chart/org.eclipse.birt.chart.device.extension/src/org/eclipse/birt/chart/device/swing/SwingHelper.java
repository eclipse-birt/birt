/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.swing;

import java.awt.Cursor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.CursorType;

/**
 * The class provides methods for swing.
 * 
 * @since 2.5
 */

public class SwingHelper {
	/** This field maps the standard cursor types to Swing cursor types. */
	public static final Map<CursorType, Integer> CURSOR_MAP = new HashMap<org.eclipse.birt.chart.model.attribute.CursorType, Integer>();
	static {
		CURSOR_MAP.put(CursorType.CROSSHAIR, Integer.valueOf(Cursor.CROSSHAIR_CURSOR));
		CURSOR_MAP.put(CursorType.DEFAULT, Integer.valueOf(Cursor.DEFAULT_CURSOR));
		CURSOR_MAP.put(CursorType.POINTER, Integer.valueOf(Cursor.HAND_CURSOR));
		CURSOR_MAP.put(CursorType.MOVE, Integer.valueOf(Cursor.MOVE_CURSOR));
		CURSOR_MAP.put(CursorType.TEXT, Integer.valueOf(Cursor.TEXT_CURSOR));
		CURSOR_MAP.put(CursorType.WAIT, Integer.valueOf(Cursor.WAIT_CURSOR));
		CURSOR_MAP.put(CursorType.ERESIZE, Integer.valueOf(Cursor.E_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.NE_RESIZE, Integer.valueOf(Cursor.NE_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.NW_RESIZE, Integer.valueOf(Cursor.NW_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.NRESIZE, Integer.valueOf(Cursor.N_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.SE_RESIZE, Integer.valueOf(Cursor.SE_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.SW_RESIZE, Integer.valueOf(Cursor.SW_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.SRESIZE, Integer.valueOf(Cursor.S_RESIZE_CURSOR));
		CURSOR_MAP.put(CursorType.WRESIZE, Integer.valueOf(Cursor.W_RESIZE_CURSOR));
	}
}
