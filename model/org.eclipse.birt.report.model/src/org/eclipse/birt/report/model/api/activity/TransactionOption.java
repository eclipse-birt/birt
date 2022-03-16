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

package org.eclipse.birt.report.model.api.activity;

/**
 * Class provided to set some options for the transaction. It can set event
 * filter, set event send or hold option and so on.
 */

public class TransactionOption {

	/**
	 * Instantaneous send time option. It means that the notification event will be
	 * send once it is executed/redo/undo, committed/rolled back.
	 */

	public final static int INSTANTANEOUS_SEND_TIME = 0;

	/**
	 * Self transaction send time option. It means that the notification event will
	 * be held until the latest transaction is committed or rolled back.
	 */

	public final static int SELF_TRANSACTION_SEND_TIME = 1;

	/**
	 * Outmost transaction send time option. It means that the notification event
	 * will be held until the outmost transaction is comitted or rolled back.
	 */

	public final static int OUTMOST_TRANSACTION_SEND_TIME = 2;

	/**
	 * The event filter to do some filter operation.
	 */

	IEventFilter eventFilter = null;

	/**
	 * The send time option.
	 */

	int sendTime = INSTANTANEOUS_SEND_TIME;

	/**
	 * Default constructor.
	 */

	public TransactionOption() {

	}

	/**
	 * Gets the event filter set in this option.
	 *
	 * @return the event filter
	 */

	public IEventFilter getEventFilter() {
		return eventFilter;
	}

	/**
	 * Gets the event send option. It can be one of the following options.
	 *
	 * <p>
	 * <li>INSTANTANEOUS_EVENT_SEND
	 * <li>SELF_TRANSACTION_EVENT_SEND
	 * <li>OUTMOST_TRANSACTION_EVENT_SEND
	 *
	 * @return the event send option
	 */

	public int getSendTime() {
		return sendTime;
	}

	/**
	 * @param eventFilter the eventfilter to set
	 */
	public void setEventfilter(IEventFilter eventFilter) {
		this.eventFilter = eventFilter;
	}

	/**
	 * Sets the option to control when to send the event.
	 *
	 * @param eventTime the eventTime to set
	 */

	public void setSendTime(int eventTime) {
		this.sendTime = eventTime;
	}
}
