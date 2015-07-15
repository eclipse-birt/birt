
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

import java.util.List;

/**
 * IDimensionDefinition defines a Dimension that is used in Edge.
 */

public interface IDimensionDefinition extends INamedObject
{
	/**
	 * Create the hierarchy to be used by the IDimensionDefinition instance. Currently we only support only hierarchy each dimension.
	 * @param name
	 * @return
	 */
	public IHierarchyDefinition createHierarchy( String name );
	
	/**
	 * Return the IHerarchyDefinition that are linked to this IDimensionDefinition. Currently only support one Hierarchy per Dimension.
	 * @return
	 */
	public List<IHierarchyDefinition> getHierarchy();

    /**
     * Clone itself.
     */
    public IDimensionDefinition clone( );
}
