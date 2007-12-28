
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IQueryDefinitionUtil
{
	/**
	 * 
	 * @param name
	 * @param srcSubQueryDefn
	 * @return
	 * @throws DataException
	 */
	public SubqueryDefinition createSubqueryDefinition( String name, ISubqueryDefinition srcSubQueryDefn ) throws DataException;
}
