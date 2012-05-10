package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.report.model.api.DataSetHandle;


public interface IDatasetPreviewTask extends IEngineTask
{


	/** select which data set should be executed */
	public void setDataSet( DataSetHandle dataset );

	/** execute the query and return the result set */
	public IExtractionResults execute( ) throws EngineException;;

	public void setMaxRow( int maxRow );
	
	/**
	 * set the filter conditions
	 * 
	 * @param filters
	 */
	public void setFilters( IFilterDefinition[] filters );

	/**
	 * set the sorting conditions
	 * 
	 * @param sorts
	 */
	public void setSorts( ISortDefinition[] sorts );
	
}
