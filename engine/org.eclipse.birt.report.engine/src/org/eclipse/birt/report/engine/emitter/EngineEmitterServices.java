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

package org.eclipse.birt.report.engine.emitter;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;

/**
 * Provides necessray information to emitters
 */
public class EngineEmitterServices implements IEmitterServices
{
	/**
	 * report executor
	 */
	public IReportExecutor executor;

	/**
	 * emitter configuration information
	 */
	protected HashMap emitterConfig = new HashMap( );

	/**
	 * The rendering options
	 */
	protected IRenderOption renderOption;

	/**
	 * the name of the report design. empty string if the input is stream
	 */
	protected String reportName = ""; //$NON-NLS-1$

	/**
	 * rendering context
	 */
	protected Object renderContext;

	/**
	 * The report runnable
	 */
	protected IReportRunnable reportRunnable;

	/**
	 * The task that results in this
	 */
	protected EngineTask task;
	
	/**
	 * the context used to execute the rport
	 */
	protected IReportContext context;

	/**
	 * @param task
	 *            he engine task that results in the creation of emitter
	 */
	public EngineEmitterServices( EngineTask task )
	{
		this.task = task;
	}

	/**
	 * @return Returns the emitterConfig.
	 */
	public HashMap getEmitterConfig( )
	{
		return emitterConfig;
	}

	/**
	 * @param emitterConfig
	 *            The emitterConfig to set.
	 */
	public void setEmitterConfig( HashMap emitterConfig )
	{
		this.emitterConfig = emitterConfig;
	}

	/**
	 * @return Returns the rendering options.
	 */
	public IRenderOption getRenderOption( )
	{
		return renderOption;
	}

	/**
	 * @param renderOption
	 *            The renderOption to set.
	 */
	public void setRenderOption( IRenderOption renderOption )
	{
		this.renderOption = renderOption;
	}

	/**
	 * @return Returns the reportName.
	 */
	public String getReportName( )
	{
		return reportRunnable.getReportName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getOption(java.lang.String)
	 */
	public Object getOption( String name )
	{
		return renderOption.getOutputSetting( ).get( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getRenderContext()
	 */
	public Object getRenderContext( )
	{
		return renderContext;
	}

	public void setRenderContext( Object renderContext )
	{
		this.renderContext = renderContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEmitterServices#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable( )
	{
		return reportRunnable;
	}

	/**
	 * @param reportRunnable
	 *            The reportRunnable to set.
	 */
	public void setReportRunnable( IReportRunnable reportRunnable )
	{
		this.reportRunnable = reportRunnable;
	}

	/**
	 * @return Returns the task.
	 */
	public IEngineTask getTask( )
	{
		return task;
	}

	public IReportExecutor getExecutor()
	{
		return executor;
	}
	
	public void setExecutor(IReportExecutor executor)
	{
		this.executor = executor;
	}
	
	public IReportContext getReportContext( )
	{
		return task.getReportContext( );
	}
}