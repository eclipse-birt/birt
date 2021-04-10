/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

/**
 * Implement of ILibraryProvider Return libraries defined in preference and
 * libraries in the same project with the report file.
 * 
 * @deprecated
 */

public class LibraryProvider implements ILibraryProvider {

	private static final String MSG_OPEN_DEFINED_LIBRARY_ERROR_TITLE = Messages
			.getString("LibraryProvider.openDefinedLibrary.error.dialog.title"); //$NON-NLS-1$
	private static final String MSG_OPEN_DEFINED_LIBRARY_ERROR_MSG = Messages
			.getString("LibraryProvider.openDefinedLibrary.error.dialog.message"); //$NON-NLS-1$

	private List projectLibrarys;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.lib.ILibraryProvider#
	 * getLibrarys()
	 */
	public LibraryHandle[] getLibraries() {
		projectLibrarys = new ArrayList();
		ArrayList libList = new ArrayList();
		try {
			libList.addAll(getProjectLibraryList());
		} catch (CoreException e1) {
		}
		libList.addAll(getPreferenceLibraries(libList));
		return (LibraryHandle[]) libList.toArray(new LibraryHandle[libList.size()]);
	}

	private ArrayList getProjectLibraryList() throws CoreException {
		IEditorPart editor = UIUtil.getActiveEditor(true);
		if (editor != null) {
			IFile file = ((IFileEditorInput) editor.getEditorInput()).getFile();
			IProject project = file.getProject();
			IResource[] resources = project.members();
			return buildLibraryList(resources);
		}
		return null;
	}

	private ArrayList buildLibraryList(IResource[] resources) {
		ArrayList libList = new ArrayList();
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FILE) {
				LibraryHandle handle = null;
				try {
					handle = SessionHandleAdapter.getInstance().getSessionHandle()
							.openLibrary(resource.getLocation().toOSString());
				} catch (Exception e) {
					continue;
				}
				libList.add(handle);
				projectLibrarys.add(handle.getFileName());
			} else if (resource.getType() == IResource.FOLDER) {
				try {
					libList.addAll(buildLibraryList(((IFolder) resource).members()));
				} catch (CoreException e) {
				}
			}
		}
		return libList;
	}

	private ArrayList getPreferenceLibraries(ArrayList existList) {
		String[] predefinedLibrarys = ReportPlugin.getDefault().getLibraryPreference();
		ArrayList libList = new ArrayList();

		for (int i = 0; i < predefinedLibrarys.length; i++) {
			try {
				LibraryHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
						.openLibrary(predefinedLibrarys[i]);
				if (!isLibExist(existList, handle))
					libList.add(handle);
			} catch (DesignFileException e) {
				ExceptionUtil.handle(e, MSG_OPEN_DEFINED_LIBRARY_ERROR_TITLE, MSG_OPEN_DEFINED_LIBRARY_ERROR_MSG);
			}
		}
		return libList;
	}

	// private ArrayList getFolderLibraries( File folder )
	// {
	// ArrayList libList = new ArrayList( );
	// File[] libs = folder.listFiles( new FileFilter( ) {
	//
	// public boolean accept( File pathname )
	// {
	// return pathname.getPath( ).indexOf( ".rptlibrary" ) == pathname.getPath(
	// ) //$NON-NLS-1$
	// .length( ) - 11;
	// }
	//
	// } );
	// for ( int i = 0; i < libs.length; i++ )
	// {
	// File lib = libs[i];
	// if ( lib.isFile( ) )
	// {
	// LibraryHandle handle = null;
	// try
	// {
	// handle = SessionHandleAdapter.getInstance( )
	// .getSessionHandle( )
	// .openLibrary( lib.getAbsolutePath( ) );
	// }
	// catch ( DesignFileException e )
	// {
	// continue;
	// }
	// if ( !isLibExist( libList, handle ) )
	// libList.add( handle );
	// }
	// else
	// {
	// libList.addAll( getFolderLibraries( lib ) );
	// }
	// }
	// return libList;
	// }
	//
	// private File getInputForlder( )
	// {
	// IEditorPart editor = UIUtil.getActiveEditor( true );
	// if ( editor != null )
	// {
	// IFile file = ( (IFileEditorInput) editor.getEditorInput( ) ).getFile( );
	// IProject project = file.getProject( );
	// return project.getProjectRelativePath( ).toFile( );
	// }
	// return null;
	// }

	private boolean isLibExist(List list, LibraryHandle handle) {
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			LibraryHandle element = (LibraryHandle) iter.next();
			if (element.getFileName().equals(handle.getFileName()))
				return true;
		}
		return false;
	}

	private boolean isInProjectFolder(final LibraryHandle handle) {
		if (projectLibrarys == null) {
			return false;
		}
		return projectLibrarys.contains(handle.getFileName());
	}

	public Image getDisplayIcon(LibraryHandle handle) {
		if (!isInProjectFolder(handle)) {
			return ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_LIBRARY_REFERENCED);
		}
		return null;
	}
}
