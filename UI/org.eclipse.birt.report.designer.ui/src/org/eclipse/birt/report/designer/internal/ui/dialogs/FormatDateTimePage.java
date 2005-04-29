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
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.Style;
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
 * Format date time page for formatting date and time.
 */

public class FormatDateTimePage extends Composite implements IFormatPage
{

	private static final String LABEL_FORMAT_DATE_TIME_PAGE = Messages
			.getString( "FormatDateTimePage.label.format.page" ); //$NON-NLS-1$
	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages
			.getString( "FormatDateTimePage.label.general.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS = Messages
			.getString( "FormatDateTimePage.label.custom.settings" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_EXAMPLE_FORMATS = Messages
			.getString( "FormatDateTimePage.label.example.formats" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTING_LABEL = Messages
			.getString( "FormatDateTimePage.label.style.custom.setting.label" ); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages
			.getString( "FormatDateTimePage.label.format.code" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_GROUP = Messages
			.getString( "FormatDateTimePage.label.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_DATE_TIME = Messages
			.getString( "FormatDateTimePage.label.preview.dateTime" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_LABEL = Messages
			.getString( "FormatDateTimePage.label.preview.label" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages
			.getString( "FormatDateTimePage.label.table.column.format.code" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages
			.getString( "FormatDateTimePage.label.table.column.format.result" ); //$NON-NLS-1$

	private static final String PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW = Messages
			.getString( "FormatDateTimePage.preview.invalid.dateTime" ); //$NON-NLS-1$
	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages
			.getString( "FormatDateTimePage.preview.invalid.formatCode" ); //$NON-NLS-1$

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

	private Label generalPreviewLabel, cusPreviewLabel;
	private Label guideLabel;
	private Text previewTextBox;
	private Text formatCode;

	private boolean hasLoaded = false;

	private String previewText = null;

	private boolean isDirty = false;

	/**
	 * Constructs a page for formatting date time.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 * @param pageAlignment
	 *            Aligns the page virtically(PAGE_ALIGN_VIRTICAL) or
	 *            horizontally(PAGE_ALIGN_HORIZONTAL).
	 */
	public FormatDateTimePage( Composite parent, int style, int pageAlignment )
	{
		super( parent, style );
		createContents( );
	}

	/**
	 * Constructs a page for formatting date time, default aligns the page
	 * virtically.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 */
	public FormatDateTimePage( Composite parent, int style )
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
		new Label( topContainer, SWT.NONE )
				.setText( LABEL_FORMAT_DATE_TIME_PAGE );
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
		typeChoicer.select( 0 );

		infoComp = new Composite( this, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );
		createInfoPages( infoComp );
	}

	/**
	 * Re layouts sub pages according to the selected format type.
	 */
	protected void reLayoutSubPages( )
	{
		String category = getCategory4UIDisplayName( typeChoicer.getText( ) );

		Control control = (Control) categoryPageMaps.get( category );

		( (StackLayout) infoComp.getLayout( ) ).topControl = control;

		infoComp.layout( );
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
		{ // special case: only contains category, copy the category to
			// pattern.
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
			if ( categoryStr
					.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM ) )
			{
				formatCode.setText( patternStr == null ? "" : patternStr ); //$NON-NLS-1$
			}
			typeChoicer.select( getIndexOfCategory( categoryStr ) );
		}

		// re layout out sub pages.
		reLayoutSubPages( );

		// update preview.
		updatePreview( );

		// set initail.
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
	 * @param datetiem_format_type_general_date
	 */
	private void setCategory( String category )
	{
		this.category = category;
	}

	/**
	 * Returns the choiceArray of this choice element from model.
	 */
	protected String[][] initChoiceArray( )
	{
		if ( choiceArray == null )
		{
			IChoiceSet set = ChoiceSetFactory.getStructChoiceSet(
					DateTimeFormatValue.FORMAT_VALUE_STRUCT,
					DateTimeFormatValue.CATEGORY_MEMBER );
			IChoice[] choices = set.getChoices( );
			if ( choices.length > 0 )
			{
				// excludes "unformatted" category.
				choiceArray = new String[choices.length - 1][2];
				for ( int i = 0, j = 0; i < choices.length; i++ )
				{
					if ( !choices[i]
							.getName( )
							.equals(
									DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED ) )
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
				String fmtStr = ""; //$NON-NLS-1$
				String category = choiceArray[i][1];
				if ( category
						.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM ) )
				{
					fmtStr = category;
				}
				else
				{
					// uses UI specified display names.
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
	private int getIndexOfCategory( String category )
	{
		int index = 0;
		if ( initChoiceArray( ) != null )
		{
			for ( int i = 0; i < choiceArray.length; i++ )
			{
				if ( choiceArray[i][1].equals( category ) )
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
	private String getCategory4UIDisplayName( String displayName )
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
	 * Gets the corresponding internal display name given the category.
	 * 
	 * @param category
	 * @return
	 */
	private String getDisplayName4Category( String category )
	{
		return ChoiceSetFactory.getStructDisplayName(
				DateTimeFormatValue.FORMAT_VALUE_STRUCT,
				DateTimeFormatValue.CATEGORY_MEMBER, category );
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
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE
				.equals( category ) )
		{
			pattern = "General Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE
				.equals( category ) )
		{
			pattern = "Long Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE
				.equals( category ) )
		{
			pattern = "Medium Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE
				.equals( category ) )
		{
			pattern = "Short Date"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME
				.equals( category ) )
		{
			pattern = "Long Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME
				.equals( category ) )
		{
			pattern = "Medium Time"; //$NON-NLS-1$
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME
				.equals( category ) )
		{
			pattern = "Short Time"; //$NON-NLS-1$
		}
		else
		{
			// default, unformatted.
			pattern = ""; //$NON-NLS-1$
		}
		return pattern;
	}

	/**
	 * Creates info panes for each format type choicer, adds them into paneMap
	 * for after getting.
	 * 
	 * @param parent
	 *            Parent contains these info panes.
	 */
	private void createInfoPages( Composite parent )
	{
		categoryPageMaps = new HashMap( );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME,
				getGeneralPage( parent ) );
		categoryPageMaps.put(
				DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM,
				getCustomPage( parent ) );
	}

	/**
	 * Updates the format Pattern String, and Preview.
	 */
	private void updatePreview( )
	{
		markDirty( hasLoaded );

		String pattern = ""; //$NON-NLS-1$
		String fmtStr = ""; //$NON-NLS-1$

		String category = getCategory4UIDisplayName( typeChoicer.getText( ) );
		setCategory( category );

		Date sampleDateTime = new Date( );
		if ( getPreviewText( ) != null )
		{
			try
			{
				sampleDateTime = new Date( getPreviewText( ) );
			}
			catch ( Exception e )
			{
				//
			}
		}

		if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME
				.equals( category ) )
		{
			pattern = getPatternForCategory( category );
			fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
			return;
		}
		else if ( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM
				.equals( category ) )
		{
			pattern = formatCode.getText( );
			if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
			{
				fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			}
			else
			{
				String text = previewTextBox.getText( );
				try
				{
					fmtStr = new DateFormatter( pattern ).format( new Date(
							text ) );
				}
				catch ( Exception e )
				{
					fmtStr = PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW;
				}
			}

			cusPreviewLabel.setText( validatedFmtStr( fmtStr ) );
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
			group.setText( LABEL_CUSTOM_SETTINGS );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			group.setLayoutData( data );
			group.setLayout( new GridLayout( 2, false ) );

			Label label = new Label( group, SWT.NONE );
			label.setText( LABEL_CUSTOM_EXAMPLE_FORMATS );
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
			GridLayout layout = new GridLayout( 2, false );
			layout.verticalSpacing = 1;
			group.setLayout( layout );

			new Label( group, SWT.NONE ).setText( LABEL_PREVIEW_DATE_TIME );
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
			previewTextBox.setText( "" ); //$NON-NLS-1$
			previewTextBox.setLayoutData( new GridData(
					GridData.FILL_HORIZONTAL ) );
			previewTextBox.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					if ( hasLoaded )
					{
						setPreviewText( previewTextBox.getText( ) );
					}
					if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
					{
						guideLabel.setText( "" ); //$NON-NLS-1$
					}
					else
					{
						guideLabel
								.setText( Messages
										.getString( "FormatDateTimePage.label.guide.text" ) ); //$NON-NLS-1$
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

			new Label( group, SWT.NONE ).setText( LABEL_PREVIEW_LABEL );
			cusPreviewLabel = new Label( group, SWT.NONE );
			cusPreviewLabel.setText( "" ); //$NON-NLS-1$
			cusPreviewLabel.setLayoutData( new GridData(
					GridData.FILL_HORIZONTAL ) );
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
				| SWT.HIDE_SELECTION | SWT.BORDER );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 2;
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = ( (TableItem) e.item )
						.getText( FORMAT_TYPE_INDEX );
				String category = ChoiceSetFactory.getStructPropValue(
						DateTimeFormatValue.FORMAT_VALUE_STRUCT,
						DateTimeFormatValue.CATEGORY_MEMBER, displayName );
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
		tableColumnDisplay.setWidth( 200 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_GENERAL_DATE ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_DATE ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MUDIUM_DATE ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_DATE ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_LONG_TIME ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_MEDIUM_TIME ) )
								.format( new Date( ) )} );
		new TableItem( table, SWT.NONE )
				.setText( new String[]{
						getDisplayName4Category( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME ),
						new DateFormatter(
								getPatternForCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_SHORT_TIME ) )
								.format( new Date( ) )} );
	}
}