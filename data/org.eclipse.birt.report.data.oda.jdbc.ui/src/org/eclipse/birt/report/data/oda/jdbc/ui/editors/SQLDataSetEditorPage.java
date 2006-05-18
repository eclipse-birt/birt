/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.Bidi;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.DateSetPreferencePage;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.IMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DbObject;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Procedure;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ProcedureParameter;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.47 $ $Date: 2006/05/17 10:45:10 $
 */

public class SQLDataSetEditorPage extends DataSetWizardPage implements SelectionListener
{
	private transient Document doc = null;
	private SourceViewer viewer = null;
	private Hashtable htActions = new Hashtable( );
    private TreeItem rootNode = null;
    private Text searchTxt = null;
    private boolean isSchemaSupported = false;
    private boolean expandDbObjectsTree = false;
    private Tree availableDbObjectsTree = null;
    private IMetaDataProvider metaDataProvider = null;
    private JdbcSQLSourceViewerConfiguration sourceViewerConfiguration = null;
	// Images that will be used in displayign the tables, views etc
	private Image schemaImage, tableImage, viewImage, 
		    dataBaseImage, columnImage;
	
	// List of Schema Name
	protected ArrayList schemaList;
	
	// List of Table names 
	protected ArrayList tableList;
	
	private ComboViewer filterComboViewer = null;
	private Combo schemaCombo = null;
	private Label schemaLabel = null;
	Connection jdbcConnection = null;
	boolean validConnection = false;
	private Button identifierQuoteStringCheckBox = null; 
	private static String TABLE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.TableIcon";
	private static String VIEW_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.ViewIcon";
	private static String PAGE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.PageIcon";
	private static String SCHEMA_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.SchemaIcon";
	private static String DATABASE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.DbIcon";
	private static String COLUMN_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.ColumnIcon";
	
	private String cachedSearchTxt = "";
	private String formerQueryTxt = "";
	private String cachedDbType = "";
	private int cachedSchemaComboIndex = -1;
	private DataSourceDesign prevDataSourceDesign;
	private DataSetDesign dataSetDesign;
	private static String DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "dataset.new.query" );//$NON-NLS-1$	
	
	static
	{
		try
		{
	
			ImageRegistry reg = JFaceResources.getImageRegistry( );
			reg.put( TABLE_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/table.gif" ) );//$NON-NLS-1$
			reg.put( VIEW_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/view.gif" ) );//$NON-NLS-1$
			reg.put( PAGE_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/create_join_wizard.gif" ) );//$NON-NLS-1$
			reg.put( SCHEMA_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/schema.gif" ) );//$NON-NLS-1$
			reg.put( DATABASE_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/data_source.gif" ) );//$NON-NLS-1$
			reg.put( COLUMN_ICON,
					ImageDescriptor.createFromFile( JdbcPlugin.class,
							"icons/column.gif" ) );//$NON-NLS-1$
		}
		catch ( Exception ex )
		{
			
		} 
	}
 
	public SQLDataSetEditorPage( String pageName )
	{
		super( pageName );
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
		setControl( createPageControl( parent ) );
		initializeControl( );
	}
	
	/**
	 * initial dataset control
	 *
	 */
	private void initializeControl( )
	{
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "dataset.new.query" );
		setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE);
		refreshPage( );
		prepareUI( );
	}
	
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSetWizardPageCore#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{
		if ( design != null && doc != null )
			design.setQueryText( doc.get( ) );
		if ( !formerQueryTxt.equals( design.getQueryText( ) ) )
		{
			savePage( design );
			formerQueryTxt = design.getQueryText( );
		}
		return design;
	}
	
	/**
	 * save resultset metadata and parameter metadata in dataset design
	 * 
	 * @param design
	 */
	private void savePage( DataSetDesign design )
	{
       // obtain query's result set metadata, and update
        // the dataSetDesign with it
        IConnection conn = null;
		try
		{
			IDriver jdbcDriver = new OdaJdbcDriver( );
			conn = jdbcDriver.getConnection( null );
			java.util.Properties prop = new java.util.Properties( );
			DataSourceDesign dataSourceDesign = design.getDataSourceDesign( );
			if ( dataSourceDesign != null )
			{
				prop.put( Constants.ODADriverClass,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODADriverClass ) == null
								? "" : dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODADriverClass ) );
				prop.put( Constants.ODAURL,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAURL ) == null ? ""
								: dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAURL ) );
				prop.put( Constants.ODAUser,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAUser ) == null ? ""
								: dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAUser ) );
				prop.put( Constants.ODAPassword,
						dataSourceDesign.getPublicProperties( )
								.getProperty( Constants.ODAPassword ) == null
								? "" : dataSourceDesign.getPublicProperties( )
										.getProperty( Constants.ODAPassword ) );
			}
			conn.open( prop );
			IQuery query = conn.newQuery( design.getOdaExtensionDataSetId( ) );
			query.setMaxRows( 1 );
			query.prepare( design.getQueryText( ) );

			// set parameter metadata
			IParameterMetaData paramMetaData = query.getParameterMetaData( );
			mergeParameterMetaData( design, paramMetaData );
			query.executeQuery( );

			// set resultset metadata
			IResultSetMetaData metadata = query.getMetaData( );
			setResultSetMetaData( design, metadata );

		}
        catch( OdaException e )
        {
            // no result set definition available, reset in dataSetDesign
        	design.setResultSets( null );
        }
        finally
        {
            closeConnection( conn );
        }
	}

    /**
     * 
     * @param dataSetDesign
     * @param md
     * @throws OdaException
     */
    private void setResultSetMetaData( DataSetDesign dataSetDesign,
			IResultSetMetaData md ) throws OdaException
	{
    	
		ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

		ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE.createResultSetDefinition( );
		// jdbc does not support result set name
		resultSetDefn.setResultSetColumns( columns );
		// no exception; go ahead and assign to specified dataSetDesign
		dataSetDesign.setPrimaryResultSet( resultSetDefn );
		dataSetDesign.getResultSets( ).setDerivedMetaData( true );
	}
    
    /**
	 * merge paramter meta data between dataParameter and datasetDesign's
	 * parameter.
	 * 
	 * @param dataSetDesign
	 * @param md
	 * @throws OdaException
	 */
	private void mergeParameterMetaData( DataSetDesign dataSetDesign,
			IParameterMetaData md ) throws OdaException
	{
		DataSetParameters parameters = dataSetDesign.getParameters( );
		DataSetParameters dataSetParameter = DesignSessionUtil.toDataSetParametersDesign( md );
		if ( parameters == null
				|| parameters.getParameterDefinitions( ).size( ) == 0 )
		{
			if ( dataSetParameter != null )
			{
				Iterator iter = dataSetParameter.getParameterDefinitions( )
						.iterator( );
				while ( iter.hasNext( ) )
				{
					ParameterDefinition defn = (ParameterDefinition) iter.next( );
					proccessParamDefn( defn, dataSetParameter );
				}
			}
			dataSetDesign.setParameters( dataSetParameter );
		}
		else
		{
			int designParamSize = 0;
			if ( dataSetParameter != null )
				designParamSize = dataSetParameter.getParameterDefinitions( )
						.size( );
			int dataParamSize = parameters.getParameterDefinitions( ).size( );
			while ( designParamSize > dataParamSize )
			{
				ParameterDefinition defn = (ParameterDefinition) dataSetParameter.getParameterDefinitions( )
						.get( dataParamSize );

				proccessParamDefn( defn, parameters );
				parameters.getParameterDefinitions( ).add( defn );
				designParamSize--;
			}
		}
	}
    
    /**
	 * Process the parameter definition for some special case
	 * 
	 * @param defn
	 * @param parameters
	 */
	private void proccessParamDefn( ParameterDefinition defn,
			DataSetParameters parameters )
	{
		if ( defn.getAttributes( ).getName( ) == null
				|| defn.getAttributes( ).getName( ).trim( ).equals( "" ) )
			defn.getAttributes( ).setName( getUniqueName( parameters ) );
		// An interim solution for the parameter in/out mode,
		// because the validtion on parameter will throw an
		// exception
		if ( !defn.isSetInOutMode( ) )
		{
			defn.setInOutMode( defn.getInOutMode( ) );
		}
		if ( defn.getAttributes( ).getNativeDataTypeCode( ) == Types.NULL )
		{
			defn.getAttributes( ).setNativeDataTypeCode( Types.CHAR );
		}
	}

	/**
	 * Get a unique name for dataset parameter
	 * @param parameters
	 * @return
	 */
    protected final String getUniqueName( DataSetParameters parameters )
	{
		int n = 1;
		String prefix = "param"; //$NON-NLS-1$
		StringBuffer buf = new StringBuffer( );
		while ( buf.length( ) == 0 )
		{
			buf.append( prefix ).append( n++ );
			if ( parameters != null )
			{
				Iterator iter = parameters.getParameterDefinitions( )
						.iterator( );
				if ( iter != null )
				{
					while ( iter.hasNext( ) && buf.length( ) > 0 )
					{
						ParameterDefinition parameter = (ParameterDefinition) iter.next( );
						if ( buf.toString( )
								.equalsIgnoreCase( parameter.getAttributes( )
										.getName( ) ) )
						{
							buf.setLength( 0 );
						}
					}
				}
			}
		}
		return buf.toString( );
	}

	/**
	 * close the connection
	 * @param conn
	 */
    private void closeConnection( IConnection conn )
	{
		try
		{
			if ( conn != null )
				conn.close( );
		}
		catch ( OdaException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}
    
	/**
	 * 
	 * @param parent
	 * @return
	 */
	public Control createPageControl( Composite parent )
	{
		SashForm SashForm = new SashForm( parent, SWT.HORIZONTAL );
		SashForm.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		initialize( );

		initJdbcInfo( );

		createTableSelectionComposite( SashForm );

		// Populate the available Items
		populateAvailableDbObjects( );

		createTextualQueryComposite( SashForm );

		setSashFormWeights( SashForm );

		return SashForm;
	}
	
	/**
	 * Sets Splitter Weights.
	 * if left side is too wide,set weights with default value 40,60.  
	 * @param splitter
	 */
	private void setSashFormWeights(SashForm sashForm) {
		int leftWidth = sashForm.getChildren( )[0].computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).x;
		int totalWidth = sashForm.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		if ( (double) leftWidth / (double) totalWidth > 0.4 )
		{
			//if left side is too wide, set it to default value 40:60
			sashForm.setWeights( new int[]{
					40, 60
			} );
		}
		else
		{
			sashForm.setWeights( new int[]{
					leftWidth, totalWidth - leftWidth
			} );
		}
		
	}
	
	/**
	 * Creates the composite,  for displaying the list of available db objects
	 * @param parent
	 */
	private void createTableSelectionComposite( Composite parent )
	{
		Composite tablescomposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		
		tablescomposite.setLayout(layout);
		{
			GridData data = new GridData(GridData.FILL_BOTH);
			data.grabExcessVerticalSpace = true;
			tablescomposite.setLayoutData(data);
		}
		
		// Available Items 
		Label dataSourceLabel = new Label( tablescomposite, SWT.LEFT );
		dataSourceLabel.setText( JdbcPlugin.getResourceString( "tablepage.label.availableItems" ) );//$NON-NLS-1$
		{
			GridData data = new GridData();
			dataSourceLabel.setLayoutData(data);
		}
		
		availableDbObjectsTree = new Tree(tablescomposite, SWT.BORDER|SWT.MULTI );
		
		{
			GridData data = new GridData(GridData.FILL_BOTH);
			data.grabExcessHorizontalSpace = true;
			data.grabExcessVerticalSpace = true;
			data.heightHint = 150;
			availableDbObjectsTree.setLayoutData(data);
		}
		
		availableDbObjectsTree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) 
			{
				populateEventData( e );
				insertText( (String) e.data );
			}
		});
		availableDbObjectsTree.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
			  if ( event.widget.getClass() != null )
				 handleAvailabeTreeSelection();
			}

			private void handleAvailabeTreeSelection() {
				TreeItem items[] = availableDbObjectsTree.getSelection();	
				for ( int i = 0; i <items.length; i++ )
				{
					if ( items[i].getGrayed() )
					{
						availableDbObjectsTree.setRedraw(false);
						availableDbObjectsTree.deselectAll();
						availableDbObjectsTree.setRedraw(true);	
						availableDbObjectsTree.redraw();	
					}
				}
			}
		  });

		// Group for selecting the Tables etc
		// Searching the Tables and Views
		Group selectTableGroup = new Group(tablescomposite, SWT.FILL);
		{
			GridLayout groupLayout = new GridLayout();
			groupLayout.numColumns = 3;
			//groupLayout.horizontalSpacing = 10;
			groupLayout.verticalSpacing = 10;
			selectTableGroup.setLayout(groupLayout);
			
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			selectTableGroup.setLayoutData(data);
		}
		
		schemaLabel = new Label( selectTableGroup, SWT.LEFT );
		schemaLabel.setText( JdbcPlugin.getResourceString("tablepage.label.schema") );

		schemaCombo = new Combo( selectTableGroup, SWT.READ_ONLY );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		schemaCombo.setLayoutData( gd );
		enableSchemaComponent( isSchemaSupported );
	
		Label FilterLabel = new Label(selectTableGroup, SWT.LEFT);
		FilterLabel.setText(JdbcPlugin.getResourceString("tablepage.label.filter"));
		
		searchTxt = new Text(selectTableGroup, SWT.BORDER) ;
		{
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			searchTxt.setLayoutData(data);
		}
		
		// Select Type
		Label selectTypeLabel = new Label(selectTableGroup, SWT.NONE);
		selectTypeLabel.setText(JdbcPlugin.getResourceString("tablepage.label.selecttype"));
		
		// Filter Combo
		filterComboViewer = new ComboViewer(selectTableGroup, SWT.READ_ONLY);
		setFilterComboContents(filterComboViewer);
		filterComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	
		// Find Button
		Button findButton = new Button(selectTableGroup, SWT.NONE);
		findButton.setText(JdbcPlugin.getResourceString("tablepage.button.filter"));//$NON-NLS-1$
		
		// Add listener to the find button
		findButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				PlatformUI.getWorkbench( )
						.getDisplay( )
						.asyncExec( new Runnable( ) {

							public void run( )
							{
								populateAvailableDbObjects( );

							}

						} );
			}
		} );
		
		setupIdentifierQuoteStringCheckBox( selectTableGroup );
		
		setRootElement();
		
		//	 Create the drag source on the tree
		addDragSupportToTree();   
	}
	
	/**
	 * 
	 * @param group
	 */
	private void setupIdentifierQuoteStringCheckBox(Group group )
	{
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 3;
		identifierQuoteStringCheckBox = new Button( group, SWT.CHECK );
		identifierQuoteStringCheckBox.setText( JdbcPlugin.getResourceString( "tablepage.button.dnd" ) ); //$NON-NLS-1$
		identifierQuoteStringCheckBox.setSelection( false );
		identifierQuoteStringCheckBox.setLayoutData( layoutData );
	}
	
	/**
	 * 
	 * @param b
	 */
	private void enableSchemaComponent( boolean b )
	{
		if ( b )
		{
			this.schemaCombo.setEnabled( true );
			this.schemaLabel.setEnabled( true );
		}
		else
		{
			this.schemaCombo.removeAll( );
			this.schemaCombo.setEnabled( false );
			this.schemaLabel.setEnabled( false );
		}
	}

	/**
	 * 
	 * @param filterComboViewer
	 */
	private void setFilterComboContents(ComboViewer filterComboViewer)
	{
		if( filterComboViewer == null )
		{
			return;
		}
		
		ArrayList dbTypeList = new ArrayList();
		
		DbType tableType  = new DbType(DbType.TABLE_TYPE, JdbcPlugin.getResourceString("tablepage.text.tabletype"));
		DbType viewType = new DbType(DbType.VIEW_TYPE, JdbcPlugin.getResourceString("tablepage.text.viewtype"));
		DbType allType = new DbType(DbType.ALL_TYPE, JdbcPlugin.getResourceString("tablepage.text.All"));
		DbType procedureType = new DbType(DbType.PROCEDURE_TYPE,JdbcPlugin.getResourceString("tablepage.text.procedure"));
		// Populate the Types of Data bases objects which can be retrieved
		dbTypeList.add(allType);
		dbTypeList.add(tableType);
		dbTypeList.add(viewType);
		if(metaDataProvider.isSchemaSupported()) 
			dbTypeList.add(procedureType);
        filterComboViewer.setContentProvider(new IStructuredContentProvider(){

            public Object[] getElements(Object inputElement)
            {
                if(inputElement != null)
                {
                    return ((ArrayList)inputElement).toArray();
                }
                return new DbType[]{};
            }

            public void dispose()
            {
                // TODO Auto-generated method stub
                
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
            {
                // TODO Auto-generated method stub
                
            }
            
        });
        
        filterComboViewer.setLabelProvider(new LabelProvider(){
            public String getText(Object inputElement)
            {
                DbType dbType = (DbType)inputElement;
                return dbType.getName(); 
            }
            
        });
        
		filterComboViewer.setInput(dbTypeList);
		
		// Set the Default selection to the First Item , which is "All"
		filterComboViewer.getCombo().select(0);
	}

	/*
	 * This method is invoked when the find button is clicked
	 * It populates the Available Data Base obecets ( in the Tree control ) 
	 * 
	 */
	protected void populateAvailableDbObjects()
	{
		DataSetDesign dataSetDesign = getDataSetDesign( );

		DataSourceDesign curDataSourceDesign = dataSetDesign.getDataSourceDesign( );
		
		if ( curDataSourceDesign == prevDataSourceDesign )
		{
			if ( ( cachedSearchTxt == searchTxt.getText( ) || ( cachedSearchTxt != null && cachedSearchTxt.equals( searchTxt.getText( ) ) ) )
					&& ( cachedDbType == getSelectedDbType( ) || ( cachedDbType != null && cachedDbType.equals( getSelectedDbType( ) ) ) ) )
			{
				if ( schemaList != null && schemaList.size( ) > 0 )
				{
					if ( cachedSchemaComboIndex == schemaCombo.getSelectionIndex( ) )
					{
						return;
					}
				}
				else
					return;
			}
		}
		
		// Clear of the Old values in the Available Db objects 
		// in the tree
		
		RemoveAllAvailableDbObjects();
		
		setRootElement();
		setRefreshInfo();
		if ( isSchemaSupported )
		{
			populateSchemaList( );
		}
		else
		{
			populateTableList( );
		}
		addFetchDbObjectListener( );  
		
		// Set the focus on the root node
		if( rootNode != null )
		{
			selectNode(rootNode);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private DataSetDesign getDataSetDesign( )
	{
		if ( dataSetDesign == null )
			dataSetDesign = this.getInitializationDesign( );
		return dataSetDesign;
	}
	
	/**
	 * populate shema list if the schema is supported
	 *
	 */
	protected void populateSchemaList()
	{
		if ( rootNode != null )
		{
			removeTreeItem( rootNode );
		}
		getAvailableSchema();
		// If the schemaCombo have not be initialized yet.
		if ( schemaCombo.getItemCount() < 1)
		{
			schemaCombo.add( JdbcPlugin.getResourceString("tablepage.text.All") );
			schemaCombo.select( 0 );
			if( schemaList != null){
				Iterator it = schemaList.iterator( );
				while ( it.hasNext( ) )
					schemaCombo.add( it.next( ).toString( ) );
			}
		}
		ArrayList targetSchemaList = new ArrayList();	
		ArrayList schemaObjectList = new ArrayList();
		if(schemaList!=null)
		{
			int numberOfSchema = 0;
			Preferences preferences = JdbcPlugin.getDefault( )
					.getPluginPreferences( );
			if ( preferences.contains( DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA ) )
			{
				numberOfSchema = preferences.getInt( DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA );
			}
			else
			{
				numberOfSchema = DateSetPreferencePage.DEFAULT_MAX_NUM_OF_SCHEMA;
				preferences.setValue( DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA,
						numberOfSchema );
			}
			cachedSchemaComboIndex = schemaCombo.getSelectionIndex();
			if ( schemaCombo.getSelectionIndex() == 0)
			{
				targetSchemaList = schemaList;
			}
			else
			{
				targetSchemaList.add( schemaCombo.getItem( schemaCombo.getSelectionIndex() ));
				numberOfSchema = 1;
			}

			for ( int i = 0; i < targetSchemaList.size( ) && i < numberOfSchema; i++ )
			{
				String schemaName = (String) targetSchemaList.get( i );
				DbObject schemaObj = new DbObject( schemaName,
						schemaName,
						DbObject.SCHEMA_TYPE,
						schemaImage );
				schemaObjectList.add( schemaObj );
			}
			TreeItem[] items = Utility.createTreeItems( rootNode,
					schemaObjectList,
					SWT.NONE,
					schemaImage );
			if ( items != null && items.length > 0 )
			{
				availableDbObjectsTree.showItem( items[0] );
			}
		}
	}
	
	/*
	 * Sets the Root Element of the Available Data Sources
	 * This is usually the Name of the Catalog of the Database
	 */
	protected void setRootElement()
	{
		rootNode = new TreeItem(availableDbObjectsTree,SWT.NONE);
		rootNode.setImage(dataBaseImage);
		
		DataSourceDesign dataSourceHandle = this.getDataSetDesign( ).getDataSourceDesign( );
		
		rootNode.setText(dataSourceHandle.getName());
	}
	
	private void RemoveAllAvailableDbObjects( )
	{
		if ( availableDbObjectsTree != null )
			availableDbObjectsTree.removeAll( );
	}
	
	/**
	 *  Gets the list of schema objects 
	 */
	private void getAvailableSchema()
	{
		if (isSchemaSupported)
		{
			ResultSet schemas = metaDataProvider.getAllSchema();
			schemaList = createSchemaList( schemas );
		}
	}
	
	protected void populateTableList( String schemaName ,TreeItem schemaTreeItem )
	{
		if(schemaTreeItem!=null)
		{
			removeTreeItem( schemaTreeItem );
		}
		String namePattern = null;
		String[] tableType = null;
		cachedSearchTxt = searchTxt.getText();	
		namePattern = getTailoredSearchText( searchTxt.getText() );
		  
		String dbtype = getSelectedDbType( );
		cachedDbType = dbtype;
		if ( dbtype != null )
		{
			if ( DbType.TABLE_STRING.equalsIgnoreCase( dbtype )
					|| DbType.VIEW_STRING.equalsIgnoreCase( dbtype ) )
			{
				tableType = new String[]{
					dbtype
				};
			}
		}

	    String catalogName = metaDataProvider.getCatalog();
		ArrayList tableList = new ArrayList();
	
		ResultSet tablesRs = null;
		ArrayList procedureRs = null;
		if (schemaName != null && schemaName.trim().length() > 0)
		{
			// For each schema Get  the List of Tables
			
			{
				if( metaDataProvider.isProcedureSupported() )
					procedureRs = metaDataProvider.getAllProcedure( catalogName, schemaName, namePattern );
				if( !DbType.PROCEDURE_STRING.equalsIgnoreCase(dbtype))
					tablesRs = metaDataProvider.getAlltables(catalogName,schemaName,namePattern,tableType);	
				tableList = new ArrayList();

				try
				{
					// Create the schema Node
					Image image = tableImage;
							
					if ( tablesRs != null )
					{
						int numberOfTable;
						
						Preferences preferences = JdbcPlugin.getDefault( ).getPluginPreferences( );
						if ( preferences.contains( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA ) )
						{
							numberOfTable = preferences.getInt( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA );
						}
						else
						{
							numberOfTable = DateSetPreferencePage.DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA;
							preferences.setValue( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA,
									numberOfTable );
						}
						int count = 0;			
						
						while ( tablesRs.next( ) && count < numberOfTable)
						{
							// tablesRs.getString("TABLE_NAME") must be called
							// before
							// tablesRs.getString("TABLE_TYPE"). This is because
							// once using JDBC-ODBC-SQLSERVER
							// the index of "TABLE_NAME" is higher than that of
							// "TABLE_TYPE".And when connection
							// is built using JDBC-ODBC-SQLSERVER the
							// ResultSet.getString() method, if being called
							// use a low index, then called using a high index,
							// will result in an exception.
							String tableName = tablesRs.getString( "TABLE_NAME" );
							String type = tablesRs.getString( "TABLE_TYPE" );
							if ( type.equalsIgnoreCase( "SYSTEM TABLE" ) )
								continue;

							int dbType = DbObject.TABLE_TYPE;

							if ( type.equalsIgnoreCase( "TABLE" ) )
							{
								image = tableImage;
								dbType = DbObject.TABLE_TYPE;
							}
							else if ( type.equalsIgnoreCase( "VIEW" ) )
							{
								image = viewImage;
								dbType = DbObject.VIEW_TYPE;
							}

							DbObject dbObject = new DbObject( getTableNameWithSchema( schemaName,
									tableName ),
									tableName,
									dbType,
									image );
							tableList.add( dbObject );
							count ++;

						}
					}
					if ( needToCreateProcedureNode( dbtype, procedureRs ))
					{
						String fullyQualifiedTableName = "STORED PROCEDURES";
						if ( schemaName != null
								&& schemaName.trim( ).length( ) > 0 )
						{
							fullyQualifiedTableName = schemaName + "." + "STORED PROCEDURES";
						}
						DbObject dbObject = new DbObject( fullyQualifiedTableName,"STORED PROCEDURES", DbObject.PROCEDURE_TYPE, tableImage);

						tableList.add( dbObject );
					}

					if ( schemaTreeItem != null ) 
					{
						TreeItem item[] = Utility.createTreeItems( schemaTreeItem,
								tableList,
								SWT.NONE,
								null );
						//expand table TreeItem
						if ( expandDbObjectsTree && item != null && item.length > 0 )
						{
							availableDbObjectsTree.showItem( item[0] );
						}
					}
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param schemaName
	 * @param tableName
	 * @return
	 */
	private String getTableNameWithSchema( String schemaName,
			String tableName )
	{
		String tableNameWithSchema = tableName;

		if ( schemaName != null && schemaName.trim( ).length( ) > 0 )
		{
			tableNameWithSchema = schemaName + "." + tableName;
		}

		return tableNameWithSchema;
	}
	
	/**
	 * if the schema is not support, populate the table list
	 *
	 */
	protected void populateTableList( )
	{
		if ( rootNode != null )
		{
			removeTreeItem( rootNode );
		}
		ResultSet tablesRs = null;
		ArrayList procedureRs = null;
		String catalogName = metaDataProvider.getCatalog( );

		String namePattern = null;
		String[] tableType = null;
		cachedSearchTxt = searchTxt.getText( );
		namePattern = getTailoredSearchText( searchTxt.getText( ) );

		String dbtype = getSelectedDbType( );
		cachedDbType = dbtype;
		
		if ( dbtype != null )
		{
			if ( DbType.TABLE_STRING.equalsIgnoreCase( dbtype )
					|| DbType.VIEW_STRING.equalsIgnoreCase( dbtype ) )
			{
				tableType = new String[]{
					dbtype
				};
			}
		}

		if ( metaDataProvider.isProcedureSupported( ) )
			procedureRs = metaDataProvider.getAllProcedure( catalogName,
					null,
					namePattern );
		if ( !DbType.PROCEDURE_STRING.equalsIgnoreCase( dbtype ) )
			tablesRs = metaDataProvider.getAlltables( catalogName,
					null,
					namePattern,
					tableType );

		if ( tablesRs == null && procedureRs == null )
		{
			return;
		}
		try
		{
			Image image = tableImage;
		
			if ( tablesRs != null )
			{
				int numberOfTable;
				
				Preferences preferences = JdbcPlugin.getDefault( ).getPluginPreferences( );
				if ( preferences.contains( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA ) )
				{
					numberOfTable = preferences.getInt( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA );
				}
				else
				{
					numberOfTable = DateSetPreferencePage.DEFAULT_MAX_NUM_OF_TABLE_EACH_SCHEMA;
					preferences.setValue( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA,
							numberOfTable );
				}
				int count = 0;
				tableList = new ArrayList( );
				while ( tablesRs.next( ) && count < numberOfTable )
				{
					String tableName = tablesRs.getString( "TABLE_NAME" );
					String type = tablesRs.getString( "TABLE_TYPE" );//$NON-NLS-1$
					if ( type.equalsIgnoreCase( "SYSTEM TABLE" ) )
						continue;
					// String SchemaName =
					// tablesRs.getString("TABLE_SCHEM");//$NON-NLS-1$
					int dbType = DbObject.TABLE_TYPE;

					if ( type.equalsIgnoreCase( "TABLE" ) )
					{
						image = tableImage;
						dbType = DbObject.TABLE_TYPE;
					}
					else if ( type.equalsIgnoreCase( "VIEW" ) )
					{
						image = viewImage;
						dbType = DbObject.VIEW_TYPE;
					}

					DbObject dbObject = new DbObject( getTableNameWithSchema( null,
							tableName ),
							tableName,
							dbType,
							image );
					tableList.add( dbObject );
					count++;
				}
			}
			if ( needToCreateProcedureNode( dbtype, procedureRs ) )
			{
				String fullyQualifiedTableName = "STORED PROCEDURES";

				DbObject dbObject = new DbObject( fullyQualifiedTableName,
						"STORED PROCEDURES",
						DbObject.PROCEDURE_TYPE,
						tableImage );

				tableList.add( dbObject );
			}
			TreeItem item[] = Utility.createTreeItems( rootNode,
					tableList,
					SWT.NONE,
					null );

			// expand table TreeItem
			if ( item != null && item.length > 0 )
				availableDbObjectsTree.showItem( item[0] );

			// Add listener to display the column names when expanded
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
	}


	/**
	 * @param namePattern
	 * @return
	 */
	private String getTailoredSearchText( String namePattern )
	{
		if ( namePattern != null )
		{
			if ( namePattern.lastIndexOf( '%' ) == -1 )
			{
				namePattern = namePattern + "%";
			}
		}else
		{
			namePattern = "%";
		}
		
		return namePattern;
	}

	/**
	 * @param dbtype
	 * @param procedureRs
	 * @return
	 */
	private boolean needToCreateProcedureNode( String dbtype, ArrayList procedureRs )
	{
		return procedureRs!=null&& procedureRs.size()>0  && (DbType.ALL_STRING.equalsIgnoreCase(dbtype)||DbType.PROCEDURE_STRING.equalsIgnoreCase(dbtype) );
	}
	
	// Connects the metadata provider to the specified data source
	protected Connection connectMetadataProvider( IMetaDataProvider metadata, DataSourceDesign dataSourceDesign )
	{
		return metadata.connect( dataSourceDesign );
	}
	
	private void initialize()
	{
//		dataSourceImage = JFaceResources.getImage( PAGE_ICON );
		
		tableImage = JFaceResources.getImage( TABLE_ICON );
		
		viewImage = JFaceResources.getImage( VIEW_ICON );
		
		schemaImage = JFaceResources.getImage(SCHEMA_ICON);
		
		dataBaseImage = JFaceResources.getImage(DATABASE_ICON);
		
		columnImage = JFaceResources.getImage(COLUMN_ICON);
		
	}

	/**
	 *  Initializes the Jdbc related information , used  by this page
	 * ( such as the Jdbc Connection , Catalog Name etc )
	 *
	 */
	protected void initJdbcInfo()
	{
		createMetaDataProvider( );

		prevDataSourceDesign = this.getDataSetDesign( ).getDataSourceDesign( );
		jdbcConnection = connectMetadataProvider( metaDataProvider, prevDataSourceDesign);
		
		validConnection = (jdbcConnection == null) ? false: true; 
		
		try
		{
			if ( jdbcConnection != null )
			{
				
				// Check if schema is supported
				isSchemaSupported = metaDataProvider.isSchemaSupported( );

			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.showException( this.getShell( ),
					JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
					e.getLocalizedMessage( ),
					e );
		}
	}
	/**
	 *  Create Metadata Provider
	 */
	protected void createMetaDataProvider( )
	{
		if ( metaDataProvider == null )
		{
			metaDataProvider = new JdbcMetaDataProvider(null);		
		}
	}
	
	/**
	 *  Initializes the Jdbc related information , used  by this page
	 * ( such as the Jdbc Connection , Catalog Name etc )
	 * @param curDataSourceHandle
	 *
	 */
	protected void resetJdbcInfo(DataSourceDesign curDataSourceDesign)
	{
		if( metaDataProvider != null )
		{
			metaDataProvider.closeConnection( );
			metaDataProvider = null;
			createMetaDataProvider( );
			jdbcConnection = connectMetadataProvider( metaDataProvider, curDataSourceDesign);
			
			// Clear the Table list and the schema List
			tableList = null;
			schemaList = null;
			schemaCombo.removeAll();
		}
		
		try
		{
			if ( jdbcConnection != null)
			{
				isSchemaSupported = metaDataProvider.isSchemaSupported();
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.showException( this.getShell( ),
					JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
					e.getLocalizedMessage( ),
					e );

		}
	}
	

	
	/**
	 *  Called to indicate that the process of getting the available Db objects
	 *   is in progress
	 *
	 */
	private void setRefreshInfo()
	{
		if ( rootNode == null )
		{
			return;
		}
		
		TreeItem item = new TreeItem(rootNode,0);
		item.setText(JdbcPlugin.getResourceString("tablepage.refreshing"));
	}

	private void selectNode(TreeItem item)
	{
		TreeItem[] selectedItem = new TreeItem[1];
		selectedItem[0] = item;
		availableDbObjectsTree.setSelection(selectedItem);
		availableDbObjectsTree.setFocus();

	}

	/**
	 * @param schemaRs: The ResultSet containing the List of schema
	 * @return A List of schema names
	 */
	private ArrayList createSchemaList(ResultSet schemaRs)
	{
		if ( schemaRs == null )
		{
			return null;
		}
		
		ArrayList schemas = new ArrayList();
		ArrayList allSchemas = new ArrayList();
		try
		{
			while( schemaRs.next() )
			{
				allSchemas.add( schemaRs.getString("TABLE_SCHEM") );
			}
			
			ResultSet rs = null;
			Iterator it = allSchemas.iterator();
			
			while( it.hasNext())
			{
				String schema = it.next().toString();
				rs = metaDataProvider.getAlltables( metaDataProvider.getCatalog( ),
						schema,
						"%",
						new String[]{
								"TABLE", "VIEW"
						} );				
				boolean hasNonSystemTable = false;				
				if ( rs != null )
				{
					while ( rs.next( ) )
					{
						if ( !"SYSTEM TABLE".equalsIgnoreCase( rs.getString( "TABLE_TYPE" ) ) )
						{
							hasNonSystemTable = true;
							break;
						}
					}
				}				
				if ( hasNonSystemTable )
				{
					schemas.add( schema );//$NON-NLS-1$					
				}
			}
		}
		catch( SQLException e)
		{
			e.printStackTrace();
		}
		
		return schemas;
	}
	
	/**
	 * 
	 * @return The Type of the object selected in the type combo ( Can be one of the following )
	 *   1) TABLE
	 *   2) VIEW
	 *   3) ALL
	 */
	private String  getSelectedDbType()
	{
		
		IStructuredSelection selection = (IStructuredSelection)filterComboViewer.getSelection();
		String type = DbType.ALL_STRING;
		if(selection != null && selection.getFirstElement() != null )
		{
			DbType dbType =  (DbType)selection.getFirstElement();
			
			switch ( dbType.getType())
			{
				case DbType.TABLE_TYPE:
					type = DbType.TABLE_STRING;
					break;
				case DbType.VIEW_TYPE:
					type = DbType.VIEW_STRING;
					break;
				case DbType.PROCEDURE_TYPE:
					type = DbType.PROCEDURE_STRING;
					break;
			}
		}
		
		return type;
	}

	/**
	* @param item A tree Item which has to be tested
	* @return if the TreeItem represents a Schema node
	*/
	protected boolean isSchemaNode( TreeItem item )
	{
		if ( item != null && isSchemaSupported )
		{
			if (item.getParentItem() == rootNode)
			{
				return true;
			}
		}
		return false;
	}
	
	private void refreshPage() 
	{

		// Get the currently selected Data Source
		DataSourceDesign curDataSourceDesign = this.getDataSetDesign( )
				.getDataSourceDesign( );
		
		if( curDataSourceDesign != prevDataSourceDesign )
		{
			RemoveAllAvailableDbObjects();
			resetJdbcInfo(curDataSourceDesign);
			enableSchemaComponent( isSchemaSupported );
			setRootElement();
            sourceViewerConfiguration.getContentAssistProcessor().setDataSourceHandle(curDataSourceDesign);
			
			populateAvailableDbObjects();
			prevDataSourceDesign = curDataSourceDesign;
		}
	}	
	
	/**
	 *
	 */
	private void addFetchDbObjectListener()
	{
		
		availableDbObjectsTree.addListener(SWT.Expand, new Listener(){
			
			/*
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
			 */
			public void handleEvent( final Event event )
			{
				TreeItem item = (TreeItem)event.item;
				BusyIndicator.showWhile( item.getDisplay( ), new Runnable( ) {
					/*
					 * @see java.lang.Runnable#run()
					 */
					public void run( )
					{
						showTable( event );
					}
				} );
			}
			
			/**
			 * @param event
			 */
			private void showTable(Event event)
			{
				TreeItem item = (TreeItem)event.item;
				if (item == null) return;
				
				if( item == rootNode )
					return;				
				if ( isSchemaNode( item ) )
				{
					populateTableList( item.getText( ), item );
					return;
				}//TODO
				
				String tableName = Utility.getTreeItemsName( item );

				String catalogName = metaDataProvider.getCatalog();
				String schemaName = null;
					
				String schemaSeparator = ".";
					
				if (metaDataProvider.isSchemaSupported())
				{
					// remove the schema name from the fully qualified name
					int index = -1;
					if ((index = tableName.lastIndexOf(schemaSeparator)) != -1)
					{
						schemaName = tableName.substring(0, index);
						tableName = tableName.substring( index + 1);
					}
				}

				if ( item.getData( ) instanceof DbObject )
				{
					DbObject obj = (DbObject) item.getData( );
					if ( obj.getType( ) == DbObject.TABLE_TYPE
							|| obj.getType( ) == DbObject.VIEW_TYPE )
					{
						ArrayList columnList = metaDataProvider.getColumns( catalogName,
								schemaName,
								tableName,
								null );
						TreeItem[] items = item.getItems( );
						if ( items != null )
						{
							for ( int i = 0; i < items.length; i++ )
							{
								items[i].dispose( );
							}
						}
						Utility.createTreeItems( item,
								columnList,
								SWT.NONE,
								columnImage );

					}
					else if ( obj.getType( ) == DbObject.PROCEDURE_TYPE )
					{
						ArrayList procedureList = metaDataProvider.getAllProcedure( catalogName,
								schemaName,
								getTailoredSearchText( searchTxt.getText() ) );
						TreeItem[] items = item.getItems( );
						if ( items != null )
						{
							for ( int i = 0; i < items.length; i++ )
							{
								items[i].dispose( );
							}
						}
						Utility.createTreeItems( item,
								procedureList,
								SWT.NONE,
								columnImage );
						//expand procedure TreeItem

					}
				}
				else if ( item.getData( ) instanceof Procedure )
				{
					Procedure obj = (Procedure) item.getData( );
					{
						ArrayList columnList = metaDataProvider.getProcedureColumns( obj.getCatalog( ),
								schemaName,
								tableName,
								getTailoredSearchText(null) );
						TreeItem[] items = item.getItems( );
						if ( items != null )
						{
							for ( int i = 0; i < items.length; i++ )
							{
								items[i].dispose( );
							}
						}
						Utility.createTreeItems( item,
								columnList,
								SWT.NONE,
								columnImage );
					}
				}
			}
		});
	}
	

	/**
	 * Adds drag support to tree..Must set tree before execution.
	 */
	public void addDragSupportToTree( )
	{

		DragSource dragSource = new DragSource( availableDbObjectsTree, DND.DROP_COPY );
		dragSource.setTransfer( new Transfer[]{TextTransfer.getInstance( )} );
		dragSource.addDragListener( new DragSourceAdapter( ) {

			public void dragStart( DragSourceEvent event )
			{
				TreeItem[] selection = availableDbObjectsTree.getSelection( );
				if ( selection.length > 0 )
				{
					if ( selection[0].getData( ) instanceof DbObject )
					{
						if ( ( (DbObject) selection[0].getData( ) ).getType( ) == DbObject.PROCEDURE_TYPE )
						{
							event.doit = false;
							return;
						}
					}
					else if ( selection[0].getData( ) instanceof ProcedureParameter )
					{
						event.doit = false;
						return;
					}
				}
				else if ( selection.length <= 0
						|| selection[0].getData( ) == null )
				{
					event.doit = false;
					return;
				}
			}

			public void dragSetData( DragSourceEvent event )
			{
				if ( TextTransfer.getInstance( )
						.isSupportedType( event.dataType ) )
				{
					populateEventData( event );
				}
			}
		} );
	}
	
	private void populateEventData( TypedEvent event )
	{
		TreeItem[] selection = availableDbObjectsTree.getSelection( );
		if ( selection.length > 0 )
		{
			Object obj = selection[0].getData( );
			// table
			if ( obj instanceof DbObject )
			{
				event.data = getDnDString( ( (DbObject) obj ).getName( ) );
			}
			// stored procedure
			else if ( obj instanceof Procedure )
			{
				event.data = getDnDString( ( (Procedure) obj ).getProcedureNameWithSchema( ) );
			}
			// column
			else
			{
				event.data = getDnDString( selection[0].getData( ) );
			}
		}
	}
	
	/**
	 * @param obj
	 * @return
	 */
	private Object getDnDString(Object obj )
	{
		if ( !identifierQuoteStringCheckBox.getSelection( )
				|| !( obj instanceof String ) )
			return obj;

		String identifierQuoteString = "";
		String dndString = (String) obj;
		try
		{
			identifierQuoteString = metaDataProvider.getMetaData( )
					.getIdentifierQuoteString( );
		}
		catch ( SQLException e )
		{
			identifierQuoteString = " ";
		}

		if ( !identifierQuoteString.equals( " " ) )
		{
			if ( dndString.indexOf( "." ) == -1 )
				return identifierQuoteString + dndString + identifierQuoteString;

			String[] str = dndString.split( "[.]" );
			dndString = "";

			for ( int i = 0; i < str.length; i++ )
			{
				dndString += identifierQuoteString
						+ str[i] + identifierQuoteString + ".";
			}
			return dndString.substring( 0, dndString.lastIndexOf( "." ) );
		}

		return dndString;
	}
	
	/**
	 * Adds drop support to viewer.Must set viewer before execution.
	 *  
	 */
	public void addDropSupportToViewer( )
	{
		final StyledText text = viewer.getTextWidget( );
		DropTarget dropTarget = new DropTarget( text, DND.DROP_COPY | DND.DROP_DEFAULT );
		dropTarget.setTransfer( new Transfer[]{TextTransfer.getInstance( )} );
		dropTarget.addDropListener( new DropTargetAdapter( ) {

			public void dragEnter( DropTargetEvent event )
			{
				text.setFocus( );
				if ( event.detail == DND.DROP_DEFAULT )
					event.detail = DND.DROP_COPY;
				if ( event.detail != DND.DROP_COPY )
					event.detail = DND.DROP_NONE;
			}

			public void dragOver( DropTargetEvent event )
			{
				event.feedback = DND.FEEDBACK_SCROLL
						| DND.FEEDBACK_INSERT_BEFORE;
			}

			public void dragOperationChanged( DropTargetEvent event )
			{
				dragEnter( event );
			}

			public void drop( DropTargetEvent event )
			{
				if ( event.data instanceof String )
					insertText( (String) event.data );
			}
		} );
	}

	/**
	 * Insert a text string into the text area
	 * 
	 * @param text
	 */
	private void insertText( String text )
	{
		if ( text == null )
			return;

		StyledText textWidget = viewer.getTextWidget( );
		int selectionStart = textWidget.getSelection( ).x;
		textWidget.insert( text );
		textWidget.setSelection( selectionStart + text.length( ) );
		textWidget.setFocus( );
	}
	
	/**
	 * @param lineText
	 * @return
	 */
	private int[] getBidiLineSegments( String lineText )
	{
		int[] seg = null;
		if ( lineText != null
				&& lineText.length( ) > 0
				&& !new Bidi( lineText, Bidi.DIRECTION_LEFT_TO_RIGHT ).isLeftToRight( ) )
		{
			List list = new ArrayList( );

			// Punctuations will be regarded as delimiter so that different
			// splits could be rendered separately.
			Object[] splits = lineText.split( "\\p{Punct}" );

			// !=, <> etc. leading to "" will be filtered to meet the rule that
			// segments must not have duplicates.
			for ( int i = 0; i < splits.length; i++ )
			{
				if ( !splits[i].equals( "" ) )
					list.add( splits[i] );
			}
			splits = list.toArray( );

			// first segment must be 0
			// last segment does not necessarily equal to line length
			seg = new int[splits.length + 1];
			for ( int i = 0; i < splits.length; i++ )
			{
				seg[i + 1] = lineText.indexOf( (String) splits[i], seg[i] )
						+ ( (String) splits[i] ).length( );
			}
		}

		return seg;
	}

// private boolean isValidConnection()
//	{
//		prevDataSourceHandle = (OdaDataSourceHandle) ((OdaDataSetHandle) getContainer( ).getModel( )).getDataSource();
//		metaDataProvider = new JdbcMetaDataProvider(null);
//		Connection jdbcConnection = connectMetadataProvider( metaDataProvider, prevDataSourceHandle);
//		validConnection = ( jdbcConnection == null) ? false: true;
//		return validConnection;
//	}

	/**
	 * Creates the textual query editor 
	 * @param parent
	 */
	private void createTextualQueryComposite( Composite parent )
	{
		
        Composite composite = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);
        
		CompositeRuler ruler = new CompositeRuler( );
		LineNumberRulerColumn lineNumbers = new LineNumberRulerColumn( );
		ruler.addDecorator( 0, lineNumbers );
		viewer = new SourceViewer( composite, ruler, SWT.H_SCROLL
				| SWT.V_SCROLL );
		sourceViewerConfiguration = new JdbcSQLSourceViewerConfiguration( this.getDataSetDesign( ) );
		viewer.configure( sourceViewerConfiguration );
		
		doc = new Document( getQueryText() );
		FastPartitioner partitioner = new FastPartitioner( new SQLPartitionScanner( ),
				new String[]{
						SQLPartitionScanner.SINGLE_LINE_COMMENT1,
						SQLPartitionScanner.SINGLE_LINE_COMMENT2,
						SQLPartitionScanner.MULTI_LINE_COMMENT,
						IDocument.DEFAULT_CONTENT_TYPE
				} );
		partitioner.connect( doc );
		doc.setDocumentPartitioner( partitioner );
		viewer.setDocument( doc );
		viewer.getTextWidget( ).setFont( JFaceResources.getTextFont( ) );
		viewer.getTextWidget( )
				.addBidiSegmentListener( new BidiSegmentListener( ) {
					/*
					 * @see org.eclipse.swt.custom.BidiSegmentListener#lineGetSegments(org.eclipse.swt.custom.BidiSegmentEvent)
					 */
					public void lineGetSegments( BidiSegmentEvent event )
					{
						event.segments = getBidiLineSegments( event.lineText );
					}
				} );
		attachMenus( viewer );
        
        GridData data = new GridData(GridData.FILL_BOTH);
        viewer.getControl().setLayoutData(data);
        
        // Add drop support to the viewer
        addDropSupportToViewer();
        
        if(isExternalEditorConfigured())
        {
            Button btnExternalEditor = new Button(composite, SWT.NONE);
            btnExternalEditor.setText("Edit with external editor");
            btnExternalEditor.addSelectionListener(this);
        }
        
        // add support of additional accelerated key
        viewer.getTextWidget( ).addKeyListener( new KeyListener( ) {
			public void keyPressed( KeyEvent e )
			{
				if ( isUndoKeyPress( e ) )
				{
					viewer.doOperation( ITextOperationTarget.UNDO );
				}
				else if ( isRedoKeyPress( e ) )
				{
					viewer.doOperation( ITextOperationTarget.REDO );
				}
			}
			private boolean isUndoKeyPress( KeyEvent e )
			{
				// CTRL + z
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'z' ) || ( e.keyCode == 'Z' ) );
			}
			private boolean isRedoKeyPress( KeyEvent e )
			{
				// CTRL + y
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'y' ) || ( e.keyCode == 'Y' ) );
			}
			public void keyReleased( KeyEvent e )
			{
				// do nothing
			}
		} );
	}
    
    private final boolean isExternalEditorConfigured()
    {
    	return false;
//        DataSourceDesign handle = this.getDataSetDesign( ).getDataSourceDesign( );
//        String editorName = Utility.getUserProperty(handle, ExternalEditorPreferenceManager.PROPERTY_NAME_PREFIX + "externaleditortype");
//        preference = ExternalEditorPreferenceManager.getInstance().getEditor(editorName); 
//        return (preference != null && preference.canBeLaunched(this.getDataSetDesign( )));
    }

	private final void attachMenus( SourceViewer viewer )
	{
		StyledText widget = viewer.getTextWidget( );
		MenuManager manager = new MenuManager( );
		Separator separator = new Separator( "undo" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "copy" );//$NON-NLS-1$
		manager.add( separator );
		separator = new Separator( "select" );//$NON-NLS-1$
		manager.add( separator );
		manager.appendToGroup( "undo", getAction( "undo", viewer, JdbcPlugin.getResourceString( "sqleditor.action.undo" ), ITextOperationTarget.UNDO ) );//$NON-NLS-1$
		manager.appendToGroup( "undo", getAction( "redo", viewer, JdbcPlugin.getResourceString( "sqleditor.action.redo" ), ITextOperationTarget.REDO ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "cut", viewer, JdbcPlugin.getResourceString( "sqleditor.action.cut" ), ITextOperationTarget.CUT ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "copy", viewer, JdbcPlugin.getResourceString( "sqleditor.action.copy" ), ITextOperationTarget.COPY ) );//$NON-NLS-1$
		manager.appendToGroup( "copy", getAction( "paste", viewer, JdbcPlugin.getResourceString( "sqleditor.action.paste" ), ITextOperationTarget.PASTE ) );//$NON-NLS-1$
		manager.appendToGroup( "select", getAction( "selectall", viewer, JdbcPlugin.getResourceString( "sqleditor.action.selectAll" ), ITextOperationTarget.SELECT_ALL ) );//$NON-NLS-1$
		Menu menu = manager.createContextMenu( widget );

		manager.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				Enumeration elements = htActions.elements( );
				while ( elements.hasMoreElements( ) )
				{
					SQLEditorAction action = (SQLEditorAction) elements.nextElement( );
					action.update( );
				}
			}
		} );
		widget.setMenu( menu );
	}

	/**
	 * 
	 * @param id
	 * @param viewer
	 * @param name
	 * @param operation
	 * @return
	 */
	private final SQLEditorAction getAction( String id, SourceViewer viewer,
			String name, int operation )
	{
		SQLEditorAction action = (SQLEditorAction) htActions.get( id );
		if ( action == null )
		{
			action = new SQLEditorAction( viewer, name, operation );
			htActions.put( id, action );
		}
		return action;
	}

    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.DataSetWizardPageCore#refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
     */
    protected void refresh( DataSetDesign dataSetDesign )
	{
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "dataset.editor.page.query" );
		setMessage( DEFAULT_MESSAGE );
		refreshPage( );
		prepareUI( );
	}
	
	/**
	 * Prepare UI when pageActivated event is invoked Following things will be
	 * done: Set StyledText content Set StyledText as focus
	 * 
	 */
	private void prepareUI()
	{
		StyledText styledText = viewer.getTextWidget( );
		String queryText = styledText.getText( );
		this.formerQueryTxt = queryText;
		if ( queryText != null
				&& queryText.equalsIgnoreCase( getQueryPresetTextString( ) ) )
		{
			String[] lines = getQueryPresetTextArray( );
			if ( lines != null && lines.length > 0 )
				styledText.setSelection( lines[0].length( ) + 1,
						lines[0].length( ) + 1 );
		}
		styledText.setFocus();
	}
	
	/**
	 * return the query text. If the query text is empty then return the pre-defined pattern
	 * 
	 * @return
	 */
	private String getQueryText( )
	{
		String queryText = this.getDataSetDesign( ).getQueryText( );
		if ( queryText != null && queryText.trim( ).length( ) > 0 )
			return queryText;

		return getQueryPresetTextString( );
	}
	
	/**
	 * Return pre-defined query text pattern with every element in a cell.
	 * 
	 * @return pre-defined query text
	 */
	private String getQueryPresetTextString( )
	{
		String[] lines = getQueryPresetTextArray( );
		String result = "";
		if ( lines != null && lines.length > 0 )
		{
			for ( int i = 0; i < lines.length; i++ )
			{
				result = result
						+ lines[i] + ( i == lines.length - 1 ? " " : " \n" );
			}
		}
		return result;
	}
	
	/** 
	 * Return pre-defined query text pattern with every element in a cell in an Array
	 * 
	 * @return pre-defined query text in an Array
	 */
	private String[] getQueryPresetTextArray( )
	{
		// TODO: to be externalized
		final String[] lines;
		if ( this.getDataSetDesign( ).getOdaExtensionDataSetId( )
				.equals( "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet" ) )
			lines = new String[]{
				"{call procedure-name(arg1,arg2, ...)}"
			};
		else
			lines = new String[]{
					"select", "from"
			};
		return lines;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#cleanup()
	 */
	protected void cleanup( )
	{
		if ( metaDataProvider != null )
		{
			metaDataProvider.closeConnection( );
		}
	}

	/**
	 * Whether should the DatabaseObjectTree should be expanded.
	 * 
	 * @param expand True if the expanding is expected. Otherwise false.
	 */
	public void setDatabaseObjectTreeExpansion( boolean expand )
	{
		this.expandDbObjectsTree = expand;
	}
	
	/**
	 * remove the tree item's direct child treeItem, cause since 3.1,
	 * TreeItem.removeAll is supported.But in 3.0,this method is not supported.
	 * 
	 * @param treeItem
	 */
	private void removeTreeItem( TreeItem treeItem )
	{
		if ( treeItem.isDisposed( ) )
			return;
		TreeItem[] items = treeItem.getItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i] != null && !items[i].isDisposed( ) )
			{
				items[i].dispose( );
			}
		}
	}
	
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
//        if(preference != null)
//        {
//            try
//            {
//                this.getDataSetDesign( ).setQueryText( doc.get( ) );
//				String command = preference.getPreparedCommandLine( this.getDataSetDesign( ) );
//				Process process = Runtime.getRuntime( ).exec( command );
//                process.waitFor();
//                FileInputStream fis = new FileInputStream(preference.getTemporaryFile());
//                StringBuffer stringBuffer = new StringBuffer();
//                byte[] buf = new byte[10000];
//                int n = -1;
//                while((n = fis.read(buf)) != -1)
//                {
//                    stringBuffer.append(new String(buf, 0, n));
//                }
//                doc.set(stringBuffer.toString());
//                preference.getTemporaryFile().delete();
//                
//            }
//            catch (Exception e1)
//            {
//            	ExceptionHandler.showException( null, "title", "msg", e1 );
//            }
//        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }
	
}

class SQLEditorAction extends Action
{

	private int operationCode = -1;
	private SourceViewer viewer = null;

	public SQLEditorAction( SourceViewer viewer, String text, int operationCode )
	{
		super( text );
		this.operationCode = operationCode;
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		viewer.doOperation( operationCode );
	}

	public void update( )
	{
		setEnabled( viewer.canDoOperation( operationCode ) );
	}
	
 }

class DbType
{
	public static final int TABLE_TYPE = 0;
	public static final String TABLE_STRING = "TABLE";
	public static final int VIEW_TYPE = 1;
	public static final String VIEW_STRING = "VIEW";
	public static final int ALL_TYPE = 2;
	public static final String ALL_STRING = "ALL";
	public static final int PROCEDURE_TYPE = 3;
	public static final String PROCEDURE_STRING = "PROCEDURE";
	public static final int MAX_ITEMS_DISPLAY_COUNT = 500;

    private int type;
    private String name;
    
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public DbType(int type, String name)
    {
        super();
        this.type = type;
        this.name = name;
    }
    
}
 
