
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
 * IDimensionDefinition defines a Dimension that is used in Edge.
 */

public interface IDimensionDefinition extends INamedObject {
	/**
	 * Create the hierarchy to be used by the IDimensionDefinition instance.
	 * Currently we only support only hierarchy each dimension.
	 *
	 * @param name
	 * @return
	 */
	IHierarchyDefinition createHierarchy(String name);

	/**
	 * Return the IHerarchyDefinition that are linked to this IDimensionDefinition.
	 * Currently only support one Hierarchy per Dimension.
	 *
	 * @return
	 */
	List<IHierarchyDefinition> getHierarchy();

	/**
	 * Clone itself.
	 */
	IDimensionDefinition clone();
}
