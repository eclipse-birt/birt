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

import org.eclipse.birt.chart.computation.DataSetIterator;

/**
 * 
 */
public interface ISeriesRenderingHints
{
    /**
     * 
     */
    public static final int UNDEFINED = 0;
    
    /**
     * 
     */
    public static final int BASE_ORTHOGONAL_IN_SYNC = 1;
    
    /**
     * 
     */
    public static final int BASE_ORTHOGONAL_OUT_OF_SYNC = 2;
    
    /**
     * 
     */
    public static final int BASE_EMPTY = 4;
    
    /**
     * 
     */
    public static final int ORTHOGONAL_EMPTY = 8;
    
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
}
