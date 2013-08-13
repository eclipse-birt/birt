package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;

/**
 * Interceptor for one type of data set.
 * 
 *
 */
public interface IDataSetInterceptor
{
	/**
	 * 
	 * @param appContext
	 * @param dsource
	 * @param dset
	 * @param moduleHandle
	 * @throws BirtException
	 */
	void preDefineDataSet( DataSessionContext appContext,
			IBaseDataSourceDesign dsource,
			IBaseDataSetDesign dset,
			IQueryDefinition query,
			IDataQueryDefinition[] registedQueries ) throws BirtException;
	
	/**
	 * release resources
	 * @throws BirtException
	 */
	void close( ) throws BirtException;
}
