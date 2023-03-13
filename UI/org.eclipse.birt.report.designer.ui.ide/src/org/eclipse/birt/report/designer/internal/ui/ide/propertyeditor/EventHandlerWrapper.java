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

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

/**
 * EventHandlerWrapper
 */
public class EventHandlerWrapper {

	public static String getEventHandlerClassName(DesignElementHandle handle) {
		if (handle instanceof CellHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof DataItemHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ScriptDataSourceHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSourceEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof DataSourceHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataSourceEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ScriptDataSetHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof DataSetHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof TextDataHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof GridHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ImageHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof LabelHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ListHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ListGroupHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof ReportDesignHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof RowHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof TableHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof TableGroupHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof TextItemHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITextItemEventHandler"; //$NON-NLS-1$
		} else if (handle instanceof AutoTextHandle) {
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler"; //$NON-NLS-1$
		}

		return null;
	}
}
