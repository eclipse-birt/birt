package org.eclipse.birt.report.soapengine.api;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "BRDExpression")
@XmlAccessorType(XmlAccessType.FIELD)
public class BRDExpression {
	@XmlElement(name = "Expression")
	private String expression;
	@XmlElement(name = "IsValid")
	private Boolean isValid;
	@XmlElement(name = "ParserError")
	private String parserError;

	public BRDExpression() {
	}

	public BRDExpression(String expression, Boolean isValid, String parserError) {
		this.expression = expression;
		this.isValid = isValid;
		this.parserError = parserError;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}

	public String getParserError() {
		return parserError;
	}

	public void setParserError(String parserError) {
		this.parserError = parserError;
	}
}
