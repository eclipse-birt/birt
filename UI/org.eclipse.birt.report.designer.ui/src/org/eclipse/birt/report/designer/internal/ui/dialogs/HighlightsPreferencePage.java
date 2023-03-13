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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HighlightRuleBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.HighlightHandleProvider;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Preference page for highlights style.
 */

public class HighlightsPreferencePage extends BaseStylePreferencePage {

	class HighlightLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return provider.getColumnText(element, columnIndex);
		}

	}

	class HighlightContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			return provider.getElements(inputElement);
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

	}

	private TableViewer fTableViewer;

	private Button fAddButton;

	private Button fEditButton;

	private Button fDeleteButton;

	private Button fMoveUpButton;

	private Button fMoveDownButton;

	private HighlightHandleProvider provider = new HighlightHandleProvider();

	private Object model;

	private Button fDuplicateButton;

	/**
	 * The constructor.
	 *
	 * @param model the model of preference page.
	 */
	public HighlightsPreferencePage(Object model) {
		super(model);
		setTitle(Messages.getString("HighlightsPreferencePage.displayname.Title")); //$NON-NLS-1$
		setDescription(Messages.getString("HighlightsPreferencePage.text.Description")); //$NON-NLS-1$

		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createContents
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		super.createFieldEditors();

		UIUtil.bindHelp(parent, IHelpContextIds.STYLE_BUILDER_HIGHTLIGHTS_ID);

		final Composite innerParent = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		innerParent.setLayout(layout);
		innerParent.setLayoutData(new GridData(GridData.FILL_BOTH));

		final Table table = new Table(innerParent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);

		GridData data = new GridData(GridData.FILL_BOTH);
		table.setLayoutData(data);

		table.setHeaderVisible(true);
		table.setLinesVisible(false);

		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);

		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(Messages.getString("HighlightsPreferencePage.displayname.Preview")); //$NON-NLS-1$

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(Messages.getString("HighlightsPreferencePage.displayname.Condition")); //$NON-NLS-1$

		fTableViewer = new TableViewer(table);
		fTableViewer.setLabelProvider(new HighlightLabelProvider());
		fTableViewer.setContentProvider(new HighlightContentProvider());

		fTableViewer.setSorter(null);

		fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}
		});

		fTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				edit();
			}
		});

		table.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				handleTableKeyPressEvent(e);
			}
		});

		innerParent.addControlListener(new ControlAdapter() {

			// Resize the columns by proportion when the parent control is
			// resized.
			@Override
			public void controlResized(ControlEvent e) {
				Rectangle area = innerParent.getClientArea();
				Point preferredSize = table.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * table.getBorderWidth();
				if (preferredSize.y > area.height) {
					Point vBarSize = table.getVerticalBar().getSize();
					width -= vBarSize.x;
				}
				Point oldSize = table.getSize();
				if (oldSize.x > width) {
					column1.setWidth(2 * width / 5);
					column2.setWidth(width - column1.getWidth() - 50);
					// table.setSize( width, area.height );
				} else {
					// table.setSize( width, area.height );
					column1.setWidth(2 * width / 5);
					column2.setWidth(width - column1.getWidth() - 50);
				}
			}
		});

		Composite buttons = new Composite(innerParent, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		layout = new GridLayout();
		layout.numColumns = 6;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		fAddButton = new Button(buttons, SWT.PUSH);
		fAddButton.setText(Messages.getString("HighlightsPreferencePage.text.Add")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(fAddButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fAddButton.setLayoutData(data);
		fAddButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				add();
			}
		});

		fEditButton = new Button(buttons, SWT.PUSH);
		fEditButton.setText(Messages.getString("HighlightsPreferencePage.text.Edit")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(fEditButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fEditButton.setLayoutData(data);
		fEditButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				edit();
			}
		});

		fDeleteButton = new Button(buttons, SWT.PUSH);
		fDeleteButton.setText(Messages.getString("HighlightsPreferencePage.text.Delete")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_END);
		data.widthHint = Math.max(fDeleteButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fDeleteButton.setLayoutData(data);
		fDeleteButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				delete();
			}
		});

		fMoveUpButton = new Button(buttons, SWT.PUSH);
		fMoveUpButton.setText(Messages.getString("FormPage.Button.Up")); //$NON-NLS-1$
		fMoveUpButton.setToolTipText(Messages.getString("HighlightsPreferencePage.toolTipText.Up")); //$NON-NLS-1$

		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = Math.max(fMoveUpButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fMoveUpButton.setLayoutData(data);
		fMoveUpButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				moveUp();
			}
		});

		fMoveDownButton = new Button(buttons, SWT.PUSH);
		fMoveDownButton.setText(Messages.getString("FormPage.Button.Down")); //$NON-NLS-1$
		fMoveDownButton.setToolTipText(Messages.getString("HighlightsPreferencePage.toolTipText.Down")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = Math.max(fMoveDownButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fMoveDownButton.setLayoutData(data);
		fMoveDownButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				moveDown();
			}
		});

		fDuplicateButton = new Button(buttons, SWT.PUSH);
		fDuplicateButton.setText(Messages.getString("HighlightsPreferencePage.text.Duplicate")); //$NON-NLS-1$
		fDuplicateButton.setToolTipText(Messages.getString("HighlightsPreferencePage.toolTipText.Duplicate")); //$NON-NLS-1$
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = Math.max(fDuplicateButton.computeSize(-1, -1).x, 60);
		// data.heightHint = 24;
		fDuplicateButton.setLayoutData(data);
		fDuplicateButton.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event e) {
				duplicate();
			}
		});

		fTableViewer.setInput(model);

		refreshTableItemView();

		updateButtons();

		Dialog.applyDialogFont(parent);

		return innerParent;
	}

	protected void duplicate() {
		int index = fTableViewer.getTable().getSelectionIndex();
		PropertyHandle phandle = ((StyleHandle) model).getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		HighlightRule rule = (HighlightRule) phandle.getListValue().get(index);
		try {
			HighlightRule newRule = (HighlightRule) rule.copy();
			phandle.addItem(newRule);

			fTableViewer.add(newRule.getHandle(phandle, phandle.getItems().size() - 1));
			int itemCount = fTableViewer.getTable().getItemCount();
			fTableViewer.getTable().deselectAll();
			fTableViewer.getTable().select(itemCount - 1);
			fTableViewer.getTable().setFocus();

			updateButtons();
			refreshTableItemView();
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	private void refreshTableItemView() {
		for (int i = 0; i < fTableViewer.getTable().getItemCount(); i++) {
			TableItem ti = fTableViewer.getTable().getItem(i);

			HighlightRuleHandle handle = (HighlightRuleHandle) ti.getData();

			if (handle.getColor().isSet()) {
				ti.setForeground(0, ColorManager.getColor(handle.getColor().getRGB()));
			}
			ti.setText(0, provider.getColumnText(handle, 0));
			ti.setText(1, provider.getColumnText(handle, 1));
		}

		fTableViewer.getTable().setFocus();
	}

	private void updateButtons() {
		fEditButton.setEnabled(fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount());
		fDeleteButton.setEnabled(fEditButton.getEnabled());
		fDuplicateButton.setEnabled(fEditButton.getEnabled());

		fMoveUpButton.setEnabled(fTableViewer.getTable().getSelectionIndex() > 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount());
		fMoveDownButton.setEnabled(fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount() - 1);
	}

	private void add() {
		HighlightRuleBuilder builder = new HighlightRuleBuilder(getShell(),
				Messages.getString("HighlightsPage.Dialog.NewHighlight"), //$NON-NLS-1$
				provider);

		builder.updateHandle(null, fTableViewer.getTable().getItemCount());
		builder.setDesignHandle((DesignElementHandle) model);
		if (model instanceof ReportItemHandle) {
			builder.setReportElement((ReportItemHandle) model);
		} else if (model instanceof GroupHandle) {
			builder.setReportElement((ReportItemHandle) ((GroupHandle) model).getContainer());
		}

		if (builder.open() == Window.OK) {
			fTableViewer.add(builder.getHandle());

			int itemCount = fTableViewer.getTable().getItemCount();

			fTableViewer.getTable().deselectAll();

			fTableViewer.getTable().select(itemCount - 1);

			fTableViewer.getTable().setFocus();

			updateButtons();

			refreshTableItemView();

			getBuilder().refreshPagesStatus();
		}
	}

	private void edit() {
		if (fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount()) {
			HighlightRuleBuilder builder = new HighlightRuleBuilder(getShell(),
					Messages.getString("HighlightsPage.Dialog.EditHighlight"), //$NON-NLS-1$
					provider);

			HighlightRuleHandle handle = (HighlightRuleHandle) fTableViewer.getTable()
					.getItem(fTableViewer.getTable().getSelectionIndex()).getData();

			builder.updateHandle(handle, fTableViewer.getTable().getItemCount());

			builder.setDesignHandle((DesignElementHandle) model);
			if (model instanceof ReportItemHandle) {
				builder.setReportElement((ReportItemHandle) model);
			} else if (model instanceof GroupHandle) {
				builder.setReportElement((ReportItemHandle) ((GroupHandle) model).getContainer());
			}

			if (builder.open() == Window.OK) {
				updateButtons();

				refreshTableItemView();
			}
		}

	}

	private void delete() {
		if (fTableViewer.getTable().getSelectionIndex() >= 0
				&& fTableViewer.getTable().getSelectionIndex() < fTableViewer.getTable().getItemCount()) {
			int idx = fTableViewer.getTable().getSelectionIndex();

			try {
				provider.doDeleteItem(idx);

				fTableViewer.getTable().remove(idx);
				fTableViewer.refresh();

				if (idx >= fTableViewer.getTable().getItemCount()) {
					idx--;
				}

				fTableViewer.getTable().select(idx);

				refreshTableItemView();
			} catch (Exception e) {
				WidgetUtil.processError(getShell(), e);
			}

			updateButtons();

			getBuilder().refreshPagesStatus();
		}
	}

	private void moveUp() {
		int index = fTableViewer.getTable().getSelectionIndex();

		try {
			provider.doSwapItem(index, -1);

			Object handle = fTableViewer.getTable().getItem(index).getData();

			fTableViewer.remove(handle);
			fTableViewer.insert(handle, index - 1);
			fTableViewer.refresh();

			fTableViewer.getTable().select(index - 1);

			refreshTableItemView();
		} catch (Exception e) {
			WidgetUtil.processError(getShell(), e);
		}

		updateButtons();
	}

	private void moveDown() {
		int index = fTableViewer.getTable().getSelectionIndex();

		try {
			provider.doSwapItem(index, 1);

			Object handle = fTableViewer.getTable().getItem(index).getData();

			fTableViewer.remove(handle);
			fTableViewer.insert(handle, index + 1);
			fTableViewer.refresh();

			fTableViewer.getTable().select(index + 1);

			refreshTableItemView();

		} catch (Exception e) {
			WidgetUtil.processError(getShell(), e);
		}

		updateButtons();
	}

	protected void handleTableKeyPressEvent(KeyEvent e) {
		if (e.keyCode == SWT.DEL) {
			delete();
		}
	}

	@Override
	protected String[] getPreferenceNames() {
		return new String[0];
	}

	@Override
	public boolean hasLocaleProperties() {
		PropertyHandle phandle = ((StyleHandle) model).getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
		if (phandle.getListValue() != null && phandle.getListValue().size() > 0) {
			return true;
		}
		return false;
	}
}
