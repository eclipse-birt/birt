package org.eclipse.birt.report.engine.api.impl;

import java.util.List;
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
import org.eclipse.birt.report.engine.api.IRunnable;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;



public class DatasetPreviewTask extends EngineTask implements IDatasetPreviewTask
{
	
	protected IRunnable runnable;

	protected String datasetName;
	
	protected int maxRow;
	
	protected DataRequestSession dteSession;
	
	protected DatasetPreviewTask( ReportEngine engine )
	{
		super(engine, TASK_DATASETPREVIEW);
	}

	public IExtractionResults execute( ) throws EngineException
	{
		if ( datasetName == null || datasetName.length( ) == 0 )
		{
			throw new IllegalArgumentException(
					"datasetName can not be null or emtpy!" );
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

	public void setDataSet( String dataset )
	{
		if ( dataset == null || dataset.length( ) == 0 )
		{
			throw new IllegalArgumentException(
					"datasetName can not be null or emtpy!" );
		}
		this.datasetName = dataset;
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
			ModuleHandle handle = getHandle( );
			List ds = handle.getAllDataSets( );
			for ( Object obj : ds )
			{
				DataSetHandle dataset = (DataSetHandle) obj;
				if ( datasetName.equals( dataset.getQualifiedName( ) ) )
				{
					result = extractQuery( dataset );
				}
			}

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

	
	
	
	protected DataRequestSession getDataRequestSession( ) throws BirtException
	{
		if ( dteSession == null )
		{
			DataSessionContext dteSessionContext = new DataSessionContext(
					DataSessionContext.MODE_DIRECT_PRESENTATION, ((ReportRunnable)runnable).getModuleHandle( ),
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

}
