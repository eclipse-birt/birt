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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * report item executor manager
 * 
 * @author liugang
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
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

	protected static Log log = LogFactory.getLog( ExecutorManager.class );

    /**
     * execution context
     */
	protected ExecutionContext context;
    /**
     * report executor visitor
     */
	protected ReportExecutorVisitor visitor;

    /**
     * array of free list 
     */
	protected LinkedList[] freeList = new LinkedList[NUMBER];
    /**
     * array of busy list
     */
	protected LinkedList[] busyList = new LinkedList[NUMBER];

    /**
     * constructor
     * @param context
     * @param visitor
     */
	public ExecutorManager( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		this.context = context;
		this.visitor = visitor;
		for ( int i = 0; i < NUMBER; i++ )
		{
			freeList[i] = new LinkedList( );
			busyList[i] = new LinkedList( );
		}
	}

    /**
     * get item executor
     * @param type the executor type
     * @return item executor
     */
	public ReportItemExecutor getItemExecutor( int type )
	{
		assert ( type >= 0 ) && ( type < NUMBER );
		ReportItemExecutor ret;
		if ( freeList[type].isEmpty() )
		{
			switch ( type )
            {
                case GRIDITEM :
                    ret = new GridItemExecutor( context, visitor );
                    break;
                case IMAGEITEM :
                    ret = new ImageItemExecutor( context, visitor );
                    break;
                case LABELITEM :
                    ret = new LabelItemExecutor( context, visitor );
                    break;
                case LISTITEM :
                    ret = new ListItemExecutor( context, visitor );
                    break;
                case TABLEITEM :
                    ret = new TableItemExecutor( context, visitor );
                    break;
                case MULTILINEITEM :
                    ret = new MultiLineItemExecutor( context, visitor );
                    break;
                case TEXTITEM :
                    ret = new TextItemExecutor( context, visitor );
                    break;
                case DATAITEM :
                    ret = new DataItemExecutor( context, visitor );
                    break;
                case EXTENDEDITEM:
                	ret = new ExtendedItemExecutor(context, visitor);
                	break;
                default :
                    throw new UnsupportedOperationException("unsupported executor!");
            }
            busyList[type].add( ret );
            return ret;
		}
		else
		{          
            ret = (ReportItemExecutor) freeList[type].getFirst();
            freeList[type].remove( ret );
            busyList[type].add( ret );
            return ret;
		}
	}

    /**
     * release item executor
     * @param type the executor type
     * @param itemExecutor the item executor
     */
	public void releaseExecutor( int type, ReportItemExecutor itemExecutor )
	{
		assert ( type >= 0 ) && ( type < NUMBER );
		assert ( busyList[type].contains( itemExecutor ) );
		busyList[type].remove( itemExecutor );
		itemExecutor.reset( );
		freeList[type].add( itemExecutor );
	}
}