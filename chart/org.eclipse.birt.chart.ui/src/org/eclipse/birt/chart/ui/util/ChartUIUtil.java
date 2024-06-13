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

package org.eclipse.birt.chart.ui.util;

import java.text.Collator;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainterBase;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * ChartUIUtil
 */
public class ChartUIUtil {
	public static final String FONT_AUTO = Messages.getString("ChartUIUtil.Font.Auto"); //$NON-NLS-1$

	public static final String[] FONT_SIZES = { ChartUIUtil.FONT_AUTO, "9", //$NON-NLS-1$
			"10", //$NON-NLS-1$
			"12", //$NON-NLS-1$
			"14", //$NON-NLS-1$
			"16", //$NON-NLS-1$
			"18", //$NON-NLS-1$
			"24", //$NON-NLS-1$
			"36" //$NON-NLS-1$
	};

	private static IDisplayServer swtDisplayServer = null;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui/swt"); //$NON-NLS-1$

	private static HashMap<String, ISeriesUIProvider> htSeriesAttributeUIProviders = new HashMap<>();

	private static LinkedHashMap<String, IChartType> htTypes = null;

	static {
		// Get the SWT device
		try {
			swtDisplayServer = PluginSettings.instance().getDisplayServer("ds.SWT"); //$NON-NLS-1$
		} catch (ChartException e) {
			logger.log(e);
		}

		// Get collection of registered UI Providers
		Collection<ISeriesUIProvider> cRegisteredEntries = ChartUIExtensionsImpl.instance()
				.getSeriesUIComponents(ChartWizardContext.class.getSimpleName());
		Iterator<ISeriesUIProvider> iterEntries = cRegisteredEntries.iterator();
		while (iterEntries.hasNext()) {
			ISeriesUIProvider provider = iterEntries.next();
			String sSeries = provider.getSeriesClass();
			htSeriesAttributeUIProviders.put(sSeries, provider);
		}
	}

	/** Initializes locales. */
	public final static TreeMap<String, ULocale> LOCALE_TABLE = new TreeMap<>(Collator.getInstance());

	static {
		// Initialize the locale mapping table
		ULocale[] locales = ULocale.getAvailableLocales();
		if (locales != null) {
			for (int i = 0; i < locales.length; i++) {
				ULocale locale = locales[i];
				if (locale != null) {
					LOCALE_TABLE.put(locale.getDisplayName(), locale);
				}
			}
		}
	}

	public static IDisplayServer getDisplayServer() {
		return swtDisplayServer;
	}

	public static void setBackgroundColor(Control control, boolean selected, Color color) {
		if (control != null && !control.isDisposed()) {
			if (selected) {
				control.setBackground(color);
			} else {
				control.setBackground(null);
			}
		}
	}

	public static Composite createCompositeWrapper(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		cmp.setLayout(gridLayout);
		return cmp;
	}

	public static Query getDataQuery(SeriesDefinition seriesDefn, int queryIndex) {
		return ChartUtil.getDataQuery(seriesDefn, queryIndex);
	}

	/**
	 * Create a row expression based on column name.
	 *
	 * @param colName
	 */
	public static String getExpressionString(String colName) {
		return ExpressionUtil.createJSRowExpression(colName);
	}

	/**
	 * Returns the default number format instance for default locale.
	 *
	 * @return the default number format
	 */
	public static NumberFormat getDefaultNumberFormatInstance() {
		NumberFormat numberFormat = NumberFormat.getInstance(ULocale.getDefault());
		// fix icu limitation which only allow 3 fraction digits as maximum by
		// default. ?100 is enough.
		numberFormat.setMaximumFractionDigits(100);
		return numberFormat;
	}

	public static EList<SeriesDefinition> getBaseSeriesDefinitions(Chart chart) {
		return ChartUtil.getBaseSeriesDefinitions(chart);
	}

	public static int getOrthogonalAxisNumber(Chart chart) {
		return ChartUtil.getOrthogonalAxisNumber(chart);
	}

	/**
	 * Return specified axis definitions.
	 *
	 * @param chart     chart
	 * @param axisIndex If chart is without axis type, it always return all
	 *                  orthogonal series definition.
	 * @return specified axis definitions or all series definitions
	 */
	public static EList<SeriesDefinition> getOrthogonalSeriesDefinitions(Chart chart, int axisIndex) {
		return ChartUtil.getOrthogonalSeriesDefinitions(chart, axisIndex);
	}

	/**
	 * Return specified axis definitions or all series definitions. Remember return
	 * type is ArrayList, not EList, no event is fired when adding or removing an
	 * element.
	 *
	 * @param chart chart
	 * @return specified axis definitions or all series definitions
	 */
	public static List<SeriesDefinition> getAllOrthogonalSeriesDefinitions(Chart chart) {
		return ChartUtil.getAllOrthogonalSeriesDefinitions(chart);
	}

	public static String getStockTitle(int index) {
		switch (index) {
		case 0:
			return Messages.getString("ChartUIUtil.StockExp.High"); //$NON-NLS-1$
		case 1:
			return Messages.getString("ChartUIUtil.StockExp.Low"); //$NON-NLS-1$
		case 2:
			return Messages.getString("ChartUIUtil.StockExp.Open"); //$NON-NLS-1$
		case 3:
			return Messages.getString("ChartUIUtil.StockExp.Close"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public static String getGanttTitle(int index) {
		switch (index) {
		case 0:
			return Messages.getString("ChartUIUtil.GanttExp.Start"); //$NON-NLS-1$
		case 1:
			return Messages.getString("ChartUIUtil.GanttExp.End"); //$NON-NLS-1$
		case 2:
			return Messages.getString("ChartUIUtil.GanttExp.Label"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public static String getBubbleTitle(int index) {
		switch (index) {
		case 0:
			return Messages.getString("ChartUIUtil.BubbleExp.Label"); //$NON-NLS-1$
		case 1:
			return Messages.getString("ChartUIUtil.BubbleExp.Size"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public static String getDifferenceTitle(int index) {
		switch (index) {
		case 0:
			return Messages.getString("ChartUIUtil.DifferenceExp.Postive"); //$NON-NLS-1$
		case 1:
			return Messages.getString("ChartUIUtil.DifferenceExp.Negative"); //$NON-NLS-1$
		default:
			return ""; //$NON-NLS-1$
		}
	}

	public static Axis getAxisXForProcessing(ChartWithAxes chartWithAxis) {
		return chartWithAxis.getAxes().get(0);
	}

	public static Axis getAxisYForProcessing(ChartWithAxes chartWithAxis, int axisIndex) {
		return getAxisXForProcessing(chartWithAxis).getAssociatedAxes().get(axisIndex);
	}

	public static Axis getAxisZForProcessing(ChartWithAxes chartWithAxis) {
		return getAxisXForProcessing(chartWithAxis).getAncillaryAxes().get(0);
	}

	public static boolean is3DType(Chart chart) {
		return chart.getDimension().getValue() == ChartDimension.THREE_DIMENSIONAL;
	}

	public static int getFontRotation(FontDefinition font) {
		return font.isSetRotation() ? (int) font.getRotation() : 0;
	}

	public static String getFontName(FontDefinition font) {
		return font.getName() == null ? FONT_AUTO : font.getName();
	}

	public static TextAlignment getFontTextAlignment(FontDefinition font) {
		return font.getAlignment() == null ? TextAlignmentImpl.create() : font.getAlignment();
	}

	/**
	 * Checks all data definitions are bound
	 *
	 * @param chart chart model
	 */
	public static boolean checkDataBinding(Chart chart) {
		List<SeriesDefinition> sdList = ChartUIUtil.getBaseSeriesDefinitions(chart);
		if (!checkDataDefinition(sdList)) {
			return false;
		}
		for (int i = 0; i < ChartUIUtil.getOrthogonalAxisNumber(chart); i++) {
			sdList = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, i);
			if (!checkDataDefinition(sdList)) {
				return false;
			}
		}
		return true;
	}

	private static boolean checkDataDefinition(List<SeriesDefinition> sdList) {
		for (int i = 0; i < sdList.size(); i++) {
			Series series = sdList.get(i).getDesignTimeSeries();
			EList<Query> ddList = series.getDataDefinition();
			if (ddList.size() == 0) {
				return false;
			}

			// Only check valid index
			int[] validIndex = series.getDefinedDataDefinitionIndex();
			for (int j = 0; j < validIndex.length; j++) {
				int vi = validIndex[j];
				if (vi >= 0 && vi < ddList.size()) {
					String query = ddList.get(vi).getDefinition();
					if (query == null || query.length() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Synchronize runtime series with design series.
	 *
	 * @param chart
	 */
	public static void syncRuntimeSeries(Chart chart) {
		if (chart instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) chart;

			// !NO NEED TO SYNC BASE SERIES
			// SeriesDefinition sdBase = (SeriesDefinition) cwa.getBaseAxes(
			// )[0].getSeriesDefinitions( )
			// .get( 0 );
			// Series seDesignBase = sdBase.getDesignTimeSeries( );

			// dss = new DesignSeriesSynchronizer( seDesignBase,
			// sdBase.getRunTimeSeries( ) );
			// seDesignBase.eAdapters( ).add(0, dss );
			// synchronizers.add( dss );

			Axis[] axa = cwa.getOrthogonalAxes(cwa.getPrimaryBaseAxes()[0], true);
			int iOrthogonalSeriesDefinitionCount = 0;

			for (int i = 0; i < axa.length; i++) {
				EList<SeriesDefinition> elSD = axa[i].getSeriesDefinitions();
				for (int j = 0; j < elSD.size(); j++) {
					SeriesDefinition sd = elSD.get(j);
					Query qy = sd.getQuery();
					if (qy == null) {
						continue;
					}
					String sExpression = qy.getDefinition();
					if (sExpression == null || sExpression.length() == 0) {
						continue;
					}
					iOrthogonalSeriesDefinitionCount++;
				}
			}

			// SYNC ALL ORTHOGONAL SERIES
			for (int i = 0; i < axa.length; i++) {
				for (Iterator<SeriesDefinition> itr = axa[i].getSeriesDefinitions().iterator(); itr.hasNext();) {
					SeriesDefinition sdOrthogonal = itr.next();
					Series seDesignOrthogonal = sdOrthogonal.getDesignTimeSeries();

					List<Series> seRuntimes = sdOrthogonal.getRunTimeSeries();

					sdOrthogonal.getSeries().removeAll(seRuntimes);

					for (int j = 0; j < seRuntimes.size(); j++) {
						Series seRuntimeOrthogonal = seDesignOrthogonal.copyInstance();
						seRuntimeOrthogonal.setDataSet(seRuntimes.get(j).getDataSet());
						if (iOrthogonalSeriesDefinitionCount < 1) {
							seRuntimeOrthogonal.setSeriesIdentifier(seDesignOrthogonal.getSeriesIdentifier());
						} else {
							seRuntimeOrthogonal.setSeriesIdentifier(seRuntimes.get(j).getSeriesIdentifier());
						}
						sdOrthogonal.getSeries().add(seRuntimeOrthogonal);
					}
				}
			}
		} else if (chart instanceof ChartWithoutAxes) {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) chart;

			final SeriesDefinition sdBase = cwoa.getSeriesDefinitions().get(0);
			int iOrthogonalSeriesDefinitionCount = 0;

			EList<SeriesDefinition> elSD = sdBase.getSeriesDefinitions();
			for (int j = 0; j < elSD.size(); j++) {
				SeriesDefinition sd = elSD.get(j);
				Query qy = sd.getQuery();
				if (qy == null) {
					continue;
				}
				String sExpression = qy.getDefinition();
				if (sExpression == null || sExpression.length() == 0) {
					continue;
				}
				iOrthogonalSeriesDefinitionCount++;
			}

			// SYNC ALL ORTHOGONAL SERIES
			for (Iterator<SeriesDefinition> itr = elSD.iterator(); itr.hasNext();) {
				SeriesDefinition sdOrthogonal = itr.next();
				Series seDesignOrthogonal = sdOrthogonal.getDesignTimeSeries();

				List<Series> seRuntimes = sdOrthogonal.getRunTimeSeries();

				sdOrthogonal.getSeries().removeAll(seRuntimes);

				for (int j = 0; j < seRuntimes.size(); j++) {
					Series seRuntimeOrthogonal = seDesignOrthogonal.copyInstance();
					seRuntimeOrthogonal.setDataSet(seRuntimes.get(j).getDataSet());
					if (iOrthogonalSeriesDefinitionCount < 1) {
						seRuntimeOrthogonal.setSeriesIdentifier(seDesignOrthogonal.getSeriesIdentifier());
					} else {
						seRuntimeOrthogonal.setSeriesIdentifier(seRuntimes.get(j).getSeriesIdentifier());
					}
					sdOrthogonal.getSeries().add(seRuntimeOrthogonal);
				}
			}
		}

	}

	/**
	 * Does Live Preview. Need to check all series data binding complete before
	 * invoking
	 *
	 * @param cmRunTime    chart model that can add runtime data
	 * @param dataProvider data service provider
	 * @return chart model with runtime data
	 * @throws ChartException
	 */
	public static Chart doLivePreview(Chart cmRunTime, IDataServiceProvider dataProvider, IActionEvaluator iae)
			throws ChartException {
		boolean isSharingQuery = dataProvider.checkState(IDataServiceProvider.SHARE_QUERY)
				|| dataProvider.checkState(IDataServiceProvider.INHERIT_COLUMNS_GROUPS);
		// Here we will set isSharingQuery to false if it is sharing
		// chart case to ensure that chart generates correct expressions
		// to do evaluating in chart generator phase.
		isSharingQuery &= !dataProvider.checkState(IDataServiceProvider.SHARE_CHART_QUERY_RECURSIVELY);

		final List<String> expressions = Generator.instance().getRowExpressions(cmRunTime, iae, !isSharingQuery);

		IDataRowExpressionEvaluator evaluator = dataProvider.prepareRowExpressionEvaluator(cmRunTime, expressions, -1,
				false);

		RunTimeContext context = new RunTimeContext();
		context.setULocale(ULocale.getDefault());
		context.setSharingQuery(isSharingQuery);
		context.enableNonHierarchyCategoryData(
				dataProvider.checkState(IDataServiceProvider.USE_NON_HIERARCHY_CATEGORY_DATA));
		dataProvider.update(ChartUIConstants.UPDATE_MODEL, cmRunTime);
		Generator.instance().bindData(evaluator, iae, cmRunTime, context);

		if (evaluator != null) {
			evaluator.close();
		}

		return cmRunTime;
	}

	/**
	 * Converts sample data according to AxisType
	 *
	 * @param axisType           axis type
	 * @param sOldRepresentation old sample data representation
	 * @param index              sample data index
	 * @return new sample data representation
	 */
	public static String getConvertedSampleDataRepresentation(AxisType axisType, String sOldRepresentation, int index) {
		// StringTokenizer strtok = new StringTokenizer( sOldRepresentation, ","
		// ); //$NON-NLS-1$
		// NumberFormat nf = NumberFormat.getNumberInstance( );
		// SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy",
		// Locale.getDefault( ) ); //$NON-NLS-1$
		// StringBuffer sbNewRepresentation = new StringBuffer( "" );
		// //$NON-NLS-1$
		// while ( strtok.hasMoreTokens( ) )
		// {
		// String sElement = strtok.nextToken( ).trim( );
		// if ( sElement.startsWith( "'" ) ) //$NON-NLS-1$
		// {
		// sElement = sElement.substring( 1, sElement.length( ) - 1 );
		// }
		// try
		// {
		// if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		// {
		// sdf.parse( sElement );
		// }
		// else if ( axisType.equals( AxisType.TEXT_LITERAL ) )
		// {
		// if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
		// {
		// sElement = "'" + sElement + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		// else
		// {
		// double dbl = nf.parse( sElement ).doubleValue( );
		// sElement = String.valueOf( dbl );
		// }
		// }
		// catch ( ParseException e )
		// {
		// // Use the orginal sample data if parse exception encountered
		//
		// // if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		// // {
		// // Calendar cal = Calendar.getInstance( Locale.getDefault( ) );
		// // StringBuffer sbNewDate = new StringBuffer( "" );
		// // //$NON-NLS-1$
		// // sbNewDate.append( cal.get( Calendar.MONTH ) + 1 );
		// // sbNewDate.append( "/" ); //$NON-NLS-1$
		// // // Increasing the date beyond the last date for the month
		// // // causes the month to roll over
		// // sbNewDate.append( cal.get( Calendar.DATE ) + iValueCount );
		// // sbNewDate.append( "/" ); //$NON-NLS-1$
		// // sbNewDate.append( cal.get( Calendar.YEAR ) );
		// // sElement = sbNewDate.toString( );
		// // }
		// // else
		// // {
		// // sElement = String.valueOf( 6.0 + iValueCount );
		// // }
		// }
		// sbNewRepresentation.append( sElement );
		// sbNewRepresentation.append( "," ); //$NON-NLS-1$
		// }
		// return sbNewRepresentation.toString( ).substring( 0,
		// sbNewRepresentation.length( ) - 1 );
		return ChartUtil.getNewSampleData(axisType, index);
	}

	/**
	 * Sets the query to all series grouping, except for the first which should be
	 * set manually
	 *
	 * @param chart           chart model
	 * @param queryDefinition group query definition
	 */
	public static void setAllGroupingQueryExceptFirst(Chart chart, String queryDefinition) {
		List<SeriesDefinition> sds = getAllOrthogonalSeriesDefinitions(chart);
		for (int i = 0; i < sds.size(); i++) {
			if (i != 0) {
				// Except for the first, which is changed manually.
				SeriesDefinition sd = sds.get(i);
				if (sd.getQuery() != null) {
					sd.getQuery().setDefinition(queryDefinition);
					if (sds.get(0).getQuery().getGrouping() != null) {
						sd.getQuery().setGrouping(sds.get(0).getQuery().getGrouping().copyInstance());
						sd.getQuery().getGrouping().eAdapters().addAll(sd.getQuery().eAdapters());
					}
				} else {
					Query query = QueryImpl.create(queryDefinition);
					query.eAdapters().addAll(sd.eAdapters());
					if (sds.get(0).getQuery().getGrouping() != null) {
						query.setGrouping(sds.get(0).getQuery().getGrouping().copyInstance());
						query.getGrouping().eAdapters().addAll(query.eAdapters());
					}
					sd.setQuery(query);
				}
			}
		}
	}

	/**
	 * Adds an orthogonal axis. Ensures only one event is fired.
	 *
	 * @param chartModel chart model
	 */
	public static void addAxis(final ChartWithAxes chartModel) {
		// Prevent notifications rendering preview
		ChartAdapter.beginIgnoreNotifications();
		// --------------------------Begin

		// Create a clone of the existing Y Axis
		Axis xAxis = chartModel.getAxes().get(0);
		Axis yAxis = xAxis.getAssociatedAxes().get(0);
		Axis overlayAxis = yAxis.copyInstance();
		// Now update overlay axis to set the properties that are
		// different from
		// the original
		overlayAxis.setPrimaryAxis(false);
		overlayAxis.setOrigin(AxisOriginImpl.create(IntersectionType.MAX_LITERAL, null));
		overlayAxis.setLabelPosition(Position.RIGHT_LITERAL);
		overlayAxis.setTitlePosition(Position.RIGHT_LITERAL);
		overlayAxis.eAdapters().addAll(yAxis.eAdapters());

		// Retain the first series of the axis. Remove others
		if (overlayAxis.getSeriesDefinitions().size() > 1) {
			EList<SeriesDefinition> list = overlayAxis.getSeriesDefinitions();
			for (int i = list.size() - 1; i > 0; i--) {
				list.remove(i);
			}
		}

		// Update overlay series definition(retain the group query,
		// clean the data query)
		SeriesDefinition sdOverlay = overlayAxis.getSeriesDefinitions().get(0);
		EList<Query> dds = sdOverlay.getDesignTimeSeries().getDataDefinition();
		for (int i = 0; i < dds.size(); i++) {
			dds.get(i).setDefinition(""); //$NON-NLS-1$
		}

		// Update the sample values for the new overlay series
		SampleData sd = chartModel.getSampleData();
		// Create a new OrthogonalSampleData instance from the existing
		// one
		int currentSize = sd.getOrthogonalSampleData().size();
		OrthogonalSampleData sdOrthogonal = chartModel.getSampleData().getOrthogonalSampleData().get(0).copyInstance();
		sdOrthogonal.setDataSetRepresentation(ChartUtil.getNewSampleData(overlayAxis.getType(), currentSize));
		sdOrthogonal.setSeriesDefinitionIndex(currentSize);
		sdOrthogonal.eAdapters().addAll(sd.eAdapters());
		sd.getOrthogonalSampleData().add(sdOrthogonal);
		chartModel.getAxes().get(0).getAssociatedAxes().add(overlayAxis);
		// ------------------End
		ChartAdapter.endIgnoreNotifications();

		setSeriesName(chartModel);
	}

	/**
	 * Add default name for series.
	 *
	 * @param chart
	 * @param series
	 * @since 2.3
	 */
	public static void setSeriesName(Chart chart) {
		List<SeriesDefinition> seriesDefinitions = getAllOrthogonalSeriesDefinitions(chart);
		String seriesText = Messages.getString("ChartUIUtil.SeriesLabel"); //$NON-NLS-1$
		SeriesDefinition sd;
		for (int i = 0; i < seriesDefinitions.size(); i++) {
			sd = seriesDefinitions.get(i);
			if (needSeriesName(sd.getDesignTimeSeries().getSeriesIdentifier() == null ? null
					: sd.getDesignTimeSeries().getSeriesIdentifier().toString(), seriesText)) {
				sd.getDesignTimeSeries()
						.setSeriesIdentifier(MessageFormat.format(seriesText, new Object[] { Integer.valueOf(i + 1) }));
			}

		}

	}

	private static boolean needSeriesName(String name, String seriesText) {
		if (name == null) {
			return false;
		}

		String pattern = MessageFormat.format(seriesText, new Object[] { "[0-9]+" }); //$NON-NLS-1$
		return name.trim().matches(pattern) || name.trim().length() == 0;
	}

	/**
	 * Removes the last orthogonal axes. This is a batch operation.
	 *
	 * @param chartModel        chart model
	 * @param removedAxisNumber the number of axes to be removed
	 */
	public static void removeLastAxes(ChartWithAxes chartModel, int removedAxisNumber) {
		for (int i = 0; i < removedAxisNumber; i++) {
			removeLastAxis(chartModel);
		}
	}

	/**
	 * Removes the last orthogonal axis. Ensures only one event is fired.
	 *
	 * @param chartModel chart model
	 */
	public static void removeLastAxis(ChartWithAxes chartModel) {
		removeAxis(chartModel, getOrthogonalAxisNumber(chartModel) - 1);
	}

	/**
	 * Removes an orthogonal axis in the specified position. Ensures only one event
	 * is fired.
	 *
	 * @param chartModel chart model
	 * @param axisIndex  the index of the axis to be removed
	 */
	public static void removeAxis(final Chart chartModel, final int axisIndex) {
		if (chartModel instanceof ChartWithoutAxes) {
			return;
		}

		ChartAdapter.beginIgnoreNotifications();

		// Ensure one primary axis existent
		Axis oldPrimaryAxis = getAxisYForProcessing((ChartWithAxes) chartModel, axisIndex);
		if (oldPrimaryAxis.isPrimaryAxis()) {
			int yAxisSize = getOrthogonalAxisNumber(chartModel);
			Axis newPrimaryAxis = null;
			if (axisIndex + 1 < yAxisSize) {
				newPrimaryAxis = getAxisYForProcessing((ChartWithAxes) chartModel, axisIndex + 1);
			} else {
				newPrimaryAxis = getAxisYForProcessing((ChartWithAxes) chartModel, 0);
			}
			// Retain the properties of primary axis
			newPrimaryAxis.setPrimaryAxis(true);
			newPrimaryAxis.setOrigin(oldPrimaryAxis.getOrigin());
			newPrimaryAxis.setLabelPosition(oldPrimaryAxis.getLabelPosition());
			newPrimaryAxis.setTitlePosition(oldPrimaryAxis.getTitlePosition());
		}
		ChartAdapter.endIgnoreNotifications();

		// Remove the orthogonal axis
		getAxisXForProcessing((ChartWithAxes) chartModel).getAssociatedAxes().remove(axisIndex);
	}

	public static int getLastSeriesIndexWithinAxis(Chart chartModel, int axisIndex) {
		if (chartModel instanceof ChartWithoutAxes || axisIndex < 0
				|| axisIndex >= getOrthogonalAxisNumber(chartModel)) {
			return -1;
		}
		int seriesIndex = -1;
		for (int i = 0; i <= axisIndex; i++) {
			seriesIndex += getOrthogonalSeriesDefinitions(chartModel, i).size();
		}
		return seriesIndex;
	}

	/**
	 * Reorder the index of the orthogonal sample data. If index is wrong, sample
	 * data can't display correctly.
	 *
	 * @param chartModel
	 */
	public static void reorderOrthogonalSampleDataIndex(Chart chartModel) {
		EList<OrthogonalSampleData> list = chartModel.getSampleData().getOrthogonalSampleData();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSeriesDefinitionIndex(i);
		}
	}

	/**
	 * Copies general attributes in Series.
	 *
	 * @param oldSeries copied series
	 * @param newSeries target series
	 */
	public static void copyGeneralSeriesAttributes(Series oldSeries, Series newSeries) {
		newSeries.setLabel(oldSeries.getLabel());
		newSeries.setSeriesIdentifier(oldSeries.getSeriesIdentifier());
		if (oldSeries.isSetVisible()) {
			newSeries.setVisible(oldSeries.isVisible());
		}
		// Attention: stacked will be set in chart conversion according to
		// sub-type.
		if (oldSeries.isSetStacked() && newSeries.canBeStacked()) {
			newSeries.setStacked(oldSeries.isStacked());
		}
		if (oldSeries.isSetTranslucent()) {
			newSeries.setTranslucent(oldSeries.isTranslucent());
		}
		if (oldSeries.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers())) {
			newSeries.getTriggers().clear();
			newSeries.getTriggers().addAll(oldSeries.getTriggers());
		}
		if (oldSeries.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint())) {
			newSeries.setDataPoint(oldSeries.getDataPoint());
		}
		if (oldSeries.eIsSet(ComponentPackage.eINSTANCE.getSeries_CurveFitting())) {
			newSeries.setCurveFitting(oldSeries.getCurveFitting());
		}

		// Label position
		if (oldSeries.isSetLabelPosition()) {
			if ((oldSeries.getLabelPosition().equals(Position.INSIDE_LITERAL)
					|| oldSeries.getLabelPosition().equals(Position.OUTSIDE_LITERAL))) {
				if (newSeries instanceof LineSeries || newSeries instanceof StockSeries
						|| newSeries instanceof GanttSeries) {
					newSeries.setLabelPosition(Position.ABOVE_LITERAL);
				} else {
					newSeries.setLabelPosition(oldSeries.getLabelPosition());
				}
			} else if (newSeries instanceof LineSeries || newSeries instanceof StockSeries
					|| newSeries instanceof GanttSeries) {
				newSeries.setLabelPosition(oldSeries.getLabelPosition());
			} else {
				newSeries.setLabelPosition(Position.OUTSIDE_LITERAL);
			}
		}

		// Data definition
		if (oldSeries.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition())) {
			Query query = oldSeries.getDataDefinition().get(0);
			newSeries.getDataDefinition().clear();
			if (newSeries instanceof StockSeries) {
				if (oldSeries.getDataDefinition().size() != 4) {
					// For High value
					newSeries.getDataDefinition().add(query.copyInstance());
					// For Low value
					newSeries.getDataDefinition().add(query.copyInstance());
					// For Open value
					newSeries.getDataDefinition().add(query.copyInstance());
					// For Close value
					newSeries.getDataDefinition().add(query.copyInstance());
				} else {
					newSeries.getDataDefinition().addAll(oldSeries.getDataDefinition());
				}
			} else if (newSeries instanceof BubbleSeries) {
				if (oldSeries.getDataDefinition().size() != 2) {
					// For value
					newSeries.getDataDefinition().add(query.copyInstance());
					// For Size
					newSeries.getDataDefinition().add(query.copyInstance());
				} else {
					newSeries.getDataDefinition().addAll(oldSeries.getDataDefinition());
				}
			} else if (newSeries instanceof DifferenceSeries) {
				if (oldSeries.getDataDefinition().size() != 2) {
					newSeries.getDataDefinition().add(query.copyInstance());
					newSeries.getDataDefinition().add(query.copyInstance());
				} else {
					newSeries.getDataDefinition().addAll(oldSeries.getDataDefinition());
				}
			} else if (newSeries instanceof GanttSeries) {
				if (oldSeries.getDataDefinition().size() != 3) {
					// For start
					newSeries.getDataDefinition().add(query.copyInstance());
					// For end
					newSeries.getDataDefinition().add(query.copyInstance());
					// For Label
					newSeries.getDataDefinition().add(query.copyInstance());
				} else {
					newSeries.getDataDefinition().addAll(oldSeries.getDataDefinition());
				}
			} else {
				newSeries.getDataDefinition().add(query);
			}
		}

		// Markers for Line-similar series
		if (oldSeries instanceof LineSeries && newSeries instanceof LineSeries) {
			List<Marker> oldMarkers = ((LineSeries) oldSeries).getMarkers();
			List<Marker> newMarkers = ((LineSeries) newSeries).getMarkers();
			if (oldMarkers.size() > 1) {
				newMarkers.clear();
				for (Marker m : oldMarkers) {
					newMarkers.add(m.copyInstance());
				}
			}
		}
	}

	public static ChartDimension getDimensionType(String sDimension) {
		if (sDimension == null || sDimension.equals(IChartType.TWO_DIMENSION_TYPE)) {
			return ChartDimension.TWO_DIMENSIONAL_LITERAL;
		}
		if (sDimension.equals(IChartType.THREE_DIMENSION_TYPE)) {
			return ChartDimension.THREE_DIMENSIONAL_LITERAL;
		}
		return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
	}

	public static String getDimensionString(ChartDimension dimension) {
		if (dimension == null || dimension == ChartDimension.TWO_DIMENSIONAL_LITERAL) {
			return IChartType.TWO_DIMENSION_TYPE;
		}
		if (dimension == ChartDimension.THREE_DIMENSIONAL_LITERAL) {
			return IChartType.THREE_DIMENSION_TYPE;
		}
		return IChartType.TWO_DIMENSION_WITH_DEPTH_TYPE;
	}

	/**
	 * Sets the given help context id on the given control's shell.
	 *
	 * @param control   the control on which to register the context id
	 * @param contextId the context id to use when F1 help is invoked
	 */
	public static void bindHelp(Control control, String contextId) {
		// disable help
		// try
		// {
		// IWorkbench workbench = PlatformUI.getWorkbench( );
		// workbench.getHelpSystem( ).setHelp( control.getShell( ), contextId );
		// }
		// catch ( RuntimeException e )
		// {
		// // Do nothing since there's no workbench
		// }
	}

	/**
	 * Returns whether wall/floor has been set if it's 3D chart.
	 *
	 * @param chart Chart model
	 * @return Returns whether wall/floor has been set if it's 3D chart. Returns
	 *         true if chart is ChartWithoutAxes or not 3D
	 */
	public static boolean is3DWallFloorSet(Chart chart) {
		if (!ChartUIUtil.is3DType(chart) || chart instanceof ChartWithoutAxes) {
			return true;
		}
		ChartWithAxes chartWithAxes = (ChartWithAxes) chart;

		Fill wall = chartWithAxes.getWallFill();
		if (wall != null) {
			if (wall instanceof ColorDefinition && ((ColorDefinition) wall).getTransparency() > 0) {
				return true;
			}
			return true;
		}

		Fill floor = chartWithAxes.getFloorFill();
		if (floor != null) {
			if (floor instanceof ColorDefinition && ((ColorDefinition) floor).getTransparency() > 0) {
				return true;
			}
			return true;
		}

		return false;
	}

	/**
	 * Convert the displayed anchor in the case of flipped axes.
	 *
	 * @param anchor
	 * @param isFlippedAxes true if the Orientation is Horizontal
	 */
	public static Anchor getFlippedAnchor(Anchor anchor, boolean isFlippedAxes) {
		if (isFlippedAxes) {
			return ChartUtil.transposeAnchor(anchor);
		}
		return anchor;
	}

	/**
	 * Convert the displayed text alignment in the case of flipped axes.
	 *
	 * @param ta
	 * @param isFlippedAxes true if the Orientation is Horizontal
	 * @return The flipped text alignment
	 */
	public static TextAlignment getFlippedAlignment(TextAlignment ta, boolean isFlippedAxes) {
		if (isFlippedAxes) {
			return ChartUtil.transposeAlignment(ta);
		}
		return ta;
	}

	/**
	 * Convert the displayed position in the case of flipped axes.
	 *
	 * @param position
	 * @param isFlippedAxes true if the Orientation is Horizontal
	 */
	public static Position getFlippedPosition(Position position, boolean isFlippedAxes) {
		if (position != null && isFlippedAxes) {
			switch (position.getValue()) {
			case Position.ABOVE:
				position = Position.RIGHT_LITERAL;
				break;
			case Position.BELOW:
				position = Position.LEFT_LITERAL;
				break;
			case Position.LEFT:
				position = Position.BELOW_LITERAL;
				break;
			case Position.RIGHT:
				position = Position.ABOVE_LITERAL;
				break;
			}
		}
		return position;
	}

	/**
	 * Gets the array of position display names
	 *
	 * @param positionScope position scope
	 * @param isFlipped     current chart model's direction
	 * @return string array of position display names
	 */
	public static String[] getPositionDisplayNames(int positionScope, boolean isFlipped) {
		if ((positionScope & ChartUIConstants.ALLOW_ALL_POSITION) == ChartUIConstants.ALLOW_ALL_POSITION) {
			return LiteralHelper.fullPositionSet.getDisplayNames();
		}

		List<String> items = new ArrayList<>(5);
		// check vertical
		if ((positionScope & ChartUIConstants.ALLOW_VERTICAL_POSITION) == ChartUIConstants.ALLOW_VERTICAL_POSITION) {
			if (isFlipped) {
				addArrayToList(LiteralHelper.horizontalPositionSet.getDisplayNames(), items);
			} else {
				addArrayToList(LiteralHelper.verticalPositionSet.getDisplayNames(), items);
			}
		}
		// check horizontal
		if ((positionScope
				& ChartUIConstants.ALLOW_HORIZONTAL_POSITION) == ChartUIConstants.ALLOW_HORIZONTAL_POSITION) {
			if (isFlipped) {
				addArrayToList(LiteralHelper.verticalPositionSet.getDisplayNames(), items);
			} else {
				addArrayToList(LiteralHelper.horizontalPositionSet.getDisplayNames(), items);
			}
		}
		// check inout
		// Inside or outside can be added separately
		if ((positionScope & ChartUIConstants.ALLOW_IN_POSITION) == ChartUIConstants.ALLOW_IN_POSITION) {
			items.add(LiteralHelper.inoutPositionSet.getDisplayNameByName(Position.INSIDE_LITERAL.getName()));
		}
		if ((positionScope & ChartUIConstants.ALLOW_OUT_POSITION) == ChartUIConstants.ALLOW_OUT_POSITION) {
			items.add(LiteralHelper.inoutPositionSet.getDisplayNameByName(Position.OUTSIDE_LITERAL.getName()));
		}

		return items.toArray(new String[items.size()]);
	}

	public static String[] getPositionNames(int positionScope, boolean isFlipped) {
		if ((positionScope & ChartUIConstants.ALLOW_ALL_POSITION) == ChartUIConstants.ALLOW_ALL_POSITION) {
			return LiteralHelper.fullPositionSet.getNames();
		}

		List<String> items = new ArrayList<>(5);
		// check vertical
		if ((positionScope & ChartUIConstants.ALLOW_VERTICAL_POSITION) == ChartUIConstants.ALLOW_VERTICAL_POSITION) {
			if (isFlipped) {
				addArrayToList(LiteralHelper.horizontalPositionSet.getNames(), items);
			} else {
				addArrayToList(LiteralHelper.verticalPositionSet.getNames(), items);
			}
		}
		// check horizontal
		if ((positionScope
				& ChartUIConstants.ALLOW_HORIZONTAL_POSITION) == ChartUIConstants.ALLOW_HORIZONTAL_POSITION) {
			if (isFlipped) {
				addArrayToList(LiteralHelper.verticalPositionSet.getNames(), items);
			} else {
				addArrayToList(LiteralHelper.horizontalPositionSet.getNames(), items);
			}
		}
		// check inout
		// Inside or outside can be added separately
		if ((positionScope & ChartUIConstants.ALLOW_IN_POSITION) == ChartUIConstants.ALLOW_IN_POSITION) {
			items.add(Position.INSIDE_LITERAL.getName());
		}
		if ((positionScope & ChartUIConstants.ALLOW_OUT_POSITION) == ChartUIConstants.ALLOW_OUT_POSITION) {
			items.add(Position.OUTSIDE_LITERAL.getName());
		}

		return items.toArray(new String[items.size()]);
	}

	private static void addArrayToList(String[] array, List<String> list) {
		for (int i = 0; i < array.length; i++) {
			list.add(array[i]);
		}
	}

	/**
	 * Caches label position for different subtype.
	 *
	 * @param seriesDefinition the series definition in chart.
	 */
	public static void saveLabelPositionIntoCache(SeriesDefinition seriesDefinition) {
		if (seriesDefinition == null) {
			return;
		}

		// Cache label position of first BarSeries in all series.
		for (Series series : seriesDefinition.getSeries()) {
			if (series instanceof BarSeries) {
				String stackedCase = ChartUIConstants.NON_STACKED_TYPE;
				if (series.isStacked()) {
					stackedCase = ChartUIConstants.STACKED_TYPE;
				}

				ChartCacheManager.getInstance().cacheLabelPositionWithStackedCase(stackedCase,
						series.getLabelPosition());
				break;
			}
		}
	}

	/**
	 * Restore cached label position to BarSeries in current chart.
	 *
	 * @param currentChart current chart.
	 */
	public static void restoreLabelPositionFromCache(Chart currentChart) {
		if (currentChart == null) {
			return;
		}

		SeriesDefinition[] sds = currentChart.getSeriesForLegend();
		for (int i = 0; i < sds.length; i++) {
			EList<Series> seriesList = sds[i].getSeries();
			for (Iterator<Series> iter = seriesList.iterator(); iter.hasNext();) {
				Series series = iter.next();
				if (series instanceof BarSeries) {
					String stackedCase = ChartUIConstants.NON_STACKED_TYPE;
					if (series.isStacked()) {
						stackedCase = ChartUIConstants.STACKED_TYPE;
					}

					Position labelPosition = ChartCacheManager.getInstance()
							.findLabelPositionWithStackedCase(stackedCase);

					if (labelPosition != null) {
						series.setLabelPosition(labelPosition);
					} else if (series.isStacked()) {
						series.setLabelPosition(Position.INSIDE_LITERAL);
					}
				}
			}
		}
	}

	/**
	 * Gets the specific series UI provider. Do not use this method to get series
	 * composite since current instance is always basic chart UI implementation.
	 *
	 * @param series series in chart model
	 * @return series UI provider
	 * @since 2.2.1
	 */
	public static ISeriesUIProvider getSeriesUIProvider(Series series) {
		return htSeriesAttributeUIProviders.get(series.getClass().getName());
	}

	/** The default image button height of Chart. */
	public static final int BUTTON_HEIGHT = 20;

	/** The default image button width of Chart. */
	public static final int BUTTON_WIDTH = 20;

	/**
	 * @param gridData
	 *
	 * @since 2.3
	 */
	public static void setChartImageButtonSizeByPlatform(GridData gridData) {
		if (isWindows()) {
			gridData.heightHint = BUTTON_HEIGHT;
			gridData.widthHint = BUTTON_WIDTH;
		}
	}

	/**
	 * @param gridData
	 *
	 * @since 2.3
	 */
	public static void setChartImageButtonHeightByPlatform(GridData gridData) {
		if (isWindows()) {
			gridData.heightHint = BUTTON_HEIGHT;
		}
	}

	/**
	 * @since 2.3
	 */
	public static int getImageButtonDefaultHeightByPlatform() {
		if (isWindows()) {
			return BUTTON_HEIGHT;
		}

		return 0;
	}

	private static boolean isWindows() {
		String platform = SWT.getPlatform();
		if ("win32".equals(platform)) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks if grouping is supported.
	 *
	 * @param wizardContext
	 * @since 2.3
	 */
	public static boolean isGroupingSupported(ChartWizardContext wizardContext) {
		// If predefined query is found, that means cube bindings is used, so
		// grouping is unsupported in this case.
		//
		// #191701, 2/5/2009, now the predefined query can not be used to indicate if
		// grouping is supported, since the predefined query has been used to
		// store available expressions for the content assist on UI, so the
		// predefined query should not be null. We just need to check the
		// sharing query case to enable/disable the group.

		// TED#12813 If inheritance is used, "inherit columns and groups" mode
		// will disable grouping and aggregation
		final int state = wizardContext.getDataServiceProvider().getState();
		return (state & IDataServiceProvider.SHARE_QUERY) != IDataServiceProvider.SHARE_QUERY
				&& (state & IDataServiceProvider.INHERIT_COLUMNS_GROUPS) != IDataServiceProvider.INHERIT_COLUMNS_GROUPS
				// do not allow group for chart consuming cube
				&& (state & IDataServiceProvider.HAS_CUBE) != IDataServiceProvider.HAS_CUBE
				&& (state & IDataServiceProvider.INHERIT_CUBE) != IDataServiceProvider.INHERIT_CUBE;
	}

	private static String checkGroupTypeOnCategory(ChartWizardContext context, Chart chart) {
		String isConsistent = ""; //$NON-NLS-1$
		SeriesDefinition seriesdefinition = getBaseSeriesDefinitions(chart).get(0);
		DataType queryDataType = context.getDataServiceProvider()
				.getDataType(ChartUIUtil.getDataQuery(seriesdefinition, 0).getDefinition());
		if (queryDataType != null && seriesdefinition.getGrouping() != null) {
			if (!seriesdefinition.getGrouping().isEnabled()) {
				return isConsistent;
			}

			DataType groupDataType = seriesdefinition.getGrouping().getGroupType();
			if (queryDataType == DataType.NUMERIC_LITERAL && groupDataType == DataType.DATE_TIME_LITERAL) {
				return groupDataType.getName();
			} else if (queryDataType == DataType.TEXT_LITERAL && groupDataType != DataType.TEXT_LITERAL) {
				return groupDataType.getName();
			}
		}

		return isConsistent;
	}

	private static String checkGroupTypeOnYGrouping(ChartWizardContext context, Chart chart) {
		String isConsistent = ""; //$NON-NLS-1$
		SeriesDefinition seriesdefinition = ChartUIUtil.getOrthogonalSeriesDefinitions(chart, 0).get(0);
		if (seriesdefinition.getQuery() != null) {
			DataType queryDataType = context.getDataServiceProvider()
					.getDataType(seriesdefinition.getQuery().getDefinition());
			if (queryDataType != null && seriesdefinition.getQuery().getGrouping() != null) {
				DataType groupDataType = seriesdefinition.getQuery().getGrouping().getGroupType();
				if (queryDataType == DataType.NUMERIC_LITERAL && groupDataType == DataType.DATE_TIME_LITERAL) {
					return groupDataType.getName();
				} else if (queryDataType == DataType.TEXT_LITERAL && groupDataType != DataType.TEXT_LITERAL) {
					return groupDataType.getName();
				}
			}
		}

		return isConsistent;
	}

	/**
	 * To check the group type of category and y series. If not compatible , show a
	 * warning.
	 *
	 * @param context
	 * @param chart
	 */
	public static void checkGroupType(ChartWizardContext context, Chart chart) {
		String cGroupWarning = checkGroupTypeOnCategory(context, chart);
		if (cGroupWarning.length() != 0) {
			cGroupWarning = Messages.getString("TaskSelectData.Warning.CategoryGroupTypeCheck", //$NON-NLS-1$
					new String[] { cGroupWarning });
		}
		String yGroupWarning = checkGroupTypeOnYGrouping(context, chart);
		if (yGroupWarning.length() != 0) {
			yGroupWarning = Messages.getString("TaskSelectData.Warning.YGroupTypeCheck", //$NON-NLS-1$
					new String[] { yGroupWarning });
		}
		if (cGroupWarning.length() != 0 || yGroupWarning.length() != 0) {
			ChartWizard.showException(ChartWizard.ChartUIUtil_cGType_ID, cGroupWarning + yGroupWarning);
		} else {
			ChartWizard.removeException(ChartWizard.ChartUIUtil_cGType_ID);
		}
	}

	/**
	 * Check if default aggregation and value series aggregation are valid for Gantt
	 * chart and show warning message in UI.
	 *
	 * @param context
	 * @since 2.3
	 */
	public static boolean checkAggregateType(ChartWizardContext context) {
		boolean isValid = true;
		SeriesDefinition baseSD = ChartUIUtil.getBaseSeriesDefinitions(context.getModel()).get(0);

		for (SeriesDefinition orthSD : ChartUIUtil.getOrthogonalSeriesDefinitions(context.getModel(), 0)) {
			if (!isValid) {
				break;
			}
			Series oSeries = orthSD.getDesignTimeSeries();

			isValid = getSeriesUIProvider(oSeries).isValidAggregationType(oSeries, orthSD, baseSD);

		}

		return isValid;
	}

	/**
	 * Get the compatible axis type according to series type.
	 *
	 * @param series
	 * @return NameSet
	 * @since 2.3
	 */
	public static NameSet getCompatibleAxisType(Series series) {
		ISeriesUIProvider provider = getSeriesUIProvider(series);
		AxisType[] types = provider.getCompatibleAxisType(series);
		String[] names = new String[types.length];
		for (int i = 0; i < types.length; i++) {
			names[i] = types[i].getName();
		}
		String prefix = "AxisType."; //$NON-NLS-1$
		String suffix = ".displayName"; //$NON-NLS-1$

		return new NameSet(prefix, suffix, names);
	}

	/**
	 * Create runtime chart model and bind preview data.
	 *
	 * @param cm
	 * @param dataServiceProvider
	 * @since BIRT 2.3
	 */
	public static Chart prepareLivePreview(Chart cm, IDataServiceProvider dataServiceProvider) {
		return prepareLivePreview(cm, dataServiceProvider, null);
	}

	/**
	 * Create runtime chart model and bind preview data.
	 *
	 * @param cm
	 * @param dataServiceProvider
	 * @since BIRT 2.3
	 */
	public static Chart prepareLivePreview(Chart cm, IDataServiceProvider dataServiceProvider, IActionEvaluator iae) {
		Chart cmRuntime = null;

		if (dataServiceProvider.isLivePreviewEnabled() && ChartUIUtil.checkDataBinding(cm)) {
			// Enable live preview
			ChartPreviewPainterBase.activateLivePreview(true);

			boolean bException = false;

			try {
				// Remove adapters to avoid listening caused by runtime model change
				cmRuntime = cm.copyInstance();
				cmRuntime.eAdapters().clear();
				for (Iterator<EObject> iterator = cmRuntime.eAllContents(); iterator.hasNext();) {
					EObject content = iterator.next();
					content.eAdapters().clear();
				}

				cmRuntime = ChartUIUtil.doLivePreview(cmRuntime, dataServiceProvider, iae);
			}
			// Includes RuntimeException
			catch (Exception e) {
				// Enable sample data instead
				ChartPreviewPainterBase.activateLivePreview(false);

				// Zero dataset message will not display, it will use sample
				// data to do live preview.
				boolean isZeroDataset = false;
				if (e instanceof ChartException && ((ChartException) e).getType() == ChartException.ZERO_DATASET) {
					isZeroDataset = true;
				}

				if (!isZeroDataset) {
					bException = true;
					ChartPreviewPainterBase.activateLivePreview(false);
					ChartWizard.showException(ChartWizard.ChartUIUtil_pLiPreview_ID, e.getLocalizedMessage());
				}
			}
			if (!bException) {
				ChartWizard.removeException(ChartWizard.ChartUIUtil_pLiPreview_ID);
			}
		} else {
			// Disable live preview
			ChartPreviewPainterBase.activateLivePreview(false);
			// remove the error message
			ChartWizard.removeException(ChartWizard.ChartUIUtil_pLiPreview_ID);
		}

		return cmRuntime != null ? cmRuntime : cm.copyInstance();
	}

	public static String getText(Control control) {
		if (control instanceof Text) {
			return ((Text) control).getText();
		}
		if (control instanceof CCombo) {
			// Fix a CCombo bug. Since a blank character is added to display the
			// text correctly, trim it when saving.
			return ((CCombo) control).getText().trim();
		}
		if (control instanceof Combo) {
			return ((Combo) control).getText();
		}
		return ""; //$NON-NLS-1$
	}

	public static void setText(Control control, String text) {
		if (control instanceof Text) {
			((Text) control).setText(text);
		} else if (control instanceof CCombo) {
			if (text.trim().length() > 0) {
				// Fix a CCombo bug. If the text is too long and CCombo is
				// shorter than it, CCombo will only display the tail.
				text += " "; //$NON-NLS-1$
			}
			((CCombo) control).setText(text);
		} else if (control instanceof Combo) {
			((Combo) control).setText(text);
		}
	}

	/**
	 * help method for verifying the data type compatibility of the series, returns
	 * true if the series has a numeric aggregation function
	 *
	 * @param series
	 */
	public static boolean isNumericAggregate(Series series) {
		Chart cm = ChartUtil.getChartFromSeries(series);
		SeriesDefinition baseSD = (ChartUIUtil.getBaseSeriesDefinitions(cm).get(0));
		SeriesDefinition orthSD;
		orthSD = (SeriesDefinition) series.eContainer();

		for (Query query : series.getDataDefinition()) {
			String aggFunc = null;
			try {
				aggFunc = ChartUtil.getAggregateFuncExpr(orthSD, baseSD, query);
			} catch (ChartException e) {
			}
			if (baseSD != orthSD && ChartUtil.isMagicAggregate(aggFunc)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets default aggregation functions for model
	 *
	 * @param cm model
	 * @since 2.5.1
	 */
	public static void updateDefaultAggregations(Chart cm) {
		for (SeriesDefinition vsd : getAllOrthogonalSeriesDefinitions(cm)) {
			Series vs = vsd.getDesignTimeSeries();
			if (vs instanceof StockSeries) {
				EList<Query> queries = vs.getDataDefinition();
				while (queries.size() < 4) {
					queries.add(QueryImpl.create("")); //$NON-NLS-1$
				}
				setSeriesAggregation(queries.get(0), "Max");// High //$NON-NLS-1$
				setSeriesAggregation(queries.get(1), "Min");// Low //$NON-NLS-1$
				setSeriesAggregation(queries.get(2), "First");// Open //$NON-NLS-1$
				setSeriesAggregation(queries.get(3), "Last");// Close //$NON-NLS-1$
			}
			if (vs instanceof GanttSeries) {
				EList<Query> queries = vs.getDataDefinition();
				while (queries.size() < 3) {
					queries.add(QueryImpl.create("")); //$NON-NLS-1$
				}
				setSeriesAggregation(queries.get(0), "Min");// Start //$NON-NLS-1$
				setSeriesAggregation(queries.get(1), "Max");// End //$NON-NLS-1$
			}
		}
	}

	private static void setSeriesAggregation(Query query, String aggFunc) {
		SeriesGrouping grouping = query.getGrouping();
		if (grouping == null) {
			grouping = SeriesGroupingImpl.create();
			query.setGrouping(grouping);
		}
		grouping.setEnabled(true);
		grouping.setAggregateExpression(aggFunc);
		query.setGrouping(grouping);
	}

	/**
	 * Populate the chart types for UI.
	 *
	 * @param context
	 * @since 2.5
	 */
	public static void populateTypeTable(IWizardContext context) {
		if (htTypes == null) {
			htTypes = new LinkedHashMap<>();
		}
		htTypes.clear();

		Collection<IChartType> cTypes = ChartUIExtensionsImpl.instance()
				.getUIChartTypeExtensions(((ChartWizardContext) context).getIdentifier());
		Iterator<IChartType> iterTypes = cTypes.iterator();
		boolean needExpand = ((ChartWizardContext) context).getDataServiceProvider()
				.checkState(IDataServiceProvider.PART_CHART);
		while (iterTypes.hasNext()) {
			IChartType type = iterTypes.next();
			// If chart needs expand according to category number, only display
			// the chart that can expand
			if (!needExpand || type.canExpand()) {
				htTypes.put(type.getName(), type);
			}
		}
	}

	/**
	 * Returns all chart type instances.
	 *
	 * @return chart type iterator.
	 */
	public static Iterator<IChartType> getChartTypeInstancesIterator() {
		return htTypes.values().iterator();
	}

	public static Iterator<String> getChartTypeNameIterator() {
		return htTypes.keySet().iterator();
	}

	public static IChartType getChartType(String name) {
		return htTypes.get(name);
	}

	public static abstract class ComboProxy {

		public abstract void setText(String string);

		public abstract String getText();

		public abstract void removeAll();

		public abstract void setItem(int index, String string);

		public abstract void setItems(String[] items);

		public abstract String[] getItems();

		public abstract void add(String string);

		public abstract void setData(String key, Object value);

		public abstract Object getData(String key);

		public abstract int getSelectionIndex();

		public static ComboProxy getInstance(Control control) {
			if (control != null && !control.isDisposed()) {
				if (control instanceof Combo) {
					return new ProxyOfCombo((Combo) control);
				} else if (control instanceof CCombo) {
					return new ProxyOfCCombo((CCombo) control);
				}
			}

			return null;
		}

		public boolean contains(String text) {
			for (String item : getItems()) {
				if (item.equals(text)) {
					return true;
				}
			}
			return false;
		}

		static class ProxyOfCombo extends ComboProxy {

			private final Combo combo;

			public ProxyOfCombo(Combo combo) {
				this.combo = combo;
			}

			@Override
			public void add(String string) {
				combo.add(string);
			}

			@Override
			public String[] getItems() {
				return combo.getItems();
			}

			@Override
			public void removeAll() {
				combo.removeAll();
			}

			@Override
			public void setItem(int index, String string) {
				combo.setItem(index, string);
			}

			@Override
			public void setItems(String[] items) {
				combo.setItems(items);
			}

			@Override
			public void setText(String string) {
				combo.setText(string);
			}

			@Override
			public String getText() {
				return combo.getText();
			}

			@Override
			public Object getData(String key) {
				return combo.getData(key);
			}

			@Override
			public void setData(String key, Object value) {
				combo.setData(key, value);
			}

			@Override
			public int getSelectionIndex() {
				return combo.getSelectionIndex();
			}

		}

		static class ProxyOfCCombo extends ComboProxy {

			private final CCombo ccombo;

			public ProxyOfCCombo(CCombo ccombo) {
				this.ccombo = ccombo;
			}

			@Override
			public void add(String string) {
				ccombo.add(string);
			}

			@Override
			public String[] getItems() {
				return ccombo.getItems();
			}

			@Override
			public void removeAll() {
				ccombo.removeAll();
			}

			@Override
			public void setItem(int index, String string) {
				ccombo.setItem(index, string);
			}

			@Override
			public void setItems(String[] items) {
				ccombo.setItems(items);
			}

			@Override
			public void setText(String string) {
				ccombo.setText(string);
			}

			@Override
			public String getText() {
				return ccombo.getText();
			}

			@Override
			public Object getData(String key) {
				return ccombo.getData(key);
			}

			@Override
			public void setData(String key, Object value) {
				ccombo.setData(key, value);
			}

			@Override
			public int getSelectionIndex() {
				return ccombo.getSelectionIndex();
			}

		}
	}

	public static class EAttributeAccessor<T> {

		private final EObject eObj;
		private final EAttribute eAttr;

		public EAttributeAccessor(EObject eObj, EAttribute eAttr) {
			this.eObj = eObj;
			this.eAttr = eAttr;
		}

		@SuppressWarnings("unchecked")
		public T load() {
			return (T) eObj.eGet(eAttr);
		}

		public void save(T value) {
			save(value, false);
		}

		public void save(T value, boolean bIgnoreNotification) {
			if (bIgnoreNotification) {
				ChartAdapter.beginIgnoreNotifications();
			}
			eObj.eSet(eAttr, value);
			if (bIgnoreNotification) {
				ChartAdapter.endIgnoreNotifications();
			}
		}
	}

	/**
	 * Check if it just allows category expression as category sort key. When Y
	 * optional expressions is specified and not sharing/cube cases, the Y optional
	 * will be top group and category expression will be the second use category
	 * expression as the sort key.
	 *
	 * @param wizardContext
	 * @return result
	 * @since 2.5.3
	 */
	public static boolean hasLimitOnCategorySortKey(ChartWizardContext wizardContext) {
		// Below code is commented out, since now we don't limit sort key on category
		// again, the new rule is:
		// if multiple series has different orders on categories, the first
		// series' order will be always used, first series' categories order
		// will be used for other series.

//		int stateInfo = wizardContext.getDataServiceProvider( ).getState( );
//		boolean isCube = ( stateInfo & IDataServiceProvider.HAS_CUBE ) == IDataServiceProvider.HAS_CUBE
//				&& ( stateInfo & IDataServiceProvider.SHARE_QUERY ) != IDataServiceProvider.SHARE_QUERY;
//
//		if ( ChartUtil.isSpecifiedYOptionalExpression( wizardContext.getModel( ) ) && !isCube )
//
//		{
//			return true;
//		}

		return false;
	}

	/**
	 * Checks if current aggregation function supports datetime data or not.
	 *
	 * @param aggFunc
	 * @return supported or not
	 */
	public static boolean isDataTimeSupportedAgg(String aggFunc) {
		return PluginSettings.DefaultAggregations.AVERAGE.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.FIRST.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.LAST.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.MIN.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.MAX.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.MEDIAN.equals(aggFunc)
				|| PluginSettings.DefaultAggregations.MODE.equals(aggFunc) || ChartUtil.isMagicAggregate(aggFunc);

	}

	/**
	 * Add specified description to composite to support Screen Reader tool.
	 *
	 * @param composite   composite
	 * @param description description
	 */
	public static void addScreenReaderAccessbility(Control composite, final String description) {
		if (composite == null) {
			// if control component is null, do nothing.
			return;
		}
		if (description != null) {
			if (composite instanceof Spinner) {
				addSpinnerScreenReaderAccessbility((Spinner) composite, description);
			} else {
				composite.getAccessible().addAccessibleListener(new AccessibleAdapter() {

					@Override
					public void getName(AccessibleEvent e) {
						e.result = description.replace("&", IConstants.EMPTY_STRING) //$NON-NLS-1$
								.replace(":", IConstants.EMPTY_STRING) //$NON-NLS-1$
								.replace("*", IConstants.EMPTY_STRING); //$NON-NLS-1$
					}
				});
			}

		}
	}

	/**
	 * Add specified description to spinner to support Screen Reader tool.
	 *
	 * @param composite   composite
	 * @param description description
	 */
	public static void addSpinnerScreenReaderAccessbility(final Spinner spinner, final String description) {
		spinner.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			@Override
			public void getName(AccessibleEvent e) {
				e.result = description.replace("&", IConstants.EMPTY_STRING) //$NON-NLS-1$
						.replace(":", IConstants.EMPTY_STRING) //$NON-NLS-1$
						.replace("*", IConstants.EMPTY_STRING); //$NON-NLS-1$
			}
		});
		spinner.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				// work around part 1: when the text gets focus, set accessible focus to the
				// spinner
				spinner.getAccessible().setFocus(ACC.CHILDID_SELF);
			}
		});
		spinner.getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getValue(AccessibleControlEvent e) {
				// work around part 2: propagate the text's value to the spinner
				e.result = Integer.toString(spinner.getSelection());
			}
		});
	}

	/**
	 * Support Screen Reader for custom chart composites, like ChartCombo,
	 * ChartCheckBox and so on, it try to find parent composite's left label as
	 * description of Screen Reader.
	 *
	 * @param chartComposite
	 * @param composite
	 */
	public static void addScreenReaderAccessibility(Control chartComposite, Control composite) {
		Control[] controls = chartComposite.getParent().getChildren();
		for (int i = 1; i < controls.length; i++) {
			if (controls[i] == chartComposite && (controls[i - 1] instanceof Label)) {
				ChartUIUtil.addScreenReaderAccessbility(composite, ((Label) controls[i - 1]).getText());
				break;
			}
		}
	}
}
