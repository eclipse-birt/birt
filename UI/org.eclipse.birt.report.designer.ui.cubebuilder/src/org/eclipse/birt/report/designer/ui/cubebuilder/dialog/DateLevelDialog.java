
package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeACLExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.LinkToCubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class DateLevelDialog extends BaseTitleAreaDialog
{

	private static final String NONE = Messages.getString( "DateLevelDialog.None" ); //$NON-NLS-1$
	private Text nameText;
	private Combo typeCombo;
	private TabularLevelHandle input;
	private IDialogHelper helper;

	private static HashMap formatMap = new HashMap( );
	static
	{
		Date defaultDate = new Date( );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR,
				new String[][]{
						{
								new DateFormatter( "yyyy" ).format( defaultDate ), //$NON-NLS-1$
								"yyyy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "yy" ).format( defaultDate ), //$NON-NLS-1$
								"yy" //$NON-NLS-1$
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH,
				new String[][]{
						{
								new DateFormatter( "MM" ).format( defaultDate ), //$NON-NLS-1$
								"MM" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MM yyyy" ).format( defaultDate ), //$NON-NLS-1$
								"MM yyyy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MM yy" ).format( defaultDate ), //$NON-NLS-1$
								"MM yy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMM" ).format( defaultDate ), //$NON-NLS-1$
								"MMM" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMM yyyy" ).format( defaultDate ), //$NON-NLS-1$
								"MMM yyyy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMM yy" ).format( defaultDate ), //$NON-NLS-1$
								"MMM yy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMMM" ).format( defaultDate ), //$NON-NLS-1$
								"MMMM" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMMM yyyy" ).format( defaultDate ), //$NON-NLS-1$
								"MMMM yyyy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMMM yy" ).format( defaultDate ), //$NON-NLS-1$
								"MMMM yy" //$NON-NLS-1$
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR,
				new String[][]{
						{
								new DateFormatter( "MMMM dd, yyyy" ).format( defaultDate ), //$NON-NLS-1$
								"MMMM dd, yyyy" //$NON-NLS-1$
						},
						{
								new DateFormatter( "MMMM dd, yy" ).format( defaultDate ), //$NON-NLS-1$
								"MMMM dd, yy" //$NON-NLS-1$
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH,
				new String[][]{
					{
							new DateFormatter( "dd" ).format( defaultDate ), //$NON-NLS-1$
							"dd" //$NON-NLS-1$
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK,
				new String[][]{
					{
							new DateFormatter( "EEEE" ).format( defaultDate ), //$NON-NLS-1$
							"EEEE" //$NON-NLS-1$
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR,
				new String[][]{
						{
								new DateFormatter( "HH:mm:ss aaa" ).format( defaultDate ), //$NON-NLS-1$
								"HH:mm:ss aaa" //$NON-NLS-1$
						},
						{
								new DateFormatter( "HH:mm:ss" ).format( defaultDate ), //$NON-NLS-1$
								"HH:mm:ss" //$NON-NLS-1$
						},
						{
								new DateFormatter( "HH:mm" ).format( defaultDate ), //$NON-NLS-1$
								"HH:mm" //$NON-NLS-1$
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE,
				new String[][]{
					{
							new DateFormatter( "mm" ).format( defaultDate ), //$NON-NLS-1$
							"mm" //$NON-NLS-1$
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND,
				new String[][]{
					{
							new DateFormatter( "ss" ).format( defaultDate ), //$NON-NLS-1$
							"ss" //$NON-NLS-1$
					}
				} );
	};

	private String[] getFormatDisplayItems( String type )
	{
		String[][] formatPattern = (String[][]) formatMap.get( type );
		if ( formatPattern == null )
			return new String[0];
		String[] items = new String[formatPattern.length];
		for ( int i = 0; i < items.length; i++ )
		{
			items[i] = getFormatPattenString( formatPattern, i );
		}
		return items;
	}

	private String getFormatPattenString( String[][] formatPattern, int i )
	{
		return formatPattern[i][0] + " (" + formatPattern[i][1] + ")";
	}

	private String[] getFormatPatternItems( String type )
	{
		String[][] formatPattern = (String[][]) formatMap.get( type );
		String[] items = new String[formatPattern.length];
		for ( int i = 0; i < items.length; i++ )
		{
			items[i] = formatPattern[i][1];
		}
		return items;
	}

	private String getDateFormatDisplayName( String dateType, String pattern )
	{
		if ( dateType == null )
			return NONE;
		String[][] formatPattern = (String[][]) formatMap.get( dateType );
		if ( pattern == null )
			return NONE;
		for ( int i = 0; i < formatPattern.length; i++ )
		{
			if ( pattern.equals( formatPattern[i][1] ) )
				return getFormatPattenString( formatPattern, i );
		}
		return NONE;
	}

	public DateLevelDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
		setShellStyle( getShellStyle( ) | SWT.RESIZE | SWT.MAX );
	}

	public void setInput( TabularCubeHandle cube, TabularLevelHandle level )
	{
		this.input = level;
	}
	// private Button noneIntervalButton;
	// private Button intervalButton;
	private Combo formatCombo;
	private Text fieldText;

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

	private String[] getAvailableDateTypeDisplayNames( )
	{
		List dateTypeList = getAvailableDateTypeNames( );
		List dateTypeDisplayList = new ArrayList( );
		for ( int i = 0; i < dateTypeList.size( ); i++ )
		{
			dateTypeDisplayList.add( getDateTypeDisplayName( dateTypeList.get( i )
					.toString( ) ) );
		}
		return (String[]) dateTypeDisplayList.toArray( new String[0] );
	}

	private IChoice[] getLevelTypesByDateType( )
	{
		TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) input.getContainer( );
		String dataField = input.getColumnName( );
		if ( hierarchy == null || dataField == null )
			return null;
		ResultSetColumnHandle column = OlapUtil.getDataField( OlapUtil.getHierarchyDataset( hierarchy ),
				dataField );
		if ( column == null )
			return OlapUtil.getDateTimeLevelTypeChoices( );
		String dataType = column.getDataType( );
		if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
			return OlapUtil.getDateTimeLevelTypeChoices( );
		else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE ) )
			return OlapUtil.getDateLevelTypeChoices( );
		else
			return OlapUtil.getTimeLevelTypeChoices( );
	}

	private List getAvailableDateTypeNames( )
	{
		List dateTypeList = new ArrayList( );
		dateTypeList.addAll( getDateTypeNames( getLevelTypesByDateType( ) ) );
		List levels = input.getContainer( )
				.getContents( IHierarchyModel.LEVELS_PROP );
		for ( int i = 0; i < levels.size( ); i++ )
		{
			if ( levels.get( i ) == input )
				continue;
			dateTypeList.remove( ( (TabularLevelHandle) levels.get( i ) ).getDateTimeLevelType( ) );
		}
		return dateTypeList;
	}

	public String getDateTypeDisplayName( String name )
	{
		if ( name == null )
			return ""; //$NON-NLS-1$
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				OlapUtil.getDateTimeLevelTypeChoiceSet( ) );
	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.CUBE_DATE_LEVEL_DIALOG );
		setTitle( Messages.getString( "DateLevelDialog.Title" ) ); //$NON-NLS-1$
		getShell( ).setText( Messages.getString( "DateLevelDialog.Shell.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "DateLevelDialog.Message" ) ); //$NON-NLS-1$

		Composite area = (Composite) super.createDialogArea( parent );

		Composite contents = new Composite( area, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 20;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = convertWidthInCharsToPixels( 80 );
		// data.heightHint = 200;
		contents.setLayoutData( data );

		createContentArea( contents );

		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initLevelDialog( );

		parent.layout( );

		return contents;
	}

	private void initLevelDialog( )
	{
		nameText.setText( input.getName( ) );
		fieldText.setText( input.getColumnName( ) );
		typeCombo.setItems( getAvailableDateTypeDisplayNames( ) );
		typeCombo.setText( getDateTypeDisplayName( input.getDateTimeLevelType( ) ) );
		if ( typeCombo.getSelectionIndex( ) > -1 )
		{
			formatCombo.setItems( getFormatDisplayItems( getAvailableDateTypeNames( ).get( typeCombo.getSelectionIndex( ) )
					.toString( ) ) );
		}
		formatCombo.add( NONE, 0 );
		formatCombo.setText( getDateFormatDisplayName( input.getDateTimeLevelType( ),
				input.getDateTimeFormat( ) ) );
	}

	private void createContentArea( Composite parent )
	{
		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		content.setLayout( layout );
		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.Name" ) ); //$NON-NLS-1$
		nameText = new Text( content, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		nameText.setLayoutData( gd );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.KeyField" ) ); //$NON-NLS-1$
		fieldText = new Text( content, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		fieldText.setLayoutData( gd );

		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.Type" ) ); //$NON-NLS-1$
		typeCombo = new Combo( content, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		typeCombo.setLayoutData( gd );
		typeCombo.setVisibleItemCount( 30 );
		typeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkOkButtonStatus( );
				formatCombo.setItems( new String[0] );
				formatCombo.setItems( getFormatDisplayItems( getAvailableDateTypeNames( ).get( typeCombo.getSelectionIndex( ) )
						.toString( ) ) );
				formatCombo.add( NONE, 0 );
				formatCombo.select( 0 );
			}

		} );

		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.Format" ) ); //$NON-NLS-1$
		formatCombo = new Combo( content, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		formatCombo.setLayoutData( gd );
		formatCombo.setVisibleItemCount( 30 );
		formatCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		createSecurityPart( content );
		createHyperLinkPart( content );
	}

	private IDialogHelper createHyperLinkPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					final IDialogHelper hyperLinkHelper = helperProvider.createHelper( this,
							BuilderConstants.HYPERLINK_HELPER_KEY );
					if ( hyperLinkHelper != null )
					{
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_LABEL,
								Messages.getString( "DateLevelDialog.Label.LinkTo" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_BUTTON_TEXT,
								Messages.getString( "DateLevelDialog.Button.Text.Edit" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_REPORT_ITEM_HANDLE,
								input );
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_REPORT_ITEM_PROVIDER,
								new LinkToCubeExpressionProvider( input ) );
						hyperLinkHelper.createContent( parent );
						hyperLinkHelper.addListener( SWT.Modify,
								new Listener( ) {

									public void handleEvent( Event event )
									{
										hyperLinkHelper.update( false );
									}
								} );
						hyperLinkHelper.update( true );
						return hyperLinkHelper;
					}
				}
			}
		}
		return null;
	}

	private void createSecurityPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
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
								Messages.getString( "DateLevelDialog.Access.Control.List.Expression" ) ); //$NON-NLS-1$
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_CONTEXT,
								input );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeACLExpressionProvider( input ) );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								input.getACLExpression( ) );
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

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		checkOkButtonStatus( );
	}

	protected void checkOkButtonStatus( )
	{
		if ( nameText.getText( ) == null
				|| nameText.getText( ).trim( ).equals( "" ) //$NON-NLS-1$
				|| typeCombo.getSelectionIndex( ) == -1
				|| formatCombo.getSelectionIndex( ) == -1 )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
			setMessage( null );
			setErrorMessage( Messages.getString( "DateLevelDialog.Message.BlankName" ) ); //$NON-NLS-1$
		}
		else if ( !UIUtil.validateDimensionName( nameText.getText( ) ) )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
			setMessage( null );
			setErrorMessage( Messages.getString( "LevelPropertyDialog.Message.NumericName" ) ); //$NON-NLS-1$
		}
		else
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( true );
			setErrorMessage( null );
			setMessage( Messages.getString( "DateLevelDialog.Message" ) ); //$NON-NLS-1$
		}
	}

	protected void okPressed( )
	{
		try
		{
			if ( nameText.getText( ) != null
					&& !nameText.getText( ).trim( ).equals( "" ) ) //$NON-NLS-1$
			{
				input.setName( nameText.getText( ) );
			}

			if ( typeCombo.getText( ) != null )
			{
				input.setDateTimeLevelType( getAvailableDateTypeNames( ).get( typeCombo.getSelectionIndex( ) )
						.toString( ) );
			}
			if ( formatCombo.getText( ) != null )
			{
				if ( formatCombo.getText( ).equals( NONE ) )
					input.setDateTimeFormat( null );
				else
					input.setDateTimeFormat( getFormatPatternItems( getAvailableDateTypeNames( ).get( typeCombo.getSelectionIndex( ) )
							.toString( ) )[formatCombo.getSelectionIndex( ) - 1] );
			}
			if ( helper != null )
			{
				helper.validate( );
				input.setExpressionProperty( LevelHandle.ACL_EXPRESSION_PROP,
						(Expression) helper.getProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY ) );
			}
		}
		catch ( Exception e )
		{
			ExceptionUtil.handle( e );
			return;
		}
		super.okPressed( );
	}
}
