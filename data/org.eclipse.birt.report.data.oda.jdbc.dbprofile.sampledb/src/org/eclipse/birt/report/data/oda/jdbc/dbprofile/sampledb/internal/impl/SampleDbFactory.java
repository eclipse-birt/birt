/*
 *************************************************************************
 * Copyright (c) 2008, 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb.internal.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb.nls.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.services.PluginResourceLocator;

/**
 * The factory class that creates the BIRT SampleDB Derby Embedded Database
 * specified by the sample database connection profile. It is specified as the
 * connectionInitializer class in the extension that implements the
 * org.eclipse.datatools.connectivity.ProfileManagerInitializationProvider
 * extension point.
 */
public class SampleDbFactory implements IExecutableExtension {
	static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb"; //$NON-NLS-1$
	private static final String SAMPLEDB_PLUGIN_ID = "org.eclipse.birt.report.data.oda.sampledb"; //$NON-NLS-1$
	private static final String SAMPLE_DB_HOME_SUBDIR = "db"; //$NON-NLS-1$
	private static final String SAMPLE_DB_NAME = "BirtSample"; //$NON-NLS-1$
	private static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar"; //$NON-NLS-1$
	private static final String SAMPLE_DB_LOG = "log"; //$NON-NLS-1$
	private static final String SAMPLE_DB_SEG = "seg0"; //$NON-NLS-1$
	private static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$

	// key and name of pre-defined driver and profile definition, as specified in
	// the
	// org.eclipse.datatools.connectivity.ProfileManagerInitializationProvider
	// extension and plugin.properties
	private static final String DRIVER_DEFN_NAME_RESOURCE_KEY = "%driver.definition.name"; //$NON-NLS-1$
	private static final String SAMPLEDB_DRIVER_DEFN_DEFAULT_NAME = "BIRT SampleDb Derby Embedded Driver"; //$NON-NLS-1$
	private static final String SAMPLEDB_DRIVER_DEFN_RESOURCE_KEY = DRIVER_DEFN_NAME_RESOURCE_KEY + " " //$NON-NLS-1$
			+ SAMPLEDB_DRIVER_DEFN_DEFAULT_NAME;

	private static final String SAMPLEDB_DRIVER_DEFN_ID_PREFIX = "DriverDefn." + PLUGIN_ID + ".driverTemplate."; //$NON-NLS-1$ //$NON-NLS-2$
	static final String SAMPLEDB_DEFAULT_DRIVER_DEFN_ID = SAMPLEDB_DRIVER_DEFN_ID_PREFIX
			+ SAMPLEDB_DRIVER_DEFN_DEFAULT_NAME;

	private static final String PROFILE_NAME_RESOURCE_KEY = "%connection.profile.name"; //$NON-NLS-1$
	private static final String SAMPLEDB_DEFAULT_PROFILE_NAME = "BIRT Classic Models Sample Database"; //$NON-NLS-1$
	private static final String SAMPLEDB_PROFILE_NAME_RESOURCE_KEY = PROFILE_NAME_RESOURCE_KEY + " " //$NON-NLS-1$
			+ SAMPLEDB_DEFAULT_PROFILE_NAME;

	static final String SAMPLEDB_URL_RELATIVE_SUFFIX = PLUGIN_ID + PATH_SEPARATOR + SAMPLE_DB_HOME_SUBDIR
			+ PATH_SEPARATOR + SAMPLE_DB_NAME;

	private static String sm_nlsDriverDefinitionId;
	private static String sm_nlsDriverDefinitionName = getLocalizedDriverDefinitionNameImpl();
	private static String sm_nlsSampleDbProfileName = getLocalizedSampleDbProfileNameImpl();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.
	 * eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		String stateLocation = getSampleDbRootPath(PLUGIN_ID);

		try {
			initSampleDb(stateLocation);

			// remove the sampledb driver instance if its jar path is obsolete, so
			// a new driver instance with the correct jarList will get created automatically
			// by DTP profile manager as part of processing this
			// ProfileManagerInitializationProvider extension
			removeObsoleteDriverDefinition();
		} catch (RuntimeException | IOException ex) {
			ex.printStackTrace();
			throw new CoreException((IStatus) new Status(IStatus.ERROR, PLUGIN_ID, ex.getLocalizedMessage(), ex));
		}
	}

	static String getSampleDbRootPath(String extensionPluginId) {
		IPath workLoc = PluginResourceLocator.getPluginStateLocation(extensionPluginId);
		if (workLoc == null) // not found
		{
			// try use this plugin installation path instead
			workLoc = PluginResourceLocator.getPluginRootPath(extensionPluginId);
		}

		return workLoc != null ? workLoc.toOSString() : null;
	}

	private void initSampleDb(String rootPath) throws IOException, IllegalArgumentException {
		if (rootPath == null || rootPath.length() == 0 || !(new File(rootPath).isDirectory())) {
			throw new IllegalArgumentException(Messages.bind(Messages.sampleDbFactory_invalidDirectory, rootPath));
		}

		File dbDir = new File(rootPath + PATH_SEPARATOR + SAMPLE_DB_HOME_SUBDIR);
		if (dbDir.exists()) {
			// check if contains BirtSample sub-directory
			File[] subDir = dbDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.equals(SAMPLE_DB_NAME);
				}
			});
			if (subDir == null || subDir.length > 1) { // filter should have returned empty or one File in subDir
				throw new IllegalArgumentException(
						Messages.bind(Messages.sampleDbFactory_invalidDirectory, dbDir.toString()));
			}

			if (subDir.length == 1) {
				File sampleDb = subDir[0];
				if (!sampleDb.isDirectory()) {
					throw new IllegalArgumentException(
							Messages.bind(Messages.sampleDbFactory_invalidDirectory, sampleDb.toString()));
				}

				// check if BirtSample contains db content
				File[] sampleDbFiles = sampleDb.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.equals(SAMPLE_DB_LOG) || name.equals(SAMPLE_DB_SEG);
					}
				});

				if (sampleDbFiles.length == 2) {
					return; // done; BirtSample already exists
				}
			}
		}

		if (!dbDir.exists()) {
			dbDir.mkdir();
		}

		// unpack copy of Sample DB under rootPath/db

		// Get an input stream to read DB Jar file from sampledb plugin
		String dbJarEntryName = SAMPLE_DB_HOME_SUBDIR + PATH_SEPARATOR + SAMPLE_DB_JAR_FILE;
		URL fileURL = PluginResourceLocator.getPluginEntry(SAMPLEDB_PLUGIN_ID, dbJarEntryName);
		if (fileURL == null) {
			throw new RuntimeException(Messages.bind(Messages.sampleDbFactory_noSampleDbJarFile, dbJarEntryName));
		}

		// Copy entries in the DB jar file to corresponding location in db dir
		InputStream dbFileStream = new BufferedInputStream(fileURL.openStream());
		ZipInputStream zipStream = new ZipInputStream(dbFileStream);
		ZipEntry entry;
		while ((entry = zipStream.getNextEntry()) != null) {
			File entryFile = new File(dbDir, entry.getName());
			if (entry.isDirectory()) {
				entryFile.mkdirs();
			} else {
				File parent = entryFile.getParentFile();
				if (parent != null) {
					parent.mkdirs();
				}
				// Copy zip entry to local file
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

	private void removeObsoleteDriverDefinition() {
		String driverDefnName = getLocalizedDriverDefinitionName();

		// remove the driver definition instance if it is invalid
		ProfileDriverUtil.removeInvalidDriverDefinition(driverDefnName);
	}

	private static String getLocalizedDriverDefinitionName() {
		return sm_nlsDriverDefinitionName;
	}

	private static String getLocalizedDriverDefinitionNameImpl() {
		return PluginResourceLocator.getResourceString(PLUGIN_ID, SAMPLEDB_DRIVER_DEFN_RESOURCE_KEY);
	}

	static String getLocalizedSampleDbProfileName() {
		return sm_nlsSampleDbProfileName;
	}

	private static String getLocalizedSampleDbProfileNameImpl() {
		return PluginResourceLocator.getResourceString(PLUGIN_ID, SAMPLEDB_PROFILE_NAME_RESOURCE_KEY);
	}

	static String getLocalizedDriverDefinitionId() {
		if (sm_nlsDriverDefinitionId == null) {
			synchronized (SampleDbFactory.class) {
				if (sm_nlsDriverDefinitionId == null) {
					sm_nlsDriverDefinitionId = SAMPLEDB_DRIVER_DEFN_ID_PREFIX + getLocalizedDriverDefinitionName();
				}
			}
		}
		return sm_nlsDriverDefinitionId;
	}

}
