
package org.eclipse.birt.core.ui.frameworks.taskwizard;

import java.util.LinkedHashMap;
import java.util.Vector;

import org.eclipse.birt.core.ui.frameworks.errordisplay.ErrorDialog;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IRegistrationListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.birt.core.ui.i18n.Messages;
import org.eclipse.jface.resource.JFaceResources;
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
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.ibm.icu.util.ULocale;

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

	private transient CTabFolder cmpTaskContainer = null;

	private transient ButtonPanel buttonpanel = null;

	private transient String sCurrentActiveTask = null;

	protected transient IWizardContext context = null;

	private transient String sWizardID = "org.eclipse.birt.framework.taskwizard.prototype.SampleWizard"; //$NON-NLS-1$

	private transient int iWizardHeightMinimum = 100;

	private transient int iWizardWidthMinimum = 100;

	private transient String wizardTitle = "Task Wizard"; //$NON-NLS-1$

	private transient String strHeader = ""; //$NON-NLS-1$

	private transient Label lblHeader;

	private transient Image imgShell = null;

	private transient Image imgHeader = null;

	private transient Shell shellParent = null;

	// TRANSIENT STORAGE FOR ERRORS REPORTED BY TASKS USING 'errorDisplay()'
	private transient Object[] errorHints = null;

	private transient boolean bWasCancelled = true;

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

		// Initialize UI elements
		GridLayout glShell = new GridLayout( );
		glShell.marginHeight = 0;
		glShell.marginWidth = 0;
		glShell.verticalSpacing = 0;

		display = Display.getDefault( );
		if ( shellParent == null )
		{
			shell = new Shell( display, SWT.DIALOG_TRIM
					| SWT.RESIZE | SWT.APPLICATION_MODAL );
		}
		else
		{
			shell = new Shell( shellParent, SWT.DIALOG_TRIM
					| SWT.RESIZE | SWT.APPLICATION_MODAL );
		}

		// Set shell properties
		shell.setLayout( glShell );
		shell.setSize( iWizardWidthMinimum, iWizardHeightMinimum );
		shell.setText( wizardTitle );
		if ( imgShell != null )
		{
			shell.setImage( imgShell );
		}
		shell.addControlListener( this );

		placeComponents( );

		// Add tasks
		String[] allTasks = TasksManager.instance( )
				.getTasksForWizard( sWizardID );
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
				if ( vTaskIDs.size( ) > 1 )
				{
					buttonpanel.setButtonEnabled( ButtonPanel.NEXT, true );
				}
			}
		}
		else
		{
			assert vTaskIDs.contains( topTaskId );
			sCurrentActiveTask = topTaskId;
			int taskIndex = vTaskIDs.indexOf( topTaskId );
			cmpTaskContainer.setSelection( taskIndex );
			if ( taskIndex > 0 )
			{
				buttonpanel.setButtonEnabled( ButtonPanel.BACK, true );
			}
			if ( taskIndex < vTaskIDs.size( ) - 1 )
			{
				buttonpanel.setButtonEnabled( ButtonPanel.NEXT, true );
			}
		}

		if ( getCurrentTask( ) != null )
		{
			getCurrentTask( ).setContext( initialContext );
			switchTo( sCurrentActiveTask );
		}

		shell.setLocation( ( display.getClientArea( ).width / 2 - ( shell.getSize( ).x / 2 ) ),
				( display.getClientArea( ).height / 2 )
						- ( shell.getSize( ).y / 2 ) );

		shell.addDisposeListener( this );
		shell.open( );
		while ( !shell.isDisposed( ) )
		{
			if ( !display.readAndDispatch( ) )
			{
				display.sleep( );
			}
		}

		if ( !bWasCancelled )
		{
			return this.context;
		}
		return null;
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
		String sLabel = task.getDisplayLabel( ULocale.getDefault( ) );

		// Create the blank tab item. UI is only created after the first
		// initializtion.
		CTabItem item = new CTabItem( cmpTaskContainer, SWT.NONE );
		item.setText( sLabel );

		// DO NOT ADD DUPLICATE TASKS
		if ( !vTaskIDs.contains( sTaskID ) )
		{
			availableTasks.put( sTaskID, task );
			vTaskLabels.add( sLabel );
			vTaskIDs.add( sTaskID );
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
			vTaskIDs.remove( iTaskIndex );
			vTaskLabels.remove( iTaskIndex );
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
				ErrorDialog ed = new ErrorDialog( shellParent,
						Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
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
		detachPopup( );

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

		// Set the description for each task
		String strDesc = getCurrentTask( ).getDescription( ULocale.getDefault( ) );
		if ( strDesc != null )
		{
			lblHeader.setText( strDesc );
		}
		
		// Update or create UI
		if ( cmpTaskContainer.getSelectionIndex( ) < 0 )
		{
			cmpTaskContainer.setSelection( 0 );
		}
		CTabItem currentItem = cmpTaskContainer.getItem( cmpTaskContainer.getSelectionIndex( ) );
		Composite cmpTask = getCurrentTask( ).getUI( cmpTaskContainer );
		if ( currentItem.getControl( ) == null )
		{
			currentItem.setControl( cmpTask );
		}
		cmpTask.layout( );

		// Pack every task to show as much as possible
		packWizard( );
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
			// Make the popup modal on the Linux platform. See bugzilla#123386
			int shellStyle = SWT.DIALOG_TRIM | SWT.RESIZE;
			if ( SWT.getPlatform( ).indexOf( "win32" ) < 0 ) //$NON-NLS-1$
			{
				shellStyle |= SWT.APPLICATION_MODAL;
			}
			shellPopup = new Shell( shell, shellStyle );
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
	 *            '&' will be removed for accelerator key, if the popup title is
	 *            from the control text.
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
	 *            image displayed in the task bar. If null, leave blank.
	 */
	public WizardBase( String sID, int iInitialWidth, int iInitialHeight,
			String strTitle, Image imgTitle, String strHeader, Image imgHeader )
	{
		this( sID );
		this.iWizardWidthMinimum = iInitialWidth;
		this.iWizardHeightMinimum = iInitialHeight;
		this.wizardTitle = strTitle;
		this.imgShell = imgTitle;
		this.strHeader = strHeader;
		this.imgHeader = imgHeader;
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
	public static void displayException( Throwable t )
	{
		new ErrorDialog( null,
				Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
				Messages.getString( "WizardBase.error.FollowingErrorEncountered" ), //$NON-NLS-1$
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

	/**
	 * Sets the parent shell
	 * 
	 * @param parentShell
	 *            parent shell. Null indicates current Display is used.
	 * 
	 */
	public void setParentShell( Shell parentShell )
	{
		this.shellParent = parentShell;
	}

	private void placeComponents( )
	{
		// Initialize and layout UI components of the framework
		Composite cmpHeader = new Composite( shell, SWT.NONE );
		{
			cmpHeader.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_WHITE ) );
			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.verticalSpacing = 5;
			layout.horizontalSpacing = 0;
			cmpHeader.setLayout( layout );
			cmpHeader.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label lblTitle = new Label( cmpHeader, SWT.NONE );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalIndent = 10;
			gd.verticalIndent = 5;
			lblTitle.setLayoutData( gd );
			lblTitle.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_WHITE ) );
			lblTitle.setFont( JFaceResources.getBannerFont( ) );
			lblTitle.setText( wizardTitle );
		}

		if ( imgHeader != null )
		{
			Label lblImage = new Label( cmpHeader, SWT.NONE );
			GridData gd = new GridData( );
			gd.verticalSpan = 2;
			lblImage.setLayoutData( gd );
			lblImage.setImage( imgHeader );
		}

		lblHeader = new Label( cmpHeader, SWT.WRAP );
		{
			GridData gd = new GridData( GridData.FILL_VERTICAL );
			gd.horizontalIndent = 15;
			gd.widthHint = iWizardWidthMinimum - 50;
			lblHeader.setLayoutData( gd );
			lblHeader.setBackground( Display.getDefault( )
					.getSystemColor( SWT.COLOR_WHITE ) );
			lblHeader.setText( strHeader );
		}

		Label lblSeparator = new Label( shell, SWT.SEPARATOR | SWT.HORIZONTAL );
		lblSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		cmpTaskContainer = new CTabFolder( shell, SWT.TOP | SWT.FLAT );
		{
			cmpTaskContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			cmpTaskContainer.setTabHeight( 25 );
			// cmpTaskContainer.setSimple( false );
			cmpTaskContainer.addSelectionListener( this );
		}

		lblSeparator = new Label( shell, SWT.SEPARATOR | SWT.HORIZONTAL );
		lblSeparator.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite cmpButton = new Composite( shell, SWT.NONE );
		{
			cmpButton.setLayout( new GridLayout( ) );
			GridData gd = new GridData( );
			gd.horizontalAlignment = SWT.END;
			cmpButton.setLayoutData( gd );
		}
		buttonpanel = new ButtonPanel( cmpButton, SWT.NONE, this );
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
					cmpTaskContainer.setSelection( i + 1 );
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
					cmpTaskContainer.setSelection( i - 1 );
					this.switchTo( (String) vTaskIDs.get( i - 1 ) );
					buttonpanel.setButtonEnabled( ButtonPanel.NEXT, true );
				}
				if ( i == 1 )
				{
					// Just switched to first tab
					buttonpanel.setButtonEnabled( ButtonPanel.BACK, false );
				}
			}
			else if ( sCmd.equals( Messages.getString( "WizardBase.Finish" ) ) ) //$NON-NLS-1$
			{
				final String[] saMessages = validate( );
				if ( saMessages != null && saMessages.length > 0 )
				{
					ErrorDialog ed = new ErrorDialog( shellParent,
							Messages.getString( "WizardBase.error.ErrorsEncountered" ), //$NON-NLS-1$
							Messages.getString( "WizardBase.error.FollowingErrorsReportedWhileVerifying" ), //$NON-NLS-1$
							saMessages,
							new String[]{} );
					if ( ed.getOption( ) == ErrorDialog.OPTION_ACCEPT )
					{
						// Stop quitting to fix manually
						return;
					}
				}
				this.bWasCancelled = false;
				shell.dispose( );
			}
			else if ( sCmd.equals( Messages.getString( "WizardBase.Cancel" ) ) ) //$NON-NLS-1$
			{
				// Ensure context is not null to dispose
				shell.dispose( );
				context = null;
			}
		}
		else if ( e.getSource( ) instanceof CTabFolder )
		{
			String sCmd = ( (CTabItem) e.item ).getText( );
			int indexLabel = vTaskLabels.indexOf( sCmd );
			if ( indexLabel >= 0 )
			{
				switchTo( (String) vTaskIDs.get( indexLabel ) );
				buttonpanel.setButtonEnabled( ButtonPanel.NEXT,
						indexLabel < vTaskIDs.size( ) - 1 );
				buttonpanel.setButtonEnabled( ButtonPanel.BACK, indexLabel > 0 );
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

	private void setPopupLocation( )
	{
		if ( shellPopup != null && !shellPopup.isDisposed( ) )
		{
			int x = 0;
			if ( shell.getLocation( ).x
					+ shell.getSize( ).x + shellPopup.getSize( ).x > display.getClientArea( ).width )
			{
				// Avoid the popup exceeds the right border of the display area
				x = display.getClientArea( ).width - shellPopup.getSize( ).x;
			}
			else
			{
				x = shell.getLocation( ).x + shell.getSize( ).x;
			}
			shellPopup.setLocation( x, shell.getLocation( ).y + 20 );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
	 */
	public void widgetDisposed( DisposeEvent e )
	{
		// TODO Add cleanup code here...including removal of adapters
	}

	public void setTitle( String wizardTitle )
	{
		this.wizardTitle = wizardTitle;
	}

	/**
	 * Packs the wizard to display enough size
	 * 
	 */
	public void packWizard( )
	{
		boolean changed = false;
		Point wizardSize = shell.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		int iWizardWidth = Math.max( wizardSize.x, iWizardWidthMinimum );
		int iWizardHeight = Math.max( wizardSize.y, iWizardHeightMinimum );
		Point oldSize = shell.getSize( );
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
			shell.setSize( oldSize );
			shell.layout( );
		}
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

	private static final int BUTTON_HEIGHT = SWT.DEFAULT;
	private static final int BUTTON_WIDTH = 70;

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
		rlButtons.marginWidth = 10;
		rlButtons.spacing = 5;
		rlButtons.wrap = false;
		rlButtons.justify = false;

		this.setLayout( rlButtons );
		this.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		btnPrevious = new Button( this, SWT.NONE );
		btnPrevious.setText( Messages.getString( "WizardBase.Back" ) ); //$NON-NLS-1$
		btnPrevious.addSelectionListener( wb );
		btnPrevious.setLayoutData( new RowData( btnPrevious.getText( ).length( ) < 8
				? BUTTON_WIDTH : SWT.DEFAULT,
				BUTTON_HEIGHT ) );
		// DISABLED INITIALLY
		btnPrevious.setEnabled( false );

		btnNext = new Button( this, SWT.NONE );
		btnNext.setText( Messages.getString( "WizardBase.Next" ) ); //$NON-NLS-1$
		btnNext.addSelectionListener( wb );
		btnNext.setLayoutData( new RowData( btnNext.getText( ).length( ) < 8
				? BUTTON_WIDTH : SWT.DEFAULT, BUTTON_HEIGHT ) );
		// DISABLED INITIALLY
		btnNext.setEnabled( false );

		btnAccept = new Button( this, SWT.NONE );
		btnAccept.setText( Messages.getString( "WizardBase.Finish" ) ); //$NON-NLS-1$
		btnAccept.addSelectionListener( wb );
		btnAccept.setLayoutData( new RowData( btnAccept.getText( ).length( ) < 8
				? BUTTON_WIDTH : SWT.DEFAULT,
				BUTTON_HEIGHT ) );

		btnCancel = new Button( this, SWT.NONE );
		btnCancel.setText( Messages.getString( "WizardBase.Cancel" ) ); //$NON-NLS-1$
		btnCancel.addSelectionListener( wb );
		btnCancel.setLayoutData( new RowData( btnCancel.getText( ).length( ) < 8
				? BUTTON_WIDTH : SWT.DEFAULT,
				BUTTON_HEIGHT ) );
	}

	void setButtonEnabled( int iButton, boolean bState )
	{
		Button currentButton = null;
		switch ( iButton )
		{
			case BACK :
				currentButton = btnPrevious;
				break;
			case NEXT :
				currentButton = btnNext;
				break;
			case ACCEPT :
				currentButton = btnAccept;
				break;
			case CANCEL :
				currentButton = btnCancel;
				break;
		}
		if ( currentButton != null )
		{
			currentButton.setEnabled( bState );
			if ( bState )
			{
				// Make the enabled button get focus explicitly, to avoid the
				// tab item getting
				currentButton.setFocus( );
			}
		}
	}
}