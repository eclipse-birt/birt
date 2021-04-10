/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

public interface IDataExtractionOption extends IExtractionOption {
	public static final String EXTENSION = "extension"; //$NON-NLS-1$
	public static final String INSTANCE_ID = "instanceId";//$NON-NLS-1$
	public static final String IMAGE_HANDLER = IHTMLRenderOption.IMAGE_HANDLER;
	public static final String ACTION_HANDLER = IHTMLRenderOption.ACTION_HANDLER;
	public static final String LOCALE = "locale"; //$NON-NLS-1$

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
