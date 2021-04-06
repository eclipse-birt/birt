/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;

public class ReportExecutorUtil {

	public static void execute(IReportExecutor executor, IContentEmitter emitter) throws BirtException {
		IReportContent report = executor.execute();
		emitter.start(report);
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				execute(childExecutor, emitter);
			}
		}

		emitter.end(report);
	}

	public static void execute(IReportItemExecutor executor, IContentEmitter emitter) throws BirtException {
		IContent content = executor.execute();
		if (content != null) {
			ContentEmitterUtil.startContent(content, emitter);
		}
		executeAll(executor, emitter);
		if (content != null) {
			ContentEmitterUtil.endContent(content, emitter);
		}
		executor.close();
	}

	public static IPageContent executeMasterPage(IReportExecutor executor, long pageNumber, MasterPageDesign pageDesign)
			throws BirtException {
		IReportItemExecutor pageExecutor = executor.createPageExecutor(pageNumber, pageDesign);
		if (pageExecutor != null) {
			IPageContent pageContent = (IPageContent) pageExecutor.execute();
			if (pageContent != null) {
				DOMBuilderEmitter emitter = new DOMBuilderEmitter(pageContent);
				executeAll(pageExecutor, emitter);
			}
			pageExecutor.close();
			return pageContent;
		}
		return null;
	}

	static protected void executeAll(IReportItemExecutor executor, IContentEmitter emitter) throws BirtException {
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				IContent childContent = childExecutor.execute();
				if (childContent != null) {
					ContentEmitterUtil.startContent(childContent, emitter);
				}
				executeAll(childExecutor, emitter);
				if (childContent != null) {
					ContentEmitterUtil.endContent(childContent, emitter);
				}
				childExecutor.close();
			}
		}
	}

}
