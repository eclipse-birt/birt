/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension.engine;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;

abstract public class ReportEngineExtensionAdapter
		implements
			IReportEngineExtension
{

	protected IReportEngine engine;

	public ReportEngineExtensionAdapter( IReportEngine engine )
	{
		this.engine = engine;
	}

	public IReportEngine getReportEngine( )
	{
		return engine;
	}

	public void close( )
	{
	}

	public IDataExtension createDataExtension( IRunContext context )
	{
		return null;
	}

	public IDocumentExtension createDocumentExtension( IRunContext context )
			throws EngineException
	{
		return null;
	}

	public IGenerateExtension createGenerateExtension( IRunContext context )
			throws EngineException
	{
		return null;
	}

	public IRenderExtension createRenderExtension( IRenderContext context )
			throws EngineException
	{
		return null;
	}
}
