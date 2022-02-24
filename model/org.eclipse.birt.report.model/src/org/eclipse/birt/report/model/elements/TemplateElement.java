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

import java.util.List;

import org.eclipse.birt.report.model.api.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Abstract class for template elements. A template element is a place holder to
 * generate a real report item or data set element. Application reads the
 * default element of it by method {@link #getDefaultElement(Module)} and clone
 * a new report item or data set based on the default element. Then application
 * can make some changes about the cloned element, such as set some property
 * values, add some contents, delete some contents. Now, application can use the
 * cloned element with changes or with no change to transform this place holder
 * and get a real report item or data set.
 * 
 * @see org.eclipse.birt.report.model.elements.TemplateReportItem
 * @see org.eclipse.birt.report.model.elements.TemplateDataSet
 */

public abstract class TemplateElement extends DesignElement {

	/**
	 * Default constructor.
	 */

	public TemplateElement() {
	}

	/**
	 * Constructs the template element with a name.
	 * 
	 * @param theName the name
	 */

	public TemplateElement(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	public List validate(Module module) {
		List list = super.validate(module);

		// Check the element reference of refTemplateParameter property

		list.addAll(ElementReferenceValidator.getInstance().validate(module, this, REF_TEMPLATE_PARAMETER_PROP));

		return list;
	}

	/**
	 * Gets allowed type of the template element.
	 * 
	 * @param module the module of the template element
	 * @return the allowed type of the template element
	 */

	public String getAllowedType(Module module) {
		TemplateParameterDefinition refTemplateParam = (TemplateParameterDefinition) getTemplateParameterElement(
				module);
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getAllowedType(module);
	}

	/**
	 * Returns the static description for the template element.
	 * 
	 * @param module the module of the template element
	 * @return the static description to display
	 */

	public String getDescription(Module module) {
		TemplateParameterDefinition refTemplateParam = (TemplateParameterDefinition) getTemplateParameterElement(
				module);
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDescription(module);
	}

	/**
	 * Returns the localized description for the template element. If the localized
	 * description for the description resource key is found, it will be returned.
	 * Otherwise, the static description will be returned.
	 * 
	 * @param module the module of the template element
	 * @return the localized description for the template element
	 */

	public String getDisplayDescription(Module module) {
		TemplateParameterDefinition refTemplateParam = (TemplateParameterDefinition) getTemplateParameterElement(
				module);
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDisplayDescription(module);
	}

	/**
	 * Returns the resource key of the static description of the template element.
	 * 
	 * @param module the module of the template element
	 * @return the resource key of the static description
	 */

	public String getDescriptionKey(Module module) {
		TemplateParameterDefinition refTemplateParam = (TemplateParameterDefinition) getTemplateParameterElement(
				module);
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDescriptionKey(module);
	}

	/**
	 * Gets the default element of this template element.
	 * 
	 * @param module the module of the template element
	 * @return the default element of this template element
	 */

	public DesignElement getDefaultElement(Module module) {
		TemplateParameterDefinition refTemplateParam = (TemplateParameterDefinition) getTemplateParameterElement(
				module);
		if (refTemplateParam == null)
			return null;
		return refTemplateParam.getDefaultElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#clone()
	 */

	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		DesignElement element = (DesignElement) super.doClone(policy);

		// if template parameter definition is resolved, copy the resolved
		// element to the result

		ElementRefValue templateParam = (ElementRefValue) propValues
				.get(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP);
		if (templateParam != null) {
			if (templateParam.getElement() != null) {
				ElementRefValue ref = new ElementRefValue(null, templateParam.getElement());
				element.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, ref);
			}

		}

		return element;
	}

}
