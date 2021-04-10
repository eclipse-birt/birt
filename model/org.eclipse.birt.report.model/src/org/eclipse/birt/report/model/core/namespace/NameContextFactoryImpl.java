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

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.olap.Dimension;

/**
 * 
 */

public class NameContextFactoryImpl {

	public NameContextFactoryImpl() {
		super();
	}

	/**
	 * Returns the module name space with the attached module and name space ID.
	 * 
	 * @param module      the attached module
	 * @param nameSpaceID the name space ID. The different module name space for
	 *                    different name space ID.
	 * @return the produced element name space
	 */

	public static INameContext createModuleNameContext(Module module, String nameSpaceID) {
		if (nameSpaceID == Module.STYLE_NAME_SPACE)
			return new StyleNameContext(module);

		if (nameSpaceID == Module.CUBE_NAME_SPACE || nameSpaceID == Module.DIMENSION_NAME_SPACE)
			return new CubeNameContext(module, nameSpaceID);

		return new GeneralModuleNameContext(module, nameSpaceID);
	}

	/**
	 * Creates the dimension name context.
	 * 
	 * @param dimension   the dimension.
	 * @param nameSpaceID the name space id.
	 * @return the dimension name context.
	 */
	public static INameContext createDimensionNameContext(Dimension dimension, String nameSpaceID) {
		if (Dimension.LEVEL_NAME_SPACE.equals(nameSpaceID))
			return new DimensionNameContext(dimension);
		return null;
	}
}