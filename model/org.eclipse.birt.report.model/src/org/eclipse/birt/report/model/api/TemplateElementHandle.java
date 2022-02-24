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
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.strategy.CopyForTemplatePolicy;

/**
 * Abstract handle for template elements. A template element is a place holder
 * to generate a real report item or data set element. Application reads the
 * default element of it by method {@link #getDefaultElement()} and clone a new
 * report item or data set based on the default element. Then application can
 * make some changes about the cloned element, such as set some property values,
 * add some contents, delete some contents. Now, application can use the cloned
 * element with changes or with no change to transform this place holder and get
 * a real report item or data set.
 * 
 * @see org.eclipse.birt.report.model.api.TemplateReportItemHandle
 * @see org.eclipse.birt.report.model.api.TemplateDataSetHandle
 */

public abstract class TemplateElementHandle extends ReportElementHandle {

	/**
	 * Constructs the handle for a report item with the given design and element.
	 * The application generally does not create handles directly. Instead, it uses
	 * one of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TemplateElementHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the referred template parameter definition of the template element.
	 * 
	 * @return the handle to the referred template parameter definition
	 */

	TemplateParameterDefinitionHandle getRefTemplateParameter() {
		TemplateParameterDefinition refTemplateParam = ((TemplateElement) getElement())
				.getTemplateParameterElement(module);
		if (refTemplateParam == null)
			return null;

		return (TemplateParameterDefinitionHandle) refTemplateParam.getHandle(module);
	}

	/**
	 * Gets allowed type of the template element.
	 * 
	 * @return the allowed type of the template element
	 */

	public String getAllowedType() {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getAllowedType();
	}

	/**
	 * Returns the static description for the template element.
	 * 
	 * @return the static description to display
	 */

	public String getDescription() {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDescription();
	}

	/**
	 * Returns the localized description for the template element. If the localized
	 * description for the description resource key is found, it will be returned.
	 * Otherwise, the static description will be returned.
	 * 
	 * @return the localized description for the template element
	 */

	public String getDisplayDescription() {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDisplayDescription();
	}

	/**
	 * Sets the description of the template element. Sets the static description
	 * itself. If the template element is to be externalized, then set the
	 * description ID separately.
	 * 
	 * @param description the new description for the template element
	 * @throws SemanticException if the property is locked.
	 */

	public void setDescription(String description) throws SemanticException {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return;
		refTemplateParam.setDescription(description);
	}

	/**
	 * Returns the resource key of the static description of the template element.
	 * 
	 * @return the resource key of the static description
	 */

	public String getDescriptionKey() {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDescriptionKey();
	}

	/**
	 * Sets the resource key of the static description of the template element.
	 * 
	 * @param resourceKey the resource key of the static description
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setDescriptionKey(String resourceKey) throws SemanticException {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return;
		refTemplateParam.setDescriptionKey(resourceKey);
	}

	/**
	 * Gets the default element of this template element.
	 * 
	 * @return the default element of this template element
	 */

	public DesignElementHandle getDefaultElement() {
		TemplateParameterDefinitionHandle refTemplateParam = getRefTemplateParameter();
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDefaultElement();
	}

	/**
	 * Returns a copy for the default element in the template element.
	 * 
	 * @return a clone element of the default element
	 */

	public IDesignElement copyDefaultElement() {
		try {
			return (IDesignElement) getDefaultElement().getElement().doClone(CopyForTemplatePolicy.getInstance());
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return null;
	}
}
