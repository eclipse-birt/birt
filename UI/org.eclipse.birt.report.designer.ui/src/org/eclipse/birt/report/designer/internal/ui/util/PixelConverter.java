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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;

/**
 * PixelConverter
 * 
 * @deprecated
 */
public class PixelConverter extends org.eclipse.birt.report.designer.ui.util.PixelConverter {

	public PixelConverter(Control control) {
		super(control);
	}

	public PixelConverter(Font font) {
		super(font);
	}

}
