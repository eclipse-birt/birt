
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.net.URL;

import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;

/**
 * Incremental cache data set design interface.
 */

public interface IIncreCacheDataSetDesign extends IOdaDataSetDesign {

	/**
	 * MODE_INCREMENTAL is one of the incremental cache modes, which cache the
	 * latest updated data from the remote database and merge to the local cache to
	 * speed up data accessing. The local cache will be persistent in the local
	 * disk. In future, we may introduce another cache mode: MODE_EXPIRE, in which
	 * the local disk cache will be expired in some period.
	 */
	public final static int MODE_PERSISTENT = 1;

	/**
	 * get current cache mode: currently it will just return
	 * DataEngineContext.CACHE_MODE_INCREMENTAL.
	 * 
	 * @return
	 */
	public int getCacheMode();

	/**
	 * get the specified timestamp column name.
	 * 
	 * @return
	 */
	public String getTimestampColumn();

	/**
	 * get the parsed query for updating to retrieve the delta data.
	 * 
	 * @param time
	 * @return
	 */
	public String getQueryForUpdate(long time);

	/**
	 * get the configure file URL.
	 * 
	 * @return
	 */
	public URL getConfigFileUrl();
}
