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

package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IRegistrationListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.birt.core.ui.utils.UIHelper;

public class TasksManager {

	// Hashmap of registered tasks...sequence of registration is maintained
	private transient LinkedHashMap<String, TaskRegistrationEntry> registeredTasks = null;

	// Hashtable of registered wizards...sequence of registration is NOT
	// maintained
	private transient Hashtable<String, Vector<String>> registeredWizards = null;

	// Collection of registered event listeners (WizardBase implementations)
	private transient Vector<IRegistrationListener> registeredListeners = null;

	// Singleton Instance of TasksManager
	private static TasksManager thisInstance = null;

	/**
	 * This method returns the instance of TasksManager. If an instance does not
	 * exist, one is created.
	 * 
	 * @return Singleton instance of TasksManager
	 */
	public static TasksManager instance() {
		if (thisInstance == null) {
			thisInstance = new TasksManager();
		}
		return thisInstance;
	}

	// PRIVATE CONSTRUCTOR OF A SINGLETON
	private TasksManager() {
		registeredTasks = new LinkedHashMap<String, TaskRegistrationEntry>();
		registeredWizards = new Hashtable<String, Vector<String>>();
		registeredListeners = new Vector<IRegistrationListener>();
		processExtensions();
	}

	private void processExtensions() {
		// Actually process extensions
		if (UIHelper.isEclipseMode()) {
			// PROCESS 'tasks' EXTENSIONS
			IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.birt.core.ui", //$NON-NLS-1$
					"tasks").getExtensions(); //$NON-NLS-1$
			for (int iC = 0; iC < extensions.length; iC++) {
				IConfigurationElement[] elements = extensions[iC].getConfigurationElements();
				for (int i = 0; i < elements.length; i++) {
					try {
						String sID = elements[i].getAttribute("taskID"); //$NON-NLS-1$
						String strPriority = elements[i].getAttribute("priority"); //$NON-NLS-1$
						int priority = 0;
						try {
							priority = Integer.valueOf(strPriority);
						} catch (NumberFormatException ex) {
							priority = 0;
						}
						if (registeredTasks.containsKey(sID)) {
							TaskRegistrationEntry entry = registeredTasks.get(sID);
							if (entry.getPriority() >= priority) {
								// Always use the higher priority
								continue;
							}
						}
						ITask task = (ITask) elements[i].createExecutableExtension("classDefinition"); //$NON-NLS-1$
						TaskRegistrationEntry entry = new TaskRegistrationEntry(sID, task, priority);
						registeredTasks.put(sID, entry);
					} catch (FrameworkException e) {
						WizardBase.displayException(e);
					}
				}
			}
			// PROCESS 'taskWizards' EXTENSIONS
			extensions = Platform.getExtensionRegistry().getExtensionPoint("org.eclipse.birt.core.ui", //$NON-NLS-1$
					"taskWizards").getExtensions(); //$NON-NLS-1$
			for (int iC = 0; iC < extensions.length; iC++) {
				IConfigurationElement[] elements = extensions[iC].getConfigurationElements();
				for (int i = 0; i < elements.length; i++) {
					String sID = elements[i].getAttribute("wizardID"); //$NON-NLS-1$
					String sTaskList = elements[i].getAttribute("tasklist"); //$NON-NLS-1$
					String[] sTasks = new String[0];
					if (sTaskList != null) {
						sTasks = sTaskList.split(","); //$NON-NLS-1$
					}
					if (registeredWizards.containsKey(sID)) {
						String sInsertionKey = elements[i].getAttribute("positionBefore"); //$NON-NLS-1$
						// An ID for a task included in this wizard before which
						// the tasks specified by 'tasklist' are to be inserted.
						// This can also be an integer index on which the tasks
						// will be inserted. Default value is blank, which means
						// inserting to the last.
						int insertIndex;
						try {
							insertIndex = Integer.parseInt(sInsertionKey);
						} catch (NumberFormatException e) {
							insertIndex = -1;
						}
						Vector<String> vTemp = registeredWizards.get(sID);
						// IF INSERTION KEY IS SPECIFIED
						if (sInsertionKey != null && sInsertionKey.trim().length() > 0 && insertIndex < 0) {
							int iInsertionPosition = registeredWizards.get(sID).indexOf(sInsertionKey);
							// IF INSERTION KEY MATCHES A LOCATION IN WIZARD'S
							// EXISTING TASK LIST
							if (iInsertionPosition != -1) {
								for (int iTaskIndex = 0; iTaskIndex < sTasks.length; iTaskIndex++) {
									vTemp.add(iInsertionPosition + iTaskIndex, sTasks[iTaskIndex].trim());
								}
								continue;
							}
						}
						registeredWizards.put(sID, addAllTasks(vTemp, sTasks, insertIndex));
					} else {
						if (sTaskList != null && sTaskList.trim().length() > 0) {
							registeredWizards.put(sID, addAllTasks(new Vector<String>(), sTasks, -1));
						} else {
							registeredWizards.put(sID, new Vector<String>());
						}
					}
				}
			}
		} else {
			// DO NOTHING...REGISTRATION SHOULD BE DONE THROUGH API WHEN RUNNING
			// OUTSIDE OF ECLIPSE
		}
	}

	private Vector<String> addAllTasks(Vector<String> vTemp, String[] sTasks, int insertIndex) {
		// IF INSERTION KEY IS NOT SPECIFIED OR IS NOT FOUND...ADD ALL TASKS TO
		// THE END OF EXISTING TASK LIST
		for (int iTaskIndex = 0; iTaskIndex < sTasks.length; iTaskIndex++) {
			if (insertIndex >= 0) {
				// Insert to specified index
				vTemp.add(iTaskIndex + insertIndex, sTasks[iTaskIndex].trim());
			} else {
				// Insert to last
				vTemp.add(sTasks[iTaskIndex].trim());
			}
		}
		return vTemp;
	}

	private void updateWizard(String sWizardID, String sTasks, String sPosition) {
		Vector<String> vTaskList = new Vector<String>();
		if (registeredWizards.containsKey(sWizardID)) {
			vTaskList = registeredWizards.get(sWizardID);
		}
		if (sTasks != null && sTasks.trim().length() > 0) {
			// TODO: Use the position indicator to rearrange tasks in list
			String[] sTaskArr = sTasks.split(","); //$NON-NLS-1$
			for (int i = 0; i < sTaskArr.length; i++) {
				vTaskList.add(sTaskArr[i]);
			}
		}
		registeredWizards.put(sWizardID, vTaskList);
	}

	/**
	 * This method registers a task with the TasksManager. It throws an exception if
	 * the task ID is already in use or if the ITask instance is null.
	 * 
	 * @param sTaskID The unique identifier with which the task is to be registered
	 * @param task    The ITask instance that represents the Wizard UI for the task
	 * @throws IllegalArgumentException if taskID is not unique or if task argument
	 *                                  is null
	 */
	public void registerTask(String sTaskID, ITask task) throws IllegalArgumentException {
		if (!registeredTasks.containsKey(sTaskID) && task != null) {
			registeredTasks.put(sTaskID, new TaskRegistrationEntry(sTaskID, task, 0));
			fireTaskRegisteredEvent(sTaskID);
		} else {
			throw new IllegalArgumentException(Messages.getFormattedString("TasksManager.Exception.RegisterTask", //$NON-NLS-1$
					sTaskID));
		}
	}

	/**
	 * This method removes a registered task from the TasksManager. It throws an
	 * exception if the task ID is not found.
	 * 
	 * @param sTaskID The unique identifier of the task that is to be deregistered
	 * @throws IllegalArgumentException if task with specified ID is not registered
	 */
	public void deregisterTask(String sTaskID) throws IllegalArgumentException {
		if (registeredTasks.containsKey(sTaskID)) {
			registeredTasks.remove(sTaskID);
			fireTaskDeregisteredEvent(sTaskID);
		} else {
			throw new IllegalArgumentException(Messages.getFormattedString("TasksManager.Exception.DeregisterTask", //$NON-NLS-1$
					sTaskID));
		}
	}

	/**
	 * This method registers a wizard with the TasksManager. It throws an exception
	 * if the WizardID instance is null.
	 * 
	 * @param sWizardID The unique identifier of the wizard
	 * @param sTasks    A comma separated list of TaskIDs that specify tasks to be
	 *                  automatically added to the wizard on invocation
	 * @param sPosition A TaskID before which the above list of tasks should be
	 *                  inserted in the wizard
	 * @throws IllegalArgumentException if WizardID is null
	 */
	public void registerWizard(String sWizardID, String sTasks, String sPosition) throws IllegalArgumentException {
		if (sWizardID != null) {
			updateWizard(sWizardID, sTasks, sPosition);
		} else {
			throw new IllegalArgumentException(Messages.getString("TasksManager.Excepion.RegisterWizard")); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the ITask instance registered with the specified ID.
	 * 
	 * @param sTaskID The ID uniquely identifying the task to be obtained
	 * @return the task currently registered with the specified ID
	 */
	public ITask getTask(String sTaskID) {
		if (!isRegistered(sTaskID)) {
			return null;
		}
		return registeredTasks.get(sTaskID).getClassDefinition();
	}

	/**
	 * Returns the tasks (in the correct order) registered for use with the
	 * specified wizard. If a wizard with such an ID has not been registered, an
	 * empty array is returned.
	 * 
	 * @param sWizardID The ID uniquely identifying the wizard whose tasks are to be
	 *                  returned
	 * @return an array of task IDs currently registered for use with the specified
	 *         wizard
	 */
	public String[] getTasksForWizard(String sWizardID) {
		if (registeredWizards.containsKey(sWizardID)) {
			Vector<String> vTemp = registeredWizards.get(sWizardID);
			String[] sTasks = new String[vTemp.size()];
			for (int iTaskCount = 0; iTaskCount < vTemp.size(); iTaskCount++) {
				sTasks[iTaskCount] = vTemp.get(iTaskCount);
			}
			return sTasks;
		}
		return new String[] {};
	}

	/**
	 * Returns whether or not a task has been registered with the specified ID. This
	 * can be used to determine if an ID being used for a task is actually unique
	 * before attempting to register it.
	 * 
	 * @param sTaskID The ID which is to be checked.
	 * @return true if there exists a task registered with the specified ID, false
	 *         otherwise
	 */
	public boolean isRegistered(String sTaskID) {
		boolean b = registeredTasks.containsKey(sTaskID);
		return b;
	}

	/**
	 * Adds a listener to be notified of registration events.
	 * 
	 * @param listener Instance of IRegistrationListener that should be notified on
	 *                 events
	 */
	public void addRegistrationListener(IRegistrationListener listener) {
		registeredListeners.add(listener);
	}

	/**
	 * Removes a registered listener. This listener will no longer recieve
	 * notification of registration events.
	 * 
	 * @param listener Instance of IRegistrationListener that should be removed
	 */
	public void removeRegistrationListener(IRegistrationListener listener) {
		registeredListeners.remove(listener);
	}

	// SENDS REGISTRATION NOTIFICATION TO ALL REGISTERED LISTENERS
	private void fireTaskRegisteredEvent(String sTaskID) {
		for (int i = 0; i < registeredListeners.size(); i++) {
			registeredListeners.get(i).taskRegistered(sTaskID);
		}
	}

	// SENDS DEREGISTRATION NOTIFICATION TO ALL REGISTERED LISTENERS
	private void fireTaskDeregisteredEvent(String sTaskID) {
		for (int i = 0; i < registeredListeners.size(); i++) {
			registeredListeners.get(i).taskDeregistered(sTaskID);
		}
	}
}
