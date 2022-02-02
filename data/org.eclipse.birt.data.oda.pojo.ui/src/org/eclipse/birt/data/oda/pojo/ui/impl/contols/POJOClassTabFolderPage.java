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

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.dialogs.ClassPathsPageHelper;
import org.eclipse.birt.data.oda.pojo.ui.impl.dialogs.JarsSelectionDialog;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.birt.data.oda.pojo.ui.impl.providers.ClassPathTableProvider;
import org.eclipse.birt.data.oda.pojo.ui.util.Constants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;

public class POJOClassTabFolderPage {

	private ClassPathsPageHelper parent;
	private TableViewer classPathsTableViewer;
	private Button editBtn, upBtn, downBtn, removeBtn;
	private ClassSelectionButton jarButton;
	private Composite left, right;

	private File resouceDir;
	private List<ClassPathElement> elements;
	private String promp;
	private String dataSetClassPath;

	private static String PATH_SEPARATOR = ";"; //$NON-NLS-1$

	private POJOClassTabFolderPage friendPage;
	private boolean isPageEditable;

	public TableViewer getClassPathsTableViewer() {
		return classPathsTableViewer;
	}

	public POJOClassTabFolderPage getTabFriendClassTabFolderPage() {
		return friendPage;
	}

	public void setFriendPage(POJOClassTabFolderPage friendPage) {
		this.friendPage = friendPage;
	}

	public POJOClassTabFolderPage(ClassPathsPageHelper parent, File resouceDir) {
		this.parent = parent;
		this.resouceDir = resouceDir;
		this.isPageEditable = true;
	}

	public TabItem createContents(TabFolder tabFolder) {
		final Composite page = new Composite(tabFolder, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 10;
		layout.verticalSpacing = 5;
		page.setLayout(layout);
		page.setLayoutData(new GridData(GridData.FILL_BOTH));

		TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
		tabItem.setControl(page);

		createComposite(page);

		return tabItem;
	}

	public void setPrompMessage(String promp) {
		this.promp = promp;
	}

	private void createComposite(Composite page) {
		createLeftArea(page);
		createRightArea(page);
	}

	private void createLeftArea(Composite page) {
		left = new Composite(page, SWT.NONE);
		left.setLayout(new GridLayout(1, false));
		left.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(left, SWT.NONE);
		label.setText(promp);

		classPathsTableViewer = new TableViewer(left,
				SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 250;
		gd.widthHint = 300;

		classPathsTableViewer.getTable().setLayoutData(gd);
		classPathsTableViewer.getTable().setLinesVisible(false);

		ClassPathTableProvider provider = new ClassPathTableProvider();
		classPathsTableViewer.setContentProvider(provider);
		classPathsTableViewer.setLabelProvider(provider);

		classPathsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				updateButtons();
			}
		});

		classPathsTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				doEdit();
			}
		});
	}

	private void createRightArea(Composite page) {
		right = new Composite(page, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 30;
		right.setLayout(layout);
		right.setLayoutData(new GridData(GridData.FILL_BOTH));

		createMenuButtons(right);

		new Label(right, SWT.NONE);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		editBtn = new Button(right, SWT.PUSH);
		editBtn.setText(Messages.getString("DataSource.POJOClassTabFolderPage.button.edit")); //$NON-NLS-1$
		editBtn.setLayoutData(data);
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				doEdit();
			}
		});

		removeBtn = new Button(right, SWT.PUSH);
		removeBtn.setText(Messages.getString("DataSource.POJOClassTabFolderPage.button.remove")); //$NON-NLS-1$
		removeBtn.setLayoutData(data);
		removeBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				doRemoveItems();
			}
		});

		new Label(right, SWT.NONE);

		upBtn = new Button(right, SWT.PUSH);
		upBtn.setText(Messages.getString("DataSource.POJOClassTabFolderPage.button.up")); //$NON-NLS-1$
		upBtn.setLayoutData(data);
		upBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				doMoveUp();
			}
		});

		downBtn = new Button(right, SWT.PUSH);
		downBtn.setText(Messages.getString("DataSource.POJOClassTabFolderPage.button.down")); //$NON-NLS-1$
		downBtn.setLayoutData(data);
		downBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				doMoveDown();
			}
		});

		resetButtonSize();

		updateButtons();
	}

	private void createMenuButtons(Composite right) {
		GridData data = new GridData(GridData.FILL_HORIZONTAL);

		jarButton = MenuButtonUtil.createClassSelectionButton(this, right, classPathsTableViewer,
				new MenuButtonProvider(), null, SWT.PUSH);
		jarButton.getMenuButtonHelper().setProperty(Constants.RESOURCE_FILE_DIR, this.resouceDir);

		jarButton.refreshMenuItems();

		jarButton.getControl().setLayoutData(data);
		if (jarButton.getProvider().getMenuItems().length == 2) {
			jarButton.getControl().setToolTipText(Messages.getString("DataSource.button.tooltip.AddJar.TwoItems")); //$NON-NLS-1$
		} else {
			jarButton.getControl().setToolTipText(Messages.getString("DataSource.button.tooltip.AddJar.OneItems")); //$NON-NLS-1$
		}
	}

	private void resetButtonSize() {
		int maxWidth = 80;
		maxWidth = computeMaxWidth(jarButton.getControl(), maxWidth);
		maxWidth = computeMaxWidth(editBtn, maxWidth);
		maxWidth = computeMaxWidth(removeBtn, maxWidth);
		maxWidth = computeMaxWidth(upBtn, maxWidth);
		maxWidth = computeMaxWidth(downBtn, maxWidth);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.widthHint = maxWidth;

		jarButton.getControl().setLayoutData(data);
		editBtn.setLayoutData(data);
		removeBtn.setLayoutData(data);
		upBtn.setLayoutData(data);
		downBtn.setLayoutData(data);

	}

	private int computeMaxWidth(Control button, int size) {
		int widthHint = button.computeSize(-1, -1).x - button.getBorderWidth();
		return widthHint > size ? widthHint : size;
	}

	private void updateButtons() {
		if (isPageEditable) {
			if (classPathsTableViewer.getTable().getSelectionCount() == 1) {
				editBtn.setEnabled(true);
				upBtn.setEnabled(classPathsTableViewer.getTable().getSelectionIndex() > 0);
				downBtn.setEnabled(classPathsTableViewer.getTable()
						.getSelectionIndex() < (classPathsTableViewer.getTable().getItemCount() - 1));
			} else {
				editBtn.setEnabled(false);
				upBtn.setEnabled(false);
				downBtn.setEnabled(false);
			}
			removeBtn.setEnabled(classPathsTableViewer.getTable().getSelectionCount() > 0);
		} else {
			editBtn.setEnabled(false);
			upBtn.setEnabled(false);
			downBtn.setEnabled(false);
			removeBtn.setEnabled(false);
		}
	}

	public void setEnabled(boolean enabled) {
		left.setEnabled(enabled);
		right.setEnabled(enabled);
		jarButton.getControl().setEnabled(enabled);

		Control[] children = left.getChildren();
		setAllEnabled(children, enabled);
		this.isPageEditable = enabled;
	}

	private void setAllEnabled(Control[] controls, boolean enabled) {
		for (int i = 0; i < controls.length; i++) {
			controls[i].setEnabled(enabled);
			if (controls[i] instanceof Composite) {
				setAllEnabled(((Composite) controls[i]).getChildren(), enabled);

			}
		}
		if (!enabled) {
			editBtn.setEnabled(false);
			upBtn.setEnabled(false);
			downBtn.setEnabled(false);
			removeBtn.setEnabled(false);
		}
	}

	protected void updateWizardPageStatus() {
		if (parent != null)
			parent.updatePageStatus();
	}

	private void synchronizeClassPath() {
		if (!getTabFriendClassTabFolderPage().isPageEditable()) {
			getTabFriendClassTabFolderPage().resetJarElements(getJarElements());
			getTabFriendClassTabFolderPage().updateWizardPageStatus();
		}
	}

	public void initClassPathElements() {
		if (elements == null)
			elements = new ArrayList<ClassPathElement>();
		else
			elements.clear();

		((MenuButtonHelper) this.jarButton.getMenuButtonHelper()).clearTableElementsList();

		if (dataSetClassPath != null && dataSetClassPath.trim().length() > 0) {
			String paths[] = dataSetClassPath.split(PATH_SEPARATOR);
			ClassPathElement[] classPathElements = new ClassPathElement[paths.length];
			for (int i = 0; i < paths.length; i++) {
				File file = new File(paths[i]);

				ClassPathElement element = new ClassPathElement(file.getName(), paths[i], !file.isAbsolute());

				classPathElements[i] = element;
				elements.add(element);
			}

			this.jarButton.getMenuButtonHelper().addClassPathElements(classPathElements, true);
		}

		classPathsTableViewer.setInput(elements);
		classPathsTableViewer.refresh();
		updateWizardPageStatus();
	}

	public boolean canFinish() {
		return elements != null && elements.size() > 0;
	}

	private void doEdit() {
		if (classPathsTableViewer.getTable().getSelectionCount() != 1)
			return;

		ClassPathElement element = (ClassPathElement) classPathsTableViewer.getTable().getSelection()[0].getData();
		Object value = jarButton.getMenuButtonHelper().getPropertyValue(Constants.RESOURCE_FILE_DIR);

		String[] fileNames = null;
		String rootPath = null;
		if (value != null && value instanceof File && element.isRelativePath()) {

			JarsSelectionDialog dialog = new JarsSelectionDialog(jarButton.getControl().getShell(), (File) value);
			dialog.setInitialSelection(element);
			if (dialog.open() == Window.OK) {
				fileNames = dialog.getSelectedItems();
			}
		} else {
			FileDialog dialog = new FileDialog(jarButton.getControl().getShell(), SWT.MULTI);
			dialog.setFilterExtensions(new String[] { "*.jar; *.zip" //$NON-NLS-1$ , $NON-NLS-2$
			});
			dialog.setFileName(element.getValue());
			if (dialog.open() != null) {
				fileNames = dialog.getFileNames();
				rootPath = dialog.getFilterPath();
			}
		}

		if (fileNames != null && fileNames.length > 0) {
			element.setValue(new File(fileNames[0]).getName());
			element.setFullPath(fileNames[0] + File.separator + rootPath);
			classPathsTableViewer.refresh();

			elements.remove(element);
			jarButton.handleSelection(fileNames, rootPath, element.isRelativePath());
		}
		updateWizardPageStatus();
	}

	private void doMoveUp() {
		if (elements != null) {
			int currentIndex = classPathsTableViewer.getTable().getSelectionIndex();
			if (currentIndex > 0) {
				ClassPathElement originalAboveElement = elements.get(currentIndex - 1);
				elements.set(currentIndex - 1, elements.get(currentIndex));
				elements.set(currentIndex, originalAboveElement);
				classPathsTableViewer.refresh();
				updateButtons();
			}
		}
		synchronizeClassPath();
	}

	private void doMoveDown() {
		if (elements != null) {
			int currentIndex = classPathsTableViewer.getTable().getSelectionIndex();
			if (currentIndex < elements.size() - 1) {
				ClassPathElement originalAboveElement = elements.get(currentIndex + 1);
				elements.set(currentIndex + 1, elements.get(currentIndex));
				elements.set(currentIndex, originalAboveElement);
				classPathsTableViewer.refresh();
				updateButtons();
			}
		}
		synchronizeClassPath();
	}

	private void doRemoveItems() {
		if (elements != null) {
			TableItem[] items = classPathsTableViewer.getTable().getSelection();
			for (int i = 0; i < items.length; i++) {
				Object data = items[i].getData();
				if (data instanceof ClassPathElement) {
					elements.remove((ClassPathElement) data);
				}
			}
			classPathsTableViewer.refresh();
			updateButtons();
			updateWizardPageStatus();
		}
		synchronizeClassPath();
	}

	public String getClassPathString() {
		StringBuilder result = new StringBuilder();
		if (elements != null) {
			for (int i = 0; i < elements.size(); i++) {
				ClassPathElement element = elements.get(i);
				result.append(element.getFullPath()).append(PATH_SEPARATOR);
			}
		}
		return result.toString();
	}

	public void setClassPath(String dataSetClassPath) {
		this.dataSetClassPath = dataSetClassPath;
	}

	public void refresh() {
		if (elements != null) {
			elements.clear();
			classPathsTableViewer.refresh();
			((MenuButtonHelper) this.jarButton.getMenuButtonHelper()).clearTableElementsList();
		} else {
			elements = new ArrayList<ClassPathElement>();
		}
		initClassPathElements();
	}

	public List<ClassPathElement> getJarElements() {
		return elements;
	}

	public void resetJarElements(List<ClassPathElement> paths) {
		if (elements != null) {
			elements.clear();
		} else {
			elements = new ArrayList<ClassPathElement>();
		}

		if (paths != null) {
			elements.addAll(paths);
		}
		classPathsTableViewer.refresh();
	}

	public boolean isPageEditable() {
		return isPageEditable;
	}

	public void setPageEditable(boolean isPageEditable) {
		this.isPageEditable = isPageEditable;
	}

}
