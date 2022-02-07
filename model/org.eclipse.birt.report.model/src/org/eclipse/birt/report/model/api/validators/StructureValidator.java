/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates a structure property of element. The property type should structure
 * .
 */

public class StructureValidator extends AbstractPropertyValidator {
	/**
	 * Name of this validator.
	 */
	public final static String NAME = "StructureValidator"; //$NON-NLS-1$

	/**
	 * Singleton instantce of the class.
	 */

	private static StructureValidator instance = new StructureValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static StructureValidator getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.validators.AbstractPropertyValidator#validate(
	 * org.eclipse.birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement, java.lang.String)
	 */

	public List validate(Module module, DesignElement element, String propName) {
		List errorList = new ArrayList();
		ElementPropertyDefn propDefn = element.getPropertyDefn(propName);

		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE && !propDefn.isList();

		Structure struct = (Structure) element.getLocalProperty(module, propDefn);
		if (struct != null) {
			errorList.addAll(struct.validate(module, element));
		}

		return errorList;
	}

}
