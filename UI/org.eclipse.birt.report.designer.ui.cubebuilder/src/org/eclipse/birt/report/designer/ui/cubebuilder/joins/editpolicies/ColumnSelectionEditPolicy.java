/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class ColumnSelectionEditPolicy extends SelectionEditPolicy {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	@Override
	protected void hideSelection() {
		((ColumnFigure) this.getHostFigure()).setDeselectedColors();
		((ColumnFigure) this.getHostFigure()).setDeselectedFonts();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	@Override
	protected void showSelection() {
		if (this.getHost().getSelected() == EditPart.SELECTED_PRIMARY) {
			((ColumnFigure) this.getHostFigure()).setSelectedColors();
		} else {
			((ColumnFigure) this.getHostFigure()).setSelectedFonts();
		}
	}
}
