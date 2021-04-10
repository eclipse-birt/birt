/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
