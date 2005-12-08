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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.PaletteEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * 
 */

public class SeriesPaletteSheet extends AbstractPopupSheet
{

	private transient Composite cmpContent = null;

	private transient Group grpPalette = null;

	private transient SeriesDefinition seriesDefn = null;

	public SeriesPaletteSheet( Composite parent, ChartWizardContext context,
			SeriesDefinition seriesDefn )
	{
		super( parent, context, false );
		this.seriesDefn = seriesDefn;
		cmpTop = getComponent( parent );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public Composite getComponent( Composite parent )
	{
		// Sheet content composite
		cmpContent = new Composite( parent, SWT.NONE );
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout( );
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout( glContent );
		}

		// Palete composite
		grpPalette = new Group( cmpContent, SWT.NONE );
		GridData gdGRPPalette = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPPalette.heightHint = 300;
		grpPalette.setLayoutData( gdGRPPalette );
		grpPalette.setLayout( new FillLayout( ) );
		grpPalette.setText( Messages.getString( "BaseSeriesAttributeSheetImpl.Lbl.Palette" ) ); //$NON-NLS-1$

		// Palette list
		new PaletteEditorComposite( grpPalette, seriesDefn.getSeriesPalette( ) );

		return cmpContent;
	}
}