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
package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.report.model.extension.IElementCommand;

/**
 * @author Actuate Corporation
 *  
 */
public class ChartElementCommandImpl implements IElementCommand
{

    /**
     *  
     */
    public ChartElementCommandImpl()
    {
        super();
        // TODO Auto-generated constructor stub
        int i = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#execute()
     */
    public void execute()
    {
        int i = 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#undo()
     */
    public void undo()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#redo()
     */
    public void redo()
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#canUndo()
     */
    public boolean canUndo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#canRedo()
     */
    public boolean canRedo()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#getLabel()
     */
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return "Chart";
    }

}