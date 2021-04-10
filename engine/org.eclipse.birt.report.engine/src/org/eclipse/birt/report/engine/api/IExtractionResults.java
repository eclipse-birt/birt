package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * A handle used to retrieve data stored in a report. Extraction results could
 * contain multiple resultsets, especially when the extraction is at report
 * level, i.e., get all the data stored for a report
 */
public interface IExtractionResults {
	/**
	 * Returns the metadata of the first or current result set <br>
	 * This method provides the result metadata without having to first fetch the
	 * result data.
	 * <p>
	 * Returns Null if the metadata is not available before fetching from an
	 * <code>IResultIterator</code>, or if it is ambiguous on which result set to
	 * reference. In such case, one should obtain the result metadata from a
	 * specific <code>IResultIterator<code>.
	 * 
	 * @return The metadata of the first result set's detail row in this
	 *         <code>IQueryResults<code>. Null if not available or ambiguous on
	 *         which result set to reference.
	 * @throws EngineException if error occurs during extraction
	 */
	public IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Returns the current result's iterator. Repeated call of this method without
	 * having advanced to the next result would return the same iterator at its
	 * current state.
	 * 
	 * @return The current result's iterator.
	 * @throws EngineException if error occurs during extraction
	 */
	public IDataIterator nextResultIterator() throws BirtException;

	/**
	 * Closes all query result set(s) associated with this object; provides a hint
	 * to the query that it can safely release all associated resources. The query
	 * results might have iterators open on them. Iterators associated with the
	 * query result sets are invalidated and can no longer be used.
	 */
	public void close();
}
