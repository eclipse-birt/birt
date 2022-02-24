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

package org.eclipse.birt.report.model.metadata;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.util.SecurityUtil;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;

/**
 * Default meta logger class for model's own use. Logs the exceptions into a
 * file.
 */

class FileMetaLogger implements IMetaLogger {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(FileMetaLogger.class.getName());

	/**
	 * Default log file name.
	 */

	protected final static String DEFAULT_LOG_FILE = "meta.log"; //$NON-NLS-1$

	/**
	 * Default output encoding.
	 */

	protected final static String DEFAULT_ENCODING = "UTF-8"; //$NON-NLS-1$

	/**
	 * The writer that does the actual writing to disk.
	 */

	protected Writer writer = null;

	/**
	 * The name of the log file.
	 */

	private String fileName = null;

	/**
	 * Date formatter to be used when formatting error messages.
	 */

	protected final static SimpleDateFormat df;

	// Set default time zone and initialize the df. Due to the bug of icu, if
	// default time zone is not set, the <code>SimpleDateForma</code> cann't be
	// initialized.

	static {
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //$NON-NLS-1$
	}

	/**
	 * Constructor to initialize the meta logger using the default log file.
	 */

	public FileMetaLogger() {
		this(DEFAULT_LOG_FILE);
	}

	/**
	 * Constructor to initialize the meta logger using the specified file.
	 * 
	 * @param fileName log file name
	 */

	public FileMetaLogger(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IMetaLogger#log(java.lang.String)
	 */

	public void log(String message) {
		if (canLog()) {
			try {
				Date dateTime = Calendar.getInstance().getTime();
				StringBuffer sb = new StringBuffer();

				sb.append(df.format(dateTime));
				sb.append(" Message ["); //$NON-NLS-1$
				sb.append(message.toString() + "]" + "\n"); //$NON-NLS-1$ //$NON-NLS-2$

				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) {
				// ignore
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IMetaLogger#log(java.lang.String,
	 * java.lang.Throwable)
	 */

	public void log(String message, Throwable t) {
		if (canLog()) {
			try {
				Date dateTime = Calendar.getInstance().getTime();
				StringBuffer sb = new StringBuffer();

				sb.append(df.format(dateTime));
				sb.append("  Message ["); //$NON-NLS-1$
				sb.append(message.toString() + "]" + "\n"); //$NON-NLS-1$ //$NON-NLS-2$

				sb.append("\t\t\t\t\t"); //$NON-NLS-1$
				sb.append(" Exception ["); //$NON-NLS-1$
				sb.append(getExceptionString(t));
				sb.append("]\n"); //$NON-NLS-1$

				writer.write(sb.toString());
				writer.flush();
			} catch (IOException e) {
				// ignored.
			}
		}
	}

	/**
	 * Returns the stack trace for the given exception as a string.
	 * 
	 * @param aException The exception for which the stack trace is required
	 * @return String the stack trace as a string for the given exception.
	 */

	protected String getExceptionString(Throwable aException) {

		ByteArrayOutputStream outStream = null;
		PrintStream printStream = null;

		try {
			outStream = new ByteArrayOutputStream();
			printStream = new PrintStream(outStream);
			aException.printStackTrace(printStream);

			String stackTrace = outStream.toString();

			printStream.close();
			printStream = null;

			outStream.close();
			outStream = null;

			return stackTrace;

		} catch (Exception e) {
			// ignore.
		} finally {
			try {
				if (printStream != null)
					printStream.close();

				if (outStream != null)
					outStream.close();
			} catch (IOException e) {
				// ignore.
			}
		}

		return null;
	}

	/**
	 * Returns an OutputStreamWriter when passed an OutputStream. The stream
	 * encoding will be in "UTF-8".
	 * 
	 * @param fileName name of the file that the writer will be bound to.
	 * @return the corresponding writer.
	 * @throws IOException if an I/O exception occurs or error occurs when opening
	 *                     the file.
	 */

	protected OutputStreamWriter createWriter(final String fileName) throws IOException {

		OutputStreamWriter retWriter = null;

		File outputFile = new File(fileName);
		File parentFile = outputFile.getParentFile();

		// there may be permission issues.
		// 1. the output file is a directory; 2. the output file exists and has
		// no write permission. 3. the parent directory has no writer
		// permission.

		boolean existed = outputFile.exists();

		if (outputFile.isDirectory() || (existed && !outputFile.canWrite())
				|| (!existed && parentFile != null && !parentFile.canWrite())) {
			logger.log(Level.WARNING, "No permission to write metadata log file."); //$NON-NLS-1$
			return null;
		}

		try {

			retWriter = new OutputStreamWriter(SecurityUtil.createFileOutputStream(outputFile), DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			// The encoding string must be wrong.

			assert false;
		}

		return retWriter;
	}

	/**
	 * Determines if there is a sense in attempting to append.
	 * <p>
	 * It checks whether there is a output target. If these checks fail, then the
	 * boolean value <code>false</code> is returned.
	 * <p>
	 * It will be called by {@link #log(String)}before actually perform a logging to
	 * check if a log action can be performed. The subclass may override this method
	 * to rewrite the precondition of a log action.
	 * 
	 * @return <code>true</code> if there is a set output target.
	 */

	protected boolean canLog() {
		if (fileName != null && writer == null) {
			try {
				writer = createWriter(fileName);
			} catch (IOException e) {
				fileName = null;
				return false;
			}
		}

		if (writer == null)
			return false;

		return true;
	}

	/**
	 * Closes the stream, release the log file.
	 */

	public void close() {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				// do nothing, simply ignore.
			} finally {
				writer = null;
			}
		}
	}
}
