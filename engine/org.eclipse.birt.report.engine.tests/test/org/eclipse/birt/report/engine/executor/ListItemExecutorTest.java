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
 * 
 * test ListItemExecutor
 * 
 * 
 * @version $Revision: 1.11 $ $Date: 2005/11/11 06:26:42 $
 */
public class ListItemExecutorTest extends ReportItemExecutorTestAbs
{

    /**
	 * test single table
	 * @throws Exception
	 */
	public void testExcuteList1( ) throws Exception
	{
		compare("List1.xml", "List1.txt");
	}
}
