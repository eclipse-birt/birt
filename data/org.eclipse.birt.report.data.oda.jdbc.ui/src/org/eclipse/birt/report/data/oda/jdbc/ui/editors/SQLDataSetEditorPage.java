/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;


import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.externaleditor.ExternalEditorPreferenceManager;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.externaleditor.IExternalEditorPreference;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DbObject;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.designer.ui.editors.sql.SQLPartitionScanner;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.compare.Splitter;
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
import org.eclipse.jface.text.rules.DefaultPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
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
 * @version $Revision: 1.21 $ $Date: 2005/08/11 06:10:55 $
 */

public class SQLDataSetEditorPage extends AbstractPropertyPage implements SelectionListener
{
	private transient Document doc = null;
	private SourceViewer viewer = null;
	private Hashtable htActions = new Hashtable( );
    private transient IExternalEditorPreference preference = null;
    private TreeItem rootNode = null;
    private Text searchTxt = null;
    private boolean isSchemaSupported = false;
    private boolean expandDbObjectsTree = false;
    private Tree availableDbObjectsTree = null;
    private JdbcMetaDataProvider metaDataProvider = null;
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
	OdaDataSourceHandle prevDataSourceHandle = null;
	Connection jdbcConnection = null;
	boolean validConnection = false;
	
	private static String TABLE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.TableIcon";
	private static String VIEW_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.ViewIcon";
	private static String PAGE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.PageIcon";
	private static String SCHEMA_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.SchemaIcon";
	private static String DATABASE_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.DbIcon";
	private static String COLUMN_ICON = "org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage.ColumnIcon";
	
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


    
	/**
	 * @param pageName
	 */
	public SQLDataSetEditorPage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl( Composite parent )
	{
		Splitter splitter = new Splitter( parent, SWT.NONE );
		splitter.setOrientation( SWT.HORIZONTAL );
		splitter.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		initialize();
		
		initJdbcInfo();
		
		createTableSelectionComposite(splitter);
	
		// Populate the available Items
		populateAvailableDbObjects();		

		createTextualQueryComposite(splitter);

		setSplitterWeights( splitter );
			
		return splitter;
	}
	
	/**
	 * Sets Splitter Weights.
	 * In Eclipse 3.0.1, the comptuersize of splitter is not correct,
	 * so set weights with default value 40,60.  
	 * @param splitter
	 */
	private void setSplitterWeights(Splitter splitter) {
		int leftWidth = splitter.getChildren( )[0].computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).x;
		int totalWidth = splitter.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		if ( (double) leftWidth / (double) totalWidth > 0.4 )
		{
			//if left side is too wide, set it to default value 40:60
			splitter.setWeights( new int[]{
					40, 60
			} );
		}
		else
		{
			splitter.setWeights( new int[]{
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
			//data.heightHint = 150;
			availableDbObjectsTree.setLayoutData(data);
		}
		
		availableDbObjectsTree.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) 
			{
				//addTable();
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
		findButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
				 	PlatformUI.getWorkbench( ).getDisplay( ).asyncExec(
				 	new Runnable()
				 	{
						public void run() 
						{
							populateAvailableDbObjects();
							
						}

				 	}
				 	);
				 }
		
			 });
		

		
		setRootElement();
		
		
		//	 Create the drag source on the tree
		addDragSupportToTree();   
		

	}
	
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

		// Populate the Types of Data bases objects which can be retrieved
		dbTypeList.add(tableType);
		dbTypeList.add(viewType);
		dbTypeList.add(allType);

		
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
		
		// Set the Default selection to the First Item , which is "Table"
		filterComboViewer.getCombo().select(0);
        
        
		
	}

	/*
	 * This method is invoked when the find button is clicked
	 * It populates the Available Data Base obecets ( in the Tree control ) 
	 * 
	 */
	protected void populateAvailableDbObjects()
	{
		
		// Clear of the Old values in the Available Db objects 
		// in the tree
		
		RemoveAllAvailableDbObjects();
		
		setRootElement();
		setRefreshInfo();
		if ( isSchemaSupported )
		{
			getAvailableSchema();
			// If the schemaCombo have not be initialized yet.
			if ( schemaCombo.getItemCount() < 1)
			{
				schemaCombo.add( JdbcPlugin.getResourceString("tablepage.text.All") );
				schemaCombo.select( 0 );
				Iterator it = schemaList.iterator();
				while ( it.hasNext() )
					schemaCombo.add( it.next().toString() );
			}
			populateTableList();
		}
		else
		{
			populateTableList();
		}
		
		// Set the focus on the root node
		if( rootNode != null )
		{
			selectNode(rootNode);
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
		
		OdaDataSourceHandle dataSourceHandle = (OdaDataSourceHandle) ((OdaDataSetHandle) getContainer( ).getModel( )).getDataSource();
		
		rootNode.setText(dataSourceHandle.getName());
		
	}
	
	private void RemoveAllAvailableDbObjects()
	{
		availableDbObjectsTree.removeAll();
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
	
	protected void populateTableList()
	{
		 // Remove all the existing children of the root Node
		 if ( rootNode != null )
		 {
		 	availableDbObjectsTree.removeAll();
		 	setRootElement();
		 }

		  String namePattern = null;
		  String[] tableType = null;

		  
		  if ( searchTxt.getText().length() > 0 )
		  {
		  	namePattern = searchTxt.getText();
		  	// Add the % by default if there is no such pattern
		  	if ( namePattern != null )
		  	{
		  		if ( namePattern.lastIndexOf('%') == -1)
		  		{
		  			namePattern = namePattern + "%";
		  		}
		  	}
		  }
		  
		  String dbtype = getSelectedDbType();
		  if ( dbtype != null && ! DbType.ALL_STRING.equalsIgnoreCase(dbtype))
		  {
		  	tableType = new String[]{ dbtype };
		  }
		  

	    String catalogName = metaDataProvider.getCatalog();
		ArrayList tableList = new ArrayList();
		ArrayList targetSchemaList = new ArrayList();		
	
		if (schemaList != null && schemaList.size() > 0)
		{
			if ( schemaCombo.getSelectionIndex() == 0)
			{
				targetSchemaList = schemaList;
			}
			else
			{
				targetSchemaList.add( schemaCombo.getItem( schemaCombo.getSelectionIndex() ));
			}
			
			ResultSet tablesRs = null;
			// For each schema Get  the List of Tables
			int numTables = 0;
			//if ( schemaComboViewer.getSelection().)
	
			for( int i=0; i< targetSchemaList.size(); i++)
			{
				int count = 0;
				String schemaName = (String)targetSchemaList.get(i);
				tablesRs = metaDataProvider.getAlltables(catalogName,schemaName,namePattern,tableType);
				tableList = new ArrayList();
				if( tablesRs == null )
				{
					continue;
				}
	
				try
				{
					// Create the schema Node

					ArrayList schema = new ArrayList();
					TreeItem schemaTreeItem[] = null;
					Image image = tableImage;
					
					if( count == 0 )
					{
						schema.add(schemaName);
						schemaTreeItem = Utility.createTreeItems(rootNode, schema, SWT.NONE, schemaImage);
						//expand schema TreeItem
						if( schemaTreeItem != null && schemaTreeItem.length > 0)
							availableDbObjectsTree.showItem(schemaTreeItem[0]);
					}
					
					while( tablesRs.next()) 
					{
						String type = tablesRs.getString("TABLE_TYPE");
						if ( type.equalsIgnoreCase("SYSTEM TABLE"))
							continue;
						count++;
//						String SchemaName = tablesRs.getString("TABLE_SCHEM");//$NON-NLS-1$
						String tableName = tablesRs.getString("TABLE_NAME");//$NON-NLS-1$
						//$NON-NLS-1$
						
						int dbType = DbObject.TABLE_TYPE;
				
						if(type.equalsIgnoreCase("TABLE"))
						{
							image = tableImage;
							dbType = DbObject.TABLE_TYPE;
						}
						else if(type.equalsIgnoreCase("VIEW"))
						{
							image = viewImage;
							dbType = DbObject.VIEW_TYPE;
						}
						
						String fullyQualifiedTableName = tableName;
						if( schemaName != null && schemaName.trim().length() > 0)
						{
							fullyQualifiedTableName = schemaName + "." + tableName;
						}
						DbObject dbObject = new DbObject(fullyQualifiedTableName,tableName, dbType, image);
						tableList.add(dbObject);
						numTables ++;
						
					}
					
					if ( schemaTreeItem != null 
							&& schemaTreeItem.length > 0 ) 
					{
						TreeItem item[] = Utility.createTreeItems( schemaTreeItem[0],
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
		else
		{
			//ResultSet tablesRs = metaDataProvider.getAlltables(catalogName,null,namePattern,tableType);
			ResultSet tablesRs = metaDataProvider.getAlltables(catalogName,null,namePattern,tableType);
			if( tablesRs == null)
			{
				return;
			}
			try
			{
				Image image = tableImage;
				while( tablesRs.next())
				{
					String type = tablesRs.getString("TABLE_TYPE");//$NON-NLS-1$
					if ( type.equalsIgnoreCase("SYSTEM TABLE"))
						continue;
	//				String SchemaName = tablesRs.getString("TABLE_SCHEM");//$NON-NLS-1$
					String tableName = tablesRs.getString("TABLE_NAME");//$NON-NLS-1$
					int dbType = DbObject.TABLE_TYPE;
								
					if(type.equalsIgnoreCase("TABLE"))
					{
						image = tableImage;
						dbType = DbObject.TABLE_TYPE;
					}
					else if(type.equalsIgnoreCase("VIEW"))
					{
						image = viewImage;
						dbType = DbObject.VIEW_TYPE;
					}
					
					DbObject dbObject = new DbObject(tableName, tableName,dbType, image);
					tableList.add(dbObject);
				}
				
				TreeItem item[] = Utility.createTreeItems(rootNode, tableList, SWT.NONE, null);
				
				//expand table TreeItem
				if( item != null && item.length > 0)
					availableDbObjectsTree.showItem(item[0]);
				
				// Add listener to display the column names when expanded
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
		}

	
		// Add a listener for fetching columns
	   addFetchColumnListener();  

	}

	
	// Connects the metadata provider to the specified data source
	protected Connection connectMetadataProvider( JdbcMetaDataProvider metadata, OdaDataSourceHandle dataSourceHandle )
	{
		return metadata.connect( dataSourceHandle );
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
		if ( metaDataProvider == null )
		{
			metaDataProvider = new JdbcMetaDataProvider(null);		
		}

		prevDataSourceHandle = (OdaDataSourceHandle) ((OdaDataSetHandle) getContainer( ).getModel( )).getDataSource();
		jdbcConnection = connectMetadataProvider( metaDataProvider, prevDataSourceHandle);
		
		validConnection = (jdbcConnection == null) ? false: true; 
		
		try
		{
			if ( jdbcConnection != null )
			{
				
				// Check if schema is supported
				isSchemaSupported = metaDataProvider.isSchemaSupported();
 
			}
		}
		catch(Exception e)
		{
			ExceptionHandler.handle( e );
		}
	}
	/**
	 *  Initializes the Jdbc related information , used  by this page
	 * ( such as the Jdbc Connection , Catalog Name etc )
	 * @param curDataSourceHandle
	 *
	 */
	protected void resetJdbcInfo(OdaDataSourceHandle curDataSourceHandle)
	{
		if( metaDataProvider != null )
		{
			metaDataProvider.closeConnection();
			metaDataProvider = new JdbcMetaDataProvider(null);
			jdbcConnection = connectMetadataProvider( metaDataProvider, curDataSourceHandle);
			
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
		catch(Exception e)
		{
			ExceptionHandler.handle( e );
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
				case DbType.ALL_TYPE:
					type = null;
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
		OdaDataSourceHandle curDataSourceHandle = (OdaDataSourceHandle) ((OdaDataSetHandle) getContainer( ).getModel( )).getDataSource();
		
		if( curDataSourceHandle != prevDataSourceHandle )
		{
			RemoveAllAvailableDbObjects();
			resetJdbcInfo(curDataSourceHandle);
			enableSchemaComponent( isSchemaSupported );
			setRootElement();
            sourceViewerConfiguration.getContentAssistProcessor().setDataSourceHandle(curDataSourceHandle);
			prevDataSourceHandle = curDataSourceHandle;
			
			populateAvailableDbObjects();
		}
		
	}	
	
	private void addFetchColumnListener()
	{
		
		
		availableDbObjectsTree.addListener(SWT.Expand, new Listener(){

			public void handleEvent(Event event) {

				TreeItem item = (TreeItem)event.item;
				if (item == null) return;
				
				if (isSchemaNode(item) || (item == rootNode))
				{
					return;
				}
				
				String tableName = (String)item.getData();
					
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
				
					
				ArrayList columnList = metaDataProvider.getColumns(catalogName,schemaName, tableName,null);
				TreeItem[] items = item.getItems();
				if ( items != null )
				{
					for ( int i=0; i < items.length; i++)
					{
						items[i].dispose();
					}
				}
				Utility.createTreeItems(item, columnList, SWT.NONE, columnImage);
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
				if ( selection.length <= 0
						|| selection[0].getData( ) == null )
				{
					event.doit = false;
					return;
				}
			}

			public void dragSetData( DragSourceEvent event )
			{
				if ( TextTransfer.getInstance( ).isSupportedType(
						event.dataType ) )
				{
					TreeItem[] selection = availableDbObjectsTree.getSelection( );
					if ( selection.length > 0 )
					{
						event.data = selection[0].getData( );
					}
				}
			}
		} );
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
		StyledText textWidget = viewer.getTextWidget( );
		int selectionStart = textWidget.getSelection( ).x;
		textWidget.insert( text );
		textWidget.setSelection( selectionStart + text.length( ) );
		textWidget.setFocus( );
	}

//	private boolean isValidConnection()
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
        sourceViewerConfiguration = new JdbcSQLSourceViewerConfiguration( ( (OdaDataSetHandle) getContainer( ).getModel( ) ) );
		viewer.configure( sourceViewerConfiguration );
		
		doc = new Document( getQueryText() );
		DefaultPartitioner partitioner = new DefaultPartitioner( new SQLPartitionScanner( ),
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
        OdaDataSourceHandle handle = (OdaDataSourceHandle) ((OdaDataSetHandle) getContainer( ).getModel( )).getDataSource();
        String editorName = Utility.getUserProperty(handle, ExternalEditorPreferenceManager.PROPERTY_NAME_PREFIX + "externaleditortype");
        preference = ExternalEditorPreferenceManager.getInstance().getEditor(editorName); 
        return (preference != null && preference.canBeLaunched((OdaDataSetHandle) getContainer( ).getModel( )));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated( )
	{
		getContainer( ).setMessage( JdbcPlugin.getResourceString( "dataset.editor.page.query" ), IMessageProvider.NONE );//$NON-NLS-1$
		
		// If the Selected Data Source HAs changed then the 
		// Table Selection Page and the Textual query editor should reflect this change
		refreshPage();
		
		prepareUI();		
	}

	/**
	 * Prepare UI when pageActivated event is invoked
	 * Following things will be done:
	 * 		Set StyledText content
	 * 		Set StyledText as focus
	 * 		
	 */
	private void prepareUI()
	{	
		StyledText styledText = viewer.getTextWidget();
		String queryText = styledText.getText( );
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
		String queryText = ( (OdaDataSetHandle) getContainer( ).getModel( ) ).getQueryText( );
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
	private String[] getQueryPresetTextArray()
	{
		// TODO: to be externalized
		final String[] lines = new String[]{
				"select", "from"
		};
		return lines;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#getName()
	 */
	public String getName( )
	{
		return JdbcPlugin.getResourceString( "dataset.editor.page.query" );//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#canLeave()
	 */
	public boolean canLeave( )
	{
		try
		{
			( (OdaDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
		}
		catch ( SemanticException e )
		{
			return false;
		}
		return true;
	}

	/* 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		try
		{
			( (OdaDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
		}
		catch ( SemanticException e )
		{
			return false;
		}
		finally
		{
			cleanUp( );
		}
		return true;
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performCancel()
	 */
	public boolean performCancel( )
	{
		cleanUp();
		return super.performCancel();
	}

	/**
	 * CleanUp database connection
	 */
	private void cleanUp( )
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
    /*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
    public void widgetSelected(SelectionEvent e)
    {
        if(preference != null)
        {
            try
            {
                ( (OdaDataSetHandle) getContainer( ).getModel( ) ).setQueryText( doc.get( ) );
                String command = preference.getPreparedCommandLine((OdaDataSetHandle) getContainer( ).getModel( ));
                Process process = Runtime.getRuntime().exec(command);
                process.waitFor();
                FileInputStream fis = new FileInputStream(preference.getTemporaryFile());
                StringBuffer stringBuffer = new StringBuffer();
                byte[] buf = new byte[10000];
                int n = -1;
                while((n = fis.read(buf)) != -1)
                {
                    stringBuffer.append(new String(buf, 0, n));
                }
                doc.set(stringBuffer.toString());
                preference.getTemporaryFile().delete();
                
            }
            catch (Exception e1)
            {
                ExceptionHandler.handle(e1);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#getToolTip()
	 */
	public String getToolTip( )
	{
		// TODO: to be externalized
		return "Create or Edit an SQL SELECT statement";
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
	public static final int MAX_ITEMS_DISPLAY_COUNT = 500;

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

    int type;
    String name;

    public DbType(int type, String name)
    {
        super();
        this.type = type;
        this.name = name;
    }
    
}

