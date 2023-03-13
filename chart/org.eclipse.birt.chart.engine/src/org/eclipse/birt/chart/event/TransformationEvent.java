/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.IConstants;

/**
 * An event type for transformation.
 */
public final class TransformationEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -2322114654814388838L;

	/**
	 * Indicates the transformation type is undefined.
	 */
	public static final int UNDEFINED = IConstants.UNDEFINED;

	/**
	 * Indicates this is a scaling transformation.
	 */
	public static final int SCALE = 1;

	/**
	 * Indicates this is a tranlating transformatino.
	 */
	public static final int TRANSLATE = 2;

	/**
	 * Indicates this is a rotating transformation.
	 */
	public static final int ROTATE = 4;

	private int _iTransform = UNDEFINED;

	private double _dScale = 1.0;

	private double _dTranslateX = 0;

	private double _dTranslateY = 0;

	private double _dRotationInDegrees = 0;

	/**
	 * The constructor.
	 */
	public TransformationEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the transformation type of this event. The value must be one of these
	 * defined in this class:
	 * <ul>
	 * <li>{@link #SCALE}
	 * <li>{@link #TRANSLATE}
	 * <li>{@link #ROTATE}
	 * </ul>
	 */
	public void setTransform(int iTransform) {
		_iTransform = iTransform;
	}

	/**
	 *
	 * @return Returns the transformation type of this event. The value could be one
	 *         of these defined in this class:
	 *         <ul>
	 *         <li>{@link #SCALE}
	 *         <li>{@link #TRANSLATE}
	 *         <li>{@link #ROTATE}
	 *         <li>{@link #UNDEFINED}
	 *         </ul>
	 */
	public int getTransform() {
		return _iTransform;
	}

	/**
	 * Sets the scale of current event.
	 */
	public void setScale(double dScale) {
		_dScale = dScale;
	}

	/**
	 * @return Returns the scale of current event.
	 */
	public double getScale() {
		return _dScale;
	}

	/**
	 * Sets the translation of current event.
	 */
	public void setTranslation(double dTranslateX, double dTranslateY) {
		_dTranslateX = dTranslateX;
		_dTranslateY = dTranslateY;
	}

	/**
	 * @return Returns the X translation of current event.
	 */
	public double getTranslateX() {
		return _dTranslateX;
	}

	/**
	 * @return Returns the Y tranlation of current event.
	 */
	public double getTranslateY() {
		return _dTranslateY;
	}

	/**
	 * Sets the rotation of current event.
	 */
	public void setRotation(double dAngleInDegrees) {
		_dRotationInDegrees = dAngleInDegrees;
	}

	/**
	 * @return Returns the rotation of current event.
	 */
	public double getRotation() {
		return _dRotationInDegrees;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		// no-op
	}
}
