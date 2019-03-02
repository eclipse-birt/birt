/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * CSS file in outline is not refreshed and can not be edited after undo.
 * <p>
 * Test description:
 * <p>
 * Check undo renameCss will return detail events.
 * </p>
 */
public class Regression_241931 extends BaseTestCase
{

	private final static String REPORT = "regression_241931.xml";
	private final static String CSS1 = "regression_241931_1.css";
	private final static String CSS2 = "regression_241931_2.css";
	
	protected void setUp( ) throws Exception
	{
		super.setUp( );
		removeResource( );
		
		copyInputToFile ( INPUT_FOLDER + "/" + REPORT );
		copyInputToFile ( INPUT_FOLDER + "/" + CSS1 );
		copyInputToFile(INPUT_FOLDER+ "/" + CSS2);
	}
	/**
	 * @throws Exception 
	 * 
	 */
	public void test_regression_241931( ) throws Exception
	{
		openDesign( REPORT );
		ActivityStack actStack=(ActivityStack)designHandle.getCommandStack();
		IncludedCssStyleSheetHandle cssHandle=designHandle.findIncludedCssStyleSheetHandleByFileName(CSS1);

		CssListener listener1=new CssListener();
		designHandle.addListener(listener1);
		
		designHandle.renameCss(cssHandle, CSS2);
		List notification1=listener1.getNotifications();
		int count=notification1.size();
		listener1.restart();

		actStack.undo();
//		notification1=listener1.getNotifications();
		assertEquals(count,notification1.size());
	}
	
	/**
	 * Listener for element change notification 
	 *
	 */
	private static class CssListener implements Listener{
		List notifications = new ArrayList( );

		public static class Notification
		{

			DesignElementHandle target = null;
			NotificationEvent event = null;

			Notification( DesignElementHandle element, NotificationEvent event )
			{
				this.target = element;
				this.event = event;
			}
			
			public NotificationEvent getEvent(){
				return this.event;
			}
		}
	
		public void elementChanged(DesignElementHandle focus,
				NotificationEvent ev ){
			this.notifications.add(new Notification(focus,ev));
		}

		public void restart( )
		{
			this.notifications.clear( );
		}
		
		public List getNotifications(){
			return this.notifications;
		}
	}
}
