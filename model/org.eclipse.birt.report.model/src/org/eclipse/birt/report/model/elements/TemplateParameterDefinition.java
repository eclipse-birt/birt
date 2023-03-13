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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
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
 */

public class TemplateParameterDefinition extends ReferenceableElement implements ITemplateParameterDefinitionModel {

	/**
	 * Holds the default report item or data set that reside directly on the
	 * template parameter definition.
	 */

	/**
	 * Default constructor.
	 */

	public TemplateParameterDefinition() {
		initSlots();
	}

	/**
	 * Constructs the template parameter definition with a required name.
	 *
	 * @param theName the required name
	 */

	public TemplateParameterDefinition(String theName) {
		super(theName);
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitTemplateParameterDefinition(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.TEMPLATE_PARAMETER_DEFINITION;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design of the style
	 *
	 * @return an API handle for this element
	 */

	public TemplateParameterDefinitionHandle handle(Module module) {
		if (handle == null) {
			handle = new TemplateParameterDefinitionHandle(module, this);
		}
		return (TemplateParameterDefinitionHandle) handle;
	}

	/**
	 * Returns the slot in this cell defined by the slot ID.
	 *
	 * @param slot the slot ID
	 *
	 * @return the retrieved slot.
	 *
	 *
	 */

	@Override
	public ContainerSlot getSlot(int slot) {
		assert (slot == DEFAULT_SLOT);
		return slots[DEFAULT_SLOT];
	}

	/**
	 * Gets allowed type of the template parameter definition.
	 *
	 * @param module the module of this parameter definition
	 * @return the allowed type of the template parameter definition
	 */

	public String getAllowedType(Module module) {
		return getStringProperty(module, ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP);
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
	 * @param module the module of this parameter definition
	 * @return the static description to display
	 */

	public String getDescription(Module module) {
		return getStringProperty(module, ITemplateParameterDefinitionModel.DESCRIPTION_PROP);
	}

	/**
	 * Returns the localized description for the template parameter definition. If
	 * the localized description for the description resource key is found, it will
	 * be returned. Otherwise, the static description will be returned.
	 *
	 * @param module the module of this parameter definition
	 * @return the localized description for the template parameter definition
	 */

	public String getDisplayDescription(Module module) {
		String textKey = getStringProperty(module, ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP);
		if (!StringUtil.isBlank(textKey)) {
			// find in report.

			String localizedText = module.getMessage(textKey);
			if (!StringUtil.isBlank(localizedText)) {
				return localizedText;
			}
		}

		// use static text.

		return getDescription(module);
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
		setProperty(ITemplateParameterDefinitionModel.DESCRIPTION_PROP, description);
	}

	/**
	 * Returns the resource key of the static description of the template parameter
	 * definition.
	 *
	 * @param module the module of this parameter definition
	 * @return the resource key of the static description
	 */

	public String getDescriptionKey(Module module) {
		return getStringProperty(module, ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP);
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
		setProperty(ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP, resourceKey);
	}

	/**
	 * Gets the default element of this template parameter definition.
	 *
	 * @return the default element of this template parameter definition
	 */

	public DesignElement getDefaultElement() {
		ContainerSlot defaultElement = getSlot(ITemplateParameterDefinitionModel.DEFAULT_SLOT);
		if (defaultElement.getCount() == 0) {
			return null;
		}
		assert defaultElement.getCount() == 1;
		return (DesignElement) defaultElement.getContent(0);
	}
}
