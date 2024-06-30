/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.mediator;

import java.util.Map;
import java.util.WeakHashMap;

import org.eclipse.birt.report.designer.core.mediator.IMediatorTarget.ITargetDisposeListener;
import org.eclipse.birt.report.designer.core.mediator.impl.MediatorImpl;

/**
 * MediatorManager
 */
public final class MediatorManager {

	private static volatile MediatorManager manager;

	private Map<IMediatorTarget, IMediator> mediatorMap = new WeakHashMap<>();

	private ITargetDisposeListener listener = new ITargetDisposeListener() {

		@Override
		public void dispose(IMediatorTarget target) {
			removeMediator(target);
		}
	};

	public synchronized static MediatorManager getInstance() {
		if (manager == null) {
			manager = new MediatorManager();
		}
		return manager;
	}

	public static void addGlobalColleague(IMediatorColleague colleague) {
		MediatorImpl.addGlobalColleague(colleague);
	}

	public static void removeGlobalColleague(IMediatorColleague colleague) {
		MediatorImpl.removeGlobalColleague(colleague);
	}

	/**
	 * Returns the mediator for given target, if the mediator does not exist and
	 * "force" is True, registers and returns a new one automatically, otherwise,
	 * returns null.
	 *
	 * @param target
	 * @param force
	 * @return
	 */
	public IMediator getMediator(IMediatorTarget target, boolean force) {
		if (target == null) {
			return null;
		}

		IMediator mediator = mediatorMap.get(target);

		if (mediator == null && force) {
			mediator = new MediatorImpl();
			mediatorMap.put(target, mediator);
			target.addDisposeListener(listener);
		}

		return mediator;
	}

	/**
	 * Removes the mediator for the given target if exists.
	 *
	 * @param target
	 */
	public void removeMediator(IMediatorTarget target) {
		if (target == null) {
			return;
		}

		target.removeDisposeListener(listener);
		IMediator mediator = mediatorMap.remove(target);
		if (mediator != null) {
			mediator.dispose();
		}
	}

	/**
	 * Reassociates the mediator of the old target to the new target, if it exists.
	 *
	 * @param oldTarget
	 * @param newTarget
	 */
	public void resetTarget(IMediatorTarget oldTarget, IMediatorTarget newTarget) {
		if (oldTarget == null || newTarget == null) {
			return;
		}

		IMediator mediator = mediatorMap.get(oldTarget);
		if (mediator == null) {
			return;
		}

		oldTarget.removeDisposeListener(listener);
		mediatorMap.remove(oldTarget);
		newTarget.addDisposeListener(listener);
		mediatorMap.put(newTarget, mediator);
	}
}
