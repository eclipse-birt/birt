/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
	public static final Map<CursorType, Integer> CURSOR_MAP = new HashMap<>();
	static {
		CURSOR_MAP.put(CursorType.CROSSHAIR, Cursor.CROSSHAIR_CURSOR);
		CURSOR_MAP.put(CursorType.DEFAULT, Cursor.DEFAULT_CURSOR);
		CURSOR_MAP.put(CursorType.POINTER, Cursor.HAND_CURSOR);
		CURSOR_MAP.put(CursorType.MOVE, Cursor.MOVE_CURSOR);
		CURSOR_MAP.put(CursorType.TEXT, Cursor.TEXT_CURSOR);
		CURSOR_MAP.put(CursorType.WAIT, Cursor.WAIT_CURSOR);
		CURSOR_MAP.put(CursorType.ERESIZE, Cursor.E_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.NE_RESIZE, Cursor.NE_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.NW_RESIZE, Cursor.NW_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.NRESIZE, Cursor.N_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.SE_RESIZE, Cursor.SE_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.SW_RESIZE, Cursor.SW_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.SRESIZE, Cursor.S_RESIZE_CURSOR);
		CURSOR_MAP.put(CursorType.WRESIZE, Cursor.W_RESIZE_CURSOR);
	}
}
