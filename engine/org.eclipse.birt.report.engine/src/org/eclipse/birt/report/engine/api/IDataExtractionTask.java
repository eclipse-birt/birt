package org.eclipse.birt.report.engine.api;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;

/**
 * an engine task that extracts data from a report. The task allows the return
 * of metadata and data from engine
 * 
 * User first creates the task from engine, then sets a report component ID, or
 * report component instance ID. If none is set, data extraction is assumed to
 * be based on all the data stored in the report. The user can call the
 * getMetaData method to retrieve metadata for each resultset. Based on the
 * metadata, he can select additional columns, add filter conditions, or specify
 * sorting conditions.
 */
public interface IDataExtractionTask extends IEngineTask
{
	// public static int SORT_DIRECTION_ASCENDING = 0;
	// public static int SORT_DIRECTION_DESCENDING = 1;	
	
	/**
	 * sets the report item identifier that data extraction will happen on
	 * 
	 * @param cid
	 *            report item identifier
	 */
	// public void setItemID( ComponentID cid );
	
	/**	 
	 * * @param iid
	 * 	 
	 *   identifies a report item instance that data extraction will
	 *            happen on
	 */
	public void setInstanceID( InstanceID iid );
	
	/**
	 * returns the metadata corresponding to the data stored in the report
	 * document, for the specific extraction level, i.e., report, daat set,
	 * report item, or report item instance levels. To get the metadata for the
	 * extracted data, use the getResultMetaData method from the IDataIterator
	 * interface.
	 * 
	 * @return a List of IResultMetaData. The list usually has one result set
	 *         meta data, but could have more if data extraction is based on the
	 *         whole report
	 * @deprecated
	 */
	public List getMetaData( ) throws EngineException;
	
	/**
	 * returns the metadata corresponding to the data stored in the report
	 * document, for the specific extraction level, i.e., report, daat set,
	 * report item, or report item instance levels. To get the metadata for the
	 * extracted data, use the getResultMetaData method from the IDataIterator
	 * interface.
	 * 
	 * @return a List of IResultSetItem.
	 */
	public List getResultSetList( ) throws EngineException;	
	
	/**
	 * select the result set from which to export data.
	 * @param resultSetName the result set name
	 */
	public void selectResultSet( String resultSetName );
	
	/**
	 * @param columnName
	 *            name of the column to be included in the data set
	 */
	public void selectColumns( String[] columnNames );
	
	/**
	 * @param simpleFilterExpression
	 *            add one filter condition to the extraction. Only simple filter
	 *            expressions are supported for now, i.e., LHS must be a column
	 *            name, only <, >, = and startWith is supported.
	 */
	public void setFilters( IFilterDefinition[] simpleFilterExpression );
	
	/**
	 * @param simpleSortExpression
	 *            add one sort condition to the extraction
	 */
	public void setSorts( ISortDefinition[] simpleSortExpression );

	/**
	 * @param maxRows
	 *            set the maximum rows that are returned from ResultSet
	 */
	public void setMaxRows( int maxRows );
	
	/**
	 * @param columnNames
	 *            names of the columns to sort on
	 * @param directions
	 *            the directions for sorting the data based on the specified
	 *            columns
	 */
	// public void setSortConditions(String[] columnNames, int[] directions);
	
	/**
	 * sets query string for data extraction. Not suppoted now.
	 * 
	 * @param queryString
	 *            a query string that acts as extraction criterion
	 */
	// public void setQuery(String queryString);
	
	
	/**
	 * @return an object of type IExtractionResults, from which data iterators
	 *         can be obtained and data can be retrieved
	 */
	public IExtractionResults extract() throws EngineException;

	public void extract( IDataExtractionOption option ) throws BirtException;
}