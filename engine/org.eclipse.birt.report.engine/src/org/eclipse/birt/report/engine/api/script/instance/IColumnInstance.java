/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.instance;

public interface IColumnInstance {

	/**
	 * Get the style of this column.
	 * 
	 */
	IScriptStyle getStyle();

	/**
	 * Get the width of this column.
	 * 
	 */
	String getWidth();

	/**
	 * Set the width of this column.
	 * 
	 */
	void setWidth(String width);

}
