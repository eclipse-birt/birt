/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.nls.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.sqltools.editor.core.connection.ISQLEditorConnectionInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.input.ISQLBuilderEditorInputUsageOptions;
import org.eclipse.datatools.sqltools.sqlbuilder.input.SQLBuilderEditorInputUsageOptions;
import org.eclipse.datatools.sqltools.sqlbuilder.input.SQLBuilderStorageEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.model.ControlStateInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.IControlStateInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.IWindowStateInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.SQLBuilderConnectionInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.model.WindowStateInfo;
import org.eclipse.datatools.sqltools.sqlbuilder.util.SQLBuilderEditorInputUtil;
import org.eclipse.datatools.sqltools.sqleditor.SQLEditorStorage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressIndicator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class SQBDataSetWizardPage extends DataSetWizardPage
{
    private static final String SQB_STATE_CURRENT_VERSION = "1.0"; //$NON-NLS-1$
    private static final String NEWLINE_CHAR = "\n"; //$NON-NLS-1$
    private static final String EMPTY_STR = ""; //$NON-NLS-1$
    
    private IConnectionProfile m_dataSourceProfile;
    private ISQLBuilderEditorInput m_sqbInput;    
	private CustomSQLBuilderDialog m_sqbDialog;
	private boolean m_updatedQueryInput = false;

	public SQBDataSetWizardPage( String pageName )
	{
		super( pageName );	
	}    

	private IConnectionProfile getConnectionProfile( boolean raiseErrorIfNull )
	{
	    if( m_dataSourceProfile == null )
	    {
            java.util.Properties connProps = null;
            String exceptionMessage = EMPTY_STR;
            try
            {
                connProps = DesignSessionUtil.getEffectiveDataSourceProperties( 
                                getEditingDesign().getDataSourceDesign() );
            }
            catch( OdaException ex )
            {
                exceptionMessage = ex.getLocalizedMessage();
            }

            m_dataSourceProfile = OdaProfileExplorer.getInstance()
               .getProfileByName( connProps, null );
            
            if( m_dataSourceProfile == null && raiseErrorIfNull )
                MessageDialog.openError( getShell(), Messages.sqbWizPage_dataSourceDesignError, 
                        Messages.sqbWizPage_noConnProfileMsg + NEWLINE_CHAR + exceptionMessage );
	    }

	    return m_dataSourceProfile;
	}
    
    private String getDataSetDesignName()
    {
        return getEditingDesign().getName();
    }
	
	private String getDataSetDesignQueryText()
	{
	    return getEditingDesign().getQueryText();
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
        IConnectionProfile connProfile = getConnectionProfile( true );
        if( connProfile == null )
            return;
        
		m_sqbInput = createSQBInput( parent, connProfile );
		setControl( createSQBControl( parent, m_sqbInput ) );
	}

	private ISQLBuilderEditorInput createSQBInput( Composite parent, IConnectionProfile connProfile )
	{
	    m_updatedQueryInput = false;
	    SQLBuilderStorageEditorInput sqbInput = restoreSQLBuilderStateFromDesign();
	    if( sqbInput == null )
	    {
	        // create a default input with empty SQL statement
	        return new DefaultSQBInput( connProfile );
	    }

        // replace restored storage with input's query text (to workaround problem with
	    // position of storage content stream);
        // if restored input has a different SQL statement, first ask user if ok to replace 
	    // with data set design's query text
        String sqlText = sqbInput.exists() ? sqbInput.getSQL() : EMPTY_STR;
        if( sqlText.length() > 0 &&
            ! sqlText.equalsIgnoreCase( getDataSetDesignQueryText() ))
        {
            if( openReplaceSQLMessageBox( parent.getShell() ) )
            {
                sqlText = getDataSetDesignQueryText();
                m_updatedQueryInput = true;
            }
        }
        sqbInput.setStorage( new SQLEditorStorage( getDataSetDesignName(), sqlText ));
        
        
        // replace with latest data set design name
        sqbInput.setName( getDataSetDesignName() );
        
        // replace with specified connection profile
        sqbInput.setConnectionInfo( new SQLBuilderConnectionInfo( connProfile ) );
        
        // override option to always use window state
        setUseWindowState( sqbInput, true );

        return sqbInput;	    
	}

	private SQLBuilderStorageEditorInput restoreSQLBuilderStateFromDesign()
	{
	    DesignerState designerState = getInitializationDesignerState();
	    if( designerState == null || designerState.getStateContent() == null )
	        return null;
	    
	    // TODO - check for version compatibility of designerState.getVersion 
	    
	    String designStateValue = designerState.getStateContent().getStateContentAsString();
	    if( designStateValue == null )
            return null;

	    return SQLBuilderEditorInputUtil.createSQLBuilderStorageEditorInput( designStateValue );
	}
	
    private static void setUseWindowState( SQLBuilderStorageEditorInput sqbInput, 
            boolean useWindowState )
    {       
        ISQLBuilderEditorInputUsageOptions usageOption = sqbInput.getInputUsageOptions();
        if( usageOption != null )
        {
            usageOption.setUseWindowState( useWindowState );
            return;     // done
        }
        
        usageOption = new SQLBuilderEditorInputUsageOptions( useWindowState );
        sqbInput.setInputUsageOptions( usageOption );
    }
    
	private Control createSQBControl( Composite parent, ISQLBuilderEditorInput sqbInput )
	{
		Composite pageContainer = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 2;
		pageContainer.setLayout( layout );
		pageContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		 
		// initiate connect before setting input for better error handling
		Shell parentShell = parent.getShell();
		boolean isConnected = connect( parentShell, sqbInput );
		if( ! isConnected )
		{
	        setPageComplete( false );
            return pageContainer; // returns empty composite
		}
		
        m_sqbDialog = new CustomSQLBuilderDialog( parentShell );        
		boolean isInputLoaded = m_sqbDialog.setInput( sqbInput );
		if( ! isInputLoaded && ! (sqbInput instanceof DefaultSQBInput ) )
		{
		    // raise user message that the preserved state is no longer valid
		    if( openInvalidInputMessageBox( parentShell ) )
		    {
    		    // substitute with default input instead, re-using same connection profile
    		    isInputLoaded = m_sqbDialog.setInput( 
		                        new DefaultSQBInput( sqbInput.getConnectionInfo() ) );
    		    m_updatedQueryInput = true;
		    }
		}

		if( isInputLoaded )
		{
		    m_sqbDialog.createDialogArea( pageContainer );
		    if( m_updatedQueryInput )
		        m_sqbDialog.setDirty( true );
		}
		else
		    m_sqbDialog = null;

		setPageComplete( isInputLoaded );
		return pageContainer;
	}

	/**
	 * Attempts to connect based on the specified ISQLBuilderEditorInput's connection info.
	 * If connection fails, raise an error message dialog with the connection failure messages.
	 * @return true if connect succeeds; false otherwise
	 */
    private boolean connect( Shell parentShell, ISQLBuilderEditorInput sqbInput )
    {
        IConnectionProfile connProfile = sqbInput.getConnectionInfo().getConnectionProfile();
		if( connProfile.supportsWorkOfflineMode() && connProfile.canWorkOffline() )
		    return true;

		Object connectStatus = new ProgressBarDialog( parentShell, connProfile ).open( );
		
	    // handle error if found
	    new ConnectionJobListener( parentShell ).done( connectStatus == null
				? null : (IStatus) connectStatus );
		return connectStatus == null ? false
				: ( (IStatus) connectStatus ).isOK( );
    }

    private class ConnectionJobListener implements IJobChangeListener
    {
        private Shell m_parentShell;
        
        private ConnectionJobListener( Shell parentShell )
        {
            m_parentShell = parentShell;
        }
        
        /*
         * (non-Javadoc)
         * @see org.eclipse.core.runtime.jobs.IJobChangeListener#done(org.eclipse.core.runtime.jobs.IJobChangeEvent)
         */
        public void done( IJobChangeEvent event ) 
        {
            IStatus connectStatus = event.getResult();
            done( connectStatus );
        }
        
        /**
         * Format and raise an user message if the specified status is not OK.
         * @param connectStatus
         */
        void done( IStatus connectStatus ) 
        {
            if( connectStatus!= null && connectStatus.isOK() )
                return;
            
            if( connectStatus == null )
            {
            	ExceptionHandler.showException( m_parentShell, 
                        Messages.sqbWizPage_cannotOpenConnectionTitle, Messages.sqbWizPage_cannotOpenConnectionMsg, null );
            }
            // failed to connect, raise error message dialog
            String errorMessage = Messages.sqbWizPage_cannotOpenConnectionMsg
                + NEWLINE_CHAR + Messages.sqbWizPage_dbErrorMsg;
            errorMessage += connectStatus.getMessage();
            
            IStatus[] childrenStatus = connectStatus.getChildren();
            
            //Collect the first exception info.
            Throwable ex = connectStatus.getException( );
            
            for( int i=0; i < childrenStatus.length; i++ )
            {
                if ( ex == null )
                	ex = childrenStatus[i].getException( );
            }
            
            ExceptionHandler.showException( m_parentShell, 
                    Messages.sqbWizPage_cannotOpenConnectionTitle, errorMessage, ex );
        }

        // ignored events
        public void aboutToRun( IJobChangeEvent event ) {} 

        public void awake( IJobChangeEvent event ) {} 

        public void running( IJobChangeEvent event ) {}  

        public void scheduled( IJobChangeEvent event ) {}  

        public void sleeping( IJobChangeEvent event ) {} 
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectResponseState()
	 */
    protected void collectResponseState()
    {
        if ( getControl() == null || getControl().isDisposed() || m_sqbDialog == null )
            return;

        super.collectResponseState();
        
        String sqbState = m_sqbDialog.saveSQBState( getDataSetDesignName() );
        if( sqbState != null && sqbState.length() > 0 )
        {
            DesignerState designerState = DesignFactory.eINSTANCE.createDesignerState();
            designerState.setNewStateContentAsString( sqbState );
            designerState.setVersion( SQB_STATE_CURRENT_VERSION );
            
            setResponseDesignerState( designerState );
        }
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
     */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{	
        if ( getControl() == null || getControl().isDisposed() || m_sqbDialog == null )
	        return design;
		
	    // saves query and its metadata in DataSetDesign
        if( m_sqbDialog.isDirty() )
        {
            SQLQueryUtility.updateDataSetDesign( design, m_sqbDialog.getSQLQueryStatement(),
                                            getConnectionProfile( false ) );
            m_sqbDialog.setDirty( false );
        }

		return design;
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#cleanup()
     */
    protected void cleanup()
    {
         IConnectionProfile connProfile = getConnectionProfile( false );
        if( connProfile != null && connProfile.getConnectionState() == IConnectionProfile.CONNECTED_STATE )
            connProfile.disconnect( null );
        
        if( m_sqbDialog != null )
        {
            m_sqbDialog.close();
            m_sqbDialog = null;
        }
    }

    private static boolean openInvalidInputMessageBox( Shell parentShell )
    {
        String userMessage = Messages.sqbWizPage_invalidSqbStateMsg
            + NEWLINE_CHAR
            + Messages.sqbDialog_inputFailOnOpenAskUserMessage;
        return  MessageDialog.openQuestion( parentShell,
                    Messages.sqbWizPage_invalidSqbStateTitle, userMessage );
    }

    private static boolean openReplaceSQLMessageBox( Shell parentShell )
    {
        return MessageDialog.openQuestion( parentShell, 
                Messages.sqbWizPage_detectSqlTextChangedTitle,
                Messages.sqbWizPage_detectSqlTextChangedMsg );
    }
    
	/**
	 * Internal class for a default SQB Input used when no existing input is available.
	 */
    private class DefaultSQBInput extends SQLBuilderStorageEditorInput
    {
        private static final int SQBCONTROL_HEIGHT = 400;

		DefaultSQBInput( IConnectionProfile connProfile )
        {
            this( new SQLBuilderConnectionInfo( connProfile ) );   
        }
        
        DefaultSQBInput( ISQLEditorConnectionInfo connInfo )
        {
            super( getDataSetDesignName(), getDataSetDesignQueryText() );   
            init( connInfo );
        }
        
        private void init( ISQLEditorConnectionInfo connInfo )
        {
            setConnectionInfo( connInfo );
            setOmitSchemaInfo( null );  // use setting from SQB preference page
            setWindowStateInfo( createDefaultWindowState() );
            setUseWindowState( this, true );
        }
        
        private IWindowStateInfo createDefaultWindowState()
        {
            // hide Outline control by default
            IControlStateInfo outlineControlState = 
                new ControlStateInfo( IControlStateInfo.OUTLINE_CONTROL );
            outlineControlState.setIsVisible( false );

            IWindowStateInfo windowState = new WindowStateInfo();
            windowState.put( outlineControlState.getControlType(), outlineControlState );
           
	        windowState.setHeight( SQBCONTROL_HEIGHT );
            return windowState;
        }
    };

    /**
     * Class which start a thread to connect to a DB. 
     *
     */
    class ProcessThread extends Thread
	{
		private IConnectionProfile connProf;
		private IStatus result;
		
		//Constructor
		ProcessThread( IConnectionProfile connProf )
		{
			this.connProf = connProf;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run( )
		{
			result = connProf.connectWithoutJob( );
		}

		/**
		 * 
		 * @return
		 */
		public IStatus getResult( )
		{
			return this.result;
		}
	}
    
    /**
     * 
     * @author Administrator
     *
     */
    public class ProgressBarDialog extends Dialog
	{
    	//
    	private ProgressIndicator progressIndicator = null; 

		private IConnectionProfile connProf;
		private volatile boolean isClosed = false;

		private String connectingString = Messages.sqbWizPage_connectingDB; 

		//
		public ProgressBarDialog( Shell parent, IConnectionProfile connProf )
		{
			super( parent );
			this.connProf = connProf;
		}

		/**
		 * Open a ProgressBarDialog.
		 * @return
		 */
		public Object open( )
		{
			Shell shell = new Shell( getParent( ), SWT.PRIMARY_MODAL );
			createContents( shell ); //create window
			
			shell.layout( );
			shell.pack( );
			Point pl = this.getParent( ).getLocation( ); 
			Point ps = this.getParent( ).getSize( );
			Point size = shell.getSize();
			
			shell.setLocation( pl.x + (ps.x - size.x)/2, pl.y + (ps.y - size.y)/2);
			
			shell.open( );
			progressIndicator.beginAnimatedTask( );
			//start work
			ProcessThread thread = new ProcessThread( this.connProf );
			thread.start( );

			Display display = getParent( ).getDisplay( );
			while ( !shell.isDisposed( )
					&& !isClosed && thread.getResult( ) == null )
			{
				if ( !display.readAndDispatch( ) )
				{
					display.sleep( );
				}
			}
			shell.close( );

			return thread.getResult( );
		}

		/**
		 * Create contents of the processing bar.
		 * @param shell
		 */
		private void createContents( Shell shell )
		{

			final GridLayout gridLayout = new GridLayout( );

			shell.setLayout( gridLayout );

			final Composite composite = new Composite( shell, SWT.NONE );
			composite.setLayoutData( new GridData( GridData.CENTER,
					GridData.CENTER,
					true,
					false ) );
			composite.setLayout( new GridLayout( ) );

			Label lable = new Label( composite, SWT.NONE );
			lable.setText( connectingString );

			Composite progressBarComposite = new Composite( shell, SWT.NONE );
			progressBarComposite.setLayoutData( new GridData( GridData.FILL,
					GridData.CENTER,
					false,
					false ) );
			progressBarComposite.setLayout( new FillLayout( ) );

			progressIndicator = new ProgressIndicator( progressBarComposite,
					SWT.INDETERMINATE );

			Composite cancelComposite = new Composite( shell, SWT.NONE );
			cancelComposite.setLayoutData( new GridData( GridData.END,
					GridData.CENTER,
					false,
					false ) );
			final GridLayout gridLayout_1 = new GridLayout( );
			gridLayout_1.numColumns = 2;
			cancelComposite.setLayout( gridLayout_1 );

			Button cancelButton = new Button( cancelComposite, SWT.FLAT );
			cancelButton.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					isClosed = true;
				}
			} );
			cancelButton.setLayoutData( new GridData( 60, SWT.DEFAULT ) );
			cancelButton.setText( Messages.sqbWizPage_cancelButton );
			cancelButton.setEnabled( true );

		}
	}
}
