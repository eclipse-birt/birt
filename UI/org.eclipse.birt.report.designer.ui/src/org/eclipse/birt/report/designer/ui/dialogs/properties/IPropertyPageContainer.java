/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs.properties;

/**
 * Represents the property container object. This interface provides methods by
 * which the content pages of the property dialog can interact with the property
 * dialog itself. It provides methods to set/get the model to/from the
 * container. It also provides a method for updating the message that is
 * displayed on the container. The container implementation can use this to
 * display a title or just update the title.
 *
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.birt.report.designer.data.ui.property.IPropertyPageContainer
 *             org.eclipse.birt.report.designer.data.ui.property.IPropertyPageContainer
 *             }.
 */

@Deprecated
public interface IPropertyPageContainer {
	/**
	 * The implementation of this method should save the model object into the
	 * container.
	 *
	 * @param model The model object for the container and its pages.
	 */
	void setModel(Object model);

	/**
	 * The implementation of this method should ideally return the model object that
	 * was set by the setModel method.
	 *
	 * @return Object The model object
	 */
	Object getModel();

	/**
	 * This method can be called to set a message to be displayed in the container
	 * implementation. It can be used to show status/error information for the
	 * property pages based on user operation. The message type should be one of the
	 * constants from org.eclipse.jface.dialog.IMessageProvider.
	 *
	 * @param message     The message to be displayed to the user
	 * @param messageType The type of message. The value should be one of the
	 *                    constants from org.eclipse.jface.dialog.IMessageProvider.
	 */
	void setMessage(String message, int messageType);
}
