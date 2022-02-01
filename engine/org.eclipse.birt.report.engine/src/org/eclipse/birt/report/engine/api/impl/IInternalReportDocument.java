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

package org.eclipse.birt.report.engine.api.impl;

import java.util.Map;

import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.extension.engine.IReportDocumentExtension;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public interface IInternalReportDocument extends IReportDocument {

	/**
	 * Gets the class loader used by this report document.
	 */
	ClassLoader getClassLoader();

	/**
	 * get engine internal representation of report design
	 */
	Report getReportIR(ReportDesignHandle designHandle);

	IReportRunnable getOnPreparedRunnable();

	IReportDocumentExtension getDocumentExtension(String extension) throws EngineException;

	ITOCReader getTOCReader(ClassLoader loader) throws EngineException;

	Map<String, ParameterAttribute> loadParameters(ClassLoader loader) throws EngineException;

	Map<String, Object> loadVariables(ClassLoader loader) throws EngineException;
}
