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

package org.eclipse.birt.report.designer.ui.views;

/**
 * IReportResourceChangeEvent
 */
public interface IReportResourceChangeEvent {

	public static int NewResource = 1;
	public static int LibraySaveChange = 2;
	public static int ImageResourceChange = 4;
	public static int DataDesignSaveChange = 8;
	// public static int LibrayContentChange = 4;

	Object getData();

	Object getSource();

	int getType();
}
