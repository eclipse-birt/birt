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

package org.eclipse.birt.report.model.api.extension;

import java.util.HashMap;
import java.util.ResourceBundle;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PeerExtensionElementDefn;

/**
 * Base class for all peer-provided exceptions. The easiest implementation is to
 * simply wrap the specialized peer implementation inside one of these
 * exceptions.
 */

public class ExtendedElementException extends SemanticException {

	/**
	 * The reference to the editor.For example graphic editor, xml source editor,
	 * script editor and so on.
	 * 
	 */

	public static final String SUB_EDITOR = "sub_editor"; //$NON-NLS-1$

	/**
	 * Number of line.
	 */

	public static final String LINE_NUMBER = "lineNo"; //$NON-NLS-1$

	/**
	 * Localized message.
	 */

	public static final String LOCALIZED_MESSAGE = "localized_message"; //$NON-NLS-1$

	/**
	 * Comment for <code>serialVersionUID</code>
	 */

	private static final long serialVersionUID = 1L;

	/**
	 * Hash map for the extended element exception properties
	 */

	protected HashMap<String, Object> properties = new HashMap<String, Object>();

	/**
	 * Constructs a new model exception with no cause object.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, ResourceBundle bundle) {
		super(pluginId, errorCode, bundle);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param cause     the nested exception
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, ResourceBundle bundle,
			Throwable cause) {
		super(pluginId, errorCode, bundle, cause);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param args      string arguments used to format error messages
	 * @param cause     the nested exception
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, Object[] args,
			ResourceBundle bundle, Throwable cause) {
		super(pluginId, errorCode, args, bundle, cause);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param cause     the nested exception
	 * @param arg0      first argument used to format error messages
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, Object arg0,
			ResourceBundle bundle, Throwable cause) {
		super(pluginId, errorCode, arg0, bundle, cause);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param args      string arguments used to format error messages
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, Object[] args,
			ResourceBundle bundle) {
		super(pluginId, errorCode, args, bundle);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param bundle    the resourceBundle used to translate the message.
	 * @param arg0      first argument used to format error messages
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, Object arg0,
			ResourceBundle bundle) {
		super(pluginId, errorCode, arg0, bundle);
		this.element = element;
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param element   The element with semantic error.
	 * @param pluginId  Returns the unique identifier of the plug-in associated with
	 *                  this exception
	 * @param errorCode used to retrieve a piece of externalized message displayed
	 *                  to end user.
	 * @param cause     the nested exception
	 * @param args      string arguments used to format error messages
	 */

	public ExtendedElementException(DesignElement element, String pluginId, String errorCode, Object[] args,
			Throwable cause) {
		super(pluginId, errorCode, args, cause);
		this.element = element;
	}

	/**
	 * Sets extended element exception properties.
	 * 
	 * @param propName property name of extended element exception.
	 * @param value    value of extended element exception.
	 */

	public void setProperty(String propName, Object value) {
		if (propName == null)
			return;
		properties.put(propName, value);
	}

	/**
	 * Returns extended element exception properties.
	 * 
	 * @param propName property name of extended element exception.
	 * @return value of extended element exception.
	 */

	public Object getProperty(String propName) {
		return properties.get(propName);
	}

	/**
	 * Returns localized message.
	 * 
	 * @return localized message.
	 */

	public String getLocalizedMessage() {
		if (getProperty(LOCALIZED_MESSAGE) != null)
			return (String) getProperty(LOCALIZED_MESSAGE);

		// IF the elemetn is not null

		if (element == null)
			return sResourceKey;

		if (element instanceof ExtendedItem) {
			// Get the message from the IMessage
			ExtensionElementDefn extDefn = ((ExtendedItem) element).getExtDefn();

			if (extDefn == null) {
				return sResourceKey;
			}

			PeerExtensionElementDefn peerDefn = (PeerExtensionElementDefn) extDefn;
			IReportItemFactory peerFactory = peerDefn.getReportItemFactory();

			assert peerFactory != null;

			String externalizedMessage = sResourceKey;
			IMessages msgs = peerFactory.getMessages();
			if (msgs != null) {
				externalizedMessage = msgs.getMessage(sResourceKey, ThreadResources.getLocale());
			}

			return externalizedMessage;

		}

		throw new IllegalArgumentException(sResourceKey);

	}
}
