/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.element.IListGroup;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.ListGroup;
import org.eclipse.birt.report.engine.script.internal.instance.ReportElementInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.ListGroupHandle;

public class ListGroupScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(ListGroupHandle groupHandle, ExecutionContext context) {
		try {
			IListGroup group = new ListGroup(groupHandle);
			IListGroupEventHandler eh = getEventHandler(groupHandle, context);
			if (eh != null) {
				eh.onPrepare(group, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(IListGroupContent content, ExecutionContext context) {
		ReportItemDesign listGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(listGroupDesign)) {
			return;
		}
		try {
			ReportElementInstance list = new ReportElementInstance(content, context, RunningState.CREATE);
			if (handleScript(list, listGroupDesign.getOnCreate(), context).didRun()) {
				return;
			}
			IListGroupEventHandler eh = getEventHandler(listGroupDesign, context);
			if (eh != null) {
				eh.onCreate(list, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, listGroupDesign.getHandle());
		}
	}

	public static void handleOnRender(IListGroupContent content, ExecutionContext context) {
		ReportItemDesign listGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(listGroupDesign)) {
			return;
		}
		try {
			ReportElementInstance list = new ReportElementInstance(content, context, RunningState.RENDER);
			if (handleScript(list, listGroupDesign.getOnRender(), context).didRun()) {
				return;
			}
			IListGroupEventHandler eh = getEventHandler(listGroupDesign, context);
			if (eh != null) {
				eh.onRender(list, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, listGroupDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(IListGroupContent content, ExecutionContext context) {
		ReportItemDesign listGroupDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(listGroupDesign, context)) {
			return;
		}
		try {
			ReportElementInstance list = new ReportElementInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(list, listGroupDesign.getOnPageBreak(), context).didRun()) {
				return;
			}
			IListGroupEventHandler eh = getEventHandler(listGroupDesign, context);
			if (eh != null) {
				eh.onPageBreak(list, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, listGroupDesign.getHandle());
		}
	}

	private static IListGroupEventHandler getEventHandler(ListGroupHandle handle, ExecutionContext context) {
		try {
			return (IListGroupEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, IListGroupEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}

	private static IListGroupEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (IListGroupEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), IListGroupEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

}
