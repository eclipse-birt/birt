
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
 * IEdgeDefinition defines dimensions and levels that are used in the edge.
 */
public interface IEdgeDefinition extends INamedObject
{
	/**
	 * Create the dimension used by the edge. The created dimension will be automatically linked to the edge.
	 * @param name
	 * @return
	 */
	public IDimensionDefinition createDimension( String name );
	
	/**
	 * Return all used dimensions.
	 * @return
	 */
	public List getDimensions( );
	
	/**
	 * Return a new IEdgeDrillingDownDefinition.
	 * @param name
	 * @return
	 */
	public IEdgeDrillingDownDefinition createDrillingDownDefinition( String name );
	
	/**
	 * Return all drilling down definition.
	 * @return
	 */
	public List getDrillingDownDefinition();
	
	/**
	 * Create a new IEdgeDrillingUpDefinition
	 * @param name
	 * @return
	 */
	public IEdgeDrillingUpDefinition createDrillingUpDefinition( String name );
	
	/**
	 * Return all drill up definition.
	 * @return
	 */
	public List getDrillingUpDefinition();
}
