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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefn;
import org.eclipse.birt.data.engine.api.IBaseTransform;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefn;
import org.eclipse.birt.data.engine.api.IGroupDefn;
import org.eclipse.birt.data.engine.api.IInputParamBinding;
import org.eclipse.birt.data.engine.api.ISortDefn;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefn;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefn;
import org.eclipse.birt.data.engine.api.querydefn.InputParamBinding;
import org.eclipse.birt.data.engine.api.querydefn.JSExpression;
import org.eclipse.birt.data.engine.api.querydefn.ReportQueryDefn;
import org.eclipse.birt.data.engine.api.querydefn.SortDefn;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefn;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
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
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.parser.DOMParser;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * visit the report design and set all query and sub-query to report item
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
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
	 * @param report the entry point to the report design
	 * @param context the execution context
	 */
	public void build( Report report, ExecutionContext context )
	{
		new QueryBuilderVisitor( ).buildQuery( report, context );
	}

	// TODO handle extended item report query
	protected class QueryBuilderVisitor extends DefaultReportItemVisitorImpl
	{

		/**
		 * total queries
		 */
		protected Collection queries;

		/**
		 * current expression list
		 */
		protected Collection expressions;
		/**
		 * total query created in this report
		 */
		protected int queryCount = 0;
		/**
		 * the query stack
		 */
		protected LinkedList transformStack = new LinkedList( );
		/**
		 * the expression stack
		 */
		protected LinkedList expressionStack = new LinkedList( );

		protected Log logger = LogFactory.getLog( QueryBuilderVisitor.class );

		protected Report report;

		protected ExecutionContext context;

		/**
		 * create report definition for this report.
		 * 
		 * the create report should be inserted into report.getQueires(), and
		 * each report item will have the item.getQuery() be set.
		 * 
		 * @param report
		 */
		public void buildQuery( Report report, ExecutionContext context )
		{
			this.report = report;
			this.context = context;
			//use this array to store all queries.
			queries = report.getQueries( );
			//first clear the collection in case the caller call this
			//function more than once.
			queries.clear( );

			//visit the report items, to setup the queries.
			for ( int i = 0; i < report.getContentCount( ); i++ )
			{
				report.getContent( i ).accept( this );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign)
		 */
		public void visitFreeFormItem( FreeFormItemDesign container )
		{
			IBaseQueryDefn query = createQuery( container );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}

			handleReportItem( container );
			for ( int i = 0; i < container.getItemCount( ); i++ )
			{
				container.getItem( i ).accept( this );
			}
			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign)
		 */
		public void visitGridItem( GridItemDesign grid )
		{
			IBaseQueryDefn query = createQuery( grid );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}
			handleReportItem( grid );
			//handleReportItem(grid);
			for ( int i = 0; i < grid.getColumnCount( ); i++ )
			{
				ColumnDesign column = grid.getColumn( i );
				handleStyle( column.getStyle( ) );
			}
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				handleRow( grid.getRow( i ) );
			}

			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.birt.report.engine.ir.IReportItemVisitor#visitImageItem(org.eclipse.birt.report.engine.ir.ImageItemDesign)
		 */
		public void visitImageItem( ImageItemDesign image )
		{
			IBaseQueryDefn query = createQuery( image );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}

			handleReportItem( image );
			handleAction( image.getAction( ) );
			if ( image.getImageSource( ) == ImageItemDesign.IMAGE_EXPRESSION )
			{
				addExpression( image.getImageExpression( ) );
				addExpression( image.getImageFormat( ) );
			}

			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitLabelItem(org.eclipse.birt.report.engine.ir.LabelItemDesign)
		 */
		public void visitLabelItem( LabelItemDesign label )
		{
			IBaseQueryDefn query = createQuery( label );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}
			handleReportItem( label );
			handleAction( label.getAction( ) );
			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitListItem(org.eclipse.birt.report.engine.ir.ListItemDesign)
		 */
		public void visitListItem( ListItemDesign list )
		{
			BaseQueryDefn query = createQuery( list );

			SlotHandle groupsSlot = ( (ListHandle) list.getHandle( ) )
					.getGroups( );
			assert query != null;

			pushTransform( query );

			pushExpressions( query.getBeforeExpressions( ) );
			handleReportItem( list );
			handleListBand( list.getHeader( ) );
			popExpressions( );

			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				handleListGroup( list.getGroup( i ), (GroupHandle) groupsSlot
						.get( i ) );
			}

			if ( list.getDetail( ).getContentCount( ) != 0 )
			{
				query.setUsesDetails( true );
			}

			pushExpressions( query.getRowExpressions( ) );
			handleListBand( list.getDetail( ) );
			popExpressions( );

			pushExpressions( query.getAfterExpressions( ) );
			handleListBand( list.getFooter( ) );
			popExpressions( );

			popTransform( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign)
		 */
		public void visitTextItem( TextItemDesign text )
		{
			IBaseQueryDefn query = createQuery( text );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}
			handleReportItem( text );
			if ( text.getDomTree( ) == null )
			{
				String content = getLocalizedString( text.getContentKey( ),
						text.getContent( ) );
				text.setDomTree( new DOMParser( ).parse( content, text
						.getContentType( ) ) );
			}
			Document doc = text.getDomTree( );
			if ( doc != null )
			{
				getEmbeddedExpression( doc.getFirstChild( ), text );
			}
			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign)
		 */
		public void visitTableItem( TableItemDesign table )
		{
			BaseQueryDefn query = createQuery( table );

			assert query != null;

			pushTransform( query );

			pushExpressions( query.getBeforeExpressions( ) );
			handleReportItem( table );

			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				ColumnDesign column = table.getColumn( i );
				handleStyle( column.getStyle( ) );
			}
			handleTableBand( table.getHeader( ) );
			popExpressions( );
			SlotHandle groupsSlot = ( (TableHandle) table.getHandle( ) )
					.getGroups( );
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				handleTableGroup( table.getGroup( i ), (GroupHandle) groupsSlot
						.get( i ) );
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

			popTransform( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.ReportItemVisitor#visitMultiLineItem(org.eclipse.birt.report.engine.ir.MultiLineItemDesign)
		 */
		public void visitMultiLineItem( MultiLineItemDesign multiLine )
		{
			IBaseQueryDefn query = createQuery( multiLine );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}
			handleReportItem( multiLine );

			addExpression( multiLine.getContent( ) );
			addExpression( multiLine.getContentType( ) );
			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		public void visitDataItem( DataItemDesign data )
		{
			IBaseQueryDefn query = createQuery( data );
			if ( query != null )
			{
				pushTransform( query );
				pushExpressions( query.getRowExpressions( ) );
			}
			handleReportItem( data );
			handleAction( data.getAction( ) );
			addExpression( data.getValue( ) );
			if ( query != null )
			{
				popExpressions( );
				popTransform( );
			}
		}

		/**
		 * handle the report item.
		 * 
		 * report item contains book-mark, style expressions
		 * 
		 * @param item
		 */
		protected void handleReportItem( ReportItemDesign item )
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
			handleHighlight(item.getHighlight());
			handleMap(item.getMap());
		}

		protected void handleListBand( ListBandDesign band )
		{
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this );
			}
		}

		protected void handleListGroup( ListGroupDesign group,
				GroupHandle handle )
		{
			IGroupDefn groupDefn = handleGroup( group, handle );
			pushTransform( groupDefn );
			pushExpressions( groupDefn.getBeforeExpressions( ) );
			handleListBand( group.getHeader( ) );
			popExpressions( );

			pushExpressions( groupDefn.getAfterExpressions( ) );
			handleListBand( group.getFooter( ) );
			popExpressions( );
			popTransform( );
		}

		protected IGroupDefn handleGroup( GroupDesign group, GroupHandle handle )
		{
			GroupDefn groupDefn = new GroupDefn( group.getName( ) );
			groupDefn.setKeyExpresion( handle.getKeyExpr( ) );
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
			getQuery( ).getGroups( ).add( groupDefn );

			return groupDefn;
		}

		protected void handleTableBand( TableBandDesign band )
		{
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				handleRow( band.getRow( i ) );
			}
		}

		protected void handleTableGroup( TableGroupDesign group,
				GroupHandle handle )
		{
			IGroupDefn groupDefn = handleGroup( group, handle );
			pushTransform( groupDefn );
			pushExpressions( groupDefn.getBeforeExpressions( ) );
			handleTableBand( group.getHeader( ) );
			popExpressions( );

			pushExpressions( groupDefn.getAfterExpressions( ) );
			handleTableBand( group.getFooter( ) );
			popExpressions( );
			popTransform( );
		}

		/**
		 * handle style design. style contains highlight/mapping expressions
		 * 
		 * @param style
		 *            style design
		 */
		protected void handleStyle( StyleDesign style )
		{
			/*if ( style != null )
			{
				handleHighlight( style.getHighlight( ) );
				handleMap( style.getMap( ) );
			}*/
		}

		/**
		 * handle mapping design
		 * 
		 * @param map
		 */
		protected void handleMap( MapDesign map )
		{
			if ( map != null )
			{
				for ( int i = 0; i < map.getRuleCount( ); i++ )
				{
					MapRuleDesign rule = map.getRule( i );
					if ( rule != null )
					{
						addExpression(rule.getConditionExpr());
					}

				}

			}
		}

		/**
		 * handle highlight design
		 * 
		 * @param highlight
		 */
		protected void handleHighlight( HighlightDesign highlight )
		{
			if ( highlight != null )
			{
				for ( int i = 0; i < highlight.getRuleCount( ); i++ )
				{
					HighlightRuleDesign rule = highlight.getRule( i );
					if ( rule != null )
					{
						addExpression(rule.getConditionExpr());
					}

				}

			}
		}

		/**
		 * visit action design. action design contains book-mark and hyper-link
		 * expressions.
		 * 
		 * @param action
		 *            action design
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
						assert false;
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
		 * 
		 * @param row
		 *            row design
		 */
		protected void handleRow( RowDesign row )
		{
			handleStyle( row.getStyle( ) );
			if ( row.getVisibility( ) != null )
			{
				for ( int i = 0; i < row.getVisibility( ).count( ); i++ )
				{
					addExpression( row.getVisibility( ).getRule( i )
							.getExpression( ) );
				}
			}
			addExpression( row.getBookmark( ) );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				handleCell( row.getCell( i ) );
			}
		}

		/**
		 * visit content of a cell
		 * 
		 * @param cell
		 *            cell design
		 */
		protected void handleCell( CellDesign cell )
		{
			handleStyle( cell.getStyle( ) );
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this );
			}
		}

		protected void pushExpressions( Collection expressions )
		{
			this.expressionStack.addLast( expressions );
			this.expressions = expressions;
		}

		protected void popExpressions( )
		{
			assert !expressionStack.isEmpty( );
			this.expressions = (Collection) expressionStack.removeLast( );
		}

		protected void pushTransform( IBaseTransform query )
		{
			this.transformStack.addLast( query );
		}

		protected void popTransform( )
		{
			assert !transformStack.isEmpty( );
			transformStack.removeLast( );
		}

		/**
		 * add expression into the expression list
		 * 
		 * @param expr
		 *            expression to be added
		 */
		protected void addExpression( IBaseExpression expr )
		{
			//expressions may be null, that means the
			//expression in the topmost element, which has
			//no data set associated with it.
			if ( expr != null && expressions != null )
			{
				expressions.add( expr );
			}
		}

		protected IBaseTransform getTransform( )
		{
			if ( transformStack.isEmpty( ) )
			{
				return null;
			}
			return (IBaseTransform) transformStack.getLast( );
		}

		protected BaseQueryDefn getQuery( )
		{
			if ( transformStack.isEmpty( ) )
			{
				return null;
			}
			for ( int i = transformStack.size( ) - 1; i >= 0; i-- )
			{
				if ( transformStack.get( i ) instanceof BaseQueryDefn )
				{
					return (BaseQueryDefn) transformStack.get( i );
				}
			}
			return null;

		}

		protected Collection getExpressions( )
		{
			return expressions;
		}

		protected String createUniqueQueryName( )
		{
			queryCount++;
			return String.valueOf( queryCount );
		}

		/**
		 * create query for non-listing report item
		 * 
		 * @param item
		 * @return
		 */
		protected BaseQueryDefn createQuery( ReportItemDesign item )
		{
			DataSetHandle dsHandle = (DataSetHandle) ( (ReportItemHandle) item
					.getHandle( ) ).getDataSet( );

			if ( dsHandle != null )
			{
				ReportItemHandle riHandle =(ReportItemHandle)item.getHandle();
				ReportQueryDefn query = new ReportQueryDefn( getQuery( ) );
				query.setDataSetName( dsHandle.getName( ) );

				query.getInputParamBindings( ).addAll(
						createParamBindings( riHandle.paramBindingsIterator() ) );
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
		 * @return
		 */
		protected BaseQueryDefn createQuery( ListingDesign listing )
		{
			DataSetHandle dsHandle = (DataSetHandle) ( (ReportItemHandle) listing
					.getHandle( ) ).getDataSet( );

			if ( dsHandle != null )
			{
				ListingHandle listingHandle = (ListingHandle)listing.getHandle();
				ReportQueryDefn query = new ReportQueryDefn( getQuery( ) );
				query.setDataSetName( dsHandle.getName( ) );
				listing.setQuery( query );
				query.getInputParamBindings( ).addAll(
						createParamBindings( listingHandle.paramBindingsIterator() ) );
				query.getSorts( ).addAll( createSorts( listing ) );
				query.getFilters( ).addAll( createFilters( listing ) );
				this.queries.add( query );
				return query;
			}
			String name = createUniqueQueryName( );
			SubqueryDefn query = new SubqueryDefn( name );
			listing.setQuery( query );
			query.getSorts( ).addAll( createSorts( listing ) );
			query.getFilters( ).addAll( createFilters( listing ) );
			getTransform( ).getSubqueries( ).add( query );
			return query;
		}

		/**
		 * get Localized string by the resouce key of this item and
		 * <code>Locale</code> object in <code>context</code>
		 * 
		 * @param resourceKey
		 *            the resource key
		 * @param text
		 *            the default value
		 * 
		 * @return the localized string if it is defined in report deign, else
		 *         return the default value
		 *  
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
				if ( logger.isErrorEnabled( ) )
				{
					logger.error( "get resource error, resource key:"
							+ resourceKey + " Locale:"
							+ context.getLocale( ).toString( ) );
				}
				return text;
			}
			return ret;
		}

		/**
		 * Walk on the DOM tree to evaluate the embedded expression and format
		 * 
		 * After evaluating,the second child node of embedded expression node
		 * holds the value if no error exists, otherwise it has not the second
		 * child node.
		 * 
		 * @param node
		 *            the node in the DOM tree
		 * @param style
		 *            the style design for format string
		 */
		protected void getEmbeddedExpression( Node node, TextItemDesign text )
		{
			if ( node.getNodeType( ) == Node.ELEMENT_NODE )
			{
				Element ele = (Element) node;
				if ( node.getNodeName( ).equals( "value-of" ) ) // $NON-NLS-1$
				{
					if ( !text.containExpr( node.getFirstChild( )
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
				else if ( node.getNodeName( ).equals( "image" ) ) // $NON-NLS-1$
				{

					String imageType = ( (Element) ( node ) )
							.getAttribute( "type" ); // $NON-NLS-1$

					if ( "expr".equals( imageType ) ) // $NON-NLS-1$
					{
						if ( !text.containExpr( node.getFirstChild( )
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
		 * create Filter by giving FilterConditionHandle
		 * 
		 * @param handle
		 *            FilterConditionHandle
		 * @return the filter
		 */
		private IFilterDefn createFilter( FilterConditionHandle handle )
		{
			String filterExpr = handle.getExpr( );
			if ( filterExpr == null || filterExpr.length( ) == 0 )
				return null; // no filter defined

			// converts to DtE exprFilter if there is no operator
			String filterOpr = handle.getOperator( );
			if ( filterOpr == null || filterOpr.length( ) == 0 )
				return new FilterDefn( new JSExpression( filterExpr ) );

			/*
			 * has operator defined, try to convert filter condition to
			 * operator/operand style column filter with 0 to 2 operands
			 */

			String column = filterExpr;
			int dteOpr = toDteFilterOperator( filterOpr );
			String operand1 = handle.getValue1( );
			String operand2 = handle.getValue2( );
			return new FilterDefn( new ConditionalExpression( column, dteOpr,
					operand1, operand2 ) );
		}

		/**
		 * create a filter array by giving iterator
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
					IFilterDefn filter = createFilter( filterHandle );
					filters.add( filter );
				}
			}
			return filters;
		}

		/**
		 * create filter array by giving ListingDesign
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
		 * create fileter array by giving DataSetHandle
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
		 * create filter array by giving GroupHandle
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
		 * create sort array by giving iterator
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
		 * create sort by giving SortKeyHandle
		 * 
		 * @param handle
		 *            the SortKeyHandle
		 * @return the sort object
		 */
		private ISortDefn createSort( SortKeyHandle handle )
		{
			SortDefn sort = new SortDefn( );
			sort.setExpression( handle.getKey( ) );
			sort.setSortDirection( handle.getDirection( ).equals(
					DesignChoiceConstants.SORT_DIRECTION_ASC ) ? 0 : 1 );
			return sort;

		}

		/**
		 * create sort array by giving ListingDesign
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
		protected IInputParamBinding createParamBinding(
				ParamBindingHandle handle )
		{
			if ( handle.getExpression( ) == null )
				return null; // no expression is bound
			JSExpression expr = new JSExpression( handle.getExpression( ) );
			// model provides binding by name only
			return new InputParamBinding( handle.getParamName( ), expr );

		}
		
		/**
		 * create input parameter bindings
		 * @param iter
		 * @return
		 */
		protected ArrayList createParamBindings(Iterator iter)
		{
			ArrayList list = new ArrayList();
	        if ( iter != null )
	        {
	            while ( iter.hasNext() )
	            {
	                ParamBindingHandle modelParamBinding = (ParamBindingHandle) iter.next();
	                IInputParamBinding binding = createParamBinding( modelParamBinding );
	                if(binding != null)
	                {
	                	list.add(binding);
	                }
	            }
	        }
	        return list;
		}


		protected int parseInterval( String interval )
		{
			if ( DesignChoiceConstants.INTERVAL_YEAR.equals( interval ) ) 
			{
				return IGroupDefn.YEAR_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_MONTH.equals( interval ) ) 
			{
				return IGroupDefn.MONTH_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_WEEK.equals( interval ) ) // 
			{
				return IGroupDefn.WEEK_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_QUARTER.equals( interval ) ) 
			{
				return IGroupDefn.QUARTER_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_DAY.equals( interval ) ) 
			{
				return IGroupDefn.DAY_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_HOUR.equals( interval ) ) 
			{
				return IGroupDefn.HOUR_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_MINUTE.equals( interval ) ) 
			{
				return IGroupDefn.MINUTE_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_PREFIX.equals( interval ) ) 
			{
				return IGroupDefn.STRING_PREFIX_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_SECOND.equals( interval ) ) 
			{
				return IGroupDefn.SECOND_INTERVAL;
			}
			if ( DesignChoiceConstants.INTERVAL_INTERVAL.equals( interval ) ) 
			{
				return IGroupDefn.NUMERIC_INTERVAL;
			}
			return IGroupDefn.NO_INTERVAL;
		}

		protected int parseSortDirection( String direction )
		{
			if ( "asc".equals( direction ) ) // $NON-NLS-1$
			{
				return ISortDefn.SORT_ASC;
			}
			if ( "desc".equals( direction ) ) // $NON-NLS-1$
			{
				return ISortDefn.SORT_DESC;
			}
			assert false;
			return 0;
		}

//		 Convert model operator value to DtE IColumnFilter enum value
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
			if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_ANY ) )
				return IConditionalExpression.OP_ANY;

			return IConditionalExpression.OP_NONE;
		}
	}
}