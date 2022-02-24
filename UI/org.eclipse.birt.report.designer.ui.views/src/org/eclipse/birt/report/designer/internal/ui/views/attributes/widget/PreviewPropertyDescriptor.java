/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.dialogs.PreviewLabel;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PreviewPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class PreviewPropertyDescriptor extends PropertyDescriptor implements IFastConsumerProcessor {

	public PreviewPropertyDescriptor(boolean formStyle) {
		setFormStyle(true);
	}

	protected TableViewer fTableViewer;

	protected Button fAddButton;

	protected Button fDeleteButton;

	protected Button fMoveUpButton;

	protected Button fMoveDownButton;

	protected PreviewLabel previewLabel;

	public Control createControl(Composite parent) {

		content = new Composite(parent, SWT.NONE);
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin(2, false);
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		layout.horizontalSpacing = 10;
		content.setLayout(layout);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite buttons = new Composite(content, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		// data.heightHint = QUICK_BUTTON_HEIGHT;
		buttons.setLayoutData(data);
		layout = new GridLayout();
		layout.numColumns = 6;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		Label lb = FormWidgetFactory.getInstance().createLabel(buttons, isFormStyle());
		lb.setText(provider.getText(-1)); // $NON-NLS-1$
		lb.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));

		fAddButton = FormWidgetFactory.getInstance().createButton(buttons, SWT.PUSH, isFormStyle());
		fAddButton.setText(provider.getText(1)); // $NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(60, fAddButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.heightHint = QUICK_BUTTON_HEIGHT - 2;
		fAddButton.setLayoutData(data);
		fAddButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleAddSelectedEvent();
			}
		});

		fDeleteButton = FormWidgetFactory.getInstance().createButton(buttons, SWT.PUSH, isFormStyle());
		fDeleteButton.setText(provider.getText(2)); // $NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);

		data.widthHint = Math.max(60, fDeleteButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.heightHint = QUICK_BUTTON_HEIGHT - 2;
		fDeleteButton.setLayoutData(data);
		fDeleteButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDeleteSelectedEvent();
			}
		});

		fMoveUpButton = FormWidgetFactory.getInstance().createButton(buttons, SWT.PUSH, isFormStyle());
		fMoveUpButton.setText(provider.getText(3)); // $NON-NLS-1$
		fMoveUpButton.setToolTipText(provider.getText(4)); // $NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(60, fMoveUpButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.heightHint = QUICK_BUTTON_HEIGHT - 2;
		fMoveUpButton.setLayoutData(data);
		fMoveUpButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleMoveUpSelectedEvent();
			}
		});

		fMoveDownButton = FormWidgetFactory.getInstance().createButton(buttons, SWT.PUSH, isFormStyle());
		fMoveDownButton.setText(provider.getText(5)); // $NON-NLS-1$
		fMoveDownButton.setToolTipText(provider.getText(6)); // $NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(60, fMoveDownButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.heightHint = QUICK_BUTTON_HEIGHT - 2;
		fMoveDownButton.setLayoutData(data);
		fMoveDownButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleMoveDownSelectedEvent();
			}
		});

		fDuplicateButton = FormWidgetFactory.getInstance().createButton(buttons, SWT.PUSH, isFormStyle());
		fDuplicateButton.setText(provider.getText(10)); // $NON-NLS-1$
		fDuplicateButton.setToolTipText(provider.getText(11)); // $NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(60, fDuplicateButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
		// data.heightHint = QUICK_BUTTON_HEIGHT - 2;
		fDuplicateButton.setLayoutData(data);
		fDuplicateButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDuplicateButtonSelectedEvent();
			}
		});

		FormWidgetFactory.getInstance().createLabel(content, isFormStyle()).setText(provider.getText(7)); // $NON-NLS-1$

		int style = SWT.FULL_SELECTION;
		if (!isFormStyle())
			style |= SWT.BORDER;
		Table table = FormWidgetFactory.getInstance().createTable(content, style);
		TableColumn column = new TableColumn(table, SWT.LEFT);
		column.setText(provider.getText(8));
		column.setWidth(400);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(GridData.FILL_BOTH));

		fTableViewer = new TableViewer(table);
		fTableViewer.setLabelProvider(provider.getLabelProvider());
		fTableViewer.setContentProvider(provider.getContentProvider(this));
		fTableViewer.setSorter(null);

		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
				refreshTableItemView();
			}
		});

		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				handleEditEvent();
			}
		});

		fTableViewer.getTable().addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					handleDeleteSelectedEvent();
				}
			}

		});

		style = SWT.BORDER;
		if (isFormStyle())
			style = SWT.NONE;
		previewLabel = new PreviewLabel(content, style);
		previewLabel.setText(provider.getText(9)); // $NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 300;
		previewLabel.setLayoutData(gd);
		FormWidgetFactory.getInstance().adapt(previewLabel);
		updateButtons();

		return content;

	}

	protected void handleDuplicateButtonSelectedEvent() {
		if (fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount()) {
			int idx = fTableViewer.getTable().getSelectionIndex();
			if (provider.duplicate(idx)) {
				int itemCount = fTableViewer.getTable().getItemCount();
				fTableViewer.getTable().deselectAll();
				fTableViewer.getTable().select(itemCount - 1);
				fTableViewer.getTable().setFocus();
			}
			;
			updateButtons();
			refreshTableItemView();
		}
	}

	public Control getControl() {
		return content;
	}

	protected void handleEditEvent() {

		if (fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount()) {
			int idx = fTableViewer.getTable().getSelectionIndex();

			fTableViewer.getTable().setFocus();
			if (provider.edit(fTableViewer.getTable().getItem(fTableViewer.getTable().getSelectionIndex()).getData(),
					fTableViewer.getTable().getItemCount())) {
				fTableViewer.getTable().select(idx);
				fTableViewer.getTable().setFocus();
				updateButtons();
				refreshTableItemView();
			}
		}
	}

	protected void handleMoveDownSelectedEvent() {
		int index = fTableViewer.getTable().getSelectionIndex();
		if (provider.moveDown(index)) {
			fTableViewer.getTable().select(index + 1);
			fTableViewer.getTable().setFocus();
		}
		updateButtons();
		refreshTableItemView();
	}

	protected void handleMoveUpSelectedEvent() {

		int index = fTableViewer.getTable().getSelectionIndex();
		if (provider.moveUp(index)) {
			fTableViewer.getTable().select(index - 1);
			fTableViewer.getTable().setFocus();
		}

		updateButtons();
		refreshTableItemView();
	}

	protected void handleDeleteSelectedEvent() {
		if (fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount()) {
			int idx = fTableViewer.getTable().getSelectionIndex();

			if (provider.delete(idx)) {
				if (idx >= fTableViewer.getTable().getItemCount()) {
					idx--;
				}
				fTableViewer.getTable().select(idx);
				fTableViewer.getTable().setFocus();
			}
		}
		updateButtons();
		refreshTableItemView();
	}

	protected void handleAddSelectedEvent() {
		int itemCount = fTableViewer.getTable().getItemCount();
		if (provider.add(itemCount)) {
			itemCount = fTableViewer.getTable().getItemCount();
			fTableViewer.getTable().deselectAll();
			fTableViewer.getTable().select(itemCount - 1);
			fTableViewer.getTable().setFocus();
		}
		;
		updateButtons();
		refreshTableItemView();
	}

	protected void updateButtons() {
		if (fTableViewer.getTable().isDisposed())
			return;

		fDeleteButton.setEnabled(fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount());

		fMoveUpButton.setEnabled(fTableViewer.getTable().getSelectionIndex() > 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount());

		fMoveDownButton.setEnabled(fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount() - 1);

		fDuplicateButton.setEnabled(fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount());
	}

	protected void refreshTableItemView() {
		if (fTableViewer.getTable().isDisposed())
			return;

		for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
			TableItem ti = fTableViewer.getTable().getItem(i);
			ti.setText(0, provider.getColumnText(ti.getData(), 1));
		}

		if (fTableViewer.getTable().getSelectionIndex() >= 0) {
			updatePreview(fTableViewer.getTable().getItem(fTableViewer.getTable().getSelectionIndex()).getData());
		} else {
			updatePreview(null);
		}
	}

	protected void updatePreview(Object handle) {

	}

	protected void enableUI(boolean enabled) {
		if (fTableViewer != null) {
			fAddButton.setEnabled(enabled);
			fDeleteButton.setEnabled(enabled);
			fMoveUpButton.setEnabled(enabled);
			fMoveDownButton.setEnabled(enabled);
			fDeleteButton.setEnabled(enabled);
			fTableViewer.getTable().setEnabled(enabled);

			if (enabled) {
				updateButtons();
			}
		}
	}

	public void load() {
		if (DEUtil.getInputSize(input) != 1) {
			enableUI(false);
			return;
		}
		enableUI(true);
		fTableViewer.setInput(DEUtil.getInputElements(input));
		refreshTableItemView();
		;
	}

	public void save(Object obj) throws SemanticException {
		// TODO Auto-generated method stub

	}

	public void setInput(Object input) {
		this.input = input;
		getDescriptorProvider().setInput(input);
	}

	protected PreviewPropertyDescriptorProvider provider;

	private Composite content;

	private Button fDuplicateButton;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof PreviewPropertyDescriptorProvider)
			this.provider = (PreviewPropertyDescriptorProvider) provider;
	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (fTableViewer != null) {
			if (fTableViewer.getContentProvider() == null) {
				return;
			}
			fTableViewer.setInput(input);
			refreshTableItemView();
		}
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(content, isHidden);
	}

	public void setVisible(boolean isVisible) {
		content.setVisible(isVisible);
	}

	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {

	}

	public void clear() {

	}

	public boolean isOverdued() {
		return fTableViewer == null || fTableViewer.getContentProvider() == null || fTableViewer.getControl() == null
				|| fTableViewer.getControl().isDisposed();
	}

	public void postElementEvent() {
		if (fTableViewer == null || fTableViewer.getContentProvider() == null) {
			return;
		}
		if (fTableViewer != null) {
			fTableViewer.setInput(input);
			refreshTableItemView();
		}
	}

	public Object getAdapter(Class adapter) {
		return null;
	}
}
