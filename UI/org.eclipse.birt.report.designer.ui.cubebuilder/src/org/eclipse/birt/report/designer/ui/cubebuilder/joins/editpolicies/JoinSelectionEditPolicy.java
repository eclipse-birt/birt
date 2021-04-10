/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.JoinConditionEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

public class JoinSelectionEditPolicy extends SelectionEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#hideSelection()
	 */
	protected void hideSelection() {
		JoinConditionEditPart part = (JoinConditionEditPart) this.getHost();
		((ColumnConnection) this.getHostFigure()).setLineWidth(1);
		if (part.getTarget() instanceof ColumnEditPart) {
			if (part.getSource() != null)
				part.getSource().setSelected(EditPart.SELECTED_NONE);
			part.getTarget().setSelected(EditPart.SELECTED_NONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.SelectionEditPolicy#showSelection()
	 */
	protected void showSelection() {
		JoinConditionEditPart part = (JoinConditionEditPart) this.getHost();
		((ColumnConnection) this.getHostFigure()).setLineWidth(2);
		if (part.getTarget() instanceof ColumnEditPart) {
			if (part.getSource() != null)
				part.getSource().setSelected(EditPart.SELECTED);
			part.getTarget().setSelected(EditPart.SELECTED);
		}
	}

}
