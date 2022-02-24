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
