/*******************************************************************************
 * Copyright (c) 2005,2009 Actuate Corporation.
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

import java.io.Serializable;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.text.MessageFormat;
import com.ibm.icu.util.TimeZone;

/**
 * Implementation of the IReportContext interface
 */
public class ReportContextImpl implements IReportContext {

	protected ExecutionContext context;

	public ReportContextImpl(ExecutionContext context) {
		this.context = context;
	}

	@Override
	public IReportRunnable getReportRunnable() {
		return context.getRunnable();
	}

	@Override
	public Map getAppContext() {
		return context.getAppContext();
	}

	public void setAppContext(Map appContext) {
		context.setAppContext(appContext);
	}

	@Override
	public void setGlobalVariable(String name, Object obj) {
		context.registerBean(name, obj);
	}

	@Override
	public void deleteGlobalVariable(String name) {
		context.registerBean(name, null);
	}

	@Override
	public Object getGlobalVariable(String name) {
		return context.getBeans().get(name);
	}

	@Override
	public void setPersistentGlobalVariable(String name, Serializable obj) {
		context.registerGlobalBean(name, obj);
	}

	@Override
	public void deletePersistentGlobalVariable(String name) {
		context.unregisterGlobalBean(name);
	}

	@Override
	public Object getPersistentGlobalVariable(String name) {
		return context.getGlobalBeans().get(name);
	}

	public void setRegisteredPersistantObjects(Map persistantMap) {
		context.registerBeans(persistantMap);
	}

	@Override
	public Object getParameterValue(String name) {
		return context.getParameterValue(name);
	}

	@Override
	public void setParameterValue(String name, Object value) {
		context.setParameterValue(name, value);
	}

	@Override
	public Object getPageVariable(String name) {
		return context.getPageVariable(name);
	}

	@Override
	public void setPageVariable(String name, Object value) {
		context.setPageVariable(name, value);
	}

	@Override
	public Locale getLocale() {
		return context.getLocale();
	}

	@Override
	public TimeZone getTimeZone() {
		return context.getTimeZone();
	}

	@Override
	public String getOutputFormat() {
		return context.getOutputFormat();
	}

	@Override
	public IRenderOption getRenderOption() {
		return context.getRenderOption();
	}

	@Override
	public Object getHttpServletRequest() {
		return getAppContext().get(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST);
	}

	@Override
	public String getMessage(String key) {
		return context.getDesign().getMessage(key);
	}

	@Override
	public String getMessage(String key, Locale locale) {
		return context.getDesign().getMessage(key, locale);
	}

	@Override
	public String getMessage(String key, Object[] params) {
		String msg = context.getDesign().getMessage(key);
		if (msg == null) {
			return "";
		}
		return MessageFormat.format(msg, params);
	}

	@Override
	public String getMessage(String key, Locale locale, Object[] params) {
		String msg = context.getDesign().getMessage(key, locale);
		if (msg == null) {
			return "";
		}
		MessageFormat formatter = new MessageFormat(msg, locale);
		return formatter.format(params, new StringBuffer(), null).toString();
	}

	@Override
	public Object getParameterDisplayText(String name) {
		return context.getParameterDisplayText(name);
	}

	@Override
	public void setParameterDisplayText(String name, String displayText) {
		context.setParameterDisplayText(name, displayText);
	}

	@Override
	public int getTaskType() {
		IEngineTask task = context.getEngineTask();
		if (task != null) {
			return task.getTaskType();
		}
		return IEngineTask.TASK_UNKNOWN;
	}

	@Override
	public ReportDesignHandle getDesignHandle() {
		return (ReportDesignHandle) getReportRunnable().getDesignHandle();
	}

	@Override
	public URL getResource(String resourceName) {
		return context.getResource(resourceName);
	}

	@Override
	public String getResourceRenderURL(String resourceName) {
		IRenderOption option = context.getRenderOption();
		if (option != null) {
			IHTMLImageHandler imageHandler = option.getImageHandler();
			if (imageHandler != null) {
				URL resourceUrl = context.getResource(resourceName);
				if (resourceUrl != null) {
					Image image = new Image(resourceUrl.toExternalForm());
					if (image.getSource() == Image.FILE_IMAGE) {
						return imageHandler.onFileImage(image, this);
					}
					return imageHandler.onURLImage(image, this);
				}
			}
		}
		return resourceName;
	}

	@Override
	public Object evaluate(String script) throws BirtException {
		if (null != script && script.length() > 0) {
			return context.evaluate(script);
		}
		return null;
	}

	@Override
	public Object evaluate(String language, String script) throws BirtException {
		if (null != script && script.length() > 0) {
			return context.evaluateInlineScript(language, script);
		}
		return null;
	}

	@Override
	public ClassLoader getApplicationClassLoader() {
		return context.getApplicationClassLoader();
	}

	@Override
	public Object evaluate(Expression script) throws BirtException {
		return context.evaluate(script);
	}

	@Override
	public void cancel() {
		cancel(null);
	}

	@Override
	public void cancel(String msg) {
		IEngineTask task = context.getEngineTask();
		if (task != null) {
			task.cancel(msg);
		}
	}

	@Override
	public boolean isReportDocumentFinished() {
		return context.isReportDocumentFinished();
	}
}
