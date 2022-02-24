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

/**
 * A event type indicates a structure change.
 */
public final class StructureChangeEvent extends ChartEvent {

	private static final long serialVersionUID = -8897456257858266632L;

	public static final int UNDEFINED = 0;

	public static final int BEFORE = 1;

	public static final int AFTER = 2;

	private static final String BEFORE_PREFIX = "before"; //$NON-NLS-1$

	private static final String AFTER_PREFIX = "after"; //$NON-NLS-1$

	private String sEventName = null;

	/**
	 * The constructor.
	 */
	public StructureChangeEvent(Object source) {
		super(source);
	}

	/**
	 * Returns the event name.
	 *
	 * @param bStripType Specifies if the name if striped.
	 * @return
	 */
	public String getEventName(boolean bStripType) {
		if (bStripType) // STRIP OUT THE 'before' OR 'after' PREFIX IF
		// REQUESTED
		{
			int iPrefixLength, iType = getEventType();
			if (iType == BEFORE) {
				iPrefixLength = BEFORE_PREFIX.length();
			} else if (iType == AFTER) {
				iPrefixLength = BEFORE_PREFIX.length();
			} else {
				iPrefixLength = 0;
			}
			return sEventName.substring(iPrefixLength);
		} else {
			return sEventName;
		}
	}

	/**
	 * Sets the event name.
	 *
	 * @param sEventName This must include the 'before' or 'after' prefix as defined
	 *                   by each of the constants in IStructureDefinition
	 */
	public void setEventName(String sEventName) {
		this.sEventName = sEventName;
	}

	/**
	 * A convenience method provided to indicate if the event occurs before the
	 * start of a structure definition or after the end of a structure definition.
	 *
	 * @return An event type indicating BEFORE or AFTER a structure definition
	 */
	public int getEventType() {
		if (sEventName == null) {
			return UNDEFINED;
		} else if (sEventName.startsWith(BEFORE_PREFIX)) {
			return BEFORE;
		} else if (sEventName.startsWith(AFTER_PREFIX)) {
			return AFTER;
		}
		return UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		// NO-OP
	}

}
