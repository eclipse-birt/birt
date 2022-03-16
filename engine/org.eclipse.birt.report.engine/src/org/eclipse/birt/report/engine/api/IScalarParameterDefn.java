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

package org.eclipse.birt.report.engine.api;

/**
 * Defines a scalar parameter
 */
public interface IScalarParameterDefn extends IParameterDefn {
	int TEXT_BOX = 0;
	int LIST_BOX = 1;
	int RADIO_BUTTON = 2;
	int CHECK_BOX = 3;
	int AUTO_SUGGEST = 4;

	int AUTO = 0;
	int LEFT = 1;
	int CENTER = 2;
	int RIGHT = 3;

	/**
	 * returns whether the user can enter a value different from values in a
	 * selection list Applies only to parameters with a selection list. Usually, a
	 * parameter with allowNewValue=true is displayed as a combo-box, while a
	 * parameter with allowNewValue=false is displayed as a list. This is only a UI
	 * gesture. Engine does not validate whether the value passed in is in the list.
	 *
	 * @return whether the user can enter a value different from all values in the
	 *         list. Applies only when the parameter has a selection list. Default
	 *         is true.
	 */
	boolean allowNewValues();

	/**
	 * returns whether the UI should display the seleciton list in a fixed order.
	 * Only applies to parameters with a selection list.
	 *
	 * @return whether the UI should display the selection list in fixed order as
	 *         the values appear in the list. Default is true.
	 */
	boolean displayInFixedOrder();

	/**
	 * @return whether the input value needs to be concealed (i.e., password, bank
	 *         account number, etc.)
	 */
	boolean isValueConcealed();

	/**
	 * @deprecated
	 * @return whether the parameter allow null value. If it does not, the end user
	 *         has to supply a value for the parameter before the report can be run
	 */
	@Deprecated
	boolean allowNull();

	/**
	 * @deprecated
	 * @return whether the parameter allow empty string as input. If not, the end
	 *         user has to supply a string value that is non-empty
	 */
	@Deprecated
	boolean allowBlank();

	/**
	 * @return the formatting instructions for the parameter value within the
	 *         parameter UI
	 *
	 */
	String getDisplayFormat();

	/**
	 * @return the control type used in the parameter UI. Supports TEXT_BOX
	 *         (default), LIST_BOX, RADIO_BUTTON and CHECK_BOX.
	 */
	int getControlType();

	/**
	 * @return how the items should appear in the UI. Choices are AUTO (default),
	 *         LEFT, CENTER and RIGHT
	 */
	int getAlignment();

	/**
	 * @return the default value
	 */
	String getDefaultValue();

	/**
	 * @return the scalar parameter type, like "simple", "multi-value" or "ad-hoc"
	 */
	String getScalarParameterType();

	/**
	 * Set parameter type.
	 *
	 * @param type scalar parameter type
	 */
	void setScalarParameterType(String type);

	/**
	 * @return the number of values that a picklist could have
	 */
	int getAutoSuggestThreshold();
}
