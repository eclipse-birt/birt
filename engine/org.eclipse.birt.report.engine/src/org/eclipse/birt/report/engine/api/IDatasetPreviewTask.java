package org.eclipse.birt.report.engine.api;


public interface IDatasetPreviewTask extends IEngineTask
{

	/** set the runnable which contains the dataset design */
	public void setRunnable( IRunnable runnable );

	/** select which data set should be executed */
	public void setDataSet( String dataset );

	/** execute the query and return the result set */
	public IExtractionResults execute( ) throws EngineException;;

	public void setMaxRow( int maxRow );
	
}
