/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.preferences;

import java.text.MessageFormat;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class CPListLabelProvider extends LabelProvider {

	private String fMissing;
	private static final String[] fgArchiveExtensions = { "jar", "zip" }; //$NON-NLS-1$ //$NON-NLS-2$

	public CPListLabelProvider() {
		fMissing = Messages.getString("CPListLabelProvider.misssing"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) {
		if (element instanceof CPListElement) {
			return getCPListElementText((CPListElement) element);
		}
		return super.getText(element);
	}

	/**
	 * Gets the entry Text to display
	 * 
	 * @param cpentry
	 * @return
	 */
	public String getCPListElementText(CPListElement cpentry) {
		IPath path = cpentry.getPath();

		String label = getPathString(path, true);
		if (cpentry.isMissing()) {
			label = label + ' ' + fMissing;
		}
		return label;
	}

	private String getPathString(IPath path, boolean isExternal) {
		if (isArchivePath(path, true)) {
			String appended = getPathLabel(path.removeLastSegments(1), isExternal);
			String lastSegment = path.lastSegment();
			return MessageFormat.format("{0} - {1}", new String[] { //$NON-NLS-1$
					lastSegment, appended });
		} else {
			return getPathLabel(path, isExternal);
		}
	}

	public static boolean isArchivePath(IPath path, boolean allowAllAchives) {
		if (allowAllAchives)
			return true;

		String ext = path.getFileExtension();
		if (ext != null && ext.length() != 0) {
			return isArchiveFileExtension(ext);
		}
		return false;
	}

	public static boolean isArchiveFileExtension(String ext) {
		for (int i = 0; i < fgArchiveExtensions.length; i++) {
			if (ext.equalsIgnoreCase(fgArchiveExtensions[i])) {
				return true;
			}
		}
		return false;
	}

	private Image getCPListElementBaseImage(CPListElement cpentry) {
		if (isArchivePath(cpentry.getPath(), false)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_EXTJAR);
		} else {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_EXTFOL);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof CPListElement) {
			CPListElement cpentry = (CPListElement) element;
			Image image = getCPListElementBaseImage(cpentry);
			return image;
		}
		return null;
	}

	/**
	 * Gets the path label
	 * 
	 * @param path
	 * @param isOSPath
	 * @return
	 */
	public static String getPathLabel(IPath path, boolean isOSPath) {
		String label;
		if (isOSPath) {
			label = path.toOSString();
		} else {
			label = path.makeRelative().toString();
		}
		return label;
	}

}
