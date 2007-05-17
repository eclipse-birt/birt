
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
package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.mozilla.javascript.Scriptable;


/**
 * 
 */

public class PreparedCubeQuery implements IPreparedCubeQuery
{
	private ICubeQueryDefinition cubeQueryDefn;
	private DataEngineSession session;
	private DataEngineContext context;
	/**
	 * 
	 * @param defn
	 * @param scope
	 */
	public PreparedCubeQuery( ICubeQueryDefinition defn, DataEngineSession session, DataEngineContext context )
	{
		this.cubeQueryDefn = defn;
		this.session = session;
		this.context = context;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#execute(org.mozilla.javascript.Scriptable)
	 */
	public ICubeQueryResults execute( Scriptable scope )
	{
		return new CubeQueryResults( this, this.session, scope == null? this.session.getSharedScope( ):scope, this.context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery#getCubeQueryDefinition()
	 */
	public ICubeQueryDefinition getCubeQueryDefinition( )
	{
		return this.cubeQueryDefn;
	}

}
