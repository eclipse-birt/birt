/***********************************************************************
 * Copyright (c) 2004, 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.swt.wizard.data.YOptionalDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class BubbleSeriesUIProvider extends DefaultSeriesUIProvider {

	private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl"; //$NON-NLS-1$

	public BubbleSeriesUIProvider() {
		super();
	}

	public Composite getSeriesAttributeSheet(Composite parent, Series series, ChartWizardContext context) {
		return new BubbleSeriesAttributeComposite(parent, SWT.NONE, context, series);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesClass()
	 */
	public String getSeriesClass() {
		return SERIES_CLASS;
	}

	public ISelectDataComponent getSeriesDataComponent(int seriesType, SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle) {
		if (seriesType == ISelectDataCustomizeUI.ORTHOGONAL_SERIES) {
			return new BubbleDataDefinitionComponent(seriesDefn, context, sTitle);
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
	 * @see
	 * org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#getCompatibleAxisType(
	 * org.eclipse.birt.chart.model.component.Series)
	 */
	public AxisType[] getCompatibleAxisType(Series series) {
		return new AxisType[] { AxisType.LINEAR_LITERAL, AxisType.LOGARITHMIC_LITERAL, AxisType.DATE_TIME_LITERAL };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#
	 * validateSeriesBindingType(org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider)
	 */
	public void validateSeriesBindingType(Series series, IDataServiceProvider idsp) throws ChartException {
		List<Query> entries = series.getDataDefinition();
		if (entries.size() >= 2) {
			// Only check if Bubble size is numeric. Bubble value can support
			// any types.
			Query query = entries.get(1);
			DataType dataType = idsp.getDataType(query.getDefinition());
			if (dataType == DataType.TEXT_LITERAL || dataType == DataType.DATE_TIME_LITERAL) {
				final ExpressionCodec codec = ChartModelHelper.instance().createExpressionCodec();
				codec.decode(query.getDefinition());
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.DATA_BINDING, codec.getExpression());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#validationIndex
	 * (org.eclipse.birt.chart.model.component.Series)
	 */
	@Override
	public int[] validationIndex(Series series) {
		return new int[] { 0, 1 };
	}

}
