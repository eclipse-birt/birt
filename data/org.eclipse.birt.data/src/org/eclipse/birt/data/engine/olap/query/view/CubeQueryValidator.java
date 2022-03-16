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

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;

/**
 * validate cube query definition with edge, measure definition.
 *
 */
class CubeQueryValidator {

	private CubeQueryValidator() {
	}

	/**
	 *
	 * @param view
	 * @param cube
	 * @param calculatedMember
	 * @throws DataException
	 */
	static void validateCubeQueryDefinition(BirtCubeView view, ICube cube) throws DataException {
		// for presentation mode, cube might be null.
		if (cube == null) {
			return;
		}
		if (view.getColumnEdgeView() == null && view.getRowEdgeView() == null) {
			// A cube query definition can have no edge if it only contains grand total
			// bindings
			// throw new DataException( ResourceConstants.NO_EDGEDEFN_FOUND );
		}
		if (view.getColumnEdgeView() != null) {
			validateOnEdgeDefinition(cube, view.getColumnEdgeView());
		}
		if (view.getRowEdgeView() != null) {
			validateOnEdgeDefinition(cube, view.getRowEdgeView());
		}
	}

	/**
	 * validate on edge definition
	 *
	 * @param cube
	 * @param edgeView
	 * @throws DataException
	 */
	static void validateOnEdgeDefinition(ICube cube, BirtEdgeView edgeView) throws DataException {
		for (int i = 0; i < edgeView.getDimensionViews().size(); i++) {
			BirtDimensionView birtDimensionView = (BirtDimensionView) edgeView.getDimensionViews().get(i);
			for (int j = 0; j < birtDimensionView.getMemberSelection().size(); j++) {
				ILevelDefinition levelDefinition = (ILevelDefinition) birtDimensionView.getMemberSelection().get(j);
				String dimensionName = levelDefinition.getHierarchy().getDimension().getName();
				String levelName = levelDefinition.getName();
				String hierarchyName = levelDefinition.getHierarchy().getName();
				if (!validateWithRawCube(cube, levelName, dimensionName, hierarchyName)) {
					throw new DataException(ResourceConstants.CANNOT_FIND_LEVEL,
							new Object[] { levelName, dimensionName });
				}
			}
		}
	}

	/**
	 *
	 * @param cube
	 * @param dimensionName
	 * @param levelName
	 * @param hierarchyName
	 * @return
	 */
	static boolean validateWithRawCube(ICube cube, String levelName, String dimensionName, String hierarchyName) {
		boolean validate = false;
		for (int k = 0; k < cube.getDimesions().length; k++) {
			if (cube.getDimesions()[k].isTime()) {
				return true;
			}
			if (dimensionName.equals(cube.getDimesions()[k].getName())) {
				if (hierarchyName.equals(cube.getDimesions()[k].getHierarchy().getName())) {
					for (int t = 0; t < cube.getDimesions()[k].getHierarchy().getLevels().length; t++) {
						if (levelName.equals(cube.getDimesions()[k].getHierarchy().getLevels()[t].getName())) {
							validate = true;
							return validate;
						}
					}
				}
			}
		}
		return validate;
	}
}
