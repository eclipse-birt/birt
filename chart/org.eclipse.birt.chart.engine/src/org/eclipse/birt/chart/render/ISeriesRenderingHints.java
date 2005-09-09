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

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * ISeriesRenderingHints
 */
public interface ISeriesRenderingHints
{
    public static final int UNDEFINED = 0;
    
    public static final int BASE_ORTHOGONAL_IN_SYNC = 1;
    
    public static final int BASE_ORTHOGONAL_OUT_OF_SYNC = 2;
    
    public static final int BASE_EMPTY = 4;
    
    public static final int ORTHOGONAL_EMPTY = 8;
    
    public static final int BASE_ANCILLARY_IN_SYNC = 16;
    
    public static final int BASE_ANCILLARY_OUT_OF_SYNC = 32;
    
    public static final int ANCILLARY_EMPTY = 64;
    
    /**
     * @return
     */
    public int getDataSetStructure();
    
    /**
     * @return
     */
    public DataSetIterator getBaseDataSet();
    
    /**
     * @return
     */
    public DataSetIterator getOrthogonalDataSet();
    
    /**
     * @param bReduceByInsets
     * @return
     */
    public Bounds getClientAreaBounds( boolean bReduceByInsets );
    
    /**
     * @return
     */
    public DataPointHints[] getDataPoints( );
}
