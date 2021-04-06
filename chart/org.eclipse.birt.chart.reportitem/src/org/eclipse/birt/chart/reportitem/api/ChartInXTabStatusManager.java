/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Status manager for chart in xtab. This manager is used to maintain the data
 * item in grand total cell. This status can be used to check if grand total
 * items should be kept, or just replaced by axis chart.
 */

public class ChartInXTabStatusManager {

	private static Map<AggregationCellHandle, Boolean> mapGrandItems = new HashMap<AggregationCellHandle, Boolean>();

	@SuppressWarnings("unchecked")
	public static void updateGrandItemStatus(AggregationCellHandle detailCell) throws BirtException {
		if (detailCell != null) {
			AggregationCellHandle grandCell = ChartCubeUtil.getGrandTotalAggregationCell(detailCell, detailCell
					.getCrosstab().getMeasureDirection().equals(ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL));
			if (grandCell != null) {
				List contents = grandCell.getContents();
				if (contents.size() > 0) {
					for (Object content : contents) {
						if (content instanceof DataItemHandle) {
							mapGrandItems.put(detailCell, true);
							return;
						}
					}
				}
			}
			mapGrandItems.put(detailCell, false);
		}
	}

	public static boolean hasGrandItem(AggregationCellHandle detailCell) {
		if (mapGrandItems.containsKey(detailCell)) {
			return mapGrandItems.get(detailCell);
		}
		return false;
	}

	public static boolean hasGrandItem(ExtendedItemHandle chartHandle) throws BirtException {
		if (ChartCubeUtil.isPlotChart(chartHandle)) {
			return hasGrandItem(ChartCubeUtil.getXtabContainerCell(chartHandle));
		}
		return false;
	}
}
