/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal.data;

import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.script.api.data.INumberDataElement;

/**
 * 
 */

public class NumberElementImpl implements INumberDataElement
{

	private NumberDataElement data;

	public NumberElementImpl( NumberDataElement data )
	{
		this.data = data;
	}

	public double getValue( )
	{
		return data.getValue( );
	}

	public boolean isSetValue( )
	{
		return data.isSetValue( );
	}

	public void setValue( double value )
	{
		data.setValue( value );
	}

	public void unsetValue( )
	{
		data.unsetValue( );
	}

}
