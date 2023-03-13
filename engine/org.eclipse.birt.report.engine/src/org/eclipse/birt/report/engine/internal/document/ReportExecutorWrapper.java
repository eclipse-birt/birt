/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class ReportExecutorWrapper implements IReportExecutor {

	public static final int EXECUTOR_VERSION_UNKNOWN = -1;
	public static final int EXECUTOR_VERSION_1 = 1;
	public static final int EXECUTOR_VERSION_2 = 2;
	public static final int EXECUTOR_VERSION_3 = 3;
	public static final int EXECUTOR_VERSION_4 = 4;

	protected IReportExecutor executor;

	public IPageContent createPage(long pageNumber, MasterPageDesign pageDesign) throws BirtException {
		IReportItemExecutor executor = createPageExecutor(pageNumber, pageDesign);
		IPageContent content = (IPageContent) executor.execute();
		DOMBuilderEmitter emitter = new DOMBuilderEmitter(content);
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			IContent childContent = childExecutor.execute();
			if (childContent != null) {
				ContentEmitterUtil.startContent(childContent, emitter);
			}
			executeAll(executor, emitter);
			if (childContent != null) {
				ContentEmitterUtil.endContent(childContent, emitter);
			}
			childExecutor.close();
		}
		executor.close();
		return content;
	}

	protected void executeAll(IReportItemExecutor executor, IContentEmitter emitter) throws BirtException {
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			IContent childContent = childExecutor.execute();
			if (childContent != null) {
				ContentEmitterUtil.startContent(childContent, emitter);
			}
			executeAll(executor, emitter);
			if (childContent != null) {
				ContentEmitterUtil.endContent(childContent, emitter);
			}
			childExecutor.close();
		}

	}

	@Override
	public IReportItemExecutor createPageExecutor(long pageNumber, MasterPageDesign pageDesign) throws BirtException {
		return executor.createPageExecutor(pageNumber, pageDesign);
	}

	@Override
	public IReportContent execute() throws BirtException {
		return executor.execute();
	}

	@Override
	public void close() throws BirtException {
		executor.close();
	}

	@Override
	public IReportItemExecutor getNextChild() throws BirtException {
		return executor.getNextChild();
	}

	@Override
	public boolean hasNextChild() throws BirtException {
		return executor.hasNextChild();
	}

	public static int getVersion(IReportDocument document) throws IOException {
		String birtVersion = document.getVersion();
		int version = EXECUTOR_VERSION_UNKNOWN;
		if (birtVersion != null) {
			if (ReportDocumentConstants.BIRT_ENGINE_VERSION_2_0_0.equals(birtVersion)) {
				version = EXECUTOR_VERSION_2;
			} else if (ReportDocumentConstants.BIRT_ENGINE_VERSION_2_1_0.equals(birtVersion)) {
				version = EXECUTOR_VERSION_3;
			} else {
				version = EXECUTOR_VERSION_4;
			}
		}
		return version;
	}
}
