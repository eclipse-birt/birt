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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;


/**
 * add comment here
 * 
 */
public class EditorRuler
{
	public static final String PROPERTY_CHILDREN = "children changed"; //$NON-NLS-1$
	public static final String PROPERTY_UNIT = "units changed"; //$NON-NLS-1$
	public static final String PROPERTY_LEFTMARGIN = "left margin"; //$NON-NLS-1$
	public static final String PROPERTY_RIGHTMARGIN = "right margin"; //$NON-NLS-1$
		
	static final long serialVersionUID = 1;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private int unit;	
	private boolean horizontal;
	private List guides = new ArrayList();
	
	private int leftMargin, rightMargin;

	private EditorGuide left, right;
	/**
	 * @return Returns the listeners.
	 */
	public PropertyChangeSupport getListeners( )
	{
		return listeners;
	}
	/**
	 * @param listeners The listeners to set.
	 */
	public void setListeners( PropertyChangeSupport listeners )
	{
		this.listeners = listeners;
	}
	/**
	 * @return Returns the rightMargin.
	 */
	public int getRightMargin( )
	{
		if (right != null)
		{
			return right.getPosition();
		}
		return -1;
	}
	/**
	 * @param rightMargin The rightMargin to set.
	 */
	public void setRightMargin( int newMargin )
	{
//		if (rightMargin != newMargin) {
//			int oldUnit = rightMargin;
//			rightMargin = newMargin;
//			listeners.firePropertyChange(PROPERTY_RIGHTMARGIN, oldUnit, rightMargin);
//		}
		
		if (right == null)
		{
			right = new EditorGuide(!isHorizontal(), EditorGuide.RIGHT);
			right.setPosition(newMargin);
			addGuide(right);
		}
		else 
		{
			right.setPosition(newMargin);
		}
	}
	public EditorRuler(boolean isHorizontal) {
		this(isHorizontal, RulerProvider.UNIT_INCHES);
	}

	public EditorRuler(boolean isHorizontal, int unit) {
		horizontal = isHorizontal;
		setUnit(unit);
	}

	public void addGuide(EditorGuide guide) {
		if (!guides.contains(guide)) {
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

//	 the returned list should not be modified
	public List getGuides() {
		return guides;
	}

	public int getUnit() {
		return unit;
	}

	public boolean isHidden() {
		return false;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void removeGuide(EditorGuide guide) {
		if (guides.remove(guide)) {
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void setHidden(boolean isHidden) {
	}

	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
		}
	}
	/**
	 * @return Returns the leftMargin.
	 */
	public int getLeftMargin( )
	{
		if (left != null)
		{
			return left.getPosition();
		}
		return -1;
	}
	/**
	 * @param leftMargin The leftMargin to set.
	 */
	public void setLeftMargin( int newMargin )
	{
//		if (leftMargin != newMargin) {
//			int oldUnit = leftMargin;
//			leftMargin = newMargin;
//			listeners.firePropertyChange(PROPERTY_LEFTMARGIN, oldUnit, leftMargin);
//		}
		
		if (left == null)
		{
			left = new EditorGuide(!isHorizontal(), EditorGuide.LEFT);
			left.setPosition(newMargin);
			addGuide(left);
		}
		else 
		{
			left.setPosition(newMargin);
		}
	}
}
