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

import java.util.Date;
import java.util.HashMap;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FormatDataTimeDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.designer.util.FormatDateTimePattern;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
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
 * Format date time page for formatting date and time.
 */

public class FormatDateTimeDescriptor extends PropertyDescriptor implements
		IFormatPage
{

	private static final String LABEL_FORMAT_DATE_TIME_PAGE = Messages.getString( "FormatDateTimePage.label.format.page" ); //$NON-NLS-1$
	private static final String LABEL_GENERAL_PREVIEW_GROUP = Messages.getString( "FormatDateTimePage.label.general.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS = Messages.getString( "FormatDateTimePage.label.custom.settings" ); //$NON-NLS-1$
	private static final String LABEL_CUSTOM_SETTINGS_LABEL = Messages.getString( "FormatDateTimePage.label.custom.settings.label" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_DATETIME = Messages.getString( "FormatDateTimePage.label.preview.dateTime" ); //$NON-NLS-1$
	private static final String LABEL_FORMAT_CODE = Messages.getString( "FormatDateTimePage.label.format.code" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_GROUP = Messages.getString( "FormatDateTimePage.label.preview.group" ); //$NON-NLS-1$
	private static final String LABEL_PREVIEW_LABEL = Messages.getString( "FormatDateTimePage.label.preview.label" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_NAME = Messages.getString( "FormatDateTimePage.label.table.column.format.name" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT = Messages.getString( "FormatDateTimePage.label.table.column.format.result" ); //$NON-NLS-1$
	private static final String LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE = Messages.getString( "FormatDateTimePage.label.table.column.format.code" ); //$NON-NLS-1$

	private static final String ENTER_DATE_TIME_GUIDE_FORMAT = Messages.getString( "FormatDateTimePage.label.guide.format" ); //$NON-NLS-1$
	private static final String ENTER_DATE_TIME_GUIDE_TEXT = Messages.getString( "FormatDateTimePage.label.guide.text" ); //$NON-NLS-1$

	private static final String PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW = Messages.getString( "FormatDateTimePage.preview.invalid.dateTime" ); //$NON-NLS-1$
	private static final String PREVIEW_TEXT_INVALID_FORMAT_CODE = Messages.getString( "FormatDateTimePage.preview.invalid.formatCode" ); //$NON-NLS-1$

	private String pattern = null;
	private String category = null;
	private String oldCategory = null;
	private String oldPattern = null;

	private HashMap categoryPageMaps;

	private static final int FORMAT_CODE_INDEX = 2;
	private static final int DEFAULT_CATEGORY_CONTAINER_WIDTH = 220;

	private int pageAlignment;

	private CCombo typeChoicer;
	private Composite infoComp;
	private Composite formatCodeComp;

	private Composite generalPage;
	private Composite customPage;

	private Composite generalFormatCodePage;
	private Composite customFormatCodePage;

	private Label generalPreviewLabel, cusPreviewLabel;
	private Label guideLabel;
	private Text previewTextBox;
	private Text formatCode;

	private Table table;

	private boolean hasLoaded = false;

	private String previewText = null;

	private boolean isDirty = false;

	/**
	 * Listener, or <code>null</code> if none
	 */

	private Date defaultDate = new Date( );

	private String defaultDateTime = new DateFormatter( ENTER_DATE_TIME_GUIDE_FORMAT,
			ULocale.getDefault( ) ).format( defaultDate );
	private Composite content;

	/**
	 * Constructs a page for formatting date time, default aligns the page
	 * virtically.
	 * 
	 * @param parent
	 *            The container
	 * @param style
	 *            The style of the page
	 */

	public FormatDateTimeDescriptor( )
	{
		this( PAGE_ALIGN_VIRTICAL, true );
	}

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

	public FormatDateTimeDescriptor( int pageAlignment, boolean isFormStyle )
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
				isFormStyle( ) ).setText( LABEL_FORMAT_DATE_TIME_PAGE );
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
		typeChoicer.select( 0 );

		infoComp = new Composite( content, SWT.NONE );
		infoComp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		infoComp.setLayout( new StackLayout( ) );

		createCategoryPages( infoComp );

		setInput( null, null );
		setPreviewText( defaultDateTime );
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
				.setText( LABEL_FORMAT_DATE_TIME_PAGE ); //$NON-NLS-1$
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
		setPreviewText( defaultDateTime );
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

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_UNFORMATTED,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_GENERAL_DATE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_LONG_DATE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_MUDIUM_DATE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_SHORT_DATE,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_LONG_TIME,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_MEDIUM_TIME,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_SHORT_TIME,
				getGeneralPage( parent ) );

		categoryPageMaps.put( provider.DATETIEM_FORMAT_TYPE_CUSTOM,
				getCustomPage( parent ) );
	}

	private void createFormatCodePages( Composite parent )
	{
		getHorizonGeneralFormatCodePage( parent );

		getHorizonCustomFormatCodePage( parent );
	}

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

	public void save( Object obj ) throws SemanticException
	{
		provider.save( obj );
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

		// set initail.
		oldCategory = categoryStr;
		oldPattern = patternStr;

		hasLoaded = true;
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.dialogs.IFormatPage#
	 * setPreviewText(java.lang.String)
	 */

	public void setPreviewText( String text )
	{
		setDefaultPreviewText( text );
		updatePreview( );
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
			return provider.DATETIEM_FORMAT_TYPE_UNFORMATTED;
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
		setControlsEnabeld( enabled );
	}

	private void setDefaultPreviewText( String text )
	{
		if ( text == null || StringUtil.isBlank( text ) )
		{
			previewText = null;
		}
		else
		{
			previewText = text;
		}
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
	 * Sets the pattern string for this preference.
	 * 
	 * @param pattern
	 *            The pattern to set.
	 */

	private void setPattern( String pattern )
	{
		this.pattern = pattern;
	}

	/**
	 * @param datetiem_format_type_general_date
	 */

	private void setCategory( String category )
	{
		this.category = category;
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

		String category = provider.getCategory4UIDisplayName( typeChoicer.getText( ) );
		setCategory( category );

		boolean invalidPreviewText = false;
		Date sampleDateTime = defaultDate;
		if ( getPreviewText( ) != null
				&& !getPreviewText( ).equals( defaultDateTime ) )
		{
			try
			{
				sampleDateTime = new DateFormatter( ENTER_DATE_TIME_GUIDE_FORMAT,
						ULocale.getDefault( ) ).parse( getPreviewText( ) );
			}
			catch ( Exception e )
			{
				invalidPreviewText = true;
				// do nothing, leave sampleDate to be defaultDate.
			}
		}

		if ( provider.DATETIEM_FORMAT_TYPE_CUSTOM.equals( category ) )
		{
			String pattern = formatCode.getText( );
			String fmtStr;

			if ( invalidPreviewText )
			{
				fmtStr = PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW;
			}
			else
			{
				try
				{
					fmtStr = new DateFormatter( pattern, ULocale.getDefault( ) ).format( sampleDateTime );
				}
				catch ( Exception e )
				{
					fmtStr = PREVIEW_TEXT_INVALID_DATETIME_TO_PREVIEW;
				}
			}

			cusPreviewLabel.setText( validatedFmtStr( fmtStr ) );
			setPattern( pattern );
		}
		else
		{
			String pattern = null;
			if ( !provider.DATETIEM_FORMAT_TYPE_UNFORMATTED.equals( category ) )
			{
				pattern = FormatDateTimePattern.getPatternForCategory( category );
			}
			String fmtStr = new DateFormatter( pattern ).format( sampleDateTime );
			generalPreviewLabel.setText( validatedFmtStr( fmtStr ) );
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
			if ( categoryStr.equals( provider.DATETIEM_FORMAT_TYPE_CUSTOM ) )
			{
				formatCode.setText( patternStr == null ? "" : patternStr ); //$NON-NLS-1$
			}
			typeChoicer.select( provider.getIndexOfCategory( categoryStr ) );
		}
	}

	/**
	 * Re layouts sub pages according to the selected format type.
	 */

	private void reLayoutSubPages( )
	{
		String category = provider.getCategory4UIDisplayName( typeChoicer.getText( ) );

		Control control = (Control) categoryPageMaps.get( category );

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
					isFormStyle( ) ).setText( LABEL_FORMAT_CODE ); //$NON-NLS-1$
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

	/**
	 * Creates preview part for general page.
	 */

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

		Label previewLabel = FormWidgetFactory.getInstance( )
				.createLabel( group,
						SWT.CENTER | SWT.HORIZONTAL | SWT.VERTICAL,
						isFormStyle( ) );
		previewLabel.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		return previewLabel;
	}

	private void createCustomSettingsPart( Composite parent )
	{
		Group group;
		if ( !isFormStyle( ) )
			group = new Group( parent, SWT.NONE );
		else
			group = FormWidgetFactory.getInstance( ).createGroup( parent, "" ); //$NON-NLS-1$
		group.setText( LABEL_CUSTOM_SETTINGS ); //$NON-NLS-1$
		group.setLayoutData( createGridData4Part( ) );
		group.setLayout( new GridLayout( 2, false ) );

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_CUSTOM_SETTINGS_LABEL ); //$NON-NLS-1$
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
		group.setText( LABEL_PREVIEW_GROUP ); //$NON-NLS-1$
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
				.setText( LABEL_PREVIEW_DATETIME ); //$NON-NLS-1$
		if ( !isFormStyle( ) )
			previewTextBox = new Text( group, SWT.SINGLE | SWT.BORDER );
		else
			previewTextBox = FormWidgetFactory.getInstance( )
					.createText( group, "", SWT.SINGLE ); //$NON-NLS-1$
		previewTextBox.setText( defaultDateTime == null ? "" : defaultDateTime ); //$NON-NLS-1$
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
				if ( StringUtil.isBlank( previewTextBox.getText( ) ) )
				{
					guideLabel.setText( "" ); //$NON-NLS-1$
				}
				else
				{
					guideLabel.setText( ENTER_DATE_TIME_GUIDE_TEXT );
				}
			}
		} );

		if ( pageAlignment == PAGE_ALIGN_VIRTICAL )
		{
			FormWidgetFactory.getInstance( )
					.createLabel( group, isFormStyle( ) );
		}
		guideLabel = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		guideLabel.setText( "" ); //$NON-NLS-1$
		Font font = JFaceResources.getDialogFont( );
		FontData fData = font.getFontData( )[0];
		fData.setHeight( fData.getHeight( ) - 1 );
		guideLabel.setFont( FontManager.getFont( fData ) );

		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalIndent = 10;
		guideLabel.setLayoutData( data );

		Label label = FormWidgetFactory.getInstance( ).createLabel( group,
				isFormStyle( ) );
		label.setText( LABEL_PREVIEW_LABEL ); //$NON-NLS-1$
		label.setLayoutData( new GridData( ) );

		cusPreviewLabel = FormWidgetFactory.getInstance( ).createLabel( group,
				SWT.CENTER | SWT.HORIZONTAL | SWT.VIRTUAL,
				isFormStyle( ) );
		cusPreviewLabel.setText( "" ); //$NON-NLS-1$
		data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 1;
		cusPreviewLabel.setLayoutData( data );
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
		if ( pageAlignment == PAGE_ALIGN_HORIZONTAL )
		{
			data.widthHint = 240;
		}
		table.setLayoutData( data );

		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				formatCode.setText( ( (TableItem) e.item ).getText( FORMAT_CODE_INDEX ) );
				updatePreview( );
				notifyFormatChange( );
			}
		} );
		TableColumn tableColumValue = new TableColumn( table, SWT.NONE );
		tableColumValue.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_NAME );
		tableColumValue.setWidth( 120 );
		tableColumValue.setResizable( true );

		TableColumn tableColumnDisplay = new TableColumn( table, SWT.NONE );
		tableColumnDisplay.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_RESULT );
		tableColumnDisplay.setWidth( 115 );
		tableColumnDisplay.setResizable( true );

		TableColumn tableColumnFormatCode = new TableColumn( table, SWT.NONE );
		tableColumnFormatCode.setText( LABEL_TABLE_COLUMN_EXAMPLE_FORMAT_CODE );
		tableColumnFormatCode.setWidth( 150 );
		tableColumnFormatCode.setResizable( true );

		String[][] items = provider.getTableItems( );
		for ( int i = 0; i < items.length; i++ )
		{
			new TableItem( table, SWT.NONE ).setText( items[i] );
		}
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
		return;
	}

	private FormatDataTimeDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof FormatDataTimeDescriptorProvider )
			this.provider = (FormatDataTimeDescriptorProvider) provider;
	}

}