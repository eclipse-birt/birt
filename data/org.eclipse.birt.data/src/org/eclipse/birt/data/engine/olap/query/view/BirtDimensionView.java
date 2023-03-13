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
		if (hierarchyList == null) {
			return;
		}
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
