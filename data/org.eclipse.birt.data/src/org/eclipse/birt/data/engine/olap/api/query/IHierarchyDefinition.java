
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
 * IHierarchyDefinition defines Hierarchy and its levels that are used in cube.
 */

public interface IHierarchyDefinition extends INamedObject {
	/**
	 * Return the dimension this hierarchy belongs to.
	 *
	 * @return
	 */
	IDimensionDefinition getDimension();

	/**
	 * Add a level to the IHierarchyDefinition.
	 *
	 * @param levelName
	 * @return
	 */
	ILevelDefinition createLevel(String levelName);

	/**
	 * Return the levels that are added to the hierarchy.
	 *
	 * @return
	 */
	List<ILevelDefinition> getLevels();

	/**
	 * Clone itself.
	 */
	IHierarchyDefinition clone();
}
