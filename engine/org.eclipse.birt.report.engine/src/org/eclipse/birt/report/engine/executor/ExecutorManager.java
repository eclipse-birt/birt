/*
 * ExecutorManager.java Created on 2004-11-4
 *
 * Copyright (c) 2004 Actuate Corp.
 * 701 Gateway Blvd, South San Francisco, CA 94080, U.S.A.
 * All rights reserved.
 *
 * This software is confidential and proprietary information of 
 * Actuate Corp. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered
 * into with Actuate.
 */

package org.eclipse.birt.report.engine.executor;

import java.util.LinkedList;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.ir.IReportItemVisitor;

/**
 * 
 * report item executor manager
 * 
 * @author liugang
 * @version $Revision: 1.7 $ $Date: 2005/03/18 19:40:27 $
 */
public class ExecutorManager
{

	/**
	 * item executor type
	 */
	public static final int GRIDITEM = 0;
	public static final int IMAGEITEM = 1;
	public static final int LABELITEM = 2;
	public static final int LISTITEM = 3;
	public static final int TABLEITEM = 4;
	public static final int MULTILINEITEM = 5;
	public static final int TEXTITEM = 6;
	public static final int DATAITEM = 7;
	public static final int EXTENDEDITEM = 8;

	/**
	 * the number of suppported executor
	 */
	public static final int NUMBER = 9;

	protected static Logger log = Logger.getLogger( ExecutorManager.class
			.getName( ) );

	/**
	 * execution context
	 */
	protected ExecutionContext context;
	/**
	 * report executor visitor
	 */
	protected IReportItemVisitor visitor;

	/**
	 * array of free list
	 */
	protected LinkedList[] freeList = new LinkedList[NUMBER];

	/**
	 * constructor
	 * 
	 * @param context
	 * @param visitor
	 */
	public ExecutorManager( ExecutionContext context, IReportItemVisitor visitor )
	{
		this.context = context;
		this.visitor = visitor;
		for ( int i = 0; i < NUMBER; i++ )
		{
			freeList[i] = new LinkedList( );
		}
	}

	/**
	 * get item executor
	 * 
	 * @param type
	 *            the executor type
	 * @return item executor
	 */
	public ReportItemExecutor getItemExecutor( int type )
	{
		assert ( type >= 0 ) && ( type < NUMBER );
		if ( !freeList[type].isEmpty( ) )
		{
			// the free list is non-empty
			return (ReportItemExecutor) freeList[type].removeFirst( );
		}
		switch ( type )
		{
			case GRIDITEM :
				return new GridItemExecutor( context, visitor );
			case IMAGEITEM :
				return new ImageItemExecutor( context, visitor );
			case LABELITEM :
				return new LabelItemExecutor( context, visitor );
			case LISTITEM :
				return new ListItemExecutor( context, visitor );
			case TABLEITEM :
				return new TableItemExecutor( context, visitor );
			case MULTILINEITEM :
				return new MultiLineItemExecutor( context, visitor );
			case TEXTITEM :
				return new TextItemExecutor( context, visitor );
			case DATAITEM :
				return new DataItemExecutor( context, visitor );
			case EXTENDEDITEM :
				return new ExtendedItemExecutor( context, visitor );
			default :
				throw new UnsupportedOperationException(
						"unsupported executor!" ); //$NON-NLS-1$
		}
	}

	/**
	 * release item executor
	 * 
	 * @param type
	 *            the executor type
	 * @param itemExecutor
	 *            the item executor
	 */
	public void releaseExecutor( int type, ReportItemExecutor itemExecutor )
	{
		assert ( type >= 0 ) && ( type < NUMBER );
		itemExecutor.reset( );
		freeList[type].add( itemExecutor );
	}
}