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

import java.io.OutputStream;
import java.util.Map;

public class DataExtractionOption extends TaskOption implements IDataExtractionOption {

	public DataExtractionOption() {
		super();
	}

	public DataExtractionOption(Map options) {
		super(options);
	}

	/**
	 * Set extension id.
	 * 
	 * @param extension extension id.
	 */
	public void setExtension(String extension) {
		setOption(EXTENSION, extension);
	}

	/**
	 * Set output file.
	 * 
	 * @param filename name of the output file.
	 */
	public void setOutputFile(String filename) {
		setOption(OUTPUT_FILE_NAME, filename);
	}

	/**
	 * Set output format.
	 * 
	 * @param format output format.
	 */
	public void setOutputFormat(String format) {
		setOption(OUTPUT_FORMAT, format);
	}

	/**
	 * Set output stream.
	 * 
	 * @param out output stream.
	 */
	public void setOutputStream(OutputStream out) {
		setOption(OUTPUT_STREAM, out);
	}

	/**
	 * Get extension.
	 */
	public String getExtension() {
		return getStringOption(EXTENSION);
	}

	/**
	 * Get output file name.
	 */
	public String getOutputFile() {
		return getStringOption(OUTPUT_FILE_NAME);
	}

	/**
	 * Get output format.
	 */
	public String getOutputFormat() {
		return getStringOption(OUTPUT_FORMAT);
	}

	/**
	 * Get output stream.
	 */
	public OutputStream getOutputStream() {
		Object value = getOption(OUTPUT_STREAM);
		if (value instanceof OutputStream) {
			return (OutputStream) value;
		}
		return null;
	}

	public IHTMLActionHandler getActionHandler() {
		Object handler = getOption(ACTION_HANDLER);
		if (handler instanceof IHTMLActionHandler) {
			return (IHTMLActionHandler) handler;
		}
		return null;
	}

	public IHTMLImageHandler getImageHandler() {
		Object handler = getOption(IMAGE_HANDLER);
		if (handler instanceof IHTMLImageHandler) {
			return (IHTMLImageHandler) handler;
		}
		return null;
	}

	public InstanceID getInstanceID() {
		Object instanceId = getOption(INSTANCE_ID);
		if (instanceId instanceof InstanceID) {
			return (InstanceID) instanceId;
		}
		return null;
	}

	public void setActionHandler(IHTMLActionHandler actionHandler) {
		setOption(ACTION_HANDLER, actionHandler);
	}

	public void setImageHandler(IHTMLImageHandler imageHandler) {
		setOption(IMAGE_HANDLER, imageHandler);
	}

	public void setInstanceID(InstanceID iid) {
		setOption(INSTANCE_ID, iid);
	}

	public void setFormatter(Map formatters) {
		setOption(OPTION_FORMATTER, formatters);
	}

	public Map getFormatter() {
		Object formatter = getOption(OPTION_FORMATTER);
		if (formatter instanceof Map) {
			return (Map) formatter;
		}
		return null;
	}
}
