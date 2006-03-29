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

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

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
 * 
 */

public class EventHandlerWrapper
{

	public static String getEventHandlerClassName( DesignElementHandle handle )
	{
		if ( handle instanceof CellHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler";
		}
		else if ( handle instanceof DataItemHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler";
		}
		else if ( handle instanceof ScriptDataSourceHandle) 
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSourceEventHandler";
		}
		else if ( handle instanceof DataSourceHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataSourceEventHandler";
		}
		else if ( handle instanceof ScriptDataSetHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IScriptedDataSetEventHandler";
		}
		else if ( handle instanceof DataSetHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDataSetEventHandler";
		}
		else if ( handle instanceof TextDataHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler";
		}
		else if ( handle instanceof GridHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler";
		}
		else if ( handle instanceof ImageHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler";
		}
		else if ( handle instanceof LabelHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler";
		}
		else if ( handle instanceof ListHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler";
		}
		else if ( handle instanceof ListGroupHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler";
		}
		else if ( handle instanceof ReportDesignHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler";
		}
		else if ( handle instanceof RowHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler";
		}
		else if ( handle instanceof TableHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler";
		}
		else if ( handle instanceof TableGroupHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler";
		}
		else if ( handle instanceof TextItemHandle )
		{
			return "org.eclipse.birt.report.engine.api.script.eventhandler.ITextItemEventHandler";
		}
		
		return null;
	}
}
