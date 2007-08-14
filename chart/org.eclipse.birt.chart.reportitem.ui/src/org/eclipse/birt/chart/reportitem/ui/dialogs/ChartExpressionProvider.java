/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Provide a specific expression builder to explode pie slices.
 */

public class ChartExpressionProvider extends ExpressionProvider
{

	public static final String CHART_VARIABLES = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.Category.ChartVariables" );//$NON-NLS-1$

	private static final String DATA_POINTS = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.ChartVariables.DataPoints" );//$NON-NLS-1$

	public static final int CATEGORY_BASE = 0;

	public static final int CATEGORY_WITH_CHART_VARIABLES = 1;

	public static final int CATEGORY_WITH_BIRT_VARIABLES = 2;

	public static final int CATEGORY_WITH_COLUMN_BINDINGS = 4;

	public static final int CATEGORY_WITH_REPORT_PARAMS = 8;

	private final int _categoryStyle;

	public ChartExpressionProvider( )
	{
		this( null, CATEGORY_BASE );
	}

	public ChartExpressionProvider( DesignElementHandle handle,
			int categoryStyle )
	{
		super( handle, true );
		this._categoryStyle = categoryStyle;
		init( );
	}

	private void init( )
	{
		// Filter categories according to the style
		final List filteredList = new ArrayList( 3 );
		if ( ( this._categoryStyle & CATEGORY_WITH_BIRT_VARIABLES ) != CATEGORY_WITH_BIRT_VARIABLES )
		{
			filteredList.add( BIRT_OBJECTS );
		}
		if ( ( this._categoryStyle & CATEGORY_WITH_COLUMN_BINDINGS ) != CATEGORY_WITH_COLUMN_BINDINGS )
		{
			filteredList.add( COLUMN_BINDINGS );
		}
		if ( ( this._categoryStyle & CATEGORY_WITH_REPORT_PARAMS ) != CATEGORY_WITH_REPORT_PARAMS )
		{
			filteredList.add( PARAMETERS );
		}

		if ( !filteredList.isEmpty( ) )
		{
			addFilter( new ExpressionFilter( ) {

				public boolean select( Object parentElement, Object element )
				{
					return !filteredList.contains( element );
				}
			} );
		}
	}

	protected List getCategoryList( )
	{
		List list = super.getCategoryList( );
		if ( ( this._categoryStyle & CATEGORY_WITH_CHART_VARIABLES ) == CATEGORY_WITH_CHART_VARIABLES )
		{
			list.add( CHART_VARIABLES );
		}
		return list;
	}

	protected List getChildrenList( Object parent )
	{
		List list = super.getChildrenList( parent );

		if ( DATA_POINTS.equals( parent ) )
		{
			list.add( ScriptHandler.BASE_VALUE );
			list.add( ScriptHandler.ORTHOGONAL_VALUE );
			list.add( ScriptHandler.SERIES_VALUE );
		}
		else
		{
			if ( CHART_VARIABLES.equals( parent ) )
			{
				list.add( DATA_POINTS );
			}
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#getDisplayText(java.lang.Object)
	 */
	public String getDisplayText( Object element )
	{
		if ( element.equals( ScriptHandler.BASE_VALUE ) )
		{
			return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.BaseValue" );//$NON-NLS-1$;
		}
		else if ( element.equals( ScriptHandler.ORTHOGONAL_VALUE ) )
		{
			return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.OrthogonalValue" );//$NON-NLS-1$
		}
		else if ( element.equals( ScriptHandler.SERIES_VALUE ) )
		{
			return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartExpressionProvider.DataPoints.SeriesValue" );//$NON-NLS-1$
		}
		return super.getDisplayText( element );
	}

}
