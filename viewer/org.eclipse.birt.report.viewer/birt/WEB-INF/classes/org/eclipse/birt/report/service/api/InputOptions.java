/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.service.api;

import java.util.HashMap;
import java.util.Map;

public class InputOptions {
	public static final String OPT_LOCALE = "locale"; //$NON-NLS-1$

	public static final String OPT_TIMEZONE = "timeZone"; //$NON-NLS-1$

	public static final String OPT_FORMAT = "format"; //$NON-NLS-1$

	public static final String OPT_EMITTER_ID = "emitterId"; //$NON-NLS-1$

	public static final String OPT_BASE_URL = "baseURL"; //$NON-NLS-1$

	public static final String OPT_REQUEST = "request"; //$NON-NLS-1$

	public static final String OPT_IS_MASTER_PAGE_CONTENT = "isMasterPageContent"; //$NON-NLS-1$

	public static final String OPT_SVG_FLAG = "svgFlag"; //$NON-NLS-1$

	public static final String OPT_RENDER_FORMAT = "format"; //$NON-NLS-1$

	public static final String OPT_IS_DESIGNER = "isDesigner"; //$NON-NLS-1$

	public static final String OPT_SRC_DOCUMENT = "sourceDocument"; //$NON-NLS-1$

	public static final String OPT_REPORT_DESIGN = "reportDesign"; //$NON-NLS-1$

	public static final String OPT_ENABLE_METADATA = "enableMetaData"; //$NON-NLS-1$

	public static final String OPT_DISPLAY_FILTER_ICON = "DisplayFilterIcon"; //$NON-NLS-1$

	public static final String OPT_CONNECTIONHANDLE = "connectionHandle"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_TYPE = "outputFileType"; //$NON-NLS-1$

	public static final String OPT_RTL = "rtl"; //$NON-NLS-1$

	public static final String OPT_SERVLET_PATH = "servletPath"; //$NON-NLS-1$

	public static final String OPT_PAGE_OVERFLOW = "pageOverflow"; //$NON-NLS-1$

	// add options here that matches the common engine run or render options

	private Map<String, Object> options;

	public InputOptions() {
		this.options = new HashMap<>();
	}

	public void setOption(String optName, Object optValue) {
		options.put(optName, optValue);
	}

	public Object getOption(String optName) {
		return options.get(optName);
	}

	public Map<String, Object> getOptions() {
		return options;
	}

}
