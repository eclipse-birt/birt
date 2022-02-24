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

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.report.engine.api.impl.ParameterSelectionChoice;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Defines a standalone reporting application that uses
 * <code>StandaloneReportEngine</code> class. This application allows running a
 * report to output in one or multiple formats.
 * <p>
 * Report parameters are handled as command line parameters. Currently, only
 * scalar parameters are handled.
 * 
 */
public class ReportRunner {

	// The static logger
	static protected Logger logger = Logger.getLogger(ReportRunner.class.getName());

	/**
	 * the input paramters
	 */
	protected String[] args; // The command line arguments
	/**
	 * the source input ,can be designName or reportArchive name
	 */
	protected String source; // Name of the design file
	/**
	 * The target file name
	 */
	protected String targetFile = null;
	/**
	 * execution mode, one of the Run, RunAndRender and Render. the default mode is
	 * RunAndRender
	 */
	protected String mode = "RunAndRender"; // the execution mode, one of the
	/**
	 * the output locale, used in Run and RunAndRender modes.
	 */
	protected String locale = "en"; // Application running locale //$NON-NLS-1$

	/**
	 * the output format, used in Render and RunAndRender.
	 */
	protected String format = "html"; // The output format, defaults to HTML
	/**
	 * used to decorate the HTML output, used in Render and RunAndRender mode.
	 */
	protected String htmlType = "HTML"; // The type of html
	/**
	 * the output encoding, used in html format in Render and RunAndRender modes.
	 */
	protected String encoding = "utf-8"; // the targe encoding
	/**
	 * paramters used to execute the report, used in Run and RunAndRender modes.
	 */
	protected HashMap params = new HashMap(); // the input params
	/**
	 * output page number, used in Render mode.
	 */
	protected long pageNumber = -1;

	/**
	 * engine used to execute the tasks.
	 */
	private IReportEngine engine;

	/**
	 * Constructor of ReportRunner
	 * 
	 * @param args - application arguments
	 */
	public ReportRunner(String[] args) {
		this.args = (String[]) args.clone();
	}

	/**
	 * Main function.
	 * 
	 * @param args - application argumetns.
	 */
	public static void main(String[] args) {
		int result = new ReportRunner(args).execute();
		System.exit(result);
	}

	/**
	 * Check if the arguments are valid. If yes, continue to execuate the report. If
	 * no, simply return.
	 */
	public int execute() {
		// Process command line arguments
		if (parseHelpOptions() > 0)
			return 0;
		try {
			parseNormalOptions();
			// startup the platform
			if (engine == null) {
				EngineConfig config = createEngineConfig();
				Platform.startup(config);
				IReportEngineFactory factory = (IReportEngineFactory) Platform
						.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
				engine = factory.createReportEngine(config);

				// JRE default level is INFO, which may reveal too much internal
				// logging
				// information.
				engine.changeLogLevel(Level.WARNING);
			}

			if ("Run".equalsIgnoreCase(mode)) {
				return runReport();
			} else if ("Render".equalsIgnoreCase(mode)) {
				return renderReport();
			} else {
				return runAndRenderReport();
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "exception in parsing the paramters", ex);
			return -1;
		} finally {
			Platform.shutdown();
		}

	}

	/**
	 * Execute the report design which includes: <br>
	 * 1. Read the input design and create the task. <br>
	 * 2. Set report render options (including format, locale, output file name
	 * etc). <br>
	 * 3. Run the task.
	 */
	protected int runAndRenderReport() {
		try {
			// parse the source to get the report runnable
			IReportRunnable runnable = engine.openReportDesign(source);
			// validate the input paramter values
			HashMap inputValues = evaluateParameterValues(runnable);

			IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);
			Iterator iter = inputValues.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String paraName = (String) entry.getKey();
				ParameterAttribute pa = (ParameterAttribute) entry.getValue();
				Object valueObject = pa.getValue();
				if (valueObject instanceof Object[]) {
					Object[] valueArray = (Object[]) valueObject;
					String[] displayTextArray = (String[]) pa.getDisplayText();
					task.setParameter(paraName, valueArray, displayTextArray);
				} else {
					task.setParameter(paraName, pa.getValue(), (String) pa.getDisplayText());
				}
			}

			// set report render options
			IRenderOption options = new RenderOption();

			options.setOutputFormat(format);

			// setup the output file
			options.setOutputFileName(targetFile);

			// setup the application context
			if (format.equalsIgnoreCase("html")) {
				HTMLRenderOption htmlOptions = new HTMLRenderOption(options);
				if ("ReportletNoCSS".equals(htmlType))
					htmlOptions.setEmbeddable(true);
				// setup the output encoding
				htmlOptions.setUrlEncoding(encoding);
				htmlOptions.setHtmlPagination(true);
				htmlOptions.setImageDirectory("image"); //$NON-NLS-1$
			}

			// set the render options
			task.setRenderOption(options);

			// setup the locale
			task.setLocale(getLocale(locale));

			task.run();
			return 0;
		} catch (EngineException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	/**
	 * running the report to create the report document
	 */
	protected int runReport() {
		try {
			// parse the source to get the report runnable
			IReportRunnable runnable = engine.openReportDesign(source);
			// create the report task
			IRunTask task = engine.createRunTask(runnable);

			// set the paramter values
			HashMap inputValues = evaluateParameterValues(runnable);
			Iterator iter = inputValues.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String paraName = (String) entry.getKey();
				ParameterAttribute pa = (ParameterAttribute) entry.getValue();
				Object valueObject = pa.getValue();
				if (valueObject instanceof Object[]) {
					Object[] valueArray = (Object[]) valueObject;
					String[] displayTextArray = (String[]) pa.getDisplayText();
					task.setParameter(paraName, valueArray, displayTextArray);
				} else {
					task.setParameter(paraName, pa.getValue(), (String) pa.getDisplayText());
				}
			}

			// set the application context
			task.setAppContext(new HashMap());

			// run the task to create the report document
			task.run(targetFile);

			// close the task.
			task.close();

			return 0;
		} catch (org.eclipse.birt.report.engine.api.EngineException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	/**
	 * render the report.
	 */
	protected int renderReport() {
		try {
			// use the archive to open the report document
			IReportDocument document = engine.openReportDocument(source);

			// create the render task
			IRenderTask task = engine.createRenderTask(document);

			// set report render options
			IRenderOption options = new RenderOption();

			// set the output format
			options.setOutputFormat(format);

			// setup the application context
			if (format.equalsIgnoreCase("html")) {
				HTMLRenderOption htmlOptions = new HTMLRenderOption(options);
				if ("ReportletNoCSS".equals(htmlType))
					htmlOptions.setEmbeddable(true);
				htmlOptions.setImageDirectory("image"); //$NON-NLS-1$
				// setup the output encoding
				htmlOptions.setUrlEncoding(encoding);
			}

			// set the render options
			task.setRenderOption(options);

			// setup the locale
			task.setLocale(getLocale(locale));

			// setup the output file
			if (pageNumber <= 0) {
				int extPos = targetFile.lastIndexOf('.');
				assert extPos != -1;
				String pathName = targetFile.substring(0, extPos);
				String extName = targetFile.substring(extPos);
				long pageCount = document.getPageCount();
				if (pageCount == 1) {
					options.setOutputFileName(targetFile);
					task.setPageNumber(1); // $NON-NLS-1$
					task.render();
				} else {
					for (long i = 1; i <= pageCount; i++) {
						String fileName = pathName + '_' + (i) + extName;
						options.setOutputFileName(fileName);
						task.setPageNumber(i);
						task.render();
					}
				}
			} else {
				options.setOutputFileName(targetFile);
				task.setPageNumber(pageNumber);
				task.render();
			}
			task.close();
			return 0;
		} catch (BirtException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return -1;
		}
	}

	/**
	 * print out the command line usage.
	 * 
	 */
	protected void printGeneralUsage() {
		System.out.println("Help for ReportRunner"); //$NON-NLS-1$
		System.out.println(""); //$NON-NLS-1$

		System.out.println(
				"--mode/-m [run|render|runrender] [options] [rptdesign|rptdocument]\n\tThe default mode is runrender.");
		System.out.println("To see options for run mode, use:");
		System.out.println("\t--help/-h run");
		System.out.println("To see options for render mode, use:");
		System.out.println("\t--help/-h render");
		System.out.println("To see options for runrender mode, use:");
		System.out.println("\t--help/-h runrender");
		System.out.println("Print current message, use --help/-h");
	}

	protected void printRunUsage() {
		System.out.println("ReportRunner's run mode:");
		System.out.println("--mode/-m run [options] <rptdesign file>");
		System.out.println("where options could be:"); //$NON-NLS-1$
		System.out.println("\t--output/-o <target file>"); //$NON-NLS-1$
		// System.out.println( "\t--locale/-l <locale> default is english" );
		// //$NON-NLS-1$
		System.out.println("\t--parameter/-p <\"parameterName=parameterValue\">"); //$NON-NLS-1$
		System.out.println("\t--config/-c <\"configName=configValue\">");
		System.out.println("\t--file/-F <file>\n"); //$NON-NLS-1$
		System.out.println("1. parameters/configs in command line will overide those in file"); //$NON-NLS-1$
		System.out.println("2. parameter/config name can not include characters such as ' ', '=', ':' "); //$NON-NLS-1$
		System.out.println("use \"--help/-h configNames\" for a list of configurables");
		System.out.println("use \"--help/-h file\" for options in <file>");
	}

	protected void printRenderUsage() {
		System.out.println("ReportRunner's RENDER mode:");
		System.out.println("--mode/-m render [options] <rptdocument file>");
		System.out.println("where options could be:"); //$NON-NLS-1$
		System.out.println("\t--format/-f [HTML|PDF]"); //$NON-NLS-1$
		System.out.println("\t--output/-o <target file>"); //$NON-NLS-1$
		System.out.println("\t--page/-n <pageNumber>");
		System.out.println("\t--locale/-l <locale>     default is english"); //$NON-NLS-1$
		System.out.println("\t--config/-c <\"configName=configValue\">");
		System.out.println("\t--renderOption/-r <\"optionName=optionValue\">");
		System.out.println("\t--file/-F <file>\n"); //$NON-NLS-1$

		System.out.println("1. configs/renderOptions in command line will overide those in file"); //$NON-NLS-1$
		System.out.println("2. config/renderOption name can not include characters such as ' ', '=', ':' "); //$NON-NLS-1$
		System.out.println("use \"--help/-h configNames\" for a list of configurables");
		System.out.println("use \"--help/-h renderOptions\" for a list of render options");
		System.out.println("use \"--help/-h file\" for options in <file>");
	}

	protected void printRunRenderUsage() {
		System.out.println("ReportRunner's RUNRENDER mode:");
		System.out.println("--mode/-m runrender [options] <rptdesign file>");
		System.out.println("where options could be:"); //$NON-NLS-1$
		System.out.println("\t--format/-f [HTML|PDF]"); //$NON-NLS-1$
		System.out.println("\t--output/-o <target file>"); //$NON-NLS-1$
		System.out.println("\t--htmlType/-t < HTML | ReportletNoCSS >"); //$NON-NLS-1$
		System.out.println("\t--encoding/-e <target encoding>"); //$NON-NLS-1$
		System.out.println("\t--locale/-l <locale>    default is english"); //$NON-NLS-1$
		System.out.println("\t--parameter/-p <\"parameterName=parameterValue\">"); //$NON-NLS-1$
		System.out.println("\t--config/-c <\"configName=configValue\">");
		System.out.println("\t--renderOption/-r <\"optionName=optionValue\">");
		System.out.println("\t--file/-F <file>\n"); //$NON-NLS-1$

		System.out.println("1. parameters/configs/renderOptions in command line will overide those in file"); //$NON-NLS-1$
		System.out.println("2. parameter/config/renderOption name can not include characters such as ' ', '=', ':' "); //$NON-NLS-1$
		System.out.println("use \"--help/-h configNames\" for a list of configurables");
		System.out.println("use \"--help/-h renderOptions\" for a list of render options");
		System.out.println("use \"--help/-h file\" for options in <file>");

	}

	protected void printConfigUsage() {
		System.out.println("Configurables include:");
		System.out.println("\tresourceDir    the directory where resources reside");
		System.out.println("\ttempDir        the directory to place temporary file");
		System.out.println("\tlogDir         the directory where logs are generated");
		System.out.println("\tlogLevel       log level, see java.util.Level");
		System.out.println("\tlogFile        the log file");
		System.out.println("\tscriptPath     the class path where to find scripts");
	}

	protected void printRenderOptionUsage() {
		System.out.println("RenderOptions include:");
		System.out.println("\tformat      the output format, html or pdf");
		System.out.println("\thtmlType    html type");
		System.out.println("\toutput      the output file");
		System.out.println("\tlocale      the locale used to render the report");
		System.out.println("\tencoding    encoding");
		System.out.println("\tpage        the page number to be rendered");
	}

	protected void printFileUsage() {
		System.out.println(
				"When specified with --file/-F <file>, " + "the <file> can be used to hold some default options.\n"
						+ "The command line options overwrite those in <file>.\n"
						+ "The options include what config/renderOption/parameter can have.\n"
						+ "Use \"-h configNames\", \"-h renderOptions\" for detailed options");
	}

	/**
	 * Private function to convert a locale name string to a locale object
	 * 
	 * @param locale - locale name string
	 * @return A locale object
	 */
	private Locale getLocale(String locale) {
		int index = locale.indexOf('_');
		if (index != -1) {
			// e.g, zh_CN (language_country)
			String language = locale.substring(0, index);
			String country = locale.substring(index + 1);
			return new Locale(language, country);
		}

		// e.g, en (language)
		return new Locale(locale);
	}

	CommandLine results;

	/**
	 * Parse running options.
	 * 
	 * @throws Exception
	 */
	protected void parseRunOptions() throws Exception {
		assert (mode.equalsIgnoreCase("Run"));

		// targetFile
		if (params.get("output") != null) {
			targetFile = (String) params.get("output");
		}
		if (results.hasOption('o')) {
			targetFile = results.getOptionValue('o');
		}
	}

	/**
	 * Parse render options.
	 * 
	 * @throws Exception
	 */
	protected void parseRenderOptions() throws Exception {
		assert (mode.equalsIgnoreCase("Render"));

		// format
		if (params.get("format") != null) {
			format = (String) params.get("format");
		}
		if (results.hasOption('f')) {
			format = results.getOptionValue('f');
		}

		// htmlType
		if (params.get("htmlType") != null) {
			htmlType = (String) params.get("htmlType");
		}
		if (results.hasOption('t')) {
			htmlType = results.getOptionValue('t');
		}

		// targetFile
		if (params.get("output") != null) {
			targetFile = (String) params.get("output");
		}
		if (results.hasOption('o')) {
			targetFile = results.getOptionValue('o');
		}

		// locale
		if (params.get("locale") != null) {
			locale = (String) params.get("locale");
		}
		if (results.hasOption('l')) {
			locale = results.getOptionValue('l');
		}

		// encoding
		if (params.get("encoding") != null) {
			encoding = (String) params.get("encoding");
		}
		if (results.hasOption('e')) {
			encoding = results.getOptionValue('e');
		}

		// pageNumber
		// -p is for backward compatibility.
		// -n takes precedence over -p in new cases
		String paramPageNumber = (String) params.get("page");
		if (results.hasOption('p')) {
			paramPageNumber = results.getOptionValue('p');
		}
		if (results.hasOption('n')) {
			paramPageNumber = results.getOptionValue('n');
		}
		if (paramPageNumber != null) {
			try {
				pageNumber = Long.parseLong(paramPageNumber);
			} catch (NumberFormatException nfe) {
				logger.log(Level.SEVERE, "Can not parse parameter(page number) \"" + paramPageNumber + "\""); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Parse run and render options.
	 * 
	 * @throws Exception
	 */
	protected void parseRunAndRenderOptions() throws Exception {
		assert (mode.equalsIgnoreCase("RunAndRender"));

		// format
		if (params.get("format") != null) {
			format = (String) params.get("format");
		}
		if (results.hasOption('f')) {
			format = results.getOptionValue('f');
		}

		// htmlType
		if (params.get("htmlType") != null) {
			htmlType = (String) params.get("htmlType");
		}
		if (results.hasOption('t')) {
			htmlType = results.getOptionValue('t');
		}

		// targetFile
		if (params.get("output") != null) {
			targetFile = (String) params.get("output");
		}
		if (results.hasOption('o')) {
			targetFile = results.getOptionValue('o');
		}

		// locale
		if (params.get("locale") != null) {
			locale = (String) params.get("locale");
		}
		if (results.hasOption('l')) {
			locale = results.getOptionValue('l');
		}

		// encoding
		if (params.get("encoding") != null) {
			encoding = (String) params.get("encoding");
		}
		if (results.hasOption('e')) {
			encoding = results.getOptionValue('e');
		}
	}

	/**
	 * 
	 * @return 1 if this command is for help information; 0 if it's normal
	 */
	protected int parseHelpOptions() {
		if (args.length == 0) {
			printGeneralUsage();
			return 1;
		}

		try {
			Options option = new Options();
			option.addOption("h", "help", true, "");

			CommandLine results = new BasicParser().parse(option, args, true);
			if (results.hasOption('h')) {
				String name = results.getOptionValue('h');
				if (name == null || name.length() == 0) {
					printGeneralUsage();
				} else if (name.equalsIgnoreCase("run")) {
					printRunUsage();
				} else if (name.equalsIgnoreCase("render")) {
					printRenderUsage();
				} else if (name.equalsIgnoreCase("runrender")) {
					printRunRenderUsage();
				} else if (name.equalsIgnoreCase("configNames")) {
					printConfigUsage();
				} else if (name.equalsIgnoreCase("renderOptions")) {
					printRenderOptionUsage();
				} else if (name.equalsIgnoreCase("file")) {
					printFileUsage();
				} else {
					printGeneralUsage();
				}
				return 1;
			}
		} catch (ParseException ex) {
			// this parse exception is probably caused by no argument to -h
			// so dont add it to log
			// logger.log( Level.SEVERE, ex.getMessage( ), ex );
			printGeneralUsage();
			return 1;
		}
		return 0;
	}

	/**
	 * parse the arguments.
	 * 
	 * -html html-file-name -pdf pdf-file-name -fo fo-file-name -l locale-name -p
	 * page-number design-file-name
	 * 
	 * @param args - arguments
	 */
	protected void parseNormalOptions() {

		source = args[args.length - 1];
		args[args.length - 1] = ""; //$NON-NLS-1$

		try {

			Options option = new Options();// Collection of all the options
			/*
			 * Add these basic argument into instance option
			 */
			option.addOption("m", "mode", true, "RunAndRender");
			option.addOption("o", "output", true, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption("f", "format", true, "html"); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption("t", "htmlType", true, ""); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption("l", "locale", true, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption("e", "encoding", true, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption("p", "parameter", true, "");
			option.addOption("n", "page", true, "");
			option.addOption("p", "page", true, "");
			// CHANGE: change file's desc since --file is used for config,parameter and
			// renderoption
			option.addOption("F", "file", true, "");
			option.addOption("c", "config", true, "");
			option.addOption("r", "renderOption", true, "");

			results = new BasicParser().parse(option, args, true);

			if (results.hasOption('F')) {
				String fileName = results.getOptionValue('F');
				readConfigurationFile(fileName, params);
			}
			parseConfigurationOptions();

			if (results.hasOption('m')) {
				mode = results.getOptionValue('m');
			}

			if ("Run".equalsIgnoreCase(mode)) {
				parseRunOptions();
			} else if ("Render".equalsIgnoreCase(mode)) {
				parseRenderOptions();
			} else {
				parseRunAndRenderOptions();
			}
			checkTargetFileName();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			printGeneralUsage();
		}
	}

	/**
	 * read paramters from the param and add it into the params.
	 * 
	 * @param param  parameter string line. use '=' to separate the name and value.
	 * @param params maps contains all the paramter name and value.
	 */
	protected void readParamString(String param, HashMap params) {
		if (param == null || param.length() < 2) {
			return;
		}
		int index = param.indexOf("=");
		if (index < 1) {
			return;
		}
		String name = param.substring(0, index).trim();
		String value = param.substring(index + 1).trim();
		if (value.startsWith("\"") && value.endsWith("\"") && value.length() > 2) {
			value = value.substring(1, value.length() - 1);
		}
		params.put(name, value);
	}

	/**
	 * paraser the config/paramter/renderoption command line inputs.
	 * 
	 * @return the HashMap contains all the paramter name and values.
	 */
	protected void parseConfigurationOptions() {
		// FIXME: maybe it's better to make options as a ReportRunner's final member?
		char[] options = { 'c', 'r', 'p' };
		for (int optIndex = 0; optIndex < options.length; optIndex++) {
			char currentOption = options[optIndex];
			if (results.hasOption(currentOption)) {
				String[] stringParams = results.getOptionValues(currentOption);

				if (stringParams != null) {
					for (int i = 0; i < stringParams.length; i++) {
						readParamString(stringParams[i], params);
					}
				}
			}
		}
	}

	/**
	 * read Config-Parameter-Render file
	 */
	protected void readConfigurationFile(String fileName, HashMap params) {
		File file = new File(fileName);
		Properties ps = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			ps.load(in);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "file: " + file.getAbsolutePath() + " not exists!"); //$NON-NLS-1$
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Can not open file: " + file.getAbsolutePath()); // $NON-NLS-1
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, "Can not close file: " + file.getAbsolutePath()); // $NON-NLS-1
				}
			}
		}
		params.putAll(ps);
	}

	/**
	 * Evaluate parameter values.
	 * 
	 * @param runnable
	 * @return
	 */
	private HashMap evaluateParameterValues(IReportRunnable runnable) {

		HashMap inputValues = new HashMap();
		IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(runnable);
		Collection paramDefns = task.getParameterDefns(false);
		Iterator iter = paramDefns.iterator();
		while (iter.hasNext()) {
			// now only support scalar parameter
			IParameterDefnBase pBase = (IParameterDefnBase) iter.next();
			if (pBase instanceof IScalarParameterDefn) {
				IScalarParameterDefn paramDefn = (IScalarParameterDefn) pBase;

				String paramName = paramDefn.getName();
				String inputValue = (String) params.get(paramName);
				int paramDataType = paramDefn.getDataType();
				String paramType = paramDefn.getScalarParameterType();

				// if allow multiple values
				boolean isAllowMutipleValues = false;
				try {
					Object paramValue = null;
					if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(paramType)) {
						paramValue = stringToObjectArray(paramDataType, inputValue);
						isAllowMutipleValues = true;
					} else {
						paramValue = stringToObject(paramDataType, inputValue);
					}
					if (paramValue != null) {
						List<ParameterSelectionChoice> selectList = paramDefn.getSelectionList();
						ParameterAttribute pa = null;
						if (isAllowMutipleValues) {
							Object[] values = (Object[]) paramValue;
							List<String> displayTextList = new ArrayList<String>();
							if (selectList != null && selectList.size() > 0) {
								for (Object o : values) {
									for (ParameterSelectionChoice select : selectList) {
										if (o.equals(select.getValue())) {
											displayTextList.add(select.getLabel());
										}
									}
								}
							}
							String[] displayTexts = new String[displayTextList.size()];
							pa = new ParameterAttribute(values, displayTextList.toArray(displayTexts));
						} else {
							String displayText = null;
							if (selectList != null && selectList.size() > 0) {
								for (ParameterSelectionChoice select : selectList) {
									if (paramValue.equals(select.getValue())) {
										displayText = select.getLabel();
										break;
									}
								}
							}
							pa = new ParameterAttribute(paramValue, displayText);
						}
						inputValues.put(paramName, pa);
					}
				} catch (BirtException ex) {
					logger.log(Level.SEVERE, "the value of parameter " + paramName + " is invalid", ex);
				}
			}
		}
		return inputValues;
	}

	/**
	 * 
	 * @param paramDataType the data type
	 * @param inputValue    parameter value in String
	 * @return parameter value in Object[]
	 * @throws BirtException
	 */
	private Object[] stringToObjectArray(int paramDataType, String inputValue) throws BirtException {
		if (inputValue == null) {
			return null;
		}
		List result = new LinkedList();
		String[] inputValues = inputValue.split(",");
		for (String value : inputValues) {
			result.add(stringToObject(paramDataType, value));
		}
		return result.toArray();
	}

	/**
	 * @param p    the scalar parameter
	 * @param expr the default value expression
	 */
	protected Object stringToObject(int type, String value) throws BirtException {
		if (value == null) {
			return null;
		}
		switch (type) {
		case IScalarParameterDefn.TYPE_BOOLEAN:
			return DataTypeUtil.toBoolean(value);

		case IScalarParameterDefn.TYPE_DATE:
			return DataTypeUtil.toSqlDate(value);

		case IScalarParameterDefn.TYPE_TIME:
			return DataTypeUtil.toSqlTime(value);

		case IScalarParameterDefn.TYPE_DATE_TIME:
			return DataTypeUtil.toDate(value);
		case IScalarParameterDefn.TYPE_DECIMAL:
			return DataTypeUtil.toBigDecimal(value);

		case IScalarParameterDefn.TYPE_FLOAT:
			return DataTypeUtil.toDouble(value);

		case IScalarParameterDefn.TYPE_STRING:
			return DataTypeUtil.toString(value);

		case IScalarParameterDefn.TYPE_INTEGER:
			return DataTypeUtil.toInteger(value);
		}
		return null;

	}

	/**
	 * If -o (targetFile) is not specified, assume same directory as inputfile, and
	 * inputfile.*** as output file name (where *** is the output format.
	 * 
	 * If -o specifies a directory, assume the file name is the same as
	 * inputfile.***.
	 * 
	 * If -o specifies a file, it has a path part and a filename part. Take the path
	 * as the directory to store the file and other resources (for example image).
	 */
	protected void checkTargetFileName() {
		String fileExt = "." + format;
		if ("Run".equalsIgnoreCase(mode)) {
			fileExt = ".rptdocument";
		}
		File designFile = new File(new File(source).getAbsolutePath());

		String designFileName = designFile.getName();

		int n = designFileName.lastIndexOf('.');
		if (n != -1) {
			designFileName = designFileName.substring(0, n);
		}

		if (targetFile == null) {
			targetFile = designFile.getParent() + File.separatorChar + designFileName + fileExt;
		} else if (!targetFile.toLowerCase().endsWith(fileExt.toLowerCase())) {
			targetFile = targetFile + File.separatorChar + designFileName + fileExt;
			File file = new File(targetFile);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdir();
			}
		}
	}

	/**
	 * new a EngineConfig and config it with user's setting
	 * 
	 */
	protected EngineConfig createEngineConfig() {
		EngineConfig config = new EngineConfig();

		String resourcePath = (String) params.get("resourceDir");
		if (resourcePath != null)
			config.setResourcePath(resourcePath.trim());

		String tempDir = (String) params.get("tempDir");
		if (tempDir != null)
			config.setTempDir(tempDir.trim());

		String logDir = (String) params.get("logDir");
		String logLevel = (String) params.get("logLevel");
		Level level = null;
		if (logLevel != null) {
			logLevel = logLevel.trim();
			if ("all".equalsIgnoreCase(logLevel)) {
				level = Level.ALL;
			} else if ("config".equalsIgnoreCase(logLevel)) {
				level = Level.CONFIG;
			} else if ("fine".equalsIgnoreCase(logLevel)) {
				level = Level.FINE;
			} else if ("finer".equalsIgnoreCase(logLevel)) {
				level = Level.FINER;
			} else if ("finest".equalsIgnoreCase(logLevel)) {
				level = Level.FINEST;
			} else if ("info".equalsIgnoreCase(logLevel)) {
				level = Level.INFO;
			} else if ("off".equalsIgnoreCase(logLevel)) {
				level = Level.OFF;
			} else if ("severe".equalsIgnoreCase(logLevel)) {
				level = Level.SEVERE;
			} else if ("warning".equalsIgnoreCase(logLevel)) {
				level = Level.WARNING;
			}
		}
		String logD = (logDir == null) ? config.getLogDirectory() : logDir;
		Level logL = (level == null) ? config.getLogLevel() : level;
		config.setLogConfig(logD, logL);

		String logFile = (String) params.get("logFile");
		if (logFile != null)
			config.setLogFile(logFile.trim());

		String scripts = (String) params.get("scriptPath");
		HashMap map = new HashMap();
		map.put(EngineConstants.PROJECT_CLASSPATH_KEY, scripts);
		config.setAppContext(map);

		return config;
	}
}
