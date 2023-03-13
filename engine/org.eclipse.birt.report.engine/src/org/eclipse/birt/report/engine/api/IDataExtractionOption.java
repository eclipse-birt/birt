/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

public interface IDataExtractionOption extends IExtractionOption {
	String EXTENSION = "extension"; //$NON-NLS-1$
	String INSTANCE_ID = "instanceId";//$NON-NLS-1$
	String IMAGE_HANDLER = IHTMLRenderOption.IMAGE_HANDLER;
	String ACTION_HANDLER = IHTMLRenderOption.ACTION_HANDLER;
	String LOCALE = "locale"; //$NON-NLS-1$

	/**
	 * Set extension id.
	 *
	 * @param extension extension id.
	 */
	void setExtension(String extension);

	/**
	 * Get extension.
	 */
	String getExtension();

	void setInstanceID(InstanceID iid);

	InstanceID getInstanceID();

	void setImageHandler(IHTMLImageHandler imageHandler);

	IHTMLImageHandler getImageHandler();

	void setActionHandler(IHTMLActionHandler actionHandler);

	IHTMLActionHandler getActionHandler();
}
