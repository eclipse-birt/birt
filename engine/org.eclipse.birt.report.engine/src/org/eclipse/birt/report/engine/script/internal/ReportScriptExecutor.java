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
import java.util.Collection;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.eventadapter.ReportEventAdapter;
import org.eclipse.birt.report.engine.api.script.eventhandler.IReportEventHandler;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.executor.EventHandlerManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.engine.script.internal.instance.PageInstance;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;

public class ReportScriptExecutor extends ScriptExecutor {

	public static void handleOnPrepare(ReportDesignHandle report, ExecutionContext context) {
		try {
			String scriptText = report.getOnPrepare();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(report.getPropertyHandle(IReportDesignModel.ON_PREPARE_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(null, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(report, context);
			if (eh != null) {
				eh.onPrepare(context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, report);
		}
	}

	public static void handleInitialize(ModuleHandle design, ExecutionContext context) {
		try {
			String scriptText = design.getInitialize();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(design.getPropertyHandle(IModuleModel.INITIALIZE_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(null, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(design, context);
			if (eh != null) {
				eh.initialize(context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, design);
		}
	}

	public static void handleBeforeFactory(ReportDesignHandle report, ExecutionContext context) {
		try {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(report);
			IReportDesign reportDesign = new ReportDesign(report);
			String scriptText = report.getBeforeFactory();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(report.getPropertyHandle(IReportDesignModel.BEFORE_FACTORY_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(element, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(report, context);
			if (eh != null) {
				eh.beforeFactory(reportDesign, context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, report);
		}
	}

	public static void handleAfterFactory(ReportDesignHandle report, ExecutionContext context) {
		try {
			String scriptText = report.getAfterFactory();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(report.getPropertyHandle(IReportDesignModel.AFTER_FACTORY_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(null, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(report, context);
			if (eh != null) {
				eh.afterFactory(context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, report);
		}
	}

	public static void handleBeforeRender(ReportDesignHandle report, ExecutionContext context) {
		try {
			String scriptText = report.getBeforeRender();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(report.getPropertyHandle(IReportDesignModel.BEFORE_RENDER_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(null, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(report, context);
			if (eh != null) {
				eh.beforeRender(context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, report);
		}
	}

	public static void handleAfterRender(ReportDesignHandle report, ExecutionContext context) {
		try {
			String scriptText = report.getAfterRender();
			Expression.Script scriptExpr = null;
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(report.getPropertyHandle(IReportDesignModel.AFTER_RENDER_METHOD));
				scriptExpr = Expression.newScript(scriptText);
				scriptExpr.setFileName(id);
			}
			if (handleScript(null, scriptExpr, context).didRun()) {
				return;
			}
			IReportEventHandler eh = (IReportEventHandler) getInstance(report, context);
			if (eh != null) {
				eh.afterRender(context.getReportContext());
			}
		} catch (Exception e) {
			addException(context, e, report);
		}
	}

	public static void handleOnPageEndScript(Report report, ExecutionContext context, PageContent pageContent,
			Collection<IContent> contents) {
		try {
			if (!needOnPageEnd(report)) {
				return;
			}
			IPageInstance pageInstance = new PageInstance(context, pageContent, contents);
			if (handleScript(pageInstance, report.getOnPageEnd(), context).didRun()) {
				return;
			}
			IReportEventHandler eh = getEventHandler(report, context);
			if (eh != null) {
				eh.onPageEnd(pageInstance, context.getReportContext());
			}

		} catch (Exception e) {
			addException(context, e, report.getReportDesign());
		}
	}

	public static void handleOnPageStartScript(Report report, ExecutionContext context, PageContent pageContent,
			Collection<IContent> contents) {
		try {
			if (!needOnPageStart(report)) {
				return;
			}

			IPageInstance pageInstance = new PageInstance(context, pageContent, contents);
			if (handleScript(pageInstance, report.getOnPageStart(), context).didRun()) {
				return;
			}
			IReportEventHandler eh = getEventHandler(report, context);
			if (eh != null) {
				eh.onPageStart(pageInstance, context.getReportContext());
			}

		} catch (Exception e) {
			addException(context, e, report.getReportDesign());
		}
	}

	private static boolean needOnPageStart(Report report) {
		return report.getOnPageStart() != null || report.getJavaClass() != null;
	}

	private static boolean needOnPageEnd(Report report) {
		return report.getOnPageEnd() != null || report.getJavaClass() != null;
	}

	private static IReportEventHandler getEventHandler(Report report, ExecutionContext context) {
		try {
			return (IReportEventHandler) getInstance(report.getJavaClass(), context);
		} catch (ClassCastException e) {
			addClassCastException(context, e, report.getReportDesign(), IReportEventHandler.class);
		} catch (EngineException e) {
			addException(context, e, report.getReportDesign());
		}
		return null;
	}

	public static boolean existPageScript(Report report, ExecutionContext context) {
		String javaClass = report.getJavaClass();
		if (javaClass == null) {
			return false;
		}
		Class<?> clazz = null;
		try {
			clazz = EventHandlerManager.loadClass(javaClass, context);
			return checkPageScriptMethod("onPageStart", (Class<?>) clazz, IReportEventHandler.class,
					IPageInstance.class, ReportEventAdapter.class)
					|| checkPageScriptMethod("onPageEnd", (Class<?>) clazz, IReportEventHandler.class,
							IPageInstance.class, ReportEventAdapter.class);

		} catch (EngineException e) {
			e.printStackTrace();
			return false;
		}
	}

	protected static boolean checkPageScriptMethod(String methodName, Class<?> clazz, Class<?> handler,
			Class<?> instance, Class<?> adapter) {
		if (!handler.isAssignableFrom(clazz)) {
			return false;
		}
		try {
			Method method = clazz.getMethod(methodName, instance, IReportContext.class);
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
