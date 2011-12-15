/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.AbstractChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIHelper;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

/**
 * This class defines common chart UI methods.
 */

public class ChartUIExtensionUtil
{

	public static int PROPERTY_UPDATE = ChartElementUtil.PROPERTY_UPDATE;
	public static int PROPERTY_UNSET = ChartElementUtil.PROPERTY_UNSET;

	/**
	 * Returns 'auto' string.
	 * 
	 * @return Auto message
	 */
	public static String getAutoMessage( )
	{
		return Messages.getString( "ItemLabel.Auto" ); //$NON-NLS-1$
	}

	/**
	 * Returns an string array and start with 'auto' item.
	 * 
	 * @param items
	 * @return string items with Auto
	 */
	public static String[] getItemsWithAuto( String[] items )
	{
		List<String> names = new ArrayList<String>( Arrays.asList( items ) );
		names.add( 0, getAutoMessage( ) );
		return names.toArray( new String[]{} );
	}

	/**
	 * Creates a combo list with specified items.
	 * 
	 * @param parent
	 * @param items
	 * @return new Combo
	 */
	public static Combo createCombo( Composite parent, String[] items )
	{
		Combo c = new Combo( parent, SWT.DROP_DOWN | SWT.READ_ONLY );
		c.setItems( items );
		return c;
	}

	/**
	 * Checks if the 'auto' item is selected in specified combo list.
	 * 
	 * @param combo
	 * @return true means Auto is selected
	 */
	public static boolean isAutoSelection( Combo combo )
	{
		return combo != null && ( combo.getSelectionIndex( ) == 0 );
	}

	/**
	 * Populates series type list.
	 * 
	 * @param htSeriesNames
	 * @param cmbTypes
	 * @param context
	 * @param allChartType
	 * @param currentSeries
	 */
	public static void populateSeriesTypesList(
			Hashtable<String, Series> htSeriesNames, Combo cmbTypes,
			ChartWizardContext context, Collection<IChartType> allChartType,
			Series currentSeries )
	{
		IChartUIHelper helper = context.getUIFactory( ).createUIHelper( );
		IChartType currentChartType = ChartUIUtil.getChartType( context.getModel( )
				.getType( ) );

		// Populate Series Types List
		cmbTypes.removeAll( );
		if ( helper.canCombine( currentChartType, context ) )
		{
			Orientation orientation = ( (ChartWithAxes) context.getModel( ) ).getOrientation( );
			Iterator<IChartType> iterTypes = allChartType.iterator( );
			while ( iterTypes.hasNext( ) )
			{
				IChartType type = iterTypes.next( );
				Series newSeries = type.getSeries( false );

				if ( helper.canCombine( type, context ) )
				{
					if ( newSeries instanceof AreaSeries
							&& context.getModel( ).getDimension( ) == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL )
					{
						continue;
					}
					if ( !( newSeries instanceof StockSeries )
							|| ( orientation.getValue( ) == Orientation.VERTICAL ) )
					{
						String sDisplayName = newSeries.getDisplayName( );
						htSeriesNames.put( sDisplayName, newSeries );
						cmbTypes.add( sDisplayName );
					}

					// Select the same series type
					if ( type.getName( )
							.equals( context.getModel( ).getType( ) ) )
					{
						cmbTypes.select( cmbTypes.getItemCount( ) - 1 );
					}
				}
			}
			String sDisplayName = currentSeries.getDisplayName( );
			cmbTypes.setText( sDisplayName );
		}
		else
		{
			String seriesName = currentSeries.getDisplayName( );
			cmbTypes.add( seriesName );
			cmbTypes.select( 0 );
		}

	}
	
	/**
	 * Check if the state of specified button allows to enable UI component.
	 * 
	 * @param button
	 * @return true if the state of specified button allows to enable UI component.
	 */
	public static boolean canEnableUI( AbstractChartCheckbox button )
	{
		return button.getSelectionState( ) != ChartCheckbox.STATE_UNSELECTED;
	}
	
	/**
	 * Checks if specified EMF object is set invisible.
	 * 
	 * @param obj
	 * @return true if specified EMF object is set invisible.
	 */
	public static boolean isSetInvisible( EObject obj )
	{
		boolean isSetInvisible = false;
		try
		{
			Method m;
			m = obj.getClass( ).getMethod( "isSetVisible" ); //$NON-NLS-1$
			isSetInvisible = (Boolean)m.invoke( obj );
			
			m = obj.getClass( ).getMethod( "isVisible" ); //$NON-NLS-1$
			isSetInvisible = isSetInvisible && !( (Boolean)m.invoke( obj ) );
		}
		catch ( SecurityException e )
		{
			// Do nothing.
		}
		catch ( NoSuchMethodException e )
		{
			// Do nothing.
		}
		catch ( IllegalArgumentException e )
		{
			// Do nothing.
		}
		catch ( IllegalAccessException e )
		{
			// Do nothing.
		}
		catch ( InvocationTargetException e )
		{
			// Do nothing.
		}
		return isSetInvisible;
	}
}
