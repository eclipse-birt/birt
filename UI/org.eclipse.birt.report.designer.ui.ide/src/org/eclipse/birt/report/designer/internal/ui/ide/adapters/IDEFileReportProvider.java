/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.FileReportProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * IDE ReportProvider This ReportProvider uses IFileEditorInput as report editor
 * input class.
 */
public class IDEFileReportProvider implements IReportProvider {

	private ModuleHandle model = null;
	private static final String VERSION_MESSAGE = Messages.getString("TextPropertyDescriptor.Message.Version"); //$NON-NLS-1$
	private WorkspaceOperationRunner fOperationRunner;

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#connect(org
	 * .eclipse.birt.report.model.api.ModuleHandle)
	 */
	@Override
	public void connect(ModuleHandle model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * queryReportModuleHandle()
	 */
	@Override
	public ModuleHandle queryReportModuleHandle() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * getReportModuleHandle(java.lang.Object)
	 */
	@Override
	public ModuleHandle getReportModuleHandle(Object element) {
		return getReportModuleHandle(element, false);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#saveReport
	 * (org.eclipse.birt.report.model.api.ModuleHandle, java.lang.Object,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void saveReport(ModuleHandle moduleHandle, Object element, IProgressMonitor monitor) {
		saveReport(moduleHandle, element, null, monitor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#saveReport
	 * (org.eclipse.birt.report.model.api.ModuleHandle, java.lang.Object,
	 * org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void saveReport(ModuleHandle moduleHandle, Object element, IPath origReportPath, IProgressMonitor monitor) {
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) element;
			IFile file = input.getFile();
			if (ResourcesPlugin.getWorkspace().validateEdit(new IFile[] { file }, IWorkspace.VALIDATE_PROMPT)
					.getSeverity() == IStatus.OK) {
				saveFile(moduleHandle, file, origReportPath, monitor);
			}
		} else if (element instanceof IEditorInput) {
			IPath path = getInputPath((IEditorInput) element);
			if (path != null) {
				saveFile(moduleHandle, path.toFile(), origReportPath, monitor);
			}
		}

	}

	/**
	 * Save content to a java.io.File
	 *
	 * @param moduleHandle
	 * @param file
	 * @param monitor
	 */
	private void saveFile(final ModuleHandle moduleHandle, final File file, final IPath oldReportPath,
			IProgressMonitor monitor) {
		if (file.exists() && !file.canWrite()) {
			MessageDialog.openError(UIUtil.getDefaultShell(),
					Messages.getString("IDEFileReportProvider.ReadOnlyEncounter.Title"), //$NON-NLS-1$
					Messages.getFormattedString("IDEFileReportProvider.ReadOnlyEncounter.Message", //$NON-NLS-1$
							new Object[] { file.getAbsolutePath() }));
			return;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public synchronized final void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {

						@Override
						public void run(IProgressMonitor pm) throws CoreException {
							try {
								execute(pm);
							} catch (CoreException e) {
								throw e;
							} catch (IOException e) {
								ExceptionHandler.handle(e);
							}
						}
					};

					ResourcesPlugin.getWorkspace().run(workspaceRunnable, ResourcesPlugin.getWorkspace().getRoot(),
							IResource.NONE, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (OperationCanceledException e) {
					throw new InterruptedException(e.getMessage());
				}
			}

			public void execute(final IProgressMonitor monitor) throws CoreException, IOException {
				if (file.exists() || file.createNewFile()) {
					try (OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 8192 * 2)) {
						moduleHandle.serialize(out);
						out.flush();
						if (oldReportPath != null) {
							FileReportProvider.copyReportConfigFile(new Path(file.getAbsolutePath()), oldReportPath);
						}
					} catch (IOException e) {
					}
				}
			}
		};

		try {
			IRunnableContext runner = getOperationRunner(monitor);
			if (runner != null) {
				runner.run(false, false, op);
			} else {
				new ProgressMonitorDialog(UIUtil.getDefaultShell()).run(false, true, op);
			}
		} catch (InterruptedException x) {
			// do nothing now
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Save content to workspace file.
	 *
	 * @param moduleHandle
	 * @param file
	 * @param monitor
	 */
	private void saveFile(final ModuleHandle moduleHandle, final IFile file, final IPath oldReportPath,
			IProgressMonitor monitor) {
		if (file.exists() && file.isReadOnly()) {
			MessageDialog.openError(UIUtil.getDefaultShell(),
					Messages.getString("IDEFileReportProvider.ReadOnlyEncounter.Title"), //$NON-NLS-1$
					Messages.getFormattedString("IDEFileReportProvider.ReadOnlyEncounter.Message", //$NON-NLS-1$
							new Object[] { file.getFullPath() }));
			return;
		}

		IRunnableWithProgress op = new IRunnableWithProgress() {

			@Override
			public synchronized final void run(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				try {
					IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {

						@Override
						public void run(IProgressMonitor pm) throws CoreException {
							try {
								execute(pm);
							} catch (CoreException e) {
								throw e;
							} catch (IOException e) {
								ExceptionHandler.handle(e);
							}
						}
					};

					ResourcesPlugin.getWorkspace().run(workspaceRunnable, ResourcesPlugin.getWorkspace().getRoot(),
							IResource.NONE, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (OperationCanceledException e) {
					throw new InterruptedException(e.getMessage());
				}
			}

			public void execute(final IProgressMonitor monitor) throws CoreException, IOException {

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				moduleHandle.serialize(out);
				byte[] bytes = out.toByteArray();
				out.close();

				ByteArrayInputStream is = new ByteArrayInputStream(bytes);

				IContainer container = file.getParent();
				if (!container.exists() && container instanceof IFolder) {
					UIUtil.createFolder((IFolder) container, monitor);
				}

				if (file.exists()) {
					file.setContents(is, true, true, monitor);
				} else {
					// Save to new file.
					file.create(is, true, monitor);
				}

				if (oldReportPath != null) {
					FileReportProvider.copyReportConfigFile(file.getLocation(), oldReportPath);
				}
			}
		};

		try {
			IRunnableContext runner = getOperationRunner(monitor);
			if (runner != null) {
				runner.run(false, false, op);
			} else {
				new ProgressMonitorDialog(UIUtil.getDefaultShell()).run(false, true, op);
			}
		} catch (InterruptedException x) {
			// do nothing now
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		}

		try {
			file.refreshLocal(0, monitor);
		} catch (CoreException e) {
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportProvider#getSaveAsPath
	 * (java.lang.Object)
	 */
	@Override
	public IPath getSaveAsPath(Object element) {
		IFile file = null;
		if (element instanceof IFileEditorInput) {
			IFileEditorInput input = (IFileEditorInput) element;
			file = input.getFile();
		}
		SaveReportAsWizardDialog dialog = new SaveReportAsWizardDialog(UIUtil.getDefaultShell(),
				new SaveReportAsWizard(model, file));
		if (dialog.open() == Window.OK) {
			return dialog.getResult();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * createNewEditorInput(org.eclipse.core.runtime.IPath)
	 */
	@Override
	public IEditorInput createNewEditorInput(IPath path) {
		return new FileEditorInput(ResourcesPlugin.getWorkspace().getRoot().getFile(path));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getInputPath
	 * (org.eclipse.ui.IEditorInput)
	 */
	@Override
	public IPath getInputPath(IEditorInput input) {
		if (input instanceof IURIEditorInput) {
			// return new Path( ( (IURIEditorInput) input ).getURI( ).getPath( ) );
			URI uri = ((IURIEditorInput) input).getURI();
			if (uri == null && input instanceof IFileEditorInput) {
				return ((IFileEditorInput) input).getFile().getFullPath();
			}
			IPath localPath = URIUtil.toPath(uri);
			String host = uri.getHost();
			if (host != null && localPath == null) {
				return new Path(host + uri.getPath()).makeUNC(true);
			}
			return localPath;
		}
		if (input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile().getLocation();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * getReportDocumentProvider(java.lang.Object)
	 */

	@Override
	public IDocumentProvider getReportDocumentProvider(Object element) {
		if (element instanceof FileEditorInput) {
			// workspace file
			return new ReportDocumentProvider();
		} else {
			// system file
			return new IDEFileReportDocumentProvider();
		}
	}

	@Override
	public ModuleHandle getReportModuleHandle(Object element, boolean reset) {
		if (model == null || reset) {
			IEditorInput input = (IEditorInput) element;
			IPath path = getInputPath(input);
			if (path != null) {
				String fileName = path.toOSString();
				try {
					Map properties = new HashMap();

					String designerVersion = MessageFormat.format(VERSION_MESSAGE,
							new String[] { ReportPlugin.getVersion(), ReportPlugin.getBuildInfo() });
					properties.put(IModuleModel.CREATED_BY_PROP, designerVersion);
					properties.put(IModuleOption.CREATED_BY_KEY, designerVersion);
					if (fileName.endsWith("." + IReportElementConstants.TEMPLATE_FILE_EXTENSION)) {
						properties.put(IModuleOption.PARSER_SEMANTIC_CHECK_KEY, false);
					}
					String projectFolder = getProjectFolder(input);
					if (projectFolder != null) {
						properties.put(IModuleOption.RESOURCE_FOLDER_KEY, projectFolder);
					}
					model = SessionHandleAdapter.getInstance().init(fileName, new FileInputStream(path.toFile()),
							properties);
				} catch (DesignFileException | IOException e) {
					// not safe pop up a dialog here, just log it.
					ExceptionHandler.handle(e, true);
				}
			}
		}
		return model;
	}

	private String getProjectFolder(IEditorInput input) {

		String retValue = UIUtil.getProjectFolder(input);
		if (retValue == null) {
			IPath path = getInputPath(input);
			if (path != null) {
				return path.toFile().getParent();
			}
		}
		return retValue;
	}

	private IRunnableContext getOperationRunner(IProgressMonitor monitor) {
		if (fOperationRunner == null) {
			fOperationRunner = new WorkspaceOperationRunner();
		}
		fOperationRunner.setProgressMonitor(monitor);
		return fOperationRunner;
	}

}
