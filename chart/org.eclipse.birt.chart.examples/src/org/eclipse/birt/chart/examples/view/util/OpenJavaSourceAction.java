/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.eclipse.birt.chart.examples.view.ChartExamples;
import org.eclipse.birt.chart.examples.view.description.Messages;
import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.FileEditorInput;
import org.osgi.framework.Bundle;

public class OpenJavaSourceAction extends Action {

	private IWorkbenchWindow window;

	private static String JAVA_EXTENSION = ".java"; //$NON-NLS-1$

	public OpenJavaSourceAction(Tools tool, IWorkbenchWindow window) {
		super();
		this.window = window;
		String id = tool.group + '.' + tool.name;
		setId(id);
		// action = tool.action;
		setEnabled(tool.isEnabled());
		setImageDescriptor(UIHelper.getImageDescriptor(ExampleConstants.IMAGE_ENABLE_IMPORT)); // $NON-NLS-1$
		setDisabledImageDescriptor(UIHelper.getImageDescriptor(ExampleConstants.IMAGE_DISABLE_IMPORT));
		setToolTipText(Messages.getDescription("OpenJavaSourceAction.Text.ToolTip")); //$NON-NLS-1$
		setDescription(Messages.getDescription("OpenJavaSourceAction.Text.Description")); //$NON-NLS-1$
	}

	public void run() {
		String fileName = ChartExamples.getClassName();
		if (fileName != null) {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(getPath(fileName)));
			fileStore = fileStore.getChild(fileName + JAVA_EXTENSION);
			if (!fileStore.fetchInfo().isDirectory() && fileStore.fetchInfo().exists()) {
				IEditorInput input = createEditorInput(fileStore);
				// no org.eclipse.jdt.ui.CompilationUnitEditor in RCP
				// so it will open the java source with external editor.
				String editorId = getEditorId(fileStore);
				try {
					window.getActivePage().openEditor(input, editorId);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getPath(String fileName) {
		Bundle bundle = EclipseUtil.getBundle(ChartExamplesPlugin.ID);
		Path relativePath = new Path("/src/org/eclipse/birt/chart/examples/view/models/" + fileName + JAVA_EXTENSION); //$NON-NLS-1$
		URL relativeURL = FileLocator.find(bundle, relativePath, null);

		String absolutePath = null;
		try {
			URL absoluteURL = FileLocator.toFileURL(relativeURL);
			String tmp = absoluteURL.getPath();
			absolutePath = tmp.substring(0, tmp.lastIndexOf("/")); //$NON-NLS-1$
		} catch (IOException io) {
			io.printStackTrace();
		}
		return absolutePath;
	}

	private interface IEPInput extends IEditorInput, IPathEditorInput {

	}

	private IEditorInput createEditorInput(final IFileStore fileStore) {
		IFile workspaceFile = getWorkspaceFile(fileStore);
		if (workspaceFile != null)
			return new FileEditorInput(workspaceFile);
		IEditorInput iei = null;
		try {
			Class.forName("org.eclipse.ui.ide.FileStoreEditorInput"); //$NON-NLS-1$
			iei = new FileStoreEditorInput(fileStore);
		} catch (ClassNotFoundException e) {
			// RCP
			return new IEPInput() {

				public boolean exists() {
					return fileStore.fetchInfo().exists();
				}

				public ImageDescriptor getImageDescriptor() {
					return null;
				}

				public String getName() {
					return fileStore.getName();
				}

				public IPersistableElement getPersistable() {
					return null;
				}

				public String getToolTipText() {
					return fileStore.toString();
				}

				public Object getAdapter(Class adapter) {
					if (IWorkbenchAdapter.class.equals(adapter))
						return new IWorkbenchAdapter() {

							public Object[] getChildren(Object o) {
								return null;
							}

							public ImageDescriptor getImageDescriptor(Object object) {
								return null;
							}

							public String getLabel(Object o) {
								return ((FileStoreEditorInput) o).getName();
							}

							public Object getParent(Object o) {
								return null;
							}

						};
					return Platform.getAdapterManager().getAdapter(this, adapter);
				}

				public IPath getPath() {
					return new Path(fileStore.toURI().getPath());
				}

			};
		}
		return iei;
	}

	private IFile getWorkspaceFile(IFileStore fileStore) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IFile[] files = workspace.getRoot().findFilesForLocation(new Path(fileStore.toURI().getPath()));
		files = filterNonExistentFiles(files);
		if (files == null || files.length == 0) {
			return null;
		} else {
			return files[0];
		}
	}

	private IFile[] filterNonExistentFiles(IFile[] files) {
		if (files == null)
			return null;

		int length = files.length;
		ArrayList existentFiles = new ArrayList(length);
		for (int i = 0; i < length; i++) {
			if (files[i].exists())
				existentFiles.add(files[i]);
		}
		return (IFile[]) existentFiles.toArray(new IFile[existentFiles.size()]);
	}

	private String getEditorId(IFileStore file) {
		IWorkbench workbench = window.getWorkbench();
		IEditorRegistry editorRegistry = workbench.getEditorRegistry();
		IEditorDescriptor descriptor = editorRegistry.getDefaultEditor(file.getName(), getContentType(file));

		// check the OS for in-place editor (OLE on Win32)
		if (descriptor == null && editorRegistry.isSystemInPlaceEditorAvailable(file.getName())) {
			descriptor = editorRegistry.findEditor(IEditorRegistry.SYSTEM_INPLACE_EDITOR_ID);
		}

		// check the OS for external editor
		if (descriptor == null && editorRegistry.isSystemExternalEditorAvailable(file.getName())) {
			descriptor = editorRegistry.findEditor(IEditorRegistry.SYSTEM_EXTERNAL_EDITOR_ID);
		}

		return (descriptor == null) ? "" : descriptor.getId(); //$NON-NLS-1$
	}

	private IContentType getContentType(IFileStore fileStore) {
		if (fileStore == null)
			return null;

		InputStream stream = null;
		try {
			stream = fileStore.openInputStream(EFS.NONE, null);
			return Platform.getContentTypeManager().findContentTypeFor(stream, fileStore.getName());
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		} catch (CoreException ce) {
			// Do not log FileNotFoundException (no access)
			if (!(ce.getStatus().getException() instanceof FileNotFoundException))

				ce.printStackTrace();
			return null;
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}
