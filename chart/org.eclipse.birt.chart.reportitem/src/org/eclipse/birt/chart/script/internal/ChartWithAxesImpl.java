/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.script.api.IChartWithAxes;
import org.eclipse.birt.chart.script.api.component.IAxis;
import org.eclipse.birt.chart.script.api.component.ICategory;
import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.internal.component.AxisImpl;
import org.eclipse.birt.chart.script.internal.component.CategoryImpl;
import org.eclipse.birt.chart.script.internal.component.ValueSeriesImpl;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.emf.common.util.EList;

/**
 * 
 */

public class ChartWithAxesImpl extends ChartImpl implements IChartWithAxes {

	public ChartWithAxesImpl(ExtendedItemHandle eih, ChartWithAxes cm) {
		super(eih, cm);
	}

	public IAxis getCategoryAxis() {
		return new AxisImpl((Axis) getChartWithAxes().getAxes().get(0));
	}

	public IAxis[] getValueAxes() {
		Axis bAxis = (Axis) getChartWithAxes().getAxes().get(0);
		EList oAxes = bAxis.getAssociatedAxes();
		IAxis[] valueAxes = new IAxis[oAxes.size()];
		for (int i = 0; i < valueAxes.length; i++) {
			valueAxes[i] = new AxisImpl((Axis) oAxes.get(i));
		}
		return valueAxes;
	}

	public IValueSeries[][] getValueSeries() {
		Axis bAxis = (Axis) getChartWithAxes().getAxes().get(0);
		EList oAxes = bAxis.getAssociatedAxes();
		IValueSeries[][] valueSeries = new IValueSeries[oAxes.size()][];
		for (int i = 0; i < oAxes.size(); i++) {
			Axis oAxis = (Axis) oAxes.get(i);
			EList oSeries = oAxis.getSeriesDefinitions();
			valueSeries[i] = new IValueSeries[oSeries.size()];
			for (int j = 0; j < oSeries.size(); j++) {
				SeriesDefinition sd = (SeriesDefinition) oSeries.get(j);
				valueSeries[i][j] = ValueSeriesImpl.createValueSeries(sd, cm);
			}
		}
		return valueSeries;
	}

	public boolean isHorizontal() {
		return getChartWithAxes().isTransposed();
	}

	public void setHorizontal(boolean horizontal) {
		getChartWithAxes().setTransposed(horizontal);
	}

	public ICategory getCategory() {
		Axis bAxis = (Axis) getChartWithAxes().getAxes().get(0);
		SeriesDefinition bSd = (SeriesDefinition) bAxis.getSeriesDefinitions().get(0);
		return new CategoryImpl(bSd, cm);
	}

	private ChartWithAxes getChartWithAxes() {
		return (ChartWithAxes) cm;
	}

	public void setDimension(String dimensionName) {
		super.setDimension(dimensionName);
		if (ChartDimension.THREE_DIMENSIONAL_LITERAL.getName().equals(dimensionName)) {
			create3DModel();
		}
	}

	private void create3DModel() {
		getChartWithAxes().setRotation(Rotation3DImpl.create(new Angle3D[] { Angle3DImpl.create(-20, 45, 0) }));

		getChartWithAxes().getPrimaryBaseAxes()[0].getAncillaryAxes().clear();

		Axis zAxisAncillary = org.eclipse.birt.chart.model.component.impl.AxisImpl.create(Axis.ANCILLARY_BASE);
		zAxisAncillary.setTitlePosition(Position.BELOW_LITERAL);
		zAxisAncillary.getTitle().getCaption().setValue("Z Axis"); //$NON-NLS-1$
		zAxisAncillary.getTitle().setVisible(true);
		zAxisAncillary.setPrimaryAxis(true);
		zAxisAncillary.setLabelPosition(Position.BELOW_LITERAL);
		zAxisAncillary.setOrientation(Orientation.HORIZONTAL_LITERAL);
		zAxisAncillary.getOrigin().setType(IntersectionType.MIN_LITERAL);
		zAxisAncillary.getOrigin().setValue(NumberDataElementImpl.create(0));
		zAxisAncillary.getTitle().setVisible(false);
		zAxisAncillary.setType(AxisType.TEXT_LITERAL);
		getChartWithAxes().getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisAncillary);

		getChartWithAxes().getPrimaryOrthogonalAxis(getChartWithAxes().getPrimaryBaseAxes()[0]).getTitle().getCaption()
				.getFont().setRotation(0);

		SeriesDefinition sdZ = SeriesDefinitionImpl.create();
		sdZ.getSeriesPalette().shift(0);
		sdZ.getSeries().add(SeriesImpl.create());
		zAxisAncillary.getSeriesDefinitions().add(sdZ);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		sd.getBaseSampleData().clear();
		sd.getOrthogonalSampleData().clear();

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("A, B, C"); //$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
		oSample.setDataSetRepresentation("5,4,12"); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData();
		sdAncillary.setDataSetRepresentation("Series 1"); //$NON-NLS-1$
		sd.getAncillarySampleData().add(sdAncillary);
	}
}
