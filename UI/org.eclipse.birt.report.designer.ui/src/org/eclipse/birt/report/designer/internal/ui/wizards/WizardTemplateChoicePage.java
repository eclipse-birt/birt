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

import org.eclipse.birt.report.designer.internal.ui.util.graphics.ImageCanvas;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * Supplies template selection page of new report wizard
 * @author David Michonneau
 *  
 */
public class WizardTemplateChoicePage extends WizardPage
{

	private List templateList;

	private ImageCanvas previewCanvas;

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
			new Template( "Blank Report",
					"Blank Report",
					"/templates/blank_report.rptdesign",
					"/templates/blank_report.gif",
					""),
			new Template( "Simple Listing",
					"Simple Listing",
					"/templates/simple_listing.rptdesign",
					"/templates/simple_listing.gif",
					"org.eclipse.birt.report.designer.ui.cheatsheet.simplelisting"),
			new Template( "Grouped Listing",
					"Grouped Listing 1",
					"/templates/grouped_listing.rptdesign",
					"/templates/grouped_listing.gif",
					"org.eclipse.birt.report.designer.ui.cheatsheet.groupedlisting"),
			new Template( "Chart & Listing",
					"Chart & Listing",
					"/templates/chart_listing.rptdesign",
					"/templates/chart_listing.gif",
					""),
			new Template( "Dual Chart & Listing",
					"Dual Chart & Listing",
					"/templates/dual_column_chart_listing.rptdesign",
					"/templates/dual_column_chart_listing.gif",
					"" ),
			new Template( "Side by Side Chart & Listing",
					"Side by Side Chart & Listing",
					"/templates/sidebyside_chart_listing.rptdesign",
					"/templates/sidebyside_chart_listing.gif",
					"" ),
			new Template( "Mailing Labels",
					"Mailing Labels",
					"/templates/mailing_labels.rptdesign",
					"/templates/mailing_labels.gif",
					"" ),
			new Template( "Letter",
					"Letter",
					"/templates/letter.rptdesign",
					"/templates/letter.gif",
					"" )
	};

	protected int selectedIndex;

	/**
	 * @author David Michonneau
	 *  
	 */
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
		label0.setText( "Report Templates:" );

		Label previewLabel = new Label( composite, SWT.NONE );
		previewLabel.setText( "Preview:" );

		templateList = new List( composite, SWT.BORDER );
		for ( int i = 0; i < templates.length; i++ )
		{
			templateList.add( templates[i].name );
		}

		GridData data = new GridData( );
		data.widthHint = 170;
		data.heightHint = 550;
		data.verticalAlignment = GridData.BEGINNING;
		data.verticalSpan = 3;
		templateList.setLayoutData( data );

		previewCanvas = new ImageCanvas( composite, SWT.BORDER );

		data = new GridData( );
		data.widthHint = 370;
		data.heightHint = 474;
		data.verticalAlignment = GridData.BEGINNING;
		previewCanvas.setLayoutData( data );

		Label descriptionTitle = new Label( composite, SWT.NONE );
		descriptionTitle.setText( "Description:" );
		data = new GridData( );
		data.verticalAlignment = GridData.BEGINNING;
		data.widthHint = 370;
		descriptionTitle.setLayoutData( data );

		description = new Label( composite, SWT.WRAP );

		data = new GridData( );
		data.verticalAlignment = GridData.BEGINNING;
		data.widthHint = 370;
		description.setLayoutData( data );

		hookListeners( );
		templateList.select( 0 );
		templateListener.handleEvent( new Event( ) );
		setControl( composite );
	}

	/**
	 * @param templateList
	 * @param description
	 * @param previewCanvas
	 */
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
			previewCanvas.setImageData( ReportPlugin.getImage( templates[selectedIndex].picturePath )
					.getImageData( ) );

		}
	};

	/**
	 * @return Returns the templetes of selected item.
	 */
	public Template getTemplate( )
	{

		return templates[selectedIndex];
	}

}