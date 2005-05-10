/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * Supplies template selection page of new report wizard
 *  
 */
public class WizardTemplateChoicePage extends WizardPage
{

	private static final String MESSAGE_DESCRIPTION = Messages.getString( "WizardTemplateChoicePage.label.Description" ); //$NON-NLS-1$
	private static final String MESSAGE_PREVIEW = Messages.getString( "WizardTemplateChoicePage.label.Preview" ); //$NON-NLS-1$
	private static final String MESSAGE_REPORT_TEMPLATES = Messages.getString( "WizardTemplateChoicePage.label.ReportTemplates" ); //$NON-NLS-1$
	private static final String MESSAGE_SHOW_CHEATSHEET = Messages.getString( "WizardTemplateChoicePage.label.ShowCheatSheets" ); //$NON-NLS-1$)
	private static final String TITLE_LETTER = Messages.getString( "WizardTemplateChoicePage.title.Letter" ); //$NON-NLS-1$
	private static final String TITLE_SIDE_BY_SIDE_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.title.SideBySideChartListing" ); //$NON-NLS-1$
	private static final String TITLE_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.title.ChartListing" ); //$NON-NLS-1$
	private static final String TITLE_GROUPED_LISTING = Messages.getString( "WizardTemplateChoicePage.title.GroupedListing" ); //$NON-NLS-1$
	private static final String TITLE_SIMPLE_LISTING = Messages.getString( "WizardTemplateChoicePage.title.SimpleListing" ); //$NON-NLS-1$
	private static final String TITLE_BLANK_REPORT = Messages.getString( "WizardTemplateChoicePage.title.BlankReport" ); //$NON-NLS-1$
	private static final String TITLE_DUAL_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.title.DualChartListing" ); //$NON-NLS-1$
	private static final String TITLE_DUAL_COLUMN_LISTING = Messages.getString( "WizardTemplateChoicePage.title.DualColumnListing" ); //$NON-NLS-1$
	private static final String TITLE_FIRST_REPORT = Messages.getString( "WizardTemplateChoicePage.title.FirstReport" ); //$NON-NLS-1$
	private static final String DESCRIPTION_LETTER = Messages.getString( "WizardTemplateChoicePage.message.Letter" ); //$NON-NLS-1$
	private static final String DESCRIPTION_SIDE_BY_SIDE_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.message.SideBySideChartListing" ); //$NON-NLS-1$
	private static final String DESCRIPTION_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.message.ChartListing" ); //$NON-NLS-1$
	private static final String DESCRIPTION_GROUPED_LISTING = Messages.getString( "WizardTemplateChoicePage.message.GroupedListing" ); //$NON-NLS-1$
	private static final String DESCRIPTION_SIMPLE_LISTING = Messages.getString( "WizardTemplateChoicePage.message.SimpleListing" ); //$NON-NLS-1$
	private static final String DESCRIPTION_BLANK_REPORT = Messages.getString( "WizardTemplateChoicePage.message.BlankReport" ); //$NON-NLS-1$
	private static final String DESCRIPTION_DUAL_CHART_LISTING = Messages.getString( "WizardTemplateChoicePage.message.DualChartListing" ); //$NON-NLS-1$
	private static final String DESCRIPTION_DUAL_COLUMN_LISTING = Messages.getString( "WizardTemplateChoicePage.message.DualColumnListing" ); //$NON-NLS-1$
    private static final String DESCRIPTION_FIRST_REPORT = Messages.getString( "WizardTemplateChoicePage.message.FirstReport" ); //$NON-NLS-1$
    
	private List templateList;

	private ImageCanvas previewCanvas;
	private Button chkBox;

	private Label description;


	protected class Template
	{

		public Template( String name, String description, String reportPath,
				String picturePath, String cheatSheetId )
		{
			this.name = name;
			this.description = description;
			this.reportPath = reportPath;
			this.picturePath = picturePath;
			this.cheatSheetId = cheatSheetId;
		}

		public String name;

		public String description;

		public String reportPath;

		public String picturePath;

		public String cheatSheetId;

	}

	protected Template[] templates = new Template[]{
			new Template( TITLE_BLANK_REPORT,
					DESCRIPTION_BLANK_REPORT,
					"/templates/blank_report.rptdesign", //$NON-NLS-1$
					"/templates/blank_report.gif", //$NON-NLS-1$
					"" ), //$NON-NLS-1$
			new Template( TITLE_FIRST_REPORT,
					DESCRIPTION_FIRST_REPORT,
					"/templates/blank_report.rptdesign", //$NON-NLS-1$
					"/templates/first_report.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.firstreport" ), //$NON-NLS-1$
			new Template( TITLE_SIMPLE_LISTING,
					DESCRIPTION_SIMPLE_LISTING,
					"/templates/simple_listing.rptdesign", //$NON-NLS-1$
					"/templates/simple_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.simplelisting" ), //$NON-NLS-1$
			new Template( TITLE_GROUPED_LISTING,
					DESCRIPTION_GROUPED_LISTING,
					"/templates/grouped_listing.rptdesign", //$NON-NLS-1$
					"/templates/grouped_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.groupedlisting" ), //$NON-NLS-1$
			new Template( TITLE_DUAL_COLUMN_LISTING,
					DESCRIPTION_DUAL_COLUMN_LISTING,
					"/templates/dual_column_listing.rptdesign", //$NON-NLS-1$
					"/templates/dual_column_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.dualcolumnlisting" ), //$NON-NLS-1$
			new Template( TITLE_CHART_LISTING,
					DESCRIPTION_CHART_LISTING,
					"/templates/chart_listing.rptdesign", //$NON-NLS-1$
					"/templates/chart_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.chartlisting" ), //$NON-NLS-1$
			new Template( TITLE_DUAL_CHART_LISTING,
					DESCRIPTION_DUAL_CHART_LISTING,
					"/templates/dual_column_chart_listing.rptdesign", //$NON-NLS-1$
					"/templates/dual_column_chart_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.dualchartlisting" ), //$NON-NLS-1$
			new Template( TITLE_SIDE_BY_SIDE_CHART_LISTING,
					DESCRIPTION_SIDE_BY_SIDE_CHART_LISTING,
					"/templates/sidebyside_chart_listing.rptdesign", //$NON-NLS-1$
					"/templates/sidebyside_chart_listing.gif", //$NON-NLS-1$
					"org.eclipse.birt.report.designer.ui.cheatsheet.sidebysidechartlisting" ), //$NON-NLS-1$
			/*new Template( TITLE_MAILING_LABELS,
					DESCRIPTION_MAILING_LABELS,
					"/templates/mailing_labels.rptdesign", //$NON-NLS-1$
					"/templates/mailing_labels.gif", //$NON-NLS-1$
					"" ), //$NON-NLS-1$*/
			new Template( TITLE_LETTER,
					DESCRIPTION_LETTER,
					"/templates/letter.rptdesign", //$NON-NLS-1$
					"/templates/letter.gif", //$NON-NLS-1$
					"" ) //$NON-NLS-1$
	};

	protected int selectedIndex;

	protected Map imageMap;

	private Composite previewPane;

	public class TemplateType
	{

		public static final int BLANK_REPORT = 0;

		public static final int SIMPLE_LISTING = 1;

		public static final int GROUPED_LISTING = 2;

		public static final int CHART_LISTING = 3;

		public static final int CROSSTAB = 4;

		public static final int MAILING_LABELS = 5;

		public static final int FREE_FORMAT = 6;

		public static final int GROUPED_LISTING_HEADING_OUTSIDE = 7;

		public static final int DUALCHART_LISTING = 8;

		public static final int LETTER = 9;

		public static final int SIDEBYSIDE_CHART_LISTING = 10;

		public static final int DUAL_COLUMN_CHART_LISTING = 11;

		public static final int DASHBOARD_REPORT = 12;

	}

	/**
	 * @param pageName
	 */
	protected WizardTemplateChoicePage( String pageName )
	{
		super( pageName );

		imageMap = new HashMap( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 10;
		gridLayout.marginWidth = 10;
		gridLayout.horizontalSpacing = 10;
		gridLayout.verticalSpacing = 10;
		composite.setLayout( gridLayout );

		Label label0 = new Label( composite, SWT.NONE );
		label0.setText( MESSAGE_REPORT_TEMPLATES );

		Label previewLabel = new Label( composite, SWT.NONE );
		previewLabel.setText( MESSAGE_PREVIEW );
		GridData data = new GridData( GridData.BEGINNING );
		previewLabel.setLayoutData( data );

		templateList = new List( composite, SWT.BORDER );
		for ( int i = 0; i < templates.length; i++ )
		{
			templateList.add( templates[i].name );
		}

		data = new GridData( GridData.BEGINNING | GridData.FILL_VERTICAL );
		data.widthHint = 170;
		templateList.setLayoutData( data );

		previewPane = new Composite( composite, 0 );
		data = new GridData( GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL );
		previewPane.setLayoutData( data );
		gridLayout = new GridLayout( );
		gridLayout.verticalSpacing = 10;
		previewPane.setLayout( gridLayout );

		previewCanvas = new ImageCanvas( previewPane, SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL );

		data = new GridData( GridData.FILL_VERTICAL | GridData.FILL_HORIZONTAL );
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.heightHint = 230;
		data.widthHint = 185;
		previewCanvas.setLayoutData( data );

		Label descriptionTitle = new Label( previewPane, SWT.NONE );
		descriptionTitle.setText( MESSAGE_DESCRIPTION );
		data = new GridData( GridData.FILL_HORIZONTAL );
		descriptionTitle.setLayoutData( data );

		description = new Label( previewPane, SWT.WRAP );

		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalIndent = 20;
		description.setLayoutData( data );

		chkBox = new Button( composite, SWT.CHECK );
		chkBox.setText( MESSAGE_SHOW_CHEATSHEET );
		chkBox.setSelection( ReportPlugin.readCheatSheetPreference( ) );
		chkBox.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ReportPlugin.writeCheatSheetPreference( chkBox.getSelection( ) );
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{
				ReportPlugin.writeCheatSheetPreference( chkBox.getSelection( ) );
			}
		} );

		hookListeners( );
		templateList.select( 0 );
		templateListener.handleEvent( new Event( ) );
		setControl( composite );
	}

	private void hookListeners( )
	{
		templateList.addListener( SWT.Selection, templateListener );

	}

	private Listener templateListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			//change description/image
			selectedIndex = templateList.getSelectionIndex( );
			description.setText( templates[selectedIndex].description );

			String key = templates[selectedIndex].picturePath;
			Object img = imageMap.get( key );

			if ( img == null )
			{
				img = ReportPlugin.getImage( key );

				imageMap.put( key, img );
			}

			previewCanvas.clear( );
			previewCanvas.loadImage( ( (Image) img ) );
			previewCanvas.showOriginal( );

			chkBox.setEnabled( !templates[selectedIndex].cheatSheetId.equals( "" ) );
		}
	};

	/**
	 * @return Returns the templates of selected item.
	 */
	public Template getTemplate( )
	{
		return templates[selectedIndex];
	}

	/**
	 * @return Returns the blank report template.
	 */
	public Template getBlankTemplate( )
	{
		return templates[0];
	}

	/**
	 * @return true if show CheatSheets is checked.
	 */
	public boolean getShowCheatSheet( )
	{
		return chkBox.getSelection( );
	}

}