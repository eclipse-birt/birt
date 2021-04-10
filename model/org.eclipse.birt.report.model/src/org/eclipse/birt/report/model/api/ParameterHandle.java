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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IParameterModel;

/**
 * Represents the various parameter types. This abstract base parameter element
 * defines properties common to all types of parameters.
 * 
 * @see org.eclipse.birt.report.model.elements.Parameter
 */

public abstract class ParameterHandle extends ReportElementHandle implements IParameterModel {

	/**
	 * Constructs the handle for a parameter with the given design and element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public ParameterHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the additional text to display for the parameter to explain how to
	 * use the parameter.
	 * 
	 * @return the help text
	 */

	public String getHelpText() {
		return getStringProperty(IParameterModel.HELP_TEXT_PROP);
	}

	/**
	 * Returns the additional text to display for the parameter to explain how to
	 * use the parameter.
	 * 
	 * @param helpText the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpText(String helpText) throws SemanticException {
		setStringProperty(IParameterModel.HELP_TEXT_PROP, helpText);
	}

	/**
	 * Returns the resource key of the additional text to display for the parameter
	 * to explain how to use the parameter.
	 * 
	 * @return the resource key of the help text
	 */

	public String getHelpTextKey() {
		return getStringProperty(IParameterModel.HELP_TEXT_KEY_PROP);
	}

	/**
	 * Sets the resource key of the additional text to display for the parameter to
	 * explain how to use the parameter.
	 * 
	 * @param resourceKey the resource key of the help text
	 * 
	 * @throws SemanticException if the property is locked.
	 */

	public void setHelpTextKey(String resourceKey) throws SemanticException {
		setStringProperty(IParameterModel.HELP_TEXT_KEY_PROP, resourceKey);
	}

	/**
	 * Tests whether the parameter will appear in the Requester page. Parameter is
	 * visible by default.
	 * 
	 * @return <code>true</code> means the parameter will not be visible.
	 *         <code>false</code> means the invisibility of the parameter.
	 */

	public boolean isHidden() {
		return getBooleanProperty(IParameterModel.HIDDEN_PROP);
	}

	/**
	 * Sets the hidden property of this parameter. If <code>true</code>, the
	 * parameter will not appear in the Requester page. Parameter is visible by
	 * default.
	 * 
	 * @param hidden <code>true</code> if the parameter is visible. Otherwise
	 *               <code>false</code>.
	 * @throws SemanticException if the property is locked.
	 */

	public void setHidden(boolean hidden) throws SemanticException {
		setBooleanProperty(IParameterModel.HIDDEN_PROP, hidden);
	}

	/**
	 * Gets the custom validation code for the parameter.
	 * 
	 * @return the custom validation code for the parameter
	 */

	public String getValidate() {
		return getStringProperty(IParameterModel.VALIDATE_PROP);
	}

	/**
	 * Sets the custom validation code for the parameter.
	 * 
	 * @param validation the custom validation code to set
	 * @throws SemanticException if the property is locked
	 */

	public void setValidate(String validation) throws SemanticException {
		setProperty(IParameterModel.VALIDATE_PROP, validation);
	}

	/**
	 * Set the value for the display prompt context.
	 * 
	 * @param promptValue The display prompt context.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptText(String promptValue) throws SemanticException {
		setStringProperty(PROMPT_TEXT_PROP, promptValue);
	}

	/**
	 * get the display prompt text.
	 * 
	 * @return the display prompt.
	 * 
	 */

	public String getPromptText() {
		return getStringProperty(PROMPT_TEXT_PROP);
	}

	/**
	 * Set the value for the prompt text ID.
	 * 
	 * @param promptIDValue The prompt text ID.
	 * 
	 * @throws SemanticException
	 * 
	 */

	public void setPromptTextID(String promptIDValue) throws SemanticException {
		setStringProperty(PROMPT_TEXT_ID_PROP, promptIDValue);
	}

	/**
	 * Returns the prompt text ID.
	 * 
	 * @return the prompt text ID.
	 * 
	 */

	public String getPromptTextID() {
		return getStringProperty(PROMPT_TEXT_ID_PROP);
	}

	/**
	 * Returns the localized text for prompt text. If the localized text for the
	 * text resource key is found, it will be returned. Otherwise, the static text
	 * will be returned.
	 * 
	 * @return the localized text for the prompt text
	 */

	public String getDisplayPromptText() {
		return getExternalizedValue(PROMPT_TEXT_ID_PROP, PROMPT_TEXT_PROP);
	}
}