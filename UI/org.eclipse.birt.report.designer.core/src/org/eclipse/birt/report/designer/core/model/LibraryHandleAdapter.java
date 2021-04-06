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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Adapter class to adapt model handle. This adapter provides convenience
 * methods to GUI requirement ReportDesignHandleAdapter responds to model
 * LibraryHandle
 * 
 */
public class LibraryHandleAdapter extends ReportDesignHandleAdapter {

	private Object currentEditorModel;

	private Object oldEditorModel;

	public static final String CURRENTMODEL = "current model"; //$NON-NLS-1$

	public static final String CREATE_ELEMENT = "create element"; //$NON-NLS-1$

	private List listeners = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param handle The moudle handle
	 */
	public LibraryHandleAdapter(ModuleHandle handle) {
		super(handle);
		setCurrentEditorModel(handle, CURRENTMODEL);
	}

	/**
	 * Constructor
	 * 
	 * @param handle The moudle handle
	 */
	public LibraryHandleAdapter(ModuleHandle handle, IModelAdapterHelper mark) {
		super(handle, mark);
		setCurrentEditorModel(handle, CURRENTMODEL);
	}

	/**
	 * Add listener
	 * 
	 * @param listener The listener to add
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {

		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Remove listener
	 * 
	 * @param listener The listener to remove
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire property change
	 * 
	 * @param event The property change event
	 */
	public void firePropertyChangeEvent(PropertyChangeEvent event) {
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			PropertyChangeListener listener = (PropertyChangeListener) (listeners.get(i));
			listener.propertyChange(event);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter
	 * #getChildren()
	 */
	public List getChildren() {
		// if currentEditorModel is a compound componnet , gets its childrem
		List list = new ArrayList();
		list.add(getTopContainer(getCurrentEditorModel()));
		return list;
		// return getModuleHandle().getComponents().getContents( );
	}

	/**
	 * Get top container
	 * 
	 * @param currentModel The specified object
	 * @return The sepecifed object's container
	 */
	private Object getTopContainer(Object currentModel) {
		Object obj = currentModel;
		if (currentModel instanceof DesignElementHandle) {
			DesignElementHandle handle = (DesignElementHandle) currentModel;
			while (handle.getContainer() != null) {
				if (handle.getContainer() instanceof ModuleHandle) {
					obj = handle;
					break;
				}
				handle = handle.getContainer();
			}

		} else if (currentModel instanceof IAdaptable) {
			Object adapter = ((IAdaptable) currentModel).getAdapter(this.getClass());
			if (adapter instanceof DesignElementHandle)
				return getTopContainer(adapter);
		}
		return obj;
	}

	/**
	 * Get current eitor model
	 * 
	 * @return Returns the currentEditorModel.
	 */
	public Object getCurrentEditorModel() {
		return currentEditorModel;
	}

	/**
	 * Get current eitor model
	 * 
	 * @param current The current editor model to set.
	 * @param type    The type
	 */
	public void setCurrentEditorModel(Object current, String type) {
		oldEditorModel = this.currentEditorModel;
		if (current == null || current instanceof LibraryHandle) {
			this.currentEditorModel = new LibRootModel(current);
		} else {
			this.currentEditorModel = current;
		}

		// if (currentEditorModel == old)
		// {
		// return;
		// }
		PropertyChangeEvent event = new PropertyChangeEvent(this, type, oldEditorModel, this.currentEditorModel);
		firePropertyChangeEvent(event);
	}

	public Object getOldEditorModel() {
		return oldEditorModel;
	}
}
