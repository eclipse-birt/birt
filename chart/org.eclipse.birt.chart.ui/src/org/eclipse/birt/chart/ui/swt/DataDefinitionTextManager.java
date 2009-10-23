/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;

/**
 * The manager for synchronizing data query, text and background color.
 */

public class DataDefinitionTextManager
{

	private static DataDefinitionTextManager instance;
	private HashMap<Control, IQueryExpressionManager> textCollection = null;

	private DataDefinitionTextManager( )
	{
		textCollection = new HashMap<Control, IQueryExpressionManager>( 10 );
	}

	public synchronized static DataDefinitionTextManager getInstance( )
	{
		if ( instance == null )
			instance = new DataDefinitionTextManager( );
		return instance;
	}

	public void addDataDefinitionText( Control text,
			IQueryExpressionManager queryManager )
	{
		textCollection.put( text, queryManager );
		text.addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( e.widget instanceof Control )
				{
					removeDataDefinitionText( (Control) e.widget );
				}

			}
		} );
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
		for ( Iterator<Control> iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = iterator.next( );
			updateText( text );
		}
	}

	/**
	 * Checks all texts and removes disposed controls
	 * 
	 */
	private void checkAll( )
	{
		List<Control> listToRemove = new ArrayList<Control>( textCollection.size( ) );
		for ( Iterator<Control> iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = iterator.next( );
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
		for ( Iterator<Control> iterator = textCollection.keySet( ).iterator( ); iterator.hasNext( ); )
		{
			Control text = iterator.next( );
			if ( ChartUIUtil.getText( text ).equals( expression ) )
			{
				number++;
			}
		}
		return number;
	}

	public Control findText( Query query )
	{
		Iterator<Map.Entry<Control, IQueryExpressionManager>> iterator = textCollection.entrySet( )
				.iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry<Control, IQueryExpressionManager> entry = iterator.next( );
			if ( entry.getValue( ).getQuery( ) == query )
			{
				return entry.getKey( );
			}
		}
		return null;
	}

	public void updateText( Control text )
	{
		if ( textCollection.containsKey( text ) )
		{
//			IQueryExpressionManager query = textCollection.get( text );
			// ChartUIUtil.setText( text, query.getDisplayExpression( ) );
			Color color = ColorPalette.getInstance( )
					.getColor( ChartUIUtil.getText( text ) );
			text.setBackground( color );
		}
	}

	public void updateText( Query query )
	{
		Control text = findText( query );
		if ( text != null )
		{
			IQueryExpressionManager queryManager = textCollection.get( text );
			// Buzilla #229211. Query definition in model may be different from
			// display expression that is used as the unique id here.
			String displayExpr = queryManager.getDisplayExpression( );
			// ChartUIUtil.setText( text, displayExpr );

			// Bind color to this data definition
			updateControlBackground( text, displayExpr );
		}
	}

	/**
	 * Update query data by specified expression, if current is sharing-binding
	 * case, the expression will be converted and set to query, else directly
	 * set query with the expression.
	 * 
	 * @param query
	 * @param expression
	 * @since 2.3
	 */
	public void updateQuery( Query query, String expression )
	{
		Control control = findText( query );
		if ( control != null )
		{
			IQueryExpressionManager queryManager = textCollection.get( control );
			queryManager.updateQuery( expression );
		}
	}

	public void updateQuery( Control control )
	{
		if ( textCollection.containsKey( control ) )
		{
			IQueryExpressionManager queryManager = textCollection.get( control );
			queryManager.updateQuery( ChartUIUtil.getText( control ) );

			adjustScaleData( queryManager.getQuery( ) );

			// control may be disposed when updating query
			if ( control.isDisposed( ) )
			{
				control = findText( queryManager.getQuery( ) );
			}

			// Bind color to this data definition
			if ( control != null )
			{
				updateControlBackground( control, ChartUIUtil.getText( control ) );
			}
		}
	}

	/**
	 * Binding color to specified control.
	 * 
	 * @param control
	 * @param expression
	 * @since 2.5
	 */
	private void updateControlBackground( Control control, String expression )
	{
		ColorPalette.getInstance( ).putColor( expression );
		control.setBackground( ColorPalette.getInstance( )
				.getColor( expression ) );
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
			if ( object != null )
			{
				object = object.eContainer( );
			}
			else
			{
				return;
			}
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
		else if ( axisType == AxisType.LINEAR_LITERAL
				|| axisType == AxisType.LOGARITHMIC_LITERAL )
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

	/**
	 * Check if expression is valid.
	 * 
	 * @param control
	 * @param expression
	 * @return valid expression or not
	 * @since 2.3
	 */
	public boolean isValidExpression( Control control, String expression )
	{
		if ( textCollection.containsKey( control ) )
		{
			IQueryExpressionManager queryManager = textCollection.get( control );
			return queryManager.isValidExpression( expression );
		}

		return false;
	}

	/**
	 * Check if specified expression is valid to specified query. Now, only for
	 * share binding case, it should check it, other's case still returns true.
	 * 
	 * @param query
	 * @param expr
	 * @param isShareBinding
	 * @since 2.3
	 */
	public boolean isAcceptableExpression( Query query, String expr,
			boolean isShareBinding )
	{
		if ( !isShareBinding )
		{
			return true;
		}

		Control control = findText( query );
		if ( control != null )
		{
			return isValidExpression( control, expr );
		}

		return false;
	}

	/**
	 * Update the tooltip for value data definition component after grouping
	 * changed.
	 * 
	 * @since 2.5
	 */
	public void updateTooltip( )
	{
		for ( IQueryExpressionManager queryExprM : textCollection.values( ) )
		{
			queryExprM.setTooltipForInputControl( );
		}
	}
}
