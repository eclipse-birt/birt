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

package org.eclipse.birt.report.designer.internal.ui.layout;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author David Michonneau
 *
 */
public class ReportItemConstraint extends Rectangle {

	private static final long serialVersionUID = 1L;

	public static final int NONE = 0;

	public static final int INLINE = 1;

	public static final int BLOCK = 2;

	// private int m_display = NONE;
	private int m_display = BLOCK;

	private Insets m_margins = null;

	private double measure = 0;
	private String units = ""; //$NON-NLS-1$

	private boolean isFitTable = false;

	private int align = ReportFlowLayout.ALIGN_RIGHTBOTTOM;

	public int getAlign() {
		return align;
	}

	public void setAlign(int align) {
		this.align = align;
	}

	/**
	 * @return
	 */
	public boolean isFitContiner() {
		return isFitTable;
	}

	/**
	 * @param isFitTable
	 */
	public void setFitTable(boolean isFitTable) {
		this.isFitTable = isFitTable;
	}

	/**
	 * @return
	 */
	public double getMeasure() {
		return measure;
	}

	/**
	 * @param measure
	 */
	public void setMeasure(double measure) {
		this.measure = measure;
	}

	/**
	 * @return
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * @param units
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	public int getDisplay() {
		return m_display;
	}

	/**
	 * @param isInline The isInline to set.
	 */
	public void setDisplay(String display) {
		if (display.equals(DesignChoiceConstants.DISPLAY_INLINE)) {
			m_display = INLINE;
//		else if ( display.equals( DesignChoiceConstants.DISPLAY_BLOCK ) )
//			m_display = BLOCK;
		} else {
			m_display = BLOCK;
		}
	}

	/**
	 * @return true if the element display is inline
	 */
	public boolean isInline() {
		return m_display == INLINE;
	}

	/**
	 * @return true if the element display is block
	 */
	public boolean isBlock() {
		return m_display == BLOCK;
	}

	/**
	 * @return true if the display is none
	 */
	public boolean isNone() {
		return m_display == NONE;
	}

	/**
	 * @return the margin of the element
	 */
	public Insets getMargin() {
		return m_margins;
	}

	/**
	 * Set the margin of this element
	 *
	 * @param margin
	 */
	public void setMargin(final Insets margin) {
		m_margins = margin;
	}
}
