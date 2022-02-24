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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemGenerationInfo;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class ReportItemGenerationInfo implements IReportItemGenerationInfo {

	private ExtendedItemHandle modelHandle = null;
	private ClassLoader loader = null;
	private IReportContext context = null;
	private IDataQueryDefinition[] queries = null;
	private IContent content = null;

	@Override
	public ClassLoader getApplicationClassLoader() {
		return loader;
	}

	public void setApplicationClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	@Override
	public ExtendedItemHandle getModelObject() {
		return modelHandle;
	}

	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	@Override
	public IReportContext getReportContext() {
		return context;
	}

	public void setReportContext(IReportContext context) {
		this.context = context;
	}

	@Override
	public IDataQueryDefinition[] getReportQueries() {
		return queries;
	}

	public void setReportQueries(IDataQueryDefinition[] queries) {
		this.queries = queries;
	}

	@Override
	public IContent getExtendedItemContent() {
		return content;
	}

	public void setExtendedItemContent(IContent content) {
		this.content = content;
	}
}
