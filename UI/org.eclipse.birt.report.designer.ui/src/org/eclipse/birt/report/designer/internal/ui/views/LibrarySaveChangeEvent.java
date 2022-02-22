/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views;

/**
 *
 */

public class LibrarySaveChangeEvent extends ReportResourceChangeEvent {
	private String fileName;

	/**
	 * @param source
	 * @param data
	 * @param type
	 * @param fileName
	 */
	public LibrarySaveChangeEvent(Object source, Object data, int type, String fileName) {
		super(source, data, type);
		this.fileName = fileName;
	}

	/**
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

}
