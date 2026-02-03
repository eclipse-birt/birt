/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * FormatRule.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "FormatRule")
@XmlAccessorType(XmlAccessType.NONE)
public class FormatRule implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "Condition")
	private org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition;
	@XmlElement(name = "Effect")
	private org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect;

	public FormatRule() {
	}

	public FormatRule(org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition,
			org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect) {
		this.condition = condition;
		this.effect = effect;
	}

	/**
	 * Gets the condition value for this FormatRule.
	 *
	 * @return condition
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRuleCondition getCondition() {
		return condition;
	}

	/**
	 * Sets the condition value for this FormatRule.
	 *
	 * @param condition
	 */
	public void setCondition(org.eclipse.birt.report.soapengine.api.FormatRuleCondition condition) {
		this.condition = condition;
	}

	/**
	 * Gets the effect value for this FormatRule.
	 *
	 * @return effect
	 */
	public org.eclipse.birt.report.soapengine.api.FormatRuleEffect getEffect() {
		return effect;
	}

	/**
	 * Sets the effect value for this FormatRule.
	 *
	 * @param effect
	 */
	public void setEffect(org.eclipse.birt.report.soapengine.api.FormatRuleEffect effect) {
		this.effect = effect;
	}

	private java.lang.Object __equalsCalc = null;

	@Override
	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof FormatRule)) {
			return false;
		}
		FormatRule other = (FormatRule) obj;
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true
				&& ((this.condition == null && other.getCondition() == null)
						|| (this.condition != null && this.condition.equals(other.getCondition())))
				&& ((this.effect == null && other.getEffect() == null)
						|| (this.effect != null && this.effect.equals(other.getEffect())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	@Override
	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getCondition() != null) {
			_hashCode += getCondition().hashCode();
		}
		if (getEffect() != null) {
			_hashCode += getEffect().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	}
