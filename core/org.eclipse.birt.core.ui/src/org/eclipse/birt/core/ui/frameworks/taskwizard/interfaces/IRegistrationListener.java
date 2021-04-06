package org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces;

public interface IRegistrationListener {
	/**
	 * Notification method...called when a new task is successfully registered with
	 * the TasksManager.
	 * 
	 * @param sTaskID ID of the newly registered task.
	 */
	public void taskRegistered(String sTaskID);

	/**
	 * Notification method...called when a task is successfully deregistered from
	 * the TasksManager.
	 * 
	 * @param sTaskID ID of the deregistered task.
	 */
	public void taskDeregistered(String sTaskID);
}