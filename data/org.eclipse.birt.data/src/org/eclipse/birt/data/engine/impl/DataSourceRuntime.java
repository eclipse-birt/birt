/*
 *************************************************************************
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
 *
 *************************************************************************
 */

package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.script.IBaseDataSourceEventHandler;
import org.eclipse.birt.data.engine.api.script.IDataSourceInstanceHandle;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IDataSource;
import org.eclipse.birt.data.engine.script.DataSourceJSEventHandler;
import org.eclipse.birt.data.engine.script.JSDataSourceImpl;
import org.eclipse.birt.data.engine.script.ScriptDataSourceJSEventHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * This class encapulates runtime properties of a DtE data source. Certain data
 * source properties are updatable by scripts at runtime. Value of those
 * properties are retained by this class. Value for non-modifiable properties
 * are delegated to the design object
 */
public abstract class DataSourceRuntime implements IDataSourceInstanceHandle {
	/** Associated data source design */
	private IBaseDataSourceDesign design;
	private IBaseDataSourceEventHandler eventHandler;

	/** top scope */
	private Scriptable sharedScope;

	/** Javascript DataSource object that wraps this data source */
	private Scriptable jsDataSourceObject;

	/**
	 * An open OdiDataSource associated with this data source If null, this data
	 * source is not open
	 */
	private IDataSource odiDataSource;

	/** log instance */
	protected static Logger logger = Logger.getLogger(DataSourceRuntime.class.getName());

	/**
	 * Creates an instance of the appropriate subclass based on a specified
	 * design-time data source definition
	 *
	 * @param dataSetDefn Design-time data source definition.
	 */
	public static DataSourceRuntime newInstance(IBaseDataSourceDesign dataSource, DataEngineImpl dataEngine)
			throws DataException {
		if (dataSource instanceof IOdaDataSourceDesign) {
			return new OdaDataSourceRuntime((IOdaDataSourceDesign) dataSource, dataEngine.getSession().getSharedScope(),
					dataEngine.getSession().getEngineContext().getScriptContext());
		} else if (dataSource instanceof IScriptDataSourceDesign) {
			return new ScriptDataSourceRuntime((IScriptDataSourceDesign) dataSource,
					dataEngine.getSession().getSharedScope(),
					dataEngine.getSession().getEngineContext().getScriptContext());
		} else if (dataSource instanceof IBaseDataSourceDesign) {
			return new GeneralDataSourceRuntime(dataSource, dataEngine.getSession().getSharedScope(),
					dataEngine.getSession().getEngineContext().getScriptContext());
		} else {
			return null;
		}
	}

	/**
	 * @param dataSourceDesign
	 * @param dataEngine
	 */
	protected DataSourceRuntime(IBaseDataSourceDesign dataSourceDesign, Scriptable sharedScope, ScriptContext cx) {
		Object[] params = { dataSourceDesign, sharedScope };
		logger.entering(DataSourceRuntime.class.getName(), "DataSourceRuntime", params);

		assert dataSourceDesign != null;

		this.design = dataSourceDesign;
		this.sharedScope = sharedScope;
		this.eventHandler = dataSourceDesign.getEventHandler();

		/*
		 * TODO: TEMPORARY the follow code is temporary. It will be removed once Engine
		 * takes over script execution from DtE
		 */
		if (eventHandler == null) {
			if (dataSourceDesign instanceof IScriptDataSourceDesign) {
				eventHandler = new ScriptDataSourceJSEventHandler(cx, (IScriptDataSourceDesign) dataSourceDesign);
			} else {
				eventHandler = new DataSourceJSEventHandler(cx, dataSourceDesign);
			}
		}
		logger.exiting(DataSourceRuntime.class.getName(), "DataSourceRuntime");
		/*
		 * END Temporary
		 */
	}

	/**
	 * Gets the IBaseDataSourceDesign object which defines the design time
	 * properties associated with this data source
	 *
	 * @return IBaseDataSourceDesign
	 */
	public IBaseDataSourceDesign getDesign() {
		return design;
	}

	/**
	 * Gets the name of the design time properties associated with this data source
	 *
	 * @param name
	 */
	@Override
	public String getName() {
		return design.getName();
	}

	/**
	 * @return
	 */
	protected IBaseDataSourceEventHandler getEventHandler() {
		return eventHandler;
	}

	/*
	 * Data source event handlers are executed as methods on the DataSet object
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.script.IJavascriptContext#getScriptScope()
	 */
	@Override
	public Scriptable getScriptScope() {
		return getJSDataSourceObject();
	}

	/**
	 * Gets a ROM Script DataSource object wrapper for this object
	 *
	 * @return Scriptable
	 */
	private Scriptable getJSDataSourceObject() {
		// Script object is created on demand
		if (jsDataSourceObject == null) {
			Scriptable topScope = this.sharedScope;
			jsDataSourceObject = (Scriptable) Context.javaToJS(new JSDataSourceImpl(this), topScope);
			jsDataSourceObject.setParentScope(topScope);
			jsDataSourceObject.setPrototype(topScope);
		}

		return jsDataSourceObject;
	}

	/**
	 * Returns true if data source is currently open.
	 */
	public boolean isOpen() {
		// A data source is open if it has an associated odi data source
		return odiDataSource != null;
	}

	/**
	 * Gets the associated odi data source. If null, data source is not open
	 */
	public IDataSource getOdiDataSource() {
		return this.odiDataSource;
	}

	/**
	 * Opens the specified odiDataSource and associate it with this data source
	 * runtime. Event scripts associated with this data source are NOT run in this
	 * method
	 */
	public void openOdiDataSource(IDataSource odiDataSource) throws DataException {
		odiDataSource.open();
		this.odiDataSource = odiDataSource;
	}

	/**
	 * @return
	 * @throws DataException
	 */
	public boolean canClose() {
		if (odiDataSource != null) {
			return odiDataSource.canClose();
		}

		return true;
	}

	/**
	 * Closes the associated odiDataSource. Event scripts associated with this data
	 * source are NOT runt in this method
	 */
	public void closeOdiDataSource() throws DataException {
		if (odiDataSource != null) {
			odiDataSource.close();
			odiDataSource = null;
		}
	}

	/** Executes the beforeOpen script associated with the data source */
	public void beforeOpen() throws DataException {
		if (eventHandler != null) {
			try {
				eventHandler.handleBeforeOpen(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	/** Executes the afterOpen script associated with the data source */
	public void afterOpen() throws DataException {
		if (eventHandler != null) {
			try {
				eventHandler.handleAfterOpen(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	/** Executes the beforeClose script associated with the data source */
	public void beforeClose() throws DataException {
		if (eventHandler != null) {
			try {
				eventHandler.handleBeforeClose(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	/** Executes the afterClose script associated with the data source */
	public void afterClose() throws DataException {
		if (eventHandler != null) {
			try {
				eventHandler.handleAfterClose(this);
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}
	}

	private static class GeneralDataSourceRuntime extends DataSourceRuntime {
		private Map<String, String> properties = new HashMap<>();

		protected GeneralDataSourceRuntime(IBaseDataSourceDesign dataSourceDesign, Scriptable sharedScope,
				ScriptContext cx) {
			super(dataSourceDesign, sharedScope, cx);
		}

		@Override
		public String getExtensionID() {
			return null;
		}

		@Override
		public String getExtensionProperty(String name) {
			return properties.get(name);
		}

		@Override
		public void setExtensionProperty(String name, String value) {
			properties.put(name, value);
		}

		@Override
		public Map getAllExtensionProperties() {
			return properties;
		}
	}
}
