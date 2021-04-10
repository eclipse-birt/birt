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
 * This interface for method info of script.
 */
public interface IScriptMethodInfo {

	/**
	 * Returns the method name.
	 * 
	 * @return the method name.
	 */
	String getName();

	/**
	 * Returns the method position.
	 * 
	 * @return the method position.
	 */
	Position getPosition();
}
