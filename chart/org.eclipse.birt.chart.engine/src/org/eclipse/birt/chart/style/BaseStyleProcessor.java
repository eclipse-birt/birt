/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.StyledComponent;

/**
 * Provides a base implementation of style processor. It should be used as super
 * class of all style processors.
 */

public class BaseStyleProcessor implements IStyleProcessor
{

	private boolean isHighContrast = false;
	public IStyle getStyle( Chart model, StyledComponent name )
	{
		return null;
	}

	public void processStyle( Chart model )
	{

	}

	public void setHighContrast( boolean ishighContrast )
	{
		this.isHighContrast = ishighContrast;
	}

	public boolean isHighContrast( )
	{
		try
		{
			return isHighContrast;
		}
		finally
		{
			// reset the environment
			isHighContrast = false;
		}
	}

}
