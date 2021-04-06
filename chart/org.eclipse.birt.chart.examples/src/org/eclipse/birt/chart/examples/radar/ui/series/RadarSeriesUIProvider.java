/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.series;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.integrate.SimpleSeriesButtonEntry;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesButtonEntry;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.swt.wizard.data.YOptionalDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * RadarSeriesUIProvider
 */
public class RadarSeriesUIProvider extends DefaultSeriesUIProvider {

	private static final String SERIES_CLASS = RadarSeriesImpl.class.getName();

	/**
	 * 
	 */
	public RadarSeriesUIProvider() {
		super();
	}

	public Composite getSeriesAttributeSheet(Composite parent, Series series, ChartWizardContext context) {
		return new RadarSeriesAttributeComposite(parent, SWT.NONE, context, series);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesClass ()
	 */
	public String getSeriesClass() {
		return SERIES_CLASS;
	}

	public ISelectDataComponent getSeriesDataComponent(int seriesType, SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle) {
		if (seriesType == ISelectDataCustomizeUI.ORTHOGONAL_SERIES) {
			return new BaseDataDefinitionComponent(BaseDataDefinitionComponent.BUTTON_AGGREGATION,
					ChartUIConstants.QUERY_VALUE, seriesDefn, ChartUIUtil.getDataQuery(seriesDefn, 0), context, sTitle);
		} else if (seriesType == ISelectDataCustomizeUI.GROUPING_SERIES) {
			BaseDataDefinitionComponent ddc = new YOptionalDataDefinitionComponent(
					BaseDataDefinitionComponent.BUTTON_GROUP, ChartUIConstants.QUERY_OPTIONAL, seriesDefn,
					seriesDefn.getQuery(), context, sTitle);
			return ddc;
		}
		return new DefaultSelectDataComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#
	 * validateSeriesBindingType(org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider)
	 */
	public void validateSeriesBindingType(Series series, IDataServiceProvider idsp) throws ChartException {
		Iterator<Query> iterEntries = series.getDataDefinition().iterator();
		while (iterEntries.hasNext()) {
			Query query = iterEntries.next();
			DataType dataType = idsp.getDataType(query.getDefinition());
			if (dataType == DataType.TEXT_LITERAL || dataType == DataType.DATE_TIME_LITERAL) {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.DATA_BINDING, query.getDefinition());
			}
		}
	}

	public List<ISeriesButtonEntry> getCustomButtons(ChartWizardContext context, SeriesDefinition sd) {
		List<ISeriesButtonEntry> list = new ArrayList<ISeriesButtonEntry>(3);
		// Only the first series can set advanced settings
		if (ChartUtil.getOrthogonalSeriesDefinitions(context.getModel(), 0).get(0) == sd) {
			RadarSeries series = getDesignTimeSeries(sd);
			ISeriesButtonEntry radarLineEntry = new SimpleSeriesButtonEntry(".RadarLine", //$NON-NLS-1$
					Messages.getString("RadarSeriesUIProvider.Label.RadarLine"), //$NON-NLS-1$
					new RadarLineSheet(Messages.getString("RadarSeriesUIProvider..Title.RadarLine"), context, false, //$NON-NLS-1$
							series), true);
			list.add(radarLineEntry);

			ISeriesButtonEntry webLabelEntry = new SimpleSeriesButtonEntry(".RadarWebLabels", //$NON-NLS-1$
					Messages.getString("RadarSeriesUIProvider.Label.WebLabels"), //$NON-NLS-1$
					new RadarWebLabelSheet(Messages.getString("RadarSeriesUIProvider.Title.WebLabels"), //$NON-NLS-1$
							context, false, series),
					true);
			list.add(webLabelEntry);

			ISeriesButtonEntry categoryLabelEntry = new SimpleSeriesButtonEntry(".RadarCategoryLabels", //$NON-NLS-1$
					Messages.getString("RadarSeriesUIProvider.Label.CategoryLabels"), //$NON-NLS-1$
					new RadarCategoryLabelSheet(Messages.getString("RadarSeriesUIProvider.Title.CategoryLabels"), //$NON-NLS-1$
							context, false, series),
					true);
			list.add(categoryLabelEntry);
		}
		return list;
	}

	protected RadarSeries getDesignTimeSeries(SeriesDefinition sd) {
		final EList<Series> el = sd.getSeries();
		Series se;
		for (int i = 0; i < el.size(); i++) {
			se = el.get(i);
			if (se.getDataSet() == null && se instanceof RadarSeries) {
				return (RadarSeries) se;
			}
		}
		return null;
	}
}
