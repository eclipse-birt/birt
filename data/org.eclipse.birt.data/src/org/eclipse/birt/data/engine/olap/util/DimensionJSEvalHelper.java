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

package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;

/**
 * 
 */

public class DimensionJSEvalHelper extends BaseJSEvalHelper {

	protected String dimName;

	protected void registerJSObjectPopulators() throws DataException {
		this.dimName = OlapExpressionUtil.getReferencedDimensionName(this.expr, queryDefn.getBindings());
		if (dimName != null) {
			register(new DimensionJSObjectPopulator(scope, dimName, getTargetDimensionLevelNames()));
		}
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	protected List getTargetDimensionLevelNames() throws DataException {
		IDimensionDefinition dimDefn = getTargetDimension();
		if (dimDefn == null) {
			return new ArrayList();
		}
		List result = new ArrayList();
		List levels = ((IHierarchyDefinition) dimDefn.getHierarchy().get(0)).getLevels();
		for (int j = 0; j < levels.size(); j++) {
			ILevelDefinition level = (ILevelDefinition) levels.get(j);
			result.add(level.getName());
		}
		return result;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	protected IDimensionDefinition getTargetDimension() throws DataException {
		IEdgeDefinition columnEdge = this.queryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		IEdgeDefinition rowEdge = this.queryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE);
		List dims = new ArrayList();
		if (columnEdge != null)
			dims.addAll(columnEdge.getDimensions());
		if (rowEdge != null)
			dims.addAll(rowEdge.getDimensions());

		for (int i = 0; i < dims.size(); i++) {
			IDimensionDefinition dimDefn = (IDimensionDefinition) dims.get(i);
			if (dimDefn.getName().equals(dimName)) {
				return dimDefn;
			}
		}
		return null;
	}

}
