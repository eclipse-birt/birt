
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
