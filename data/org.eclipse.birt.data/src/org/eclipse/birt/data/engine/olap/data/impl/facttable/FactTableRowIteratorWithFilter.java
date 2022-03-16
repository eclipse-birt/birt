
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionResultIterator;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;

/**
 *
 */

public class FactTableRowIteratorWithFilter implements IFactTableRowIterator {
	private DimensionResultIterator[] dimesionResultIterators;
	private IFactTableRowIterator facttableRowIterator;
	private int[] currentPos = null;
	private Object[] currentMeasures;
	private IDimension[] dimensions;

	public FactTableRowIteratorWithFilter(IDimension[] dimensions, IFactTableRowIterator facttableRowIterator,
			StopSign stopSign) throws IOException {
		this.dimensions = dimensions;
		this.dimesionResultIterators = getDimesionResultIterators(stopSign);
		this.facttableRowIterator = facttableRowIterator;
		this.currentPos = new int[dimesionResultIterators.length];
		this.currentMeasures = new Object[facttableRowIterator.getMeasureCount()];
	}

	/**
	 *
	 * @param lCube
	 * @return
	 * @throws IOException
	 */
	private DimensionResultIterator[] getDimesionResultIterators(StopSign stopSign) throws IOException {
		DimensionResultIterator[] dimesionResultIterators = new DimensionResultIterator[dimensions.length];
		for (int i = 0; i < dimensions.length; i++) {
			dimesionResultIterators[i] = new DimensionResultIterator((Dimension) dimensions[i], dimensions[i].findAll(),
					stopSign);
		}
		return dimesionResultIterators;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionCount()
	 */
	@Override
	public int getDimensionCount() {
		return dimensions.length;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionIndex(java.lang.String)
	 */
	@Override
	public int getDimensionIndex(String dimensionName) {
		for (int i = 0; i < dimensions.length; i++) {
			if (dimensions[i].getName().equals(dimensionName)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionPosition(int)
	 */
	@Override
	public int getDimensionPosition(int dimensionIndex) {
		return currentPos[dimensionIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getDimensionPosition()
	 */
	@Override
	public int[] getDimensionPosition() {
		return currentPos;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasure(int)
	 */
	@Override
	public Object getMeasure(int measureIndex) {
		return currentMeasures[measureIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureCount()
	 */
	@Override
	public int getMeasureCount() {
		return currentMeasures.length;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureIndex(java.lang.String)
	 */
	@Override
	public int getMeasureIndex(String measureName) {
		return facttableRowIterator.getMeasureIndex(measureName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * next()
	 */
	@Override
	public boolean next() throws IOException, DataException {
		boolean hasNext = facttableRowIterator.next();
		while (hasNext) {
			for (int i = 0; i < currentPos.length; i++) {
				currentPos[i] = facttableRowIterator.getDimensionPosition(i);
			}
			for (int i = 0; i < currentMeasures.length; i++) {
				currentMeasures[i] = facttableRowIterator.getMeasure(i);
			}
			if (filter()) {
				break;
			}
			hasNext = facttableRowIterator.next();
		}
		return hasNext;
	}

	private boolean filter() throws IOException {
		DimensionRow[] dimRows = new DimensionRow[currentPos.length];
		for (int i = 0; i < currentPos.length; i++) {
			dimesionResultIterators[i].seek(currentPos[i]);
			dimRows[i] = dimesionResultIterators[i].getDimensionRow();
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * getMeasureInfo()
	 */
	@Override
	public MeasureInfo[] getMeasureInfos() {
		return facttableRowIterator.getMeasureInfos();
	}

	@Override
	public void close() throws DataException, IOException {
		facttableRowIterator.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator#
	 * isDuplicatedRow()
	 */
	@Override
	public boolean isDuplicatedRow() {
		return false;
	}

}
