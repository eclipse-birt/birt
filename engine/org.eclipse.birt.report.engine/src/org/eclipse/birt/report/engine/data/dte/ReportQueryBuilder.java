/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.data.dte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.QueryExecutionHints;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil;
import org.eclipse.birt.report.engine.adapter.ExpressionUtil;
import org.eclipse.birt.report.engine.adapter.ITotalExprBindings;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ResultMetaData;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryContext;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.extension.internal.QueryContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.RuleDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * visit the report design and prepare all report queries and sub-queries to
 * send to data engine
 * 
 */
public class ReportQueryBuilder
{

	protected static Logger logger = Logger.getLogger( ReportQueryBuilder.class
			.getName( ) );

	private ExpressionUtil expressionUtil;

	private QueryBuilderVisitor queryBuilder;

	/**
	 * query and it's IDs
	 */
	protected HashMap<IDataQueryDefinition, String> queryIDs;

	/**
	 * query and result metaData
	 */
	protected HashMap<IBaseQueryDefinition, ResultMetaData> resultMetaData;

	/**
	 * a collection of all the queries
	 */
	protected Collection<IDataQueryDefinition> queries;

	/**
	 * entry point to the report
	 */
	protected Report report;

	/**
	 * the execution context
	 */
	protected ExecutionContext context;

	/**
	 * the max rows per query
	 */
	protected int maxRows = 0;

	protected IQueryContext queryContext;

	protected DataRequestSession dteSession;

	/**
	 * used to register the unresolved query reference.
	 */
	protected Map<ReportItemHandle, List<ReportItemDesign>> unresolvedQueryReferences = new HashMap<ReportItemHandle, List<ReportItemDesign>>( );

	/**
	 * @param report
	 *            the entry point to the report design
	 * @param context
	 *            the execution context
	 */
	protected ReportQueryBuilder( Report report, ExecutionContext context,
			DataRequestSession dteSession )
	{
		expressionUtil = new ExpressionUtil( );
		queryBuilder = createQueryBuilderVisitor( );
		this.dteSession = dteSession;
		this.report = report;
		this.context = context;

		// get max rows per query
		if ( null != this.context )
		{
			maxRows = this.context.getMaxRowsPerQuery( );
			if ( maxRows == -1 )
			{
				IReportEngine engine = this.context.getEngine( );
				if ( null != engine )
				{
					EngineConfig engineConfig = engine.getConfig( );
					if ( null != engineConfig )
					{
						maxRows = engineConfig.getMaxRowsPerQuery( );
					}
				}
			}
		}

		queryContext = new QueryContext( context, this );
	}

	public void build( )
	{
		synchronized ( report )
		{
			if ( report.getQueries( ).isEmpty( ) )
			{
				queries = report.getQueries( );
				// first clear the collection in case the caller call this
				// function
				// more than once.
				queries.clear( );

				queryIDs = report.getQueryIDs( );
				queryIDs.clear( );

				resultMetaData = report.getResultMetaData( );
				resultMetaData.clear( );

				// visit master page
				for ( int i = 0; i < report.getPageSetup( )
						.getMasterPageCount( ); i++ )
				{
					MasterPageDesign masterPage = report.getPageSetup( )
							.getMasterPage( i );
					if ( masterPage != null )
					{
						SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) masterPage;
						for ( int j = 0; j < pageDesign.getHeaderCount( ); j++ )
						{
							build( null, pageDesign.getHeader( j ) );
						}
						for ( int j = 0; j < pageDesign.getFooterCount( ); j++ )
						{
							build( null, pageDesign.getFooter( j ) );
						}
					}
				}

				// visit report
				for ( int i = 0; i < report.getContentCount( ); i++ )
					build( null, report.getContent( i ) );

				checkQueries( );
			}
		}
	}

	/**
	 * @param parentQuery
	 *            parent query
	 * @param design
	 *            current root design
	 * @return queries array of this design
	 */
	public IDataQueryDefinition[] build( IDataQueryDefinition parentQuery,
			ReportItemDesign design )
	{
		synchronized ( report )
		{
			Object result = design.accept( queryBuilder, parentQuery );

			if ( result == null )
			{
				return null;
			}
			IDataQueryDefinition[] queries = (IDataQueryDefinition[]) result;
			design.setQueries( queries );
			if ( !design.useCachedResult( ) )
			{
				for ( int i = 0; i < queries.length; i++ )
				{
					IDataQueryDefinition query = queries[i];
					if ( query != null )
					{
						/*
						 * before 2.1, the user can only defined one query for a
						 * item, so the generated query id is the id of element.
						 * after 2.2, we changes the interface, there could be
						 * multiple queries, so the first query is un-changed,
						 * but the remain queries will have the sequence id.
						 */
						String queryId = String.valueOf( design.getID( ) );
						if ( i > 0 )
						{
							queryId = queryId + "_" + String.valueOf( i );
						}
						if ( query instanceof IQueryDefinition ) 
						{
							query.setName(queryId);
						}
						this.queryIDs.put( query, queryId );
						// we do not support cube's metaData now. And we so do
						// support CUB data's extration.
						if ( query instanceof IBaseQueryDefinition )
						{
							IBaseQueryDefinition baseQuery = (IBaseQueryDefinition) query;
							ResultMetaData metaData = new ResultMetaData(
									baseQuery, design.getHandle( ) );
							resultMetaData.put( baseQuery, metaData );
						}
						registerQueryAndElement( query, design );
						if ( !( query instanceof ISubqueryDefinition ) )
						{
							this.queries.add( query );
						}
						else if ( query instanceof ISubqueryDefinition )
						{
							// TODO: chart engine make a mistake here
							if ( !( parentQuery instanceof IBaseQueryDefinition ) )
							{
								context
										.addException(
												design.getHandle( ),
												new EngineException(
														MessageConstants.SUBQUERY_CREATE_ERROR,
														design.getID( ) ) );
							}

							IBaseQueryDefinition pQuery = (IBaseQueryDefinition) parentQuery;
							Collection subQueries = pQuery.getSubqueries( );
							if ( !subQueries.contains( query ) )
							{
								subQueries.add( query );
							}
						}
					}
				}
				registerQueryToHandle( design, queries );
				resolveQueryReference( design, queries );
				return queries;
			}
			else
			{
				for ( int i = 0; i < queries.length; i++ )
				{
					IDataQueryDefinition query = queries[i];
					// we do not support cube's metaData now. And we so do
					// support CUB data's extration.
					if ( query instanceof IBaseQueryDefinition )
					{
						IBaseQueryDefinition baseQuery = (IBaseQueryDefinition) query;
						ResultMetaData metaData = new ResultMetaData( baseQuery, design.getHandle( ) );
						resultMetaData.put( baseQuery, metaData );
					}
				}
				registerQueryToHandle( design, queries );
				resolveQueryReference( design, queries );
				return null;
			}
		}
	}

	/**
	 * link the query and report item
	 * 
	 * @param query
	 * @param reportItem
	 */
	private void registerQueryAndElement( IDataQueryDefinition query,
			ReportItemDesign reportItem )
	{
		assert query != null && reportItem != null;
		HashMap map = report.getReportItemToQueryMap( );
		assert map != null;
		map.put( query, reportItem );
	}

	/**
	 * register the query to handle
	 * 
	 * @param handle
	 * @param query
	 */
	private void registerQueryToHandle( ReportItemDesign reportItem,
			IDataQueryDefinition[] queries )
	{
		DesignElementHandle handle = reportItem.getHandle( );
		assert handle instanceof ReportItemHandle;
		ReportItemHandle itemHandle = (ReportItemHandle) handle;
		report.setQueryToReportHandle( itemHandle, queries );
	}

	private void resolveQueryReference( ReportItemDesign reportItem,
			IDataQueryDefinition[] queries )
	{
		DesignElementHandle handle = reportItem.getHandle( );
		assert handle instanceof ReportItemHandle;
		ReportItemHandle itemHandle = (ReportItemHandle) handle;
		if ( unresolvedQueryReferences.containsKey( itemHandle ) )
		{
			List<ReportItemDesign> items = unresolvedQueryReferences
					.get( itemHandle );
			for ( int i = 0; i < items.size( ); i++ )
			{
				ReportItemDesign item = items.get( i );
				build( null, item );
			}
			unresolvedQueryReferences.remove( itemHandle );
			for ( int i = 0; i < queries.length; i++ )
			{
				IDataQueryDefinition referenceQuery = queries[i];
				if ( referenceQuery instanceof BaseQueryDefinition )
				{
					( (BaseQueryDefinition) referenceQuery )
							.setCacheQueryResults( true );
				}
				else if ( referenceQuery instanceof ICubeQueryDefinition )
				{
					( (ICubeQueryDefinition) referenceQuery )
							.setCacheQueryResults( true );
				}
				else
				{
					// FIXME: throw out an exception
					throw new IllegalStateException(
							"unsupported query, report item: "
									+ reportItem.getID( ) );
				}
			}
		}
	}
	
	private void checkQueries( )
	{
		Set failedIDs = new HashSet( );
		Iterator<List<ReportItemDesign>> itr = unresolvedQueryReferences
				.values( ).iterator( );
		while ( itr.hasNext( ) )
		{
			List<ReportItemDesign> list = itr.next( );
			for ( ReportItemDesign design : list )
			{
				failedIDs.add( design.getName( ) != null
						? design.getName( )
						: design.getID( ) );
			}
		}
		for ( Object o : failedIDs )
		{
			EngineException ex = new EngineException(
					MessageConstants.QUERY_NOT_BUILT_ERROR, new Object[]{o} );
			context.addException( ex );
			logger.log( Level.WARNING, ex.getMessage( ), ex );
		}
	}
	
	protected QueryBuilderVisitor createQueryBuilderVisitor( )
    {
        return new QueryBuilderVisitor( );
    }

	/**
	 * The visitor class that actually builds the report query
	 */
	protected class QueryBuilderVisitor extends DefaultReportItemVisitorImpl
	{

		public Object visitTemplate( TemplateDesign template, Object value )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
		 */
		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			IDataQueryDefinition query;
			if ( container.useCachedResult( ) )
			{
				query = getRefenceQuery( container );
				if ( query == null )
				{
					registerUnresolvedQueryReference( container );
					return null;
				}
			}
			else
			{
				query = createQuery( container, (IDataQueryDefinition) value );
			}

			for ( int i = 0; i < container.getItemCount( ); i++ )
				build( query, container.getItem( i ) );
			try
			{
				transformExpressions( container, query );
			}
			catch ( BirtException ex )
			{
				context.addException( container.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
		 */
		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			IDataQueryDefinition query;
			if ( grid.useCachedResult( ) )
			{
				query = getRefenceQuery( grid );
				if ( query == null )
				{
					registerUnresolvedQueryReference( grid );
					return null;
				}
			}
			else
			{
				query = createQuery( grid, (IDataQueryDefinition) value );
			}

			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				build( query, grid.getRow( i ) );
			}
			try
			{
				transformExpressions( grid, query );
			}
			catch ( BirtException ex )
			{
				context.addException( grid.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
		 */
		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			IDataQueryDefinition query;
			if ( image.useCachedResult( ) )
			{
				query = getRefenceQuery( image );
				if ( query == null )
				{
					registerUnresolvedQueryReference( image );
					return null;
				}
			}
			else
			{
				query = createQuery( image, (IDataQueryDefinition) value );
			}
			if( query != value && query instanceof BaseQueryDefinition )
			{
				setUsesDetails( (BaseQueryDefinition)query );
			}
			try
			{
				if ( image.getImageSource( ) == ImageItemDesign.IMAGE_EXPRESSION )
				{
					Expression newImageExpression = transformExpression( image
							.getImageExpression( ), query );
					Expression newImageFormat = transformExpression( image
							.getImageFormat( ), query );
					image.setImageExpression( newImageExpression,
							newImageFormat );
				}
				else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_URI )
				{
					Expression newImageUri = transformExpression( image
							.getImageUri( ), query );
					image.setImageUri( newImageUri );
				}
				else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_FILE )
				{
					Expression newImageUri = transformExpression( image
							.getImageUri( ), query );
					image.setImageFile( newImageUri );
				}

				transformExpressions( image, query );
				return getResultQuery( query, value );
			}
			catch ( BirtException ex )
			{
				context.addException( image.getHandle( ), ex );
				return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
		 */
		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			IDataQueryDefinition query;
			if ( label.useCachedResult( ) )
			{
				query = getRefenceQuery( label );
				if ( query == null )
				{
					registerUnresolvedQueryReference( label );
					return null;
				}
			}
			else
			{
				query = createQuery( label, (IDataQueryDefinition) value );
			}
			if( query != value && query instanceof BaseQueryDefinition )
			{
				setUsesDetails( (BaseQueryDefinition)query );
			}
			try
			{
				transformExpressions( label, query );
			}
			catch ( BirtException ex )
			{
				context.addException( label.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitExtendedItem(org.eclipse.birt.report.engine.ir.ExtendedItemDesign)
		 */
		public Object visitExtendedItem( ExtendedItemDesign item, Object value )
		{
			// create user-defined generation-time helper object
			ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );

			ReportItemHandle referenceHandle = getDataBindingReference( handle );
			if ( referenceHandle != null )
			{
				IDataQueryDefinition[] queries = report
						.getQueryByReportHandle( referenceHandle );
				if ( queries != null && queries.length > 0 )
				{
					for ( int i = 0; i < queries.length; i++ )
					{
						IDataQueryDefinition referenceQuery = queries[i];
						if ( referenceQuery instanceof BaseQueryDefinition )
						{
							( (BaseQueryDefinition) referenceQuery )
									.setCacheQueryResults( true );
						}
						else if ( referenceQuery instanceof ICubeQueryDefinition )
						{
							( (ICubeQueryDefinition) referenceQuery )
									.setCacheQueryResults( true );
						}
					}
					IDataQueryDefinition query = queries[0];
					if ( query instanceof IBaseQueryDefinition )
					{
						try
						{
							transformExpressions( item,
									(IBaseQueryDefinition) query );
						}
						catch ( BirtException ex )
						{
							context.addException( item.getHandle( ), ex );
						}
					}
					return queries;
				}
				else
				{
					registerUnresolvedQueryReference( item );
					return null;
				}
			}

			// TODO: check in plugin registry whetherthe needQuery property is
			// set to host or item.
			// Only do the following for "host"
			IReportItemQuery itemQuery = context.getExtendedItemManager( )
					.createQuery( handle );
			IDataQueryDefinition[] queries = null;
			IDataQueryDefinition parentQuery = (IDataQueryDefinition) value;

			if ( itemQuery != null )
			{
				try
				{
					itemQuery.setModelObject( handle );
					itemQuery.setQueryContext( queryContext );
					queries = itemQuery.createReportQueries( parentQuery );
				}
				catch ( BirtException ex )
				{
					context.addException( handle, ex );
				}
				if ( queries != null )
				{
					if ( queries.length > 0 )
					{
						IDataQueryDefinition query = queries[0];
						if ( query instanceof IBaseQueryDefinition )
						{
							try
							{
								IBaseQueryDefinition baseQuery = (IBaseQueryDefinition) query;
								baseQuery.setMaxRows( maxRows );
								transformExpressions( item, baseQuery );

								// Fix compatibility bug :211547. extended item
								// may
								// define query by itself and doesn't keep query
								// name compatible.
								// Engine change the query name so that it can
								// keep compatible.
								// Only sub-query has the problem because
								// sub-query name need to
								// be used to load query.
								if ( query instanceof ISubqueryDefinition )
								{
									ISubqueryDefinition subQuery = (ISubqueryDefinition) query;
									String name = String
											.valueOf( item.getID( ) );
									queries[0] = changeSubqueryName( subQuery,
											name );
								}
							}
							catch ( BirtException ex )
							{
								context.addException( handle, ex );
							}
						}
					}
					return queries;
				}
			}
			IDataQueryDefinition query = createQuery( item, parentQuery );
			try
			{
				transformExpressions( item, query );
			}
			catch ( BirtException ex )
			{
				context.addException( item.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		private SubqueryDefinition changeSubqueryName(
				ISubqueryDefinition subQuery, String name )
				throws DataException
		{
			IQueryDefinitionUtil queryCopyUtil = dteSession
					.getQueryDefinitionUtil( );
			SubqueryDefinition subqueryDefinition = queryCopyUtil
					.createSubqueryDefinition( name, subQuery );
			return subqueryDefinition;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
		 */
		public Object visitListItem( ListItemDesign list, Object value )
		{
			IDataQueryDefinition query;
			if ( list.useCachedResult( ) )
			{
				query = getRefenceQuery( list );
				if ( query == null )
				{
					registerUnresolvedQueryReference( list );
					return null;
				}
			}
			else
			{
				query = createQuery( list, (IDataQueryDefinition) value );
			}
			BaseQueryDefinition baseQuery = (BaseQueryDefinition) query;
			if ( query == null )
			{
				handleListingBand( list.getHeader( ), baseQuery, true, null );
				handleListingBand( list.getFooter( ), baseQuery, true, null );
			}
			else
			{
				handleListingQuery( list, baseQuery );
				handleListingBand( list.getHeader( ), baseQuery, true, null );

				SlotHandle groupsSlot = ( (ListHandle) list.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < list.getGroupCount( ); i++ )
				{
					handleListingGroup( list.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), query );
				}

				BandDesign detail = list.getDetail();
				setUsesDetails(detail, baseQuery);
				handleListingBand( detail, baseQuery, false, null );

				handleListingBand( list.getFooter( ), baseQuery, true, null );

			}
			try
			{
				transformExpressions( list, baseQuery );
			}
			catch ( BirtException ex )
			{
				context.addException( list.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		public Object visitReportItem( ReportItemDesign item, Object value )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
		 */
		public Object visitTextItem( TextItemDesign text, Object value )
		{
			IDataQueryDefinition query;
			if ( text.useCachedResult( ) )
			{
				query = getRefenceQuery( text );
				if ( query == null )
				{
					registerUnresolvedQueryReference( text );
					return null;
				}
			}
			else
			{
				query = createQuery( text, (IDataQueryDefinition) value );
			}
			HashMap<String, Expression> exprs = text.getExpressions( );
			if ( exprs != null )
			{
				for ( Map.Entry<String, Expression> entry : exprs.entrySet( ) )
				{
					try
					{
						Expression newExpr = transformExpression( entry
								.getValue( ), query );
						entry.setValue( newExpr );
					}
					catch ( BirtException ex )
					{
						context.addException( text.getHandle( ), ex );
						entry.setValue( null );
					}
				}
			}
			if( query != value && query instanceof BaseQueryDefinition )
			{
				setUsesDetails( (BaseQueryDefinition)query );
			}
			try
			{
				transformExpressions( text, query );
			}
			catch ( BirtException ex )
			{
				context.addException( text.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public void handleColumn( ColumnDesign column,
				IDataQueryDefinition query )
		{
			try
			{
				transformColumnExpressions( column, query );
			}
			catch ( BirtException ex )
			{
				context.addException( column, ex );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public Object visitTableItem( TableItemDesign table, Object value )
		{
			IDataQueryDefinition query;
			if ( table.useCachedResult( ) )
			{
				query = getRefenceQuery( table );
				if ( query == null )
				{
					registerUnresolvedQueryReference( table );
					return null;
				}
			}
			else
			{
				query = createQuery( table, (IDataQueryDefinition) value );
			}

			BaseQueryDefinition baseQuery = (BaseQueryDefinition) query;

			if ( query == null )
			{
				handleListingBand( table.getHeader( ), baseQuery, true, null );

				handleListingBand( table.getFooter( ), baseQuery, true, null );
			}
			else
			{
				// transformExpressions( table, query, null );

				handleListingQuery( table, baseQuery );
				for ( int i = 0; i < table.getColumnCount( ); i++ )
				{
					handleColumn( table.getColumn( i ), query );
				}

				handleListingBand( table.getHeader( ), baseQuery, true, null );

				SlotHandle groupsSlot = ( (TableHandle) table.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < table.getGroupCount( ); i++ )
				{
					handleListingGroup( table.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), baseQuery );
				}

				BandDesign detail = table.getDetail(); 
				setUsesDetails(detail, baseQuery);
				handleListingBand( detail, baseQuery, false, null );

				handleListingBand( table.getFooter( ), baseQuery, true, null );
			}
			try
			{
				transformExpressions( table, baseQuery );
			}
			catch ( BirtException ex )
			{
				context.addException( table.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		private void setUsesDetails(BandDesign detail,
				BaseQueryDefinition baseQuery) {
			if (detail == null || detail.getContentCount() == 0)
			{
				if ( !baseQuery.cacheQueryResults() )
					baseQuery.setUsesDetails(false);
			}
			else
			{
				baseQuery.setUsesDetails(true);
			}
		}
		
		private void setUsesDetails( BaseQueryDefinition baseQuery )
		{
			if ( baseQuery instanceof QueryDefinition
					&& !baseQuery.cacheQueryResults( ) )
			{
				( (QueryDefinition) baseQuery ).setIsSummaryQuery( true );
			}
			if ( baseQuery != null && !baseQuery.cacheQueryResults( ) )
			{
				baseQuery.setUsesDetails( false );

			}
		}

		private void handleListingQuery( ListingDesign design,
				BaseQueryDefinition query )
		{
			QueryExecutionHints executionHints = new QueryExecutionHints( );
			ListingHandle handle = (ListingHandle) design.getHandle( );
			executionHints.setSortBeforeGrouping( handle.isSortByGroups( ) );
			executionHints.setEnablePushDown( handle.pushDown( ) );
			query.setQueryExecutionHints( executionHints );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
		 */
		public Object visitDynamicTextItem( DynamicTextItemDesign dynamicText,
				Object value )
		{
			IDataQueryDefinition query;
			if ( dynamicText.useCachedResult( ) )
			{
				query = getRefenceQuery( dynamicText );
				if ( query == null )
				{
					registerUnresolvedQueryReference( dynamicText );
					return null;
				}
			}
			else
			{
				query = createQuery( dynamicText, (IDataQueryDefinition) value );
			}
			if( query != value && query instanceof BaseQueryDefinition )
			{
				setUsesDetails( (BaseQueryDefinition)query );
			}
			try
			{
				Expression newContent = transformExpression( dynamicText
						.getContent( ), query );
				dynamicText.setContent( newContent );
				transformExpressions( dynamicText, query );
				return getResultQuery( query, value );
			}
			catch ( BirtException ex )
			{
				context.addException( dynamicText.getHandle( ), ex );
				return null;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
		 */
		public Object visitDataItem( DataItemDesign data, Object value )
		{
			IDataQueryDefinition query;
			if ( data.useCachedResult( ) )
			{
				query = getRefenceQuery( data );
				if ( query == null )
				{
					registerUnresolvedQueryReference( data );
					return null;
				}
			}
			else
			{
				query = createQuery( data, (IDataQueryDefinition) value );
			}
			if( query != value && query instanceof BaseQueryDefinition )
			{
				setUsesDetails( (BaseQueryDefinition)query );
			}
			try
			{
				transformExpressions( data, query );
			}
			catch ( BirtException ex )
			{
				context.addException( data.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/**
		 * Set the onGroup to the query. Remove the added subQueries from the
		 * subQueries of BaseQueryDefinition to GroupDefinition. This is because
		 * DTE want to know subQueries is on group or detail. And the subQueries
		 * in group must be added to the GroupDefinition, but their parents all
		 * should be BaseQueryDefinition.
		 * 
		 * TODO: the relation may be too complex and the arithmetic also be too
		 * ugly. Need to talk with DTE about the relations and change the
		 * arithmetic be simple.
		 * 
		 * @param band
		 * @param query
		 * @param onGroup
		 * @param groupDefn
		 */
		public void handleListingBand( BandDesign band,
				IBaseQueryDefinition query, boolean onGroup,
				IGroupDefinition groupDefn )
		{
			if ( band != null )
			{
				ArrayList subQueries = (ArrayList) ( (ArrayList) query
						.getSubqueries( ) ).clone( );
				for ( int i = 0; i < band.getContentCount( ); i++ )
				{
					// here should return queries
					build( query, band.getContent( i ) );
				}
				ArrayList subQueriesChanged = (ArrayList) query.getSubqueries( );

				if ( subQueriesChanged != null )
				{
					for ( int i = 0; i < subQueriesChanged.size( ); i++ )
					{
						SubqueryDefinition subQuery = (SubqueryDefinition) subQueriesChanged
								.get( i );
						if ( !subQueries.contains( subQuery )
								&& subQuery instanceof SubqueryDefinition )
						{
							( (SubqueryDefinition) subQuery )
									.setApplyOnGroupFlag( onGroup );
							if ( groupDefn != null )
							{
								subQueriesChanged.remove( subQuery );
								groupDefn.getSubqueries( ).add( subQuery );
								i--;
							}
						}
					}
				}
			}
		}

		/**
		 * @param group
		 *            a grouping in a list
		 * @param handle
		 *            handle to a grouping element
		 */
		protected void handleListingGroup( GroupDesign group,
				GroupHandle handle, Object value )
		{
			assert value instanceof IBaseQueryDefinition;
			IBaseQueryDefinition query = (IBaseQueryDefinition) value;

			IGroupDefinition groupDefn;
			if ( !query.cacheQueryResults( ) )
			{
				IModelAdapter adaptor = dteSession.getModelAdaptor( );
				groupDefn = adaptor.adaptGroup( handle );
				query.getGroups( ).add( groupDefn );
			}
			else
			{
				groupDefn = getGroupDefinition( group, query );
			}

			if ( groupDefn != null )
			{
				try
				{
					transformExpressions( group, query, groupDefn.getName( ) );
				}
				catch ( BirtException ex )
				{
					context.addException( group.getHandle( ), ex );
				}
				handleListingBand( group.getHeader( ), query, true, groupDefn );
				handleListingBand( group.getFooter( ), query, true, groupDefn );
			}
		}

		protected IGroupDefinition getGroupDefinition( GroupDesign design,
				IBaseQueryDefinition query )
		{
			String name = design.getName( );
			List groups = query.getGroups( );
			if ( groups != null && groups.size( ) > 0 )
			{
				for ( int i = 0; i < groups.size( ); i++ )
				{
					IGroupDefinition group = (IGroupDefinition) groups.get( i );
					if ( group.getName( ).equals( name ) )
					{
						return group;
					}
				}
			}
			EngineException ex = new EngineException( MessageConstants.INVALID_GROUP_ERROR, name );
			logger.log( Level.WARNING, ex.getMessage( ), ex );
			context.addException( design.getHandle( ), ex );
			return null;
		}

		/**
		 * visit content of a row
		 */
		public Object visitRow( RowDesign row, Object value )
		{
			IDataQueryDefinition query;
			if ( row.useCachedResult( ) )
			{
				query = getRefenceQuery( row );
				if ( query == null )
				{
					registerUnresolvedQueryReference( row );
					return null;
				}
			}
			else
			{
				query = createQuery( row, (IDataQueryDefinition) value );
			}
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				CellDesign cell = row.getCell( i );
				build( query, cell );
			}
			try
			{
				transformExpressions( row, query );
			}
			catch ( BirtException ex )
			{
				context.addException( row.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		/**
		 * handles a cell in a row
		 */
		public Object visitCell( CellDesign cell, Object value )
		{
			IDataQueryDefinition query;
			if ( cell.useCachedResult( ) )
			{
				query = getRefenceQuery( cell );
				if ( query == null )
				{
					registerUnresolvedQueryReference( cell );
					return null;
				}
			}
			else
			{
				query = createQuery( cell, (IDataQueryDefinition) value );
			}
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				build( query, cell.getContent( i ) );
			}
			try
			{
				transformExpressions( cell, query );
			}
			catch ( BirtException ex )
			{
				context.addException( cell.getHandle( ), ex );
			}
			return getResultQuery( query, value );
		}

		private IDataQueryDefinition[] getResultQuery(
				IDataQueryDefinition query, Object parent )
		{
			if ( query != null && query != parent )
			{
				return new IDataQueryDefinition[]{query};
			}
			return null;
		}

		/**
		 * Remember the relations if the reference handle's query hasn't been
		 * builder.
		 * 
		 * @param referenceHandle
		 * @param handle
		 */
		protected void registerUnresolvedQueryReference( ReportItemDesign item )
		{
			ReportItemHandle itemHandle = (ReportItemHandle) item.getHandle( );
			ReportItemHandle referenceHandle = getDataBindingReference( itemHandle );
			if ( unresolvedQueryReferences.containsKey( referenceHandle ) )
			{
				List<ReportItemDesign> items = unresolvedQueryReferences
						.get( referenceHandle );
				items.add( item );
			}
			else
			{
				List<ReportItemDesign> items = new ArrayList<ReportItemDesign>( );
				items.add( item );
				unresolvedQueryReferences.put( referenceHandle, items );
			}
		}

		private ReportItemHandle getDataBindingReference(
				ReportItemHandle itemHandle )
		{
			ReportItemHandle referenceHandle = itemHandle
					.getDataBindingReference( );
			if ( referenceHandle == null )
			{
				return null;
			}
			ReportItemHandle tmpHandle = (ReportItemHandle) referenceHandle
					.getCurrentView( );
			if ( tmpHandle != null )
			{
				return tmpHandle;
			}

			tmpHandle = (ReportItemHandle) referenceHandle.getHostViewHandle( );
			if ( tmpHandle != null )
			{
				return tmpHandle.getCurrentView( ) == null
						? (ReportItemHandle) tmpHandle
						: (ReportItemHandle) tmpHandle.getCurrentView( );
			}

			return referenceHandle;
		}
		
		protected IDataQueryDefinition getRefenceQuery( ReportItemDesign item )
		{
			ReportItemHandle itemHandle = (ReportItemHandle) item.getHandle( );
			ReportItemHandle referenceHandle = getDataBindingReference( itemHandle );
			IDataQueryDefinition[] queries = report
					.getQueryByReportHandle( referenceHandle );
			if ( queries != null && queries.length > 0 )
			{
				for ( int i = 0; i < queries.length; i++ )
				{
					IDataQueryDefinition referenceQuery = queries[i];
					if ( referenceQuery instanceof BaseQueryDefinition )
					{
						( (BaseQueryDefinition) referenceQuery )
								.setCacheQueryResults( true );
					}
				}
				if ( queries[0] instanceof BaseQueryDefinition )
				{
					return (BaseQueryDefinition) queries[0];
				}
				else
				{
					// FIXME: can't support reference normal query to cube
					// query.
					throw new IllegalStateException(
							"Can not support reference normal query to cube query" );
				}
			}
			return null;
		}

		/**
		 * create query for non-listing report item
		 * 
		 * @param item
		 *            report item
		 * @return a report query
		 */
		protected IDataQueryDefinition createQuery( ReportItemDesign item,
				IDataQueryDefinition parent )
		{

			DesignElementHandle handle = item.getHandle( );

			if ( !( handle instanceof ReportItemHandle ) )
			{
				if ( !needQuery( item, parent ) )
				{
					// return the parentQuery as the current query.
					return parent;
				}
				// we have column binding, create a sub query.
				return createSubQuery( item, parent );
			}

			ReportItemHandle designHandle = (ReportItemHandle) handle;

			DataSetHandle dsHandle = designHandle.getDataSet( );

			if ( dsHandle == null )
			{
				// dataset reference error
				String dsName = (String) designHandle
						.getProperty( ReportItemHandle.DATA_SET_PROP );
				if ( dsName != null && dsName.length( ) > 0 )
				{
					context.addException( item.getHandle( ),
							new EngineException(
									MessageConstants.UNDEFINED_DATASET_ERROR,
									dsName ) );
				}
				// we has data set name defined, so test if we have column
				// binding here.

				if ( !needQuery( item, parent ) )
				{
					// return the parentQuery as the current query.
					return parent;
				}

				// we have column binding, create a sub query.
				return createSubQuery( item, parent );
			}
			// The report item has a data set definition, must create a query
			// for it.
			BaseQueryDefinition parentQuery = null;
			if ( parent instanceof BaseQueryDefinition )
			{
				parentQuery = (BaseQueryDefinition) parent;
			}
			QueryDefinition query = new QueryDefinition( parentQuery );
			query.setDataSetName( dsHandle.getQualifiedName( ) );

			// set max rows
			query.setMaxRows( maxRows );
			if ( designHandle instanceof TableHandle )
			{
				TableHandle listing = (TableHandle) designHandle;
				query.setIsSummaryQuery( listing.isSummaryTable( ) );
			}
			query.getQueryExecutionHints( ).setEnablePushDown(
					designHandle.pushDown( ) );
			// bind the query with parameters
			addParamBinding( item, query );
			addColumnBinding( item, query );
			addSortAndFilter( item, query );

			return query;
		}

		protected BaseQueryDefinition createSubQuery( ReportItemDesign item,
				IDataQueryDefinition parent )
		{
			BaseQueryDefinition parentQuery = null;
			if ( parent instanceof BaseQueryDefinition )
			{
				parentQuery = (BaseQueryDefinition) parent;
			}
			BaseQueryDefinition query = null;
			// sub query must be defined in a transform
			if ( parentQuery == null )
			{
				// no parent query exits, so create a empty query for it.
				query = new QueryDefinition( null );
			}
			else
			{
				// create a sub query
				String name = String.valueOf( item.getID( ) );
				query = new SubqueryDefinition( name, parentQuery );
				parentQuery.getSubqueries( ).add( query );
			}

			// set max rows
			query.setMaxRows( maxRows );
			addColumnBinding( item, query );
			addSortAndFilter( item, query );

			return query;
		}

		/**
		 * An item needs query when it satisfies following conditions:
		 * 
		 * <li>Has column bindings.</li>
		 * 
		 * <li>Is a table or a list.</li>
		 * 
		 * <li>Before BIRT 2.2.1, has highlight rules and doesn't have parent
		 * query.</li>
		 * 
		 * @param item
		 *            the item.
		 * @param query
		 *            the parent query
		 * @return true if it needs query.
		 */
		private boolean needQuery( ReportItemDesign item,
				IDataQueryDefinition query )
		{
			DesignElementHandle handle = item.getHandle( );
			if ( handle instanceof ReportItemHandle )
			{
				ReportItemHandle designHandle = (ReportItemHandle) item
						.getHandle( );
				if ( designHandle.columnBindingsIterator( ).hasNext( ) )
				{
					return true;
				}
				if ( designHandle instanceof ListingHandle )
				{
					return true;
				}
				if ( designHandle instanceof ExtendedItemHandle )
				{
					ExtendedItemHandle extHandle = (ExtendedItemHandle) designHandle;
					return ExtensionManager.getInstance( ).getAllRows(
							extHandle.getExtensionName( ) );
				}
			}

			return needQueryIn2_1_x( item, query );
		}

		/**
		 * Test if the item need query in BIRT version before 2.2.1.
		 * 
		 * In BIRT 2.1.0, 2.1.1, 2.1.2, 2.1.3, 2.2.0, the report item with
		 * highlight also create a query
		 * 
		 * @param item
		 *            item handle
		 * @param query
		 *            parent query
		 * @return true if need create a query
		 */
		private boolean needQueryIn2_1_x( ReportItemDesign item,
				IDataQueryDefinition query )
		{
			if ( isBirt2_1_x( ) )
			{
				HighlightDesign highlight = item.getHighlight( );
				if ( query == null && highlight != null
						&& highlight.getRuleCount( ) > 0 )
				{
					return true;
				}
			}
			return false;
		}

		private Boolean isBirt2_1_x;

		/**
		 * If the BIRT version is before 2.2.1
		 * 
		 * @return true for version before 2.2.1, including 2.0, 2.1.0, 2.1.1,
		 *         2.1.2, 2.1.3, 2.2.0.
		 */
		private boolean isBirt2_1_x( )
		{
			if ( isBirt2_1_x == null )
			{
				isBirt2_1_x = Boolean.FALSE;
				IReportDocument document = context.getReportDocument( );
				if ( document != null )
				{
					String version = document.getVersion( );
					if ( version == ReportDocumentConstants.BIRT_ENGINE_VERSION_2_1_0
							|| version == ReportDocumentConstants.BIRT_ENGINE_VERSION_2_1_3 )
					{
						isBirt2_1_x = Boolean.TRUE;
					}
				}
			}
			return isBirt2_1_x.booleanValue( );
		}

		private void addColumnBinding( ReportItemDesign design,
				IBaseQueryDefinition query )
		{
			DesignElementHandle elementHandle = design.getHandle( );
			if ( elementHandle instanceof ReportItemHandle )
			{
				IModelAdapter adaptor = dteSession.getModelAdaptor( );
				ReportItemHandle designHandle = (ReportItemHandle) elementHandle;
				Iterator iter = designHandle.columnBindingsIterator( );;
				if ( iter != null )
				{
					while ( iter.hasNext( ) )
					{
						try
						{
							ComputedColumnHandle bindingHandle = (ComputedColumnHandle) iter
									.next( );
							IBinding binding = adaptor
									.adaptBinding( bindingHandle );
							query.addBinding( binding );
						}
						catch ( BirtException ex )
						{
							context.addException( design, ex );
						}
					}
				}
			}
		}

		private void addParamBinding( ReportItemDesign item,
				QueryDefinition query )
		{
			DesignElementHandle elementHandle = item.getHandle( );
			if ( elementHandle instanceof ReportItemHandle )
			{
				ReportItemHandle itemHandle = (ReportItemHandle) elementHandle;
				createParamBindings( itemHandle.paramBindingsIterator( ), query
						.getInputParamBindings( ) );
			}
		}

		private void addSortAndFilter( ReportItemDesign item,
				BaseQueryDefinition query )
		{
			if ( item instanceof ListingDesign )
			{
				ListingHandle listHandle = (ListingHandle) item.getHandle( );
				createSorts( listHandle.sortsIterator( ), query.getSorts( ) );
				createFilters( listHandle.filtersIterator( ), query
						.getFilters( ) );
			}
			else if ( item instanceof ExtendedItemDesign )
			{
				// It is strange that the extended item doesn't support sorting
				ExtendedItemHandle extHandle = (ExtendedItemHandle) item
						.getHandle( );
				createFilters( extHandle.filtersIterator( ), query.getFilters( ) );
			}
		}

		/**
		 * create a filter array given a filter condition handle iterator
		 * 
		 * @param iter
		 *            the iterator
		 * @return filter array
		 */
		private void createFilters( Iterator iter, List filters )
		{
			if ( iter != null )
			{
				while ( iter.hasNext( ) )
				{
					FilterConditionHandle filterHandle = (FilterConditionHandle) iter
							.next( );
					IFilterDefinition filter = dteSession.getModelAdaptor( )
							.adaptFilter( filterHandle );
					filters.add( filter );
				}
			}
		}

		/**
		 * create all sort conditions given a sort key handle iterator
		 * 
		 * @param iter
		 *            the iterator
		 * @return sort array
		 */
		private void createSorts( Iterator iter, List sorts )
		{
			if ( iter != null )
			{
				IModelAdapter adaptor = dteSession.getModelAdaptor( );
				while ( iter.hasNext( ) )
				{
					SortKeyHandle handle = (SortKeyHandle) iter.next( );
					ISortDefinition sort = adaptor.adaptSort( handle );
					sorts.add( sort );
				}
			}
		}

		/**
		 * create input parameter bindings
		 * 
		 * @param iter
		 *            parameter bindings iterator
		 * @return a list of input parameter bindings
		 */
		private void createParamBindings( Iterator iter, Collection bindings )
		{
			if ( iter != null )
			{
				IModelAdapter adaptor = dteSession.getModelAdaptor( );
				while ( iter.hasNext( ) )
				{
					ParamBindingHandle handle = (ParamBindingHandle) iter
							.next( );
					IInputParameterBinding binding = adaptor
							.adaptInputParamBinding( handle );
					if ( binding != null )
					{
						bindings.add( binding );
					}
				}
			}
		}

		/**
		 * finish the current visit. transform the expressions of the query.
		 * 
		 * @param item
		 * @param query
		 */
		private void transformExpressions( ReportItemDesign item,
				IDataQueryDefinition query ) throws BirtException
		{
			transformExpressions( item, query, null );
		}

		/**
		 * Transfers old expressions to column bindings and new expression.
		 * 
		 * @param item
		 *            the report design.
		 */
		private void transformExpressions( ReportItemDesign item,
				IDataQueryDefinition query, String groupName )
				throws BirtException
		{
			if ( query != null )
			{
				ITotalExprBindings totalExpressionBindings = getNewExpressionBindings(
						item, query, groupName );
				addNewColumnBindings( query, totalExpressionBindings );
				replaceOldExpressions( item, totalExpressionBindings );
			}
		}

		/**
		 * Transfer the old expression to column dataBinding and bind it to the
		 * Query. And create a news expression to replace the old expression.
		 * 
		 * @param expr
		 *            expression to be transfered. return the transfered
		 *            expression
		 */
		protected Expression transformExpression( Expression expr,
				IDataQueryDefinition query ) throws BirtException
		{
			if ( expr == null )
			{
				return null;
			}
			if ( query != null )
			{
				List<Expression> expressions = new ArrayList<Expression>( );
				expressions.add( expr );
				ITotalExprBindings totalExpressionBinding = null;;

				totalExpressionBinding = expressionUtil
						.prepareTotalExpressions( expressions, query );

				addNewColumnBindings( query, totalExpressionBinding );

				List<Expression> newExpressions = totalExpressionBinding
						.getNewExpression( );
				return newExpressions.get( 0 );
			}
			return expr;
		}

		/**
		 * Transfer the old visibility and hightlight expressions to column
		 * dataBinding and bind it to the Query. And create new visibility and
		 * hightlight expressions to replace the old.
		 */
		private void transformColumnExpressions( ColumnDesign column,
				IDataQueryDefinition query ) throws BirtException
		{
			if ( query == null )
			{
				return;
			}

			List<Expression> expressions = new ArrayList<Expression>( );
			VisibilityDesign visibilities = column.getVisibility( );
			if ( visibilities != null )
			{
				// get new expression bindings of this column's visibilities
				for ( int i = 0; i < visibilities.count( ); i++ )
				{
					expressions
							.add( visibilities.getRule( i ).getExpression( ) );
				}
			}
			HighlightDesign highlights = column.getHighlight( );
			if ( highlights != null )
			{
				// get new expression bindings of this column's visibilities
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					RuleDesign rule = highlights.getRule( i );
					addRuleExpression( expressions, rule );
				}
			}
			ITotalExprBindings totalExpressionBindings = null;

			totalExpressionBindings = expressionUtil
					.prepareTotalExpressions( expressions, query );

			// add new column bindings to the query
			addNewColumnBindings( query, totalExpressionBindings );

			// replace old expressions
			int expressionIndex = 0;
			List<Expression> newExpressions = totalExpressionBindings.getNewExpression( );
			if ( visibilities != null )
			{
				for ( int i = 0; i < visibilities.count( ); i++ )
				{
					Expression expr = newExpressions.get( expressionIndex++ );
					visibilities.getRule( i ).setExpression( expr );
				}
			}
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					Expression expr = newExpressions.get( expressionIndex++ );
					highlights.getRule( i ).setConditionExpr( expr );
				}
			}

		}

		private void replaceOldExpressions( ReportItemDesign item,
				ITotalExprBindings totalExpressionBindings )
		{
			int expressionIndex = 0;

			List<Expression> newExpressions = totalExpressionBindings.getNewExpression( );
			item
					.setTOC(  newExpressions
							.get( expressionIndex++ ) );
			item.setBookmark(newExpressions
					.get( expressionIndex++ ) );

			Expression onCreateScript = newExpressions.get( expressionIndex++ );
			Expression onRenderScript = newExpressions.get( expressionIndex++ );
			Expression onPageBreakScript = newExpressions
					.get( expressionIndex++ );

			item.setOnCreate( onCreateScript );
			item.setOnRender( onRenderScript );
			item.setOnPageBreak( onPageBreakScript );

			HighlightDesign highlights = item.getHighlight( );
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					highlights.getRule( i ).setConditionExpr(
							newExpressions.get( expressionIndex++ ) );
				}
			}

			MapDesign maps = item.getMap( );
			if ( maps != null )
			{
				for ( int i = 0; i < maps.getRuleCount( ); i++ )
				{
					maps.getRule( i ).setConditionExpr(
							newExpressions.get( expressionIndex++ ) );
				}
			}

			VisibilityDesign visibilities = item.getVisibility( );
			if ( visibilities != null )
			{
				for ( int i = 0; i < visibilities.count( ); i++ )
				{
					visibilities.getRule( i )
							.setExpression(
									newExpressions
											.get( expressionIndex++ ) );
				}
			}

			Map<String, Expression> userProperties = item.getUserProperties( );
			if ( userProperties != null )
			{
				for ( Map.Entry<String, Expression> entry : userProperties
						.entrySet( ) )
				{
					entry.setValue( newExpressions.get( expressionIndex++ ) );
				}
			}

			ActionDesign action = item.getAction( );
			if ( action != null )
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_BOOKMARK :
						action.setBookmark( (Expression) newExpressions
								.get( expressionIndex++ ) );
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						DrillThroughActionDesign drillThrough = action
								.getDrillThrough( );
						if ( drillThrough != null )
						{
							drillThrough
									.setBookmark( (Expression) newExpressions
											.get( expressionIndex++ ) );
							Map<String, List<Expression>> params = drillThrough
									.getParameters( );
							if ( params != null )
							{
								for ( Map.Entry<String, List<Expression>> entry : params
										.entrySet( ) )
								{
									if ( expressionIndex >= newExpressions.size( ) )
									{
										break;
									}
									Expression expr = (Expression) newExpressions.get( expressionIndex++ );
									ArrayList<Expression> exprList = new ArrayList<Expression>( );
									exprList.add( expr );
									entry.setValue( exprList );
								}
							}
						}
						break;
					case ActionDesign.ACTION_HYPERLINK :
						action.setHyperlink( (Expression) newExpressions
								.get( expressionIndex++ ) );
						break;
					default :
						assert false;
				}
			}
		}

		private ITotalExprBindings getNewExpressionBindings(
				ReportItemDesign item, IDataQueryDefinition query,
				String groupName )
		{
			List<Expression> expressions = new ArrayList<Expression>( );
			expressions.add( item.getTOC( ) );
			expressions.add( item.getBookmark( ) );
			if ( item.getOnCreate( ) != null )
			{
				expressions.add( item.getOnCreate( ) );
			}
			else
			{
				expressions.add( null );
			}
			if ( item.getOnRender( ) != null )
			{
				expressions.add( item.getOnRender( ) );
			}
			else
			{
				expressions.add( null );
			}
			if ( item.getOnPageBreak( ) != null )
			{
				expressions.add( item.getOnPageBreak( ) );
			}
			else
			{
				expressions.add( null );
			}

			HighlightDesign highlights = item.getHighlight( );
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					RuleDesign rule = highlights.getRule( i );
					addRuleExpression( expressions, rule );
				}
			}

			MapDesign maps = item.getMap( );
			if ( maps != null )
			{
				for ( int i = 0; i < maps.getRuleCount( ); i++ )
				{
					RuleDesign rule = maps.getRule( i );
					addRuleExpression( expressions, rule );
				}
			}

			VisibilityDesign visibilities = item.getVisibility( );
			if ( visibilities != null )
			{
				for ( int i = 0; i < visibilities.count( ); i++ )
				{
					expressions
							.add( visibilities.getRule( i ).getExpression( ) );
				}
			}

			Map<String, Expression> userProperties = item.getUserProperties( );
			if ( userProperties != null )
			{
				for ( Map.Entry<String, Expression> entry : userProperties
						.entrySet( ) )
				{
					expressions.add( entry.getValue( ) );
				}
			}

			ActionDesign action = item.getAction( );
			if ( action != null )
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_BOOKMARK :
						expressions.add( action.getBookmark( ) );
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						DrillThroughActionDesign drillThrough = action
								.getDrillThrough( );
						if ( drillThrough != null )
						{
							expressions.add( drillThrough.getBookmark( ) );
							Map<String, List<Expression>> params = drillThrough.getParameters( ); 
							if ( params != null )
							{
								for (Map.Entry<String, List<Expression>> entry : params.entrySet( ))
								{
									List<Expression> exprs = entry.getValue( );
									for( Expression expression : exprs )
									{
										expressions.add( expression );
									}
								}
							}
						}
						break;
					case ActionDesign.ACTION_HYPERLINK :
						expressions.add( action.getHyperlink( ) );
						break;
					default :
						assert false;
				}
			}

			ITotalExprBindings totalExpressionBindings = null;
			try
			{
				totalExpressionBindings = expressionUtil
						.prepareTotalExpressions( expressions, groupName, query );
			}
			catch ( EngineException ex )
			{
				context.addException( item.getHandle( ), ex );
			}
			return totalExpressionBindings;
		}

		private void addRuleExpression( List<Expression> expressions,
				RuleDesign rule )
		{
			if ( rule != null )
			{
				Expression.Conditional highlightExpression = null;
				if ( rule.ifValueIsList( ) )
				{
					Expression testExpression = rule.getTestExpression( );
					List<Expression> value1List = rule.getValue1List( );
					highlightExpression = Expression
							.newConditional( expressionUtil
									.createConditionExpression(
											testExpression,
											rule.getOperator( ), value1List ) );
				}
				else
				{
					Expression testExpression = rule.getTestExpression( );
					Expression value1 = rule.getValue1( );
					Expression value2 = rule.getValue2( );
					highlightExpression = Expression
							.newConditional( expressionUtil
									.createConditionalExpression(
											testExpression,
											rule.getOperator( ), value1, value2 ) );
				}
				expressions.add( highlightExpression );
			}
		}

		protected void addNewColumnBindings( IDataQueryDefinition query,
				ITotalExprBindings totalExpressionBindings )
				throws BirtException
		{
			if ( query instanceof IBaseQueryDefinition )
			{
				addQueryColumnBindings( (IBaseQueryDefinition) query,
						totalExpressionBindings );
				return;
			}
			if ( query instanceof ICubeQueryDefinition )
			{
				addCubeColumnBindings( (ICubeQueryDefinition) query,
						totalExpressionBindings );
				return;
			}
			throw new EngineException( MessageConstants.UNSUPPORTED_QUERY_DEFINITION_ERROR , query );
		}

		protected void addCubeColumnBindings( ICubeQueryDefinition query,
				ITotalExprBindings totalExpressionBindings )
				throws BirtException
		{
			IBinding[] bindings = totalExpressionBindings.getColumnBindings( );
			if ( bindings != null )
			{
				for ( int i = 0; i < bindings.length; i++ )
				{
					query.addBinding( bindings[i] );
				}
			}
		}

		private void addQueryColumnBindings( IBaseQueryDefinition query,
				ITotalExprBindings totalExpressionBindings )
				throws BirtException
		{
			IBinding[] bindings = totalExpressionBindings.getColumnBindings( );
			if ( bindings != null )
			{
				for ( int i = 0; i < bindings.length; i++ )
				{
					bindings[i].setExportable(false);
					query.addBinding( bindings[i] );
				}
			}
		}
	}
}