
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * The new IBaseQueryResults is an interface which will be extends by
 * IQueryResults and ICubeQueryResults interfaces. It provides service for
 * client to get/set query results id.
 */

public interface IBaseQueryResults extends INamedObject {
	/**
	 * Every query results has a unique id. This ID will be used to retrieve a
	 * stored query results from report document. Meantime, it might be used as a
	 * data source ID to define a query definition.
	 *
	 * @return a unique ID
	 */
	String getID();

	/**
	 * Closes all query result set(s) associated with this object; provides a hint
	 * to the query that it can safely release all associated resources. The query
	 * results might have iterators open on them. Iterators associated with the
	 * query result sets are invalidated and can no longer be used.
	 *
	 * @throws BirtException
	 */
	void close() throws BirtException;

}
