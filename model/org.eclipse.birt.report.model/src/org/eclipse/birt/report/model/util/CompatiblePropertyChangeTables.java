/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;

/**
 *
 */

public class CompatiblePropertyChangeTables {

	/**
	 * The constant indicates the invalid property type.
	 */

	private static List<CompatibleProperty> propToExprTable = null;

	static {
		propToExprTable = new ArrayList<CompatibleProperty>();

		propToExprTable.add(new CompatibleProperty(ReportDesignConstants.SCALAR_PARAMETER_ELEMENT,
				IAbstractScalarParameterModel.DEFAULT_VALUE_PROP, ExpressionType.CONSTANT, VersionUtil.VERSION_3_2_19));
	}

	/**
	 * Returns the compatibility default expression type for the given property. The
	 * algorithm checks the element, the property name and the version number. If
	 * the given version < predefined version, compatibility is required.
	 * 
	 * @param elementName the element definition name
	 * @param propName    the property name
	 * @param versionNum  the current design file version
	 * @return the default expression type
	 */

	public static String getDefaultExprType(String elementName, String propName, int versionNum) {
		for (int i = 0; i < propToExprTable.size(); i++) {
			CompatibleProperty tmpOldProp = propToExprTable.get(i);
			if (versionNum < tmpOldProp.compatibleVersion && elementName.equalsIgnoreCase(tmpOldProp.elementName)
					&& propName.equalsIgnoreCase(tmpOldProp.propName))
				return tmpOldProp.defaultType;
		}

		return null;
	}

	private static class CompatibleProperty {

		private String elementName;
		private String propName;
		private String defaultType;

		// need to do compatibility before this version.

		private int compatibleVersion;

		CompatibleProperty(String elementName, String propName, String defaultType, int compatibleVersion) {
			this.elementName = elementName;
			this.propName = propName;
			this.defaultType = defaultType;
			this.compatibleVersion = compatibleVersion;
		}
	}
}
