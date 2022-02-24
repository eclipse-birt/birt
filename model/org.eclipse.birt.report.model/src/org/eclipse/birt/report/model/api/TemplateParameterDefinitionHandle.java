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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.interfaces.ITemplateParameterDefinitionModel;

/**
 * Represents a template parameter definition element. A template parameter
 * definitio gives a definition of a template element. It can be referred by one
 * template report item or one template data set. The template parameter
 * definition has the following properties:
 * 
 * <ul>
 * <li>A required and unique name for this template parameter definition.
 * <li>A type for this template parameter definition. It is the the enumeration
 * of Table,Grid, Label, Text, ExtendedItem, other kind of report items and
 * Dataset.
 * <li>A static description message to display.
 * </ul>
 * 
 * The application generally does not create template parameter definition
 * handles directly. Instead, BIRT will create it when users replace an actual
 * report item or data set with a template element.
 */

public class TemplateParameterDefinitionHandle extends ReportElementHandle
		implements ITemplateParameterDefinitionModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TemplateParameterDefinitionHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Gets allowed type of the template parameter definition.
	 * 
	 * @return the allowed type of the template parameter definition
	 */

	public String getAllowedType() {
		return getStringProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP);
	}

	/**
	 * Sets the allowed type of the template parameter definition.
	 * 
	 * @param type the type to set
	 * @throws SemanticException if the property is locked
	 */

	public void setAllowedType(String type) throws SemanticException {
		setProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP, type);
	}

	/**
	 * Returns the static description for the template parameter definition.
	 * 
	 * @return the static description to display
	 */

	public String getDescription() {
		return getStringProperty(ITemplateParameterDefinitionModel.DESCRIPTION_PROP);
	}

	/**
	 * Returns the localized description for the template parameter definition. If
	 * the localized description for the description resource key is found, it will
	 * be returned. Otherwise, the static description will be returned.
	 * 
	 * @return the localized description for the template parameter definition
	 */

	public String getDisplayDescription() {
		return getExternalizedValue(ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP,
				ITemplateParameterDefinitionModel.DESCRIPTION_PROP);
	}

	/**
	 * Sets the description of the template parameter definition. Sets the static
	 * description itself. If the template parameter definition is to be
	 * externalized, then set the description ID separately.
	 * 
	 * @param description the new description for the template parameter definition
	 * @throws SemanticException if the property is locked.
	 */

	public void setDescription(String description) throws SemanticException {
		setStringProperty(ITemplateParameterDefinitionModel.DESCRIPTION_PROP, description);
	}

	/**
	 * Returns the resource key of the static description of the template parameter
	 * definition.
	 * 
	 * @return the resource key of the static description
	 */

	public String getDescriptionKey() {
		return getStringProperty(ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP);
	}

	/**
	 * Sets the resource key of the static description of the template parameter
	 * definition.
	 * 
	 * @param resourceKey the resource key of the static description
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setDescriptionKey(String resourceKey) throws SemanticException {
		setStringProperty(ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP, resourceKey);
	}

	/**
	 * Gets the default element of this template parameter definition.
	 * 
	 * @return the default element of this template parameter definition
	 */

	public DesignElementHandle getDefaultElement() {
		DesignElement element = ((TemplateParameterDefinition) getElement()).getDefaultElement();
		if (element == null)
			return null;
		return element.getHandle(module);
	}

	/**
	 * Gets the default slot of this template parameter definition.
	 * 
	 * @return the handle for the default slot of this template parameter definition
	 */

	SlotHandle getDefault() {
		return getSlot(ITemplateParameterDefinitionModel.DEFAULT_SLOT);
	}
}
