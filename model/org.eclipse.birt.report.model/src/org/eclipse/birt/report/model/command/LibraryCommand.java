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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.core.Module;

/**
 * Represents the command for adding and dropping library from report design.
 * For each operation, should start a new command instead of using the existing
 * command.
 */

public class LibraryCommand extends LibraryCommandImp {
	/**
	 * Construct the command with the report design.
	 *
	 * @param module the report design
	 */

	public LibraryCommand(Module module) {
		super(module);
	}
}
