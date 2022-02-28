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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.commands.SetPropertyCommand;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.PlaceHolderEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * An EditPolicy for use with container editparts. This policy can be used to
 * contribute commands to direct edit.
 *
 *
 */
public class LabelDirectEditPolicy extends DirectEditPolicy {

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest edit) {
		String labelText = (String) edit.getCellEditor().getValue();
		Map extendsData = new HashMap();
		extendsData.put(DEUtil.ELEMENT_LABELCONTENT_PROPERTY, labelText);
		EditPart host = getHost();
		Object model = null;
		if (host instanceof LabelEditPart) {
			LabelEditPart label = (LabelEditPart) getHost();
			model = label.getModel();
		} else if (host instanceof PlaceHolderEditPart) {
			PlaceHolderEditPart label = (PlaceHolderEditPart) getHost();
			model = label.getCopiedModel();
		}

		SetPropertyCommand command = new SetPropertyCommand(model, extendsData);
		return command;
	}

	/**
	 * @see DirectEditPolicy#showCurrentEditValue(DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((LabelFigure) getHostFigure()).setText(value);
		// hack to prevent async layout from placing the cell editor twice.
		getHostFigure().getUpdateManager().performUpdate();

	}

	@Override
	public boolean understandsRequest(Request request) {
		if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())
				|| RequestConstants.REQ_OPEN.equals(request.getType())
				|| ReportRequest.CREATE_ELEMENT.equals(request.getType())) {
			return true;
		}
		return super.understandsRequest(request);
	}

}
