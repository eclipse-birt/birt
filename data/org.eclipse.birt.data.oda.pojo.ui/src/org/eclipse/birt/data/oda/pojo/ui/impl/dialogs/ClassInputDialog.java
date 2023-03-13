
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

import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.util.Utils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ClassInputDialog extends TrayDialog {
	private String input;
	private Text txtName;
	private TableViewer classTableViewer;
	private String[] classes;
	private String initValue;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.
	 * Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 600;
		gd.heightHint = 250;
		top.setLayoutData(gd);
		top.setLayout(new GridLayout(1, false));
		Label l = new Label(top, SWT.NONE);
		l.setText(Messages.getString("DataSet.ClassNameFilter")); //$NON-NLS-1$
		txtName = new Text(top, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				classTableViewer.refresh();
				if (classTableViewer.getElementAt(0) != null) {
					classTableViewer.setSelection(new StructuredSelection(classTableViewer.getElementAt(0)));
				}
			}
		});
		l = new Label(top, SWT.NONE);
		l.setText(Messages.getString("DataSet.MatchingItems")); //$NON-NLS-1$

		classTableViewer = new TableViewer(top, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		classTableViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		classTableViewer.getTable().setHeaderVisible(false);
		classTableViewer.getTable().setLinesVisible(false);
		classTableViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void dispose() {

			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}

			@Override
			public Object[] getElements(Object arg0) {
				return classes;
			}

		});
		classTableViewer.setLabelProvider(new LabelProvider() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
			 */
			@Override
			public Image getImage(Object element) {
				return org.eclipse.birt.data.oda.pojo.ui.util.Utils.getClassFlagImg();
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				IStructuredSelection ss = (IStructuredSelection) classTableViewer.getSelection();
				Object selected = ss.getFirstElement();

				// the selected items labels are different from those of unselected items
				if (element != selected) {
					return getClassName((String) element);
				} else {
					return getClassName((String) element) + " - " + getPackagePath((String) element); //$NON-NLS-1$
				}
			}

		});
		classTableViewer.setSorter(new ViewerSorter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.
			 * Viewer, java.lang.Object, java.lang.Object)
			 */
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				String c1 = getClassName((String) e1);
				String c2 = getClassName((String) e2);
				return c1.compareTo(c2);
			}

		});
		classTableViewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer arg0, Object arg1, Object arg2) {
				String filter = txtName.getText().trim();
				if (filter.equals("")) //$NON-NLS-1$
				{
					return true;
				} else {
					return arg2.toString().toUpperCase().matches(Utils.toRegexPattern(filter).toUpperCase());
				}
			}

		});
		classTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection ss = (IStructuredSelection) event.getSelection();
				if (ss.size() == 1) {
					if (getButton(IDialogConstants.OK_ID) != null) {
						getButton(IDialogConstants.OK_ID).setEnabled(true);
					}
				} else if (getButton(IDialogConstants.OK_ID) != null) {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				}
				classTableViewer.refresh(); // the selected items labels are different from those of unselected items
			}

		});
		classTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection ss = (IStructuredSelection) classTableViewer.getSelection();
				if (ss.getFirstElement() != null) {
					buttonPressed(IDialogConstants.OK_ID);
				}
			}
		});
		classTableViewer.setInput(""); //$NON-NLS-1$
		if (classTableViewer.getElementAt(0) != null) {
			classTableViewer.setSelection(new StructuredSelection(classTableViewer.getElementAt(0)));
		}
		getShell().setText(Messages.getString("DataSet.ClassInputDialogTitle")); //$NON-NLS-1$
		setInitValue();
		HelpUtil.setSystemHelp(top, HelpUtil.CONEXT_ID_DATASET_POJO_CLASS_INPUT_DIALOG);
		return top;
	}

	/**
	 * @param shell
	 */
	public ClassInputDialog(Shell shell, String[] classNames, String initValue) {
		super(shell);
		this.classes = classNames;
		this.initValue = initValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		input = null;
		if (buttonId == IDialogConstants.OK_ID) {
			IStructuredSelection ss = (IStructuredSelection) classTableViewer.getSelection();
			String s = (String) ss.getFirstElement();
			if (s != null) {
				input = s;
			}
		}
		super.buttonPressed(buttonId);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		IStructuredSelection ss = (IStructuredSelection) classTableViewer.getSelection();
		if (ss.getFirstElement() == null) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	public String getInput() {
		return input;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	private void setInitValue() {
		txtName.setText(initValue);
		txtName.selectAll();
		classTableViewer.refresh();
		if (classTableViewer.getElementAt(0) != null) {
			classTableViewer.setSelection(new StructuredSelection(classTableViewer.getElementAt(0)));
		}
	}

	private static String getClassName(String fullName) {
		assert fullName != null;
		int index = fullName.lastIndexOf('.');
		if (index < 0) {
			return fullName;
		} else {
			return fullName.substring(index + 1);
		}
	}

	private static String getPackagePath(String fullName) {
		assert fullName != null;
		int index = fullName.lastIndexOf('.');
		if (index < 0) {
			return ""; //$NON-NLS-1$
		} else {
			return fullName.substring(0, index);
		}
	}
}
