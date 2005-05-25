/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.ui.extensions;
import org.eclipse.swt.widgets.Composite;
/**
 * This interface is used to represent a new tab in the Property Editor
 * view. It creates the UI, updates property values when requested,
 * and notifies the BIRT framework of any property change through this UI
 */
public interface IPropertyTabUI
{
    
    /**
     * Registers an IPropertyListener. The implementation
     * is responsible to notify this listener, whenever the user
     * modifies a value through one of the widgets in the UI.
     * @param listener The listener to register
     */
    public void addPropertyListener( IPropertyListener listener );
   
    /**
     * Deregisters the listener
     * @param listener The listener to be removed
     */
    public void removePropertyListener( IPropertyListener listener );
    
    /**
     * Creates the widgets to be shown in the tab
     * @param composite The top level composite inside the tab
     */
    public void buildUI( Composite composite );
    
    /**
     * @return the display name for the tab
     */
    public String getTabDisplayName( );
    
    /**
     * This method is in charge of updating the property value in the UI. If 
     * the property is not represented in this tab, no action is needed.
     *  
     * @param propertyName The name of the property to update
     * @param value The new value to display
     */
    public void setPropertyValue( String propertyName, IPropertyValue value );
    
}
