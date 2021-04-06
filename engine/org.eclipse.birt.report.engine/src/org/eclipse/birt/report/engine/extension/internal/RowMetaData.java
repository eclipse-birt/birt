/*
 * Created on 2005-4-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.engine.extension.IRowMetaData;

/**
 *
 */
public class RowMetaData implements IRowMetaData {
	IResultMetaData metaData;

	RowMetaData(IResultMetaData metaData) {
		this.metaData = metaData;
	}

	/**
	 * Returns the number of columns in a detail row of the result set.
	 * 
	 * @return the number of columns in a detail row.
	 */
	public int getColumnCount() {
		return metaData.getColumnCount();
	}

	/**
	 * Returns the column name at the specified index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	public String getColumnName(int index) throws BirtException {
		return metaData.getColumnName(index);
	}

	public int getColumnType(int index) throws BirtException {
		return metaData.getColumnType(index);
	}

	/**
	 * Returns the column expression that results in the data at the specified
	 * index.
	 * 
	 * @param index The projected column index.
	 * @return The name of the specified column.
	 * @throws DataException if given index is invalid.
	 */
	public String getColumnExpression(int index) throws BirtException {
		return metaData.getColumnName(index);
	}

}
