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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.text.MessageFormat;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ide.IDE;

/**
 * 
 */

public class IDECPListLabelProvider extends LabelProvider {

	private String fNewLabel, fClassLabel, fMissing;
	private static final String[] fgArchiveExtensions = { "jar", "zip" }; //$NON-NLS-1$ //$NON-NLS-2$

	public IDECPListLabelProvider() {
		fNewLabel = Messages.getString("IDECPListLabelProvider.new"); //$NON-NLS-1$
		fClassLabel = Messages.getString("IDECPListLabelProvider.folder"); //$NON-NLS-1$
		fMissing = Messages.getString("IDECPListLabelProvider.missing"); //$NON-NLS-1$
	}

	public String getText(Object element) {
		if (element instanceof IDECPListElement) {
			return getCPListElementText((IDECPListElement) element);
		}
		return super.getText(element);
	}

	public String getCPListElementText(IDECPListElement cpentry) {
		IPath path = cpentry.getPath();
		switch (cpentry.getEntryKind()) {
		case IClasspathEntry.CPE_LIBRARY: {
			IResource resource = cpentry.getResource();
			if (resource instanceof IContainer) {
				StringBuffer buf = new StringBuffer(getPathLabel(path, false));
				buf.append(' ');
				buf.append(fClassLabel);
				if (!resource.exists()) {
					buf.append(' ');
					if (cpentry.isMissing()) {
						buf.append(fMissing);
					} else {
						buf.append(fNewLabel);
					}
				}
				return buf.toString();
			} else {
				String label = getPathString(path, resource == null);
				if (cpentry.isMissing()) {
					label = label + ' ' + fMissing;
				}
				return label;
			}
		}
		case IClasspathEntry.CPE_VARIABLE: {
			String label = getVariableString(path);
			if (cpentry.isMissing()) {
				label = label + ' ' + fMissing;
			}
			return label;
		}
		case IClasspathEntry.CPE_PROJECT:
			String label = path.lastSegment();
			if (cpentry.isMissing()) {
				label = label + ' ' + fMissing;
			}
			return label;
		case IClasspathEntry.CPE_SOURCE: {
			String pathLabel = getPathLabel(path, false);
			StringBuffer buf = new StringBuffer(pathLabel);

			IResource resource = cpentry.getResource();
			if (resource != null && !resource.exists()) {
				buf.append(' ');
				if (cpentry.isMissing()) {
					buf.append(fMissing);
				} else {
					buf.append(fNewLabel);
				}
			} else if (cpentry.getOrginalPath() == null) {
				buf.append(' ');
				buf.append(fNewLabel);
			}
			return buf.toString();
		}
		default:
			// pass
		}
		return Messages.getString("IDECPListLabelProvider.unknown"); //$NON-NLS-1$
	}

	private String getPathString(IPath path, boolean isExternal) {
		if (isArchivePath(path, true)) {
			String appended = getPathLabel(path.removeLastSegments(1), isExternal);
			String lastSegment = path.lastSegment();
			return MessageFormat.format("{0} - {1}", //$NON-NLS-1$
					new String[] { lastSegment, appended });
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

	private String getVariableString(IPath path) {
		String name = getPathLabel(path, false);
		IPath entryPath = JavaCore.getClasspathVariable(path.segment(0));
		if (entryPath != null) {
			String appended = getPathLabel(entryPath.append(path.removeFirstSegments(1)), true);
			return MessageFormat.format("{0} - {1}", //$NON-NLS-1$
					new String[] { name, appended });
		} else {
			return name;
		}
	}

	private Image getCPListElementBaseImage(IDECPListElement cpentry) {
		switch (cpentry.getEntryKind()) {
		case IClasspathEntry.CPE_LIBRARY:
			IResource res = cpentry.getResource();
			if (res == null) {
				if (isArchivePath(cpentry.getPath(), false)) {
					return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_EXTJAR);
				} else {
					return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_EXTFOL);
				}
			} else if (res instanceof IFile) {
				return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_JAR);
			} else {
				return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_FOL);
			}
		case IClasspathEntry.CPE_PROJECT:
			return ReportPlugin.getDefault().getWorkbench().getSharedImages()
					.getImage(IDE.SharedImages.IMG_OBJ_PROJECT);
		case IClasspathEntry.CPE_VARIABLE:
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_NODE_VARIABLE);
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		if (element instanceof IDECPListElement) {
			IDECPListElement cpentry = (IDECPListElement) element;
			Image image = getCPListElementBaseImage(cpentry);
			return image;
		}
		return null;
	}

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
