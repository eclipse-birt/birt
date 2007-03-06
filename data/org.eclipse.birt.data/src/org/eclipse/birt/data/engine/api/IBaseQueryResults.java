
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.api;

/**
 * The new IBaseQueryResults is an interface which will be extends by IQueryResults and ICubeQueryResults interfaces. It provides service for
 * client to get/set query results id.
 */

public interface IBaseQueryResults
{
	/**
	 * Every query results has a unique id. This ID will be used to retrieve a
	 * stored query results from report document. Meantime, it might be used as
	 * a data source ID to define a query definition.
	 * 
	 * @return a unique ID
	 */
	public String getID();
}
