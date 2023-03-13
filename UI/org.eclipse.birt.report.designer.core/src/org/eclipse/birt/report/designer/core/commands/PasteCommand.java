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

package org.eclipse.birt.report.designer.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.util.CopyUtil;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.gef.commands.Command;

/**
 * Paste Command
 *
 */

public class PasteCommand extends Command {

	protected static final Logger logger = Logger.getLogger(PasteCommand.class.getName());
	/** Null permitted in instance. */
	private DesignElementHandle sourceHandle;

	private Object newContainer;

	private DesignElementHandle afterHandle;

	private Object cloneElement;

	/** True: cut; False: copy */
	private boolean isCut = false;

	private int slotID = -1;
	private String contentString = null;

	private int position = -1;

	private boolean isCloned = false;

	/**
	 * Constructor
	 *
	 * @param sourceHandle the source
	 * @param newContainer the new container, class type could be
	 *                     <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *                     or <code>ReportElementModel</code>
	 * @param afterHandle  the handle next to the source
	 * @param isCut        If true, delete source
	 */
	public PasteCommand(DesignElementHandle sourceHandle, Object newContainer, DesignElementHandle afterHandle,
			boolean isCut) {
		this.sourceHandle = sourceHandle;
		this.cloneElement = CopyUtil.copy(sourceHandle);
		this.newContainer = newContainer;
		this.afterHandle = afterHandle;
		this.isCut = isCut;
		isCloned = true;
	}

	/**
	 * Constructor
	 *
	 * @param sourceHandle the source
	 * @param newContainer the new container, class type could be
	 *                     <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *                     or <code>ReportElementModel</code>
	 * @param position     the position will be added
	 * @param isCut        If true, delete source
	 */
	public PasteCommand(DesignElementHandle sourceHandle, Object newContainer, int position, boolean isCut) {
		this.sourceHandle = sourceHandle;
		this.cloneElement = CopyUtil.copy(sourceHandle);
		this.newContainer = newContainer;
		this.position = position;
		this.isCut = isCut;
		isCloned = true;
	}

	/**
	 * Constructor
	 *
	 * @param cloneElement the copy of the source
	 * @param newContainer the new container, class type could be
	 *                     <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *                     or <code>ReportElementModel</code>
	 * @param afterHandle  the handle next to the source
	 */
	public PasteCommand(IDesignElement cloneElement, Object newContainer, DesignElementHandle afterHandle) {
		this.cloneElement = cloneElement;
		this.newContainer = newContainer;
		this.afterHandle = afterHandle;
		isCloned = false;
	}

	/**
	 * Constructor
	 *
	 * @param cloneElement the copy of the source
	 * @param newContainer the new container, class type could be
	 *                     <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *                     or <code>ReportElementModel</code>
	 * @param position     the position will be added
	 */
	public PasteCommand(IDesignElement cloneElement, Object newContainer, int position) {
		this.cloneElement = cloneElement;
		this.newContainer = newContainer;
		this.position = position;
		isCloned = false;
	}

	/**
	 * Constructor
	 *
	 * @param cloneElement the copy of the source
	 * @param newContainer the new container, class type could be
	 *                     <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *                     or <code>ReportElementModel</code>
	 * @param position     the position will be added
	 */
	public PasteCommand(IElementCopy cloneElement, Object newContainer, int position) {
		this.cloneElement = cloneElement;
		this.newContainer = newContainer;
		this.position = position;
		isCloned = false;
	}

	/**
	 * Executes the Command.
	 */
	@Override
	public void execute() {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("PasteCommand >> Starts ..."); //$NON-NLS-1$
		}
		try {
			if (!isCut || sourceHandle == null || sourceHandle.getContainer() == null) {
				isCut = false;
			}

			calculatePositionAndSlotId();

			// Drops old source handle if operation is cut
			dropSourceHandle(sourceHandle);

			// Gets new handle
			ModuleHandle currentDesignHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();

			DesignElementHandle newHandle = cloneElement instanceof IDesignElement
					? copyNewHandle((IDesignElement) cloneElement, currentDesignHandle)
					: null;

			// Adds new handle to report
			addHandleToReport(newHandle);
		} catch (Exception e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("PasteCommand >> Failed."); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Add this design element to report.
	 *
	 * @param newHandle The design element to add
	 */
	private void addHandleToReport(DesignElementHandle newHandle)
			throws ContentException, NameException, SemanticException {
		SlotHandle slotHandle = null;
		DesignElementHandle containerHandle = null;

		if (newContainer instanceof DesignElementHandle) {
			slotHandle = ((DesignElementHandle) newContainer).getSlot(slotID);
			containerHandle = (DesignElementHandle) newContainer;
		} else if (newContainer instanceof SlotHandle) {
			slotHandle = (SlotHandle) newContainer;
			containerHandle = ((SlotHandle) newContainer).getElementHandle();
		} else if (newContainer instanceof PropertyHandle) {
			containerHandle = ((PropertyHandle) newContainer).getElementHandle();
		}
		// else if ( newContainer instanceof ReportElementModel )
		// {
		// slotHandle = ( (ReportElementModel) newContainer ).getElementHandle(
		// )
		// .getSlot( slotID );
		//
		// }
		if (cloneElement instanceof IElementCopy) {
			if (slotHandle != null) {
				CopyUtil.paste((IElementCopy) cloneElement, containerHandle, slotID, position);
			} else if (newContainer instanceof PropertyHandle) {
				CopyUtil.paste((IElementCopy) cloneElement, containerHandle,
						((PropertyHandle) newContainer).getPropertyDefn().getName(), position);
			} else if (newContainer instanceof DesignElementHandle) {
				ContainerContext cc = DNDUtil.getContainerContext(containerHandle,
						((IElementCopy) cloneElement).getHandle(containerHandle.getModuleHandle()));
				if (cc == null) {
					CopyUtil.paste((IElementCopy) cloneElement, containerHandle,
							DEUtil.getDefaultContentName(newContainer), position);
				} else if (cc.getPropertyName() != null) {
					CopyUtil.paste((IElementCopy) cloneElement, containerHandle, cc.getPropertyName(), position);
				} else if (cc.getSlotID() != -1) {
					CopyUtil.paste((IElementCopy) cloneElement, containerHandle, cc.getSlotID(), position);
				}
			}
		} else if (newHandle != null) {
			if (slotHandle != null) {
				slotHandle.paste(newHandle, position);
			} else if (newContainer instanceof PropertyHandle) {
				((PropertyHandle) newContainer).paste(newHandle, position);
			} else if (newContainer instanceof DesignElementHandle) {
				((DesignElementHandle) newContainer).getPropertyHandle(contentString).paste(newHandle, position);
			}
		}

		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("PasteCommand >>  Finished. Paste " //$NON-NLS-1$
					+ cloneElement + " to the container " //$NON-NLS-1$
					+ slotHandle != null ? slotHandle.getSlotID()
							: DEUtil.getDefaultContentName(newContainer) + ",Position: " //$NON-NLS-1$
									+ position);
		}
	}

	/**
	 * Caculate the paste position
	 */
	private void calculatePositionAndSlotId() {
		DesignElementHandle container = null;
		if (newContainer instanceof DesignElementHandle) {
			slotID = DEUtil.getDefaultSlotID(newContainer);
			if (slotID == -1) {
				contentString = DEUtil.getDefaultContentName(newContainer);
			}
			container = (DesignElementHandle) newContainer;
		} else if (newContainer instanceof SlotHandle) {
			slotID = ((SlotHandle) newContainer).getSlotID();
			container = ((SlotHandle) newContainer).getElementHandle();
		}
		// else if ( newContainer instanceof ReportElementModel )
		// {
		// slotID = ( (ReportElementModel) newContainer ).getSlotId( );
		// container = ( (ReportElementModel) newContainer ).getElementHandle(
		// );
		// }
		else {
			return;
		}

		if (afterHandle != null) {
			if (slotID == -1) {
				position = DEUtil.findInsertPosition(container, afterHandle, contentString);
			} else {
				position = DEUtil.findInsertPosition(container, afterHandle, slotID);
			}
		} else if (position > -1 && isCut && sourceHandle.getContainer() == container) {
			int oldPosition = DEUtil.findInsertPosition(container, sourceHandle, slotID);
			if (oldPosition < position) {
				position--;
			}
		}

	}

	/**
	 *
	 * Drop source handle
	 *
	 * @param oldHandle The source handle
	 */
	private void dropSourceHandle(DesignElementHandle oldHandle) throws SemanticException {
		if (isCut) {
			oldHandle.drop();
		}
	}

	/**
	 * Copy new handle
	 *
	 * @param element             The elemnent to copy
	 * @param currentDesignHandle Current design handle
	 * @return The copied handle
	 * @throws CloneNotSupportedException
	 */
	private DesignElementHandle copyNewHandle(IDesignElement element, ModuleHandle currentDesignHandle)
			throws CloneNotSupportedException {
		IDesignElement newElement = isCloned ? element : (IDesignElement) element.clone();
		DesignElementHandle handle = newElement.getHandle(currentDesignHandle.getModule());

		if (newContainer instanceof ThemeHandle) {
			currentDesignHandle.rename((ThemeHandle) newContainer, handle);
		} else if (newContainer instanceof SlotHandle
				&& ((SlotHandle) newContainer).getElementHandle() instanceof ThemeHandle) {
			currentDesignHandle.rename(((SlotHandle) newContainer).getElementHandle(), handle);
		} else {
			currentDesignHandle.rename(handle);
		}
		return handle;
	}

	/**
	 * @return <code>true</code> if the command can be executed
	 */
	@Override
	public boolean canExecute() {
		if (cloneElement == null) {
			return false;
		}
		if (newContainer instanceof PropertyHandle && cloneElement instanceof IElementCopy
				&& newContainer instanceof DesignElementHandle) {
			PropertyHandle targetHandle = (PropertyHandle) newContainer;
			return CopyUtil.canPaste((IElementCopy) cloneElement, (DesignElementHandle) newContainer,
					targetHandle.getPropertyDefn().getName()).canPaste();
		}
		DesignElementHandle childHandle = sourceHandle;
		if (childHandle == null) {
			if (cloneElement instanceof IDesignElement) {
				childHandle = ((IDesignElement) cloneElement)
						.getHandle(SessionHandleAdapter.getInstance().getReportDesignHandle().getModule());
			} else if (cloneElement instanceof IElementCopy) {
				childHandle = ((IElementCopy) cloneElement)
						.getHandle(SessionHandleAdapter.getInstance().getReportDesignHandle());
			}
		}
		return DNDUtil.handleValidateTargetCanContain(newContainer, childHandle)
				&& DNDUtil.handleValidateTargetCanContainMore(newContainer, 1);
	}
}
