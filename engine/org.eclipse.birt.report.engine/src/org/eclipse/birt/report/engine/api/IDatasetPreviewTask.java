package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.report.model.api.DataSetHandle;


public interface IDatasetPreviewTask extends IExtractionTask
{


	/** select which data set should be executed */
	public void setDataSet( DataSetHandle dataset );

	/** execute the query and return the result set */
	public IExtractionResults execute( ) throws EngineException;;

	public void setMaxRow( int maxRow );
	
	public void setStartRow( int startRow );
	
	/**
	 * select columns from the data set.
	 * @param columnNames the selected column names
	 */
	public void selectColumns( String[] columnNames );
	
	/**
	 * Set the query to be executed.
	 * When the query is set, the preview task will no longer construct
	 * queries and execute this one instead.
	 * 
	 * @param query the query to be executed
	 */
	public void setQuery( QueryDefinition query );
	
	public void setDataEngineFlowMode( DataEngineFlowMode dataEngineFlowMode );
	
}
