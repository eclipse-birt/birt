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
 * test grid item executor test for single record and empty resultset
 * 
 * @version $Revision: 1.12 $ $Date: 2005/11/11 06:26:42 $
 */
public class GridItemExecutorTest extends ReportItemExecutorTestAbs
{
	/**
	 * test single table
	 * @throws Exception
	 */
	public void testExcutegrid1( ) throws Exception
	{
		compare("grid.xml", "grid.txt");
	}
	
}
