/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.model.api.GridHandle;

public class Grid extends ReportItem implements IGrid
{

    public Grid( GridHandle grid )
    {
        super( grid );
    }
    
    public Grid( org.eclipse.birt.report.model.api.simpleapi.IGrid gridImpl )
    {
        super( gridImpl );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getColumnCount()
     */

    public int getColumnCount()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl )
                .getColumnCount();
    }
}
