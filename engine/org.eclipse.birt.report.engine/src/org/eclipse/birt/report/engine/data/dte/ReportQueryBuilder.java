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
import java.util.Iterator;
import java.util.LinkedList;
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
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.model.api.DataSetHandle;
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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * visit the report design and prepare all report queries and sub-queries to
 * send to data engine
 * 
 * @version $Revision: 1.20 $ $Date: 2005/04/12 05:26:21 $
 */
public class ReportQueryBuilder
{

	/**
	 * constructor
	 */
	public ReportQueryBuilder( )
	{
	}

	/**
	 * @param report
	 *            the entry point to the report design
	 * @param context
	 *            the execution context
	 */
	public void build( Report report, ExecutionContext context )
	{
		new QueryBuilderVisitor( ).buildQuery( report, context );
	}

	/**
	 * The visitor class that actually builds the report query
	 */
	protected class QueryBuilderVisitor extends DefaultReportItemVisitorImpl
	{

		/**
		 * for logging
		 */
		protected Logger logger = Logger.getLogger( QueryBuilderVisitor.class
				.getName( ) );

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
		 * the total number of queries created in this report
		 */
		protected int queryCount = 0;

		/**
		 * a collection of the expressions on the CURRENT query
		 */
		protected Collection expressions;

		/**
		 * the expression stack. This is a link list of collections, not a
		 * link-list of individual expressions.
		 */
		protected LinkedList expressionStack = new LinkedList( );

		/**
		 * entry point to the report
		 */
		protected Report report;

		/**
		 * the execution context
		 */
		protected ExecutionContext context;

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

			queries = report.getQueries( );
			// first clear the collection in case the caller call this function
			// more than once.
			queries.clear( );

			// visit report
			for ( int i = 0; i < report.getContentCount( ); i++ )
				report.getContent( i ).accept( this );
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
			BaseQueryDefinition tempQuery = null;
			if ( item instanceof ListingDesign )
				tempQuery = createQuery( (ListingDesign) item );
			else
				tempQuery = createQuery( item );
			if ( tempQuery != null )
			{
				pushQuery( tempQuery );
				pushExpressions( tempQuery.getRowExpressions( ) );
			}
			handleReportItemExpressions( item );
			return tempQuery;
		}

		/**
		 * Clean up stack after visiting a report item
		 */
		private void finishVisit( BaseQueryDefinition query )
		{
			if ( query != null )
			{
				popExpressions( );
				popQuery( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
		 */
		public void visitFreeFormItem( FreeFormItemDesign container )
		{
			BaseQueryDefinition query = prepareVisit( container );

			for ( int i = 0; i < container.getItemCount( ); i++ )
				container.getItem( i ).accept( this );

			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
		 */
		public void visitGridItem( GridItemDesign grid )
		{
			BaseQueryDefinition query = prepareVisit( grid );

			for ( int i = 0; i < grid.getColumnCount( ); i++ )
			{
				ColumnDesign column = grid.getColumn( i );
				handleStyle( column.getStyle( ) );
			}

			for ( int i = 0; i < grid.getRowCount( ); i++ )
				handleRow( grid.getRow( i ) );

			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
		 */
		public void visitImageItem( ImageItemDesign image )
		{
			BaseQueryDefinition query = prepareVisit( image );

			handleAction( image.getAction( ) );
			if ( image.getImageSource( ) == ImageItemDesign.IMAGE_EXPRESSION )
			{
				addExpression( image.getImageExpression( ) );
				addExpression( image.getImageFormat( ) );
			}

			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
		 */
		public void visitLabelItem( LabelItemDesign label )
		{
			BaseQueryDefinition query = prepareVisit( label );
			handleAction( label.getAction( ) );
			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitExtendedItem(org.eclipse.birt.report.engine.ir.ExtendedItemDesign)
		 */
		public void visitExtendedItem( ExtendedItemDesign item )
		{
			//create user-defined generation-time helper object
			ExtendedItemHandle handle = (ExtendedItemHandle) item.getHandle( );
			String tagName = handle.getExtensionName( );

			// TODO: check in plugin registry whetherthe needQuery property is
			// set to host or item.
			// Only do the following for "host"

			IReportItemQuery itemQuery = ExtensionManager.getInstance( )
					.createQueryItem( tagName );
			IBaseQueryDefinition[] queries = null;
			IBaseQueryDefinition parentQuery = getParentQuery( );
			if ( itemQuery != null )
			{
				try
				{
					itemQuery.setModelObject( handle );

					queries = itemQuery.getReportQueries( parentQuery );
					
				}
				catch ( BirtException ex )
				{
					logger.log(Level.WARNING, ex.getMessage(), ex);
				}
				if ( queries != null )
				{
					item.setQueries(queries);
					for ( int i = 0; i < queries.length; i++ )
					{
						if (queries[i] != null)
						{
							this.queries.add( queries[i] );
						}
					}
					if (queries.length > 0)
					{
						IBaseQueryDefinition query = queries[0];
						if (query != null)
						{
							item.setQuery( query );
							pushQuery( query );
							pushExpressions( query.getRowExpressions( ) );
							handleReportItemExpressions( item );
							// handleActionExpressions(item.getAction());
							popExpressions( );
							popQuery( );
						}
					}
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
		 */
		public void visitListItem( ListItemDesign list )
		{
			BaseQueryDefinition query = prepareVisit( list );
			if ( query == null )
			{
				visitListBand( list.getHeader( ) );
				visitListBand( list.getFooter( ) );
			}
			else
			{
				pushExpressions( query.getBeforeExpressions( ) );
				visitListBand( list.getHeader( ) );
				popExpressions( );

				SlotHandle groupsSlot = ( (ListHandle) list.getHandle( ) )
						.getGroups( );
				for ( int i = 0; i < list.getGroupCount( ); i++ )
				{
					handleListGroup( list.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ) );
				}

				if ( list.getDetail( ).getContentCount( ) != 0 )
				{
					query.setUsesDetails( true );
				}

				pushExpressions( query.getRowExpressions( ) );
				visitListBand( list.getDetail( ) );
				popExpressions( );

				pushExpressions( query.getAfterExpressions( ) );
				visitListBand( list.getFooter( ) );
				popExpressions( );
			}
			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
		 */
		public void visitTextItem( TextItemDesign text )
		{
			BaseQueryDefinition query = prepareVisit( text );
			if ( text.getDomTree( ) == null )
			{
				String content = getLocalizedString( text.getTextKey( ), text
						.getText( ) );
				text.setDomTree( new TextParser( ).parse( content, text
						.getTextType( ) ) );
			}
			Document doc = text.getDomTree( );
			if ( doc != null )
			{
				getEmbeddedExpression( doc.getFirstChild( ), text );
			}
			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public void visitTableItem( TableItemDesign table )
		{
			BaseQueryDefinition query = prepareVisit( table );
			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				ColumnDesign column = table.getColumn( i );
				handleStyle( column.getStyle( ) );
			}
			if ( query == null )
			{
				handleTableBand( table.getHeader( ) );
				handleTableBand( table.getFooter( ) );
			}
			else
			{
				pushExpressions( query.getBeforeExpressions( ) );
				handleTableBand( table.getHeader( ) );
				popExpressions( );
				SlotHandle groupsSlot = ( (TableHandle) table.getHandle( ) )
						.getGroups( );
				for ( int i = 0; i < table.getGroupCount( ); i++ )
				{
					handleTableGroup( table.getGroup( i ),
							(GroupHandle) groupsSlot.get( i ) );
				}

				if ( table.getDetail( ).getRowCount( ) != 0 )
				{
					query.setUsesDetails( true );
				}

				pushExpressions( query.getRowExpressions( ) );
				handleTableBand( table.getDetail( ) );
				popExpressions( );

				pushExpressions( query.getAfterExpressions( ) );
				handleTableBand( table.getFooter( ) );
				popExpressions( );
			}
			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
		 */
		public void visitMultiLineItem( MultiLineItemDesign multiLine )
		{
			BaseQueryDefinition query = prepareVisit( multiLine );

			addExpression( multiLine.getContent( ) );
			addExpression( multiLine.getContentType( ) );
			finishVisit( query );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitDataItem(org.eclipse.birt.report.engine.ir.DataItemDesign)
		 */
		public void visitDataItem( DataItemDesign data )
		{
			BaseQueryDefinition query = prepareVisit( data );
			handleReportItemExpressions( data );
			handleAction( data.getAction( ) );
			addExpression( data.getValue( ) );
			finishVisit( query );
		}

		/**
		 * handle expressions common to all report items
		 * 
		 * @param item
		 *            a report item
		 */
		protected void handleReportItemExpressions( ReportItemDesign item )
		{
			if ( item.getVisibility( ) != null )
			{
				for ( int i = 0; i < item.getVisibility( ).count( ); i++ )
				{
					addExpression( item.getVisibility( ).getRule( i )
							.getExpression( ) );
				}
			}
			addExpression( item.getBookmark( ) );
			handleStyle( item.getStyle( ) );
			handleHighlightExpressions( item.getHighlight( ) );
			handleMapExpressions( item.getMap( ) );
		}

		/**
		 * @param band
		 *            the list band
		 */
		protected void visitListBand( ListBandDesign band )
		{
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this );
			}
		}

		/**
		 * @param group
		 *            a grouping in a list
		 * @param handle
		 *            handle to a grouping element
		 */
		protected void handleListGroup( ListGroupDesign group,
				GroupHandle handle )
		{
			IGroupDefinition groupDefn = handleGroup( group, handle );
			pushQuery( groupDefn );
			pushExpressions( groupDefn.getBeforeExpressions( ) );
			visitListBand( group.getHeader( ) );
			popExpressions( );

			pushExpressions( groupDefn.getAfterExpressions( ) );
			visitListBand( group.getFooter( ) );
			popExpressions( );
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
			//inter-range
			groupDefn.setIntervalRange( handle.getIntervalRange( ) );
			//sort-direction
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
		 * processes a band in a table
		 */
		protected void handleTableBand( TableBandDesign band )
		{
			for ( int i = 0; i < band.getRowCount( ); i++ )
				handleRow( band.getRow( i ) );
		}

		/**
		 * processes a table group
		 */
		protected void handleTableGroup( TableGroupDesign group,
				GroupHandle handle )
		{
			IGroupDefinition groupDefn = handleGroup( group, handle );
			pushQuery( groupDefn );
			pushExpressions( groupDefn.getBeforeExpressions( ) );
			handleTableBand( group.getHeader( ) );
			popExpressions( );

			pushExpressions( groupDefn.getAfterExpressions( ) );
			handleTableBand( group.getFooter( ) );
			popExpressions( );
			popQuery( );
		}

		/**
		 * handle style, which may contain highlight/mapping expressions
		 * 
		 * @param style
		 *            style design
		 */
		protected void handleStyle( StyleDesign style )
		{
			/*
			 * if ( style != null ) { handleHighlight( style.getHighlight( ) );
			 * handleMap( style.getMap( ) ); }
			 */
		}

		/**
		 * handle mapping expressions
		 */
		protected void handleMapExpressions( MapDesign map )
		{
			if ( map != null )
			{
				for ( int i = 0; i < map.getRuleCount( ); i++ )
				{
					MapRuleDesign rule = map.getRule( i );
					if ( rule != null )
						addExpression( rule.getConditionExpr( ) );
				}
			}
		}

		/**
		 * handle highlight expressions
		 */
		protected void handleHighlightExpressions( HighlightDesign highlight )
		{
			if ( highlight != null )
			{
				for ( int i = 0; i < highlight.getRuleCount( ); i++ )
				{
					HighlightRuleDesign rule = highlight.getRule( i );
					if ( rule != null )
						addExpression( rule.getConditionExpr( ) );
				}
			}
		}

		/**
		 * handles action expressions, i.e, book-mark and hyper-link
		 * expressions.
		 */
		protected void handleAction( ActionDesign action )
		{
			if ( action != null )
			{
				switch ( action.getActionType( ) )
				{
					case ActionDesign.ACTION_BOOKMARK :
						addExpression( action.getBookmark( ) );
						break;
					case ActionDesign.ACTION_DRILLTHROUGH :
						DrillThroughActionDesign drillThrough = action
								.getDrillThrough( );
						if ( drillThrough != null )
						{
							addExpression( drillThrough.getBookmark( ) );
							if ( drillThrough.getParameters( ) != null )
							{
								Iterator ite = drillThrough.getParameters( )
										.entrySet( ).iterator( );
								while ( ite.hasNext( ) )
								{
									Map.Entry entry = (Map.Entry) ite.next( );
									assert entry.getValue( ) instanceof Expression;
									addExpression( (Expression) entry
											.getValue( ) );
								}
							}
						}
						break;
					case ActionDesign.ACTION_HYPERLINK :
						addExpression( action.getHyperlink( ) );
						break;
					default :
						assert false;
				}
			}
		}

		/**
		 * visit content of a row
		 */
		protected void handleRow( RowDesign row )
		{
			handleStyle( row.getStyle( ) );
			if ( row.getVisibility( ) != null )
			{
				for ( int i = 0; i < row.getVisibility( ).count( ); i++ )
					addExpression( row.getVisibility( ).getRule( i )
							.getExpression( ) );
			}
			addExpression( row.getBookmark( ) );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				CellDesign cell = row.getCell(i);
				if (cell != null)
				{
					handleCell( cell );
				}
			}
		}

		/**
		 * handles a cell in a row
		 */
		protected void handleCell( CellDesign cell )
		{
				handleStyle( cell.getStyle( ) );
				for ( int i = 0; i < cell.getContentCount( ); i++ )
					cell.getContent( i ).accept( this );
		}

		/**
		 * A helper function for adding expression collection to stack
		 */
		protected void pushExpressions( Collection newExpressions )
		{
			this.expressionStack.addLast( this.expressions );
			this.expressions = newExpressions;
		}

		/**
		 * A helper function for removing expression collection from stack
		 */
		protected void popExpressions( )
		{
			assert !expressionStack.isEmpty( );
			this.expressions = (Collection) expressionStack.removeLast( );
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
		 * add expression to the expression collection on top of the expressions
		 * stack
		 * 
		 * @param expr
		 *            expression to be added
		 */
		protected void addExpression( IBaseExpression expr )
		{
			// expressions may be null, which means the expression is in the
			// topmost
			// element, and has no data set associated with it.
			if ( expr != null && expressions != null )
				expressions.add( expr );
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
		 * @return the expression collection
		 */
		protected Collection getExpressions( )
		{
			return expressions;
		}

		/**
		 * @return a unique query name, based on a simple integer counter
		 */
		protected String createUniqueQueryName( )
		{
			queryCount++;
			return String.valueOf( queryCount );
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
			DataSetHandle dsHandle = (DataSetHandle) ( (ReportItemHandle) item
					.getHandle( ) ).getDataSet( );

			if ( dsHandle != null )
			{
				ReportItemHandle riHandle = (ReportItemHandle) item.getHandle( );
				QueryDefinition query = new QueryDefinition( getParentQuery( ) );
				query.setDataSetName( dsHandle.getName( ) );

				query.getInputParamBindings( )
						.addAll(
								createParamBindings( riHandle
										.paramBindingsIterator( ) ) );
				this.queries.add( query );
				item.setQuery( query );
				return query;
			}
			return null;
		}

		/**
		 * create query for listing report item
		 * 
		 * @param listing
		 *            the listing item
		 * @return a report query definition
		 */
		protected BaseQueryDefinition createQuery( ListingDesign listing )
		{
			// creates its own query
			BaseQueryDefinition query = createQuery( (ReportItemDesign) listing );
			if ( query != null )
			{
				query.getSorts( ).addAll( createSorts( listing ) );
				query.getFilters( ).addAll( createFilters( listing ) );
				return query;
			}

			// creates a subquery, instead
			if ( getTransform( ) == null )
			{
				return null;
			}
			String name = createUniqueQueryName( );
			SubqueryDefinition subQuery = new SubqueryDefinition( name );
			listing.setQuery( subQuery );
			subQuery.getSorts( ).addAll( createSorts( listing ) );
			subQuery.getFilters( ).addAll( createFilters( listing ) );

			getTransform( ).getSubqueries( ).add( subQuery );
			return subQuery;

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
		 * Walk through the DOM tree from a text item to collect the embedded
		 * expressions and format expressions
		 * 
		 * After evaluating, the second child node of the embedded expression
		 * node holds the value if no error exists.
		 * 
		 * @param node
		 *            a node in the DOM tree
		 * @param text
		 *            the text object
		 */
		protected void getEmbeddedExpression( Node node, TextItemDesign text )
		{
			if ( node.getNodeType( ) == Node.ELEMENT_NODE )
			{
				if ( node.getNodeName( ).equals( "value-of" ) ) //$NON-NLS-1$
				{
					if ( !text.hasExpression( node.getFirstChild( )
							.getNodeValue( ) ) )
					{
						Expression expr = new Expression( node.getFirstChild( )
								.getNodeValue( ) );
						this.addExpression( expr );
						text.addExpression( node.getFirstChild( )
								.getNodeValue( ), expr );

						return;
					}

				}
				else if ( node.getNodeName( ).equals( "image" ) ) //$NON-NLS-1$
				{

					String imageType = ( (Element) ( node ) )
							.getAttribute( "type" ); //$NON-NLS-1$

					if ( "expr".equals( imageType ) ) //$NON-NLS-1$
					{
						if ( !text.hasExpression( node.getFirstChild( )
								.getNodeValue( ) ) )
						{
							Expression expr = new Expression( node
									.getFirstChild( ).getNodeValue( ) );

							this.addExpression( expr );
							text.addExpression( node.getFirstChild( )
									.getNodeValue( ), expr );
						}
					}
					return;
				}
				//call recursively
				for ( Node child = node.getFirstChild( ); child != null; child = child
						.getNextSibling( ) )
				{
					getEmbeddedExpression( child, text );
				}
			}

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
			int dteOpr = toDteFilterOperator( filterOpr );
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
						list.add( binding );
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
		 * converts model operator values to DtE IColumnFilter enum values
		 */
		protected int toDteFilterOperator( String modelOpr )
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
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) )
				return IConditionalExpression.OP_BETWEEN;
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) )
				return IConditionalExpression.OP_NOT_BETWEEN;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL ) )
				return IConditionalExpression.OP_NULL;
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL ) )
				return IConditionalExpression.OP_NOT_NULL;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE ) )
				return IConditionalExpression.OP_TRUE;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) )
				return IConditionalExpression.OP_FALSE;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LIKE ) )
				return IConditionalExpression.OP_LIKE;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_N ) )
				return IConditionalExpression.OP_TOP_N;
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N ) )
				return IConditionalExpression.OP_BOTTOM_N;
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT ) )
				return IConditionalExpression.OP_TOP_PERCENT;
			if ( modelOpr
					.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT ) )
				return IConditionalExpression.OP_BOTTOM_PERCENT;
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_ANY ) )
				return IConditionalExpression.OP_ANY;

			return IConditionalExpression.OP_NONE;
		}
	}
}