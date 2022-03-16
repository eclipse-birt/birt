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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.IReportElementConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * Report provider for file system report input.
 */
public class FileReportProvider implements IReportProvider {
	protected static final Logger logger = Logger.getLogger(FileReportProvider.class.getName());

	private ModuleHandle model;
	private static final String VERSION_MESSAGE = Messages.getString("TextPropertyDescriptor.Message.Version"); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * queryReportModuleHandle()
	 */
	@Override
	public ModuleHandle queryReportModuleHandle() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * getReportModuleHandle(java.lang.Object)
	 */
	@Override
	public ModuleHandle getReportModuleHandle(Object element) {
		return getReportModuleHandle(element, false);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * getReportModuleHandle(java.lang.Object, boolean)
	 */
	@Override
	public ModuleHandle getReportModuleHandle(Object element, boolean reset) {

		if ((model == null || reset) && element instanceof IPathEditorInput) {
			IPath path = ((IPathEditorInput) element).getPath();

			if (path != null) {
				String fileName = path.toOSString();
				InputStream stream = null;

				try {
					stream = new FileInputStream(path.toFile());

					Map properties = new HashMap();

					String designerVersion = MessageFormat.format(VERSION_MESSAGE,
							new String[] { ReportPlugin.getVersion(), ReportPlugin.getBuildInfo() });
					properties.put(IModuleModel.CREATED_BY_PROP, designerVersion);
					properties.put(IModuleOption.CREATED_BY_KEY, designerVersion);
					if (fileName.endsWith("." + IReportElementConstants.TEMPLATE_FILE_EXTENSION)) {
						properties.put(IModuleOption.PARSER_SEMANTIC_CHECK_KEY, false);
					}
					String projectFolder = getProjectFolder((IPathEditorInput) element);
					if (projectFolder != null) {
						properties.put(IModuleOption.RESOURCE_FOLDER_KEY, projectFolder);
					}
					model = SessionHandleAdapter.getInstance().init(fileName, stream, properties);
					// model.setResourceFolder( ReportPlugin.getDefault( ).getResourceFolder(
					// UIUtil.getCurrentProject( ), model) );
				} catch (DesignFileException | FileNotFoundException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				} finally {
					try {
						if (stream != null) {
							stream.close();
						}
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}
				}
			}
		}
		return model;
	}

	private String getProjectFolder(IEditorInput input) {
		return UIUtil.getProjectFolder(input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportProvider#saveReport(org.
	 * eclipse.birt.report.model.api.ModuleHandle, java.lang.Object,
	 * org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void saveReport(ModuleHandle moduleHandle, Object element, IProgressMonitor monitor) {
		saveReport(moduleHandle, element, null, monitor);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportProvider#saveReport(org.
	 * eclipse.birt.report.model.api.ModuleHandle, java.lang.Object,
	 * org.eclipse.core.runtime.IPath, org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void saveReport(ModuleHandle moduleHandle, Object element, IPath origReportPath, IProgressMonitor monitor) {
		if (element instanceof IPathEditorInput) {
			IPathEditorInput input = (IPathEditorInput) element;

			saveFile(moduleHandle, input.getPath().toFile(), origReportPath, monitor);
		}
	}

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
								ExceptionUtil.handle(e);
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
					OutputStream out = new BufferedOutputStream(new FileOutputStream(file), 8192 * 2);

					try (out) {
						moduleHandle.serialize(out);
						out.flush();
					}

					if (oldReportPath != null) {
						copyReportConfigFile(new Path(file.getAbsolutePath()), oldReportPath);
					}
				}
			}
		};

		try {
			new ProgressMonitorDialog(UIUtil.getDefaultShell()).run(false, true, op);
		}

		catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * Copys old report config file to new report config file.
	 *
	 * @param newReportPath the new report path.
	 * @param oldReportPath the old report path.
	 * @throws IOException if an error occurs.
	 */
	public static void copyReportConfigFile(IPath newReportPath, IPath oldReportPath) throws IOException {
		if (oldReportPath != null) {
			String retConfigExtension = "rptconfig"; //$NON-NLS-1$
			IPath newConfigPath = newReportPath.removeFileExtension();
			IPath oldConfigPath = oldReportPath.removeFileExtension();

			newConfigPath = newConfigPath.addFileExtension(retConfigExtension);
			oldConfigPath = oldConfigPath.addFileExtension(retConfigExtension);

			File newConfigFile = newConfigPath.toFile();
			File oldConfigFile = oldConfigPath.toFile();

			if (oldConfigFile.exists()) {
				copyFile(oldConfigFile, newConfigFile);
			} else if (newConfigFile.exists()) {
				if (!newConfigFile.delete()) {
					throw new IOException(Messages.getFormattedString("FileReportProvider.CopyConfigFile.DeleteFailure", //$NON-NLS-1$
							new Object[] { newConfigFile.getAbsolutePath() }));
				}
			}
		}
	}

	/**
	 * Copys a file to another file.
	 *
	 * @param srcFile  the source file
	 * @param destFile the target file
	 * @throws IOException if an error occurs.
	 */
	private static void copyFile(File srcFile, File destFile) throws IOException {
		if (srcFile.equals(destFile)) {
			// Does nothing if fils are same.
			return;
		}

		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;
		Throwable exception = null;

		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			fcin = fis.getChannel();
			fcout = fos.getChannel();

			// Does the file copy.
			fcin.transferTo(0, fcin.size(), fcout);
		} catch (Exception e) {
			exception = e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					exception = exception == null ? e : exception;
				}
			}

			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
					exception = exception == null ? e : exception;
				}
			}

			if (fcin != null) {
				try {
					fcin.close();
				} catch (Exception e) {
					exception = exception == null ? e : exception;
				}
			}

			if (fcout != null) {
				try {
					fcout.close();
				} catch (Exception e) {
					exception = exception == null ? e : exception;
				}
			}
		}

		if (exception instanceof RuntimeException) {
			throw (RuntimeException) exception;
		} else if (exception instanceof IOException) {
			throw (IOException) exception;
		} else if (exception != null) {
			logger.log(Level.SEVERE, exception.getMessage(), exception);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportProvider#getSaveAsPath(
	 * java.lang.Object)
	 */
	@Override
	public IPath getSaveAsPath(Object element) {
		if (element instanceof IPathEditorInput) {
			IEditorInput input = (IEditorInput) element;

			SaveReportAsWizardDialog dialog = new SaveReportAsWizardDialog(UIUtil.getDefaultShell(),
					new SaveReportAsWizard(model, input));
			if (dialog.open() == Window.OK) {
				return dialog.getResult();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * createNewEditorInput(org.eclipse.core.runtime.IPath)
	 */
	@Override
	public IEditorInput createNewEditorInput(IPath path) {
		File file = new File(path.toOSString());
		try {
			if (file.exists() || file.createNewFile()) {
				return new ReportEditorInput(file);
			}
		} catch (IOException e) {
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.ui.editors.IReportProvider#getInputPath(org.
	 * eclipse.ui.IEditorInput)
	 */
	@Override
	public IPath getInputPath(IEditorInput input) {
		if (input instanceof IPathEditorInput) {
			return ((IPathEditorInput) input).getPath();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#
	 * getReportDocumentProvider(java.lang.Object)
	 */
	@Override
	public IDocumentProvider getReportDocumentProvider(Object element) {
		return new FileReportDocumentProvider();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#connect(org.
	 * eclipse.birt.report.model.api.ModuleHandle)
	 */
	@Override
	public void connect(ModuleHandle handle) {
		model = handle;
	}

}
