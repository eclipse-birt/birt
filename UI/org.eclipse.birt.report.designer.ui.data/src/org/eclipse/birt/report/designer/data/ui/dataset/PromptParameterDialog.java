/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousAttribute;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousOption;
import org.eclipse.birt.report.model.adapter.oda.IAmbiguousParameterNode;
import org.eclipse.birt.report.model.api.elements.structures.OdaDataSetParameter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

public class PromptParameterDialog extends BaseDialog {
	private Object input;
	private Map<IAmbiguousParameterNode, Boolean> selectedStatusMap;

	public PromptParameterDialog(String title) {
		super(title);
		selectedStatusMap = new HashMap<IAmbiguousParameterNode, Boolean>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 400;
		composite.setLayoutData(data);

		// createResultSetGroup( composite );
		createParameterGroup(composite);
		setOkButtonText(Messages.getString("PromptParameterDialog.okButton.text"));
		return composite;
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.IGNORE_LABEL, false);

		// create OK and Cancel buttons by default
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	private void createParameterGroup(Composite composite) {
		final Group group2 = new Group(composite, SWT.NONE);
		group2.setLayout(new GridLayout());
		group2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 8));// GridData.FILL_BOTH));

		group2.setText(Messages.getString("PromptParameterDialog.parameterGroup.title"));

		Label label1 = new Label(group2, SWT.NONE);
		label1.setText(Messages.getString("PromptParameterDialog.parameterGroup.label"));

		TreeViewer viewer2 = new TreeViewer(group2, SWT.FULL_SELECTION);

		Tree tableTree2 = viewer2.getTree();
		GridData gd = new GridData(GridData.FILL_BOTH);
		tableTree2.setLayoutData(gd);
		tableTree2.setHeaderVisible(true);
		tableTree2.setLinesVisible(true);

		TreeViewerColumn tvc21 = new TreeViewerColumn(viewer2, SWT.NONE);
		tvc21.getColumn().setText(Messages.getString("PromptParameterDialog.parameterGroup.nameColumn")); //$NON-NLS-1$
		tvc21.getColumn().setWidth(200);
		tvc21.setLabelProvider(new NameLabelProvider(selectedStatusMap));
		tvc21.setEditingSupport(new ParameterEditingSupport(viewer2, selectedStatusMap));

		TreeViewerColumn tvc22 = new TreeViewerColumn(viewer2, SWT.NONE);
		tvc22.getColumn().setText(Messages.getString("PromptParameterDialog.parameterGroup.previousValue")); //$NON-NLS-1$
		tvc22.getColumn().setWidth(200);
		tvc22.setLabelProvider(new PreviousValueLabelProvider());

		TreeViewerColumn tvc23 = new TreeViewerColumn(viewer2, SWT.NONE);
		tvc23.getColumn().setText(Messages.getString("PromptParameterDialog.parameterGroup.revisedValue")); //$NON-NLS-1$
		tvc23.getColumn().setWidth(200);
		tvc23.setLabelProvider(new RevisedValueLabelProvider());

		viewer2.setContentProvider(new ParameterContentProvider());
		viewer2.setInput(((IAmbiguousOption) input).getAmbiguousParameters());
		viewer2.expandAll();
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.BaseDialog#getResult()
	 */
	public Object getResult() {
		List<OdaDataSetParameter> selectedList = new ArrayList<OdaDataSetParameter>();
		List<IAmbiguousParameterNode> ambiguousParameters = ((IAmbiguousOption) input).getAmbiguousParameters();
		for (int i = 0; i < ambiguousParameters.size(); i++) {
			Object obj = selectedStatusMap.get(ambiguousParameters.get(i));
			if (obj != null && obj instanceof Boolean) {
				if (((Boolean) obj).booleanValue()) {
					selectedList.add((OdaDataSetParameter) ((IAmbiguousParameterNode) ambiguousParameters.get(i))
							.getOdaDataSetParameterHandle().getStructure());
				}
			}
		}
		return selectedList;
	}

	public void setInput(Object input) {
		this.input = input;
	}
}

class ResultSetContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return true;
	}

	public Object[] getElements(Object input) {
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}

class ParameterContentProvider implements ITreeContentProvider {

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			return ((List) parentElement).toArray();
		}

		if (parentElement instanceof IAmbiguousParameterNode) {
			return ((IAmbiguousParameterNode) parentElement).getAmbiguousAttributes().toArray();
		}
		return null;
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof List) {
			return true;
		}
		if (element instanceof IAmbiguousParameterNode) {
			return true;
		}
		if (element instanceof IAmbiguousAttribute) {
			return false;
		}
		return true;
	}

	public Object[] getElements(Object input) {
		if (input instanceof List) {
			return ((List) input).toArray();
		}
		return new Object[0];
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}

class NameLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {
	Map selectedStatusMap;

	public NameLabelProvider(Map isSelectedMap) {
		this.selectedStatusMap = isSelectedMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) {
		String symbolicName = IReportGraphicConstants.ICON_CHECKED;

		if (element instanceof IAmbiguousParameterNode) {
			if (selectedStatusMap.containsKey(element)) {
				Object obj = selectedStatusMap.get(element);
				if (obj != null && obj instanceof Boolean) {
					if (((Boolean) obj).booleanValue())
						symbolicName = IReportGraphicConstants.ICON_CHECKED;
					else
						symbolicName = IReportGraphicConstants.ICON_UNCHECKED;
				}
			} else {
				selectedStatusMap.put(element, Boolean.TRUE);
				symbolicName = IReportGraphicConstants.ICON_CHECKED;
			}
			return ReportPlatformUIImages.getImage(symbolicName);
		}
		return super.getImage(element);
	}

	public String getText(Object element) {
		String text = getStyledText(element).toString();
		return text;
	}

	public StyledString getStyledText(Object element) {
		String value = null;
		if (element instanceof IAmbiguousParameterNode) {
			value = ((IAmbiguousParameterNode) element).getOdaDataSetParameterHandle().getName();
		} else if (element instanceof IAmbiguousAttribute) {
			value = ((IAmbiguousAttribute) element).getAttributeName();
		}
		if (value == null)
			value = ""; //$NON-NLS-1$
		StyledString styledString = new StyledString();
		styledString.append(value);
		return styledString;
	}
}

class PreviousValueLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	public String getText(Object element) {
		String text = getStyledText(element).toString();
		return text;
	}

	public StyledString getStyledText(Object element) {
		String value = null;
		if (element instanceof IAmbiguousAttribute) {
			if (((IAmbiguousAttribute) element).getPreviousValue() == null)
				value = "null";//$NON-NLS-1$
			else
				value = ((IAmbiguousAttribute) element).getPreviousValue().toString();
		}
		if (value == null)
			value = ""; //$NON-NLS-1$
		StyledString styledString = new StyledString();
		styledString.append(value);
		return styledString;
	}

}

class RevisedValueLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	public String getText(Object element) {
		String text = getStyledText(element).toString();
		return text;
	}

	public StyledString getStyledText(Object element) {
		String value = null;
		if (element instanceof IAmbiguousAttribute) {
			if (((IAmbiguousAttribute) element).getRevisedValue() == null)
				value = "null";//$NON-NLS-1$
			else
				value = ((IAmbiguousAttribute) element).getRevisedValue().toString();
		}
		if (value == null)
			value = ""; //$NON-NLS-1$
		StyledString styledString = new StyledString();
		styledString.append(value);
		return styledString;
	}

}

class ParameterEditingSupport extends EditingSupport {
	private CellEditor editor;
	private Map selectedStatusMap;
	private ColumnViewer viewer;

	public ParameterEditingSupport(ColumnViewer viewer, Map isSelectedMap) {
		super(viewer);
		this.viewer = viewer;
		editor = new CheckboxCellEditor(null, SWT.CHECK | SWT.READ_ONLY);
		this.selectedStatusMap = isSelectedMap;
	}

	protected boolean canEdit(Object arg0) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object arg0) {
		return editor;
	}

	@Override
	protected Object getValue(Object arg0) {
		if (arg0 instanceof IAmbiguousParameterNode) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	protected void setValue(Object arg0, Object arg1) {
		if (arg0 instanceof IAmbiguousParameterNode) {
			Object obj = selectedStatusMap.get(arg0);
			if (obj != null && obj instanceof Boolean) {
				if (!((Boolean) obj).booleanValue())
					selectedStatusMap.put(arg0, Boolean.TRUE);
				else
					selectedStatusMap.put(arg0, Boolean.FALSE);
			} else {
				selectedStatusMap.put(arg0, Boolean.FALSE);
			}
		}
		viewer.refresh();
	}
}
