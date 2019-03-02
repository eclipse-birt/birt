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

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.engine.api.script.element.ILabel;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Label extends ReportItem implements ILabel
{

    public Label( LabelHandle handle )
    {
        super( handle );
    }
    
    public Label( org.eclipse.birt.report.model.api.simpleapi.ILabel labelImpl )
    {
        super( labelImpl );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getText()
     */

    public String getText()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                .getText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getDisplayText()
     */

    public String getDisplayText()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                .getDisplayText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setText(java.lang.String)
     */

    public void setText( String text ) throws ScriptException
    {
        try
        {
            ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                    .setText( text );
        }
        catch( SemanticException e )
        {
            throw new ScriptException( e.getLocalizedMessage() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getTextKey()
     */

    public String getTextKey()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                .getTextKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setTextKey(java.lang.String)
     */

    public void setTextKey( String resourceKey ) throws ScriptException
    {
        try
        {
            ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                    .setTextKey( resourceKey );
        }
        catch( SemanticException e )
        {
            throw new ScriptException( e.getLocalizedMessage() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getHelpText()
     */

    public String getHelpText()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                .getHelpText();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setHelpText(java.lang.String)
     */

    public void setHelpText( String text ) throws ScriptException
    {
        try
        {
            ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                    .setHelpText( text );
        }
        catch( SemanticException e )
        {
            throw new ScriptException( e.getLocalizedMessage() );
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#getHelpTextKey()
     */

    public String getHelpTextKey()
    {
        return ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                .getHelpTextKey();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.engine.api.script.element.ILabel#setHelpTextKey(java.lang.String)
     */

    public void setHelpTextKey( String resourceKey ) throws ScriptException
    {
        try
        {
            ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                    .setHelpTextKey( resourceKey );
        }
        catch( SemanticException e )
        {
            throw new ScriptException( e.getLocalizedMessage() );
        }
    }

    public IAction getAction()
    {
        return new ActionImpl(
                ( (org.eclipse.birt.report.model.api.simpleapi.ILabel) designElementImpl )
                        .getAction() );
    }

	public void addAction( IAction action )
	{
		// TODO Auto-generated method stub
		
	}



}
