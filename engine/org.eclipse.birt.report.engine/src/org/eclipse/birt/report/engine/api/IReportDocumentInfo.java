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

package org.eclipse.birt.report.engine.api;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * the interface used to access the traisent informations of a report document.
 *
 */
public interface IReportDocumentInfo {
	List getErrors();

	/**
	 * Open a report document.
	 *
	 * @return ReportDocument object
	 * @throws BirtException
	 */
	IReportDocument openReportDocument() throws BirtException;

	/**
	 * Check if the report document is completely read.
	 *
	 * @return true if document information is completely read.
	 */
	boolean isComplete();
}
