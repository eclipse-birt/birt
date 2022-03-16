/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for table element to store the constants.
 */
public interface ITableItemModel {

	/**
	 * Name of the caption property.
	 */

	String CAPTION_PROP = "caption"; //$NON-NLS-1$

	/**
	 * Name of the caption key property.
	 */

	String CAPTION_KEY_PROP = "captionID"; //$NON-NLS-1$

	/**
	 * Column definitions.
	 */

	int COLUMN_SLOT = 4;

	/**
	 * Name of the summary property
	 */
	String SUMMARY_PROP = "summary"; //$NON-NLS-1$

	/**
	 * Name of the property which indicates if the table is a summary table which
	 * cannot contains any detail rows.
	 */
	String IS_SUMMARY_TABLE_PROP = "isSummaryTable"; //$NON-NLS-1$
}
