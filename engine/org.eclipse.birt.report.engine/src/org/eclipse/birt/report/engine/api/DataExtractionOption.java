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

/**
 * Handle extended extraction options
 *
 * @since 3.3
 *
 */
public class DataExtractionOption extends TaskOption implements IDataExtractionOption {

	/**
	 * Constructor 1
	 */
	public DataExtractionOption() {
		super();
	}

	/**
	 * Constructor 2
	 *
	 * @param options extraction option
	 */
	public DataExtractionOption(Map<String, Object> options) {
		super(options);
	}

	/**
	 * Set extension id.
	 *
	 * @param extension extension id.
	 */
	@Override
	public void setExtension(String extension) {
		setOption(EXTENSION, extension);
	}

	/**
	 * Set output file.
	 *
	 * @param filename name of the output file.
	 */
	@Override
	public void setOutputFile(String filename) {
		setOption(OUTPUT_FILE_NAME, filename);
	}

	/**
	 * Set output format.
	 *
	 * @param format output format.
	 */
	@Override
	public void setOutputFormat(String format) {
		setOption(OUTPUT_FORMAT, format);
	}

	/**
	 * Set output stream.
	 *
	 * @param out output stream.
	 */
	@Override
	public void setOutputStream(OutputStream out) {
		setOption(OUTPUT_STREAM, out);
	}

	/**
	 * Get extension.
	 */
	@Override
	public String getExtension() {
		return getStringOption(EXTENSION);
	}

	/**
	 * Get output file name.
	 */
	@Override
	public String getOutputFile() {
		return getStringOption(OUTPUT_FILE_NAME);
	}

	/**
	 * Get output format.
	 */
	@Override
	public String getOutputFormat() {
		return getStringOption(OUTPUT_FORMAT);
	}

	/**
	 * Get output stream.
	 */
	@Override
	public OutputStream getOutputStream() {
		Object value = getOption(OUTPUT_STREAM);
		if (value instanceof OutputStream) {
			return (OutputStream) value;
		}
		return null;
	}

	@Override
	public IHTMLActionHandler getActionHandler() {
		Object handler = getOption(ACTION_HANDLER);
		if (handler instanceof IHTMLActionHandler) {
			return (IHTMLActionHandler) handler;
		}
		return null;
	}

	@Override
	public IHTMLImageHandler getImageHandler() {
		Object handler = getOption(IMAGE_HANDLER);
		if (handler instanceof IHTMLImageHandler) {
			return (IHTMLImageHandler) handler;
		}
		return null;
	}

	@Override
	public InstanceID getInstanceID() {
		Object instanceId = getOption(INSTANCE_ID);
		if (instanceId instanceof InstanceID) {
			return (InstanceID) instanceId;
		}
		return null;
	}

	@Override
	public void setActionHandler(IHTMLActionHandler actionHandler) {
		setOption(ACTION_HANDLER, actionHandler);
	}

	@Override
	public void setImageHandler(IHTMLImageHandler imageHandler) {
		setOption(IMAGE_HANDLER, imageHandler);
	}

	@Override
	public void setInstanceID(InstanceID iid) {
		setOption(INSTANCE_ID, iid);
	}

	@Override
	public void setFormatter(Map formatters) {
		setOption(OPTION_FORMATTER, formatters);
	}

	@Override
	public Map getFormatter() {
		Object formatter = getOption(OPTION_FORMATTER);
		if (formatter instanceof Map) {
			return (Map) formatter;
		}
		return null;
	}
}
