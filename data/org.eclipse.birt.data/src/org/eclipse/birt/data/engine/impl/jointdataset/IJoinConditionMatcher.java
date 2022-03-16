/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * This is the interface which define the behavior of a JointDataSet matcher,
 * which is used to test whether the join condition is held or not.
 */
public interface IJoinConditionMatcher {
	/**
	 * Return whether the join conditions are matching.
	 *
	 * @return
	 * @throws DataException
	 */
	boolean match() throws DataException;

	/**
	 * Compares the two specified key objects.
	 *
	 * @return
	 * @throws DataException
	 */
	int compare(Object[] lObjects, Object[] rObjects) throws DataException;

	/**
	 * Get list of compare values.
	 *
	 * @param left
	 * @return
	 * @throws DataException
	 */
	Object[] getCompareValue(boolean left) throws DataException;
}
