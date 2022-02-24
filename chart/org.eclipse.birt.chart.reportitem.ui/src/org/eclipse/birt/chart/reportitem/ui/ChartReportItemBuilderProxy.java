/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.IFormatSpecifierHandler;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * This class is a proxy class to create different instance of
 * <code>IUIServiceProvider</code> according to context.
 */

public class ChartReportItemBuilderProxy extends ReportItemBuilderUI implements IUIServiceProvider {

	private ChartReportItemBuilderImpl instance;

	/**
	 * The constructor.
	 */
	public ChartReportItemBuilderProxy() {
		instance = ChartReportItemUIFactory.instance().createReportItemBuilder(null);
	}

	/**
	 * Open the chart with specified task
	 * 
	 * @param taskId specified task to open
	 */
	public ChartReportItemBuilderProxy(String taskId) {
		instance = ChartReportItemUIFactory.instance().createReportItemBuilder(taskId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI#open(org.
	 * eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public int open(final ExtendedItemHandle eih) {
		return instance.open(eih);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.
	 * String, java.lang.Object, java.lang.String)
	 */
	public String invoke(String sExpression, Object context, String sTitle) {
		return instance.invoke(sExpression, context, sTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.
	 * String, java.lang.Object, java.lang.String, boolean)
	 */
	public String invoke(String sExpression, Object context, String sTitle, boolean isChartProvider) {
		return instance.invoke(sExpression, context, sTitle, isChartProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(int,
	 * java.lang.String, java.lang.Object, java.lang.String)
	 */
	public String invoke(int command, String value, Object context, String sTitle) throws ChartException {
		return instance.invoke(command, value, context, sTitle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(org.
	 * eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider.Command,
	 * java.lang.Object[])
	 */
	public Object invoke(Command command, Object... inData) throws ChartException {
		return instance.invoke(command, inData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#
	 * isInvokingSupported()
	 */
	public boolean isInvokingSupported() {
		return instance.isInvokingSupported();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#validate(org.
	 * eclipse.birt.chart.model.Chart, java.lang.Object)
	 */
	public String[] validate(Chart chartModel, Object oContext) {
		return instance.validate(chartModel, oContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getRegisteredKeys
	 * ()
	 */
	public List<String> getRegisteredKeys() {
		return instance.getRegisteredKeys();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getValue(java.
	 * lang.String)
	 */
	public String getValue(String sKey) {
		return instance.getValue(sKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getConvertedValue
	 * (double, java.lang.String, java.lang.String)
	 */
	public double getConvertedValue(double dOriginalValue, String sFromUnits, String sToUnits) {
		return instance.getConvertedValue(dOriginalValue, sFromUnits, sToUnits);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#
	 * isEclipseModeSupported()
	 */
	public boolean isEclipseModeSupported() {
		return instance.isEclipseModeSupported();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#
	 * getFormatSpecifierHandler()
	 */
	public IFormatSpecifierHandler getFormatSpecifierHandler() {
		return instance.getFormatSpecifierHandler();
	}

}
