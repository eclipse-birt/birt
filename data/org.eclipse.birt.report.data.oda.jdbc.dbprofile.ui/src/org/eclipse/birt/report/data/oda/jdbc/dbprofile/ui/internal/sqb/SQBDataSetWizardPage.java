/*******************************************************************************
* Copyright (c) 2008, 2011 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl.Connection;
import org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.nls.Messages;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.SortSpecification;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.modelbase.sql.query.QueryStatement;
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
import org.eclipse.datatools.sqltools.sqleditor.SQLEditorStorage;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SQBDataSetWizardPage extends DataSetWizardPage
{
private static final String NEWLINE_CHAR = "\n"; //$NON-NLS-1$
private static final String EMPTY_STR = ""; //$NON-NLS-1$
private static final String DEFAULT_MESSAGE = Messages.sqbWizPage_defaultMessage;

private IConnectionProfile m_dataSourceProfile;
    private CustomSQLBuilderDialog m_sqbDialog;
    private boolean m_updatedQueryInput = false;
    private SortSpecification m_initQuerySortSpec;
    
    public SQBDataSetWizardPage( String pageName )
    {
            super( pageName );	
    setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE );
    }    

    private IConnectionProfile getConnectionProfile( boolean raiseErrorIfNull, boolean refreshProfileStore )
    {
        if( m_dataSourceProfile == null )
        {
        if( refreshProfileStore )
            OdaProfileExplorer.getInstance().refresh();

        java.util.Properties connProps = DesignUtil.convertDataSourceProperties( 
                            getEditingDesign().getDataSourceDesign() );
        m_dataSourceProfile = loadConnectionProfile( connProps,
                                getEditingDesign().getDataSourceDesign().getHostResourceIdentifiers() );
            
            
            if( m_dataSourceProfile == null && raiseErrorIfNull )
                MessageDialog.openError( getShell(), Messages.sqbWizPage_dataSourceDesignError, 
                        Messages.sqbWizPage_noConnProfileMsg );
	    }

	    return m_dataSourceProfile;
	}
    
	private static IConnectionProfile loadConnectionProfile( java.util.Properties connProps,
            ResourceIdentifiers designResourceIdentifiers )
	{
        Map<String, Object> designSessionContext =
                DesignSessionUtil.createResourceIdentifiersContext( designResourceIdentifiers );
	    try
        {
            return Connection.loadProfileFromProperties( connProps, designSessionContext );
        }
        catch( OdaException ex )
        {
            // ignore, let the caller handles null profile
            return null;
        }
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
        IConnectionProfile connProfile = getConnectionProfile( true, true );
        if( connProfile == null )
            return;
        
        ISQLBuilderEditorInput sqbInput = createSQBInput( parent, connProfile );
		setControl( createSQBControl( parent, sqbInput ) );
		
		SQLQueryUtility.setSystemHelp( getControl( ),
				IHelpConstants.CONEXT_ID_DATASET_SQLWIZARDPAGE );
	}

	@Override
    protected void refresh( DataSetDesign dataSetDesign )
    {
        super.refresh( dataSetDesign );
        if( m_sqbDialog != null )
            resetQueryDesignState( m_sqbDialog.getSQLQueryStatement(), dataSetDesign );
    }

    private ISQLBuilderEditorInput createSQBInput( Composite parent, IConnectionProfile connProfile )
	{
	    m_updatedQueryInput = false;
	    SQLBuilderDesignState sqbState = restoreSQLBuilderStateFromDesign( parent.getShell() );
	    if( sqbState == null || ! sqbState.hasSQBInput() )
	    {
	        // create a default input with empty SQL statement
	        return new DefaultSQBInput( connProfile );
	    }

        // if restored input has a different SQL statement, first ask user if ok to replace 
	    // with data set design's query text
        SQLBuilderStorageEditorInput sqbInput = sqbState.getSQBStorageInput();
        String sqlText = sqbInput.exists() ? sqbInput.getSQL() : EMPTY_STR;
        if( ! isSQLUpToDateInSQBInput( sqbState ) )
        {
            if( openReplaceSQLMessageBox( parent.getShell() ) )
            {
                sqlText = getDataSetDesignQueryText();
                m_updatedQueryInput = true;
            }
        }

        // replace restored storage with input's query text (to workaround problem with
        // position of storage content stream);
        sqbInput.setStorage( new SQLEditorStorage( getDataSetDesignName(), sqlText ));
        
        
        // replace with latest data set design name
        sqbInput.setName( getDataSetDesignName() );
        
        // replace with specified connection profile
        sqbInput.setConnectionInfo( new SQLBuilderConnectionInfo( connProfile ) );
        
        // override option to always use window state
        setUseWindowState( sqbInput, true );

        return sqbInput;	    
	}

	private void resetQueryDesignState( final QueryStatement queryStmt, final DataSetDesign dataSetDesign )
	{
	    m_initQuerySortSpec = null;
        if( queryStmt == null )
            return;            // no query state to set, done
        
        ResultSetDefinition resultSetDefn = dataSetDesign != null ?
                dataSetDesign.getPrimaryResultSet() : null;
        if( resultSetDefn == null )
            return;

        // track the initial state of the OrderBy clause in the SQB query
        try
        {
            m_initQuerySortSpec = SQLQueryUtility.convertOrderByClauseToSortSpec( queryStmt, null, resultSetDefn );
        }
        catch( OdaException e )
        {
            // ignore
        }
	}
	
	private SQLBuilderDesignState restoreSQLBuilderStateFromDesign( Shell parentShell )
	{
	    DesignerState designerState = getInitializationDesignerState();
	    if( designerState == null || designerState.getStateContent() == null )
	        return null;
	    
	    SQLBuilderDesignState sqbState;
        try
        {
            sqbState = new SQLBuilderDesignState( designerState );
        }
        catch( OdaException ex )
        {
	        openInvalidInputMessageBox( parentShell, false );
	        return null;
	    }

	    return sqbState;
	}
	
	private boolean isSQLUpToDateInSQBInput( SQLBuilderDesignState sqbState )
	{
        SQLBuilderStorageEditorInput sqbInput = sqbState.getSQBStorageInput();
        String sqlInSQBInput = sqbInput != null && sqbInput.exists() ? 
                            sqbInput.getSQL() : EMPTY_STR;
        String editingQueryText = getDataSetDesignQueryText();
        if( SQLQueryUtility.isEquivalentSQL( sqlInSQBInput, editingQueryText ) )
            return true;

        sqlInSQBInput = sqbState.getPreparableSQL();
        if( sqlInSQBInput != null && sqlInSQBInput.equals( editingQueryText ) )
            return true;

        return false;
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
		    if( openInvalidInputMessageBox( parentShell, true ) )
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
		if( connProfile.getConnectionState() == IConnectionProfile.CONNECTED_STATE )
		    return true;      // already connected

		assert( connProfile.equals( getConnectionProfile( false, false ) ) );
		return runConnect( parentShell ); 		
    }

    /**
     * Connect to database in a runnable with progress bar dialog.
     * @param connProfile
     * @param parentShell
     */
    private boolean runConnect( final Shell parentShell )
	{
		IRunnableWithProgress runnable = new IRunnableWithProgress( ) 
		{
			public void run( IProgressMonitor monitor )
					throws InvocationTargetException, InterruptedException
			{
				monitor.beginTask( Messages.sqbWizPage_connectingDB, IProgressMonitor.UNKNOWN );
				IStatus status = doConnect();
				monitor.done( );
				
				if( status == null || ! status.isOK() )
				    throw new InvocationTargetException( Connection.getStatusException( status ) );				
			}
		};

		try
		{
			new ProgressMonitorDialog( parentShell ) {}
			    .run( true, false, runnable );
		}
		catch ( InvocationTargetException e )
		{
		    raiseConnectionErrorMessage( parentShell, e );
			return false;
		}
		catch ( InterruptedException e )
		{
            raiseConnectionErrorMessage( parentShell, e );
            return false;
		}
		
		return true;
	}
    
    private IStatus doConnect()
    {
        IConnectionProfile connProfile = getConnectionProfile( false, false );
        assert( connProfile != null );
        // TODO - this connect task cannot be cancelled; should replace with a cancellable one (BZ 228292)
        return connProfile.connectWithoutJob();
    }
    
     /**
     * Raises an error message dialog associated with the given parent shell and displays the
     * error messages from the given exception.
     * @param parentShell
     * @param connectException  may be null
     */
    private static void raiseConnectionErrorMessage( Shell parentShell, Throwable connectException )
    {
        String errorMessage = Messages.sqbWizPage_cannotOpenConnectionMsg;
        
        if( connectException != null )
        {
            String dbMessage = connectException.getMessage();
            if( dbMessage != null )
                errorMessage += NEWLINE_CHAR + Messages.sqbWizPage_dbErrorMsg + dbMessage;
        }
        
        ExceptionHandler.showException( parentShell, 
                Messages.sqbWizPage_cannotOpenConnectionTitle, errorMessage, connectException );
    }
    
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectResponseState()
	 */
    protected void collectResponseState()
    {
        if ( getControl( ) == null
				|| getControl( ).isDisposed( ) || m_sqbDialog == null )
		{
			setResponseDesignerState( getInitializationDesignerState( ) );
			return;
		} 

        super.collectResponseState();
        
        SQLBuilderDesignState sqbState = m_sqbDialog.saveSQBState( getDataSetDesignName() );
        if( sqbState == null )
            return;     // done; no state info
        
        String sqbStateContent = sqbState.toString();
        if( sqbStateContent.length() == 0 )
            return;     // done; no state info

        DesignerState designerState = DesignFactory.eINSTANCE.createDesignerState();
        designerState.setNewStateContentAsString( sqbStateContent );
        designerState.setVersion( sqbState.getVersion() );
        
        setResponseDesignerState( designerState );
    }

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
     */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{	
        if ( getControl() == null || getControl().isDisposed() )    // page is not active
	        return design;          // not in an active session, keep the design as is
        if( m_sqbDialog == null )   // likely error with the data set query
            return null;            // return null to trigger a response session status error
			
	    // saves query and its metadata in DataSetDesign
        if( m_sqbDialog.isDirty() )
        {
            SQLQueryUtility.updateDataSetDesign( design, m_sqbDialog.getSQLQueryStatement(),
                                            getConnectionProfile( false, false ), 
                                            getInitializationDesign(),
                                            m_initQuerySortSpec );
            m_sqbDialog.setDirty( false );
        }

		return design;
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#cleanup()
     */
    protected void cleanup()
    {
        Connection.closeProfile( m_dataSourceProfile );
        m_dataSourceProfile = null;

        m_initQuerySortSpec = null;
        
        if( m_sqbDialog != null )
        {
            m_sqbDialog.close();
            m_sqbDialog = null;
        }
    }

    private static boolean openInvalidInputMessageBox( Shell parentShell, boolean askUser )
    {
        String userMessage = Messages.sqbWizPage_invalidSqbStateMsg;
        if( askUser )
        {
            userMessage += NEWLINE_CHAR
                            + Messages.sqbWizPage_inputFailOnOpenAskUserMessage;
            return MessageDialog.openQuestion( parentShell,
                        Messages.sqbWizPage_invalidSqbStateTitle, userMessage );
        }
        
        // not an user option, raise warning and continue
        MessageDialog.openWarning( parentShell,
                        Messages.sqbWizPage_invalidSqbStateTitle, userMessage );
        return true; // continue
    }

    private static boolean openReplaceSQLMessageBox( Shell parentShell )
    {
        return MessageDialog.openQuestion( parentShell, 
                Messages.sqbWizPage_detectSqlTextChangedTitle,
                Messages.sqbWizPage_detectExternalSqlTextChangedMsg );
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

}
