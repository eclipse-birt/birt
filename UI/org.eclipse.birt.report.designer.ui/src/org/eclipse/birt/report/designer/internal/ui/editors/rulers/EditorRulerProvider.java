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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.MoveGuideCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * This class represents a ruler and provides the necessary information about
 * them.
 */
public class EditorRulerProvider extends RulerProvider {

	public static final int UNIT_NOSUPPOER = -1;
	public static final int UNIT_MM = 4;
	public static final int UNIT_PT = 5;
	public static final int UNIT_PC = 6;

	protected PropertyChangeListener rulerListener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(EditorRuler.PROPERTY_CHILDREN)) {
				EditorGuide guide = (EditorGuide) evt.getNewValue();
				if (getGuides().contains(guide)) {
					guide.addPropertyChangeListener(guideListener);
				} else {
					guide.removePropertyChangeListener(guideListener);
				}
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyGuideReparented(guide);
				}
			} else {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyUnitsChanged(ruler.getUnit());
				}
			}
		}
	};

	protected PropertyChangeListener guideListener = new PropertyChangeListener() {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(EditorGuide.PROPERTY_CHILDREN)) {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyPartAttachmentChanged(evt.getNewValue(),
							evt.getSource());
				}
			} else {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyGuideMoved(evt.getSource());
				}
			}
		}
	};

	protected EditorRuler ruler = null;

	protected Rectangle layoutSize = null;

	protected EditorRulerProvider() {
	}

	/**
	 * The constructor
	 * 
	 * @param handle
	 */
	public EditorRulerProvider(ModuleHandle handle, boolean isHorizontal) {
		this.ruler = new EditorRuler(isHorizontal);
		this.ruler.addPropertyChangeListener(rulerListener);
		List guides = getGuides();
		for (int i = 0; i < guides.size(); i++) {
			((EditorGuide) guides.get(i)).addPropertyChangeListener(guideListener);
		}

		initLayoutSize(handle);
	}

	/**
	 * Return null command to forbid create guide beHavior (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getCreateGuideCommand(int)
	 */
	public Command getCreateGuideCommand(int position) {
		return null;
	}

	/**
	 * Return null command to forbid delete guide beHavior (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getCreateGuideCommand(int)
	 */

	public Command getDeleteGuideCommand(Object guide) {
		return null;
	}

	/**
	 * Sets the actual layout size.
	 * 
	 * @param rct
	 */
	public void setLayoutSize(Rectangle rct) {
		this.layoutSize = rct;
	}

	protected void initLayoutSize(ModuleHandle module) {
		if (module != null) {
			Dimension dim = EditorRulerComposite
					.getMasterPageSize(SessionHandleAdapter.getInstance().getFirstMasterPageHandle(module));

			layoutSize = new Rectangle(0, 0, dim.width, dim.height);
		}
	}

	/**
	 * Returns the current layout size.
	 * 
	 * @return
	 */
	private Rectangle getLayoutSize() {
		if (layoutSize == null) {
			return new Rectangle(0, 0, 0, 0);
		}

		return this.layoutSize;
	}

	/**
	 * Return null command to forbid move guide beHavior (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getCreateGuideCommand(int)
	 */
	public Command getMoveGuideCommand(Object obj, int pDelta) {
		EditorGuide guide = (EditorGuide) obj;
		// String propertyName = guide.getPropertyName( );
		pDelta = getMarginValue(obj, pDelta);

		return new MoveGuideCommand(pDelta, guide.getPropertyName());
	}

	public int getMarginValue(Object obj, int pDelta) {
		EditorGuide guide = (EditorGuide) obj;
		String propertyName = guide.getPropertyName();
		if (MasterPageHandle.RIGHT_MARGIN_PROP.equals(propertyName)) {
			pDelta = getLayoutSize().right() - (guide.getPosition() + pDelta);
		} else if (MasterPageHandle.BOTTOM_MARGIN_PROP.equals(propertyName)) {
			pDelta = getLayoutSize().bottom() - (guide.getPosition() + pDelta);
		} else if (MasterPageHandle.LEFT_MARGIN_PROP.equals(propertyName)) {
			pDelta = guide.getPosition() + pDelta - getLeftSpace().x;
		} else {
			pDelta = guide.getPosition() + pDelta - getLeftSpace().y;
		}

		return pDelta;
	}

	public String getPrefixLabel(Object obj) {
		EditorGuide guide = (EditorGuide) obj;
		return guide.getPrefixLabel();
	}

	/**
	 * Returns the ruler model.
	 * 
	 * @return
	 */
	public Object getModel() {
		return getRuler();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getUnit()
	 */
	public int getUnit() {
		return ruler.getUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#setUnit(int)
	 */
	public void setUnit(int newUnit) {
		ruler.setUnit(newUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getRuler()
	 */
	public Object getRuler() {
		return ruler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuidePosition(java.lang.Object)
	 */
	public int getGuidePosition(Object guide) {
		return ((EditorGuide) guide).getPosition();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuides()
	 */
	public List getGuides() {
		return ruler.getGuides();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.rulers.RulerProvider#getGuidePositions()
	 */
	public int[] getGuidePositions() {
		List guides = getGuides();
		int[] result = new int[guides.size()];
		for (int i = 0; i < guides.size(); i++) {
			result[i] = ((EditorGuide) guides.get(i)).getPosition();
		}
		return result;
	}

	/**
	 * @param leftSpace The leftSpace to set.
	 */
	public void setLeftSpace(Rectangle space) {
		ruler.setLeftSpace(space);
	}

	/**
	 * @return
	 */
	public Rectangle getLeftSpace() {
		return ruler.getLeftSpace();
	}
}