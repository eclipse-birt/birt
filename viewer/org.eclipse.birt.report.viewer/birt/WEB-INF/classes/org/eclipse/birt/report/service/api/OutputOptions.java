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

public class OutputOptions {
	public static final String OPT_REPORT_GENERATION_COMPLETED = "completed"; //$NON-NLS-1$

	public static final String OPT_CONNECTIONHANDLE = "connectionhandle"; //$NON-NLS-1$

	private Map options;

	public OutputOptions() {
		this.options = new HashMap();
	}

	public void setOption(String optName, Object optValue) {
		options.put(optName, optValue);
	}

	public Object getOption(String optName) {
		return options.get(optName);
	}

	public Map getOptions() {
		return options;
	}

}
