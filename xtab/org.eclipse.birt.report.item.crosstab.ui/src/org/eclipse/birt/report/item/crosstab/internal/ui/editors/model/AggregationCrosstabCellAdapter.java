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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Adapter for the AggregationCrosstabCell
 */

public class AggregationCrosstabCellAdapter extends CrosstabCellAdapter {

	/**
	 * Constructor
	 * 
	 * @param handle
	 */
	public AggregationCrosstabCellAdapter(AggregationCellHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.
	 * BaseCrosstabAdapter#hashCode()
	 */
	public int hashCode() {
		return getCrosstabItemHandle().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.
	 * BaseCrosstabAdapter#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		// if (obj == getCrosstabItemHandle( ))
		// {
		// return true;
		// }
		// if (obj instanceof CrosstabCellAdapter)
		// {
		// return getCrosstabItemHandle( ) ==
		// ((CrosstabCellAdapter)obj).getCrosstabItemHandle();
		// }
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.util.IVirtualValidator#handleValidate(java.
	 * lang.Object)
	 */
	public boolean handleValidate(Object obj) {
		boolean bool = super.handleValidate(obj);
		if (bool) {
			return bool;
		}
		CrosstabReportItemHandle crosstab = getCrosstabCellHandle().getCrosstab();
		if (obj instanceof Object[]) {
			Object[] objects = (Object[]) obj;
			int len = objects.length;
			if (len == 0) {
				return false;
			}
			if (len == 1) {
				return handleValidate(objects[0]);
			} else {
				for (int i = 0; i < len; i++) {
					Object temp = objects[i];
					if (temp instanceof MeasureHandle || temp instanceof MeasureGroupHandle) {
						if (getPositionType().equals(ICrosstabCellAdapterFactory.CELL_MEASURE)
								&& crosstab.getCube() == CrosstabAdaptUtil.getCubeHandle((DesignElementHandle) temp)) {
							continue;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
				return true;
			}

		}

		if (obj instanceof MeasureHandle) {
			if (getPositionType().equals(ICrosstabCellAdapterFactory.CELL_MEASURE)
					&& CrosstabUtil.canContain(crosstab, (MeasureHandle) obj)) {
				return true;
			}
		}

		if (obj instanceof MeasureGroupHandle) {
			if (getPositionType().equals(ICrosstabCellAdapterFactory.CELL_MEASURE)
					&& crosstab.getCube() == CrosstabAdaptUtil.getCubeHandle((DesignElementHandle) obj)) {
				return true;
			}
		}
		return false;
	}
}
