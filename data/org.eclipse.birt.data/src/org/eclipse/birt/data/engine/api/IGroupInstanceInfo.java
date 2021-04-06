
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.api;

/**
 * This class provides information that can identify a unique group instance of
 * a ResultSet.
 *
 */

public interface IGroupInstanceInfo

{

	/**
	 * Return row number of one of rows in the group.
	 * 
	 * @return
	 */

	public int getRowId();

	/**
	 * Return the group level of target group instance.
	 * 
	 * @return
	 */

	public int getGroupLevel();

}
