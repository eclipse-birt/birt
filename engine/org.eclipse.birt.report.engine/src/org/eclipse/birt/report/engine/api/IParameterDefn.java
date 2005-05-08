/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

/**
 * base interface for a BIRT report parameter
 */
public interface IParameterDefn extends IParameterDefnBase
{	
	/**
	 * returns whether the parameter is a hidden parameter
	 * 
	 * @return whether the parameter is a hidden parameter
	 */
	public boolean isHidden( );
}