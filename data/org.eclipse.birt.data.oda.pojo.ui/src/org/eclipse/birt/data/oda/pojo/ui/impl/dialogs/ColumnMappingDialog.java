/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.util.Arrays;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.dialogs.MethodParameterDialog.IModifyValidator;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ColumnDefinition;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.OdaType;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ColumnMappingPageHelper;
import org.eclipse.birt.data.oda.pojo.ui.util.Constants;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;

/**
 * 
 */

public class ColumnMappingDialog extends StatusDialog {

	private ColumnMappingPageHelper helper;
	private ColumnDefinition input;
	private Text txtName, txtMappingPath;

	private ComboViewer comboTypes;
	private ColumnDefinition output;
	private TableViewer checkBoxViewer;
	private boolean containsParam, isEditMode;
	private Button editBtn;
	private String name;

	/**
	 * @param shell
	 */
	public ColumnMappingDialog(Shell shell, ColumnDefinition cd, ColumnMappingPageHelper helper, boolean isEditMode,
			boolean containsParameter) {
		super(shell);
		this.input = cd;
		this.helper = helper;
		this.containsParam = containsParameter;
		this.isEditMode = isEditMode;
	}

	protected void setColumnDefinition(ColumnDefinition column) {
		this.input = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		layout.horizontalSpacing = 10;
		composite.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 300;
		composite.setLayoutData(gd);

		Composite topArea = new Composite(composite, SWT.NONE);
		GridLayout topLayout = new GridLayout(2, false);
		topLayout.marginWidth = 10;
		topLayout.horizontalSpacing = 20;
		topArea.setLayout(topLayout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		topArea.setLayoutData(layoutData);

		Label columnLabel = new Label(topArea, SWT.NONE);
		columnLabel.setText(Messages.getString("ColumnMappingDialog.Label.ColumnName")); //$NON-NLS-1$
		txtName = new Text(topArea, SWT.BORDER);
		txtName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		txtName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				name = txtName.getText().trim();
				validateSyntax();
			}
		});

		Label typeLabel = new Label(topArea, SWT.NONE);
		typeLabel.setText(Messages.getString("ColumnMappingDialog.Label.Type")); //$NON-NLS-1$
		comboTypes = new ComboViewer(topArea, SWT.READ_ONLY);
		GridData comboData = new GridData(GridData.FILL_HORIZONTAL);
		comboData.widthHint = 200;
		comboTypes.getCombo().setLayoutData(comboData);

		comboTypes.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object arg0) {
				OdaType[] result = OdaType.values();
				Arrays.sort(result, new OdaType.OdaTypeComparator());
				return result;
			}

			public void dispose() {
			}

			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
		});
		comboTypes.setInput(""); //$NON-NLS-1$

		if (containsParam) {
			createParameterArea(composite);
		} else {
			Label methodLabel = new Label(topArea, SWT.NONE);
			methodLabel.setText(Messages.getString("ColumnMappingDialog.Label.ColumnMethodField")); //$NON-NLS-1$
			txtMappingPath = new Text(topArea, SWT.BORDER);
			txtMappingPath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			txtMappingPath.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					validateSyntax();
				}
			});
		}

		if (input != null && input.getName() != null) {
			this.name = isEditMode ? input.getName() : helper.getDistinctName(input.getName());
			txtName.setText(this.name);
			if (!containsParam) {
				txtMappingPath.setText(input.getMappingPathText());
			} else {
				IMappingSource item = input.getMappingPath()[input.getMappingPath().length - 1];
				if (item instanceof MethodSource) {
					checkBoxViewer.setInput((MethodSource) item);
				}
			}
			comboTypes.setSelection(new StructuredSelection(input.getType()));
		} else {
			comboTypes.setSelection(new StructuredSelection(OdaType.String));
		}

		getShell().setText(isEditMode ? Messages.getString("DataSet.EditColumnMapping") //$NON-NLS-1$
				: Messages.getString("DataSet.AddColumnMapping")); //$NON-NLS-1$

		HelpUtil.setSystemHelp(composite, HelpUtil.CONEXT_ID_DATASET_POJO_COLUMN_MAPPING_DIALOG);

		return composite;
	}

	private void createParameterArea(Composite composite) {
		Composite paramArea = new Composite(composite, SWT.NONE);
		GridLayout paramAreaLayout = new GridLayout(2, false);
		paramAreaLayout.marginWidth = 5;
		paramAreaLayout.marginHeight = 15;
		paramAreaLayout.horizontalSpacing = 15;
		paramArea.setLayout(paramAreaLayout);
		GridData layoutData = new GridData(GridData.FILL_BOTH);
		layoutData.horizontalSpan = 2;
		layoutData.heightHint = 200;
		paramArea.setLayoutData(layoutData);

		createTreeViewer(paramArea);

		createRightArea(paramArea);
	}

	private void createRightArea(Composite paramArea) {
		Composite right = new Composite(paramArea, SWT.NONE);
		right.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		right.setLayoutData(gd);

		Label tableLabel = new Label(right, SWT.NONE);
		tableLabel.setText(Messages.getString("ColumnMappingDialog.table.promptLabel")); //$NON-NLS-1$
		GridData lableData = new GridData(GridData.FILL_HORIZONTAL);
		tableLabel.setLayoutData(lableData);

		final Table table = new Table(right, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.SCROLL_LINE);

		GridData gridData = new GridData(GridData.FILL_BOTH);

		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn checkBoxColumn = new TableColumn(table, SWT.LEFT);
		checkBoxColumn.setResizable(true);
		checkBoxColumn.setText(Messages.getString("ColumnMappingDialog.table.head.mapped")); //$NON-NLS-1$
		checkBoxColumn.setWidth(70);

		TableColumn nameColumn = new TableColumn(table, SWT.LEFT);
		nameColumn.setResizable(true);
		nameColumn.setText(Messages.getString("ColumnMappingDialog.table.head.name")); //$NON-NLS-1$
		nameColumn.setWidth(80);

		TableColumn valueColumn = new TableColumn(table, SWT.LEFT);
		valueColumn.setResizable(true);
		valueColumn.setText(Messages.getString("ColumnMappingDialog.table.head.value")); //$NON-NLS-1$
		valueColumn.setWidth(80);

		TableColumn typeColumn = new TableColumn(table, SWT.LEFT);
		typeColumn.setResizable(true);
		typeColumn.setText(Messages.getString("ColumnMappingDialog.table.head.dataType")); //$NON-NLS-1$
		typeColumn.setWidth(80);

		checkBoxViewer = new TableViewer(table);
		checkBoxViewer.getTable().setLayoutData(gd);

		TableProvider provider = new TableProvider();
		checkBoxViewer.setContentProvider(provider);
		checkBoxViewer.setLabelProvider(provider);

		checkBoxViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				editBtn.setEnabled(checkBoxViewer.getTable().getSelectionCount() == 1);
			}
		});

		checkBoxViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				doEditPrameter();
			}
		});

		createButtonArea(right);
	}

	private void createButtonArea(Composite right) {
		Composite composite = new Composite(right, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		editBtn = new Button(composite, SWT.PUSH);
		editBtn.setText(Messages.getString("ColumnMappingDialog.button.edit")); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.END;
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = editBtn.computeSize(-1, -1).x - editBtn.getBorderWidth() + 20;
		editBtn.setLayoutData(gd);
		editBtn.setEnabled(false);
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				doEditPrameter();
			}
		});

	}

	private void createTreeViewer(Composite paramArea) {
		Composite left = new Composite(paramArea, SWT.NONE);
		left.setLayout(new GridLayout(1, false));

		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 150;

		left.setLayoutData(data);

		Label label = new Label(left, SWT.NONE);
		label.setText(Messages.getString("ColumnMappingDialog.Label.methodHierarchy")); //$NON-NLS-1$

		final TreeViewer treeViewer = new TreeViewer(left, SWT.BORDER | SWT.SINGLE);

		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		TreeProvider treeProvider = new TreeProvider();
		treeViewer.setLabelProvider(treeProvider);
		treeViewer.setContentProvider(treeProvider);
		if (this.input != null)
			treeViewer.setInput(this.input.getMappingPath());

		treeViewer.expandAll();
		TreeItem[] items = treeViewer.getTree().getItems();
		TreeItem item = null;
		while (items.length > 0) {
			item = items[0];
			items = item.getItems();
		}
		if (item != null)
			treeViewer.getTree().select(item);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				editBtn.setEnabled(false);
				if (treeViewer.getTree().getSelectionCount() == 1) {
					Object item = treeViewer.getTree().getSelection()[0].getData();
					if (item instanceof MethodSource) {
						checkBoxViewer.setInput((MethodSource) item);
						// updateCheckBoxStatus( );
						checkBoxViewer.refresh();
					}
				}
			}
		});
	}

	/*
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#buttonPressed(int)
	 */
	@Override
	protected void buttonPressed(int buttonId) {
		output = null;
		if (buttonId == IDialogConstants.OK_ID) {
			IMappingSource[] sources = null;
			try {
				if (this.containsParam) {
					sources = input.getMappingPath();
				} else
					sources = Utils.getMappingSource(txtMappingPath.getText().trim());
			} catch (OdaException e) {
				ExceptionHandler.showException(getShell(), e.getLocalizedMessage(), e.getLocalizedMessage(), e);
				txtMappingPath.selectAll();
				txtMappingPath.setFocus();
				return;
			}
			output = new ColumnDefinition(sources, txtName.getText().trim(), getSelectedType());
		}
		super.buttonPressed(buttonId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		validateSyntax();
	}

	private void doEditPrameter() {
		if (checkBoxViewer.getTable().getSelectionCount() == 1) {
			Object element = ((StructuredSelection) checkBoxViewer.getSelection()).getFirstElement();
			if (element instanceof IMethodParameter) {
				MethodParameterDialog dialog = new MethodParameterDialog((IMethodParameter) element);
				dialog.setValidator(new IModifyValidator() {

					public boolean validateInputValue(Object value, Object[] args) {
						if ((value instanceof VariableParameter) && args.length >= 2) {
							return helper.isValidParamName((VariableParameter) value, (String) args[0],
									(String) args[1]);
						}
						return true;
					}

				});
				if (dialog.open() == Window.OK) {
					((MethodSource) checkBoxViewer.getInput()).updateMethodParameter((IMethodParameter) element,
							dialog.updateMethodParameter());
					checkBoxViewer.refresh();
				}
			}
		}
	}

	private OdaType getSelectedType() {
		IStructuredSelection ss = (IStructuredSelection) comboTypes.getSelection();
		return (OdaType) ss.getFirstElement();
	}

	private void validateSyntax() {
		IStatus status = null;

		if (!(isEditMode && this.input.getName().equals(this.name)) && !helper.isUniqueColumnName(this.name)) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("ColumnMappingDialog.error.DuplicatedColumnName")); //$NON-NLS-1$
		} else if (Utils.isEmptyString(this.name)) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("ColumnMappingDialog.error.EmptyColumnName")); //$NON-NLS-1$
		} else if (!containsParam && txtMappingPath != null && Utils.isEmptyString(txtMappingPath.getText())) {
			status = getMiscStatus(IStatus.ERROR, Messages.getString("ColumnMappingDialog.error.EmptyMappingPath")); //$NON-NLS-1$
		} else {
			status = getOKStatus();
		}

		if (status != null)
			updateStatus(status);
	}

	private Status getMiscStatus(int severity, String message) {
		return new Status(severity, PlatformUI.PLUGIN_ID, severity, message, null);
	}

	private Status getOKStatus() {
		return getMiscStatus(IStatus.OK, ""); //$NON-NLS-1$
	}

	public ColumnDefinition getColumnDefinition() {
		return output;
	}

	/*
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#isResizable()
	 */
	@Override
	protected boolean isResizable() {
		return true;
	}

	private static class TableProvider implements ITableLabelProvider, IStructuredContentProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 0) {
				if (element instanceof VariableParameter)
					return Utils.getOKIcon();

				else if (element instanceof ConstantParameter)
					return Utils.getFailIcon();
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof IMethodParameter) {
				IMethodParameter param = (IMethodParameter) element;
				if (columnIndex == 0) {
					return null;
				}
				if (columnIndex == 1) {
					return (param instanceof VariableParameter) ? ((VariableParameter) param).getName()
							: Constants.DISPLAY_NONE_VALUE;
				} else if (columnIndex == 2) {
					return param.getStringValue() == null ? "" //$NON-NLS-1$
							: param.getStringValue().toString();
				} else {
					return param.getDataType();
				}
			}
			return null;
		}

		public void addListener(ILabelProviderListener listener) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof MethodSource) {
				return ((MethodSource) inputElement).getParameters();
			}
			return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

	private static class TreeProvider implements ILabelProvider, ITreeContentProvider {

		private IMappingSource[] sources;
		private int count;

		public Image getImage(Object element) {
			if (element instanceof MethodSource)
				return Utils.getMethodFlagImg();

			else if (element instanceof FieldSource)
				return Utils.getFieldFlagImg();

			return null;
		}

		public String getText(Object element) {
			if (element instanceof FieldSource)
				return ((FieldSource) element).getName();

			else if (element instanceof MethodSource)
				return ((MethodSource) element).getName() + "(" //$NON-NLS-1$
						+ getParametersLabel((MethodSource) element) + ")"; //$NON-NLS-1$

			return "";//$NON-NLS-1$
		}

		private String getParametersLabel(MethodSource method) {
			if (method == null)
				return ""; //$NON-NLS-1$

			StringBuffer sb = new StringBuffer();
			for (IMethodParameter param : method.getParameters()) {
				sb.append(", ").append(param.getDataType()); //$NON-NLS-1$
			}
			String result = sb.toString();
			if (result.length() > 0) {
				result = result.substring(2); // cut ", " at the beginning
			}
			return result;
		}

		public void addListener(ILabelProviderListener listener) {

		}

		public void dispose() {

		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {

		}

		public Object[] getChildren(Object parentElement) {
			if (count < sources.length) {
				return new Object[] { sources[count++] };
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return count < sources.length;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IMappingSource[]) {
				sources = (IMappingSource[]) inputElement;
				count = 0;
				return getChildren((IMappingSource[]) inputElement);
			}

			return new Object[0];
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

	}

}
