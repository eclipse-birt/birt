/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.extension.engine.IDataExtension;
import org.eclipse.birt.report.engine.extension.engine.IDocumentExtension;
import org.eclipse.birt.report.engine.extension.engine.IGenerateExtension;
import org.eclipse.birt.report.engine.extension.engine.IRenderExtension;
import org.eclipse.birt.report.engine.extension.engine.IReportEngineExtension;
import org.eclipse.birt.report.engine.extension.internal.RenderContext;
import org.eclipse.birt.report.engine.extension.internal.RunContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

public class EngineExtensionManager {

	ExecutionContext context;
	HashMap<String, IRenderExtension> renderExtensions = new HashMap<String, IRenderExtension>();
	HashMap<String, IGenerateExtension> generateExtensions = new HashMap<String, IGenerateExtension>();
	HashMap<String, IDocumentExtension> documentExtensions = new HashMap<String, IDocumentExtension>();
	HashMap<String, IDataExtension> dataExtensions = new HashMap<String, IDataExtension>();

	public EngineExtensionManager(ExecutionContext context) {
		this.context = context;
	}

	protected IReportEngineExtension getEngineExtension(String name) throws EngineException {
		ReportEngine engine = context.getEngine();
		if (engine != null) {
			IReportEngineExtension ext = engine.getEngineExtension(name);
			if (ext == null) {
				throw new EngineException(MessageConstants.UNSUPPORTED_ENGINE_EXTENSION, new Object[] { name });
			}
			return ext;
		}
		return null;
	}

	public IRenderExtension getRenderExtension(String name) throws EngineException {
		if (renderExtensions.containsKey(name)) {
			return renderExtensions.get(name);
		}
		IReportEngineExtension extension = getEngineExtension(name);
		if (extension != null) {
			IRenderExtension renderExtension = extension.createRenderExtension(new RenderContext(context));
			renderExtensions.put(name, renderExtension);
			return renderExtension;
		}
		return null;
	}

	public IGenerateExtension getGenerateExtension(String name) throws EngineException {
		if (generateExtensions.containsKey(name)) {
			return generateExtensions.get(name);
		}
		IReportEngineExtension extension = getEngineExtension(name);
		if (extension != null) {
			IGenerateExtension generateExtension = extension.createGenerateExtension(new RunContext(context));
			generateExtensions.put(name, generateExtension);
			return generateExtension;
		}
		return null;
	}

	public IDocumentExtension getDocumentExtension(String name) throws EngineException {
		if (documentExtensions.containsKey(name)) {
			return documentExtensions.get(name);
		}
		IReportEngineExtension extension = getEngineExtension(name);
		if (extension != null) {
			IDocumentExtension documentExtension = extension.createDocumentExtension(new RunContext(context));
			documentExtensions.put(name, documentExtension);
			return documentExtension;
		}
		return null;

	}

	public IDataExtension getDataExtension(String name) throws EngineException {
		if (dataExtensions.containsKey(name)) {
			return dataExtensions.get(name);
		}
		IReportEngineExtension extension = getEngineExtension(name);
		if (extension != null) {
			IDataExtension dataExtension = extension.createDataExtension(new RunContext(context));
			dataExtensions.put(name, dataExtension);
			return dataExtension;
		}
		return null;
	}

	public void close() {
		for (IGenerateExtension generateExt : generateExtensions.values()) {
			if (generateExt != null) {
				generateExt.close();
			}
		}
		for (IRenderExtension renderExt : renderExtensions.values()) {
			if (renderExt != null) {
				renderExt.close();
			}
		}
		for (IDocumentExtension documentExt : documentExtensions.values()) {
			if (documentExt != null) {
				documentExt.close();
			}
		}

		for (IDataExtension dataExt : dataExtensions.values()) {
			if (dataExt != null) {
				dataExt.close();
			}
		}
		generateExtensions.clear();
		renderExtensions.clear();
		documentExtensions.clear();
	}
}
