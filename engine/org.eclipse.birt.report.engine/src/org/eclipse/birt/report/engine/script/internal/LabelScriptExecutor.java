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
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.Label;
import org.eclipse.birt.report.engine.script.internal.instance.LabelInstance;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.model.api.LabelHandle;

public class LabelScriptExecutor extends ScriptExecutor {
	public static void handleOnPrepare(LabelHandle labelHandle, ExecutionContext context) {
		try {
			ILabel label = new Label(labelHandle);
			ILabelEventHandler eh = getEventHandler(labelHandle, context);
			if (eh != null)
				eh.onPrepare(label, context.getReportContext());
		} catch (Exception e) {
			addException(context, e);
		}
	}

	public static void handleOnCreate(ILabelContent content, ExecutionContext context) {
		ReportItemDesign labelDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnCreate(labelDesign)) {
			return;
		}
		try {
			ILabelInstance label = new LabelInstance(content, context, RunningState.CREATE);
			if (handleScript(label, labelDesign.getOnCreate(), context).didRun())
				return;
			ILabelEventHandler eh = getEventHandler(labelDesign, context);
			if (eh != null)
				eh.onCreate(label, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, labelDesign.getHandle());
		}
	}

	public static void handleOnRender(ILabelContent content, ExecutionContext context) {
		ReportItemDesign labelDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnRender(labelDesign)) {
			return;
		}
		try {
			ILabelInstance label = new LabelInstance(content, context, RunningState.RENDER);
			if (handleScript(label, labelDesign.getOnRender(), context).didRun())
				return;
			ILabelEventHandler eh = getEventHandler(labelDesign, context);
			if (eh != null)
				eh.onRender(label, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, labelDesign.getHandle());
		}
	}

	public static void handleOnPageBreak(ILabelContent content, ExecutionContext context) {
		ReportItemDesign labelDesign = (ReportItemDesign) content.getGenerateBy();
		if (!needOnPageBreak(labelDesign, context)) {
			return;
		}
		try {
			ILabelInstance label = new LabelInstance(content, context, RunningState.PAGEBREAK);
			if (handleScript(label, labelDesign.getOnPageBreak(), context).didRun())
				return;

			ILabelEventHandler eh = getEventHandler(labelDesign, context);
			if (eh != null)
				eh.onPageBreak(label, context.getReportContext());
		} catch (Exception e) {
			addException(context, e, labelDesign.getHandle());
		}
	}

	private static ILabelEventHandler getEventHandler(ReportItemDesign design, ExecutionContext context) {
		try {
			return (ILabelEventHandler) getInstance(design, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, design.getHandle(), ILabelEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, design.getHandle());
		}
		return null;
	}

	private static ILabelEventHandler getEventHandler(LabelHandle handle, ExecutionContext context) {
		try {
			return (ILabelEventHandler) getInstance(handle, context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, handle, ILabelEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, handle);
		}
		return null;
	}
}
