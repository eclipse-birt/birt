/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * Editor ruler.
 *
 */
public class EditorRuler {

	private Rectangle leftSpace = new Rectangle();
	public static final String PROPERTY_CHILDREN = "children changed"; //$NON-NLS-1$
	public static final String PROPERTY_UNIT = "units changed"; //$NON-NLS-1$
	public static final String PROPERTY_LEFTMARGIN = "left margin"; //$NON-NLS-1$
	public static final String PROPERTY_RIGHTMARGIN = "right margin"; //$NON-NLS-1$

	static final long serialVersionUID = 1;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	private int unit;
	private boolean horizontal;
	private List guides = new ArrayList();

	private EditorGuide left, right;

	private boolean marginOff = false;

	/**
	 * Returns the listeners.
	 *
	 * @return Returns the listeners.
	 */
	public PropertyChangeSupport getListeners() {
		return listeners;
	}

	/**
	 * Sets the listeners.
	 *
	 * @param listeners The listeners to set.
	 */
	public void setListeners(PropertyChangeSupport listeners) {
		this.listeners = listeners;
	}

	/**
	 * Returns the right margin.
	 *
	 * @return Returns the rightMargin.
	 */
	public int getRightMargin() {
		if (right != null) {
			return right.getPosition();
		}
		return -1;
	}

	/**
	 * @param rightMargin The rightMargin to set.
	 */
	public void setRightMargin(int newMargin) {
		if (!marginOff) {
			if (right == null) {
				right = new EditorGuide(!isHorizontal(), EditorGuide.RIGHT);
				right.setPosition(newMargin);
				addGuide(right);
			} else {
				right.setPosition(newMargin);
			}
		}
	}

	/**
	 * The constructor.
	 *
	 * @param isHorizontal
	 */
	public EditorRuler(boolean isHorizontal) {
		this(isHorizontal, RulerProvider.UNIT_INCHES);
	}

	/**
	 * The constructor.
	 *
	 * @param isHorizontal
	 * @param unit
	 */
	public EditorRuler(boolean isHorizontal, int unit) {
		horizontal = isHorizontal;
		setUnit(unit);
	}

	/**
	 * Adds ruler guide.
	 *
	 * @param guide
	 */
	public void addGuide(EditorGuide guide) {
		if (!guides.contains(guide)) {
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	/**
	 * Adds the property listener.
	 *
	 * @param listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * Returns the ruler guides.the returned list should not be modified.
	 *
	 * @return
	 */
	public List getGuides() {
		return guides;
	}

	/**
	 * Returns the ruler unit.
	 *
	 * @return
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * Returns if the ruler is hidden.
	 *
	 * @return
	 */
	public boolean isHidden() {
		return false;
	}

	/**
	 * Returns if the ruler is horizontal.
	 *
	 * @return
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * Removes the guide from the ruler.
	 *
	 * @param guide
	 */
	public void removeGuide(EditorGuide guide) {
		if (guides.remove(guide)) {
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	/**
	 * Remove the property listener.
	 *
	 * @param listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets if display the margin guide.
	 *
	 * @param marginOff
	 */
	public void setMarginOff(boolean marginOff) {
		this.marginOff = marginOff;

		if (marginOff) {
			removeGuide(left);
			removeGuide(right);

			left = null;
			right = null;
		}
	}

	/**
	 * Sets the hidden property of the ruler.
	 *
	 * @param isHidden
	 */
	public void setHidden(boolean isHidden) {
	}

	/**
	 * Sets the ruler unit.
	 *
	 * @param newUnit
	 */
	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
		}
	}

	/**
	 * Returns the left margin.
	 *
	 * @return Returns the leftMargin.
	 */
	public int getLeftMargin() {
		if (left != null) {
			return left.getPosition();
		}
		return -1;
	}

	/**
	 * Sets the left magin of the ruler.
	 *
	 * @param leftMargin The leftMargin to set.
	 */
	public void setLeftMargin(int newMargin) {
		if (!marginOff) {
			if (left == null) {
				left = new EditorGuide(!isHorizontal(), EditorGuide.LEFT);
				left.setPosition(newMargin);
				addGuide(left);
			} else {
				left.setPosition(newMargin);
			}
		}
	}

	/**
	 * @param leftSpace The leftSpace to set.
	 */
	public void setLeftSpace(Rectangle space) {
		if (!leftSpace.equals(space)) {
			Rectangle oldSpace = leftSpace;
			leftSpace = space;
			listeners.firePropertyChange(PROPERTY_UNIT, oldSpace, space);
		}
	}

	/**
	 * @return
	 */
	public Rectangle getLeftSpace() {
		return leftSpace;
	}

	/**
	 * Change the drag guide
	 */
	public void changeDragGuide(int position, boolean horizontal) {
		DragEditorGuide dragGuide = findDragEditorGuide();
		if (position <= 0) {
			if (dragGuide != null) {
				removeGuide(dragGuide);
			}
		} else if (dragGuide != null) {
			dragGuide.setPosition(position);
		} else {
			dragGuide = new DragEditorGuide(horizontal);
			dragGuide.setPosition(position);
			addGuide(dragGuide);
		}
	}

	private DragEditorGuide findDragEditorGuide() {
		for (int i = 0; i < guides.size(); i++) {
			if (guides.get(i) instanceof DragEditorGuide) {
				return (DragEditorGuide) guides.get(i);
			}
		}
		return null;
	}
}
