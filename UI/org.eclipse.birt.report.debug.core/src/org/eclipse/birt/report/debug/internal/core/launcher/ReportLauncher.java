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

package org.eclipse.birt.report.debug.internal.core.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.debug.core.i18n.Messages;
import org.eclipse.birt.report.debug.internal.core.vm.ReportVMServer;
import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMContextData;
import org.eclipse.birt.report.debug.internal.core.vm.VMException;
import org.eclipse.birt.report.debug.internal.core.vm.VMListener;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.mozilla.javascript.Context;

/**
 * Run this class when debug the script.
 */
public class ReportLauncher implements VMListener, IReportLaunchConstants {
	public static final String NULLVALUE = "NULL NULL NULL NULL NULL VALUE"; //$NON-NLS-1$
	private static final Logger logger = Logger.getLogger(ReportLauncher.class.getName());

	private static final String RPTDOC_SUFFIX = "rptdocument"; //$NON-NLS-1$
	private static final String RPTDOC_SUFFIXA = "RPTDOCUMENT"; //$NON-NLS-1$

	private IReportEngine engine;

	private EngineConfig engineConfig;
	private Map paramValues = new HashMap();

	private String reportDesignFile;
	private boolean debugScript;
	private String targetFormat;

	private ReportVMServer server;

	private IRunTask runTask;
	private IRenderTask renderTask;
	private IRunAndRenderTask runAndRenderTask;

	IGetParameterDefinitionTask task;
	List allParameters;
	private String engineType = IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY;
	private TaskConfig taskConfig;

	public void setTaskConfig(TaskConfig taskConfig) {
		this.taskConfig = taskConfig;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public IReportEngine getEngine() {
		return engine;
	}

	public ReportLauncher() {
		reportDesignFile = getFileName();
		debugScript = (getDebugType() & DEBUG_TYPE_JAVA_SCRIPT) == DEBUG_TYPE_JAVA_SCRIPT;
		targetFormat = getTargetFormat();

		// initParameters( );
	}

	public static void main(String[] args) {
		new ReportLauncher().run();
	}

	private static int getListenPort() {
		String str = System.getProperty(ATTR_LISTEN_PORT);
		if (str == null) {
			throw new Error(Messages.getString("ReportLauncher.PortValueAbsent"));//$NON-NLS-1$
		}

		return Integer.parseInt(str);
	}

	private static String getFileName() {
		return System.getProperty(ATTR_REPORT_FILE_NAME);
	}

	private static String getEngineHome() {
		return System.getProperty(ATTR_ENGINE_HOME);
	}

	public static String getResourceFolder() {
		return System.getProperty(ATTR_RESOURCE_FOLDER);
	}

	private static String getOutputFolder() {
		return System.getProperty(ATTR_TEMP_FOLDER);
	}

	private static String getTargetFormat() {
		return System.getProperty(ATTR_TARGET_FORMAT);
	}

	private static int getTaskType() {
		String str = System.getProperty(ATTR_TASK_TYPE);
		if (str == null) {
			return DEFAULT_TASK_TYPE;
		}

		return Integer.parseInt(str);
	}

	private static int getDebugType() {
		String str = System.getProperty(ATTR_DEBUG_TYPE);
		if (str == null) {
			return DEFAULT_DEBUG_TYPE;
		}

		return Integer.parseInt(str);
	}

	private static String getUserClassPath() {
		return System.getProperty(ATTR_USER_CLASS_PATH);
	}

	private void initParameters() {

		if (getFileName().endsWith("." + RPTDOC_SUFFIX) || getFileName().endsWith("." + RPTDOC_SUFFIXA)) {
			return;
		}
		if (allParameters == null) {
			allParameters = new ArrayList();

			IReportRunnable runnable;
			try {
				runnable = engine.openReportDesign(reportDesignFile);
				List list = runnable.getDesignHandle().getRoot().getParametersAndParameterGroups();
				initAllParameters(allParameters, list);
			} catch (EngineException e) {
			}
		}
		Properties propertys = System.getProperties();
		Iterator itor = propertys.keySet().iterator();
		while (itor.hasNext()) {
			String str = (String) itor.next();
			if (str.startsWith(ATTR_PARAMRTER)) {
				addParameter(paramValues, str, propertys.getProperty(str));
			} else if (str.startsWith(ATTR_MULPARAMRTER + "0")) //$NON-NLS-1$
			{
				addMulitipleParameter(paramValues, str, propertys.getProperty(str));
			}
		}
	}

	private void addParameter(Map map, String key, String value) {
		String temp = key.substring(ATTR_PARAMRTER.length());
		// map.put( temp, value );
		map.put(temp, getParameterObject(temp, value));
	}

	private Date doValidateDateTimeByPattern(String value, String dataType) throws ValidationValueException {
		try {
			long time = Long.parseLong(value);
			return new Date(time);
		} catch (Exception e) {
			Object obj = null;
			try {
				obj = convert(value, dataType);
			} catch (BirtException e1) {
			}
			if (obj instanceof Date) {
				return (Date) obj;
			}
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME);
		}
	}

	private java.sql.Date doValidateSqlDateTimeByPattern(String value, String dataType)
			throws ValidationValueException {
		try {
			long time = Long.parseLong(value);
			return new java.sql.Date(time);
		} catch (Exception e) {
			Object obj = null;
			try {
				obj = convert(value, dataType);
			} catch (BirtException e1) {
			}
			if (obj instanceof java.sql.Date) {
				return (java.sql.Date) obj;
			}
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME);
		}
	}

	static private java.sql.Time doValidateTimeDateTimeByPattern(String value, String dataType)
			throws ValidationValueException {
		try {
			long time = Long.parseLong(value);
			return new java.sql.Time(time);
		} catch (Exception e) {
			Object obj = null;
			try {
				obj = convert(value, dataType);
			} catch (BirtException e1) {
			}
			if (obj instanceof java.sql.Time) {
				return (java.sql.Time) obj;
			}
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME);
		}
	}

	private Object getParameterObject(String key, String value) {
		if (NULLVALUE.equals(value)) {
			value = null;
		}
		ParameterHandle temp = findParameter(key);
		if (temp instanceof ScalarParameterHandle) {
			ScalarParameterHandle handle = (ScalarParameterHandle) temp;
			// String formate = handle.getPattern( );
			String dataType = handle.getDataType();

			try {
				if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {

					return doValidateSqlDateTimeByPattern(value, dataType);

				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
					return doValidateTimeDateTimeByPattern(value, dataType);

				} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
					return doValidateDateTimeByPattern(value, dataType);
				}
			} catch (ValidationValueException e) {
				return value;
			}

			try {
				return convert(value, dataType);
			} catch (BirtException e) {
				return value;
			}
		} else {
			return value;
		}
		// return null;
	}

	public static Object convert(Object value, String dataType) throws BirtException {
		if (value == null) {
			return value;
		}
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(dataType)) {
			return DataTypeUtil.toBoolean(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
			return DataTypeUtil.toDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(dataType)) {
			return DataTypeUtil.toSqlDate(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(dataType)) {
			return DataTypeUtil.toSqlTime(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(dataType)) {
			return DataTypeUtil.toBigDecimal(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(dataType)) {
			return DataTypeUtil.toDouble(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(dataType)) {
			return DataTypeUtil.toString(value);
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(dataType)) {
			return DataTypeUtil.toInteger(value);
		}
		return value;
	}

	private ParameterHandle findParameter(String key) {
		for (int i = 0; i < allParameters.size(); i++) {
			Object obj = allParameters.get(i);

			if (obj instanceof ParameterHandle) {
				ParameterHandle handle = (ParameterHandle) obj;
				if (handle.getName().equals(key)) {
					return handle;
				}
			}
		}
		return null;
	}

	private List initAllParameters(List allParameters, List list) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof ParameterHandle) {
				allParameters.add(obj);
			} else if (obj instanceof ParameterGroupHandle) {
				initAllParameters(allParameters, ((ParameterGroupHandle) obj).getParameters().getContents());
			}
		}

		return allParameters;
	}

	private void addMulitipleParameter(Map map, String key, String value) {
		List list = new ArrayList();
		String temp = key.substring(ATTR_MULPARAMRTER.length() + 1);
		list.add(getParameterObject(temp, value));

		int i = 1;
		Properties propertys = System.getProperties();
		Set set = propertys.keySet();
		while (set.contains(ATTR_MULPARAMRTER + i + temp)) {
			// list.add( propertys.get( ATTR_MULPARAMRTER + i + temp ) );

			list.add(getParameterObject(temp, (String) propertys.get(ATTR_MULPARAMRTER + i + temp)));
			i++;
		}
		Object[] objs = new Object[list.size()];
		list.toArray(objs);
		map.put(temp, objs);
	}

	public void run() {
		init();
		renderReport();
		dispose();
		System.exit(EXIT_OK);
	}

	private void init() {
		if (debugScript) {
			server = new ReportVMServer();
			server.addVMListener(this);
			Context cx = Context.enter();

			try {
				server.start(getListenPort(), cx);
			} catch (VMException e1) {
				throw new Error(Messages.getString("ReportLauncher.FailToStartDebugServer"));//$NON-NLS-1$
			}
		}

		engineConfig = new LauncherEngineConfig();
		engineConfig.setEngineHome(getEngineHome());
		if (getResourceFolder() != null) {
			engineConfig.setResourcePath(getResourceFolder());
		}

		try {
			Platform.startup(engineConfig);
		} catch (BirtException e) {
			throw new Error(Messages.getString("ReportLauncher.FailToStartReportPlatform"));//$NON-NLS-1$
		}

		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject(engineType);

		configEngine();

		this.engine = factory.createReportEngine(engineConfig);
		engine.changeLogLevel(Level.WARNING);

		initParameters();
	}

	private void configEngine() {
		String userClassPath = getUserClassPath();

		logger.info(Messages.getString("ReportLauncher.UserClassPathReceived") + userClassPath); //$NON-NLS-1$

		// clear dev user classpath state
		System.clearProperty(EngineConstants.PROJECT_CLASSPATH_KEY);

		if (userClassPath != null) {
			// set user classpath for engine
			engineConfig.getAppContext().put(EngineConstants.PROJECT_CLASSPATH_KEY, userClassPath);

			// also set dev user classpath
			System.setProperty(EngineConstants.PROJECT_CLASSPATH_KEY, userClassPath);
		}
	}

	private void renderReport() {
		String outputFolder = getOutputFolder();

		// TODO validate parameters

		int taskType = getTaskType();

		boolean isRunAndRenderTask = (taskType & TASK_TYPE_RUN_AND_RENDER) != 0;
		boolean isRunTask = (taskType & TASK_TYPE_RUN) != 0;
		boolean isRenderTask = (taskType & TASK_TYPE_RENDER) != 0;

		try {
			if (isRunAndRenderTask) {
				createReport(reportDesignFile, outputFolder, paramValues);
			} else {
				String reportDocumentFile = reportDesignFile;

				if (isRunTask) {
					reportDocumentFile = createReportDocument(reportDesignFile, outputFolder, paramValues);
				}

				if (isRenderTask) {
					createReportOutput(reportDocumentFile, outputFolder);
				}
			}
		} catch (EngineException e) {
			logger.log(Level.SEVERE, Messages.getString("ReportLauncher.EngineException"), e); //$NON-NLS-1$
		} catch (IOException e) {
			logger.log(Level.SEVERE, Messages.getString("ReportLauncher.IOException"), e); //$NON-NLS-1$
		}
	}

	private void createReport(String reportDesignFile, String outputFolder, Map parameters)
			throws IOException, EngineException {
		String outputFile = getOutputFileName(outputFolder, new File(reportDesignFile).getName(), targetFormat);

		IReportRunnable report = engine.openReportDesign(reportDesignFile);
		runAndRenderTask = engine.createRunAndRenderTask(report);

		IRenderOption renderOption = new HTMLRenderOption();
		renderOption.setOutputFileName(outputFile);
		renderOption.setOutputFormat(targetFormat);

		try {
			if (parameters != null) {
				runAndRenderTask.setParameterValues(parameters);
			}
			runAndRenderTask.setAppContext(Collections.EMPTY_MAP);
			runAndRenderTask.setRenderOption(renderOption);
			configTask(runAndRenderTask);
			runAndRenderTask.run();
		} catch (EngineException e) {
			throw e;
		} finally {
			runAndRenderTask.close();
			report = null;
			runAndRenderTask = null;
		}
	}

	private String createReportDocument(String reportDesignFile, String outputFolder, Map parameters)
			throws IOException, EngineException {
		String reportDocumentFile = getOutputFileName(outputFolder, new File(reportDesignFile).getName(),
				RPTDOC_SUFFIX);

		IDocArchiveWriter archive = new FileArchiveWriter(reportDocumentFile);
		IReportRunnable report = engine.openReportDesign(reportDesignFile);
		runTask = engine.createRunTask(report);
		try {
			if (parameters != null) {
				runTask.setParameterValues(parameters);
			}
			runTask.setAppContext(Collections.EMPTY_MAP);
			configTask(runTask);
			runTask.run(archive);
		} catch (EngineException e) {
			throw e;
		} finally {
			runTask.close();
			report = null;
			runTask = null;
		}
		return reportDocumentFile;
	}

	private void createReportOutput(String reportDocumentFile, String outputFolder)
			throws EngineException, IOException {
		IReportDocument document = engine.openReportDocument(reportDocumentFile);
		renderTask = engine.createRenderTask(document);

		String outputFile = getOutputFileName(outputFolder, new File(reportDesignFile).getName(), targetFormat);

		IRenderOption renderOption = new HTMLRenderOption();
		renderOption.setOutputFileName(outputFile);
		renderOption.setOutputFormat(targetFormat);

		try {
			renderTask.setRenderOption(renderOption);
			configTask(renderTask);
			renderTask.render();
		} catch (EngineException e) {
			throw e;
		} finally {
			renderTask.close();
			renderTask = null;
			document.close();
			document = null;
		}
	}

	public static String getOutputFileName(String outputFolder, String fileName, String suffix) {
		return outputFolder + File.separator + fileName + "." //$NON-NLS-1$
				+ suffix;
	}

	public void handleEvent(int eventCode, VMContextData context) {
		if (eventCode == VMConstants.VM_TERMINATED) {
			dispose();
		}
	}

	private void dispose() {
		try {
			if (runTask != null) {
				runTask.cancel();
				runTask.close();
				runTask = null;
			}
		} catch (Throwable e) {
			// do nothing
		}

		try {
			if (renderTask != null) {
				renderTask.cancel();
				renderTask.close();
				renderTask = null;
			}
		} catch (Throwable e) {
			// do nothing
		}

		try {
			if (runAndRenderTask != null) {
				runAndRenderTask.cancel();
				runAndRenderTask.close();
				runAndRenderTask = null;
			}
		} catch (Throwable e) {
			// do nothing
		}

		if (server != null) {
			server.shutdown(Context.enter());
		}

		Platform.shutdown();
	}

	private void configTask(IEngineTask task) {
		if (taskConfig != null) {
			taskConfig.configTask(task);
		}
	}

	public interface TaskConfig {
		void configTask(IEngineTask task);
	}
}
