package org.eclipse.birt.report.engine.api;

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
	
}
