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

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public interface IReportItemGenerationInfo {

	public ExtendedItemHandle getModelObject();

	public ClassLoader getApplicationClassLoader();

	public IReportContext getReportContext();

	public IDataQueryDefinition[] getReportQueries();

	public IContent getExtendedItemContent();
}
