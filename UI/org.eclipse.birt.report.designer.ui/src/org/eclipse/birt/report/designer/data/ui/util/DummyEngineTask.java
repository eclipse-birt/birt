
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;

/**
 * 
 */

public class DummyEngineTask extends RunAndRenderTask
{
	public DummyEngineTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );
	}
	
	public void run() throws EngineException
	{
		usingParameterValues( );
		loadDesign( );

	}
	
}
