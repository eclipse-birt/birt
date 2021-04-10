/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.api;

import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.ICloseListener;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.report.data.adapter.api.script.DataAdapterTopLevelScope;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Context information used to initialize a DataRequestSession. The most
 * important attributes of a session context are:
 * <li>Mode: Indicates how the the session makes use of the report document
 * <li>Top Level Scope: the top-level Javascript scope used to evaluate all
 * Javascript expressions. The consumer, if capable, should provide a scope
 * which defines all report-level Javascript objects. If the scope is null, the
 * adaptor will create a new top level scope with some limited implementation of
 * report level objects.
 * <li>Report Document Streams: reader and writer stream handles to read/write
 * the report document
 * <li>Cache options: whether and how the data session will make use of data set
 * caches
 */
public class DataSessionContext {
	/** Indicates Generation mode for data requests */
	public final static int MODE_GENERATION = DataEngineContext.MODE_GENERATION;
	/** Indicates Presentation mode for data requests */
	public final static int MODE_PRESENTATION = DataEngineContext.MODE_PRESENTATION;
	/**
	 * Indicates Direct Presentation (Generation/Presentation) mode for data
	 * requests
	 */
	public final static int MODE_DIRECT_PRESENTATION = DataEngineContext.DIRECT_PRESENTATION;

	public final static int MODE_UPDATE = DataEngineContext.MODE_UPDATE;
	/**
	 * AppContext and Data Set cache count setting decide whether cache is used,
	 * which is default value for data engine context.
	 */
	public final static int CACHE_USE_DEFAULT = DataEngineContext.CACHE_USE_DEFAULT;
	/**
	 * Do not use cache, regardless of data set cache setting
	 */
	public final static int CACHE_USE_DISABLE = DataEngineContext.CACHE_USE_DISABLE;
	/**
	 * Always use cached data if available, disregard data set cache setting and
	 * AppContext. cachRowCount parameter decides cache count.
	 */
	public final static int CACHE_USE_ALWAYS = DataEngineContext.CACHE_USE_ALWAYS;

	private DataEngineContext context = null;
	private boolean hasExternalScope = false;
	private int mode = MODE_DIRECT_PRESENTATION;
	private Scriptable topScope;
	private IDocArchiveReader docReader;
	private IDocArchiveWriter docWriter;
	private boolean cacheSet = false;
	private int cacheOption = CACHE_USE_DEFAULT;
	private int cacheCount = 0;
	private ModuleHandle moduleHandle;
	private Map appContext;
	private ScriptContext sContext;
	private ClassLoader appClassLoader;

	/**
	 * Constructs a context in the provided mode. A context created using this
	 * constructor has no associated report design, and no externally defined top
	 * level Javascript scope. The adaptor will provide its own implementation of
	 * the top level scope.
	 * 
	 * @param mode Data Session mode. Can be MODE_GENERATION, MODE_PRESENTATION or
	 *             MODE_DIRECT_PRESENTATION
	 */
	public DataSessionContext(int mode) throws BirtException {
		this(mode, null);
	}

	/**
	 * Constructs a context in the provided mode and associate it with the provided
	 * report design handle. No externally defined top level Javascript scope is
	 * specified. The adaptor will provide its own implementation of the top level
	 * scope.
	 * 
	 * @param mode         Data Session mode. Can be MODE_GENERATION,
	 *                     MODE_PRESENTATION or MODE_DIRECT_PRESENTATION
	 * @param moduleHandle If not null, this report module is used to look up data
	 *                     set and data source definition when executing queries.
	 */
	public DataSessionContext(int mode, ModuleHandle moduleHandle) throws BirtException {
		this(mode, moduleHandle, null, null);
	}

	/**
	 * Creates a DataEngine context in the provided mode, using the provided top
	 * level scope. Also sets the handle of the report design being executed.
	 * 
	 * @deprecated
	 * @param mode         Data Session mode. Can be MODE_GENERATION,
	 *                     MODE_PRESENTATION or MODE_DIRECT_PRESENTATION
	 * @param moduleHandle If not null, this report module is used to look up data
	 *                     set and data source definition when executing queries.
	 * @param topScope     A top-level scope for evaluating all Javascript code and
	 *                     expressions when executing queries. If a scope is
	 *                     provided, all report-level scripting objects (such as
	 *                     "params") must be available in the scope.
	 */
	public DataSessionContext(int mode, ModuleHandle moduleHandle, Scriptable topScope) throws BirtException {

		this(mode, moduleHandle, prepareScriptContext(topScope), null);
	}

	private static ScriptContext prepareScriptContext(Scriptable topScope) {
		ScriptContext cx = new ScriptContext();
		cx.newContext(topScope);
		return cx;
	}

	/**
	 * Creates a DataEngine context in the provided mode, using the provided top
	 * level scope. Also sets the handle of the report design being executed.
	 *
	 * @param mode          Data Session mode. Can be MODE_GENERATION,
	 *                      MODE_PRESENTATION or MODE_DIRECT_PRESENTATION
	 * @param moduleHandle  If not null, this report module is used to look up data
	 *                      set and data source definition when executing queries.
	 * @param scriptContext
	 * @param classLoader
	 * @throws AdapterException
	 */
	public DataSessionContext(int mode, ModuleHandle moduleHandle, ScriptContext scriptContext, ClassLoader classLoader)
			throws AdapterException {
		try {
			if (!(mode == MODE_GENERATION || mode == MODE_PRESENTATION || mode == MODE_DIRECT_PRESENTATION
					|| mode == MODE_UPDATE))
				throw new AdapterException(ResourceConstants.ADAPTER_INVALID_MODE, Integer.valueOf(mode));

			this.mode = mode;
			this.moduleHandle = moduleHandle;
			this.appClassLoader = classLoader;
			this.hasExternalScope = scriptContext != null;
			ScriptContext internalScriptContext = null;
			if (scriptContext == null) {
				internalScriptContext = new ScriptContext();
				scriptContext = internalScriptContext.newContext(this.getTopScope());
				DataEngineThreadLocal.getInstance().getCloseListener()
						.add(new ScriptContextCloser(internalScriptContext));
			}
			this.sContext = scriptContext;
			this.topScope = ((IDataScriptEngine) scriptContext.getScriptEngine(IDataScriptEngine.ENGINE_NAME))
					.getJSScope(scriptContext);
		} catch (BirtException e) {
			throw new AdapterException(e.getErrorCode(), e);
		}
	}

	/**
	 * Sets the report doc reader. A reader is required in presentation mode
	 */
	public void setDocumentReader(IDocArchiveReader reader) {
		this.docReader = reader;
	}

	/**
	 * Sets the report doc writer. A writer is required in generation mode
	 */
	public void setDocumentWriter(IDocArchiveWriter writer) {
		this.docWriter = writer;
	}

	/**
	 * Return the document writer.
	 * 
	 * @return
	 */
	public IDocArchiveWriter getDocumentWriter() {
		return this.docWriter;
	}

	/**
	 * Sets the cache option. This function only available for
	 * MODE_DIRECT_PRESENTATION mode. In other cases, exception will be thrown.
	 * 
	 */
	public void setCacheOption(int option, int cacheCount) throws BirtException {
		if (!(option == CACHE_USE_DEFAULT || option == CACHE_USE_DISABLE || option == CACHE_USE_ALWAYS))
			throw new AdapterException(ResourceConstants.INVALID_CAHCE_OPTION, Integer.valueOf(option));
		this.cacheSet = true;
		this.cacheOption = option;
		this.cacheCount = cacheCount;
	}

	/**
	 * Gets a DataEngineContext for use to initialize data engine
	 */
	public DataEngineContext getDataEngineContext() throws BirtException {
		if (this.context != null)
			return this.context;
		this.context = DataEngineContext.newInstance(mode, sContext, docReader, docWriter, appClassLoader);
		if (cacheSet)
			this.context.setCacheOption(cacheOption, cacheCount);
		return this.context;
	}

	public void setDataEngineContext(DataEngineContext context) {
		if (context != null)
			this.context = context;
	}

	/**
	 * Whether a user-provided top scope is used. If return value is false, the
	 * adaptor has provided its own implementation of a top level scope
	 */
	public boolean hasExternalScope() {
		return hasExternalScope;
	}

	/**
	 * Gets a top level scope for evaluating javascript expressions. This scope can
	 * be either user provided, or one created by this adaptor
	 */
	public Scriptable getTopScope() {
		if (topScope == null) {
			// No scope provided by user; create one
			Context cx = Context.enter();
			topScope = new DataAdapterTopLevelScope(cx, moduleHandle);
			Context.exit();
		}
		return topScope;
	}

	/**
	 * Gets the report design module associated with the session
	 */
	public ModuleHandle getModuleHandle() {
		return moduleHandle;
	}

	public void setModuleHandle(ModuleHandle moduleHandle) {
		this.moduleHandle = moduleHandle;
	}

	/**
	 * Sets the application context to be passed to data engine and data drivers
	 * 
	 * @param context - task contexts in a map. The map contains name(String) -
	 *                value pairs
	 */
	public void setAppContext(Map context) {
		this.appContext = context;
	}

	/**
	 * Gets the application context map for this request.
	 * 
	 * @returns Context as name(string) -> value map. Null if no application context
	 *          has been set
	 */
	public Map getAppContext() {
		return this.appContext;
	}

	class ScriptContextCloser implements ICloseListener {
		private ScriptContext scriptContext;

		ScriptContextCloser(ScriptContext scriptContext) {
			this.scriptContext = scriptContext;
		}

		public void close() throws IOException {
			this.scriptContext.close();
		}
	}
}
