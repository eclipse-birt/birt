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

package org.eclipse.birt.data.engine.impl.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.ResultObject;
import org.eclipse.birt.data.engine.impl.PLSUtil;
import org.eclipse.birt.data.engine.odi.IDataSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.olap.data.util.DataType;

/**
 * This class is an implementation of IDataSetPopulator. It wrapped a document.ResultIterator.
 * 
 * The wrapping is executed by following means:
 * 1.For all the columns in ResultIterator's enclosed DataSetResultSet (which reprensents the data from a data set),
 * the ResultClass provided by this class will include them.
 * 2.For all the non-aggr bindings that not directly referred a data set column, the ResultClass provide access to them 
 * as well. The names of those bindings in ResultClass are specified in constructNonReCalBindingDataSetName() method.
 * 3.For all the aggregation bindings that higher than the highest group level defined in List<IGroupInstanceInfo>, the 
 * ResultClass provides access to them as well. The naming convention is same as that of 2.
 */

public class PLSEnabledDataSetPopulator implements IDataSetPopulator
{

	//
	private PLSDataPopulator populator = null;
	private IResultClass resultClass;
	private List<String> originalBindingNames;
	private Map<String, String> datasetColumnNameBindingNameMapping;

	/**
	 * Constructor
	 * 
	 * @param query
	 * @param targetGroups
	 * @param docIt
	 * @throws DataException
	 */
	public PLSEnabledDataSetPopulator( IQueryDefinition query,
			List<IGroupInstanceInfo> targetGroups, ResultIterator docIt )
			throws DataException
	{

		this.populator = new PLSDataPopulator( targetGroups, docIt );
		try
		{
			assert docIt.getExprResultSet( ).getDataSetResultSet( ) != null;
			this.resultClass = populateResultClass( query,
					targetGroups,
					docIt.getExprResultSet( )
							.getDataSetResultSet( )
							.getResultClass( ) );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.odi.IDataSetPopulator#next()
	 */
	public IResultObject next( ) throws DataException
	{
		if( !this.populator.next( ) )
			return null;
		Object[] field = new Object[this.resultClass.getFieldCount( )];

		int proceedField = this.originalBindingNames.size( );
		for ( int i = 0; i < this.resultClass.getFieldCount( ) - proceedField; i++ )
		{
			try
			{
				field[i] = this.populator.getDocumentIterator( )
						.getValue( datasetColumnNameBindingNameMapping.get( this.resultClass.getFieldName( i + 1 ) ));
				}
			catch ( BirtException e )
			{				
				throw DataException.wrap( e );
			}
		}
		
		for ( int i = this.resultClass.getFieldCount( )-proceedField; i < field.length; i++ )
		{
			try
			{
				field[i] = this.populator.getDocumentIterator( )
						.getValue( this.originalBindingNames.get( i + proceedField - this.resultClass.getFieldCount( ) ) );
			}
			catch ( BirtException e )
			{
				throw DataException.wrap( e );
			}
		}
		return new ResultObject( this.resultClass, field );
	}

	/**
	 * Return the result class.
	 * @return
	 */
	public IResultClass getResultClass( )
	{
		return this.resultClass;
	}

	/**
	 * 
	 * @param query
	 * @param targetGroups
	 * @param original
	 * @return
	 * @throws BirtException
	 */
	private IResultClass populateResultClass( IQueryDefinition query,
			List<IGroupInstanceInfo> targetGroups, IResultClass original )
			throws BirtException
	{
		List<ResultFieldMetadata> list = new ArrayList<ResultFieldMetadata>( );
		for ( int i = 1; i <= original.getFieldCount( ); i++ )
		{
			list.add( original.getFieldMetaData( i ) );
		}

		this.originalBindingNames = new ArrayList<String>( );
		this.datasetColumnNameBindingNameMapping = new HashMap<String,String>();
		Iterator<IBinding> bindings = query.getBindings( ).values( ).iterator( );
		while ( bindings.hasNext( ) )
		{
			IBinding binding = bindings.next( );
			if ( PLSUtil.isPLSProcessedBinding( binding ) )
			{
				ResultFieldMetadata rfmeta = new ResultFieldMetadata( -1,
						PLSUtil.constructNonReCalBindingDataSetName( binding.getBindingName( ) ),
						null,
						DataType.getClass( binding.getDataType( ) ),
						null,
						false );
				list.add( rfmeta );
				this.originalBindingNames.add( binding.getBindingName( ) );
			}
			else if ( binding.getExpression( ) instanceof IScriptExpression
					&& (IScriptExpression) binding.getExpression( ) != null
					&& binding.getAggrFunction( ) == null )
			{
				String name = ExpressionUtil.getColumnName( ( (IScriptExpression) binding.getExpression( ) ).getText( ) );
				if ( name != null )
					this.datasetColumnNameBindingNameMapping.put( name,
							binding.getBindingName( ) );
			}
		}
		return new ResultClass( list );
	}
}
