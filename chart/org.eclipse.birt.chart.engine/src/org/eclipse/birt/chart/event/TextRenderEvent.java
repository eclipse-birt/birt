/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.layout.LabelBlock;

/**
 * A rendering event type for rendering text object.
 */
public class TextRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = 1683131722588162319L;

	/**
	 * An undefined action that will result in an error
	 */
	public static final int UNDEFINED = 0;

	/**
	 * Renders a shadow offset with the encapsulating container rectangle's corner
	 * or edge aligned with a given point
	 * 
	 * This action requires Label, Location, TextPosition to be set
	 */
	public static final int RENDER_SHADOW_AT_LOCATION = 1;

	/**
	 * Renders text (with optional insets, border, fill, etc) with the encapsulating
	 * container rectangle's corner or edge aligning with a given point
	 * 
	 * This action requires Label, Location, TextPosition to be set
	 */
	public static final int RENDER_TEXT_AT_LOCATION = 2;

	/**
	 * Renders text (with optional insets, border, fill, etc) with the encapsulating
	 * container rectangle's bounding box aligned with a parent block's bounds
	 * 
	 * This action requires Label, BlockBounds, BlockAlignment to be set
	 */
	public static final int RENDER_TEXT_IN_BLOCK = 3;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that the
	 * text is positioned to the left of the reference point 'Location'
	 */
	public static final int LEFT = IConstants.LEFT;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that the
	 * text is positioned to the right of the reference point 'Location'
	 */
	public static final int RIGHT = IConstants.RIGHT;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that the
	 * text is positioned above the reference point 'Location'
	 */
	public static final int ABOVE = IConstants.ABOVE;

	/**
	 * A constant used with the 'TextPosition' attribute. This indicates that the
	 * text is positioned below the reference point 'Location'
	 */
	public static final int BELOW = IConstants.BELOW;

	/**
	 * The bounds of the enclosing block space in which the text's bounding box will
	 * be aligned
	 */
	protected transient Bounds _boBlock;

	protected transient Label _la;

	protected transient TextAlignment _taBlock;

	protected int _iAction = UNDEFINED;

	protected transient Location _lo;

	protected int _iTextPosition;

	/**
	 * The constructor.
	 */
	public TextRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the block bounds of the text.
	 */
	public final void setBlockBounds(Bounds boBlock) {
		_boBlock = boBlock;
	}

	// bidi_acgc added start
	/**
	 * Adds the "RLE" and "PDF" unicode control characters to label caption where
	 * "RLE" is added to the beginning and "PDF" to the end to apply right to left
	 * reading order
	 */
	public final void setRtlCaption() {
		Label lbl = this.getLabel();
		if (lbl != null) {
			Text txt = lbl.getCaption();
			String val = txt.getValue();
			if (val.length() > 0)
				if ('\u202b' != val.charAt(0))
					txt.setValue('\u202b' + val + '\u202c');
		}
	}

	// bidi_acgc added end
	/**
	 * @return Returns the block bounds of the text.
	 */
	public final Bounds getBlockBounds() {
		return _boBlock;
	}

	/**
	 * Sets the label of the text.
	 */
	public final void setLabel(Label la) {
		_la = la;
	}

	/**
	 * @return Returns the label of the text.
	 */
	public final Label getLabel() {
		return _la;
	}

	/**
	 * Sets the block alignment of the text.
	 */
	public final void setBlockAlignment(TextAlignment taBlock) {
		_taBlock = taBlock;
	}

	/**
	 * @return Returns the block alignment of the text.
	 */
	public final TextAlignment getBlockAlignment() {
		return _taBlock;
	}

	/**
	 * Sets the action of current event. The value must be one of these defined in
	 * this class:
	 * <ul>
	 * <li>{@link #RENDER_TEXT_AT_LOCATION}
	 * <li>{@link #RENDER_TEXT_IN_BLOCK}
	 * <li>{@link #RENDER_SHADOW_AT_LOCATION}
	 * </ul>
	 */
	public final void setAction(int iAction) {
		_iAction = iAction;
	}

	/**
	 * @return Returns the action of current event. The value could be one of these
	 *         defined in this class:
	 *         <ul>
	 *         <li>{@link #RENDER_TEXT_AT_LOCATION}
	 *         <li>{@link #RENDER_TEXT_IN_BLOCK}
	 *         <li>{@link #RENDER_SHADOW_AT_LOCATION}
	 *         <li>{@link #UNDEFINED}
	 *         </ul>
	 */
	public final int getAction() {
		return _iAction;
	}

	/**
	 * Sets the location of the text.
	 */
	public final void setLocation(Location lo) {
		_lo = lo;
	}

	/**
	 * @return Returns the location of the text.
	 */
	public final Location getLocation() {
		return _lo;
	}

	/**
	 * Sets the position of the text. The value must be on of these defined in this
	 * class:
	 * <ul>
	 * <li>{@link #LEFT}
	 * <li>{@link #RIGHT}
	 * <li>{@link #ABOVE}
	 * <li>{@link #BELOW}
	 * </ul>
	 */
	public final void setTextPosition(int iTextPosition) {
		_iTextPosition = iTextPosition;
	}

	/**
	 * @return Returns the position of the text. The value could be on of these
	 *         defined in this class:
	 *         <ul>
	 *         <li>{@link #LEFT}
	 *         <li>{@link #RIGHT}
	 *         <li>{@link #ABOVE}
	 *         <li>{@link #BELOW}
	 *         <li>{@link #UNDEFINED}
	 *         </ul>
	 */
	public final int getTextPosition() {
		return _iTextPosition;
	}

	/**
	 * Updates the event by given LabelBlock object.
	 */
	public final void updateFrom(LabelBlock lb, double dScale, RunTimeContext rtc) {
		setLabel(lb.getLabel());

		Bounds bo = goFactory.scaleBounds(lb.getBounds(), dScale);
		bo.adjust(goFactory.scaleInsets(lb.getInsets(), dScale));

		setBlockBounds(bo);
		setBlockAlignment(lb.getLabel().getCaption().getFont().getAlignment());
		setAction(TextRenderEvent.RENDER_TEXT_IN_BLOCK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		final TextRenderEvent tre = new TextRenderEvent(source);
		tre.setBlockBounds(goFactory.copyOf(_boBlock));
		tre.setAction(_iAction);
		tre.setTextPosition(_iTextPosition);
		if (_la != null) {
			tre.setLabel(goFactory.copyCompactLabel(_la));
		}
		if (_lo != null) {
			tre.setLocation(_lo.copyInstance());
		}
		if (_taBlock != null) {
			tre.setBlockAlignment(goFactory.copyOf(_taBlock));
		}
		return tre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void fill(IDeviceRenderer idr) throws ChartException {
		draw(idr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public final void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawText(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	public void reset() {
		_boBlock = null;
		_la = null;
		_lo = null;
		_taBlock = null;

	}
}
