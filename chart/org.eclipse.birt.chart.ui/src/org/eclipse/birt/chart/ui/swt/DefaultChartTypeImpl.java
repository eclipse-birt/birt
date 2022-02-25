/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.graphics.Image;

/**
 * DefaultChartTypeImpl
 */
public class DefaultChartTypeImpl implements IChartType {

	protected String chartTitle = ""; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getName()
	 */
	@Override
	public String getName() {
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getImage()
	 */
	@Override
	public Image getImage() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(
	 * java.lang.String, org.eclipse.birt.chart.model.attribute.Orientation)
	 */
	@Override
	public Collection<IChartSubType> getChartSubtypes(String Dimension, Orientation orientation) {
		return new Vector<>();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#canAdapt(org.eclipse
	 * .birt.chart.model.Chart, java.util.Hashtable)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public boolean canAdapt(Chart cModel, Hashtable htModelHints) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang
	 * .String, org.eclipse.birt.chart.model.attribute.Orientation,
	 * java.lang.String, org.eclipse.birt.chart.model.Chart)
	 */
	@Override
	public Chart getModel(String sType, Orientation Orientation, String Dimension, Chart currentChart) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions ()
	 */
	@Override
	public String[] getSupportedDimensions() {
		return new String[] { TWO_DIMENSION_TYPE };
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	@Override
	public String getDefaultDimension() {
		return TWO_DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition ()
	 */
	@Override
	public boolean supportsTransposition() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition
	 * (java.lang.String)
	 */
	@Override
	public boolean supportsTransposition(String dimension) {
		return supportsTransposition();
	}

	@Override
	public Orientation getDefaultOrientation() {
		return Orientation.VERTICAL_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getHelp()
	 */
	@Override
	public IHelpContent getHelp() {
		return new HelpContentImpl("{Title}", "{Description}"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	public ISelectDataComponent getBaseUI(Chart chart, ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle) {
		return new DefaultSelectDataComponent();
	}

	@Override
	public boolean isDimensionSupported(String dimensionType, ChartWizardContext context, int nbOfAxes,
			int nbOfSeries) {
		boolean isSupported = false;

		// Check whether general dimension types include specified type
		String[] supportedDimensions = getSupportedDimensions();
		for (int i = 0; i < supportedDimensions.length; i++) {
			if (supportedDimensions[i].equals(dimensionType)) {
				isSupported = true;
				break;
			}
		}

		if (isSupported && THREE_DIMENSION_TYPE.equals(dimensionType)) {
			if (context.getDataServiceProvider().checkState(IDataServiceProvider.PART_CHART)) {
				// Not support 3D in xtab
				return false;
			}
			isSupported = nbOfAxes <= 1;
		}

		return isSupported;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	@Override
	public Series getSeries() {
		// TODO Auto-generated method stub
		return getSeries(true);
	}

	/**
	 * Make the series the same type as the other one
	 *
	 * @param series
	 * @param seriesIndex
	 * @param firtsSeries
	 * @return converted series
	 * @since 2.3
	 */
	protected Series getConvertedSeriesAsFirst(Series series, int seriesIndex, Series firstSeries) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		Series tmpseries = ChartCacheManager.getInstance().findSeries(firstSeries.getClass().getName(), seriesIndex);
		if (tmpseries == null) {
			tmpseries = firstSeries.copyInstance();
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, tmpseries);

		if (firstSeries instanceof BarSeriesImpl) {
			((BarSeriesImpl) tmpseries).setRiser(((BarSeriesImpl) firstSeries).getRiser());
		}

		return tmpseries;
	}

	/**
	 * Converts sample data for chart types.
	 *
	 * @param currentSampleData
	 * @param xAxisType
	 * @param yAxisType
	 * @return
	 */
	protected SampleData getConvertedSampleData(SampleData currentSampleData, AxisType xAxisType, AxisType yAxisType) {
		// Convert base sample data
		EList<BaseSampleData> bsdList = currentSampleData.getBaseSampleData();
		Vector<BaseSampleData> vNewBaseSampleData = getConvertedBaseSampleDataRepresentation(bsdList, xAxisType);
		currentSampleData.getBaseSampleData().clear();
		currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);

		// Convert orthogonal sample data
		EList<OrthogonalSampleData> osdList = currentSampleData.getOrthogonalSampleData();
		Vector<OrthogonalSampleData> vNewOrthogonalSampleData = new Vector<>();
		for (int i = 0; i < osdList.size(); i++) {
			OrthogonalSampleData osd = osdList.get(i);
			osd.setDataSetRepresentation(
					ChartUIUtil.getConvertedSampleDataRepresentation(yAxisType, osd.getDataSetRepresentation(), i));
			vNewOrthogonalSampleData.add(osd);
		}
		currentSampleData.getOrthogonalSampleData().clear();
		currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
		return currentSampleData;
	}

	/**
	 * Converts sample data for chart types.
	 *
	 * @param currentSampleData
	 * @param xAxisType
	 * @param yAxisTypes
	 * @return
	 */
	protected SampleData getConvertedSampleData(SampleData currentSampleData, AxisType xAxisType,
			List<AxisType> yAxisTypes) {
		// Convert base sample data
		EList<BaseSampleData> bsdList = currentSampleData.getBaseSampleData();
		Vector<BaseSampleData> vNewBaseSampleData = getConvertedBaseSampleDataRepresentation(bsdList, xAxisType);
		currentSampleData.getBaseSampleData().clear();
		currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);

		// Convert orthogonal sample data
		EList<OrthogonalSampleData> osdList = currentSampleData.getOrthogonalSampleData();
		Vector<OrthogonalSampleData> vNewOrthogonalSampleData = getConvertedOrthogonalSampleDataRepresentation(osdList,
				yAxisTypes);
		currentSampleData.getOrthogonalSampleData().clear();
		currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
		return currentSampleData;
	}

	private Vector<BaseSampleData> getConvertedBaseSampleDataRepresentation(EList<BaseSampleData> bsdList,
			AxisType xAxisType) {
		Vector<BaseSampleData> vNewBaseSampleData = new Vector<>();
		for (int i = 0; i < bsdList.size(); i++) {
			BaseSampleData bsd = bsdList.get(i);
			bsd.setDataSetRepresentation(
					ChartUIUtil.getConvertedSampleDataRepresentation(xAxisType, bsd.getDataSetRepresentation(), i));
			vNewBaseSampleData.add(bsd);
		}
		return vNewBaseSampleData;
	}

	private Vector<OrthogonalSampleData> getConvertedOrthogonalSampleDataRepresentation(
			EList<OrthogonalSampleData> osdList, List<AxisType> axisTypes) {
		Vector<OrthogonalSampleData> vNewOrthogonalSampleData = new Vector<>();
		for (int i = 0; i < axisTypes.size(); i++) {
			OrthogonalSampleData osd = osdList.get(i);
			osd.setDataSetRepresentation(ChartUIUtil.getConvertedSampleDataRepresentation(axisTypes.get(i),
					osd.getDataSetRepresentation(), i));
			vNewOrthogonalSampleData.add(osd);
		}
		return vNewOrthogonalSampleData;
	}

	@Override
	public boolean canCombine() {
		return false;
	}

	@Override
	public String getDefaultTitle() {
		return chartTitle;
	}

	@Override
	public boolean canExpand() {
		return false;
	}

	/**
	 * Copies generic chart properties
	 *
	 * @param oldChart chart model as copy source
	 * @param newChart chart model as copy target
	 */
	protected void copyChartProperties(Chart oldChart, Chart newChart) {
		// Copy generic chart properties from the old chart
		newChart.setBlock(oldChart.getBlock());
		newChart.setDescription(oldChart.getDescription());
		if (newChart.isSetGridColumnCount()) {
			newChart.setGridColumnCount(oldChart.getGridColumnCount());
		}
		newChart.setSampleData(oldChart.getSampleData());
		newChart.setScript(oldChart.getScript());
		newChart.setUnits(oldChart.getUnits());
		if (oldChart.isSetSeriesThickness()) {
			newChart.setSeriesThickness(oldChart.getSeriesThickness());
		}

		newChart.getExtendedProperties().clear();
		newChart.getExtendedProperties().addAll(oldChart.getExtendedProperties());

		if (oldChart.getInteractivity() != null) {
			if (oldChart.getInteractivity().isSetEnable()) {
				newChart.getInteractivity().setEnable(oldChart.getInteractivity().isEnable());
			}
			if (oldChart.getInteractivity().isSetLegendBehavior()) {
				newChart.getInteractivity().setLegendBehavior(oldChart.getInteractivity().getLegendBehavior());
			}
		}
	}

	@Override
	public String getValueDefinitionName() {
		return Messages.getString("DefaultChartTypeImpl.Label.ValueDefinitionName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#isChartWithAxis()
	 */
	@Override
	public boolean isChartWithAxes() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries(boolean)
	 */
	@Override
	public Series getSeries(boolean needInitialing) {
		return null;
	}
}
