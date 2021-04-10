/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	protected org.eclipse.gef.commands.Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteCommand command = new DeleteCommand(this.getHost().getModel());
		return command;
	}
}