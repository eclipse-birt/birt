/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;

/**
 * IntersectionValue
 */
public final class IntersectionValue extends Methods implements IConstants {

	public static final IntersectionValue MAX_VALUE = new IntersectionValue(MAX, 0);
	public static final IntersectionValue MIN_VALUE = new IntersectionValue(MIN, 0);

	int iType;

	Object oValue;

	/**
	 * @param _iType
	 * @param _dValue
	 */
	public IntersectionValue(int _iType, double _dValue) {
		iType = _iType;
		oValue = new Double(_dValue);
	}

	/**
	 * @param _iType
	 * @param _oValue
	 */
	public IntersectionValue(int _iType, Object _oValue) {
		iType = _iType;
		oValue = _oValue;
	}

	/**
	 * @return
	 */
	public final int getType() {
		return iType;
	}

	/**
	 * @return
	 */
	public final Object getValue() {
		return oValue;
	}

	/**
	 * @return
	 */
	public final double getValueAsDouble() {
		return asDouble(oValue).doubleValue();
	}

	/**
	 * @param sc
	 * @return
	 */
	public final double getValueAsDouble(AutoScale sc) {
		if (iType == MAX) {
			return asDouble(sc.getMaximum()).doubleValue();
		} else if (iType == MIN) {
			return asDouble(sc.getMinimum()).doubleValue();
		}
		return asDouble(oValue).doubleValue();
	}

}
