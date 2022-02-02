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

import java.util.List;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * An implementation of IJoinConditionMatcher.
 */
public class JoinConditionMatcher implements IJoinConditionMatcher {
	//
	private JoinConditionMatchUnit[] left = new JoinConditionMatchUnit[0];
	private JoinConditionMatchUnit[] right = new JoinConditionMatchUnit[0];

	/**
	 * Constructor.
	 * 
	 * @param leftRi
	 * @param rightRi
	 * @param leftScope
	 * @param rightScope
	 * @param joinConditions
	 */
	public JoinConditionMatcher(IResultIterator leftRi, IResultIterator rightRi, Scriptable leftScope,
			Scriptable rightScope, ScriptContext cx, List joinConditions) {
		this.left = new JoinConditionMatchUnit[joinConditions.size()];
		this.right = new JoinConditionMatchUnit[joinConditions.size()];

		for (int i = 0; i < joinConditions.size(); i++) {
			populateJoinUnit(((IJoinCondition) joinConditions.get(i)).getLeftExpression(), cx, i, this.left, leftRi,
					leftScope);
			populateJoinUnit(((IJoinCondition) joinConditions.get(i)).getRightExpression(), cx, i, this.right, rightRi,
					rightScope);
		}

	}

	/**
	 * @param joinConditions
	 * @param cx
	 * @param i
	 */
	private void populateJoinUnit(IScriptExpression expr, ScriptContext cx, int i, JoinConditionMatchUnit[] toArray,
			IResultIterator ri, Scriptable scope) {
		toArray[i] = new JoinConditionMatchUnit(expr, scope, cx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.jointdataset.IJoinConditionMatcher#match()
	 */
	public boolean match() throws DataException {
		for (int i = 0; i < left.length; i++) {
			Object leftValue = left[i].getColumnValue();
			Object rightValue = right[i].getColumnValue();

			if (JointDataSetUtil.compare(leftValue, rightValue) != 0)
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.jointdataset.IJoinConditionMatcher#compare(
	 * java.lang.Object[], java.lang.Object[])
	 */
	public int compare(Object[] lObjects, Object[] rObjects) throws DataException {
		int result = 0;
		for (int i = 0; i < lObjects.length; i++) {
			result = JointDataSetUtil.compare(lObjects[i], rObjects[i]);
			if (result != 0)
				return result;
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.jointdataset.IJoinConditionMatcher#
	 * getCompareValue(boolean)
	 */
	public Object[] getCompareValue(boolean isLeft) throws DataException {
		JoinConditionMatchUnit[] array = null;
		if (isLeft)
			array = left;
		else
			array = right;

		Object[] result = new Object[array.length];
		for (int i = 0; i < result.length; i++) {
			result[i] = array[i].getColumnValue();
		}
		return result;
	}

}
