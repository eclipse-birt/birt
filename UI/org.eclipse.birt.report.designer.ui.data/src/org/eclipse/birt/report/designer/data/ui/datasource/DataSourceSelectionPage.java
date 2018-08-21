/*******************************************************************************
 * Copyright (c) 2005, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.datasource;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DataUIConstants;
import org.eclipse.birt.report.designer.data.ui.util.IHelpConstants;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.odadatasource.wizards.AbstractDataSourceConnectionWizard;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession.IDesignNameValidator;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: Please document
 * 
 * @version $Revision$ $Date$
 */
public class DataSourceSelectionPage extends WizardPage
{

	private transient ListViewer dataSourceList = null;
	private transient Label dataSourceNameLabel = null;
	private transient Text dataSourceName = null;
	private transient Button dsChoiceListRadio = null;
	private transient Button connectionProfileRadio = null;
	private transient DataSourceDesignSession m_designSession = null;
	private transient DesignElementHandle parentHandle = null;
	private transient SlotHandle slotHandle = null;
	private String dsName = null;

	private static final String EMPTY_NAME = Messages.getString( "error.DataSource.emptyName" );//$NON-NLS-1$
	private static final String DUPLICATE_NAME = Messages.getString( "error.duplicateName" );//$NON-NLS-1$
	private static final String CREATE_DATA_SOURCE_TRANS_NAME = Messages.getString( "wizard.transaction.createDataSource" ); //$NON-NLS-1$

	private Object prevSelectedDataSourceType = null;

	// From data source selection page to specified data source creation page,
	// there is a transaction created for it. When creation page returns to
	// selection page, this transaction should rollback to indicate nothing
	// happens. Provided enough time, such kind of logic should be enhanced to
	// be clear and simple.
	private boolean dataSourceIsCreated = false;
	private boolean useODAV3 = false;
	private DataSourceSelectionHelper helper;

	private boolean validated = false;
	private String errorMessage = null;

	private static Logger logger = Logger.getLogger( DataSourceSelectionPage.class.getName( ) );

	/**
	 * @param pageName
	 */
	public DataSourceSelectionPage( String pageName )
	{
		super( pageName );
		setTitle( Messages.getString( "datasource.wizard.title.select" ) );//$NON-NLS-1$
		this.setMessage( Messages.getString( "datasource.wizard.message.selectType" ) );//$NON-NLS-1$
		setImageDescriptor( ReportPlatformUIImages.getImageDescriptor( "DataSourceBasePage" ) ); //$NON-NLS-1$
	}

	/**
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public DataSourceSelectionPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
		super( pageName, title, titleImage );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl( Composite parent )
	{
		helper = new DataSourceSelectionHelper( );

		ScrolledComposite scrollContent = new ScrolledComposite( parent,
				SWT.H_SCROLL | SWT.V_SCROLL );
		scrollContent.setAlwaysShowScrollBars( false );
		scrollContent.setExpandHorizontal( true );
		scrollContent.setMinWidth( 500 );
		scrollContent.setMinHeight( 300 );
		scrollContent.setLayout( new FillLayout( ) );
		scrollContent.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Composite composite = new Composite( scrollContent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		composite.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_BOTH );
		composite.setLayoutData( gd );

		setupDSChoiceListRadio( composite );
		setupConnectionProfileRadio( composite );

		GridData layoutData = new GridData( GridData.FILL_BOTH );
		layoutData.horizontalSpan = 2;
		dataSourceList = new ListViewer( composite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL );
		dataSourceList.getList( ).setLayoutData( layoutData );

		dataSourceList.setContentProvider( new IStructuredContentProvider( ) {

			public Object[] getElements( Object inputElement )
			{
				return (Object[]) inputElement;
			}

			public void dispose( )
			{
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}
		} );

		dataSourceList.setLabelProvider( new LabelProvider( ) {

			public String getText( Object element )
			{
				String displayName = null;
				if ( element instanceof ExtensionManifest )
				{
					ExtensionManifest config = (ExtensionManifest) element;
					DataSetProvider.findDataSourceElement( config.getExtensionID( ) );
					displayName = config.getDataSourceDisplayName( );
				}
				else
				{
					displayName = element.toString( );
				}

				return displayName;
			}
		} );

		dataSourceList.setInput( helper.getFilteredDataSourceArray( ) );

		dataSourceList.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				validateDataSourceName( );
				prevSelectedDataSourceType = getSelectedDataSource( );
				setPageComplete( !helper.hasNextPage( prevSelectedDataSourceType )
						&& getMessageType( ) != ERROR );
			}
		} );

		dataSourceList.setSorter( new ViewerSorter( ) );

		dataSourceNameLabel = new Label( composite, SWT.NONE );
		dataSourceNameLabel.setText( Messages.getString( "datasource.wizard.label.dataSourceName" ) ); //$NON-NLS-1$
		dataSourceName = new Text( composite, SWT.BORDER );

		String name = ReportPlugin.getDefault( )
				.getCustomName( ReportDesignConstants.DATA_SOURCE_ELEMENT );
		if ( name != null )
		{
			dataSourceName.setText( Utility.getUniqueDataSourceName( name ) );
		}
		else
		// can't get default name
		{
			dataSourceName.setText( Utility.getUniqueDataSourceName( Messages.getString( "datasource.new.defaultName" ) ) ); //$NON-NLS-1$
		}

		dsName = dataSourceName.getText( ).trim( );

		layoutData = new GridData( GridData.FILL_HORIZONTAL );
		dataSourceName.setLayoutData( layoutData );
		dataSourceName.setToolTipText( EMPTY_NAME );

		dataSourceName.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validateDataSourceName( );
			}
		} );

		setControl( scrollContent );

		dataSourceName.setFocus( );
		Point size = composite.computeSize( SWT.DEFAULT, SWT.DEFAULT );
		composite.setSize( size );
		scrollContent.setContent( composite );

		Utility.setSystemHelp( getControl( ),
				IHelpConstants.CONEXT_ID_DATASOURCE_NEW );
	}

	/**
	 * Setup DSChoiceListRadio layout
	 * 
	 * @param composite
	 */
	private void setupDSChoiceListRadio( Composite composite )
	{
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 2;
		layoutData.verticalIndent = 3;
		dsChoiceListRadio = new Button( composite, SWT.RADIO );
		dsChoiceListRadio.setLayoutData( layoutData );
		dsChoiceListRadio.setSelection( true );
		dsChoiceListRadio.setText( Messages.getString( "datasource.wizard.label.datasources" ) ); //$NON-NLS-1$

		dsChoiceListRadio.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				enableNonCP( dsChoiceListRadio.getSelection( ) );

				if ( dsChoiceListRadio.getSelection( ) )
				{
					validateDataSourceName( );
					prevSelectedDataSourceType = getSelectedDataSource( );
					setPageComplete( !helper.hasNextPage( prevSelectedDataSourceType )
							&& getMessageType( ) != ERROR );
				}
			}

		} );
	}

	/**
	 * Setup ConnectionProfileRadio layout
	 * 
	 * @param composite
	 */
	private void setupConnectionProfileRadio( Composite composite )
	{
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 2;
		connectionProfileRadio = new Button( composite, SWT.RADIO );
		connectionProfileRadio.setSelection( false );
		connectionProfileRadio.setText( Messages.getString( "datasource.wizard.label.connectionfile" ) ); //$NON-NLS-1$
		connectionProfileRadio.setLayoutData( layoutData );
		connectionProfileRadio.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				enableNonCP( !connectionProfileRadio.getSelection( ) );

				if ( connectionProfileRadio.getSelection( ) )
				{
					setMessage( Messages.getString( "datasource.wizard.message.selectType" ) ); //$NON-NLS-1$
					setPageComplete( false );
				}
			}

		} );
	}

	/**
	 * Enable or disable non connection profile components
	 * 
	 * @param bool
	 */
	private void enableNonCP( boolean bool )
	{
		dataSourceList.getList( ).setEnabled( bool );
		dataSourceNameLabel.setEnabled( bool );
		dataSourceName.setEnabled( bool );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizardPage#getNextPage()
	 */
	public IWizardPage getNextPage( )
	{
		if ( isCPSelected( ) )
			return getNextPageCP( );

		prevSelectedDataSourceType = getSelectedDataSource( );
		if ( prevSelectedDataSourceType instanceof ExtensionManifest )
		{
			String dataSourceElementID = ( (ExtensionManifest) prevSelectedDataSourceType ).getDataSourceElementID( );
			String dataSourceDisplayName = ( (ExtensionManifest) prevSelectedDataSourceType ).getDataSourceDisplayName( );
			String dataSourceExtensionID = ( (ExtensionManifest) prevSelectedDataSourceType ).getExtensionID( );
			IWizardPage nextPage = getExtensionDataSourcePage( dataSourceElementID,
					dataSourceDisplayName,
					dataSourceExtensionID );
			if ( nextPage != null )
				return nextPage;
		}
		return helper.getNextPage( prevSelectedDataSourceType );
	}

	public IWizardPage getExtensionDataSourcePage( String dataSourceElementID,
			String dataSourceDisplayName, String dataSourceExtensionID )
	{
		if ( dsName == null )
			dsName = dataSourceDisplayName;
		DesignSessionRequest request = null;
		try
		{
			URI applURI = DTPUtil.getInstance( ).getBIRTResourcePath( );
			URI designURI = DTPUtil.getInstance( ).getReportDesignPath( );
			request = DesignSessionUtil.createNewDataSourceRequest( dataSourceExtensionID,
					dataSourceElementID,
					dataSourceDisplayName,
					applURI,
					designURI );
		}
		catch ( OdaException e )
		{
			ExceptionHandler.handle( e );
		}

		if ( DesignSessionUtil.hasValidOdaDesignUIExtension( dataSourceElementID ) )
		{
			return getNextPageODAV3( dataSourceElementID, request );
		}

		IConfigurationElement dataSourceElement = DataSetProvider.findDataSourceElement( dataSourceElementID );
		if ( dataSourceElement != null )
		{
			return getNextPageODAV2( dataSourceElement );
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	private IWizardPage getNextPageCP( )
	{
		try
		{
			if ( m_designSession == null )
				m_designSession = DataSourceDesignSession.startNewDesignFromProfile( );

			ResourceIdentifiers designResourceIds = DesignFactory.eINSTANCE.createResourceIdentifiers( );
			designResourceIds.setApplResourceBaseURI( DTPUtil.getInstance( )
					.getBIRTResourcePath( ) );
			designResourceIds.setDesignResourceBaseURI( DTPUtil.getInstance( )
					.getReportDesignPath( ) );
			m_designSession.setUseProfileSelectionPage( true, designResourceIds );

			m_designSession.setDesignNameValidator( new DataSourceDesignNameValidator( ) );
			return m_designSession.getWizardStartingPage( );
		}
		catch ( OdaException e )
		{
			logger.log( Level.FINE, e.getMessage( ), e );
		}

		return super.getNextPage( );
	}

	/**
	 * Implements the ODA IDesignNameValidator for the ODA design session to
	 * validate the data source design name according to BIRT naming rules.
	 */
	private class DataSourceDesignNameValidator implements IDesignNameValidator
	{

		/*
		 * @see org.eclipse.datatools.connectivity.oda.design.ui.designsession.
		 * DataSourceDesignSession
		 * .IDesignNameValidator#isValid(java.lang.String)
		 */
		public boolean isValid( String designName ) throws OdaException
		{
			if ( Utility.checkDataSourceName( designName ) ) // name already
																// used
				throw new OdaException( Messages.getFormattedString( "datasource.editor.duplicatedName", //$NON-NLS-1$
						new Object[]{
							designName
						} ) );

			if ( containInvalidCharactor( designName ) ) // name contains
															// invalid special
															// character(s)
			{
				throw new OdaException( Messages.getFormattedString( "error.invalidName", //$NON-NLS-1$
						new Object[]{
							designName
						} ) );
			}

			return true;
		}
	}

	/**
	 * 
	 * @param dataSourceElementID
	 * @return
	 */
	private IWizardPage getNextPageODAV3( String dataSourceElementID,
			DesignSessionRequest request )
	{
		useODAV3 = true;

		try
		{
			if ( m_designSession == null )
				m_designSession = DataSourceDesignSession.startNewDesign( dataSourceElementID,
						dsName,
						null,
						request );
			else
				// preserve user edits on custom wizard page, if appropriate
				m_designSession.restartNewDesign( dataSourceElementID,
						dsName,
						null,
						request );
			m_designSession.setUseProfileSelectionPage( false );
			return m_designSession.getWizardStartingPage( );
		}
		catch ( OdaException e )
		{
			logger.log( Level.FINE, e.getMessage( ), e );
		}

		return super.getNextPage( );
	}

	/**
	 * 
	 * @param dataSourceElement
	 * @return
	 */
	private IWizardPage getNextPageODAV2(
			IConfigurationElement dataSourceElement )
	{
		useODAV3 = false;

		// Everytime new connectionWizard will be created.
		AbstractDataSourceConnectionWizard connectionWizard = null;// (AbstractDataSourceConnectionWizard)

		// Get the new Data source wizard element
		IConfigurationElement[] elements = dataSourceElement.getChildren( "newDataSourceWizard" );//$NON-NLS-1$
		if ( elements != null && elements.length > 0 )
		{
			Object wizard = null;
			try
			{
				wizard = elements[0].createExecutableExtension( "class" );//$NON-NLS-1$
			}
			catch ( CoreException e )
			{
				logger.log( Level.FINE, e.getMessage( ), e );
			}

			if ( wizard instanceof AbstractDataSourceConnectionWizard )
			{
				String wizardTitle = Messages.getString( "datasource.new" );//$NON-NLS-1$
				connectionWizard = ( (AbstractDataSourceConnectionWizard) wizard );
				connectionWizard.setWindowTitle( wizardTitle );
				connectionWizard.setConfigurationElement( dataSourceElement );
				// Allow the wizard to add its pages
				connectionWizard.addPages( );
			}
		}

		dataSourceIsCreated = true;

		if ( connectionWizard != null )
		{
			try
			{
				// Create the data source and set the selected name
				connectionWizard.getDataSource( ).setName( dsName );
				return connectionWizard.getStartingPage( );
			}
			catch ( NameException e )
			{
				dataSourceIsCreated = false;
				ExceptionHandler.handle( e );
			}
		}

		return super.getNextPage( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.IWizardPage#canFlipToNextPage()
	 */
	public boolean canFlipToNextPage( )
	{
		return ( getMessageType( ) != ERROR ) && !isPageComplete( );
	}

	/**
	 * checks if the name is duplicate
	 * 
	 * @return Returns true if the name is duplicate,and false if it is
	 *         duplicate
	 */
	private boolean isDuplicateName( )
	{
		String name = dataSourceName.getText( ).trim( );
		return Utility.checkDataSourceName( name );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible( boolean visible )
	{
		super.setVisible( visible );
		getControl( ).setFocus( );

		if ( isCPSelected( ) )
			return;

		if ( visible && dataSourceList.getInput( ) != null )
		{
			if ( prevSelectedDataSourceType == null )
			{
				dataSourceList.setSelection( new StructuredSelection( dataSourceList.getElementAt( 0 ) ) );
			}
			else
			{
				dataSourceList.setSelection( new StructuredSelection( prevSelectedDataSourceType ) );
			}
		}

		// rollback the data source created just now
		if ( visible && dataSourceIsCreated == true )
		{
			getActivityStack( ).rollback( );
			dataSourceIsCreated = false;
		}
	}

	public boolean performFinish( )
	{
		if ( isCPSelected( ) )
		{
			IRunnableWithProgress op = new IRunnableWithProgress( ) {

				public void run( IProgressMonitor monitor )
						throws InvocationTargetException
				{
					try
					{
						IPath location = Path.fromOSString( SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getFileName( ) );
						IFile resource = ResourcesPlugin.getWorkspace( )
								.getRoot( )
								.getFileForLocation( location );
						if ( resource != null && resource.getProject( ) != null )
						{
							resource.getProject( )
									.refreshLocal( IResource.DEPTH_INFINITE,
											monitor );
						}
					}
					catch ( CoreException e )
					{
						throw new InvocationTargetException( e );
					}
					finally
					{
						monitor.done( );
					}
				}
			};
			try
			{
				getContainer( ).run( true, false, op );
			}
			catch ( InterruptedException e )
			{
				ExceptionHandler.handle( e );
				return false;
			}
			catch ( InvocationTargetException e )
			{
				Throwable realException = e.getTargetException( );
				ExceptionHandler.handle( realException );
				return false;
			}
		}

		return this.createSelectedDataSource( );
	}

	/**
	 * 
	 * @return
	 */
	public boolean createSelectedDataSource( )
	{
		createSelectedDataSourceInit( );

		if ( isCPSelected( ) )
			return createSelectedDataSourceODAV3( );

		if ( !( getSelectedDataSource( ) instanceof ExtensionManifest ) )
		{
			return createNoneODASelectedDataSource( );
		}
		if ( useODAV3 )
			return createSelectedDataSourceODAV3( );
		else
			return createSelectedDataSourceODAV2( );
	}

	public boolean createExtensionDataSource( )
	{
		createSelectedDataSourceInit( );
		if ( useODAV3 )
			return createSelectedDataSourceODAV3( );
		else
			return createSelectedDataSourceODAV2( );
	}

	/**
	 * To start a Model transaction and get Handles ready
	 * 
	 */
	protected void createSelectedDataSourceInit( )
	{
		getActivityStack( ).startTrans( CREATE_DATA_SOURCE_TRANS_NAME );

		parentHandle = Utility.getReportModuleHandle( );
		slotHandle = ( (ModuleHandle) parentHandle ).getDataSources( );
	}

	/**
	 * whether name contains ".", "/", "\", "!", ";", "," charactors
	 * 
	 * @param name
	 * @return
	 */
	private boolean containInvalidCharactor( String name )
	{
		if ( name == null )
			return false;
		else if ( name.indexOf( "." ) > -1 || //$NON-NLS-1$
				name.indexOf( "\\" ) > -1 || name.indexOf( "/" ) > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf( "!" ) > -1 || name.indexOf( ";" ) > -1 || //$NON-NLS-1$ //$NON-NLS-2$
				name.indexOf( "," ) > -1 ) //$NON-NLS-1$
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @return
	 */
	private boolean createNoneODASelectedDataSource( )
	{
		DataSourceHandle dsHandle = null;
		if ( helper.SCRIPT_DATA_SOURCE_DISPLAY_NAME.equals( prevSelectedDataSourceType.toString( ) ) )
		{
			String driverName = DataUIConstants.DATA_SOURCE_SCRIPT;
			Class classType = ScriptDataSourceHandle.class;

			dsHandle = helper.createDataSource( classType, dsName, driverName );
		}
		else
		{
			dsHandle = helper.createNoneOdaDataSourceHandle( dsName,
					prevSelectedDataSourceType );
		}
		if ( dsHandle == null )
			return false;
		try
		{
			slotHandle.add( dsHandle );
		}
		catch ( ContentException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( NameException e )
		{
			ExceptionHandler.handle( e );
		}

		if ( dsHandle instanceof ScriptDataSourceHandle )
		{
			Utility.setScriptActivityEditor( );
		}

		createSelectedDataSourceTearDown( );

		return true;
	}

	/**
	 * 
	 * @return
	 */
	private boolean createSelectedDataSourceODAV3( )
	{
		try
		{
			DataSourceHandle dataSourceHandle = DTPUtil.getInstance( )
					.createOdaDataSourceHandle( m_designSession.finish( )
							.getResponse( ),
							(ModuleHandle) parentHandle );

			if ( dataSourceHandle != null )
			{
				slotHandle.add( dataSourceHandle );

				m_designSession = null; // reset
				createSelectedDataSourceTearDown( );
			}
			return true;
		}
		catch ( SemanticException e )
		{
			getActivityStack( ).rollback( );
			ExceptionHandler.handle( e );
			return false;
		}
		catch ( OdaException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean createSelectedDataSourceODAV2( )
	{
		Object dataSourceObj = getSelectedDataSource( );
		String driverName = null;
		Class classType = null;
		if ( dataSourceObj instanceof ExtensionManifest )
		{
			driverName = ( (ExtensionManifest) dataSourceObj ).getExtensionID( );
			classType = OdaDataSourceHandle.class;
		}

		try
		{
			DataSourceHandle dsHandle = helper.createDataSource( classType,
					dsName,
					driverName );
			slotHandle.add( dsHandle );

			createSelectedDataSourceTearDown( );

			return true;
		}
		catch ( SemanticException e )
		{
			getActivityStack( ).rollback( );
			ExceptionHandler.handle( e );
			return false;
		}
	}

	/**
	 * Commit to Model
	 * 
	 */
	private void createSelectedDataSourceTearDown( )
	{
		dataSourceIsCreated = true;
		getActivityStack( ).commit( );
	}

	/**
	 * 
	 * @return
	 */
	public CommandStack getActivityStack( )
	{
		return Utility.getCommandStack( );
	}

	/**
	 * 
	 * @return
	 */
	private Object getSelectedDataSource( )
	{
		return ( (IStructuredSelection) dataSourceList.getSelection( ) ).getFirstElement( );
	}

	/**
	 * 
	 * @return
	 */
	private boolean isCPSelected( )
	{
		return connectionProfileRadio.getSelection( );
	}

	private void validateDataSourceName( )
	{
		dsName = dataSourceName.getText( ).trim( );

		if ( StringUtil.isBlank( dataSourceName.getText( ).trim( ) ) )
		{// name is empty
			setMessage( EMPTY_NAME, ERROR );
		}
		else if ( isDuplicateName( ) )
		{// name is duplicated
			setMessage( DUPLICATE_NAME, ERROR );
		}
		else if ( containInvalidCharactor( dataSourceName.getText( ) ) )
		{// name contains invalid "." charactor
			String msg = Messages.getFormattedString( "error.invalidName", //$NON-NLS-1$
					new Object[]{
						dataSourceName.getText( )
					} );
			setMessage( msg, ERROR );
		}
		else
		{
			setMessage( Messages.getString( "datasource.wizard.message.selectType" ) ); //$NON-NLS-1$
		}
		setPageComplete( !helper.hasNextPage( getSelectedDataSource( ) )
				&& getMessageType( ) != ERROR );
	}
}