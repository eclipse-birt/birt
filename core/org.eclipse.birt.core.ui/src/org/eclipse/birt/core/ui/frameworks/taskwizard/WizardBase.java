/***********************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

import org.eclipse.birt.core.ui.frameworks.errordisplay.ErrorDialog;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IButtonHandler;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IRegistrationListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WizardBase implements IRegistrationListener {

	// HOLDS ALL TASKS ADDED TO THIS INVOCATION...THIS IS NOT A CACHE
	private transient LinkedHashMap<String, ITask> availableTasks = null;

	// HOLDS COLLECTION OF TASK IDS IN SEQUENCE...FOR INDEXING
	transient Vector<String> vTaskIDs = null;

	transient List<IButtonHandler> buttonList = null;

	List<IButtonHandler> tabToolButtonList = null;

	transient String sCurrentActiveTask = null;

	protected transient IWizardContext context = null;

	transient String sWizardID = ""; //$NON-NLS-1$

	transient Shell shellParent = null;

	// TRANSIENT STORAGE FOR ERRORS REPORTED BY TASKS USING 'errorDisplay()'
	private transient Object[] errorHints = null;

	private WizardBaseDialog dialog;

	// Internal fields to detect if wizard needs pack.
	boolean packNeeded = true;

	/**
	 * Indicates if wizard should be closed if Enter is pressed
	 */
	boolean bEnterClosed = true;

	/**
	 * Indicates if wizard is disposed or not.
	 */
	boolean isDisposed = false;

	/**
	 * Launches the wizard with the specified tasks in 'Available' state...and the
	 * specified task sets as the 'Active' task.
	 * 
	 * @param sTasks         Array of task IDs to add. Null indicates nothing added.
	 * @param topTaskId      Task to open at first. Null indicates the first task
	 *                       will be the top.
	 * @param initialContext Initial Context for the wizard
	 * @return Wizard Context
	 */
	public IWizardContext open(String[] sTasks, String topTaskId, IWizardContext initialContext) {
		// Update initial context
		context = initialContext;
		dialog.tmpTaskArray = sTasks;
		dialog.tmpTopTaskId = topTaskId;

		return dialog.open() == Window.OK ? this.context : null;
	}

	/**
	 * Launches the wizard with the first tasks in 'Available' state. Ensure the
	 * task is registered at first.
	 * 
	 * @param initialContext Initial Context for the wizard
	 * @return Wizard Context
	 */
	public IWizardContext open(IWizardContext initialContext) {
		return open(null, null, initialContext);
	}

	/**
	 * Sets the minimum size of the wizard
	 * 
	 * @param iWidth  width minimum
	 * @param iHeight height minimum
	 */
	public void setMinimumSize(int iWidth, int iHeight) {
		dialog.setMinimumSize(iWidth, iHeight);
	}

	public void firePageChanged(IDialogPage taskPage) {
		dialog.firePageChanged(new PageChangedEvent(dialog, taskPage));
	}

	/**
	 * Sets if wizard should be closed when Enter key is pressed. Default value is
	 * true.
	 * 
	 * @param bClosed true then close wizard when Enter key is pressed
	 * @since 2.3.1 and 2.5
	 */
	public void setWizardClosedWhenEnterPressed(boolean bClosed) {
		this.bEnterClosed = bClosed;
	}

	/**
	 * Adds a custom button after built-in buttons. This method must be invoked
	 * before invoking {@link #open(String[], String, IWizardContext)}
	 * 
	 * @param buttonHandler Custom button handler
	 */
	public void addCustomButton(IButtonHandler buttonHandler) {
		buttonList.add(buttonHandler);
	}

	protected List<IButtonHandler> getCustomButtons() {
		return buttonList;
	}

	/**
	 * Adds tab tool button.
	 * 
	 * @param buttonHandler
	 */
	public void addTabToolButton(IButtonHandler buttonHandler) {
		tabToolButtonList.add(buttonHandler);
	}

	/**
	 * Returns all tab tool buttons.
	 * 
	 * @return all tab tool buttons.
	 */
	public List<IButtonHandler> getTabToolButtons() {
		return tabToolButtonList;
	}

	public void addTask(String sTaskID) {
		ITask task = TasksManager.instance().getTask(sTaskID);
		if (task == null) {
			try {
				throw new RuntimeException("Task " + sTaskID + " is not registered!"); //$NON-NLS-1$ //$NON-NLS-2$
			} catch (RuntimeException e) {
				e.printStackTrace();
				return;
			}
		}
		// REGISTER WIZARDBASE INSTANCE WITH TASK
		task.setUIProvider(this);

		// DO NOT ADD DUPLICATE TASKS
		if (!vTaskIDs.contains(sTaskID)) {
			availableTasks.put(sTaskID, task);
			vTaskIDs.add(sTaskID);
		}
	}

	public void removeTask(String sTaskID) {
		// DO NOT ALLOW REMOVAL OF ALL TASKS!
		if (vTaskIDs.size() == 1) {
			// TODO: WE SHOULD THROW AN EXCEPTION HERE
			throw new RuntimeException(
					"There is only one task left in the wizard...you are not allowed to remove all tasks from a wizard!"); //$NON-NLS-1$
		}
		// REMOVE ALL REFERENCES TO THE TASK AND UPDATE ALL COLLECTION FIELDS
		if (availableTasks.containsKey(sTaskID)) {
			availableTasks.remove(sTaskID);
			int iTaskIndex = vTaskIDs.indexOf(sTaskID);
			vTaskIDs.remove(iTaskIndex);
			// SELECT THE FIRST TASK
			switchTo(vTaskIDs.get(0));
		}
	}

	public ITask getCurrentTask() {
		return availableTasks.get(sCurrentActiveTask);
	}

	public void switchTo(String sTaskID) {
		// Update the context from the current task...if available
		if (sCurrentActiveTask != null) {
			this.context = getCurrentTask().getContext();
		}

		// Update current active task ID
		sCurrentActiveTask = sTaskID;

		// Pass the errorHints if any have been reported by previous task
		if (errorHints != null) {
			getCurrentTask().setErrorHints(errorHints);
		}
		// Pass the context to the new task...so it can prepare its UI
		getCurrentTask().setContext(context);
		// Clear errorHints
		errorHints = null;
		ErrorsManager.instance().removeErrors();

		// Clear any existing popup
		detachPopup();

		// Switch UI
		try {
			dialog.switchTask();
		} catch (Exception e) {
			displayException(e);
		}
	}

	public Shell createPopupContainer() {
		return dialog.createPopupContainer();
	}

	public Shell getPopupContainer() {
		return dialog.getPopupContainer();
	}

	/**
	 * Attaches the popup window.
	 * 
	 * @param sPopupTitle popup title
	 */
	public void attachPopup(String sPopupTitle, int iWidth, int iHeight) {
		dialog.attachPopup(sPopupTitle, iWidth, iHeight);
	}

	public void detachPopup() {
		dialog.detachPopup();
	}

	public void updateContext(IWizardContext wizardcontext) {
		this.context = wizardcontext;
	}

	public WizardBase(String sID) {
		this(null, sID, SWT.DEFAULT, SWT.DEFAULT, null, null, null, null);
	}

	/**
	 * Creates an instance of the wizard. Needs to invoke <code>open</code> method
	 * to create the wizard dialog.
	 * 
	 * @param sID            wizard id
	 * @param iInitialWidth  width minimum
	 * @param iInitialHeight height minimum
	 * @param strTitle       wizard title
	 * @param imgTitle       wizard image
	 * @param strHeader      the header description
	 * @param imgHeader      image displayed in the task bar. If null, leave blank.
	 * *
	 * @deprecated To use
	 *             {@link #WizardBase(Shell, String, int, int, String, Image, String, Image)}
	 */
	public WizardBase(String sID, int iInitialWidth, int iInitialHeight, String strTitle, Image imgTitle,
			String strHeader, Image imgHeader) {
		this(null, sID, iInitialWidth, iInitialHeight, strTitle, imgTitle, strHeader, imgHeader);
	}

	/**
	 * Creates an instance of the wizard. Needs to invoke <code>open</code> method
	 * to create the wizard dialog.
	 * 
	 * @param parentShell    parent shell
	 * @param sID            wizard id
	 * @param iInitialWidth  width minimum
	 * @param iInitialHeight height minimum
	 * @param strTitle       wizard title
	 * @param imgTitle       wizard image
	 * @param strHeader      the header description
	 * @param imgHeader      image displayed in the task bar. If null, leave blank.
	 * @since 2.1.1
	 */
	public WizardBase(Shell parentShell, String sID, int iInitialWidth, int iInitialHeight, String strTitle,
			Image imgTitle, String strHeader, Image imgHeader) {
		this.shellParent = parentShell;
		this.sWizardID = sID;
		// Initialize tasks manager...so that extensions get processed if they
		// haven't already
		TasksManager.instance();
		// Initialize error manager
		ErrorsManager.instance();
		// Initialize instance variables
		availableTasks = new LinkedHashMap<String, ITask>();
		vTaskIDs = new Vector<String>();
		buttonList = new ArrayList<IButtonHandler>(1);
		tabToolButtonList = new ArrayList<IButtonHandler>(1);

		Shell shell = shellParent;
		if (shell == null) {
			shell = new Shell(Display.getCurrent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		}

		dialog = createDialog(shell, iInitialWidth, iInitialHeight, strTitle, imgTitle);

		// dialog.setMessage( strHeader );
		dialog.setTitleImage(imgHeader);
		ErrorsManager.instance().registerWizard(this);
	}

	protected WizardBaseDialog createDialog(Shell shell, int initialWidth, int initialHeight, String strTitle,
			Image imgTitle) {
		return new WizardBaseDialog(this, shell, initialWidth, initialHeight, strTitle, imgTitle);
	}

	public WizardBase() {
		this("org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase"); //$NON-NLS-1$
	}

	/**
	 * Clears the cached task instances. This can be used between invocations when a
	 * wizard instance is being reused in an application. Calling this will cause
	 * fresh instances of tasks to be fetched from the TasksManager when the wizard
	 * is invoked.
	 */
	public void clearCache() {
		// Reset all instance variables to clear cached task instances
		availableTasks.clear();
		vTaskIDs.clear();
		buttonList.clear();
	}

	/**
	 * Displays the exception in a common Error Display UI mechanism.
	 * 
	 * @param t exception to be displayed to the user
	 */
	public static void displayException(Throwable t) {
		new ErrorDialog(null, Messages.getString("WizardBase.error.ErrorsEncountered"), //$NON-NLS-1$
				Messages.getString("WizardBase.error.FollowingErrorEncountered"), //$NON-NLS-1$
				t);
	}

	/**
	 * Displays the exception in an Eclipse error mechanism.
	 * 
	 * @param t exception to be displayed to the user
	 */
	public static void showException(String errorMessage) {
		ErrorsManager.instance().showErrors(errorMessage);
	}

	/**
	 * Remove the error message in the dialog.
	 */
	public static void removeException() {
		ErrorsManager.instance().removeErrors();
	}

	public static String getErrors() {
		return ErrorsManager.instance().getErrors();
	}

	/**
	 * Displays the errors in a common Error Display UI mechanism. Also displayed
	 * are possible solutions to the problems. The user can also be given the option
	 * of switching to a different task where the fix needs to be made. (This is not
	 * implemented yet).
	 * 
	 * @param sErrors        Array of error strings
	 * @param sFixes         Array of strings listing possible solutions to above
	 *                       errors
	 * @param sTaskIDs       Array of task IDs which the user can switch to. The
	 *                       appropriate task labels should be indicated in the
	 *                       solutions to allow users to make the connection
	 * @param currentContext Updated IWizardContext instance...this instance will
	 *                       include the erroneous settings
	 * @param hints          Object array that will be passed to the target
	 *                       task...which can be used to indicate specific problems
	 *                       or to customize behavior of the task UI
	 */
	public void displayError(String[] sErrors, String[] sFixes, String[] sTaskIDs, IWizardContext currentContext,
			Object[] hints) {
		if (sErrors != null && sErrors.length > 0) {
			this.errorHints = hints;
			ErrorDialog dlg = new ErrorDialog(shellParent, Messages.getString("WizardBase.error.ErrorsEncountered"), //$NON-NLS-1$
					Messages.getString("WizardBase.error.FollowingErrorEncountered"), //$NON-NLS-1$
					sErrors, sFixes/* , currentContext, errorHints */);
			if (dlg.getOption() == ErrorDialog.OPTION_ACCEPT) {
				// TODO: FIX THE PROBLEM
			} else {
				// TODO: PROCEED WITHOUT FIXING THE PROBLEM
			}
		}
	}

	/**
	 * Notification method called by the
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.TasksManager instance when
	 * a new ITask instance is successfully registered. Default behavior is to do
	 * nothing.
	 * 
	 * @param sTaskID The ID for the newly registered task
	 */
	public void taskRegistered(String sTaskID) {
		// DO NOTHING...NEWLY REGISTERED TASKS DO NOT AFFECT AN EXISTING WIZARD
		// IN MOST CASES
	}

	/**
	 * Notification method called by the
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.TasksManager instance when
	 * an existing ITask instance is successfully deregistered. Default behavior is
	 * to do nothing. This can be overridden by individual wizards to handle
	 * deregistration of tasks currently available in the wizard.
	 * 
	 * @param sTaskID The ID for the deregistered task
	 */
	public void taskDeregistered(String sTaskID) {
		// DO NOTHING...IF EXISTING TASKS ARE DEREGISTERED, THEY WOULD NOT
		// DIRECTLY AFFECT A RUNNING WIZARD...
		// HOWEVER, CUSTOM WIZARDS MAY INTERPRET SUCH A DEREGISTRATION AS
		// INDICATING NON-AVAILABILITY OF SOME
		// FEATURE...AND COULD TAKE ACTION ACCORDINGLY.
	}

	/**
	 * Validates before pressing OK.
	 * 
	 * @return validation results
	 * 
	 */
	protected String[] validate() {
		return null;
	}

	public void dispose() {
		isDisposed = true;
		Iterator<ITask> tasks = availableTasks.values().iterator();
		while (tasks.hasNext()) {
			tasks.next().dispose();
		}
	}

	public boolean isDisposed() {
		return isDisposed;
	}

	protected TitleAreaDialog getDialog() {
		return dialog;
	}

	protected void setTitle(String wizardTitle) {
		if (dialog != null) {
			dialog.wizardTitle = wizardTitle;
			dialog.setTitle(wizardTitle);
		}
	}

	protected String getTitle() {
		return dialog.wizardTitle;
	}

	/**
	 * Packs the wizard to display enough size
	 * 
	 */
	public void packWizard() {
		dialog.packWizard();
	}

	/**
	 * The method makes user can do custom pack actions for current dialog.
	 * 
	 * @return <code>true</code> means custom pack has been done.
	 */
	protected boolean applyCustomPack() {
		return false;
	}

}
