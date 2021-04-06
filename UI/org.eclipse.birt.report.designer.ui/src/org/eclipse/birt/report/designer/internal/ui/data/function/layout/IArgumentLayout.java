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
package org.eclipse.birt.report.designer.internal.ui.data.function.layout;

public interface IArgumentLayout {
//	0x0 -
//	By default, the layout hint returns 0, which means the argument should be layouted in next line and the label is before the value field. This meets most default behavior for the standard argument layout.
//
//	0x10 -
//	The argument should be layouted inline with previous one, the label is before the value field.
//
//	0x11 -
//	The argument should be layouted inline with previous one, the label is after the value field.
//
//	0x12-
//	the argument should be layouted inline with previous one, and with no label.	
	public static final int ALIGN_BLOCK = 0x0;
	public static final int ALIGN_INLINE = 0x10;
	public static final int ALIGN_INLINE_BEFORE = 0x10;
	public static final int LIGN_INLINEL_AFTER = 0x11;
	public static final int ALIGN_INLINE_NONE = 0x12;

	public int getLayoutHint();

	public void setLayoutHint(int layoutHint);

	public String getName();
}
