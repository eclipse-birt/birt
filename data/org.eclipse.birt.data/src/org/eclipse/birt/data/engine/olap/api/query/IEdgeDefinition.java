
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
public interface IEdgeDefinition extends INamedObject {
	/**
	 * Create the dimension used by the edge. The created dimension will be
	 * automatically linked to the edge.
	 * 
	 * @param name
	 * @return
	 */
	public IDimensionDefinition createDimension(String name);

	/**
	 * Return all used dimensions.
	 * 
	 * @return
	 */
	public List<IDimensionDefinition> getDimensions();

	/**
	 * Return a new IEdgeDrillFilter.
	 * 
	 * @param name
	 * @return
	 */
	public IEdgeDrillFilter createDrillFilter(String name);

	/**
	 * Return all drilling down definition.
	 * 
	 * @return
	 */
	public List<IEdgeDrillFilter> getDrillFilter();

	/**
	 * Get the drill definition from the specified dimension.
	 * 
	 * @param dim
	 * @return
	 */
	public IEdgeDrillFilter[] getDrillFilter(IDimensionDefinition dim);

	/**
	 * Return the level where mirror gets start.
	 * 
	 * @deprecated
	 * @see getMirroredDefinition()
	 * @return
	 */
	public ILevelDefinition getMirrorStartingLevel();

	/**
	 * Set whether the level is mirrored.
	 * 
	 * @deprecated
	 * @param isMirrored
	 * @see creatMirrorDefinition()
	 * @return
	 */
	public void setMirrorStartingLevel(ILevelDefinition level);

	/**
	 * @param level          The mirror starting level
	 * @param breakHierarchy Whether to keep the relation ship between levels.
	 * @return
	 */
	public void creatMirrorDefinition(ILevelDefinition level, boolean breakHierarchy);

	/**
	 * 
	 * @return
	 */
	public IMirroredDefinition getMirroredDefinition();

	/**
	 * Clone itself.
	 * 
	 * @return
	 */
	public IEdgeDefinition clone();

}
