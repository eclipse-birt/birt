/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
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
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
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
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.graphics.Image;

/**
 * ScatterChart
 */
public class ScatterChart extends DefaultChartTypeImpl {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_SCATTER;

	protected static final String STANDARD_SUBTYPE_LITERAL = "Standard Scatter Chart"; //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString("ScatterChart.Txt.Description"); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image img2D = null;

	public ScatterChart() {
		imgIcon = UIHelper.getImage("icons/obj16/scattercharticon.gif"); //$NON-NLS-1$
		super.chartTitle = Messages.getString("ScatterChart.Txt.DefaultScatterChartTitle"); //$NON-NLS-1$
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
		return new HelpContentImpl(TYPE_LITERAL, Messages.getString("ScatterChart.Txt.HelpText")); //$NON-NLS-1$
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
				img2D = UIHelper.getImage("icons/wizban/scatterchartimage.gif"); //$NON-NLS-1$
			} else {
				img2D = UIHelper.getImage("icons/wizban/horizontalscatterchartimage.gif"); //$NON-NLS-1$
			}

			vSubTypes.add(new DefaultChartSubTypeImpl(STANDARD_SUBTYPE_LITERAL, img2D, sStandardDescription,
					Messages.getString("ScatterChart.SubType.Standard"))); //$NON-NLS-1$
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

		Axis xAxis = newChart.getAxes().get(0);

		SeriesDefinition sdX = SeriesDefinitionImpl.createDefault();
		Series baseSeries = SeriesImpl.createDefault();
		sdX.getSeries().add(baseSeries);
		xAxis.getSeriesDefinitions().add(sdX);

		Axis yAxis = xAxis.getAssociatedAxes().get(0);

		SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
		Series orthogonalSeries = ScatterSeriesImpl.createDefault();
		sdY.getSeries().add(orthogonalSeries);
		yAxis.getSeriesDefinitions().add(sdY);

		if (sSubType.equalsIgnoreCase(STANDARD_SUBTYPE_LITERAL)) {
			newChart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);
		}
		addSampleData(newChart);
		return newChart;
	}

	private void addSampleData(Chart newChart) {
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		sd.getBaseSampleData().clear();
		sd.getOrthogonalSampleData().clear();

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("3,9,-2"); //$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
		oSample.setDataSetRepresentation("15,-4,12"); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		/*
		 * OrthogonalSampleData oSample2 =
		 * DataFactory.eINSTANCE.createOrthogonalSampleData();
		 * oSample2.setDataSetRepresentation("2,27,35");
		 * oSample2.setSeriesDefinitionIndex(0);
		 * sd.getOrthogonalSampleData().add(oSample2);
		 */
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
			if (currentChart.getType().equals(TYPE_LITERAL)) {
				if (!currentChart.getSubType().equals(sNewSubType)) {
					currentChart.setSubType(sNewSubType);
					EList<Axis> axes = ((ChartWithAxes) currentChart).getAxes().get(0).getAssociatedAxes();
					for (int i = 0; i < axes.size(); i++) {
						axes.get(i).setPercent(false);
						EList<SeriesDefinition> seriesdefinitions = axes.get(i).getSeriesDefinitions();
						for (int j = 0; j < seriesdefinitions.size(); j++) {
							Series series = seriesdefinitions.get(j).getDesignTimeSeries();
							series.unsetStacked();
						}
					}
				}
			} else if (currentChart.getType().equals(BarChart.TYPE_LITERAL)
					|| currentChart.getType().equals(TubeChart.TYPE_LITERAL)
					|| currentChart.getType().equals(ConeChart.TYPE_LITERAL)
					|| currentChart.getType().equals(PyramidChart.TYPE_LITERAL)
					|| currentChart.getType().equals(StockChart.TYPE_LITERAL)
					|| currentChart.getType().equals(AreaChart.TYPE_LITERAL)
					|| currentChart.getType().equals(LineChart.TYPE_LITERAL)
					|| currentChart.getType().equals(BubbleChart.TYPE_LITERAL)
					|| currentChart.getType().equals(DifferenceChart.TYPE_LITERAL)
					|| currentChart.getType().equals(GanttChart.TYPE_LITERAL)) {
				currentChart.setType(TYPE_LITERAL);
				currentChart.setSubType(sNewSubType);
				Text title = currentChart.getTitle().getLabel().getCaption();
				if (title.getValue() != null && (title.getValue().trim().length() == 0
						|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
					title.setValue(getDefaultTitle());
				}

				List<AxisType> axisTypes = new ArrayList<AxisType>();
				EList<Axis> axes = ((ChartWithAxes) currentChart).getAxes().get(0).getAssociatedAxes();
				for (int i = 0, seriesIndex = 0; i < axes.size(); i++) {
					// Buzilla#200885. For the usable sake, if data is not
					// bound, always set Linear axis type for Scatter chart. If
					// data is bound, the actual axis type will be reset
					// correctly according to the real data type.
					if (!ChartPreviewPainter.isLivePreviewActive() && axes.get(i).isSetType()
							&& !isNumbericAxis(axes.get(i))) {
						axes.get(i).setType(AxisType.LINEAR_LITERAL);
					}

					axes.get(i).unsetPercent();
					EList<SeriesDefinition> seriesdefinitions = axes.get(i).getSeriesDefinitions();
					for (int j = 0; j < seriesdefinitions.size(); j++) {
						Series series = seriesdefinitions.get(j).getDesignTimeSeries();
						series = getConvertedSeries(series, seriesIndex++);
						series.unsetStacked();
						seriesdefinitions.get(j).getSeries().clear();
						seriesdefinitions.get(j).getSeries().add(series);
						axisTypes.add(axes.get(i).getType());
					}
				}
				ChartElementUtil.setEObjectAttribute(currentChart, "orientation", //$NON-NLS-1$
						newOrientation, newOrientation == null);
				ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
						getDimensionFor(sNewDimension), sNewDimension == null);

				currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData(),
						((ChartWithAxes) currentChart).getAxes().get(0).getType(), axisTypes));
			} else {
				return null;
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

			Axis xAxis = ((ChartWithAxes) currentChart).getAxes().get(0);

			Axis yAxis = xAxis.getAssociatedAxes().get(0);

			{
				// Clear existing series definitions
				xAxis.getSeriesDefinitions().clear();

				// Copy base series definitions
				xAxis.getSeriesDefinitions().add(((ChartWithoutAxes) helperModel).getSeriesDefinitions().get(0));

				// Clear existing series definitions
				yAxis.getSeriesDefinitions().clear();

				// Copy orthogonal series definitions
				yAxis.getSeriesDefinitions().addAll((xAxis.getSeriesDefinitions().get(0)).getSeriesDefinitions());

				// Update the base series
				Series series = xAxis.getSeriesDefinitions().get(0).getDesignTimeSeries();
				// series = getConvertedSeries( series );

				// Clear existing series
				xAxis.getSeriesDefinitions().get(0).getSeries().clear();

				// Add converted series
				xAxis.getSeriesDefinitions().get(0).getSeries().add(series);

				// Update the orthogonal series
				EList<SeriesDefinition> seriesdefinitions = yAxis.getSeriesDefinitions();
				for (int j = 0; j < seriesdefinitions.size(); j++) {
					series = seriesdefinitions.get(j).getDesignTimeSeries();
					series = getConvertedSeries(series, j);
					series.getLabel().unsetVisible();
					series.unsetStacked();
					// Clear any existing series
					seriesdefinitions.get(j).getSeries().clear();
					// Add the new series
					seriesdefinitions.get(j).getSeries().add(series);
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
		return currentChart;
	}

	private Series getConvertedSeries(Series series, int seriesIndex) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		ScatterSeries scatterseries = (ScatterSeries) ChartCacheManager.getInstance()
				.findSeries(ScatterSeriesImpl.class.getName(), seriesIndex);
		if (scatterseries == null) {
			scatterseries = (ScatterSeries) ScatterSeriesImpl.createDefault();
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, scatterseries);

		return scatterseries;
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
		return true;
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
		return Messages.getString("ScatterChart.Txt.DisplayName"); //$NON-NLS-1$
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
			return ScatterSeriesImpl.create();
		} else {
			return ScatterSeriesImpl.createDefault();
		}
	}

	private boolean isNumbericAxis(Axis axis) {
		return (axis.getType().getValue() == AxisType.LINEAR) || (axis.getType().getValue() == AxisType.LOGARITHMIC);
	}

	@Override
	public boolean canExpand() {
		return true;
	}

}
