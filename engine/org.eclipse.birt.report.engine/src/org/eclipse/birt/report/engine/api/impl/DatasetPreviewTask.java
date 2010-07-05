package org.eclipse.birt.report.engine.api.impl;

import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBasePreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.engine.adapter.ModelDteApiAdapter;
import org.eclipse.birt.report.engine.api.DataExtractionOption;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDatasetPreviewTask;
import org.eclipse.birt.report.engine.api.IExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunnable;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Expression.Script;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;



public class DatasetPreviewTask extends EngineTask implements IDatasetPreviewTask
{
	
	protected IRunnable runnable;
	
	protected DataSetHandle dataset;

	protected int maxRow;
	
	protected DataRequestSession dteSession;
	
	protected DatasetPreviewTask( ReportEngine engine )
	{
		super(engine, TASK_DATASETPREVIEW);
	}

	public IExtractionResults execute( ) throws EngineException
	{
		if ( dataset == null )
		{
			throw new IllegalArgumentException(
					"dataset can not be null" );
		}
		return runDataset( );
	}
	
	public void setMaxRow(int maxRow)
	{
		this.maxRow = maxRow;
	}
	
	protected void checkRequiredParamenter(String paramName, String value) throws ParameterValidationException
	{
		
	}

	public void execute( IExtractionOption options ) throws EngineException
	{
		DataExtractionOption option = null;
		if ( options == null )
		{
			option = new DataExtractionOption( );
		}
		else
		{
			option = new DataExtractionOption( options.getOptions( ) );
		}

	}

	public void setDataSet( DataSetHandle dataset )
	{
		if ( dataset == null )
		{
			throw new IllegalArgumentException(
					"dataset can not be null!" );
		}
		this.dataset = dataset;
		ModuleHandle mh = dataset.getModuleHandle( );
		runnable = new ReportRunnable(engine, mh);
		setReportRunnable( (ReportRunnable)runnable );
	}

	public void setRunnable( IRunnable runnable )
	{
		this.runnable = runnable;
		setReportRunnable( (ReportRunnable)runnable );
	}
	
	protected ModuleHandle getHandle( )
	{
		return ( (ReportRunnable) runnable ).getModuleHandle( );
	}
	
	protected IExtractionResults runDataset( ) throws EngineException
	{
		IExtractionResults resultset = null;
		try
		{
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			if ( runnable == null )
			{
				throw new EngineException( "report runnable is not set" ); //$NON-NLS-1$
			}
			resultset = doRun( );
		}
		finally
		{
			changeStatusToStopped( );
			switchClassLoaderBack( );
		}
		
		return resultset;
	}
	
	/**
	 * runs the report
	 * 
	 * @throws EngineException
	 *             throws exception when there is a run error
	 */
	protected IExtractionResults doRun( ) throws EngineException
	{
		IExtractionResults result = null;
		doValidateParameters( );
		ReportDesignHandle design = executionContext.getReportDesign( );
		loadDesign( );
		prepareDesign( );
		startFactory( );
		try
		{

			executionContext.openDataEngine( );
			result = extractQuery( dataset );

			// executionContext.closeDataEngine( );
		}
		catch ( Exception ex )
		{
			log.log(
					Level.SEVERE,
					"An error happened while extracting data the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException( MessageConstants.REPORT_RUN_ERROR, ex );
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"There is insufficient memory to extract data from this report." ); //$NON-NLS-1$
			throw err;
		}
		catch ( Throwable t )
		{
			log.log( Level.SEVERE,
					"Error happened while running the report.", t ); //$NON-NLS-1$
			throw new EngineException( MessageConstants.REPORT_RUN_ERROR, t ); //$NON-NLS-1$
		}
		finally
		{
			closeFactory( );
		}
		return result;
	}
	
	protected IExtractionResults extractQuery( DataSetHandle dataset )
			throws BirtException
	{
		QueryDefinition newQuery = constructQuery( dataset );
		DataRequestSession session = getDataRequestSession( );
		ModelDteApiAdapter apiAdapter = new ModelDteApiAdapter(
				executionContext );
		apiAdapter.defineDataSet( dataset, dteSession );
		session.registerQueries( new IQueryDefinition[]{newQuery} );
		IBasePreparedQuery preparedQuery = session.prepare( newQuery );
		IQueryResults result = (IQueryResults) session.execute( preparedQuery,
				null, executionContext.getScriptContext( ) );
		ResultMetaData metadata = new ResultMetaData(
				result.getResultMetaData( ) );
		return new ExtractionResults( result, metadata, null, 0, maxRow );
	}

	protected ModuleHandle getModuleHandle( )
	{
		return dataset.getModuleHandle( );
	}
	
	
	protected DataRequestSession getDataRequestSession( ) throws BirtException
	{
		if ( dteSession == null )
		{
			DataSessionContext dteSessionContext = new DataSessionContext(
					DataSessionContext.MODE_DIRECT_PRESENTATION, getModuleHandle( ),
					executionContext.getScriptContext( ), executionContext
							.getApplicationClassLoader( ) );
			dteSessionContext.setAppContext( executionContext.getAppContext( ) );
			DataEngineContext dteEngineContext = dteSessionContext
					.getDataEngineContext( );
			dteEngineContext.setLocale( executionContext.getLocale( ) );
			dteEngineContext.setTimeZone( executionContext.getTimeZone( ) );
			dteEngineContext.setTmpdir( engine.getConfig( ).getTempDir( ) );

			dteSession = DataRequestSession.newSession( dteSessionContext );
		}
		return dteSession;
	}


	protected QueryDefinition constructQuery( DataSetHandle dataset )
			throws DataException
	{
		QueryDefinition query = new QueryDefinition( );
		query.setDataSetName( dataset.getQualifiedName( ) );
		query.setAutoBinding( true );
		// set max rows
		if(maxRow>0)
		{
			query.setMaxRows( maxRow );
		}
		return query;

	}
	
	protected void validateStringParameter( String paramName,
			Object paramValue, AbstractScalarParameterHandle paramHandle )
			throws ParameterValidationException
	{
		//do not check length of parameter value even when parameter value is required
	}
	
	protected void loadDesign( )
	{

		IReportRunnable runnable = executionContext.getRunnable( );
		if ( runnable != null )
		{
			ReportDesignHandle reportDesign = executionContext
					.getReportDesign( );
			if ( reportDesign != null )
			{
				// execute scripts defined in include-script element of the
				// libraries
				Iterator iter = reportDesign.includeLibraryScriptsIterator( );
				loadScript( iter );
				// execute scripts defined in include-script element of this
				// report
				iter = reportDesign.includeScriptsIterator( );
				loadScript( iter );

				// Intialize the report
				ReportScriptExecutor.handleInitialize( reportDesign,
						executionContext );
			}
			else
			{
				if ( dataset != null )
				{
					ModuleHandle moduleHandle = dataset.getModuleHandle( );
					Iterator iter = moduleHandle.includeScriptsIterator( );
					loadScript( iter );

					// Intialize the report
					ReportScriptExecutor.handleInitialize( moduleHandle,
							executionContext );

				}
			}
		}

	}


}
