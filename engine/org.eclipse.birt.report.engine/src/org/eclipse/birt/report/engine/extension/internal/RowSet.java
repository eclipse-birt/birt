/*
 * Created on 2005-4-13
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.data.dte.DteResultSet;
import org.eclipse.birt.report.engine.extension.IRowMetaData;
import org.eclipse.birt.report.engine.extension.IRowSet;


/**
 *
 * @version $Revision:$ $Date:$
 */
public class RowSet implements IRowSet
{
	protected DteResultSet rset;
	protected IRowMetaData metaData;
	
	public RowSet(DteResultSet rset)
	{
		this.rset = rset;
		try
		{
			metaData = new RowMetaData(rset.getQr().getResultMetaData());
		}
		catch(BirtException ex)
		{
			
		}
		
	}
	
	
	/**
	 * returns the definition for the data row
	 * 
	 * @return the definition for the data row
	 */
	public IRowMetaData getMetaData()
	{
		return metaData;
	}
	
	public boolean next()
	{
		return rset.next();
	}
	
	public Object evaluate(IBaseExpression expr)
	{
		return rset.evaluate(expr);
	}
	
	public Object getValue(int columnIndex)
	{
		throw new UnsupportedOperationException();
	}
	
	public Object getValue(String columnName)
	{
		throw new UnsupportedOperationException();
	}
	
	public void close()
	{
		rset.close();
	}
	
}
