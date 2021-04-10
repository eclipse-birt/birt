/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.element.IList;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IListInstance;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.List;
import org.eclipse.birt.report.engine.script.internal.instance.ListInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.ListHandle;

public class ListScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(ListHandle listHandle, ExecutionContext context) {
		try {
			IList list = new List(listHandle);
			IListEventHandler eh = getEventHandler(listHandle, context);
			if (eh != null)
				eh.onPrepare(list, context.getReportContext());
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(IListContent content, ExecutionContext context) {
		ReportItemDesign listDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(listDesign)) {
			return;
		}
		try {
			IListInstance list = new ListInstance(content, context, RunningState.CREATE);
			if (handleScript(list, listDesign.getOnCreate(), context).didRun())
				return;
			IListEventHandler eh = getEventHandler(listDesign, context);
			if (eh != null)
				eh.onCreate(list, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, listDesign.getHandle());
		}
	}

	public static void handleOnRender(IListContent content, ExecutionContext context) {
		ReportItemDesign listDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(listDesign)) {
			return;
		}
		try {
			IListInstance list = new ListInstance(content, context, RunningState.RENDER);
			if (handleScript(list, listDesign.getOnRender(), context).didRun())
				return;
			IListEventHandler eh = getEventHandler(listDesign, context);
			if (eh != null)
				eh.onRender(list, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, listDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(IListContent content, ExecutionContext context) {
		ReportItemDesign listDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(listDesign, context)) {
			return;
		}
		try {
			IListInstance list = new ListInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(list, listDesign.getOnPageBreak(), context).didRun())
				return;
			IListEventHandler eh = getEventHandler(listDesign, context);
			if (eh != null)
				eh.onPageBreak(list, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, listDesign.getHandle());
		}
	}

	private static IListEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IListEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IListEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static IListEventHandler getEventHandler(ListHandle handle, ExecutionContext context) {
		try {
			return (IListEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IListEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
