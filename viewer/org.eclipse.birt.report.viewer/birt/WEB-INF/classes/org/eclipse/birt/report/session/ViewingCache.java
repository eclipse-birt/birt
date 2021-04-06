/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.session;

import java.io.File;
import java.io.Serializable;

import org.eclipse.birt.report.IBirtConstants;

/**
 * Manager class for the files cached for each session.
 */
public class ViewingCache implements Serializable {
	private static final long serialVersionUID = -348510678864943788L;

	/**
	 * Prefix of sub document folder
	 */
	private static final String PREFIX_SUB_DOC_FOLDER = "BIRTDOC"; //$NON-NLS-1$

	/**
	 * Prefix of sub image folder
	 */
	private static final String PREFIX_SUB_IMAGE_FOLDER = "BIRTIMG"; //$NON-NLS-1$

	/**
	 * Document folder to put the report files and created documents.
	 */
	public String documentFolder = null;

	/**
	 * Image folder to put the image files
	 */
	public String imageFolder = null;

	/**
	 * Instantiates a new cache manager.
	 * 
	 * @param documentFolder base folder to use for cached documents
	 * @param imageFolder    base folder to use for cached images
	 */
	public ViewingCache(String documentFolder, String imageFolder) {
		this.documentFolder = documentFolder;
		this.imageFolder = imageFolder;

		if (!documentFolder.endsWith(File.separator)) {
			this.documentFolder += File.separator;
		}
		if (!imageFolder.endsWith(File.separator)) {
			this.imageFolder += File.separator;
		}

		clearTempFiles();
	}

	/**
	 * Create the file path of the the document. The document will be put under the
	 * document folder based on different session id.
	 * 
	 * @param filePath the document path cretaed from the report design file.
	 * @param request  Http request, used to get the session Id.
	 * @return
	 */
	public String createDocumentPath(String sessionId, String subSessionId, String filePath) {

		String documentName = null;

		if ((filePath == null) || (filePath.length() == 0))
			return ""; //$NON-NLS-1$

		String fileSeparator = "\\"; //$NON-NLS-1$

		if (filePath.lastIndexOf(fileSeparator) == -1)
			fileSeparator = "/"; //$NON-NLS-1$

		// parse document file name
		if (filePath.lastIndexOf(fileSeparator) != -1) {

			documentName = filePath.substring(filePath.lastIndexOf(fileSeparator) + 1);
		} else {
			documentName = filePath;
		}

		return documentFolder + getSessionSubfolder(PREFIX_SUB_DOC_FOLDER, sessionId, subSessionId) + documentName;
	}

	/**
	 * Returns the path to the folder containing the files for a given sub session.
	 * 
	 * @param sessionId session ID
	 * @return file path
	 */
	private String getSessionSubfolder(String prefix, String sessionId, String subSessionId) {
		String folder = ""; //$NON-NLS-1$
		if (sessionId != null) {
			folder = (prefix + sessionId) + File.separator;
			if (subSessionId != null) {
				folder += subSessionId + File.separator;
			}
		} else {
			folder = ""; //$NON-NLS-1$
		}
		return folder;
	}

	/**
	 * Returns the temp image folder with session id
	 * 
	 * @param request
	 * @return
	 */
	public String getImageTempFolder(String sessionId, String subSessionId) {
		return imageFolder + getSessionSubfolder(PREFIX_SUB_IMAGE_FOLDER, sessionId, subSessionId);
	}

	/**
	 * Clear the temp files when session is expired
	 * 
	 * @param sessionId    session ID
	 * @param subSessionId sub session ID or null to clear the master session
	 */
	public void clearSession(String sessionId, String subSessionId) {
		if (sessionId == null)
			return;

		deleteDir(documentFolder + getSessionSubfolder(PREFIX_SUB_DOC_FOLDER, sessionId, subSessionId));
		deleteDir(imageFolder + getSessionSubfolder(PREFIX_SUB_IMAGE_FOLDER, sessionId, subSessionId));
	}

	/**
	 * Clears the report document/image files which had been created last time the
	 * server starts up.
	 */
	private void clearTempFiles() {
		// clear the document files
		File file = new File(documentFolder);
		if (file != null && file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				if (children[i].startsWith(PREFIX_SUB_DOC_FOLDER))
					deleteDir(new File(file, children[i]));
			}
		}

		// clear image files
		file = new File(imageFolder);
		if (file != null && file.isDirectory()) {
			String[] children = file.list();
			for (int i = 0; i < children.length; i++) {
				if (children[i].startsWith(PREFIX_SUB_IMAGE_FOLDER))
					deleteDir(new File(file, children[i]));
			}
		}
	}

	/**
	 * Deletes all files and sub directories under dirName. Returns true if all
	 * deletions were successful. If a deletion fails, the method stops attempting
	 * to delete and returns false.
	 * 
	 * @param dir directory name
	 */
	private static boolean deleteDir(String dirName) {
		return deleteDir(new File(dirName));
	}

	/**
	 * Deletes all files and subdirectories under dir. Returns true if all deletions
	 * were successful. If a deletion fails, the method stops attempting to delete
	 * and returns false.
	 * 
	 * @param dir directory
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// The directory is now empty so delete it
		return dir.delete();
	}

	/**
	 * Generate document name according to report name.
	 * 
	 * @param reportName
	 * @param id
	 * @return document name.
	 */

	private static String generateDocumentFromReport(String reportName, String id) {
		if (reportName == null)
			return null;

		String documentFile = reportName;
		if (reportName.indexOf('.') >= 0) {
			documentFile = reportName.substring(0, reportName.lastIndexOf('.'));
		}

		// Get viewer id
		if (id != null && id.length() > 0) {
			documentFile = documentFile + id + IBirtConstants.SUFFIX_REPORT_DOCUMENT;
		} else {
			documentFile = documentFile + IBirtConstants.SUFFIX_REPORT_DOCUMENT;
		}

		return documentFile;
	}

	/**
	 * Return the document file according to report name
	 * 
	 * @param request
	 * @param reportFile
	 * @param id
	 * @return
	 */
	public String getReportDocument(String reportFile, String sessionId, String subSessionId, String viewerId) {
		if (reportFile == null)
			return null;

		String documentFile = generateDocumentFromReport(reportFile, viewerId);
		documentFile = createDocumentPath(sessionId, subSessionId, documentFile);

		return documentFile;

	}
}
