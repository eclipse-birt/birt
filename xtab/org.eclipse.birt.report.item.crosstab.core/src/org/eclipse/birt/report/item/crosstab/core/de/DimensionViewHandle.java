/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.de;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IDimensionViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.internal.DimensionViewTask;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * DimensionViewHandle.
 */
public class DimensionViewHandle extends AbstractCrosstabItemHandle
		implements IDimensionViewConstants, ICrosstabConstants {

	/**
	 * 
	 * @param handle
	 */
	DimensionViewHandle(DesignElementHandle handle) {
		super(handle);
	}

	/**
	 * Gets the levels property of this dimension view.
	 * 
	 * @return levels property handle of this dimension view
	 */

	public PropertyHandle getLevelsProperty() {
		return handle.getPropertyHandle(LEVELS_PROP);
	}

	/**
	 * Gets the referred OLAP dimension handle.
	 * 
	 * @return the referrred OLAP dimension handle
	 */
	public DimensionHandle getCubeDimension() {
		DimensionHandle cubeDimension = (DimensionHandle) handle.getElementProperty(DIMENSION_PROP);
		if (cubeDimension == null) {
			String dimensionName = this.getCubeDimensionName();
			CrosstabReportItemHandle crosstab = this.getCrosstab();
			CubeHandle cube = (crosstab != null) ? crosstab.getCube() : null;
			if (dimensionName != null && cube != null) {
				cubeDimension = cube.getDimension(dimensionName);
			}
		}

		return cubeDimension;
	}

	/**
	 * Gets the referred OLAP dimension qualified name.
	 * 
	 * @return the referrred OLAP dimension qualified name
	 */
	public String getCubeDimensionName() {
		return handle.getStringProperty(DIMENSION_PROP);
	}

	/**
	 * Gets the count of the level view handle.
	 * 
	 * @return count of the level view
	 */
	public int getLevelCount() {
		return getLevelsProperty().getContentCount();
	}

	/**
	 * Gets the level view handle that refers a cube level element with the given
	 * name.
	 * 
	 * @param name the qualified name of the cube level to find
	 * @return level view handle if found, otherwise null
	 */
	public LevelViewHandle getLevel(String name) {
		for (int i = 0; i < getLevelCount(); i++) {
			LevelViewHandle levelView = getLevel(i);
			if (levelView != null) {
				String cubeLevelName = levelView.getCubeLevelName();
				if ((cubeLevelName != null && cubeLevelName.equals(name)) || (cubeLevelName == null && name == null))
					return levelView;
			}
		}
		return null;
	}

	/**
	 * Gets the level view handle that refers a cube level element with the given
	 * name.
	 * 
	 * @param name the short name of the cube level to find
	 * @return level view handle if found, otherwise null
	 */
	public LevelViewHandle findLevel(String name) {
		for (int i = 0; i < getLevelCount(); i++) {
			LevelViewHandle levelView = getLevel(i);
			if (levelView != null) {
				LevelHandle handle = levelView.getCubeLevel();
				if (handle != null) {
					String cubeLevelName = handle.getName();
					if ((cubeLevelName != null && cubeLevelName.equals(name))
							|| (cubeLevelName == null && name == null)) {
						return levelView;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the level view with the given index. Position index is 0-based integer.
	 * 
	 * @param index a 0-based integer of the level position
	 * @return the level view handle if found, otherwise null
	 */
	public LevelViewHandle getLevel(int index) {
		DesignElementHandle element = getLevelsProperty().getContent(index);
		return (LevelViewHandle) CrosstabUtil.getReportItem(element, LEVEL_VIEW_EXTENSION_NAME);
	}

	/**
	 * Inserts a level view to the given position. The position index is a 0-based
	 * integer.
	 * 
	 * @param levelHandle the cube level handle to insert
	 * @param index       the target position, 0-based integer
	 * @return the level view handle that is generated and inserted to this
	 *         dimension view, null if OLAP level handle is used by another level
	 *         view or insert operation fails
	 * @throws SemanticException
	 */
	public LevelViewHandle insertLevel(LevelHandle levelHandle, int index) throws SemanticException {
		return new DimensionViewTask(this).insertLevel(levelHandle, index);
	}

	/**
	 * Removes a level view that refers a cube level element with the given name.
	 * 
	 * @param name name of the cube level element to remove
	 * @throws SemanticException
	 */
	public void removeLevel(String name) throws SemanticException {
		new DimensionViewTask(this).removeLevel(name);
	}

	/**
	 * Removes a level view at the given position. The position index is 0-based
	 * integer.
	 * 
	 * @param index the position index of the level view to remove
	 * @throws SemanticException
	 */
	public void removeLevel(int index) throws SemanticException {
		new DimensionViewTask(this).removeLevel(index);
	}

	/**
	 * Gets the position index of this dimension view in the crosstab row/column.
	 * 
	 * @return the position index of this dimension view in the crosstab row/column
	 *         if this dimension is in the design tree and return value is 0-based
	 *         integer, otherwise -1
	 */
	public int getIndex() {
		return handle.getIndex();
	}

	/**
	 * Gets the axis type of this dimension view in the crosstab. If this dimension
	 * lies in the design tree, the returned value is either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Otherwise return
	 * <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 * 
	 * @return the axis type if this dimension resides in design tree, otherwise -1;
	 */
	public int getAxisType() {
		CrosstabViewHandle crosstabView = (CrosstabViewHandle) CrosstabUtil.getReportItem(handle.getContainer(),
				CROSSTAB_VIEW_EXTENSION_NAME);
		return crosstabView == null ? NO_AXIS_TYPE : crosstabView.getAxisType();

	}

}
