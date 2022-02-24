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

package org.eclipse.birt.report.model.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.command.ContentElementInfo;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.Hierarchy;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;

/**
 * Convenience class to automate routine records that work directly with a
 * design element. Execute, undo and redo call the {@link #perform perform( )}
 * method, making it easy to implement simple operations, especially when a
 * single record implements two different related operations (such as add and
 * delete).
 * <p>
 * Derived commands that must create a "memento" record the initial state should
 * do so in the constructor. This means that the constructor should gather all
 * the data needed to perform the record.
 * 
 */

public abstract class SimpleRecord extends AbstractElementRecord {

	/**
	 * The destination of the event.
	 */

	protected ContentElementInfo eventTarget;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#execute()
	 */

	public void execute() {
		perform(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#undo()
	 */

	public void undo() {
		perform(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#redo()
	 */

	public void redo() {
		perform(false);
	}

	/**
	 * Performs the actual operation.
	 * 
	 * @param undo whether to undo (true) or execute/redo (false) the operation.
	 */

	protected abstract void perform(boolean undo);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#rollback()
	 */

	public void rollback() {
		undo();
		setState(ActivityRecord.UNDONE_STATE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityRecord#getPostTasks()
	 */

	protected List<RecordTask> getPostTasks() {
		DesignElement element = getTarget();
		assert element != null;
		if (element.getRoot() == null)
			return Collections.emptyList();

		List<RecordTask> retList = new ArrayList<RecordTask>();
		retList.addAll(super.getPostTasks());
		retList.add(new ValidationRecordTask(element.getRoot()));
		return retList;
	}

	/**
	 * Sets the event destination. This is used when the command need to specify
	 * what event should be sent out.
	 * 
	 * @param eventTarget the target
	 */

	public void setEventTarget(ContentElementInfo eventTarget) {
		this.eventTarget = eventTarget;
	}

	protected void updateSharedDimension(Module module, DesignElement target) {
		DesignElement container = target;
		if (container instanceof Dimension || container instanceof Hierarchy || container instanceof Level) {
			while (container != null) {
				if (!(container instanceof Dimension)) {
					container = container.getContainer();
					continue;
				}

				Dimension dimension = (Dimension) container;
				if (dimension.getContainer() instanceof Module) {
					List<BackRef> refList = dimension.getClientList();
					if (refList != null) {
						for (BackRef ref : refList) {
							DesignElement client = ref.getElement();
							String propName = ref.getPropertyName();
							if (client instanceof TabularDimension
									&& ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(propName)) {
								((TabularDimension) client).updateLayout(module);
							}
						}
					}
				}

				break;

			}
		}
	}

	protected void sendEventToSharedDimension(DesignElement target, List<RecordTask> retValue,
			NotificationEvent event) {
		DesignElement e = target;
		if (e instanceof Dimension || e instanceof Hierarchy || e instanceof Level) {
			while (e != null) {
				if (!(e instanceof Dimension)) {
					e = e.getContainer();
					continue;
				}

				Dimension shareDimension = (Dimension) e;
				if (shareDimension.getContainer() instanceof Module) {
					List<BackRef> refList = shareDimension.getClientList();
					if (refList != null) {
						for (BackRef ref : refList) {
							DesignElement client = ref.getElement();
							String propName = ref.getPropertyName();
							if (client instanceof TabularDimension
									&& ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(propName)) {
								retValue.add(new NotificationRecordTask(client, event));
							}
						}
					}
				}
				break;

			}
		}
	}

}
