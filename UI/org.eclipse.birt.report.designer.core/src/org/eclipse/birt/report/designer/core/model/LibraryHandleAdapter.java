/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.core.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;


/**
 *Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ReportDesignHandleAdapter responds to model
 * LibraryHandle
 * 
 */
public class LibraryHandleAdapter extends ReportDesignHandleAdapter
{

	private Object currentEditorModel;
	
	public static final String CURRENTMODEL = "current model";
	
	
	private List listeners = new ArrayList();
	/**
	 * @param handle
	 */
	public LibraryHandleAdapter( ModuleHandle handle )
	{
		super( handle );
		setCurrentEditorModel(handle);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener)
	{
		if (!listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener)
	{
		listeners.remove(listener);
	}
	
	public void firePropertyChangeEvent(PropertyChangeEvent event)
	{
		int size = listeners.size();
		for (int i=0; i<size; i++)
		{
			PropertyChangeListener listener = (PropertyChangeListener)(listeners.get(i));
			listener.propertyChange(event);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter#getChildren()
	 */
	public List getChildren( )
	{
		//if currentEditorModel is a compound componnet , gets its childrem
		List list = new ArrayList();
		list.add(currentEditorModel);
		return list;
		//return getModuleHandle().getComponents().getContents( );
	}
	/**
	 * @return Returns the currentEditorModel.
	 */
	public Object getCurrentEditorModel( )
	{
		return currentEditorModel;
	}
	/**
	 * @param currentEditorModel The currentEditorModel to set.
	 */
	public void setCurrentEditorModel( Object current )
	{
		Object old = this.currentEditorModel;
		if (current == null || current instanceof LibraryHandle)
		{
			this.currentEditorModel = new LibRootModel(current);
		}
		else
		{
			this.currentEditorModel = current;
		}
		
		if (currentEditorModel == old)
		{
			return;
		}
		PropertyChangeEvent event = new PropertyChangeEvent(this, CURRENTMODEL,old, this.currentEditorModel );
		firePropertyChangeEvent(event);
	}
}
