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
