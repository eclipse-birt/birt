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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;
import org.eclipse.birt.report.model.validators.AbstractSemanticValidator;

/**
 * Represents the definition of the validation which is applied to property or
 * slot. It defines the actual usage context for validation. Generally, it has
 * the following in all usage contexts:
 * <ul>
 * <li>Validator name
 * <li>Validator instance
 * <li>Flag indicating whether this validation is pre-requisite
 * </ul>
 * There are three usage contexts:
 * <ul>
 * <li>Specific Property Change
 * <li>Specific Slot Change - the name of target element definition. It's
 * because this change requires the validation be performed on target element
 * type, instead of the direct container.
 * <li>Property Change with Specific Type - the property name. It's because this
 * change requires the validation be performed on specific property.
 * </ul>
 * 
 * So it contains not only the validator name and validator, but also the flag
 * indicating whether this validation is pre-requisite, the property name and
 * the target element name.
 */

public class SemanticTriggerDefn {

	/**
	 * The name of validator.
	 */

	private String validatorName = null;

	/**
	 * Whether this validation is pre-requisite.
	 */

	private boolean preRequisite = false;

	/**
	 * The validator instance.
	 */

	private AbstractSemanticValidator validator = null;

	/**
	 * Name of the property
	 */

	private String propertyName = null;

	/**
	 * Name of the target element definition.
	 */

	private String targetElement = null;

	/**
	 * Constructs the semantic validation definition with validator name.
	 * 
	 * @param validatorName semantic validator name
	 */

	SemanticTriggerDefn(String validatorName) {
		this.validatorName = validatorName;
	}

	/**
	 * Whether this validation is pre-requisite.
	 * 
	 * @return <code>true</code>, if this validation is pre-requisite.
	 */

	public boolean isPreRequisite() {
		return preRequisite;
	}

	/**
	 * Returns whether this validation is pre-requisite.
	 * 
	 * @param preRequisite the flag to set
	 */

	void setPreRequisite(boolean preRequisite) {
		this.preRequisite = preRequisite;
	}

	/**
	 * Returns the validator name.
	 * 
	 * @return the validator name
	 */

	public String getValidatorName() {
		return validatorName;
	}

	/**
	 * Sets the validator.
	 * 
	 * @param validator the validator to set
	 */

	void setValidator(AbstractSemanticValidator validator) {
		this.validator = validator;
	}

	/**
	 * Returns the validator instance.
	 * 
	 * @return validator instance
	 */

	public AbstractSemanticValidator getValidator() {
		return validator;
	}

	/**
	 * Returns the property name when this validation is used in Property Change
	 * with Specific Type.
	 * 
	 * @return the name of the property this validation is applied on
	 */

	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the property name
	 * 
	 * @param propName the property name to set
	 */

	void setPropertyName(String propName) {
		this.propertyName = propName;
	}

	/**
	 * Returns the name of the target element definition.
	 * 
	 * @return the name of the target element definition
	 */

	public String getTargetElement() {
		return targetElement;
	}

	/**
	 * Sets the name of the target element definition.
	 * 
	 * @param targetElement the name of the target element definition.
	 */

	void setTargetElement(String targetElement) {
		this.targetElement = targetElement;
	}

	/**
	 * Returns the ID of one validation, which is used to identify one validation.
	 * <ul>
	 * <li>For {@link AbstractElementValidator}, the validation ID is just the
	 * validator name. For example, "CellOverlappingValidator".
	 * <li>For {@link AbstractPropertyValidator}, the validation ID is the
	 * validation name and name of the property on which the validator is applied.
	 * For example, "ValueRequiredValidator.DataSet". "DataSet" is the name of the
	 * property which should be validated by ValueRequiredValidator.
	 * </ul>
	 * 
	 * @return the validation ID of this trigger definition
	 */

	public String getValidationID() {
		String validationID;

		if (!StringUtil.isBlank(propertyName))
			validationID = validatorName + "." + propertyName; //$NON-NLS-1$
		else
			validationID = validatorName;

		return validationID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getValidatorName()))
			return getValidatorName();
		return super.toString();
	}
}
