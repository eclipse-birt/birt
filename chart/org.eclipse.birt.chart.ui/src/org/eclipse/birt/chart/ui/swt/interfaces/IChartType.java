/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.interfaces;

import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.swt.graphics.Image;

/**
 * @author Actuate Corporation
 */
public interface IChartType
{

    /**
     * Returns the name of the chart type. This is what appears in the selection list in the Chart Selecter UI.
     * 
     * @return Chart type name.
     */
    public String getName();

    public Image getImage();

    /**
     * Returns the names of the chart sub-types available for this type. These names are used to build the sub-type
     * selection panel in the Chart Selecter UI.
     * 
     * @return Array of sub-type names.
     */
    public Collection getChartSubtypes(String Dimension, Orientation orientation);

    /**
     * Returns whether this type implementation can process the specified model. The first instance that returns true
     * will be considered as the correct chart type. If all types return false, the chart type and subtype from the
     * model will be used.
     * 
     * @param cModel
     *            chart model representing an existing chart
     * @param htModelHints
     *            pre-computed 'hints' from the model to reduce computations needed to be performed by each
     *            implementation.
     * @return true if this chart type can adapt the specified model to its own type. false if it cannot.
     */
    public boolean canAdapt(Chart cModel, Hashtable htModelHints);

    public Chart getModel(String sType, Orientation Orientation, String Dimension, Chart currentChart);

    public String[] getSupportedDimensions();

    public String getDefaultDimension();

    public boolean supportsTransposition();

    public IHelpContent getHelp();
}