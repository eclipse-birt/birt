/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.script;

import org.eclipse.jface.text.Position;

/**
 * This class is a representation of method info.
 */
public class ScriptMethodInfo implements IScriptMethodInfo {

	/** The method name. */
	private String name;

	/** The method position. */
	private Position position;

	/**
	 * Creates a <code>ScriptMethodInfo</code> object with the specified name and
	 * the specified position.
	 *
	 * @param name     the method name.
	 * @param position the method position.
	 */
	public ScriptMethodInfo(String name, Position position) {
		setName(name);
		setPosition(position);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptMethodInfo
	 * #getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the method name.
	 *
	 * @param name the method name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.script.IScriptMethodInfo
	 * #getPosition()
	 */
	@Override
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the method position.
	 *
	 * @param position the method position to set.
	 */
	public void setPosition(Position position) {
		this.position = position;
	}
}
