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

package org.eclipse.birt.data.engine.olap.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jakarta.olap.OLAPException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 */

public class JSLevelAccessor extends ScriptableObject {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private ICubeQueryDefinition cubeQueryDefn;
	private Map dims;

	public JSLevelAccessor(ICubeQueryDefinition defn, BirtCubeView cursor) throws OLAPException {
		this.dims = new HashMap();
		this.cubeQueryDefn = defn;

		if (cursor.getPageEdgeView() != null && this.cubeQueryDefn.getEdge(ICubeQueryDefinition.PAGE_EDGE) != null) {
			populateDimensionObjects(this.cubeQueryDefn.getEdge(ICubeQueryDefinition.PAGE_EDGE).getDimensions(),
					cursor.getPageEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}

		/*
		 * Populate Row Edge dimension objects.
		 */
		if (cursor.getRowEdgeView() != null && this.cubeQueryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE) != null) {
			populateDimensionObjects(this.cubeQueryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE).getDimensions(),
					cursor.getRowEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}

		/*
		 * Populate Column Edge dimension objects.
		 */
		if (cursor.getColumnEdgeView() != null
				&& this.cubeQueryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE) != null) {
			populateDimensionObjects(this.cubeQueryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE).getDimensions(),
					cursor.getColumnEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}

	}

	/**
	 *
	 * @param dimList
	 * @param cursors
	 * @throws OLAPException
	 */
	private void populateDimensionObjects(List dimList, Iterator cursors) throws OLAPException {

		for (int i = 0; i < dimList.size(); i++) {
			IDimensionDefinition dimDefn = (IDimensionDefinition) dimList.get(i);
			IHierarchyDefinition hier = (IHierarchyDefinition) dimDefn.getHierarchy().get(0);
			List levelNames = new ArrayList();
			List dimCursors = new ArrayList();
			for (int j = 0; j < hier.getLevels().size(); j++) {
				levelNames.add(((ILevelDefinition) hier.getLevels().get(j)).getName());
				dimCursors.add(cursors.next());
			}
			this.dims.put(dimDefn.getName(), new JSDimensionObject(levelNames, dimCursors));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	@Override
	public String getClassName() {
		return "JSDimensionObject";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		if (!this.dims.containsKey(name)) {
			throw new RuntimeException(new DataException(ResourceConstants.DIMENSION_NAME_NOT_FOUND, name));
		}
		return this.dims.get(name);
	}
}
