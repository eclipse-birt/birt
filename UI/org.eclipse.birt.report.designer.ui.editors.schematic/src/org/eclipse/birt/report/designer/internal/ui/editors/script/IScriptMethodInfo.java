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

import java.util.Collection;

/**
 * This interface for method info of script.
 */
public interface IScriptMethodInfo
{

	/**
	 * Returns a collection of all method info. Elements are instance of
	 * <code>MethodInfo</code>.
	 * 
	 * @return a unmodifiable collection of all method info. Elements are instance of
	 *         <code>MethodInfo</code>.
	 */
	Collection getAllMethodInfo( );
}
