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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies;

import org.eclipse.draw2d.XYLayout;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Crosstable editpart layout police
 */

public class CrosstabXYLayoutEditPolicy extends XYLayoutEditPolicy {

	/**
	 * Constructor
	 *
	 * @param layout
	 */
	public CrosstabXYLayoutEditPolicy(XYLayout layout) {
		super();
		setXyLayout(layout);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.requests.ChangeBoundsRequest,
	 * org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new CrosstabCellResizeEditPolicy();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org
	 * .eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.
	 * gef.requests.CreateRequest)
	 */
	@Override
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.
	 * eclipse.gef.Request)
	 */
	@Override
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#
	 * getResizeChildrenCommand(org.eclipse.gef.requests.ChangeBoundsRequest)
	 */
	@Override
	protected Command getResizeChildrenCommand(ChangeBoundsRequest request) {
		return null;
	}
}
