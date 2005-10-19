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

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Implement Meter Chart -> Orthogonal Series -> Series Details 
 */
public class MeterSeriesAttributeComposite extends Composite implements
		Listener {

	private transient Composite cmpContent = null;
	
	private transient Composite cmpContentAngle = null;
	
	private transient TextEditorComposite txtRadius = null;

	private transient IntegerSpinControl iscStartAngle = null;

	private transient IntegerSpinControl iscStopAngle = null;

	private transient DialSeries series = null;

	private static ILogger logger = Logger
			.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public MeterSeriesAttributeComposite(Composite parent, int style,
			Series series) 
	{
		super(parent, style);
		if (!(series instanceof DialSeriesImpl)) 
		{
			try {
				throw new ChartException(						
						ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"MeterSeriesAttributeComposite.Exception.IllegalArgument", new Object[] { series.getClass().getName() }, Messages.getResourceBundle()); //$NON-NLS-1$
			} catch (ChartException e){			
				logger.log(e);
				e.printStackTrace();
			}
		}
		this.series = (DialSeries) series;
		init();
		placeComponents();
	}

	private void init() 
	{
		this.setSize(getParent().getClientArea().width, getParent()
				.getClientArea().height);
	}

	private void placeComponents() 
	{
		// Layout for the content composite
		GridLayout glContent = new GridLayout();
		glContent.verticalSpacing=0;
		glContent.marginHeight = 7;
		glContent.marginWidth = 7;

		// Main content composite
		this.setLayout(glContent);
		
		//Composite for Radius
		cmpContent = new Composite(this, SWT.NONE);		
		GridData gdcmpContent = new GridData(GridData.FILL_HORIZONTAL);
		cmpContent.setLayoutData(gdcmpContent);
		cmpContent.setLayout(new GridLayout(8, true));

		Label lblRadius = new Label(cmpContent, SWT.NONE);
		GridData gdLBLRadius = new GridData(GridData.HORIZONTAL_ALIGN_END);
		lblRadius.setLayoutData(gdLBLRadius);
		lblRadius.setText(Messages
				.getString("MeterSeriesAttributeComposite.Lbl.Radius")); //$NON-NLS-1$

		txtRadius = new TextEditorComposite(cmpContent, SWT.BORDER
				| SWT.SINGLE);
		GridData gdTXTRadius = new GridData(GridData.FILL_HORIZONTAL);
		gdTXTRadius.horizontalSpan=2;
		txtRadius.setText(String.valueOf(series.getDial().getRadius()));
		txtRadius.setLayoutData(gdTXTRadius);
		txtRadius.addListener(this);

		//Composite for Angle
		cmpContentAngle = new Composite(this, SWT.NONE);		
		GridData gdcmpContentAngle = new GridData(GridData.FILL_HORIZONTAL);
		cmpContentAngle.setLayoutData(gdcmpContentAngle);
		cmpContentAngle.setLayout(new GridLayout(8, true));
		
		Label lblStartAngle = new Label(cmpContentAngle, SWT.NONE);
		GridData gdLBLStartAngle = new GridData(GridData.HORIZONTAL_ALIGN_END);
		lblStartAngle.setLayoutData(gdLBLStartAngle);
		lblStartAngle.setText(Messages
				.getString("MeterSeriesAttributeComposite.Lbl.StartAngle")); //$NON-NLS-1$

		iscStartAngle = new IntegerSpinControl(cmpContentAngle, SWT.NONE,
				(int) ((DialSeries) series).getDial().getStartAngle());
		GridData gdISCStartAngle = new GridData(GridData.FILL_HORIZONTAL);
		gdISCStartAngle.horizontalSpan=2;
		iscStartAngle.setLayoutData(gdISCStartAngle);
		iscStartAngle.setValue((int)(series.getDial().getStartAngle()));
		iscStartAngle.setMinimum(0);
		iscStartAngle.setMaximum(180);
		iscStartAngle.addListener(this);

		Label lblStopAngle = new Label(cmpContentAngle, SWT.NONE);
		GridData gdLBLStopAngle = new GridData(GridData.HORIZONTAL_ALIGN_END);
		lblStopAngle.setLayoutData(gdLBLStopAngle);
		lblStopAngle.setText(Messages
				.getString("MeterSeriesAttributeComposite.Lbl.StopAngle")); //$NON-NLS-1$

		iscStopAngle = new IntegerSpinControl(cmpContentAngle, SWT.NONE,
				(int) ((DialSeries) series).getDial().getStopAngle());
		GridData gdISCStopAngle = new GridData(GridData.FILL_HORIZONTAL);
		gdISCStopAngle.horizontalSpan=2;
		iscStopAngle.setLayoutData(gdISCStopAngle);
		iscStopAngle.setValue((int)(series.getDial().getStopAngle()));
		iscStopAngle.setMinimum(0);
		iscStopAngle.setMaximum(360);
		iscStopAngle.addListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) 
	{
		if (event.widget.equals(txtRadius))
        {
            series.getDial().setRadius(Double.parseDouble(txtRadius.getText()));
        }
		else if (event.widget.equals(iscStartAngle)) 
		{
			series.getDial().setStartAngle(((Integer) event.data).intValue());
		}
		else if (event.widget.equals(iscStopAngle)) 
		{
			series.getDial().setStopAngle(((Integer) event.data).intValue());
		}
	}
}
