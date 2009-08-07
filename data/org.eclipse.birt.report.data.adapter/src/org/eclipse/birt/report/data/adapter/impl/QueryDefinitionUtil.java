
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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.SubqueryDefinitionCopyUtil;
import org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil;


/**
 * 
 */

public class QueryDefinitionUtil implements IQueryDefinitionUtil
{
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.IQueryDefinitionUtil#createSubqueryDefinition(java.lang.String, org.eclipse.birt.data.engine.api.ISubqueryDefinition)
	 */
	public SubqueryDefinition createSubqueryDefinition( String name,
			ISubqueryDefinition srcSubQueryDefn ) throws DataException
	{
		return SubqueryDefinitionCopyUtil.createSubqueryDefinition( name, srcSubQueryDefn );
	}

	public Map<String, IBinding> getAccessibleBindings( IBaseQueryDefinition qd )
			throws DataException
	{
		return org.eclipse.birt.data.engine.impl.QueryDefinitionUtil.getAccessibleBindings( qd );
	}

}
