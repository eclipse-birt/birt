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

package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportEventHandler;
import org.eclipse.birt.report.engine.extension.internal.OnCreateEvent;
import org.eclipse.birt.report.engine.extension.internal.OnPrepareEvent;
import org.eclipse.birt.report.engine.extension.internal.OnRenderEvent;
import org.eclipse.birt.report.engine.extension.internal.ReportEventContext;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class ExtendedItemScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(ExtendedItemHandle handle, ExecutionContext context) {
		IReportEventHandler eventHandler = context.getExtendedItemManager().createEventHandler(handle);
		if (eventHandler != null) {
			try {
				OnPrepareEvent event = new OnPrepareEvent(new ReportEventContext(context), handle);
				eventHandler.handle(event);
			} catch (Exception e) {
				addException(context, e, handle);
			}
		}
	}

	public static void handleOnCreate(ExtendedItemDesign design, IContent content, ExecutionContext context) {
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle();
		IReportEventHandler eventHandler = context.getExtendedItemManager().createEventHandler(handle);
		if (eventHandler != null) {
			try {
				OnCreateEvent event = new OnCreateEvent(new ReportEventContext(context), handle, content);
				eventHandler.handle(event);
			} catch (Exception e) {
				addException(context, e, handle);
			}
		}
	}

	public static void handleOnRender(ExtendedItemDesign design, IContent content, ExecutionContext context) {
		ExtendedItemHandle handle = (ExtendedItemHandle) design.getHandle();
		IReportEventHandler eventHandler = context.getExtendedItemManager().createEventHandler(handle);
		if (eventHandler != null) {
			try {
				OnRenderEvent event = new OnRenderEvent(new ReportEventContext(context), handle, content);
				eventHandler.handle(event);
			} catch (Exception e) {
				addException(context, e, handle);
			}
		}
	}
}
