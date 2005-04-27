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

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.FormatHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
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

	private String patternStr = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;
	private HashMap categoryPatternMaps;

	private static String[][] choiceArray = null;
	private static String[] formatTypes = null;

	private static final int DEFAULT_WITH = 250;
	private static final int FORMAT_TYPE_INDEX = 0;

	private static String[] symbols = {
			// "none", "£¤","$", "?", "¡ê"
			Messages.getString( "FormatNumberPreferencePage.symbol.none" ), "\uffe5", "$", "\u20ac", "\uffe1" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	};

	public static String SYMBOL_POSITION_AFTER = Messages.getString( "FormatNumberPage.symblePos.after" ); //$NON-NLS-1$
	public static String SYMBOL_POSITION_BEFORE = Messages.getString( "FormatNumberPage.symblePos.before" ); //$NON-NLS-1$

	private Combo typeChoicer;

	private Composite infoComp;
	private Composite generalPage;
	private Composite currencyPage;
	private Composite fixedPage;
	private Composite percentPage;
	private Composite scientificPage;
	private Composite customPage;

	private Text previewTextBox;
	private Text formatCode;

	private Label gPreviewLabel, cPreviewLabel, fPreviewLabel, pPreviewLabel,
			sPreviewLabel, cusPreviewLabel;

	private Combo cPlacesChoice, cSymbolChoice, cSymPosChoice, fPlacesChoice,
			pSymPosChoice, pPlacesChoice, sPlacesChoice;

	private Button cUseSep, fUseSep, fUseZero, pUseSep, pUseZero;

	private List cNegNumChoice, fNegNumChoice, pNegNumChoice;

	private boolean hasLoaded = false;

	private int sourceType = SOURCE_TYPE_STYLE;

	private static String DEFAULT_PREVIEW_TEXT = Messages.getString( "FormatNumberPage.default.preview.text" ); //$NON-NLS-1$

	private String previewText = null;

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
	 * Constructs a page for formatting numbers.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 * @param sourceType
	 *            The container's type
	 */
	public FormatNumberPage( Composite parent, int style, int sourceType )
	{
		super( parent, style );
		this.sourceType = sourceType;
		createContent( );
	}

	/**
	 * Creates the contents of the page.
	 *  
	 */
	protected void createContent( )
	{
		setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		initChoiceArray( );
		getFormatTypes( );

		Composite topContainer = new Composite( this, SWT.NONE );
		GridData data = new GridData( );
		if ( sourceType != SOURCE_TYPE_STYLE )
		{
			data = new GridData( GridData.FILL_HORIZONTAL );
		}
		topContainer.setLayoutData( data );
		topContainer.setLayout( new GridLayout( 2, false ) );
		new Label( topContainer, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.formatNumber.label" ) ); //$NON-NLS-1$
		typeChoicer = new Combo( topContainer, SWT.READ_ONLY );
		data = new GridData( GridData.FILL_HORIZONTAL );
		typeChoicer.setLayoutData( data );
		typeChoicer.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = typeChoicer.getText( );
				String category = getCategoryForDisplayName( displayName );

				Control control = (Control) categoryPageMaps.get( category );
				( (StackLayout) infoComp.getLayout( ) ).topControl = control;
				infoComp.layout( );
				updatePreview( );
			}
		} );
		typeChoicer.setItems( getFormatTypes( ) );

		infoComp = new Composite( this, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );
		createCategoryPanes( infoComp );
	}

	/**
	 * Creates info panes for each format type choicer, adds them into paneMap
	 * for after getting.
	 * 
	 * @param parent
	 *            Parent contains these info panes.
	 */
	private HashMap createCategoryPanes( Composite parent )
	{
		if ( categoryPageMaps == null )
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
		if ( categoryPatternMaps == null )
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
		return categoryPageMaps;
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */
	protected String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
					Style.NUMBER_FORMAT_PROP );
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
	private String getCategoryForDisplayName( String displayName )
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
	 * Sets input of the page.
	 * 
	 * @param input
	 *            The input formatHandle.
	 */
	public void setInput( FormatHandle input )
	{
		setInput( input.getCategory( ), input.getPattern( ) );
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
		{// only contains category, copy the category to pattern.-----> for
			// parameter dialog use.
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
	public void setInput( String category, String patternStr )
	{
		hasLoaded = false;
		oldCategory = category;
		oldPattern = patternStr;

		if ( oldCategory == null )
		{
			typeChoicer.select( 0 );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
			FormatCurrencyNumPattern pattern = ( (FormatCurrencyNumPattern) categoryPatternMaps.get( oldCategory ) );
			pattern.setPattern( oldPattern );
			refreshCurrencySetting( pattern );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
			FormatFixedNumPattern pattern = ( (FormatFixedNumPattern) categoryPatternMaps.get( oldCategory ) );
			pattern.setPattern( oldPattern );
			refreshFixedSetting( pattern );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
			FormatPercentNumPattern pattern = ( (FormatPercentNumPattern) categoryPatternMaps.get( oldCategory ) );
			pattern.setPattern( oldPattern );
			refreshPercentSetting( pattern );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
			FormatScientificNumPattern pattern = ( (FormatScientificNumPattern) categoryPatternMaps.get( oldCategory ) );
			pattern.setPattern( oldPattern );
			refreshScientificSetting( pattern );
		}
		else if ( oldCategory.equalsIgnoreCase( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) )
		{
			typeChoicer.select( getIndexOfCategory( oldCategory ) );
			FormatCustomNumPattern pattern = ( (FormatCustomNumPattern) categoryPatternMaps.get( oldCategory ) );
			pattern.setPattern( oldPattern );
			refreshCustomSetting( pattern );
		}
		else
		{
			// default for illegal input category.
			typeChoicer.select( 0 );
		}
		( (StackLayout) infoComp.getLayout( ) ).topControl = (Control) categoryPageMaps.get( getCategoryForDisplayName( typeChoicer.getText( ) ) );
		infoComp.layout( );
		hasLoaded = true;
		updatePreview( );
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage#setPreviewText(java.lang.String)
	 */
	public void setPreviewText( String text )
	{
		if ( text == null
				|| StringUtil.isBlank( text )
				|| !DEUtil.isValidNumber( text ) )
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
	 * Gets the previewText.
	 * 
	 * @return Returns the previewText.
	 */
	public String getPreviewText( )
	{
		return previewText;
	}

	/**
	 * Returns the patternStr from the page.
	 */
	public String getPatternStr( )
	{
		return patternStr;
	}

	/**
	 * Returns the category resulted from the page.
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
	 * Determines the format string is modified or not from the page.
	 * 
	 * @return true if the format string is modified.
	 */
	public boolean isFormatStrModified( )
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

	private FormatNumberPattern getFmtPattern( )
	{
		String displayName = typeChoicer.getText( );
		String category = getCategoryForDisplayName( displayName );

		FormatNumberPattern pattern = null;
		if ( categoryPatternMaps != null )
		{
			pattern = (FormatNumberPattern) categoryPatternMaps.get( category );
		}
		if ( pattern == null )
		{// avoid null when categoryPatternMas is not initialized or the
			// key(category) is not contained in the map.
			pattern = new FormatNumberPattern( );
		}
		return pattern;
	}

	/**
	 * Updates the format Pattern String, and Preview.
	 */
	private void updatePreview( )
	{
		String pattern = ""; //$NON-NLS-1$
		String fmtStr = ""; //$NON-NLS-1$
		if ( hasLoaded )
		{// avoid setting pattern from controls when the typechoicer is selected
			// before loading.
			setPatternFromControls( );
		}
		FormatNumberPattern fmtPattern = getFmtPattern( );

		setCategory( fmtPattern.getCategory( ) );
		setPatternStr( fmtPattern.getPattern( ) );

		pattern = fmtPattern.getPattern( );

		double num = Double.parseDouble( getPreviewText( ) == null ? DEFAULT_PREVIEW_TEXT
				: getPreviewText( ) );

		String displayName = typeChoicer.getText( );
		String category = getCategoryForDisplayName( displayName );

		if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER ) )
		{
			fmtStr = new NumberFormatter( pattern ).format( num );
			gPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) )
		{
			fmtStr = new NumberFormatter( pattern ).format( num );
			cPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) )
		{
			fmtStr = new NumberFormatter( pattern ).format( num );
			fPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) )
		{
			fmtStr = new NumberFormatter( pattern ).format( num );
			pPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
		{
			fmtStr = new NumberFormatter( pattern ).format( num );
			sPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
		else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM ) )
		{
			if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
			{
				fmtStr = new NumberFormatter( pattern ).format( num );
			}
			else if ( DEUtil.isValidNumber( previewTextBox.getText( ) ) )
			{
				fmtStr = new NumberFormatter( pattern ).format( Double.parseDouble( previewTextBox.getText( ) ) );
			}
			else
			{
				fmtStr = Messages.getString( "FormatNumberPreferencePage.previewLabel.invalidNumberToPreview" ); //$NON-NLS-1$
			}
			cusPreviewLabel.setText( validateFmtStr( fmtStr ) );
			return;
		}
	}

	private String validateFmtStr( String fmtStr )
	{
		String text = fmtStr;
		if ( text == null || text == "" ) //$NON-NLS-1$
		{
			text = Messages.getString( "FormatNumberPreferencePage.previewLabel.invalidFormatCode" ); //$NON-NLS-1$
		}
		return text;
	}

	/**
	 * Sets the pattern string for this preference.
	 * 
	 * @param patternStr
	 *            The patternStr to set.
	 */
	private void setPatternStr( String patternStr )
	{
		this.patternStr = patternStr == "" ? null : patternStr; //$NON-NLS-1$
	}

	/**
	 * @param category
	 *            The category to set.
	 */
	private void setCategory( String category )
	{
		this.category = category == "" ? null : category; //$NON-NLS-1$
	}

	private void refreshCurrencySetting( FormatCurrencyNumPattern pattern )
	{
		cPlacesChoice.setText( String.valueOf( pattern.getDecPlaces( ) ) );
		cUseSep.setSelection( pattern.getUseSep( ) );
		if ( pattern.getSymbol( ) != "" ) //$NON-NLS-1$
		{
			cSymbolChoice.setText( pattern.getSymbol( ) );
		}
		if ( pattern.getSymPos( ) != "" ) //$NON-NLS-1$
		{
			cSymPosChoice.setText( pattern.getSymPos( ) );
			cSymPosChoice.setEnabled( true );
		}
		if ( pattern.getUseBracket( ) )
		{
			cNegNumChoice.select( 1 );
		}
		else
		{
			cNegNumChoice.select( 0 );
		}
	}

	private void refreshFixedSetting( FormatFixedNumPattern pattern )
	{
		fPlacesChoice.setText( String.valueOf( pattern.getDecPlaces( ) ) );
		fUseSep.setSelection( pattern.getUseSep( ) );
		fUseZero.setSelection( pattern.getUseZero( ) );
		if ( pattern.getUseBracket( ) )
		{
			fNegNumChoice.select( 1 );
		}
		else
		{
			fNegNumChoice.select( 0 );
		}
	}

	private void refreshPercentSetting( FormatPercentNumPattern pattern )
	{
		pPlacesChoice.setText( String.valueOf( pattern.getDecPlaces( ) ) );
		pUseSep.setSelection( pattern.getUseSep( ) );
		pUseZero.setSelection( pattern.getUseZero( ) );
		pSymPosChoice.setText( pattern.getSymPos( ) );
		if ( pattern.getUseBracket( ) )
		{
			pNegNumChoice.select( 1 );
		}
		else
		{
			pNegNumChoice.select( 0 );
		}
	}

	private void refreshScientificSetting( FormatScientificNumPattern pattern )
	{
		sPlacesChoice.setText( String.valueOf( pattern.getDecPlaces( ) ) );
	}

	private void refreshCustomSetting( FormatCustomNumPattern pattern )
	{
		formatCode.setText( pattern.getFmtCode( ) );
	}

	private void setPatternFromControls( )
	{
		if ( categoryPatternMaps == null )
		{
			return;
		}
		String displayName = typeChoicer.getText( );
		String category = getCategoryForDisplayName( displayName );

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
			pattern.setPattern( formatCode.getText( ) );
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

			gPreviewLabel = createPreviewText( generalPage, DEFAULT_WITH );
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
			currencyPage.setLayout( new GridLayout( 1, false ) );

			Group cSetting = new Group( currencyPage, SWT.NONE );
			cSetting.setText( Messages.getString( "FormatNumberPreferencePage.currencySetting.groupLabel" ) ); //$NON-NLS-1$
			cSetting.setLayoutData( createGridData4Group( ) );
			cSetting.setLayout( new GridLayout( 2, false ) );

			new Label( cSetting, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.currencySetting.decimalPlaces" ) ); //$NON-NLS-1$
			cPlacesChoice = new Combo( cSetting, SWT.BORDER
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

			cUseSep = new Button( cSetting, SWT.CHECK );
			cUseSep.setText( Messages.getString( "FormatNumberPreferencePage.use1000sSeparator.label" ) ); //$NON-NLS-1$
			data = new GridData( );
			data.horizontalSpan = 2;
			cUseSep.setLayoutData( data );
			cUseSep.addSelectionListener( mySelectionListener );

			new Label( cSetting, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.symbol.label" ) ); //$NON-NLS-1$
			cSymbolChoice = new Combo( cSetting, SWT.DROP_DOWN | SWT.READ_ONLY );
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

			new Label( cSetting, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.symbolPosition.label" ) ); //$NON-NLS-1$
			cSymPosChoice = new Combo( cSetting, SWT.DROP_DOWN | SWT.READ_ONLY );
			cSymPosChoice.setItems( new String[]{
					SYMBOL_POSITION_AFTER, SYMBOL_POSITION_BEFORE
			} );
			cSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			cSymPosChoice.addSelectionListener( mySelectionListener );
			cSymPosChoice.setEnabled( false );

			Label label = new Label( cSetting, SWT.NONE );
			label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			label.setText( Messages.getString( "FormatNumberPreferencePage.negativeNum.label" ) ); //$NON-NLS-1$

			cNegNumChoice = new List( cSetting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
			cNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
			cNegNumChoice.add( "(-1234.56)" ); //$NON-NLS-1$
			data = new GridData( GridData.FILL_HORIZONTAL );
			cNegNumChoice.setLayoutData( data );
			cNegNumChoice.addSelectionListener( mySelectionListener );
			cNegNumChoice.select( 0 );

			cPreviewLabel = createPreviewText( currencyPage, DEFAULT_WITH );
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
			fixedPage.setLayout( new GridLayout( 1, false ) );

			Group setting = new Group( fixedPage, SWT.NONE );
			setting.setText( Messages.getString( "FormatNumberPreferencePage.fixedSettings.groupLabel" ) ); //$NON-NLS-1$
			GridLayout layout = new GridLayout( 2, false );
			setting.setLayout( layout );
			setting.setLayoutData( createGridData4Group( ) );

			Label label = new Label( setting, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.decimalPlaces.label" ) ); //$NON-NLS-1$
			fPlacesChoice = new Combo( setting, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
			fPlacesChoice.setItems( new String[]{
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
			} );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			fPlacesChoice.setLayoutData( data );
			fPlacesChoice.addSelectionListener( mySelectionListener );
			fPlacesChoice.addModifyListener( myModifyListener );
			fPlacesChoice.select( 2 );

			fUseSep = new Button( setting, SWT.CHECK );
			fUseSep.setText( Messages.getString( "FormatNumberPreferencePage.use1000sSeparator.label" ) ); //$NON-NLS-1$
			GridData gData = new GridData( );
			gData.horizontalSpan = 2;
			fUseSep.setLayoutData( gData );
			fUseSep.addSelectionListener( mySelectionListener );

			fUseZero = new Button( setting, SWT.CHECK );
			fUseZero.setText( Messages.getString( "FormatNumberPreferencePage.useLeadingZero.label" ) ); //$NON-NLS-1$
			gData = new GridData( );
			gData.horizontalSpan = 2;
			fUseZero.setLayoutData( gData );
			fUseZero.addSelectionListener( mySelectionListener );

			label = new Label( setting, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.negativeNum.label" ) ); //$NON-NLS-1$
			label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			fNegNumChoice = new List( setting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
			fNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
			fNegNumChoice.add( "(-1234.56)" ); //$NON-NLS-1$
			gData = new GridData( GridData.FILL_HORIZONTAL );
			fNegNumChoice.setLayoutData( gData );
			fNegNumChoice.addSelectionListener( mySelectionListener );
			fNegNumChoice.select( 0 );

			fPreviewLabel = createPreviewText( fixedPage, DEFAULT_WITH );

		}
		return fixedPage;
	}

	private GridData createGridData4Group( )
	{
		GridData data = new GridData( );
		if ( sourceType == SOURCE_TYPE_STYLE )
		{
			data = new GridData( );
			data.widthHint = DEFAULT_WITH;
		}
		else if ( sourceType == SOURCE_TYPE_PARAMETER )
		{
			data = new GridData( GridData.FILL_HORIZONTAL );
		}
		return data;
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
			percentPage.setLayout( new GridLayout( 1, false ) );

			Group setting = new Group( percentPage, SWT.NONE );
			setting.setText( Messages.getString( "FormatNumberPreferencePage.percentSettings.groupLabel" ) ); //$NON-NLS-1$
			setting.setLayoutData( createGridData4Group( ) );
			setting.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( setting, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.decimalPlaces.label" ) ); //$NON-NLS-1$
			pPlacesChoice = new Combo( setting, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
			pPlacesChoice.setItems( new String[]{
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
			} );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.heightHint = 20;
			pPlacesChoice.setLayoutData( data );
			pPlacesChoice.addSelectionListener( mySelectionListener );
			pPlacesChoice.addModifyListener( myModifyListener );
			pPlacesChoice.select( 2 );

			pUseSep = new Button( setting, SWT.CHECK );
			pUseSep.setText( Messages.getString( "FormatNumberPreferencePage.use1000sSeparator.label" ) ); //$NON-NLS-1$
			GridData gData = new GridData( );
			gData.horizontalSpan = 2;
			pUseSep.setLayoutData( gData );
			pUseSep.addSelectionListener( mySelectionListener );

			pUseZero = new Button( setting, SWT.CHECK );
			pUseZero.setText( Messages.getString( "FormatNumberPreferencePage.useLeadingZero.label" ) ); //$NON-NLS-1$
			gData = new GridData( );
			gData.horizontalSpan = 2;
			pUseZero.setLayoutData( gData );
			pUseZero.addSelectionListener( mySelectionListener );

			label = new Label( setting, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.symbolPosition.label" ) ); //$NON-NLS-1$
			pSymPosChoice = new Combo( setting, SWT.DROP_DOWN | SWT.READ_ONLY );
			pSymPosChoice.setItems( new String[]{
					SYMBOL_POSITION_AFTER, SYMBOL_POSITION_BEFORE
			} );
			pSymPosChoice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			pSymPosChoice.addSelectionListener( mySelectionListener );
			pSymPosChoice.select( 0 );

			label = new Label( setting, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.negativeNum.label" ) ); //$NON-NLS-1$
			label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
			pNegNumChoice = new List( setting, SWT.SINGLE
					| SWT.BORDER
					| SWT.V_SCROLL );
			pNegNumChoice.add( "-1234.56" ); //$NON-NLS-1$
			pNegNumChoice.add( "(-1234.56)" ); //$NON-NLS-1$
			gData = new GridData( GridData.FILL_HORIZONTAL );
			pNegNumChoice.setLayoutData( gData );
			pNegNumChoice.addSelectionListener( mySelectionListener );
			pNegNumChoice.select( 0 );

			pPreviewLabel = createPreviewText( percentPage, DEFAULT_WITH );

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
			scientificPage.setLayout( new GridLayout( 1, false ) );

			Group group = new Group( scientificPage, SWT.NONE );
			group.setText( Messages.getString( "FormatNumberPreferencePage.scientificSettings.groupLabel" ) ); //$NON-NLS-1$
			group.setLayoutData( createGridData4Group( ) );
			group.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( group, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.decimalPlaces.label" ) ); //$NON-NLS-1$
			sPlacesChoice = new Combo( group, SWT.BORDER
					| SWT.SINGLE
					| SWT.V_SCROLL );
			sPlacesChoice.setItems( new String[]{
					"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$
			} );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			sPlacesChoice.setLayoutData( data );
			sPlacesChoice.addSelectionListener( mySelectionListener );
			sPlacesChoice.addModifyListener( myModifyListener );
			sPlacesChoice.select( 2 );

			sPreviewLabel = createPreviewText( scientificPage, DEFAULT_WITH );

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
			customPage.setLayout( new GridLayout( 1, false ) );

			Group group = new Group( customPage, SWT.NONE );
			group.setText( Messages.getString( "FormatNumberPreferencePage.customSettings.groupLabel" ) ); //$NON-NLS-1$
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( group, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.exampleFormats.label" ) ); //$NON-NLS-1$
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			label = new Label( group, SWT.NONE );
			label.setText( Messages.getString( "FormatNumberPreferencePage.exampleFormats.label2" ) ); //$NON-NLS-1$
			data = new GridData( );
			data.horizontalSpan = 2;
			label.setLayoutData( data );

			createTable( group );

			Composite container = new Composite( customPage, SWT.NONE );
			data = new GridData( GridData.FILL_HORIZONTAL );
			container.setLayoutData( data );
			container.setLayout( new GridLayout( 2, false ) );

			new Label( container, SWT.NULL ).setText( Messages.getString( "FormatNumberPreferencePage.formatCode.label" ) ); //$NON-NLS-1$
			formatCode = new Text( container, SWT.SINGLE | SWT.BORDER );
			formatCode.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			formatCode.addModifyListener( myModifyListener );

			group = new Group( customPage, SWT.NONE );
			group.setText( Messages.getString( "FormatNumberPreferencePage.customPreviewGroup.label" ) ); //$NON-NLS-1$
			data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			new Label( group, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.numberToPreview.label" ) ); //$NON-NLS-1$
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
			previewTextBox.setText( DEFAULT_PREVIEW_TEXT ); //$NON-NLS-1$
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

			new Label( group, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.previewLabel.lable" ) ); //$NON-NLS-1$
			cusPreviewLabel = new Label( group, SWT.NONE );
			cusPreviewLabel.setText( "" ); //$NON-NLS-1$
			cusPreviewLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			Composite helpBar = new Composite( customPage, SWT.NONE );
			data = new GridData( GridData.FILL_HORIZONTAL );
			helpBar.setLayoutData( data );
			helpBar.setLayout( new GridLayout( 2, false ) );

			new Label( helpBar, SWT.NONE ).setText( Messages.getString( "FormatNumberPreferencePage.helpLabel.label" ) ); //$NON-NLS-1$
			Button help = new Button( helpBar, SWT.PUSH );
			help.setText( Messages.getString( "FormatNumberPreferencePage.helpButton.label" ) ); //$NON-NLS-1$
			help.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END
					| GridData.GRAB_HORIZONTAL ) );
		}
		return customPage;
	}

	/**
	 * Creates the table in custom page.
	 * 
	 * @param parent
	 *            Parent contains the table.
	 */
	private void createTable( Composite group )
	{
		Table table = new Table( group, SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION
				| SWT.BORDER );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		data.heightHint = 60;
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String category = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );
				String pattern = ""; //$NON-NLS-1$
				if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY ) )
				{
					pattern = "\u00A4###,##0.00"; //$NON-NLS-1$
				}
				else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED ) )
				{
					pattern = "#0.00"; //$NON-NLS-1$
				}
				else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT ) )
				{
					pattern = "0.00%"; //$NON-NLS-1$
				}
				else if ( category.equals( DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC ) )
				{
					pattern = "0.00E00"; //$NON-NLS-1$
				}
				formatCode.setText( pattern );
				updatePreview( );
			}
		} );
		table.select( 0 );

		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( Messages.getString( "FormatNumberPreferencePage.toolColumn.formatCode.label" ) ); //$NON-NLS-1$
		tableColumValue.setWidth( 120 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( Messages.getString( "FormatNumberPreferencePage.toolColumn.formatResult.label" ) ); //$NON-NLS-1$
		tableColumnDisplay.setWidth( 120 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatNumberPreferencePage.toolItem.formatCode.currency" ), "$4,567.89" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatNumberPreferencePage.toolItem.formatCode.fixed" ), "4567.89" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatNumberPreferencePage.toolItem.formatCode.percent" ), "4567.89%" //$NON-NLS-1$ //$NON-NLS-2$
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				Messages.getString( "FormatNumberPreferencePage.toolItem.formatCode.scientific" ), "4.57E+03" //$NON-NLS-1$ //$NON-NLS-2$
		} );
	}

	/**
	 * Creates preview part for page.
	 */
	private Label createPreviewText( Composite parent, int groupWidth )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( Messages.getString( "FormatNumberPreferencePage.previewGroup.label" ) ); //$NON-NLS-1$
		group.setLayoutData( createGridData4Group( ) );
		group.setLayout( new GridLayout( 1, false ) );

		Label previewText = new Label( group, SWT.NONE );
		previewText.setText( "123456" ); //$NON-NLS-1$
		previewText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		return previewText;
	}

}