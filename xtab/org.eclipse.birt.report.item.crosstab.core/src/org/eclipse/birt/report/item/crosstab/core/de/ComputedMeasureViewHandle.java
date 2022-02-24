/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.birt.report.item.crosstab.core.IComputedMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * ComputedMeasureViewHandle
 */
public class ComputedMeasureViewHandle extends MeasureViewHandle implements IComputedMeasureViewConstants {

	/**
	 * 
	 * @param handle
	 */
	ComputedMeasureViewHandle(DesignElementHandle handle) {
		super(handle);
	}

	public String getName() {
		return handle.getName();
	}

	@Override
	public MeasureHandle getCubeMeasure() {
		// this does not apply to regular data cubes
		if (CrosstabUtil.isBoundToLinkedDataSet(this.getCrosstab())) {
			// this only applies if measure has its own aggregation
			if (CrosstabUtil.measureHasItsOwnAggregation(this.getCrosstab(), super.getCubeMeasure())) {
				// return the super implementation
				return super.getCubeMeasure();
			}
		}
		// otherwise in all other cases return normal
		return null;
	}

	@Override
	public String getCubeMeasureName() {
		String measureName = (String) handle.getProperty(IComputedMeasureViewConstants.MEASURE_NAME_PROP);
		if (measureName == null) {
			measureName = handle.getName();
		}
		return measureName;
	}
}
