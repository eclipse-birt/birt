/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
