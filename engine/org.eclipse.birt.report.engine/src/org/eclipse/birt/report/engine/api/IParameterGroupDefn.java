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

import java.util.ArrayList;
import java.util.Locale;

/**
 * The interface for objects which visually groups report parameters.
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public interface IParameterGroupDefn extends IParameterDefnBase
{

	/**
	 * Gets the additional pop-up help text associated with the group.
	 * 
	 * @return The help text.
	 */
	public String getHelpText( );

	/**
	 * Get the help text of the specified locale.
	 * 
	 * @param locale
	 *            locale
	 * @return The help text.
	 */
	public String getHelpText( Locale locale );

	/**
	 * @return The set of parameters that appear inside the group.
	 */
	public ArrayList getParameters( );
}