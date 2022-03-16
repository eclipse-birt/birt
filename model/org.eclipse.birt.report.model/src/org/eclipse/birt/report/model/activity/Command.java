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

package org.eclipse.birt.report.model.activity;

import org.eclipse.birt.report.model.core.Module;

/**
 * This class is the base class for commands that modify elements. Provides
 * utility methods needed by derived classes.
 * <p>
 * A command is an application-level operation. A command may translate into any
 * number of activity records, possibly none. Commands are not directly undone
 * or redone; instead the activity records that implement the command are undone
 * and redone.
 *
 */

public abstract class Command {

	/**
	 * The module that provides access to the command stack.
	 */

	protected Module module = null;

	/**
	 * Optional UI hint. The sender identifies the UI that issued this command. This
	 * allows the UI to ignore notifications for change that it, itself, made. This
	 * behavior is optional, and the need for it depends on the implementation of
	 * any particular part of the UI.
	 */

	protected Object sender = null;

	/**
	 * Constructor.
	 *
	 * @param module the module
	 */

	public Command(Module module) {
		this.module = module;
	}

	/**
	 * Returns the activity stack.
	 *
	 * @return the activity stack.
	 */

	public ActivityStack getActivityStack() {
		return module.getActivityStack();
	}

	/**
	 * Returns the module.
	 *
	 * @return the module.
	 */

	public Module getModule() {
		return module;
	}

	/**
	 * Returns the UI element that issued this command.
	 *
	 * @return the sender.
	 */

	public Object getSender() {
		return sender;
	}

	/**
	 * Sets the optional UI hint for this command.
	 *
	 * @param hint The sender to set.
	 */

	public void setSender(Object hint) {
		sender = hint;
	}

}
