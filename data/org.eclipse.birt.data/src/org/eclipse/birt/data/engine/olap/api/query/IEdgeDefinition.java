
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	IDimensionDefinition createDimension(String name);

	/**
	 * Return all used dimensions.
	 *
	 * @return
	 */
	List<IDimensionDefinition> getDimensions();

	/**
	 * Return a new IEdgeDrillFilter.
	 *
	 * @param name
	 * @return
	 */
	IEdgeDrillFilter createDrillFilter(String name);

	/**
	 * Return all drilling down definition.
	 *
	 * @return
	 */
	List<IEdgeDrillFilter> getDrillFilter();

	/**
	 * Get the drill definition from the specified dimension.
	 *
	 * @param dim
	 * @return
	 */
	IEdgeDrillFilter[] getDrillFilter(IDimensionDefinition dim);

	/**
	 * Return the level where mirror gets start.
	 *
	 * @deprecated
	 * @see getMirroredDefinition()
	 * @return
	 */
	@Deprecated
	ILevelDefinition getMirrorStartingLevel();

	/**
	 * Set whether the level is mirrored.
	 *
	 * @deprecated
	 * @param isMirrored
	 * @see creatMirrorDefinition()
	 * @return
	 */
	@Deprecated
	void setMirrorStartingLevel(ILevelDefinition level);

	/**
	 * @param level          The mirror starting level
	 * @param breakHierarchy Whether to keep the relation ship between levels.
	 * @return
	 */
	void creatMirrorDefinition(ILevelDefinition level, boolean breakHierarchy);

	/**
	 *
	 * @return
	 */
	IMirroredDefinition getMirroredDefinition();

	/**
	 * Clone itself.
	 *
	 * @return
	 */
	IEdgeDefinition clone();

}
