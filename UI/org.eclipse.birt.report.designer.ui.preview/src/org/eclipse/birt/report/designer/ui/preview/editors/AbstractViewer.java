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

package org.eclipse.birt.report.designer.ui.preview.editors;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.ui.preview.extension.IViewer;
import org.eclipse.birt.report.designer.ui.preview.parameter.ParameterFactory;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;

/**
 * Represents the viewer
 */
public abstract class AbstractViewer implements IViewer {

	private IReportEngine engine;

	private static String RPTDOC_SUFFIX = "rptdocument"; //$NON-NLS-1$

	public void init() {
		EngineConfig engineConfig = getEngineConfig();
		if (engineConfig == null) {
			engineConfig = new EngineConfig();
		}
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		this.engine = factory.createReportEngine(engineConfig);
		engine.changeLogLevel(Level.WARNING);
	}

	@Override
	public void close() {
		this.engine.destroy();
	}

	protected abstract IRenderOption getRenderOption();

	protected abstract EngineConfig getEngineConfig();

	protected String createReportDocument(String reportDesignFile, String outputFolder, Map parameters)
			throws IOException, EngineException {
		File designFile = new File(reportDesignFile);

		String reportDocumentFile = outputFolder + File.separator + designFile.getName() + "." //$NON-NLS-1$
				+ RPTDOC_SUFFIX;

		IDocArchiveWriter archive = new FileArchiveWriter(reportDocumentFile);
		IReportRunnable report = engine.openReportDesign(reportDesignFile);
		IRunTask runTask = engine.createRunTask(report);
		try {
			if (parameters != null) {
				runTask.setParameterValues(parameters);
			}
			Map appContext = new HashMap();
			if (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter() != null) {
				appContext = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter().getAppContext();
			}
			runTask.setAppContext(appContext);
			runTask.run(archive);
		} catch (EngineException e) {
			throw e;
		} finally {
			runTask.close();
			report = null;
			runTask = null;
		}
		return reportDocumentFile;
	}

	protected IReportDocument openReportDocument(String reportDocumentFile) throws EngineException {
		return engine.openReportDocument(reportDocumentFile);
	}

	protected long createReportOutput(String reportDocumentFile, String outputFile, long pageNumber)
			throws EngineException, IOException {
		IReportDocument document = engine.openReportDocument(reportDocumentFile);
		long pageCount = document.getPageCount();
		IRenderTask task = engine.createRenderTask(document);

		IRenderOption renderOption = getRenderOption();
		renderOption.setOutputFileName(outputFile);

		try {
			task.setRenderOption(renderOption);
			task.setPageNumber(pageNumber);
			Map appContext = Collections.EMPTY_MAP;
			if (ExtendedDataModelUIAdapterHelper.getInstance().getAdapter() != null) {
				appContext = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter().getAppContext();
			}
			task.setAppContext(appContext);
			task.render();
		} catch (EngineException e) {
			throw e;
		} finally {
			task.close();
			task = null;
			document.close();
			document = null;
		}
		return pageCount;
	}

	protected List getInputParameters(String reportDesignFile) {
		try {
			IGetParameterDefinitionTask task = engine
					.createGetParameterDefinitionTask(engine.openReportDesign(reportDesignFile));
			ParameterFactory factory = new ParameterFactory(task);
			List parameters = factory.getRootChildren();
			task.close();
			task = null;
			return parameters;
		} catch (EngineException e) {
		}
		return null;
	}

}
