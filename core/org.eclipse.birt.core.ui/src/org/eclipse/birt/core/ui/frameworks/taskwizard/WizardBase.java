
package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Vector;

import org.eclipse.birt.core.ui.frameworks.errordisplay.ErrorDialog;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IRegistrationListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class WizardBase
		implements
			IRegistrationListener,
			SelectionListener,
			ControlListener,
			DisposeListener
{

	// HOLDS ALL TASKS ADDED TO THIS INVOCATION...THIS IS NOT A CACHE
	private transient LinkedHashMap availableTasks = null;

	// HOLDS COLLECTION OF TASK LABELS IN SEQUENCE...INDEX OF LABEL MATCHES
	// CORRESPONDING INDEX OF TASK ID IN vTaskIDs
	private transient Vector vTaskLabels = null;

	// HOLDS COLLECTION OF TASK IDS IN SEQUENCE...FOR INDEXING
	private transient Vector vTaskIDs = null;

	private transient Display display = null;

	private transient Shell shell = null;

	private transient Shell shellPopup = null;

	private transient TaskList tasklist = null;

	private transient Composite cmpTaskContainer = null;

	private transient StackLayout slTaskContainer = null;

	private transient ButtonPanel buttonpanel = null;

	private transient String sCurrentActiveTask = null;

	protected transient IWizardContext context = null;

	private transient String sWizardID = "org.eclipse.birt.framework.taskwizard.prototype.SampleWizard"; //$NON-NLS-1$

	private transient int iWizardHeight = 400;

	private transient int iWizardWidth = 500;

	private transient String wizardTitle = "Task Wizard"; //$NON-NLS-1$

	private transient Image wizardImage = null;

	// TRANSIENT STORAGE FOR ERRORS REPORTED BY TASKS USING 'errorDisplay()'
	private transient Object[] errorHints = null;

	/**
	 * Launches the wizard with the specified tasks in 'Available' state...and
	 * the first task set as the 'Active' task.
	 * 
	 * @param sTasks
	 *            Array of task IDs
	 * @param initialContext
	 *            Initial Context for the wizard
	 * @return Wizard Context
	 */
	public IWizardContext open( String[] sTasks, IWizardContext initialContext )
	{
		// Update initial context
		context = initialContext;

		// Initialize UI elelents
		GridLayout glShell = new GridLayout( );
		glShell.numColumns = 1;
		glShell.marginHeight = 5;
		glShell.marginWidth = 5;

		display = Display.getDefault( );
		if ( PlatformUI.isWorkbenchRunning( ) )
		{
			shell = new Shell( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ), SWT.DIALOG_TRIM
					| SWT.RESIZE | SWT.APPLICATION_MODAL );
		}
		else
		{
			shell = new Shell( display, SWT.DIALOG_TRIM
					| SWT.RESIZE | SWT.APPLICATION_MODAL );
		}
		// Set shell properties
		shell.setLayout( glShell );
		shell.setSize( iWizardWidth, iWizardHeight );
		shell.setLocation( ( display.getClientArea( ).width / 2 - ( iWizardWidth / 2 ) ),
				( display.getClientArea( ).height / 2 ) - ( iWizardHeight / 2 ) );
		shell.setText( wizardTitle );
		if ( wizardImage != null )
		{
			shell.setImage( wizardImage );
		}
		shell.addControlListener( this );

		// Initialize and layout UI components of the framework
		tasklist = new TaskList( shell, SWT.NONE, this );
		tasklist.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		placeComponents( );
		buttonpanel = new ButtonPanel( shell, SWT.NONE, this );

		// Add tasks
		String[] allTasks = TasksManager.instance( )
				.getTasksForWizard( sWizardID );
		if ( allTasks.length > 0 )
		{
			buttonpanel.setButtonEnabled( ButtonPanel.NEXT, true );
		}
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
		switchTo( (String) vTaskIDs.get( 0 ) );
		sCurrentActiveTask = vTaskIDs.get( 0 ).toString( );
		shell.addDisposeListener( this );
		shell.open( );
		while ( !shell.isDisposed( ) )
		{
			if ( !display.readAndDispatch( ) )
			{
				display.sleep( );
			}
		}
		return this.context;
	}

	public void enable( String sTaskID )
	{
		// TODO: Handle enabling of a task
	}

	public void disable( String sTaskID )
	{
		// TODO: Handle disabling of a task
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
		String sLabel = TasksManager.instance( )
				.getTask( sTaskID )
				.getDisplayLabel( Locale.getDefault( ) );
		// DO NOT ADD DUPLICATE TASKS
		if ( !vTaskIDs.contains( sTaskID ) )
		{
			availableTasks.put( sTaskID, task );
			vTaskLabels.add( sLabel );
			vTaskIDs.add( sTaskID );
			tasklist.addTask( sLabel );
			cmpTaskContainer.layout( );
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
			String sTaskLabel = (String) vTaskLabels.elementAt( iTaskIndex );
			vTaskIDs.remove( iTaskIndex );
			vTaskLabels.remove( iTaskIndex );
			tasklist.removeTask( sTaskLabel );
			// SELECT THE FIRST TASK
			switchTo( (String) vTaskIDs.get( 0 ) );
		}
	}

	public ITask getCurrentTask( )
	{
		return (ITask) availableTasks.get( sCurrentActiveTask );
	}

	public void switchTo( String sTaskID )
	{
		// Update the context from the current task...if available
		if ( sCurrentActiveTask != null )
		{
			String[] sErrors = getCurrentTask( ).getErrors( );
			if ( sErrors != null && sErrors.length > 0 )
			{
				ErrorDialog ed = new ErrorDialog( Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
						Messages.getString( "WizardBase.error.FollowingErrorsReportedByTask" ), //$NON-NLS-1$
						sErrors,
						new String[]{} );
				if ( ed.getOption( ) == ErrorDialog.OPTION_ACCEPT )
				{
					// Pressing OK will retain current task
					return;
				}
			}
			this.context = getCurrentTask( ).getContext( );
		}
		// Clear any existing popup
		if ( shellPopup != null && !shellPopup.isDisposed( ) )
		{
			shellPopup.close( );
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
		// Get the new UI and display it
		Control c = getCurrentTask( ).getUI( cmpTaskContainer );
		slTaskContainer.topControl = c;
		cmpTaskContainer.layout( );
		tasklist.setActive( (String) vTaskLabels.get( vTaskIDs.indexOf( sTaskID ) ) );

		// TODO: Handle enabling / disabling of Buttons
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
			shellPopup = new Shell( shell, SWT.DIALOG_TRIM | SWT.RESIZE );
			shellPopup.setLayout( new FillLayout( ) );
			shellPopup.setLocation( ( shell.getLocation( ).x + shell.getSize( ).x ),
					( shell.getLocation( ).y + 20 ) );
		}
		return shellPopup;
	}

	public Shell getPopupContainer( )
	{
		return shellPopup;
	}

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
		shellPopup.open( );
	}

	public void detachPopup( Shell parent )
	{
		shellPopup.close( );
	}

	public void updateContext( IWizardContext wizardcontext )
	{
		this.context = wizardcontext;
	}

	public WizardBase( String sID )
	{
		this.sWizardID = sID;
		// Initialize tasks manager...so that extensions get processed if they
		// haven't already
		TasksManager.instance( );
		// Initialize instance variables
		availableTasks = new LinkedHashMap( );
		vTaskLabels = new Vector( );
		vTaskIDs = new Vector( );
	}

	public WizardBase( String sID, int iInitialWidth, int iInitialHeight,
			String wizardTitle, Image wizardImage )
	{
		this( sID );
		this.iWizardWidth = iInitialWidth;
		this.iWizardHeight = iInitialHeight;
		this.wizardTitle = wizardTitle;
		this.wizardImage = wizardImage;
	}

	public WizardBase( )
	{
		// Initialize tasks manager...so that extensions get processed if they
		// haven't already
		TasksManager.instance( );
		// Initialize instance variables
		availableTasks = new LinkedHashMap( );
		vTaskLabels = new Vector( );
		vTaskIDs = new Vector( );
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
		vTaskLabels.clear( );
	}

	/**
	 * Displays the exception in a common Error Display UI mechanism.
	 * 
	 * @param t
	 *            exception to be displayed to the user
	 */
	public void displayException( Throwable t )
	{
		// TODO: Implement linkage with the ErrorDialog
		new ErrorDialog( Messages.getString( "WizardBase.error.ExceptionEncountered" ), //$NON-NLS-1$
				Messages.getString( "WizardBase.error.FollowingExceptionEncountered" ), //$NON-NLS-1$
				t );
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
			ErrorDialog dlg = new ErrorDialog( Messages.getString( "WizardBase.error.ExceptionEncountered" ), //$NON-NLS-1$
					Messages.getString( "WizardBase.error.FollowingExceptionEncountered" ), //$NON-NLS-1$
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

	public void setSize( int iWidth, int iHeight )
	{
		iWizardWidth = iWidth;
		iWizardHeight = iHeight;
	}

	private void placeComponents( )
	{
		GridLayout glTest = new GridLayout( );
		glTest.numColumns = 1;
		glTest.marginHeight = 10;
		glTest.marginWidth = 20;
		cmpTaskContainer = new Canvas( shell, SWT.NONE );
		slTaskContainer = new StackLayout( );
		cmpTaskContainer.setLayout( slTaskContainer );
		cmpTaskContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ) instanceof Button )
		{
			String sCmd = ( (Button) e.getSource( ) ).getText( );
			if ( sCmd.equals( Messages.getString( "WizardBase.Next" ) ) ) //$NON-NLS-1$
			{
				int i = vTaskIDs.indexOf( this.sCurrentActiveTask );
				if ( i < vTaskIDs.size( ) - 1 )
				{
					this.switchTo( (String) vTaskIDs.get( i + 1 ) );
					buttonpanel.setButtonEnabled( ButtonPanel.BACK, true );
				}
				if ( i == vTaskIDs.size( ) - 2 )
				{
					buttonpanel.setButtonEnabled( ButtonPanel.NEXT, false );
				}
			}
			else if ( sCmd.equals( Messages.getString( "WizardBase.Back" ) ) ) //$NON-NLS-1$
			{
				int i = vTaskIDs.indexOf( this.sCurrentActiveTask );
				if ( i > 0 )
				{
					this.switchTo( (String) vTaskIDs.get( i - 1 ) );
					buttonpanel.setButtonEnabled( ButtonPanel.NEXT, true );
				}
				if ( i == 1 )
				{
					// Just switched to first tab
					buttonpanel.setButtonEnabled( ButtonPanel.BACK, false );
				}
			}
			else if ( sCmd.equals( Messages.getString( "WizardBase.Ok" ) ) ) //$NON-NLS-1$
			{
				final String[] saMessages = validate( );
				if ( saMessages != null && saMessages.length > 0 )
				{
					ErrorDialog ed = new ErrorDialog( Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
							Messages.getString( "WizardBase.error.FollowingErrorsReportedWhileVerifying" ), //$NON-NLS-1$
							saMessages,
							new String[]{} );
					if ( ed.getOption( ) == ErrorDialog.OPTION_ACCEPT )
					{
						// Stop quitting to fix manually
						return;
					}
				}
				shell.dispose( );
			}
			else if ( sCmd.equals( Messages.getString( "WizardBase.Cancel" ) ) ) //$NON-NLS-1$
			{
				context = null;
				shell.dispose( );
			}
			else
			{
				switchTo( (String) vTaskIDs.get( vTaskLabels.indexOf( sCmd ) ) );
				int i = vTaskIDs.indexOf( this.sCurrentActiveTask );
				buttonpanel.setButtonEnabled( ButtonPanel.NEXT,
						( i < vTaskIDs.size( ) - 1 ) ? true : false );
				buttonpanel.setButtonEnabled( ButtonPanel.BACK, ( i > 0 )
						? true : false );
			}
		}
	}

	/**
	 * Validates before pressing OK.
	 * 
	 * @return validation results
	 * 
	 */
	public String[] validate( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlMoved( ControlEvent e )
	{
		if ( shellPopup != null && !shellPopup.isDisposed( ) )
		{
			shellPopup.setLocation( ( shell.getLocation( ).x + shell.getSize( ).x ),
					( shell.getLocation( ).y + 20 ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt.events.ControlEvent)
	 */
	public void controlResized( ControlEvent e )
	{
		if ( shellPopup != null && !shellPopup.isDisposed( ) )
		{
			shellPopup.setLocation( ( shell.getLocation( ).x + shell.getSize( ).x ),
					( shell.getLocation( ).y + 20 ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e )
	{
		// TODO Add cleanup code here...including removal of adapters
	}
}

class TaskList extends Composite implements DisposeListener
{

	private transient Vector vTasks = null;
	private transient WizardBase wb = null;

	public TaskList( Composite parent, int iStyle, WizardBase wb )
	{
		super( parent, iStyle );
		vTasks = new Vector( 5, 2 );
		this.wb = wb;
		placeComponents( );
	}

	public void addTask( String sTaskLabel )
	{
		vTasks.add( sTaskLabel );
		addButton( );
	}

	public void removeTask( String sTaskLabel )
	{
		vTasks.remove( sTaskLabel );
		findButton( sTaskLabel, true );
	}

	public void insertTask( int iTaskIndex, String sTaskLabel )
	{
		vTasks.add( iTaskIndex, sTaskLabel );
	}

	public void setActive( String sTaskLabel )
	{
		// Disable the button with current task
		Control[] c = this.getChildren( );
		for ( int i = 0; i < c.length; i++ )
		{
			if ( c[i] instanceof Button )
			{
				if ( ( (Button) c[i] ).getText( ).equals( sTaskLabel ) )
				{
					c[i].setEnabled( false );
				}
				else
				{
					c[i].setEnabled( true );
				}
			}
		}
	}

	private int findButton( String sTaskLabel, boolean bRemove )
	{
		Control[] c = this.getChildren( );
		for ( int i = 0; i < c.length; i++ )
		{
			if ( c[i] instanceof Button )
			{
				if ( ( (Button) c[i] ).getText( ).equals( sTaskLabel ) )
				{
					if ( bRemove )
					{
						c[i].dispose( );
						this.layout( );
					}
					return i;
				}
			}
		}
		return -1;
	}

	private void addButton( )
	{
		Button btnTask = new Button( this, SWT.FLAT | SWT.NO_FOCUS );
		btnTask.setText( (String) vTasks.get( vTasks.size( ) - 1 ) );
		btnTask.addSelectionListener( wb );
		btnTask.setLayoutData( new RowData( 100, 30 ) );
	}

	private void placeComponents( )
	{
		RowLayout rlTasks = new RowLayout( SWT.HORIZONTAL );
		rlTasks.marginHeight = 10;
		rlTasks.marginWidth = 10;
		rlTasks.spacing = 5;

		this.setLayout( rlTasks );

		this.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e )
	{

	}
}

class ButtonPanel extends Composite
{

	private transient WizardBase wb = null;

	private transient Button btnPrevious = null;
	private transient Button btnNext = null;
	private transient Button btnAccept = null;
	private transient Button btnCancel = null;

	public static final int BACK = 0;
	public static final int NEXT = 1;
	public static final int ACCEPT = 2;
	public static final int CANCEL = 3;

	/**
	 * @param parent
	 * @param style
	 */
	public ButtonPanel( Composite parent, int style, WizardBase wb )
	{
		super( parent, style );
		this.wb = wb;
		placeComponents( );
	}

	private void placeComponents( )
	{
		RowLayout rlButtons = new RowLayout( SWT.HORIZONTAL );
		rlButtons.marginHeight = 10;
		rlButtons.marginWidth = 10;
		rlButtons.spacing = 5;
		rlButtons.wrap = false;
		rlButtons.justify = false;

		this.setLayout( rlButtons );
		this.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		btnPrevious = new Button( this, SWT.FLAT );
		btnPrevious.setText( Messages.getString( "WizardBase.Back" ) ); //$NON-NLS-1$
		btnPrevious.addSelectionListener( wb );
		btnPrevious.setLayoutData( new RowData( 70, 30 ) );
		// DISABLED INITIALLY
		btnPrevious.setEnabled( false );

		btnNext = new Button( this, SWT.FLAT );
		btnNext.setText( Messages.getString( "WizardBase.Next" ) ); //$NON-NLS-1$
		btnNext.addSelectionListener( wb );
		btnNext.setLayoutData( new RowData( 70, 30 ) );
		// DISABLED INITIALLY
		btnNext.setEnabled( false );

		btnAccept = new Button( this, SWT.FLAT );
		btnAccept.setText( Messages.getString( "WizardBase.Ok" ) ); //$NON-NLS-1$
		btnAccept.addSelectionListener( wb );
		btnAccept.setLayoutData( new RowData( 70, 30 ) );

		btnCancel = new Button( this, SWT.FLAT );
		btnCancel.setText( Messages.getString( "WizardBase.Cancel" ) ); //$NON-NLS-1$
		btnCancel.addSelectionListener( wb );
		btnCancel.setLayoutData( new RowData( 70, 30 ) );
	}

	void setButtonEnabled( int iButton, boolean bState )
	{
		switch ( iButton )
		{
			case 0 :
				btnPrevious.setEnabled( bState );
				break;
			case 1 :
				btnNext.setEnabled( bState );
				break;
			case 2 :
				btnAccept.setEnabled( bState );
				break;
			case 3 :
				btnCancel.setEnabled( bState );
				break;
		}
	}
}