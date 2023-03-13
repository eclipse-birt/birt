/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
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

import org.eclipse.birt.core.framework.IConfigurationElement;

public class DataExtractionFormatInfo {
	/**
	 * Extraction format of the output file.
	 */
	private String format;

	/**
	 * Data extraction extension id.
	 */
	private String id;

	/**
	 * Mime type of the output file.
	 */
	private String mimeType;

	/**
	 * Data extraction extension name.
	 */
	private String name;

	/**
	 * Configuration element of data extraction extension.
	 */
	private IConfigurationElement dataExtractionExtension;

	/**
	 * whether format is shown
	 */
	private Boolean isHidden;

	/**
	 * Constructor of the class DataExtractionFormatInfo.
	 *
	 * @param id
	 * @param format
	 * @param mimeType
	 * @param name
	 * @param dataExtractionExtension
	 */
	public DataExtractionFormatInfo(String id, String format, String mimeType, String name, Boolean isHidden,
			IConfigurationElement dataExtractionExtension) {
		this.id = id;
		this.format = format;
		this.mimeType = mimeType;
		this.name = name;
		this.isHidden = isHidden;
		this.dataExtractionExtension = dataExtractionExtension;
	}

	/**
	 * Get extraction format of the output file.
	 *
	 * @return format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Get data extraction extension id.
	 *
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get mime type of the output file.
	 *
	 * @return mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Get data extraction extension name.
	 *
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get configuration element of data extraction extension.
	 *
	 * @return dataExtractionExtension
	 */
	public IConfigurationElement getDataExtractionExtension() {
		return dataExtractionExtension;
	}

	/**
	 * Get whether format could be shown
	 *
	 * @return hideFormat
	 */
	public Boolean isHidden() {
		return isHidden;
	}
}
