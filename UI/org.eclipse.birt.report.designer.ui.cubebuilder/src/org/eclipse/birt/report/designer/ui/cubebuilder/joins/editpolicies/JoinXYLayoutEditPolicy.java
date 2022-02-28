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

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands.SetConstraintCommand;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.DatasetNodeEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

public class JoinXYLayoutEditPolicy extends XYLayoutEditPolicy {

	private TabularCubeHandle cube;

	public JoinXYLayoutEditPolicy(TabularCubeHandle cube) {
		this.cube = cube;
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
		// TODO Auto-generated method stub
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
		SetConstraintCommand locationCommand = new SetConstraintCommand();
		if (child instanceof DatasetNodeEditPart) {
			locationCommand.setModuleHandle(((DatasetNodeEditPart) child).getCube().getRoot());
		} else {
			locationCommand.setModuleHandle(((DesignElementHandle) child.getModel()).getRoot());
		}

		locationCommand.setId(UIHelper.getId(child.getModel(), cube));

		Rectangle rect = new Rectangle((Rectangle) constraint);
		locationCommand.setLocation(rect);
		return locationCommand;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new ResizableEditPolicy() {

			@Override
			protected IFigure createDragSourceFeedbackFigure() {
				// Use a ghost rectangle for feedback
				Figure r = new Figure() {

					@Override
					protected void paintFigure(Graphics graphics) {
						Rectangle rect = getBounds().getCopy();

						graphics.setXORMode(true);
						graphics.setForegroundColor(ColorConstants.white);
						graphics.setBackgroundColor(ColorManager.getColor(31, 31, 31));

						graphics.translate(getLocation());

						PointList outline = new PointList();

						outline.addPoint(0, 0);
						outline.addPoint(rect.width, 0);
						outline.addPoint(rect.width - 1, 0);
						outline.addPoint(rect.width - 1, rect.height - 1);
						outline.addPoint(0, rect.height - 1);

						graphics.fillPolygon(outline);

						// draw the inner outline
						PointList innerLine = new PointList();

						innerLine.addPoint(rect.width - 0 - 1, 0);
						innerLine.addPoint(rect.width - 0 - 1, 0);
						innerLine.addPoint(rect.width - 1, 0);
						innerLine.addPoint(rect.width - 0 - 1, 0);
						innerLine.addPoint(0, 0);
						innerLine.addPoint(0, rect.height - 1);
						innerLine.addPoint(rect.width - 1, rect.height - 1);
						innerLine.addPoint(rect.width - 1, 0);

						graphics.drawPolygon(innerLine);

						graphics.drawLine(rect.width - 0 - 1, 0, rect.width - 1, 0);

						graphics.translate(getLocation().getNegated());
					}
				};

				r.setBounds(getInitialFeedbackBounds());
				addFeedback(r);
				return r;
			}

		};
	}
}
