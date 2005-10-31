
package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Vector;

import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IRegistrationListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.PlatformUI;

public class TasksManager
{

	// Hashmap of registered tasks...sequence of registration is maintained
	private transient LinkedHashMap registeredTasks = null;

	// Hashtable of registered wizards...sequence of registration is NOT
	// maintained
	private transient Hashtable registeredWizards = null;

	// Collection of registered event listeners (WizardBase implementations)
	private transient Vector registeredListeners = null;

	// Singleton Instance of TasksManager
	private static TasksManager thisInstance = null;

	/**
	 * This method returns the instance of TasksManager. If an instance does not
	 * exist, one is created.
	 * 
	 * @return Singleton instance of TasksManager
	 */
	public static TasksManager instance( )
	{
		if ( thisInstance == null )
		{
			thisInstance = new TasksManager( );
		}
		return thisInstance;
	}

	// PRIVATE CONSTRUCTOR OF A SINGLETON
	private TasksManager( )
	{
		registeredTasks = new LinkedHashMap( );
		registeredWizards = new Hashtable( );
		registeredListeners = new Vector( );
		processExtensions( );
	}

	private void processExtensions( )
	{
		// TODO: Actually process extensions
		if ( PlatformUI.isWorkbenchRunning( ) )
		{
			// PROCESS 'tasks' EXTENSIONS
			IConfigurationElement[] elements = Platform.getExtensionRegistry( )
					.getConfigurationElementsFor( "org.eclipse.birt.core.ui.tasks" ); //$NON-NLS-1$
			for ( int i = 0; i < elements.length; i++ )
			{
				try
				{
					String sID = elements[i].getAttribute( "taskID" ); //$NON-NLS-1$
					ITask task = (ITask) elements[i].createExecutableExtension( "classDefinition" ); //$NON-NLS-1$
					registeredTasks.put( sID, task );
				}
				catch ( CoreException e )
				{
					e.printStackTrace( );
				}
			}
			// PROCESS 'taskWizards' EXTENSIONS
			elements = Platform.getExtensionRegistry( )
					.getConfigurationElementsFor( "org.eclipse.birt.core.ui.taskWizards" ); //$NON-NLS-1$
			for ( int i = 0; i < elements.length; i++ )
			{
				String sID = elements[i].getAttribute( "wizardID" ); //$NON-NLS-1$
				String sTaskList = elements[i].getAttribute( "tasklist" ); //$NON-NLS-1$
				String[] sTasks = sTaskList.split( "," ); //$NON-NLS-1$
				if ( registeredWizards.containsKey( sID ) )
				{
					String sInsertionKey = elements[i].getAttribute( "positionBefore" ); //$NON-NLS-1$
					Vector vTemp = (Vector) registeredWizards.get( sID );
					// IF INSERTION KEY IS SPECIFIED
					if ( sInsertionKey != null
							&& sInsertionKey.trim( ).length( ) > 0 )
					{
						int iInsertionPosition = ( (Vector) registeredWizards.get( sID ) ).indexOf( sInsertionKey );
						// IF INSERTION KEY MATCHES A LOCATION IN WIZARD'S
						// EXISTING TASK LIST
						if ( iInsertionPosition != -1 )
						{
							for ( int iTaskIndex = 0; iTaskIndex < sTasks.length; iTaskIndex++ )
							{
								vTemp.add( iInsertionPosition + iTaskIndex,
										sTasks[iTaskIndex].trim( ) );
							}
							continue;
						}
					}
					registeredWizards.put( sID, addAllTasks( vTemp, sTasks ) );
				}
				else
				{
					if ( sTaskList != null && sTaskList.trim( ).length( ) > 0 )
					{
						registeredWizards.put( sID, addAllTasks( new Vector( ),
								sTasks ) );
					}
					else
					{
						registeredWizards.put( sID, new Vector( ) );
					}
				}
			}
		}
		else
		{
			// DO NOTHING...REGISTRATION SHOULD BE DONE THROUGH API WHEN RUNNING
			// OUTSIDE OF ECLIPSE
		}
	}

	private Vector addAllTasks( Vector vTemp, String[] sTasks )
	{
		// IF INSERTION KEY IS NOT SPECIFIED OR IS NOT FOUND...ADD ALL TASKS TO
		// THE END OF EXISTING TASK LIST
		for ( int iTaskIndex = 0; iTaskIndex < sTasks.length; iTaskIndex++ )
		{
			vTemp.add( sTasks[iTaskIndex].trim( ) );
		}
		return vTemp;
	}

	private void updateWizard( String sWizardID, String sTasks, String sPosition )
	{
		Vector vTaskList = new Vector( );
		if ( registeredWizards.containsKey( sWizardID ) )
		{
			vTaskList = (Vector) registeredWizards.get( sWizardID );
		}
		if ( sTasks != null && sTasks.trim( ).length( ) > 0 )
		{
			// TODO: Use the position indicator to rearrange tasks in list
			String[] sTaskArr = sTasks.split( "," ); //$NON-NLS-1$
			for ( int i = 0; i < sTaskArr.length; i++ )
			{
				vTaskList.add( sTaskArr[i] );
			}
		}
		registeredWizards.put( sWizardID, vTaskList );
	}

	/**
	 * This method registers a task with the TasksManager. It throws an
	 * exception if the task ID is already in use or if the ITask instance is
	 * null.
	 * 
	 * @param sTaskID
	 *            The unique identifier with which the task is to be registered
	 * @param task
	 *            The ITask instance that represents the Wizard UI for the task
	 * @throws IllegalArgumentException
	 *             if taskID is not unique or if task argument is null
	 */
	public void registerTask( String sTaskID, ITask task )
			throws IllegalArgumentException
	{
		if ( !registeredTasks.containsKey( sTaskID ) && task != null )
		{
			registeredTasks.put( sTaskID, task );
			fireTaskRegisteredEvent( sTaskID );
		}
		else
		{
			throw new IllegalArgumentException( Messages.getFormattedString( "TasksManager.Exception.RegisterTask", //$NON-NLS-1$
					sTaskID ) );
		}
	}

	/**
	 * This method removes a registered task from the TasksManager. It throws an
	 * exception if the task ID is not found.
	 * 
	 * @param sTaskID
	 *            The unique identifier of the task that is to be deregistered
	 * @throws IllegalArgumentException
	 *             if task with specified ID is not registered
	 */
	public void deregisterTask( String sTaskID )
			throws IllegalArgumentException
	{
		if ( registeredTasks.containsKey( sTaskID ) )
		{
			registeredTasks.remove( sTaskID );
			fireTaskDeregisteredEvent( sTaskID );
		}
		else
		{
			throw new IllegalArgumentException( Messages.getFormattedString( "TasksManager.Exception.DeregisterTask", //$NON-NLS-1$
					sTaskID ) );
		}
	}

	/**
	 * This method registers a wizard with the TasksManager. It throws an
	 * exception if the WizardID instance is null.
	 * 
	 * @param sWizardID
	 *            The unique identifier of the wizard
	 * @param sTasks
	 *            A comma separated list of TaskIDs that specify tasks to be
	 *            automatically added to the wizard on invocation
	 * @param sPosition
	 *            A TaskID before which the above list of tasks should be
	 *            inserted in the wizard
	 * @throws IllegalArgumentException
	 *             if WizardID is null
	 */
	public void registerWizard( String sWizardID, String sTasks,
			String sPosition ) throws IllegalArgumentException
	{
		if ( sWizardID != null )
		{
			updateWizard( sWizardID, sTasks, sPosition );
		}
		else
		{
			throw new IllegalArgumentException( Messages.getString( "TasksManager.Excepion.RegisterWizard" ) ); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the ITask instance registered with the specified ID.
	 * 
	 * @param sTaskID
	 *            The ID uniquely identifying the task to be obtained
	 * @return the task currently registered with the specified ID
	 */
	public ITask getTask( String sTaskID )
	{
		if ( !isRegistered( sTaskID ) )
		{
			return null;
		}
		return (ITask) registeredTasks.get( sTaskID );
	}

	/**
	 * Returns the tasks (in the correct order) registered for use with the
	 * specified wizard. If a wizard with such an ID has not been registered, an
	 * empty array is returned.
	 * 
	 * @param sWizardID
	 *            The ID uniquely identifying the wizard whose tasks are to be
	 *            returned
	 * @return an array of task IDs currently registered for use with the
	 *         specified wizard
	 */
	public String[] getTasksForWizard( String sWizardID )
	{
		if ( registeredWizards.containsKey( sWizardID ) )
		{
			Vector vTemp = (Vector) registeredWizards.get( sWizardID );
			String[] sTasks = new String[vTemp.size( )];
			for ( int iTaskCount = 0; iTaskCount < vTemp.size( ); iTaskCount++ )
			{
				sTasks[iTaskCount] = vTemp.get( iTaskCount ).toString( );
			}
			return sTasks;
		}
		return new String[]{};
	}

	/**
	 * Returns whether or not a task has been registered with the specified ID.
	 * This can be used to determine if an ID being used for a task is actually
	 * unique before attempting to register it.
	 * 
	 * @param sTaskID
	 *            The ID which is to be checked.
	 * @return true if there exists a task registered with the specified ID,
	 *         false otherwise
	 */
	public boolean isRegistered( String sTaskID )
	{
		boolean b = registeredTasks.containsKey( sTaskID );
		return b;
	}

	/**
	 * Adds a listener to be notified of registration events.
	 * 
	 * @param listener
	 *            Instance of IRegistrationListener that should be notified on
	 *            events
	 */
	public void addRegistrationListener( IRegistrationListener listener )
	{
		registeredListeners.add( listener );
	}

	/**
	 * Removes a registered listener. This listener will no longer recieve
	 * notification of registration events.
	 * 
	 * @param listener
	 *            Instance of IRegistrationListener that should be removed
	 */
	public void removeRegistrationListener( IRegistrationListener listener )
	{
		registeredListeners.remove( listener );
	}

	// SENDS REGISTRATION NOTIFICATION TO ALL REGISTERED LISTENERS
	private void fireTaskRegisteredEvent( String sTaskID )
	{
		for ( int i = 0; i < registeredListeners.size( ); i++ )
		{
			( (IRegistrationListener) registeredListeners.get( i ) ).taskRegistered( sTaskID );
		}
	}

	// SENDS DEREGISTRATION NOTIFICATION TO ALL REGISTERED LISTENERS
	private void fireTaskDeregisteredEvent( String sTaskID )
	{
		for ( int i = 0; i < registeredListeners.size( ); i++ )
		{
			( (IRegistrationListener) registeredListeners.get( i ) ).taskDeregistered( sTaskID );
		}
	}
}