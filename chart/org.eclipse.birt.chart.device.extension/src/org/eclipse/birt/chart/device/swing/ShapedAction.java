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

package org.eclipse.birt.chart.device.swing;

import java.awt.Polygon;
import java.awt.Shape;

import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This class ...
 * 
 * @author Actuate Corporation
 */

final class ShapedAction
{

    private final Object _oSource;

    private final Shape _sh;

    private final Action _ac;

    /**
     * This constructor supports polygon shapes Future shapes (and corresponding constructors) will be added later
     * 
     * @param loa
     * @param ac
     */
    ShapedAction(Object oSource, Location[] loa, Action ac)
    {
        _oSource = oSource;
        final int[][] i2a = SwingRendererImpl.getCoordinatesAsInts(loa);
        _sh = new Polygon(i2a[0], i2a[1], loa.length);
        _ac = ac;
    }

    /**
     * 
     * @return
     */
    final Shape getShape()
    {
        return _sh;
    }

    /**
     * 
     * @return
     */
    final Action getAction()
    {
        return _ac;
    }

    /**
     * 
     * @return
     */
    final Object getSource()
    {
        return _oSource;
    }
}