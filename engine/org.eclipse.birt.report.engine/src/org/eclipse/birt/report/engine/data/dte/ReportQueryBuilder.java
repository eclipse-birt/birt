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

package org.eclipse.birt.report.engine.data.dte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryDefinition;
import org.eclipse.birt.report.engine.adapter.ExpressionUtil;
import org.eclipse.birt.report.engine.adapter.ITotalExprBindings;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
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
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

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
	protected HashMap queryIDs;

	/**
	 * query and result metaData
	 */
	protected HashMap resultMetaData;

	/**
	 * a collection of all the queries
	 */
	protected Collection queries;

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

	/**
	 * used to register the unresolved query reference.
	 */
	protected Map unresolvedQueryReferences = new HashMap( ); 

	public ReportQueryBuilder( )
	{
		expressionUtil = new ExpressionUtil( );
		queryBuilder = new QueryBuilderVisitor( );

	}

	/**
	 * @param report
	 *            the entry point to the report design
	 * @param context
	 *            the execution context
	 */
	public ReportQueryBuilder( Report report, ExecutionContext context )
	{
		expressionUtil = new ExpressionUtil( );
		queryBuilder = new QueryBuilderVisitor( );
		this.report = report;
		this.context = context;

		// get max rows per query
		if ( null != this.context )
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
						this.queryIDs.put( query, queryId );
						// we do not support cube's metaData now. And we so do
						// support CUB data's extration.
						if ( query instanceof IBaseQueryDefinition )
						{
							ResultMetaData metaData = new ResultMetaData(
									(IBaseQueryDefinition) query );
							resultMetaData.put( query, metaData );
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
								context.addException( new EngineException(
										"subquery can only be created in another subquery/query"
												+ design.getID( ) ) );
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
						ResultMetaData metaData = new ResultMetaData(
								(IBaseQueryDefinition) query );
						resultMetaData.put( query, metaData );
					}
				}
				registerQueryToHandle( design, queries );
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
			ArrayList items = (ArrayList) unresolvedQueryReferences
					.get( itemHandle );
			for ( int i = 0; i < items.size( ); i++ )
			{
				ReportItemDesign item = (ReportItemDesign) items.get( i );
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
			BaseQueryDefinition query;
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
				query = createQuery( container, value );
			}

			for ( int i = 0; i < container.getItemCount( ); i++ )
				build( query, container.getItem( i ) );

			transformExpressions( container, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
		 */
		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( grid, value );
			}

			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				build( query, grid.getRow( i ) );
			}

			transformExpressions( grid, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
		 */
		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( image, value );
			}

			if ( image.getImageSource( ) == ImageItemDesign.IMAGE_EXPRESSION )
			{
				String newImageExpression = transformExpression( image
						.getImageExpression( ), query, null );
				String newImageFormat = transformExpression( image
						.getImageFormat( ), query, null );
				image.setImageExpression( newImageExpression, newImageFormat );
			}
			else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_URI )
			{
				String newImageUri = transformExpression( image.getImageUri( ),
						query, null );
				image.setImageUri( newImageUri );
			}
			else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_FILE )
			{
				String newImageUri = transformExpression( image.getImageUri( ),
						query, null );
				image.setImageFile( newImageUri );
			}

			transformExpressions( image, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
		 */
		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( label, value );
			}
			transformExpressions( label, query );
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

			ReportItemHandle referenceHandle = handle.getDataBindingReference( );
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
						transformExpressions( item,
								(IBaseQueryDefinition) query );
					}
					return queries;
				}
				else
				{
					registerUnresolvedQueryReference( item );
					return null;
				}
			}

			String tagName = handle.getExtensionName( );

			// TODO: check in plugin registry whetherthe needQuery property is
			// set to host or item.
			// Only do the following for "host"

			IReportItemQuery itemQuery = ExtensionManager.getInstance( )
					.createQueryItem( tagName );
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
					logger.log( Level.WARNING, ex.getMessage( ), ex );
					context.addException( ex );
				}
				if ( queries != null )
				{
					if ( queries.length > 0 )
					{
						IDataQueryDefinition query = queries[0];
						if ( query instanceof IBaseQueryDefinition )
						{
							transformExpressions( item,
									(IBaseQueryDefinition) query, null );
						}
					}
					return queries;
				}
			}
			BaseQueryDefinition query = createQuery( item, parentQuery );
			transformExpressions( item, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
		 */
		public Object visitListItem( ListItemDesign list, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( list, value );
			}
			if ( query == null )
			{
				handleListingBand( list.getHeader( ), query, true, null );
				handleListingBand( list.getFooter( ), query, true, null );
			}
			else
			{
				handleListingBand( list.getHeader( ), query, true, null );

				SlotHandle groupsSlot = ( (ListHandle) list.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < list.getGroupCount( ); i++ )
				{
					handleListingGroup( list.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), query );
				}

				BandDesign detail = list.getDetail( );
				if ( !query.cacheQueryResults( ) )
				{
					if ( detail == null || detail.getContentCount( ) == 0 )
					{
						query.setUsesDetails( false );
					}
				}
				handleListingBand( detail, query, false, null );

				handleListingBand( list.getFooter( ), query, true, null );

			}
			transformExpressions( list, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
		 */
		public Object visitTextItem( TextItemDesign text, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( text, value );
			}
			HashMap exprs = text.getExpressions( );
			if ( exprs != null )
			{
				Iterator ite = exprs.entrySet( ).iterator( );
				while ( ite.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) ite.next( );
					assert entry.getValue( ) instanceof String;
					String newExpr = transformExpression( entry.getValue( )
							.toString( ), query, null );
					entry.setValue( newExpr );
				}
			}

			transformExpressions( text, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public void handleColumn( ColumnDesign column,
				IBaseQueryDefinition query )
		{
			transformColumnExpressions( column, query, null );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public Object visitTableItem( TableItemDesign table, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( table, value );
			}
			if ( query == null )
			{
				handleListingBand( table.getHeader( ), query, true, null );

				handleListingBand( table.getFooter( ), query, true, null );
			}
			else
			{
				// transformExpressions( table, query, null );

				for ( int i = 0; i < table.getColumnCount( ); i++ )
				{
					handleColumn( table.getColumn( i ), query );
				}

				handleListingBand( table.getHeader( ), query, true, null );

				SlotHandle groupsSlot = ( (TableHandle) table.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < table.getGroupCount( ); i++ )
				{
					handleListingGroup( table.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), query );
				}

				BandDesign detail = table.getDetail( );
				if ( !query.cacheQueryResults( ) )
				{
					if ( detail == null || detail.getContentCount( ) == 0 )
					{
						query.setUsesDetails( false );
					}
				}
				handleListingBand( detail, query, false, null );

				handleListingBand( table.getFooter( ), query, true, null );
			}
			transformExpressions( table, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
		 */
		public Object visitDynamicTextItem( DynamicTextItemDesign dynamicText,
				Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( dynamicText, value );
			}
			String newContent = transformExpression( dynamicText.getContent( ),
					query, null );
			dynamicText.setContent( newContent );
			transformExpressions( dynamicText, query );
			return getResultQuery( query, value );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
		 */
		public Object visitDataItem( DataItemDesign data, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( data, value );
			}

			transformExpressions( data, query );
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
				groupDefn = handleGroup( group, handle, query );
			}
			else
			{
				groupDefn = getGroupDefinition( group, query );
			}
			
			if ( groupDefn != null )
			{
				transformExpressions( group, query, groupDefn.getName( ) );
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
			EngineException ex = new EngineException( "Invalid group {0}", name );
			logger.log( Level.WARNING, ex.getMessage( ), ex );
			context.addException( ex );
			return null;
		}

		/**
		 * processes a table/list group
		 */
		protected IGroupDefinition handleGroup( GroupDesign group,
				GroupHandle handle, IBaseQueryDefinition query )
		{
			GroupDefinition groupDefn = new GroupDefinition( group.getName( ) );
			groupDefn.setKeyExpression( handle.getKeyExpr( ) );
			String interval = handle.getInterval( );
			if ( interval != null )
			{
				groupDefn.setInterval( parseInterval( interval ) );
			}
			// inter-range
			groupDefn.setIntervalRange( handle.getIntervalRange( ) );
			// inter-start-value
			groupDefn.setIntervalStart( handle.getIntervalBase( ) );
			// sort-direction
			String direction = handle.getSortDirection( );
			if ( direction != null )
			{
				groupDefn.setSortDirection( parseSortDirection( direction ) );
			}

			groupDefn.getSorts( ).addAll( createSorts( handle ) );
			groupDefn.getFilters( ).addAll( createFilters( handle ) );

			query.getGroups( ).add( groupDefn );

			return groupDefn;
		}

		/**
		 * visit content of a row
		 */
		public Object visitRow( RowDesign row, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( row, value );
			}
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				CellDesign cell = row.getCell( i );
				build( query, cell );
			}
			transformExpressions( row, query );
			return getResultQuery( query, value );
		}

		/**
		 * handles a cell in a row
		 */
		public Object visitCell( CellDesign cell, Object value )
		{
			BaseQueryDefinition query;
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
				query = createQuery( cell, value );
			}
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				build( query, cell.getContent( i ) );
			}
			transformExpressions( cell, query );
			return getResultQuery( query, value );
		}

		private IBaseQueryDefinition[] getResultQuery(
				IBaseQueryDefinition query, Object parent )
		{
			if ( query != null && query != parent )
			{
				return new IBaseQueryDefinition[]{query};
			}
			return null;
		}

		protected void addColumBinding( IBaseQueryDefinition transfer,
				ComputedColumnHandle columnBinding )
		{
			String name = columnBinding.getName( );
			String expr = columnBinding.getExpression( );
			String type = columnBinding.getDataType( );
			int dbType = ModelDteApiAdapter.toDteDataType( type );
			IBaseExpression dbExpr = new ScriptExpression( expr, dbType );
			if ( columnBinding.getAggregateOn( ) != null )
			{
				dbExpr.setGroupName( columnBinding.getAggregateOn( ) );
			}
			IBinding binding = new Binding( name, dbExpr );
			try
			{
				if ( columnBinding.getAggregateOn( ) != null )
					binding.addAggregateOn( columnBinding.getAggregateOn( ) );
				if ( columnBinding.getAggregateFunction( ) != null )
				{
					binding.setAggrFunction( columnBinding.getAggregateFunction( ) );
				}
				String filter = columnBinding.getFilterExpression( );
				if ( filter != null )
				{
					binding.setFilter( new ScriptExpression( filter ) );
				}
				Iterator arguments = columnBinding.argumentsIterator( );
				if ( arguments != null )
				{
					while ( arguments.hasNext( ) )
					{
						AggregationArgumentHandle argumentHandle = (AggregationArgumentHandle) arguments
								.next( );
						String argument = argumentHandle.getValue( );
						if ( argument != null )
						{
							binding.addArgument( new ScriptExpression( argument ) );
						}
					}
				}
				transfer.addBinding( binding );
			}
			catch ( DataException ex )
			{
				context.addException( ex );
			}
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
			ReportItemHandle referenceHandle = itemHandle
					.getDataBindingReference( );
			if ( unresolvedQueryReferences.containsKey( referenceHandle ) )
			{
				List items = (ArrayList) unresolvedQueryReferences
						.get( referenceHandle );
				items.add( item );
			}
			else
			{
				List items = new ArrayList( );
				items.add( item );
				unresolvedQueryReferences.put( referenceHandle, items );
			}
		}
		
		protected BaseQueryDefinition getRefenceQuery( ReportItemDesign item )
		{
			ReportItemHandle itemHandle = (ReportItemHandle) item.getHandle( );
			ReportItemHandle referenceHandle = itemHandle
					.getDataBindingReference( );
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
				if ( (BaseQueryDefinition) queries[0] instanceof BaseQueryDefinition )
				{
					return (BaseQueryDefinition) queries[0];
				}
				else
				{
					// FIXME: can't support reference normal query to cube
					// query.
					throw new IllegalStateException(
							"Can't support reference normal query to cube query" ); 
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
		protected BaseQueryDefinition createQuery( ReportItemDesign item,
				Object parent )
		{

			DesignElementHandle handle = item.getHandle( );			

			BaseQueryDefinition parentQuery = null;
			if ( parent instanceof BaseQueryDefinition )
			{
				parentQuery = (BaseQueryDefinition) parent;
			}
			if ( !( handle instanceof ReportItemHandle ) )
			{
				if ( !needQuery( item, parentQuery ) )
				{
					// return the parentQuery as the current query.
					return parentQuery;
				}
				// we have column binding, create a sub query.
				return createSubQuery( item, parentQuery );
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

				if ( !needQuery( item, parentQuery ) )
				{
					// return the parentQuery as the current query.
					return parentQuery;
				}
				
				if ( parent instanceof CubeQueryDefinition )
				{
					return null;
					//return createSubQuery(item, null);
				}

				// we have column binding, create a sub query.
				return createSubQuery( item, parentQuery );
			}
			// The report item has a data set definition, must create a query for
			// it.
			QueryDefinition query = new QueryDefinition( parentQuery );
			query.setDataSetName( dsHandle.getQualifiedName( ) );

			// bind the query with parameters
			query.getInputParamBindings( )
					.addAll(
							createParamBindings( designHandle
									.paramBindingsIterator( ) ) );

			// set max rows
			query.setMaxRows( maxRows );

			Iterator iter = designHandle.columnBindingsIterator( );
			while ( iter.hasNext( ) )
			{
				ComputedColumnHandle binding = (ComputedColumnHandle) iter
						.next( );
				addColumBinding( query, binding );
			}

			addSortAndFilter( item, query );

			return query;
		}

		protected BaseQueryDefinition createSubQuery( ReportItemDesign item,
				BaseQueryDefinition parentQuery )
		{
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

			if ( item.getHandle( ) instanceof ReportItemHandle )
			{
				ReportItemHandle designHandle = (ReportItemHandle) item
						.getHandle( );

				Iterator iter = designHandle.columnBindingsIterator( );
				while ( iter.hasNext( ) )
				{
					ComputedColumnHandle binding = (ComputedColumnHandle) iter
							.next( );
					addColumBinding( query, binding );
				}
			}

			addSortAndFilter( item, query );

			return query;
		}

		/**
		 * An item needs query when it satisfies following conditions:
		 * <li>Has column bindings.
		 * <li>Is a table or a list.
		 * <li>Has hightlight rules and doesn't have parent query.
		 * 
		 * @param item
		 *            the item.
		 * @return true if it needs query.
		 */
		private boolean needQuery( ReportItemDesign item,
				IBaseQueryDefinition query )
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
			return false;
		}

		private void addSortAndFilter( ReportItemDesign item,
				BaseQueryDefinition query )
		{
			if ( item instanceof ListingDesign )
			{
				query.getSorts( ).addAll( createSorts( (ListingDesign) item ) );
				query.getFilters( ).addAll(
						createFilters( (ListingDesign) item ) );
			}
			else if ( item instanceof ExtendedItemDesign )
			{
				query.getFilters( ).addAll(
						createFilters( (ExtendedItemDesign) item ) );
			}
		}

		/**
		 * get Localized string by the resouce key and <code>Locale</code>
		 * object in <code>context</code>
		 * 
		 * @param resourceKey
		 *            the resource key
		 * @param text
		 *            the default value
		 * @return the localized string if it is defined in report deign, else
		 *         return the default value
		 */
		protected String getLocalizedString( String resourceKey, String text )
		{
			if ( resourceKey == null )
			{
				return text;
			}
			String ret = report.getReportDesign( ).getMessage( resourceKey,
					context.getLocale( ) );
			if ( ret == null )
			{
				logger.log( Level.SEVERE, "get resource error, resource key:" //$NON-NLS-1$
						+ resourceKey + " Locale:" //$NON-NLS-1$
						+ context.getLocale( ).toString( ) );
				return text;
			}
			return ret;
		}

		/**
		 * create one Filter given a filter condition handle
		 * 
		 * @param handle
		 *            a filter condition handle
		 * @return the filter
		 */
		private IFilterDefinition createFilter( FilterConditionHandle handle )
		{
			String filterExpr = handle.getExpr( );
			if ( filterExpr == null || filterExpr.length( ) == 0 )
				return null; // no filter defined

			// converts to DtE exprFilter if there is no operator
			String filterOpr = handle.getOperator( );
			if ( filterOpr == null || filterOpr.length( ) == 0 )
				return new FilterDefinition( new ScriptExpression( filterExpr ) );

			/*
			 * has operator defined, try to convert filter condition to
			 * operator/operand style column filter with 0 to 2 operands
			 */

			String column = filterExpr;
			int dteOpr = ModelDteApiAdapter.toDteFilterOperator( filterOpr );
//			List operand1List = handle.getValue1List( );
//			if ( operand1List != null )
//			{
//				return new FilterDefinition( new ConditionalExpression( column,
//						dteOpr, operand1List ) );
//			}
			String operand1 = handle.getValue1( );
			String operand2 = handle.getValue2( );
			return new FilterDefinition( new ConditionalExpression( column,
					dteOpr, operand1, operand2 ) );
		}

		/**
		 * create a filter array given a filter condition handle iterator
		 * 
		 * @param iter
		 *            the iterator
		 * @return filter array
		 */
		private ArrayList createFilters( Iterator iter )
		{
			ArrayList filters = new ArrayList( );
			if ( iter != null )
			{

				while ( iter.hasNext( ) )
				{
					FilterConditionHandle filterHandle = (FilterConditionHandle) iter
							.next( );
					IFilterDefinition filter = createFilter( filterHandle );
					filters.add( filter );
				}
			}
			return filters;
		}

		/**
		 * create filter array given a Listing design element
		 * 
		 * @param listing
		 *            the ListingDesign
		 * @return the filter array
		 */
		public ArrayList createFilters( ListingDesign listing )
		{
			return createFilters( ( (ListingHandle) listing.getHandle( ) )
					.filtersIterator( ) );
		}

		/**
		 * create fileter array given a DataSetHandle
		 * 
		 * @param dataSet
		 *            the DataSetHandle
		 * @return the filer array
		 */
		public ArrayList createFilters( DataSetHandle dataSet )
		{
			return createFilters( dataSet.filtersIterator( ) );
		}

		/**
		 * create filter array given a GroupHandle
		 * 
		 * @param group
		 *            the GroupHandle
		 * @return filter array
		 */
		public ArrayList createFilters( GroupHandle group )
		{
			return createFilters( group.filtersIterator( ) );
		}

		/**
		 * create filter array given a ExtendedItemHandle
		 * 
		 * @param group
		 *            the GroupHandle
		 * @return filter array
		 */
		public ArrayList createFilters( ExtendedItemDesign extendedItem )
		{
			return createFilters( ( (ExtendedItemHandle) extendedItem
					.getHandle( ) ).filtersIterator( ) );
		}

		/**
		 * create one sort condition
		 * 
		 * @param handle
		 *            the SortKeyHandle
		 * @return the sort object
		 */
		private ISortDefinition createSort( SortKeyHandle handle )
		{
			SortDefinition sort = new SortDefinition( );
			sort.setExpression( handle.getKey( ) );
			sort.setSortDirection( handle.getDirection( ).equals(
					DesignChoiceConstants.SORT_DIRECTION_ASC ) ? 0 : 1 );
			return sort;

		}

		/**
		 * create all sort conditions given a sort key handle iterator
		 * 
		 * @param iter
		 *            the iterator
		 * @return sort array
		 */
		private ArrayList createSorts( Iterator iter )
		{
			ArrayList sorts = new ArrayList( );
			if ( iter != null )
			{

				while ( iter.hasNext( ) )
				{
					SortKeyHandle handle = (SortKeyHandle) iter.next( );
					sorts.add( createSort( handle ) );
				}
			}
			return sorts;
		}

		/**
		 * create all sort conditions in a listing element
		 * 
		 * @param listing
		 *            ListingDesign
		 * @return the sort array
		 */
		protected ArrayList createSorts( ListingDesign listing )
		{
			return createSorts( ( (ListingHandle) listing.getHandle( ) )
					.sortsIterator( ) );
		}

		/**
		 * create sort array by giving GroupHandle
		 * 
		 * @param group
		 *            the GroupHandle
		 * @return the sort array
		 */
		protected ArrayList createSorts( GroupHandle group )
		{
			return createSorts( group.sortsIterator( ) );
		}

		/**
		 * create input parameter binding
		 * 
		 * @param handle
		 * @return
		 */
		protected IInputParameterBinding createParamBinding(
				ParamBindingHandle handle )
		{
			if ( handle.getExpression( ) == null )
				return null; // no expression is bound
			ScriptExpression expr = new ScriptExpression( handle
					.getExpression( ) );
			// model provides binding by name only
			return new InputParameterBinding( handle.getParamName( ), expr );

		}

		/**
		 * create input parameter bindings
		 * 
		 * @param iter
		 *            parameter bindings iterator
		 * @return a list of input parameter bindings
		 */
		protected ArrayList createParamBindings( Iterator iter )
		{
			ArrayList list = new ArrayList( );
			if ( iter != null )
			{
				while ( iter.hasNext( ) )
				{
					ParamBindingHandle modelParamBinding = (ParamBindingHandle) iter
							.next( );
					IInputParameterBinding binding = createParamBinding( modelParamBinding );
					if ( binding != null )
					{
						list.add( binding );
					}
				}
			}
			return list;
		}

		/**
		 * converts interval string values to integer values
		 */
		protected int parseInterval( String interval )
		{
			if ( DesignChoiceConstants.INTERVAL_YEAR.equals( interval ) )
			{
				return IGroupDefinition.YEAR_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_MONTH.equals( interval ) )
			{
				return IGroupDefinition.MONTH_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_WEEK.equals( interval ) ) // 
			{
				return IGroupDefinition.WEEK_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_QUARTER.equals( interval ) )
			{
				return IGroupDefinition.QUARTER_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_DAY.equals( interval ) )
			{
				return IGroupDefinition.DAY_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_HOUR.equals( interval ) )
			{
				return IGroupDefinition.HOUR_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_MINUTE.equals( interval ) )
			{
				return IGroupDefinition.MINUTE_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_PREFIX.equals( interval ) )
			{
				return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_SECOND.equals( interval ) )
			{
				return IGroupDefinition.SECOND_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_INTERVAL.equals( interval ) )
			{
				return IGroupDefinition.NUMERIC_INTERVAL;
			}
			return IGroupDefinition.NO_INTERVAL;
		}

		/**
		 * @param direction
		 *            "asc" or "desc" string
		 * @return integer value defined in <code>ISortDefn</code>
		 */
		protected int parseSortDirection( String direction )
		{
			if ( "asc".equals( direction ) ) //$NON-NLS-1$
				return ISortDefinition.SORT_ASC;
			if ( "desc".equals( direction ) ) //$NON-NLS-1$
				return ISortDefinition.SORT_DESC;
			assert false;
			return 0;
		}

		/**
		 * Transfers old expressions to column bindings and new expression.
		 * 
		 * @param item
		 *            the report design.
		 */
		private void transformExpressions( ReportItemDesign item,
				IBaseQueryDefinition query, String groupName )
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
		protected String transformExpression( String expr,
				IBaseQueryDefinition query, String groupName )
		{
			if ( expr == null )
			{
				return null;
			}
			if ( query != null )
			{
				List expressions = new ArrayList( );
				expressions.add( expr );
				ITotalExprBindings totalExpressionBinding = null;;
				try
				{
					totalExpressionBinding = expressionUtil
							.prepareTotalExpressions( expressions, groupName );
				}
				catch ( EngineException ex )
				{
					context.addException( ex );
				}

				addNewColumnBindings( query, totalExpressionBinding );

				List newExpressions = totalExpressionBinding.getNewExpression( );
				return (String) newExpressions.get( 0 );
			}
			return expr;
		}

		/**
		 * Transfer the old visibility and hightlight expressions to column
		 * dataBinding and bind it to the Query. And create new visibility and
		 * hightlight expressions to replace the old.
		 */
		private void transformColumnExpressions( ColumnDesign column,
				IBaseQueryDefinition query, String groupName )
		{
			if ( query == null )
			{
				return;
			}

			List expressions = new ArrayList( );
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
					if ( rule != null )
					{
						expressions.add( expressionUtil
								.createConditionalExpression( rule
										.getTestExpression( ), rule
										.getOperator( ), rule.getValue1( ),
										rule.getValue2( ) ) );
					}
				}
			}
			ITotalExprBindings totalExpressionBindings = null;
			try
			{
				totalExpressionBindings = expressionUtil
						.prepareTotalExpressions( expressions, groupName );
			}
			catch ( EngineException ex )
			{
				context.addException( ex );
			}

			// add new column bindings to the query
			addNewColumnBindings( query, totalExpressionBindings );

			// replace old expressions
			int expressionIndex = 0;
			List newExpressions = totalExpressionBindings.getNewExpression( );
			if ( visibilities != null )
			{
				for ( int i = 0; i < visibilities.count( ); i++ )
				{
					visibilities.getRule( i ).setExpression(
							(String) newExpressions.get( expressionIndex++ ) );
				}
			}
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					highlights.getRule( i ).setConditionExpr(
							newExpressions.get( expressionIndex++ ) );
				}
			}

		}

		private void replaceOldExpressions( ReportItemDesign item,
				ITotalExprBindings totalExpressionBindings )
		{
			int expressionIndex = 0;

			List newExpressions = totalExpressionBindings.getNewExpression( );
			item.setTOC( (String) newExpressions.get( expressionIndex++ ) );
			item.setBookmark( (String) newExpressions.get( expressionIndex++ ) );
			item.setOnCreate( (String) newExpressions.get( expressionIndex++ ) );
			item.setOnRender( (String) newExpressions.get( expressionIndex++ ) );
			item.setOnPageBreak( (String) newExpressions
					.get( expressionIndex++ ) );

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
					visibilities.getRule( i ).setExpression(
							(String) newExpressions.get( expressionIndex++ ) );
				}
			}

			Map namedExpressions = item.getNamedExpressions( );
			if ( namedExpressions != null )
			{
				Collection exprs = namedExpressions.entrySet( );
				Iterator exprIter = exprs.iterator( );
				Map.Entry entry = null;
				while ( exprIter.hasNext( ) )
				{
					entry = (Map.Entry) exprIter.next( );
					entry.setValue( (String) newExpressions
							.get( expressionIndex++ ) );
				}
			}

			ActionDesign action = item.getAction( );
			if ( action != null )
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_BOOKMARK :
						action.setBookmark( (String) newExpressions
								.get( expressionIndex++ ) );
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						DrillThroughActionDesign drillThrough = action
								.getDrillThrough( );
						if ( drillThrough != null )
						{
							drillThrough.setBookmark( (String) newExpressions
									.get( expressionIndex++ ) );
							if ( drillThrough.getParameters( ) != null )
							{
								Iterator ite = drillThrough.getParameters( )
										.entrySet( ).iterator( );
								while ( ite.hasNext( ) )
								{
									Map.Entry entry = (Map.Entry) ite.next( );
									entry.setValue( (String) newExpressions
											.get( expressionIndex++ ) );
								}
							}
						}
						break;
					case ActionDesign.ACTION_HYPERLINK :
						action.setHyperlink( (String) newExpressions
								.get( expressionIndex++ ) );
						break;
					default :
						assert false;
				}
			}
		}

		private ITotalExprBindings getNewExpressionBindings(
				ReportItemDesign item, IBaseQueryDefinition query,
				String groupName )
		{
			List expressions = new ArrayList( );
			expressions.add( item.getTOC( ) );
			expressions.add( item.getBookmark( ) );
			expressions.add( item.getOnCreate( ) );
			expressions.add( item.getOnRender( ) );
			expressions.add( item.getOnPageBreak( ) );

			HighlightDesign highlights = item.getHighlight( );
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					RuleDesign rule = highlights.getRule( i );
					if ( rule != null )
					{
						expressions.add( expressionUtil
								.createConditionalExpression( rule
										.getTestExpression( ), rule
										.getOperator( ), rule.getValue1( ),
										rule.getValue2( ) ) );
					}
				}
			}

			MapDesign maps = item.getMap( );
			if ( maps != null )
			{
				for ( int i = 0; i < maps.getRuleCount( ); i++ )
				{
					RuleDesign rule = maps.getRule( i );
					if ( rule != null )
					{
						expressions.add( expressionUtil
								.createConditionalExpression( rule
										.getTestExpression( ), rule
										.getOperator( ), rule.getValue1( ),
										rule.getValue2( ) ) );
					}
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

			Map namedExpressions = item.getNamedExpressions( );
			if ( namedExpressions != null )
			{
				Collection exprs = namedExpressions.entrySet( );
				Iterator exprIter = exprs.iterator( );
				Map.Entry entry = null;
				while ( exprIter.hasNext( ) )
				{
					entry = (Map.Entry) exprIter.next( );
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
							if ( drillThrough.getParameters( ) != null )
							{
								Iterator ite = drillThrough.getParameters( )
										.entrySet( ).iterator( );
								while ( ite.hasNext( ) )
								{
									Map.Entry entry = (Map.Entry) ite.next( );
									expressions.add( entry.getValue( ) );
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
						.prepareTotalExpressions( expressions, groupName );
			}
			catch ( EngineException ex )
			{
				context.addException( ex );
			}
			return totalExpressionBindings;
		}

		private void addNewColumnBindings( IBaseQueryDefinition query,
				ITotalExprBindings totalExpressionBindings )
		{
			IBinding[] bindings = totalExpressionBindings.getColumnBindings( );
			if ( bindings != null )
			{
				try
				{
					for ( int i = 0; i < bindings.length; i++ )
					{
						query.addBinding( bindings[i] );
					}
				}
				catch ( DataException e )
				{
					context.addException( e );
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
				IBaseQueryDefinition query )
		{
			// we can't change the paramter-binding, sorting, filtering
			// for shared result set, but we can add column bindings to it.
			if ( query != null )
			{
				transformExpressions( item, query, null );
			}
		}
	}

}