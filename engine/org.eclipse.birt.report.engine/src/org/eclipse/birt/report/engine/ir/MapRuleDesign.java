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

package org.eclipse.birt.report.engine.ir;

/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class MapRuleDesign extends RuleDesign
{

	/**
	 * display text key
	 */
	protected String displayKey;
	/**
	 * display text
	 */
	protected String displayText;

	/**
	 * @param displayKey
	 *            The displayKey to set.
	 */
	public void setDisplayText( String displayKey, String displayText )
	{
		this.displayKey = displayKey;
		this.displayText = displayText;
	}

	/**
	 * @return Returns the displayText.
	 */
	public String getDisplayText( )
	{
		return displayText;
	}

	/**
	 * @return Returns the displayKey.
	 */
	public String getDisplayKey( )
	{
		return displayKey;
	}
}
