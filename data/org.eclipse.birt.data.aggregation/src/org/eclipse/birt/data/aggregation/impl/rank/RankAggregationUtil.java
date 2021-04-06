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

package org.eclipse.birt.data.aggregation.impl.rank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.aggregation.i18n.ResourceConstants;
import org.eclipse.birt.data.aggregation.impl.AggrException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * The utility class use by this package.
 */
final class RankAggregationUtil {

	// The dummy object.
	private static DummyObject dummy = new DummyObject();
	private static NullObject nullObject = new NullObject();

	/**
	 * Return the next top index
	 * 
	 * @param cachedValues
	 * @return
	 * @throws DataException
	 */
	static int getNextTopIndex(List cachedValues) throws DataException {
		return getIndex(cachedValues, true);
	}

	/**
	 * Return next bottom index.
	 * 
	 * @param cachedValues
	 * @return
	 * @throws DataException
	 */
	static int getNextBottomIndex(List cachedValues) throws DataException {
		return getIndex(cachedValues, false);
	}

	/**
	 * 
	 * @param cachedValues
	 * @param top
	 * @return
	 * @throws DataException
	 */
	private static int getIndex(List cachedValues, boolean top) throws DataException {
		int result = -1;
		for (int i = 0; i < cachedValues.size(); i++) {
			if (cachedValues.get(i).getClass() == DummyObject.class)
				continue;

			if (result == -1) {
				result = i;
				continue;
			}

			if (top ? compareTop(cachedValues.get(result), cachedValues.get(i))
					: compareBottom(cachedValues.get(result), cachedValues.get(i))) {
				result = i;
			}
		}
		cachedValues.set(result, dummy);
		return result;
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 * @throws DataException
	 */
	private static boolean compareTop(Object value1, Object value2) throws DataException {
		if (value1.getClass() == DummyObject.class || value2.getClass() == DummyObject.class)
			return false;
		if (value1.getClass() == NullObject.class)
			return true;
		if (value2.getClass() == NullObject.class)
			return false;
		return Boolean.valueOf(
				ScriptEvalUtil.evalConditionalExpr(value1, IConditionalExpression.OP_LT, value2, null).toString())
				.booleanValue();
	}

	/**
	 * 
	 * @param value1
	 * @param value2
	 * @return
	 * @throws DataException
	 */
	private static boolean compareBottom(Object value1, Object value2) throws DataException {
		if (value1.getClass() == DummyObject.class || value2.getClass() == DummyObject.class)
			return false;
		if (value1.getClass() == NullObject.class)
			return false;
		if (value2.getClass() == NullObject.class)
			return true;
		return Boolean.valueOf(
				ScriptEvalUtil.evalConditionalExpr(value1, IConditionalExpression.OP_GT, value2, null).toString())
				.booleanValue();
	}

	/**
	 * 
	 * @param o
	 * @return
	 * @throws DataException
	 */
	static Double getNumericValue(Object o) throws DataException {
		try {
			if (o instanceof Date)
				return new Double(((Date) o).getTime());
			else
				return new Double(o.toString());
		} catch (NumberFormatException e) {
			throw DataException.wrap(new AggrException(ResourceConstants.DATATYPEUTIL_ERROR, e));
		}
	}

	/**
	 * 
	 * @return
	 */
	static NullObject getNullObject() {
		return nullObject;
	}

	/**
	 * 
	 * @param objs
	 * @throws DataException
	 */
	static void sortArray(Object[] objs) throws DataException {
		try {
			Arrays.sort(objs, new ValueComparator());
		} catch (DataComparisonException e) {
			throw e.getWrappedException();
		}
	}
}

/**
 * A comparator which is used to compare two objects.
 * 
 */
class ValueComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		if (o1 instanceof NullObject)
			return -1;
		else if (o2 instanceof NullObject)
			return 1;
		else {
			try {
				boolean gt = new Boolean(
						ScriptEvalUtil.evalConditionalExpr(o1, IConditionalExpression.OP_GT, o2, null).toString())
								.booleanValue();
				if (gt)
					return 1;
				boolean eq = Boolean.valueOf(
						ScriptEvalUtil.evalConditionalExpr(o1, IConditionalExpression.OP_EQ, o2, null).toString())
						.booleanValue();
				if (eq)
					return 0;
				else
					return -1;
			} catch (DataException e) {
				throw new DataComparisonException(e);
			}
		}
	}
}

/**
 * 
 * 
 */
class DataComparisonException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3658822923142229473L;

	private DataException wrappedException = null;

	DataComparisonException(DataException e) {
		this.wrappedException = e;
	}

	/**
	 * 
	 * @return
	 */
	public DataException getWrappedException() {
		return this.wrappedException;
	}
}