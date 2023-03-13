/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

/**
 * The data provider that want to use
 * <code>org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationExecutor</code>
 * to calculate aggregation
 */
public interface IDataSet4Aggregation {
	// Meta info
	MetaInfo getMetaInfo();

	// Move to next row
	boolean next() throws DataException, IOException;

	void close() throws DataException, IOException;

	// current row values
	Object getMeasureValue(int measureIndex) throws DataException, IOException;

	Member getMember(int dimIndex, int levelIndex) throws DataException, IOException;

	boolean isDuplicatedRow();

	int[] getDimensionPosition();

	public interface MetaInfo {
		MeasureInfo[] getMeasureInfos() throws IOException;

		int getMeasureIndex(String measureName) throws IOException;

		int getDimensionIndex(String dimensionName);

		int getLevelIndex(String dimensionName, String levelName);

		ColumnInfo getColumnInfo(DimColumn dimColumn) throws DataException;

		String[] getKeyNames(int dimIndex, int levelIndex);

		String[] getAttributeNames(int dimIndex, int levelIndex);
	}

}
