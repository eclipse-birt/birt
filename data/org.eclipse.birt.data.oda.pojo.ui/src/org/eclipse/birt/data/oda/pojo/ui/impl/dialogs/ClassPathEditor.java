
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

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ClassPathEditor {
	private Text txtClassPaths;

	public ClassPathEditor(Composite parent, Object layoutData, final File topDirForRelativePath, int index) {
		final Composite topComposite = new Composite(parent, SWT.NONE);
		topComposite.setLayoutData(layoutData);
		topComposite.setLayout(new GridLayout(2, false));

		txtClassPaths = new Text(topComposite, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gd.widthHint = 150;
		txtClassPaths.setLayoutData(gd);

		Composite buttonComposite = new Composite(topComposite, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		buttonComposite.setLayout(new FillLayout(SWT.VERTICAL));

		Button addJarsButton = new Button(buttonComposite, SWT.NONE);
		addJarsButton.setText(Messages.getString("DataSource.AddRelativeJars" + index)); //$NON-NLS-1$
		addJarsButton.setToolTipText(Messages.getString("DataSource.button.tooltip.AddRelativeJars")); //$NON-NLS-1$
		addJarsButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				JarsSelectionDialog jsd = new JarsSelectionDialog(topComposite.getShell(), topDirForRelativePath);
				jsd.open();
				String[] paths = jsd.getSelectedItems();
				for (String path : paths) {
					appendPathSeparator();
					txtClassPaths.append(path);
				}

			}
		});
		if (topDirForRelativePath == null) {
			// meaningless for relative path
			addJarsButton.setEnabled(false);
		}

		Button addClassFoldersButton = new Button(buttonComposite, SWT.NONE);
		addClassFoldersButton.setText(Messages.getString("DataSource.AddRelativeClassFolder" + index)); //$NON-NLS-1$
		addClassFoldersButton.setToolTipText(Messages.getString("DataSource.button.tooltip.AddRelativeClassFolder")); //$NON-NLS-1$
		addClassFoldersButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				ClassFoldersSelectionDialog cfsd = new ClassFoldersSelectionDialog(topComposite.getShell(),
						topDirForRelativePath);
				cfsd.open();
				String[] paths = cfsd.getSelectedItems();
				for (String path : paths) {
					appendPathSeparator();
					txtClassPaths.append(path);
				}
			}
		});
		if (topDirForRelativePath == null) {
			// meaningless for relative path
			addClassFoldersButton.setEnabled(false);
		}

		addJarsButton = new Button(buttonComposite, SWT.NONE);
		addJarsButton.setText(Messages.getString("DataSource.AddAbsoluteJars" + index)); //$NON-NLS-1$
		addJarsButton.setToolTipText(Messages.getString("DataSource.button.tooltip.AddAbsoluteJars")); //$NON-NLS-1$
		addJarsButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(txtClassPaths.getShell(), SWT.MULTI);
				dialog.setFilterExtensions(new String[] { "*.jar;*.zip" }); //$NON-NLS-1$
				dialog.open();
				for (String file : dialog.getFileNames()) {
					appendPathSeparator();
					txtClassPaths.append(dialog.getFilterPath() + File.separator);
					txtClassPaths.append(file);
				}
			}
		});

		addClassFoldersButton = new Button(buttonComposite, SWT.NONE);
		addClassFoldersButton.setText(Messages.getString("DataSource.AddAbsoluteClassFolder" + index)); //$NON-NLS-1$
		addClassFoldersButton.setToolTipText(Messages.getString("DataSource.button.tooltip.AddAbsoluteClassFolder")); //$NON-NLS-1$
		addClassFoldersButton.addSelectionListener(new SelectionAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events
			 * .SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(txtClassPaths.getShell());
				dialog.open();
				if (dialog.getFilterPath() != null && dialog.getFilterPath().length() > 0) {
					appendPathSeparator();
					txtClassPaths.append(dialog.getFilterPath());
				}
			}
		});
	}

	private void appendPathSeparator() {
		if (txtClassPaths.getText().trim().length() == 0) {
			txtClassPaths.setText(""); //$NON-NLS-1$
			return;
		}
		if (!txtClassPaths.getText().trim().endsWith(String.valueOf(Constants.CLASS_PATH_SEPERATOR))) {
			txtClassPaths.append(String.valueOf(Constants.CLASS_PATH_SEPERATOR));
		}
	}

	public void setClassPath(String s) {
		if (s == null) {
			txtClassPaths.setText(""); //$NON-NLS-1$
		} else {
			txtClassPaths.setText(s.trim());
		}
	}

	public String getClassPath() {
		return txtClassPaths.getText().trim();
	}
}
