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

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Format string page for formatting a string.
 */

public class FormatStringPage extends Composite implements IFormatPage
{

	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages.getString( "FormatStringPage.previewText.invalidFormatCode" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT_STRING_PAGE = Messages.getString( "FormatStringPage.label.formatStringAs" ); //$NON-NLS-1$
	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages.getString( "FormatStringPage.label.previewWithFormat" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTING_GROUP = Messages.getString( "FormatStringPage.label.customSettings" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTING = Messages.getString( "FormatStringPage.label.exampleFormats" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTING_LABEL = Messages.getString("FormatStringPage.label.style.custom.settings.label");  //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages.getString( "FormatStringPage.label.format.code" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_GROUP = Messages.getString( "FormatStringPage.label.preview" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_STRING = Messages.getString( "FormatStringPage.label.stringToPreview" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_LABEL = Messages.getString( "FormatStringPage.label.previewLabel" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages.getString( "FormatStringPage.label.table.collumn.exampleFormatCode" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages.getString( "FormatStringPage.label.table.collumn.exampleFormatResult" ); //$NON-NLS-1$

	private static final String SAMPLE_TEXT_ZIP_CODE = "94103"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_ZIP_C0DE4 = "941031234"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PHONE_NUMBER = "4155551111"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER = "123456789"; //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_TEXT = Messages.getString( "FormatStringPage.default.preview.text" ); //$NON-NLS-1$

	Locale DEFAULT_LOCALE = Locale.getDefault( );

	private String patternStr = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	private static final int FORMAT_TYPE_INDEX = 0;

	private Combo typeChoicer;
	private Composite infoComp;
	private Composite generalPage;
	private Composite customPage;

	private Label generalPreviewLabel;
	private Label cPreviewLabel;
	private Text formatCode;
	private Text previewTextBox;

	private boolean hasLoaded = false;

	private String previewText = null;

	private boolean isDirty = false;

	/**
	 * Constructs a new instance of format string page.
	 * 
	 * @param parent
	 *            The parent container of the page.
	 * @param style
	 *            style of the page
	 * @param pageAlignment
	 *            Aligns the page virtically(PAGE_ALIGN_VIRTICAL) or
	 *            horizontally(PAGE_ALIGN_HORIZONTAL).
	 */
	public FormatStringPage( Composite parent, int style, int pageAlignment )
	{
		super( parent, style );
		createContents( );
	}

	/**
	 * Constructs a new instance of format string page, default aligns the page
	 * virtically.
	 * 
	 * @param parent
	 *            The parent container of the page.
	 * @param style
	 *            style of the page
	 */
	public FormatStringPage( Composite parent, int style )
	{
		this( parent, style, PAGE_ALIGN_VIRTICAL );
	}

	/**
	 * Creates the contents of the page.
	 *  
	 */
	protected void createContents( )
	{
		setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		initChoiceArray( );
		getFormatTypes( );

		Composite topContainer = new Composite( this, SWT.NONE );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		topContainer.setLayoutData( data );
		topContainer.setLayout( new GridLayout( 2, false ) );

		new Label( topContainer, SWT.NONE ).setText( LABEL_FORMAT_STRING_PAGE );
		typeChoicer = new Combo( topContainer, SWT.READ_ONLY );
		data = new GridData( GridData.FILL_HORIZONTAL );
		typeChoicer.setLayoutData( data );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				reLayoutSubPages( );
				updatePreview( );
			}

		} );
		typeChoicer.setItems( getFormatTypes( ) );

		infoComp = new Composite( this, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );
		createCategoryPages( infoComp );
	}

	private void reLayoutSubPages( )
	{
		String category = getCategory4DisplayName( typeChoicer.getText( ) );

		Control control = (Control) categoryPageMaps.get( category );

		( (StackLayout) infoComp.getLayout( ) ).topControl = control;

		infoComp.layout( );
	}

	/**
	 * Sets the pattern string for this preference.
	 * 
	 * @param patternStr
	 *            The patternStr to set.
	 */
	private void setPattern( String patternStr )
	{
		this.patternStr = patternStr;
	}

	/**
	 * @param category
	 *            The category to set.
	 */
	private void setCategory( String category )
	{
		this.category = category;
	}

	/**
	 * Returns the patternStr from the page.
	 */
	public String getPattern( )
	{
		return patternStr;
	}

	/**
	 * Returns the category from the page.
	 */
	public String getCategory( )
	{
		return category;
	}

	/**
	 * Returns the formatString from the page.
	 */
	public String getFormatString( )
	{
		if ( category == null && patternStr == null )
		{
			return null;
		}
		if ( category == null )
		{
			category = ""; //$NON-NLS-1$
		}
		if ( patternStr == null )
		{
			patternStr = ""; //$NON-NLS-1$
		}
		// special case: when pattern equals category, omits(eliminatess) the
		// pattern, only returns the category.-----> for parameter dialog use.
		if ( category.equals( patternStr ) )
		{
			return category;
		}
		return category + ":" + patternStr; //$NON-NLS-1$
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */
	protected String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( StringFormatValue.FORMAT_VALUE_STRUCT,
					StringFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				choiceArray = new String[4][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
					if ( choices[i].getName( )
							.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE )
							|| choices[i].getName( )
									.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM ) )
					{
						choiceArray[j][0] = choices[i].getDisplayName( );
						choiceArray[j][1] = choices[i].getName( );
						j++;
					}
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
		if ( choiceArray != null )
		{
			formatTypes = new String[choiceArray.length];
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				formatTypes[i] = choiceArray[i][0];
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
		if ( choiceArray != null )
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
	private String getCategory4DisplayName( String displayName )
	{
		if ( choiceArray != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][0].equals( displayName ) )
				{
					return choiceArray[i][1];
				}
			}
		}
		return displayName;
	}

	/**
	 * Gets the corresponding internal display name given the category.
	 * 
	 * @param category
	 * @return
	 */
	private String getDisplayName4Category( String category )
	{
		return ChoiceSetFactory.getStructDisplayName(
				StringFormatValue.FORMAT_VALUE_STRUCT,
				StringFormatValue.CATEGORY_MEMBER, category );
	}

	/**
	 * Retrieves format pattern from arrays given format type name.
	 * 
	 * @param formatType
	 *            Given format type name.
	 * @return The corresponding format pattern string.
	 */
	protected String getPatternForCategory( String category )
	{
		String pattern = category;
		if ( category == null )
		{
			pattern = ""; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equals( category ) )
		{
			pattern = ""; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals( category ) )
		{
			pattern = ">"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals( category ) )
		{
			pattern = "<"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals( category ) )
		{
			pattern = "@@@@@"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals( category ) )
		{
			pattern = "@@@@@-@@@@"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals( category ) )
		{
			pattern = "(@@@)@@@-@@@@"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals( category ) )
		{
			pattern = "@@@-@@-@@@@"; //$NON-NLS-1$
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
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage#setPreviewText(java.lang.String)
	 */
	public void setPreviewText( String text )
	{
		if ( text == null || StringUtil.isBlank( text ) )
		{
			previewText = null;
		}
		else
		{
			previewText = text;
		}
		updatePreview( );
		return;
	}

	/**
	 * @return Returns the previewText.
	 */
	private String getPreviewText( )
	{
		return previewText;
	}

	/**
	 * Sets input of the page.
	 * 
	 * @param formatString
	 *            The input format string.
	 * @author Liu sanyong: -----> for parameter dialog use.
	 */
	public void setInput( String formatString )
	{
		if ( formatString == null )
		{
			setInput( null, null );
			return;
		}
		String fmtStr = formatString;
		int pos = fmtStr.indexOf( ":" ); //$NON-NLS-1$
		if ( StringUtil.isBlank( fmtStr ) )
		{
			setInput( null, null );
			return;
		}
		else if ( pos == -1 )
		{ // special case: only contains category, copy the category to pattern.
			setInput( fmtStr, fmtStr );
			return;
		}

		String category = fmtStr.substring( 0, pos );
		String patternStr = fmtStr.substring( pos + 1 );
		setInput( category, patternStr );
		return;
	}

	/**
	 * Sets input of the page.
	 * 
	 * @param category
	 *            The category of the format string.
	 * @param patternStr
	 *            The pattern of the format string.
	 */
	public void setInput( String categoryStr, String patternStr )
	{
		hasLoaded = false;

		if ( categoryStr == null )
		{
			typeChoicer.select( 0 );
		}
		else
		{
			if ( categoryStr.equals( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM ) )
			{
				formatCode.setText( patternStr == null ? "" : patternStr ); //$NON-NLS-1$
			}
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}

		// re layout sub pages.
		reLayoutSubPages( );

		// update preview.
		updatePreview( );

		// set initial.
		oldCategory = categoryStr;
		oldPattern = patternStr;

		hasLoaded = true;
		return;
	}

	/**
	 * Determines the format string is modified or not from the page.
	 * 
	 * @return Returns true if the format string is modified.
	 */
	public boolean isFormatModified( )
	{
		String c = getCategory( );
		String p = getPattern( );
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
	 * Returns whether the page is modified.
	 * 
	 * @return Returns the isDirty.
	 */
	public boolean isDirty( )
	{
		return isDirty;
	}

	/**
	 * Marks the dirty marker of the page.
	 * 
	 * @param dirty
	 */
	private void markDirty( boolean dirty )
	{
		isDirty = dirty;
	}

	/**
	 * Updates the format Pattern String, and Preview.
	 */
	private void updatePreview( )
	{
		markDirty( hasLoaded );

		String pattern = ""; //$NON-NLS-1$
		String fmtStr = ""; //$NON-NLS-1$

		String gText = getPreviewText( ) == null ? DEFAULT_PREVIEW_TEXT
				: getPreviewText( );

		String category = getCategory4DisplayName( typeChoicer.getText( ) );
		setCategory( category );

		if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			generalPreviewLabel.setText( gText );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			gText = SAMPLE_TEXT_ZIP_CODE;
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			gText = SAMPLE_TEXT_ZIP_C0DE4;
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			gText = SAMPLE_TEXT_PHONE_NUMBER;
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			gText = SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER;
			fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals( category ) )
		{
			pattern = formatCode.getText( );
			if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
			{
				fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			}
			else
			{
				fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( previewTextBox.getText( ) );
			}

			cPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
	}

	private String validatedFmtStr( String fmtStr )
	{
		String text = fmtStr;
		if ( text == null )
		{
			text = PREVIEW_TEXT_INVALID_FORMAT_CODE;
		}
		return text;
	}

	/**
	 * Creates info panes for each format type choicer, adds them into paneMap
	 * for after getting.
	 * 
	 * @param parent
	 *            Parent contains these info panes.
	 */
	private void createCategoryPages( Composite parent )
	{
		categoryPageMaps = new HashMap( );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM,
				getCustomPage( parent ) );
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
		group.setText( LABEL_GENERAL_PREVIEW_GROUP );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		group.setLayoutData( data );
		group.setLayout( new GridLayout( 1, false ) );

		Label previewText = new Label( group, SWT.NONE );
		previewText.setText( "" ); //$NON-NLS-1$
		previewText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		return previewText;
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
			group.setText( LABEL_CUSTOM_SETTING_GROUP );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( group, SWT.NONE );
			label.setText( LABEL_CUSTOM_SETTING );
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			label = new Label( group, SWT.NONE );
			label.setText( LABEL_CUSTOM_SETTING_LABEL );
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			createTable( group );

			Composite container = new Composite( customPage, SWT.NONE );
			data = new GridData( GridData.FILL_HORIZONTAL );
			container.setLayoutData( data );
			container.setLayout( new GridLayout( 2, false ) );

			new Label( container, SWT.NULL ).setText( LABEL_FORMAT_CODE );
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
			group.setText( LABEL_PREVIEW_GROUP );
			data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			new Label( group, SWT.NONE ).setText( LABEL_PREVIEW_STRING );
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
			previewTextBox.setText( DEFAULT_PREVIEW_TEXT );
			previewTextBox.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			previewTextBox.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					if ( hasLoaded )
					{
						setPreviewText( previewTextBox.getText( ) );
					}
				}
			} );

			new Label( group, SWT.NONE ).setText( LABEL_PREVIEW_LABEL );
			cPreviewLabel = new Label( group, SWT.NONE );
			cPreviewLabel.setText( DEFAULT_PREVIEW_TEXT );
			cPreviewLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 2;
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );
				String category = ChoiceSetFactory.getStructPropValue(
						StringFormatValue.FORMAT_VALUE_STRUCT,
						StringFormatValue.CATEGORY_MEMBER, displayName );
				String pattern = getPatternForCategory( category );
				formatCode.setText( pattern );
				updatePreview( );
			}
		} );
		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE );
		tableColumValue.setWidth( 120 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT );
		tableColumnDisplay.setWidth( 120 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE ),
				new StringFormatter( getPatternForCategory( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE ),
						DEFAULT_LOCALE ).format( DEFAULT_PREVIEW_TEXT )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE ),
				new StringFormatter( getPatternForCategory( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE ),
						DEFAULT_LOCALE ).format( DEFAULT_PREVIEW_TEXT )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4 ),
				new StringFormatter( getPatternForCategory( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4 ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_C0DE4 )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER ),
				new StringFormatter( getPatternForCategory( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_PHONE_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER ),
				new StringFormatter( getPatternForCategory( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER )
		} );
	}
}