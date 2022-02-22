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

package org.eclipse.birt.report.designer.internal.ui.util.bidi;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Widget;

/**
 * Bidi API used by design GUI.
 *
 * @author bidi_hcg
 *
 */
public class BidiUIUtils {
	public static final char LRE = '\u202a';
	public static final char RLE = '\u202b';
	public static final char PDF = '\u202c';

	private static int OS_STYLE_INDEX = 0;
	private static int WS_EX_LAYOUTRTL = 0;
	private static int WS_EX_NOINHERITLAYOUT = 0;
	private static Class osWinClass = null;
	private static Method GET_WINDOW_LONG = null;
	private static Method SET_WINDOW_LONG = null;
	private static Method INVALIDATE_RECT = null;
	private static Field STYLE_FIELD = null;
	private static Field HANDLE = null;

	private TextLayout layout;
	private boolean isInitialized = false;

	public static final BidiUIUtils INSTANCE = new BidiUIUtils();

	private BidiUIUtils() {
	}

	private void init() {
		if (isInitialized) {
			return;
		}

		isInitialized = true;

		try {
			osWinClass = Class.forName("org.eclipse.swt.internal.win32.OS"); //$NON-NLS-1$

			if (osWinClass != null) {
				GET_WINDOW_LONG = osWinClass.getMethod("GetWindowLong", //$NON-NLS-1$
						new Class[] { Integer.TYPE, Integer.TYPE });

				SET_WINDOW_LONG = osWinClass.getMethod("SetWindowLongW", //$NON-NLS-1$
						new Class[] { Integer.TYPE, Integer.TYPE, Integer.TYPE });

				INVALIDATE_RECT = osWinClass.getMethod("InvalidateRect", //$NON-NLS-1$
						new Class[] { Integer.TYPE, Class.forName("org.eclipse.swt.internal.win32.RECT"), //$NON-NLS-1$
								Boolean.TYPE });

				Field field = osWinClass.getField("GWL_EXSTYLE"); //$NON-NLS-1$
				OS_STYLE_INDEX = field.getInt(null);

				field = osWinClass.getField("WS_EX_LAYOUTRTL"); //$NON-NLS-1$
				WS_EX_LAYOUTRTL = field.getInt(null);

				field = osWinClass.getField("WS_EX_NOINHERITLAYOUT"); //$NON-NLS-1$
				WS_EX_NOINHERITLAYOUT = field.getInt(null);

				STYLE_FIELD = Widget.class.getDeclaredField("style"); //$NON-NLS-1$
				STYLE_FIELD.setAccessible(true);

				HANDLE = Control.class.getDeclaredField("handle"); //$NON-NLS-1$
				HANDLE.setAccessible(true);
			}
		} catch (ClassNotFoundException e) {
			osWinClass = null;
			// Don't need handle the exception
			// ExceptionHandler.handle( e, true );
		} catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			osWinClass = null;
			ExceptionHandler.handle(e, true);
		}
	}

	public void applyOrientation(Control control, boolean mirrored) {
		if (control == null) {
			return;
		}
		if (!isInitialized) {
			init();
		}
		if (osWinClass == null) {
			return;
		}

		int swtStyle = control.getStyle() & ~(SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT | SWT.MIRRORED);
		try {
			int osStyle = ((Integer) GET_WINDOW_LONG.invoke(null,
					new Object[] { Integer.valueOf(getControHandle(control)), Integer.valueOf(OS_STYLE_INDEX) }))
							.intValue();

			if (mirrored) {
				SET_WINDOW_LONG.invoke(null,
						new Object[] { Integer.valueOf(getControHandle(control)), Integer.valueOf(OS_STYLE_INDEX),
								Integer.valueOf(osStyle | WS_EX_LAYOUTRTL | WS_EX_NOINHERITLAYOUT) });
				swtStyle |= SWT.RIGHT_TO_LEFT | SWT.MIRRORED;
				STYLE_FIELD.setInt(control, swtStyle);
			} else {
				SET_WINDOW_LONG.invoke(null, new Object[] { Integer.valueOf(getControHandle(control)),
						Integer.valueOf(OS_STYLE_INDEX), Integer.valueOf(osStyle & ~WS_EX_LAYOUTRTL) });
				swtStyle |= SWT.LEFT_TO_RIGHT;
				STYLE_FIELD.setInt(control, swtStyle);
			}
			INVALIDATE_RECT.invoke(null,
					new Object[] { Integer.valueOf(getControHandle(control)), null, Boolean.TRUE });
		} catch (SecurityException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
			ExceptionHandler.handle(e, true);
		}

		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			if (children != null) {
				for (int i = children.length; i-- > 0;) {
					applyOrientation(children[i], mirrored);
				}
			}
		}
	}

	public boolean isDirectionRTL(Object model) {
		return model instanceof DesignElementHandle && ((DesignElementHandle) model).isDirectionRTL();
	}

	public boolean isMirrored(Control control) {
		return control != null && (control.getStyle() & SWT.RIGHT_TO_LEFT) != 0;
	}

	private int getControHandle(Control control) {
		if (HANDLE != null) {
			try {
				return HANDLE.getInt(control);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do notjing now
			}
		}
		return -1;
	}

	/**
	 * Provides a TextLayout that can be used for Bidi purposes. This TextLayout
	 * should not be disposed by clients.
	 *
	 * @return an SWT TextLayout instance
	 */
	public synchronized TextLayout getTextLayout(int orientation) {
		if (layout == null || layout.isDisposed()) {
			layout = new TextLayout(Display.getDefault());
		}

		layout.setOrientation(orientation);
		return layout;
	}

	@Override
	protected void finalize() throws Throwable {
		System.out.println("layout finalized"); //$NON-NLS-1$
		if (layout != null && !layout.isDisposed()) {
			layout.dispose();
		}
		layout = null;
		super.finalize();
	}

}
