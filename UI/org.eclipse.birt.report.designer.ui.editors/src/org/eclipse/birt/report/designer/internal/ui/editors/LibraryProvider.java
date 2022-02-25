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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPathEditorInput;

/**
 * Implement of ILibraryProvider Return libraries defined in preference and
 * libraries in the same folder(and sub folders) with the report file. Used in
 * when editor input is no workspace resource.
 *
 * @deprecated
 */

@Deprecated
public class LibraryProvider implements ILibraryProvider {

	private static final String MSG_OPEN_DEFINED_LIBRARY_ERROR_TITLE = Messages
			.getString("LibraryProvider.openDefinedLibrary.error.dialog.title"); //$NON-NLS-1$
	private static final String MSG_OPEN_DEFINED_LIBRARY_ERROR_MSG = Messages
			.getString("LibraryProvider.openDefinedLibrary.error.dialog.message"); //$NON-NLS-1$
	private static final LibraryHandle[] empty = {};

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.lib.ILibraryProvider#
	 * getLibrarys()
	 */
	@Override
	public LibraryHandle[] getLibraries() {
		File inputFolder = getInputForlder();
		if (inputFolder != null) {
			Set libSet = new LinkedHashSet();
			libSet.addAll(getFolderLibraries(inputFolder));
			String[] predefinedLibrarys = ReportPlugin.getDefault().getLibraryPreference();
			for (int i = 0; i < predefinedLibrarys.length; i++) {
				try {
					LibraryHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
							.openLibrary(predefinedLibrarys[i]);
					if (!isLibExist(libSet, handle)) {
						libSet.add(handle);
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e, MSG_OPEN_DEFINED_LIBRARY_ERROR_TITLE, MSG_OPEN_DEFINED_LIBRARY_ERROR_MSG);
				}
			}
			return (LibraryHandle[]) libSet.toArray(new LibraryHandle[libSet.size()]);
		}
		return empty;
	}

	private ArrayList getFolderLibraries(File folder) {
		ArrayList libList = new ArrayList();
		File[] libs = folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getPath().indexOf(".rptlibrary") == pathname.getPath() //$NON-NLS-1$
						.length() - 11;
			}

		});
		for (int i = 0; i < libs.length; i++) {
			File lib = libs[i];
			if (lib.isFile()) {
				LibraryHandle handle = null;
				try {
					handle = SessionHandleAdapter.getInstance().getSessionHandle().openLibrary(lib.getAbsolutePath());
				} catch (Exception e) {
					continue;
				}
				if (!isLibExist(libList, handle)) {
					libList.add(handle);
				}
			} else {
				libList.addAll(getFolderLibraries(lib));
			}
		}
		return libList;
	}

	private File getInputForlder() {
		IEditorPart editor = UIUtil.getActiveEditor(true);
		if (editor != null) {
			IEditorInput input = editor.getEditorInput();
			if (input instanceof IPathEditorInput) {
				return ((IPathEditorInput) input).getPath().toFile().getParentFile();
			}
		}
		return null;
	}

	private boolean isLibExist(Collection list, LibraryHandle handle) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			LibraryHandle element = (LibraryHandle) iter.next();
			if (element.getFileName().equals(handle.getFileName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isInCurrentFileFolder(final LibraryHandle handle) {
		return new File(handle.getFileName()).getParentFile().equals(getInputForlder());
	}

	@Override
	public Image getDisplayIcon(LibraryHandle handle) {
		if (!isInCurrentFileFolder(handle)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_LIBRARY_REFERENCED);
		}
		return null;
	}
}
