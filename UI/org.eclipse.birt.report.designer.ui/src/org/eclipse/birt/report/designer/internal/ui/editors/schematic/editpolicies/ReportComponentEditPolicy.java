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

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

/**
 * Component Role edit policy Provide delete support
 *
 *
 */
public class ReportComponentEditPolicy extends ComponentEditPolicy {

	/**
	 * Constructor
	 */
	public ReportComponentEditPolicy() {
		super();
	}

	@Override
	protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand command = new DeleteCommand(this.getHost().getModel());
		return command;
	}
}
