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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
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
	private static final String LABEL_CUSTOM_SETTINGS_GROUP = Messages.getString( "FormatStringPage.label.customSettings" ); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages.getString( "FormatStringPage.label.format.code" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages.getString( "FormatStringPage.label.table.collumn.exampleFormatCode" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages.getString( "FormatStringPage.label.table.collumn.exampleFormatResult" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_LABEL = Messages.getString( "FormatStringPage.label.custom.preview.label" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_GROUP = Messages.getString( "FormatStringPage.label.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTING_TEXT = Messages.getString( "FormatStringPage.label.custom.settings.label" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_STRING = Messages.getString( "FormatStringPage.label.preview.string" ); //$NON-NLS-1$

	private static final String SAMPLE_TEXT_ZIP_CODE = "94103"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_ZIP_C0DE4 = "941031234"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PHONE_NUMBER = "4155551111"; //$NON-NLS-1$
	private static final String SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER = "123456789"; //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_TEXT = Messages.getString( "FormatStringPage.default.preview.text" ); //$NON-NLS-1$

	Locale DEFAULT_LOCALE = Locale.getDefault( );

	private String pattern = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	private static final int FORMAT_TYPE_INDEX = 0;
	private static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private Combo typeChoicer;
	private Composite infoComp;
	private Composite formatCodeComp;

	private Composite generalPage;
	private Composite customPage;

	private Composite generalFormatCodePage;
	private Composite customFormatCodePage;

	private Label generalPreviewLabel;
	private Label cPreviewLabel;
	private Text formatCode;
	private Text previewTextBox;

	private boolean hasLoaded = false;

	private String previewText = null;

	private boolean isDirty = false;

	/**
	 * Listener, or <code>null</code> if none
	 */
	private java.util.List listeners = new ArrayList( );

	private int pageAlignment;

	private Table table;

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
		this.pageAlignment = pageAlignment;

		createContents( pageAlignment );
	}

	/**
	 * Creates the contents of the page.
	 *  
	 */

	protected void createContents( int pageAlignment )
	{
		initChoiceArray( );
		getFormatTypes( );

		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			createContentsHorizontally( );
		}
		else
		{
			createContentsVirtically( );
		}
	}

	protected void createContentsVirtically( )
	{
		setLayout( UIUtil.createGridLayoutWithoutMargin( ) );

		Composite topContainer = new Composite( this, SWT.NONE );
		topContainer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		topContainer.setLayout( new GridLayout( 2, false ) );

		new Label( topContainer, SWT.NONE ).setText( LABEL_FORMAT_STRING_PAGE );
		typeChoicer = new Combo( topContainer, SWT.READ_ONLY );
		typeChoicer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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

		setInput( null, null );
		setPreviewText( DEFAULT_PREVIEW_TEXT );
	}

	protected void createContentsHorizontally( )
	{
		setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		// create format type choicer
		Composite container = new Composite( this, SWT.NONE );
		GridData data = new GridData( );
		data.widthHint = DEFAULT_CATEGORY_CONTAINER_WIDTH;
		container.setLayoutData( data );
		container.setLayout( new GridLayout( 1, false ) );

		new Label( container, SWT.NONE ).setText( LABEL_FORMAT_STRING_PAGE );
		typeChoicer = new Combo( container, SWT.READ_ONLY );
		typeChoicer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				reLayoutSubPages( );

				updatePreview( );
			}
		} );
		typeChoicer.setItems( getFormatTypes( ) );

		// create the right part setting pane
		infoComp = new Composite( this, SWT.NONE );
		data = new GridData( GridData.FILL_BOTH );
		data.verticalSpan = 2;
		infoComp.setLayoutData( data );
		infoComp.setLayout( new StackLayout( ) );

		createCategoryPages( infoComp );

		// create left bottom part format code pane
		formatCodeComp = new Composite( this, SWT.NONE );
		data = new GridData( GridData.FILL_VERTICAL );
		data.widthHint = DEFAULT_CATEGORY_CONTAINER_WIDTH;
		formatCodeComp.setLayoutData( data );
		formatCodeComp.setLayout( new StackLayout( ) );

		createFormatCodePages( formatCodeComp );

		setInput( null, null );
		setPreviewText( DEFAULT_PREVIEW_TEXT );

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

		categoryPageMaps.put( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM,
				getCustomPage( parent ) );
	}

	/**
	 * Creates formatCode pages.
	 */

	private void createFormatCodePages( Composite parent )
	{
		getHorizonGeneralFormatCodePage( parent );

		getHorizonCustomFormatCodePage( parent );
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */

	private String[][] initChoiceArray( )
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
		if ( initChoiceArray( ) != null )
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
		return 0;
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
		return ChoiceSetFactory.getStructDisplayName( StringFormatValue.FORMAT_VALUE_STRUCT,
				StringFormatValue.CATEGORY_MEMBER,
				category );
	}

	/**
	 * Retrieves format pattern from arrays given format type name.
	 * 
	 * @param formatType
	 *            Given format type name.
	 * @return The corresponding format pattern string.
	 */

	private String getPatternForCategory( String category )
	{
		String pattern;
		if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equals( category ) )
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

	private void fireFormatChanged( String newCategory, String newPattern )
	{
		if ( listeners.isEmpty( ) )
		{
			return;
		}
		FormatChangeEvent event = new FormatChangeEvent( this,
				StyleHandle.STRING_FORMAT_PROP,
				newCategory,
				newPattern );
		for ( Iterator iter = listeners.iterator( ); iter.hasNext( ); )
		{
			Object listener = iter.next( );
			if ( listener instanceof IFormatChangeListener )
			{
				( (IFormatChangeListener) listener ).formatChange( event );
			}
		}
	}

	/**
	 * Adds format change listener to the litener list of this format page.
	 * 
	 * @param listener
	 *            The Format change listener to add.
	 */

	public void addFormatChangeListener( IFormatChangeListener listener )
	{
		if ( !listeners.contains( listener ) )
		{
			listeners.add( listener );
		}
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
		{ 
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

		initiatePageLayout( categoryStr, patternStr );
		reLayoutSubPages( );
		updatePreview( );

		// set initial.
		oldCategory = categoryStr;
		oldPattern = patternStr;

		hasLoaded = true;
		return;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage#setPreviewText(java.lang.String)
	 */

	public void setPreviewText( String preText )
	{
		if ( preText == null )
		{
			previewTextBox.setText( DEFAULT_PREVIEW_TEXT );
		}
		else
		{
			previewTextBox.setText( preText );
		}
		return;
	}

	/**
	 * Returns the patternStr from the page.
	 */

	public String getPattern( )
	{
		return pattern;
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
		if ( category == null && pattern == null )
		{
			return null;
		}
		if ( category == null )
		{
			category = ""; //$NON-NLS-1$
		}
		if ( pattern == null )
		{
			pattern = ""; //$NON-NLS-1$
		}
		if ( category.equals( pattern ) )
		{
			return category;
		}
		return category + ":" + pattern; //$NON-NLS-1$
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
	 */

	public void setEnabled( boolean enabled )
	{
		super.setEnabled( enabled );
		setControlsEnabeld( enabled );
	}

	/**
	 * @return Returns the previewText.
	 */

	private String getPreviewText( )
	{
		return previewText;
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
	 * Sets the pattern string for this preference.
	 * 
	 * @param pattern
	 *            The patternStr to set.
	 */

	private void setPattern( String pattern )
	{
		this.pattern = pattern;
	}

	private void setDefaultPreviewText( String defText )
	{
		if ( defText == null || StringUtil.isBlank( defText ) )
		{
			previewText = null;
		}
		else
		{
			previewText = defText;
		}
		return;
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
	 * Updates the format Pattern String, and Preview.
	 */

	private void updatePreview( )
	{
		markDirty( hasLoaded );

		String gText;
		if ( getPreviewText( ) == null )
		{
			gText = DEFAULT_PREVIEW_TEXT;
		}
		else
		{
			gText = getPreviewText( );
		}

		String category = getCategory4DisplayName( typeChoicer.getText( ) );
		setCategory( category );

		if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			generalPreviewLabel.setText( gText );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_UPPERCASE.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_LOWERCASE.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_CODE );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_ZIP_CODE_4.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_C0DE4 );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_PHONE_NUMBER.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_PHONE_NUMBER );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals( category ) )
		{
			String pattern = getPatternForCategory( category );
			gText = SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER;
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals( category ) )
		{
			String pattern = formatCode.getText( );
			String fmtStr;
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
		}

		if ( hasLoaded )
		{
			fireFormatChanged( getCategory( ), getPattern( ) );
		}
		return;
	}

	private void initiatePageLayout( String categoryStr, String patternStr )
	{
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
	}

	private void reLayoutSubPages( )
	{
		String category = getCategory4DisplayName( typeChoicer.getText( ) );

		Control control = (Control) categoryPageMaps.get( category );

		( (StackLayout) infoComp.getLayout( ) ).topControl = control;

		infoComp.layout( );

		if ( formatCodeComp != null )
		{
			if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) )
			{
				( (StackLayout) formatCodeComp.getLayout( ) ).topControl = getHorizonCustomFormatCodePage( formatCodeComp );
			}
			else
			{
				( (StackLayout) formatCodeComp.getLayout( ) ).topControl = getHorizonGeneralFormatCodePage( formatCodeComp );
			}
			formatCodeComp.layout( );
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
			GridLayout layout = new GridLayout( 1, false );
			layout.marginHeight = 0;
			generalPage.setLayout( layout );

			generalPreviewLabel = createGeneralPreviewPart( generalPage );
		}
		return generalPage;
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
			customPage.setLayout( createGridLayout4Page( ) );

			createCustomSettingsPart( customPage );

			if ( pageAlignment == PAGE_ALIGN_VIRTICAL )
			{
				Composite container = new Composite( customPage, SWT.NONE );
				container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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
			}

			createCustomPreviewPart( customPage );

		}
		return customPage;
	}

	private Composite getHorizonGeneralFormatCodePage( Composite parent )
	{
		if ( generalFormatCodePage == null )
		{
			generalFormatCodePage = new Composite( parent, SWT.NULL );
			GridLayout layout = new GridLayout( 1, false );
			layout.marginHeight = 1;
			generalFormatCodePage.setLayout( layout );

			Label l = new Label( generalFormatCodePage, SWT.SEPARATOR
					| SWT.HORIZONTAL );
			l.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}
		return generalFormatCodePage;
	}

	private Composite getHorizonCustomFormatCodePage( Composite parent )
	{
		if ( customFormatCodePage == null )
		{
			customFormatCodePage = new Composite( parent, SWT.NONE );
			GridLayout layout = new GridLayout( 1, false );
			layout.marginHeight = 1;
			customFormatCodePage.setLayout( layout );

			Label l = new Label( customFormatCodePage, SWT.SEPARATOR
					| SWT.HORIZONTAL );
			l.setLayoutData(  new GridData( GridData.FILL_HORIZONTAL ) );

			Composite container = new Composite( customFormatCodePage, SWT.NONE );
			container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
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
		}
		return customFormatCodePage;
	}

	private Label createGeneralPreviewPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( LABEL_GENERAL_PREVIEW_GROUP );
		GridData data;
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			data = new GridData( GridData.FILL_BOTH );
		}
		else
		{
			data = new GridData( GridData.FILL_HORIZONTAL );
		}
		group.setLayoutData( data );
		group.setLayout( new GridLayout( 1, false ) );

		Label previewText = new Label( group, SWT.CENTER
				| SWT.HORIZONTAL
				| SWT.VERTICAL );
		previewText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		return previewText;
	}

	private void createCustomSettingsPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( LABEL_CUSTOM_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 2, false ) );

		Label label = new Label( group, SWT.NONE );
		label.setText( LABEL_CUSTOM_SETTING_TEXT );
		GridData data = new GridData( );
		data.horizontalSpan = 2;
		label.setLayoutData( data );

		createTable( group );
	}

	private void createCustomPreviewPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( LABEL_CUSTOM_PREVIEW_GROUP );
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			group.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			group.setLayout( new GridLayout( 1, false ) );
		}
		else
		{
			group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			group.setLayout( new GridLayout( 2, false ) );
		}

		new Label( group, SWT.NONE ).setText( LABEL_CUSTOM_PREVIEW_STRING );
		previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
		previewTextBox.setText( DEFAULT_PREVIEW_TEXT ); //$NON-NLS-1$
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			data.horizontalIndent = 10;
		}
		previewTextBox.setLayoutData( data );
		previewTextBox.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				setDefaultPreviewText( previewTextBox.getText( ) );
				if ( hasLoaded )
				{
					updatePreview( );
				}
			}
		} );

		Label label = new Label( group, SWT.NONE );
		label.setText( LABEL_CUSTOM_PREVIEW_LABEL );
		label.setLayoutData( new GridData( ) );

		cPreviewLabel = new Label( group, SWT.CENTER
				| SWT.HORIZONTAL
				| SWT.VIRTUAL );
		cPreviewLabel.setText( "" ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 1;
		cPreviewLabel.setLayoutData( data );
	}

	/**
	 * Creates the table in custom page.
	 * 
	 * @param parent
	 *            Parent contains the table.
	 */

	private void createTable( Composite parent )
	{
		table = new Table( parent, SWT.FULL_SELECTION
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
				String category = ChoiceSetFactory.getStructPropValue( StringFormatValue.FORMAT_VALUE_STRUCT,
						StringFormatValue.CATEGORY_MEMBER,
						displayName );
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

	private GridLayout createGridLayout4Page( )
	{
		GridLayout layout;
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			layout = new GridLayout( 2, false );
			layout.marginHeight = 0;
		}
		else
		{
			layout = new GridLayout( 1, false );
			layout.marginHeight = 0;
		}
		return layout;
	}

	private GridData createGridData4Part( )
	{
		GridData data;
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			data = new GridData( GridData.FILL_VERTICAL );
		}
		else
		{
			data = new GridData( GridData.FILL_HORIZONTAL );
		}
		return data;
	}

	private void setControlsEnabeld( boolean b )
	{
		typeChoicer.setEnabled( b );

		formatCode.setEnabled( b );
		previewTextBox.setEnabled( b );
		table.setEnabled( b );
	}
}