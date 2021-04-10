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

package org.eclipse.birt.report.model.api.activity;

import java.util.ResourceBundle;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Represents exceptions encountered during parsing the xml file, it will
 * include a reference to the element which causes the error.
 * 
 */

public class SemanticException extends ModelException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -5947401970525709666L;

	/**
	 * The error code that indicates the element can not be exported to the library
	 * file, for it self or any of its contents has duplicate name with that in the
	 * library file and the element in the library can not be dropped.
	 */
	public static final String DESIGN_EXCEPTION__EXPORT_ELEMENT_FAIL = MessageConstants.SEMANTIC_EXCEPTION_EXPORT_ELEMENT_FAIL;

	/**
	 * The element with semantic error.
	 */

	protected DesignElement element;

	/**
	 * Constructor.
	 * 
	 * @param errCode the error code
	 * 
	 */

	protected SemanticException(String errCode) {
		super(errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param element the element which has errors
	 * @param errCode the error code
	 */

	public SemanticException(DesignElement element, String errCode) {
		super(errCode);
		this.element = element;
	}

	/**
	 * Constructor.
	 * 
	 * @param element the element which has errors
	 * @param errCode the error code
	 * @param cause   the nested exception
	 */

	public SemanticException(DesignElement element, String errCode, Throwable cause) {
		super(PLUGIN_ID, errCode, null, null, cause);
		this.element = element;
	}

	/**
	 * Constructor.
	 * 
	 * @param element the element which has errors
	 * @param values  value array used for error message
	 * @param errCode the error code
	 */

	public SemanticException(DesignElement element, String[] values, String errCode) {
		super(errCode, values, null);
		this.element = element;
	}

	/**
	 * Constructor.
	 * 
	 * @param element the element which has errors
	 * @param values  value array used for error message
	 * @param errCode the error code
	 * @param cause   the nested exception
	 */

	public SemanticException(DesignElement element, String[] values, String errCode, Throwable cause) {
		super(errCode, values, cause);
		this.element = element;
	}

	/**
	 * Constructs a new model exception with no cause object.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 */

	public SemanticException(String pluginId, String errorCode, ResourceBundle bundle) {
		super(pluginId, errorCode, bundle);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param cause     the nested exception
	 */

	public SemanticException(String pluginId, String errorCode, ResourceBundle bundle, Throwable cause) {
		super(pluginId, errorCode, bundle, cause);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param args      string arguments used to format error messages
	 * @param cause     the nested exception
	 */

	public SemanticException(String pluginId, String errorCode, Object[] args, ResourceBundle bundle, Throwable cause) {
		super(pluginId, errorCode, args, bundle, cause);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param cause     the nested exception
	 * @param arg0      first argument used to format error messages
	 */

	public SemanticException(String pluginId, String errorCode, Object arg0, ResourceBundle bundle, Throwable cause) {
		super(pluginId, errorCode, arg0, bundle, cause);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param args      string arguments used to format error messages
	 */

	public SemanticException(String pluginId, String errorCode, Object[] args, ResourceBundle bundle) {
		super(pluginId, errorCode, args, bundle);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param arg0      first argument used to format error messages
	 */

	public SemanticException(String pluginId, String errorCode, Object arg0, ResourceBundle bundle) {
		super(pluginId, errorCode, arg0, bundle);
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param cause     the nested exception
	 * @param args      string arguments used to format error messages
	 */

	public SemanticException(String pluginId, String errorCode, Object[] args, Throwable cause) {
		super(pluginId, errorCode, args, cause);
	}

	/**
	 * Returns the element having semantic error.
	 * 
	 * @return the element having semantic error
	 */

	public DesignElement getElement() {
		return element;
	}

	/**
	 * Returns the element name if it exists.
	 * 
	 * @param element the design element
	 * @return the element name if it exists. Otherwise, return empty string.
	 */

	protected static String getElementName(DesignElement element) {
		// if it is not library/report, returns the identifier.
		if (!(element instanceof Module))
			return element.getIdentifier();

		return element.getDefn().getDisplayName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */

	public String getMessage() {
		return getLocalizedMessage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.exception.BirtException#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		if (oaMessageArguments != null)
			return ModelMessages.getMessage(sResourceKey, oaMessageArguments);
		return ModelMessages.getMessage(sResourceKey);

	}
}