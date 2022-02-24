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

package org.eclipse.birt.report.item.crosstab.core.util;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.IDimensionViewConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Factory class to create some extended item handle for all crosstab item
 * types.
 */

public class CrosstabExtendedItemFactory implements ICrosstabConstants {

	/**
	 * Creates a crosstab extended item to use the given OLAP cube handle.
	 * 
	 * @param module the module handle for extended item lying in
	 * @param cube   the OLAP cube handle to apply
	 * @return the crosstab extended item handle
	 * @throws SemanticException if the given cube can not be set to a crosstab
	 */
	public static ExtendedItemHandle createCrosstabReportItem(ModuleHandle module, CubeHandle cube, String name)
			throws SemanticException {
		if (module == null)
			return null;

		ExtendedItemHandle extendedItem = module.getElementFactory().newExtendedItem(name, CROSSTAB_EXTENSION_NAME);

		if (extendedItem != null) {
			extendedItem.setExtensionVersion(CROSSTAB_CURRENT_VERSION);

			extendedItem.setProperty(IReportItemModel.CUBE_PROP, cube);

			// prepare header cell
			ExtendedItemHandle cellHandle = createCrosstabCell(module);
			if (cellHandle != null) {
				extendedItem.getPropertyHandle(ICrosstabReportItemConstants.HEADER_PROP).add(cellHandle);
			}
		}

		return extendedItem;
	}

	/**
	 * Creates a crosstab view extended item.
	 * 
	 * @param module
	 * @return
	 */
	public static ExtendedItemHandle createCrosstabView(ModuleHandle module) {
		if (module == null)
			return null;
		return module.getElementFactory().newExtendedItem(null, CROSSTAB_VIEW_EXTENSION_NAME);
	}

	/**
	 * Creates a dimension view extended item to use the given OLAP dimension
	 * handle.
	 * 
	 * @param module    the module handle for extended item lying in
	 * @param dimension the OLAP dimension handle to apply
	 * @return the dimension view extended item handle
	 * @throws SemanticException if the given OLAP dimension can not be set to a
	 *                           dimension view
	 */
	public static ExtendedItemHandle createDimensionView(ModuleHandle module, DimensionHandle dimension)
			throws SemanticException {
		if (module == null)
			return null;
		ExtendedItemHandle extendedItem = module.getElementFactory().newExtendedItem(null,
				DIMENSION_VIEW_EXTENSION_NAME);
		if (extendedItem != null) {
			extendedItem.setProperty(IDimensionViewConstants.DIMENSION_PROP, dimension);
		}
		return extendedItem;
	}

	/**
	 * Creates a measure view extended item to use the given OLAP measure handle.
	 * 
	 * @param module  the module handle for extended item lying in
	 * @param measure the OLAP measure handle to apply
	 * @return the measure view extended item handle
	 * @throws SemanticException if the given OLAP measure can not be set to a
	 *                           measure view
	 */
	public static ExtendedItemHandle createMeasureView(ModuleHandle module, MeasureHandle measure)
			throws SemanticException {
		if (module == null)
			return null;
		ExtendedItemHandle extendedItem = module.getElementFactory().newExtendedItem(null, MEASURE_VIEW_EXTENSION_NAME);

		if (extendedItem == null)
			return null;

		// set cube measure reference
		extendedItem.setProperty(IMeasureViewConstants.MEASURE_PROP, measure);
		// prepare detail cell
		ExtendedItemHandle cellHandle = createAggregationCell(module);
		if (cellHandle != null) {
			extendedItem.getPropertyHandle(IMeasureViewConstants.DETAIL_PROP).add(cellHandle);
		}

		return extendedItem;
	}

	public static ExtendedItemHandle createComputedMeasureView(ModuleHandle module, String measureViewName)
			throws SemanticException {
		if (module == null)
			return null;
		ExtendedItemHandle extendedItem = module.getElementFactory().newExtendedItem(measureViewName,
				COMPUTED_MEASURE_VIEW_EXTENSION_NAME);

		if (extendedItem == null)
			return null;

		// prepare detail cell
		ExtendedItemHandle cellHandle = createAggregationCell(module);
		if (cellHandle != null) {
			extendedItem.getPropertyHandle(IMeasureViewConstants.DETAIL_PROP).add(cellHandle);
		}

		return extendedItem;
	}

	/**
	 * Creates a level view extended item to use the given OLAP level handle.
	 * 
	 * @param module the module handle for extended item lying in
	 * @param level  the OLAP level handle to apply
	 * @return the level view extended item handle
	 * @throws SemanticException if the given OLAP level can not be set to a level
	 *                           view
	 */
	public static ExtendedItemHandle createLevelView(ModuleHandle module, LevelHandle level) throws SemanticException {
		if (module == null)
			return null;
		ExtendedItemHandle extendedItem = module.getElementFactory().newExtendedItem(null, LEVEL_VIEW_EXTENSION_NAME);

		if (extendedItem == null)
			return null;

		// set cube level reference
		extendedItem.setProperty(ILevelViewConstants.LEVEL_PROP, level);
		// prepare detail cell
		ExtendedItemHandle cellHandle = createCrosstabCell(module);
		if (cellHandle != null) {
			extendedItem.getPropertyHandle(ILevelViewConstants.MEMBER_PROP).add(cellHandle);
		}

		return extendedItem;
	}

	/**
	 * Creates a general crosstab cell.
	 * 
	 * @param module
	 * @return the general crosstab cell extended item handle
	 */
	public static ExtendedItemHandle createCrosstabCell(ModuleHandle module) {
		if (module == null)
			return null;
		return module.getElementFactory().newExtendedItem(null, CROSSTAB_CELL_EXTENSION_NAME);
	}

	/**
	 * Creates an aggregation cell.
	 * 
	 * @param module
	 * @return the aggregation cell extended item handle
	 */
	public static ExtendedItemHandle createAggregationCell(ModuleHandle module) {
		if (module == null)
			return null;
		return module.getElementFactory().newExtendedItem(null, AGGREGATION_CELL_EXTENSION_NAME);
	}

}
