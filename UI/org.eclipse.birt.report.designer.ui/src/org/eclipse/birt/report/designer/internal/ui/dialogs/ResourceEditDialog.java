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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.AutoResizeTableLayout;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.LinkedProperties;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog class for resource key/value select and edit
 */

public class ResourceEditDialog extends BaseDialog {

	protected TableViewer viewer;

	protected Text keyText, valueText;

	private Button btnDelete;

	private LinkedProperties[] contents;

	protected String[] propFileName;

	protected boolean listChanged;

	private URL[] resourceURLs;

	private Button btnAdd;

	protected List<GlobalProperty> globalLinkedProperties = new ArrayList<GlobalProperty>();

	/**
	 * PropertyLabelProvider
	 */
	static class PropertyLabelProvider extends LabelProvider implements ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.
		 * lang.Object, int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof GlobalProperty) {
				GlobalProperty entry = (GlobalProperty) element;

				switch (columnIndex) {
				case 0:
					return String.valueOf(entry.key);
				case 1:
					return String.valueOf(entry.value);
				}

			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java
		 * .lang.Object, int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	/**
	 * ResourceSorter
	 */
	static class ResourceSorter extends ViewerSorter {

		private boolean descent;
		private boolean second;

		/**
		 * The constructor.
		 * 
		 * @param descent sorting order.
		 * @param second  if it's the second column.
		 */
		public ResourceSorter(boolean descent, boolean second) {
			super();

			this.descent = descent;
			this.second = second;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerSorter#compare(org.eclipse.jface.
		 * viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public int compare(Viewer viewer, Object e1, Object e2) {
			String name1;
			String name2;

			if (viewer == null || !(viewer instanceof ContentViewer)) {
				if (descent) {
					name1 = e2.toString();
					name2 = e1.toString();
				} else {
					name1 = e1.toString();
					name2 = e2.toString();
				}
			} else {
				IBaseLabelProvider prov = ((ContentViewer) viewer).getLabelProvider();
				if (prov instanceof ITableLabelProvider) {
					ITableLabelProvider lprov = (ITableLabelProvider) prov;
					if (second) {
						if (descent) {
							name1 = lprov.getColumnText(e2, 1);
							name2 = lprov.getColumnText(e1, 1);
						} else {
							name1 = lprov.getColumnText(e1, 1);
							name2 = lprov.getColumnText(e2, 1);
						}
					} else {
						if (descent) {
							name1 = lprov.getColumnText(e2, 0);
							name2 = lprov.getColumnText(e1, 0);
						} else {
							name1 = lprov.getColumnText(e1, 0);
							name2 = lprov.getColumnText(e2, 0);
						}
					}
				} else {
					if (descent) {
						name1 = e2.toString();
						name2 = e1.toString();
					} else {
						name1 = e1.toString();
						name2 = e2.toString();
					}
				}
			}
			if (name1 == null) {
				name1 = ""; //$NON-NLS-1$
			}
			if (name2 == null) {
				name2 = ""; //$NON-NLS-1$
			}

			return collator.compare(name1, name2);
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param parentShell
	 * @param title
	 */
	public ResourceEditDialog(Shell parentShell, String title) {
		super(parentShell, title);

		setShellStyle(SWT.CLOSE | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.RESIZE);

		listChanged = false;

	}

	public void setResourceURL(URL url) {
		this.resourceURLs = new URL[] { url };
	}

	/**
	 * Set the resource file URL. The url is computed by Model.
	 * 
	 * @param url
	 */
	public void setResourceURLs(URL[] urls) {
		this.resourceURLs = urls;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#create()
	 */
	public void create() {
		super.create();

		updateButtonState();
	}

	/**
	 * Loads the key/value from message file.
	 */
	private void loadMessage() {
		if (this.resourceURLs != null && this.resourceURLs.length > 0) {
			if (contents == null)
				contents = new LinkedProperties[resourceURLs.length];
			if (propFileName == null)
				propFileName = new String[resourceURLs.length];
			LinkedHashMap<String, GlobalProperty> propertyMap = new LinkedHashMap<String, GlobalProperty>();
			for (int i = 0; i < resourceURLs.length; i++) {
				contents[i] = new LinkedProperties();
				try {
					if (this.resourceURLs[i] != null) {
						InputStream in = this.resourceURLs[i].openStream();
						contents[i].load(in);
						in.close();
						propFileName[i] = DEUtil.getFilePathFormURL(resourceURLs[i]);

						Iterator iter = contents[i].keySet().iterator();
						if (iter != null) {
							while (iter.hasNext()) {
								String key = (String) iter.next();
								if (!propertyMap.containsKey(key)) {
									GlobalProperty property = new GlobalProperty();
									property.key = key;
									property.value = contents[i].getProperty(key);
									property.holder = contents[i];
									property.isDeleted = false;
									property.holderFile = propFileName[i];
									propertyMap.put(key, property);
								}
							}
						}
					}
				} catch (Exception e) {
					ExceptionHandler.handle(e);
				}
			}

			for (Iterator iter = propertyMap.keySet().iterator(); iter.hasNext();) {
				globalLinkedProperties.add(propertyMap.get(iter.next()));
			}
		}
	}

	/**
	 * Save the key/value to message file, if the file not exists, create it.
	 */
	private boolean saveMessage() {
		if (isFileSystemFile()) {
			if (listChanged) {
				for (int i = 0; i < globalLinkedProperties.size(); i++) {
					GlobalProperty property = globalLinkedProperties.get(i);
					if (property.isDeleted && property.holder != null) {
						property.holder.remove(property.key);
						globalLinkedProperties.remove(i);
						i--;
					}
				}
				for (int i = 0; i < globalLinkedProperties.size(); i++) {
					GlobalProperty property = globalLinkedProperties.get(i);
					if (!property.isDeleted && property.holder != null) {
						property.holder.put(property.key, property.value);
					}
				}
				for (int i = 0; i < resourceURLs.length; i++) {
					URL url = resourceURLs[i];
					if (url != null) {
						if (url.getProtocol().equals("file")) //$NON-NLS-1$
						{
							try {
								saveFile(DEUtil.getFilePathFormURL(url), contents[i], propFileName[i]);
							} catch (Exception e) {
								ExceptionHandler.handle(e);
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}

	private boolean saveFile(String filePath, LinkedProperties properties, String fileName) {
		File f = new File(filePath);
		if (!(f.exists() && f.isFile())) {
			MessageDialog.openError(getShell(), Messages.getString("ResourceEditDialog.NotFile.Title"), //$NON-NLS-1$
					Messages.getFormattedString("ResourceEditDialog.NotFile.Message", //$NON-NLS-1$
							new Object[] { propFileName }));
			return false;
		} else if (!f.canWrite()) {
			MessageDialog.openError(getShell(), Messages.getString("ResourceEditDialog.ReadOnlyEncounter.Title"), //$NON-NLS-1$
					Messages.getFormattedString("ResourceEditDialog.ReadOnlyEncounter.Message", //$NON-NLS-1$
							new Object[] { propFileName }));
			return false;
		}

		FileOutputStream fos = null;
		try {
			if (f.canWrite()) {
				fos = new FileOutputStream(f);

				properties.store(fos, ""); //$NON-NLS-1$

			}
			return true;
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					ExceptionHandler.handle(e);
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.RESOURCE_EDIT_DIALOG_ID);
		loadMessage();

		final Composite innerParent = (Composite) super.createDialogArea(parent);

		final Table table = new Table(innerParent, SWT.BORDER | SWT.FULL_SELECTION);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = 450;
		data.heightHint = 200;
		table.setLayoutData(data);

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);

		final TableColumn column1 = new TableColumn(table, SWT.NONE);
		column1.setText(Messages.getString("ResourceEditDialog.text.Key.TableColumn")); //$NON-NLS-1$
		column1.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(column1);
				viewer.setSorter(new ResourceSorter(table.getSortDirection() == SWT.UP, false));
				table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
			}
		});

		final TableColumn column2 = new TableColumn(table, SWT.NONE);
		column2.setText(Messages.getString("ResourceEditDialog.text.Value.TableColumn")); //$NON-NLS-1$
		column2.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				table.setSortColumn(column2);
				viewer.setSorter(new ResourceSorter(table.getSortDirection() == SWT.UP, true));
				table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
			}
		});

		viewer = new TableViewer(table);
		viewer.setContentProvider(new IStructuredContentProvider() {

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					List list = (List) inputElement;
					List availableList = new ArrayList();
					for (int i = 0; i < list.size(); i++) {
						GlobalProperty property = (GlobalProperty) list.get(i);
						if (!property.isDeleted)
							availableList.add(property);
					}
					return availableList.toArray();
				}

				return new Object[0];
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}

			public void dispose() {
			}
		});
		viewer.setLabelProvider(new PropertyLabelProvider());

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateSelection();
			}
		});

		TableLayout layout = new AutoResizeTableLayout(viewer.getTable());
		layout.addColumnData(new ColumnWeightData(50, true));
		layout.addColumnData(new ColumnWeightData(50, true));
		viewer.getTable().setLayout(layout);

		// table.setSortColumn( column1 );
		// table.setSortDirection( SWT.UP );
		// viewer.setSorter( new ResourceSorter( false, false ) );

		Group gp = new Group(innerParent, SWT.NONE);
		gp.setText(Messages.getString("ResourceEditDialog.text.QuickAdd")); //$NON-NLS-1$
		gp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		gp.setLayout(new GridLayout(6, false));

		Label lb = new Label(gp, 0);
		lb.setText(Messages.getString("ResourceEditDialog.text.Key")); //$NON-NLS-1$

		keyText = new Text(gp, SWT.BORDER | SWT.SINGLE);
		keyText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		lb = new Label(gp, 0);
		lb.setText(Messages.getString("ResourceEditDialog.text.Value")); //$NON-NLS-1$

		// lb = new Label( gp, 0 );
		// lb = new Label( gp, 0 );
		valueText = new Text(gp, SWT.BORDER | SWT.SINGLE);
		valueText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnAdd = new Button(gp, SWT.PUSH);
		btnAdd.setText(Messages.getString("ResourceEditDialog.text.Add")); //$NON-NLS-1$
		btnAdd.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				addSelection();
			}
		});
		btnAdd.setEnabled(isFileSystemFile());

		btnDelete = new Button(gp, SWT.PUSH);
		btnDelete.setText(Messages.getString("ResourceEditDialog.text.Delete")); //$NON-NLS-1$
		btnDelete.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				deleteSelection();
			}
		});

		lb = new Label(innerParent, 0);
		lb.setText(Messages.getString("ResourceEditDialog.message.AddNote")); //$NON-NLS-1$
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		viewer.setInput(globalLinkedProperties);

		return innerParent;
	}

	protected void updateSelection() {
		if (viewer.getTable().getSelectionCount() > 0) {
			keyText.setText(viewer.getTable().getSelection()[0].getText(0));
			valueText.setText(viewer.getTable().getSelection()[0].getText(1));
		}

		updateButtonState();
	}

	protected void addSelection() {
		String key = keyText.getText();
		String val = valueText.getText();
		if (key != null && key.trim().length() > 0) {
			boolean isContained = false;
			for (int i = 0; i < globalLinkedProperties.size(); i++) {
				GlobalProperty property = (GlobalProperty) globalLinkedProperties.get(i);
				if (key.equals(property.key) && !property.isDeleted) {
					property.value = val;
					isContained = true;
					break;
				}
			}

			if (!isContained) {
				// if the file is read-only then change is not allowed.
				if (propFileName[0] == null) {
					return;
				} else {
					File f = new File(propFileName[0]);
					if (!(f.exists() && f.isFile())) {
						MessageDialog.openError(getShell(), Messages.getString("ResourceEditDialog.NotFile.Title"), //$NON-NLS-1$
								Messages.getFormattedString("ResourceEditDialog.NotFile.Message", //$NON-NLS-1$
										new Object[] { propFileName }));
						return;
					} else if (!f.canWrite()) {
						MessageDialog.openError(getShell(),
								Messages.getString("ResourceEditDialog.ReadOnlyEncounter.Title"), //$NON-NLS-1$
								Messages.getFormattedString("ResourceEditDialog.ReadOnlyEncounter.Message", //$NON-NLS-1$
										new Object[] { propFileName }));
						return;
					}

					GlobalProperty property = new GlobalProperty();
					property.key = key;
					property.value = val;
					property.holder = contents[0];
					property.isDeleted = false;
					property.holderFile = propFileName[0];

					globalLinkedProperties.add(property);
				}
			}

			viewer.refresh();
			listChanged = true;
			updateSelection();

		} else {
			MessageDialog.openWarning(getShell(), Messages.getString("ResourceEditDialog.text.AddWarningTitle"), //$NON-NLS-1$
					Messages.getString("ResourceEditDialog.text.AddWarningMsg")); //$NON-NLS-1$
		}
	}

	private void deleteSelection() {
		if (viewer.getTable().getSelectionIndex() == -1) {
			return;
		}

		StructuredSelection selection = (StructuredSelection) viewer.getSelection();
		if (selection.getFirstElement() instanceof GlobalProperty) {
			GlobalProperty property = (GlobalProperty) selection.getFirstElement();
			String file = property.holderFile;
			if (file != null) {
				File f = new File(file);
				if (!(f.exists() && f.isFile())) {
					MessageDialog.openError(getShell(), Messages.getString("ResourceEditDialog.NotFile.Title"), //$NON-NLS-1$
							Messages.getFormattedString("ResourceEditDialog.NotFile.Message", //$NON-NLS-1$
									new Object[] { file }));
					return;
				} else if (!f.canWrite()) {
					MessageDialog.openError(getShell(),
							Messages.getString("ResourceEditDialog.ReadOnlyEncounter.Title"), //$NON-NLS-1$
							Messages.getFormattedString("ResourceEditDialog.ReadOnlyEncounter.Message", //$NON-NLS-1$
									new Object[] { file }));
					return;
				}
			}

			// if the file is read-only then change is not allowed.
			listChanged = true;
			if (property.holderFile != null)
				property.isDeleted = true;
			else
				globalLinkedProperties.remove(property);
			viewer.refresh();
			updateSelection();
		}
	}

	private void updateButtonState() {
		getOkButton().setEnabled(viewer.getTable().getSelectionCount() > 0);

		btnDelete.setEnabled(isFileSystemFile() && viewer.getTable().getSelectionIndex() != -1);
	}

	private boolean isFileSystemFile() {
		if (getAvailableResourceUrls() == null || getAvailableResourceUrls().length == 0)
			return false;
		else {
			boolean flag = true;
			for (int i = 0; i < resourceURLs.length; i++) {
				URL url = resourceURLs[i];
				if (url != null) {
					if (!url.getProtocol().equals("file")) //$NON-NLS-1$
					{
						flag = false;
						break;
					}
				}
			}
			return flag;
		}
	}

	private URL[] getAvailableResourceUrls() {
		List<URL> urls = new ArrayList<URL>();
		if (resourceURLs == null)
			return urls.toArray(new URL[0]);
		else {
			for (int i = 0; i < resourceURLs.length; i++) {
				if (resourceURLs[i] != null)
					urls.add(resourceURLs[i]);
			}
			return urls.toArray(new URL[0]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		saveMessage();

		setResult(viewer.getTable().getSelection()[0].getText(0));
		setDetailResult(new String[] { viewer.getTable().getSelection()[0].getText(0),
				viewer.getTable().getSelection()[0].getText(1) });

		super.okPressed();
	}

	public Object getDetailResult() {
		return detailResult;
	}

	private Object detailResult;

	final protected void setDetailResult(Object value) {
		detailResult = value;
	}

	public boolean isKeyValueListChanged() {
		return listChanged;
	}

	private static class GlobalProperty {

		private String key;
		private String value;
		private LinkedProperties holder;
		private String holderFile;
		private boolean isDeleted;
	}

	protected boolean needRememberLastSize() {
		return true;
	}
}
