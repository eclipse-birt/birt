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

package org.eclipse.birt.report.engine.api;

import java.util.ArrayList;

/**
 * The interface for objects which visually groups report parameters.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:08:26 $
 */
public interface IParameterGroupDefn extends IParameterDefnBase
{
	/**
	 * returns the set of parameters that appear inside the group.
	 * 
	 * @return the set of parameters that appear inside the group.
	 */
	public ArrayList getContents( );
	
	/**
	 * @return whether to display the parameter group expanded
	 */
	public boolean displayExpanded(); 
}