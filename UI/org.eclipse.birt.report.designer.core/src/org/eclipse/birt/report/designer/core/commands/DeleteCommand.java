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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.IDropValidator;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Deletes single, multiple objects or do nothing.
 * 
 * 
 */

public class DeleteCommand extends Command {

	protected static final Logger logger = Logger.getLogger(DeleteCommand.class.getName());
	private Object model = null;

	private ArrayList embeddedImageList = new ArrayList();

	private boolean isClear = true;

	/**
	 * @return
	 */
	public boolean isClear() {
		return isClear;
	}

	/**
	 * @param isClear
	 */
	public void setClear(boolean isClear) {
		this.isClear = isClear;
	}

	/**
	 * Deletes the command
	 * 
	 * @param model the model
	 */

	public DeleteCommand(Object model) {
		this.model = model;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 */

	public void execute() {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("DeleteCommand >> Starts ... "); //$NON-NLS-1$
		}
		try {
			dropSource(model);
			if (!embeddedImageList.isEmpty()) {
				for (int i = 0; i < embeddedImageList.size(); i++) {
					IStructure item = ((EmbeddedImageHandle) embeddedImageList.get(i)).getStructure();
					String name = ((EmbeddedImageHandle) embeddedImageList.get(i)).getName();
					SessionHandleAdapter.getInstance().getReportDesignHandle()
							.getPropertyHandle(ReportDesignHandle.IMAGES_PROP).removeItem(item);
					if (DesignerConstants.TRACING_COMMANDS) {
						System.out.println("DeleteCommand >> Dropping embedded image " //$NON-NLS-1$
								+ item.getStructName());
						;
					}
					// remove cached image
					String key = ImageManager.getInstance()
							.generateKey(SessionHandleAdapter.getInstance().getReportDesignHandle(), name);
					ImageManager.getInstance().removeCachedImage(key);
				}
			}
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("DeleteCommand >> Finished. "); //$NON-NLS-1$
			}
		} catch (SemanticException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("DeleteCommand >> Failed. "); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	protected void dropSource(Object source) throws SemanticException {
		source = DNDUtil.unwrapToModel(source);
		if (source instanceof Object[]) {
			Object[] array = (Object[]) source;
			for (int i = 0; i < array.length; i++) {
				dropSource(array[i]);
			}
		} else if (source instanceof StructuredSelection) {
			dropSource(((StructuredSelection) source).toArray());
		} else if (source instanceof DesignElementHandle) {
			dropSourceElementHandle((DesignElementHandle) source);
		} else if (source instanceof EmbeddedImageHandle) {
			dropEmbeddedImageHandle((EmbeddedImageHandle) (source));
		} else if (source instanceof SlotHandle) {
			dropSourceSlotHandle((SlotHandle) source);
		} else if (source instanceof CssStyleSheetHandle) {
			dropCssStyleHandle((CssStyleSheetHandle) source);
		} else if (source instanceof ScriptObjectNode) {
			((ScriptObjectNode) source).reset();
		}
	}

	private void dropEmbeddedImageHandle(EmbeddedImageHandle embeddedImage) {
		embeddedImageList.add(embeddedImage);
	}

	protected void dropSourceElementHandle(DesignElementHandle handle) throws SemanticException {
		if (handle.getContainer() != null) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("DeleteCommand >> Dropping " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel(handle));
			}
			// if (isExtendedCell( handle ))
			if (handle instanceof ExtendedItemHandle && isExtendedCell((ExtendedItemHandle) handle)) {
				ExtendedItemHandle extendedHandle = (ExtendedItemHandle) handle;
				List list = extendedHandle.getContents(DEUtil.getDefaultContentName(handle));
				for (int i = 0; i < list.size(); i++) {
					dropSourceElementHandle((DesignElementHandle) list.get(i));
				}
			} else if (handle instanceof CellHandle) {
				dropSourceSlotHandle(((CellHandle) handle).getContent());
			} else if (handle instanceof RowHandle) {
				new DeleteRowCommand(handle).execute();
			} else if (handle instanceof ColumnHandle) {
				new DeleteColumnCommand(handle).execute();
			} else {
				if (isClear()) {
					handle.dropAndClear();
				} else {
					handle.drop();
				}

			}
		}
	}

	// This is a temp method to fixed bug 190959.
	// TODO Through the extened point to do it
	private boolean isExtendedCell(ExtendedItemHandle handle) {
		return handle.getExtensionName().indexOf("Cell") > -1;//$NON-NLS-1$
	}

	protected void dropSourceSlotHandle(SlotHandle slot) throws SemanticException {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("DeleteCommand >> Dropping slot " //$NON-NLS-1$
					+ slot.getSlotID() + " of " //$NON-NLS-1$
					+ DEUtil.getDisplayLabel(slot.getElementHandle()));
		}
		List list = slot.getContents();
		for (int i = 0; i < list.size(); i++) {
			dropSourceElementHandle((DesignElementHandle) list.get(i));
		}
	}

	protected void dropCssStyleHandle(CssStyleSheetHandle cssStyleHandle) throws SemanticException {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("DeleteCommand >> Dropping " //$NON-NLS-1$
					+ DEUtil.getDisplayLabel(cssStyleHandle.getElementHandle()));
		}
		DesignElementHandle containerHandle = cssStyleHandle.getContainerHandle();

		if (containerHandle instanceof ReportDesignHandle) {
			ReportDesignHandle report = (ReportDesignHandle) containerHandle;
			if (report.canDropCssStyleSheet(cssStyleHandle)) {
				report.dropCss(cssStyleHandle);
			}
		} else if (containerHandle instanceof AbstractThemeHandle) {
			AbstractThemeHandle theme = (AbstractThemeHandle) containerHandle;
			if (theme.canDropCssStyleSheet(cssStyleHandle)) {
				theme.dropCss(cssStyleHandle);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute() {
		return canDrop(model);
	}

	/**
	 * Returns the object can be deleted. If the parent can be deleted, the children
	 * will be skippedl
	 * 
	 * @param source single or multiple objects
	 */
	protected boolean canDrop(Object source) {
		if (SessionHandleAdapter.getInstance().getReportDesignHandle() == null) {
			return false;
		}
		if (source == null) {
			return false;
		}
		if (source instanceof List) {
			return canDrop(((List) source).toArray());
		}
		if (source instanceof StructuredSelection) {
			return canDrop(((StructuredSelection) source).toArray());
		}
		if (source instanceof Object[]) {
			Object[] array = (Object[]) source;
			if (array.length == 0) {
				return false;
			}

			// If the container can drop, the children will be skipped
			for (int i = 0; i < array.length; i++) {
				if (DNDUtil.checkContainerExists(array[i], array))
					continue;
				// 267156 Can't delete all master pages
				if (array[i] instanceof MasterPageHandle) {
					int masterPageCount = SessionHandleAdapter.getInstance().getReportDesignHandle().getMasterPages()
							.getCount();
					for (int j = 0; j < array.length; j++) {
						if (array[j] instanceof MasterPageHandle)
							masterPageCount--;
					}
					if (masterPageCount == 0)
						return false;
				}
				if (!canDrop(array[i]))
					return false;
			}
			return true;
		}
		source = DNDUtil.unwrapToModel(source);
		if (source instanceof SlotHandle) {
			SlotHandle slot = (SlotHandle) source;
			DesignElementHandle handle = slot.getElementHandle();
			return slot.getContents().size() > 0 && handle != null && handle.canDrop() && canDrop(slot.getContents());
		}
		if (source instanceof EmbeddedImageHandle) {
			return true;
		}
		if (source instanceof ExtendedItemHandle) {
			Object dropValidator = Platform.getAdapterManager().getAdapter(source, IDropValidator.class);
			if (dropValidator instanceof IDropValidator && ((IDropValidator) dropValidator).accpetValidator()) {
				return ((IDropValidator) dropValidator).canDrop();
			}
		}
		if (source instanceof CellHandle) {
			// CellHandle is subclass of ReportElementHandle
			return ((CellHandle) source).getContent().getContents().size() > 0 && ((CellHandle) source).canDrop();
		}

		if (source instanceof MasterPageHandle) {
			if (SessionHandleAdapter.getInstance().getReportDesignHandle().getMasterPages().getCount() > 1) {
				return true;
			}
			return false;
		} else if (source instanceof ModuleHandle) {
			return false;
		} else if (source instanceof DesignElementHandle) {
			return ((DesignElementHandle) source).canDrop();

		} else if (source instanceof LibraryHandle) {
			if (((LibraryHandle) source).getHostHandle() != null)
				return true;
			else
				return false;
		} else if (source instanceof CssStyleSheetHandle) {
			DesignElementHandle elementHandle = ((CssStyleSheetHandle) source).getContainerHandle();
			if (elementHandle instanceof ReportDesignHandle) {
				return ((ReportDesignHandle) elementHandle).canDropCssStyleSheet((CssStyleSheetHandle) source);
			} else if (elementHandle instanceof AbstractThemeHandle) {
				return ((AbstractThemeHandle) elementHandle).canDropCssStyleSheet((CssStyleSheetHandle) source);
			} else {
				return false;
			}

		} else if (source instanceof ScriptObjectNode) {
			return true;
		} else
			return false;

		// return (source instanceof ReportElementHandle
		// // && (SessionHandleAdapter.getInstance( ).getReportDesignHandle()
		// instanceof LibraryHandle)
		// && !( source instanceof MasterPageHandle ));

	}
}
