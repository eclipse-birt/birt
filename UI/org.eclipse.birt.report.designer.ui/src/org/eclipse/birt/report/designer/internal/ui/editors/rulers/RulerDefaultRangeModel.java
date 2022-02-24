/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.RangeModel;

/**
 * 
 */

public class RulerDefaultRangeModel implements RangeModel, PropertyChangeListener {

	private RangeModel delegate;
	private List listeners = new ArrayList();

	/**
	 * @param model
	 */
	public RulerDefaultRangeModel(RangeModel model) {
		assert model != null;
		this.delegate = model;
		delegate.addPropertyChangeListener(this);
	}

	/**
	 * Registers the given listener as a PropertyChangeListener.
	 * 
	 * @param listener the listener to be added
	 * @since 2.0
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Notifies any listening PropertyChangeListeners that the property with the
	 * given id has changed.
	 * 
	 * @param string   the property name
	 * @param oldValue the old value
	 * @param newValue the new value
	 * @since 2.0
	 */
	protected void firePropertyChange(PropertyChangeEvent evt) {
		for (int i = 0; i < listeners.size(); i++) {
			PropertyChangeListener propertyListener = (PropertyChangeListener) listeners.get(i);
			propertyListener.propertyChange(evt);
		}
	}

	/**
	 * @return the extent
	 */
	public int getExtent() {
		return delegate.getExtent();
	}

	/**
	 * @return the maximum value
	 */
	public int getMaximum() {
		return delegate.getMaximum();
	}

	/**
	 * @return the minimum value
	 */
	public int getMinimum() {
		return delegate.getMinimum();
	}

	/**
	 * @return the current value
	 */
	public int getValue() {
		return delegate.getValue();
	}

	/**
	 * @return whether the extent is between the minimum and maximum values
	 */
	public boolean isEnabled() {
		return (getMaximum() - getMinimum()) > getExtent();
	}

	/**
	 * Removes the given PropertyChangeListener from the list of listeners.
	 * 
	 * @param listener the listener to be removed
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * @see org.eclipse.draw2d.RangeModel#setAll(int, int, int)
	 */
	public void setAll(int min, int ext, int max) {
		delegate.setAll(min, ext, max);
	}

	/**
	 * Sets this RangeModel's extent and fires a property change if the given value
	 * is different from the current extent.
	 * 
	 * @param extent the new extent value
	 */
	public void setExtent(int extent) {
		delegate.setExtent(extent);
	}

	/**
	 * Sets this RangeModel's maximum value and fires a property change if the given
	 * value is different from the current maximum value.
	 * 
	 * @param maximum the new maximum value
	 */
	public void setMaximum(int maximum) {
		delegate.setMaximum(maximum);
	}

	/**
	 * Sets this RangeModel's minimum value and fires a property change if the given
	 * value is different from the current minimum value.
	 * 
	 * @param minimum the new minumum value
	 */
	public void setMinimum(int minimum) {
		delegate.setMinimum(minimum);
	}

	/**
	 * Sets this RangeModel's current value. If the given value is greater than the
	 * maximum, the maximum value is used. If the given value is less than the
	 * minimum, the minimum value is used. If the adjusted value is different from
	 * the current value, a property change is fired.
	 * 
	 * @param value the new value
	 */
	public void setValue(int value) {
		delegate.setValue(value);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return delegate.toString();
	}

	/**
	 * 
	 */
	public void dispose() {
		delegate.removePropertyChangeListener(this);
		delegate = null;
		listeners.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		firePropertyChange(evt);
	}
}
