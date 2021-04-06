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
