/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class EventHandlerManager {

	private Map<DesignElementHandle, Object> eventHandlers;

	public EventHandlerManager() {
		eventHandlers = new HashMap<DesignElementHandle, Object>();
	}

	public Object getInstance(DesignElementHandle element, ExecutionContext context) throws EngineException {
		if (element == null) {
			return null;
		}
		if (element.newHandlerOnEachEvent()) {
			return getInstance(element.getEventHandlerClass(), context);
		}
		if (eventHandlers.containsKey(element)) {
			return eventHandlers.get(element);
		} else {
			Object eventHandler = getInstance(element.getEventHandlerClass(), context);
			eventHandlers.put(element, eventHandler);
			return eventHandler;
		}
	}

	public Object getInstance(ReportItemDesign element, ExecutionContext context) throws EngineException {
		if (element == null)
			return null;
		return getInstance(element.getHandle(), context);
	}

	public static Object getInstance(String className, ExecutionContext context) throws EngineException {
		if (className == null)
			return null;

		Object o = null;
		Class c = null;

		try {
			c = loadClass(className, context);
			o = c.newInstance();
		} catch (IllegalAccessException e) {
			throw new EngineException(MessageConstants.SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR, new Object[] { className },
					e); // $NON-NLS-1$
		} catch (InstantiationException e) {
			throw new EngineException(MessageConstants.SCRIPT_CLASS_INSTANTIATION_ERROR, new Object[] { className }, e); // $NON-NLS-1$
		}
		return o;
	}

	public static Class loadClass(String className, ExecutionContext context) throws EngineException {
		try {
			ClassLoader classLoader = context.getApplicationClassLoader();
			Class c = classLoader.loadClass(className);
			return c;
		} catch (ClassNotFoundException e) {
			throw new EngineException(MessageConstants.SCRIPT_CLASS_NOT_FOUND_ERROR, new Object[] { className }, e); // $NON-NLS-1$
		}
	}

}