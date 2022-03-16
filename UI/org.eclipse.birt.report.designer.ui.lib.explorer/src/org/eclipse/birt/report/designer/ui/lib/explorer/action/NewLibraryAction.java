/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.dialog.NewLibraryDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.osgi.framework.Bundle;

/**
 * The action class for creating a libary in resource explorer.
 */
public class NewLibraryAction extends ResourceAction {

	/**
	 * Constructs an action for creating library.
	 *
	 * @param page the resource explorer page
	 */
	public NewLibraryAction(LibraryExplorerTreeViewPage page) {
		super(Messages.getString("NewLibraryAction.Text"), page); //$NON-NLS-1$
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_NEW_LIBRARY));
	}

	@Override
	public boolean isEnabled() {
		try {
			return canInsertIntoSelectedContainer();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void run() {
		try {
			File container = getSelectedContainer();

			if (container == null) {
				return;
			}

			NewLibraryDialog dialog = new NewLibraryDialog(
					getUniqueFile(container, Messages.getString("NewLibraryWizard.displayName.NewReportFileNamePrefix"), //$NON-NLS-1$
							Messages.getString("NewLibraryWizard.displayName.NewReportFileExtension"))); //$NON-NLS-1$

			if (dialog.open() == Window.OK) {
				createLibrary(dialog.getPath());
			}
		} catch (IOException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Returns an unique file with the specified prefix and the specified ext name
	 * in the specified path.
	 *
	 * @param path   the specified path.
	 * @param prefix the specified prefix name.
	 * @param ext    the specified ext name.
	 * @return the unique file.
	 */
	private File getUniqueFile(File path, String prefix, String ext) {
		int i = 0;
		File file = null;

		do {
			String filename = i == 0 ? prefix + ext : prefix + "_" + i + ext; //$NON-NLS-1$

			file = new File(path, filename);
			i++;
		} while (file != null && file.exists());

		return file;
	}

	/**
	 * Creates an library with the specified file name.
	 *
	 * @param fileName the library's file name.
	 * @throws IOException if an I/O error occurs.
	 */
	private void createLibrary(final String fileName) throws IOException {
		final String templateName = getLibraryTemplateName();
		IRunnableWithProgress op = new IRunnableWithProgress() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse
			 * .core.runtime.IProgressMonitor)
			 */
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(fileName, templateName, monitor);
				} catch (SemanticException | DesignFileException | IOException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			new ProgressMonitorDialog(getShell()).run(true, false, op);
		} catch (InvocationTargetException | InterruptedException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Returns the library template's file name.
	 *
	 * @return the library template's file name.
	 * @throws IOException if an I/O error occurs.
	 */
	private String getLibraryTemplateName() throws IOException {
		Bundle resourceBundle = Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST);
		URL url = resourceBundle == null ? null
				: FileLocator.find(resourceBundle, new Path("/templates/blank_library.rpttemplate"), //$NON-NLS-1$
						null);

		return url == null ? null : FileLocator.resolve(url).getPath();
	}

	/**
	 * Finishes the work.
	 *
	 * @param libraryName  the library's file name.
	 * @param templateName the library template's file name.
	 * @param monitor      the progress monitor to use to display progress and
	 *                     receive requests for cancelation.
	 * @throws DesignFileException If the library template is not found, or it
	 *                             contains fatal errors.
	 * @throws SemanticException   if the value of a property is incorrect.
	 * @throws IOException         if the file cannot be saved.
	 */
	private void doFinish(final String libraryName, String templateName, IProgressMonitor monitor)
			throws DesignFileException, SemanticException, IOException {
		monitor.beginTask(null, IProgressMonitor.UNKNOWN);

		try {
			makeLibrary(libraryName, templateName);
			openLibrary(new File(libraryName), true);
		} finally {
			monitor.done();
		}
	}

	/**
	 * Creates a new library with the specified file name.
	 *
	 * @param fileName     the library's file name.
	 * @param templateName the library template's file name.
	 * @throws DesignFileException If the library template is not found, or it
	 *                             contains fatal errors.
	 * @throws SemanticException   if the value of a property is incorrect.
	 * @throws IOException         if the file cannot be saved.
	 */
	private void makeLibrary(final String fileName, String templateName)
			throws DesignFileException, SemanticException, IOException {
		ModuleHandle handle = SessionHandleAdapter.getInstance().getSessionHandle()
				.createLibraryFromTemplate(templateName);

		if (ReportPlugin.getDefault().getEnableCommentPreference()) {
			handle.setStringProperty(ModuleHandle.COMMENTS_PROP, ReportPlugin.getDefault().getCommentPreference());
		}

		if (inPredifinedTemplateFolder(templateName)) {
			String description = handle.getDescription();

			if (description != null && description.trim().length() > 0) {
				handle.setDescription(Messages.getString(description));
			}
		}
		handle.saveAs(fileName);
		handle.close();
	}

	private boolean inPredifinedTemplateFolder(String templateName) {
		String predifinedDir = UIUtil.getFragmentDirectory();
		File predifinedFile = new File(predifinedDir);
		File sourceFile = new File(templateName);

		if (sourceFile.getAbsolutePath().startsWith(predifinedFile.getAbsolutePath())) {
			return true;
		}
		return false;
	}
}
