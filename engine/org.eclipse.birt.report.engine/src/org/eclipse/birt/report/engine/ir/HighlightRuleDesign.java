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
public class HighlightRuleDesign extends RuleDesign
{

	/**
	 * style defined in this rule.
	 */
	protected StyleDesign style;

	/**
	 * @return Returns the style.
	 */
	public StyleDesign getStyle( )
	{
		return style;
	}

	/**
	 * @param style
	 *            The style to set.
	 */
	public void setStyle( StyleDesign style )
	{
		this.style = style;
	}
}
