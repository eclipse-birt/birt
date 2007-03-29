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

package org.eclipse.birt.chart.script.internal.component;

import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.IMarkerRange;
import org.eclipse.birt.chart.script.api.data.IDataElement;

/**
 * 
 */

public class MarkerRangeImpl implements IMarkerRange
{

	private MarkerRange range;

	public MarkerRangeImpl( MarkerRange range )
	{
		this.range = range;
	}

	public IDataElement getEndValue( )
	{
		return ChartComponentFactory.convertDataElement( range.getEndValue( ) );
	}

	public IDataElement getStartValue( )
	{
		return ChartComponentFactory.convertDataElement( range.getStartValue( ) );
	}

	public void setEndValue( IDataElement value )
	{
		range.setEndValue( ChartComponentFactory.convertIDataElement( value ) );
	}

	public void setStartValue( IDataElement value )
	{
		range.setStartValue( ChartComponentFactory.convertIDataElement( value ) );
	}

	public ILabel getTitle( )
	{
		return ChartComponentFactory.convertLabel( range.getLabel( ) );
	}

	public boolean isVisible( )
	{
		return range.getOutline( ).isVisible( );
	}

	public void setTitle( ILabel title )
	{
		range.setLabel( ChartComponentFactory.convertILabel( title ) );
	}

	public void setVisible( boolean visible )
	{
		range.getOutline( ).setVisible( visible );
	}

}
