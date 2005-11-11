package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IReportView;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.ReportEngine;


public class RenderTask extends EngineTask implements IRenderTask
{
	IRenderOption renderOptions;
	IReportView reportView;
	
	RenderTask(ReportEngine engine, IReportRunnable runnable)
	{
		super(engine, runnable);
	}

	public void setRenderOption( IRenderOption options )
	{
		this.renderOptions = options;
	}

	public void setReportView( IReportView view )
	{
		this.reportView = view;
	}

	public IRenderOption getRenderOption( )
	{
		return renderOptions;
	}

	public void render( ) throws EngineException
	{
		throw new UnsupportedOperationException("render");
	}

	public void render( int pageNumber ) throws EngineException
	{
		throw new UnsupportedOperationException("render");
	}

	public void render( String pageRange ) throws EngineException
	{
		throw new UnsupportedOperationException("render");
	}

	public void render( InstanceID iid ) throws EngineException
	{
		throw new UnsupportedOperationException("render");
	}

}
