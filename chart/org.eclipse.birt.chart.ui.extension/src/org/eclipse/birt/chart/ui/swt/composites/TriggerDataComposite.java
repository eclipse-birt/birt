/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 *  
 */
public class TriggerDataComposite extends Composite implements SelectionListener, Listener, KeyListener
{

    private transient Group grpValue = null;

    private transient Composite cmpURL = null;

    private transient Text txtBaseURL = null;

    private transient Text txtTarget = null;

    private transient Group grpParameters = null;

    private transient Text txtBaseParm = null;

    private transient Text txtValueParm = null;

    private transient Text txtSeriesParm = null;

    private transient Composite cmpScript = null;

    private transient Text txtScript = null;

    private transient Composite cmpTooltip = null;

    private transient IntegerSpinControl iscDelay = null;

    private transient Text txtTooltipText = null;

    private transient Composite cmpVisibility = null;

    private transient Text txtSeriesDefinition = null;

    private transient StackLayout slValues = null;

    private transient Combo cmbTriggerType = null;

    private transient Combo cmbActionType = null;

    private transient Trigger trigger = null;

    private transient Vector vListeners = null;

    /**
     * @param parent
     * @param style
     */
    public TriggerDataComposite(Composite parent, int style, Trigger trigger)
    {
        super(parent, style);
        this.trigger = trigger;
        init();
        placeComponents();
    }

    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        vListeners = new Vector();
    }

    private void placeComponents()
    {
        // Layout for the content composite
        GridLayout glContent = new GridLayout();
        glContent.numColumns = 6;
        glContent.horizontalSpacing = 16;
        glContent.verticalSpacing = 4;
        glContent.marginHeight = 2;
        glContent.marginWidth = 2;

        // Layout for the Action Details group
        slValues = new StackLayout();

        // Layout for script value composite
        GridLayout glScript = new GridLayout();
        glScript.marginWidth = 4;
        glScript.marginHeight = 6;

        // Layout for script value composite
        GridLayout glVisibility = new GridLayout();
        glVisibility.marginWidth = 4;
        glVisibility.marginHeight = 6;
        glVisibility.horizontalSpacing = 6;
        glVisibility.numColumns = 3;

        // Layout for script value composite
        GridLayout glTooltip = new GridLayout();
        glTooltip.marginWidth = 2;
        glTooltip.marginHeight = 6;
        glTooltip.horizontalSpacing = 6;
        glTooltip.numColumns = 3;

        // Layout for script value composite
        GridLayout glURL = new GridLayout();
        glURL.marginWidth = 2;
        glURL.marginHeight = 6;
        glURL.horizontalSpacing = 6;
        glURL.numColumns = 3;

        // Layout for script value composite
        GridLayout glParameter = new GridLayout();
        glParameter.marginWidth = 2;
        glParameter.marginHeight = 6;
        glParameter.horizontalSpacing = 6;
        glParameter.numColumns = 3;

        // Main content composite
        this.setLayout(glContent);

        Label lblTriggerType = new Label(this, SWT.NONE);
        GridData gdLBLTriggerType = new GridData();
        gdLBLTriggerType.widthHint = 40;
        gdLBLTriggerType.horizontalIndent = 4;
        lblTriggerType.setLayoutData(gdLBLTriggerType);
        lblTriggerType.setText("Type:");

        cmbTriggerType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBTriggerType = new GridData(GridData.FILL_HORIZONTAL);
        gdCMBTriggerType.horizontalSpan = 2;
        cmbTriggerType.setLayoutData(gdCMBTriggerType);
        cmbTriggerType.addSelectionListener(this);

        Label lblActionType = new Label(this, SWT.NONE);
        GridData gdLBLActionType = new GridData();
        gdLBLActionType.widthHint = 34;
        lblActionType.setLayoutData(gdLBLActionType);
        lblActionType.setText("Action:");

        cmbActionType = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gdCMBActionType = new GridData(GridData.FILL_HORIZONTAL);
        gdCMBActionType.horizontalSpan = 2;
        cmbActionType.setLayoutData(gdCMBActionType);
        cmbActionType.addSelectionListener(this);

        grpValue = new Group(this, SWT.NONE);
        GridData gdGRPValue = new GridData(GridData.FILL_BOTH);
        gdGRPValue.horizontalSpan = 6;
        grpValue.setLayoutData(gdGRPValue);
        grpValue.setText("Action Details");
        grpValue.setLayout(slValues);

        // Composite for script value
        cmpScript = new Composite(grpValue, SWT.NONE);
        cmpScript.setLayout(glScript);

        Label lblScript = new Label(cmpScript, SWT.NONE);
        GridData gdLBLScript = new GridData();
        lblScript.setLayoutData(gdLBLScript);
        lblScript.setText("Script:");

        txtScript = new Text(cmpScript, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
        GridData gdTXTScript = new GridData(GridData.FILL_BOTH);
        txtScript.setLayoutData(gdTXTScript);

        // Composite for series value
        cmpVisibility = new Composite(grpValue, SWT.NONE);
        cmpVisibility.setLayout(glVisibility);

        Label lblSeries = new Label(cmpVisibility, SWT.NONE);
        GridData gdLBLSeries = new GridData();
        lblSeries.setLayoutData(gdLBLSeries);
        lblSeries.setText("Series Definition:");

        txtSeriesDefinition = new Text(cmpVisibility, SWT.BORDER);
        GridData gdTXTSeriesDefinition = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTSeriesDefinition.horizontalSpan = 2;
        txtSeriesDefinition.setLayoutData(gdTXTSeriesDefinition);

        // Composite for tooltip value
        cmpTooltip = new Composite(grpValue, SWT.NONE);
        cmpTooltip.setLayout(glTooltip);

        Label lblDelay = new Label(cmpTooltip, SWT.NONE);
        GridData gdLBLDelay = new GridData();
        lblDelay.setLayoutData(gdLBLDelay);
        lblDelay.setText("Tooltip Delay (in ms):");

        iscDelay = new IntegerSpinControl(cmpTooltip, SWT.NONE, 200);
        GridData gdISCDelay = new GridData();
        gdISCDelay.horizontalSpan = 2;
        gdISCDelay.widthHint = 50;
        iscDelay.setLayoutData(gdISCDelay);
        iscDelay.setMinimum(100);
        iscDelay.setMaximum(5000);
        iscDelay.setIncrement(100);

        Label lblText = new Label(cmpTooltip, SWT.NONE);
        GridData gdLBLText = new GridData();
        gdLBLText.horizontalSpan = 3;
        lblText.setLayoutData(gdLBLText);
        lblText.setText("Tooltip Text:");

        txtTooltipText = new Text(cmpTooltip, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        GridData gdTXTTooltipText = new GridData(GridData.FILL_BOTH);
        gdTXTTooltipText.horizontalSpan = 3;
        txtTooltipText.setLayoutData(gdTXTTooltipText);

        cmpURL = new Composite(grpValue, SWT.NONE);
        cmpURL.setLayout(glURL);

        Label lblBaseURL = new Label(cmpURL, SWT.NONE);
        GridData gdLBLBaseURL = new GridData();
        lblBaseURL.setLayoutData(gdLBLBaseURL);
        lblBaseURL.setText("Base URL:");

        txtBaseURL = new Text(cmpURL, SWT.BORDER);
        GridData gdTXTBaseURL = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTBaseURL.horizontalSpan = 2;
        txtBaseURL.setLayoutData(gdTXTBaseURL);

        Label lblTarget = new Label(cmpURL, SWT.NONE);
        GridData gdLBLTarget = new GridData();
        lblTarget.setLayoutData(gdLBLTarget);
        lblTarget.setText("Target:");

        txtTarget = new Text(cmpURL, SWT.BORDER);
        GridData gdTXTTarget = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTTarget.horizontalSpan = 2;
        txtTarget.setLayoutData(gdTXTTarget);

        grpParameters = new Group(cmpURL, SWT.NONE);
        GridData gdGRPParameters = new GridData(GridData.FILL_HORIZONTAL);
        gdGRPParameters.horizontalSpan = 3;
        grpParameters.setLayoutData(gdGRPParameters);
        grpParameters.setLayout(glParameter);
        grpParameters.setText("Parameter Names");

        Label lblBaseParm = new Label(grpParameters, SWT.NONE);
        GridData gdLBLBaseParm = new GridData();
        lblBaseParm.setLayoutData(gdLBLBaseParm);
        lblBaseParm.setText("Base Parameter:");

        txtBaseParm = new Text(grpParameters, SWT.BORDER);
        GridData gdTXTBaseParm = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTBaseParm.horizontalSpan = 2;
        txtBaseParm.setLayoutData(gdTXTBaseParm);

        Label lblValueParm = new Label(grpParameters, SWT.NONE);
        GridData gdLBLValueParm = new GridData();
        lblValueParm.setLayoutData(gdLBLValueParm);
        lblValueParm.setText("Value Parameter:");

        txtValueParm = new Text(grpParameters, SWT.BORDER);
        GridData gdTXTValueParm = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTValueParm.horizontalSpan = 2;
        txtValueParm.setLayoutData(gdTXTValueParm);

        Label lblSeriesParm = new Label(grpParameters, SWT.NONE);
        GridData gdLBLSeriesParm = new GridData();
        lblSeriesParm.setLayoutData(gdLBLSeriesParm);
        lblSeriesParm.setText("Series Parameter:");

        txtSeriesParm = new Text(grpParameters, SWT.BORDER);
        GridData gdTXTSeriesParm = new GridData(GridData.FILL_HORIZONTAL);
        gdTXTSeriesParm.horizontalSpan = 2;
        txtSeriesParm.setLayoutData(gdTXTSeriesParm);

        populateLists();
        slValues.topControl = cmpURL;
    }

    private void populateLists()
    {
        cmbTriggerType.add(TriggerCondition.MOUSE_CLICK_LITERAL.getName());
        cmbTriggerType.add(TriggerCondition.MOUSE_HOVER_LITERAL.getName());
        cmbTriggerType.select(0);

        cmbActionType.add(ActionType.URL_REDIRECT_LITERAL.getName());
        cmbActionType.add(ActionType.TOGGLE_VISIBILITY_LITERAL.getName());
        cmbActionType.add(ActionType.SHOW_TOOLTIP_LITERAL.getName());
        cmbActionType.add(ActionType.INVOKE_SCRIPT_LITERAL.getName());
        cmbActionType.select(0);
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void fireEvent()
    {
        Event event = new Event();
        event.widget = this;
        // TODO: Set the event type and data before firing
        for (int i = 0; i < vListeners.size(); i++)
        {
            ((Listener) vListeners.get(i)).handleEvent(event);
        }
    }

    public Point getPreferredSize()
    {
        return new Point(360, 260);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        if (e.getSource().equals(cmbTriggerType))
        {

        }
        else if (e.getSource().equals(cmbActionType))
        {
            switch (cmbActionType.getSelectionIndex())
            {
                case 0:
                    this.slValues.topControl = cmpURL;
                    grpValue.layout();
                    break;
                case 1:
                    this.slValues.topControl = cmpVisibility;
                    grpValue.layout();
                    break;
                case 2:
                    this.slValues.topControl = cmpTooltip;
                    grpValue.layout();
                    break;
                case 3:
                    this.slValues.topControl = cmpScript;
                    grpValue.layout();
                    break;
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
        // TODO Auto-generated method stub

    }
}