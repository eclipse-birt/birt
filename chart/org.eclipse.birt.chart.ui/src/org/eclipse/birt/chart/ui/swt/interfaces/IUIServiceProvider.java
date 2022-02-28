/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;

/**
 * IUIServiceProvider
 */
public interface IUIServiceProvider {

	public enum Command {
		EXPRESS_BUTTON_CREATE
	}

	/**
	 * Constant indicating a fatal error in the model
	 */
	int FATAL_ERRORS = -10;

	/**
	 * Constant indicating a major (though not fatal) problem in the model
	 */
	int CRITICAL_ERRORS = -5;

	/**
	 * Constant indicating a minor error in the model
	 */
	int MINOR_ERRORS = -1;

	/**
	 * Constant indicating no detectable problems exist in the model
	 */
	int NO_ERRORS = 0;

	/**
	 * Constant indicating possible problems detected in the model
	 */
	int POSSIBLE_ERRORS = 1;

	/**
	 * Constant indicating an expression builder to input data bindings.
	 */
	int COMMAND_EXPRESSION_DATA_BINDINGS = 1;

	/**
	 * Constant indicating an expression builder to input chart data point
	 * variables.
	 */
	int COMMAND_EXPRESSION_CHART_DATAPOINTS = 2;

	/**
	 * Constant indicating an expression builder to input triggers except for value
	 * series.
	 */
	int COMMAND_EXPRESSION_TRIGGERS_SIMPLE = 3;

	/**
	 * Constant indicating an expression builder to input script (one type of
	 * triigers) for value series.
	 */
	int COMMAND_EXPRESSION_SCRIPT_DATAPOINTS = 4;

	/**
	 * Constant indicating an expression builder to input tooltips (one type of
	 * triggers) for value series.
	 */
	int COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS = 5;

	/**
	 * Constant indicating an expression builder to input tootips (one type of
	 * triggers ) for value series while using cube case
	 */
	int COMMAND_CUBE_EXPRESSION_TOOLTIPS_DATAPOINTS = 9;

	/**
	 * Constant indicating a common expression builder.
	 *
	 * @deprecated
	 */
	@Deprecated
	int COMMAND_EXPRESSION = 1;

	/**
	 * Constant indicating an expression builder specifically for chart.
	 *
	 * @deprecated
	 */
	@Deprecated
	int COMMAND_CHART_EXPRESSION = COMMAND_EXPRESSION_CHART_DATAPOINTS;

	/**
	 * Constant indicating a builder to input hyper-link except for data points.
	 */
	int COMMAND_HYPERLINK = 6;

	/**
	 * Constant indicating a builder to input hyper-link for data points.
	 */
	int COMMAND_HYPERLINK_DATAPOINTS = 7;

	/**
	 * Constant indicating a builder to input hyper-link for legend.
	 */
	int COMMAND_HYPERLINK_LEGEND = 8;

	/**
	 * Constant indicating a builder to input hyper-link for data points but raw
	 * data expression not allowed.
	 */
	int COMMAND_HYPERLINK_DATAPOINTS_SIMPLE = 10;

	/**
	 * Constant indicating a builder to select resource file
	 */
	int COMMAND_RESOURCE_SELECTION_DIALOG = 11;

	/**
	 * This method will be used by the Chart Builder UI to invoke the expression
	 * builder with any previously defined expression. The parameter may be null if
	 * a new expression is to be built.
	 *
	 * @param sExpression the expression to be displayed in the builder (after
	 *                    re-entry)
	 * @param oContext    the application-specific context used by the Expression
	 *                    Builder for each invocation
	 * @param sTitle      the title to be used for the Expression Builder Dialog
	 *
	 * @return The final expression string built by the user in the expression
	 *         builder
	 *
	 * @deprecated use invoke( int command, String value, Object context, String
	 *             sTitle ) instead.
	 */
	@Deprecated
	String invoke(String sExpression, Object Context, String sTitle);

	/**
	 * This method will be used by the Chart Builder UI to invoke the expression
	 * builder with any previously defined expression. The parameter may be null if
	 * a new expression is to be built.
	 *
	 * @param sExpression     the expression to be displayed in the builder (after
	 *                        re-entry)
	 * @param oContext        the application-specific context used by the
	 *                        Expression Builder for each invocation
	 * @param sTitle          the title to be used for the Expression Builder Dialog
	 * @param isChartProvider specified for chart expression provider.
	 * @return The final expression string built by the user in the expression
	 *         builder
	 * @deprecated use invoke( int command, String value, Object context, String
	 *             sTitle ) instead.
	 */
	@Deprecated
	String invoke(String sExpression, Object Context, String sTitle, boolean isChartProvider);

	/**
	 * This method will be used by the Chart Builder UI to invoke numerous builder
	 * by a specified command.
	 *
	 * @param command Indicate which command will be executed.
	 * @param value   initial value.
	 * @param context command context.
	 * @param sTitle  dialog title if applicable.
	 *
	 * @since 2.0
	 */
	String invoke(int command, String value, Object context, String sTitle) throws ChartException;

	/**
	 * This method will be used by the Chart Builder UI to invoke numerous builder
	 * by a specified command.
	 *
	 * @param command
	 * @param inData
	 * @return any objects that caller needs
	 * @throws ChartException
	 */
	Object invoke(Command command, Object... inData) throws ChartException;

	/**
	 * Returns whether all outside builder invokings are supported
	 *
	 * @since 2.1
	 */
	boolean isInvokingSupported();

	/**
	 * This method will be used by the Chart Builder UI to validate the model and
	 * show any error messages before the user leaves the dialog.
	 *
	 * @param chartModel the model to be validated
	 * @param oContext   the application-specific context associated with the
	 *                   extended chart item
	 * @return an array of user-friendly messages indicating problems with the model
	 */
	String[] validate(Chart chartModel, Object oContext);

	/**
	 * Fetches the list of registered keys for externalizing chart content
	 *
	 * @return List containing available keys for externalized content
	 */
	List<String> getRegisteredKeys();

	/**
	 * Fetches the value for the specified key from the properties file appropriate
	 * for the current locale
	 *
	 * @param sKey the lookup key for the externalized string
	 * @return the value associated with the key for the current locale, null if
	 *         resource key is blank
	 */
	String getValue(String sKey);

	/**
	 * Gets the result of converting the given value between the specified absolute
	 * units of measurement. Any implementation of this method needs to support
	 * conversion between at least the following units: Inches, Centimeters,
	 * Millimeters, Points and Pixels
	 *
	 * @param dOriginalValue the value to be converted
	 * @param sFromUnits     the units of measurement from which the conversion is
	 *                       to be done
	 * @param sToUnits       the units of measurement to which the conversion is to
	 *                       be done
	 * @return the converted value
	 */
	double getConvertedValue(double dOriginalValue, String sFromUnits, String sToUnits);

	/**
	 * Returns whether the application is running under Eclipse Mode
	 *
	 * @since 2.2
	 */
	boolean isEclipseModeSupported();

	/**
	 * Creates a UI to process format specifier.
	 *
	 * @return
	 */
	IFormatSpecifierHandler getFormatSpecifierHandler();
}
