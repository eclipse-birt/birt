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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.ActionType;

public class ActionTypeTest extends TestCase {
	
	public void testConstant() 
	{		
		assertEquals( ActionType.URL_REDIRECT, 0 );
		assertEquals( ActionType.SHOW_TOOLTIP, 1 );		
		assertEquals( ActionType.TOGGLE_VISIBILITY, 2 );	
		assertEquals( ActionType.INVOKE_SCRIPT, 3 );	
	}
	
	public void testGet() 
	{
		assertEquals( ActionType.URL_REDIRECT_LITERAL, ActionType.get(ActionType.URL_REDIRECT) );
		assertEquals( ActionType.SHOW_TOOLTIP_LITERAL, ActionType.get(ActionType.SHOW_TOOLTIP) );
		assertEquals( ActionType.TOGGLE_VISIBILITY_LITERAL, ActionType.get(2) );
		assertEquals( ActionType.INVOKE_SCRIPT_LITERAL, ActionType.get(3) );
		
		assertEquals( ActionType.URL_REDIRECT_LITERAL, ActionType.get("URL_Redirect") );
		assertEquals( ActionType.SHOW_TOOLTIP_LITERAL, ActionType.get("Show_Tooltip") );
		assertEquals( ActionType.TOGGLE_VISIBILITY_LITERAL, ActionType.get("Toggle_Visibility") );
		assertEquals( ActionType.INVOKE_SCRIPT_LITERAL, ActionType.get("Invoke_Script") );
		
		assertNull(ActionType.get("No Match") );
		assertNull(ActionType.get(5) );
	}
}
