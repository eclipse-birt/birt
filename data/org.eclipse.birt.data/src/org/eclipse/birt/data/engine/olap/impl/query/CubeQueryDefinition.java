
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseLinkDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.NamedObject;


/**
 * 
 */

public class CubeQueryDefinition extends NamedObject
		implements
			ICubeQueryDefinition 
{
	private IEdgeDefinition columnEdge, rowEdge, pageEdge;
	private List measureList, bindingList, filterList, sortList, computedMeasureList, derivedMeasureList;
	private List<ICubeOperation> cubeOperations;
	private String queryResultsID;
	private boolean cacheQueryResults;
	private boolean needAccessFactTable;
	private int breakHierarchyOption = 0;
	private String ID;
	
	private Set<IBaseLinkDefinition> links = new HashSet<IBaseLinkDefinition>( );
	
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
		this.derivedMeasureList = new ArrayList();
		this.cubeOperations = new ArrayList<ICubeOperation>();
		this.cacheQueryResults = false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#addBinding(org.eclipse.birt.data.engine.api.IBinding)
	 */
	public void addBinding( IBinding binding )
	{
		if( needReconstructure( binding) )
		{
			binding = constructNewBinding( binding );
		}
		bindingList.add( binding );
	}
	
	/**
	 * 
	 * @param binding
	 * @return
	 */
	private static boolean needReconstructure( IBinding binding )
	{
		try
		{
			if ( binding.getAggrFunction( ) != null && binding.getExpression( ) == null )
			{
				IAggrFunction aggrFunction = AggregationManager.getInstance( ).getAggregation( binding.getAggrFunction( ) );
				if( aggrFunction == null )
					return false;
				IParameterDefn[] parameterDefn = aggrFunction.getParameterDefn( );
				if ( parameterDefn != null && parameterDefn.length > 0 && parameterDefn[0].isDataField( ) )
				{
					return true;
				}
			}
		}
		catch ( DataException e )
		{
			
		}
		return false;
	}
	
	/**
	 * Before new aggregation extension point is introduced, The binding
	 * expression is serve as first argument of aggregation. This function is
	 * used to construct a old version binding. 
	 * 
	 * @param binding
	 * @return
	 */
	private static IBinding constructNewBinding( IBinding binding )
	{
		IBinding newBinding = null;
		try
		{
			newBinding = new Binding( binding.getBindingName( ) );
			List aggregationOn = binding.getAggregatOns( );
			if( aggregationOn != null )
			{
				for( int i = 0 ; i < aggregationOn.size( ); i++ )
				{
					newBinding.addAggregateOn( (String) aggregationOn.get( i ) );
				}
			}
			
			if( binding.getArguments( )!= null )
			{
				for( int i = 1 ; i < binding.getArguments( ).size(); i++ )
				{
					newBinding.addArgument( (IBaseExpression) binding.getArguments( ).get( i ) );
				}
			}
			if ( binding.getArguments( ).size( ) > 0 )
			{
				newBinding.setExpression( (IBaseExpression) binding.getArguments( ).get( 0 ) );
			}
			newBinding.setAggrFunction( binding.getAggrFunction() );
			newBinding.setDataType( binding.getDataType() );
			newBinding.setDisplayName( binding.getDisplayName() );
			newBinding.setFilter( binding.getFilter() );
			newBinding.setTimeFunction( binding.getTimeFunction( ) );
		}
		catch (DataException e)
		{
			// TODO Auto-generated catch block
		}
		return newBinding;
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
		else if ( type == ICubeQueryDefinition.PAGE_EDGE )
		{
			pageEdge = new EdgeDefinition( "PAGE_EDGE" );
			return pageEdge;
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
		//The best API should be designed as returning IBinding[]
		//Here, return an unmodifiable list to avoid the internal bindingList being modified outside from the result of this API
		return Collections.unmodifiableList( bindingList );
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
		else if( type == ICubeQueryDefinition.PAGE_EDGE )
		{
			return this.pageEdge;
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
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getMeasures()
	 */
	public List getDerivedMeasures( )
	{
		return this.derivedMeasureList;
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
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#createCalculatedMeasure(java.lang.String, int, org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public IDerivedMeasureDefinition createDerivedMeasure( String measureName, int type,
			IBaseExpression expr ) throws DataException
	{
		DerivedMeasureDefinition dmd = new DerivedMeasureDefinition( measureName, type, expr );
		this.derivedMeasureList.add( dmd );
		return dmd;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#getComputedMeasures()
	 */
	public List getComputedMeasures( )
	{
		return this.computedMeasureList;
	}

	public void addCubeOperation( ICubeOperation cubeOperation )
	{
		if (cubeOperation == null)
		{
			throw new NullPointerException("cubeOperation is null");
		}
		cubeOperations.add(cubeOperation);
	}

	public ICubeOperation[] getCubeOperations( )
	{
		return cubeOperations.toArray( new ICubeOperation[0] );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#needAccessFactTable()
	 */
	public boolean needAccessFactTable( )
	{
		return needAccessFactTable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition#setNeedAccessFactTable(boolean)
	 */
	public void setNeedAccessFactTable( boolean needAccessFactTable )
	{
		this.needAccessFactTable = needAccessFactTable;
	}

	public String getID( )
	{
		return this.ID;
	}

	public void setID( String ID )
	{
		this.ID = ID;
	}

    /**
     * Clone itself
     */
    public ICubeQueryDefinition clone( )
    {
        CubeQueryDefinition cloned = new CubeQueryDefinition( this.getName( ) );
        cloneFields( cloned );

        return cloned;
    }

    /*
     * Clone fields. Separate this method for extension classes.
     */
    protected void cloneFields( CubeQueryDefinition cloned )
    {
        cloned.bindingList.addAll( this.bindingList );
        cloned.breakHierarchyOption = this.breakHierarchyOption;
        cloned.cacheQueryResults = this.cacheQueryResults;
        cloned.columnEdge = this.columnEdge != null
                ? this.columnEdge.clone( )
                : null;
        cloned.computedMeasureList.addAll( this.computedMeasureList );
        cloned.cubeOperations.addAll( this.cubeOperations );
        cloned.derivedMeasureList.addAll( this.derivedMeasureList );
        cloned.filterList.addAll( this.filterList );
        cloned.ID = this.ID;
        cloned.measureList.addAll( this.measureList );
        cloned.needAccessFactTable = this.needAccessFactTable;
        cloned.pageEdge = this.pageEdge != null ? this.pageEdge.clone( ) : null;
        cloned.queryResultsID = this.queryResultsID;
        cloned.rowEdge = this.rowEdge != null ? this.rowEdge.clone( ) : null;
        cloned.sortList.addAll( this.sortList );
    }
    
    @Override
    public Set<IBaseLinkDefinition> getLinks( )
    {
        return this.links;
    }

    @Override
    public void addLink( IBaseLinkDefinition link )
    {
        this.links.add( link );
    }

}
