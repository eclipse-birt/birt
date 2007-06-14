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

package org.eclipse.birt.report.item.crosstab.core.util;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Utility clas for crosstab.
 */

public class CrosstabUtil implements ICrosstabConstants
{

	private CrosstabUtil( )
	{
	}

	/**
	 * 
	 * @param element
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		try
		{
			return extendedItem.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			return null;
		}
	}

	/**
	 * 
	 * @param element
	 * @param extensionName
	 * @return report item if found, otherwise null
	 */
	public static IReportItem getReportItem( DesignElementHandle element,
			String extensionName )
	{
		if ( !( element instanceof ExtendedItemHandle ) )
			return null;
		if ( extensionName == null )
			throw new IllegalArgumentException( "extension name can not be null" ); //$NON-NLS-1$
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) element;
		if ( extensionName.equals( extendedItem.getExtensionName( ) ) )
		{
			try
			{
				return extendedItem.getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				return null;
			}
		}
		return null;
	}

	/**
	 * Check the containment logic for dimension from outside of crosstab
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param dimension
	 * @return
	 */
	public static boolean canContain( CrosstabReportItemHandle crosstab,
			DimensionHandle dimension )
	{
		if ( crosstab != null && dimension != null )
		{
			CubeHandle currentCube = crosstab.getCube( );

			if ( currentCube == null )
			{
				return true;
			}

			// check containment consistence
			if ( dimension.getElement( )
					.isContentOf( currentCube.getElement( ) ) )
			{
				for ( int i = 0; i < crosstab.getDimensionCount( ROW_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstab.getDimension( ROW_AXIS_TYPE,
							i );

					if ( dv.getCubeDimension( ) == dimension )
					{
						return false;
					}
				}

				for ( int i = 0; i < crosstab.getDimensionCount( COLUMN_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstab.getDimension( COLUMN_AXIS_TYPE,
							i );

					if ( dv.getCubeDimension( ) == dimension )
					{
						return false;
					}
				}

				return true;
			}

		}

		return false;
	}

	/**
	 * Check the containment logic for measure from outside of crosstab
	 * 
	 * @param crosstab
	 * @param measure
	 * @return
	 */
	public static boolean canContain( CrosstabReportItemHandle crosstab,
			MeasureHandle measure )
	{
		if ( crosstab != null && measure != null )
		{
			CubeHandle currentCube = crosstab.getCube( );

			if ( currentCube == null )
			{
				return true;
			}

			// check containment consistence
			if ( measure.getElement( ).isContentOf( currentCube.getElement( ) ) )
			{
				for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
				{
					MeasureViewHandle mv = crosstab.getMeasure( i );

					if ( mv.getCubeMeasure( ) == measure )
					{
						return false;
					}
				}

				return true;
			}

		}

		return false;
	}

	/**
	 * Checks if the add/remove aggregation operation should be perform on all
	 * measures on given axis type
	 */
	public static boolean isAggregationAffectAllMeasures(
			CrosstabReportItemHandle crosstabItem, int axisType )
	{
		String measureDirection = crosstabItem.getMeasureDirection( );

		if ( ( MEASURE_DIRECTION_HORIZONTAL.equals( measureDirection ) && ( axisType == ROW_AXIS_TYPE ) )
				|| ( MEASURE_DIRECTION_VERTICAL.equals( measureDirection ) && ( axisType == COLUMN_AXIS_TYPE ) ) )
		{
			return true;
		}

		return false;
	}

}
