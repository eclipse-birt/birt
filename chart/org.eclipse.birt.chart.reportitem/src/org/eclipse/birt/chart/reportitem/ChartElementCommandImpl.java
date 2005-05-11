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

import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.report.model.api.extension.IElementCommand;

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
        //        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#execute()
     */
    public void execute()
    {
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
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.extension.IElementCommand#canRedo()
     */
    public boolean canRedo()
    {
        // TODO Auto-generated method stub
        return true;
    }



    /* (non-Javadoc)
     * @see org.eclipse.birt.report.model.api.extension.IElementCommand#getLabel()
     */
    public String getLabel( )
    {
        return Messages.getString( "ChartElementCommandImpl.editChart" ) ; 
		   
    }

}