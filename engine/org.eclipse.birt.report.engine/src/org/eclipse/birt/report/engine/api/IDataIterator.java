package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;

/**
 * An iterator on a result set from a prepared and executed query. Multiple
 * IResultIterator objects could be associated with the same IExtractionResults
 * object, if extraction is done at report level
 */
public interface IDataIterator {
	/**
	 * Returns {@link org.eclipse.birt.report.engine.api.IExtractionResults} from
	 * which this data iterator is obtained.
	 */
	public IExtractionResults getQueryResults();

	/**
	 * Returns the metadata of this result set's detail row.
	 * 
	 * @return The result metadata of a detail row.
	 */
	public IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Moves down one element from its current position of the iterator.
	 * 
	 * @return true if next element exists and has not reached the limit on the
	 *         maximum number of rows that can be accessed.
	 * @throws BirtException if error occurs
	 */
	public boolean next() throws BirtException;

	/**
	 * Returns the value of a column.
	 * 
	 * @param columnName the name of the column
	 * @return The value of the given column. It could be null.
	 * @throws BirtException if error occurs
	 */
	public Object getValue(String columnName) throws BirtException;

	/**
	 * @param index column index. It is 1 based
	 * @throws BirtException if error occurs
	 */
	public Object getValue(int index) throws BirtException;

	/**
	 * Closes this result and provide a hint that the consumer is done with this
	 * result, whose resources can be safely released as appropriate.
	 */
	public void close();

	/**
	 * see whether this iterator is empty
	 * 
	 * @return true if this iterator is false; otherwise, return false
	 * @throws BirtException
	 */
	public boolean isEmpty() throws BirtException;

	/**
	 * return the IResultIterator directly
	 * 
	 * @return the IResultIterator
	 */
	public IResultIterator getResultIterator();
}
