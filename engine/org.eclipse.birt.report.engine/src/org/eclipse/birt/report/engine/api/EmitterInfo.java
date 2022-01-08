/*******************************************************************************
 * Copyright (c) 2004, 2005, 2007 Actuate Corporation.
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

/**
 * The descriptor of the plugin emitter.
 */

public class EmitterInfo {

	private String format;
	private String id;
	private String pagination;
	private String mimeType;
	private String icon;
	private String namespace;
	private IConfigurationElement emitter;
	private String fileExtension;
	private Boolean isHidden;
	private String supportedImageFormats;
	private boolean needOutputResultSet;
	private int overridePriority;
	private boolean isFormatDeprecated;

	/**
	 * whether emitter need to output the display:none or process it in layout
	 * engine. true: output display:none in emitter and do not process it in layout
	 * engine. false: process it in layout engine, not output it in emitter.
	 */
	private Boolean outputDisplayNone;

	public EmitterInfo(String format, String id, String pagination, String mimeType, String icon, String namespace,
			String fileExt, Boolean outputDisplayNone, Boolean isHidden, String supportedImageFormats,
			boolean needOutputResultSet, IConfigurationElement emitter) {
		this.format = format;
		this.id = id;
		this.emitter = emitter;
		this.pagination = pagination;
		this.mimeType = mimeType;
		this.icon = icon;
		this.namespace = namespace;
		this.fileExtension = fileExt;
		this.outputDisplayNone = outputDisplayNone;
		this.isHidden = isHidden;
		this.supportedImageFormats = supportedImageFormats;
		this.needOutputResultSet = needOutputResultSet;
	}

	/**
	 * Get the namespace of the emitter.
	 * 
	 * @return namespace of the emitter
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * Get the icon of the emitter.
	 * 
	 * @return
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Get the format of the emitter.
	 * 
	 * @return format of the emitter
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * Get the id of the emitter.
	 * 
	 * @return id of the emitter
	 */
	public String getID() {
		return id;
	}

	/**
	 * Get the emitter instance of the emitter.
	 * 
	 * @return emitter instance
	 */
	public IConfigurationElement getEmitter() {
		return emitter;
	}

	/**
	 * Get the mimeType of the emitter.
	 * 
	 * @return mimeType of the emitter
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Get the pagination of the emitter.
	 * 
	 * @return pagination of the emitter
	 */
	public String getPagination() {
		return pagination;
	}

	/**
	 * Get the outputDisplayNone of the emitter.
	 * 
	 * @return outputDisplayNone of the emitter
	 */
	public Boolean getOutputDisplayNone() {
		return outputDisplayNone;
	}

	/**
	 * get the file extension of this emitter's output, "." not included
	 * 
	 * @return file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * get whether format is allowed to be shown
	 * 
	 * @return hideFormat
	 */
	public Boolean isHidden() {
		return isHidden;
	}

	/**
	 * get the image formats the emitter supports
	 * 
	 * @return the supported image formats.
	 */
	public String getSupportedImageFormats() {
		return supportedImageFormats;
	}

	/**
	 * If the emitter needs output query result set.
	 * 
	 * @return
	 */
	public boolean needOutputResultSet() {
		return needOutputResultSet;
	}

	/**
	 * Set needOutputResultSet.
	 * 
	 * @param needOutputResultSet
	 */
	public void setNeedOutputResultSet(boolean needOutputResultSet) {
		this.needOutputResultSet = needOutputResultSet;
	}

	/**
	 * Get the emitter override priority.
	 * 
	 * @return the override priority.
	 */
	public int getOverridePriority() {
		return overridePriority;
	}

	/**
	 * Set the emitter override priority.
	 * 
	 * @param overridePriority
	 */
	public void setOverridePriority(int overridePriority) {
		this.overridePriority = overridePriority;
	}

	/**
	 * Get if the emitter format is deprecated
	 * 
	 * @return
	 */
	public boolean isFormatDeprecated() {
		return isFormatDeprecated;
	}

	/**
	 * Set if the emitter format is deprecated
	 * 
	 * @param isFormatDeprecated
	 */
	public void setFormatDeprecated(boolean isFormatDeprecated) {
		this.isFormatDeprecated = isFormatDeprecated;
	}
}
