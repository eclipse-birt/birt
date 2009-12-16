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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class TitleBlockSheet extends AbstractPopupSheet
		implements
			SelectionListener,
			Listener
{

	private transient Composite cmpContent;

	private transient Group grpGeneral;

	private transient Combo cmbAnchor;

	private transient Combo cmbStretch;

	private transient Group grpOutline;

	private transient LineAttributesComposite liacOutline;
	
	private transient FillChooserComposite fccBackground;

	private transient InsetsComposite ic;
	
	public TitleBlockSheet( String title, ChartWizardContext context )
	{
		super( title, context, false );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_TITLE_BLOCK);
		
		// Layout for the content composite
		GridLayout glContent = new GridLayout( );
		glContent.horizontalSpacing = 5;
		glContent.verticalSpacing = 5;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Sheet content composite
		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( glContent );

		grpGeneral = new Group( cmpContent, SWT.NONE );
		{
			// Layout for general composite
			GridLayout glGeneral = new GridLayout( );
			glGeneral.verticalSpacing = 10;
			glGeneral.marginHeight = 7;
			glGeneral.marginWidth = 7;
			GridData gdCMPGeneral = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
					| GridData.FILL_HORIZONTAL );
			grpGeneral.setLayoutData( gdCMPGeneral );
			grpGeneral.setLayout( glGeneral );
			grpGeneral.setText( Messages.getString( "TitlePropertiesSheet.Label.TitleArea" ) ); //$NON-NLS-1$
		}

		Composite cmpGeneralTop = new Composite( grpGeneral, SWT.NONE );
		{
			GridLayout layout = new GridLayout( );
			layout.numColumns = 2;
			layout.horizontalSpacing = 5;
			layout.verticalSpacing = 5;
			cmpGeneralTop.setLayout( layout );
			cmpGeneralTop.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		Label lblAnchor = new Label( cmpGeneralTop, SWT.NONE );
		GridData gdLBLAnchor = new GridData( );
		lblAnchor.setLayoutData( gdLBLAnchor );
		lblAnchor.setText( Messages.getString( "TitlePropertiesSheet.Label.Anchor" ) ); //$NON-NLS-1$

		cmbAnchor = new Combo( cmpGeneralTop, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		cmbAnchor.setLayoutData( gdCBAnchor );
		cmbAnchor.addSelectionListener( this );
		cmbAnchor.setVisibleItemCount( 30 );

		Label lblStretch = new Label( cmpGeneralTop, SWT.NONE );
		GridData gdLBLStretch = new GridData( );
		lblStretch.setLayoutData( gdLBLStretch );
		lblStretch.setText( Messages.getString( "TitlePropertiesSheet.Label.Stretch" ) ); //$NON-NLS-1$

		cmbStretch = new Combo( cmpGeneralTop, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBStretch = new GridData( GridData.FILL_HORIZONTAL );
		cmbStretch.setLayoutData( gdCBStretch );
		cmbStretch.addSelectionListener( this );

		Label lblBackground = new Label( cmpGeneralTop, SWT.NONE );
		GridData gdLBLBackground = new GridData( );
		lblBackground.setLayoutData( gdLBLBackground );
		lblBackground.setText( Messages.getString( "TitlePropertiesSheet.Label.Background" ) ); //$NON-NLS-1$

		fccBackground = new FillChooserComposite( cmpGeneralTop,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getBackground( ),
				true,
				true );
		GridData gdFCCBackground = new GridData( GridData.FILL_HORIZONTAL );
		fccBackground.setLayoutData( gdFCCBackground );
		fccBackground.addListener( this );

		grpOutline = new Group( grpGeneral, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "TitlePropertiesSheet.Label.Outline" ) ); //$NON-NLS-1$

		liacOutline = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getOutline( ),
				true,
				true,
				true );
		liacOutline.addListener( this );

		ic = new InsetsComposite( grpGeneral,
				SWT.NONE,
				getBlockForProcessing( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		ic.setLayoutData( gdInsets );

		populateLists( );

		return cmpContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fccBackground ) )
		{
			getBlockForProcessing( ).setBackground( (Fill) event.data );
		}
		else if ( event.widget.equals( this.liacOutline ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
	}

	private boolean isAnchorVertical( Anchor anchor )
	{
		return anchor.getValue( ) == Anchor.EAST
				|| anchor.getValue( ) == Anchor.WEST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cmbAnchor ) )
		{
			boolean bAnchorVerticalOld = isAnchorVertical( getBlockForProcessing( ).getAnchor( ) );
			getBlockForProcessing( ).setAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbAnchor.getText( ) ) ) );
			boolean bAnchorVerticalNew = isAnchorVertical( getBlockForProcessing( ).getAnchor( ) );
			if ( bAnchorVerticalOld != bAnchorVerticalNew )
			{
				double rotationOld = getBlockForProcessing( ).getLabel( )
						.getCaption( )
						.getFont( )
						.getRotation( );
				double rotationNew = rotationOld >= 0 ? 90 - rotationOld : -90
						- rotationOld;
				getBlockForProcessing( ).getLabel( )
						.getCaption( )
						.getFont( )
						.setRotation( rotationNew );
			}
		}
		else if ( oSource.equals( cmbStretch ) )
		{
			getBlockForProcessing( ).setStretch( Stretch.getByName( LiteralHelper.stretchSet.getNameByDisplayName( cmbStretch.getText( ) ) ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	private LabelBlock getBlockForProcessing( )
	{
		return getChart( ).getTitle( );
	}

	private void populateLists( )
	{
		// Set block Anchor property
		NameSet ns = LiteralHelper.titleAnchorSet;
		cmbAnchor.setItems( ns.getDisplayNames( ) );
		cmbAnchor.select( ns.getSafeNameIndex( getBlockForProcessing( ).getAnchor( )
				.getName( ) ) );

		// Set the block Stretch property
		ns = LiteralHelper.stretchSet;
		cmbStretch.setItems( ns.getDisplayNames( ) );
		cmbStretch.select( ns.getSafeNameIndex( getBlockForProcessing( ).getStretch( )
				.getName( ) ) );
	}

}
