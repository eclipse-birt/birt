/**
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;

public class DrillOnDimensionHierarchy {

	private Map<DimLevel, List<IEdgeDrillFilter>> drillOnLevelsMap = new HashMap<DimLevel, List<IEdgeDrillFilter>>();
	private IEdgeDrillFilter[] drill;
	private List<List<IEdgeDrillFilter>> sortedDrillFilter = new ArrayList<List<IEdgeDrillFilter>>();

	public DrillOnDimensionHierarchy(IDimensionDefinition dimension, IEdgeDrillFilter[] drill) {
		if (dimension == null || drill.length == 0)
			return;

		this.drill = drill;
		for (int i = 0; i < dimension.getHierarchy().get(0).getLevels().size(); i++) {
			ILevelDefinition level = dimension.getHierarchy().get(0).getLevels().get(i);
			List<IEdgeDrillFilter> drillList = new ArrayList<IEdgeDrillFilter>();
			for (int j = 0; j < drill.length; j++) {
				if (drill[j].getTargetLevelName().equals(level.getName())) {
					drillList.add(drill[j]);
				}
			}
			drillOnLevelsMap.put(new DimLevel(dimension.getName(), level.getName()), drillList);
			sortedDrillFilter.add(drillList);
		}
	}

	public List<IEdgeDrillFilter> getDrillFilterByLevel(DimLevel dimLevel) {
		return drillOnLevelsMap.get(dimLevel);
	}

	public Iterator<List<IEdgeDrillFilter>> getDrillFilterIterator() {
		return sortedDrillFilter.iterator();
	}

	public IEdgeDrillFilter[] getDrillByDimension() {
		return this.drill;
	}

	public boolean contains(IEdgeDrillFilter targetDrill) {
		for (int i = 0; i < this.drill.length; i++) {
			if (this.drill[i].equals(targetDrill)) {
				return true;
			}
		}
		return false;
	}
}
