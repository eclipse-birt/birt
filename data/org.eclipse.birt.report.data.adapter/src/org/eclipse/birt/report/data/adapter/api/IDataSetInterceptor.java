package org.eclipse.birt.report.data.adapter.api;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.report.model.api.ModuleHandle;

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
			ModuleHandle moduleHandle ) throws BirtException;
}
