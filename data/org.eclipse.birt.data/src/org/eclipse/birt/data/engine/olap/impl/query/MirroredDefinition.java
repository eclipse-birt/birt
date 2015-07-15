/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition;

/**
 * Implementation of the IMirroredDefinition
 *
 */
public class MirroredDefinition implements IMirroredDefinition
{
	
	private ILevelDefinition level;
	private boolean breakHierarchy;

	public MirroredDefinition( ILevelDefinition level, boolean breakHierarchy )
	{
		this.level = level;
		this.breakHierarchy = breakHierarchy;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition#getMirrorStartingLevel()
	 */
	public ILevelDefinition getMirrorStartingLevel( )
	{
		return this.level;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.olap.api.query.IMirroredDefinition#isBreakHierarchy()
	 */
	public boolean isBreakHierarchy( )
	{
		return this.breakHierarchy;
	}

    /**
     * Clone itself.
     */
    public IMirroredDefinition clone( )
    {
        MirroredDefinition cloned = new MirroredDefinition(
                this.level.clone( ), this.breakHierarchy );

        return cloned;
    }

}
