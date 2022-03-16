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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.computation.Methods;
import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.internal.factory.DateFormatWrapperFactory;
import org.eclipse.birt.chart.internal.factory.IDateFormatWrapper;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.text.DecimalFormat;

final class TextAxisLabelTextProvider extends AxisLabelTextProvider {

	boolean bTickBetweenCategories;

	protected TextAxisLabelTextProvider(OneAxis oax) {
		super(oax);
	}

	@Override
	protected void init() {
		bTickBetweenCategories = oax.getModelAxis().getScale().isTickBetweenCategories();
	}

	@Override
	public String getLabelText(int index) throws ChartException {
		if (!bTickBetweenCategories && index == 0) {
			return ""; //$NON-NLS-1$
		} else {
			return sc.getComputedLabelText(index);
		}
	}

}

final class DatetimeAxisLabelTextProvider extends AxisLabelTextProvider {

	private CDateTime cdtMin;
	private int iUnit, iStep;
	private IDateFormatWrapper sdf;

	protected DatetimeAxisLabelTextProvider(OneAxis oax) {
		super(oax);
	}

	@Override
	protected void init() {
		cdtMin = Methods.asDateTime(sc.getMinimum());
		iUnit = Methods.asInteger(sc.getUnit());
		iStep = Methods.asInteger(sc.getStep());

		if (axModel.getFormatSpecifier() == null) {
			sdf = DateFormatWrapperFactory.getPreferredDateFormat(iUnit, oax.getRunTimeContext().getULocale());
		}
	}

	@Override
	public String getLabelText(int index) throws ChartException {
		CDateTime cdt = cdtMin.forward(iUnit, iStep * index);

		return ValueFormatter.format(cdt, axModel.getFormatSpecifier(), oax.getRunTimeContext().getULocale(), sdf);

	}

}

final class LinearAxisLabelTextProvider extends AxisLabelTextProvider {

	private NumberDataElement nde = NumberDataElementImpl.create(0);
	private double dMinValue;
	private double dStep;
	private DecimalFormat df;

	protected LinearAxisLabelTextProvider(OneAxis oax) {
		super(oax);
	}

	@Override
	protected void init() {
		dMinValue = Methods.asDouble(sc.getMinimum()).doubleValue();
		dStep = Methods.asDouble(sc.getStep()).doubleValue();

		if (axModel.getFormatSpecifier() == null) {
			df = sc.computeDecimalFormat(dMinValue, dStep);
		}
	}

	@Override
	public String getLabelText(int index) throws ChartException {
		double dValue;

		if (index == da.size() - 1) {
			// This is the last tick, use pre-computed value to
			// handle non-equal scale unit case.
			dValue = Methods.asDouble(sc.getMaximum()).doubleValue();
		} else {
			dValue = dMinValue + dStep * index;
		}

		nde.setValue(dValue);

		return ValueFormatter.format(nde, axModel.getFormatSpecifier(), oax.getRunTimeContext().getULocale(), df);
	}

}

final class LogAxisLabelTextProvider extends AxisLabelTextProvider {

	private NumberDataElement nde = NumberDataElementImpl.create(0);
	private double dMinValue;
	private double dStep;
	private DecimalFormat df;

	protected LogAxisLabelTextProvider(OneAxis oax) {
		super(oax);
	}

	@Override
	protected void init() {
		dMinValue = Methods.asDouble(sc.getMinimum()).doubleValue();
		dStep = Methods.asDouble(sc.getStep()).doubleValue();

		if (axModel.getFormatSpecifier() == null) {
			df = sc.computeDecimalFormat(dMinValue, dStep);
		}
	}

	@Override
	public String getLabelText(int index) throws ChartException {
		double dValue = dMinValue * Math.pow(dStep, index);
		nde.setValue(dValue);

		return ValueFormatter.format(nde, axModel.getFormatSpecifier(), oax.getRunTimeContext().getULocale(), df);

	}

}

/**
 * This class provides the axis label texts of an axis.
 */

public abstract class AxisLabelTextProvider {

	protected OneAxis oax;
	protected Axis axModel;
	protected AxisTickCoordinates da;
	protected AutoScale sc;

	protected AxisLabelTextProvider(OneAxis oax) {
		this.oax = oax;
		this.axModel = oax.getModelAxis();
		this.da = oax.getScale().getTickCordinates();
		this.sc = oax.getScale();
		init();
	}

	public static AxisLabelTextProvider create(OneAxis oax) {
		AutoScale sc = oax.getScale();

		if ((sc.getType() & IConstants.TEXT) == IConstants.TEXT || sc.isCategoryScale()) {
			return new TextAxisLabelTextProvider(oax);
		} else if ((sc.getType() & IConstants.LINEAR) == IConstants.LINEAR) {
			return new LinearAxisLabelTextProvider(oax);
		} else if ((sc.getType() & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC) {
			return new LogAxisLabelTextProvider(oax);
		} else if ((sc.getType() & IConstants.DATE_TIME) == IConstants.DATE_TIME) {
			return new DatetimeAxisLabelTextProvider(oax);
		} else {
			return null;
		}
	}

	protected abstract void init();

	public abstract String getLabelText(int index) throws ChartException;
}
