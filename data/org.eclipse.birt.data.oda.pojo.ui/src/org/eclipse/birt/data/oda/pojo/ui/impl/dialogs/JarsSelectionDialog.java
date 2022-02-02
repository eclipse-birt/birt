/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.Activator;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * 
 */

public class JarsSelectionDialog extends ElementTreeSelectionDialog {

	private static final String JAR_EXT = ".jar"; //$NON-NLS-1$
	private static final String ZIP_EXT = ".zip"; //$NON-NLS-1$

	private final static Image IMG_FOLDER = PlatformUI.getWorkbench().getSharedImages()
			.getImage(ISharedImages.IMG_OBJ_FOLDER);

	private final static Image IMG_FILE = PlatformUI.getWorkbench().getSharedImages()
			.getImage(ISharedImages.IMG_OBJ_FILE);

	private File topDir;
	private Button importButton;

	public JarsSelectionDialog(Shell parent, File topDir) {
		super(parent, new LabelProvider(), new ContentProvider());

		assert topDir != null;

		this.setValidator(new SelectionValidator());
		this.setInput(topDir.getAbsolutePath());
		this.setTitle(Messages.getString("DataSet.JarsSelectDlg.Title")); //$NON-NLS-1$
		this.topDir = topDir;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org
	 * .eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Control c = super.createDialogArea(parent);

		createImportFileArea((Composite) c);

		this.getTreeViewer().expandToLevel(2);
		return c;
	}

	private void createImportFileArea(Composite parent) {
		importButton = new Button(parent, SWT.PUSH);
		importButton.setText(Messages.getString("JarsSelectionDialog.button.importFile")); //$NON-NLS-1$
		importButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterExtensions(new String[] { "*.jar;*.zip" //$NON-NLS-1$
				});

				String selectedLocation = dialog.open();
				if (selectedLocation != null) {
					File targetFolder = getSelectedFolder();
					File srcFile = new File(selectedLocation);
					File targetFile = new File(targetFolder, srcFile.getName());

					if (targetFile.exists() && !MessageDialog.openConfirm(getShell(),
							Messages.getString("JarsSelectionDialog.title.overwrite"), //$NON-NLS-1$
							Messages.getString("JarsSelectionDialog.overwrite.msg"))) //$NON-NLS-1$
					{
						return;
					}

					importFile(targetFile, srcFile);

					IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault()
							.getResourceSynchronizerService();

					if (synchronizer != null) {
						synchronizer.notifyResourceChanged(
								new ReportResourceChangeEvent(this, Path.fromOSString(targetFile.getAbsolutePath()),
										IReportResourceChangeEvent.NewResource));
					}

					getTreeViewer().refresh(targetFolder);
					getTreeViewer().expandToLevel(targetFolder, 1);
				}
			};
		});
	}

	private File getSelectedFolder() {
		Object[] selection = getResult();
		if (selection != null && selection.length > 0) {
			if (selection[0] instanceof File) {
				File f = (File) selection[0];

				if (f.isFile()) {
					return f.getParentFile();
				}
				return f;
			}
		}
		return null;
	}

	private void importFile(final File target, final File src) {
		try {
			new ProgressMonitorDialog(getShell()).run(true, false, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.getString("JarsSelectionDialog.import.msg"), //$NON-NLS-1$
							1);

					try {
						doImport(target, src);
					} catch (IOException e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (Exception e) {
			MessageDialog.openError(getShell(), Messages.getString("JarsSelectionDialog.title.error"), //$NON-NLS-1$
					e.getLocalizedMessage());
		}
	}

	private void doImport(File target, File src) throws IOException {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			fis = new FileInputStream(src);
			fos = new FileOutputStream(target);
			fcin = fis.getChannel();
			fcout = fos.getChannel();

			fcin.transferTo(0, fcin.size(), fcout);
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (fcin != null) {
				fcin.close();
			}
			if (fcout != null) {
				fcout.close();
			}
		}
	}

	@Override
	protected void updateOKStatus() {
		super.updateOKStatus();

		importButton.setEnabled(getSelectedFolder() != null);
	}

	private static class ContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object arg0) {
			if (arg0 instanceof File) {
				File f = (File) arg0;
				return JarsSelectionDialog.getChildren(f);
			}
			return null;
		}

		public Object getParent(Object arg0) {
			return null;
		}

		public boolean hasChildren(Object arg0) {
			if (arg0 instanceof File) {
				File f = (File) arg0;
				return JarsSelectionDialog.getChildren(f).length > 0;
			}
			return false;
		}

		public Object[] getElements(Object arg0) {
			if (arg0 instanceof String) {
				return new Object[] { new File((String) arg0) };
			}
			return null;
		}

		public void dispose() {

		}

		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {

		}
	}

	private static class LabelProvider implements ILabelProvider {

		public Image getImage(Object arg0) {
			if (arg0 instanceof File) {
				File f = (File) arg0;
				if (f.isFile()) {
					return IMG_FILE;
				}
				return IMG_FOLDER;
			}
			return null;
		}

		public String getText(Object arg0) {
			if (arg0 instanceof File) {
				File f = (File) arg0;
				if (f.getName().trim().equals("")) //$NON-NLS-1$
				{
					// For the case "File("C:\\")"
					return f.getPath();
				}
				return f.getName();
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener arg0) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		public void removeListener(ILabelProviderListener arg0) {

		}

	}

	private static class SelectionValidator implements ISelectionStatusValidator {

		public IStatus validate(Object[] selections) {
			if (selections != null && selections.length > 0) {
				for (Object o : selections) {
					if (o instanceof File) {
						if (((File) o).isFile()) {
							return new Status(IStatus.OK, Activator.PLUGIN_ID, IStatus.OK, "", //$NON-NLS-1$
									null);
						}
					}
				}
			}
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, "", //$NON-NLS-1$
					null);
		}

	}

	private static File[] getChildren(File f) {
		if (!f.isDirectory()) {
			return new File[0];
		}
		File[] result = f.listFiles(new FileFilter() {

			public boolean accept(File child) {
				if (child.isDirectory()) {
					return true;
				}
				String name = child.getName().toLowerCase();
				return name.endsWith(JAR_EXT) || name.endsWith(ZIP_EXT);
			}
		});
		if (result != null) {
			Arrays.sort(result, new Utils.FileComparator());
		}
		return result == null ? new File[0] : result;
	}

	public String[] getSelectedItems() {
		List<String> result = new ArrayList<String>();
		Object[] selected = this.getResult() == null ? new Object[0] : this.getResult();
		for (Object o : selected) {
			File f = (File) o;
			if (f.isFile()) {
				URI relative = topDir.toURI().relativize(f.toURI());
				result.add(relative.getPath());
			}
		}

		return result.toArray(new String[0]);
	}
}
