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

package org.eclipse.birt.chart.ui.swt.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
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
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
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
 * MeterChart
 */
public class MeterChart extends DefaultChartTypeImpl {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_METER;

	protected static final String STANDARD_SUBTYPE_LITERAL = "Standard Meter Chart"; //$NON-NLS-1$

	protected static final String SUPERIMPOSED_SUBTYPE_LITERAL = "Superimposed Meter Chart"; //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString("MeterChart.Txt.Description"); //$NON-NLS-1$

	private static final String sSuperimposedDescription = Messages.getString("MeterChart.Txt.SuperimposedDescription"); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image imgStandard = null;

	private transient Image imgSuperimposed = null;

	public MeterChart() {
		imgIcon = UIHelper.getImage("icons/obj16/metercharticon.gif"); //$NON-NLS-1$
		super.chartTitle = Messages.getString("MeterChart.Txt.DefaultMeterChartTitle"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
	 */
	public String getName() {
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
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
		return new HelpContentImpl(TYPE_LITERAL, Messages.getString("MeterChart.Txt.HelpText")); //$NON-NLS-1$
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
		// Do not respond to requests for unknown orientations
		if (!orientation.equals(Orientation.VERTICAL_LITERAL)) {
			return vSubTypes;
		}
		if (sDimension.equals(TWO_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName())) {
			imgStandard = UIHelper.getImage("icons/wizban/meterchartimage.gif"); //$NON-NLS-1$
			imgSuperimposed = UIHelper.getImage("icons/wizban/meterchartsuperimposedimage.gif"); //$NON-NLS-1$

			vSubTypes.add(new DefaultChartSubTypeImpl(STANDARD_SUBTYPE_LITERAL, imgStandard, sStandardDescription,
					Messages.getString("MeterChart.SubType.Standard"))); //$NON-NLS-1$
			vSubTypes.add(new DefaultChartSubTypeImpl(SUPERIMPOSED_SUBTYPE_LITERAL, imgSuperimposed,
					sSuperimposedDescription, Messages.getString("MeterChart.SubType.Superimposed"))); //$NON-NLS-1$
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
		DialChart newChart = null;
		if (currentChart != null) {
			newChart = (DialChart) getConvertedChart(currentChart, sSubType, sDimension);
			if (newChart != null) {
				return newChart;
			}
		}
		newChart = (DialChart) DialChartImpl.createDefault();
		newChart.setType(TYPE_LITERAL);
		newChart.setSubType(sSubType);
		ChartElementUtil.setEObjectAttribute(newChart, "dimension", //$NON-NLS-1$
				getDimensionFor(sDimension), sDimension == null);

		newChart.setDialSuperimposition(sSubType.equals(SUPERIMPOSED_SUBTYPE_LITERAL));

		SeriesDefinition sdX = SeriesDefinitionImpl.createDefault();
		Series categorySeries = SeriesImpl.createDefault();
		sdX.getSeries().add(categorySeries);
		sdX.getQuery().setDefinition("Base Series"); //$NON-NLS-1$

		SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
		DialSeries valueSeries = (DialSeries) DialSeriesImpl.createDefault();
		valueSeries.setSeriesIdentifier("valueSeriesIdentifier"); //$NON-NLS-1$
		sdY.getSeries().add(valueSeries);

		sdX.getSeriesDefinitions().add(sdY);

		newChart.getSeriesDefinitions().add(sdX);

		addSampleData(newChart);
		return newChart;
	}

	private void addSampleData(Chart newChart) {
		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		sd.getBaseSampleData().clear();
		sd.getOrthogonalSampleData().clear();

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("A, B, C"); //$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
		oSample.setDataSetRepresentation("5, 4, 12"); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		newChart.setSampleData(sd);
	}

	private Chart getConvertedChart(Chart currentChart, String sNewSubType, String sNewDimension) {
		Chart helperModel = currentChart.copyInstance();
		helperModel.eAdapters().addAll(currentChart.eAdapters());
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance().cacheSeries(ChartUIUtil.getAllOrthogonalSeriesDefinitions(helperModel));
		IChartType oldType = ChartUIUtil.getChartType(currentChart.getType());
		if (currentChart instanceof ChartWithAxes) {
			if (!ChartPreviewPainter.isLivePreviewActive()) {
				helperModel.setSampleData(getConvertedSampleData(helperModel.getSampleData(),
						((ChartWithAxes) currentChart).getAxes().get(0).getType(), AxisType.LINEAR_LITERAL));
			}

			// Create a new instance of the correct type and set initial
			// properties
			currentChart = DialChartImpl.createDefault();
			copyChartProperties(helperModel, currentChart);
			currentChart.setType(TYPE_LITERAL);
			currentChart.setSubType(sNewSubType);
			ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
					getDimensionFor(sNewDimension), sNewDimension == null);
			((DialChart) currentChart).setDialSuperimposition(false);

			// Copy series definitions from old chart
			((ChartWithoutAxes) currentChart).getSeriesDefinitions()
					.add(((ChartWithAxes) helperModel).getAxes().get(0).getSeriesDefinitions().get(0));
			Vector<SeriesDefinition> vOSD = new Vector<SeriesDefinition>();

			// Only convert series in primary orthogonal axis.
			Axis primaryOrthogonalAxis = ((ChartWithAxes) helperModel)
					.getPrimaryOrthogonalAxis(((ChartWithAxes) helperModel).getAxes().get(0));
			EList<SeriesDefinition> osd = primaryOrthogonalAxis.getSeriesDefinitions();
			for (int j = 0; j < osd.size(); j++) {
				SeriesDefinition sd = osd.get(j);
				Series series = sd.getDesignTimeSeries();
				sd.getSeries().clear();
				sd.getSeries().add(getConvertedSeries(series, j));
				vOSD.add(sd);
			}

			((ChartWithoutAxes) currentChart).getSeriesDefinitions().get(0).getSeriesDefinitions().clear();
			((ChartWithoutAxes) currentChart).getSeriesDefinitions().get(0).getSeriesDefinitions().addAll(vOSD);

			Text title = currentChart.getTitle().getLabel().getCaption();
			if (title.getValue() != null && (title.getValue().trim().length() == 0
					|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
				title.setValue(getDefaultTitle());
			}
		} else if (currentChart instanceof ChartWithoutAxes) {
			if (currentChart.getType().equals(TYPE_LITERAL)) {
				currentChart.setSubType(sNewSubType);
				((DialChart) currentChart).setDialSuperimposition(sNewSubType.equals(SUPERIMPOSED_SUBTYPE_LITERAL));
				ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
						getDimensionFor(sNewDimension), sNewDimension == null);
			} else {
				// Create a new instance of the correct type and set initial
				// properties
				currentChart = DialChartImpl.createDefault();
				copyChartProperties(helperModel, currentChart);
				currentChart.setType(TYPE_LITERAL);
				currentChart.setSubType(sNewSubType);
				ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
						getDimensionFor(sNewDimension), sNewDimension == null);

				((DialChart) currentChart).setDialSuperimposition(sNewSubType.equals(SUPERIMPOSED_SUBTYPE_LITERAL));

				// Clear existing series definitions
				((ChartWithoutAxes) currentChart).getSeriesDefinitions().clear();

				// Copy series definitions
				((ChartWithoutAxes) currentChart).getSeriesDefinitions()
						.add(((ChartWithoutAxes) helperModel).getSeriesDefinitions().get(0));

				// Update the series
				EList<SeriesDefinition> seriesdefinitions = ((ChartWithoutAxes) currentChart).getSeriesDefinitions()
						.get(0).getSeriesDefinitions();
				for (int j = 0; j < seriesdefinitions.size(); j++) {
					Series series = seriesdefinitions.get(j).getDesignTimeSeries();
					series = getConvertedSeries(series, j);

					// Clear any existing series
					seriesdefinitions.get(j).getSeries().clear();
					// Add the new series
					seriesdefinitions.get(j).getSeries().add(series);
				}

				Text title = currentChart.getTitle().getLabel().getCaption();
				if (title.getValue() != null && title.getValue().trim().length() != 0
						&& title.getValue().trim().equals(oldType.getDefaultTitle().trim())) {
					title.setValue(getDefaultTitle());
				}
			}
		} else {
			return null;
		}
		return currentChart;
	}

	private Series getConvertedSeries(Series series, int seriesIndex) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		DialSeries dialseries = (DialSeries) ChartCacheManager.getInstance().findSeries(DialSeriesImpl.class.getName(),
				seriesIndex);
		if (dialseries == null) {
			dialseries = (DialSeries) DialSeriesImpl.createDefault();
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, dialseries);

		return dialseries;
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
		return ChartDimension.TWO_DIMENSIONAL_LITERAL;
	}

	public ISelectDataComponent getBaseUI(Chart chart, ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle) {
		DefaultBaseSeriesComponent component = new DefaultBaseSeriesComponent(
				ChartUIUtil.getBaseSeriesDefinitions(chart).get(0), context, sTitle);
		component.setLabelText(Messages.getString("PieBaseSeriesComponent.Label.CategoryDefinition")); //$NON-NLS-1$
		component.setTooltipWhenBlank(Messages.getString("MeterChart.Tooltip.InputExpression")); //$NON-NLS-1$
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("MeterChart.Txt.DisplayName"); //$NON-NLS-1$
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
			return DialSeriesImpl.create();
		} else {
			return DialSeriesImpl.createDefault();
		}
	}

	public String getValueDefinitionName() {
		return Messages.getString("DialBottomAreaComponent.Label.GaugeValueDefinition"); //$NON-NLS-1$
	}

	@Override
	public boolean isChartWithAxes() {
		return false;
	}
}
