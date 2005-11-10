/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.MoreOptionsChartPlotSheet;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartPlotSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent = null;

	private transient Button btnIncludingVisible;

	private transient Button btnWithinVisible;

	private transient FillChooserComposite cmbBlockColor;

	private transient FillChooserComposite cmbClientAreaColor;

	private transient Button btnArea;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, true );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 2, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		Label lblIncludingAxes = new Label( cmpBasic, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			lblIncludingAxes.setLayoutData( gd );
			lblIncludingAxes.setFont( JFaceResources.getBannerFont( ) );
			lblIncludingAxes.setText( Messages.getString( "ChartPlotSheetImpl.Label.AreaIncludingAxes" ) ); //$NON-NLS-1$
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBlockColor = new FillChooserComposite( cmpBasic,
				SWT.DROP_DOWN | SWT.READ_ONLY,
				getChart( ).getPlot( ).getBackground( ),
				true,
				true );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmbBlockColor.setLayoutData( gd );
			cmbBlockColor.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Outline" ) ); //$NON-NLS-1$

		btnIncludingVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnIncludingVisible.setText( Messages.getString( "ChartPlotSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnIncludingVisible.addSelectionListener( this );
			btnIncludingVisible.setSelection( getChart( ).getPlot( )
					.getOutline( )
					.isVisible( ) );
		}

		Label lblWithinAxes = new Label( cmpBasic, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			lblWithinAxes.setLayoutData( gd );
			lblWithinAxes.setFont( JFaceResources.getBannerFont( ) );
			lblWithinAxes.setText( Messages.getString( "ChartPlotSheetImpl.Label.AreaWithinAxes" ) ); //$NON-NLS-1$
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbClientAreaColor = new FillChooserComposite( cmpBasic, SWT.DROP_DOWN
				| SWT.READ_ONLY, getChart( ).getPlot( )
				.getClientArea( )
				.getBackground( ), true, true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbClientAreaColor.setLayoutData( gridData );
			cmbClientAreaColor.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartPlotSheetImpl.Label.Outline" ) ); //$NON-NLS-1$

		btnWithinVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnWithinVisible.setText( Messages.getString( "ChartPlotSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnWithinVisible.addSelectionListener( this );
			btnWithinVisible.setSelection( getChart( ).getPlot( )
					.getClientArea( )
					.getOutline( )
					.isVisible( ) );
		}

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, true ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		btnArea = new Button( cmp, SWT.TOGGLE );
		btnArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		btnArea.setText( Messages.getString( "ChartPlotSheetImpl.Label.ClientArea" ) ); //$NON-NLS-1$
		btnArea.addSelectionListener( this );
	}

	public Object onHide( )
	{
		detachPopup( );
		cmpContent.dispose( );
		return getContext( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( cmbBlockColor ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getChart( ).getPlot( ).setBackground( (Fill) event.data );
			}
		}
		else if ( event.widget.equals( cmbClientAreaColor ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getChart( ).getPlot( )
						.getClientArea( )
						.setBackground( (Fill) event.data );
			}
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		// detach popup dialogue
		if ( detachPopup( e.widget ) )
		{
			return;
		}
		if ( e.widget instanceof Button
				&& ( ( (Button) e.widget ).getStyle( ) & SWT.TOGGLE ) == SWT.TOGGLE
				&& ( (Button) e.widget ).getSelection( ) )
		{
			selectAllButtons( false );
			( (Button) e.widget ).setSelection( true );
		}

		if ( e.widget.equals( btnIncludingVisible ) )
		{
			getChart( ).getPlot( )
					.getOutline( )
					.setVisible( ( (Button) e.widget ).getSelection( ) );
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( btnWithinVisible ) )
		{
			getChart( ).getPlot( )
					.getClientArea( )
					.getOutline( )
					.setVisible( ( (Button) e.widget ).getSelection( ) );
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( btnArea ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new MoreOptionsChartPlotSheet( popupShell, getChart( ) );
			getWizard( ).attachPopup( btnArea.getText( ), -1, -1 );
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	protected void selectAllButtons( boolean isSelected )
	{
		btnArea.setSelection( isSelected );
	}

}