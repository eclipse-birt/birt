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

    public Chart getModel(String sType, Orientation Orientation, String Dimension);

    public String[] getSupportedDimensions();

    public String getDefaultDimension();

    public boolean supportsTransposition();

    public IHelpContent getHelp();
}