/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.Date;
import java.util.HashMap;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Creates a preference page for DateTime format.
 */

public class FormatDateTimePreferencePage extends BaseStylePreferencePage
{

	private String name;
	private String patternStr = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	private static final int FORMAT_TYPE_INDEX = 0;
	private static final int FORMAT_SAMPLE_INDEX = 1;

	private Combo typeChoicer;
	private Composite infoComp;
	private Composite generalPage;
	private Composite customPage;

	private Label generalPreviewLabel, cusPreviewLabel;
	private Label guideLabel;
	private Text previewText;
	private Text formatCode;

	private boolean hasLoaded = false;

	/**
	 * Constructs a format datetime preference page.
	 * 
	 * @param model
	 *            The model
	 */
	public FormatDateTimePreferencePage( Object model )
	{
		super( model );
		setTitle( Messages.getString( "FormatDateTimePreferencePage.formatDateTime.title" ) ); //$NON-NLS-1$
		setPreferenceName( Style.DATE_TIME_FORMAT_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#adjustGridLayout()
	 */
	protected void adjustGridLayout( )
	{
		( (GridLayout) getFieldEditorParent( ).getLayout( ) ).numColumns = 1;
	}

	/**
	 * Sets the pattern string for this preference.
	 * 
	 * @param patternStr
	 *            The patternStr to set.
	 */
	private void setPatternStr( String patternStr )
	{
		this.patternStr = patternStr == "" ? null : patternStr;
	}

	/**
	 * Returns the patternStr.
	 */
	private String getPatternStr( )
	{
		return patternStr;
	}

	/**
	 * @param datetiem_format_type_general_date
	 */
	private void setCategory( String category )
	{
		this.category = category == "" ? null : category;
	}

	/**
	 * @return
	 */
	private String getCategory( )
	{
		return category;
	}

	/**
	 * Sets the preference name.
	 */
	private void setPreferenceName( String name )
	{
		this.name = name;
	}

	/**
	 * Gets the preference name.
	 */
	protected String getPreferenceName( )
	{
		return name;
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */
	protected String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
					Style.DATE_TIME_FORMAT_PROP );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				choiceArray = new String[choices.length][2];
				for ( int i = 0; i < choices.length; i++ )
				{
					choiceArray[i][0] = choices[i].getDisplayName( );
					choiceArray[i][1] = choices[i].getName( );
				}
			}
			else
			{
				choiceArray = new String[0][0];
			}
		}
		return choiceArray;
	}

	/**
	 * Gets the format types for display names.
	 */
	private String[] getFormatTypes( )
	{
		if ( initChoiceArray( ) != null )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				String fmtStr = ""; //$NON-NLS-1$
				String category = choiceArray[i][1];
				if ( category.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM ) )
				{
					fmtStr = category;
				}
				else
				{
					String pattern = getPatternForCategory( category );
					fmtStr = new DateFormatter( pattern ).format( new Date( ) );
				}
				formatTypes[i] = fmtStr;
			}
		}
		else
		{
			formatTypes = new String[0];
		}
		return formatTypes;
	}

	/**
	 * Gets the index of given category.
	 */
	private int getIndexOfCategory( String name )
	{
		int index = 0;
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][1].equals( name ) )
				{
					return i;
				}
			}
		}
		return index;
	}

	/**
	 * Gets the corresponding category for given display name.
	 */
	private String getCategoryorDisplayName( String displayName )
	{
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{

				if ( formatTypes[i].equals( displayName ) )
				{
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	/**
	 * Retrieves format pattern from arrays given format type categorys.
	 * 
	 * @param category
	 *            Given format type category.
	 * @return The corresponding format pattern string.
	 */
	protected String getPatternForCategory( String category )
	{
		String pattern = category;
		if ( category == null )
		{
			pattern = ""; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE.equals( category ) )
		{
			pattern = "General Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE.equals( category ) )
		{
			pattern = "Long Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE.equals( category ) )
		{
			pattern = "Medium Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE.equals( category ) )
		{
			pattern = "Short Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME.equals( category ) )
		{
			pattern = "Long Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME.equals( category ) )
		{
			pattern = "Medium Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME.equals( category ) )
		{
			pattern = "Short Time"; //$NON-NLS-1$
		}
		else
		{
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors( )
	{
		super.createFieldEditors( );
		final Composite parent = getFieldEditorParent( );
		initChoiceArray( );
		getFormatTypes( );

		Composite topContainer = new Composite( parent, SWT.NONE );
		topContainer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		topContainer.setLayout( new GridLayout( 2, false ) );
		new Label( topContainer, SWT.NONE ).setText( Messages.getString( "FormatDateTimePreferencePage.formatDateTime.label" ) ); //$NON-NLS-1$
		typeChoicer = new Combo( topContainer, SWT.READ_ONLY );
		GridData data = new GridData( );
		//		data.widthHint = 120;
		typeChoicer.setLayoutData( data );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = typeChoicer.getText( );
				String category = getCategoryorDisplayName( displayName );
				Control control = (Control) categoryPageMaps.get( category );
				( (StackLayout) infoComp.getLayout( ) ).topControl = control;
				infoComp.layout( );
				updatePreview( );
			}
		} );
		typeChoicer.setItems( getFormatTypes( ) );
		typeChoicer.select( 0 );

		infoComp = new Composite( parent, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );
		createInfoPages( infoComp );

		doLoad( );
	}

	/**
	 * Loads format pattern string from preference store.
	 */
	private void doLoad( )
	{
		hasLoaded = false;
		oldCategory = ( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormat( )
				.getCategory( );
		oldPattern = ( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormat( )
				.getPattern( );

		int index = 0;
		if ( oldCategory != null )
		{
			index = getIndexOfCategory( oldCategory );
			if ( oldCategory.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM ) )
			{
				formatCode.setText( oldPattern == null ? "" : oldPattern ); //$NON-NLS-1$
			}
		}
		typeChoicer.select( index );
		( (StackLayout) infoComp.getLayout( ) ).topControl = (Control) categoryPageMaps.get( choiceArray[index][0] );
		infoComp.layout( );
		hasLoaded = true;
		updatePreview( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.BaseStylePreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		IPreferenceStore ps = getPreferenceStore( );
		if ( ps instanceof StylePreferenceStore )
		{
			( (StylePreferenceStore) ps ).clearError( );
		}
		boolean rt = doStore( );
		if ( ps instanceof StylePreferenceStore )
		{
			return !( (StylePreferenceStore) ps ).hasError( );
		}
		return rt;
	}

	/**
	 * Stores the result pattern string into Preference Store.
	 * 
	 * @return
	 */
	protected boolean doStore( )
	{
		if ( typeChoicer == null || !isModified( ) )
		{
			return true;
		}
		try
		{
			( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormat( )
					.setCategory( getCategory( ) );
			( (StylePreferenceStore) getPreferenceStore( ) ).getDateTimeFormat( )
					.setPattern( getPatternStr( ) );
			return true;
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
			return false;
		}
	}

	private boolean isModified( )
	{
		String c = getCategory( );
		String p = getPatternStr( );
		if ( oldCategory == null )
		{
			if ( c != null )
			{
				return true;
			}
		}
		else if ( !oldCategory.equals( c ) )
		{
			return true;
		}
		if ( oldPattern == null )
		{
			if ( p != null )
			{
				return true;
			}
		}
		else if ( !oldPattern.equals( p ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Creates info panes for each format type choicer, adds them into paneMap
	 * for after getting.
	 * 
	 * @param parent
	 *            Parent contains these info panes.
	 */
	private HashMap createInfoPages( Composite parent )
	{
		if ( categoryPageMaps == null )
		{
			categoryPageMaps = new HashMap( 8 );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME,
					getGeneralPage( parent ) );
			categoryPageMaps.put( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM,
					getCustomPage( parent ) );
		}
		return categoryPageMaps;
	}

	/**
	 * Updates the format Pattern String, and Preview.
	 */
	private void updatePreview( )
	{
		String pattern = ""; //$NON-NLS-1$
		String fmtStr = ""; //$NON-NLS-1$

		String displayName = typeChoicer.getText( );
		String category = getCategoryorDisplayName( displayName );
		setCategory( category );

		if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			generalPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM.equals( category ) )
		{
			pattern = formatCode.getText( );
			if ( !StringUtil.isBlank( previewText.getText( ) ) )
			{
				String text = previewText.getText( );
				try
				{
					Date date = new Date( text );
					fmtStr = new DateFormatter( pattern ).format( new Date( text ) );
				}
				catch ( Exception e )
				{
					fmtStr = Messages.getString( "FormatDateTimePreferencePage.preview.invalidDateTimeToPreview" ); //$NON-NLS-1$
				}
			}
			else
			{
				fmtStr = new DateFormatter( pattern ).format( new Date( ) );
			}
			if ( fmtStr == null || fmtStr == "" ) //$NON-NLS-1$
			{
				fmtStr = Messages.getString( "FormatDateTimePreferencePage.preview.invalidFormatCode" ); //$NON-NLS-1$
				//				pattern = "";
			}
			cusPreviewLabel.setText( fmtStr );
			setPatternStr( pattern );
			return;
		}
	}

	/**
	 * Lazily creates the general page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The general page.
	 */
	private Composite getGeneralPage( Composite parent )
	{
		if ( generalPage == null )
		{
			generalPage = new Composite( parent, SWT.NULL );
			generalPage.setLayout( new GridLayout( 1, false ) );

			generalPreviewLabel = createPreviewPart( generalPage );
		}
		return generalPage;
	}

	/**
	 * Creates preview part for general page.
	 */
	private Label createPreviewPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getString( "FormatDateTimePreferencePage.previewPart.groupLabel" ) ); //$NON-NLS-1$
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		group.setLayoutData( data );
		group.setLayout( new GridLayout( 1, false ) );

		Label previewLabel = new Label( group, SWT.VERTICAL );
		previewLabel.setText( "" ); //$NON-NLS-1$
		previewLabel.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		return previewLabel;
	}

	/**
	 * Lazily creates the custom page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The custom page.
	 */
	private Composite getCustomPage( Composite parent )
	{
		if ( customPage == null )
		{
			customPage = new Composite( parent, SWT.NULL );
			customPage.setLayout( new GridLayout( 1, false ) );

			Group group = new Group( customPage, SWT.NONE );
			group.setText( Messages.getString( "FormatDateTimePreferencePage.customSettings.groupLabel" ) ); //$NON-NLS-1$
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( group, SWT.NONE );
			label.setText( Messages.getString( "FormatDateTimePreferencePage.exampleFormat.lable" ) ); //$NON-NLS-1$
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			label = new Label( group, SWT.NONE );
			label.setText( Messages.getString( "FormatDateTimePreferencePage.exampleFormat.lable2" ) ); //$NON-NLS-1$
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			createTable( group );

			Composite container = new Composite( customPage, SWT.NONE );
			data = new GridData( GridData.FILL_HORIZONTAL );
			container.setLayoutData( data );
			container.setLayout( new GridLayout( 2, false ) );

			new Label( container, SWT.NULL ).setText( Messages.getString( "FormatDateTimePreferencePage.formatCode.label" ) ); //$NON-NLS-1$
			formatCode = new Text( container, SWT.SINGLE | SWT.BORDER );
			formatCode.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			formatCode.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					if ( hasLoaded )
					{
						updatePreview( );
					}
				}
			} );

			group = new Group( customPage, SWT.NONE );
			group.setText( Messages.getString( "FormatDateTimePreferencePage.previewGroup.label" ) ); //$NON-NLS-1$
			data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			GridLayout layout = new GridLayout( 2, false );
			layout.verticalSpacing = 1;
			group.setLayout( layout );

			new Label( group, SWT.NONE ).setText( Messages.getString( "FormatDateTimePreferencePage.previewText.label" ) ); //$NON-NLS-1$
			previewText = new Text( group, SWT.SINGLE | SWT.BORDER );
			previewText.setText( "" ); //$NON-NLS-1$
			previewText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			previewText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					if ( hasLoaded )
					{
						updatePreview( );
					}
					if ( StringUtil.isBlank( previewText.getText( ) ) )
					{
						guideLabel.setText( "" ); //$NON-NLS-1$
					}
					else
					{
						guideLabel.setText( Messages.getString( "FormatDateTimePreferencePage.previewText.guideText" ) ); //$NON-NLS-1$
					}
				}
			} );
			new Label( group, SWT.NONE ).setText( "" ); //$NON-NLS-1$

			guideLabel = new Label( group, SWT.NONE );
			guideLabel.setText( "" ); //$NON-NLS-1$
			Font font = JFaceResources.getDialogFont( );
			FontData fData = font.getFontData( )[0];
			fData.setHeight( fData.getHeight( ) - 1 );
			guideLabel.setFont( new Font( Display.getCurrent( ), fData ) );

			data = new GridData( GridData.FILL_HORIZONTAL );
			guideLabel.setLayoutData( data );

			new Label( group, SWT.NONE ).setText( Messages.getString( "FormatDateTimePreferencePage.previewLabel.label" ) ); //$NON-NLS-1$
			cusPreviewLabel = new Label( group, SWT.NONE );
			cusPreviewLabel.setText( "" ); //$NON-NLS-1$
			cusPreviewLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			Composite helpBar = new Composite( customPage, SWT.NONE );
			data = new GridData( GridData.FILL_HORIZONTAL );
			helpBar.setLayoutData( data );
			helpBar.setLayout( new GridLayout( 3, false ) );

			new Label( helpBar, SWT.NONE ).setText( Messages.getString( "FormatDateTimePreferencePage.helpLabel.label" ) ); //$NON-NLS-1$
			Button help = new Button( helpBar, SWT.PUSH );
			help.setText( Messages.getString( "FormatDateTimePreferencePage.helpButton.label" ) ); //$NON-NLS-1$
			setButtonLayoutData( help );
			data = (GridData) help.getLayoutData( );
			data.horizontalAlignment = GridData.END;
			data.grabExcessHorizontalSpace = true;
		}
		return customPage;
	}

	/**
	 * Creates the table in custom page.
	 * 
	 * @param parent
	 *            Parent contains the table.
	 */
	private void createTable( Composite parent )
	{
		Table table = new Table( parent, SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION
				| SWT.BORDER );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String category = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );
				String pattern = getPatternForCategory( category );
				formatCode.setText( pattern );
				updatePreview( );
			}
		} );
		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( Messages.getString( "FormatDateTimePreferencePage.tableColumn.formatCode" ) ); //$NON-NLS-1$
		tableColumValue.setWidth( 120 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( Messages.getString( "FormatDateTimePreferencePage.tableColumn.formatResult" ) ); //$NON-NLS-1$
		tableColumnDisplay.setWidth( 130 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.generalDate" ), "01/23/2001 8:53:03 PM" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.longDate" ), "January 23, 2001" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.mediumDate" ), "23-Jan-01" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.shortDate" ), "01-23-2001" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.longTime" ), "8:45:00 PM" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.tableItem.mediumTime" ), "8:45:00 PM" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatDateTimePreferencePage.table.shortTime" ), "20:45" //$NON-NLS-1$ //$NON-NLS-2$
		} );
	}
}