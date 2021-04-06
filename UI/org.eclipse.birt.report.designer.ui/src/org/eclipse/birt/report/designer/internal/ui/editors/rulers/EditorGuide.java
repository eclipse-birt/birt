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
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * add comment here
 * 
 */
public class EditorGuide {

	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	/**
	 * Property used to notify listeners when the parts attached to a guide are
	 * changed
	 */
	public static final String PROPERTY_CHILDREN = "subparts changed"; //$NON-NLS-1$
	/**
	 * Property used to notify listeners when the guide is re-positioned
	 */
	public static final String PROPERTY_POSITION = "position changed"; //$NON-NLS-1$

	static final long serialVersionUID = 1;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private Map map;
	private int position;
	private boolean horizontal;
	private int direction;

	/**
	 * Constructor
	 * 
	 * @param isHorizontal <code>true</code> if the guide is horizontal (i.e.,
	 *                     placed on a vertical ruler)
	 */
	public EditorGuide(boolean isHorizontal, int direction) {
		setHorizontal(isHorizontal);
		setDirection(direction);
	}

	/**
	 * @see PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * This methods returns the edge along which the given part is attached to this
	 * guide. This information is used by
	 * {@link org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy
	 * LogicXYLayoutEditPolicy} to determine whether to attach or detach a part from
	 * a guide during resize operations.
	 * 
	 * @param part The part whose alignment has to be found
	 * @return an int representing the edge along which the given part is attached
	 *         to this guide; 1 is bottom or right; 0, center; -1, top or left; -2
	 *         if the part is not attached to this guide
	 * @see org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest,
	 *      EditPart, Object)
	 */
	public int getAlignment(EditorRuler part) {
		if (getMap().get(part) != null)
			return ((Integer) getMap().get(part)).intValue();
		return -2;
	}

	/**
	 * @return The Map containing all the parts attached to this guide, and their
	 *         alignments; the keys are LogicSubparts and values are Integers
	 */
	public Map getMap() {
		if (map == null) {
			map = new Hashtable();
		}
		return map;
	}

	/**
	 * @return the set of all the parts attached to this guide; a set is used
	 *         because a part can only be attached to a guide along one edge.
	 */
	public Set getParts() {
		return getMap().keySet();
	}

	/**
	 * @return the position/location of the guide (in pixels)
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return <code>true</code> if the guide is horizontal (i.e., placed on a
	 *         vertical ruler)
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the orientation of the guide
	 * 
	 * @param isHorizontal <code>true</code> if this guide is to be placed on a
	 *                     vertical ruler
	 */
	public void setHorizontal(boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	/**
	 * Sets the location of the guide
	 * 
	 * @param offset The location of the guide (in pixels)
	 */
	public void setPosition(int offset) {
		if (position != offset) {
			int oldValue = position;
			position = offset;
			listeners.firePropertyChange(PROPERTY_POSITION, Integer.valueOf(oldValue), Integer.valueOf(position));
		}
	}

	/**
	 * @return Returns the direction.
	 */
	public int getDirection() {
		return direction;
	}

	/**
	 * @param direction The direction to set.
	 */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getPropertyName() {
		if (getDirection() == LEFT && !isHorizontal()) {
			return MasterPageHandle.LEFT_MARGIN_PROP;
		} else if (getDirection() == RIGHT && !isHorizontal()) {
			return MasterPageHandle.RIGHT_MARGIN_PROP;
		} else if (getDirection() == LEFT && isHorizontal()) {
			return MasterPageHandle.TOP_MARGIN_PROP;
		} else if (getDirection() == RIGHT && isHorizontal()) {
			return MasterPageHandle.BOTTOM_MARGIN_PROP;
		}
		return null;
	}

	public String getPrefixLabel() {
		if (getDirection() == LEFT && !isHorizontal()) {
			return Messages.getString("EditorGuide.left.label"); //$NON-NLS-1$
		} else if (getDirection() == RIGHT && !isHorizontal()) {
			return Messages.getString("EditorGuide.right.label"); //$NON-NLS-1$
		} else if (getDirection() == LEFT && isHorizontal()) {
			return Messages.getString("EditorGuide.top.label"); //$NON-NLS-1$
		} else if (getDirection() == RIGHT && isHorizontal()) {
			return Messages.getString("EditorGuide.bottom.label"); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}
}
