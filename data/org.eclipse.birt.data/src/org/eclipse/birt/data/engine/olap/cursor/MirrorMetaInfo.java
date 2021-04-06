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
package org.eclipse.birt.data.engine.olap.cursor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;

public class MirrorMetaInfo {
	private IMirroredDefinition mirroDefinition;
	private IEdgeDefinition edgeDefn;
	private BirtCubeView view;
	private Map<DimLevel, String> levelTypes;

	public MirrorMetaInfo(IMirroredDefinition mirrorDefinition, IEdgeDefinition edgeDefn, BirtCubeView view) {
		this.mirroDefinition = mirrorDefinition;
		this.edgeDefn = edgeDefn;
		this.view = view;
		this.levelTypes = new HashMap<DimLevel, String>();
		Iterator dims = edgeDefn.getDimensions().iterator();
		while (dims.hasNext()) {
			IDimensionDefinition defn = (IDimensionDefinition) dims.next();
			populateTimeDimensionTypes(defn, view.getCube());
		}
	}

	public int getMirrorStartPosition() {
		int index = 0;
		int pageEndingPosition = -1;

		if (view.getPageEdgeView() != null) {
			pageEndingPosition = view.getPageEdgeView().getDimensionViews().size() - 1;
		}

		if (this.mirroDefinition != null) {
			ILevelDefinition[] levelArray = CubeQueryDefinitionUtil.getLevelsOnEdge(this.edgeDefn);
			for (int i = 0; i < levelArray.length; i++) {
				if (levelArray[i].equals(this.mirroDefinition.getMirrorStartingLevel())) {
					if (pageEndingPosition >= 0)
						index = i + pageEndingPosition + 1;
					else
						index = i;
					break;
				}
			}
			return index;
		} else
			return index;
	}

	public boolean isBreakHierarchy() {
		if (this.mirroDefinition != null) {
			return this.mirroDefinition.isBreakHierarchy();
		} else
			return false;
	}

	public DataEngineSession getSession() {
		return this.view.getCubeQueryExecutor().getSession();
	}

	public String getLevelType(DimLevel dimLevel) {
		return this.levelTypes.get(dimLevel);
	}

	private void populateTimeDimensionTypes(IDimensionDefinition defn, ICube cube) {
		if (cube != null)
			for (int i = 0; i < cube.getDimesions().length; i++) {
				IDimension dimension = cube.getDimesions()[i];
				if (dimension.getName().equals(defn.getName())
						&& dimension.getHierarchy().getName().equals(defn.getHierarchy().get(0).getName())) {
					List<ILevelDefinition> levelDefn = defn.getHierarchy().get(0).getLevels();
					ILevel[] levels = dimension.getHierarchy().getLevels();
					for (int j = 0; j < levelDefn.size(); j++) {
						for (int k = 0; k < levels.length; k++) {
							if (levelDefn.get(j).getName().equals(levels[k].getName())) {
								levelTypes.put(new DimLevel(defn.getName(), levelDefn.get(j).getName()),
										levels[k].getLeveType());
								break;
							}
						}
					}
					break;
				}
			}
	}
}
