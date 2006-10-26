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

/**
 * Test case for MultiLineItem
 * 
 * @version $Revision: 1.9 $ $Date: 2005/11/11 06:26:42 $
 */
public class MultiLineItemExecutorTest extends ReportItemExecutorTestAbs
{

	public void testExcute( ) throws Exception
	{
		compare( "multiline.xml", "multiline.txt" );
	}

}