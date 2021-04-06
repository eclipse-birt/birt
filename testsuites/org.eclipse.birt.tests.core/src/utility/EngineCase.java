/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.ReportRunner;

/**
 * Base class for Engine test.
 */

public abstract class EngineCase extends TestCase {

	private String caseName;

	protected static final String BUNDLE_NAME = "utility.messages";//$NON-NLS-1$

	protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	protected static final String PLUGIN_NAME = "utility"; //$NON-NLS-1$
	protected static final String PLUGINLOC = "/utility/"; //$NON-NLS-1$

	protected static final String PLUGIN_PATH = System.getProperty("user.dir") //$NON-NLS-1$
			+ "/plugins/" + PLUGINLOC.substring(PLUGINLOC.indexOf("/") + 1) //$NON-NLS-1$//$NON-NLS-2$
			+ "bin/"; //$NON-NLS-1$

	protected static final String TEST_FOLDER = "src/"; //$NON-NLS-1$
	protected static final String OUTPUT_FOLDER = "output"; //$NON-NLS-1$
	protected static final String INPUT_FOLDER = "input"; //$NON-NLS-1$
	protected static final String GOLDEN_FOLDER = "golden"; //$NON-NLS-1$

	protected IReportEngine engine = null;

	protected IEngineTask engineTask = null;

	private static final String FORMAT_HTML = "html"; //$NON-NLS-1$
	private static final String FORMAT_PDF = "pdf";
	private static final String ENCODING_UTF8 = "UTF-8"; //$NON-NLS-1$
	private String IMAGE_DIR = "image"; //$NON-NLS-1$

	private boolean pagination = false;
	private Locale locale = Locale.ENGLISH;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		// IPlatformContext context = new PlatformFileContext( );
		// config.setEngineContext( context );
		// this.engine = new ReportEngine( config );

		EngineConfig config = new EngineConfig();
		this.engine = createReportEngine(config);
	}

	/**
	 * Create a report engine instance.
	 * 
	 * @param config
	 * @return
	 * @throws BirtException
	 */
	public IReportEngine createReportEngine(EngineConfig config) throws BirtException {
		setScriptingPath();

		if (config == null) {
			config = new EngineConfig();
		}

		Platform.startup(new PlatformConfig());
		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			return ((IReportEngineFactory) factory).createReportEngine(config);
		}
		return null;
	}

	/**
	 * Constructor.
	 */

	public EngineCase() {
		super(null);
	}

	/**
	 * Constructor for DemoCase.
	 * 
	 * @param name
	 */
	public EngineCase(String name) {
		super(name);
	}

	protected void setCase(String caseName) {
		// set the case and emitter manager accroding to caseName.
		this.caseName = caseName;
	}

	protected void runCase(String args[]) {
		Vector runArgs = new Vector();
		// invoke the report runner.
		String input = PLUGIN_PATH + System.getProperty("file.separator") //$NON-NLS-1$
				+ RESOURCE_BUNDLE.getString("CASE_INPUT"); //$NON-NLS-1$
		input += System.getProperty("file.separator") + caseName //$NON-NLS-1$
				+ ".rptdesign"; //$NON-NLS-1$
		System.out.println("input is : " + input); //$NON-NLS-1$

		// run report runner.

		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				runArgs.add(args[i]);
			}
		}
		runArgs.add("-f"); //$NON-NLS-1$
		runArgs.add("test"); //$NON-NLS-1$
		runArgs.add(input);

		args = (String[]) runArgs.toArray(new String[runArgs.size()]);
		ReportRunner.main(args);
	}

	/**
	 * Make a copy of a given file to the target file.
	 * 
	 * @param from the file where to copy from
	 * @param to   the target file to copy to.
	 * @throws IOException
	 */

	protected final void copyFile(String from, String to) throws IOException {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			new File(to).createNewFile();

			bis = new BufferedInputStream(new FileInputStream(from));
			bos = new BufferedOutputStream(new FileOutputStream(to));

			int nextByte = 0;
			while ((nextByte = bis.read()) != -1) {
				bos.write(nextByte);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bis != null)
					bis.close();

				if (bos != null)
					bos.close();
			} catch (IOException e) {
				// ignore
			}

		}
	}

	protected void copyResource(String src, String tgt, String folder) {

		String className = getFullQualifiedClassName();
		tgt = this.tempFolder() + className + "/" + folder + "/" + tgt;
		className = className.replace('.', '/');

		src = className + "/" + folder + "/" + src;

		System.out.println("src: " + src);
		System.out.println("tgt: " + tgt);
		File parent = new File(tgt).getParentFile();

		if (parent != null) {
			parent.mkdirs();
		}

		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();

		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	protected void copyResource_INPUT(String input_resource, String input) {
		this.copyResource(input_resource, input, INPUT_FOLDER);
	}

	protected void copyResource_GOLDEN(String input_resource, String golden) {
		this.copyResource(input_resource, golden, GOLDEN_FOLDER);
	}

	/**
	 * Remove a given file or directory recursively.
	 * 
	 * @param file
	 */
	public void removeFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				removeFile(children[i]);
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println(file.toString() + " can't be removed"); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Remove a given file or directory recursively.
	 * 
	 * @param file
	 */

	public void removeFile(String file) {
		removeFile(new File(file));
	}

	public void removeResource() {
		String className = getFullQualifiedClassName();
		removeFile(className);
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */

	protected String getClassFolder() {
		String pathBase = null;

		ProtectionDomain domain = this.getClass().getProtectionDomain();
		if (domain != null) {
			CodeSource source = domain.getCodeSource();
			if (source != null) {
				URL url = source.getLocation();
				pathBase = url.getPath();

				if (pathBase.endsWith("bin/")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				if (pathBase.endsWith("bin")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = pathBase + className.replace('.', '/');

		return className;
	}

	/**
	 * Compares two text file. The comparison will ignore the line containing
	 * "modificationDate".
	 * 
	 * @param golden the 1st file name to be compared.
	 * @param output the 2nd file name to be compared.
	 * @return true if two text files are same line by line
	 * @throws Exception if any exception.
	 */

	protected boolean compareHTML(String golden, String output) throws Exception {
		FileReader readerA = null;
		FileReader readerB = null;
		boolean same = true;
		StringBuffer errorText = new StringBuffer();

		try {

			String outputFile = genOutputFile(output);

			String goldenFile = this.tempFolder() + getFullQualifiedClassName() + "/" + GOLDEN_FOLDER + "/" + golden;
			readerA = new FileReader(goldenFile);
			readerB = new FileReader(outputFile);

			same = compareTextFile(readerA, readerB, output);
		} catch (IOException e) {
			errorText.append(e.toString());
			errorText.append("\n"); //$NON-NLS-1$
			e.printStackTrace();
		} finally {
			try {
				readerA.close();
				readerB.close();
			} catch (Exception e) {
				readerA = null;
				readerB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares the string. The comparison will ignore the line containing
	 * 
	 * @param output      the file name to be compared.
	 * @param checkstring the string to be compared.
	 * @param checktimes  the times that the checkstring display.
	 * @return true if the string display times is the same as checktimes
	 * @throws Exception if any exception.
	 */
	protected boolean compareHTML_STRING(String output, String checkstring, int checktimes) {
		StringBuffer errorText = new StringBuffer();
		String outputFile = genOutputFile(output);
		String line = null;
		int count = 0;
		boolean same = true;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(outputFile)));
			while ((line = reader.readLine()) != null) {
				if (line.indexOf(checkstring) > 0) {
					count++;
				}
			}
			same = compareString(checktimes, count);
		} catch (IOException e) {
			errorText.append(e.toString());
			errorText.append("\n"); //$NON-NLS-1$
			e.printStackTrace();
		}

		return same;

	}

	/**
	 * Run and render the given design file into html file. If the input is "a.xml",
	 * output html file will be named "a.html" under folder "output".
	 * 
	 * @param input
	 * @throws EngineException
	 */

	protected ArrayList runAndRender_HTML(String input, String output) throws EngineException {
		this.pagination = false;
		return runAndRender(input, output, null, FORMAT_HTML); // $NON-NLS-1$
	}

	/**
	 * Run and render the given design file into html file with pagination. If the
	 * input is "a.xml", output html file will be named "a.html" under folder
	 * "output".
	 * 
	 * @param input
	 * @throws EngineException
	 */

	protected ArrayList runAndRender_HTMLWithPagination(String input, String output) throws EngineException {
		this.pagination = true;
		return runAndRender(input, output, null, FORMAT_HTML); // $NON-NLS-1$
	}

	/**
	 * Run and render the given design file into pdf file. If the input is "a.xml",
	 * output html file will be named "a.pdf" under folder "output".
	 * 
	 * @param input
	 * @throws EngineException
	 */

	protected void runAndRender_PDF(String input, String output) throws EngineException {
		runAndRender(input, output, null, FORMAT_PDF); // $NON-NLS-1$
	}

	protected String getFullQualifiedClassName() {
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);

		return className;
	}

	/**
	 * RunAndRender a report with the given parameters.
	 */

	protected final ArrayList runAndRender(String input, String output, Map paramValues, String format)
			throws EngineException {
		String outputFile = genOutputFile(output);
		String inputFile = this.tempFolder() + getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + input;

		IReportRunnable runnable = engine.openReportDesign(inputFile.replace('\\', '/'));
		IRunAndRenderTask task = engine.createRunAndRenderTask(runnable);

		// set engine task
		engineTask = task;

		if (paramValues != null) {
			Iterator keys = paramValues.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				task.setParameterValue(key, paramValues.get(key));
			}
		}

		task.setLocale(locale);

		IRenderOption options = null;
		if (FORMAT_PDF.equals(format)) // $NON-NLS-1$
		{
			options = new RenderOptionBase();
			options.setOutputFileName(outputFile);
		} else {
			options = new HTMLRenderOption();
			options.setOutputFileName(outputFile);
			((HTMLRenderOption) options).setHtmlPagination(this.pagination);
			HTMLRenderContext renderContext = new HTMLRenderContext();
			renderContext.setImageDirectory(IMAGE_DIR);
			HashMap appContext = new HashMap();
			appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
			task.setAppContext(appContext);
		}

		options.setOutputFormat(format);
		options.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, ENCODING_UTF8);
		task.setRenderOption(options);
		task.run();
		ArrayList errors = (ArrayList) task.getErrors();
		task.close();
		return errors;
	}

	/**
	 * Run a report, generate a self-contained report document.
	 * 
	 * @throws EngineException
	 */

	protected final ArrayList run(String input, String output) throws EngineException {
		String outputFile = genOutputFile(output);
		input = getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + input;

		IReportRunnable runnable = engine.openReportDesign(input);
		IRunTask task = engine.createRunTask(runnable);
		task.setAppContext(new HashMap());
		task.setLocale(locale);
		IDocArchiveWriter archive = null;
		try {
			archive = new FileArchiveWriter(outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		task.run(archive);
		ArrayList errors = (ArrayList) task.getErrors();
		task.close();
		return errors;
	}

	/**
	 * @param doc       input rpt docuement file
	 * @param output    output file of the generation.
	 * @param pageRange The pages to render, use "All" to render all, use 1-N to
	 *                  render a selected page.
	 * @throws EngineException
	 */

	protected ArrayList render_HTML(String doc, String output, String pageRange) throws EngineException {
		this.pagination = true;
		return render(FORMAT_HTML, doc, output, pageRange); // $NON-NLS-1$
	}

	/**
	 * Render a report document into PDF file.
	 * 
	 * @param doc
	 * @param output
	 * @param pageRange
	 * @throws EngineException
	 */

	protected ArrayList render_PDF(String doc, String output, String pageRange) throws EngineException {
		return render(FORMAT_PDF, doc, output, pageRange); // $NON-NLS-1$
	}

	private ArrayList render(String format, String doc, String output, String pageRange) throws EngineException {

		String outputFile = genOutputFile(output);
		doc = getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + doc;

		String encoding = "UTF-8"; //$NON-NLS-1$

		IReportDocument document = engine.openReportDocument(doc);
		IRenderTask task = engine.createRenderTask(document);
		task.setLocale(locale);

		IRenderOption options = new HTMLRenderOption();
		options.setOutputFileName(outputFile);
		options.setOutputFormat(format);
		options.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, encoding);
		if (!format.equalsIgnoreCase(FORMAT_PDF)) {
			((HTMLRenderOption) options).setHtmlPagination(this.pagination);
		}
		HTMLRenderContext renderContext = new HTMLRenderContext();
		renderContext.setImageDirectory(IMAGE_DIR);
		HashMap appContext = new HashMap();
		appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
		appContext.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, EngineCase.class.getClassLoader());

		task.setAppContext(appContext);
		task.setRenderOption(options);

		task.setPageRange(pageRange);
		task.render();
		ArrayList errors = (ArrayList) task.getErrors();
		task.close();
		return errors;
	}

	/**
	 * Run the input design, generate a report document, and then render the report
	 * document into a html file, <code>pageRange</code> specified the page(s) to
	 * render.
	 * 
	 * @throws IOException
	 * @throws EngineException
	 */

	protected final ArrayList runAndThenRender(String input, String output, String pageRange, String format)
			throws Exception {
		String tempDoc = "temp_123aaabbbccc789.rptdocument"; //$NON-NLS-1$

		ArrayList errors = run(input, tempDoc);
		if (errors.size() > 0) {
			return errors;
		}

		String from = genOutputFile(tempDoc);

		try {
			copyFile(from, this.getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + tempDoc);
			if (FORMAT_PDF.equals(format)) // $NON-NLS-1$
				return render_PDF(tempDoc, output, pageRange);
			else
				return render_HTML(tempDoc, output, pageRange);
		} catch (Exception e) {
			throw e;
		} finally {
			// remove the temp file on exit.
			removeFile(tempDoc);
		}
	}

	/**
	 * Compares the two text files.
	 * 
	 * @param golden the reader for golden file
	 * @param output the reader for output file
	 * @return true if two text files are same.
	 * @throws Exception if any exception
	 */

	protected boolean compareTextFile(Reader golden, Reader output, String fileName) throws Exception {
		StringBuffer errorText = new StringBuffer();

		BufferedReader lineReaderA = null;
		BufferedReader lineReaderB = null;
		boolean same = true;
		int lineNo = 1;
		try {
			lineReaderA = new BufferedReader(golden);
			lineReaderB = new BufferedReader(output);

			String strA = lineReaderA.readLine().trim();
			String strB = lineReaderB.readLine().trim();

			while (strA != null) {
				// filter the random part of the page.

				String filterA = this.filterLine(strA);
				String filterB = this.filterLine(strB);

				same = filterA.trim().equals(filterB.trim());

				if (!same) {
					StringBuffer message = new StringBuffer();

					message.append("line="); //$NON-NLS-1$
					message.append(lineNo);
					message.append("("); //$NON-NLS-1$
					message.append(fileName);
					message.append(")"); //$NON-NLS-1$
					message.append(" is different:\n");//$NON-NLS-1$
					message.append(" The line from golden file: ");//$NON-NLS-1$
					message.append(strA);
					message.append("\n");//$NON-NLS-1$
					message.append(" The line from result file: ");//$NON-NLS-1$
					message.append(strB);
					message.append("\n");//$NON-NLS-1$

					message.append("Text after filtering: \n"); //$NON-NLS-1$
					message.append(" golden file: "); //$NON-NLS-1$
					message.append(filterA);
					message.append("\n");//$NON-NLS-1$
					message.append(" result file: "); //$NON-NLS-1$
					message.append(filterB);

					throw new Exception(message.toString());
				}

				strA = lineReaderA.readLine();
				strB = lineReaderB.readLine();

				lineNo++;
			}

			same = (strA == null) && (strB == null);
		} finally {
			try {
				lineReaderA.close();
				lineReaderB.close();
			} catch (Exception e) {
				lineReaderA = null;
				lineReaderB = null;

				errorText.append(e.toString());

				throw new Exception(errorText.toString());
			}
		}

		return same;
	}

	/**
	 * Compares the two times.
	 * 
	 * @param checktimes  the times that the string display.
	 * @param countstring the golden times.
	 * @return true if two times are same.
	 * @throws Exception if any exception
	 */
	private boolean compareString(int checktimes, int countstring) {
		boolean same = true;
		StringBuffer errorText = new StringBuffer();
		try {
			if (checktimes == countstring)
				same = true;
			else
				same = false;
		} catch (Exception e) {
			errorText.append(e.toString());
		}
		return same;

	}

	/**
	 * All kinds of filter-pattern pairs that will be filtered and replace during
	 * comparasion.
	 */

	// Sample: id="AUTOGENBOOKMARK_6354527823361272054"
	private final static Pattern PATTERN_ID_AUTOBOOKMARK = Pattern.compile("id[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\""); //$NON-NLS-1$

	// Sample:
	private final static Pattern PATTERN_NAME_AUTOBOOKMARK = Pattern
			.compile("name[\\s]*=[\\s]*\"AUTOGENBOOKMARK_[\\d]+\""); //$NON-NLS-1$

	// Sample: iid="/9(QuRs13:0)"
	private final static Pattern PATTERN_IID = Pattern.compile("iid[\\s]*=[\\s]*\"/.*(.*)\""); //$NON-NLS-1$

	// Sample: style="background-image:url(image\file44.jpg)"
	private final static Pattern PATTERN_BG_IMAGE = Pattern.compile("background-image[\\s]*:url[(]image.*[)]"); //$NON-NLS-1$

	// Sample: .style_1 { background-image: url('image%5cfile24.jpg');}
	private final static Pattern PATTERN_BG_IMAGE2 = Pattern.compile("background-image[\\s]*:[\\s]*url[(]'image.*'[)]"); //$NON-NLS-1$

	// Sample: src="image/design31"
	// src="image\file31.jpg"
	private final static Pattern PATTERN_IMAGE_SOURCE = Pattern.compile("src=\"image.*\""); //$NON-NLS-1$

	/**
	 * Normalize some seeding values, lines that matches certain patterns will be
	 * repalced by a replacement.
	 */

	private static Object[][] FILTER_PATTERNS = { { PATTERN_ID_AUTOBOOKMARK, "REPLACEMENT_ID_AUTOBOOKMARK" }, //$NON-NLS-1$
			{ PATTERN_NAME_AUTOBOOKMARK, "REPLACEMENT_NAME_AUTOBOOKMARK" }, //$NON-NLS-1$
			{ PATTERN_IID, "REPLACEMENT_IID" }, //$NON-NLS-1$
			{ PATTERN_BG_IMAGE, "REPLACEMENT_BG_IMAGE" }, //$NON-NLS-1$
			{ PATTERN_BG_IMAGE2, "REPLACEMENT_BG_IMAGE2" }, //$NON-NLS-1$
			{ PATTERN_IMAGE_SOURCE, "REPLACEMENT_IMAGE_SOURCE" } //$NON-NLS-1$
	};

	/**
	 * Replace the given string with a replacement if it matches a certain pattern.
	 * 
	 * @param str
	 * @return filtered string, the tokens that matches the patterns are replaced
	 *         with replacement.
	 */

	protected String filterLine(String str) {
		String result = str;

		for (int i = 0; i < FILTER_PATTERNS.length; i++) {
			Pattern pattern = (Pattern) FILTER_PATTERNS[i][0];
			String replacement = (String) FILTER_PATTERNS[i][1];

			Matcher matcher = pattern.matcher(result);
			while (matcher.find()) {
				result = matcher.replaceFirst(replacement);
			}
			// result = matcher.replaceAll( replacement );
		}

		return result;
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path where the test java source file locates.
	 */
	protected String getBaseFolder() {
		String className = getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		return PLUGIN_PATH + className.replace('.', '/');
	}

	protected URL getResource(String name) {
		return this.getClass().getResource(name);
	}

	/**
	 * Set scripts class folder
	 */

	protected void setScriptingPath() {
		System.setProperty(EngineConstants.WEBAPP_CLASSPATH_KEY, this.getClassFolder() + "/input/scripts");
	}

	/**
	 * Set locale for run/render report
	 * 
	 * @param loc location used to run report
	 */
	protected void setLocale(Locale loc) {
		this.locale = loc;
	}

	/**
	 * Set image folder to save rendered temp image file
	 * 
	 * @param imageDir folder to save temp image.
	 */
	protected void setImageDir(String imageDir) {
		IMAGE_DIR = imageDir;
	}

	protected void tearDown() throws Exception {
		this.engine.destroy();
		super.tearDown();
	}

	protected String genOutputFile(String output) {
		String tempDir = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;
		String outputFile = tempDir + getFullQualifiedClassName() // $NON-NLS-1$
				+ "/" + OUTPUT_FOLDER + "/" + output;
		return outputFile;
	}

	private void copyFolder(File from, File to) throws Exception {
		if (!from.isDirectory() || !from.exists()) {
			throw new Exception("Input foler: " + from + " doesn't exist."); //$NON-NLS-1$//$NON-NLS-2$
		}

		File[] files = from.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return true;
			}
		});

		if (!to.exists())
			to.mkdir();
		System.out.println("size is " + files.length);
		for (int i = 0; i < files.length; i++) {
			// File file = files[i];

			if (files[i].isDirectory()) {
				this.copyFolder(files[i], new File(to.getPath() + "/" + files[i].getName()));
			}

			DataInputStream instr;
			DataOutputStream outstr;
			File outFile = new File(to.getPath());
			try {
				instr = new DataInputStream(new BufferedInputStream(new FileInputStream(files[i])));
				outstr = new DataOutputStream(
						new BufferedOutputStream(new FileOutputStream(outFile + "\\" + files[i].getName())));

				try {
					int data;
					while (true) {
						data = instr.readUnsignedByte();
						outstr.writeByte(data);
					}
				} catch (EOFException eof) {
					outstr.close();
					instr.close();
				}
			}

			catch (FileNotFoundException nfx) {
				System.out.println("Problem opening files:" + files[i]);
			} catch (IOException iox) {
				System.out.println("IO Problems");
			}
		}

	}

	public void copyFolder(String from, String to) throws Exception {
		copyFolder(new File(from), new File(to));
	}

	public String tempFolder() {
		String tempDir = System.getProperty("java.io.tmpdir");
		if (!tempDir.endsWith(File.separator))
			tempDir += File.separator;
		return tempDir;
	}
}