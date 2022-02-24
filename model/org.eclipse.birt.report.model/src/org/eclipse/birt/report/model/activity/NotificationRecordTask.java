/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.Stack;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.TransactionOption;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;

/**
 * The task to send notifications to corresponding element after the execution
 * of records.
 * 
 */

public class NotificationRecordTask extends RecordTask {

	/**
	 * Root element.
	 */

	private Module root = null;

	/**
	 * The wrappered notification event.
	 */

	private NotificationEvent event = null;

	/**
	 * A flag used when we need to filter a list of events. Indicating whether or
	 * not a event needs to be filtered.
	 * 
	 */

	private boolean filtered = false;

	/**
	 * Constructs a new event wrapper, <code>target.broadcast( event, root )</code>
	 * will be called when the notification event is sent.
	 * 
	 * @param target Target element where the event will be sent to.
	 * @param event  The wrappered notification event.
	 */

	public NotificationRecordTask(DesignElement target, NotificationEvent event) {
		super(target);

		this.event = event;
		this.root = target.getRoot();
	}

	/**
	 * Constructs a new Notification instance. Sometimes the target element is
	 * dropped from the design tree when the notification is sent. So original root
	 * element should be specified explicitly.
	 * <code>target.broadcast( event, root )</code> will be called when the
	 * notification event is sent.
	 * 
	 * @param target      Target element where the event will be sent to.
	 * @param event       The wrappered notification event.
	 * @param rootElement the root node of the design tree.
	 */

	public NotificationRecordTask(DesignElement target, NotificationEvent event, Module rootElement) {
		super(target);

		this.event = event;
		this.root = rootElement;
	}

	/**
	 * Constructs a new event wrapper, <code>target.broadcast( event )</code> will
	 * be called when the notification is sent.
	 * 
	 * @param target Target element where the event will be sent to.
	 * @param event  The wrappered notification event.
	 */

	public NotificationRecordTask(ReferencableStructure target, NotificationEvent event) {
		super(target);
		this.event = event;
	}

	/**
	 * Sets the filter flag, indicating whether or not the event will be filtered.
	 * 
	 * @param filtered a filter flag, indicating whether or not the event will be
	 *                 filtered.
	 */

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	/**
	 * Returns the flag indication whether or not the event will be filtered.
	 * 
	 * @return The flag indication whether or not the event will be filtered.
	 */

	public boolean isFiltered() {
		return filtered;
	}

	/**
	 * Returns the wrappered notification event.
	 * 
	 * @return the wrappered notification event.
	 */

	public NotificationEvent getEvent() {
		return event;
	}

	/**
	 * Returns <code>true</code> if need to hold the event at this time. We need to
	 * hold the event if it is sent inside a transaction that declared to filter
	 * notification events( <code>FilterEventsCompoundRecord</code>).
	 * 
	 * @param transStack the transaction stack.
	 * @return <code>true</code> if need to hold the event at this time, returns
	 *         <code>false</code> otherwise.
	 */

	protected final boolean holdNotificationForFilterEventRecord(Stack<CompoundRecord> transStack) {
		if (transStack != null && !transStack.isEmpty()) {
			CompoundRecord cr = transStack.peek();
			if (cr instanceof FilterEventsCompoundRecord)
				return true;
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if needs to hold the event at this time. We need to
	 * hold the event if transaction option sets the event send to
	 * <code>SELF_TRANSACTION_EVENT_SEND</code> or
	 * <code>OUTMOST_TRANSACTION_EVENT_SEND</code>.
	 * 
	 * @param record     the activity record to justify
	 * @param transStack the transaction stack status
	 * @return true if needs to hold the event at this time, otherwise false
	 */

	protected final boolean holdNotification(ActivityRecord record, Stack<CompoundRecord> transStack) {
		if (record instanceof AbstractElementRecord) {
			if (transStack != null && !transStack.isEmpty()) {
				// if the record is a simple one, then peek the nearest
				// transaction in the stack and see the options

				CompoundRecord cr = transStack.peek();
				TransactionOption options = cr.getOptions();
				if (options != null && options.getSendTime() != TransactionOption.INSTANTANEOUS_SEND_TIME) {
					return true;
				}
			}
		} else if (record instanceof CompoundRecord) {
			CompoundRecord cr = (CompoundRecord) record;
			TransactionOption options = cr.getOptions();
			if (options != null) {
				// if the event send equals 'outmost' and transaction stack is
				// not empty, then hold the event

				if (options.getSendTime() == TransactionOption.OUTMOST_TRANSACTION_SEND_TIME && transStack != null
						&& !transStack.isEmpty())
					return true;
			} else {
				// out transaction sets the event hold, then return true

				if (transStack != null && !transStack.isEmpty()) {
					for (int i = transStack.size() - 1; i >= 0; i--) {
						CompoundRecord trans = transStack.get(i);
						options = trans.getOptions();
						if (options != null && options.getSendTime() != TransactionOption.INSTANTANEOUS_SEND_TIME)
							return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.IInterceptorTask#doTask(org
	 * .eclipse.birt.report.model.activity.ActivityRecord)
	 */

	public void doTask(ActivityRecord record, Stack<CompoundRecord> transStack) {
		// check there is filterEventCompoundRecord, if yes, the notifications
		// will not be send

		if (holdNotificationForFilterEventRecord(transStack))
			return;

		// check for the normal case to determine whether to hold the
		// notifications

		if (!(record instanceof FilterEventsCompoundRecord) && holdNotification(record, transStack))
			return;

		if (getTarget() instanceof DesignElement) {
			// case 1:
			// StyleHandle label = factory.newStyle( label );
			// label.setProperty();
			// root == null, should not broadcast events.

			// case 2:
			// dataSetHandle.setDataSource( dataSourceHandle );
			// dataSourceHandle.drop();
			//
			// Datasource not on the tree, its root is explicitly specified.
			// event should be broadcast.

			DesignElement elementTarget = (DesignElement) getTarget();
			Module theRoot = this.root == null ? elementTarget.getRoot() : this.root;

			if (!filtered && theRoot != null)
				elementTarget.broadcast(event, theRoot);

			if (event instanceof ElementDeletedEvent) {
				elementTarget.clearListeners();
			}
		} else if (getTarget() instanceof ReferencableStructure) {
			if (!filtered)
				((ReferencableStructure) getTarget()).broadcast(event);
		} else
			assert false;
	}
}
