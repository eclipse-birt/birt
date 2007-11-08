
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.impl.query;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;


/**
 * 
 */

public class CubeQueryDefinition extends NamedObject
		implements
			ICubeQueryDefinition 
{
	private IEdgeDefinition columnEdge, rowEdge;
	private List measureList, bindingList, filterList, sortList, computedMeasureList;
	private String queryResultsID;
	private boolean cacheQueryResults;
	private int breakHierarchyOption = 0;
	
	/**
	 * Constructor. The name of CubeQueryDefinition must equal to the name
	 * of cube being queried.
	 * @param name
	 */
	public CubeQueryDefinition( String name )
	{
		super( name );
		this.bindingList = new ArrayList();
		this.measureList = new ArrayList();
		this.filterList = new ArrayList();
		this.sortList = new ArrayList();
		this.computedMeasureList = new ArrayList();
		this.cacheQueryResults = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#addBinding(org.eclipse.birt.data.engine.api.IBinding)
	 */
	public void addBinding( IBinding binding )
	{
		bindingList.add( binding );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#addFilter(org.eclipse.birt.data.engine.api.IFilterDefinition)
	 */
	public void addFilter( IFilterDefinition filterDefn )
	{
		this.filterList.add( filterDefn );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#addSort(org.eclipse.birt.data.engine.api.ISortDefinition)
	 */
	public void addSort( ISortDefinition sortDefn )
	{
		this.sortList.add( sortDefn );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#createEdge(int)
	 */
	public IEdgeDefinition createEdge( int type )
	{
		if ( type == ICubeQueryDefinition.COLUMN_EDGE )
		{
			columnEdge = new EdgeDefinition( "COLUMN_EDGE" );
			return columnEdge;
		}
		else if ( type == ICubeQueryDefinition.ROW_EDGE )
		{
			rowEdge = new EdgeDefinition( "ROW_EDGE" );
			return rowEdge;
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#createMeasure(java.lang.String)
	 */
	public IMeasureDefinition createMeasure( String name )
	{
		IMeasureDefinition measureDfn = new MeasureDefinition( name );
		measureList.add( measureDfn );
		return measureDfn;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getBindings()
	 */
	public List getBindings( )
	{
		return this.bindingList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getEdge(int)
	 */
	public IEdgeDefinition getEdge( int type )
	{
		if ( type == ICubeQueryDefinition.COLUMN_EDGE )
		{
			return this.columnEdge;
		}
		else if ( type == ICubeQueryDefinition.ROW_EDGE )
		{
			return this.rowEdge;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getFilters()
	 */
	public List getFilters( )
	{
		return this.filterList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getMeasures()
	 */
	public List getMeasures( )
	{
		return this.measureList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getSorts()
	 */
	public List getSorts( )
	{
		return this.sortList;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getQueryResultsID()
	 */
	public String getQueryResultsID( )
	{
		return this.queryResultsID;
	}
	
	/**
	 * 
	 * @param queryResultsID
	 */
	public void setQueryResultsID( String queryResultsID )
	{
		this.queryResultsID = queryResultsID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#cacheQueryResults()
	 */
	public boolean cacheQueryResults( )
	{
		return this.cacheQueryResults;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#setCacheQueryResults(boolean)
	 */
	public void setCacheQueryResults( boolean cacheQueryResults )
	{
		this.cacheQueryResults = cacheQueryResults;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getFilterOption()
	 */
	public int getFilterOption( )
	{
		return breakHierarchyOption;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#setFilterOption(int)
	 */
	public void setFilterOption( int breakHierarchyOption )
	{
		this.breakHierarchyOption = breakHierarchyOption;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#createComputedMeasure(java.lang.String, int, org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public IComputedMeasureDefinition createComputedMeasure( String measureName, int type,
			IBaseExpression expr ) throws DataException
	{
		ComputedMeasureDefinition cmd = new ComputedMeasureDefinition( measureName, type, expr );
		this.computedMeasureList.add( cmd );
		return cmd;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getComputedMeasures()
	 */
	public List getComputedMeasures( )
	{
		return this.computedMeasureList;
	}
}
