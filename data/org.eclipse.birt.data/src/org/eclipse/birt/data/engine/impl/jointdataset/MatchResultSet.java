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
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * An iterator that an user can iterate to get objects which are sequential and
 * matched for each other from another iterator.
 */
public class MatchResultSet implements IDataSetPopulator {
	private IResultIterator resultIterator = null;
	private Object[] matchValues = null;
	private IJoinConditionMatcher jcm = null;
	private boolean jcmLeft;
	private boolean isFirst;

	/**
	 * 
	 * @param resultIterator
	 * @param jcm
	 * @param jcmLeft
	 * @throws DataException
	 */
	MatchResultSet(IResultIterator resultIterator, IJoinConditionMatcher jcm, boolean jcmLeft) throws DataException {
		this.resultIterator = resultIterator;
		this.jcm = jcm;
		this.jcmLeft = jcmLeft;
		this.matchValues = jcm.getCompareValue(jcmLeft);
		this.isFirst = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next() throws DataException {
		if (!isFirst) {
			if (!resultIterator.next()) {
				return null;
			}
			if (jcm.compare(matchValues, jcm.getCompareValue(jcmLeft)) != 0) {
				return null;
			}
		}
		isFirst = false;
		return resultIterator.getCurrentResult();
	}

}
