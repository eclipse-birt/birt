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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.layout.MultipleLayout;
import org.eclipse.draw2d.Figure;

/**
 * Multiple figure.
 */

public class MultipleFigure extends Figure {
	/**
	 * Constructor
	 */
	public MultipleFigure() {
		// setBorder( new LineBorder(1) );
		setLayoutManager(new MultipleLayout());
	}
}
