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
