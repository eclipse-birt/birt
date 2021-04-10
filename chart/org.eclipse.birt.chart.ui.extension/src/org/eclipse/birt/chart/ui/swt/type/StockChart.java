/*******************************************************************************
 * Copyright (c) Oct 22, 2004 Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation {ADD
 * SUBSEQUENT AUTHOR & CONTRIBUTION}
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.DefaultBaseSeriesComponent;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.swt.graphics.Image;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.StringTokenizer;

/**
 * StockChart
 */
public class StockChart extends DefaultChartTypeImpl {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_STOCK;

	protected static final String STANDARD_SUBTYPE_LITERAL = "Standard Stock Chart"; //$NON-NLS-1$

	protected static final String BAR_STICK_SUBTYPE_LITERAL = "Bar Stick Stock Chart"; //$NON-NLS-1$

	private static final String sCandleStickDescription = Messages.getString("StockChart.Txt.CandleStickDescription"); //$NON-NLS-1$

	private static final String sBarStickDescription = Messages.getString("StockChart.Txt.BarStickDescription"); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image img2DCandleStick = null;

	private transient Image img2DBarlStick = null;

	public StockChart() {
		imgIcon = UIHelper.getImage("icons/obj16/stockcharticon.gif"); //$NON-NLS-1$
		super.chartTitle = Messages.getString("StockChart.Txt.DefaultStockChartTitle"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getName()
	 */
	public String getName() {
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getImage()
	 */
	public Image getImage() {
		return imgIcon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	public IHelpContent getHelp() {
		return new HelpContentImpl(TYPE_LITERAL, Messages.getString("StockChart.Txt.HelpText")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.
	 * lang.String)
	 */
	public Collection<IChartSubType> getChartSubtypes(String sDimension, Orientation orientation) {
		Vector<IChartSubType> vSubTypes = new Vector<IChartSubType>();
		if (sDimension.equals(TWO_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName())) {
			if (orientation.equals(Orientation.VERTICAL_LITERAL)) {
				img2DCandleStick = UIHelper.getImage("icons/wizban/stockchartimage.gif"); //$NON-NLS-1$
				img2DBarlStick = UIHelper.getImage("icons/wizban/stockchartbarstickimage.gif"); //$NON-NLS-1$
			} else {
				img2DCandleStick = UIHelper.getImage("icons/wizban/horizontalstockchartimage.gif"); //$NON-NLS-1$
				img2DBarlStick = UIHelper.getImage("icons/wizban/horizontalstockchartbarstickimage.gif"); //$NON-NLS-1$
			}
			vSubTypes.add(new DefaultChartSubTypeImpl(STANDARD_SUBTYPE_LITERAL, img2DCandleStick,
					sCandleStickDescription, Messages.getString("StockChart.SubType.CandleStick"))); //$NON-NLS-1$
			vSubTypes.add(new DefaultChartSubTypeImpl(BAR_STICK_SUBTYPE_LITERAL, img2DBarlStick, sBarStickDescription,
					Messages.getString("StockChart.SubType.BarStick"))); //$NON-NLS-1$
		}
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.
	 * String, java.lang.String, java.lang.String)
	 */
	public Chart getModel(String sSubType, Orientation orientation, String sDimension, Chart currentChart) {
		ChartWithAxes newChart = null;
		if (currentChart != null) {
			newChart = (ChartWithAxes) getConvertedChart(currentChart, sSubType, orientation, sDimension);
			if (newChart != null) {
				return newChart;
			}
		}
		newChart = ChartWithAxesImpl.createDefault();
		newChart.setType(TYPE_LITERAL);
		newChart.setSubType(sSubType);
		ChartElementUtil.setEObjectAttribute(newChart, "orientation", //$NON-NLS-1$
				orientation, orientation == null);
		ChartElementUtil.setEObjectAttribute(newChart, "dimension", //$NON-NLS-1$
				getDimensionFor(sDimension), sDimension == null);
		try {
			ChartElementUtil.setDefaultValue(newChart.getAxes().get(0), "categoryAxis", //$NON-NLS-1$
					true);
		} catch (ChartException e) {
			// Do nothing.
		}

		Axis xAxis = newChart.getAxes().get(0);

		SeriesDefinition sdX = SeriesDefinitionImpl.createDefault();
		Series categorySeries = SeriesImpl.createDefault();
		sdX.getSeries().add(categorySeries);
		xAxis.getSeriesDefinitions().add(sdX);

		Axis yAxis = xAxis.getAssociatedAxes().get(0);

		SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
		Series valueSeries = StockSeriesImpl.createDefault();
		if (BAR_STICK_SUBTYPE_LITERAL.equals(sSubType)) {
			((StockSeries) valueSeries).setShowAsBarStick(true);
		}
		sdY.getSeries().add(valueSeries);
		yAxis.getSeriesDefinitions().add(sdY);

		addSampleData(newChart);
		return newChart;
	}

	private void addSampleData(Chart newChart) {
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		sd.getBaseSampleData().clear();
		sd.getOrthogonalSampleData().clear();

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("01/25/2005,01/26/2005"); //$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
		oSample.setDataSetRepresentation("5,4,12"); //$NON-NLS-1$
		// oSample.setDataSetRepresentation( "H5.3 L1.3 O4.5 C3.4,H4.2 L3.1 O3.4
		// C4.1" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		newChart.setSampleData(sd);
	}

	private Chart getConvertedChart(Chart currentChart, String sNewSubType, Orientation newOrientation,
			String sNewDimension) {
		Chart helperModel = currentChart.copyInstance();
		helperModel.eAdapters().addAll(currentChart.eAdapters());
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance().cacheSeries(ChartUIUtil.getAllOrthogonalSeriesDefinitions(helperModel));
		IChartType oldType = ChartUIUtil.getChartType(currentChart.getType());
		if ((currentChart instanceof ChartWithAxes)) {
			Axis xAxis = ((ChartWithAxes) currentChart).getAxes().get(0);
			// Original chart is of this type (StockChart)
			if (currentChart.getType().equals(TYPE_LITERAL)) {
				// Original chart is of the required subtype
				if (!currentChart.getSubType().equals(sNewSubType)) {
					currentChart.setSubType(sNewSubType);
					for (Axis yAxis : xAxis.getAssociatedAxes()) {
						yAxis.unsetPercent();
						for (SeriesDefinition ysd : yAxis.getSeriesDefinitions()) {
							Series series = ysd.getDesignTimeSeries();
							series.unsetStacked();
							if (series instanceof StockSeries) {
								((StockSeries) series)
										.setShowAsBarStick(BAR_STICK_SUBTYPE_LITERAL.equals(currentChart.getSubType()));
							}
						}
					}
				}
			} else {
				if (!currentChart.getType().equals(TYPE_LITERAL)) {
					currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData(), false));
				}
				currentChart.setType(TYPE_LITERAL);
				Text title = currentChart.getTitle().getLabel().getCaption();
				if (title.getValue() != null && (title.getValue().trim().length() == 0
						|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
					title.setValue(getDefaultTitle());
				}

				currentChart.setSubType(sNewSubType);
				int seriesIndex = 0;
				for (Axis yAxis : xAxis.getAssociatedAxes()) {
					if (!ChartPreviewPainter.isLivePreviewActive()) {
						yAxis.setType(AxisType.LINEAR_LITERAL);
					}
					yAxis.unsetPercent();
					for (SeriesDefinition ysd : yAxis.getSeriesDefinitions()) {
						Series series = ysd.getDesignTimeSeries();
						series = getConvertedSeries(series, seriesIndex);
						series.unsetStacked();
						ysd.getSeries().clear();
						ysd.getSeries().add(series);
						seriesIndex++;
					}
				}
			}
		} else {
			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithAxesImpl.createDefault();
			copyChartProperties(helperModel, currentChart);
			currentChart.setType(TYPE_LITERAL);
			currentChart.setSubType(sNewSubType);
			ChartElementUtil.setEObjectAttribute(currentChart, "orientation", //$NON-NLS-1$
					newOrientation, newOrientation == null);
			ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
					getDimensionFor(sNewDimension), sNewDimension == null);
			try {
				ChartElementUtil.setDefaultValue(((ChartWithAxes) currentChart).getAxes().get(0), "categoryAxis", //$NON-NLS-1$
						true);
			} catch (ChartException e) {
				// Do nothing.
			}

			Axis xAxis = ((ChartWithAxes) currentChart).getAxes().get(0);

			Axis yAxis = xAxis.getAssociatedAxes().get(0);

			currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData(), true));

			{
				// Clear existing series definitions
				xAxis.getSeriesDefinitions().clear();

				// Copy base series definitions
				xAxis.getSeriesDefinitions().add(((ChartWithoutAxes) helperModel).getSeriesDefinitions().get(0));

				// Clear existing series definitions
				yAxis.getSeriesDefinitions().clear();

				// Copy orthogonal series definitions
				yAxis.getSeriesDefinitions().addAll(xAxis.getSeriesDefinitions().get(0).getSeriesDefinitions());

				// Update the base series
				SeriesDefinition bsd = xAxis.getSeriesDefinitions().get(0);
				Series series = bsd.getDesignTimeSeries();

				// Clear existing series
				bsd.getSeries().clear();

				// Add converted series
				bsd.getSeries().add(series);

				// Update the orthogonal series
				int j = 0;
				for (SeriesDefinition vsd : yAxis.getSeriesDefinitions()) {
					series = vsd.getDesignTimeSeries();
					series = getConvertedSeries(series, j++);
					series.getLabel().unsetVisible();
					series.unsetStacked();
					// Clear any existing series
					vsd.getSeries().clear();
					// Add the new series
					vsd.getSeries().add(series);
				}
			}

			Text title = currentChart.getTitle().getLabel().getCaption();
			if (title.getValue() != null && (title.getValue().trim().length() == 0
					|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
				title.setValue(getDefaultTitle());
			}
		}
		ChartElementUtil.setEObjectAttribute(currentChart, "orientation", //$NON-NLS-1$
				newOrientation, newOrientation == null);
		ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
				getDimensionFor(sNewDimension), sNewDimension == null);

		// Restore label position for different sub type of chart.
		ChartUIUtil.restoreLabelPositionFromCache(currentChart);

		// Restore aggregations by setting default aggregations
		SeriesGrouping sg = ChartUtil.getBaseSeriesDefinitions(currentChart).get(0).getGrouping();
		if (sg.getGroupType().getValue() == DataType.DATE_TIME) {
			ChartUIUtil.updateDefaultAggregations(currentChart);
		}

		return currentChart;
	}

	private Series getConvertedSeries(Series series, int seriesIndex) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		StockSeries stockseries = (StockSeries) ChartCacheManager.getInstance()
				.findSeries(StockSeriesImpl.class.getName(), seriesIndex);
		if (stockseries == null) {
			stockseries = (StockSeries) StockSeriesImpl.createDefault();
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, stockseries);

		return stockseries;
	}

	private SampleData getConvertedSampleData(SampleData currentSampleData, boolean convertBaseToDate) {
		if (convertBaseToDate) {
			// Convert base sample data to dateTime type.
			List<BaseSampleData> vNewBaseSampleData = new ArrayList<BaseSampleData>();
			for (BaseSampleData bsd : currentSampleData.getBaseSampleData()) {
				bsd.setDataSetRepresentation(getConvertedBaseSampleDataRepresentation(bsd.getDataSetRepresentation()));
				vNewBaseSampleData.add(bsd);
			}
			currentSampleData.getBaseSampleData().clear();
			currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);
		}

		// Convert orthogonal sample data
		List<OrthogonalSampleData> vNewOrthogonalSampleData = new ArrayList<OrthogonalSampleData>();
		int i = 0;
		for (OrthogonalSampleData osd : currentSampleData.getOrthogonalSampleData()) {
			osd.setDataSetRepresentation(ChartUIUtil.getConvertedSampleDataRepresentation(AxisType.LINEAR_LITERAL,
					osd.getDataSetRepresentation(), i++));
			vNewOrthogonalSampleData.add(osd);
		}
		currentSampleData.getOrthogonalSampleData().clear();
		currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
		return currentSampleData;
	}

	private String getConvertedBaseSampleDataRepresentation(String sOldRepresentation) {
		StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ","); //$NON-NLS-1$
		StringBuffer sbNewRepresentation = new StringBuffer(""); //$NON-NLS-1$
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); //$NON-NLS-1$
		int iValueCount = 0;
		while (strtok.hasMoreTokens()) {
			String sElement = strtok.nextToken().trim();
			if (!sElement.startsWith("'")) //$NON-NLS-1$
			{
				Calendar cal = Calendar.getInstance();
				// Increment the date once for each entry so that you get a
				// sequence of dates
				cal.set(Calendar.DATE, cal.get(Calendar.DATE) + iValueCount);
				sbNewRepresentation.append(sdf.format(cal.getTime()));
				iValueCount++;
			} else {
				sElement = sElement.substring(1, sElement.length() - 1);
				try {
					sdf.parse(sElement);
					sbNewRepresentation.append(sElement);
				} catch (ParseException e) {
					Calendar cal = Calendar.getInstance();
					// Increment the date once for each entry so that you get a
					// sequence of dates
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) + iValueCount);
					sbNewRepresentation.append(sdf.format(cal.getTime()));
					iValueCount++;
				}
			}
			sbNewRepresentation.append(","); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions() {
		return new String[] { TWO_DIMENSION_TYPE };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension() {
		return TWO_DIMENSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition() {
		return false;
	}

	private ChartDimension getDimensionFor(String sDimension) {
		// Other types are not supported.
		return ChartDimension.TWO_DIMENSIONAL_LITERAL;
	}

	public ISelectDataComponent getBaseUI(Chart chart, ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle) {
		return new DefaultBaseSeriesComponent(ChartUIUtil.getBaseSeriesDefinitions(chart).get(0), context, sTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("StockChart.Txt.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	public Series getSeries() {
		return getSeries(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getSeries(boolean)
	 */
	public Series getSeries(boolean needInitialing) {
		if (needInitialing) {
			return StockSeriesImpl.create();
		} else {
			return StockSeriesImpl.createDefault();
		}
	}

	@Override
	public boolean canCombine() {
		return true;
	}
}