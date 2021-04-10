/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
