/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;
import org.eclipse.birt.data.engine.olap.cursor.MirrorMetaInfo;
import org.eclipse.birt.data.engine.olap.cursor.MirroredAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.SortedAggregationRowArray;

/**
 * Execute mirror operation on aggregate result set
 *
 */
public class MirrorOperationExecutor {

	public IAggregationResultSet[] execute(IAggregationResultSet[] rs, BirtCubeView view,
			CubeQueryExecutorHelper cubeQueryExecutorHelper) throws IOException, DataException {
		ICubeQueryDefinition query = view.getCubeQueryDefinition();
		IEdgeDefinition columnEdge = query.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = query.getEdge(ICubeQueryDefinition.ROW_EDGE);
		IMirroredDefinition columnMirror = null, rowMirror = null;
		if (columnEdge != null)
			columnMirror = columnEdge.getMirroredDefinition();
		if (rowEdge != null)
			rowMirror = rowEdge.getMirroredDefinition();

		int index = 0;
		if (columnEdge != null) {
			if (columnMirror != null) {
				rs[index] = new MirroredAggregationResultSet(rs[index],
						new MirrorMetaInfo(columnMirror, columnEdge, view), cubeQueryExecutorHelper.getColumnSort());
			}
			index++;
		}

		if (rowMirror != null) {
			rs[index] = new MirroredAggregationResultSet(rs[index], new MirrorMetaInfo(rowMirror, rowEdge, view),
					cubeQueryExecutorHelper.getRowSort());
		}

		return rs;
	}

	private IAggregationResultSet sortAggregationResultSet(IAggregationResultSet rs) throws IOException {
		SortedAggregationRowArray sarr = new SortedAggregationRowArray(rs);
		rs.close();
		return new AggregationResultSet(rs.getAggregationDefinition(), rs.getAllLevels(), sarr.getSortedRows(),
				rs.getKeyNames(), rs.getAttributeNames());
	}
}
