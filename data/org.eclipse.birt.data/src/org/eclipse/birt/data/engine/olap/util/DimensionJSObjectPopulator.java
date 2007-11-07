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

package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DimensionJSObjectPopulator implements IJSObjectPopulator
{

	private DummyJSLevels dimObj;
	private Scriptable scope;
	private String dimensionName;
	private List levelNames;

	public DimensionJSObjectPopulator( Scriptable scope, String dimensionName,
			List levelNames )
	{
		this.scope = scope;
		this.dimensionName = dimensionName;
		this.levelNames = levelNames;
	}

	public void doInit( ) throws DataException
	{
		this.dimObj = new DummyJSLevels( dimensionName );
		DummyJSDimensionObject dimObj = new DummyJSDimensionObject( this.dimObj,
				levelNames );

		scope.put( "dimension",//$NON-NLS-1$
				scope,
				new DummyJSDimensionAccessor( dimensionName, dimObj ) );

	}

	public void setResultRow( IResultRow resultRow )
	{
		dimObj.setResultRow( resultRow );
	}

	public void cleanUp( )
	{
		this.scope.delete( "dimension" );//$NON-NLS-1$
		this.scope.setParentScope( null );
	}

}
