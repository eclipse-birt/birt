
package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

public class GroupDialog extends TitleAreaDialog
{

	private boolean isNew;

	public GroupDialog( boolean isNew )
	{
		super( UIUtil.getDefaultShell( ) );
		this.isNew = isNew;
	}

	private String dataField;
	private TabularHierarchyHandle hierarchy;
	private List levelList = new ArrayList( );

	public void setInput( TabularHierarchyHandle hierarchy, String dataField )
	{
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

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.CUBE_BUILDER_GROUP_DIALOG ); //$NON-NLS-1$
		setTitle( Messages.getString( "DateGroupDialog.Title" ) ); //$NON-NLS-1$
		getShell( ).setText( Messages.getString( "DateGroupDialog.Shell.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "DateGroupDialog.Message" ) ); //$NON-NLS-1$

		Composite area = (Composite) super.createDialogArea( parent );

		Composite contents = new Composite( area, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 20;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = convertWidthInCharsToPixels( 80 );
		data.heightHint = 300;
		contents.setLayoutData( data );

		createGroupTypeArea( contents );
		createContentArea( contents );

		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initDialog( );

		parent.layout( );

		return contents;
	}

	private void createGroupTypeArea( Composite contents )
	{
		regularButton = new Button( contents, SWT.RADIO );
		regularButton.setText( "Regular Group" );
		regularButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleButtonSelection( regularButton );
			}

		} );
		dateButton = new Button( contents, SWT.RADIO );
		dateButton.setText( "Date Group" );
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
			levelViewer.getTree( ).setVisible( false );
			setMessage( "" );
		}
		else
		{
			regularButton.setSelection( false );
			dateButton.setSelection( true );
			if ( dataField != null )
			{
				levelViewer.getTree( ).setVisible( true );
				setMessage( Messages.getString( "DateGroupDialog.Message" ) );
			}
			else
			{
				levelViewer.getTree( ).setVisible( false );
				setMessage( "" );
			}
		}
		checkOKButtonStatus( );
	}

	private void initDialog( )
	{
		nameText.setText( hierarchy.getContainer( ).getName( ) );
		if ( !isNew )
		{
			if ( ( (DimensionHandle) hierarchy.getContainer( ) ).isTimeType( ) )
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
		if ( !isNew )
		{
			WidgetUtil.setExcludeGridData( regularButton, true );
			WidgetUtil.setExcludeGridData( dateButton, true );
		}
		if ( !isNew
				&& !( (DimensionHandle) hierarchy.getContainer( ) ).isTimeType( ) )
			levelViewer.getTree( ).setVisible( false );

		levelViewer.setInput( getDateTypeNames( getLevelTypesByDateType( ) ) );
		levelViewer.expandAll( );
		TreeItem topNode = (TreeItem) levelViewer.getTree( ).getItem( 0 );
		do
		{
			if ( levelList.contains( topNode.getData( ) ) )
				topNode.setChecked( true );
			topNode = topNode.getItem( 0 );
		} while ( topNode.getItemCount( ) > 0 );
		if ( levelList.contains( topNode.getData( ) ) )
			topNode.setChecked( true );
		checkOKButtonStatus( );
	}

	private TreeItem getItem( String text )
	{
		TreeItem topNode = (TreeItem) levelViewer.getTree( ).getItem( 0 );
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
			return new Object[]{
				getDateTypeNames( getLevelTypesByDateType( ) ).get( 0 )
			};
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

		public Image getImage( Object element )
		{
			return UIHelper.getImage( BuilderConstancts.IMAGE_LEVEL );
		}

		public String getText( Object element )
		{
			return getDateTypeDisplayName( element.toString( ) );
		}
	}

	protected void okPressed( )
	{
		try
		{
			hierarchy.getContainer( ).setName( nameText.getText( ).trim( ) );
		}
		catch ( NameException e1 )
		{
			ExceptionHandler.handle( e1 );
		}

		if ( regularButton.getSelection( ) )
		{
			try
			{
				if ( ( (DimensionHandle) hierarchy.getContainer( ) ).isTimeType( ) )
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
									dataField );
					level.setColumnName( dataField );
					DataSetHandle dataset = hierarchy.getDataSet( );
					if ( dataset == null )
					{
						dataset = ( (TabularCubeHandle) hierarchy.getContainer( )
								.getContainer( ) ).getDataSet( );
					}
					level.setDataType( OlapUtil.getDataField( dataset,
							dataField ).getDataType( ) );
					hierarchy.add( IHierarchyModel.LEVELS_PROP, level );
				}
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return;
			}
		}
		else
		{
			try
			{
				if ( !( (DimensionHandle) hierarchy.getContainer( ) ).isTimeType( ) )
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
				ExceptionHandler.handle( e );
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
									dateType );
					try
					{
						level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
						level.setDateTimeLevelType( dateType );
						level.setColumnName( dataField );
						hierarchy.add( HierarchyHandle.LEVELS_PROP, level );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
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
												dateType );
								try
								{
									level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
									level.setDateTimeLevelType( dateType );
									level.setColumnName( dataField );
									hierarchy.add( HierarchyHandle.LEVELS_PROP,
											level,
											j );
									levelList.add( j, dateType );
									exit = true;
									break;
								}
								catch ( SemanticException e )
								{
									ExceptionHandler.handle( e );
								}
							}
						}
						if ( exit )
							continue;
						// out of old level list:month out (year,quarter)
						TabularLevelHandle level = DesignElementFactory.getInstance( )
								.newTabularLevel( (DimensionHandle) hierarchy.getContainer( ),
										dateType );
						try
						{
							level.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER );
							level.setDateTimeLevelType( dateType );
							level.setColumnName( dataField );
							hierarchy.add( HierarchyHandle.LEVELS_PROP, level );
							levelList.add( j++, dateType );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}
			}
		}
		super.okPressed( );
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
	private CheckboxTreeViewer levelViewer;
	private Button regularButton;
	private Button dateButton;

	private void createContentArea( Composite parent )
	{
		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		content.setLayout( layout );
		new Label( content, SWT.NONE ).setText( Messages.getString( "DateGroupDialog.Name" ) ); //$NON-NLS-1$
		nameText = new Text( content, SWT.BORDER );
		nameText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOKButtonStatus( );
			}

		} );

		levelViewer = new CheckboxTreeViewer( content, SWT.SINGLE | SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 2;
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

	}

	protected void checkOKButtonStatus( )
	{
		if ( nameText.getText( ).trim( ).length( ) == 0 )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
		}
		else
		{
			if ( dateButton.getSelection( )
					&& dateTypeSelectedList.size( ) == 0
					&& ( isNew || dataField != null ) )
			{
				if ( getButton( IDialogConstants.OK_ID ) != null )
					getButton( IDialogConstants.OK_ID ).setEnabled( false );
			}
			else if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( true );
		}
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
				setInput( hierarchy,
						( (TabularLevelHandle) hierarchy.getLevel( 0 ) ).getColumnName( ) );
		}

	}

	private IChoice[] getLevelTypesByDateType( )
	{
		if ( hierarchy == null || dataField == null )
			return null;
		DataSetHandle dataset = hierarchy.getDataSet( );
		if ( dataset == null )
		{
			dataset = ( (TabularCubeHandle) hierarchy.getContainer( )
					.getContainer( ) ).getDataSet( );
		}
		ResultSetColumnHandle column = OlapUtil.getDataField( dataset,
				dataField );
		if ( column == null )
			return null;
		String dataType = column.getDataType( );
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

}
