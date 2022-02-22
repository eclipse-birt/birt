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

package org.eclipse.birt.report.designer.ui.preferences;

import java.util.Set;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class ProjectSelectionDialog extends SelectionStatusDialog {

	// the visual selection widget group
	private TableViewer fTableViewer;
	private Set fProjectsWithSpecifics;

	// sizing constants
	private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;
	private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

	private final static String DIALOG_SETTINGS_SHOW_ALL = "ProjectSelectionDialog.show_all"; //$NON-NLS-1$

	private ViewerFilter fFilter;

	public ProjectSelectionDialog(Shell parentShell, Set projectsWithSpecifics) {
		super(parentShell);
		setHelpAvailable(false);
		setTitle(Messages.getString("ProjectSelectionDialog.Title")); //$NON-NLS-1$
		setMessage(Messages.getString("ProjectSelectionDialog.Message")); //$NON-NLS-1$
		fProjectsWithSpecifics = projectsWithSpecifics;

		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fFilter = new ViewerFilter() {

			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				return fProjectsWithSpecifics.contains(element);
			}
		};

	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// page group
		Composite composite = (Composite) super.createDialogArea(parent);

		Font font = parent.getFont();
		composite.setFont(font);

		createMessageArea(composite);

		fTableViewer = new TableViewer(composite, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				doSelectionChanged(((IStructuredSelection) event.getSelection()).toArray());
			}
		});
		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
		data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
		fTableViewer.getTable().setLayoutData(data);

		ReportProjectsProvider provider = new ReportProjectsProvider();
		fTableViewer.setLabelProvider(provider);
		fTableViewer.setContentProvider(provider);
		fTableViewer.getControl().setFont(font);

		Button checkbox = new Button(composite, SWT.CHECK);
		checkbox.setText(Messages.getString("ProjectSelectionDialog.Text.SpecialSettings")); //$NON-NLS-1$
		checkbox.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
		checkbox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateFilter(((Button) e.widget).getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				updateFilter(((Button) e.widget).getSelection());
			}
		});
		IDialogSettings dialogSettings = ReportPlugin.getDefault().getDialogSettings();
		boolean doFilter = !dialogSettings.getBoolean(DIALOG_SETTINGS_SHOW_ALL) && !fProjectsWithSpecifics.isEmpty();
		checkbox.setSelection(doFilter);
		updateFilter(doFilter);
		// IJavaModel input=
		// JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		fTableViewer.setInput(ReportPlugin.getWorkspace().getRoot());

		doSelectionChanged(new Object[0]);
		Dialog.applyDialogFont(composite);
		return composite;
	}

	protected void updateFilter(boolean selected) {
		if (selected) {
			fTableViewer.addFilter(fFilter);
		} else {
			fTableViewer.removeFilter(fFilter);
		}
		ReportPlugin.getDefault().getDialogSettings().put(DIALOG_SETTINGS_SHOW_ALL, !selected);
	}

	private void doSelectionChanged(Object[] objects) {
		if (objects.length != 1) {
			updateStatus(new StatusInfo(IStatus.ERROR, "")); //$NON-NLS-1$
			setSelectionResult(null);
		} else {
			updateStatus(new StatusInfo());
			setSelectionResult(objects);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#computeResult()
	 */
	@Override
	protected void computeResult() {
	}
}
