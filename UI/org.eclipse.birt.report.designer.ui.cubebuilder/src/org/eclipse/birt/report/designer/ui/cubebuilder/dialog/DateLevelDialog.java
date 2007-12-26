
package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DateLevelDialog extends TitleAreaDialog
{

	private static final String NONE = Messages.getString( "DateLevelDialog.None" );
	private Text nameText;
	private Combo typeCombo;
	private TabularLevelHandle input;

	private static HashMap formatMap = new HashMap( );
	static
	{
		Date defaultDate = new Date( );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR,
				new String[][]{
						{
								new DateFormatter( "yyyy" ).format( defaultDate ),
								"yyyy"
						},
						{
								new DateFormatter( "yy" ).format( defaultDate ),
								"yy"
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH,
				new String[][]{
						{
								new DateFormatter( "MMMM" ).format( defaultDate ),
								"MMMM"
						},
						{
								new DateFormatter( "MMM yyyy" ).format( defaultDate ),
								"MMM yyyy"
						},
						{
								new DateFormatter( "MMM yy" ).format( defaultDate ),
								"MMM yy"
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR,
				new String[][]{
						{
								new DateFormatter( "MMMM dd, yyyy" ).format( defaultDate ),
								"MMMM dd, yyyy"
						},
						{
								new DateFormatter( "MMMM dd, yy" ).format( defaultDate ),
								"MMMM dd, yy"
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH,
				new String[][]{
					{
							new DateFormatter( "dd" ).format( defaultDate ),
							"dd"
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK,
				new String[][]{
					{
							new DateFormatter( "EEEE" ).format( defaultDate ),
							"EEEE"
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR,
				new String[][]{
						{
								new DateFormatter( "HH:mm:ss aaa" ).format( defaultDate ),
								"HH:mm:ss aaa"
						},
						{
								new DateFormatter( "HH:mm:ss" ).format( defaultDate ),
								"HH:mm:ss"
						},
						{
								new DateFormatter( "HH:mm" ).format( defaultDate ),
								"HH:mm"
						}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE,
				new String[][]{
					{
							new DateFormatter( "mm" ).format( defaultDate ),
							"mm"
					}
				} );
		formatMap.put( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND,
				new String[][]{
					{
							new DateFormatter( "ss" ).format( defaultDate ),
							"ss"
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
			items[i] = formatPattern[i][0];
		}
		return items;
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
		String[][] formatPattern = (String[][]) formatMap.get( dateType );
		if ( pattern == null )
			return NONE;
		for ( int i = 0; i < formatPattern.length; i++ )
		{
			if ( pattern.equals( formatPattern[i][1] ) )
				return formatPattern[i][0];
		}
		return NONE;
	}

	public DateLevelDialog( )
	{
		super( UIUtil.getDefaultShell( ) );
	}

	public void setInput( TabularLevelHandle level )
	{
		this.input = level;
	}
	// private Button noneIntervalButton;
	// private Button intervalButton;
	private Combo formatCombo;

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
			return null;
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
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				OlapUtil.getDateTimeLevelTypeChoiceSet( ) );
	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.CUBE_DATE_LEVEL_DIALOG ); //$NON-NLS-1$
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
		typeCombo.setItems( getAvailableDateTypeDisplayNames( ) );
		typeCombo.setText( getDateTypeDisplayName( input.getDateTimeLevelType( ) ) );
		formatCombo.setItems( getFormatDisplayItems( getAvailableDateTypeNames( ).get( typeCombo.getSelectionIndex( ) )
				.toString( ) ) );
		formatCombo.add( NONE, 0 );
		formatCombo.setText( getDateFormatDisplayName( input.getDateTimeLevelType( ),
				input.getDateTimeFormat( ) ) );
	}

	private void createContentArea( Composite parent )
	{
		Composite content = new Composite( parent, SWT.NONE );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		content.setLayout( layout );
		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.Name" ) ); //$NON-NLS-1$
		nameText = new Text( content, SWT.BORDER );
		nameText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		new Label( content, SWT.NONE ).setText( Messages.getString( "DateLevelDialog.Type" ) ); //$NON-NLS-1$
		typeCombo = new Combo( content, SWT.BORDER | SWT.READ_ONLY );
		typeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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
		formatCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkOkButtonStatus( );
			}

		} );
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
		}
		else
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
				getButton( IDialogConstants.OK_ID ).setEnabled( true );
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
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
			return;
		}
		super.okPressed( );
	}
}
