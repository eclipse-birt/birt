/*******************************************************************************
 * Copyright (c) 2014 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.ide.navigator;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;
import org.eclipse.ui.model.AdaptableList;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchPartLabelProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;

public class ResourceCloseManagement {

	public static boolean saveDirtyAndCloseOpenFile(File file) {
		List<IResource> resources = new ArrayList<IResource>();
		String sFilePath = file.getAbsolutePath();
		IPath filePath = new Path(sFilePath);
		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();
		int offset = workspacePath.segmentCount();

		// get file path relative to its project
		IPath fileRelativePath = filePath.removeFirstSegments(offset);
		IResource res = ResourcesPlugin.getWorkspace().getRoot().getFile(fileRelativePath);
		resources.add(res);

		return saveDirtyAndCloseOpenFiles(resources);
	}

	public static boolean saveDirtyAndCloseOpenFiles(List<IResource> resources) {
		if (resources.isEmpty()) {
			return true;
		}
		List<IEditorPart> openedDirtyFiles = new ArrayList<IEditorPart>();
		List<IEditorPart> openedFiles = new ArrayList<IEditorPart>();

		try {
			checkOpenResources(resources, openedFiles, openedDirtyFiles);
		} catch (CoreException e) {
			ExceptionHandler.handle(e, true);
		}

		if (openedFiles.isEmpty()) {
			return true;
		}

		if (showClosingFileMessage(resources, openedFiles)) {
			if (!checkAndSaveDirtyFiles(openedDirtyFiles)) {
				return false;
			}
			closeAllOpenFiles(openedFiles);
			return true;
		}
		return false;
	}

	private static boolean showClosingFileMessage(List<IResource> resources, List<IEditorPart> openedFiles) {
		String msg = getResourceType(resources, openedFiles);
		return MessageDialog.openQuestion(null, Messages.getString("renameChecker.closeResourceTitle"), //$NON-NLS-1$
				msg + "  " + Messages.getString("renameChecker.closeResourceMessage.proceed")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String getResourceType(List<IResource> resources, List<IEditorPart> openedFiles) {

		IResource currentResource = resources.get(0);
		IEditorInput editorInput = openedFiles.get(0).getEditorInput();

		if (editorInput instanceof FileEditorInput) {
			currentResource = ((FileEditorInput) editorInput).getFile();
		}

		switch (currentResource.getType()) {
		case IResource.FILE:
			return openedFiles.size() != 1 ? Messages.getString("renameChecker.closeResourceMessage.forManyFile") //$NON-NLS-1$
					: currentResource.getName() + " " //$NON-NLS-1$
							+ Messages.getString("renameChecker.closeResourceMessage.forOneFile"); //$NON-NLS-1$
		case IResource.PROJECT:
			return Messages.getString("renameChecker.closeResourceMessage.forProject"); //$NON-NLS-1$
		default:
			return Messages.getString("renameChecker.closeResourceMessage.forFolder"); //$NON-NLS-1$
		}
	}

	/**
	 * Saves any modified files after confirmation from the user (if needed).
	 * 
	 * @return true if the files were saved, false otherwise.
	 */
	public static boolean checkAndSaveAllFiles() {
		ArrayList<IEditorPart> editorsToSave = new ArrayList<IEditorPart>();
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow windows[] = workbench.getWorkbenchWindows();
		for (int currWindow = 0; currWindow < windows.length; currWindow++) {
			IWorkbenchPage pages[] = windows[currWindow].getPages();
			for (int currPage = 0; currPage < pages.length; currPage++) {
				IEditorReference editors[] = pages[currPage].getEditorReferences();
				for (IEditorReference currEditorRef : editors) {
					IEditorPart currEditor = currEditorRef.getEditor(false);

					if (currEditor != null && currEditor.isDirty()) {
						editorsToSave.add(currEditor);
					}
				}
			}
		}

		// Ask to save open files
		return checkAndSaveDirtyFiles(editorsToSave);
	}

	public static void closeAllOpenFiles(List<IEditorPart> openedFiles) {
		for (IEditorPart part : openedFiles) {
			part.getSite().getPage().closeEditor(part, false);
		}
	}

	public static boolean checkAndSaveDirtyFiles(List<IEditorPart> openedDirtyFiles) {
		if (openedDirtyFiles.isEmpty()) {
			return true;
		}
		if (!showSaveDirtyFileDialog(openedDirtyFiles)) {
			return false;
		}

		final List<IEditorPart> finalEditors = openedDirtyFiles;
		// Copied code from EditorManager
		IRunnableWithProgress progressOp = new IRunnableWithProgress() {

			public void run(IProgressMonitor monitor) {
				monitor.beginTask("", finalEditors.size()); //$NON-NLS-1$
				Iterator<IEditorPart> edenum = finalEditors.iterator();
				while (edenum.hasNext()) {
					IEditorPart part = edenum.next();
					part.doSave(new SubProgressMonitor(monitor, 1));
				}
				monitor.done();
			}

		};

		IRunnableContext ctx = new ProgressMonitorDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		try {
			ctx.run(false, false, progressOp);
		} catch (InvocationTargetException e) {
			ExceptionHandler.handle(e, true);
		} catch (InterruptedException e) {
		}

		return true;
	}

	private static boolean showSaveDirtyFileDialog(List<IEditorPart> dirtyEditors) {
		AdaptableList input = new AdaptableList(dirtyEditors);

		ListDialog dlg = new ListDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
		dlg.setContentProvider(new BaseWorkbenchContentProvider());
		dlg.setLabelProvider(new WorkbenchPartLabelProvider());
		dlg.setInput(input);
		dlg.setMessage(Messages.getString("renameChecker.saveResourcesMessage")); //$NON-NLS-1$
		dlg.setTitle(Messages.getString("renameChecker.saveResourcesTitle")); //$NON-NLS-1$

		// Just return false to prevent the operation continuing
		return dlg.open() == IDialogConstants.OK_ID;
	}

	private static IEditorReference getEditorRefInOpenFileList(IFile resourceToCheck) {
		IEditorReference[] editors = getOpenedFileRefs();
		for (int i = 0; i < editors.length; i++) {
			IFile file = getEditorFile(editors[i]);

			if ((file != null) && file.equals(resourceToCheck)) {
				return editors[i];
			}
		}
		return null;
	}

	private static IFile getEditorFile(IEditorReference fileRef) {
		if (fileRef != null) {
			IEditorPart part = (IEditorPart) fileRef.getPart(false);
			if (part != null) {
				IEditorInput input = part.getEditorInput();

				if (input != null && input instanceof IFileEditorInput) {
					return ((IFileEditorInput) input).getFile();
				}
			}
		}
		return null;
	}

	private static void checkOpenResources(List<IResource> itemsToCheck, List<IEditorPart> openedEditorRefs,
			List<IEditorPart> openedDirtyEditorRefs) throws CoreException {
		for (IResource resourceToCheck : itemsToCheck) {
			switch (resourceToCheck.getType()) {
			case IResource.FILE:
				IEditorReference fileRef = getEditorRefInOpenFileList((IFile) resourceToCheck);
				checkAndAddToEditorLists(openedEditorRefs, openedDirtyEditorRefs, fileRef);
				break;
			case IResource.PROJECT:
				getOpenedFileInProject((IProject) resourceToCheck, openedEditorRefs, openedDirtyEditorRefs);
				break;
			default:
				checkOpenResources(Arrays.asList(((IContainer) resourceToCheck).members()), openedEditorRefs,
						openedDirtyEditorRefs);
			}
		}
	}

	private static void checkAndAddToEditorLists(List<IEditorPart> openedEditorRefs,
			List<IEditorPart> openedDirtyEditorRefs, IEditorReference fileRef) {
		if (fileRef != null) {
			IEditorPart part = (IEditorPart) fileRef.getPart(false);
			if (part != null) {
				if (part.isDirty()) {
					openedDirtyEditorRefs.add(part);
				}
				openedEditorRefs.add(part);
			}
		}
	}

	private static void getOpenedFileInProject(IProject project, List<IEditorPart> openedEditorRefs,
			List<IEditorPart> openedDirtyEditorRefs) {
		IEditorReference[] editors = getOpenedFileRefs();
		for (int i = 0; i < editors.length; i++) {
			IFile file = getEditorFile(editors[i]);

			if ((file != null) && (file.getProject() != null) && file.getProject().equals(project)) {
				checkAndAddToEditorLists(openedEditorRefs, openedDirtyEditorRefs, editors[i]);
			}
		}
	}

	private static IEditorReference[] getOpenedFileRefs() {
		IWorkbenchWindow window = org.eclipse.ui.PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IWorkbenchPage page = window.getActivePage();

		return page.getEditorReferences();
	}

}
