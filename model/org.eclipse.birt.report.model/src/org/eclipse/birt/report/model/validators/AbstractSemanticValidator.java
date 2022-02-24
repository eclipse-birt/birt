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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Abstract class for semantic validator. It provides the validator name and the
 * target element on which the validation is performed.
 */

public class AbstractSemanticValidator {

	/**
	 * Constant for indicating one validator can be applied to design.
	 */

	private final static String MODULE_DESIGN = "design"; //$NON-NLS-1$

	/**
	 * Constant for indicating one validator can be applied to library.
	 */

	private final static String MODULE_LIBRARY = "library"; //$NON-NLS-1$

	/**
	 * The internal name of the validator.
	 */

	protected String name = null;

	/**
	 * The modules that this validator can be applied on.
	 */

	protected String[] modules = new String[2];

	/**
	 * Returns the validator name.
	 * 
	 * @return the validator name
	 */

	public String getName() {
		return name;
	}

	/**
	 * Set the name of the validator, name is referenced by a property as key to the
	 * validator.
	 * 
	 * @param name name of the validator, can not be <code>null</code>.
	 */

	public void setName(String name) {
		assert name != null;

		this.name = name;
	}

	/**
	 * Sets the modules which this validator can be applied to. The
	 * <code>moduleNames</code> is the string seperated by commas.
	 * <p>
	 * For example, "design, library"
	 * 
	 * @param moduleNames the module names to set
	 */

	public void setModules(String moduleNames) {
		String[] splittedModuleNames = moduleNames.split(","); //$NON-NLS-1$
		assert splittedModuleNames.length <= 2;

		for (int i = 0; i < splittedModuleNames.length; i++) {
			modules[i] = splittedModuleNames[i].trim();
		}
	}

	/**
	 * Returns whether this validator can be applied to design.
	 * 
	 * @return true if this validator can be applied to design; otherwise, return
	 *         false.
	 */

	public boolean canApplyToDesign() {
		return canApplyToModule(MODULE_DESIGN);
	}

	/**
	 * Returns whether this validator can be applied to library.
	 * 
	 * @return true if this validator can be applied to library; otherwise, return
	 *         false.
	 */

	public boolean canApplyToLibrary() {
		return canApplyToModule(MODULE_LIBRARY);
	}

	/**
	 * Returns whether this validator can be applied to the given module.
	 * 
	 * @param moduleName the module name.
	 * @return true if this validator can be applied to the given module.
	 */

	private boolean canApplyToModule(String moduleName) {
		assert moduleName == MODULE_DESIGN || moduleName == MODULE_LIBRARY;

		for (int i = 0; i < modules.length; i++) {
			if (moduleName.equals(modules[i]))
				return true;
		}

		return false;
	}

	/**
	 * Checks whether the given element is contained by one of template parameter
	 * definition.
	 * 
	 * @param element the design element
	 * @return <code>true</code> if the element is in the template parameter
	 *         definition. Otherwise, <code>false</code>.
	 */
	protected static boolean isInTemplateParameterDefinitionSlot(DesignElement element) {
		if (element == null)
			return false;
		return element.isInTemplateParameterDefinitionSlot();
	}
}
