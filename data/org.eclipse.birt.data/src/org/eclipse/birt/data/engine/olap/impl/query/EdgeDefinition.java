
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingDownDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillingUpDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;

/**
 * 
 */

public class EdgeDefinition extends NamedObject implements IEdgeDefinition
{
	private List dims;
	private ILevelDefinition mirrorStartingLevel;
	private IMirroredDefinition mirror;
	
	public EdgeDefinition( String name )
	{
		super( name );
		this.dims = new ArrayList();
	}
	public IDimensionDefinition createDimension( String name )
	{
		IDimensionDefinition dim = new DimensionDefinition( name );
		this.dims.add( dim );
		return dim;
	}

	public IEdgeDrillingDownDefinition createDrillingDownDefinition( String name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public IEdgeDrillingUpDefinition createDrillingUpDefinition( String name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List getDimensions( )
	{
		return this.dims;
	}

	public List getDrillingDownDefinition( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public List getDrillingUpDefinition( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void creatMirrorDefinition( ILevelDefinition level,
			boolean breakHierarchy )
	{
		this.mirror = new MirroredDefinition( level, breakHierarchy );
	}
	
	public IMirroredDefinition getMirroredDefinition( )
	{
		return this.mirror;
	}
	
	/*
	 * (non-Javadoc)
	 * @deprecated
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#setMirrorStartingLevel(org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition)
	 */
	public void setMirrorStartingLevel( ILevelDefinition level )
	{
		this.mirror = new MirroredDefinition( level, true );
	}

	public ILevelDefinition getMirrorStartingLevel( )
	{
		return this.mirrorStartingLevel;
	}

}
