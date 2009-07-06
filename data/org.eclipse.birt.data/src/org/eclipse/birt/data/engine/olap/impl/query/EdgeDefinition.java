
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
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;

/**
 * 
 */

public class EdgeDefinition extends NamedObject implements IEdgeDefinition
{
	private List<IDimensionDefinition> dimensions;
	private List<IEdgeDrillFilter> drillOperation;
	
	private ILevelDefinition mirrorStartingLevel;
	private IMirroredDefinition mirror;

	public EdgeDefinition( String name )
	{
		super( name );
		this.dimensions = new ArrayList<IDimensionDefinition>( );
		this.drillOperation = new ArrayList<IEdgeDrillFilter>( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#createDimension(java.lang.String)
	 */
	public IDimensionDefinition createDimension( String name )
	{
		IDimensionDefinition dim = new DimensionDefinition( name );
		this.dimensions.add( dim );
		return dim;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#getDimensions()
	 */
	public List<IDimensionDefinition> getDimensions( )
	{
		return this.dimensions;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#creatMirrorDefinition(org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition, boolean)
	 */
	public void creatMirrorDefinition( ILevelDefinition level,
			boolean breakHierarchy )
	{
		this.mirror = new MirroredDefinition( level, breakHierarchy );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#getMirroredDefinition()
	 */
	public IMirroredDefinition getMirroredDefinition( )
	{
		return this.mirror;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#setMirrorStartingLevel(org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition)
	 */
	public void setMirrorStartingLevel( ILevelDefinition level )
	{
		this.mirror = new MirroredDefinition( level, true );
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#getMirrorStartingLevel()
	 */
	public ILevelDefinition getMirrorStartingLevel( )
	{
		return this.mirrorStartingLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#createDrillingFilterDefinition(java.lang.String, int)
	 */
	public IEdgeDrillFilter createDrillFilter(
			String name, int drillType )
	{
		IEdgeDrillFilter drill = new EdgeDrillingFilterDefinition( name,
				drillType );
		drillOperation.add( drill );
		return drill;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition#getDrillingFilterDefinition()
	 */
	public List<IEdgeDrillFilter> getDrillFilter( )
	{
		return this.drillOperation;
	}
}
