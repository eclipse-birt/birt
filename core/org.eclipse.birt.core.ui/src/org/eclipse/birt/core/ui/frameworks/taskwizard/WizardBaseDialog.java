/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.List;

import org.eclipse.birt.core.ui.frameworks.errordisplay.ErrorDialog;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IButtonHandler;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Provides Dialog for WizardBase
 */

public class WizardBaseDialog extends TitleAreaDialog
		implements SelectionListener, ControlListener, DisposeListener, IPageChangeProvider {

	/**
	 * Comment for <code>wizardBase</code>
	 */
	protected final WizardBase wizardBase;

	private ListenerList pageChangedListeners = new ListenerList();

	private transient CTabFolder cmpTaskContainer;

	private transient int iWizardHeightMinimum = 100;

	private transient int iWizardWidthMinimum = 100;

	transient String wizardTitle = "Task Wizard"; //$NON-NLS-1$

	private transient Image imgShell = null;

	private transient Shell shellPopup = null;

	transient String[] tmpTaskArray;
	transient String tmpTopTaskId;

	public WizardBaseDialog(WizardBase wizardBase, Shell parentShell, int iInitialWidth, int iInitialHeight,
			String strTitle, Image imgTitle) {
		super(parentShell);
		this.wizardBase = wizardBase;
		setHelpAvailable(false);

		this.iWizardWidthMinimum = iInitialWidth;
		this.iWizardHeightMinimum = iInitialHeight;
		this.wizardTitle = strTitle;
		this.imgShell = imgTitle;
	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	private void configureTaskContext(String[] sTasks, String topTaskId) {
		// Add tasks
		String[] allTasks = TasksManager.instance().getTasksForWizard(this.wizardBase.sWizardID);
		// ADD DEFAULT TASKS AS DEFINED BY EXTENSIONS
		for (int i = 0; i < allTasks.length; i++) {
			this.wizardBase.addTask(allTasks[i]);
		}
		// ADD TASKS SPECIFIED DURING INVOCATION
		if (sTasks != null && sTasks.length > 0) {
			for (int i = 0; i < sTasks.length; i++) {
				if (!this.wizardBase.vTaskIDs.contains(sTasks[i])) {
					this.wizardBase.addTask(sTasks[i]);
				}
			}
		}

		// Open the specified task
		if (topTaskId == null) {
			if (this.wizardBase.vTaskIDs.size() > 0) {
				this.wizardBase.sCurrentActiveTask = this.wizardBase.vTaskIDs.get(0).toString();
			}
		} else {
			assert this.wizardBase.vTaskIDs.contains(topTaskId);
			this.wizardBase.sCurrentActiveTask = topTaskId;
		}
	}

	protected void initializeBounds() {
		// Set shell properties
		getShell().setText(wizardTitle);
		setTitle(wizardTitle);
		if (imgShell != null) {
			getShell().setImage(imgShell);
		}
		getShell().addControlListener(this);
		getShell().addDisposeListener(this);

		// Add each task to container
		String[] allTasks = TasksManager.instance().getTasksForWizard(this.wizardBase.sWizardID);
		for (int i = 0; i < allTasks.length; i++) {
			// Create the blank tab item.
			CTabItem item = new CTabItem(getTabContainer(), SWT.NONE);
			item.setImage(TasksManager.instance().getTask(allTasks[i]).getImage());
			item.setText(TasksManager.instance().getTask(allTasks[i]).getTitle());
			item.setData(allTasks[i]);
		}

		if (tmpTopTaskId != null) {
			int taskIndex = this.wizardBase.vTaskIDs.indexOf(tmpTopTaskId);
			cmpTaskContainer.setSelection(taskIndex);
		}

		// Open current task
		if (this.wizardBase.getCurrentTask() != null) {
			this.wizardBase.getCurrentTask().setContext(this.wizardBase.context);

			// Do not pack wizard since the bound has been calculated by
			// jface
			this.wizardBase.packNeeded = false;
			this.wizardBase.switchTo(this.wizardBase.sCurrentActiveTask);
			this.wizardBase.packNeeded = true;
		}

		super.initializeBounds();
	}

	public void create() {
		configureTaskContext(tmpTaskArray, tmpTopTaskId);
		super.create();
	}

	protected Control createDialogArea(Composite parent) {
		// create the top level composite for the dialog area
		Composite composite = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.verticalSpacing = 0;
			layout.horizontalSpacing = 0;
			composite.setLayout(layout);
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
			composite.setFont(parent.getFont());
		}

		Label lblSeparator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Initialize and layout UI components of the framework
		cmpTaskContainer = new CTabFolder(composite, SWT.TOP | SWT.FLAT);
		{
			cmpTaskContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
			cmpTaskContainer.setTabHeight(25);
			// cmpTaskContainer.setSimple( false );
			cmpTaskContainer.addSelectionListener(this);
		}

		createTabToolButtons(cmpTaskContainer);

		lblSeparator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblSeparator.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		return composite;
	}

	protected void createTabToolButtons(CTabFolder tabFolder) {
		List<IButtonHandler> buttons = wizardBase.getTabToolButtons();
		if (buttons.size() == 0) {
			return;
		}
		ToolBar toolbar = new ToolBar(tabFolder, SWT.FLAT | SWT.WRAP);
		tabFolder.setTopRight(toolbar);
		for (IButtonHandler btnHandler : buttons) {
			ToolItem btn = new ToolItem(toolbar, SWT.NONE);
			btn.addSelectionListener(this);
			btn.setData(btnHandler);
			if (btnHandler.getLabel() != null) {
				btn.setText(btnHandler.getLabel());
			}
			if (btnHandler.getTooltip() != null) {
				btn.setToolTipText(btnHandler.getTooltip());
			}
			if (btnHandler.getIcon() != null) {
				btn.setImage(btnHandler.getIcon());
			}
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.BACK_ID, Messages.getString("WizardBase.Back"), //$NON-NLS-1$
				false);
		createButton(parent, IDialogConstants.NEXT_ID, Messages.getString("WizardBase.Next"), //$NON-NLS-1$
				false);
		createButton(parent, IDialogConstants.FINISH_ID, Messages.getString("WizardBase.Finish"), //$NON-NLS-1$
				this.wizardBase.bEnterClosed);
		createButton(parent, IDialogConstants.CANCEL_ID, Messages.getString("WizardBase.Cancel"), //$NON-NLS-1$
				false);

		for (int i = 0; i < this.wizardBase.buttonList.size(); i++) {
			IButtonHandler buttonHandler = this.wizardBase.buttonList.get(i);
			// Make sure the same id was not registered.
			assert getButton(buttonHandler.getId()) == null;
			buttonHandler.setButton(createButton(parent, buttonHandler.getId(), buttonHandler.getLabel(), false));
		}

		// Update buttons status
		int taskIndex = this.wizardBase.vTaskIDs.indexOf(this.wizardBase.sCurrentActiveTask);
		if (taskIndex > 0) {
			getButton(IDialogConstants.BACK_ID).setEnabled(true);
		} else {
			getButton(IDialogConstants.BACK_ID).setEnabled(false);
		}
		if (taskIndex < this.wizardBase.vTaskIDs.size() - 1) {
			getButton(IDialogConstants.NEXT_ID).setEnabled(true);
		} else {
			getButton(IDialogConstants.NEXT_ID).setEnabled(false);
		}
	}

	protected void buttonPressed(int buttonId) {
		if (IDialogConstants.FINISH_ID == buttonId) {
			okPressed();
		} else if (IDialogConstants.CANCEL_ID == buttonId) {
			cancelPressed();
		} else if (IDialogConstants.BACK_ID == buttonId) {
			backPressed();
		} else if (IDialogConstants.NEXT_ID == buttonId) {
			nextPressed();
		}

		for (int i = 0; i < this.wizardBase.buttonList.size(); i++) {
			IButtonHandler buttonHandler = this.wizardBase.buttonList.get(i);
			if (buttonId == buttonHandler.getId()) {
				buttonHandler.run();
				break;
			}
		}
	}

	public void switchTask() {
		// Set the description for each task
		String strDesc = this.wizardBase.getCurrentTask().getDescription();
		if (strDesc != null) {
			setMessage(strDesc);
		}

		// Update or create UI
		if (getTabContainer().getSelectionIndex() < 0) {
			getTabContainer().setSelection(0);
		}
		CTabItem currentItem = getTabContainer().getItem(getTabContainer().getSelectionIndex());
		this.wizardBase.getCurrentTask().createControl(getTabContainer());
		if (currentItem.getControl() == null) {
			currentItem.setControl(this.wizardBase.getCurrentTask().getControl());
		}

		// Pack every task to show as much as possible
		packWizard();

		// Notify page changed to refresh help page
		firePageChanged(new PageChangedEvent(this, this.wizardBase.getCurrentTask()));
	}

	private void backPressed() {
		int i = this.wizardBase.vTaskIDs.indexOf(this.wizardBase.sCurrentActiveTask);
		if (i > 0) {
			cmpTaskContainer.setSelection(i - 1);
			this.wizardBase.switchTo(this.wizardBase.vTaskIDs.get(i - 1));
			getButton(IDialogConstants.NEXT_ID).setEnabled(true);
		}
		if (i == 1) {
			// Just switched to first tab
			getButton(IDialogConstants.BACK_ID).setEnabled(false);
		}
	}

	private void nextPressed() {
		int i = this.wizardBase.vTaskIDs.indexOf(this.wizardBase.sCurrentActiveTask);
		if (i < this.wizardBase.vTaskIDs.size() - 1) {
			cmpTaskContainer.setSelection(i + 1);
			this.wizardBase.switchTo(this.wizardBase.vTaskIDs.get(i + 1));
			getButton(IDialogConstants.BACK_ID).setEnabled(true);
		}
		if (i == this.wizardBase.vTaskIDs.size() - 2) {
			getButton(IDialogConstants.NEXT_ID).setEnabled(false);
		}
	}

	protected void okPressed() {
		final String[] saMessages = this.wizardBase.validate();
		if (saMessages != null && saMessages.length > 0) {
			ErrorDialog ed = new ErrorDialog(this.wizardBase.shellParent,
					Messages.getString("WizardBase.error.ErrorsEncountered"), //$NON-NLS-1$
					Messages.getString("WizardBase.error.FollowingErrorsReportedWhileVerifying"), //$NON-NLS-1$
					saMessages, new String[] {});
			if (ed.getOption() == ErrorDialog.OPTION_ACCEPT) {
				// Stop quitting to fix manually
				return;
			}
		}
		super.okPressed();
	}

	/**
	 * Sets the minimum size of the wizard
	 * 
	 * @param iWidth  width minimum
	 * @param iHeight height minimum
	 */
	public void setMinimumSize(int iWidth, int iHeight) {
		iWizardWidthMinimum = iWidth;
		iWizardHeightMinimum = iHeight;
	}

	public Shell createPopupContainer() {
		// CLEAR ANY EXISTING POPUP
		if (shellPopup != null && !shellPopup.isDisposed()) {
			shellPopup.dispose();
		}
		// CREATE AND DISPLAY THE NEW POPUP
		if (shellPopup == null || shellPopup.isDisposed()) {
			// Make the popup modal on the Linux platform. See
			// bugzilla#123386
			int shellStyle = SWT.DIALOG_TRIM | SWT.RESIZE;
			shellPopup = new Shell(getShell(), shellStyle);
			shellPopup.setLayout(new FillLayout());
		}
		return shellPopup;
	}

	public Shell getPopupContainer() {
		return shellPopup;
	}

	/**
	 * Attaches the popup window.
	 * 
	 * @param sPopupTitle '&' will be removed for accelerator key, if the popup
	 *                    title is from the control text.
	 */
	public void attachPopup(String sPopupTitle, int iWidth, int iHeight) {
		shellPopup.setText(sPopupTitle);
		// IF PREFERRED SIZE IS SPECIFIED USE IT...ELSE USE PACK
		if (iWidth != -1 && iHeight != -1) {
			shellPopup.setSize(iWidth, iHeight);
		} else {
			shellPopup.pack();
		}
		setPopupLocation();
		shellPopup.open();
	}

	public void detachPopup() {
		if (shellPopup != null && !shellPopup.isDisposed()) {
			shellPopup.close();
		}
	}

	/**
	 * Packs the wizard to display enough size
	 * 
	 */
	public void packWizard() {
		if (!this.wizardBase.packNeeded) {
			return;
		}

		// Execute custom pack method, if it is success, no need to do
		// default pack.
		if (this.wizardBase.applyCustomPack()) {
			return;
		}

		boolean changed = false;
		Point wizardSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int iWizardWidth = Math.max(wizardSize.x, iWizardWidthMinimum);
		int iWizardHeight = Math.max(wizardSize.y, iWizardHeightMinimum);
		Point oldSize = getShell().getSize();
		Rectangle screen = getShell().getDisplay().getClientArea();
		if (oldSize.x < iWizardWidth) {
			oldSize.x = Math.min(iWizardWidth, screen.width);
			changed = true;
		}
		if (oldSize.y < iWizardHeight) {
			// Do not exceed the screen height minus task bar height
			oldSize.y = Math.min(iWizardHeight, screen.height - 40);
			changed = true;
		}
		if (changed) {
			getShell().setSize(oldSize);
			getShell().layout();
		}
	}

	CTabFolder getTabContainer() {
		return cmpTaskContainer;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	public void widgetSelected(SelectionEvent e) {
		if (e.getSource() instanceof CTabFolder) {
			String taskId = (String) e.item.getData();
			int indexLabel = this.wizardBase.vTaskIDs.indexOf(taskId);
			if (indexLabel >= 0) {
				this.wizardBase.switchTo(taskId);
				getButton(IDialogConstants.NEXT_ID).setEnabled(indexLabel < this.wizardBase.vTaskIDs.size() - 1);
				getButton(IDialogConstants.BACK_ID).setEnabled(indexLabel > 0);
			}
		} else if (e.getSource() instanceof ToolItem) {
			if (wizardBase.getTabToolButtons().contains(((ToolItem) e.getSource()).getData())) {
				IButtonHandler btnHandle = (IButtonHandler) ((ToolItem) e.getSource()).getData();
				btnHandle.run();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlMoved(ControlEvent e) {
		setPopupLocation();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.
	 * ControlEvent)
	 */
	public void controlResized(ControlEvent e) {
		setPopupLocation();
	}

	private void setPopupLocation() {
		if (shellPopup != null && !shellPopup.isDisposed()) {
			int x = 0;
			if (getShell().getLocation().x + getShell().getSize().x
					+ shellPopup.getSize().x > getShell().getDisplay().getClientArea().width) {
				// Avoid the popup exceeds the right border of the display
				// area
				x = getShell().getDisplay().getClientArea().width - shellPopup.getSize().x;
			} else {
				x = getShell().getLocation().x + getShell().getSize().x;
			}
			shellPopup.setLocation(x, getShell().getLocation().y + 20);
		}
	}

	public void widgetDisposed(DisposeEvent e) {
		this.wizardBase.dispose();
	}

	public void addPageChangedListener(IPageChangedListener listener) {
		pageChangedListeners.add(listener);
	}

	public Object getSelectedPage() {
		return this.wizardBase.getCurrentTask();
	}

	public void removePageChangedListener(IPageChangedListener listener) {
		pageChangedListeners.remove(listener);
	}

	/**
	 * Notifies any selection changed listeners that the selected page has changed.
	 * Only listeners registered at the time this method is called are notified.
	 * 
	 * @param event a selection changed event
	 * 
	 * @see IPageChangedListener#pageChanged
	 * 
	 * @since 2.1
	 */
	void firePageChanged(final PageChangedEvent event) {
		Object[] listeners = pageChangedListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			final IPageChangedListener l = (IPageChangedListener) listeners[i];
			SafeRunnable.run(new SafeRunnable() {

				public void run() {
					l.pageChanged(event);
				}
			});
		}
	}
}
