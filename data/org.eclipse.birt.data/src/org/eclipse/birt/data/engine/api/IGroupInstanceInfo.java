
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

	int getRowId();

	/**
	 * Return the group level of target group instance.
	 *
	 * @return
	 */

	int getGroupLevel();

}
