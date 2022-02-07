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

import java.util.ArrayList;

import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.executor.cache.CacheRequest;
import org.eclipse.birt.data.engine.executor.cache.OdiAdapter;
import org.eclipse.birt.data.engine.executor.cache.SmartCache;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.document.viewing.DummyEventHandler;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * An implementation of IJointDataSetPopulator. It encapsulates the general
 * algorithm to deal with inner, left outer, and right outer joins.The right
 * outer join is actually treated as left out join in this class, except that
 * when dealing with right out join the primaryIterator would be set to the
 * right Iterator rather than left one.
 */
public class BaseJointDataSetPopulator implements IDataSetPopulator {

	//
	private JointResultMetadata meta;

	private IResultIterator primaryIterator;
	private IResultIterator secondaryIterator;
	private int joinType;

	private IJoinConditionMatcher jcm;

	private IResultObject curPrimaryResultObject = null;
	private Object[] curPrimaryMatchValues = null;

	private SmartCache curSecondaryResultObjects = null;
	private Object[] curSecondaryMatchValues = null;

	// last compared result
	private int curComparedResult = 0;

	// indicate whether this object is initialized.
	private boolean beInitialized = false;

	// indicate whether the SecondaryResultObjects have been used to create
	// joint result object
	private boolean beSecondaryUsed = false;

	private DataEngineSession session;

	private int rowFetchLimit;

	private int rowCount;

	private boolean shouldContinueSeek;

	/**
	 * Constructor.
	 * 
	 * @param left
	 * @param right
	 * @param meta
	 * @param jcm
	 * @param joinType
	 * @param seeker
	 * @throws DataException
	 */
	public BaseJointDataSetPopulator(IResultIterator left, IResultIterator right, JointResultMetadata meta,
			IJoinConditionMatcher jcm, int joinType, IMatchResultObjectSeeker seeker, DataEngineSession session,
			int rowFetchLimit) throws DataException {
		this.meta = meta;
		this.joinType = joinType;
		this.jcm = jcm;
		this.session = session;
		if (isPrimaryLeft()) {
			this.primaryIterator = left;
			this.secondaryIterator = right;
		} else {
			this.primaryIterator = right;
			this.secondaryIterator = left;
		}

		beInitialized = false;

		this.rowFetchLimit = rowFetchLimit;
		this.rowCount = 0;
		this.shouldContinueSeek = false;

	}

	/**
	 * Initialize this object.
	 * 
	 * @return
	 * @throws DataException
	 */
	private void initialize() throws DataException {
		fetchPrimaryObject();
		fetchSecondaryObjects();
		curComparedResult = getCompartorResult();
	}

	/**
	 * Return whether primary iterator is left.
	 * 
	 * @return
	 */
	private boolean isPrimaryLeft() {
		return joinType != IJointDataSetDesign.RIGHT_OUTER_JOIN;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next() throws DataException {
		if (this.rowFetchLimit <= 0 || this.rowCount < this.rowFetchLimit) {
			IResultObject result = doNext();
			while (this.shouldContinueSeek) {
				result = doNext();
			}
			if (result != null)
				this.rowCount++;
			return result;
		}

		return null;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	private IResultObject doNext() throws DataException {
		this.shouldContinueSeek = false;

		if (!beInitialized) {
			initialize();
			beInitialized = true;
		}

		if (curComparedResult == 0) {
			return equalNext();
		}

		if (curComparedResult < 0) {
			return lessNext();
		}

		if (curComparedResult > 0) {
			return greaterNext();
		}

		// Return null means there is no more rows.
		return null;
	}

	/**
	 * Return the IResultObject instance of a data set when curComparedResult is
	 * equal to 0;
	 * 
	 * @return
	 * @throws DataException
	 */
	private IResultObject equalNext() throws DataException {
		if (curSecondaryResultObjects.next()) {
			beSecondaryUsed = true;
			return createResultObject(curPrimaryResultObject,
					(IResultObject) (curSecondaryResultObjects.getCurrentResult()));
		} else {
			fetchPrimaryObject();
			curComparedResult = getCompartorResult();
			this.shouldContinueSeek = true;
			return null;
		}
	}

	/**
	 * Return the IResultObject instance of a data set when curComparedResult is
	 * less than 0;
	 * 
	 * @return
	 * @throws DataException
	 */
	private IResultObject lessNext() throws DataException {
		if (curPrimaryMatchValues == null) {
			return null;
		}
		if (joinType == IJointDataSetDesign.INNER_JOIN && curSecondaryMatchValues == null) {
			return null;
		}

		IResultObject resultObject = null;
		if (joinType != IJointDataSetDesign.INNER_JOIN)
			resultObject = createResultObject(curPrimaryResultObject, null);

		fetchPrimaryObject();
		curComparedResult = getCompartorResult();

		if (joinType != IJointDataSetDesign.INNER_JOIN) {
			return resultObject;
		} else {
			this.shouldContinueSeek = true;
			return null;
		}

	}

	/**
	 * Return the IResultObject instance of a data set when curComparedResult is
	 * greater than 0;
	 * 
	 * @return
	 * @throws DataException
	 */
	private IResultObject greaterNext() throws DataException {
		if (curPrimaryMatchValues == null && joinType != IJointDataSetDesign.FULL_OUTER_JOIN) {
			return null;
		}

		if (joinType == IJointDataSetDesign.FULL_OUTER_JOIN && curSecondaryResultObjects.next() && !beSecondaryUsed) {
			return createResultObject(null, curSecondaryResultObjects.getCurrentResult());
		}

		fetchSecondaryObjects();
		curComparedResult = getCompartorResult();

		this.shouldContinueSeek = true;
		return null;
	}

	/**
	 * Compare primary object and secondary object and return result;
	 * 
	 * @return
	 * @throws DataException
	 */
	private int getCompartorResult() throws DataException {
		if (curPrimaryMatchValues == null && curSecondaryMatchValues == null) {
			return -1;
		}
		if (curPrimaryMatchValues != null && curSecondaryMatchValues == null) {
			return -1;
		}
		if (curPrimaryMatchValues == null && curSecondaryMatchValues != null) {
			return 1;
		}
		return jcm.compare(curPrimaryMatchValues, curSecondaryMatchValues);
	}

	/**
	 * Fetch a primary object.
	 * 
	 * @return
	 * @throws DataException
	 */
	private void fetchPrimaryObject() throws DataException {
		if (primaryIterator.getCurrentResult() == null) {
			curPrimaryResultObject = null;
			curPrimaryMatchValues = null;
		} else {

			curPrimaryResultObject = primaryIterator.getCurrentResult();
			curPrimaryMatchValues = jcm.getCompareValue(isPrimaryLeft());

			if (curSecondaryResultObjects != null)
				curSecondaryResultObjects.reset();
			primaryIterator.next();
		}
	}

	/**
	 * Fetch sequence and equal secondary objects
	 * 
	 * @return
	 * @throws DataException
	 */
	private void fetchSecondaryObjects() throws DataException {
		clearSecondaryObjects();

		if (secondaryIterator.getCurrentResult() == null)
			return;

		curSecondaryMatchValues = jcm.getCompareValue(!isPrimaryLeft());

		MatchResultSet resultSet = new MatchResultSet(secondaryIterator, jcm, !isPrimaryLeft());

		curSecondaryResultObjects = new SmartCache(new CacheRequest(0, new ArrayList(), null, new DummyEventHandler()),
				new OdiAdapter(resultSet), secondaryIterator.getResultClass(), this.session);

		beSecondaryUsed = false;
	}

	/**
	 * @throws DataException
	 * 
	 * 
	 */
	private void clearSecondaryObjects() throws DataException {
		if (curSecondaryResultObjects != null)
			curSecondaryResultObjects.close();
		curSecondaryResultObjects = null;
		curSecondaryMatchValues = null;
	}

	/**
	 * Create an instance of IResultObject.
	 * 
	 * @param primary
	 * @param secondary
	 * @return
	 * @throws DataException
	 */
	private IResultObject createResultObject(IResultObject primary, IResultObject secondary) throws DataException {
		Object[] fields = new Object[meta.getResultClass().getFieldCount()];
		for (int i = 1; i <= fields.length; i++) {
			IResultObject ri = null;

			if (meta.getColumnSource(i) == JointResultMetadata.COLUMN_TYPE_LEFT) {
				if (isPrimaryLeft())
					ri = primary;
				else
					ri = secondary;
			} else if (meta.getColumnSource(i) == JointResultMetadata.COLUMN_TYPE_RIGHT) {
				if (isPrimaryLeft())
					ri = secondary;
				else
					ri = primary;
			}

			fields[i - 1] = ri == null ? null : ri.getFieldValue(meta.getSourceIndex(i));
		}
		return new ResultObject(meta.getResultClass(), fields);
	}
}
