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

import java.util.Arrays;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 */
public class ElementNamesConfigurationBlock extends OptionsConfigurationBlock {

	private final Key PREF_DEFAULT_NAME = getReportKey(ReportPlugin.DEFAULT_NAME_PREFERENCE);
	private final Key PREF_CUSTOM_NAME = getReportKey(ReportPlugin.CUSTOM_NAME_PREFERENCE);
	private final Key PREF_DESCRIPTION = getReportKey(ReportPlugin.DESCRIPTION_PREFERENCE);
	private PixelConverter fPixelConverter;

	public ElementNamesConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = { PREF_DEFAULT_NAME, PREF_CUSTOM_NAME, PREF_DESCRIPTION };
		return keys;
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		fPixelConverter = new PixelConverter(parent);
		setShell(parent.getShell());

		Composite mainComp = new Composite(parent, SWT.NONE);
		mainComp.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout(layout);

		Composite othersComposite = createBuildPathTabContent(mainComp);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels(20);
		othersComposite.setLayoutData(gridData);

		validateSettings(null, null, null);

		return mainComp;
	}

	private Composite createBuildPathTabContent(Composite parent) {
		Composite pageContent = new Composite(parent, SWT.NONE);

		GridData data = new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		pageContent.setLayout(layout);

		createTable(pageContent);
		createTableViewer();

		tableViewer.setContentProvider(new ContentProvider());
		tableViewer.setLabelProvider(new ElementNameLabelProvider());

		// The input for the table viewer is the instance of ItemContentList
		itemContentList = new ItemContentList(this, getKeys());
		tableViewer.setInput(itemContentList);

		sortTable(tableViewer.getTable().getColumn(0), true);

		return pageContent;
	}

	// The default width of the column
	private static final int columnWidth = Integer
			.parseInt(Messages.getString("designer.preview.preference.elementname.columnwidth")); //$NON-NLS-1$

	// The names of column
	private static final String elementNames[] = {
			Messages.getString("designer.preview.preference.elementname.defaultname"), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.elementname.customname"), //$NON-NLS-1$
			Messages.getString("designer.preview.preference.elementname.description") //$NON-NLS-1$
	};
	private Table table;
	private TableViewer tableViewer;
	private ItemContentList itemContentList;

	private void createTable(Composite parent) {

		int tableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION;
		table = new Table(parent, tableStyle);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.grabExcessHorizontalSpace = true;
		table.setLayoutData(data);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableColumn column;
		int i;
		for (i = 0; i < elementNames.length; i++) {
			column = new TableColumn(table, SWT.NONE, i);
			column.setText(elementNames[i]);
			column.setWidth(columnWidth);

			column.addSelectionListener(new SelectionListener() {
				boolean asc = true;

				@Override
				public void widgetSelected(final SelectionEvent e) {
					TableColumn selectedColumn = (TableColumn) e.widget;
					if (table.getSortColumn() == selectedColumn) {
						asc = !asc;
					}
					sortTable(selectedColumn, asc);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
		}

	}

	/**
	 * create a tableview for the existed table
	 *
	 */
	private void createTableViewer() {
		tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);
		tableViewer.setColumnProperties(elementNames);

		// Create the cell editors
		CellEditor[] editors = new CellEditor[elementNames.length];

		for (int i = 0; i < elementNames.length; i++) {
			TextCellEditor textEditor = new TextCellEditor(table);
			((Text) textEditor.getControl()).setTextLimit(60);
			if (i == 1) {
				// assure that the CUSTOM NAME column doesn't contain
				// ReportPlugin.PREFERENCE_DELIMITER
				((Text) textEditor.getControl()).addVerifyListener(

						new VerifyListener() {

							@Override
							public void verifyText(VerifyEvent e) {
								e.doit = e.text.indexOf(ReportPlugin.PREFERENCE_DELIMITER) < 0;
							}
						});

			}
			editors[i] = textEditor;
		}

		// Assign the cell editors to the viewer
		tableViewer.setCellEditors(editors);
		// Set the cell modifier for the viewer
		tableViewer.setCellModifier(new ElementNamesCellModifier(this));
	}

	private void sortTable(final TableColumn column, final boolean asc) {
		Table table = tableViewer.getTable();
		table.setSortColumn(column);
		table.setSortDirection(asc ? SWT.UP : SWT.DOWN);
		tableViewer.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object o1, Object o2) {
				int result;
				switch (tableViewer.getTable().indexOf(column)) {
				case 0:
				default:
					result = ((ItemContent) o1).getDisplayName().compareTo(((ItemContent) o2).getDisplayName());
					break;
				case 1:
					result = ((ItemContent) o1).getCustomName().compareTo(((ItemContent) o2).getCustomName());
					break;
				case 2:
					result = ((ItemContent) o1).getDescription().compareTo(((ItemContent) o2).getDescription());
					break;
				}
				return asc ? result : result * -1;
			}
		});

	}

	/**
	 * Get the list of elementNames
	 *
	 */
	public java.util.List getElementNames() {
		return Arrays.asList(elementNames);
	}

	@Override
	protected void updateControls() {
		itemContentList = new ItemContentList(this, getKeys());
		tableViewer.setInput(itemContentList);
	}

	/**
	 * get selected item
	 *
	 * @return selection
	 */
	public ISelection getSelection() {
		return tableViewer.getSelection();
	}

	/**
	 * Return the ExampleTaskList
	 */
	public ItemContentList getContentList() {
		return itemContentList;
	}

	/**
	 * InnerClass that acts as a proxy for the ItemContentList providing content for
	 * the Table. It implements the IItemListViewer interface since it must register
	 * changeListeners with the ItemContentList
	 */
	class ContentProvider implements IStructuredContentProvider, IItemListViewer {

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.
		 * viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
			if (newInput != null) {
				((ItemContentList) newInput).addChangeListener(this);
			}
			if (oldInput != null) {
				((ItemContentList) oldInput).removeChangeListener(this);
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		@Override
		public void dispose() {
			itemContentList.removeChangeListener(this);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.
		 * Object)
		 */
		@Override
		public Object[] getElements(Object parent) {
			return itemContentList.getContents().toArray();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ITaskListViewer#addTask(ExampleTask)
		 */
		@Override
		public void addContent(ItemContent content) {
			tableViewer.add(content);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ITaskListViewer#removeTask(ExampleTask)
		 */
		@Override
		public void removeContent(ItemContent content) {
			tableViewer.remove(content);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see ITaskListViewer#updateTask(ExampleTask)
		 */
		@Override
		public void updateContent(ItemContent content) {
			tableViewer.update(content, null);
		}
	}

}
