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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.action.DeleteJoinAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.JoinSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnection;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class JoinConditionEditPart extends AbstractConnectionEditPart {

	private static final String SELECTION_POLICY = "Selection Policy"; //$NON-NLS-1$

	/**
	 * @param context
	 * @param join
	 */
	public JoinConditionEditPart(EditPart context, DimensionJoinConditionHandle join) {
		setModel(join);
		setParent(context);
	}

	@Override
	protected IFigure createFigure() {
		return new ColumnConnection();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(SELECTION_POLICY, new JoinSelectionEditPolicy());
	}

	public DeleteJoinAction getRemoveAction() {
		DeleteJoinAction removeAction = new DeleteJoinAction(this, getModel());
		return removeAction;
	}

}
