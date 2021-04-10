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
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * This implementation of IMatchResultObjectSeek is used for Cartesian join
 * only.
 */
public class CartesianResultObjectSeeker implements IMatchResultObjectSeeker {
	//
	private IJoinConditionMatcher matcher;
	private IResultIterator secondaryIterator;

	/**
	 * Constructor.
	 * 
	 * @param matcher
	 */
	CartesianResultObjectSeeker(IJoinConditionMatcher matcher) {
		this.matcher = matcher;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.jointdataset.IMatchResultObjectSeeker#
	 * getNextMatchedResultObject(int)
	 */
	public IResultObject getNextMatchedResultObject(int currentPrimaryIndex) throws DataException {
		IResultObject result = null;

		do {
			if (secondaryIterator.getCurrentResult() != null && matcher.match()) {
				result = this.secondaryIterator.getCurrentResult();
				secondaryIterator.next();
				return result;
			}
		} while (secondaryIterator.next());

		secondaryIterator.first(0);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.jointdataset.IMatchResultObjectSeeker#
	 * setResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator)
	 */
	public void setResultIterator(IResultIterator ri) {
		this.secondaryIterator = ri;
	}
}
