/*******************************************************************************
 * Copyright (c) Oct 22, 2004 Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation {ADD
 * SUBSEQUENT AUTHOR & CONTRIBUTION}
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
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
 * LineChart
 */
public class LineChart extends DefaultChartTypeImpl {

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = ChartUIConstants.TYPE_LINE;

	protected static final String STACKED_SUBTYPE_LITERAL = "Stacked"; //$NON-NLS-1$

	protected static final String PERCENTSTACKED_SUBTYPE_LITERAL = "Percent Stacked"; //$NON-NLS-1$

	protected static final String OVERLAY_SUBTYPE_LITERAL = "Overlay"; //$NON-NLS-1$

	public LineChart() {
		super.chartTitle = Messages.getString("LineChart.Txt.DefaultLineChartTitle"); //$NON-NLS-1$ ;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getName()
	 */
	@Override
	public String getName() {
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getImage()
	 */
	@Override
	public Image getImage() {
		return UIHelper.getImage("icons/obj16/linecharticon.gif"); //$NON-NLS-1$ ;
	}

	/**
	 * Returns the icons for subtypes.
	 *
	 * @param sDimension
	 * @param orientation
	 * @param subtype
	 * @return
	 */
	protected Image getImageForSubtype(String sDimension, Orientation orientation, String subtype) {
		String imagePath = null;
		if (sDimension.equals(TWO_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName())) {
			if (subtype.equals(OVERLAY_SUBTYPE_LITERAL)) {
				if (orientation == Orientation.VERTICAL_LITERAL) {
					imagePath = "icons/wizban/sidebysidelinechartimage.gif"; //$NON-NLS-1$
				} else {
					imagePath = "icons/wizban/horizontalsidebysidelinechartimage.gif"; //$NON-NLS-1$
				}
			} else if (subtype.equals(STACKED_SUBTYPE_LITERAL)) {
				if (orientation == Orientation.VERTICAL_LITERAL) {
					imagePath = "icons/wizban/stackedlinechartimage.gif"; //$NON-NLS-1$
				} else {
					imagePath = "icons/wizban/horizontalstackedlinechartimage.gif"; //$NON-NLS-1$
				}
			} else if (subtype.equals(PERCENTSTACKED_SUBTYPE_LITERAL)) {
				if (orientation == Orientation.VERTICAL_LITERAL) {
					imagePath = "icons/wizban/percentstackedlinechartimage.gif"; //$NON-NLS-1$
				} else {
					imagePath = "icons/wizban/horizontalpercentstackedlinechartimage.gif"; //$NON-NLS-1$
				}
			}
		} else if (sDimension.equals(THREE_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.THREE_DIMENSIONAL_LITERAL.getName())) {
			imagePath = "icons/wizban/sidebysidelinechart3dimage.gif"; //$NON-NLS-1$
		}
		if (imagePath != null) {
			return UIHelper.getImage(imagePath);
		}
		return null;
	}

	protected String getDescriptionForSubtype(String subtypeLiteral) {
		if (OVERLAY_SUBTYPE_LITERAL.equals(subtypeLiteral)) {
			return Messages.getString("LineChart.Txt.OverlayDescription"); //$NON-NLS-1$
		}
		if (STACKED_SUBTYPE_LITERAL.equals(subtypeLiteral)) {
			return Messages.getString("LineChart.Txt.StackedDescription"); //$NON-NLS-1$
		}
		if (PERCENTSTACKED_SUBTYPE_LITERAL.equals(subtypeLiteral)) {
			return Messages.getString("LineChart.Txt.PercentStackedDescription"); //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	@Override
	public IHelpContent getHelp() {
		return new HelpContentImpl(TYPE_LITERAL, Messages.getString("LineChart.Txt.HelpText")); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(
	 * java.lang.String)
	 */
	@Override
	public Collection<IChartSubType> getChartSubtypes(String sDimension, Orientation orientation) {
		Vector<IChartSubType> vSubTypes = new Vector<>();
		if (sDimension.equals(TWO_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.TWO_DIMENSIONAL_LITERAL.getName())) {
			vSubTypes.add(new DefaultChartSubTypeImpl(OVERLAY_SUBTYPE_LITERAL,
					getImageForSubtype(sDimension, orientation, OVERLAY_SUBTYPE_LITERAL),
					getDescriptionForSubtype(OVERLAY_SUBTYPE_LITERAL),
					Messages.getString("LineChart.SubType.Overlay"))); //$NON-NLS-1$
			if (isStackedSupported()) {
				vSubTypes.add(new DefaultChartSubTypeImpl(STACKED_SUBTYPE_LITERAL,
						getImageForSubtype(sDimension, orientation, STACKED_SUBTYPE_LITERAL),
						getDescriptionForSubtype(STACKED_SUBTYPE_LITERAL),
						Messages.getString("LineChart.SubType.Stacked"))); //$NON-NLS-1$
			}
			if (isPercentStackedSupported()) {
				vSubTypes.add(new DefaultChartSubTypeImpl(PERCENTSTACKED_SUBTYPE_LITERAL,
						getImageForSubtype(sDimension, orientation, PERCENTSTACKED_SUBTYPE_LITERAL),
						getDescriptionForSubtype(PERCENTSTACKED_SUBTYPE_LITERAL),
						Messages.getString("LineChart.SubType.PercentStacked"))); //$NON-NLS-1$
			}
		} else if (sDimension.equals(THREE_DIMENSION_TYPE)
				|| sDimension.equals(ChartDimension.THREE_DIMENSIONAL_LITERAL.getName())) {
			vSubTypes.add(new DefaultChartSubTypeImpl(OVERLAY_SUBTYPE_LITERAL,
					getImageForSubtype(sDimension, orientation, OVERLAY_SUBTYPE_LITERAL),
					getDescriptionForSubtype(OVERLAY_SUBTYPE_LITERAL),
					Messages.getString("LineChart.SubType.Overlay"))); //$NON-NLS-1$
		}
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	@Override
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
				ChartUIUtil.getDimensionType(sDimension), sDimension == null);
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
		if (sSubType.equalsIgnoreCase(STACKED_SUBTYPE_LITERAL)) {
			SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
			Series valueSeries = getSeries(false);
			valueSeries.setStacked(true);
			sdY.getSeries().add(valueSeries);
			yAxis.getSeriesDefinitions().add(sdY);
		} else if (sSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL)) {
			SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
			Series valueSeries = getSeries(false);
			valueSeries.setStacked(true);
			sdY.getSeries().add(valueSeries);
			yAxis.getSeriesDefinitions().add(sdY);
		} else if (sSubType.equalsIgnoreCase(OVERLAY_SUBTYPE_LITERAL)) {
			SeriesDefinition sdY = SeriesDefinitionImpl.createDefault();
			Series valueSeries = getSeries(false);
			sdY.getSeries().add(valueSeries);
			yAxis.getSeriesDefinitions().add(sdY);
		}

		if (sDimension != null && sDimension.equals(THREE_DIMENSION_TYPE)) {
			newChart.setRotation(Rotation3DImpl.createDefault(new Angle3D[] { Angle3DImpl.createDefault(-20, 45, 0) }));

			newChart.getPrimaryBaseAxes()[0].getAncillaryAxes().clear();

			Axis zAxisAncillary = AxisImpl.createDefault(Axis.ANCILLARY_BASE);
			zAxisAncillary.getOrigin().setValue(NumberDataElementImpl.create(0));
			newChart.getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisAncillary);

			SeriesDefinition sdZ = SeriesDefinitionImpl.createDefault();
			sdZ.getSeries().add(SeriesImpl.createDefault());
			zAxisAncillary.getSeriesDefinitions().add(sdZ);
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
		sdBase.setDataSetRepresentation("A, B, C"); //$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData();
		oSample.setDataSetRepresentation("5,-4,12"); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(oSample);

		if (newChart.getDimension() == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
			BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData();
			sdAncillary.setDataSetRepresentation("Series 1"); //$NON-NLS-1$
			sd.getAncillarySampleData().add(sdAncillary);
		}

		newChart.setSampleData(sd);
	}

	private Chart getConvertedChart(Chart currentChart, String sNewSubType, Orientation newOrientation,
			String sNewDimension) {
		Chart helperModel = currentChart.copyInstance();
		helperModel.eAdapters().addAll(currentChart.eAdapters());
		ChartDimension oldDimension = currentChart.getDimension();
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance().cacheSeries(ChartUIUtil.getAllOrthogonalSeriesDefinitions(helperModel));
		IChartType oldType = ChartUIUtil.getChartType(currentChart.getType());
		if ((currentChart instanceof ChartWithAxes)) {
			if (currentChart.getType().equals(TYPE_LITERAL)) {
				currentChart.setSubType(sNewSubType);
				EList<Axis> axes = ((ChartWithAxes) currentChart).getAxes().get(0).getAssociatedAxes();
				for (int i = 0, seriesIndex = 0; i < axes.size(); i++) {
					if (sNewSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL)) {
						if (!ChartPreviewPainter.isLivePreviewActive() && !isNumbericAxis(axes.get(i))) {
							axes.get(i).setType(AxisType.LINEAR_LITERAL);
						}
						axes.get(i).setPercent(true);
					} else {
						axes.get(i).setPercent(false);
					}
					EList<SeriesDefinition> seriesdefinitions = axes.get(i).getSeriesDefinitions();
					Series firstSeries = seriesdefinitions.get(0).getDesignTimeSeries();
					for (int j = 0; j < seriesdefinitions.size(); j++) {
						Series series = seriesdefinitions.get(j).getDesignTimeSeries();
						if ((sNewSubType.equalsIgnoreCase(STACKED_SUBTYPE_LITERAL)
								|| sNewSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL))) {
							if (j != 0) {
								series = getConvertedSeriesAsFirst(series, seriesIndex, firstSeries);
							}
							seriesIndex++;
							if (!ChartPreviewPainter.isLivePreviewActive() && axes.get(i).isSetType()
									&& !isNumbericAxis(axes.get(i))) {
								axes.get(i).setType(AxisType.LINEAR_LITERAL);
							}
							if (series.canBeStacked()) {
								series.setStacked(true);
							}
							seriesdefinitions.get(j).getSeries().clear();
							seriesdefinitions.get(j).getSeries().add(series);
						} else {
							series.setStacked(false);
						}
					}
				}
			} else {
				currentChart.setType(TYPE_LITERAL);
				currentChart.setSubType(sNewSubType);
				Text title = currentChart.getTitle().getLabel().getCaption();
				if (title.getValue() != null && (title.getValue().trim().length() == 0
						|| title.getValue().trim().equals(oldType.getDefaultTitle().trim()))) {
					title.setValue(getDefaultTitle());
				}

				List<AxisType> axisTypes = new ArrayList<>();
				EList<Axis> axes = ((ChartWithAxes) currentChart).getAxes().get(0).getAssociatedAxes();

				for (int i = 0, seriesIndex = 0; i < axes.size(); i++) {
					if (!ChartPreviewPainter.isLivePreviewActive() && axes.get(i).isSetType()
							&& !isNumbericAxis(axes.get(i))) {
						axes.get(i).setType(AxisType.LINEAR_LITERAL);
					}
					axes.get(i).setPercent(sNewSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL));
					EList<SeriesDefinition> seriesdefinitions = axes.get(i).getSeriesDefinitions();
					for (int j = 0; j < seriesdefinitions.size(); j++) {
						Series series = seriesdefinitions.get(j).getDesignTimeSeries();
						series = getConvertedSeries(series, seriesIndex++);
						if (!ChartPreviewPainter.isLivePreviewActive() && axes.get(i).isSetType()
								&& !isNumbericAxis(axes.get(i))) {
							axes.get(i).setType(AxisType.LINEAR_LITERAL);
						}
						boolean isStacked = (sNewSubType.equalsIgnoreCase(STACKED_SUBTYPE_LITERAL)
								|| sNewSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL));
						series.setStacked(isStacked);
						seriesdefinitions.get(j).getSeries().clear();
						seriesdefinitions.get(j).getSeries().add(series);
						axisTypes.add(axes.get(i).getType());
					}
				}

				currentChart.setSampleData(getConvertedSampleData(currentChart.getSampleData(),
						((ChartWithAxes) currentChart).getAxes().get(0).getType(), axisTypes));
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
					ChartUIUtil.getDimensionType(sNewDimension), sNewDimension == null);
			try {
				ChartElementUtil.setDefaultValue(((ChartWithAxes) currentChart).getAxes().get(0), "categoryAxis", //$NON-NLS-1$
						true);
			} catch (ChartException e) {
				// Do nothing.
			}

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
				yAxis.getSeriesDefinitions().addAll(xAxis.getSeriesDefinitions().get(0).getSeriesDefinitions());

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
					if ((sNewSubType.equalsIgnoreCase(STACKED_SUBTYPE_LITERAL)
							|| sNewSubType.equalsIgnoreCase(PERCENTSTACKED_SUBTYPE_LITERAL))) {
						series.setStacked(true);
					} else {
						series.setStacked(false);
					}
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
				ChartUIUtil.getDimensionType(sNewDimension), sNewDimension == null);

		if (sNewDimension != null && sNewDimension.equals(THREE_DIMENSION_TYPE)
				&& ChartUIUtil.getDimensionType(sNewDimension) != oldDimension) {
			((ChartWithAxes) currentChart)
					.setRotation(Rotation3DImpl.createDefault(new Angle3D[] { Angle3DImpl.createDefault(-20, 45, 0) }));

			((ChartWithAxes) currentChart).getPrimaryBaseAxes()[0].getAncillaryAxes().clear();

			Axis zAxisAncillary = AxisImpl.createDefault(Axis.ANCILLARY_BASE);
			zAxisAncillary.getOrigin().setValue(NumberDataElementImpl.create(0));
			((ChartWithAxes) currentChart).getPrimaryBaseAxes()[0].getAncillaryAxes().add(zAxisAncillary);

			SeriesDefinition sdZ = SeriesDefinitionImpl.createDefault();
			sdZ.getSeries().add(SeriesImpl.createDefault());
			zAxisAncillary.getSeriesDefinitions().add(sdZ);

			if (currentChart.getSampleData().getAncillarySampleData().isEmpty()) {
				BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData();
				sdAncillary.setDataSetRepresentation("Series 1"); //$NON-NLS-1$
				currentChart.getSampleData().getAncillarySampleData().add(sdAncillary);
			}

			EList<SeriesDefinition> seriesdefinitions = ChartUIUtil.getOrthogonalSeriesDefinitions(currentChart, 0);
			for (int j = 0; j < seriesdefinitions.size(); j++) {
				Series series = seriesdefinitions.get(j).getDesignTimeSeries();
				series.setStacked(false);// Stacked is unsupported in 3D
			}
		}

		// Restore label position for different sub type of chart.
		ChartUIUtil.restoreLabelPositionFromCache(currentChart);

		return currentChart;
	}

	private boolean isNumbericAxis(Axis axis) {
		return (axis.getType().getValue() == AxisType.LINEAR) || (axis.getType().getValue() == AxisType.LOGARITHMIC);
	}

	private Series getConvertedSeries(Series series, int seriesIndex) {
		// Do not convert base series
		if (series.getClass().getName().equals(SeriesImpl.class.getName())) {
			return series;
		}

		LineSeries lineseries = (LineSeries) ChartCacheManager.getInstance().findSeries(LineSeriesImpl.class.getName(),
				seriesIndex);
		if (lineseries == null) {
			lineseries = (LineSeries) getSeries(false);
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes(series, lineseries);

		return lineseries;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions ()
	 */
	@Override
	public String[] getSupportedDimensions() {
		return new String[] { TWO_DIMENSION_TYPE, THREE_DIMENSION_TYPE };
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
		return true;
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
		if (ChartUIUtil.getDimensionType(dimension) == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
			return false;
		}

		return supportsTransposition();
	}

	@Override
	public ISelectDataComponent getBaseUI(Chart chart, ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle) {
		return new DefaultBaseSeriesComponent(ChartUIUtil.getBaseSeriesDefinitions(chart).get(0), context, sTitle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return Messages.getString("LineChart.Txt.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	@Override
	public Series getSeries() {
		return getSeries(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getSeries(boolean)
	 */
	@Override
	public Series getSeries(boolean needInitializing) {
		if (needInitializing) {
			LineSeries series = (LineSeries) LineSeriesImpl.create();
			series.getMarkers().get(0).setVisible(true);
			series.setPaletteLineColor(true);
			return series;
		} else {
			LineSeries series = (LineSeries) LineSeriesImpl.createDefault();
			return series;
		}
	}

	@Override
	public boolean canCombine() {
		return true;
	}

	protected boolean isStackedSupported() {
		return true;
	}

	protected boolean isPercentStackedSupported() {
		return true;
	}

	@Override
	public boolean canExpand() {
		return true;
	}
}
