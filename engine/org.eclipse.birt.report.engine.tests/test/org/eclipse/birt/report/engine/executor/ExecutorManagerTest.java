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

import junit.framework.TestCase;

/**
 * 
 * 
 * @version $Revision: 1.11 $ $Date: 2006/06/13 15:37:16 $
 */
public class ExecutorManagerTest extends TestCase
{

	public void testGetItemExecutor( )
	{
		ExecutorManager manager = new ExecutorManager(
				new ExecutionContext( 0 ), null );
		for ( int i = 0; i < ExecutorManager.NUMBER; i++ )
		{
			ReportItemExecutor executor = manager.getItemExecutor( i );
			assertTrue( executor != null );
			switch ( i )
			{
				case ExecutorManager.GRIDITEM :
					assertTrue( executor instanceof GridItemExecutor );
					break;
				case ExecutorManager.IMAGEITEM :
					assertTrue( executor instanceof ImageItemExecutor );
					break;
				case ExecutorManager.LABELITEM :
					assertTrue( executor instanceof LabelItemExecutor );
					break;
				case ExecutorManager.LISTITEM :
					assertTrue( executor instanceof ListItemExecutor );
					break;
				case ExecutorManager.TABLEITEM :
					assertTrue( executor instanceof TableItemExecutor );
					break;
				case ExecutorManager.MULTILINEITEM :
					assertTrue( executor instanceof MultiLineItemExecutor );
					break;
				case ExecutorManager.TEXTITEM :
					assertTrue( executor instanceof TextItemExecutor );
					break;
				case ExecutorManager.DATAITEM :
					assertTrue( executor instanceof DataItemExecutor );
					break;
				case ExecutorManager.EXTENDEDITEM :
					assertTrue( executor instanceof ExtendedItemExecutor );
					break;
				case ExecutorManager.TEMPLATEITEM :
					assertTrue( executor instanceof TemplateExecutor );
					break;
				case ExecutorManager.AUTOTEXTITEM :
					assertTrue( executor instanceof AutoTextItemExecutor );
					break;
				case ExecutorManager.LISTGROUPITEM:
					assertTrue( executor instanceof ListGroupExecutor );
					break;
				case ExecutorManager.TABLEGROUPITEM:
					assertTrue( executor instanceof TableGroupExecutor );
					break;
				case ExecutorManager.LISTBANDITEM:
					assertTrue( executor instanceof ListBandExecutor );
					break;
				case ExecutorManager.TABLEBANDITEM:
					assertTrue( executor instanceof TableBandExecutor );
					break;
				case ExecutorManager.ROWITEM:
					assertTrue( executor instanceof RowExecutor );
					break;
				case ExecutorManager.CELLITEM:
					assertTrue( executor instanceof CellExecutor );
					break;
				default :
					assertTrue( false );
			}
		}
	}

	public void testReleaseExecutor( )
	{
		ExecutorManager manager = new ExecutorManager(new ExecutionContext( 0 ) , null);
		for ( int i = 0; i < ExecutorManager.NUMBER; i++ )
		{
			ReportItemExecutor executor1 = manager.getItemExecutor( i );
			manager.releaseExecutor( i, executor1 );
			ReportItemExecutor executor2 = manager.getItemExecutor( i );
			manager.releaseExecutor( i, executor2 );
			assertTrue( executor1.equals( executor2 ) );
		}

		for ( int i = 0; i < ExecutorManager.NUMBER; i++ )
		{
			ReportItemExecutor executor1 = manager.getItemExecutor( i );
			ReportItemExecutor executor2 = manager.getItemExecutor( i );
			manager.releaseExecutor( i, executor2 );
			assertTrue( !executor1.equals( executor2 ) );
		}
	}

}
