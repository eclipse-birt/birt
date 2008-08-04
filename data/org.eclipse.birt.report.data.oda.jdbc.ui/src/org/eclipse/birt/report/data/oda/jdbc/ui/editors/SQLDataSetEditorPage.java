/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.ChildrenAllowedNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.DBNodeUtil;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.FilterConfig;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.IDBNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.RootNode;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.FilterConfig.Type;
import org.eclipse.birt.report.data.oda.jdbc.ui.preference.DateSetPreferencePage;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.ExceptionHandler;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.IHelpConstants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.events.TypedEvent;
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
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

/**
 * The JDBC SQL DatasetEditor page which enable user to browse the catalog of
 * the selected data source and input the sql text. The page extends the
 * <code>DataSetWizardPage</code> it could be loaded as a custom page for jdbc
 * ui.
 */

public class SQLDataSetEditorPage extends DataSetWizardPage
{
	// composite in editor page
	private Document doc = null;
	private SourceViewer viewer = null;
	private Text searchTxt = null;
	private ComboViewer filterComboViewer = null;
	private Combo schemaCombo = null;
	
	private Label schemaLabel = null;
	private Tree availableDbObjectsTree = null;
	private Button identifierQuoteStringCheckBox = null;
	private Button showSystemTableCheckBox = null;
	private JdbcSQLSourceViewerConfiguration sourceViewerConfiguration = null;
	private DataSetDesign dataSetDesign;

	private static String DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "dataset.new.query" );//$NON-NLS-1$	

	private int maxSchemaCount;
	private int maxTableCountPerSchema;
	boolean prefetchSchema;

	private FilterConfig fc;

	String formerQueryTxt;

	/**
	 * constructor
	 * 
	 * @param pageName
	 */
	public SQLDataSetEditorPage( String pageName )
	{
		super( pageName );
	}

	public void readPreferences( )
	{
		Preferences preferences = JdbcPlugin.getDefault( )
				.getPluginPreferences( );
		if ( DateSetPreferencePage.ENABLED.equals( preferences.getString( DateSetPreferencePage.SCHEMAS_PREFETCH_CONFIG ) ) )
		{
			prefetchSchema = true;
		}
		maxSchemaCount = preferences.getInt( DateSetPreferencePage.USER_MAX_NUM_OF_SCHEMA );
		maxTableCountPerSchema = preferences.getInt( DateSetPreferencePage.USER_MAX_NUM_OF_TABLE_EACH_SCHEMA );
		if ( maxSchemaCount <= 0 )
		{
			maxSchemaCount = Integer.MAX_VALUE;
		}
		if ( maxTableCountPerSchema <= 0 )
		{
			maxTableCountPerSchema = Integer.MAX_VALUE;
		}
	}

	private void prepareJDBCMetaDataProvider( DataSetDesign dataSetDesign )
	{
		JdbcMetaDataProvider.createInstance( dataSetDesign );
		try
		{
			JdbcMetaDataProvider.getInstance( ).reconnect( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.showException( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					JdbcPlugin.getResourceString( "exceptionHandler.title.error" ),
					e.getLocalizedMessage( ),
					e );
		}
	}

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #createPageCustomControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPageCustomControl( Composite parent )
	{
		this.dataSetDesign = this.getInitializationDesign( );
		readPreferences( );
		prepareJDBCMetaDataProvider( dataSetDesign );
		setControl( createPageControl( parent ) );
		initializeControl( );
		this.formerQueryTxt = dataSetDesign.getQueryText( );
		Utility.setSystemHelp( getControl( ),
				IHelpConstants.CONEXT_ID_DATASET_JDBC );
	}

	/**
	 * create page control for sql edit page
	 * 
	 * @param parent
	 * @return
	 */
	private Control createPageControl( Composite parent )
	{
		Composite pageContainer = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 2;
		pageContainer.setLayout( layout );
		pageContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Control left = createDBMetaDataSelectionComposite( pageContainer );
		Sash sash = createSash( pageContainer );
		Control right = createTextualQueryComposite( pageContainer );
		setWidthHints( pageContainer, left, right, sash );
		addDragListerner( sash, pageContainer, left, right );
		return pageContainer;
	}

	/**
	 * @param pageContainer
	 * @param left
	 * @param right
	 */
	private void setWidthHints( Composite pageContainer, Control left,
			Control right, Sash sash )
	{
		int leftWidth = left.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		int totalWidth = pageContainer.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;

		if ( (double) leftWidth / (double) totalWidth > 0.4 )
		{
			// if left side is too wide, set it to default value 40:60
			totalWidth = leftWidth / 40 * 100;
			leftWidth = leftWidth
					- sash.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
			GridData data = (GridData) left.getLayoutData( );
			data.widthHint = leftWidth;
			data = (GridData) right.getLayoutData( );
			data.widthHint = (int) ( totalWidth * 0.6 );
		}
		else
		{
			GridData data = (GridData) left.getLayoutData( );
			data.widthHint = leftWidth;
			data = (GridData) right.getLayoutData( );
			data.widthHint = totalWidth - leftWidth;
		}
	}

	private Sash createSash( final Composite composite )
	{
		final Sash sash = new Sash( composite, SWT.VERTICAL );
		sash.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
		return sash;
	}

	/**
	 * 
	 * @param sash
	 * @param parent
	 * @param left
	 * @param right
	 */
	private void addDragListerner( final Sash sash, final Composite parent,
			final Control left, final Control right )
	{
		sash.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				if ( event.detail == SWT.DRAG )
				{
					return;
				}
				Sash sash = (Sash) event.widget;
				int shift = event.x - sash.getBounds( ).x;

				left.setSize( left.getSize( ).x + shift, left.getSize( ).y );
				right.setSize( right.getSize( ).x - shift, right.getSize( ).y );
				right.setLocation( right.getLocation( ).x + shift,
						right.getLocation( ).y );
				sash.setLocation( sash.getLocation( ).x + shift,
						sash.getLocation( ).y );
			}
		} );
	}

	/**
	 * initial dataset control
	 * 
	 */
	private void initializeControl( )
	{
		DEFAULT_MESSAGE = JdbcPlugin.getResourceString( "dataset.new.query" );
		setMessage( DEFAULT_MESSAGE, IMessageProvider.NONE );
		viewer.getTextWidget( ).setFocus( );
	}

	/**
	 * Creates the composite, for displaying the list of available db objects
	 * 
	 * @param parent
	 */
	private Control createDBMetaDataSelectionComposite( Composite parent )
	{
		boolean supportsSchema = JdbcMetaDataProvider.getInstance( )
				.isSupportSchema( );
		boolean supportsProcedure = JdbcMetaDataProvider.getInstance( )
				.isSupportProcedure( );
		Composite tablescomposite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );

		tablescomposite.setLayout( layout );
		{
			GridData data = new GridData( GridData.FILL_VERTICAL );
			data.grabExcessVerticalSpace = true;
			tablescomposite.setLayoutData( data );
		}

		// Available Items
		Label dataSourceLabel = new Label( tablescomposite, SWT.LEFT );
		dataSourceLabel.setText( JdbcPlugin.getResourceString( "tablepage.label.availableItems" ) );//$NON-NLS-1$
		{
			GridData data = new GridData( );
			dataSourceLabel.setLayoutData( data );
		}

		availableDbObjectsTree = new Tree( tablescomposite, SWT.BORDER
				| SWT.MULTI );
		{
			GridData data = new GridData( GridData.FILL_BOTH );
			data.grabExcessHorizontalSpace = true;
			data.grabExcessVerticalSpace = true;
			data.heightHint = 150;
			availableDbObjectsTree.setLayoutData( data );
		}

		availableDbObjectsTree.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				populateEventData( e );
				insertText( (String) e.data );
			}
		} );

		// Group for selecting the Tables etc
		// Searching the Tables and Views
		Group selectTableGroup = new Group( tablescomposite, SWT.FILL );
		{
			GridLayout groupLayout = new GridLayout( );
			groupLayout.numColumns = 3;
			// groupLayout.horizontalSpacing = 10;
			groupLayout.verticalSpacing = 10;
			selectTableGroup.setLayout( groupLayout );

			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			selectTableGroup.setLayoutData( data );
		}

		schemaLabel = new Label( selectTableGroup, SWT.LEFT );
		schemaLabel.setText( JdbcPlugin.getResourceString( "tablepage.label.schema" ) );

		schemaCombo = new Combo( selectTableGroup, prefetchSchema ? SWT.READ_ONLY : SWT.DROP_DOWN );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		schemaCombo.setLayoutData( gd );

		Label FilterLabel = new Label( selectTableGroup, SWT.LEFT );
		FilterLabel.setText( JdbcPlugin.getResourceString( "tablepage.label.filter" ) );

		searchTxt = new Text( selectTableGroup, SWT.BORDER );
		{
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			searchTxt.setLayoutData( data );
		}

		// Select Type
		Label selectTypeLabel = new Label( selectTableGroup, SWT.NONE );
		selectTypeLabel.setText( JdbcPlugin.getResourceString( "tablepage.label.selecttype" ) );

		// Filter Combo
		filterComboViewer = new ComboViewer( selectTableGroup, SWT.READ_ONLY );
		setFilterComboContents( filterComboViewer, supportsProcedure );
		filterComboViewer.getControl( )
				.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// Find Button
		Button findButton = new Button( selectTableGroup, SWT.NONE );
		findButton.setText( JdbcPlugin.getResourceString( "tablepage.button.filter" ) );//$NON-NLS-1$

		// Add listener to the find button
		findButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				PlatformUI.getWorkbench( )
						.getDisplay( )
						.asyncExec( new Runnable( ) {

							public void run( )
							{					
								fc = populateFilterConfig( );
								DBNodeUtil.createTreeRoot( availableDbObjectsTree,
										new RootNode( dataSetDesign.getDataSourceDesign( )
												.getName( ) ),
										fc );
							}
						} );
			}
		} );

		setupIdentifierQuoteStringCheckBox( selectTableGroup );
		setupShowSystemTableCheckBox( selectTableGroup );

		String[] allSchemaNames = null;
		if ( supportsSchema )
		{
			String allFlag = JdbcPlugin.getResourceString( "tablepage.text.All" );
			schemaCombo.add( allFlag );
			
			if ( prefetchSchema )
			{
				allSchemaNames = JdbcMetaDataProvider.getInstance( )
						.getAllSchemaNames( );
				
				for ( String name : allSchemaNames )
				{
					schemaCombo.add( name );
				}
			}
			schemaCombo.select( 0 );
		}
		else
		{
			schemaCombo.removeAll( );
			schemaCombo.setEnabled( false );
			schemaLabel.setEnabled( false );
		}
		if ( prefetchSchema )
		{
			fc = populateFilterConfig( );
			DBNodeUtil.createTreeRoot( availableDbObjectsTree,
					new RootNode( dataSetDesign.getDataSourceDesign( ).getName( ), allSchemaNames ),
					fc );
		}
		else
		{
			DBNodeUtil.createRootTip( availableDbObjectsTree, new RootNode( dataSetDesign.getDataSourceDesign( ).getName( )));
		}
		addDragSupportToTree( );
		addFetchDbObjectListener( );
		return tablescomposite;
	}

	private FilterConfig populateFilterConfig( )
	{
		String schemaName = null;
		if ( schemaCombo.isEnabled( ) && schemaCombo.getSelectionIndex( ) != 0 )
		{
			schemaName = schemaCombo.getText( );
		}
		Type type = getSelectedFilterType( );
		String namePattern = searchTxt.getText( );
		boolean isShowSystemTable = showSystemTableCheckBox.getSelection( );
		FilterConfig result = new FilterConfig( schemaName,
				type,
				namePattern,
				isShowSystemTable,
				maxSchemaCount,
				maxTableCountPerSchema );
		return result;
	}

	/*
	 * 
	 * @seeorg.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSetWizardPageCore
	 * #collectDataSetDesign(org.eclipse.datatools.connectivity
	 * .oda.design.DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{
		//This method sometimes is called even if the whole page is ever not presented
		if ( doc != null )
		{
			design.setQueryText( doc.get( ) );
			if ( !design.getQueryText( ).equals( formerQueryTxt ) )
			{
				MetaDataRetriever retriever = new MetaDataRetriever( design );
				IResultSetMetaData resultsetMeta = retriever.getResultSetMetaData( );
				IParameterMetaData paramMeta = retriever.getParameterMetaData( );
				SQLUtility.saveDataSetDesign( design, resultsetMeta, paramMeta );
				formerQueryTxt = design.getQueryText( );
				retriever.close( );
			}
		}
		return design;
	}

	/**
	 * 
	 * @param group
	 */
	private void setupIdentifierQuoteStringCheckBox( Group group )
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
	 * @param group
	 */
	private void setupShowSystemTableCheckBox( Group group )
	{
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 3;
		showSystemTableCheckBox = new Button( group, SWT.CHECK );
		showSystemTableCheckBox.setText( JdbcPlugin.getResourceString( "tablepage.button.showSystemTables" ) ); //$NON-NLS-1$
		showSystemTableCheckBox.setSelection( false );
		showSystemTableCheckBox.setLayoutData( layoutData );
		showSystemTableCheckBox.setEnabled( true );
	}

	/**
	 * 
	 * @param filterComboViewer
	 */
	private void setFilterComboContents( ComboViewer filterComboViewer,
			boolean supportsProcedure )
	{
		if ( filterComboViewer == null )
		{
			return;
		}

		List<FilterConfig.Type> types = new ArrayList<FilterConfig.Type>( );

		// Populate the Types of Data bases objects which can be retrieved
		types.add( Type.ALL );
		types.add( Type.TABLE );
		types.add( Type.VIEW );
		if ( supportsProcedure )
		{
			types.add( Type.PROCEDURE );
		}
		filterComboViewer.setContentProvider( new IStructuredContentProvider( ) {

			@SuppressWarnings("unchecked")
			public Object[] getElements( Object inputElement )
			{
				return ( (List) inputElement ).toArray( );
			}

			public void dispose( )
			{
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}

		} );

		filterComboViewer.setLabelProvider( new LabelProvider( ) {

			public String getText( Object inputElement )
			{
				FilterConfig.Type type = (FilterConfig.Type) inputElement;
				return FilterConfig.getTypeDisplayText( type );
			}

		} );

		filterComboViewer.setInput( types );

		// Set the Default selection to the First Item , which is "All"
		filterComboViewer.getCombo( ).select( 0 );
		filterComboViewer.getCombo( )
				.addSelectionListener( new SelectionAdapter( ) {

					public void widgetSelected( SelectionEvent e )
					{
						Type type = getSelectedFilterType( );
						if ( type == Type.ALL || type == Type.TABLE )
						{
							showSystemTableCheckBox.setEnabled( true );
						}
						else
						{
							showSystemTableCheckBox.setEnabled( false );
						}
					}
				} );
	}

	/**
	 * 
	 * @return The Type of the object selected in the type combo
	 */
	private FilterConfig.Type getSelectedFilterType( )
	{
		IStructuredSelection selection = (IStructuredSelection) filterComboViewer.getSelection( );
		FilterConfig.Type type = Type.ALL;
		if ( selection != null && selection.getFirstElement( ) != null )
		{
			return (Type) selection.getFirstElement( );
		}
		return type;
	}

	/**
	 * 
	 */
	private void addFetchDbObjectListener( )
	{

		availableDbObjectsTree.addListener( SWT.Expand, new Listener( ) {

			/*
			 * @see
			 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.
			 * widgets.Event)
			 */
			public void handleEvent( final Event event )
			{
				TreeItem item = (TreeItem) event.item;
				BusyIndicator.showWhile( item.getDisplay( ), new Runnable( ) {

					/*
					 * @see java.lang.Runnable#run()
					 */
					public void run( )
					{
						listChildren( event );
					}
				} );
			}

			/**
			 * @param event
			 */
			private void listChildren( Event event )
			{
				TreeItem item = (TreeItem) event.item;
				IDBNode node = (IDBNode) item.getData( );
				if ( node instanceof ChildrenAllowedNode )
				{
					ChildrenAllowedNode parent = (ChildrenAllowedNode) node;
					if ( !parent.isChildrenPrepared( ) )
					{
						item.removeAll( );
						parent.prepareChildren( fc );
						if ( parent.getChildren( ) != null )
						{
							for ( IDBNode child : parent.getChildren( ) )
							{
								DBNodeUtil.createTreeItem( item, child );
							}
						}
					}
				}
			}
		} );
	}

	/**
	 * Adds drag support to tree..Must set tree before execution.
	 */
	public void addDragSupportToTree( )
	{
		DragSource dragSource = new DragSource( availableDbObjectsTree,
				DND.DROP_COPY );
		dragSource.setTransfer( new Transfer[]{
			TextTransfer.getInstance( )
		} );
		dragSource.addDragListener( new DragSourceAdapter( ) {

			public void dragStart( DragSourceEvent event )
			{
				TreeItem[] selection = availableDbObjectsTree.getSelection( );
				event.doit = false;
				if ( selection != null && selection.length > 0 )
				{
					for ( TreeItem item : selection )
					{
						IDBNode dbNode = (IDBNode) item.getData( );
						if ( dbNode.getQualifiedNameInSQL( identifierQuoteStringCheckBox.getSelection( ) ) != null )
						{
							event.doit = true;
							break;
						}
					}
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.dnd.DragSourceAdapter#dragSetData(org.eclipse
			 * .swt.dnd.DragSourceEvent)
			 */
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

	/**
	 * 
	 * @param event
	 */
	private void populateEventData( TypedEvent event )
	{
		TreeItem[] selection = availableDbObjectsTree.getSelection( );
		event.data = "";
		if ( selection != null && selection.length > 0 )
		{
			StringBuffer data = new StringBuffer( );
			for ( int i = 0; i < selection.length; i++ )
			{
				IDBNode dbNode = (IDBNode) selection[i].getData( );
				String sql = dbNode.getQualifiedNameInSQL( identifierQuoteStringCheckBox.getSelection( ) );
				if ( sql != null )
				{
					data.append( sql ).append( "," );
				}
			}
			String result = data.toString( );
			if ( result.length( ) > 0 )
			{
				// remove the last ","
				result = result.substring( 0, result.length( ) - 1 );
			}
			event.data = result.toString( );
		}
	}

	/**
	 * Adds drop support to viewer.Must set viewer before execution.
	 * 
	 */
	private void addDropSupportToViewer( )
	{
		final StyledText text = viewer.getTextWidget( );
		DropTarget dropTarget = new DropTarget( text, DND.DROP_COPY
				| DND.DROP_DEFAULT );
		dropTarget.setTransfer( new Transfer[]{
			TextTransfer.getInstance( )
		} );
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
				event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
			}

			public void dragOperationChanged( DropTargetEvent event )
			{
				dragEnter( event );
			}

			public void drop( DropTargetEvent event )
			{
				if ( event.data instanceof String
						&& !event.data.equals( "" ) )
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
	 * Creates the textual query editor
	 * 
	 * @param parent
	 */
	private Control createTextualQueryComposite( Composite parent )
	{

		Composite composite = new Composite( parent, SWT.FILL
				| SWT.LEFT_TO_RIGHT );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 1;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		CompositeRuler ruler = new CompositeRuler( );
		LineNumberRulerColumn lineNumbers = new LineNumberRulerColumn( );
		ruler.addDecorator( 0, lineNumbers );
		viewer = new SourceViewer( composite, ruler, SWT.H_SCROLL
				| SWT.V_SCROLL );
		sourceViewerConfiguration = new JdbcSQLSourceViewerConfiguration( );
		viewer.configure( sourceViewerConfiguration );

		doc = new Document( getQueryText( ) );
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
					 * @see
					 * org.eclipse.swt.custom.BidiSegmentListener#lineGetSegments
					 * (org.eclipse.swt.custom.BidiSegmentEvent)
					 */
					public void lineGetSegments( BidiSegmentEvent event )
					{
						event.segments = SQLUtility.getBidiLineSegments( event.lineText );
					}
				} );
		attachMenus( viewer );

		GridData data = new GridData( GridData.FILL_BOTH );
		viewer.getControl( ).setLayoutData( data );

		// Add drop support to the viewer
		addDropSupportToViewer( );

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
		return composite;
	}

	/**
	 * 
	 * @param viewer
	 */
	private final void attachMenus( SourceViewer viewer )
	{
		StyledText widget = viewer.getTextWidget( );
		TextMenuManager menuManager = new TextMenuManager( viewer );
		widget.setMenu( menuManager.getContextMenu( widget ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSetWizardPageCore
	 * #refresh(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected void refresh( DataSetDesign dataSetDesign )
	{
		this.dataSetDesign = dataSetDesign;
		initializeControl( );
	}

	/*
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible( boolean visible )
	{
		super.setVisible( visible );
		getControl( ).setFocus( );
	}

	/**
	 * return the query text. If the query text is empty then return the
	 * pre-defined pattern
	 * 
	 * @return
	 */
	private String getQueryText( )
	{
		String queryText = dataSetDesign.getQueryText( );
		if ( queryText != null && queryText.trim( ).length( ) > 0 )
			return queryText;

		return SQLUtility.getQueryPresetTextString( this.dataSetDesign.getOdaExtensionDataSetId( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage
	 * #cleanup()
	 */
	protected void cleanup( )
	{
		JdbcMetaDataProvider.release( );
		dataSetDesign = null;
	}
}