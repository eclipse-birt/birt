/*******************************************************************************
 * Copyright (c) 2004 - 2011 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.sampledb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;
import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.services.PluginResourceLocator;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Sample DB This class initializes a private copy of the
 * Sample database by unzipping the DB files to a subdir in the TEMP directory.
 * A private copy is required because (1) Derby 10.1.2.1 has a bug which
 * disabled BIRT read-only access to a JAR'ed DB. (See
 * http://issues.apache.org/jira/browse/DERBY-854) (2) BIRT instances in
 * multiple JVMs may try to access the sample DB (such when preview mode). We
 * will corrupt the DB if a single copy of the DB is used
 */
public class SampledbPlugin extends BIRTPlugin {
	private static final Logger logger = Logger.getLogger(SampledbPlugin.class.getName());

	private static String dbDir;
	private static final String SAMPLE_DB_NAME = "BirtSample";
	private static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar";
	private static final String SAMPLE_DB_HOME_DIR = "db";

	private static int startCount = 0;

	/**
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		logger.info("Sampledb plugin starts up. Current startCount=" + startCount);
		synchronized (SampledbPlugin.class) {
			if (++startCount == 1) {
				// First time to start for this instance of JVM
				// initialze database directory now
				init();
			}
		}
		super.start(context);
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		logger.info("Sampledb plugin stopping. Current startCount=" + startCount);
		synchronized (SampledbPlugin.class) {
			if (startCount >= 1) {
				if (--startCount == 0) {
					// Last one to stop for this instance of JVM
					// Clean up Derby and temp files
					cleanUp();
				}
			}
		}
		super.stop(context);
	}

	private void cleanUp() throws Exception {
		// Stop Derby engine
		shutdownDerby();
		// Clean up database files
		removeDatabase();

		dbDir = null;
	}

	/**
	 * Initialization for first time startup in this instance of JVM
	 */
	private void init() throws IOException {
		assert dbDir == null;

		// Create and remember our private directory under system temp
		// Name it "BIRTSampleDB_$timestamp$_$classinstanceid$"
		String tempDir = System.getProperty("java.io.tmpdir");
		String timeStamp = String.valueOf(System.currentTimeMillis());
		String instanceId = Integer.toHexString(hashCode());
		dbDir = tempDir + "/BIRTSampleDB_" + timeStamp + "_" + instanceId;
		logger.info("Creating Sampledb database at location " + dbDir);
		(new File(dbDir)).mkdir();

		// Set up private copy of Sample DB in system temp directory

		// Get an input stream to read DB Jar file
		// handle getting db jar file on both OSGi and OSGi-less platforms
		String dbEntryName = SAMPLE_DB_HOME_DIR + "/" + SAMPLE_DB_JAR_FILE;
		URL fileURL = PluginResourceLocator.getPluginEntry(SampleDBConstants.PLUGIN_ID, dbEntryName);
		if (fileURL == null) {
			String errMsg = "INTERNAL ERROR: SampleDB DB file not found: " + dbEntryName;
			logger.severe(errMsg);
			throw new RuntimeException(errMsg);
		}

		// Copy entries in the DB jar file to corresponding location in db dir
		InputStream dbFileStream = new BufferedInputStream(fileURL.openStream());
		ZipInputStream zipStream = new ZipInputStream(dbFileStream);
		ZipEntry entry;
		while ((entry = zipStream.getNextEntry()) != null) {
			File entryFile = new File(dbDir, entry.getName());
			if (entry.isDirectory()) {
				entryFile.mkdir();
			} else {
				// Copy zip entry to local file
				entryFile.getParentFile().mkdirs();
				OutputStream os = new FileOutputStream(entryFile);
				byte[] buf = new byte[4000];
				int len;
				while ((len = zipStream.read(buf)) > 0) {
					os.write(buf, 0, len);
				}
				os.close();
			}
		}

		zipStream.close();
		dbFileStream.close();
	}

	/**
	 * Gets Derby connection URL
	 */
	public static String getDBUrl() {
		if (dbDir == null) {
			return "jdbc:derby:classpath:BirtSample";
		}
		return "jdbc:derby:" + dbDir + "/" + SAMPLE_DB_NAME;
	}

	/**
	 * Shuts down the Derby
	 */
	private void shutdownDerby() {
		IConnectionFactory cf = null;
		try {
			cf = JDBCDriverManager.getInstance().getDriverConnectionFactory(SampleDBConstants.DRIVER_CLASS);
		} catch (OdaException e) {
		}
		if (cf != null) {
			((SampleDBJDBCConnectionFactory) cf).shutdownDerby();
		}
	}

	/**
	 * Deletes all files created for the Derby database
	 */
	private void removeDatabase() {
		logger.info("Removing Sampledb DB directory at location " + dbDir);
		File dbDirFile = new File(dbDir);
		// recursively delete the DB directory
		if (!removeDirectory(dbDirFile)) {
			assert dbDirFile != null;
			dbDirFile.deleteOnExit();
			logger.info(
					"Fail to remove one or more file in temp db directory,but it will be removed when the VM exits: "
							+ dbDir);
		}
	}

	/**
	 * Do a best-effort removal of directory.
	 */
	static boolean removeDirectory(File dir) {
		assert dir != null && dir.isDirectory();
		boolean success = true;
		String[] children = dir.list();
		for (int i = 0; i < children.length; i++) {
			File child = new File(dir, children[i]);
			if (child.isDirectory()) {
				if (!removeDirectory(child)) {
					success = false;
				}
			} else if (!child.delete()) {
				logger.info("Failed to delete temp file " + child.getAbsolutePath());
				success = false;
			}
		}
		if (!dir.delete()) {
			success = false;
		}
		return success;
	}
}
