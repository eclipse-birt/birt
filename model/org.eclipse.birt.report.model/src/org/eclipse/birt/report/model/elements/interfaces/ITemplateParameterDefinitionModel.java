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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for TemplateParameterDefinition element to store the constants
 * on TemplateParameterDefinition element.
 */

public interface ITemplateParameterDefinitionModel {

	/**
	 * Name of the property that defines the unique name of the
	 * TemplateParameterDefinition. Name of the TemplateParameterDefintion is
	 * required and unique. It will be stored in the TemplateParameterDefinition
	 * namespace in the module.
	 */

	String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the allowed element type of this
	 * TemplateParameterDefinition. It is enumeration of Table,Grid, Label, Text,
	 * ExtendedItem, other kind of report items and Dataset.
	 *
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	String ALLOWED_TYPE_PROP = "allowedType"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the description of this
	 * TemplateParameterDefinition.
	 */

	String DESCRIPTION_PROP = "description"; //$NON-NLS-1$

	/**
	 * Name of the property that given the resouce key to localize the description
	 * for this TemplateParameterDefinition.
	 */

	String DESCRIPTION_ID_PROP = "descriptionID"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the default element of the
	 * TemplateParameterDefinition.
	 */

	int DEFAULT_SLOT = 0;

	/**
	 * The slot count of template parameter definition. There are only 2 slots
	 * defined in it, the default and value slot.
	 */

	int SLOT_COUNT = 1;

}
