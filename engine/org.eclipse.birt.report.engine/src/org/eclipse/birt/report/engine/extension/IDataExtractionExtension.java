/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * An extension-point for data extraction.
 * 
 * It's recommended to extend DataExtractionExtensionBase intead of implement
 * directly.
 */
public interface IDataExtractionExtension {

	void initialize(IReportContext context, IDataExtractionOption option) throws BirtException;

	/**
	 * Outputs data defined in the result set.
	 * 
	 * @param results the result set.
	 */
	void output(IExtractionResults results) throws BirtException;

	/**
	 * Releases all resources allocated in the extension.
	 */
	void release();

}
