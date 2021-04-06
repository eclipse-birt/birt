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
package org.eclipse.birt.data.engine.olap.query.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;

/**
 * A BirtDimensionView is a class which represents a view of a level. This view
 * represents the selected dimension's level. It's associated with BirtCubeView
 * by BirtEdgeView.
 * 
 */
public class BirtDimensionView {

	private List levelDefinitionList;

	/**
	 * 
	 * @param defn
	 */
	public BirtDimensionView(IDimensionDefinition defn) {
		levelDefinitionList = new ArrayList();
		List hierarchyList = defn.getHierarchy();
		if (hierarchyList == null)
			return;
		IHierarchyDefinition hierarchy;
		for (int i = 0; i < hierarchyList.size(); i++) {
			hierarchy = (IHierarchyDefinition) (hierarchyList.get(i));
			levelDefinitionList.addAll(hierarchy.getLevels());
		}
	}

	/**
	 * 
	 * @return
	 */
	public List getMemberSelection() {
		return levelDefinitionList;
	}
}
