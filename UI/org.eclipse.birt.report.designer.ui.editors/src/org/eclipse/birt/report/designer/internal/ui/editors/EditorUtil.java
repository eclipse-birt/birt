/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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
import java.io.IOException;
import java.net.URL;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IPathEditorInputFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

/**
 * EditorUtil
 */
public class EditorUtil {

	public static void openEditor(Object adaptable, File target, String editorId) throws PartInitException {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench == null ? null : workbench.getActiveWorkbenchWindow();

		IWorkbenchPage page = window == null ? null : window.getActivePage();

		if (page != null) {
			IEditorInput input = null;
			Object adapter = Platform.getAdapterManager().getAdapter(adaptable, IPathEditorInputFactory.class);

			if (adapter instanceof IPathEditorInputFactory) {
				input = ((IPathEditorInputFactory) adapter).create(new Path(target.getAbsolutePath()));
				IFile file = (IFile) input.getAdapter(IFile.class);
				if (file != null) {
					try {
						file.refreshLocal(IResource.DEPTH_INFINITE, null);
					} catch (CoreException e) {
						// do nothing now
					}
				}
			}

			if (input == null) {
				input = new ReportEditorInput(target);
			}

			page.openEditor(input, editorId, true);
		}
	}

	public static File convertToFile(URL url) throws IOException {
		if (url == null) {
			throw new IOException(Messages.getString("ResourceAction.ConvertToFile.URLIsNull")); //$NON-NLS-1$
		}

		URL fileURL = FileLocator.toFileURL(url);
		IPath path = new Path((fileURL).getPath());
		String ref = fileURL.getRef();
		String fullPath = path.toFile().getAbsolutePath();

		if (ref != null) {
			ref = "#" + ref; //$NON-NLS-1$
			if (path.toString().endsWith("/")) //$NON-NLS-1$
			{
				return path.append(ref).toFile();
			} else {
				fullPath += ref;
			}
		}
		return new File(fullPath);
	}

}
