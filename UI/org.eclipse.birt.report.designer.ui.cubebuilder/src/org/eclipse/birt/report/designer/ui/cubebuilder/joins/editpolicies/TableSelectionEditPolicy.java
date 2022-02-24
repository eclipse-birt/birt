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
