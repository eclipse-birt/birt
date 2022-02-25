
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import org.eclipse.birt.data.engine.olap.data.impl.dimension.LevelDefinition;

/**
 *
 */

public class CubeElementFactory {
	/**
	 *
	 * @param name
	 * @param keyColumns
	 * @param attributeColumns
	 * @return
	 */
	public static ILevelDefn createLevelDefinition(String name, String[] keyColumns, String[] attributeColumns) {
		return new LevelDefinition(name, keyColumns, attributeColumns);
	}

}
