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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FormatNumberDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FormatCurrencyNumPattern;
import org.eclipse.birt.report.designer.util.FormatCustomNumPattern;
import org.eclipse.birt.report.designer.util.FormatFixedNumPattern;
import org.eclipse.birt.report.designer.util.FormatNumberPattern;
import org.eclipse.birt.report.designer.util.FormatPercentNumPattern;
import org.eclipse.birt.report.designer.util.FormatScientificNumPattern;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.Currency;

/**
 * Format number page for formatting numbers.
 */

public class FormatNumberDescriptor extends PropertyDescriptor implements
		IFormatPage
{

	private static final String PREVIEW_TEXT_INVALID_NUMBER_TO_PREVIEW = Messages.getString( "FormatNumberPage.preview.invalidNumber" ); //$NON-NLS-1$
	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages.getString( "FormatNumberPage.preview.invalidFormatCode" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT_NUMBER_PAGE = Messages.getString( "FormatNumberPage.label.format.number.page" ); //$NON-NLS-1$
	private static final String LABEL_CURRENCY_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.currency.settings" ); //$NON-NLS-1$
	private static final String LABEL_CURRENCY_SYMBOL = Messages.getString( "FormatNumberPage.label.symbol" ); //$NON-NLS-1$
	private static final String LABEL_FIXED_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.fixed.settings" ); //$NON-NLS-1$
	private static final String LABEL_PERCENT_SETTINGS_GROUP = Messages.getString( "FormatNumberPage.label.percent.settings" ); //$NON-NLS-1$
	private static final String LABEL_USE_1000S_SEPARATOR = Messages.getString( "FormatNumberPage.label.use1000sSeparator" ); //$NON-NLS-1$
	// private static final String LABEL_USE_LEADING_ZERO = Messages.getString(
	// "FormatNumberPage.label.useLeadingZero" ); //$NON-NLS-1$
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

	private String pattern = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;
	private HashMap categoryPatternMaps;

	private static final int FORMAT_TYPE_INDEX = 0;
	private static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private static final String DEFAULT_PREVIEW_TEXT = "1234.56";
	private static final String DEFAULT_LOCALE_TEXT = NumberFormat.getNumberInstance( Locale.getDefault( ) )
			.format( 1234.56 );
	private static final double DEFAULT_PREVIEW_NUMBER = Double.parseDouble( DEFAULT_PREVIEW_TEXT );

	private static String[] symbols = new String[0];
	static
	{
		java.util.List list = new ArrayList( );
		list.add( Messages.getString( "FormatNumberPage.currency.symbol.none" ) );
		list.add( "\u00A5" );
		list.add( "$" );
		list.add( "\u20ac" );
		list.add( "\u00A3" );
		list.add( "\u20A9" );
		String localSymbol = Currency.getInstance( Locale.getDefault( ) )
				.getSymbol( );
		if ( !list.contains( localSymbol ) )
			list.add( 1, localSymbol );
		symbols = (String[]) list.toArray( new String[0] );
	}

	private CCombo typeChoicer;

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

	private CCombo cPlacesChoice, cSymbolChoice, cSymPosChoice, fPlacesChoice,
			pSymPosChoice, pPlacesChoice, sPlacesChoice;

	private Button cUseSep, pUseSep, fUseSep; // fUseZero, pUseZero;
	private List cNegNumChoice, fNegNumChoice, pNegNumChoice;
	private Table table;

	private boolean hasLoaded = false;
	private boolean isDirty = false;

	private String previewText = null;

	/**
	 * Listener, or <code>null</code> if none
	 */

	private int pageAlignment;

	private SelectionListener mySelectionListener = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			updatePreview( );
			notifyFormatChange( );
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
	private FocusListener myFocusListener = new FocusListener( ) {

		public void focusLost( FocusEvent e )
		{
			notifyFormatChange( );
		}

		public void focusGained( FocusEvent e )
		{
		}
	};
	private Composite content;

	/**
	 * Constructs a page for formatting numbers, default aligns the page
	 * virtically.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 */
	public FormatNumberDescriptor( )
	{
		this( PAGE_ALIGN_VIRTICAL, true );
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
	public FormatNumberDescriptor( int pageAlignment, boolean isFormStyle )
	{
		this.pageAlignment = pageAlignment;
		setFormStyle( isFormStyle );
	}

	/**
	 * Creates the contents of the page.
	 * 
	 */

	public Control createControl( Composite parent )
	{
		content = new Composite( parent, SWT.NONE );
		provider.initChoiceArray( );
		provider.getFormatTypes( );

		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			createContentsHorizontally( );
		}
		else
		{
			createContentsVirtically( );
		}
		return content;
	}

	public Control getControl( )
	{
		return content;
	}

	protected void createContentsVirtically( )
	{
		content.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );

		Composite topContainer = new Composite( content, SWT.NONE );
		topContainer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		topContainer.setLayout( new GridLayout( 2, false ) );
		FormWidgetFactory.getInstance( ).createLabel( topContainer,
				isFormStyle( ) ).setText( LABEL_FORMAT_NUMBER_PAGE );
		if ( !isFormStyle( ) )
			typeChoicer = new CCombo( topContainer, SWT.READ_ONLY );
		else
			typeChoicer = FormWidgetFactory.getInstance( )
					.createCCombo( topContainer, true );
		typeChoicer.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				reLayoutSubPages( );

				updatePreview( );
				notifyFormatChange( );
			}
		} );
		typeChoicer.setItems( provider.getFormatTypes( ) );

		infoComp = new Composite( content, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );

		createCategoryPages( infoComp );

		createCategoryPatterns( );

		setInput( null, null );
		setPreviewText( DEFAULT_PREVIEW_TEXT );
	}

	protected void createContentsHorizontally( )
	{
		content.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		// create format type choicer
		Composite container = new Composite( content, SWT.NONE );
		GridData data = new GridData( );
		data.widthHint = DEFAULT_CATEGORY_CONTAINER_WIDTH;
		container.setLayoutData( data );
		container.setLayout( new GridLayout( 1, false ) );

		FormWidgetFactory.getInstance( )
				.createLabel( container, isFormStyle( ) )
				.setText( LABEL_FORMAT_NUMBER_PAGE );
		if ( !isFormStyle( ) )
			typeChoicer = new CCombo( container, SWT.READ_ONLY );
		else
			typeChoicer = FormWidgetFactory.getInstance( )
					.createCCombo( container, true );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				reLayoutSubPages( );

				updatePreview( );
				notifyFormatChange( );
			}
		} );
		typeChoicer.setItems( provider.getFormatTypes( ) );

		// create the right part setting pane
		infoComp = new Composite( content, SWT.NONE );
		data = new GridData( GridData.FILL_BOTH );
		data.verticalSpan = 2;
		infoComp.setLayoutData( data );
		infoComp.setLayout( new StackLayout( ) );

		createCategoryPages( infoComp );

		// create left bottom part format code pane
		formatCodeComp = new Composite( content, SWT.NONE );
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

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_UNFORMATTED,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_CURRENCY,
				getCurrencyPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_FIXED,
				getFixedPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_PERCENT,
				getPercentPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				getScientificPage( parent ) );

		categoryPageMaps.put( provider.NUMBER_FORMAT_TYPE_CUSTOM,
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

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_GENERAL_NUMBER,
				new FormatNumberPattern( ) );

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_CURRENCY,
				new FormatCurrencyNumPattern( provider.NUMBER_FORMAT_TYPE_CURRENCY ) );

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_FIXED,
				new FormatFixedNumPattern( provider.NUMBER_FORMAT_TYPE_FIXED ) );

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_PERCENT,
				new FormatPercentNumPattern( provider.NUMBER_FORMAT_TYPE_PERCENT ) );

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC,
				new FormatScientificNumPattern( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC ) );

		categoryPatternMaps.put( provider.NUMBER_FORMAT_TYPE_CUSTOM,
				new FormatCustomNumPattern( provider.NUMBER_FORMAT_TYPE_CUSTOM ) );
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */

	/**
	 * Gets the format types for display names.
	 */

	/**
	 * Gets the index of given category.
	 */

	private void notifyFormatChange( )
	{
		if ( hasLoaded )
		{
			provider.fireFormatChanged( getCategory( ), getPattern( ) );
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
		provider.addFormatChangeListener( listener );
	}

	/**
	 * Sets input of the page.
	 * 
	 * @param formatString
	 *            The input format string.
	 * @author Liu sanyong: -----> for parameter dialog use.
	 */

	public void setInput( Object object )
	{
		super.setInput( object );
		getDescriptorProvider( ).setInput( object );
	}

	public void load( )
	{
		setEnabled( true );
		String[] result = (String[]) provider.load( );
		if ( result == null )
			setEnabled( false );
		else if ( result.length == 1 )
			setInput( result[0] );
		else if ( result.length == 2 )
			setInput( result[0], result[1] );
	}

	public void setInput( String formatString )
	{
		if ( formatString == null )
		{
			setInput( null, null );
			return;
		}
		String fmtStr = formatString;
		int pos = fmtStr.indexOf( ":" ); //$NON-NLS-1$
		if ( provider.isBlank( fmtStr ) )
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
			return provider.NUMBER_FORMAT_TYPE_UNFORMATTED;
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
		content.setEnabled( enabled );
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
				|| provider.isBlank( defText )
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
		{// avoid setting pattern from controls when the typechoicer is
			// selected
			// before loading.
			setFmtPatternFromControls( );
		}

		String category = provider.getCategory4DisplayName( typeChoicer.getText( ) );
		if ( provider.NUMBER_FORMAT_TYPE_UNFORMATTED.equals( category ) )
		{
			setCategory( provider.NUMBER_FORMAT_TYPE_UNFORMATTED );
			setPattern( null );
		}
		else
		{
			FormatNumberPattern fmtPattern = getFmtPattern4Category( category );

			setCategory( fmtPattern.getCategory( ) );
			setPattern( fmtPattern.getPattern( ) );
		}
		doPreview( getCategory( ), getPattern( ) );
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
			fmtStr = new NumberFormatter( patternStr ).format( num );
			gPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_UNFORMATTED ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			gPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_GENERAL_NUMBER ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			gPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_CURRENCY ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			cPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			fPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			pPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			fmtStr = new NumberFormatter( patternStr ).format( num );
			sPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_CUSTOM ) )
		{
			if ( provider.isBlank( previewTextBox.getText( ) ) )
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

	public FormatNumberPattern getFmtPattern4Category( String category )
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

	public Control getFmtPage4Category( String category )
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

	private void initiatePageLayout( String categoryStr, String patternStr )
	{
		if ( categoryStr == null )
		{
			typeChoicer.select( 0 );
			return;
		}

		FormatNumberPattern fmtPattern = getFmtPattern4Category( categoryStr );
		fmtPattern.setPattern( patternStr );

		if ( provider.NUMBER_FORMAT_TYPE_GENERAL_NUMBER.equals( categoryStr ) )
		{
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
		else if ( provider.NUMBER_FORMAT_TYPE_CURRENCY.equals( categoryStr ) )
		{
			refreshCurrencySetting( (FormatCurrencyNumPattern) fmtPattern );
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
		else if ( provider.NUMBER_FORMAT_TYPE_FIXED.equals( categoryStr ) )
		{
			refreshFixedSetting( (FormatFixedNumPattern) fmtPattern );
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
		else if ( provider.NUMBER_FORMAT_TYPE_PERCENT.equals( categoryStr ) )
		{
			refreshPercentSetting( (FormatPercentNumPattern) fmtPattern );
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
		else if ( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC.equals( categoryStr ) )
		{
			refreshScientificSetting( (FormatScientificNumPattern) fmtPattern );
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
		else if ( provider.NUMBER_FORMAT_TYPE_CUSTOM.equals( categoryStr ) )
		{
			refreshCustomSetting( (FormatCustomNumPattern) fmtPattern );
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
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
		String category = provider.getCategory4DisplayName( typeChoicer.getText( ) );

		Control control = getFmtPage4Category( category );

		( (StackLayout) infoComp.getLayout( ) ).topControl = control;

		infoComp.layout( );

		if ( formatCodeComp != null )
		{
			if ( category.equals( provider.NUMBER_FORMAT_TYPE_CUSTOM ) )
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
		if ( !provider.isBlank( fmtPattern.getSymbol( ) ) ) //$NON-NLS-1$
		{
			cSymbolChoice.setText( fmtPattern.getSymbol( ) );
		}
		if ( !provider.isBlank( fmtPattern.getSymPos( ) ) ) //$NON-NLS-1$
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
		// fUseZero.setSelection( fmtPattern.getUseZero( ) );
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
		// pUseZero.setSelection( fmtPattern.getUseZero( ) );
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
		String category = provider.getCategory4DisplayName( typeChoicer.getText( ) );

		if ( category.equals( provider.NUMBER_FORMAT_TYPE_CURRENCY ) )
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
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			FormatFixedNumPattern pattern = (FormatFixedNumPattern) categoryPatternMaps.get( category );
			String places = fPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
			pattern.setUseSep( fUseSep.getSelection( ) );
			// pattern.setUseZero( fUseZero.getSelection( ) );
			pattern.setUseBracket( fNegNumChoice.getSelectionIndex( ) == 1 );
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			FormatPercentNumPattern pattern = (FormatPercentNumPattern) categoryPatternMaps.get( category );
			String places = pPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
			pattern.setUseSep( pUseSep.getSelection( ) );
			// pattern.setUseZero( pUseZero.getSelection( ) );
			pattern.setSymPos( pSymPosChoice.getText( ) );
			pattern.setUseBracket( pNegNumChoice.getSelectionIndex( ) == 1 );
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			FormatScientificNumPattern pattern = (FormatScientificNumPattern) categoryPatternMaps.get( category );
			String places = sPlacesChoice.getText( );
			pattern.setDecPlaces( DEUtil.isValidInteger( places ) ? Integer.parseInt( places )
					: 0 );
		}
		else if ( category.equals( provider.NUMBER_FORMAT_TYPE_CUSTOM ) )
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

				FormWidgetFactory.getInstance( ).createLabel( container,
						isFormStyle( ) ).setText( LABEL_FORMAT_CODE );
				if ( isFormStyle( ) )
					formatCodeBox = FormWidgetFactory.getInstance( )
							.createText( container, "", SWT.SINGLE );
				else
					formatCodeBox = new Text( container, SWT.SINGLE
							| SWT.BORDER );
				formatCodeBox.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
				formatCodeBox.addModifyListener( myModifyListener );
				formatCodeBox.addFocusListener( myFocusListener );
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

			Label l = FormWidgetFactory.getInstance( )
					.createLabel( generalFormatCodePage,
							SWT.SEPARATOR | SWT.HORIZONTAL,
							isFormStyle( ) );
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

			Label l = FormWidgetFactory.getInstance( )
					.createLabel( customFormatCodePage,
							SWT.SEPARATOR | SWT.HORIZONTAL,
							isFormStyle( ) );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			l.setLayoutData( data );

			Composite container = new Composite( customFormatCodePage, SWT.NONE );
			container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			container.setLayout( new GridLayout( 2, false ) );

			FormWidgetFactory.getInstance( ).createLabel( container,
					isFormStyle( ) ).setText( LABEL_FORMAT_CODE );
			if ( isFormStyle( ) )
				formatCodeBox = FormWidgetFactory.getInstance( )
						.createText( container, "", SWT.SINGLE );
			else
				formatCodeBox = new Text( container, SWT.SINGLE | SWT.BORDER );
			formatCodeBox.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			formatCodeBox.addModifyListener( myModifyListener );
			formatCodeBox.addFocusListener( myFocusListener );
		}
		return customFormatCodePage;
	}

	private void createCurrencySettingPart( Composite parent )
	{
		Group setting;
		if ( !isFormStyle( ) )
			setting = new Group( parent, SWT.NONE );
		else
			setting = FormWidgetFactory.getInstance( ).createGroup( parent, "" );
		setting.setText( LABEL_CURRENCY_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		if ( isFormStyle( ) )
		{
			layout.marginHeight = 3;
			layout.verticalSpacing = 4;
		}
		else
		{
			layout.marginHeight = 0;
			layout.verticalSpacing = 1;
		}
		setting.setLayout( layout );

		FormWidgetFactory.getInstance( )
				.createLabel( setting, isFormStyle( ) )
				.setText( LABEL_DECIMAL_PLACES );
		if ( !isFormStyle( ) )
			cPlacesChoice = new CCombo( setting, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
		else
			cPlacesChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, false );
		cPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		cPlacesChoice.setLayoutData( data );
		cPlacesChoice.addSelectionListener( mySelectionListener );
		cPlacesChoice.addModifyListener( myModifyListener );
		cPlacesChoice.addFocusListener( myFocusListener );
		cPlacesChoice.select( 2 );

		cUseSep = FormWidgetFactory.getInstance( ).createButton( setting,
				SWT.CHECK,
				isFormStyle( ) );
		cUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		data = new GridData( );
		data.horizontalSpan = 2;
		cUseSep.setLayoutData( data );
		cUseSep.addSelectionListener( mySelectionListener );

		FormWidgetFactory.getInstance( )
				.createLabel( setting, isFormStyle( ) )
				.setText( LABEL_CURRENCY_SYMBOL );
		if ( !isFormStyle( ) )
			cSymbolChoice = new CCombo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		else
			cSymbolChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, true );

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
				notifyFormatChange( );
			}
		} );
		cSymbolChoice.select( 0 );

		FormWidgetFactory.getInstance( )
				.createLabel( setting, isFormStyle( ) )
				.setText( LABEL_SYMBOL_POSITION );
		if ( !isFormStyle( ) )
			cSymPosChoice = new CCombo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		else
			cSymPosChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, true );

		cSymPosChoice.setItems( new String[]{
				FormatNumberPattern.SYMBOL_POSITION_AFTER,
				FormatNumberPattern.SYMBOL_POSITION_BEFORE
		} );
		cSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		cSymPosChoice.addSelectionListener( mySelectionListener );
		cSymPosChoice.setEnabled( false );

		Label label = FormWidgetFactory.getInstance( ).createLabel( setting,
				isFormStyle( ) );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		label.setText( LABEL_NEGATIVE_NUMBERS );

		if ( !isFormStyle( ) )
			cNegNumChoice = new List( setting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
		else
			cNegNumChoice = FormWidgetFactory.getInstance( )
					.createList( setting, SWT.SINGLE | SWT.V_SCROLL );
		cNegNumChoice.add( "-" + DEFAULT_LOCALE_TEXT + "" ); //$NON-NLS-1$
		cNegNumChoice.add( "(" + DEFAULT_LOCALE_TEXT + ")" ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_BOTH );
		cNegNumChoice.setLayoutData( data );
		cNegNumChoice.addSelectionListener( mySelectionListener );
		cNegNumChoice.select( 0 );
	}

	private void createFixedSettingPart( Composite parent )
	{
		Group setting;
		if ( !isFormStyle( ) )
			setting = new Group( parent, SWT.NONE );
		else
			setting = FormWidgetFactory.getInstance( ).createGroup( parent, "" );
		setting.setText( LABEL_FIXED_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		if ( isFormStyle( ) )
		{
			layout.marginHeight = 3;
			layout.verticalSpacing = 4;
		}
		else
		{
			layout.marginHeight = 0;
			layout.verticalSpacing = 1;
		}
		setting.setLayout( layout );

		Label label = FormWidgetFactory.getInstance( ).createLabel( setting,
				isFormStyle( ) );
		label.setText( LABEL_DECIMAL_PLACES );
		if ( !isFormStyle( ) )
			fPlacesChoice = new CCombo( setting, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
		else
			fPlacesChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, false );
		fPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		fPlacesChoice.setLayoutData( data );
		fPlacesChoice.addSelectionListener( mySelectionListener );
		fPlacesChoice.addModifyListener( myModifyListener );
		fPlacesChoice.addFocusListener( myFocusListener );
		fPlacesChoice.select( 2 );

		fUseSep = FormWidgetFactory.getInstance( ).createButton( setting,
				SWT.CHECK,
				isFormStyle( ) );
		fUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		GridData gData = new GridData( );
		gData.horizontalSpan = 2;
		fUseSep.setLayoutData( gData );
		fUseSep.addSelectionListener( mySelectionListener );

		// fUseZero = new Button( setting, SWT.CHECK );
		// fUseZero.setText( LABEL_USE_LEADING_ZERO );
		// gData = new GridData( );
		// gData.horizontalSpan = 2;
		// fUseZero.setLayoutData( gData );
		// fUseZero.addSelectionListener( mySelectionListener );

		label = FormWidgetFactory.getInstance( ).createLabel( setting,
				isFormStyle( ) );
		label.setText( LABEL_NEGATIVE_NUMBERS );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		if ( !isFormStyle( ) )
			fNegNumChoice = new List( setting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
		else
			fNegNumChoice = FormWidgetFactory.getInstance( )
					.createList( setting, SWT.SINGLE | SWT.V_SCROLL );

		fNegNumChoice.add( "-" + DEFAULT_LOCALE_TEXT + "" ); //$NON-NLS-1$
		fNegNumChoice.add( "(" + DEFAULT_LOCALE_TEXT + ")" ); //$NON-NLS-1$
		gData = new GridData( GridData.FILL_BOTH );
		fNegNumChoice.setLayoutData( gData );
		fNegNumChoice.addSelectionListener( mySelectionListener );
		fNegNumChoice.select( 0 );
	}

	private void createPercentSettingPart( Composite percent )
	{
		Group setting;
		if ( !isFormStyle( ) )
			setting = new Group( percent, SWT.NONE );
		else
			setting = FormWidgetFactory.getInstance( )
					.createGroup( percent, "" );
		setting.setText( LABEL_PERCENT_SETTINGS_GROUP );
		setting.setLayoutData( createGridData4Part( ) );
		GridLayout layout = new GridLayout( 2, false );
		if ( isFormStyle( ) )
		{
			layout.marginHeight = 3;
			layout.verticalSpacing = 4;
		}
		else
		{
			layout.marginHeight = 0;
			layout.verticalSpacing = 1;
		}
		setting.setLayout( layout );

		FormWidgetFactory.getInstance( )
				.createLabel( setting, isFormStyle( ) )
				.setText( LABEL_DECIMAL_PLACES );
		if ( !isFormStyle( ) )
			pPlacesChoice = new CCombo( setting, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
		else
			pPlacesChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, false );

		pPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		pPlacesChoice.setLayoutData( data );
		pPlacesChoice.addSelectionListener( mySelectionListener );
		pPlacesChoice.addModifyListener( myModifyListener );
		pPlacesChoice.addFocusListener( myFocusListener );
		pPlacesChoice.select( 2 );

		pUseSep = FormWidgetFactory.getInstance( ).createButton( setting,
				SWT.CHECK,
				isFormStyle( ) );
		pUseSep.setText( LABEL_USE_1000S_SEPARATOR );
		GridData gData = new GridData( );
		gData.horizontalSpan = 2;
		pUseSep.setLayoutData( gData );
		pUseSep.addSelectionListener( mySelectionListener );

		// pUseZero = new Button( setting, SWT.CHECK );
		// pUseZero.setText( LABEL_USE_LEADING_ZERO );
		// gData = new GridData( );
		// gData.horizontalSpan = 2;
		// pUseZero.setLayoutData( gData );
		// pUseZero.addSelectionListener( mySelectionListener );

		FormWidgetFactory.getInstance( )
				.createLabel( setting, isFormStyle( ) )
				.setText( LABEL_SYMBOL_POSITION );
		if ( !isFormStyle( ) )
			pSymPosChoice = new CCombo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
		else
			pSymPosChoice = FormWidgetFactory.getInstance( )
					.createCCombo( setting, true );
		pSymPosChoice.setItems( new String[]{
				FormatNumberPattern.SYMBOL_POSITION_AFTER,
				FormatNumberPattern.SYMBOL_POSITION_BEFORE
		} );
		pSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		pSymPosChoice.addSelectionListener( mySelectionListener );
		pSymPosChoice.select( 0 );

		Label label = FormWidgetFactory.getInstance( ).createLabel( setting,
				isFormStyle( ) );
		label.setText( LABEL_NEGATIVE_NUMBERS );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		if ( !isFormStyle( ) )
			pNegNumChoice = new List( setting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
		else
			pNegNumChoice = FormWidgetFactory.getInstance( )
					.createList( setting, SWT.SINGLE | SWT.V_SCROLL );
		pNegNumChoice.add( "-" + DEFAULT_LOCALE_TEXT ); //$NON-NLS-1$
		pNegNumChoice.add( "(" + DEFAULT_LOCALE_TEXT + ")" ); //$NON-NLS-1$
		pNegNumChoice.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		pNegNumChoice.addSelectionListener( mySelectionListener );
		pNegNumChoice.select( 0 );
	}

	private void createScientificSettingPart( Composite percent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( percent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( percent, "" );
		group.setText( LABEL_SCIENTIFIC_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 2, false ) );

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_DECIMAL_PLACES );
		if ( !isFormStyle( ) )
			sPlacesChoice = new CCombo( group, SWT.BORDER | SWT.V_SCROLL );
		else
			sPlacesChoice = FormWidgetFactory.getInstance( )
					.createCCombo( group, false );

		sPlacesChoice.setItems( new String[]{
				"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
		} );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.widthHint = 60;
		sPlacesChoice.setLayoutData( data );
		sPlacesChoice.addSelectionListener( mySelectionListener );
		sPlacesChoice.addModifyListener( myModifyListener );
		pPlacesChoice.addFocusListener( myFocusListener );
		sPlacesChoice.select( 2 );
	}

	private void createCustomSettingsPart( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" );
		group.setText( LABEL_CUSTOM_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 1, false ) );

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_CUSTOM_SETTINGS_LABEL );
		label.setLayoutData( new GridData( ) );

		createTable( group );
	}

	private void createCustomPreviewPart4Page( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" );
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

		FormWidgetFactory.getInstance( )
				.createLabel( group, isFormStyle( ) )
				.setText( LABEL_PREVIEW_NUMBER );

		if ( !isFormStyle( ) )
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
		else
			previewTextBox = FormWidgetFactory.getInstance( )
					.createText( group, "", SWT.SINGLE );

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

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_COSTOM_PREVIEW_LABEL );
		label.setLayoutData( new GridData( ) );

		cusPreviewLabel = FormWidgetFactory.getInstance( ).createLabel( group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VIRTUAL,
				isFormStyle( ) );
		cusPreviewLabel.setLayoutData( new GridData( GridData.FILL_BOTH ) );
	}

	private Label createGeneralPreviewPart4Page( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" );
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

		Label previewText = FormWidgetFactory.getInstance( )
				.createLabel( group,
						SWT.CENTER | SWT.HORIZONTAL | SWT.VERTICAL,
						isFormStyle( ) );
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
		if ( !isFormStyle( ) )
			table = new Table( group, SWT.FULL_SELECTION
					| SWT.HIDE_SELECTION
					| SWT.BORDER );
		else
			table = FormWidgetFactory.getInstance( ).createTable( group,
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION );
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
				provider.getDisplayName4Category( provider.NUMBER_FORMAT_TYPE_CURRENCY ),
				new NumberFormatter( provider.getPatternForCategory( provider.NUMBER_FORMAT_TYPE_CURRENCY ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.NUMBER_FORMAT_TYPE_FIXED ),
				new NumberFormatter( provider.getPatternForCategory( provider.NUMBER_FORMAT_TYPE_FIXED ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.NUMBER_FORMAT_TYPE_PERCENT ),
				new NumberFormatter( provider.getPatternForCategory( provider.NUMBER_FORMAT_TYPE_PERCENT ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC ),
				new NumberFormatter( provider.getPatternForCategory( provider.NUMBER_FORMAT_TYPE_SCIENTIFIC ) ).format( DEFAULT_PREVIEW_NUMBER )
		} );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );

				String pattern = provider.getPattern( displayName );

				formatCodeBox.setText( pattern );

				updatePreview( );
				notifyFormatChange( );
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
		// fUseZero.setEnabled( b );
		fNegNumChoice.setEnabled( b );

		pPlacesChoice.setEnabled( b );
		pUseSep.setEnabled( b );
		// pUseZero.setEnabled( b );
		pSymPosChoice.setEnabled( b );
		pNegNumChoice.setEnabled( b );

		sPlacesChoice.setEnabled( b );

		formatCodeBox.setEnabled( b );
		previewTextBox.setEnabled( b );
		table.setEnabled( b );
	}

	public void save( Object obj ) throws SemanticException
	{
		provider.save( obj );
	}

	private FormatNumberDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof FormatNumberDescriptorProvider )
			this.provider = (FormatNumberDescriptorProvider) provider;
	}
}