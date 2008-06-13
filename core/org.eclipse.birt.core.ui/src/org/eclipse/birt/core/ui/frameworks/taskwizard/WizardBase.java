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
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class WizardBase implements IRegistrationListener
{

	// HOLDS ALL TASKS ADDED TO THIS INVOCATION...THIS IS NOT A CACHE
	private transient LinkedHashMap<String, ITask> availableTasks = null;

	// HOLDS COLLECTION OF TASK IDS IN SEQUENCE...FOR INDEXING
	private transient Vector<String> vTaskIDs = null;

	private transient List<IButtonHandler> buttonList = null;

	private transient String sCurrentActiveTask = null;

	protected transient IWizardContext context = null;

	private transient String sWizardID = ""; //$NON-NLS-1$

	private transient Shell shellParent = null;

	// TRANSIENT STORAGE FOR ERRORS REPORTED BY TASKS USING 'errorDisplay()'
	private transient Object[] errorHints = null;

	private WizardBaseDialog dialog;

	// Internal fields to detect if wizard needs pack.
	private boolean packNeeded = true;
	
	/**
	 * Indicates if wizard should be closed if Enter is pressed
	 */
	private boolean bEnterClosed = true;

	/**
	 * Launches the wizard with the specified tasks in 'Available' state...and
	 * the specified task sets as the 'Active' task.
	 * 
	 * @param sTasks
	 *            Array of task IDs to add. Null indicates nothing added.
	 * @param topTaskId
	 *            Task to open at first. Null indicates the first task will be
	 *            the top.
	 * @param initialContext
	 *            Initial Context for the wizard
	 * @return Wizard Context
	 */
	public IWizardContext open( String[] sTasks, String topTaskId,
			IWizardContext initialContext )
	{
		// Update initial context
		context = initialContext;
		dialog.tmpTaskArray = sTasks;
		dialog.tmpTopTaskId = topTaskId;

		return dialog.open( ) == Window.OK ? this.context : null;
	}

	/**
	 * Launches the wizard with the first tasks in 'Available' state. Ensure the
	 * task is registered at first.
	 * 
	 * @param initialContext
	 *            Initial Context for the wizard
	 * @return Wizard Context
	 */
	public IWizardContext open( IWizardContext initialContext )
	{
		return open( null, null, initialContext );
	}

	/**
	 * Sets the minimum size of the wizard
	 * 
	 * @param iWidth
	 *            width minimum
	 * @param iHeight
	 *            height minimum
	 */
	public void setMinimumSize( int iWidth, int iHeight )
	{
		dialog.setMinimumSize( iWidth, iHeight );
	}

	public void firePageChanged( IDialogPage taskPage )
	{
		dialog.firePageChanged( new PageChangedEvent( dialog, taskPage ) );
	}
	
	/**
	 * Sets if wizard should be closed when Enter key is pressed. Default value
	 * is true.
	 * 
	 * @param bClosed
	 *            true then close wizard when Enter key is pressed
	 * @since 2.3.1 and 2.5
	 */
	public void setWizardClosedWhenEnterPressed( boolean bClosed )
	{
		this.bEnterClosed = bClosed;
	}

	/**
	 * Adds a custom button after built-in buttons. This method must be invoked
	 * before invoking {@link #open(String[], String, IWizardContext)}
	 * 
	 * @param buttonHandler
	 *            Custom button handler
	 */
	public void addCustomButton( IButtonHandler buttonHandler )
	{
		buttonList.add( buttonHandler );
	}

	protected List<IButtonHandler> getCustomButtons( )
	{
		return buttonList;
	}

	public void addTask( String sTaskID )
	{
		ITask task = TasksManager.instance( ).getTask( sTaskID );
		if ( task == null )
		{
			try
			{
				throw new RuntimeException( "Task " + sTaskID + " is not registered!" ); //$NON-NLS-1$ //$NON-NLS-2$
			}
			catch ( RuntimeException e )
			{
				e.printStackTrace( );
				return;
			}
		}
		// REGISTER WIZARDBASE INSTANCE WITH TASK
		task.setUIProvider( this );

		// DO NOT ADD DUPLICATE TASKS
		if ( !vTaskIDs.contains( sTaskID ) )
		{
			availableTasks.put( sTaskID, task );
			vTaskIDs.add( sTaskID );
		}
	}

	public void removeTask( String sTaskID )
	{
		// DO NOT ALLOW REMOVAL OF ALL TASKS!
		if ( vTaskIDs.size( ) == 1 )
		{
			// TODO: WE SHOULD THROW AN EXCEPTION HERE
			throw new RuntimeException( "There is only one task left in the wizard...you are not allowed to remove all tasks from a wizard!" ); //$NON-NLS-1$
		}
		// REMOVE ALL REFERENCES TO THE TASK AND UPDATE ALL COLLECTION FIELDS
		if ( availableTasks.containsKey( sTaskID ) )
		{
			availableTasks.remove( sTaskID );
			int iTaskIndex = vTaskIDs.indexOf( sTaskID );
			vTaskIDs.remove( iTaskIndex );
			// SELECT THE FIRST TASK
			switchTo( vTaskIDs.get( 0 ) );
		}
	}

	public ITask getCurrentTask( )
	{
		return availableTasks.get( sCurrentActiveTask );
	}

	public void switchTo( String sTaskID )
	{
		// Update the context from the current task...if available
		if ( sCurrentActiveTask != null )
		{
			this.context = getCurrentTask( ).getContext( );
		}

		// Update current active task ID
		sCurrentActiveTask = sTaskID;

		// Pass the errorHints if any have been reported by previous task
		if ( errorHints != null )
		{
			getCurrentTask( ).setErrorHints( errorHints );
		}
		// Pass the context to the new task...so it can prepare its UI
		getCurrentTask( ).setContext( context );
		// Clear errorHints
		errorHints = null;
		ErrorsManager.instance( ).removeErrors( );

		// Clear any existing popup
		detachPopup( );

		// Switch UI
		try
		{
			dialog.switchTask( );
		}
		catch ( Exception e )
		{
			displayException( e );
		}
	}

	public Shell createPopupContainer( )
	{
		return dialog.createPopupContainer( );
	}

	public Shell getPopupContainer( )
	{
		return dialog.getPopupContainer( );
	}

	/**
	 * Attaches the popup window.
	 * 
	 * @param sPopupTitle
	 *            popup title
	 */
	public void attachPopup( String sPopupTitle, int iWidth, int iHeight )
	{
		dialog.attachPopup( sPopupTitle, iWidth, iHeight );
	}

	public void detachPopup( )
	{
		dialog.detachPopup( );
	}

	public void updateContext( IWizardContext wizardcontext )
	{
		this.context = wizardcontext;
	}

	public WizardBase( String sID )
	{
		this( null, sID, SWT.DEFAULT, SWT.DEFAULT, null, null, null, null );
	}

	/**
	 * Creates an instance of the wizard. Needs to invoke <code>open</code>
	 * method to create the wizard dialog.
	 * 
	 * @param sID
	 *            wizard id
	 * @param iInitialWidth
	 *            width minimum
	 * @param iInitialHeight
	 *            height minimum
	 * @param strTitle
	 *            wizard title
	 * @param imgTitle
	 *            wizard image
	 * @param strHeader
	 *            the header description
	 * @param imgHeader
	 *            image displayed in the task bar. If null, leave blank. *
	 * @deprecated To use
	 *             {@link #WizardBase(Shell, String, int, int, String, Image, String, Image)}
	 */
	public WizardBase( String sID, int iInitialWidth, int iInitialHeight,
			String strTitle, Image imgTitle, String strHeader, Image imgHeader )
	{
		this( null,
				sID,
				iInitialWidth,
				iInitialHeight,
				strTitle,
				imgTitle,
				strHeader,
				imgHeader );
	}

	/**
	 * Creates an instance of the wizard. Needs to invoke <code>open</code>
	 * method to create the wizard dialog.
	 * 
	 * @param parentShell
	 *            parent shell
	 * @param sID
	 *            wizard id
	 * @param iInitialWidth
	 *            width minimum
	 * @param iInitialHeight
	 *            height minimum
	 * @param strTitle
	 *            wizard title
	 * @param imgTitle
	 *            wizard image
	 * @param strHeader
	 *            the header description
	 * @param imgHeader
	 *            image displayed in the task bar. If null, leave blank.
	 * @since 2.1.1
	 */
	public WizardBase( Shell parentShell, String sID, int iInitialWidth,
			int iInitialHeight, String strTitle, Image imgTitle,
			String strHeader, Image imgHeader )
	{
		this.shellParent = parentShell;
		this.sWizardID = sID;
		// Initialize tasks manager...so that extensions get processed if they
		// haven't already
		TasksManager.instance( );
		// Initialize error manager
		ErrorsManager.instance( );
		// Initialize instance variables
		availableTasks = new LinkedHashMap<String, ITask>( );
		vTaskIDs = new Vector<String>( );
		buttonList = new ArrayList<IButtonHandler>( 1 );

		Shell shell = shellParent;
		if ( shell == null )
		{
			shell = new Shell( Display.getCurrent( ), SWT.DIALOG_TRIM
					| SWT.RESIZE
					| SWT.APPLICATION_MODAL );
		}

		dialog = new WizardBaseDialog( shell,
				iInitialWidth,
				iInitialHeight,
				strTitle,
				imgTitle );

		// dialog.setMessage( strHeader );
		dialog.setTitleImage( imgHeader );
		ErrorsManager.instance( ).registerWizard( this );
	}

	public WizardBase( )
	{
		this( "org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase" ); //$NON-NLS-1$
	}

	/**
	 * Clears the cached task instances. This can be used between invocations
	 * when a wizard instance is being reused in an application. Calling this
	 * will cause fresh instances of tasks to be fetched from the TasksManager
	 * when the wizard is invoked.
	 */
	public void clearCache( )
	{
		// Reset all instance variables to clear cached task instances
		availableTasks.clear( );
		vTaskIDs.clear( );
		buttonList.clear( );
	}

	/**
	 * Displays the exception in a common Error Display UI mechanism.
	 * 
	 * @param t
	 *            exception to be displayed to the user
	 */
	public static void displayException( Throwable t )
	{
		new ErrorDialog( null,
				Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
				Messages.getString( "WizardBase.error.FollowingErrorEncountered" ), //$NON-NLS-1$
				t );
	}

	/**
	 * Displays the exception in an Eclipse error mechanism.
	 * 
	 * @param t
	 *            exception to be displayed to the user
	 */
	public static void showException( String errorMessage )
	{
		ErrorsManager.instance( ).showErrors( errorMessage );
	}

	/**
	 * Remove the error message in the dialog.
	 */
	public static void removeException( )
	{
		ErrorsManager.instance( ).removeErrors( );
	}

	public static String getErrors( )
	{
		return ErrorsManager.instance( ).getErrors( );
	}

	/**
	 * Displays the errors in a common Error Display UI mechanism. Also
	 * displayed are possible solutions to the problems. The user can also be
	 * given the option of switching to a different task where the fix needs to
	 * be made. (This is not implemented yet).
	 * 
	 * @param sErrors
	 *            Array of error strings
	 * @param sFixes
	 *            Array of strings listing possible solutions to above errors
	 * @param sTaskIDs
	 *            Array of task IDs which the user can switch to. The
	 *            appropriate task labels should be indicated in the solutions
	 *            to allow users to make the connection
	 * @param currentContext
	 *            Updated IWizardContext instance...this instance will include
	 *            the erroneous settings
	 * @param hints
	 *            Object array that will be passed to the target task...which
	 *            can be used to indicate specific problems or to customize
	 *            behavior of the task UI
	 */
	public void displayError( String[] sErrors, String[] sFixes,
			String[] sTaskIDs, IWizardContext currentContext, Object[] hints )
	{
		if ( sErrors != null && sErrors.length > 0 )
		{
			this.errorHints = hints;
			ErrorDialog dlg = new ErrorDialog( shellParent,
					Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
					Messages.getString( "WizardBase.error.FollowingErrorEncountered" ), //$NON-NLS-1$
					sErrors,
					sFixes/* , currentContext, errorHints */);
			if ( dlg.getOption( ) == ErrorDialog.OPTION_ACCEPT )
			{
				// TODO: FIX THE PROBLEM
			}
			else
			{
				// TODO: PROCEED WITHOUT FIXING THE PROBLEM
			}
		}
	}

	/**
	 * Notification method called by the
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.TasksManager instance
	 * when a new ITask instance is successfully registered. Default behavior is
	 * to do nothing.
	 * 
	 * @param sTaskID
	 *            The ID for the newly registered task
	 */
	public void taskRegistered( String sTaskID )
	{
		// DO NOTHING...NEWLY REGISTERED TASKS DO NOT AFFECT AN EXISTING WIZARD
		// IN MOST CASES
	}

	/**
	 * Notification method called by the
	 * org.eclipse.birt.frameworks.taskwizard.interfaces.TasksManager instance
	 * when an existing ITask instance is successfully deregistered. Default
	 * behavior is to do nothing. This can be overridden by individual wizards
	 * to handle deregistration of tasks currently available in the wizard.
	 * 
	 * @param sTaskID
	 *            The ID for the deregistered task
	 */
	public void taskDeregistered( String sTaskID )
	{
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
	protected String[] validate( )
	{
		return null;
	}

	public void dispose( )
	{
		Iterator<ITask> tasks = availableTasks.values( ).iterator( );
		while ( tasks.hasNext( ) )
		{
			tasks.next( ).dispose( );
		}
	}

	protected BirtTitleAreaDialog getDialog( )
	{
		return dialog;
	}

	protected void setTitle( String wizardTitle )
	{
		dialog.wizardTitle = wizardTitle;
		if ( dialog != null )
		{
			dialog.setTitle( wizardTitle );
		}
	}

	protected String getTitle( )
	{
		return dialog.wizardTitle;
	}

	/**
	 * Packs the wizard to display enough size
	 * 
	 */
	public void packWizard( )
	{
		dialog.packWizard( );
	}

	final class WizardBaseDialog extends BirtTitleAreaDialog implements
			SelectionListener,
			ControlListener,
			DisposeListener,
			IPageChangeProvider
	{

		private ListenerList pageChangedListeners = new ListenerList( );

		private transient CTabFolder cmpTaskContainer;

		private transient int iWizardHeightMinimum = 100;

		private transient int iWizardWidthMinimum = 100;

		private transient String wizardTitle = "Task Wizard"; //$NON-NLS-1$

		private transient Image imgShell = null;

		private transient Shell shellPopup = null;

		private transient String[] tmpTaskArray;
		private transient String tmpTopTaskId;

		public WizardBaseDialog( Shell parentShell, int iInitialWidth,
				int iInitialHeight, String strTitle, Image imgTitle )
		{
			super( parentShell, iInitialWidth - 100 );
			setHelpAvailable( true );

			this.iWizardWidthMinimum = iInitialWidth;
			this.iWizardHeightMinimum = iInitialHeight;
			this.wizardTitle = strTitle;
			this.imgShell = imgTitle;
		}

		protected void setShellStyle( int newShellStyle )
		{
			super.setShellStyle( newShellStyle
					| SWT.DIALOG_TRIM
					| SWT.RESIZE
					| SWT.APPLICATION_MODAL );
		}

		private void configureTaskContext( String[] sTasks, String topTaskId )
		{
			// Add tasks
			String[] allTasks = TasksManager.instance( )
					.getTasksForWizard( WizardBase.this.sWizardID );
			// ADD DEFAULT TASKS AS DEFINED BY EXTENSIONS
			for ( int i = 0; i < allTasks.length; i++ )
			{
				addTask( allTasks[i] );
			}
			// ADD TASKS SPECIFIED DURING INVOCATION
			if ( sTasks != null && sTasks.length > 0 )
			{
				for ( int i = 0; i < sTasks.length; i++ )
				{
					if ( !vTaskIDs.contains( sTasks[i] ) )
					{
						addTask( sTasks[i] );
					}
				}
			}

			// Open the specified task
			if ( topTaskId == null )
			{
				if ( vTaskIDs.size( ) > 0 )
				{
					sCurrentActiveTask = vTaskIDs.get( 0 ).toString( );
				}
			}
			else
			{
				assert vTaskIDs.contains( topTaskId );
				sCurrentActiveTask = topTaskId;
			}
		}

		protected void initializeBounds( )
		{
			// Set shell properties
			getShell( ).setText( wizardTitle );
			setTitle( wizardTitle );
			if ( imgShell != null )
			{
				getShell( ).setImage( imgShell );
			}
			getShell( ).addControlListener( this );
			getShell( ).addDisposeListener( this );

			// Add each task to container
			String[] allTasks = TasksManager.instance( )
					.getTasksForWizard( WizardBase.this.sWizardID );
			for ( int i = 0; i < allTasks.length; i++ )
			{
				// Create the blank tab item.
				CTabItem item = new CTabItem( getTabContainer( ), SWT.NONE );
				item.setImage( TasksManager.instance( )
						.getTask( allTasks[i] )
						.getImage( ) );
				item.setText( TasksManager.instance( )
						.getTask( allTasks[i] )
						.getTitle( ) );
				item.setData( allTasks[i] );
			}

			if ( tmpTopTaskId != null )
			{
				int taskIndex = vTaskIDs.indexOf( tmpTopTaskId );
				cmpTaskContainer.setSelection( taskIndex );
			}

			// Open current task
			if ( getCurrentTask( ) != null )
			{
				getCurrentTask( ).setContext( WizardBase.this.context );

				// Do not pack wizard since the bound has been calculated by
				// jface
				packNeeded = false;
				switchTo( sCurrentActiveTask );
				packNeeded = true;
			}

			super.initializeBounds( );
		}

		public void create( )
		{
			configureTaskContext( tmpTaskArray, tmpTopTaskId );
			super.create( );
		}

		protected Control createDialogArea( Composite parent )
		{
			// create the top level composite for the dialog area
			Composite composite = new Composite( parent, SWT.NONE );
			{
				GridLayout layout = new GridLayout( );
				layout.marginHeight = 0;
				layout.marginWidth = 0;
				layout.verticalSpacing = 0;
				layout.horizontalSpacing = 0;
				composite.setLayout( layout );
				composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
				composite.setFont( parent.getFont( ) );
			}

			Label lblSeparator = new Label( composite, SWT.SEPARATOR
					| SWT.HORIZONTAL );
			lblSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			// Initialize and layout UI components of the framework
			cmpTaskContainer = new CTabFolder( composite, SWT.TOP | SWT.FLAT );
			{
				cmpTaskContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
				cmpTaskContainer.setTabHeight( 25 );
				// cmpTaskContainer.setSimple( false );
				cmpTaskContainer.addSelectionListener( this );
			}

			lblSeparator = new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL );
			lblSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			return composite;
		}

		protected void createButtonsForButtonBar( Composite parent )
		{
			createButton( parent,
					IDialogConstants.BACK_ID,
					Messages.getString( "WizardBase.Back" ), //$NON-NLS-1$
					false );
			createButton( parent,
					IDialogConstants.NEXT_ID,
					Messages.getString( "WizardBase.Next" ),//$NON-NLS-1$
					false );
			createButton( parent,
					IDialogConstants.FINISH_ID,
					Messages.getString( "WizardBase.Finish" ),//$NON-NLS-1$
					bEnterClosed );
			createButton( parent,
					IDialogConstants.CANCEL_ID,
					Messages.getString( "WizardBase.Cancel" ),//$NON-NLS-1$
					false );

			for ( int i = 0; i < buttonList.size( ); i++ )
			{
				IButtonHandler buttonHandler = buttonList.get( i );
				// Make sure the same id was not registered.
				assert getButton( buttonHandler.getId( ) ) == null;
				buttonHandler.setButton( createButton( parent,
						buttonHandler.getId( ),
						buttonHandler.getLabel( ),
						false ) );
			}

			// Update buttons status
			int taskIndex = vTaskIDs.indexOf( sCurrentActiveTask );
			if ( taskIndex > 0 )
			{
				getButton( IDialogConstants.BACK_ID ).setEnabled( true );
			}
			else
			{
				getButton( IDialogConstants.BACK_ID ).setEnabled( false );
			}
			if ( taskIndex < vTaskIDs.size( ) - 1 )
			{
				getButton( IDialogConstants.NEXT_ID ).setEnabled( true );
			}
			else
			{
				getButton( IDialogConstants.NEXT_ID ).setEnabled( false );
			}
		}

		protected void buttonPressed( int buttonId )
		{
			if ( IDialogConstants.FINISH_ID == buttonId )
			{
				okPressed( );
			}
			else if ( IDialogConstants.CANCEL_ID == buttonId )
			{
				cancelPressed( );
			}
			else if ( IDialogConstants.BACK_ID == buttonId )
			{
				backPressed( );
			}
			else if ( IDialogConstants.NEXT_ID == buttonId )
			{
				nextPressed( );
			}

			for ( int i = 0; i < buttonList.size( ); i++ )
			{
				IButtonHandler buttonHandler = buttonList.get( i );
				if ( buttonId == buttonHandler.getId( ) )
				{
					buttonHandler.run( );
					break;
				}
			}
		}

		void switchTask( )
		{
			// Set the description for each task
			String strDesc = getCurrentTask( ).getDescription( );
			if ( strDesc != null )
			{
				setMessage( strDesc );
			}

			// Update or create UI
			if ( getTabContainer( ).getSelectionIndex( ) < 0 )
			{
				getTabContainer( ).setSelection( 0 );
			}
			CTabItem currentItem = getTabContainer( ).getItem( getTabContainer( ).getSelectionIndex( ) );
			getCurrentTask( ).createControl( getTabContainer( ) );
			if ( currentItem.getControl( ) == null )
			{
				currentItem.setControl( getCurrentTask( ).getControl( ) );
			}

			// Pack every task to show as much as possible
			packWizard( );

			// Notify page changed to refresh help page
			firePageChanged( new PageChangedEvent( this, getCurrentTask( ) ) );
		}

		private void backPressed( )
		{
			int i = vTaskIDs.indexOf( WizardBase.this.sCurrentActiveTask );
			if ( i > 0 )
			{
				cmpTaskContainer.setSelection( i - 1 );
				switchTo( vTaskIDs.get( i - 1 ) );
				getButton( IDialogConstants.NEXT_ID ).setEnabled( true );
			}
			if ( i == 1 )
			{
				// Just switched to first tab
				getButton( IDialogConstants.BACK_ID ).setEnabled( false );
			}
		}

		private void nextPressed( )
		{
			int i = vTaskIDs.indexOf( WizardBase.this.sCurrentActiveTask );
			if ( i < vTaskIDs.size( ) - 1 )
			{
				cmpTaskContainer.setSelection( i + 1 );
				switchTo( vTaskIDs.get( i + 1 ) );
				getButton( IDialogConstants.BACK_ID ).setEnabled( true );
			}
			if ( i == vTaskIDs.size( ) - 2 )
			{
				getButton( IDialogConstants.NEXT_ID ).setEnabled( false );
			}
		}

		protected void okPressed( )
		{
			final String[] saMessages = validate( );
			if ( saMessages != null && saMessages.length > 0 )
			{
				ErrorDialog ed = new ErrorDialog( shellParent,
						Messages.getString( "WizardBase.error.ErrorsEncountered" ),//$NON-NLS-1$
						Messages.getString( "WizardBase.error.FollowingErrorsReportedWhileVerifying" ), //$NON-NLS-1$
						saMessages,
						new String[]{} );
				if ( ed.getOption( ) == ErrorDialog.OPTION_ACCEPT )
				{
					// Stop quitting to fix manually
					return;
				}
			}
			super.okPressed( );
		}

		/**
		 * Sets the minimum size of the wizard
		 * 
		 * @param iWidth
		 *            width minimum
		 * @param iHeight
		 *            height minimum
		 */
		public void setMinimumSize( int iWidth, int iHeight )
		{
			iWizardWidthMinimum = iWidth;
			iWizardHeightMinimum = iHeight;
		}

		public Shell createPopupContainer( )
		{
			// CLEAR ANY EXISTING POPUP
			if ( shellPopup != null && !shellPopup.isDisposed( ) )
			{
				shellPopup.dispose( );
			}
			// CREATE AND DISPLAY THE NEW POPUP
			if ( shellPopup == null || shellPopup.isDisposed( ) )
			{
				// Make the popup modal on the Linux platform. See
				// bugzilla#123386
				int shellStyle = SWT.DIALOG_TRIM | SWT.RESIZE;
				shellPopup = new Shell( getShell( ), shellStyle );
				shellPopup.setLayout( new FillLayout( ) );
			}
			return shellPopup;
		}

		public Shell getPopupContainer( )
		{
			return shellPopup;
		}

		/**
		 * Attaches the popup window.
		 * 
		 * @param sPopupTitle
		 *            '&' will be removed for accelerator key, if the popup
		 *            title is from the control text.
		 */
		public void attachPopup( String sPopupTitle, int iWidth, int iHeight )
		{
			shellPopup.setText( sPopupTitle );
			// IF PREFERRED SIZE IS SPECIFIED USE IT...ELSE USE PACK
			if ( iWidth != -1 && iHeight != -1 )
			{
				shellPopup.setSize( iWidth, iHeight );
			}
			else
			{
				shellPopup.pack( );
			}
			setPopupLocation( );
			shellPopup.open( );
		}

		public void detachPopup( )
		{
			if ( shellPopup != null && !shellPopup.isDisposed( ) )
			{
				shellPopup.close( );
			}
		}

		/**
		 * Packs the wizard to display enough size
		 * 
		 */
		public void packWizard( )
		{
			if ( !packNeeded )
			{
				return;
			}
			boolean changed = false;
			Point wizardSize = getShell( ).computeSize( SWT.DEFAULT,
					SWT.DEFAULT );
			int iWizardWidth = Math.max( wizardSize.x, iWizardWidthMinimum );
			int iWizardHeight = Math.max( wizardSize.y, iWizardHeightMinimum );
			Point oldSize = getShell( ).getSize( );
			if ( oldSize.x < iWizardWidth )
			{
				oldSize.x = iWizardWidth;
				changed = true;
			}
			if ( oldSize.y < iWizardHeight )
			{
				oldSize.y = iWizardHeight;
				changed = true;
			}
			if ( changed )
			{
				getShell( ).setSize( oldSize );
				getShell( ).layout( );
			}
		}

		CTabFolder getTabContainer( )
		{
			return cmpTaskContainer;
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( e.getSource( ) instanceof CTabFolder )
			{
				String taskId = (String) e.item.getData( );
				int indexLabel = vTaskIDs.indexOf( taskId );
				if ( indexLabel >= 0 )
				{
					switchTo( taskId );
					getButton( IDialogConstants.NEXT_ID ).setEnabled( indexLabel < vTaskIDs.size( ) - 1 );
					getButton( IDialogConstants.BACK_ID ).setEnabled( indexLabel > 0 );
				}
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
		 */
		public void controlMoved( ControlEvent e )
		{
			setPopupLocation( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
		 */
		public void controlResized( ControlEvent e )
		{
			setPopupLocation( );
		}

		private void setPopupLocation( )
		{
			if ( shellPopup != null && !shellPopup.isDisposed( ) )
			{
				int x = 0;
				if ( getShell( ).getLocation( ).x
						+ getShell( ).getSize( ).x
						+ shellPopup.getSize( ).x > getShell( ).getDisplay( )
						.getClientArea( ).width )
				{
					// Avoid the popup exceeds the right border of the display
					// area
					x = getShell( ).getDisplay( ).getClientArea( ).width
							- shellPopup.getSize( ).x;
				}
				else
				{
					x = getShell( ).getLocation( ).x + getShell( ).getSize( ).x;
				}
				shellPopup.setLocation( x, getShell( ).getLocation( ).y + 20 );
			}
		}

		public void widgetDisposed( DisposeEvent e )
		{
			WizardBase.this.dispose( );
		}

		public void addPageChangedListener( IPageChangedListener listener )
		{
			pageChangedListeners.add( listener );
		}

		public Object getSelectedPage( )
		{
			return getCurrentTask( );
		}

		public void removePageChangedListener( IPageChangedListener listener )
		{
			pageChangedListeners.remove( listener );
		}

		/**
		 * Notifies any selection changed listeners that the selected page has
		 * changed. Only listeners registered at the time this method is called
		 * are notified.
		 * 
		 * @param event
		 *            a selection changed event
		 * 
		 * @see IPageChangedListener#pageChanged
		 * 
		 * @since 2.1
		 */
		private void firePageChanged( final PageChangedEvent event )
		{
			Object[] listeners = pageChangedListeners.getListeners( );
			for ( int i = 0; i < listeners.length; i++ )
			{
				final IPageChangedListener l = (IPageChangedListener) listeners[i];
				SafeRunnable.run( new SafeRunnable( ) {

					public void run( )
					{
						l.pageChanged( event );
					}
				} );
			}
		}
	}
}
