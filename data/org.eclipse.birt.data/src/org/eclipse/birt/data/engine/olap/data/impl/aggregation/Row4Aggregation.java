
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.IStructure;
import org.eclipse.birt.data.engine.olap.data.util.IStructureCreator;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;

/**
 * 
 */

public class Row4Aggregation implements IStructure {
	private Member[] levelMembers;
	private Object[] measures;
	private List<Object[]> measureList = new ArrayList<Object[]>();
	private int pos = -1;
	private Object[] parameterValues;
	private int[] dimPos;
	private Object[] firstMeasures;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.util.IStructure#getFieldValues()
	 */
	public Object[] getFieldValues() {
		Integer[] memberSize = new Integer[1];
		memberSize[0] = Integer.valueOf(getLevelMembers().length);
		Object[][] objectArrays = new Object[getLevelMembers().length + 1 + 3 + measureList.size()][];
		objectArrays[0] = memberSize;
		for (int i = 0; i < getLevelMembers().length; i++) {
			objectArrays[i + 1] = getLevelMembers()[i].getFieldValues();
		}

		objectArrays[getLevelMembers().length + 1] = parameterValues;
		Integer[] dimPosObj = null;
		if (dimPos == null) {
			dimPosObj = new Integer[1];
			dimPosObj[0] = Integer.valueOf(0);
		} else {
			dimPosObj = new Integer[dimPos.length + 1];
			dimPosObj[0] = Integer.valueOf(1);
			for (int i = 0; i < dimPos.length; i++) {
				dimPosObj[i + 1] = Integer.valueOf(dimPos[i]);
			}
		}
		objectArrays[getLevelMembers().length + 2] = dimPosObj;

		objectArrays[getLevelMembers().length + 3] = measures;
		for (int i = 0; i < measureList.size(); i++) {
			objectArrays[getLevelMembers().length + i + 4] = measureList.get(i);
		}
		return ObjectArrayUtil.convert(objectArrays);
	}

	/*
	 * 
	 */
	public static IStructureCreator getCreator() {
		return new Row4AggregationCreator();
	}

	public int[] getDimPos() {
		return dimPos;
	}

	public void setDimPos(int[] dimPos) {
		this.dimPos = dimPos;
	}

	public void setLevelMembers(Member[] levelMembers) {
		this.levelMembers = levelMembers;
	}

	public Member[] getLevelMembers() {
		return levelMembers;
	}

	public void setMeasures(Object[] measures) {
		this.measures = measures;
	}

	public void setMeasureList(List<Object[]> measureList) {
		this.measureList = measureList;
	}

	public List<Object[]> getMeasureList() {
		return this.measureList;
	}

	public void addMeasure(Object[] measures) {
		this.measureList.add(measures);
	}

	public void clearMeasure() {
		this.measureList.clear();
	}

	public Object[] getMeasures() {
		return measures;
	}

	public boolean nextMeasures() {
		if (pos == -1) {
			firstMeasures = measures;
			pos = 0;
			return true;
		} else {
			if (pos < this.measureList.size()) {
				measures = this.measureList.get(pos);
				pos++;
				return true;
			} else {
				return false;
			}
		}
	}

	public void firstMeasure() {
		measures = firstMeasures;
		pos = -1;
	}

	public void resetPosition() {
		pos = -1;
	}

	public Object[] getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(Object[] parameterValues) {
		this.parameterValues = parameterValues;
	}
}

/**
 * 
 * @author Administrator
 *
 */
class Row4AggregationCreator implements IStructureCreator {
	private static IStructureCreator levelMemberCreator = Member.getCreator();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.olap.data.util.IStructureCreator#createInstance(java.
	 * lang.Object[])
	 */
	public IStructure createInstance(Object[] fields) {
		Object[][] objectArrays = ObjectArrayUtil.convert(fields);
		Row4Aggregation result = new Row4Aggregation();
		int memberSize = (Integer) objectArrays[0][0];
		result.setLevelMembers(new Member[memberSize]);
		for (int i = 0; i < result.getLevelMembers().length; i++) {
			result.getLevelMembers()[i] = (Member) levelMemberCreator.createInstance(objectArrays[i + 1]);
		}
		result.setParameterValues(objectArrays[memberSize + 1]);
		if (objectArrays[memberSize + 2][0].equals(Integer.valueOf(1))) {
			int[] dimPos = new int[objectArrays[memberSize + 2].length - 1];
			for (int i = 0; i < dimPos.length; i++) {
				dimPos[i] = ((Integer) (objectArrays[memberSize + 2][i + 1])).intValue();
			}
			result.setDimPos(dimPos);
		}

		result.setMeasures(objectArrays[memberSize + 3]);
		for (int i = 0; i < (objectArrays.length - memberSize - 1 - 3); i++) {
			result.addMeasure(objectArrays[memberSize + 3 + i + 1]);
		}

		return result;
	}
}
