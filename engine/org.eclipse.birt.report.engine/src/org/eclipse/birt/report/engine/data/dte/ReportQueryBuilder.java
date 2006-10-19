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

package org.eclipse.birt.report.engine.data.dte;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.report.engine.adapter.ExpressionUtil;
import org.eclipse.birt.report.engine.adapter.IColumnBinding;
import org.eclipse.birt.report.engine.adapter.ITotalExprBindings;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.impl.ResultMetaData;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
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
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.RuleDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * visit the report design and prepare all report queries and sub-queries to
 * send to data engine
 * 
 * @version $Revision: 1.84 $ $Date: 2006/09/08 04:07:19 $
 */
public class ReportQueryBuilder
{

	protected static Logger logger = Logger.getLogger( ReportQueryBuilder.class
			.getName( ) );

	private ExpressionUtil expressionUtil;

	public ReportQueryBuilder( )
	{
		expressionUtil = new ExpressionUtil( );
	}

	/**
	 * @param report
	 *            the entry point to the report design
	 * @param context
	 *            the execution context
	 */
	public void build( Report report, ExecutionContext context )
	{
		if ( report.getQueries( ).isEmpty( ) )
		{
			new QueryBuilderVisitor( ).buildQuery( report, context );
		}
	}

	/**
	 * The visitor class that actually builds the report query
	 */
	protected class QueryBuilderVisitor extends DefaultReportItemVisitorImpl
	{

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
		 * the query stack. The top stores the query that is currently prepared.
		 * Needed because we could have nested queries
		 */
		protected LinkedList queryStack = new LinkedList( );
		
		/**
		 * the current condition stack which may be used in creating sub queries . 
		 * The top stores true if the currently prepared is included in the group( header or footer ) 
		 * or flase in the detail.
		 */
		protected LinkedList currentConditionStack = new LinkedList( );
		
		/*
		 * report item query stack
		 * 
		 */
		protected LinkedList reportItemQueryStack;
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

		/**
		 * create report query definitions for this report.
		 * 
		 * @param report
		 *            entry point to the report
		 * @param context
		 *            the execution context
		 */
		public void buildQuery( Report report, ExecutionContext context )
		{
			this.report = report;
			this.context = context;
			
			// get max rows per query
			if( null != this.context )
			{
				IReportEngine engine = this.context.getEngine( );
				if( null != engine)
				{
					EngineConfig engineConfig = engine.getConfig( );
					if( null != engineConfig)
					{
						maxRows = engineConfig.getMaxRowsPerQuery( );
					}
				}
				
			}

			queries = report.getQueries( );
			// first clear the collection in case the caller call this function
			// more than once.
			queries.clear( );

			queryIDs = report.getQueryIDs( );
			queryIDs.clear( );
			
			resultMetaData = report.getResultMetaData( );
			resultMetaData.clear( );

			// visit master page
			for ( int i = 0; i < report.getPageSetup( ).getMasterPageCount( ); i++ )
			{
				MasterPageDesign masterPage = report.getPageSetup( )
						.getMasterPage( i );
				if ( masterPage != null )
				{
					SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) masterPage;
					for ( int j = 0; j < pageDesign.getHeaderCount( ); j++ )
					{
						pageDesign.getHeader( j ).accept( this, null );
					}
					for ( int j = 0; j < pageDesign.getFooterCount( ); j++ )
					{
						pageDesign.getFooter( j ).accept( this, null );
					}
				}
			}

			// visit report
			for ( int i = 0; i < report.getContentCount( ); i++ )
				report.getContent( i ).accept( this, null );
		}

		/**
		 * Handles query creation and initialization with report-item related
		 * expressions
		 * 
		 * @param item
		 *            report item
		 */
		private BaseQueryDefinition prepareVisit( ReportItemDesign item )
		{
			BaseQueryDefinition tempQuery = createQuery( item );
			if ( tempQuery != null )
			{
				pushQuery( tempQuery );
			}
			transformExpressions( item );
			return tempQuery;
		}

		/**
		 * Clean up stack after visiting a report item
		 */
		private void finishVisit( BaseQueryDefinition query )
		{
			if ( query != null )
			{
				popQuery( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
		 */
		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			BaseQueryDefinition query = prepareVisit( container );

			for ( int i = 0; i < container.getItemCount( ); i++ )
				container.getItem( i ).accept( this, value );

			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
		 */
		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			BaseQueryDefinition query = prepareVisit( grid );

			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				grid.getRow( i ).accept( this, value );
			}

			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
		 */
		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			BaseQueryDefinition query = prepareVisit( image );

			if ( image.getImageSource( ) == ImageItemDesign.IMAGE_EXPRESSION )
			{
				String newImageExpression = transformExpression( image.getImageExpression( ) );
				String newImageFormat = transformExpression( image.getImageFormat( ) );
				image.setImageExpression( newImageExpression, newImageFormat );
			}
			else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_URI )
			{
				String newImageUri = transformExpression( image.getImageUri( ) );
				image.setImageUri( newImageUri );
			}
			else if ( image.getImageSource( ) == ImageItemDesign.IMAGE_FILE )
			{
				String newImageUri = transformExpression( image.getImageUri( ) );
				image.setImageFile( newImageUri );
			}

			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
		 */
		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			BaseQueryDefinition query = prepareVisit( label );
			finishVisit( query );
			return value;
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
			String tagName = handle.getExtensionName( );

			// TODO: check in plugin registry whetherthe needQuery property is
			// set to host or item.
			// Only do the following for "host"

			IReportItemQuery itemQuery = ExtensionManager.getInstance( )
					.createQueryItem( tagName );
			IBaseQueryDefinition[] queries = null;
			IBaseQueryDefinition parentQuery = getParentQuery( );
			IBaseTransform parentTrans = getTransform( );
			if ( itemQuery != null )
			{
				try
				{
					itemQuery.setModelObject( handle );

					queries = itemQuery.getReportQueries( parentQuery );

				}
				catch ( BirtException ex )
				{
					logger.log( Level.WARNING, ex.getMessage( ), ex );
				}
				if ( queries != null )
				{
					item.setQueries( queries );
					for ( int i = 0; i < queries.length; i++ )
					{
						if ( queries[i] != null )
						{
							this.queryIDs.put( queries[i], String
									.valueOf( item.getID( ) )
									+ "_" + String.valueOf( i ) );
							ResultMetaData metaData = new ResultMetaData( queries[i] );
							resultMetaData.put( queries[i], metaData );
							registerQueryAndElement( queries[i], item );
							if ( queries[i] instanceof IQueryDefinition )
							{
								this.queries.add( queries[i] );
							}
							else if ( queries[i] instanceof ISubqueryDefinition )
							{
								// TODO: chart engine make a mistake here
								if ( parentTrans != null )
								{
									parentTrans.getSubqueries( ).add(
											queries[i] );
								}
							}
						}
					}
					if ( queries.length > 0 )
					{
						IBaseQueryDefinition query = queries[0];
						if ( query != null )
						{
							transformExpressions( item );			
						}
					}
				}
			}
			BaseQueryDefinition query = prepareVisit( item );
			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
		 */
		public Object visitListItem( ListItemDesign list, Object value )
		{
			BaseQueryDefinition query = prepareVisit( list );
			if ( query == null )
			{
				pushCurrentCondition( true );
				handleListingBand( list.getHeader( ), value );
				popCurrentCondition( );
				
				pushCurrentCondition( true );
				handleListingBand( list.getFooter( ), value );
				popCurrentCondition( );
			}
			else
			{
				pushReportItemQuery( query );
				transformExpressions( list );
				pushCurrentCondition( true );
				handleListingBand( list.getHeader( ), value );
				
				SlotHandle groupsSlot = ( (ListHandle) list.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < list.getGroupCount( ); i++ )
				{
					handleListingGroup( list.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), value );
				}
				popCurrentCondition( );

				BandDesign detail = list.getDetail( );
				if ( detail == null || detail.getContentCount( ) == 0 )
				{
					query.setUsesDetails( false );
				}
				pushCurrentCondition( false );
				handleListingBand( list.getDetail( ), value );
				popCurrentCondition( );
				
				pushCurrentCondition( true );
				handleListingBand( list.getFooter( ), value );
				popCurrentCondition( );
				popReportItemQuery( );
			}
			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
		 */
		public Object visitTextItem( TextItemDesign text, Object value )
		{
			BaseQueryDefinition query = prepareVisit( text );
			HashMap exprs = text.getExpressions( );
			if ( exprs != null )
			{
				Iterator ite = exprs.entrySet( ).iterator( );
				while ( ite.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) ite.next( );
					assert entry.getValue( ) instanceof String;
					String newExpr = transformExpression( entry.getValue( ).toString( ) );
					entry.setValue( newExpr );
				}				
			}

			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public void handleColumn( ColumnDesign column )
		{
			transformColumnExpressions( column );
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public Object visitTableItem( TableItemDesign table, Object value )
		{
			BaseQueryDefinition query = prepareVisit( table );
			if ( query == null )
			{
				pushCurrentCondition( true );
				handleListingBand( table.getHeader( ), value );
				popCurrentCondition( );
				
				pushCurrentCondition( true );
				handleListingBand( table.getFooter( ), value );
				popCurrentCondition( );				
			}
			else
			{
				transformExpressions( table );
				
				for( int i = 0; i < table.getColumnCount( ); i++ )
				{
					handleColumn( table.getColumn( i ) );
				}
				
				pushCurrentCondition( true );
				handleListingBand( table.getHeader( ), value );
				
				SlotHandle groupsSlot = ( (TableHandle) table.getHandle( ) )
						.getGroups( );

				for ( int i = 0; i < table.getGroupCount( ); i++ )
				{
					handleListingGroup( table.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ), value );
				}
				popCurrentCondition( );

				BandDesign detail = table.getDetail( );
				if ( detail == null || detail.getContentCount( ) == 0 )
				{
					query.setUsesDetails( false );
				}
				
				pushCurrentCondition( false );
				handleListingBand( table.getDetail( ), value );
				popCurrentCondition( );
				
				pushCurrentCondition( true );				
				handleListingBand( table.getFooter( ), value );
				popCurrentCondition( );
			}
			finishVisit( query );
			return value;
		}

		/*
		 * associate query with Table, List and Chart item design.
		 */
		private void registerQueryAndElement( IBaseQueryDefinition query,
				ReportItemDesign reportItem )
		{
			assert query != null && reportItem != null;
			HashMap map = report.getReportItemToQueryMap( );
			assert map != null;
			map.put( query, reportItem );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
		 */
		public Object visitMultiLineItem( MultiLineItemDesign multiLine,
				Object value )
		{
			BaseQueryDefinition query = prepareVisit( multiLine );
			String newContent = transformExpression( multiLine.getContent( ) );
			multiLine.setContent( newContent );
			finishVisit( query );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
		 */
		public Object visitDataItem( DataItemDesign data, Object value )
		{
			BaseQueryDefinition query = prepareVisit( data );
			//we needn't transfer the data expression as it must be row[''].
//			String newValue = transformExpression( data.getValue( ) );
//			data.setValue( newValue );
			finishVisit( query );
			return value;
		}	
		
		/**
		 * @param band
		 *            the list band
		 */
		public void handleListingBand( BandDesign band, Object value )
		{
			if ( band != null )
			{
				for ( int i = 0; i < band.getContentCount( ); i++ )
				{
					band.getContent( i ).accept( this, value );
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
			IGroupDefinition groupDefn = handleGroup( group, handle );

			pushQuery( groupDefn );
			transformExpressions( group );
			handleListingBand( group.getHeader( ), value );
			handleListingBand( group.getFooter( ), value );
			popQuery( );
		}

		/**
		 * processes a table/list group
		 */
		protected IGroupDefinition handleGroup( GroupDesign group,
				GroupHandle handle )
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

			getParentQuery( ).getGroups( ).add( groupDefn );

			return groupDefn;
		}

		/**
		 * visit content of a row
		 */
		public Object visitRow(RowDesign row, Object value)
		{
			BaseQueryDefinition query = prepareVisit( row );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				CellDesign cell = row.getCell( i );
				cell.accept( this, value );
			}
			finishVisit( query );
			return value;
		}

		/**
		 * handles a cell in a row
		 */
		public Object visitCell( CellDesign cell, Object value )
		{
			BaseQueryDefinition query = prepareVisit( cell );
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this, value );
			}
			finishVisit( query );
			return value;
		}		

		protected void pushReportItemQuery( IBaseQueryDefinition query )
		{
			if ( this.reportItemQueryStack == null )
			{
				this.reportItemQueryStack = new LinkedList( );
			}
			this.reportItemQueryStack.addLast( query );
		}

		protected void popReportItemQuery( )
		{
			assert this.reportItemQueryStack.isEmpty( ) == false;
			this.reportItemQueryStack.removeLast( );
		}
		
		/**
		 * A helper function for adding a query to query stack
		 */
		protected void pushCurrentCondition( boolean currentCondition )
		{
			this.currentConditionStack.addLast( String.valueOf( currentCondition ) );
		}

		/**
		 * A helper function for removing a query from query stack
		 */
		protected void popCurrentCondition( )
		{
			assert !currentConditionStack.isEmpty( );
			currentConditionStack.removeLast( );
		}

		/**
		 * @return topmost element on query stack
		 */
		protected String getCurrentCondition( )
		{
			if ( currentConditionStack.isEmpty( ) )
				return String.valueOf( false );
			return ( String ) currentConditionStack.getLast( );
		}

		/**
		 * A helper function for adding a query to query stack
		 */
		protected void pushQuery( IBaseTransform query )
		{
			this.queryStack.addLast( query );
		}

		/**
		 * A helper function for removing a query from query stack
		 */
		protected void popQuery( )
		{
			assert !queryStack.isEmpty( );
			queryStack.removeLast( );
		}

		/**
		 * @return topmost element on query stack
		 */
		protected IBaseTransform getTransform( )
		{
			if ( queryStack.isEmpty( ) )
				return null;
			return (IBaseTransform) queryStack.getLast( );
		}

		/**
		 * @return the parent query for the current report item
		 */
		protected BaseQueryDefinition getParentQuery( )
		{
			if ( queryStack.isEmpty( ) )
				return null;

			for ( int i = queryStack.size( ) - 1; i >= 0; i-- )
			{
				if ( queryStack.get( i ) instanceof BaseQueryDefinition )
					return (BaseQueryDefinition) queryStack.get( i );
			}
			return null;

		}		
		
		/**
		 * @return the current group which the current report item be included
		 */
		protected GroupDefinition getCurrentGroup( )
		{
			if ( queryStack.isEmpty( ) )
				return null;

			if ( queryStack.getLast( ) instanceof GroupDefinition )
			{
				return (GroupDefinition) queryStack.getLast( );
			}
			return null;

		}	
		
		/**
		 * @return the name of the current group 
		 * which the current report item be included
		 */
		protected String getCurrentGroupName( )
		{
			GroupDefinition group = getCurrentGroup( );
			if ( group != null )
			{
				return group.getName( );
			}
			return null;
		}

		protected void addColumBinding( IBaseQueryDefinition transfer,
				ComputedColumnHandle binding )
		{
			String name = binding.getName( );
			String expr = binding.getExpression( );
			String type = binding.getDataType( );
			int dbType = ModelDteApiAdapter.toDteDataType( type );
			IBaseExpression dbExpr = new ScriptExpression( expr, dbType );
			dbExpr.setGroupName( binding.getAggregateOn( ) );
			transfer.getResultSetExpressions( ).put( name, dbExpr );
		}

		/**
		 * create query for non-listing report item
		 * 
		 * @param item
		 *            report item
		 * @return a report query
		 */
		protected BaseQueryDefinition createQuery( ReportItemDesign item )
		{
			DesignElementHandle handle = item.getHandle( );
			if ( ! ( handle instanceof ReportItemHandle ) )
			{
				if ( !needQuery( item ) )
				{
					return null;
				}
				// we have column binding, create a sub query.
				return createSubQuery( item );
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

				if ( !needQuery( item ) )
				{
					return null;
				}
				
				// we have column binding, create a sub query.
				return createSubQuery( item );
			}
			// The report item has a data set definition, must creat a query for
			// it.
			QueryDefinition query = new QueryDefinition( getParentQuery( ) );
			query.setDataSetName( dsHandle.getQualifiedName( ) );

			// bind the query with parameters
			query.getInputParamBindings().addAll( 
					createParamBindings( designHandle.paramBindingsIterator( ) ));
			
			// set max rows
			query.setMaxRows( maxRows );

			this.queryIDs.put( query, String.valueOf( item.getID( ) ) );
			this.queries.add( query );
			registerQueryAndElement( query, item );

			Iterator iter = designHandle.columnBindingsIterator( );
			while ( iter.hasNext( ) )
			{
				ComputedColumnHandle binding = (ComputedColumnHandle) iter.next( );
				addColumBinding( query, binding );
			}

			item.setQuery( query );
			
			addSortAndFilter( item, query );
			
			ResultMetaData metaData = new ResultMetaData( query );
			resultMetaData.put( query, metaData );
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
		private boolean needQuery( ReportItemDesign item )
		{
			DesignElementHandle handle = item.getHandle( );
			if ( handle instanceof ReportItemHandle )
			{
				ReportItemHandle designHandle = (ReportItemHandle) item.getHandle( );
				if ( designHandle.columnBindingsIterator( ).hasNext( ) )
				{
					return true;
				}
				if ( designHandle instanceof ListingHandle )
				{
					return true;
				}
			}
			HighlightDesign highlight = item.getHighlight( );
			if ( getParentQuery( ) == null && highlight != null
					&& highlight.getRuleCount( ) > 0 )
			{
				return true;
			}
			return false;
		}
		
		private void addSortAndFilter( ReportItemDesign item, BaseQueryDefinition query )
		{
			if ( item instanceof ListingDesign )
			{
				query.getSorts( )
						.addAll( createSorts( (ListingDesign) item ) );
				query.getFilters( )
						.addAll( createFilters( (ListingDesign) item ) );
			}
			else if ( item instanceof ExtendedItemDesign )
			{
				query.getFilters( ).addAll(
						createFilters( (ExtendedItemDesign) item ) );
			}
		}

		protected BaseQueryDefinition createSubQuery( ReportItemDesign item )
		{
			BaseQueryDefinition query = null;
			IBaseTransform parentQuery = getTransform( );
			// sub query must be defined in a transform
			if ( parentQuery == null )
			{
				// no parent query exits, so create a empty query for it.
				query = new QueryDefinition( getParentQuery( ) );
				this.queryIDs.put( query, String.valueOf( item.getID( ) ) );
				this.queries.add( query );	
				registerQueryAndElement( query, item );
			}
			else
			{
				// create a sub query
				String name = String.valueOf( item.getID( ) );
				query = new SubqueryDefinition( name, getParentQuery( ) );
				parentQuery.getSubqueries( ).add( query );
				
				this.queryIDs.put( query, String.valueOf( item.getID( ) ) );
				registerQueryAndElement( query, item );
				
				String currentCondition = getCurrentCondition( );
				
				if ( currentCondition.equals( String.valueOf( true ) ) )
				{
					( (SubqueryDefinition)query ).setApplyOnGroupFlag( true );
				}
				else
				{
					( (SubqueryDefinition)query ).setApplyOnGroupFlag( false );
				}
			}
			
			// set max rows
			query.setMaxRows( maxRows );

			item.setQuery( query );
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
			
			ResultMetaData metaData = new ResultMetaData( query );
			resultMetaData.put( query, metaData );
		
			return query;
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
			String ret = report.getMessage( resourceKey, context.getLocale( ) );
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
		private void transformExpressions( ReportItemDesign item )
		{
			IBaseQueryDefinition query = getParentQuery( );
			if ( query != null )
			{
				ITotalExprBindings totalExpressionBindings = getNewExpressionBindings( item );
				addNewColumnBindings( query, totalExpressionBindings );
				replaceOldExpressions( item, totalExpressionBindings );
			}
		}
		
		/**
		 * Transfer the old expression to column dataBinding and bind it to the Query.
		 * And create a news expression to replace the old expression.
		 * @param expr expression to be transfered.
		 * return the transfered expression
		 */
		protected String transformExpression( String expr )
		{
			if ( expr == null )
			{
				return null;
			}
			IBaseQueryDefinition query = getParentQuery( );
			if ( query != null )
			{
				List expressions = new ArrayList( );
				expressions.add( expr );	
				ITotalExprBindings totalExpressionBinding = expressionUtil
					.prepareTotalExpressions( expressions, getCurrentGroupName( ) );
				
				addNewColumnBindings( query, totalExpressionBinding );
				
				List newExpressions = totalExpressionBinding.getNewExpression( );
				return (String) newExpressions.get( 0 );
			}
			return expr;
		}			
		
		/**
		 * Transfer the old visibility and hightlight expressions to column dataBinding and bind
		 * it to the Query. And create new visibility and hightlight expressions to replace
		 * the old.
		 */
		private void transformColumnExpressions( ColumnDesign column )
		{
			IBaseQueryDefinition query = getParentQuery( );
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
					expressions.add( createConditionalExpression( highlights
							.getRule( i ) ) );
				}
			}
			ITotalExprBindings totalExpressionBindings = expressionUtil
					.prepareTotalExpressions( expressions,
							getCurrentGroupName( ) );

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
							(String) newExpressions.get( expressionIndex++ ) );
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
			item.setOnPageBreak( (String) newExpressions.get( expressionIndex++ ) );

			HighlightDesign highlights = item.getHighlight( );
			if ( highlights != null )
			{
				for ( int i = 0; i < highlights.getRuleCount( ); i++ )
				{
					highlights.getRule( i ).setConditionExpr(
							(String) newExpressions.get( expressionIndex++ ) );
				}
			}
			
			MapDesign maps = item.getMap( );

			if ( maps != null )
			{
				for ( int i = 0; i < maps.getRuleCount( ); i++ )
				{
					maps.getRule( i ).setConditionExpr(
							(String) newExpressions.get( expressionIndex++ ) );
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
				Iterator exprIter = exprs.iterator();
				Map.Entry entry = null;
				while( exprIter.hasNext( ) )
				{
					entry = ( Map.Entry ) exprIter.next( );				
					entry.setValue( ( String ) newExpressions.get( expressionIndex++ ) );
				}
			}
			
			ActionDesign action = item.getAction( );
			if ( action != null )
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_BOOKMARK :
						action.setBookmark( (String) newExpressions.get( expressionIndex++ ) );
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						DrillThroughActionDesign drillThrough = action
								.getDrillThrough( );
						if ( drillThrough != null )
						{
							drillThrough.setBookmark( (String) newExpressions.get( expressionIndex++ ) );
							if ( drillThrough.getParameters( ) != null )
							{
								Iterator ite = drillThrough.getParameters( )
										.entrySet( ).iterator( );
								while ( ite.hasNext( ) )
								{
									Map.Entry entry = (Map.Entry) ite.next( );
									entry.setValue( ( String ) newExpressions.get( expressionIndex++ ) );
								}
							}
						}
						break;
					case ActionDesign.ACTION_HYPERLINK :
						action.setHyperlink( (String) newExpressions.get( expressionIndex++ ) );
						break;
					default :
						assert false;
				}
			}
		}

		private ITotalExprBindings getNewExpressionBindings( ReportItemDesign item )
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
					expressions.add( createConditionalExpression( highlights
							.getRule( i ) ) );
				}
			}

			MapDesign maps = item.getMap( );
			if ( maps != null )
			{
				for ( int i = 0; i < maps.getRuleCount( ); i++ )
				{
					expressions
							.add( createConditionalExpression( maps.getRule( i ) ) );
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
				Iterator exprIter = exprs.iterator();
				Map.Entry entry = null;
				while( exprIter.hasNext( ) )
				{
					entry = ( Map.Entry ) exprIter.next( );
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
			
			ITotalExprBindings totalExpressionBindings = expressionUtil
					.prepareTotalExpressions( expressions, getCurrentGroupName( ) );
			return totalExpressionBindings;
		}

		private void addNewColumnBindings( IBaseQueryDefinition query,
				ITotalExprBindings totalExpressionBindings )
		{
			IColumnBinding[] bindings = totalExpressionBindings.getColumnBindings( );
			if ( bindings != null )
			{
				for ( int i = 0; i < bindings.length; i++ )
				{
					addColumnBinding( query, bindings[i] );
				}
			}
		}

		private void addColumnBinding( IBaseQueryDefinition transfer,
				IColumnBinding binding )
		{
			assert transfer != null;
			transfer.getResultSetExpressions( )
					.put( binding.getResultSetColumnName( ),
							binding.getBoundExpression( ));
		}

		private IConditionalExpression createConditionalExpression( RuleDesign rule )
		{
			ConditionalExpression expression = new ConditionalExpression( rule
					.getTestExpression( ),
					toDteFilterOperator( rule.getOperator( ) ), rule.getValue1( ),
					rule.getValue2( ) );
			return ExpressionUtil.transformConditionalExpression( expression );
		}
	}

	// Convert model operator value to DtE IColumnFilter enum value
	private int toDteFilterOperator( String modelOpr )
	{
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_EQ ) )
			return IConditionalExpression.OP_EQ;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NE ) )
			return IConditionalExpression.OP_NE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LT ) )
			return IConditionalExpression.OP_LT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LE ) )
			return IConditionalExpression.OP_LE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GE ) )
			return IConditionalExpression.OP_GE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GT ) )
			return IConditionalExpression.OP_GT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) )
			return IConditionalExpression.OP_BETWEEN;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) )
			return IConditionalExpression.OP_NOT_BETWEEN;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL ) )
			return IConditionalExpression.OP_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL ) )
			return IConditionalExpression.OP_NOT_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE ) )
			return IConditionalExpression.OP_TRUE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) )
			return IConditionalExpression.OP_FALSE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LIKE ) )
			return IConditionalExpression.OP_LIKE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_N ) )
			return IConditionalExpression.OP_TOP_N;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N ) )
			return IConditionalExpression.OP_BOTTOM_N;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT ) )
			return IConditionalExpression.OP_TOP_PERCENT;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT ) )
			return IConditionalExpression.OP_BOTTOM_PERCENT;

		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE ) )
			return IConditionalExpression.OP_NOT_LIKE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH ) )
			return IConditionalExpression.OP_NOT_MATCH;

		return IConditionalExpression.OP_NONE;
	}
}