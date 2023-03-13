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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TemplateDataSet;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateFactory;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.ITemplateParameterDefinitionModel;
import org.eclipse.birt.report.model.elements.strategy.CopyForTemplatePolicy;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.ContentExceptionFactory;
import org.eclipse.birt.report.model.util.LevelContentIterator;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * This class replaces a template element with a report item or data set,
 * replace a report item or data set with a template element and do some checks
 * about the replacements.
 */

public class TemplateCommand extends AbstractElementCommand {

	/**
	 * The container information.
	 */
	protected final ContainerContext focus;

	/**
	 * Constructor.
	 *
	 * @param module the module
	 * @param obj    the element to modify.
	 */

	public TemplateCommand(Module module, DesignElement obj) {
		super(module, obj);
		this.focus = null;
	}

	/**
	 *
	 * @param module
	 * @param containerInfor
	 */
	public TemplateCommand(Module module, ContainerContext containerInfor) {
		super(module, containerInfor.getElement());
		this.focus = containerInfor;
	}

	/**
	 * Checks the <code>REF_TEMPLATE_PARAMETER_PROP</code> of template elements to
	 * avoid that tit refers a non-exsiting template parameter definition or a wrong
	 * type definition.
	 *
	 * @param prop  the definition of property
	 *              <code>REF_TEMPLATE_PARAMETER_PROP</code> in template elements
	 * @param value the new value to set
	 * @throws PropertyValueException throws exception if it refers non-exsiting
	 *                                template parameter definition or a wrong type
	 *                                definition
	 */

	public void checkProperty(ElementPropertyDefn prop, Object value) throws PropertyValueException {
		if (value == null) {
			return;
		}

		// element is a template element, the referred template definition can
		// not be set to a non-exsiting parameter definition or a wrong type
		// definition

		if ((element instanceof ReportItem || element instanceof TemplateReportItem)
				&& IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals(prop.getName())) {
			assert value instanceof ElementRefValue;
			if (((ElementRefValue) value).getElement() == null) {
				TemplateParameterDefinition templateParam = resolveTemplateParameterDefinition(module,
						((ElementRefValue) value).getName());

				if (!(templateParam.getDefaultElement() instanceof ReportItem)) {
					throw new PropertyValueException(element, prop.getName(), value,
							PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
				}
			}
		}

		if ((element instanceof SimpleDataSet || element instanceof TemplateDataSet)
				&& IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP.equals(prop.getName())) {
			assert value instanceof ElementRefValue;
			if (((ElementRefValue) value).getElement() == null) {
				TemplateParameterDefinition templateParam = resolveTemplateParameterDefinition(module,
						((ElementRefValue) value).getName());

				if (!(templateParam.getDefaultElement() instanceof SimpleDataSet)) {
					throw new PropertyValueException(element, prop.getName(), value,
							PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE);
				}
			}
		}
	}

	/**
	 * Resolve the property value of <code>REF_TEMPLATE_PARAMETER_PROP</code> in a
	 * template element.
	 *
	 * @param module the module of the template element
	 * @param name   the name to resolve
	 * @return the element reference value
	 */

	private TemplateParameterDefinition resolveTemplateParameterDefinition(Module module, String name) {
		PropertyDefn prop = element.getPropertyDefn(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP);
		if (prop == null) {
			return null;
		}

		DesignElement resolvedElement = module.resolveElement(element, name, prop, null);
		return (TemplateParameterDefinition) resolvedElement;
	}

	/**
	 * Checks the validation of the template element to insert. Verify that the
	 * template element to insert refers a valid and exsiting template parameter
	 * definition. If the referred the template definition doesn't exsit, then
	 * return the template parameter definition to add it to the module.
	 *
	 * @param content
	 * @param slotID
	 * @throws ContentException if the value of property
	 *                          <code>REF_TEMPLATE_PARAMETER_PROP</code> in template
	 *                          element is null, or an un-resolved value
	 */

	public void checkAdd(Object content) throws ContentException {
		Object obj = null;
		if (content instanceof DesignElement) {
			DesignElement element = (DesignElement) content;
			obj = element.getProperty(module, IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP);
			if (obj == null) {
				LevelContentIterator iter = new LevelContentIterator(module, element, 1);
				while (iter.hasNext()) {
					Object eleObj = iter.next();
					checkAdd(eleObj);
				}
			} else {
				addTemplateParameterDefinition(obj, (DesignElement) content);
			}
		}
	}

	/**
	 * If the template parameter reference is resolved and the definition is not
	 * inserted to the module, then clone it and add the cloned definition into
	 * module. The content may be a TemplateElement or a ReportItem.
	 *
	 * @param obj
	 * @param content
	 * @param slotID
	 * @throws ContentException
	 */

	private void addTemplateParameterDefinition(Object obj, DesignElement content) throws ContentException {
		assert obj instanceof ElementRefValue;
		ElementRefValue templateParam = (ElementRefValue) obj;
		if (templateParam.getElement() == null) {
			// a template element must define an explicit parameter definition
			if (content instanceof TemplateElement) {
				throw ContentExceptionFactory.createContentException(focus, content,
						ContentException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT);
			}
			try {
				PropertyCommand cmd = new PropertyCommand(module, content);
				cmd.clearProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP);
			} catch (SemanticException e) {
				assert false;
			}
			return;
		} else if (!(content instanceof TemplateElement)) {
			try {
				if (needClearParameterDefinition(content, templateParam.getName())) {
					PropertyCommand cmd = new PropertyCommand(module, content);
					cmd.clearProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP);
				}
			} catch (SemanticException e) {
				assert false;
			}
			return;
		}

		TemplateParameterDefinition definition = (TemplateParameterDefinition) templateParam.getElement();
		if (module.findTemplateParameterDefinition(templateParam.getName()) != definition) {
			try {
				DesignElement copyTemplateParam = ModelUtil.getCopy(definition);
				module.makeUniqueName(copyTemplateParam);
				ContentCommand cmd = new ContentCommand(module,
						new ContainerContext(module, IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT));
				cmd.add(copyTemplateParam);
				PropertyCommand propertyCmd = new PropertyCommand(module, content);
				propertyCmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, copyTemplateParam);

			} catch (SemanticException e) {
				assert false;
			}
		}
	}

	/**
	 * Determines whether to clear the parameter definition defined in the content.
	 *
	 * @param content                 the content to determine
	 * @param parameterDefinitionName the parameter definition name defined in the
	 *                                content
	 * @return true if need to clear the parameter definition, otherwise false
	 */

	private boolean needClearParameterDefinition(DesignElement content, String parameterDefinitionName) {
		assert content != null && parameterDefinitionName != null;
		TemplateParameterDefinition templateParam = module.findTemplateParameterDefinition(parameterDefinitionName);

		// if the parameter definition is found in the module, the allowed type
		// is consistent with the current content instance, then let it be and
		// not clear the parameter definition defined in the content

		if (templateParam != null) {
			String type = templateParam.getAllowedType(module);
			IElementDefn allowedDefn = null;
			if (DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_EXTENDED_ITEM.equals(type)) {
				allowedDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.REPORT_ITEM);
			} else {
				allowedDefn = MetaDataDictionary.getInstance().getElement(type);
			}
			if (content.getDefn().isKindOf(allowedDefn)) {
				return false;
			}
		}

		// otherwise: 1) the parameter definition is not found in the module; or
		// 2) the definition is found but content instance is not kind of the
		// allowed type; then clear the parameter definition

		return true;
	}

	/**
	 * Creates a template element and replace the content element with this created
	 * template element. In this method, create a template definition based on the
	 * given content element and add it to the report design first. Then, let the
	 * created template element refer the added template parameter definition.
	 *
	 * @param base   the base report item or data set element to be transformed to a
	 *               template element
	 * @param slotID the slot of the container
	 * @param name   the given name of the created template element
	 * @return the created template element
	 * @throws SemanticException if the content can not be transformed to a template
	 *                           element, current module is not a report design and
	 *                           can not support template elements, or the
	 *                           replacement fails
	 */

	public TemplateElement createTemplateElement(DesignElement base, String name) throws SemanticException {
		assert base != null;

		TemplateElement template = TemplateFactory.createTemplate(module, base, name);

		// if content element is not a report
		// item or data set, then the operation is forbidden

		if (template == null) {
			throw new TemplateException(base, TemplateException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE);
		}
		if (!(module instanceof ReportDesign)) {
			throw new TemplateException(module, TemplateException.DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED);
		}

		ActivityStack stack = getActivityStack();
		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CREATE_TEMPLATE_ELEMENT_MESSAGE));

		try {
			createTemplateFromDesignElement(template, base);

			ContentCommand cmd = new ContentCommand(module, focus);
			cmd.transformTemplate(base, template, true);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

		return template;

	}

	/**
	 * Creates a template definition based on the "base" element, add it to the
	 * report design and let the given template element handle refer the template
	 * definition.
	 *
	 * @param template the template element
	 * @param base     the base element to create a template parameter definition
	 */

	private void createTemplateFromDesignElement(TemplateElement template, DesignElement base) {
		assert base instanceof ReportItem || base instanceof SimpleDataSet;
		assert template != null;
		assert module instanceof ReportDesign;

		// create a template parameter definition element and make a unique name
		// for it

		try {
			TemplateParameterDefinition templateParam = new TemplateParameterDefinition();
			module.makeUniqueName(templateParam);

			// get the handle to do the next operations and add it to the module

			PropertyCommand propertyCmd = new PropertyCommand(module, templateParam);
			if (base instanceof SimpleDataSet) {
				propertyCmd.setProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP,
						DesignChoiceConstants.TEMPLATE_ELEMENT_TYPE_DATA_SET);
			} else {
				propertyCmd.setProperty(ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP, base.getElementName());
			}

			ContainerSlot defaultSlot = templateParam.getSlot(ITemplateParameterDefinitionModel.DEFAULT_SLOT);
			assert defaultSlot != null;

			// clone the base element and add it to the default slot

			DesignElement defaultElement;
			defaultElement = (DesignElement) base.doClone(CopyForTemplatePolicy.getInstance());

			assert defaultElement != null;
			ContentCommand contentCmd;

			// if the default element has a referred template definition, clear
			// the value and delete the referred definition

			TemplateParameterDefinition temp = defaultElement.getTemplateParameterElement(module);
			if (temp != null) {
				propertyCmd = new PropertyCommand(module, defaultElement);
				propertyCmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, null);
				propertyCmd = new PropertyCommand(module, base);
				propertyCmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, null);
			}

			contentCmd = new ContentCommand(module,
					new ContainerContext(templateParam, ITemplateParameterDefinitionModel.DEFAULT_SLOT));
			contentCmd.add(defaultElement);

			contentCmd = new ContentCommand(module,
					new ContainerContext(module, IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT));
			contentCmd.add(templateParam);

			// let the template handle refer the template parameter definition

			propertyCmd = new PropertyCommand(module, template);
			propertyCmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, templateParam.getFullName());
		} catch (SemanticException e) {
			assert false;
		} catch (CloneNotSupportedException e) {
			assert false;
		}

	}

	/**
	 * Transforms the given template report item to a report item with the given
	 * real report item.
	 *
	 * @param templateItem the template report item to be transformed
	 * @param reportItem   the real report item to transform
	 * @throws SemanticException
	 */

	public void transformToReportItem(TemplateReportItem templateItem, ReportItem reportItem) throws SemanticException {
		// if the template report item has no template definition, it can not be
		// transformed to a report item

		TemplateParameterDefinition templateparam = templateItem.getTemplateParameterElement(module);
		if (templateparam == null) {
			throw new TemplateException(templateItem,
					TemplateException.DESIGN_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN);
		}

		module.rename(reportItem);

		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.TRANSFORM_TO_REPORT_ITEM_MESSAGE));

		try {
			PropertyCommand pcmd = new PropertyCommand(module, reportItem);
			pcmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, templateparam.getFullName());

			ContentCommand cmd = new ContentCommand(module, focus);
			cmd.transformTemplate(templateItem, reportItem, false);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

	}

	/**
	 * Transforms the given template data set to a data set with the given real data
	 * set.
	 *
	 * @param templateDataSet the template data set to be transformed
	 * @param dataSet         the real data set to transform
	 * @throws SemanticException
	 */

	public void transformToDataSet(TemplateDataSet templateDataSet, SimpleDataSet dataSet) throws SemanticException {
		// if the template data set has no template definition, it can not be
		// transformed to a data set

		TemplateParameterDefinition templateparam = templateDataSet.getTemplateParameterElement(module);
		if (templateparam == null) {
			throw new TemplateException(templateDataSet,
					TemplateException.DESIGN_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN);
		}

		ActivityStack stack = getActivityStack();

		stack.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.TRANSFORM_TO_DATA_SET_MESSAGE));

		try {
			PropertyCommand pcmd = new PropertyCommand(module, dataSet);
			pcmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, templateparam.getFullName());

			ContentCommand cmd = new ContentCommand(module, focus);
			cmd.transformTemplate(templateDataSet, dataSet, false);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

	}

	/**
	 * Reverts a report item or data set to a template element. In this method,
	 * create a template element and let the created template element refer the
	 * template parameter definition of the base element.
	 *
	 * @param base the base report item or data set element to be reverted to a
	 *             template element
	 * @param name the given name of the created template element
	 * @return the created template element
	 * @throws SemanticException if the content can not be revert to a template
	 *                           element, current module is not a repor design and
	 *                           can not support template elements, base element has
	 *                           no template definition, or the replacement fails
	 */

	public TemplateElement revertToTemplate(DesignElement base, String name) throws SemanticException {
		assert base != null;

		TemplateElement template = TemplateFactory.createTemplate(module, base, name);

		// if content element is not a report
		// item or data set, then the operarion is forbidden

		if (template == null) {
			throw new TemplateException(base, TemplateException.DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE);
		}
		if (!(module instanceof ReportDesign)) {
			throw new TemplateException(module, TemplateException.DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED);
		}

		// if the design element has no template definition, it can not be
		// reverted.

		TemplateParameterDefinition templateParam = base.getTemplateParameterElement(module);
		if (templateParam == null) {
			throw new TemplateException(base, TemplateException.DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN);
		}
		try {
			PropertyCommand propertyCmd = new PropertyCommand(module, template);
			propertyCmd.setProperty(IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP, templateParam.getFullName());
		} catch (SemanticException e) {
			assert false;
		}

		ContentCommand cmd = new ContentCommand(module, focus);
		cmd.transformTemplate(base, template, false);

		return template;
	}

}
