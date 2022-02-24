/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.SummaryGroupLevelCalculator;

/**
 * When useDetails==false, this class is used.
 */
public class ResultIterator2 extends ResultIterator {
	// the value of lower group level
	private int lowestGroupLevel;

	private int currRowIndex;

	private boolean isSummary;
	private SummaryGroupLevelCalculator groupLevelCalculator;

	/**
	 * @param context
	 * @param queryResults
	 * @param queryResultID
	 * @param lowestGroupLevel
	 * @throws DataException
	 */
	public ResultIterator2(String tempDir, DataEngineContext context, IQueryResults queryResults, String queryResultID,
			int lowestGroupLevel, boolean isSummary, IBaseQueryDefinition qd) throws DataException {
		super(tempDir, context, queryResults, queryResultID, qd);

		this.lowestGroupLevel = lowestGroupLevel;
		if (this.hasFirstNext)
			this.currRowIndex = 0;
		else
			this.currRowIndex = -1;
		this.isSummary = isSummary;
		if (this.isSummary) {
			if (lowestGroupLevel == 0)
				this.groupLevelCalculator = new SummaryGroupLevelCalculator(null);
			else {
				int[][] groupIndex = new int[lowestGroupLevel + 1][];
				for (int i = 0; i <= lowestGroupLevel; i++) {
					groupIndex[i] = this.exprResultSet.getGroupStartAndEndIndex(i);
				}

				this.groupLevelCalculator = new SummaryGroupLevelCalculator(groupIndex);
			}
		}
	}

	public ResultIterator2(String tempDir, DataEngineContext context, QueryResults queryResults, String queryResultID,
			String subQueryName, int currParentIndex, int lowestGroupLevel, IBaseQueryDefinition qd)
			throws DataException {
		super(tempDir, context, queryResults, queryResultID, subQueryName, currParentIndex, qd);
		this.lowestGroupLevel = lowestGroupLevel;
		if (this.hasFirstNext)
			this.currRowIndex = 0;
		else
			this.currRowIndex = -1;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.document.ResultIterator#doNext()
	 */
	protected boolean doNext() throws DataException {
		boolean hasNext = false;
		boolean shouldMoveForward = false;

		int index = this.exprResultSet.getCurrentIndex();
		if (this.exprResultSet.getCurrentIndex() >= 0) // not the first row
		{
			exprResultSet.skipToEnd(lowestGroupLevel);
			if ((!isSummary) && this.exprResultSet.getCurrentIndex() != index) {
				shouldMoveForward = false;
				hasNext = exprResultSet.getCurrentIndex() >= 0;

			} else {
				shouldMoveForward = true;
			}
		} else {
			shouldMoveForward = true;
		}

		if (shouldMoveForward) {
			hasNext = super.doNext();
		}
		if (hasNext) {
			currRowIndex++;
		}

		return hasNext;
	}

	public int getEndingGroupLevel() throws BirtException {
		// make sure that the ending group level value is also correct
		if (this.isSummary) {
			return this.groupLevelCalculator.getEndingGroupLevel(this.exprResultSet.getCurrentIndex());
		}

		return super.getEndingGroupLevel();
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.ResultIterator#getStartingGroupLevel()
	 * 
	 * public int getStartingGroupLevel( ) throws DataException { return
	 * cachedStartingGroupLevel; }
	 * 
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.document.ResultIterator#getEndingGroupLevel
	 * ()
	 * 
	 * public int getEndingGroupLevel( ) throws BirtException {
	 * this.exprResultSet.skipToEnd( this.lowestGroupLevel );
	 * 
	 * return super.getEndingGroupLevel( ); }
	 */

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#getRowIndex()
	 */
	public int getRowIndex() throws BirtException {
		return currRowIndex;
	}

	/*
	 * @see org.eclipse.birt.data.engine.api.IResultIterator#moveTo(int)
	 */
	public void moveTo(int rowIndex) throws BirtException {
		if (rowIndex >= 0) {
			this.isFirstNext = false;
		}
		if (rowIndex < 0 || rowIndex < this.currRowIndex)
			throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(rowIndex));
		else if (rowIndex == currRowIndex)
			return;

		int gapRows = rowIndex - currRowIndex;
		for (int i = 0; i < gapRows; i++) {
			if (this.next() == false)
				throw new DataException(ResourceConstants.INVALID_ROW_INDEX, Integer.valueOf(rowIndex));
		}
	}

}
