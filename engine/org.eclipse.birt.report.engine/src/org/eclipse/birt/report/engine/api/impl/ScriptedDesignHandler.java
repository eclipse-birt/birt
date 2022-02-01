/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IReportItemPreparation;
import org.eclipse.birt.report.engine.extension.internal.PreparationContext;
import org.eclipse.birt.report.engine.extension.internal.ReportItemPreparationInfo;
import org.eclipse.birt.report.engine.script.internal.AutoTextScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.CellScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DataItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.DynamicTextScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ExtendedItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.GridScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ImageScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.LabelScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ListScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.RowScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableGroupScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TableScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.TextItemScriptExecutor;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

public class ScriptedDesignHandler extends ScriptedDesignVisitor {

	/**
	 * logger used to log the error.
	 */
	protected static Logger logger = Logger.getLogger(ScriptedDesignHandler.class.getName());

	/**
	 * the execution context to execute the onPrepare script
	 */
	protected ExecutionContext executionContext;

	/**
	 * constructor
	 * 
	 * @param handle           - the entry point to the DE report design IR
	 * @param executionContext - the execution context to execute the onPrepare
	 *                         script
	 */
	public ScriptedDesignHandler(ReportDesignHandle handle, ExecutionContext executionContext) {
		super(handle);

		this.executionContext = executionContext;
	}

	protected void handleOnPrepare(ReportDesignHandle handle) {
		boolean hasJavaScript = (handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0);
		boolean hasJavaCode = (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0);
		if (!hasJavaScript && !hasJavaCode)
			return;
		executionContext.pushHandle(handle);
		if (hasJavaScript) {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(handle);
			processOnPrepareScript(handle, element);
			return;
		}
		try {
			ReportScriptExecutor.handleOnPrepare(handle, executionContext);
		} finally {
			executionContext.popHandle();
		}
	}

	protected void handleOnPrepare(ReportItemHandle handle) {
		boolean hasJavaScript = (handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0);
		boolean hasJavaCode = (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0);
		if (!hasJavaScript && !hasJavaCode)
			return;
		executionContext.pushHandle(handle);
		if (hasJavaScript) {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(handle);
			processOnPrepareScript(handle, element);
			return;
		}
		try {
			if (handle instanceof DataItemHandle) {
				DataItemScriptExecutor.handleOnPrepare((DataItemHandle) handle, executionContext);
			} else if (handle instanceof GridHandle) {
				GridScriptExecutor.handleOnPrepare((GridHandle) handle, executionContext);
			} else if (handle instanceof ImageHandle) {
				ImageScriptExecutor.handleOnPrepare((ImageHandle) handle, executionContext);
			} else if (handle instanceof LabelHandle) {
				LabelScriptExecutor.handleOnPrepare((LabelHandle) handle, executionContext);
			} else if (handle instanceof ListHandle) {
				ListScriptExecutor.handleOnPrepare((ListHandle) handle, executionContext);
			} else if (handle instanceof TableHandle) {
				TableScriptExecutor.handleOnPrepare((TableHandle) handle, executionContext);
			} else if (handle instanceof TextItemHandle) {
				TextItemScriptExecutor.handleOnPrepare((TextItemHandle) handle, executionContext);

			} else if (handle instanceof TextDataHandle) {
				DynamicTextScriptExecutor.handleOnPrepare((TextDataHandle) handle, executionContext);
			} else if (handle instanceof AutoTextHandle) {
				AutoTextScriptExecutor.handleOnPrepare((AutoTextHandle) handle, executionContext);
			} else if (handle instanceof ExtendedItemHandle) {
				ExtendedItemScriptExecutor.handleOnPrepare((ExtendedItemHandle) handle, executionContext);
			} else {
				processOnPrepareScript(handle, handle);
			}
		} finally {
			executionContext.popHandle();
		}
	}

	private void processOnPrepareScript(ReportDesignHandle handle, Object element) {
		if (element != null) {
			executionContext.newScope(element);
		}
		try {
			if (handle.getOnPrepare() != null) {
				if (handle.getOnPrepare() != null) {
					String scriptText = handle.getOnPrepare();
					if (null != scriptText) {
						String id = ModuleUtil
								.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_PREPARE_METHOD));
						executionContext.evaluate(id, scriptText);
					}
				}
			}
			return;
		} catch (BirtException ex) {
			executionContext.addException(handle, ex);
			return;
		} finally {
			if (element != null) {
				executionContext.exitScope();
			}
		}
	}

	private void processOnPrepareScript(ReportItemHandle handle, Object element) {
		if (element != null) {
			executionContext.newScope(element);
		}
		try {
			if (handle.getOnPrepare() != null) {
				if (handle.getOnPrepare() != null) {
					String scriptText = handle.getOnPrepare();
					if (null != scriptText) {
						String id = ModuleUtil
								.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_PREPARE_METHOD));
						executionContext.evaluate(id, scriptText);
					}
				}
			}
			return;
		} catch (BirtException ex) {
			executionContext.addException(handle, ex);
			return;
		} finally {
			if (element != null) {
				executionContext.exitScope();
			}
		}
	}

	protected ScriptExpression getOnPrepareScriptExpression(ReportItemHandle handle) {
		if (null != handle) {
			String scriptText = handle.getOnPrepare();
			if (null != scriptText) {
				String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_PREPARE_METHOD));
				return new ScriptExpression(scriptText, id);
			}
		}
		return null;
	}

	// TODO: Merge this function with the above one when DE add onPrepare to
	// DesignElementHandle
	protected void handleOnPrepare(CellHandle handle) {

		boolean hasJavaScript = (handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0);
		boolean hasJavaCode = (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0);
		if (!hasJavaScript && !hasJavaCode)
			return;
		executionContext.pushHandle(handle);
		if (hasJavaScript) {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(handle);
			try {
				if (element != null) {
					executionContext.newScope(element);
				}
				if (handle.getOnPrepare() != null) {
					String scriptText = handle.getOnPrepare();
					if (null != scriptText) {
						String id = ModuleUtil
								.getScriptUID(handle.getPropertyHandle(IReportItemModel.ON_PREPARE_METHOD));
						executionContext.evaluate(id, scriptText);
					}
				}
				return;
			} catch (BirtException ex) {
				executionContext.addException(handle, ex);
				return;
			} finally {
				if (element != null) {
					executionContext.exitScope();
				}
			}
		}
		try {
			CellScriptExecutor.handleOnPrepare(handle, executionContext);
		} finally {
			executionContext.popHandle();
		}
	}

	// TODO: Merge this function with the above one when DE add onPrepare to
	// DesignElementHandle
	protected void handleOnPrepare(GroupHandle handle) {
		boolean hasJavaScript = (handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0);
		boolean hasJavaCode = (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0);
		if (!hasJavaScript && !hasJavaCode)
			return;
		executionContext.pushHandle(handle);
		if (hasJavaScript) {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(handle);
			try {
				if (element != null) {
					executionContext.newScope(element);
				}
				if (handle.getOnPrepare() != null) {
					String scriptText = handle.getOnPrepare();
					if (null != scriptText) {
						String id = ModuleUtil
								.getScriptUID(handle.getPropertyHandle(IGroupElementModel.ON_PREPARE_METHOD));
						executionContext.evaluate(id, scriptText);
					}
				}
				return;
			} catch (BirtException ex) {
				executionContext.addException(handle, ex);
				return;
			} finally {
				if (element != null) {
					executionContext.exitScope();
				}
			}
		}
		try {
			if (handle instanceof TableGroupHandle)
				TableGroupScriptExecutor.handleOnPrepare((TableGroupHandle) handle, executionContext);
			if (handle instanceof ListGroupHandle)
				ListGroupScriptExecutor.handleOnPrepare((ListGroupHandle) handle, executionContext);
		} finally {
			executionContext.popHandle();
		}
	}

	// TODO: Merge this function with the above one when DE add onPrepare to
	// DesignElementHandle
	protected void handleOnPrepare(RowHandle handle) {
		boolean hasJavaScript = (handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0);
		boolean hasJavaCode = (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0);
		if (!hasJavaScript && !hasJavaCode)
			return;
		executionContext.pushHandle(handle);
		if (hasJavaScript) {
			IDesignElement element = SimpleElementFactory.getInstance().getElement(handle);
			try {
				if (element != null) {
					executionContext.newScope(element);
				}
				if (handle.getOnPrepare() != null) {
					String scriptText = handle.getOnPrepare();
					if (null != scriptText) {
						String id = ModuleUtil.getScriptUID(handle.getPropertyHandle(ITableRowModel.ON_PREPARE_METHOD));
						executionContext.evaluate(id, scriptText);
					}
				}
				return;
			} catch (BirtException ex) {
				executionContext.addException(handle, ex);
				return;
			} finally {
				if (element != null) {
					executionContext.exitScope();
				}
			}
		}
		try {
			RowScriptExecutor.handleOnPrepare(handle, executionContext);
		} finally {
			executionContext.popHandle();
		}
	}

	protected void visitExtendedItem(ExtendedItemHandle handle) {
		IReportItemPreparation itemPreparation = executionContext.getExtendedItemManager().createPreparation(handle);
		if (itemPreparation != null) {
			ReportItemPreparationInfo preparationInfo = new ReportItemPreparationInfo(handle,
					new PreparationContext(executionContext, this));

			itemPreparation.init(preparationInfo);
			try {
				itemPreparation.prepare();
			} catch (BirtException ex) {
				logger.log(Level.WARNING, "An error happens when preparing extended report item", ex);
				executionContext.addException(handle, ex);
			}
		} else {
			ExtendedItemScriptExecutor.handleOnPrepare((ExtendedItemHandle) handle, executionContext);

			Iterator propIter = handle.getPropertyIterator();
			while (propIter.hasNext()) {
				PropertyHandle propHandle = (PropertyHandle) propIter.next();
				IElementPropertyDefn property = propHandle.getPropertyDefn();
				if (property.getTypeCode() == IPropertyType.ELEMENT_TYPE) {
					Object children = propHandle.getValue();
					if (children instanceof List) {
						List tempList = (List) children;
						for (int i = 0; tempList != null && i < tempList.size(); i++) {
							Object tempObj = tempList.get(i);
							if (tempObj instanceof ReportItemHandle) {
								apply((ReportItemHandle) tempObj);
							}
						}
					} else if (children instanceof ReportItemHandle) {
						apply((ReportItemHandle) children);
					}
				}
			}
		}
		// ExtendedItemDesign extendedItem = new ExtendedItemDesign( );
		// setupReportItem( extendedItem, obj );
	}

}
