/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal;

import java.util.Collection;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.eventhandler.IMasterPageEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.script.internal.instance.PageInstance;

public class PageScriptExecutor extends ScriptExecutor {

	protected static boolean needOnPageStart(MasterPageDesign masterPage) {
		return masterPage.getOnPageStart() != null || masterPage.getJavaClass() != null;
	}

	protected static boolean needOnPageEnd(MasterPageDesign masterPage) {
		return masterPage.getOnPageEnd() != null || masterPage.getJavaClass() != null;
	}

	private static IMasterPageEventHandler getEventHandler(MasterPageDesign masterPage, ExecutionContext context) {
		try {
			return (IMasterPageEventHandler) getInstance(masterPage.getJavaClass(), context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, masterPage.getHandle(), IReportEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, masterPage.getHandle());
		}
		return null;
	}

	public static void handleOnPageEndScript(ExecutionContext context, PageContent pageContent,
			Collection<IContent> contents) {

		MasterPageDesign masterPage = (MasterPageDesign) pageContent.getGenerateBy();
		if (masterPage != null) {
			try {
				if (!needOnPageEnd(masterPage)) {
					return;
				}
				IPageInstance pageInstance = new PageInstance(context, pageContent, contents);
				if (handleScript(pageInstance, masterPage.getOnPageEnd(), context).didRun()) {
					return;
				}
				IMasterPageEventHandler eh = getEventHandler(masterPage, context);
				if (eh != null) {
					eh.onPageEnd(pageInstance, context.getReportContext());
				}
			} catch (Exception e) {
				addException(context, e, masterPage.getHandle());
			}

		}

	}

	public static void handleOnPageStartScript(ExecutionContext context, PageContent pageContent,
			Collection<IContent> contents) {
		MasterPageDesign masterPage = (MasterPageDesign) pageContent.getGenerateBy();
		if (masterPage != null) {
			try {
				if (!needOnPageStart(masterPage)) {
					return;
				}
				IPageInstance pageInstance = new PageInstance(context, pageContent, contents);
				if (handleScript(pageInstance, masterPage.getOnPageStart(), context).didRun()) {
					return;
				}
				IMasterPageEventHandler eh = getEventHandler(masterPage, context);
				if (eh != null) {
					eh.onPageStart(pageInstance, context.getReportContext());
				}
			} catch (Exception e) {
				addException(context, e, masterPage.getHandle());
			}

		}
	}
}
