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

package org.eclipse.birt.report.designer.data.ui.util;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;

/**
 * ChoiceSetFactory provides common interface to access all kinds of collection
 * on given property.
 */

public class ChoiceSetFactory {

	/**
	 * Gets the collection that given structure property value can selected from
	 * them.
	 * 
	 * @param elementName The name of the element.
	 * @param property    DE Property key.
	 * @return The ChoiceSet instance contains all the allowed values.
	 */
	public static IChoiceSet getStructChoiceSet(String structName, String property) {
		IPropertyDefn propertyDefn = DEUtil.getMetaDataDictionary().getStructure(structName).findProperty(property);
		return propertyDefn.getChoices();
	}

}
