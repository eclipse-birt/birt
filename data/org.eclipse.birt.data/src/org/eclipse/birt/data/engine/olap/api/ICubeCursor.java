
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.api;

import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public interface ICubeCursor extends CubeCursor
{
	public Scriptable getScope();
	
	/**
	 * Return a sub cube cursor, the content of which based on the current
	 * position of a cube cursor.
	 * 
	 * @param startingColumnLevel:
	 *            Indicates starting from which column level should the sub cube
	 *            cursor includes a full set of level member.
	 * @param startingRowLevel:
	 *            Indicates starting from which row level should the sub cube
	 *            cursor includes a full set of level member.
	 * @param stargingPageLevel:
	 *            Indicates starting from which page level should the sub cube
	 *            cursor includes a full set of level member.
	 * @return A slice of current cube cursor.
	 * @throws DataException
	 */
	public ICubeCursor getSubCubeCursor( String startingColumnLevel,
			String startingRowLevel, String startingPageLevel, Scriptable subScope )
			throws DataException;
}
