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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.model.attribute.Location;

/**
 * This class encapsulates clipping information required for clip implementation in a device. Support for clipped areas
 * is limited to polygons. In the future, it may be upgraded to support additional arbitrary shape definitions.
 */
public final class ClipRenderEvent extends PrimitiveRenderEvent
{

    /**
     *  
     */
    private Location[] _loa;

    /**
     * @param oSource
     */
    public ClipRenderEvent(Object oSource)
    {
        super(oSource);
        // TODO Auto-generated constructor stub
    }

    /**
     * @return Returns the vertices associated with a polygon.
     */
    public final Location[] getVertices()
    {
        return _loa;
    }

    /**
     * @param loa
     *            The vertices associated with the polygon area to be clipped
     */
    public final void setVertices(Location[] loa)
    {
        _loa = loa;
    }
}