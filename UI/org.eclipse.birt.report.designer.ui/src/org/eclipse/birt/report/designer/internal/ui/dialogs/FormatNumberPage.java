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
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.NumberFormatValue;
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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * Format number page for formatting numbers.
 */

public class FormatNumberPage extends Composite implements IFormatPage
{

	private static final String PREVIEW_TEXT_INVALID_NUMBER_TO_PREVIEW = Messages.getString( "FormatNumberPage.preview.invalidNumber" ); //$NON-NLS-1$
	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages.getString( "FormatNumberPage.preview.invalidFormatCode" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT_NUMBER_PAGE = Messages.getString( "FormatNumberPage.label.format.number.page" ); //$NON-NLS-1$
	private static final String LABEL_CURRENCY_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.currency.settings" ); //$NON-NLS-1$
	private static final String LABEL_CURRENCY_SYMBOL = Messages.getString( "FormatNumberPage.label.symbol" ); //$NON-NLS-1$
	private static final String LABEL_FIXED_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.fixed.settings" ); //$NON-NLS-1$
	private static final String LABEL_PERCENT_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.percent.settings" ); //$NON-NLS-1$
	private static final String LABEL_USE_1000S_SEPARATOR = Messages.getString( "FormatNumberPage.label.use1000sSeparator" ); //$NON-NLS-1$
	private static final String LABEL_USE_LEADING_ZERO = Messages.getString( "FormatNumberPage.label.useLeadingZero" ); //$NON-NLS-1$
	private static final String LABEL_SYMBOL_POSITION = Messages.getString( "FormatNumberPage.label.symbol.position" ); //$NON-NLS-1$
	private static final String LABEL_NEGATIVE_NUMBERS = Messages.getString( "FormatNumberPage.label.negative.numbers" ); //$NON-NLS-1$
	private static final String LABEL_SCIENTIFIC_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.scientific.settings" ); //$NON-NLS-1$
	private static final String LABEL_DECIMAL_PLACES = Messages.getString( "FormatNumberPage.label.decimal.places" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.custom.settings" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS_LABEL = Messages.getString( "FormatNumberPage.label.custom.settings.lable" ); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages.getString( "FormatNumberPage.label.format.code" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_PREVIEW_GROUP = Messages.getString( "FormatNumberPage.label.custom.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_NUMBER = Messages.getString( "FormatNumberPage.label.preview.number" ); //$NON-NLS-1$
	private static final String LABEL_COSTOM_PREVIEW_LABEL = Messages.getString( "FormatNumberPage.label.custom.preview.label" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages.getString( "FormatNumberPage.label.table.column.format.code" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages.getString( "FormatNumberPage.label.table.column.format.result" ); //$NON-NLS-1$
	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages.getString( "FormatNumberPage.label.general.preview.group" ); //$NON-NLS-1$

	public static final String TEXT_CURRENCY_SYMBOL_NONE = Messages.getString( "FormatNumberPage.currency.symbol.none" ); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_AFTER = Messages.getString( "FormatNumberPage.symblePos.after" ); //$NON-NLS-1$
	public static final String SYMBOL_POSITION_BEFORE = Messages.getString( "FormatNumberPage.symblePos.before" ); //$NON-NLS-1$

	private String pattern = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;
	private HashMap categoryPatternMaps;

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	private static final int FORMAT_TYPE_INDEX = 0;
	private static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private static final String DEFAULT_PREVIEW_TEXT = "1234.56"; //$NON-NLS-1$
	private static final double DEFAULT_PREVIEW_NUMBER = Double.parseDouble( DEFAULT_PREVIEW_TEXT );

	private static String[] symbols = {
			// "none", "£¤","$", "?", "¡ê"
			Messages.getString( "FormatNumberPage.currency.symbol.none" ), "\uffe5", "$", "\u20ac", "\uffe1" //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	};

	private Combo typeChoicer;

	private Composite infoComp;
	private Composite formatCodeComp;

	private Composite generalPage;
	private Composite currencyPage;
	private Composite fixedPage;
	private Composite percentPage;
	private Composite scientificPage;
	private Composite customPage;

	private Composite generalFormatCodePage;
	private Composite customFormatCodePage;

	private Text previewTextBox;
	private Text formatCodeBox;

	private Label gPreviewLabel, cPreviewLabel, fPreviewLabel, pPreviewLabel,
			sPreviewLabel, cusPreviewLabel;

	private Combo cPlacesChoice, cSymbolChoice, cSymPosChoice, fPlacesChoice,
			pSymPosChoice, pPlacesChoice, sPlacesChoice;

	private Button cUseSep, fUseSep, fUseZero, pUseSep, pUseZero;
	private List cNegNumChoice, fNegNumChoice, pNegNumChoice;
	private Table table;

	private boolean hasLoaded = false;
	private boolean isDirty = false;

	private String previewText = null;

	/**
	 * Listener, or <code>null</code> if none
	 */
	private java.util.List listeners = new ArrayList( );

	private int pageAlignment;

	private SelectionListener mySelectionListener = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			updatePreview( );
		}
	};

	private ModifyListener myModifyListener = new ModifyListener( ) {

		public void modifyText( ModifyEvent e )
		{
			if ( hasLoaded )
			{
				updatePreview( );
			}
		}
	};

	/**
	 * Constructs a page for formatting numbers, default aligns the page
	 * virtically.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 */
	public FormatNumberPage( Composite parent, int style )
	{
		this( parent, style, PAGE_ALIGN_VIRTICAL );
	}

	/**
	 * Constructs a page for formatting numbers.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 * @param pageAlignment
	 *            Aligns the page virtically(PAGE_ALIGN_VIRTICAL) or
	 *            horizontally(PAGE_ALIGN_HORIZONTAL).
	 */
	public FormatNumberPage( Composite parent, int style, int pageAlignment )
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
		new Label( topContainer, SWT.NONE ).setText( LABEL_FORMAT_NUMBER_PAGE );
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

		createCategoryPatterns( );

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

		new Label( container, SWT.NONE ).setText( LABEL_FORMAT_NUMBER_PAGE );
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

		createCategoryPatterns( );

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

		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				getGeneralPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY,
				getCurrencyPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED,
				getFixedPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT,
				getPercentPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				getScientificPage( parent ) );
		categoryPageMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM,
				getCustomPage( parent ) );
	}

	private void createFormatCodePages( Composite parent )
	{
		getHorizonGeneralFormatCodePage( parent );
		getHorizonCustomFormatCodePage( parent );
	}

	private void createCategoryPatterns( )
	{
		categoryPatternMaps = new HashMap( );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				new FormatNumberPattern( ) );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY,
				new FormatCurrencyNumPattern( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED,
				new FormatFixedNumPattern( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT,
				new FormatPercentNumPattern( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				new FormatScientificNumPattern( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) );

		categoryPatternMaps.put( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM,
				new FormatCustomNumPattern( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) );
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */

	private String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet( NumberFormatValue.FORMAT_VALUE_STRUCT,
					NumberFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				// excludes "standard" and "unformatted" category.
				choiceArray = new String[choices.length - 2][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
					if ( !choices[i].getName( )
							.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_STANDARD )
							&& !choices[i].getName( )
									.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED ) )
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
		return 0;
	}

	/**
	 * Gets the corresponding category for given display name.
	 */

	private String getCategory4DisplayName( String displayName )
	{
		if ( initChoiceArray( ) != null )
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
		return ChoiceSetFactory.getStructDisplayName( NumberFormatValue.FORMAT_VALUE_STRUCT,
				NumberFormatValue.CATEGORY_MEMBER,
				category );
	}

	private String getPatternForCategory( String category )
	{
		String pattern;
		if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals( category ) )
		{
			String sym = Currency.getInstance( Locale.getDefault( ) )
					.getSymbol( );
			pattern = sym + "###,##0.00"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED.equals( category ) )
		{
			pattern = "#0.00"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT.equals( category ) )
		{
			pattern = "0.00%"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals( category ) )
		{
			pattern = "0.00E00"; //$NON-NLS-1$
		}
		else
		{
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	private FormatNumberPattern getFmtPattern4Category( String category )
	{
		FormatNumberPattern fmtPattern = null;
		if ( categoryPatternMaps != null )
		{
			fmtPattern = (FormatNumberPattern) categoryPatternMaps.get( category );
		}
		if ( fmtPattern == null )
		{
			fmtPattern = new FormatNumberPattern( );
		}
		return fmtPattern;
	}

	private Control getFmtPage4Category( String category )
	{
		Control page = null;
		if ( categoryPageMaps != null )
		{
			page = (Control) categoryPageMaps.get( category );
		}
		if ( page == null )
		{
			page = getGeneralPage( infoComp );
		}
		return page;
	}

	private void fireFormatChanged( String newCategory, String newPattern )
	{
		if ( listeners.isEmpty( ) )
		{
			return;
		}
		FormatChangeEvent event = new FormatChangeEvent( this,
				Style.NUMBER_FORMAT_PROP,
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
	 * @param categoryStr
	 *            The category of the format string.
	 * @param patternStr
	 *            The pattern of the format string.
	 */

	public void setInput( String categoryStr, String patternStr )
	{
		hasLoaded = false;

		initiatePageLayout( categoryStr, patternStr );

		// re layout sub page.
		reLayoutSubPages( );

		updatePreview( );

		// set initial.
		oldCategory = categoryStr;
		oldPattern = patternStr;

		hasLoaded = true;
		return;
	}

	/*
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
	 * Returns the category resulted from the page.
	 */

	public String getCategory( )
	{
		return category;
	}

	/**
	 * Returns the patternStr from the page.
	 */

	public String getPattern( )
	{
		return pattern;
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
	 * @return true if the format string is modified.
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
		setControlsEnabled( enabled );
	}

	private String getPreviewText( )
	{
		return previewText;
	}

	private void setPattern( String pattern )
	{
		this.pattern = pattern; //$NON-NLS-1$
	}

	private void setCategory( String category )
	{
		this.category = category; //$NON-NLS-1$
	}

	private void setDefaultPreviewText( String defText )
	{
		if ( defText == null
				|| StringUtil.isBlank( defText )
				|| !DEUtil.isValidNumber( defText ) )
		{
			previewText = null;
		}
		else
		{
			previewText = defText;
		}
		return;
	}

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

		if ( hasLoaded )
		{// avoid setting pattern from controls when the typechoicer is selected
			// before loading.
			setFmtPatternFromControls( );
		}

		String category = getCategory4DisplayName( typeChoicer.getText( ) );
		FormatNumberPattern fmtPattern = getFmtPattern4Category( category );

		setCategory( fmtPattern.getCategory( ) );
		setPattern( fmtPattern.getPattern( ) );

		doPreview( fmtPattern.getCategory( ), fmtPattern.getPattern( ) );

		if ( hasLoaded )
		{
			fireFormatChanged( fmtPattern.getCategory( ),
					fmtPattern.getPattern( ) );
		}
	}

	/**
	 * Does preview action.
	 * 
	 * @param patternStr
	 */

	private void doPreview( String category, String patternStr )
	{
		String fmtStr;

		double num;
		if ( getPreviewText( ) == null )
		{
			num = DEFAULT_PREVIEW_NUMBER;
		}
		else
		{
			num = Double.parseDouble( getPreviewText( ) );
		}

		if ( category == null )
		{
			fmtStr = getPreviewText( );
			gPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			gPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			cPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			fPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			pPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			sPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) )
		{
			if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
			{
				fmtStr = new NumberFormatter( patternStr ).format( num );
			}
			else if ( DEUtil.isValidNumber( previewTextBox.getText( ) ) )
			{
				fmtStr = new NumberFormatter( patternStr ).format( Double.parseDouble( previewTextBox.getText( ) ) );
			}
			else
			{
				fmtStr = PREVIEW_TEXT_INVALID_NUMBER_TO_PREVIEW;
			}
			cusPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
	}

	private void initiatePageLayout( String categoryStr, String patternStr )
	{
		if ( categoryStr == null )
		{
			typeChoicer.select( 0 );
			return;
		}

		FormatNumberPattern fmtPattern = getFmtPattern4Category( categoryStr );
		fmtPattern.setPattern( patternStr );

		if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER.equals( categoryStr ) )
		{
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals( categoryStr ) )
		{
			refreshCurrencySetting( (FormatCurrencyNumPattern) fmtPattern );
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED.equals( categoryStr ) )
		{
			refreshFixedSetting( (FormatFixedNumPattern) fmtPattern );
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT.equals( categoryStr ) )
		{
			refreshPercentSetting( (FormatPercentNumPattern) fmtPattern );
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals( categoryStr ) )
		{
			refreshScientificSetting( (FormatScientificNumPattern) fmtPattern );
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals( categoryStr ) )
		{
			refreshCustomSetting( (FormatCustomNumPattern) fmtPattern );
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}
		else
		{
			// default for illegal input category.
			typeChoicer.select( 0 );
		}
		return;
	}

	/**
	 * Re layouts sub pages according to the selected format type.
	 */

	private void reLayoutSubPages( )
	{
		String category = getCategory4DisplayName( typeChoicer.getText( ) );

		Control control = getFmtPage4Category( category );

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

	private void refreshCurrencySetting( FormatCurrencyNumPattern fmtPattern )
	{
		cPlacesChoice.setText( String.valueOf( fmtPattern.getDecPlaces( ) ) );
		cUseSep.setSelection( fmtPattern.getUseSep( ) );
		if ( !StringUtil.isBlank( fmtPattern.getSymbol( ) ) ) //$NON-NLS-1$
		{
			cSymbolChoice.setText( fmtPattern.getSymbol( ) );
		}
		if ( !StringUtil.isBlank( fmtPattern.getSymPos( ) ) ) //$NON-NLS-1$
		{
			cSymPosChoice.setText( fmtPattern.getSymPos( ) );
			cSymPosChoice.setEnabled( true );
		}
		if ( fmtPattern.getUseBracket( ) )
		{
			cNegNumChoice.select( 1 );
		}
		else
		{
			cNegNumChoice.select( 0 );
		}
	}

	private void refreshFixedSetting( FormatFixedNumPattern fmtPattern )
	{
		fPlacesChoice.setText( String.valueOf( fmtPattern.getDecPlaces( ) ) );
		fUseSep.setSelection( fmtPattern.getUseSep( ) );
		fUseZero.setSelection( fmtPattern.getUseZero( ) );
		if ( fmtPattern.getUseBracket( ) )
		{
			fNegNumChoice.select( 1 );
		}
		else
		{
			fNegNumChoice.select( 0 );
		}
	}

	private void refreshPercentSetting( FormatPercentNumPattern fmtPattern )
	{
		pPlacesChoice.setText( String.valueOf( fmtPattern.getDecPlaces( ) ) );
		pUseSep.setSelection( fmtPattern.getUseSep( ) );
		pUseZero.setSelection( fmtPattern.getUseZero( ) );
		pSymPosChoice.setText( fmtPattern.getSymPos( ) );
		if ( fmtPattern.getUseBracket( ) )
		{
			pNegNumChoice.select( 1 );
		}
		else
		{
			pNegNumChoice.select( 0 );
		}
	}

	private void refreshScientificSetting( FormatScientificNumPattern fmtPattern )
	{
		sPlacesChoice.setText( String.valueOf( fmtPattern.getDecPlaces( ) ) );
	}

	private void refreshCustomSetting( FormatCustomNumPattern fmtPattern )
	{
		formatCodeBox.setText( fmtPattern.getPattern( ) == null ? "" //$NON-NLS-1$
				: fmtPattern.getPattern( ) );
	}

	private void setFmtPatternFromControls( )
	{
		if ( categoryPatternMaps == null )
		{
			return;
		}
		String category = getCategory4DisplayName( typeChoicer.getText( ) );

		if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) )
		{
			FormatCurrencyNumPattern pattern = (FormatCurrencyNumPattern) categoryPatternMaps.get( category );
			String places = cPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
			pattern.setUseSep( cUseSep.getSelection( ) );
			pattern.setSymbol( cSymbolChoice.getText( ) );
			pattern.setSymPos( cSymPosChoice.getText( ) );
			pattern.setUseBracket( cNegNumChoice.getSelectionIndex( ) == 1 );
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			FormatFixedNumPattern pattern = (FormatFixedNumPattern) categoryPatternMaps.get( category );
			String places = fPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
			pattern.setUseSep( fUseSep.getSelection( ) );
			pattern.setUseZero( fUseZero.getSelection( ) );
			pattern.setUseBracket( fNegNumChoice.getSelectionIndex( ) == 1 );
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			FormatPercentNumPattern pattern = (FormatPercentNumPattern) categoryPatternMaps.get( category );
			String places = pPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
			pattern.setUseSep( pUseSep.getSelection( ) );
			pattern.setUseZero( pUseZero.getSelection( ) );
			pattern.setSymPos( pSymPosChoice.getText( ) );
			pattern.setUseBracket( pNegNumChoice.getSelectionIndex( ) == 1 );
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			FormatScientificNumPattern pattern = (FormatScientificNumPattern) categoryPatternMaps.get( category );
			String places = sPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) )
		{
			FormatCustomNumPattern pattern = (FormatCustomNumPattern) categoryPatternMaps.get( category );
			pattern.setPattern( formatCodeBox.getText( ) );
		}
		return;
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

			gPreviewLabel = createGeneralPreviewPart4Page( generalPage );
		}
		return generalPage;
	}

	/**
	 * Lazily creates the current page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The current page.
	 */

	private Composite getCurrencyPage( Composite parent )
	{
		if ( currencyPage == null )
		{
			currencyPage = new Composite( parent, SWT.NULL );
			currencyPage.setLayout( createGridLayout4Page( ) );

			createCurrencySettingPart( currencyPage );
			cPreviewLabel = createGeneralPreviewPart4Page( currencyPage );
		}
		return currencyPage;
	}

	/**
	 * Lazily creates the fixed page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The fixed page.
	 */

	private Composite getFixedPage( Composite parent )
	{
		if ( fixedPage == null )
		{
			fixedPage = new Composite( parent, SWT.NULL );
			fixedPage.setLayout( createGridLayout4Page( ) );

			createFixedSettingPart( fixedPage );
			fPreviewLabel = createGeneralPreviewPart4Page( fixedPage );

		}
		return fixedPage;
	}

	/**
	 * Lazily creates the percent page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The percent page.
	 */

	private Composite getPercentPage( Composite parent )
	{
		if ( percentPage == null )
		{
			percentPage = new Composite( parent, SWT.NULL );
			percentPage.setLayout( createGridLayout4Page( ) );

			createPercentSettingPart( percentPage );
			pPreviewLabel = createGeneralPreviewPart4Page( percentPage );

		}
		return percentPage;
	}

	/**
	 * Lazily creates the scientific page and returns it.
	 * 
	 * @param parent
	 *            Parent contains this page.
	 * @return The scientific page.
	 */

	private Composite getScientificPage( Composite parent )
	{
		if ( scientificPage == null )
		{
			scientificPage = new Composite( parent, SWT.NULL );
			scientificPage.setLayout( createGridLayout4Page( ) );

			createScientificSettingPart( scientificPage );
			sPreviewLabel = createGeneralPreviewPart4Page( scientificPage );
		}
		return scientificPage;
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
				formatCodeBox = new Text( container, SWT.SINGLE | SWT.BORDER );
				formatCodeBox.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				formatCodeBox.addModifyListener( myModifyListener );
			}

			createCustomPreviewPart4Page( customPage );
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
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			l.setLayoutData( data );
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
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			l.setLayoutData( data );

			Composite container = new Composite( customFormatCodePage, SWT.NONE );
			container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			container.setLayout( new GridLayout( 2, false ) );

			new Label( container, SWT.NULL ).setText( LABEL_FORMAT_CODE );
			formatCodeBox = new Text( container, SWT.SINGLE | SWT.BORDER );
			formatCodeBox.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			formatCodeBox.addModifyListener( myModifyListener );
		}
		return customFormatCodePage;
	}

	private void createCurrencySettingPart( Composite parent )
	{
		Group setting = new Group( parent, SWT.NONE );
		setting.setText( LABEL_CURRENCY_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = 0;
		layout.verticalSpacing = 1;
		setting.setLayout( layout );

		new Label( setting, SWT.NONE ).setText( LABEL_DECIMAL_PLACES );
		cPlacesChoice = new Combo( setting, SWT.BORDER
				| SWT.SINGLE
				| SWT.V_SCROLL );
		cPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 20;
		cPlacesChoice.setLayoutData( data );
		cPlacesChoice.addSelectionListener( mySelectionListener );
		cPlacesChoice.addModifyListener( myModifyListener );
		cPlacesChoice.select( 2 );

		cUseSep = new Button( setting, SWT.CHECK );
		cUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		data = new GridData( );
		data.horizontalSpan = 2;
		cUseSep.setLayoutData( data );
		cUseSep.addSelectionListener( mySelectionListener );

		new Label( setting, SWT.NONE ).setText( LABEL_CURRENCY_SYMBOL );
		cSymbolChoice = new Combo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		cSymbolChoice.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_FILL ) );
		cSymbolChoice.setItems( symbols );
		cSymbolChoice.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( cSymbolChoice.getSelectionIndex( ) == 0 )
				{
					cSymPosChoice.deselectAll( );
					cSymPosChoice.setEnabled( false );
				}
				else
				{
					if ( !cSymPosChoice.isEnabled( ) )
					{
						cSymPosChoice.setEnabled( true );
						cSymPosChoice.select( 1 );
					}
				}
				updatePreview( );
			}
		} );
		cSymbolChoice.select( 0 );

		new Label( setting, SWT.NONE ).setText( LABEL_SYMBOL_POSITION );
		cSymPosChoice = new Combo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		cSymPosChoice.setItems( new String[]{
				SYMBOL_POSITION_AFTER, SYMBOL_POSITION_BEFORE
		} );
		cSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cSymPosChoice.addSelectionListener( mySelectionListener );
		cSymPosChoice.setEnabled( false );

		Label label = new Label( setting, SWT.NONE );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		label.setText( LABEL_NEGATIVE_NUMBERS );

		cNegNumChoice = new List( setting, SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL );
		cNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
		cNegNumChoice.add( "(1234.56)" ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_BOTH );
		cNegNumChoice.setLayoutData( data );
		cNegNumChoice.addSelectionListener( mySelectionListener );
		cNegNumChoice.select( 0 );
	}

	private void createFixedSettingPart( Composite parent )
	{
		Group setting = new Group( parent, SWT.NONE );
		setting.setText( LABEL_FIXED_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = 0;
		layout.verticalSpacing = 1;
		setting.setLayout( layout );

		Label label = new Label( setting, SWT.NONE );
		label.setText( LABEL_DECIMAL_PLACES );
		fPlacesChoice = new Combo( setting, SWT.BORDER
				| SWT.SINGLE
				| SWT.V_SCROLL );
		fPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 20;
		fPlacesChoice.setLayoutData( data );
		fPlacesChoice.addSelectionListener( mySelectionListener );
		fPlacesChoice.addModifyListener( myModifyListener );
		fPlacesChoice.select( 2 );

		fUseSep = new Button( setting, SWT.CHECK );
		fUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		GridData gData = new GridData( );
		gData.horizontalSpan = 2;
		fUseSep.setLayoutData( gData );
		fUseSep.addSelectionListener( mySelectionListener );

		fUseZero = new Button( setting, SWT.CHECK );
		fUseZero.setText( LABEL_USE_LEADING_ZERO );
		gData = new GridData( );
		gData.horizontalSpan = 2;
		fUseZero.setLayoutData( gData );
		fUseZero.addSelectionListener( mySelectionListener );

		label = new Label( setting, SWT.NONE );
		label.setText( LABEL_NEGATIVE_NUMBERS );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		fNegNumChoice = new List( setting, SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL );
		fNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
		fNegNumChoice.add( "(1234.56)" ); //$NON-NLS-1$
		gData = new GridData( GridData.FILL_BOTH );
		fNegNumChoice.setLayoutData( gData );
		fNegNumChoice.addSelectionListener( mySelectionListener );
		fNegNumChoice.select( 0 );
	}

	private void createPercentSettingPart( Composite percent )
	{
		Group setting = new Group( percent, SWT.NONE );
		setting.setText( LABEL_PERCENT_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = 0;
		layout.verticalSpacing = 1;
		setting.setLayout( layout );

		new Label( setting, SWT.NONE ).setText( LABEL_DECIMAL_PLACES );
		pPlacesChoice = new Combo( setting, SWT.BORDER
				| SWT.SINGLE
				| SWT.V_SCROLL );
		pPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		pPlacesChoice.setLayoutData( data );
		pPlacesChoice.addSelectionListener( mySelectionListener );
		pPlacesChoice.addModifyListener( myModifyListener );
		pPlacesChoice.select( 2 );

		pUseSep = new Button( setting, SWT.CHECK );
		pUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		GridData gData = new GridData( );
		gData.horizontalSpan = 2;
		pUseSep.setLayoutData( gData );
		pUseSep.addSelectionListener( mySelectionListener );

		pUseZero = new Button( setting, SWT.CHECK );
		pUseZero.setText( LABEL_USE_LEADING_ZERO );
		gData = new GridData( );
		gData.horizontalSpan = 2;
		pUseZero.setLayoutData( gData );
		pUseZero.addSelectionListener( mySelectionListener );

		new Label( setting, SWT.NONE ).setText( LABEL_SYMBOL_POSITION );
		pSymPosChoice = new Combo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		pSymPosChoice.setItems( new String[]{
				SYMBOL_POSITION_AFTER, SYMBOL_POSITION_BEFORE
		} );
		pSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		pSymPosChoice.addSelectionListener( mySelectionListener );
		pSymPosChoice.select( 0 );

		Label label = new Label( setting, SWT.NONE );
		label.setText( LABEL_NEGATIVE_NUMBERS );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		pNegNumChoice = new List( setting, SWT.SINGLE
				| SWT.BORDER
				| SWT.V_SCROLL );
		pNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
		pNegNumChoice.add( "(1234.56)" ); //$NON-NLS-1$
		pNegNumChoice.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		pNegNumChoice.addSelectionListener( mySelectionListener );
		pNegNumChoice.select( 0 );
	}

	private void createScientificSettingPart( Composite percent )
	{
		Group group = new Group( percent, SWT.NONE );
		group.setText( LABEL_SCIENTIFIC_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 2, false ) );

		Label label = new Label( group, SWT.NONE );
		label.setText( LABEL_DECIMAL_PLACES );
		sPlacesChoice = new Combo( group, SWT.BORDER
				| SWT.SINGLE
				| SWT.V_SCROLL );
		sPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 60;
		sPlacesChoice.setLayoutData( data );
		sPlacesChoice.addSelectionListener( mySelectionListener );
		sPlacesChoice.addModifyListener( myModifyListener );
		sPlacesChoice.select( 2 );
	}

	private void createCustomSettingsPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( LABEL_CUSTOM_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 1, false ) );

		Label label = new Label( group, SWT.NONE );
		label.setText( LABEL_CUSTOM_SETTINGS_LABEL );
		label.setLayoutData( new GridData( ) );

		createTable( group );
	}

	private void createCustomPreviewPart4Page( Composite parent )
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

		new Label( group, SWT.NONE ).setText( LABEL_PREVIEW_NUMBER );

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
		label.setText( LABEL_COSTOM_PREVIEW_LABEL );
		label.setLayoutData( new GridData( ) );

		cusPreviewLabel = new Label( group, SWT.CENTER
				| SWT.HORIZONTAL
				| SWT.VIRTUAL );
		cusPreviewLabel.setLayoutData( new GridData( GridData.FILL_BOTH ) );
	}

	private Label createGeneralPreviewPart4Page( Composite parent )
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
		previewText.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		return previewText;
	}

	/**
	 * Creates the table in custom page.
	 * 
	 * @param parent
	 *            Parent contains the table.
	 */

	private void createTable( Composite group )
	{
		table = new Table( group, SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION
				| SWT.BORDER );
		table.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE );
		tableColumValue.setWidth( 120 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT );
		tableColumnDisplay.setWidth( 120 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ),
				new NumberFormatter( getPatternForCategory( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ),
				new NumberFormatter( getPatternForCategory( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ),
				new NumberFormatter( getPatternForCategory( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				getDisplayName4Category( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ),
				new NumberFormatter( getPatternForCategory( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );

				String category = ChoiceSetFactory.getStructPropValue( NumberFormatValue.FORMAT_VALUE_STRUCT,
						NumberFormatValue.CATEGORY_MEMBER,
						displayName );

				String pattern = getPatternForCategory( category );

				formatCodeBox.setText( pattern );

				updatePreview( );
			}
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

	private void setControlsEnabled( boolean b )
	{
		typeChoicer.setEnabled( b );

		cPlacesChoice.setEnabled( b );
		cUseSep.setEnabled( b );
		cSymbolChoice.setEnabled( b );
		cSymPosChoice.setEnabled( b );
		if ( b )
		{
			if ( cSymbolChoice.getSelectionIndex( ) == 0 )
			{
				cSymPosChoice.setEnabled( false );
			}
		}
		cNegNumChoice.setEnabled( b );

		fPlacesChoice.setEnabled( b );
		fUseSep.setEnabled( b );
		fUseZero.setEnabled( b );
		fNegNumChoice.setEnabled( b );

		pPlacesChoice.setEnabled( b );
		pUseSep.setEnabled( b );
		pUseZero.setEnabled( b );
		pSymPosChoice.setEnabled( b );
		pNegNumChoice.setEnabled( b );

		sPlacesChoice.setEnabled( b );

		formatCodeBox.setEnabled( b );
		previewTextBox.setEnabled( b );
		table.setEnabled( b );
	}
}