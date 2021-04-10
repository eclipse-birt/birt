/*******************************************************************************
 * Copyright (c) 2004,2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesButtonEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class DefaultSeriesUIProvider implements ISeriesUIProvider {

	/**
	 * @deprecated
	 */
	public Composite getSeriesAttributeSheet(Composite parent, Series series) {
		return null;
	}

	public Composite getSeriesAttributeSheet(Composite parent, Series series, ChartWizardContext context) {
		return null;
	}

	/**
	 * @deprecated
	 */
	public Composite getSeriesDataSheet(Composite parent, SeriesDefinition seriesdefinition, IUIServiceProvider builder,
			Object oContext) {
		return null;
	}

	public String getSeriesClass() {
		return null;
	}

	public ISelectDataComponent getSeriesDataComponent(int seriesType, SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#
	 * validateSeriesBindingType(org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider)
	 */
	public void validateSeriesBindingType(Series series, IDataServiceProvider idsp) throws ChartException {
		// Do not validate series binding type by default.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#
	 * validateAggregationType (org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.data.SeriesDefinition,
	 * org.eclipse.birt.chart.model.data.SeriesDefinition)
	 */
	public boolean isValidAggregationType(Series series, SeriesDefinition orthSD, SeriesDefinition baseSD) {
		// Do not validate series binding type by default.
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#
	 * getCompatibleAxisType(org.eclipse.birt.chart.model.component.Series)
	 */
	public AxisType[] getCompatibleAxisType(Series series) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#validationIndex
	 * (org.eclipse.birt.chart.model.component.Series)
	 */
	public int[] validationIndex(Series series) {
		return series.getDefinedDataDefinitionIndex();
	}

	public List<ISeriesButtonEntry> getCustomButtons(ChartWizardContext context, SeriesDefinition sd) {
		return Collections.emptyList();
	}

}
