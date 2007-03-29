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

import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.script.api.ChartComponentFactory;
import org.eclipse.birt.chart.script.api.attribute.ILabel;
import org.eclipse.birt.chart.script.api.component.IMarkerLine;
import org.eclipse.birt.chart.script.api.data.IDataElement;

/**
 * 
 */

public class MarkerLineImpl implements IMarkerLine
{

	private MarkerLine line;

	public MarkerLineImpl( MarkerLine line )
	{
		this.line = line;
	}

	public IDataElement getValue( )
	{
		return ChartComponentFactory.convertDataElement( line.getValue( ) );
	}

	public void setValue( IDataElement value )
	{
		line.setValue( ChartComponentFactory.convertIDataElement( value ) );
	}

	public ILabel getTitle( )
	{
		return ChartComponentFactory.convertLabel( line.getLabel( ) );
	}

	public boolean isVisible( )
	{
		return line.getLineAttributes( ).isVisible( );
	}

	public void setTitle( ILabel title )
	{
		line.setLabel( ChartComponentFactory.convertILabel( title ) );
	}

	public void setVisible( boolean visible )
	{
		line.getLineAttributes( ).setVisible( visible );
	}

}
