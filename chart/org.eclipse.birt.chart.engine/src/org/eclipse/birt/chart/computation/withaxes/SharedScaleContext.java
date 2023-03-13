/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.computation.withaxes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.util.NumberUtil;

import com.ibm.icu.math.BigDecimal;

/**
 * We use this class to store shared scale context of chart in cross-tab.
 *
 * @since 2.5
 */

public final class SharedScaleContext {

	private ScaleContext scaleContext;
	private List<Object> alMinmax = new ArrayList<>(2);
	private long width = -1;
	private long height = -1;
	private boolean bShared = false;

	public SharedScaleContext(ScaleContext scaleContext, Object realMin, Object realMax) {
		super();
		this.scaleContext = scaleContext;
		alMinmax.add(realMin);
		alMinmax.add(realMax);
	}

	/**
	 * update the bounds info, shared scale should be recalculated when bounds
	 * changed.
	 *
	 * @param bo
	 */
	public void updateBounds(Bounds bo) {
		long widthNew = Math.round(bo.getWidth());
		long heightNew = Math.round(bo.getHeight());

		if (width != widthNew || height != heightNew) {
			width = widthNew;
			height = heightNew;
			bShared = false;
		}
	}

	/**
	 *
	 * @param oMin
	 * @param oMax
	 * @return
	 */
	public static SharedScaleContext createInstance(Object oMin, Object oMax) {
		ScaleContext sct = ScaleContext.createSimpleScale(oMin, oMax);
		return new SharedScaleContext(sct, sct.getMin(), sct.getMax());
	}

	/**
	 * @return Returns the scaleContext.
	 */
	public ScaleContext getScaleContext() {
		return scaleContext;
	}

	/**
	 * @param scaleContext The scaleContext to set.
	 */
	public void setScaleContext(ScaleContext scaleContext) {
		this.scaleContext = scaleContext;
	}

	/**
	 * Returns if the scale will be shared among multiple chart instances
	 *
	 * @return shared or not
	 * @since 2.5
	 */
	public boolean isShared() {
		return bShared;
	}

	/**
	 * @param shared
	 * @since 2.5
	 *
	 */
	public void setShared(boolean shared) {
		bShared = shared;
	}

	/**
	 * Create a DataSetIterator with the min/max value, which can be used by
	 * AutoScale.
	 *
	 * @param iDataType
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 */
	public DataSetIterator createDataSetIterator(int iDataType) throws ChartException, IllegalArgumentException {
		if ((iDataType & IConstants.NUMERICAL) == IConstants.NUMERICAL) {
			List<Object> minmax = new ArrayList<>(2);
			for (Object o : alMinmax) {
				if (o instanceof Number) {
					minmax.add(Double.valueOf(((Number) o).doubleValue()));
				} else {
					minmax.add(o);
				}
			}
			return new DataSetIterator(minmax, iDataType);
		}
		return new DataSetIterator(alMinmax, iDataType);
	}

	/**
	 * Create a DataSetIterator with the min/max value, which can be used by
	 * AutoScale. This method supports big decimal.
	 *
	 * @param iDataType
	 * @param isBigNumber indicates current is big number.
	 * @param divisor     the divisor for big number, actual big number will divide
	 *                    the divisor to get a double value, the double value is
	 *                    used to compute scale of axis.
	 * @return
	 * @throws ChartException
	 * @throws IllegalArgumentException
	 * @since 2.6
	 */
	public DataSetIterator createDataSetIterator(int iDataType, boolean isBigNumber, BigDecimal divisor)
			throws ChartException, IllegalArgumentException {
		if (isBigNumber) {
			List<Object> minmax = new ArrayList<>(2);
			for (Object o : alMinmax) {
				minmax.add(NumberUtil.asBigNumber(NumberUtil.transformNumber(o), divisor));
			}
			return new DataSetIterator(minmax, iDataType);
		}
		return createDataSetIterator(iDataType);
	}
}
