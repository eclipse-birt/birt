/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.text.MessageFormat;
import java.util.Vector;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

/**
 * This class is responsible for listening on the chart model for changes made
 * to it in the UI. Once such a change notification is received, it is
 * responsible for notifying all registered IChangeListener instances after
 * filtering notification events as necessary.
 * 
 * @author Actuate Corporation
 * 
 */
public class ChartAdapter extends EContentAdapter {

	private Vector<ITaskChangeListener> vListeners = new Vector<ITaskChangeListener>();

	// For use by sample series creation
	private static boolean bIgnoreNotifications = false;

	// Indicates Apply button needs updating when notify changed
	private static boolean needUpdateApply = false;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui/swt"); //$NON-NLS-1$

	private transient WizardBase wizardContainer;

	public ChartAdapter(WizardBase wizardContainer) {
		this.wizardContainer = wizardContainer;
	}

	public void notifyChanged(Notification notification) {
		if (needUpdateApply && wizardContainer instanceof ChartWizard) {
			// Update Apply button status when notification may be ignored.
			((ChartWizard) wizardContainer).updateApplyButton();
		}

		if (bIgnoreNotifications || notification.isTouch()) {
			needUpdateApply = false;
			return;
		}

		logger.log(ILogger.INFORMATION, new MessageFormat(Messages.getString("ChartAdapter.Info.NotificationRecieved")) //$NON-NLS-1$
				.format(new Object[] { notification.getNotifier().getClass().getName() }));
		logger.log(ILogger.INFORMATION, new MessageFormat(Messages.getString("ChartAdapter.Info.NewValue")) //$NON-NLS-1$
				.format(new Object[] { notification.getNewValue() }));

		// Notify registered change listeners
		for (int iC = 0; iC < vListeners.size(); iC++) {
			ITaskChangeListener changeLs = vListeners.elementAt(iC);
			// Only change current task
			if (wizardContainer.getCurrentTask() == changeLs
					|| !(wizardContainer.getCurrentTask() instanceof ITaskChangeListener)) {
				changeLs.changeTask(notification);
			}
		}

		if (!needUpdateApply && wizardContainer instanceof ChartWizard) {
			// Update Apply button status after notification
			((ChartWizard) wizardContainer).updateApplyButton();
		} else {
			needUpdateApply = false;
		}
	}

	public static void notifyUpdateApply() {
		needUpdateApply = true;
	}

	public static void ignoreNotifications(boolean bIgnoreNotifications) {
		ChartAdapter.bIgnoreNotifications = bIgnoreNotifications;
	}

	public static boolean isNotificationIgnored() {
		return ChartAdapter.bIgnoreNotifications;
	}

	public static void beginIgnoreNotifications() {
		bIgnoreNotifications = true;
	}

	public static void endIgnoreNotifications() {
		bIgnoreNotifications = false;
	}

	public void addListener(ITaskChangeListener listener) {
		if (!vListeners.contains(listener)) {
			vListeners.add(listener);
		}
	}

	public void clearListeners() {
		if (vListeners != null) {
			vListeners.removeAllElements();
		}
	}
}