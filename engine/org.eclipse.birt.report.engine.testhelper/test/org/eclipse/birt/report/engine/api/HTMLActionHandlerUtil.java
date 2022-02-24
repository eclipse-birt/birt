/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

/**
 * 
 */

public class HTMLActionHandlerUtil {

	public static void appendReportDesignName(HTMLActionHandler handler, StringBuffer buffer, String reportName) {
		handler.appendReportDesignName(buffer, reportName);
	}

	public static void appendFormat(HTMLActionHandler handler, StringBuffer buffer, String format) {
		handler.appendFormat(buffer, format);
	}
}
