/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TablePaneFigure;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class TableSelectionEditPolicy extends SelectionEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {

		((TablePaneFigure) this.getHostFigure()).setDeselectedColors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		((TablePaneFigure) this.getHostFigure()).setSelectedColors();
	}

}