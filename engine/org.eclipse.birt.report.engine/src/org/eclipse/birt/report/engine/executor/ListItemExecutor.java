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

package org.eclipse.birt.report.engine.executor;

import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;

/**
 * Defines execution logic for a List report item.
 * 
 * @version $Revision: 1.14 $ $Date: 2005/05/08 06:08:26 $
 */
public class ListItemExecutor extends ListingElementExecutor
{

	/**
	 * the list deign
	 */
	protected ListItemDesign list;

	/**
	 * @param context
	 *            execution context
	 * @param visitor
	 *            visitor object for driving the execution
	 */
	protected ListItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#execute(org.eclipse.birt.report.engine.ir.ReportItemDesign,
	 *      org.eclipse.birt.report.engine.emitter.IReportEmitter)
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		super.execute( item, emitter );
		/*
		 * Getting item emitter returns null means that excuting this item is
		 * not necessary. for example, items in masterpage for html emitter will
		 * be discarded.
		 */
		if ( emitter.getEmitter( "text" ) == null ) //$NON-NLS-1$
		{
			return;
		}
		list = (ListItemDesign) item;
		logger.log( Level.FINE, "start list item" ); //$NON-NLS-1$
		ContainerContent listContent = (ContainerContent) ContentFactory
				.createContainerContent( item, context.getContentObject( ) );
		context.pushContentObject( listContent );
				
		try
		{
			//execute the on start script
			context.execute( list.getOnStart( ) );
			logger.log( Level.FINE, "start get list data" ); //$NON-NLS-1$
			rs = openResultSet( list );
			boolean isRowAvailable = false;
			if ( rs != null )
			{
				isRowAvailable = rs.next( );
			}
			logger.log( Level.FINE, "end get list data" ); //$NON-NLS-1$

			String bookmarkStr = evalBookmark( item );
			if ( bookmarkStr != null )
				listContent.setBookmarkValue( bookmarkStr );			

			setVisibility( item, listContent );
			setStyles( listContent, item );
			
			emitter.getContainerEmitter( ).start( listContent );

			//access list header
			accessHeader( );

			if ( isRowAvailable )
			{
				context.execute( list.getOnRow( ) );
				this.accessGroup( 0 );
			}
			accessFooter( );
			emitter.getContainerEmitter( ).end( );
			context.execute( list.getOnFinish( ) );
		}
		catch ( Throwable t )
		{
			logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
			context.addErrorMsg( "Fails to handle List " + item.getName( )
					+ ":" + t.getLocalizedMessage( ) );
		}
		finally
		{
			closeResultSet( rs );			
			context.popContentObject( );
			logger.log( Level.FINE, "end list item" ); //$NON-NLS-1$
		}
		
	}

	/**
	 * get the group header band
	 * 
	 * @param index
	 *            the group index
	 * @param list
	 *            the design element
	 * @return the list band
	 */
	private ListBandDesign getGroupHeader( int index, ListItemDesign list )
	{
		assert ( index >= 0 ) && ( index < list.getGroupCount( ) );
		return ( ( list.getGroup( index ) ) ).getHeader( );
	}

	/**
	 * access list band, such as list header, group header, detail etc
	 * 
	 * @param band
	 *            the list band
	 * @param emitter
	 *            the report emitter
	 * @param isDetail
	 *            true if it is detail band
	 */
	private void accessListBand( ListBandDesign band, IReportEmitter emitter,
			boolean isDetail )
	{
		if ( band != null && band.getContentCount( ) > 0 )
		{
//			IContainerEmitter containerEmitter = emitter.getContainerEmitter( );
//			if ( containerEmitter != null )
//			{
			//				containerEmitter.start( ContentFactory.createContainerContent( )
			// );
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				ReportItemDesign item = band.getContent( i );
				if ( item != null )
				{
					item.accept( this.visitor );
				}
			}
//				containerEmitter.end( );
//			}
		}

	}

	/**
	 * get group footer
	 * 
	 * @param index
	 *            the group index
	 * @return the list band
	 */
	private ListBandDesign getGroupFooter( int index, ListItemDesign list )
	{
		assert ( index >= 0 ) && ( index < list.getGroupCount( ) );
		return list.getGroup( index ).getFooter( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		super.reset( );
		list = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessHeader()
	 */
	protected void accessHeader( )
	{
		accessListBand( list.getHeader( ), emitter, false );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessFooter()
	 */
	protected void accessFooter( )
	{
		accessListBand( list.getFooter( ), emitter, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessDetailOneTime()
	 */
	protected void accessDetailOnce( )
	{
		ListBandDesign band = list.getDetail( );
		accessListBand( band, emitter, true );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupHeader(int)
	 */
	protected void accessGroupHeader( int index )
	{
		assert ( index <= list.getGroupCount( ) ) && ( index >= 0 );
		ListBandDesign band = getGroupHeader( index, list );
		accessListBand( band, emitter, false );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#accessGroupFooter(int)
	 */
	protected void accessGroupFooter( int index )
	{
		assert ( index <= list.getGroupCount( ) ) && ( index >= 0 );
		ListBandDesign band = getGroupFooter( index, list );
		accessListBand( band, emitter, false );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ListingExecutor#getGroupCount(org.eclipse.birt.report.engine.ir.ReportItemDesign)
	 */
	protected int getGroupCount( ReportItemDesign item )
	{
		if ( item != null )
		{
			return ( (ListItemDesign) item ).getGroupCount( );
		}
		return 0;
	}

}