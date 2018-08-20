/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IExecutorContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.eclipse.birt.report.engine.script.internal.OnCreateScriptVisitor;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.engine.toc.TOCEntry;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * Abstract class, Represents a report item executor. Report item executor
 * execute a report item design, generate a report item instance, and pass the
 * instance to <code>emitter</code>.
 * <p>
 * According to the report item design and current context information, executor
 * calculate expression in report item design, get data instance from data
 * source, and fill it into the report item instance, and set property for the
 * report item instance.
 * <p>
 * Reset the state of report item executor by calling <code>reset()</code>
 * 
 */
//TODO: can we reuse the content object instead of create different content object for the same report design element?
public abstract class ReportItemExecutor implements IReportItemExecutor
{
	
	private static final String RELATED_ENTITY_ID_NAMED_EXPRESSION_NAME = "relatedEntityId";
	private static final String RELATED_ENTITY_ID_PROPERTY_KEY = "reportEngine.relatedEntityIdNamedExpression";

	/**
	 * executor manager used to create this executor.
	 */
	protected ExecutorManager manager;
	
	/**
	 * the type of the executor
	 */
	protected int type;

	/**
	 * the report content
	 */
	protected IReportContent report;

	/**
	 * the executor context
	 */
	protected ExecutionContext context;


	/**
	 * the executed report design
	 */
	protected ReportItemDesign design;
	
	/**
	 * the create report content
	 */
	protected IContent content;
	
	/**
	 * rset used to execute the item
	 */
	protected IQueryResultSet rset;
	
	/**
	 * toc created by this report item
	 */
	protected TOCEntry tocEntry;
	
	/**
	 * 
	 */
	protected OnCreateScriptVisitor onCreateVisitor;

	/**
	 * IExecutorContext
	 */
	protected IExecutorContext executorContext;

	/**
	 * model handle
	 */
	protected Object handle;

	/**
	 * parent executor
	 */
	protected ReportItemExecutor parent;
	
	/**
	 * the sequence id of the child executor
	 */
	protected long uniqueId;

	/**
	 * the instance id of the generated content.
	 */
	protected InstanceID instanceId;
	
	/**
	 * the current result set before execute this report item.
	 */
	protected IBaseResultSet[] parentRsets;

	/**
	 * construct a report item executor by giving execution context and report
	 * executor visitor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	protected ReportItemExecutor( ExecutorManager manager, int type )
	{
		this.manager = manager;
		this.type = type;
		this.context = manager.context;
		this.report = context.getReportContent( );
		this.onCreateVisitor = new OnCreateScriptVisitor( context );
		this.executorContext = null;
		this.parent = null;
		this.handle = null;
		this.design = null;
		this.content = null;
		this.rset = null;
		this.tocEntry = null;
		this.uniqueId = 0;
		this.instanceId = null;
		this.parentRsets = null;
	}
	
	public void setContext( IExecutorContext context )
	{
		this.executorContext = context;
	}

	public void setModelObject( Object handle )
	{
		this.handle = handle;
		if ( handle instanceof ReportItemDesign )
		{
			this.design = (ReportItemDesign) handle;
		}
		if ( handle instanceof ReportElementHandle )
		{
			ReportElementHandle element = (ReportElementHandle) handle;
			Report report = context.getReport( );
			this.design = report.findDesign( element );
		}
	}

	public void setParent( IReportItemExecutor parent )
	{
		assert parent instanceof ReportItemExecutor;
		this.parent = (ReportItemExecutor) parent;
	}

	public IExecutorContext getContext( )
	{
		return manager.getExecutorContext( );
	}

	public Object getModelObject( )
	{
		return handle;
	}

	public IReportItemExecutor getParent( )
	{
		return parent;
	}

	/**
	 * does the executor has child executor
	 * 
	 * @return
	 * @throws BirtException
	 */
	public boolean hasNextChild( ) throws BirtException
	{
		return false;
	}

	public IReportItemExecutor getNextChild( ) throws BirtException
	{
		return null;
	}

	/**
	 * reset the state of the report item executor. This operation will reset
	 * all property of this object
	 * 
	 * @throws BirtException
	 * 
	 */
	public void close( ) throws BirtException
	{
		this.executorContext = null;
		this.parent = null;
		this.handle = null;
		this.content = null;
		this.rset = null;
		this.tocEntry = null;
		this.uniqueId = 0;
		this.instanceId = null;
		this.design = null;
		this.parentRsets = null;

		manager.releaseExecutor( type, this );
	}

	IContent getParentContent()
	{
		if (parent != null)
		{
			return parent.getContent( );
		}
		return null;
	}
	ReportItemDesign getDesign()
	{
		return design;
	}
	
	public IContent getContent()
	{
		return content;
	}
	
	void setContent(IContent content)
	{
		context.setContent( content );
		this.content = content;
	}

	protected Object evaluate( Expression expr )
	{
		try
		{
			return context.evaluate( expr );
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
		return null;
	}

	protected String evaluateString( Expression expr )
	{
		try
		{
			Object value = context.evaluate( expr );
			return DataTypeUtil.toString( value );
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
		return null;
	}

	protected String evaluateBookmark( Expression expr )
	{
		try
		{
			Object value = context.evaluate( expr );
			return value != null ? value.toString( ) : null;
		}
		catch ( BirtException ex )
		{
			context.addException( ex );
		}
		return null;
	}

	protected Boolean evaluateBoolean( Expression expr )
	{
		try
		{
			Object value = context.evaluate( expr );
			return DataTypeUtil.toBoolean( value );
		}
		catch ( BirtException ex )
		{
			getLogger( ).log(
					Level.WARNING,
					"Invalid boolean expression:"
							+ ( expr == null ? "null" : expr.toString( ) ) );
		}
		return null;
	}

	/**
	 * Calculate the bookmark value which is set to
	 * <code>ReportItemContent</code> if the bookmark is not null
	 * 
	 * @param item
	 *            the ReportItemContent object
	 * @throws BirtException 
	 */
	protected void processBookmark( ReportItemDesign item, IContent itemContent )
	{
		// don't support the book mark in master page as the master page may be
		// used multiple times while the book mark should be unique in the whole
		// report.
		if ( context.isExecutingMasterPage( ) )
		{
			return;
		}
		Object tmp = null;
		String bookmark = null;
		if ( item.getBookmark( ) == null )
		{
			if ( item.getQuery( ) != null )
			{
				if ( context.getReportletBookmark( item.getID( ) ) != null )
				{
					bookmark = context.getReportletBookmark( item.getID( ) );
				}
				else
				{
					if ( !( item instanceof DataItemDesign ) )
						bookmark = this.manager.nextBookmarkID( );
				}
			}
		}
		else
		{
			tmp = evaluate( item.getBookmark( ) );
			if ( tmp != null && !"".equals( tmp ) )
			{
				bookmark = tmp.toString( );
				BookmarkManager bookmarkManager = context.getBookmarkManager( );
				if ( bookmarkManager.exist( bookmark ) )
				{
					bookmark = bookmarkManager.createBookmark( bookmark );
				}
				else
				{
					bookmarkManager.addBookmark( bookmark );
				}
			}
		}
		if ( bookmark != null )
		{
			itemContent.setBookmark( bookmark );
			// we need also set the bookmark to the result set
			if ( rset != null )
			{
				IBaseQueryResults resultSet = rset.getQueryResults( );
				if ( resultSet != null )
				{
					IDataQueryDefinition query = item.getQuery( );
					if ( query instanceof IQueryDefinition )
					{
						resultSet.setName( bookmark );
					}
				}
			}
		}
		Expression tocExpr = item.getTOC( );
		if ( tocExpr != null )
		{
			Object toc = evaluate( tocExpr );
			if ( toc != null )
			{
				itemContent.setTOC( toc );
			}
		}
	}

	/**
	 * Calculate the action value which is set to <code>ReportItemContent</code>
	 * if the action is not null.
	 * 
	 * @param action
	 *            the action design object
	 * @param itemContent
	 *            create report item content object
	 */
	protected void processAction( ReportItemDesign item, IContent itemContent )
	{
		assert itemContent != null;

		ActionDesign action = item.getAction( );
		if ( action != null )
		{

			String targetWindow = action.getTargetWindow( );
			String tooltip = action.getTooltip( );
			switch ( action.getActionType( ) )
			{
				case ActionDesign.ACTION_HYPERLINK :
					assert action.getHyperlink( ) != null;
					String hyperlink = evaluateString( action.getHyperlink( ) );
					if ( hyperlink != null )
					{
						IHyperlinkAction obj = report.createActionContent( );
						obj.setHyperlink( hyperlink, targetWindow );
						obj.setTooltip( tooltip );
						itemContent.setHyperlinkAction( obj );
					}
					break;
				case ActionDesign.ACTION_BOOKMARK :
					assert action.getBookmark( ) != null;
					String bookmark = evaluateBookmark( action.getBookmark( ) );
					if ( bookmark != null && !bookmark.equals( "" ) )
					{
						IHyperlinkAction obj = report.createActionContent( );
						obj.setBookmark( bookmark );
						obj.setTooltip( tooltip );
						itemContent.setHyperlinkAction( obj );
					}
					break;
				case ActionDesign.ACTION_DRILLTHROUGH :
					assert action.getDrillThrough( ) != null;
					DrillThroughActionDesign drill = action.getDrillThrough( );
					bookmark = evaluateBookmark( drill.getBookmark( ) );
					boolean isBookmark = drill.getBookmarkType( );
					Map<String, List<Object>> paramsVal = new HashMap<String, List<Object>>( );
					Map<String, List<Expression>> params = drill.getParameters( );
					if ( params != null )
					{
						Set<Map.Entry<String, List<Expression>>> entries = params
								.entrySet( );
						for ( Map.Entry<String, List<Expression>> entry : entries )
						{
							List<Expression> ExprList = entry.getValue( );
							if ( ExprList != null  && !ExprList.isEmpty( ) )
							{
								ArrayList<Object> valueList = new ArrayList<Object>( );
								for( Expression valueExpr: ExprList )
								{
									Object paramValue = evaluate( valueExpr );
									valueList.add( paramValue );
								}
								paramsVal.put( entry.getKey( ), valueList );
							}
						}
					}

					String reportName = evaluateString( drill.getReportName( ) );
					/*
					 * we do not set absoluted path here. we now changed this in
					 * render time ReportDesignHandle design =
					 * context.getDesign( ); if ( design != null ) { URL
					 * reportURL = design.findResource( reportName,
					 * IResourceLocator.LIBRARY ); if ( reportURL != null ) {
					 * String reportFile = reportURL.getFile( ); if ( reportFile
					 * != null ) { reportName = reportFile; } } }
					 */
					String format = drill.getFormat( );
					// XXX Do not support Search criteria
					IHyperlinkAction obj = report.createActionContent( );
					String targetFileType = drill.getTargetFileType( );
					obj.setDrillThrough( bookmark, isBookmark, reportName,
							paramsVal, null, targetWindow, format,
							targetFileType );
					obj.setTooltip( tooltip );
					itemContent.setHyperlinkAction( obj );
					break;
				default :
					assert false;
			}

		}
	}

	/**
	 * Sets the visibility property for ReportItem.
	 * 
	 * @param design
	 *            The <code>ReportItemDesign</code> object.
	 * @param content
	 *            The <code>ReportItemContent</code> object.
	 */
	protected void processVisibility( ReportItemDesign design, IContent content )
	{
		VisibilityDesign visibility = design.getVisibility( );
		if ( visibility != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				Expression expr = rule.getExpression( );
				Boolean result = evaluateBoolean( expr );
				if ( result == null )
				{
					continue;
				}
				boolean isHidden = result.booleanValue( );
				// The report element appears by default and if the
				// result is not hidden, then ignore it.
				if ( isHidden )
				{
					String format = rule.getFormat( );
					//escape "." in css text
					if ( format != null && format.indexOf( "." ) > 0 )
					{
						format = "\"" + format + "\"";
					}
					// we should use rule as the string as
					buffer.append( format );
					buffer.append( "," ); //$NON-NLS-1$
				}
			}
			if ( buffer.length( ) != 0 )
			{
				buffer.setLength( buffer.length( ) - 1 );
				content.getStyle( ).setVisibleFormat( buffer.toString( ) );
			}
		}
	}

	/**
	 * Sets the visibility property for column.
	 */
	protected void processColumnVisibility( ColumnDesign design, Column column )
	{
		VisibilityDesign visibility = design.getVisibility( );
		if ( visibility != null )
		{
			StringBuffer buffer = new StringBuffer( );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				Expression expr = rule.getExpression( );
				Boolean result = evaluateBoolean( expr );
				if ( result == null )
				{
					continue;
				}
				boolean isHidden = ( (Boolean) result ).booleanValue( );
				// The report element appears by default and if the
				// result is not hidden, then ignore it.
				if ( isHidden )
				{
					buffer.append( rule.getFormat( ) );
					buffer.append( "," ); //$NON-NLS-1$
				}
			}
			if ( buffer.length( ) != 0 )
			{
				buffer.setLength( buffer.length( ) - 1 );
				column.setVisibleFormat( buffer.toString( ) );
			}
		}
	}

	protected void processUserProperties( ReportElementDesign design,
			IContent content )
	{
		Map<String, Expression> exprs = design.getUserProperties( );
		if (exprs == null) 
		{
			exprs = new HashMap<String, Expression>();
		}
		
		Set allowedContent = new HashSet<>();
		allowedContent.add(ExecutorManager.ROWITEM);
		allowedContent.add(ExecutorManager.EXTENDEDITEM);
		allowedContent.add(ExecutorManager.LISTBANDITEM);
		allowedContent.add(ExecutorManager.LISTGROUPITEM);
		allowedContent.add(ExecutorManager.EXTENDEDITEM);
//		allowedContent.add(ExecutorManager.GRIDITEM);
		//TODO should we somehow filer processed Content?
		if (!exprs.containsKey(RELATED_ENTITY_ID_NAMED_EXPRESSION_NAME)) {
			String expression = (String) getContext().getAppContext().get(RELATED_ENTITY_ID_PROPERTY_KEY);
			if (expression != null) {
				exprs.put(RELATED_ENTITY_ID_NAMED_EXPRESSION_NAME, Expression.newScript(expression ));
			}
		}
		
		if ( exprs != null )
		{
			HashMap<String, Object> values = new HashMap<String, Object>( exprs
					.size( ) );
			for ( Map.Entry<String, Expression> entry : exprs.entrySet( ) )
			{
				String name = entry.getKey( );
				Expression expr = entry.getValue( );
				if ( expr != null )
				{
					try
					{
						Object value = context.evaluate( expr );
						values.put( name, value );
					}
					catch ( BirtException ex )
					{
						context.addException( design, ex );
					}
				}
			}
			if ( !values.isEmpty( ) )
			{
				content.setUserProperties( values );
			}
		}
	}

	protected DataID getDataID( )
	{
		if ( parent != null )
		{
			//if result set is not null, it is a normal executor.
			IQueryResultSet prset = parent.rset;
			if ( prset != null )
			{
				DataSetID dataSetID = prset.getID( );
				long position = prset.getRowIndex( );
				if ( dataSetID == null )
				{
					return null;
				}
				return new DataID( dataSetID, position );
			}
			// the result set is NULL or it may be a extended item executor, try
			// getQueryResults
			IBaseResultSet[] rsets = parent.getQueryResults( );
			if ( ( rsets != null ) && ( rsets.length > 0 ) &&
					( rsets[0] != null ) )
			{
				if ( rsets[0] instanceof IQueryResultSet )
				{
					IQueryResultSet rset = (IQueryResultSet) rsets[0];
					DataSetID dataSetID = rset.getID( );
					long position = rset.getRowIndex( );
					return new DataID( dataSetID, position );
				}
				if ( rsets[0] instanceof ICubeResultSet )
				{
					ICubeResultSet rset = (ICubeResultSet) rsets[0];
					DataSetID dataSetID = rset.getID( );
					String cellId = rset.getCellIndex( );
					return new DataID( dataSetID, cellId );
				}
			}
		}
		return null;
	}
	
	protected long generateUniqueID( )
	{
		if ( parent != null )
		{
			return parent.uniqueId++;
		}
		return manager.generateUniqueID( );
	}
	
	
	protected long getElementId( )
	{
		if ( design != null )
		{
			return design.getID( );
		}
		if ( handle != null && handle instanceof DesignElementHandle )
		{
			return ( (DesignElementHandle) handle ).getID( );
		}
		return -1;
	}
	
	protected InstanceID getInstanceID( )
	{
		if ( instanceId == null )
		{
			InstanceID pid = parent == null ? null : parent.getInstanceID( );
			long uid = generateUniqueID( );
			long id = getElementId( );
			DataID dataId = getDataID( );
			instanceId = new InstanceID( pid, uid, id, dataId );
		}
		return instanceId;
	}

	protected void initializeContent( ReportElementDesign design,
			IContent content )
	{
		IContent parent = getParentContent( );
		if ( parent != null )
		{
			content.setParent( parent );
		}
		InstanceID id = getInstanceID( );
		content.setInstanceID( id );
		content.setGenerateBy( design );
		if ( design instanceof ReportItemDesign)
		{
			processAltText( (ReportItemDesign)design, content );
		}
	}

	protected void processAltText( ReportItemDesign design, IContent content )
	{
		Expression altTextExpr = design.getAltText( );
		if ( altTextExpr != null )
		{
			Object altText = evaluate( altTextExpr );
			if ( altText != null )
			{
				content.setAltText( altText.toString( ) );
				content.setAltTextKey( design.getAltTextKey( ) );
			}
		}
	}
	
	TOCEntry getParentTOCEntry()
	{
		if (parent instanceof ReportItemExecutor)
		{
			ReportItemExecutor parentExecutor = (ReportItemExecutor)parent;
			if (parentExecutor.tocEntry != null)
			{
				return parentExecutor.tocEntry;
			}
			return parentExecutor.getParentTOCEntry();
		}
		return null;
	}

	/**
	 * starts a TOC entry, mostly used for non-leaf TOC entry, which can not be
	 * closed until its children have been written.
	 * 
	 * @param content
	 *            report item content object
	 */
	protected void startTOCEntry( IContent content )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			if ( content != null )
			{
				Object tocValue = content.getTOC( );
				if ( tocValue != null )
				{
					TOCEntry parentTOCEntry = getParentTOCEntry( );
					String hiddenFormats = content.getStyle( )
							.getVisibleFormat( );
					long elementId = getElementId( );
					String bookmark = content.getBookmark( );
					tocEntry = tocBuilder.startEntry( parentTOCEntry, tocValue,
							bookmark, hiddenFormats, elementId );
					String tocId = tocEntry.getNodeId( );
					if ( bookmark == null )
					{
						content.setBookmark( tocId );
					}
					return;
				}
				else
				{
					String hiddenFormats = content.getStyle( )
							.getVisibleFormat( );
					if ( hiddenFormats != null )
					{
						TOCEntry parentTOCEntry = getParentTOCEntry( );
						tocEntry = tocBuilder.startDummyEntry( parentTOCEntry,
								hiddenFormats );
					}
					else
					{
						Object design = content.getGenerateBy( );
						if ( design != null
								&& design instanceof ReportItemDesign )
						{
							Expression expr = ( (ReportItemDesign) design )
									.getTOC( );
							if ( expr != null )
							{
								String bookmark = content.getBookmark( );
								TOCEntry parentTOCEntry = getParentTOCEntry( );
								long elementId = getElementId( );
								tocEntry = tocBuilder.startEntry(
										parentTOCEntry, null, bookmark,
										hiddenFormats, elementId );
								if ( bookmark == null )
								{
									content.setBookmark( tocEntry.getNodeId( ) );
								}
							}
							
						}
					}
				}
			}
		}
	}

	/**
	 * finishes a TOC entry, mostly used for non-leaf TOC entry, which can not
	 * be closed until its children have been written.
	 * 
	 * @param content
	 *            report item content object
	 */
	protected void finishTOCEntry( )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			if (tocEntry != null)
			{
				tocBuilder.closeEntry( tocEntry );
			}
		}
	}

	protected void startGroupTOCEntry( IGroupContent group )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			TOCEntry entry = getParentTOCEntry();
			String hiddenFormats = group.getStyle( ).getVisibleFormat( );
			long elementId = getElementId( );
			tocEntry = tocBuilder.startGroupEntry( entry, group.getTOC( ),
					group.getBookmark( ), hiddenFormats, elementId );
			String tocId = tocEntry.getNodeId( );
			if ( group.getBookmark( ) == null )
			{
				group.setBookmark( tocId );
			}
		}
	}

	protected void finishGroupTOCEntry( )
	{
		TOCBuilder tocBuilder = context.getTOCBuilder( );
		if ( tocBuilder != null )
		{
			tocBuilder.closeGroupEntry( tocEntry );
		}
	}
	
	void setResultSet(IQueryResultSet rset)
	{
		this.rset = rset;
	}
	
	IQueryResultSet getResultSet()
	{
		return rset;
	}
	
	public IBaseResultSet[] getQueryResults( )
	{
		if ( rset != null )
		{
			return new IBaseResultSet[]{rset};
		}
		return null;
	}
	
	protected IBaseResultSet getParentResultSet( )
	{
		if ( parentRsets == null )
		{
			if ( parent != null )
			{
				if ( parent.rset == null )
				{
					IBaseResultSet[] pRsets = parent.getQueryResults( );
					if ( pRsets == null
							|| ( pRsets.length > 0 && pRsets[0] == null ) )
					{
						IBaseResultSet prset_ = parent.getParentResultSet( );
						if ( prset_ != null )
						{
							parentRsets = new IBaseResultSet[]{prset_};
						}
					}
					else
					{
						parentRsets = new IBaseResultSet[]{pRsets[0]};
					}
				}
				else
				{
					parentRsets = new IBaseResultSet[]{parent.rset};
				}
			}
		}

		if ( parentRsets != null )
		{
			return parentRsets[0];
		}
		else
		{
			return null;
		}
	}
	
	protected void restoreResultSet()
	{
		context.setResultSet( getParentResultSet( ) );
	}
	
	protected void handleOnCreate( IContent content )
	{
		if ( content.getGenerateBy( ) != null )
		{
			onCreateVisitor.onCreate( content );
		}
	}

	protected Logger getLogger( )
	{
		return context.getLogger( );
	}
}