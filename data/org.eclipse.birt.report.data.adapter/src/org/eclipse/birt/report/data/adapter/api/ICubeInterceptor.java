package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;

/**
 * Interceptor for one type of cube
 *
 */
public interface ICubeInterceptor {

	void preDefineCube(DataSessionContext appContext, CubeHandle handle) throws BirtException;

	boolean needDefineCube() throws BirtException;
}
