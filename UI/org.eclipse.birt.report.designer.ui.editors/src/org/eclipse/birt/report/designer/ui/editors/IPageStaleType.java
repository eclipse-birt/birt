/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

public interface IPageStaleType {

	/**
	 * None stale type.
	 */
	int NONE = 0;

	/**
	 * Model had change stale type.
	 */
	int MODEL_CHANGED = 1;

	/**
	 * Code had change stale type.
	 */
	int CODE_CHANGED = 2;

	int MODEL_RELOAD = 4;
}
