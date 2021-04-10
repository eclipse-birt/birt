
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
 * IHierarchyDefinition defines Hierarchy and its levels that are used in cube.
 */

public interface IHierarchyDefinition extends INamedObject {
	/**
	 * Return the dimension this hierarchy belongs to.
	 * 
	 * @return
	 */
	public IDimensionDefinition getDimension();

	/**
	 * Add a level to the IHierarchyDefinition.
	 * 
	 * @param levelName
	 * @return
	 */
	public ILevelDefinition createLevel(String levelName);

	/**
	 * Return the levels that are added to the hierarchy.
	 * 
	 * @return
	 */
	public List<ILevelDefinition> getLevels();

	/**
	 * Clone itself.
	 */
	public IHierarchyDefinition clone();
}
