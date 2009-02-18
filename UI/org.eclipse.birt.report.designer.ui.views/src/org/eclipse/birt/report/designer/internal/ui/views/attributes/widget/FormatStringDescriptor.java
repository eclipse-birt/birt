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

import java.util.HashMap;

import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FormatStringDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.FormatStringPattern;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * Format string page for formatting a string.
 */

public class FormatStringDescriptor extends PropertyDescriptor implements
		IFormatPage
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

	private static final String SAMPLE_TEXT_ZIP_CODE = Messages.getString( "FormatStringPage.SimpleTextZipCode" ); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_ZIP_C0DE4 = Messages.getString( "FormatStringPage.SimpleTextZipCode4" ); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PHONE_NUMBER = Messages.getString( "FormatStringPage.PhoneNumber" ); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER = Messages.getString( "FormatStringPage.SocialSecurityNumber" ); //$NON-NLS-1$
	private static final String SAMPLE_TEXT_PRESERVE_SPACE = Messages.getString( "FormatStringPage.Preview.PreserveWhiteSpaces"); //$NON-NLS-1$ //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_TEXT = Messages.getString( "FormatStringPage.default.preview.text" ); //$NON-NLS-1$

	ULocale DEFAULT_LOCALE = ULocale.getDefault( );

	private String pattern = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;

	private static final int FORMAT_TYPE_INDEX = 0;
	private static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private CCombo typeChoicer;
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

	public FormatStringDescriptor( )
	{
		this( PAGE_ALIGN_VIRTICAL, true );
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

	public FormatStringDescriptor( int pageAlignment, boolean isFormStyle )
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
				isFormStyle( ) ).setText( LABEL_FORMAT_STRING_PAGE );
		if ( !isFormStyle( ) )
			typeChoicer = new CCombo( topContainer, SWT.READ_ONLY | SWT.BORDER );
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
				.setText( LABEL_FORMAT_STRING_PAGE );
		if ( !isFormStyle( ) )
			typeChoicer = new CCombo( container, SWT.READ_ONLY );
		else
			typeChoicer = FormWidgetFactory.getInstance( )
					.createCCombo( container, true );
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

		categoryPageMaps.put( provider.STRING_FORMAT_TYPE_UNFORMATTED,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.STRING_FORMAT_TYPE_UPPERCASE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.STRING_FORMAT_TYPE_LOWERCASE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.STRING_FORMAT_TYPE_CUSTOM,
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

	/**
	 * Gets the format types for display names.
	 */

	/**
	 * Gets the index of given category.
	 */

	/**
	 * Gets the corresponding internal display name given the category.
	 * 
	 * @param category
	 * @return
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
			return provider.STRING_FORMAT_TYPE_UNFORMATTED;
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
		content.setEnabled( enabled );
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
		if ( defText == null || provider.isBlank( defText ) )
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

		String category = provider.getCategory4DisplayName( typeChoicer.getText( ) );
		setCategory( category );

		if ( provider.STRING_FORMAT_TYPE_UNFORMATTED.equals( category ) )
		{
			String pattern = null;
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( null );
		}
		else if ( provider.STRING_FORMAT_TYPE_UPPERCASE.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_LOWERCASE.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( gText );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_ZIP_CODE.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_CODE );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_ZIP_CODE_4.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_C0DE4 );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_PHONE_NUMBER.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_PHONE_NUMBER );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER.equals( category ) )
		{
			String pattern = FormatStringPattern.getPatternForCategory( category );
			String fmtStr = new StringFormatter( pattern, DEFAULT_LOCALE ).format( SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else if ( provider.STRING_FORMAT_TYPE_CUSTOM.equals( category ) )
		{
			String pattern = formatCode.getText( );
			String fmtStr;
			if ( provider.isBlank( previewTextBox.getText( ) ) )
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
			if ( categoryStr.equals( provider.STRING_FORMAT_TYPE_CUSTOM ) )
			{
				formatCode.setText( patternStr == null ? "" : patternStr ); //$NON-NLS-1$
			}
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
	}

	private void reLayoutSubPages( )
	{
		String category = provider.getCategory4DisplayName( typeChoicer.getText( ) );

		Control control = (Control) categoryPageMaps.get( category );

		( (StackLayout) infoComp.getLayout( ) ).topControl = control;

		infoComp.layout( );

		if ( formatCodeComp != null )
		{
			if ( category.equals( provider.STRING_FORMAT_TYPE_CUSTOM ) )
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

				FormWidgetFactory.getInstance( ).createLabel( container,
						isFormStyle( ) ).setText( LABEL_FORMAT_CODE );
				if ( !isFormStyle( ) )
					formatCode = new Text( container, SWT.SINGLE | SWT.BORDER );
				else
					formatCode = FormWidgetFactory.getInstance( )
							.createText( container, "", SWT.SINGLE ); //$NON-NLS-1$
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
				formatCode.addFocusListener( new FocusListener( ) {

					public void focusLost( FocusEvent e )
					{
						notifyFormatChange( );
					}

					public void focusGained( FocusEvent e )
					{
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

			Label l = FormWidgetFactory.getInstance( )
					.createLabel( generalFormatCodePage,
							SWT.SEPARATOR | SWT.HORIZONTAL,
							isFormStyle( ) );
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

			Label l = FormWidgetFactory.getInstance( )
					.createLabel( customFormatCodePage,
							SWT.SEPARATOR | SWT.HORIZONTAL,
							isFormStyle( ) );
			l.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			Composite container = new Composite( customFormatCodePage, SWT.NONE );
			container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			container.setLayout( new GridLayout( 2, false ) );

			FormWidgetFactory.getInstance( ).createLabel( container,
					isFormStyle( ) ).setText( LABEL_FORMAT_CODE );
			if ( !isFormStyle( ) )
				formatCode = new Text( container, SWT.SINGLE | SWT.BORDER );
			else
				formatCode = FormWidgetFactory.getInstance( )
						.createText( container, "", SWT.SINGLE ); //$NON-NLS-1$
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
			formatCode.addFocusListener( new FocusListener( ) {

				public void focusLost( FocusEvent e )
				{
					notifyFormatChange( );
				}

				public void focusGained( FocusEvent e )
				{
				}
			} );
		}
		return customFormatCodePage;
	}

	private Label createGeneralPreviewPart( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" ); //$NON-NLS-1$
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
		previewText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		return previewText;
	}

	private void createCustomSettingsPart( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" ); //$NON-NLS-1$
		group.setText( LABEL_CUSTOM_SETTINGS_GROUP );
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 2, false ) );

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_CUSTOM_SETTING_TEXT );
		GridData data = new GridData( );
		data.horizontalSpan = 2;
		label.setLayoutData( data );

		createTable( group );
	}

	private void createCustomPreviewPart( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" ); //$NON-NLS-1$
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
				.setText( LABEL_CUSTOM_PREVIEW_STRING );
		if ( !isFormStyle( ) )
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
		else
			previewTextBox = FormWidgetFactory.getInstance( )
					.createText( group, "", SWT.SINGLE ); //$NON-NLS-1$
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
		label.setText( LABEL_CUSTOM_PREVIEW_LABEL );
		label.setLayoutData( new GridData( ) );

		cPreviewLabel = FormWidgetFactory.getInstance( ).createLabel( group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VIRTUAL,
				isFormStyle( ) );
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
		if ( !isFormStyle( ) )
			table = new Table( parent, SWT.FULL_SELECTION
					| SWT.HIDE_SELECTION
					| SWT.BORDER );
		else
			table = FormWidgetFactory.getInstance( ).createTable( parent,
					SWT.FULL_SELECTION | SWT.HIDE_SELECTION );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 2;
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String displayName = ( (TableItem) e.item ).getText( FORMAT_TYPE_INDEX );
				String category = null;
				if ( displayName.equals( FormatStringDescriptorProvider.PRESERVE_WHITE_SPACES ) )
					category = provider.STRING_FORMAT_TYPE_PRESERVE_SPACE;
				else
					category = ChoiceSetFactory.getStructPropValue( provider.FORMAT_VALUE_STRUCT,
							provider.CATEGORY_MEMBER,
							displayName );
				String pattern = FormatStringPattern.getPatternForCategory( category );
				formatCode.setText( pattern );

				updatePreview( );
				notifyFormatChange( );
			}
		} );
		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE );
		tableColumValue.setWidth( 150 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT );
		tableColumnDisplay.setWidth( 200 );
		tableColumnDisplay.setResizable( true );

		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_UPPERCASE ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_UPPERCASE ),
						DEFAULT_LOCALE ).format( DEFAULT_PREVIEW_TEXT )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_LOWERCASE ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_LOWERCASE ),
						DEFAULT_LOCALE ).format( DEFAULT_PREVIEW_TEXT )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_ZIP_CODE_4 ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_ZIP_CODE_4 ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_ZIP_C0DE4 )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_PHONE_NUMBER ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_PHONE_NUMBER ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_PHONE_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_SOCIAL_SECURITY_NUMBER )
		} );
		new TableItem( table, SWT.NONE ).setText( new String[]{
				provider.getDisplayName4Category( provider.STRING_FORMAT_TYPE_PRESERVE_SPACE ),
				new StringFormatter( FormatStringPattern.getPatternForCategory( provider.STRING_FORMAT_TYPE_PRESERVE_SPACE ),
						DEFAULT_LOCALE ).format( SAMPLE_TEXT_PRESERVE_SPACE )
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

	private FormatStringDescriptorProvider provider;

	private Composite content;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof FormatStringDescriptorProvider )
			this.provider = (FormatStringDescriptorProvider) provider;
	}

	public void load( )
	{
		setEnabled( true );
		String[] format = (String[]) provider.load( );
		if ( format == null )
		{
			setInput( null, null );
			setEnabled( false );
		}
		else
		{
			setInput( format[0], format[1] );
		}

	}

	public void save( Object obj ) throws SemanticException
	{
		provider.save( obj );
	}

	public void setInput( Object input )
	{
		super.setInput( input );
		getDescriptorProvider( ).setInput( input );
	}
}