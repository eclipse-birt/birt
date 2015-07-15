
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
package org.eclipse.birt.data.engine.olap.api.query;



/**
 * 
 */

public class LevelDefiniton extends NamedObject implements ILevelDefinition
{
	private IHierarchyDefinition hierarchy;
	
	public LevelDefiniton( IHierarchyDefinition hier, String name )
	{
		super(name);
		
		assert hier!= null;
		this.hierarchy = hier;
	}
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition#getHierarchy()
	 */
	public IHierarchyDefinition getHierarchy( )
	{
		return this.hierarchy;
	}

    /**
     * Clone itself.
     */
    public ILevelDefinition clone( )
    {
        LevelDefiniton cloned = new LevelDefiniton( this.hierarchy.clone( ),
                this.getName( ) );

        return cloned;
    }
}
