
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 * 
 */

public class CubeDimensionReader implements ICubeDimensionReader {
	private DimensionResultIterator[] dimResultSet;
	private Cube cube;

	public CubeDimensionReader(Cube cube) {
		this.cube = cube;
		this.dimResultSet = new DimensionResultIterator[cube.getDimesions().length];
	}

	private void populateDimensionResultIterator(int dimIndex) throws DataException, IOException {
		IDimension[] dimensions = cube.getDimesions();

		dimResultSet[dimIndex] = new DimensionResultIterator((Dimension) dimensions[dimIndex],
				dimensions[dimIndex].findAll(), new StopSign());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getLevelMember(int, int, int)
	 */
	public Member getLevelMember(int dimIndex, int levelIndex, int dimPos) throws IOException, DataException {
		if (dimResultSet[dimIndex] == null) {
			populateDimensionResultIterator(dimIndex);
		}
		dimResultSet[dimIndex].seek(dimPos);
		return dimResultSet[dimIndex].getLevelMember(levelIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getDimension(java.lang.String)
	 */
	public IDimension getDimension(String dimensionName) {
		IDimension[] dimensions = cube.getDimesions();
		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i].getName().equals(dimensionName))
				return dimensions[i];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getLevelMembers(int, int, int)
	 */
	public Member[] getLevelMembers(int dimIndex, int endLevelIndex, int dimPos) throws IOException, DataException {
		if (dimResultSet[dimIndex] == null) {
			populateDimensionResultIterator(dimIndex);
		}
		dimResultSet[dimIndex].seek(dimPos);
		Member[] members = dimResultSet[dimIndex].getDimensionRow().getMembers();
		Member[] resultMembers = new Member[endLevelIndex + 1];
		System.arraycopy(members, 0, resultMembers, 0, endLevelIndex + 1);
		return resultMembers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getDimensionIndex(java.lang.String)
	 */
	public int getDimensionIndex(String dimensionName) {
		IDimension[] dimensions = cube.getDimesions();
		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i].getName().equals(dimensionName))
				return i;
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getLevelIndex(java.lang.String, java.lang.String)
	 */
	public int getLevelIndex(String dimensionName, String levelName) {
		IDimension dimension = getDimension(dimensionName);
		if (dimension == null)
			return -1;
		ILevel[] levels = dimension.getHierarchy().getLevels();
		for (int i = 0; i < levels.length; i++) {
			if (levelName.equals(levels[i].getName())) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.ICubeDimensionReader#
	 * getlowestLevelIndex(java.lang.String)
	 */
	public int getlowestLevelIndex(String dimensionName) {
		return getDimension(dimensionName).getHierarchy().getLevels().length - 1;
	}

}
