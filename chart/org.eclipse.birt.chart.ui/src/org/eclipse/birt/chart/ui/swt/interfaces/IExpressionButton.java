/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

/**
 * A utility class to display Expression builder and button
 */

public interface IExpressionButton
{

	/**
	 * Returns the expression that's saved in model
	 * 
	 * @return the expression that's saved in model
	 */
	String getExpression( );

	/**
	 * Sets the expression that's saved in model
	 * 
	 * @param expr
	 *            the expression that's saved in model
	 */
	void setExpression( String expr );

	/**
	 * Returns the display string in expression builder. This may be different
	 * from the value saved in model.
	 * 
	 * @return the display string in expression builder
	 */
	public String getDisplayExpression( );

	/**
	 * Sets the enabled state
	 * 
	 * @param bEnabled
	 *            enabled state
	 */
	void setEnabled( boolean bEnabled );

	/**
	 * Returns the enabled state
	 * 
	 * @return the enabled state
	 */
	boolean isEnabled( );

}
