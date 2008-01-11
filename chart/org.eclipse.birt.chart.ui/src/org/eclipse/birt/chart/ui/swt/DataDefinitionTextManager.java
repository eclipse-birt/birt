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

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * The manager for synchronizing data query, text and background color.
 */

public class DataDefinitionTextManager
{

	private static DataDefinitionTextManager instance;
	private HashMap textCollection = null;

	private DataDefinitionTextManager( )
	{
		textCollection = new HashMap( 10 );
	}

	public synchronized static DataDefinitionTextManager getInstance( )
	{
		if ( instance == null )
			instance = new DataDefinitionTextManager( );
		return instance;
	}

	public void addDataDefinitionText( Control text, Query query )
	{
		textCollection.put( text, query );
	}

	public void removeDataDefinitionText( Control text )
	{
		textCollection.remove( text );
	}

	public void removeAll( )
	{
		textCollection.clear( );
	}

	public void refreshAll( )
	{
		checkAll( );
		for ( Iterator iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = (Control) iterator.next( );
			updateText( text );
		}
	}

	/**
	 * Checks all texts and removes disposed controls
	 * 
	 */
	private void checkAll( )
	{
		ArrayList listToRemove = new ArrayList( textCollection.size( ) );
		for ( Iterator iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = (Control) iterator.next( );
			if ( text.isDisposed( ) )
			{
				listToRemove.add( text );
			}
		}
		for ( int i = 0; i < listToRemove.size( ); i++ )
		{
			textCollection.remove( listToRemove.get( i ) );
		}
	}

	public int getNumberOfSameDataDefinition( String expression )
	{
		checkAll( );
		int number = 0;
		for ( Iterator iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = (Control) iterator.next( );
			if ( getText( text ).equals( expression ) )
			{
				number++;
			}
		}
		return number;
	}

	public Control findText( Query query )
	{
		if ( textCollection.containsValue( query ) )
		{
			Iterator iterator = textCollection.entrySet( ).iterator( );
			while ( iterator.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iterator.next( );
				if ( entry.getValue( ) == query )
				{
					return (Control) entry.getKey( );
				}
			}
		}
		return null;
	}

	public void updateText( Control text )
	{
		if ( textCollection.containsKey( text ) )
		{
			Query query = (Query) textCollection.get( text );
			setText( text, query.getDefinition( ) );
			Color color = ColorPalette.getInstance( )
					.getColor( getText( text ) );
			text.setBackground( color );
		}
	}

	public void updateText( Query query )
	{
		Control text = findText( query );
		if ( text != null )
		{
			setText( text, query.getDefinition( ) );
			ColorPalette.getInstance( ).putColor( query.getDefinition( ) );
			text.setBackground( ColorPalette.getInstance( )
					.getColor( getText( text ) ) );
		}
	}

	public void updateQuery( Control control )
	{
		if ( textCollection.containsKey( control ) )
		{
			Query query = (Query) textCollection.get( control );
			query.setDefinition( getText( control ) );
			
			adjustScaleData( query );
			
			// Bind color to this data definition
			ColorPalette.getInstance( ).putColor( getText( control ) );
			control.setBackground( ColorPalette.getInstance( )
					.getColor( getText( control ) ) );
		}
	}

	/**
	 * Adjust min/max data element of scale when current expression type is
	 * different with old expression type.
	 * 
	 * @param query
	 * @since BIRT 2.3
	 */
	private void adjustScaleData( Query query )
	{
		EObject object = query;
		while ( !( object instanceof Axis ) )
		{
			object = object.eContainer( );
		}

		Axis axis = (Axis) object;
		AxisType axisType = axis.getType( );
		DataElement minElement = axis.getScale( ).getMin( );
		DataElement maxElement = axis.getScale( ).getMax( );

		if ( axisType == AxisType.DATE_TIME_LITERAL )
		{
			if ( !( minElement instanceof DateTimeDataElement ) )
			{
				axis.getScale( ).setMin( null );
			}
			if ( !( maxElement instanceof DateTimeDataElement ) )
			{
				axis.getScale( ).setMax( null );
			}
		}
		else if ( axisType == AxisType.TEXT_LITERAL )
		{
			if ( !( minElement instanceof TextDataElement ) )
			{
				axis.getScale( ).setMin( null );
			}
			if ( !( maxElement instanceof TextDataElement ) )
			{
				axis.getScale( ).setMax( null );
			}
		}
		else if ( axisType == AxisType.LINEAR_LITERAL ||
				axisType == AxisType.LOGARITHMIC_LITERAL )
		{
			if ( !( minElement instanceof NumberDataElement ) )
			{
				axis.getScale( ).setMin( null );
			}
			if ( !( maxElement instanceof NumberDataElement ) )
			{
				axis.getScale( ).setMax( null );
			}
		}
	}

	private String getText( Control control )
	{
		if ( control instanceof Text )
		{
			return ( (Text) control ).getText( );
		}
		if ( control instanceof Combo )
		{
			return ( (Combo) control ).getText( );
		}
		return ""; //$NON-NLS-1$
	}

	private void setText( Control control, String text )
	{
		if ( control instanceof Text )
		{
			( (Text) control ).setText( text );
		}
		else if ( control instanceof Combo )
		{
			( (Combo) control ).setText( text );
		}
	}
}
