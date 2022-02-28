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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.IElementDropAdapter;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

/**
 *
 */

public class SlotHandleDropAdapter implements IElementDropAdapter {

	@Override
	public boolean validateDrop(Object target, int operation, int location, Object transfer,
			TransferData transferType) {
		return TemplateTransfer.getInstance().isSupportedType(transferType);
	}

	@Override
	public boolean handleDrop(Object target, int operation, int location, Object transfer) {
		int canContain = DNDUtil.handleValidateTargetCanContain(target, transfer,
				location != ViewerDropAdapter.LOCATION_ON);
		if (operation == DND.DROP_MOVE) {
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Dropped. Operation: Copy, Target: " //$NON-NLS-1$
						+ target);
			}
			int position = DNDUtil.calculateNextPosition(target, canContain);
			if (position > -1) {
				target = DNDUtil.getDesignElementHandle(target).getContainerSlotHandle();
				if (location == ViewerDropAdapter.LOCATION_BEFORE) {
					position--;
				}
			}
			return DNDUtil.moveHandles(transfer, target, position);
		} else if (operation == DND.DROP_COPY) {
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Dropped. Operation: Move, Target: " //$NON-NLS-1$
						+ target);
			}
			// When get position, change target value if need be
			int position = DNDUtil.calculateNextPosition(target, canContain);
			if (position > -1) {
				target = DNDUtil.getDesignElementHandle(target).getContainerSlotHandle();
				if (location == ViewerDropAdapter.LOCATION_BEFORE) {
					position--;
				}
			}
			boolean result = false;
			Object transferFirstElement = getSingleTransferData(transfer);
			if (transferFirstElement instanceof DesignElementHandle) {
				DesignElementHandle sourceHandle;
				if ((sourceHandle = (DesignElementHandle) transferFirstElement).getRoot() instanceof LibraryHandle) {
					// transfer element from a library.
					LibraryHandle library = (LibraryHandle) sourceHandle.getRoot();
					ModuleHandle moduleHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
					try {
						if (moduleHandle != library) {
							// element from other library not itself, create a
							// new
							// extended element.
							if (UIUtil.includeLibrary(moduleHandle, library)) {
								DNDUtil.addElementHandle(target, moduleHandle.getElementFactory()
										.newElementFrom(sourceHandle, sourceHandle.getName()));
								result = true;
							}
						} else {
							result = DNDUtil.copyHandles(transfer, target, position);
						}
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				} else {
					result = DNDUtil.copyHandles(transfer, target, position);
				}
			} else if (transferFirstElement instanceof EmbeddedImageHandle) {
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
								DNDUtil.addEmbeddedImageHandle(target, image);
								result = true;
							}
						} else {
							result = DNDUtil.copyHandles(transfer, target, position);
						}
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
				} else {
					result = DNDUtil.copyHandles(transfer, target, position);
				}
			}
			return result;
		}
		return false;
	}

	private Object getSingleTransferData(Object template) {
		if (template instanceof Object[]) {
			return ((Object[]) template)[0];
		}
		return template;
	}
}
