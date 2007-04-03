/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview;

import org.eclipse.birt.report.designer.ui.preview.parameter.MockEngineTask;
import org.eclipse.birt.report.engine.api.IEngineTask;

import junit.framework.TestCase;

/**
 * Base Test Case
 * 
 */

public class BaseTestCase extends TestCase
{

	/**
	 * Mock engine Task instance.
	 */

	protected IEngineTask engineTask;

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		engineTask = new MockEngineTask( );
	}

	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
		engineTask = null;
	}

}
