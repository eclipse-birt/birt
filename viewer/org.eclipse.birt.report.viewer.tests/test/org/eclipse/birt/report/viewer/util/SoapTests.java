package org.eclipse.birt.report.viewer.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.birt.report.soapengine.api.GetUpdatedObjects;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class SoapTests {
	@Test
	public void parse()
			throws XPathExpressionException, JAXBException, SAXException, IOException, ParserConfigurationException {
		String xml = """
				<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
				<soap:Body>
				<GetUpdatedObjects xmlns="http://schemas.eclipse.org/birt">
				<Operation>
				<Target>
					<Id>Document</Id>
					<Type>Document</Type>
				</Target>
				<Operator>ChangeParameter</Operator>
				<Oprand>
					<Name>RP_AllowedEntities</Name>
					<Value>ALL</Value>
				</Oprand>
				<Oprand>
					<Name>__isdisplay__RP_AllowedEntities</Name>
					<Value>ALL</Value>
				</Oprand>
				<Oprand>
					<Name>RP_Creator</Name>
					<Value>default</Value>
				</Oprand>
				<Oprand>
					<Name>__isdisplay__RP_Creator</Name>
					<Value>default</Value>
				</Oprand>
				<Oprand>
					<Name>RP_UserID</Name>
					<Value>sv2</Value>
				</Oprand>
				<Oprand>
					<Name>__isdisplay__RP_UserID</Name>
					<Value>sv2</Value>
				</Oprand><Oprand><Name>RP_BankID</Name><Value>BKAUATWW</Value></Oprand>
				<Oprand><Name>__isdisplay__RP_BankID</Name><Value>BKAUATWW</Value></Oprand>
				<Oprand><Name>RP_Style</Name><Value>at.msit.banks.portal.style.UCStyle</Value></Oprand>
				<Oprand><Name>__isdisplay__RP_Style</Name><Value>at.msit.banks.portal.style.UCStyle</Value></Oprand>
				<Oprand><Name>RP_AllowReportingOnUsers</Name><Value>false</Value></Oprand>
				<Oprand><Name>__isdisplay__RP_AllowReportingOnUsers</Name><Value>false</Value></Oprand>
				<Oprand><Name>__svg</Name><Value>true</Value></Oprand>
				<Oprand><Name>__page</Name><Value>1</Value></Oprand>
				<Oprand><Name>__taskid</Name><Value>2026-0-7-18-12-42-939</Value></Oprand>
				</Operation>
				</GetUpdatedObjects>
				</soap:Body>
				</soap:Envelope>
				""";

		JAXBContext ctx = JAXBContext.newInstance(GetUpdatedObjects.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();

		InputSource inputSource = new InputSource(new StringReader(xml));

		// JAXB unmarshal
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().parse(inputSource);

// XPath na GetUpdatedObjects
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NamespaceContext() {
			@Override
			public String getNamespaceURI(String prefix) {
				return switch (prefix) {
				case "birt" -> "http://schemas.eclipse.org/birt";
				case "soap" -> "http://schemas.xmlsoap.org/soap/envelope/";
				default -> XMLConstants.NULL_NS_URI;
				};
			}

			@Override
			public String getPrefix(String uri) {
				return null;
			}

			@Override
			public Iterator<String> getPrefixes(String uri) {
				return null;
			}
		});

		Node getUpdatedObjectsNode = (Node) xpath.evaluate("/soap:Envelope/soap:Body/birt:GetUpdatedObjects", doc,
				XPathConstants.NODE);

		GetUpdatedObjects response = (GetUpdatedObjects) unmarshaller.unmarshal(getUpdatedObjectsNode);
		System.out.println("Operations: " + response.getOperation());
	}
}