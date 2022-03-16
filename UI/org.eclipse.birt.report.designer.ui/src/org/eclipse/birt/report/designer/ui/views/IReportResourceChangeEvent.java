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

package org.eclipse.birt.report.designer.ui.views;

/**
 * IReportResourceChangeEvent
 */
public interface IReportResourceChangeEvent {

	int NewResource = 1;
	int LibraySaveChange = 2;
	int ImageResourceChange = 4;
	int DataDesignSaveChange = 8;
	// public static int LibrayContentChange = 4;

	Object getData();

	Object getSource();

	int getType();
}
