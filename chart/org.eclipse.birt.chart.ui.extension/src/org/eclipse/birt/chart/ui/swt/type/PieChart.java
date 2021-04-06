/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
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
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
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
 * PieChart
 */
public class PieChart extends DefaultChartTypeImpl {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_PIE;

	protected static final String STANDARD_SUBTYPE_LITERAL = "Standard"; //$NON-NLS-1$

	public PieChart() {
		super.chartTitle = Messages.getString("PieChart.Txt.DefaultPieChartTitle"); //$NON-NLS-1$
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
		return UIHelper.getImage("icons/obj16/piecharticon.gif"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	public IHelpContent getHelp() {
		return new HelpContentImpl(TYPE_LITERAL, Messages.getString("PieChart.Txt.HelpText")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(
	 * java.lang.String)
	 */
	public Collection<IChartSubType> getChartSubtypes(String sDimension, Orientation orientation) {
		Vector<IChartSubType> vSubTypes = new Vector<IChartSubType>();
		// Do not respond to requests for unknown orientations
		if (!orientation.equals(Orientation.VERTICAL_LITERAL)) {
			return vSubTypes;
		}
		vSubTypes.add(new DefaultChartSubTypeImpl(STANDARD_SUBTYPE_LITERAL, getImageForSubtype(sDimension),
				getDescriptionForSubtype(sDimension), getDisplayNameForSubtype(sDimension)));
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public Chart getModel(String sSubType, Orientation orientation, String sDimension, Chart currentChart) {
		ChartWithoutAxes newChart = null;
		if (currentChart != null) {
			newChart = (ChartWithoutAxes) getConvertedChart(currentChart, sSubType, sDimension);
			if (newChart != null) {
				return newChart;
			}
		}
		newChart = ChartWithoutAxesImpl.createDefault();
		newChart.setType(getName());
		newChart.setSubType(sSubType);
		ChartElementUtil.setEObjectAttribute(newChart, "dimension", //$NON-NLS-1$
				getDimensionFor(sDimension), sDimension == null);

		SeriesDefinition sdX = SeriesDefinitionImpl.createDefault();
		Series categorySeries = SeriesImpl.createDefault();
		sdX.getSeries().add(categorySeries);
		sdX.getQuery().setDefinition("Base Series"); //$NON-NLS-1$

		SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
		Series valueSeries = getSeries(false);
		valueSeries.setSeriesIdentifier("valueSeriesIdentifier"); //$NON-NLS-1$
		((PieSeries) valueSeries).getTitle().getCaption().setValue("valueSeries"); //$NON-NLS-1$
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
		oSample.setDataSetRepresentation("5,4,12"); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		/*
		 * OrthogonalSampleData oSample2 =
		 * DataFactory.eINSTANCE.createOrthogonalSampleData();
		 * oSample2.setDataSetRepresentation("7,22,14");
		 * oSample2.setSeriesDefinitionIndex(0);
		 * sd.getOrthogonalSampleData().add(oSample2);
		 */
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
			currentChart = ChartWithoutAxesImpl.createDefault();
			copyChartProperties(helperModel, currentChart);
			currentChart.setType(getName());
			currentChart.setSubType(sNewSubType);
			ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
					getDimensionFor(sNewDimension), sNewDimension == null);

			if (helperModel.isSetSeriesThickness()) {
				currentChart.setSeriesThickness(helperModel.getSeriesThickness());
			}

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

			// Set the legend item type t Categories to have the chart behave as
			// expected by default.
			currentChart.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
			Text title = currentChart.getTitle().getLabel().getCaption();
			if (title.getValue() != null && (title.getValue().trim().length() == 0
					|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
				title.setValue(getDefaultTitle());
			}
		} else if (currentChart instanceof ChartWithoutAxes) {
			if (currentChart.getType().equals(getName())) {
				currentChart.setSubType(sNewSubType);
				ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
						getDimensionFor(sNewDimension), sNewDimension == null);
			} else {
				// Create a new instance of the correct type and set initial
				// properties
				currentChart = ChartWithoutAxesImpl.createDefault();
				copyChartProperties(helperModel, currentChart);
				currentChart.setType(getName());
				currentChart.setSubType(sNewSubType);
				ChartElementUtil.setEObjectAttribute(currentChart, "dimension", //$NON-NLS-1$
						getDimensionFor(sNewDimension), sNewDimension == null);

				// Clear existing series definitions
				((ChartWithoutAxes) currentChart).getSeriesDefinitions().clear();

				// Copy series definitions
				((ChartWithoutAxes) currentChart).getSeriesDefinitions()
						.addAll(((ChartWithoutAxes) helperModel).getSeriesDefinitions());

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

				currentChart.getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
				Text title = currentChart.getTitle().getLabel().getCaption();
				if (title.getValue() != null && (title.getValue().trim().length() == 0
						|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
					title.setValue(getDefaultTitle());
				}
			}
		} else {
			return null;
		}
		return currentChart;
	}

	protected Series getConvertedSeries(Series series, int seriesIndex) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		PieSeries pieseries = (PieSeries) ChartCacheManager.getInstance().findSeries(PieSeriesImpl.class.getName(),
				seriesIndex);
		if (pieseries == null) {
			pieseries = (PieSeries) getSeries(false);
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, pieseries);

		return pieseries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions ()
	 */
	public String[] getSupportedDimensions() {
		return new String[] { TWO_DIMENSION_TYPE, TWO_DIMENSION_WITH_DEPTH_TYPE };
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
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition ()
	 */
	public boolean supportsTransposition() {
		return false;
	}

	private ChartDimension getDimensionFor(String sDimension) {
		if (sDimension == null || sDimension.equals(TWO_DIMENSION_TYPE)) {
			return ChartDimension.TWO_DIMENSIONAL_LITERAL;
		}
		return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
	}

	public ISelectDataComponent getBaseUI(Chart chart, ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle) {
		DefaultBaseSeriesComponent component = new DefaultBaseSeriesComponent(
				ChartUIUtil.getBaseSeriesDefinitions(chart).get(0), context, sTitle);
		component.setLabelText(Messages.getString("PieBaseSeriesComponent.Label.CategoryDefinition")); //$NON-NLS-1$
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("PieChart.Txt.DisplayName"); //$NON-NLS-1$
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
	public Series getSeries(boolean needInitializing) {
		if (needInitializing) {
			PieSeries pieseries = (PieSeries) PieSeriesImpl.create();
			pieseries.setExplosion(0);
			pieseries.setLeaderLineLength(10.0);
			pieseries.setLeaderLineStyle(LeaderLineStyle.FIXED_LENGTH_LITERAL);
			return pieseries;
		} else {
			PieSeries pieseries = (PieSeries) PieSeriesImpl.createDefault();
			return pieseries;
		}
	}

	protected String getDescriptionForSubtype(String sDimension) {
		return Messages.getString("PieChart.Txt.Description"); //$NON-NLS-1$
	}

	protected Image getImageForSubtype(String sDimension) {
		if (sDimension.equals(TWO_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName())) {
			return UIHelper.getImage("icons/wizban/piechartimage.gif"); //$NON-NLS-1$
		}
		if (sDimension.equals(TWO_DIMENSION_WITH_DEPTH_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName())) {
			return UIHelper.getImage("icons/wizban/piechartwithdepthimage.gif"); //$NON-NLS-1$
		}
		return null;
	}

	protected String getDisplayNameForSubtype(String sDimension) {
		return Messages.getString("PieChart.SubType.Standard"); //$NON-NLS-1$
	}

	public String getValueDefinitionName() {
		return Messages.getString("PieLeftAreaComponent.Label.SliceSizeDefinition"); //$NON-NLS-1$
	}

	@Override
	public boolean isChartWithAxes() {
		return false;
	}
}