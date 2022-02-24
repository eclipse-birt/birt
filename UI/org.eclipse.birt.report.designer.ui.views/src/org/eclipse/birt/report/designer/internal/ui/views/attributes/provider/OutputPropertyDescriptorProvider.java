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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;

public class OutputPropertyDescriptorProvider extends AbstractDescriptorProvider {

	private boolean updateHideRule(DesignElementHandle element, String format, boolean checked, Expression expression)
			throws Exception {
		// save the output type
		if (checked) {
			HideRuleHandle hideHandle = getHideRuleHandle(element, format);
			if (hideHandle == null) {
				try {
					createHideRuleHandle(element, format, expression);
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			} else {

				hideHandle.setExpressionProperty(HideRule.VALUE_EXPR_MEMBER, expression);

			}
		} else {
			// remove the given output format
			Iterator visibilities = visibilityRulesIterator(element);
			if (visibilities == null) {
				return true;
			}
			while (visibilities.hasNext()) {
				HideRuleHandle handle = (HideRuleHandle) visibilities.next();
				if (format.equalsIgnoreCase(handle.getFormat())) {
					try {
						if (DEUtil.getMultiSelectionHandle((List) input).isExtendedElements()) {
							PropertyHandle propertyHandle = getVisibilityPropertyHandle(element);
							propertyHandle.getItems().clear();

						} else {
							getVisibilityPropertyHandle(element).removeItem(handle.getStructure());
						}

					} catch (PropertyValueException e) {
						ExceptionUtil.handle(e);
					}
					return true;
				}
			}
		}
		return true;
	}

	/**
	 * Gets a given hide-output Handle.
	 *
	 * @param handle The ReportItemHandle.
	 * @param format hide-output format.
	 * @return hide-output Handle.
	 */
	private HideRuleHandle getHideRuleHandle(DesignElementHandle handle, String format) {
		Iterator visibilities = visibilityRulesIterator(handle);
		if (visibilities == null) {
			return null;
		}

		while (visibilities.hasNext()) {
			HideRuleHandle hideHandle = (HideRuleHandle) visibilities.next();
			if (format.equalsIgnoreCase(hideHandle.getFormat())) {
				return hideHandle;
			}
		}
		return null;
	}

	/**
	 * Creates a new hide-output Handle.
	 *
	 * @param format hide-output format.
	 * @return hide-output Handle.
	 * @throws SemanticException
	 */
	private HideRuleHandle createHideRuleHandle(DesignElementHandle element, String format, Expression expression)
			throws SemanticException {
		PropertyHandle propertyHandle = getVisibilityPropertyHandle(element);
		HideRule hide = StructureFactory.createHideRule();

		hide.setFormat(format);
		hide.setExpressionProperty(HideRule.VALUE_EXPR_MEMBER, expression);

		propertyHandle.addItem(hide);

		return (HideRuleHandle) hide.getHandle(propertyHandle);

	}

	/**
	 * Clears the VISIBILITY_PROP property value.
	 *
	 * @return True if operation successes, false if fails.
	 */
	private boolean clearProperty(DesignElementHandle handle) throws Exception {
		if (visibilityRulesIterator(handle) != null) {
			try {
				if (DEUtil.getMultiSelectionHandle((List) input).isExtendedElements()) {
					List handRuleList = (ArrayList) handle.getProperty(ReportItemHandle.VISIBILITY_PROP);
					if (handRuleList != null && handRuleList.size() > 0) {
						handRuleList.clear();
						handle.setProperty(ReportItemHandle.VISIBILITY_PROP, handRuleList);
					}
				} else if (handle instanceof ReportItemHandle) {
					handle.clearProperty(ReportItemHandle.VISIBILITY_PROP);
				} else if (handle instanceof RowHandle) {
					handle.clearProperty(RowHandle.VISIBILITY_PROP);
				} else if (handle instanceof ColumnHandle) {
					handle.clearProperty(ColumnHandle.VISIBILITY_PROP);
				}

			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
		return true;
	}

	public void clearProperty() throws Exception {
		for (Iterator iter = DEUtil.getInputElements(input).iterator(); iter.hasNext();) {
			DesignElementHandle handle = (DesignElementHandle) iter.next();
			clearProperty(handle);
		}
	}

	/**
	 * Gets the PropertyHandle of VISIBILITY_PROP property.
	 *
	 * @return PropertyHandle
	 */
	private PropertyHandle getVisibilityPropertyHandle(DesignElementHandle handle) {
		if (handle == null) {
			return null;
		}
		if (handle instanceof ReportItemHandle) {
			return handle.getPropertyHandle(ReportItemHandle.VISIBILITY_PROP);
		} else if (handle instanceof RowHandle) {
			return handle.getPropertyHandle(RowHandle.VISIBILITY_PROP);
		} else if (handle instanceof ColumnHandle) {
			return handle.getPropertyHandle(ColumnHandle.VISIBILITY_PROP);
		} else {
			return null;
		}

	}

	/**
	 * Gets the DE CommandStack instance
	 *
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	public Iterator visibilityRulesIterator(DesignElementHandle handle) {
		Iterator visibilities = null;
		if (handle instanceof ReportItemHandle) {
			visibilities = ((ReportItemHandle) handle).visibilityRulesIterator();
		} else if (handle instanceof RowHandle) {
			visibilities = ((RowHandle) handle).visibilityRulesIterator();
		} else if (handle instanceof ColumnHandle) {
			visibilities = ((ColumnHandle) handle).visibilityRulesIterator();
		}
		return visibilities;
	}

	public boolean isEnabled() {
		return DEUtil.getInputSize(input) == 1;
	}

	private String[] typeInfo;

	public String[] getTypeInfo() {
		List<String> list = new ArrayList<>();
		if (typeInfo == null) {
			ReportEngine engine = new ReportEngine(new EngineConfig());
			// typeInfo = engine.getSupportedFormats( );
			EmitterInfo[] emitters = engine.getEmitterInfo();
			if (emitters == null || emitters.length == 0) {
				typeInfo = new String[] {};
			} else {
				List<String> temp = new ArrayList<>();
				for (int i = 0; i < emitters.length; i++) {
					EmitterInfo info = emitters[i];
					if (!info.isHidden()) {
						temp.add(info.getFormat());
					}
				}
				Collections.sort(temp, new AlphabeticallyComparator());
				typeInfo = temp.toArray(new String[temp.size()]);
			}
		}
		return typeInfo;
	}

	public DesignElementHandle getFirstElementHandle() {
		Object obj = DEUtil.getInputFirstElement(input);
		if (obj instanceof ReportItemHandle) {
			return (ReportItemHandle) obj;
		} else if (obj instanceof RowHandle) {
			return (RowHandle) obj;
		} else if (obj instanceof ColumnHandle) {
			return (ColumnHandle) obj;
		} else {
			return null;
		}
	}

	public void saveAllOutput(Expression value) throws Exception {
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("VisibilityPage.menu.SaveHides")); //$NON-NLS-1$

		for (Iterator iter = DEUtil.getInputElements(input).iterator(); iter.hasNext();) {
			DesignElementHandle element = (DesignElementHandle) iter.next();

			Iterator visibilities = visibilityRulesIterator(element);
			if (visibilities != null && visibilities.hasNext()) {
				if (getHideRuleHandle(element, DesignChoiceConstants.FORMAT_TYPE_ALL) == null) {
					boolean flag = false;
					try {
						flag = clearProperty(element);
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
					if (!flag) {
						stack.rollback();
						return;
					}
				}
			}

			updateHideRule(element, DesignChoiceConstants.FORMAT_TYPE_ALL, true, value);

		}
		stack.commit();

	}

	public void saveSpecialOutput(boolean[] selections, Expression[] expressions) throws Exception {
		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("VisibilityPage.menu.SaveHides")); //$NON-NLS-1$

		for (Iterator iter = DEUtil.getInputElements(input).iterator(); iter.hasNext();) {
			DesignElementHandle element = (DesignElementHandle) iter.next();

			boolean hideForAll = true;
			for (int i = 0; i < getTypeInfo().length; i++) {
				if (selections[i]) {
					hideForAll = false;
				}
				if (!updateHideRule(element, getTypeInfo()[i], selections[i], expressions[i])) {
					stack.rollback();
					return;
				}
			}

			if (hideForAll) {
				if (!updateHideRule(element, DesignChoiceConstants.FORMAT_TYPE_ALL, false, null)) {
					stack.rollback();
					return;
				}
			}
		}
		stack.commit();

	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	public ExpressionProvider getExpressionProvider() {
		if (isEnabled()) {
			return new ExpressionProvider(getFirstElementHandle());
		} else {
			return new ExpressionProvider();
		}
	}

	public boolean isEnableHide() {
		Iterator visibilities = getVisibilityRulesIterator();
		if ((visibilities != null) && visibilities.hasNext()) {
			return true;
		} else {
			return false;
		}
	}

	public String getFormat(Object obj) {
		HideRuleHandle ruleHandle = (HideRuleHandle) obj;
		return ruleHandle.getFormat();
	}

	public Expression getExpression(Object obj) {
		HideRuleHandle ruleHandle = (HideRuleHandle) obj;
		return (Expression) ruleHandle.getExpressionProperty(HideRule.VALUE_EXPR_MEMBER).getValue();
	}

	public boolean isFormatTypeAll(String format) {
		if (DesignChoiceConstants.FORMAT_TYPE_ALL.equalsIgnoreCase(format)) {
			return true;
		} else {
			return false;
		}
	}

	public Iterator getVisibilityRulesIterator() {
		return visibilityRulesIterator(getFirstElementHandle());
	}

}
