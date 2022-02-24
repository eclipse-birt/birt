
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.ui.PlatformUI;

/**
 * Jar file information and related action like add jar, delelte jar, check jar
 * state
 */
public class JarFile implements Serializable {
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -765442524028110564L;

	/**
	 * File name of the Jar file
	 */
	private String fileName;

	/**
	 * full path of the Jar file
	 */
	private String filePath;

	/**
	 * jar state of 'oda file not exist','original file not exist','has been
	 * restored'
	 */
	private String state;

	/**
	 * whether the jar file will be deleted on exit.
	 */
	private boolean toBeDeleted;

	/**
	 * indicate whether the jar file has been restored,it is a inner state flag used
	 * by checkJarState()
	 */
	private transient boolean hasRestored;

	public static final String FILE_HAS_BEEN_RESOTRED = "+"; //$NON-NLS-1$
	public static final String ODA_FILE_NOT_EXIST_TOKEN = "x"; //$NON-NLS-1$
	public static final String ORIGINAL_FILE_NOT_EXIST_TOKEN = "*"; //$NON-NLS-1$

	public JarFile(String fileName, String filePath, String state, boolean toBeDeleted) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.state = state;
		this.toBeDeleted = toBeDeleted;
		hasRestored = false;
	}

	public String getFilePath() {
		return filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public String getState() {
		return state;
	}

	public boolean isToBeDeleted() {
		return toBeDeleted;
	}

	public void setToBeDeleted(boolean toBeDeleted) {
		this.toBeDeleted = toBeDeleted;
	}

	public void setRestored() {
		this.hasRestored = true;
	}

	/**
	 * Copies the specified file to ODA driver path and viewer dirver path.
	 * 
	 * @param filePath
	 */
	public void copyJarToODADir() {
		File source = new File(filePath);

		File odaDir = getDriverLocation();

		File dest1 = null;

		if (odaDir != null) {
			dest1 = new File(odaDir.getAbsolutePath() + File.separator + source.getName());
		}

		if (source.exists()) {
			FileChannel in = null, out1 = null;
			try {
				if (dest1 != null) {
					try {
						out1 = new FileOutputStream(dest1).getChannel();
					} catch (FileNotFoundException e) {
						// does nothing.
					}
				}

				if (out1 != null) {
					in = new FileInputStream(source).getChannel();
					long size = in.size();
					MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
					out1.write(buf);
				}

				try {
					if (in != null) {
						in.close();
					}
				} catch (IOException e1) {
					// does nothing.
				}
			} catch (FileNotFoundException e) {
				// does nothing.
			} catch (IOException e) {
				// does nothing.
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out1 != null) {
						out1.close();
					}
				} catch (IOException e1) {
					// does nothing.
				}
			}
		}
	}

	/**
	 * Deletes the specified file from ODA driver path and viewer dirver path, NOTE
	 * just the file name is used.
	 * 
	 * @param filePath
	 */
	public void deleteJarFromODADir() {
		File source = new File(filePath);

		File odaDir = getDriverLocation();

		File dest1 = null;

		if (odaDir != null) {
			dest1 = new File(odaDir.getAbsolutePath() + File.separator + source.getName());

			if (dest1.exists()) {
				if (!dest1.delete()) {
					dest1.deleteOnExit();
				}
			}
		}
	}

	/**
	 * check if the jar exist in the oda driver directory or exist in the disk. x -
	 * not exist in the oda dirctory. <br>
	 * * - not exist in the disk.
	 */
	public void checkJarState() {
		if (hasRestored == true) {
			state = FILE_HAS_BEEN_RESOTRED;
		} else {
			File f = new File(filePath);
			if (!isUnderODAPath(f)) {
				if (f.exists())
					state = JarFile.ODA_FILE_NOT_EXIST_TOKEN;
				else
					state = JarFile.ODA_FILE_NOT_EXIST_TOKEN + JarFile.ORIGINAL_FILE_NOT_EXIST_TOKEN;
			} else {
				if (f.exists())
					state = ""; //$NON-NLS-1$
				else
					state = JarFile.ORIGINAL_FILE_NOT_EXIST_TOKEN;
			}
		}
	}

	/**
	 * Returns the ODA dirvers directory path. <br>
	 */
	public static File getDriverLocation() {
		try {
			return OdaJdbcDriver.getDriverDirectory();
		} catch (IOException e) {
			ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JdbcPlugin.getResourceString("exceptionHandler.title.error"), e.getLocalizedMessage(), e);
		} catch (OdaException e) {
			ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JdbcPlugin.getResourceString("exceptionHandler.title.error"), e.getLocalizedMessage(), e);
		}

		return null;
	}

	/**
	 * check whether the given file is under ODA Drivers path.
	 * 
	 * @param f the file to be checked
	 * @return true if <tt>f</tt> is under ODA Drivers path,else false
	 */
	private boolean isUnderODAPath(File f) {
		File odaPath = getDriverLocation();

		File ff = new File(odaPath + File.separator + f.getName());

		return ff.exists();
	}

}
