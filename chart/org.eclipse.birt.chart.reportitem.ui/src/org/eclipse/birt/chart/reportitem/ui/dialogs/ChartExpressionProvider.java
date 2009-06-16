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

import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * Provide a specific expression builder depending on context
 */

public class ChartExpressionProvider extends ExpressionProvider
{

	public static final String CHART_VARIABLES = Messages.getString( "ChartExpressionProvider.Category.ChartVariables" );//$NON-NLS-1$

	private static final String DATA_POINTS = Messages.getString( "ChartExpressionProvider.ChartVariables.DataPoints" );//$NON-NLS-1$

	private static final String LEGEND_ITEMS = Messages.getString( "ChartExpressionProvider.ChartVariables.LegendItems" );//$NON-NLS-1$

	private static final String JAVASCRIPT = Messages.getString( "ChartExpressionProvider.ChartVariables.JavaScript" );//$NON-NLS-1$

	private static final String JAVASCRIPT_EVENT = Messages.getString( "ChartExpressionProvider.ChartVariables.Event" );//$NON-NLS-1$;

	private static final String JAVASCRIPT_EVENT_PARAMETER = "evt";//$NON-NLS-1$

	public static final int CATEGORY_BASE = 0;

	public static final int CATEGORY_WITH_DATA_POINTS = 1;

	public static final int CATEGORY_WITH_BIRT_VARIABLES = 2;

	public static final int CATEGORY_WITH_COLUMN_BINDINGS = 4;

	public static final int CATEGORY_WITH_REPORT_PARAMS = 8;

	public static final int CATEGORY_WITH_JAVASCRIPT = 16;

	public static final int CATEGORY_WITH_LEGEND_ITEMS = 32;

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
		final List<Object> filteredList = new ArrayList<Object>( 3 );

		// Always remove Cube since measure/dimension expression are not
		// supported
		filteredList.add( CURRENT_CUBE );

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

	protected List<Object> getCategoryList( )
	{
		List<Object> list = super.getCategoryList( );
		if ( ( this._categoryStyle & CATEGORY_WITH_DATA_POINTS ) == CATEGORY_WITH_DATA_POINTS
				|| ( this._categoryStyle & CATEGORY_WITH_JAVASCRIPT ) == CATEGORY_WITH_JAVASCRIPT
				|| ( this._categoryStyle & CATEGORY_WITH_LEGEND_ITEMS ) == CATEGORY_WITH_LEGEND_ITEMS )
		{
			if ( !list.contains( CHART_VARIABLES ) )
			{
				list.add( CHART_VARIABLES );
			}
		}
		return list;
	}

	protected List<Object> getChildrenList( Object parent )
	{
		List<Object> list = super.getChildrenList( parent );

		if ( CHART_VARIABLES.equals( parent ) )
		{
			if ( ( this._categoryStyle & CATEGORY_WITH_DATA_POINTS ) == CATEGORY_WITH_DATA_POINTS )
				list.add( DATA_POINTS );
			if ( ( this._categoryStyle & CATEGORY_WITH_JAVASCRIPT ) == CATEGORY_WITH_JAVASCRIPT )
				list.add( JAVASCRIPT );
			if ( ( this._categoryStyle & CATEGORY_WITH_LEGEND_ITEMS ) == CATEGORY_WITH_LEGEND_ITEMS )
				list.add( LEGEND_ITEMS );

		}
		else if ( DATA_POINTS.equals( parent ) )
		{
			list.add( ScriptHandler.BASE_VALUE );
			list.add( ScriptHandler.ORTHOGONAL_VALUE );
			list.add( ScriptHandler.SERIES_VALUE );
		}
		else if ( LEGEND_ITEMS.equals( parent ) )
		{
			list.add( IActionRenderer.LEGEND_ITEM_TEXT );
			list.add( IActionRenderer.LEGEND_ITEM_VALUE );
		}
		else if ( JAVASCRIPT.equals( parent ) )
		{
			list.add( JAVASCRIPT_EVENT_PARAMETER );
		}

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider#
	 * getDisplayText(java.lang.Object)
	 */
	public String getDisplayText( Object element )
	{
		if ( element.equals( ScriptHandler.BASE_VALUE ) )
		{
			return Messages.getString( "ChartExpressionProvider.DataPoints.BaseValue" );//$NON-NLS-1$;
		}
		else if ( element.equals( ScriptHandler.ORTHOGONAL_VALUE ) )
		{
			return Messages.getString( "ChartExpressionProvider.DataPoints.OrthogonalValue" );//$NON-NLS-1$
		}
		else if ( element.equals( ScriptHandler.SERIES_VALUE ) )
		{
			return Messages.getString( "ChartExpressionProvider.DataPoints.SeriesValue" );//$NON-NLS-1$
		}
		else if ( element.equals( JAVASCRIPT_EVENT_PARAMETER ) )
		{
			return JAVASCRIPT_EVENT;
		}
		return super.getDisplayText( element );
	}

}
