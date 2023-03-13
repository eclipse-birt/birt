/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.engine;

import org.eclipse.birt.report.engine.api.IReportEngine;

/**
 * the extension factory is used to create extension for the report engine.
 *
 *
 */
public interface IReportEngineExtensionFactory {

	String getExtensionName();

	IReportEngineExtension createExtension(IReportEngine engine);
}
