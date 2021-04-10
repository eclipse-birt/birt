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

package org.eclipse.birt.doc.schema;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;

/**
 * Css style property defn filter
 * 
 */

public class CssStyleFilter implements IFilter {

	private String[] forbiddenValues = { "numberAlign", "backgroundPositionX", "backgroundPositionY" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
			, "textUnderline", "textOverline", "textLineThrough" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	};

	/**
	 * filter property defn if type is structure or boolean
	 */

	public boolean filter(IPropertyDefn propDefn) {
		if (propDefn == null) {
			return false;
		}
		int typeCode = propDefn.getTypeCode();

		// type is structure or type is boolean or name
		if (typeCode == IPropertyType.STRUCT_TYPE || typeCode == IPropertyType.NAME_TYPE
				|| typeCode == IPropertyType.BOOLEAN_TYPE) {
			// TODO check 'canShrink' can be used or not in w3c.

			return false;
		}

		return filterByManual(propDefn.getName());
	}

	/**
	 * Manually filter some property.
	 * 
	 * @param propName
	 * @return <code>true</code> if property is allowed, else return
	 *         <code>false</code>
	 */
	private boolean filterByManual(String propName) {
		for (int i = 0; i < forbiddenValues.length; ++i) {
			String forbiddenValue = forbiddenValues[i];
			if (forbiddenValue.equalsIgnoreCase(propName)) {
				return false;
			}
		}
		return true;
	}

}
