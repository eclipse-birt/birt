
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
package org.eclipse.birt.data.engine.executor.cache;

import org.eclipse.birt.data.engine.api.ClobAndBlobTest;

/**
 * 
 */

public class CacheClobAndBlobTest extends ClobAndBlobTest
{
	/*
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp( ) throws Exception
	{
		super.setUp( );
		System.setProperty( "birt.data.engine.test.memcachesize", "1" );
	}
	
}
