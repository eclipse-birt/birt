/*******************************************************************************
 * Copyright (c) 2005, 2009 Actuate Corporation.
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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.eventadapter.AutoTextEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.CellEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.DataItemEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.DynamicTextEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.GridEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.ImageEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.LabelEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.ListEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.ListGroupEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.RowEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.TableEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.TableGroupEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventadapter.TextItemEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventhandler.IAutoTextEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.ICellEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDataItemEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IDynamicTextEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IGridEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IImageEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.ILabelEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IListGroupEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.IRowEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITableGroupEventHandler;
import org.eclipse.birt.report.engine.api.script.eventhandler.ITextItemEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IAutoTextInstance;
import org.eclipse.birt.report.engine.api.script.instance.ICellInstance;
import org.eclipse.birt.report.engine.api.script.instance.IDataItemInstance;
import org.eclipse.birt.report.engine.api.script.instance.IDynamicTextInstance;
import org.eclipse.birt.report.engine.api.script.instance.IGridInstance;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.api.script.instance.ILabelInstance;
import org.eclipse.birt.report.engine.api.script.instance.IListInstance;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.api.script.instance.IRowInstance;
import org.eclipse.birt.report.engine.api.script.instance.ITableInstance;
import org.eclipse.birt.report.engine.api.script.instance.ITextItemInstance;
import org.eclipse.birt.report.engine.executor.EventHandlerManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * A class used to create script event handlers
 */
public class ScriptExecutor {

	public static final String PROPERTYSEPARATOR = EngineConstants.PROPERTYSEPARATOR;

	public static final String WEBAPP_CLASSPATH_KEY = EngineConstants.WEBAPP_CLASSPATH_KEY;

	public static final String WORKSPACE_CLASSPATH_KEY = EngineConstants.WORKSPACE_CLASSPATH_KEY;

	public static final String PROJECT_CLASSPATH_KEY = EngineConstants.PROJECT_CLASSPATH_KEY;

	protected static Logger log = Logger.getLogger(ScriptExecutor.class.getName());

	private static ScriptChecker scriptChecker = new ScriptChecker();

	protected static ScriptStatus handleScript(Object scope, Expression expr, ExecutionContext context)
			throws BirtException {
		return handleScriptInternal(scope, expr, context);
	}

	private static ScriptStatus handleScriptInternal(Object scope, Expression expr, ExecutionContext context)
			throws BirtException {
		if (expr != null) {
			try {
				if (scope != null)
					context.newScope(scope);
				Object result = null;
				result = context.evaluate(expr);
				return new ScriptStatus(true, result);
			} finally {
				if (scope != null)
					context.exitScope();
			}
		}
		return ScriptStatus.NO_RUN;
	}

	protected static boolean needOnCreate(ReportItemDesign design) {
		if (design == null) {
			return false;
		}
		return design.getOnCreate() != null || design.getJavaClass() != null;
	}

	protected static boolean needOnRender(ReportItemDesign design) {
		if (design == null) {
			return false;
		}
		return design.getOnRender() != null || design.getJavaClass() != null;
	}

	public static boolean needOnPageBreak(ReportItemDesign design, ExecutionContext context) {
		if (design == null) {
			return false;
		}
		if (design instanceof ExtendedItemDesign) {
			return false;
		}
		if (design.getOnPageBreak() != null) {
			return true;
		}
		String javaClass = design.getJavaClass();
		if (javaClass == null) {
			return false;
		}
		Object extensionData = design.getExtensionData();
		if (extensionData != null) {
			return (Boolean) extensionData;
		}
		// design.getJavaClass() must not be null here.
		EventHandlerManager eventHandlerManager = context.getEventHandlerManager();
		Class<?> clazz = null;
		boolean result = false;
		try {
			clazz = eventHandlerManager.loadClass(javaClass, context);
			result = (Boolean) design.accept(scriptChecker, clazz);
		} catch (EngineException e) {
			e.printStackTrace();
		}
		design.setExtensionData(result);
		return result;
	}

	protected static Object getInstance(DesignElementHandle handle, ExecutionContext context) throws EngineException {
		EventHandlerManager eventHandlerManager = context.getEventHandlerManager();
		return eventHandlerManager.getInstance(handle, context);
	}

	protected static Object getInstance(String className, ExecutionContext context) throws EngineException {
		EventHandlerManager eventHandlerManager = context.getEventHandlerManager();
		return eventHandlerManager.getInstance(className, context);
	}

	protected static Object getInstance(ReportItemDesign design, ExecutionContext context) throws EngineException {
		EventHandlerManager eventHandlerManager = context.getEventHandlerManager();
		return eventHandlerManager.getInstance(design, context);
	}

	/*
	 * protected static void addClassCastException( ExecutionContext context,
	 * ClassCastException e, String className, Class requiredInterface ) {
	 * addException( context, e, MessageConstants.SCRIPT_CLASS_CAST_ERROR, new
	 * Object[] { className, requiredInterface.getName( ) } ); }
	 */
	protected static void addClassCastException(ExecutionContext context, Exception e, DesignElementHandle handle,
			Class requiredInterface) {
		EngineException ex = new EngineException(MessageConstants.SCRIPT_CLASS_CAST_ERROR,
				new Object[] { handle.getEventHandlerClass(), requiredInterface.getName() }, e);

		log.log(Level.WARNING, e.getMessage(), e);
		if (context == null)
			return;

		context.addException(handle, ex);
	}

	protected static void addException(ExecutionContext context, Exception e) {
		addException(context, e, null);
	}

	protected static void addException(ExecutionContext context, Exception e, DesignElementHandle handle) {
		EngineException eex = null;
		if (e instanceof EngineException)
			eex = (EngineException) e;
		else if (e instanceof BirtException) {
			eex = new EngineException((BirtException) e);
		} else {
			eex = new EngineException(MessageConstants.UNHANDLED_SCRIPT_ERROR, e);
		}

		log.log(Level.WARNING, eex.getMessage(), eex);
		if (context == null)
			return;

		if (handle == null)
			context.addException(eex);
		else
			context.addException(handle, eex);
	}

	protected static class ScriptStatus {
		private boolean didRun;

		private Object result;

		public static final ScriptStatus NO_RUN = new ScriptStatus(false, null);

		public ScriptStatus(boolean didRun, Object result) {
			this.didRun = didRun;
			this.result = result;
		}

		public boolean didRun() {
			return didRun;
		}

		public Object result() {
			return result;
		}
	}

	private static class ScriptChecker extends DefaultReportItemVisitorImpl {

		@Override
		public Object visitListItem(ListItemDesign list, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IListEventHandler.class, IListInstance.class,
					ListEventAdapter.class);
		}

		@Override
		public Object visitTextItem(TextItemDesign text, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ITextItemEventHandler.class, ITextItemInstance.class,
					TextItemEventAdapter.class);
		}

		@Override
		public Object visitLabelItem(LabelItemDesign label, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ILabelEventHandler.class, ILabelInstance.class,
					LabelEventAdapter.class);
		}

		@Override
		public Object visitAutoTextItem(AutoTextItemDesign autoText, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IAutoTextEventHandler.class, IAutoTextInstance.class,
					AutoTextEventAdapter.class);
		}

		@Override
		public Object visitDataItem(DataItemDesign data, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IDataItemEventHandler.class, IDataItemInstance.class,
					DataItemEventAdapter.class);
		}

		@Override
		public Object visitDynamicTextItem(DynamicTextItemDesign multiLine, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IDynamicTextEventHandler.class, IDynamicTextInstance.class,
					DynamicTextEventAdapter.class);
		}

		@Override
		public Object visitGridItem(GridItemDesign grid, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IGridEventHandler.class, IGridInstance.class,
					GridEventAdapter.class);
		}

		@Override
		public Object visitTableItem(TableItemDesign table, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ITableEventHandler.class, ITableInstance.class,
					TableEventAdapter.class);
		}

		@Override
		public Object visitImageItem(ImageItemDesign image, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IImageEventHandler.class, IImageInstance.class,
					ImageEventAdapter.class);
		}

		@Override
		public Object visitRow(RowDesign row, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IRowEventHandler.class, IRowInstance.class,
					RowEventAdapter.class);
		}

		@Override
		public Object visitCell(CellDesign cell, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ICellEventHandler.class, ICellInstance.class,
					CellEventAdapter.class);
		}

		@Override
		public Object visitTemplate(TemplateDesign template, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ITextItemEventHandler.class, ITextItemInstance.class,
					TextItemEventAdapter.class);
		}

		@Override
		public Object visitListGroup(ListGroupDesign group, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, IListGroupEventHandler.class, IReportElementInstance.class,
					ListGroupEventAdapter.class);
		}

		@Override
		public Object visitTableGroup(TableGroupDesign group, Object value) {
			return checkOnPageBreakMethod((Class<?>) value, ITableGroupEventHandler.class, IReportElementInstance.class,
					TableGroupEventAdapter.class);
		}

		public Object visitReportItem(ReportItemDesign item, Object value) {
			return false;
		}

		public Object checkOnPageBreakMethod(Class<?> clazz, Class<?> handler, Class<?> instance, Class<?> adapter) {
			if (!handler.isAssignableFrom(clazz)) {
				return false;
			}
			try {
				Method method = clazz.getMethod("onPageBreak", instance, IReportContext.class);
				return method.getDeclaringClass() != adapter;
			} catch (SecurityException e) {
				// If checking the method is forbidden by security policy, the
				// method has to be executed.
				return true;
			} catch (NoSuchMethodException e) {
				return false;
			}
		}

	}
}
