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

package org.eclipse.birt.chart.script.internal.attribute;

import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;
import org.eclipse.birt.chart.script.api.attribute.IText;
import org.eclipse.birt.report.model.api.extension.IColor;
import org.eclipse.birt.report.model.api.extension.IFont;

/**
 * 
 */

public class TextImpl implements IText
{

	private Text text;

	public TextImpl( Text text )
	{
		this.text = text;
	}

	public String getValue( )
	{
		return text.getValue( );
	}

	public void setValue( String value )
	{
		text.setValue( value );
	}

	public IColor getColor( )
	{
		return ChartComponentFactory.convertColor( text.getColor( ) );
	}

	public IFont getFont( )
	{
		return ChartComponentFactory.convertFont( text.getFont( ) );
	}

	public void setColor( IColor color )
	{
		text.setColor( ChartComponentFactory.convertIColor( color ) );
	}

	public void setFont( IFont font )
	{
		text.setFont( ChartComponentFactory.convertIFont( font ) );
	}

}
