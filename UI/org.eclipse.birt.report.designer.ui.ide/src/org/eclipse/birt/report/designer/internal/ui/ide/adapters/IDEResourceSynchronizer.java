/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceSynchronizer;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

/**
 * IDEResourceSynchronizer
 */
public class IDEResourceSynchronizer extends ReportResourceSynchronizer
// implements IResourceChangeListener
{

	// private boolean notifying = false;

	public IDEResourceSynchronizer() {
		super();

		// ResourcesPlugin.getWorkspace( ).addResourceChangeListener( this,
		// org.eclipse.core.resources.IResourceChangeEvent.POST_CHANGE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org
	 * .eclipse.core.resources.IResourceChangeEvent)
	 */
	// public void resourceChanged(
	// org.eclipse.core.resources.IResourceChangeEvent event )
	// {
	// notifyListeners( new ReportResourceChangeEvent( this, event ) );
	// }
	//
	// protected void notifyListeners( final IReportResourceChangeEvent event )
	// {
	// if ( notifying )
	// {
	// return;
	// }
	//
	// super.notifyListeners( event );
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.ReportResourceSynchronizer
	 * #notifyResourceChanged(org.eclipse.birt.report.designer.ui.views.
	 * IReportResourceChangeEvent)
	 */
	public void notifyResourceChanged(IReportResourceChangeEvent event) {
		// notifying = true;

		refreshWorkspace(event);

		// notifying = false;

		notifyListeners(event);
	}

	private void refreshWorkspace(IReportResourceChangeEvent event) {
		if (event != null) {
			Object data = event.getData();

			if (data instanceof IPath) {
				refreshResource((IPath) data);
			} else if (data instanceof IPath[]) {
				// TODO smart detect path overlapping?

				for (IPath path : (IPath[]) data) {
					refreshResource(path);
				}
			}
		}
	}

	private void refreshResource(IPath resPath) {
		IResource[] res = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocation(resPath);

		if (res.length == 0) {
			res = ResourcesPlugin.getWorkspace().getRoot().findContainersForLocation(resPath);

			if (res.length == 0) {
				// not resources within the workspace
				return;
			}
		}

		try {
			final IResource[] targes = res;

			new WorkspaceModifyOperation() {

				protected void execute(IProgressMonitor monitor) throws CoreException {
					for (IResource rc : targes) {
						rc.refreshLocal(IResource.DEPTH_INFINITE, null);
					}
				}
			}.run(null);
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

	}
}
