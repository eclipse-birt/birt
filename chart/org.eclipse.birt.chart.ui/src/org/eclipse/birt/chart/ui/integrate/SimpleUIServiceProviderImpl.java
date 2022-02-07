/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.integrate;

import java.util.List;
import java.util.Vector;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IFormatSpecifierHandler;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Simple implementation of IUIServiceProvider for integration.
 */
public class SimpleUIServiceProviderImpl implements IUIServiceProvider {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui/integrate"); //$NON-NLS-1$

	private IFormatSpecifierHandler formatSpecifierHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.
	 * String)
	 */
	public String invoke(String sExpression, Object oContext, String sTitle) {
		logger.log(ILogger.WARNING, Messages.getString("SimpleUIServiceProviderImpl.Warn.Placeholder")); //$NON-NLS-1$
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(java.lang.
	 * String)
	 */
	public String invoke(String sExpression, Object oContext, String sTitle, boolean isChartProvider) {
		logger.log(ILogger.WARNING, Messages.getString("SimpleUIServiceProviderImpl.Warn.Placeholder")); //$NON-NLS-1$
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#validate(org.
	 * eclipse.birt.chart.model.Chart, java.lang.Object)
	 */
	public String[] validate(Chart chartModel, Object oContext) {
		return null;
	}

	/**
	 * Fetches the list of registered keys for externalizing chart content
	 * 
	 * @return List containing available keys for externalized content
	 */
	public List getRegisteredKeys() {
		List list = new Vector();
		list.add("SampleKey"); //$NON-NLS-1$
		return list;
	}

	/**
	 * Fetches the value for the externalized resource identified by the specified
	 * key
	 * 
	 * @return String that represents the value for the specified resource in the
	 *         current locale
	 */
	public String getValue(String sKey) {
		if (sKey.equals("SampleKey")) //$NON-NLS-1$
		{
			return "Sample Value"; //$NON-NLS-1$
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getConvertedValue
	 * (double, java.lang.String, java.lang.String)
	 */
	public double getConvertedValue(double dOriginalValue, String sFromUnits, String sToUnits) {
		return dOriginalValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(int,
	 * java.lang.String, java.lang.Object, java.lang.String)
	 */
	public String invoke(int command, String value, Object context, String sTitle) throws ChartException {
		switch (command) {
		case COMMAND_HYPERLINK:
		case COMMAND_HYPERLINK_DATAPOINTS:
			Shell shell = new Shell(Display.getDefault(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
			ChartUIUtil.bindHelp(shell, ChartHelpContextIds.DIALOG_EDIT_URL);
			SimpleHyperlinkBuilder hb = new SimpleHyperlinkBuilder(shell);
			try {
				hb.setInputString(value);
				if (sTitle != null) {
					hb.setTitle(hb.getTitle() + " - " + sTitle); //$NON-NLS-1$
				}
				if (hb.open() == Window.OK) {
					value = hb.getResultString();
				}
			} catch (Exception e) {
				throw new ChartException(ChartUIPlugin.ID, ChartException.UNDEFINED_VALUE, e);
			}
			break;
		}
		return value;
	}

	public boolean isInvokingSupported() {
		return true;
	}

	public boolean isEclipseModeSupported() {
		return false;
	}

	public Object invoke(Command command, Object... inData) throws ChartException {

		Object outData = null;
		switch (command) {
		case EXPRESS_BUTTON_CREATE:
			final Control control = (Control) inData[1];
			new Label((Composite) inData[0], SWT.NONE);
			IExpressionButton ceb = new IExpressionButton() {

				public void setExpression(String expr) {
					if (expr != null) {
						ChartUIUtil.setText(control, expr);
					}
				}

				public void setEnabled(boolean bEnabled) {
					control.setEnabled(bEnabled);
				}

				public boolean isEnabled() {
					return control.isEnabled();
				}

				public String getExpression() {
					return ChartUIUtil.getText(control);
				}

				public String getDisplayExpression() {
					return getExpression();
				}

				public void addListener(Listener listener) {
					// not implemented
				}

				public void setAccessor(EAttributeAccessor<String> accessor) {
					// not implemented
				}

				public String getExpressionType() {
					return null;
				}

				public boolean isCube() {
					return false;
				}

				public void setBindingName(String bindingName, boolean bNotifyEvents) {
					// not implemented
				}

				public void setExpression(String expr, boolean bNotifyEvents) {
					// not implemented
				}

				public void setAssitField(IAssistField assistField) {
					// not implemented
				}

				public void setPredefinedQuery(Object[] predefinedQuery) {
					// not implemented
				}
			};
			outData = ceb;
			break;
		}
		return outData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#
	 * getFormatSpecifierHandler()
	 */
	public IFormatSpecifierHandler getFormatSpecifierHandler() {
		return this.formatSpecifierHandler;
	}

	/**
	 * Sets format specifier handler object.
	 * 
	 * @param handler
	 */
	public void setFormatSpecifierHandler(IFormatSpecifierHandler handler) {
		formatSpecifierHandler = handler;
	}
}
