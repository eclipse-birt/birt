/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.core.util.LocaleUtil;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineConfig;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IStatusHandler;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.UnsupportedFormatException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.executor.ReportExtensionExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.extension.engine.IGenerateExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.v3.ReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.Expression.Script;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.eclipse.birt.report.engine.util.SecurityUtil;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DynamicFilterParameterHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Defines an engine task that could be executed, debugged (runs step by step),
 * inform caller for progress, etc.
 */
public abstract class EngineTask implements IEngineTask {
	public final static String TASK_TYPE = "task_type";
	private static int id = 0;

	protected String pagination;

	protected final static String FORMAT_HTML = "html";
	protected final static String FORMAT_XHTML = "xhtml";

	/**
	 * is cancel called
	 */
	protected boolean cancelFlag;

	/**
	 * the fatal errors occurs that will terminate the engine task.
	 */
	protected EngineException fatalError = null;

	protected int runningStatus;

	/**
	 * a reference to the report engine
	 */
	protected ReportEngine engine;

	/**
	 * logger used to output the message
	 */
	protected Logger log;

	/**
	 * the locale used in this task
	 */
	protected ULocale ulocale = ULocale.getDefault();

	/**
	 * define a time zone, and set a default value
	 */
	protected TimeZone timeZone = TimeZone.getDefault();

	/**
	 * the execution context
	 */
	protected ExecutionContext executionContext;

	/**
	 * task identifier. Could be used for logging
	 */
	protected int taskID;

	/**
	 * options used to render the report design.
	 */
	protected IRenderOption renderOptions;
	/**
	 * emitter id
	 */
	protected String emitterID;

	protected String format;

	/**
	 * does the parameter has been changed by the user.
	 */
	protected boolean parameterChanged = true;
	/**
	 * The parameter values that the caller has set explicitly
	 */
	protected HashMap inputValues = new HashMap();

	/**
	 * The parameter values that will be used to run the report. It is a merged map
	 * between the input value and the default values.
	 */
	protected HashMap runValues = new HashMap();

	/**
	 * default parameter values
	 */
	protected HashMap defaultValues = new HashMap();

	/**
	 * Engine task type. for usage in BIRT scripting.
	 */
	protected int taskType = IEngineTask.TASK_UNKNOWN;

	private Object platformContext;

	protected IPageHandler pageHandler;

	protected IProgressMonitor progressMonitor;

	protected IStatusHandler statusHandler;

	protected String[] userAcls;

	protected String cancelReason;

	private ResourceLocatorWrapper resourceLocator;

	/**
	 * @param engine     reference to report engine
	 * @param appContext a user-defined object that capsulates the context for
	 *                   running a task. The context object is passed to callback
	 *                   functions (i.e., functions in image handlers, action
	 *                   handlers, etc. ) that are written by those who embeds
	 *                   engine in their applications
	 */
	protected EngineTask(ReportEngine engine, IReportRunnable runnable, int taskType) {
		this(engine, taskType);
		setReportRunnable(runnable);
	}

	protected EngineTask(ReportEngine engine, int taskType) {
		this.engine = engine;
		this.taskType = taskType;
		taskID = id++;
		this.log = engine.getLogger();

		setupExecutionContext();
		// set the default app context
		setAppContext(null);

		cancelFlag = false;

		resourceLocator = new ResourceLocatorWrapper();
		runningStatus = STATUS_NOT_STARTED;
	}

	protected void setupExecutionContext() {
		// create execution context used by java-script
		executionContext = new ExecutionContext(this);
		// Create IReportContext used by java-based script
		executionContext.setReportContext(new ReportContextImpl(executionContext));
	}

	protected IReportRunnable getOnPreparedRunnable(IReportDocument doc) {
		IInternalReportDocument internalReportDoc = (IInternalReportDocument) doc;
		return internalReportDoc.getOnPreparedRunnable();
	}

	/**
	 * @return Returns the locale.
	 */
	@Override
	public Locale getLocale() {
		return ulocale.toLocale();
	}

	/**
	 * @return Returns the ulocale.
	 */
	@Override
	public ULocale getULocale() {
		return ulocale;
	}

	/**
	 * sets the task locale
	 *
	 * The locale must be called in the same thread which create the engine task
	 *
	 * @param locale the task locale
	 */
	@Override
	public void setLocale(Locale locale) {
		if (locale == null) {
			throw new NullPointerException();
		}
		log.log(Level.FINE, "EngineTask.setLocale: locale={0}", locale.getDisplayName());
		doSetLocale(LocaleUtil.forLocale(locale));
	}

	private void doSetLocale(ULocale locale) {
		this.ulocale = locale;
		executionContext.setLocale(ulocale);
		EngineException.setULocale(ulocale);
	}

	/**
	 * sets the task locale
	 *
	 * @param locale the task locale
	 */
	@Override
	public void setLocale(ULocale ulocale) {
		if (ulocale == null) {
			throw new NullPointerException();
		}
		log.log(Level.FINE, "EngineTask.setLocale: uLocale={0}", ulocale.getDisplayName());
		doSetLocale(ulocale);
	}

	@Override
	public void setTimeZone(TimeZone timeZone) {
		if (timeZone == null) {
			throw new NullPointerException();
		}
		this.timeZone = timeZone;
		executionContext.setTimeZone(timeZone);
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	/**
	 * sets the task context
	 *
	 * @param context the task context
	 */
	@Override
	public void setAppContext(Map context) {
		HashMap appContext = new HashMap();
		HashMap sysAppContext = engine.getConfig().getAppContext();
		if (sysAppContext != null) {
			appContext.putAll(sysAppContext);
		}
		addAppContext(context, appContext);
		if (userAcls != null) {
			appContext.put(EngineConstants.USER_ACL_KEY, userAcls);
		}

		// Allow this task to be accessed from AppContext
		appContext.put(EngineConstants.APPCONTEXT_ENGINE_TASK, this);

		executionContext.setAppContext(appContext);

		StringBuffer logStr = null;
		if (log.isLoggable(Level.FINE)) {
			logStr = new StringBuffer();
		}

		// add the contexts into ScriptableJavaObject
		if (!appContext.isEmpty()) {
			Set entries = appContext.entrySet();
			for (Iterator iter = entries.iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();

				if (entry.getKey() instanceof String) {
					executionContext.registerBean((String) entry.getKey(), entry.getValue());
					if (logStr != null) {
						logStr.append(entry.getKey());
						logStr.append("=");
						logStr.append(entry.getValue());
						logStr.append(";");
					}
				} else {
					log.log(Level.WARNING, "Map entry {0} is invalid and ignored, because its key is a not string.", //$NON-NLS-1$
							entry.getKey().toString());
				}
			}
		}

		if (logStr != null) {
			log.log(Level.FINE, "EngineTask.setAppContext: context={0}", logStr);
		}
	}

	/**
	 * Merges user specified app context to that of EngineTask. The context
	 * variables in entry with following keys will be ignored:
	 *
	 * <ul>
	 * <li><code>EngineConstants.APPCONTEXT_CLASSLOADER_KEY</code>
	 * <li><code>EngineConstants.WEBAPP_CLASSPATH_KEY</code>
	 * <li><code>EngineConstants.PROJECT_CLASSPATH_KEY</code>
	 * <li><code>EngineConstants.WORKSPACE_CLASSPATH_KEY</code>
	 * </ul>
	 *
	 * @param from the source app contexts.
	 * @param to   the destination app contexts.
	 */
	private void addAppContext(Map from, Map to) {
		if (from == null || to == null) {
			return;
		}
		Iterator iterator = from.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			// Ignore the entry that should not be set from engine task.
			if (!isDeprecatedEntry(entry)) {
				to.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private boolean isDeprecatedEntry(Map.Entry entry) {
		Object key = entry.getKey();
		if (EngineConstants.APPCONTEXT_CLASSLOADER_KEY.equals(key) || EngineConstants.WEBAPP_CLASSPATH_KEY.equals(key)
				|| EngineConstants.PROJECT_CLASSPATH_KEY.equals(key)
				|| EngineConstants.WORKSPACE_CLASSPATH_KEY.equals(key)) {
			if (entry.getValue() != getAppContext().get(key)) {
				log.log(Level.WARNING, key
						+ " could not be set in appContext of IEngineTask, please set it in appContext of IReportEngine");
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the object that encapsulates the context for running the task
	 *
	 * @return Returns the context.
	 */
	@Override
	public Map getAppContext() {
		return executionContext.getAppContext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getEngine()
	 */
	@Override
	public IReportEngine getEngine() {
		return engine;
	}

	public void setReportRunnable(IReportRunnable runnable) {
		if (runnable != null) {
			Properties systemProperties = SecurityUtil.getSystemProperties();
			executionContext.setRunnable(runnable);
			// register the properties into the scope, so the user can
			// access the config through the property name directly.
			executionContext.registerBeans(systemProperties);
			executionContext.registerBeans(runnable.getTestConfig());
			// put the properties into the configs also, so the user can
			// access the config through config["name"].
			executionContext.getConfigs().putAll(systemProperties);
			executionContext.getConfigs().putAll(runnable.getTestConfig());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getReportRunnable()
	 */
	@Override
	public IReportRunnable getReportRunnable() {
		return executionContext.getOriginalRunnable();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IRenderTask#setRenderOption(org.eclipse.
	 * birt.report.engine.api.IRenderOption)
	 */
	public void setRenderOption(IRenderOption options) {
		if (options == null) {
			throw new NullPointerException("options can not be null");
		}
		renderOptions = options;
	}

	public IRenderOption getRenderOption() {
		return renderOptions;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IRenderTask#setEmitterID(java.lang.String)
	 */
	/**
	 * @deprecated
	 */
	@Deprecated
	public void setEmitterID(String id) {
		this.emitterID = id;
	}

	/**
	 * @deprecated
	 * @return the emitter ID to be used to render this report. Could be null, in
	 *         which case the engine will choose one emitter that matches the
	 *         requested output format.
	 */
	@Deprecated
	public String getEmitterID() {
		return this.emitterID;
	}

	public DataRequestSession getDataSession() throws EngineException {
		return executionContext.getDataEngine().getDTESession();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IEngineTask#addScriptableJavaObject(java.
	 * lang.String, java.lang.Object)
	 */
	@Override
	public void addScriptableJavaObject(String jsName, Object obj) {
		executionContext.registerBean(jsName, obj);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getID()
	 */
	@Override
	public int getID() {
		return taskID;
	}

	protected Object convertToType(Object value, String type) {
		try {
			return convertParameterType(value, type);
		} catch (BirtException e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return null;
	}

	/*
	 * protected Object convertToType( List<String> values, String type, String
	 * paramType ) { if ( values == null ) { return null; } try { if (
	 * DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals( paramType )) {
	 * List<Object> list = new ArrayList<Object>(); for ( String string : values ) {
	 * list.add( convertParameterType( string, type ) ); } return list.toArray( ); }
	 * else { if ( values.size( ) == 0 ) { return null; } return
	 * convertParameterType( values.get( 0 ), type ); } } catch ( BirtException e )
	 * { log.log( Level.SEVERE, e.getLocalizedMessage( ), e ); } return null; }
	 */
	public static Object convertParameterType(Object value, String type) throws BirtException {
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			return DataTypeUtil.toBoolean(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			return DataTypeUtil.toDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			return DataTypeUtil.toSqlDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			return DataTypeUtil.toSqlTime(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)) {
			return DataTypeUtil.toBigDecimal(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			return DataTypeUtil.toDouble(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			return DataTypeUtil.toLocaleNeutralString(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			return DataTypeUtil.toInteger(value);
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IRunAndRenderTask#validateParameters()
	 */
	@Override
	public boolean validateParameters() {
		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable == null) {
			return false;
		}

		// set the parameter values into the execution context
		try {
			return doValidateParameters();
		} catch (ParameterValidationException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
		return false;
	}

	protected boolean doValidateParameters() throws ParameterValidationException {
		// set the parameter values into the execution context
		usingParameterValues();

		if (log.isLoggable(Level.FINE)) {
			loggerParamters();
		}
		// validate each parameter to see if it is validate
		ParameterValidationVisitor pv = new ParameterValidationVisitor();
		boolean result = pv.visit(executionContext.getDesign(), null, executionContext);
		if (pv.engineException != null) {
			throw pv.engineException;
		}
		return result;
	}

	private class ParameterValidationVisitor extends ParameterVisitor {
		@Override
		boolean visitScalarParameter(ScalarParameterHandle param, Object value) {
			try {
				return validateAbstractScalarParameter(param);
			} catch (ParameterValidationException pe) {
				engineException = pe;
			}
			return false;
		}

		@Override
		boolean visitDynamicFilterParameter(DynamicFilterParameterHandle param, Object value) {
			try {
				return validateAbstractScalarParameter(param);
			} catch (ParameterValidationException pe) {
				engineException = pe;
			}
			return false;
		}

		@Override
		boolean visitParameterGroup(ParameterGroupHandle group, Object value) {
			return visitParametersInGroup(group, value);
		}
	}

	protected void loggerParamters() {
		if (log.isLoggable(Level.FINE)) {
			final StringBuffer buffer = new StringBuffer();
			// validate each parameter to see if it is validate
			new ParameterVisitor() {

				@Override
				boolean visitScalarParameter(ScalarParameterHandle param, Object value) {
					String paramName = param.getName();
					Object paramValue = runValues.get(paramName);
					buffer.append(paramName);
					buffer.append(":");
					buffer.append(paramValue);
					buffer.append("\n");
					return true;
				}

				@Override
				boolean visitDynamicFilterParameter(DynamicFilterParameterHandle param, Object userData) {
					String paramName = param.getName();
					Object paramValue = runValues.get(paramName);
					buffer.append(paramName);
					buffer.append(":");
					buffer.append(paramValue);
					buffer.append("\n");
					return true;
				}

				@Override
				boolean visitParameterGroup(ParameterGroupHandle group, Object value) {
					return visitParametersInGroup(group, value);
				}
			}.visit(executionContext.getDesign(), null, executionContext);
			log.log(Level.FINE, "Running the report with paramters: {0}", buffer);
		}
	}

	/**
	 * validate whether the parameter value is a valid value for the parameter
	 *
	 * @param p          the parameter to be verified
	 * @param paramValue the value for the parameter
	 * @return true if the given parameter value is valid; false otherwise
	 */
	private boolean validateAbstractScalarParameter(AbstractScalarParameterHandle paramHandle)
			throws ParameterValidationException {

		String paramName = paramHandle.getName();
		Object paramValue = runValues.get(paramName);

		// Handle null parameter values
		if (paramValue == null) {
			if (!paramHandle.isRequired()) {
				return true;
			}

			throw new ParameterValidationException(MessageConstants.PARAMETER_IS_NULL_EXCEPTION,
					new String[] { paramName });
		}

		String source = paramHandle.getValidate();
		if (source != null && source.length() != 0) {
			try {
				Object result = executionContext.evaluate(source);
				if (!(result instanceof Boolean) || !((Boolean) result).booleanValue()) {
					throw new ParameterValidationException(MessageConstants.PARAMETER_VALIDATION_FAILURE,
							new String[] { paramName });
				}
			} catch (ParameterValidationException pve) {
				throw pve;
			} catch (BirtException ex) {
				throw new ParameterValidationException(ex);
			}
		}

		if (paramHandle instanceof ScalarParameterHandle) {
			ScalarParameterHandle sparam = (ScalarParameterHandle) paramHandle;
			String type = paramHandle.getDataType();

			String paramType = sparam.getParamType();
			if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(paramType)) {
				if (paramValue instanceof Object[]) {
					boolean isValid = true;
					Object[] paramValueList = (Object[]) paramValue;
					for (int i = 0; i < paramValueList.length; i++) {
						if (paramValueList[i] != null) {
							if (!validateParameterValueType(paramName, paramValueList[i], type, sparam)) {
								isValid = false;
							}
						}
					}
					return isValid;
				}
				throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
						new String[] { paramName, "Object[]", paramValue.getClass().getName() });
			}
			return validateParameterValueType(paramName, paramValue, type, paramHandle);
		} else if (paramHandle instanceof DynamicFilterParameterHandle) {
			return validateParameterValueType(paramName, paramValue, DesignChoiceConstants.PARAM_TYPE_STRING,
					paramHandle);
		}
		// unknown scalar parameter
		return false;
	}

	/*
	 * Validate parameter value based on parameter type
	 */
	private boolean validateParameterValueType(String paramName, Object paramValue, String type,
			AbstractScalarParameterHandle paramHandle) throws ParameterValidationException {
		/*
		 * Validate based on parameter type
		 */
		if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			if (paramValue instanceof Number) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			if (paramValue instanceof Date) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			if (paramValue instanceof java.sql.Date) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			if (paramValue instanceof java.sql.Time) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			if (!(paramValue instanceof String)) {
				throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
						new String[] { paramName, type, paramValue.getClass().getName() });
			}
			validateStringParameter(paramName, paramValue, paramHandle);
			return true;
		} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			if (paramValue instanceof Boolean) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			if (paramValue instanceof Integer) {
				return true;
			}
			throw new ParameterValidationException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, type, paramValue.getClass().getName() });
		}
		return true;
	}

	protected void validateStringParameter(String paramName, Object paramValue,
			AbstractScalarParameterHandle paramHandle) throws ParameterValidationException {
		if (paramHandle.isRequired()) // $NON-NLS-1$
		{
			String value = paramValue.toString().trim();
			if (value.length() == 0) {
				throw new ParameterValidationException(MessageConstants.PARAMETER_IS_BLANK_EXCEPTION,
						new String[] { paramName });
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IEngineTask#setParameterValues(java.util.
	 * HashMap)
	 */
	@Override
	public void setParameterValues(Map params) {
		Iterator iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String name = (String) entry.getKey();
			Object value = entry.getValue();
			setParameterValue(name, value);
		}
	}

	protected void setParameters(Map<String, ParameterAttribute> parameters) {
		for (Map.Entry<String, ParameterAttribute> entry : parameters.entrySet()) {
			inputValues.put(entry.getKey(), entry.getValue());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IEngineTask#setParameterValue(java.lang.
	 * String, java.lang.Object)
	 */
	@Override
	public void setParameterValue(String name, Object value) {
		log.log(Level.FINE, "EngineTask.setParameterValue: {0}={1} [{2}]",
				new Object[] { name, value, value == null ? null : value.getClass().getName() });
		parameterChanged = true;
		Object parameter = inputValues.get(name);
		if (parameter != null) {
			assert parameter instanceof ParameterAttribute;
			((ParameterAttribute) parameter).setValue(value);
		} else {
			inputValues.put(name, new ParameterAttribute(value, null));
		}
	}

	@Override
	public void setParameterValue(String name, Object[] values) {
		log.log(Level.FINE, "EngineTask.setParameterValue: {0}={1} [{2}]",
				new Object[] { name, values, values == null ? null : values.getClass().getName() });
		parameterChanged = true;
		Object parameter = inputValues.get(name);
		if (parameter != null) {
			assert parameter instanceof ParameterAttribute;
			((ParameterAttribute) parameter).setValue(values);
		} else {
			inputValues.put(name, new ParameterAttribute(values, new String[] { null }));
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IEngineTask#setParameterValue(java.lang.
	 * String, java.lang.Object)
	 */
	public void setValue(String name, Object value) {
		setParameterValue(name, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IEngineTask#setParameterValue(java.lang.
	 * String, java.lang.Object)
	 */
	public void setValue(String name, Object[] values) {
		setParameterValue(name, values);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#getParameterValues()
	 */
	@Override
	public HashMap getParameterValues() {
		HashMap result = new HashMap();
		Iterator iterator = inputValues.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			ParameterAttribute parameter = (ParameterAttribute) entry.getValue();
			result.put(entry.getKey(), parameter.getValue());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IEngineTask#getParameterValue(java.lang.
	 * String)
	 */
	@Override
	public Object getParameterValue(String name) {
		Object parameter = inputValues.get(name);
		if (parameter == null) {
			return null;
		}
		assert parameter instanceof ParameterAttribute;
		return ((ParameterAttribute) parameter).getValue();
	}

	@Override
	public void setParameter(String name, Object value, String displayText) {
		parameterChanged = true;
		inputValues.put(name, new ParameterAttribute(value, displayText));
	}

	@Override
	public void setParameter(String name, Object[] values, String[] displayText) {
		parameterChanged = true;
		inputValues.put(name, new ParameterAttribute(values, displayText));
	}

	@Override
	public Object getParameterDisplayText(String name) {
		Object parameter = inputValues.get(name);
		if (parameter != null) {
			assert parameter instanceof ParameterAttribute;
			return ((ParameterAttribute) parameter).getDisplayText();
		}
		return null;
	}

	public void setParameterDisplayTexts(Map params) {
		Iterator iterator = params.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			String name = (String) entry.getKey();
			Object text = entry.getValue();
			if (text != null) {
				if (text instanceof String[]) {
					setParameterDisplayText(name, (String[]) text);
				} else {
					setParameterDisplayText(name, text.toString());
				}
			}
		}
	}

	@Override
	public void setParameterDisplayText(String name, String displayText) {
		parameterChanged = true;
		Object parameter = inputValues.get(name);
		if (parameter != null) {
			assert parameter instanceof ParameterAttribute;
			((ParameterAttribute) parameter).setDisplayText(displayText);
		} else {
			inputValues.put(name, new ParameterAttribute(null, displayText));
		}
	}

	@Override
	public void setParameterDisplayText(String name, String[] displayText) {
		parameterChanged = true;
		Object parameter = inputValues.get(name);
		if (parameter != null) {
			assert parameter instanceof ParameterAttribute;
			((ParameterAttribute) parameter).setDisplayText(displayText);
		} else {
			inputValues.put(name, new ParameterAttribute(null, displayText));
		}
	}

	protected Object evaluateDefaultValue(AbstractScalarParameterHandle parameter) {
		if (parameter != null) {
			String name = parameter.getName();
			if (defaultValues.containsKey(name)) {
				return defaultValues.get(name);
			}
		}

		ScalarParameterHandle sparameter = null;
		if (parameter instanceof ScalarParameterHandle) {
			sparameter = (ScalarParameterHandle) parameter;
			String valueExpr = sparameter.getDefaultValueListMethod();
			if (valueExpr != null) {
				try {
					Object result = executionContext.evaluate(valueExpr);
					if (result == null) {
						return null;
					}
					if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(sparameter.getParamType())) {
						ArrayList results = new ArrayList();
						String dataType = sparameter.getDataType();
						if (result instanceof Collection) {
							Iterator itr = ((Collection) result).iterator();
							while (itr.hasNext()) {
								results.add(convertToType(itr.next(), dataType));
							}
						} else if (result.getClass().isArray()) {
							int count = Array.getLength(result);
							for (int index = 0; index < count; index++) {
								Object origValue = Array.get(result, index);
								results.add(convertToType(origValue, dataType));
							}
						} else {
							results.add(convertToType(result, dataType));
						}
						return results.toArray();
					} else {
						return convertToType(result, sparameter.getDataType());
					}
				} catch (BirtException e) {
					executionContext.addException(e);
					log.log(Level.FINE, e.getLocalizedMessage(), e);
				}
				return null;
			}
		}
		List<Expression> values = null;
		if (parameter != null) {
			values = parameter.getDefaultValueList();
		}
		if (values == null || values.size() == 0) {
			return null;
		}
		if (sparameter != null) {
			if (!DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(sparameter.getParamType())) {
				Expression expr = values.get(0);
				if (expr != null) {
					return evaluateExpression(expr, parameter.getDataType());
				}
				return null;
			}
			// multiple values
			ArrayList results = new ArrayList();
			for (Expression expr : values) {
				if (expr != null) {
					Object value = evaluateExpression(expr, parameter.getDataType());
					results.add(value);
				}
			}
			return results.toArray();
		} else {
			// dynamic filter parameter
			Expression expr = values.get(0);
			if (expr != null) {
				return evaluateExpression(expr, DesignChoiceConstants.PARAM_TYPE_STRING);
			}
			return null;
		}
	}

	private Object evaluateExpression(Expression expr, String type) {
		try {
			org.eclipse.birt.report.engine.ir.Expression irExpr = createExpression(expr);
			Object value = executionContext.evaluate(irExpr);
			return convertToType(value, type);
		} catch (BirtException e) {
			executionContext.addException(e);
			log.log(Level.FINE, e.getLocalizedMessage(), e);
		}
		return null;
	}

	private org.eclipse.birt.report.engine.ir.Expression createExpression(Expression expr) {
		if (expr != null) {
			String type = expr.getType();
			if (ExpressionType.CONSTANT.equals(type)) {
				String text = expr.getStringExpression();
				return org.eclipse.birt.report.engine.ir.Expression.newConstant(-1, text);
			} else {
				String text = expr.getStringExpression();
				if (text != null) {
					text = text.trim();
					if (text.length() > 0) {
						return org.eclipse.birt.report.engine.ir.Expression.newScript(type, text);
					}
				}
			}
		}
		return null;
	}

	protected org.eclipse.birt.report.engine.ir.Expression createExpression(ExpressionHandle expressionHandle) {
		if (expressionHandle != null) {
			if (expressionHandle.isSet()) {
				String type = expressionHandle.getType();
				if (ExpressionType.CONSTANT.equals(type)) {
					// String valueType = expressionHandle.getValue( );
					String text = expressionHandle.getStringExpression();
					return org.eclipse.birt.report.engine.ir.Expression.newConstant(-1, text);
				} else {
					String text = expressionHandle.getStringExpression();
					if (text != null) {
						text = text.trim();
						if (text.length() > 0) {
							return org.eclipse.birt.report.engine.ir.Expression.newScript(type, text);
						}
					}
				}
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#cancel()
	 */
	@Override
	public void cancel() {
		cancelFlag = true;
		if (executionContext != null) {
			executionContext.cancel();
		}
		disposeResourceLocator();
		changeStatusToStopped();
	}

	@Override
	public void cancel(String reason) {
		cancelReason = reason;
		cancel();
	}

//	public void cancel( Object signal )
//	{
//		if ( signal == null )
//		{
//			throw new IllegalArgumentException( "signal can not be null" );
//		}
//		cancelFlag = true;
//		long waitingTime = 0;
//		do
//		{
//			waitingTime += 100;
//			try
//			{
//				Thread.sleep( 100 );
//			}
//			catch ( Exception ex )
//			{
//			}
//			if ( runningStatus != STATUS_RUNNING )
//			{
//				return;
//			}
//		} while ( waitingTime < 5000 );
//		disposeResourceLocator( );
//		changeStatusToStopped();
//		return;
//	}

	@Override
	public boolean getCancelFlag() {
		return cancelFlag;
	}

	@Override
	public void setErrorHandlingOption(int option) {
		if (option == CANCEL_ON_ERROR) {
			executionContext.setCancelOnError(true);
		} else {
			executionContext.setCancelOnError(false);
		}
	}

	public ResourceLocatorWrapper getResourceLocatorWrapper() {
		return resourceLocator;
	}

	/**
	 * class used to visit all parameters
	 *
	 */
	static abstract class ParameterVisitor {
		ParameterValidationException engineException;

		boolean visitParametersInGroup(ParameterGroupHandle group, Object value) {
			SlotHandle parameters = group.getParameters();
			Iterator iter = parameters.iterator();
			while (iter.hasNext()) {
				Object param = iter.next();
				if (param instanceof CascadingParameterGroupHandle) {
					if (!visitCascadingParamterGroup((CascadingParameterGroupHandle) param, value)) {
						return false;
					}
				} else if (param instanceof ParameterGroupHandle) {
					if (!visitParameterGroup((ParameterGroupHandle) param, value)) {
						return false;
					}
				} else if (param instanceof ScalarParameterHandle) {
					if (!visitScalarParameter((ScalarParameterHandle) param, value)) {
						return false;
					}
				} else if (param instanceof DynamicFilterParameterHandle) {
					if (!(visitDynamicFilterParameter((DynamicFilterParameterHandle) param, value))) {
						return false;
					}
				}
			}
			return true;
		}

		boolean visitCascadingParamterGroup(CascadingParameterGroupHandle group, Object value) {
			return visitParameterGroup(group, value);
		}

		boolean visitParameterGroup(ParameterGroupHandle group, Object value) {
			return false;
		}

		boolean visitScalarParameter(ScalarParameterHandle param, Object value) {
			return false;
		}

		boolean visitDynamicFilterParameter(DynamicFilterParameterHandle param, Object value) {
			return false;
		}

		boolean visit(ModuleHandle report, ExecutionContext executionContext) {
			return visit(report, null, executionContext);
		}

		boolean visit(ModuleHandle report, Object value, ExecutionContext executionContext) {
			executionContext.clearExceptions();
			SlotHandle parameters = report.getParameters();
			if (parameters != null) {
				Iterator iter = parameters.iterator();

				while (iter.hasNext()) {
					Object param = iter.next();
					if (param instanceof CascadingParameterGroupHandle) {
						if (!visitCascadingParamterGroup((CascadingParameterGroupHandle) param, value)) {
							executionContext.addException((CascadingParameterGroupHandle) param, engineException);
						}
					} else if (param instanceof ParameterGroupHandle) {
						if (!visitParameterGroup((ParameterGroupHandle) param, value)) {
							executionContext.addException((ParameterGroupHandle) param, engineException);
						}
					} else if (param instanceof ScalarParameterHandle) {
						if (!visitScalarParameter((ScalarParameterHandle) param, value)) {
							executionContext.addException((ScalarParameterHandle) param, engineException);
						}
					} else if (param instanceof DynamicFilterParameterHandle) {
						if (!visitDynamicFilterParameter((DynamicFilterParameterHandle) param, value)) {
							executionContext.addException((DynamicFilterParameterHandle) param, engineException);
						}
					}
				}
			}
			if (executionContext.hasErrors()) {
				return false;
			}
			return true;
		}
	}

	protected IQueryResults executeDataSet(DataSetHandle hDataSet, HashMap parameters) {
		return null;
	}

	protected Object refineParameterValue(String name, Object value) {
		return value;
	}

	/**
	 * use the user setting parameters values to setup the execution context. the
	 * user setting values and default values are merged here.
	 */
	protected void usingParameterValues() {
		if (!parameterChanged) {
			return;
		}

		parameterChanged = false;

		// clear previous settings
		executionContext.clearParameters();
		runValues.clear();

		// set the user setting values into the execution context
		Iterator iterator = inputValues.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Map.Entry) iterator.next();
			Object key = entry.getKey();
			ParameterAttribute attribute = (ParameterAttribute) entry.getValue();
			Object value = refineParameterValue((String) key, attribute.getValue());
			runValues.put(key, value);
			executionContext.setParameter((String) key, value, attribute.getDisplayText());
		}
		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable == null) {
			return;
		}

		// use default value for the parameter without user value.
		new ParameterVisitor() {

			@Override
			boolean visitScalarParameter(ScalarParameterHandle param, Object userData) {
				String name = param.getName();
				if (!inputValues.containsKey(name)) {
					Object value = evaluateDefaultValue(param);
					runValues.put(name, value);
					defaultValues.put(name, value);

					// set Display Text

					ScalarParameter spd = (ScalarParameter) param.getElement();
					List<SelectionChoice> selectList = (List<SelectionChoice>) spd.getProperty(param.getModule(),
							IAbstractScalarParameterModel.SELECTION_LIST_PROP);
					String paramType = (String) spd.getFactoryProperty(param.getModule(),
							IScalarParameterModel.PARAM_TYPE_PROP);
					if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(paramType)) {
						Object[] values = (Object[]) value;
						List<String> displayTextList = new ArrayList<>();
						if (values != null && selectList != null && selectList.size() > 0) {
							for (Object o : values) {
								for (SelectionChoice select : selectList) {
									if (o == null) {
										if (select.getValue() == null) {
											displayTextList.add(select.getLabel());
										}
									} else if (o.equals(select.getValue())) {
										displayTextList.add(select.getLabel());
									}
								}

							}
						}
						String[] displayTexts = new String[displayTextList.size()];
						executionContext.setParameter(name, value, displayTextList.toArray(displayTexts));
					} else {
						String displayText = null;
						if (selectList != null && selectList.size() > 0) {
							for (SelectionChoice select : selectList) {
								if (value == null) {
									if (select.getValue() == null) {
										displayText = select.getLabel();
										break;
									}
								} else if (value.equals(select.getValue())) {
									displayText = select.getLabel();
									break;
								}
							}
						}
						executionContext.setParameter(name, value, displayText);
					}
				}
				return true;
			}

			@Override
			boolean visitDynamicFilterParameter(DynamicFilterParameterHandle param, Object userData) {
				String name = param.getName();
				if (!inputValues.containsKey(name)) {
					Object value = evaluateDefaultValue(param);
					executionContext.setParameterValue(name, value);
					runValues.put(name, value);
					defaultValues.put(name, value);
				}
				return true;
			}

			@Override
			boolean visitParameterGroup(ParameterGroupHandle group, Object value) {
				return visitParametersInGroup(group, value);
			}
		}.visit((ModuleHandle) runnable.getDesignHandle(), executionContext);
	}

	@Override
	public void close() {
		executionContext.close();
		disposeResourceLocator();
		EngineLogger.setThreadLogger(null);
	}

	private void disposeResourceLocator() {
		if (resourceLocator != null) {
			resourceLocator.dispose();
			resourceLocator = null;
		}
	}

	protected IContentEmitter createContentEmitter() throws EngineException {
		ExtensionManager extManager = ExtensionManager.getInstance();
		pagination = extManager.getPagination(emitterID);
		IRenderOption executionRenderOptions = executionContext.getRenderOption();
		if (!renderOptions.hasOption(IRenderOption.OUTPUT_DISPLAY_NONE)) {
			Boolean outputDisplayNone = extManager.getOutputDisplayNone(emitterID);
			renderOptions.setOption(IRenderOption.OUTPUT_DISPLAY_NONE, outputDisplayNone);
			executionRenderOptions.setOption(IRenderOption.OUTPUT_DISPLAY_NONE, outputDisplayNone);
		}
		if (!renderOptions.hasOption(IRenderOption.SUPPORTED_IMAGE_FORMATS)) {
			String supportedImageFormats = extManager.getSupportedImageFormats(emitterID);
			renderOptions.setOption(IRenderOption.SUPPORTED_IMAGE_FORMATS, supportedImageFormats);
			executionRenderOptions.setOption(IRenderOption.SUPPORTED_IMAGE_FORMATS, supportedImageFormats);
		}
		executionContext.setNeedOutputResultSet(extManager.needOutputResultSet(emitterID));
		IContentEmitter emitter = null;
		try {
			emitter = extManager.createEmitter(emitterID);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "Report engine can not create emitter {0}.", //$NON-NLS-1$
					emitterID); // $NON-NLS-1$
			throw new EngineException(MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION, emitterID, t);
		}
		if (emitter == null) {
			log.log(Level.SEVERE, "Report engine can not create emitter {0}.", //$NON-NLS-1$
					emitterID); // $NON-NLS-1$
			throw new EngineException(MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION, emitterID);
		}

		return emitter;
	}

	protected IReportLayoutEngine createReportLayoutEngine(String pagination, IRenderOption options) {
		IReportLayoutEngine layoutEngine = LayoutEngineFactory.createLayoutEngine(pagination);
		if (options != null) {
			Object fitToPage = renderOptions.getOption(IPDFRenderOption.FIT_TO_PAGE);
			if (fitToPage != null) {
				layoutEngine.setOption(IPDFRenderOption.FIT_TO_PAGE, fitToPage);
			}
			Object pagebreakOnly = renderOptions.getOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY);
			if (pagebreakOnly != null) {
				layoutEngine.setOption(IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY, pagebreakOnly);
			}
			Object pageOverflow = renderOptions.getOption(IPDFRenderOption.PAGE_OVERFLOW);
			if (pageOverflow != null) {
				layoutEngine.setOption(IPDFRenderOption.PAGE_OVERFLOW, pageOverflow);
			}
			Object outputDisplayNone = renderOptions.getOption(IPDFRenderOption.OUTPUT_DISPLAY_NONE);
			if (outputDisplayNone != null) {
				layoutEngine.setOption(IPDFRenderOption.OUTPUT_DISPLAY_NONE, outputDisplayNone);
			}
			Object pdfTextWrapping = renderOptions.getOption(IPDFRenderOption.PDF_TEXT_WRAPPING);
			if (pdfTextWrapping != null) {
				layoutEngine.setOption(IPDFRenderOption.PDF_TEXT_WRAPPING, pdfTextWrapping);
			}
			Object pdfFontSubstitution = renderOptions.getOption(IPDFRenderOption.PDF_FONT_SUBSTITUTION);
			if (pdfFontSubstitution != null) {
				layoutEngine.setOption(IPDFRenderOption.PDF_FONT_SUBSTITUTION, pdfFontSubstitution);
			}

			/*
			 * bidi_hcg:
			 */
			layoutEngine.setOption(IRenderOption.RTL_FLAG, renderOptions.getOption(IRenderOption.RTL_FLAG));

			Object pdfBidiProcessing = renderOptions.getOption(IPDFRenderOption.PDF_BIDI_PROCESSING);
			if (pdfBidiProcessing != null) {
				layoutEngine.setOption(IPDFRenderOption.PDF_BIDI_PROCESSING, pdfBidiProcessing);
			}

			Object pdfWordbreak = renderOptions.getOption(IPDFRenderOption.PDF_WORDBREAK);
			if (pdfWordbreak != null) {
				layoutEngine.setOption(IPDFRenderOption.PDF_WORDBREAK, pdfWordbreak);
			}

			Object dpi = renderOptions.getOption(IPDFRenderOption.DPI);
			if (dpi != null) {
				layoutEngine.setOption(IPDFRenderOption.DPI, dpi);
			}

			Object reserveDocumentPageNumbers = renderOptions.getOption(IPDFRenderOption.RESERVE_DOCUMENT_PAGE_NUMBERS);
			if (reserveDocumentPageNumbers != null) {
				layoutEngine.setOption(IPDFRenderOption.RESERVE_DOCUMENT_PAGE_NUMBERS, reserveDocumentPageNumbers);
			}

			Object repaginateForPDF = renderOptions.getOption(IPDFRenderOption.REPAGINATE_FOR_PDF);
			if (repaginateForPDF != null) {
				layoutEngine.setOption(IPDFRenderOption.REPAGINATE_FOR_PDF, repaginateForPDF);
			}

			Object userAgent = renderOptions.getOption(IHTMLRenderOption.USER_AGENT);
			if (userAgent != null) {
				layoutEngine.setOption(IHTMLRenderOption.USER_AGENT, userAgent);
			}

		}
		layoutEngine.setOption(TASK_TYPE, Integer.valueOf(taskType));
		return layoutEngine;
	}

	protected void initReportVariable() {
		ReportDesignHandle reportDesign = executionContext.getReportDesign();
		if (reportDesign != null) {
			// register the report variables
			List<VariableElementHandle> varElements = reportDesign.getPageVariables();
			for (VariableElementHandle varElement : varElements) {
				String scope = varElement.getType();
				String name = varElement.getVariableName();

				PageVariable var = new PageVariable(name, scope);
				executionContext.addPageVariable(var);

				ExpressionHandle exprHandle = varElement.getExpressionProperty(VariableElementHandle.VALUE_PROP);

				org.eclipse.birt.report.engine.ir.Expression defaultValue = createExpression(exprHandle);

				if (defaultValue != null) {
					try {
						Object value = executionContext.evaluate(defaultValue);
						var.setDefaultValue(value);
						var.setValue(value);
					} catch (BirtException ex) {
						executionContext.addException(ex);
					}
				}
			}
		}
	}

	protected void loadScript(Iterator iter) {
		while (iter.hasNext()) {
			IncludeScriptHandle includeScript = (IncludeScriptHandle) iter.next();
			String language = Script.SCRIPT_JAVASCRIPT;
			String fileName = includeScript.getFileName();
			executionContext.loadScript(language, fileName);
		}
	}

	protected void loadScripts() {
		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable != null) {
			ReportDesignHandle reportDesign = executionContext.getReportDesign();
			if (reportDesign != null) {
				// execute scripts defined in include-script element of the
				// libraries
				Iterator iter = reportDesign.includeLibraryScriptsIterator();
				loadScript(iter);
				// execute scripts defined in include-script element of this
				// report
				iter = reportDesign.includeScriptsIterator();
				loadScript(iter);
			}
		}
	}

	protected void loadDesign() {
		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable != null) {
			ReportDesignHandle reportDesign = executionContext.getReportDesign();
			if (reportDesign != null) {
				// Intialize the report
				ReportScriptExecutor.handleInitialize(reportDesign, executionContext);
			}
		}
	}

	protected void prepareDesign() {
		ReportRunnable runnable = executionContext.getRunnable();
		if (!runnable.prepared) {
			ReportDesignHandle reportDesign = executionContext.getReportDesign();
			if (reportDesign != null) {
				ScriptedDesignSearcher searcher = new ScriptedDesignSearcher(reportDesign);
				searcher.apply(reportDesign);
				boolean hasOnprepare = searcher.hasOnPrepareScript();
				if (hasOnprepare) {
					ReportRunnable newRunnable = executionContext.getRunnable().cloneRunnable();
					executionContext.updateRunnable(newRunnable);
					ReportDesignHandle newDesign = newRunnable.getReport();
					Map options = new HashMap(getAppContext());
					newDesign.setOptions(options);
					ScriptedDesignVisitor visitor = new ScriptedDesignHandler(newDesign, executionContext);
					visitor.apply(newDesign.getRoot());
					newRunnable.setPrepared(true);
				}
			}
		}
		updateOptions();
	}

	protected void updateOptions() {
		Map appContext = getAppContext();
		ReportDesignHandle handle = executionContext.getReportDesign();
		if (handle != null) {
			Map options = handle.getOptions();
			Map newOptions = new HashMap(options);
			if (!newOptions.isEmpty()) {
				Set keyset = newOptions.keySet();
				Iterator iter = keyset.iterator();
				while (iter.hasNext()) {
					Object key = iter.next();
					if (appContext.containsKey(key)) {
						newOptions.put(key, appContext.get(key));
					}
				}
			}
			handle.setOptions(newOptions);
		}
	}

	protected void startFactory() {
		ReportDesignHandle reportDesign = executionContext.getReportDesign();
		if (reportDesign != null) {
			ReportScriptExecutor.handleBeforeFactory(reportDesign, executionContext);
		}
	}

	protected void closeFactory() {
		ReportDesignHandle reportDesign = executionContext.getReportDesign();
		if (reportDesign != null) {
			ReportScriptExecutor.handleAfterFactory(reportDesign, executionContext);
		}

	}

	protected void startRender() {
		ReportDesignHandle reportDesign = executionContext.getReportDesign();
		ReportScriptExecutor.handleBeforeRender(reportDesign, executionContext);
	}

	protected void closeRender() {
		ReportDesignHandle reportDesign = executionContext.getReportDesign();
		ReportScriptExecutor.handleAfterRender(reportDesign, executionContext);
	}

	// TODO: throw out the IOException
	@Override
	public void setDataSource(IDocArchiveReader dataSource) {
		setDataSource(dataSource, null);
	}

	protected IDocArchiveReader dataSource;
	private String dataSourceReportlet;

	@Override
	public void setDataSource(IDocArchiveReader dataSource, String reportlet) {
		this.dataSource = dataSource;
		this.dataSourceReportlet = reportlet;
	}

	@Override
	public int getStatus() {
		return runningStatus;
	}

	@Override
	public List getErrors() {
		ArrayList errorList = new ArrayList();
		if (fatalError != null) {
			errorList.add(fatalError);
		}
		if (cancelReason != null) {
			errorList.add(new EngineException(cancelReason));
		}
		errorList.addAll(executionContext.getErrors());
		return errorList;
	}

	public IReportContext getReportContext() {
		return executionContext.getReportContext();
	}

	private void mergeOption(IRenderOption options, String name, Object value) {
		if (options != null) {
			if (value != null && !options.hasOption(name)) {
				options.setOption(name, value);
			}
		}
	}

	/**
	 * intialize the render options used to render the report.
	 *
	 * the render options are load from:
	 * <li>engine level default options</li>
	 * <li>engine level format options</li>
	 * <li>engine level emitter options</li>
	 * <li>task level options</li>
	 *
	 * @throws EngineException
	 *
	 */
	protected void setupRenderOption() throws EngineException {
		ExtensionManager extManager = ExtensionManager.getInstance();
		if (renderOptions != null) {
			format = renderOptions.getOutputFormat();
			emitterID = renderOptions.getEmitterID();
			if (emitterID == null && format == null) {
				// using the default format and emitter
				format = RenderOption.OUTPUT_FORMAT_HTML;
			}
			if (emitterID != null) {
				// the user use the emitter id to define the output format
				if (!extManager.isValidEmitterID(emitterID)) {
					log.log(Level.SEVERE, MessageConstants.INVALID_EMITTER_ID, emitterID);
					throw new EngineException(MessageConstants.INVALID_EMITTER_ID, emitterID);
				}
				String emitterFormat = extManager.getFormat(emitterID);
				if (format != null) {
					// check if the format matches with the emitter id
					if (!emitterFormat.equalsIgnoreCase(format)) {
						throw new EngineException(MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format);
					}
				}
				format = emitterFormat;
			} else {
				// the user defines the format
				String innerFormat = extManager.getSupportedFormat(format);
				if (innerFormat == null) {
					log.log(Level.SEVERE, MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format);
					throw new UnsupportedFormatException(MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format);
				}
				format = innerFormat;
				emitterID = engine.getConfig().getDefaultEmitter(format);
				if (emitterID == null || !extManager.isValidEmitterID(emitterID)) {
					emitterID = extManager.getEmitterID(format);
				}
			}

			renderOptions.setEmitterID(emitterID);
			renderOptions.setOutputFormat(format);
		}

		// copy the old setting to render options
		Map appContext = executionContext.getAppContext();
		if (IRenderOption.OUTPUT_EMITTERID_PDF.equals(emitterID)) {
			Object renderContext = appContext.get(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT);
			if (renderContext instanceof PDFRenderContext) {
				PDFRenderContext pdfContext = (PDFRenderContext) renderContext;
				mergeOption(renderOptions, PDFRenderOption.BASE_URL, pdfContext.getBaseURL());
				mergeOption(renderOptions, PDFRenderOption.FONT_DIRECTORY, pdfContext.getFontDirectory());
				mergeOption(renderOptions, PDFRenderOption.SUPPORTED_IMAGE_FORMATS,
						pdfContext.getSupportedImageFormats());
				mergeOption(renderOptions, PDFRenderOption.IS_EMBEDDED_FONT,
						Boolean.valueOf(pdfContext.isEmbededFont()));
			}
		} else {
			Object renderContext = appContext.get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT);
			if (renderContext instanceof HTMLRenderContext) {
				HTMLRenderContext htmlContext = (HTMLRenderContext) renderContext;

				mergeOption(renderOptions, HTMLRenderOption.BASE_IMAGE_URL, htmlContext.getBaseImageURL());
				mergeOption(renderOptions, HTMLRenderOption.BASE_URL, htmlContext.getBaseURL());
				mergeOption(renderOptions, HTMLRenderOption.IMAGE_DIRECTROY, htmlContext.getImageDirectory());
				if (FORMAT_HTML.equals(format)) {
					mergeOption(renderOptions, HTMLRenderOption.SUPPORTED_IMAGE_FORMATS,
							htmlContext.getSupportedImageFormats());
				}
			}
		}

		// setup the render options from:
		// engine default, format default, emitter default and task options
		HashMap options = new HashMap();

		// try to get the default render option from the engine config.
		HashMap configs = engine.getConfig().getEmitterConfigs();
		// get the default format of the emitters, the default format key is
		// IRenderOption.OUTPUT_FORMAT;
		IRenderOption defaultOptions = (IRenderOption) configs.get(IEngineConfig.DEFAULT_RENDER_OPTION);
		if (defaultOptions == null) {
			defaultOptions = (IRenderOption) configs.get(IRenderOption.OUTPUT_FORMAT_HTML);
		}
		if (defaultOptions != null) {
			options.putAll(defaultOptions.getOptions());
		}

		// try to get the render options by the format
		IRenderOption formatOptions = (IRenderOption) configs.get(format);
		if (formatOptions != null) {
			options.putAll(formatOptions.getOptions());
		}

		// try to load the configs through the emitter id
		if (emitterID != null) {
			IRenderOption emitterOptions = (IRenderOption) configs.get(emitterID);
			if (emitterOptions != null) {
				options.putAll(emitterOptions.getOptions());
			}
		}

		// load the options from task level options
		if (renderOptions != null) {
			options.putAll(renderOptions.getOptions());
		}

		// setup the render options used by this task
		IRenderOption allOptions = new RenderOption(options);
		executionContext.setRenderOption(allOptions);

		// copy the new setting to old APIs
		if (IRenderOption.OUTPUT_EMITTERID_PDF.equals(emitterID)) {
			Object renderContext = appContext.get(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT);
			if (renderContext == null) {
				PDFRenderOption pdfOptions = new PDFRenderOption(allOptions);
				PDFRenderContext pdfContext = new PDFRenderContext();
				pdfContext.setBaseURL(pdfOptions.getBaseURL());
				pdfContext.setEmbededFont(pdfOptions.isEmbededFont());
				pdfContext.setFontDirectory(pdfOptions.getFontDirectory());
				pdfContext.setSupportedImageFormats(pdfOptions.getSupportedImageFormats());
				appContext.put(EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, pdfContext);
			}
		} else {
			Object renderContext = appContext.get(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT);
			if (renderContext == null) {
				HTMLRenderContext htmlContext = new HTMLRenderContext();
				HTMLRenderOption htmlOptions = new HTMLRenderOption(allOptions);
				htmlContext.setBaseImageURL(htmlOptions.getBaseImageURL());
				htmlContext.setBaseURL(htmlOptions.getBaseURL());
				htmlContext.setImageDirectory(htmlOptions.getImageDirectory());
				htmlContext.setSupportedImageFormats(htmlOptions.getSupportedImageFormats());
				htmlContext.setRenderOption(allOptions);
				appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, htmlContext);
			}
		}
	}

	protected void initializeContentEmitter(IContentEmitter emitter) throws BirtException {
		// create the emitter services object that is needed in the emitters.
		HashMap configs = engine.getConfig().getEmitterConfigs();
		IReportContext reportContext = executionContext.getReportContext();
		IRenderOption options = executionContext.getRenderOption();
		EngineEmitterServices services = new EngineEmitterServices(reportContext, options, configs);

		// emitter is not null
		emitter.initialize(services);
	}

	@Override
	public int getTaskType() {
		return taskType;
	}

	protected void changeStatusToRunning() {
		runningStatus = STATUS_RUNNING;
	}

	protected void changeStatusToStopped() {
		if (cancelFlag) {
			runningStatus = STATUS_CANCELLED;
		} else if (fatalError != null || executionContext.hasErrors()) {
			runningStatus = STATUS_FAILED;
		} else {
			runningStatus = STATUS_SUCCEEDED;
		}
	}

	protected void handleFatalExceptions(Throwable t) throws EngineException {
		if (t instanceof EngineException) {
			log.log(Level.SEVERE, "An error happened while running the report. Cause:", t); //$NON-NLS-1$
			fatalError = (EngineException) t;
			throw fatalError;
		} else if (t instanceof Exception) {
			log.log(Level.SEVERE, "An error happened while running the report. Cause:", t); //$NON-NLS-1$
			fatalError = new EngineException(MessageConstants.REPORT_RUN_ERROR, t); // $NON-NLS-1$
			throw fatalError;
		} else if (t instanceof OutOfMemoryError) {
			log.log(Level.SEVERE, "There is insufficient memory to execute this report."); //$NON-NLS-1$
			fatalError = new EngineException(MessageConstants.REPORT_RUN_ERROR, t);
			throw (OutOfMemoryError) t;
		} else
		// Throwable t
		{
			log.log(Level.SEVERE, "Error happened while running the report.", t); //$NON-NLS-1$
			fatalError = new EngineException(MessageConstants.REPORT_RUN_ERROR, t); // $NON-NLS-1$
			throw fatalError;
		}
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public void setLogger(Logger logger) {
		if (logger == null || EngineLogger.isValidLogger(logger)) {
			throw new IllegalArgumentException(
					"the logger can not be NULL or children or in namespace of org.eclipse.birt");
		}
		EngineLogger.setThreadLogger(logger);
		this.log = logger;
		this.executionContext.setLogger(logger);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.IRunTask#setPageHandler(org.eclipse.birt.
	 * report.engine.api.IPageHandler)
	 */
	public void setPageHandler(IPageHandler callback) {
		this.pageHandler = callback;
	}

	protected void switchToOsgiClassLoader() {
		platformContext = Platform.enterPlatformContext();
	}

	protected void switchClassLoaderBack() {
		if (platformContext != null) {
			Platform.exitPlatformContext(platformContext);
			platformContext = null;
		}
	}

	protected IResourceLocator getResourceLocator() {
		ModuleHandle handle = executionContext.getDesign();
		if (handle != null) {
			ModuleOption opts = handle.getModule().getOptions();
			if (opts != null) {
				return opts.getResourceLocator();
			}
		}
		return null;
	}

	/**
	 *
	 * @throws EngineException
	 */
	protected void loadDataSource() throws EngineException {
		boolean refreshData = false;
		Object obj = getAppContext().get(EngineConstants.REFRESH_DATA);
		if (obj != null && obj.equals(Boolean.TRUE)) {
			refreshData = true;
		}
		// we only need setup the data source for the task which has dataSource
		if (dataSource == null || refreshData) {
			return;
		}

		// try to open the dataSource as report document
		try {
			HashMap options = new HashMap();
			IResourceLocator resourceLocator = getResourceLocator();
			if (resourceLocator != null) {
				options.put(ModuleOption.RESOURCE_LOCATOR_KEY, resourceLocator);
			}
			ReportDocumentReader document = new ReportDocumentReader(null, engine, dataSource, true, options);
			try {
				// TODO Temp solution, we need set this IReportDocument in
				// context directly.
				String buildNumber = document.getProperty(ReportDocumentConstants.BIRT_ENGINE_BUILD_NUMBER_KEY);
				Map appContext = getAppContext();
				if (appContext != null) {
					appContext.put(ReportDocumentConstants.BIRT_ENGINE_BUILD_NUMBER_KEY, buildNumber);
				}
				// load the parameter values from report document
				Map values = document.getParameterValues(executionContext.getApplicationClassLoader());
				Map texts = document.getParameterDisplayTexts();
				setParameterValues(values);
				setParameterDisplayTexts(texts);

				if (dataSourceReportlet == null) {
					executionContext.setDataSource(new DocumentDataSource(dataSource));
					return;

				}
				loadReportletDataSource(document, dataSource, dataSourceReportlet);
			} finally {
				document.close();
			}
		} catch (IOException ioex) {
			throw new EngineException(ioex.getMessage(), ioex);
		}
	}

	private void loadReportletDataSource(ReportDocumentReader document, IDocArchiveReader dataSource,
			String reportletBookmark) throws EngineException, IOException {

		InstanceID reportletIid = null;
		if (document.isReporltetDocument()) {
			String bookmark = document.getReportletBookmark();
			if (!reportletBookmark.equals(bookmark)) {
				throw new EngineException(
						"The user must specify the same reportlet with the one used to generate the document");
			}
			reportletIid = document.getReportletInstanceID();
		} else {
			// load the result set used by reportlet
			long offset = document.getBookmarkOffset(reportletBookmark);
			if (offset == -1) {
				throw new EngineException("The user specified reportlet {0} doesn''t exits in the report document",
						new Object[] { reportletBookmark });
			}

			ClassLoader loader = document.getClassLoader();
			RAInputStream in = dataSource.getInputStream(ReportDocumentConstants.CONTENT_STREAM);
			try (in) {
				ReportContentReaderV3 reader = new ReportContentReaderV3(new ReportContent(), in, loader);
				try {
					LinkedList<InstanceID> iids = new LinkedList<>();
					while (offset != -1) {
						IContent content = reader.readContent(offset);
						iids.addFirst(content.getInstanceID());
						offset = ((DocumentExtension) content.getExtension(IContent.DOCUMENT_EXTENSION)).getParent();
					}

					for (InstanceID iid : iids) {
						if (reportletIid == null) {
							reportletIid = iid;
						} else {
							reportletIid = new InstanceID(reportletIid, iid);
						}
					}
				} finally {
					reader.close();
				}
			}
		}
		// set the datasources
		executionContext.setDataSource(new DocumentDataSource(dataSource, reportletBookmark, reportletIid));
	}

	protected void updateRtLFlag() throws EngineException {
		// get RtL flag from renderOptions
		if (renderOptions == null) {
			return;
		}
		IReportRunnable runnable = executionContext.getRunnable();
		if (runnable == null) {
			return;
		}
		ReportDesignHandle handle = (ReportDesignHandle) runnable.getDesignHandle();
		if (handle != null) {
			Object bidiFlag = renderOptions.getOption(IRenderOption.RTL_FLAG);
			if (Boolean.TRUE.equals(bidiFlag)) {
				if (!handle.isDirectionRTL()) {
					updateBidiStyle(true);
				}
			} else if (Boolean.FALSE.equals(bidiFlag)) {
				if (handle.isDirectionRTL()) {
					updateBidiStyle(false);
				}
			}
		}
	}

	private void updateBidiStyle(boolean isRtl) {
		Report report = executionContext.getReport();
		AbstractStyle rootStyle = (AbstractStyle) report.getStyles().get(report.getRootStyleName());
		if (rootStyle != null) {
			rootStyle.setDirection(
					isRtl ? DesignChoiceConstants.BIDI_DIRECTION_RTL : DesignChoiceConstants.BIDI_DIRECTION_LTR);
		}
	}

	protected IReportExecutor createReportExtensionExecutor(IReportExecutor executor) throws EngineException {
		// prepare the extension executor
		String[] extensions = executionContext.getEngineExtensions();
		if (extensions != null) {
			ArrayList<IContentProcessor> processors = new ArrayList<>();
			EngineExtensionManager manager = executionContext.getEngineExtensionManager();
			for (String extName : extensions) {
				IGenerateExtension genExt = manager.getGenerateExtension(extName);
				if (genExt != null) {
					IContentProcessor processor = genExt.createGenerateProcessor();
					if (processor != null) {
						processors.add(processor);
					}
				}
			}
			if (!processors.isEmpty()) {
				return new ReportExtensionExecutor(executionContext, executor,
						processors.toArray(new IContentProcessor[processors.size()]));
			}
		}
		return executor;
	}

	@Override
	public void setUserACL(String[] acls) {
		if (acls != null) {
			String[] strippedAcls = strip(acls);
			userAcls = strippedAcls;
			executionContext.getAppContext().put(EngineConstants.USER_ACL_KEY, strippedAcls);

		} else {
			userAcls = null;
			executionContext.getAppContext().put(EngineConstants.USER_ACL_KEY, null);
		}
	}

	protected String[] strip(String[] acls) {
		ArrayList<String> strippedAcls = new ArrayList<>();
		for (int i = 0; i < acls.length; i++) {
			String acl = acls[i];
			if (acl != null) {
				String strippedAcl = acl.trim();
				if (strippedAcl.length() > 0) {
					strippedAcls.add(strippedAcl);
				}
			}
		}
		return strippedAcls.toArray(new String[strippedAcls.size()]);
	}

	@Override
	public void setProgressMonitor(IProgressMonitor monitor) {
		progressMonitor = monitor;
		executionContext.setProgressMonitor(monitor);
	}

	@Override
	public void setStatusHandler(IStatusHandler handler) {
		statusHandler = handler;
	}

	public IStatusHandler getStatusHandler() {
		return statusHandler;
	}
}
