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

	public ClassLoader getApplicationClassLoader() {
		return loader;
	}

	public void setApplicationClassLoader(ClassLoader loader) {
		this.loader = loader;
	}

	public ExtendedItemHandle getModelObject() {
		return modelHandle;
	}

	public void setModelObject(ExtendedItemHandle modelHandle) {
		this.modelHandle = modelHandle;
	}

	public IReportContext getReportContext() {
		return context;
	}

	public void setReportContext(IReportContext context) {
		this.context = context;
	}

	public IDataQueryDefinition[] getReportQueries() {
		return queries;
	}

	public void setReportQueries(IDataQueryDefinition[] queries) {
		this.queries = queries;
	}

	public IContent getExtendedItemContent() {
		return content;
	}

	public void setExtendedItemContent(IContent content) {
		this.content = content;
	}
}
