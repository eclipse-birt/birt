/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;


/**
 * Defines a Scalar parameter
 */
public interface IScalarParameterDefn extends IParameterDefn
{
	public static final int TEXT_BOX = 0;
	public static final int LIST_BOX = 1;
	public static final int RADIO_BUTTON = 2;
	public static final int CHECK_BOX = 3;

	public static final int AUTO = 0;
	public static final int LEFT = 1;
	public static final int CENTER = 2;
	public static final int RIGHT = 3;

	/**
	 * @return default value for the parameter
	 *  
	 */
	Object getDefaultValue( );


	/**
	 * @return whether the input value needs to be concealed (i.e., password,
	 *         bank account number, etc.)
	 */
	boolean isValueConcealed( );

	/**
	 * @return whether the parameter allow null value. If it does not, the end
	 *         user has to supply a value for the parameter before the report
	 *         can be run
	 */
	boolean allowNull( );

	/**
	 * @return whether the parameter allow empty string as input. If not, the
	 *         end user has to supply a string value that is non-empty
	 */
	boolean allowBlank( );

	/**
	 * @return the formatting instructions for the parameter value within the
	 *         parameter UI
	 *  
	 */
	String getFormat( );

	/**
	 * @return the control type used in the parameter UI. Supports TEXT_BOX
	 *         (default), LIST_BOX, RADIO_BUTTON and CHECK_BOX.
	 */
	int getControlType( );

	/**
	 * @return how the items should appear in the UI. Choices are AUTO
	 *         (default), LEFT, CENTER and RIGHT
	 */
	int getAlignment( );

	/**
	 * @return get a parameter value selection object, from which a list of
	 *         parameter values and label values can be retrieved.
	 */
	public IParameterSelectionList getParameterSelectionList( );

}