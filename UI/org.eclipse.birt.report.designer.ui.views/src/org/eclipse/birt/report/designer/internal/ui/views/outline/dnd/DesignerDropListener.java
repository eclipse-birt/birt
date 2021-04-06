/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.dnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.runtime.GUIException;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Supports dropping elements to designer outline view.
 */

public class DesignerDropListener extends DesignElementDropAdapter {

	private int canContain;

	private Object newTarget;

	private Map dropConstraintMap;

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public DesignerDropListener(TreeViewer viewer) {
		super(viewer);
		canContain = DNDUtil.CONTAIN_NO;
	}

	/**
	 * Validates target elements can be dropped
	 * 
	 * @param target target elements
	 * @return if target elements can be dropped
	 */
	protected boolean validateTarget(Object target) {
		return true;
	}

	/**
	 * Validates target elements can be dropped, needs to compare with transfer data
	 * 
	 * @param target   target elements
	 * @param transfer transfer data
	 * @return if target elements can be dropped
	 */
	protected boolean validateTarget(Object target, Object transfer) {
		if (DNDService.getInstance().validDrop(transfer, target, getCurrentOperation(),
				new DNDLocation(getCurrentLocation()))) {
			return true;
		}

		if (target != null) {

			List transferDropConstraintList = getDropConstraintList(target.getClass());
			for (Iterator iter = transferDropConstraintList.iterator(); iter.hasNext();) {
				IDropConstraint constraint = (IDropConstraint) iter.next();
				switch (constraint.validate(transfer, target)) {
				case IDropConstraint.RESULT_YES:
					return true;
				case IDropConstraint.RESULT_NO:
					return false;
				default:
					break;
				}
			}
		}

		// if ( target instanceof ReportElementModel )
		// {
		// ReportElementModel model = (ReportElementModel) target;
		// if ( model.getSlotId( ) == ModuleHandle.DATA_SET_SLOT)
		// return false;
		// }
		// else if ( target instanceof DataSetHandle || target instanceof
		// ScalarParameterHandle)
		// {
		// return false;
		// }
		// else

		boolean validateContainer = getCurrentLocation() != LOCATION_ON;
		if (target instanceof DataSourceHandle || target instanceof DataSetHandle || target instanceof CubeHandle)
			validateContainer = false;

		if (DNDUtil.handleValidateTargetCanContainMore(target, DNDUtil.getObjectLength(transfer))) {
			canContain = DNDUtil.handleValidateTargetCanContain(target, transfer, validateContainer);
			return canContain != DNDUtil.CONTAIN_NO;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDropAdapter#
	 * performDrop(java.lang.Object)
	 */
	public boolean performDrop(Object data) {
		if (DNDService.getInstance().performDrop(data, getCurrentTarget(), getCurrentOperation(),
				new DNDLocation(getCurrentLocation())))
			return true;
		return super.performDrop(data);
	}

	/**
	 * @see DesignElementDropAdapter#moveData(Object, Object)
	 */
	protected boolean moveData(Object transfer, Object target) {
		// When get position, change target value if need be
		int position = getPosition(target);
		boolean result = DNDUtil.moveHandles(transfer, this.newTarget, position);
		if (result)
			((StructuredViewer) getViewer()).reveal(this.newTarget);
		return result;
	}

	/**
	 * 
	 */
	protected boolean applyTheme(ThemeHandle themeHandle, ModuleHandle moudelHandle) {

		ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		if (!(themeHandle.getRoot() instanceof LibraryHandle)) {
			return false;
		}
		LibraryHandle library = (LibraryHandle) themeHandle.getRoot();
		try {
			if (UIUtil.includeLibrary(moduleHandle, library)) {
				UIUtil.applyTheme(themeHandle, moduleHandle, library);
			}
		} catch (ExtendsException e) {
			GUIException exception = GUIException.createGUIException(ReportPlugin.REPORT_UI, e,
					"Library.DND.messages.outofsync");//$NON-NLS-1$
			ExceptionUtil.handle(exception);
			return false;
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDropAdapter#
	 * copyData(java.lang.Object, java.lang.Object)
	 */

	protected boolean copyData(Object transfer, Object target) {
		// When get position, change target value if need be
		int position = getPosition(target);
		boolean result = false;
		Object transferFirstElement = getSingleTransferData(transfer);
		if (transferFirstElement != null && transferFirstElement instanceof DesignElementHandle) {
			DesignElementHandle sourceHandle;
			if ((sourceHandle = (DesignElementHandle) transferFirstElement).getRoot() instanceof LibraryHandle) {
				// transfer element from a library.
				LibraryHandle library = (LibraryHandle) sourceHandle.getRoot();
				ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
				try {
					if (moduleHandle != library) {
						// element from other library not itself, create a new
						// extended element.
						if (UIUtil.includeLibrary(moduleHandle, library)) {
							DNDUtil.addElementHandle(this.newTarget, moduleHandle.getElementFactory()
									.newElementFrom(sourceHandle, sourceHandle.getName()));
							result = true;
						}
					} else {
						result = DNDUtil.copyHandles(transfer, this.newTarget, position);
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			} else {
				result = DNDUtil.copyHandles(transfer, this.newTarget, position);
			}
		} else if (transferFirstElement != null && transferFirstElement instanceof EmbeddedImageHandle) {
			EmbeddedImageHandle sourceEmbeddedImageHandle;
			if ((sourceEmbeddedImageHandle = (EmbeddedImageHandle) transferFirstElement).getElementHandle()
					.getRoot() instanceof LibraryHandle) {
				LibraryHandle library = (LibraryHandle) sourceEmbeddedImageHandle.getElementHandle().getRoot();
				ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
				try {
					if (moduleHandle != library) {
						// create a new embeddedimage from other library and
						// extend it.
						if (UIUtil.includeLibrary(moduleHandle, library)) {
							EmbeddedImage image = StructureFactory.newEmbeddedImageFrom(sourceEmbeddedImageHandle,
									moduleHandle);
							image.setType(sourceEmbeddedImageHandle.getType());
							DNDUtil.addEmbeddedImageHandle(this.newTarget, image);
							result = true;
						}
					} else {
						result = DNDUtil.copyHandles(transfer, this.newTarget, position);
					}
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			} else {
				result = DNDUtil.copyHandles(transfer, this.newTarget, position);
			}
		}
		if (result)
			((StructuredViewer) getViewer()).reveal(this.newTarget);
		return result;
	}

	private int getPosition(Object target) {
		this.newTarget = target;
		int position = DNDUtil.calculateNextPosition(target, canContain);
		if (position > -1) {
			if (DNDUtil.getDesignElementHandle(target).getContainerSlotHandle() != null) {
				this.newTarget = DNDUtil.getDesignElementHandle(target).getContainerSlotHandle();
			} else {
				this.newTarget = DNDUtil.getDesignElementHandle(target).getContainerPropertyHandle();
			}
			if (getCurrentLocation() == LOCATION_BEFORE
					|| (getCurrentLocation() == LOCATION_AFTER && target == getSelectedObject())) {
				position--;
			}
		}
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ViewerDropAdapter#dragEnter(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
		if ((event.operations & DND.DROP_COPY) != 0 && (event.operations & DND.DROP_MOVE) == 0) {
			event.detail = DND.DROP_COPY;
		}
		super.dragEnter(event);
	}

	public void addDropConstraint(Class targetClass, IDropConstraint constraint) {
		getDropConstraintList(targetClass).add(constraint);
	}

	private List getDropConstraintList(Object key) {
		if (dropConstraintMap == null) {
			dropConstraintMap = new HashMap();
		}
		List targetClassList;
		if (dropConstraintMap.containsKey(key)) {
			targetClassList = (List) dropConstraintMap.get(key);
		} else {
			targetClassList = new ArrayList();
			dropConstraintMap.put(key, targetClassList);
		}
		return targetClassList;
	}

	private Object getSingleTransferData(Object template) {
		if (template instanceof Object[]) {
			return ((Object[]) template)[0];
		}
		return template;
	}
}