/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.olap.Dimension;

/**
 * 
 */
class NameSpaceFactoryImpl {

	static final String NO_NS_NAME = "none"; //$NON-NLS-1$
	static final String MASTER_PAGE_NS_NAME = "masterPage"; //$NON-NLS-1$
	static final String PARAMETER_NS_NAME = "parameter"; //$NON-NLS-1$
	static final String ELEMENT_NS_NAME = "element"; //$NON-NLS-1$
	static final String DATA_SOURCE_NS_NAME = "dataSource"; //$NON-NLS-1$
	static final String DATA_SET_NS_NAME = "dataSet"; //$NON-NLS-1$
	static final String STYLE_NS_NAME = "style"; //$NON-NLS-1$
	static final String THEME_NS_NAME = "theme"; //$NON-NLS-1$
	static final String TEMPLATE_PARAMETER_DEFINITION_NS_NAME = "templateParameterDefinition"; //$NON-NLS-1$
	static final String CUBE_NS_NAME = "cube"; //$NON-NLS-1$
	static final String DIMENSION_NS_NAME = "dimension"; //$NON-NLS-1$
	static final String VARIABLE_ELEMENT_NAME = "variableElement"; //$NON-NLS-1$

	// namespace in dimension

	private static final String DIMENSION_LEVEL_NAME_SPACE = "level"; //$NON-NLS-1$

	protected NameSpaceFactoryImpl() {
	}

	/**
	 * 
	 * @param holdName
	 * @param namespaceName
	 * @return
	 */
	public String getNameSpaceID(String holdName, String namespaceName) {
		if (ReportDesignConstants.DIMENSION_ELEMENT.equalsIgnoreCase(holdName)) {
			if (DIMENSION_LEVEL_NAME_SPACE.equalsIgnoreCase(namespaceName))
				return Dimension.LEVEL_NAME_SPACE;
		} else if (ReportDesignConstants.REPORT_DESIGN_ELEMENT.equalsIgnoreCase(holdName)
				|| ReportDesignConstants.LIBRARY_ELEMENT.equalsIgnoreCase(holdName)
				|| ReportDesignConstants.MODULE_ELEMENT.equalsIgnoreCase(holdName)) {
			if (namespaceName.equalsIgnoreCase(STYLE_NS_NAME))
				return Module.STYLE_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(THEME_NS_NAME))
				return Module.THEME_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(DATA_SET_NS_NAME))
				return Module.DATA_SET_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(DATA_SOURCE_NS_NAME))
				return Module.DATA_SOURCE_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(ELEMENT_NS_NAME))
				return Module.ELEMENT_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(PARAMETER_NS_NAME))
				return Module.PARAMETER_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(MASTER_PAGE_NS_NAME))
				return Module.PAGE_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(NO_NS_NAME))
				return MetaDataConstants.NO_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(VARIABLE_ELEMENT_NAME))
				return Module.VARIABLE_ELEMENT_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(CUBE_NS_NAME))
				return Module.CUBE_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(TEMPLATE_PARAMETER_DEFINITION_NS_NAME))
				return Module.TEMPLATE_PARAMETER_NAME_SPACE;
			else if (namespaceName.equalsIgnoreCase(DIMENSION_NS_NAME))
				return Module.DIMENSION_NAME_SPACE;
		}
		return MetaDataConstants.NO_NAME_SPACE;
	}

}
