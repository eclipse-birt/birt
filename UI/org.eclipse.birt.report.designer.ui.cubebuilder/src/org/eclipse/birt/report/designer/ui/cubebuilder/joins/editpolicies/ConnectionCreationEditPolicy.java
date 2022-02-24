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

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands.AddJoinConditionCommand;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.commands.ConnectionCommand;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FeedbackHelper;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.gef.requests.ReconnectRequest;

public class ConnectionCreationEditPolicy extends GraphicalNodeEditPolicy {

	public ConnectionCreationEditPolicy() {
		super();
	}

	protected org.eclipse.draw2d.Connection createDummyConnection(org.eclipse.gef.Request req) {
		ColumnConnection conn = new ColumnConnection();
		return conn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getConnectionCompleteCommand
	 * (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected org.eclipse.gef.commands.Command getConnectionCompleteCommand(CreateConnectionRequest request) {

		ConnectionCommand command = (ConnectionCommand) request.getStartCommand();

		if (command == null)
			return null;
		EditPart sourcePart = command.getSource();
		if (!(getHost() instanceof ColumnEditPart) || getHost() == sourcePart
				|| getHost().getParent() == sourcePart.getParent()) {
			return null;
		}
		ColumnEditPart targetPart = (ColumnEditPart) getHost();
		command.setTarget(targetPart);

		AddJoinConditionCommand addJoinConditionCommand = new AddJoinConditionCommand(sourcePart, targetPart);
		return addJoinConditionCommand;
	}

	protected EditPart getSourceEditPart() {
		return getHost();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getConnectionCreateCommand (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected org.eclipse.gef.commands.Command getConnectionCreateCommand(CreateConnectionRequest request) {

		ConnectionCommand command = new ConnectionCommand();
		command.setSource(getSourceEditPart());
		request.setStartCommand(command);
		return command;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		// TODO Auto-generated method stub
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#eraseCreationFeedback
	 * (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected void eraseCreationFeedback(CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		super.eraseCreationFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	public void eraseSourceFeedback(Request request) {
		// TODO Auto-generated method stub
		super.eraseSourceFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * eraseTargetConnectionFeedback(org.eclipse.gef.requests.DropRequest)
	 */
	protected void eraseTargetConnectionFeedback(DropRequest request) {
		// TODO Auto-generated method stub
		super.eraseTargetConnectionFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		// TODO Auto-generated method stub
		super.eraseTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	public Command getCommand(Request request) {
		// TODO Auto-generated method stub
		return super.getCommand(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getFeedbackHelper
	 * (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected FeedbackHelper getFeedbackHelper(CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		return super.getFeedbackHelper(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getSourceConnectionAnchor (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected ConnectionAnchor getSourceConnectionAnchor(CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		return super.getSourceConnectionAnchor(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * getTargetConnectionAnchor (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected ConnectionAnchor getTargetConnectionAnchor(CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		return super.getTargetConnectionAnchor(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	public EditPart getTargetEditPart(Request request) {
		// TODO Auto-generated method stub
		return super.getTargetEditPart(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#showCreationFeedback
	 * (org.eclipse.gef.requests.CreateConnectionRequest)
	 */
	protected void showCreationFeedback(CreateConnectionRequest request) {
		// TODO Auto-generated method stub
		super.showCreationFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	public void showSourceFeedback(Request request) {
		// TODO Auto-generated method stub
		super.showSourceFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#
	 * showTargetConnectionFeedback(org.eclipse.gef.requests.DropRequest)
	 */
	protected void showTargetConnectionFeedback(DropRequest request) {
		// TODO Auto-generated method stub
		super.showTargetConnectionFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request request) {
		// TODO Auto-generated method stub
		super.showTargetFeedback(request);
	}
}
