
package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeACLExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class GroupDialog extends TitleAreaDialog
{

	public GroupDialog( TabularHierarchyHandle hierarchy )
	{
		this( );

		dimension = (DimensionHandle) hierarchy.getContainer( );
		this.hierarchy = hierarchy;
		TabularLevelHandle[] levels = (TabularLevelHandle[]) hierarchy.getContents( IHierarchyModel.LEVELS_PROP )
				.toArray( new TabularLevelHandle[0] );
		for ( int i = 0; i < levels.length; i++ )
		{
			if ( levels[i].getDateTimeLevelType( ) != null )
				levelList.add( levels[i].getDateTimeLevelType( ) );
		}
		dateTypeSelectedList.addAll( levelList );
	}

	public GroupDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
		setHelpAvailable( false );
		setShellStyle( getShellStyle( ) | SWT.RESIZE | SWT.MAX );
	}

	protected ResultSetColumnHandle dataField;
	protected TabularCubeHandle cube;
	protected TabularHierarchyHandle hierarchy;
	private IDialogHelper helper;
	private List levelList = new ArrayList( );

	public void setInput( TabularCubeHandle cube,
			ResultSetColumnHandle dataField )
	{
		this.dataField = dataField;
		this.cube = cube;
	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.CUBE_BUILDER_GROUP_DIALOG );
		setTitle( Messages.getString( "DateGroupDialog.Title" ) ); //$NON-NLS-1$
		getShell( ).setText( Messages.getString( "DateGroupDialog.Shell.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "DateGroupDialog.Message" ) ); //$NON-NLS-1$

		Composite area = (Composite) super.createDialogArea( parent );

		contents = createDialogContentComposite( area );

		createGroupTypeArea( contents );
		createContentArea( contents );

		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initDialog( );

		parent.layout( );

		return contents;
	}

	protected Composite createDialogContentComposite( Composite area )
	{
		Composite content = new Composite( area, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 20;
		content.setLayout( layout );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = convertWidthInCharsToPixels( 80 );
		data.heightHint = 300;
		content.setLayoutData( data );
		return content;
	}

	private void createGroupTypeArea( Composite contents )
	{
		regularButton = new Button( contents, SWT.RADIO );
		regularButton.setText( Messages.getString( "GroupDialog.Button.RegularGroup" ) ); //$NON-NLS-1$
		regularButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleButtonSelection( regularButton );
			}

		} );
		dateButton = new Button( contents, SWT.RADIO );
		dateButton.setText( Messages.getString( "GroupDialog.Button.DateGroup" ) ); //$NON-NLS-1$
		dateButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleButtonSelection( dateButton );
			}

		} );
	}

	protected void handleButtonSelection( Button button )
	{
		if ( button == regularButton )
		{
			regularButton.setSelection( true );
			dateButton.setSelection( false );
			WidgetUtil.setExcludeGridData( levelViewer.getTree( ), true );
			setMessage( "" ); //$NON-NLS-1$
			isRegularButton = true;
		}
		else
		{
			regularButton.setSelection( false );
			dateButton.setSelection( true );
			if ( dataField != null )
			{
				WidgetUtil.setExcludeGridData( levelViewer.getTree( ), false );
				setMessage( Messages.getString( "DateGroupDialog.Message" ) ); //$NON-NLS-1$
			}
			else
			{
				WidgetUtil.setExcludeGridData( levelViewer.getTree( ), true );
				setMessage( "" ); //$NON-NLS-1$
			}
			isRegularButton = false;
		}
		levelViewer.getTree( ).getParent( ).layout( );
		checkOKButtonStatus( );
	}

	protected void initDialog( )
	{
		if ( hierarchy != null )
		{
			nameText.setText( hierarchy.getContainer( ).getName( ) );
		}
		else
		{
			DimensionHandle dimension = DesignElementFactory.getInstance( )
					.newTabularDimension( null );
			nameText.setText( dimension.getName( ) );
		}
		if ( dimension != null )
		{
			if ( isTimeType( dimension ) )
			{
				dateButton.setSelection( true );
				handleButtonSelection( dateButton );
			}
			else
			{
				regularButton.setSelection( true );
				handleButtonSelection( regularButton );
			}
		}
		else
		{
			dateButton.setSelection( true );
			handleButtonSelection( dateButton );
		}
		if ( dimension != null )
		{
			WidgetUtil.setExcludeGridData( regularButton, true );
			WidgetUtil.setExcludeGridData( dateButton, true );
		}
		if ( dimension != null && !isTimeType( dimension ) )
			levelViewer.getTree( ).setVisible( false );

		levelViewer.setInput( getDateTypeNames( getLevelTypesByDateType( ) ) );
		levelViewer.expandAll( );
		if ( levelViewer.getTree( ).getItemCount( ) > 0 )
		{
			TreeItem topNode = levelViewer.getTree( ).getItem( 0 );
			do
			{
				if ( levelList.contains( topNode.getData( ) ) )
					topNode.setChecked( true );
				topNode = topNode.getItem( 0 );
			} while ( topNode.getItemCount( ) > 0 );
			if ( levelList.contains( topNode.getData( ) ) )
				topNode.setChecked( true );
		}
		checkOKButtonStatus( );
	}

	protected boolean isTimeType( DimensionHandle dimension )
	{
		return dimension.isTimeType( );
	}

	private TreeItem getItem( String text )
	{
		TreeItem topNode = levelViewer.getTree( ).getItem( 0 );
		do
		{
			if ( text.equals( topNode.getData( ) ) )
				return topNode;
			topNode = topNode.getItem( 0 );
		} while ( topNode.getItemCount( ) > 0 );
		if ( text.equals( topNode.getData( ) ) )
			return topNode;
		else
			return null;
	}

	private List getDateTypeNames( IChoice[] choices )
	{
		List dateTypeList = new ArrayList( );
		if ( choices == null )
			return dateTypeList;
		for ( int i = 0; i < choices.length; i++ )
		{
			dateTypeList.add( choices[i].getName( ) );
		}
		return dateTypeList;
	}

	private String getDateTypeDisplayName( String name )
	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				OlapUtil.getDateTimeLevelTypeChoiceSet( ) );
	}

	class DateLevelProvider extends LabelProvider implements
			ITreeContentProvider
	{

		public Object[] getChildren( Object parentElement )
		{
			int index = getDateTypeNames( getLevelTypesByDateType( ) ).indexOf( parentElement );
			return new Object[]{
				getDateTypeNames( getLevelTypesByDateType( ) ).get( index + 1 )
			};
		}

		public Object getParent( Object element )
		{
			int index = getDateTypeNames( getLevelTypesByDateType( ) ).indexOf( element );
			if ( index == 0 )
				return null;
			else
				return getDateTypeNames( getLevelTypesByDateType( ) ).get( index - 1 );
		}

		public boolean hasChildren( Object element )
		{
			int index = getDateTypeNames( getLevelTypesByDateType( ) ).indexOf( element );
			if ( index >= getDateTypeNames( getLevelTypesByDateType( ) ).size( ) - 1 )
				return false;
			return true;
		}

		public Object[] getElements( Object inputElement )
		{
			if ( getLevelTypesByDateType( ) != null
					&& getLevelTypesByDateType( ).length > 0 )
				return new Object[]{
					getDateTypeNames( getLevelTypesByDateType( ) ).get( 0 )
				};

			return new Object[0];
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

		public Image getImage( Object element )
		{
			return UIHelper.getImage( BuilderConstants.IMAGE_LEVEL );
		}

		public String getText( Object element )
		{
			return getDateTypeDisplayName( element.toString( ) );
		}
	}

	protected void okPressed( )
	{
		saveResult( );
		super.okPressed( );
	}

	protected void saveResult( )
	{
		boolean isNew = hierarchy == null;

		if ( isNew )
		{
			dimension = createDimension( );
			hierarchy = (TabularHierarchyHandle) dimension.getDefaultHierarchy( );

			if ( dataField != null )
			{
				DataSetHandle dataset = (DataSetHandle) dataField.getElementHandle( );
				if ( hierarchy.getDataSet( ) == null
						&& hierarchy.getLevelCount( ) == 0
						&& cube != null
						&& ( dataset != null && dataset != cube.getDataSet( ) ) )
				{
					try
					{
						hierarchy.setDataSet( dataset );
					}
					catch ( SemanticException e )
					{
						ExceptionUtil.handle( e );
					}
				}
			}
		}

		try
		{
			dimension.setName( nameText.getText( ).trim( ) );
		}
		catch ( NameException e1 )
		{
			ExceptionUtil.handle( e1 );
		}
		if ( helper != null )
		{
			try
			{
				helper.validate( );
				dimension.setExpressionProperty( DimensionHandle.ACL_EXPRESSION_PROP,
						(Expression) helper.getProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY ) );
			}
			catch ( SemanticException e )
			{
				ExceptionUtil.handle( e );
			}
		}

		if ( regularButton.getSelection( ) )
		{
			try
			{
				if ( isTimeType( (DimensionHandle) hierarchy.getContainer( ) ) )
				{
					while ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
					{
						hierarchy.dropAndClear( IHierarchyModel.LEVELS_PROP, 0 );
					}
				}
				( (DimensionHandle) hierarchy.getContainer( ) ).setTimeType( false );
				if ( isNew )
				{
					TabularLevelHandle level = DesignElementFactory.getInstance( )
							.newTabularLevel( (DimensionHandle) hierarchy.getContainer( ),
									OlapUtil.getDataFieldDisplayName( dataField ) );
					level.setColumnName( dataField.getColumnName( ) );
					DataSetHandle dataset = hierarchy.getDataSet( );
					if ( dataset == null && cube != null )
					{
						dataset = cube.getDataSet( );
					}
					level.setDataType( dataField.getDataType( ) );
					ColumnHintHandle column = OlapUtil.getColumnHintHandle( dataField );
					if ( column != null )
					{
						level.setAlignment( column.getHorizontalAlign( ) );
						level.setFormat( column.getValueFormat( ) );
					}
					hierarchy.add( IHierarchyModel.LEVELS_PROP, level );
				}
			}
			catch ( SemanticException e )
			{
				ExceptionUtil.handle( e );
				return;
			}
		}
		else
		{
			try
			{
				if ( !isTimeType( (DimensionHandle) hierarchy.getContainer( ) ) )
				{
					while ( hierarchy.getContentCount( IHierarchyModel.LEVELS_PROP ) > 0 )
					{
						hierarchy.dropAndClear( IHierarchyModel.LEVELS_PROP, 0 );
					}
				}
				( (DimensionHandle) hierarchy.getContainer( ) ).setTimeType( true );
			}
			catch ( SemanticException e )
			{
				ExceptionUtil.handle( e );
			}

			// remove unused level
			if ( levelList.size( ) > 0 )
			{
				for ( int i = 0; i < OlapUtil.getDateTimeLevelTypeChoices( ).length; i++ )
				{
					String dateType = OlapUtil.getDateTimeLevelTypeChoices( )[i].getName( );
					if ( levelList.contains( dateType )
							&& !dateTypeSelectedList.contains( dateType ) )
					{
						LevelHandle level = hierarchy.getLevel( levelList.indexOf( dateType ) );
						boolean hasExecuted = OlapUtil.enableDrop( level );
						if ( hasExecuted )
						{
							new DeleteCommand( level ).execute( );
							levelList.remove( dateType );
						}
					}

				}
			}
			// New
			if ( levelList.size( ) == 0 )
			{
				sortDataType( );
				for ( int i = 0; i < dateTypeSelectedList.size( ); i++ )
				{
					String dateType = (String) dateTypeSelectedList.get( i );
					TabularLevelHandle level = DesignElementFactory.getInstance( )
							.newTabularLevel( (DimensionHandle) hierarchy.getContainer( ),
									getDateTypeDisplayName( dateType ) );
					try
					{
						hierarchy.add( HierarchyHandle.LEVELS_PROP, level );
						level.setColumnName( dataField.getColumnName( ) );
						level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
						level.setDateTimeLevelType( dateType );
					}
					catch ( SemanticException e )
					{
						ExceptionUtil.handle( e );
					}
				}
			}
			// Edit
			else
			{
				int j = 0;
				sortDataType( );
				for ( int i = 0; i < dateTypeSelectedList.size( ); i++ )
				{
					String dateType = (String) dateTypeSelectedList.get( i );
					if ( !levelList.contains( dateType ) )
					{
						boolean exit = false;
						// in the old level list: month in (year,day)
						for ( ; j < levelList.size( ); j++ )
						{
							if ( getDateTypeNames( getLevelTypesByDateType( ) ).indexOf( dateType ) < getDateTypeNames( getLevelTypesByDateType( ) ).indexOf( levelList.get( j ) ) )
							{
								TabularLevelHandle level = DesignElementFactory.getInstance( )
										.newTabularLevel( (DimensionHandle) hierarchy.getContainer( ),
												getDateTypeDisplayName( dateType ) );
								try
								{
									hierarchy.add( HierarchyHandle.LEVELS_PROP,
											level,
											j );
									level.setColumnName( dataField.getColumnName( ) );
									level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
									level.setDateTimeLevelType( dateType );
									levelList.add( j, dateType );
									exit = true;
									break;
								}
								catch ( SemanticException e )
								{
									ExceptionUtil.handle( e );
								}
							}
						}
						if ( exit )
							continue;
						// out of old level list:month out (year,quarter)
						TabularLevelHandle level = DesignElementFactory.getInstance( )
								.newTabularLevel( (DimensionHandle) hierarchy.getContainer( ),
										getDateTypeDisplayName( dateType ) );
						try
						{
							hierarchy.add( HierarchyHandle.LEVELS_PROP, level );
							level.setColumnName( dataField.getColumnName( ) );
							level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
							level.setDateTimeLevelType( dateType );
							levelList.add( j++, dateType );
						}
						catch ( SemanticException e )
						{
							ExceptionUtil.handle( e );
						}
					}
				}
			}
		}

		if ( isNew )
		{
			insertDimension( );
			updateDateLevels( );
		}
	}

	protected void updateDateLevels( )
	{
		if ( dimension.isTimeType( ) )
		{
			try
			{
				for ( int i = 0; i < hierarchy.getLevelCount( ); i++ )
				{
					TabularLevelHandle level = (TabularLevelHandle) hierarchy.getLevel( i );
					Iterator attrs = level.attributesIterator( );
					while ( attrs.hasNext( ) )
					{
						LevelAttributeHandle attr = (LevelAttributeHandle) attrs.next( );
						if ( LevelAttribute.DATE_TIME_ATTRIBUTE_NAME.equals( attr.getName( ) ) )
						{
							attr.setDataType( dataField.getDataType( ) );
							break;
						}
					}
				}
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	protected void insertDimension( )
	{
		try
		{
			if ( cube != null )
				cube.add( CubeHandle.DIMENSIONS_PROP, dimension );
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
	}

	protected DimensionHandle createDimension( )
	{
		return DesignElementFactory.getInstance( ).newTabularDimension( null );
	}

	private void sortDataType( )
	{
		List list = new ArrayList( );
		List typeNames = getDateTypeNames( getLevelTypesByDateType( ) );
		for ( int i = 0; i < typeNames.size( ); i++ )
		{
			Object typeName = typeNames.get( i );
			for ( int j = 0; j < dateTypeSelectedList.size( ); j++ )
			{
				if ( dateTypeSelectedList.get( j ).equals( typeName ) )
				{
					list.add( typeName );
					break;
				}
			}
		}
		dateTypeSelectedList.clear( );
		dateTypeSelectedList.addAll( list );
	}

	private List dateTypeSelectedList = new ArrayList( );
	private Text nameText;
	protected CheckboxTreeViewer levelViewer;
	protected Button regularButton;
	protected Button dateButton;
	private boolean isRegularButton = false;
	protected DimensionHandle dimension;
	protected Composite contents;

	protected void createContentArea( Composite parent )
	{
		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		content.setLayout( layout );

		createNamePart( content );
		createLevelViewerPart( content );
		createSecurityPart( content );
	}

	protected void createLevelViewerPart( Composite content )
	{
		levelViewer = new CheckboxTreeViewer( content, SWT.SINGLE | SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 3;
		levelViewer.getTree( ).setLayoutData( gd );

		DateLevelProvider provider = new DateLevelProvider( );
		levelViewer.setContentProvider( provider );
		levelViewer.setLabelProvider( provider );

		/**
		 * The two listener behaviors are so special behaviors, because they are
		 * used to fix the bug 205934
		 */
		levelViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{
				String itemText = (String) event.getElement( );
				TreeItem item = getItem( itemText );
				checkItem( item );
			}
		} );

		levelViewer.getTree( ).addMouseListener( new MouseAdapter( ) {

			public void mouseDown( MouseEvent e )
			{
				TreeItem item = levelViewer.getTree( ).getItem( new Point( e.x,
						e.y ) );
				checkItem( item );
			}
		} );

		levelViewer.getTree( ).addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.character == ' ' )
				{
					TreeItem[] item = levelViewer.getTree( ).getSelection( );
					if ( item != null && item.length == 1 )
						checkItem( item[0] );
				}
			}
		} );
	}

	protected Composite createNamePart( Composite content )
	{
		Composite nameContainer = new Composite( content, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		layout.marginWidth = layout.marginHeight = 0;
		nameContainer.setLayout( layout );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		nameContainer.setLayoutData( gd );

		new Label( nameContainer, SWT.NONE ).setText( Messages.getString( "DateGroupDialog.Name" ) ); //$NON-NLS-1$
		nameText = new Text( nameContainer, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		nameText.setLayoutData( gd );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOKButtonStatus( );
			}

		} );
		return nameContainer;
	}

	protected void createSecurityPart( Composite parent )
	{
		if ( dimension == null )
			return;
		Object[] helperProviders = ElementAdapterManager.getAdapters( dimension,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null && helper == null )
				{
					helper = helperProvider.createHelper( this,
							BuilderConstants.SECURITY_HELPER_KEY );
					if ( helper != null )
					{
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString( "GroupDialog.Access.Control.List.Expression" ) ); //$NON-NLS-1$
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_CONTEXT,
								dimension );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeACLExpressionProvider( dimension ) );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								dimension.getACLExpression( ) );
						helper.createContent( parent );
						helper.addListener( SWT.Modify, new Listener( ) {

							public void handleEvent( Event event )
							{
								helper.update( false );
							}
						} );
						helper.update( true );
					}
				}
			}
		}

	}

	protected void checkOKButtonStatus( )
	{
		if ( nameText.getText( ).trim( ).length( ) == 0 )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
			setMessage( null );
			setErrorMessage( Messages.getString( "DateGroupDialog.Message.BlankName" ) ); //$NON-NLS-1$
		}
		else if ( !UIUtil.validateDimensionName( nameText.getText( ) ) )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
			setMessage( null );
			setErrorMessage( Messages.getString( "DateGroupDialog.Message.NumericName" ) ); //$NON-NLS-1$
		}
		else if ( checkDuplicateName( nameText.getText( ) ) )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
			setMessage( null );
			setErrorMessage( Messages.getString( "DateGroupDialog.Message.DuplicateName" ) ); //$NON-NLS-1$
		}
		else
		{
			if ( dateButton.getSelection( )
					&& dateTypeSelectedList.size( ) == 0
					&& ( dimension == null || dataField != null ) )
			{
				if ( getButton( IDialogConstants.OK_ID ) != null )

					getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setErrorMessage( null );
				setMessage( Messages.getString( "DateGroupDialog.Message" ) ); //$NON-NLS-1$

			}
			else if ( getButton( IDialogConstants.OK_ID ) != null )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( true );
				if ( isRegularButton )
				{
					setErrorMessage( null );
					setMessage( Messages.getString( "DateGroupDialog.Message.Regular" ) ); //$NON-NLS-1$
				}
				else
				{
					setErrorMessage( null );
					setMessage( Messages.getString( "DateGroupDialog.Message" ) ); //$NON-NLS-1$
				}
			}
		}
	}

	protected boolean checkDuplicateName( String name )
	{
		try
		{
			DimensionHandle dimension = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.findDimension( name );
			DimensionHandle input = null;
			if ( hierarchy != null )
			{
				input = (DimensionHandle) hierarchy.getContainer( );
			}
			if ( dimension != null && dimension != input )
				return true;
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return false;
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		checkOKButtonStatus( );
	}

	public void setInput( TabularHierarchyHandle hierarchy )
	{
		if ( hierarchy.getLevelCount( ) == 0 )
			setInput( hierarchy, null );
		else
		{
			if ( !isDateType( hierarchy,
					( (TabularLevelHandle) hierarchy.getLevel( 0 ) ).getColumnName( ) ) )
				setInput( hierarchy, null );
			else
			{
				DataSetHandle dataset = getDataSet( hierarchy );
				setInput( hierarchy,
						OlapUtil.getDataField( dataset,
								( (TabularLevelHandle) hierarchy.getLevel( 0 ) ).getColumnName( ) ) );
			}
		}

	}

	protected DataSetHandle getDataSet( TabularHierarchyHandle hierarchy )
	{
		DataSetHandle dataset = hierarchy.getDataSet( );
		if ( dataset == null )
		{
			dataset = ( (TabularCubeHandle) hierarchy.getContainer( )
					.getContainer( ) ).getDataSet( );
		}
		return dataset;
	}

	private void setInput( TabularHierarchyHandle hierarchy,
			ResultSetColumnHandle dataField )
	{
		dimension = (DimensionHandle) hierarchy.getContainer( );
		this.dataField = dataField;
		this.hierarchy = hierarchy;
		TabularLevelHandle[] levels = (TabularLevelHandle[]) hierarchy.getContents( IHierarchyModel.LEVELS_PROP )
				.toArray( new TabularLevelHandle[0] );
		for ( int i = 0; i < levels.length; i++ )
		{
			if ( levels[i].getDateTimeLevelType( ) != null )
				levelList.add( levels[i].getDateTimeLevelType( ) );
		}
		dateTypeSelectedList.addAll( levelList );
	}

	private IChoice[] getLevelTypesByDateType( )
	{
		if ( dataField == null )
			return null;
		String dataType = dataField.getDataType( );
		if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
			return OlapUtil.getDateTimeLevelTypeChoices( );
		else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE ) )
			return OlapUtil.getDateLevelTypeChoices( );
		else
			return OlapUtil.getTimeLevelTypeChoices( );
	}

	private boolean isDateType( TabularHierarchyHandle hierarchy,
			String columnName )
	{

		ResultSetColumnHandle column = OlapUtil.getDataField( OlapUtil.getHierarchyDataset( hierarchy ),
				columnName );
		if ( column == null )
			return false;
		String dataType = column.getDataType( );
		return dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME )
				|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE )
				|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME );
	}

	private void checkItem( TreeItem item )
	{
		if ( item != null )
		{
			item.setChecked( !item.getChecked( ) );
			levelViewer.getTree( ).setSelection( item );
			if ( item.getChecked( ) )
			{
				if ( !dateTypeSelectedList.contains( item.getData( ) ) )
				{
					dateTypeSelectedList.add( item.getData( ) );
				}

			}
			else
			{
				if ( dateTypeSelectedList.contains( item.getData( ) ) )
					dateTypeSelectedList.remove( item.getData( ) );
			}
			checkOKButtonStatus( );
		}
	}

	public DimensionHandle getResult( )
	{
		return dimension;
	}

}
