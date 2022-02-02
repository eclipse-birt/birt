
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * Describes a aggregation result row.
 */

public class AggregationResultRow implements IAggregationResultRow {
	private Member[] levelMembers = null;
	private Object[] aggregationValues = null;

	public AggregationResultRow() {

	}

	public AggregationResultRow(Member[] levelMembers, Object[] aggregationValues) {
		this.levelMembers = levelMembers;
		this.aggregationValues = aggregationValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues() {
		int memberLength = 0;
		if (levelMembers != null)
			memberLength = levelMembers.length;

		Object[][] objectArrays = new Object[memberLength + 1][];
		for (int i = 0; i < memberLength; i++) {
			if (levelMembers[i] == null)
				objectArrays[i] = null;
			else
				objectArrays[i] = levelMembers[i].getFieldValues();
		}
		if (getAggregationValues() != null) {
			objectArrays[objectArrays.length - 1] = new Object[getAggregationValues().length + 1];
			objectArrays[objectArrays.length - 1][0] = Integer.valueOf(1);
			System.arraycopy(getAggregationValues(), 0, objectArrays[objectArrays.length - 1], 1,
					getAggregationValues().length);
		} else {
			objectArrays[objectArrays.length - 1] = new Object[1];
			objectArrays[objectArrays.length - 1][0] = 0;
		}
		return ObjectArrayUtil.convert(objectArrays);
	}

	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator() {
		return new AggregationResultObjectCreator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		IAggregationResultRow other = (IAggregationResultRow) o;
		for (int i = 0; i < getLevelMembers().length; i++) {
			// only for drill operation, the member key value will be null
			if (getLevelMembers()[i] == null || other.getLevelMembers()[i] == null) {
				continue;
			}
			int result = (getLevelMembers()[i]).compareTo(other.getLevelMembers()[i]);
			if (result < 0) {
				return result;
			} else if (result > 0) {
				return result;
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IAggregationResultRow
	 * #setLevelMembers(org.eclipse.birt.data.engine.olap.data.impl.dimension.Member
	 * [])
	 */
	public void setLevelMembers(Member[] levelMembers) {
		this.levelMembers = levelMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IAggregationResultRow
	 * #getLevelMembers()
	 */
	public Member[] getLevelMembers() {
		return levelMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IAggregationResultRow
	 * #setAggregationValues(java.lang.Object[])
	 */
	public void setAggregationValues(Object[] aggregationValues) {
		this.aggregationValues = aggregationValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IAggregationResultRow
	 * #getAggregationValues()
	 */
	public Object[] getAggregationValues() {
		return aggregationValues;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class AggregationResultObjectCreator implements IStructureCreator {
	private static IStructureCreator levelMemberCreator = Member.getCreator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.
	 * lang.Object[])
	 */
	public IStructure createInstance(Object[] fields) {
		AggregationResultRow result = new AggregationResultRow();
		Object[][] objectArrays = ObjectArrayUtil.convert(fields);

		result.setLevelMembers(new Member[objectArrays.length - 1]);
		for (int i = 0; i < result.getLevelMembers().length; i++) {
			if (objectArrays[i] == null)
				result.getLevelMembers()[i] = null;
			else
				result.getLevelMembers()[i] = (Member) levelMemberCreator.createInstance(objectArrays[i]);
		}
		if (objectArrays[objectArrays.length - 1][0].equals(Integer.valueOf(1))) {
			result.setAggregationValues(new Object[objectArrays[objectArrays.length - 1].length - 1]);
			System.arraycopy(objectArrays[objectArrays.length - 1], 1, result.getAggregationValues(), 0,
					result.getAggregationValues().length);
		}

		return result;
	}
}
