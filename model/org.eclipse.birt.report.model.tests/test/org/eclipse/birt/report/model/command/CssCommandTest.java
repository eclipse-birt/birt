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

package org.eclipse.birt.report.model.command;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.CssEvent;
import org.eclipse.birt.report.model.api.command.CssReloadedEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for class CssCommand.
 * <tr>
 * <td> test undo and redo can work when add or drop css
 * </tr>
 * 
 * <tr>
 * <td>test send message is ok 
 * </tr>
 */

public class CssCommandTest extends BaseTestCase
{

	/**
	 * Tests undo , redo operation.
	 * 
	 * @throws Exception
	 */

	public void testUndoAndRedo( ) throws Exception
	{
		openDesign( "BlankReportDesign.xml" ); //$NON-NLS-1$

		// test undo redo operation

		LabelHandle labelHandle = (LabelHandle)designHandle.findElement( "label" );//$NON-NLS-1$
		
		assertEquals( "left" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		designHandle.addCss( "reslove.css" );//$NON-NLS-1$
		
		assertEquals( "center" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		designHandle.getCommandStack( ).undo( );
		assertEquals( "left" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		designHandle.getCommandStack( ).redo();
		assertEquals( "center" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		Iterator iter = designHandle.includeCssesIterator( );
		iter.next();
		IncludedCssStyleSheetHandle handle = (IncludedCssStyleSheetHandle)iter.next();
		//drop reslove.css
		designHandle.dropCss( handle );
		assertEquals( "left" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		designHandle.getCommandStack( ).undo( );
		assertEquals( "center" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
		designHandle.getCommandStack( ).redo();
		assertEquals( "left" , labelHandle.getStyle( ).getTextAlign( ) );//$NON-NLS-1$
		
	}

	/**
	 * Test addCss method.
	 * 
	 * @throws Exception
	 */

	public void testAdd( ) throws Exception
	{

		openDesign( "BlankReportDesign.xml" ); //$NON-NLS-1$

		MyListener listener = new MyListener( );
		designHandle.addListener( listener );

		designHandle.addCss( "reslove.css" );//$NON-NLS-1$

		assertEquals( NotificationEvent.CSS_EVENT, listener.getEventType( ) );
		assertEquals( CssEvent.ADD, listener.getAction( ) );

		// designHandle.reloadCsses( );

		// assertEquals( NotificationEvent.CSS_RELOADED_EVENT, listener
		// .getEventType( ) );

		designHandle.dropCss( (IncludedCssStyleSheetHandle) designHandle
				.includeCssesIterator( ).next( ) );

		assertEquals( CssEvent.DROP, listener.getAction( ) );
	}

	class MyListener implements Listener
	{

		int action = CssEvent.ADD;
		int eventType = NotificationEvent.CSS_EVENT;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt.report.model.core.DesignElement,
		 *      org.eclipse.birt.report.model.activity.NotificationEvent)
		 */

		public void elementChanged( DesignElementHandle focus,
				NotificationEvent ev )
		{
			if ( ev.getEventType( ) == NotificationEvent.CSS_EVENT )
			{
				CssEvent event = (CssEvent) ev;
				action = event.getAction( );
				eventType = event.getEventType( );

			}
			else if ( ev.getEventType( ) == NotificationEvent.CSS_RELOADED_EVENT )
			{
				CssReloadedEvent event = (CssReloadedEvent) ev;
				eventType = event.getEventType( );
			}
		}

		/**
		 * Gets action code
		 * 
		 * @return action
		 * 
		 */

		public int getAction( )
		{
			return action;
		}

		/**
		 * Gets event type
		 * 
		 * @return event type
		 */

		public int getEventType( )
		{
			return eventType;
		}

	}
}
