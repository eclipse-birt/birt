package org.eclipse.birt.report.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.birt.report.soapengine.api.GetUpdatedObjects;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

public class BirtSoapParser {

	/**
	 * Unmershall SOAP request and return GetUpdatedObjects.
	 *
	 *
	 * <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
	 * <soap:Body> <GetUpdatedObjects xmlns="http://schemas.eclipse.org/birt"> ...
	 * </GetUpdatedObjects> </soap:Body> </soap:Envelope>
	 *
	 * @param request HttpServletRequest zo servletu
	 * @return GetUpdatedObjects pripravený pre BirtSoapBindingImpl
	 * @throws IOException
	 * @throws XMLStreamException
	 * @throws JAXBException
	 */
	public static GetUpdatedObjects parseGetUpdatedObjects(HttpServletRequest request)
			throws XPathExpressionException, SAXException, ParserConfigurationException, JAXBException, IOException {
		JAXBContext ctx = JAXBContext.newInstance(GetUpdatedObjects.class);
		Unmarshaller unmarshaller = ctx.createUnmarshaller();

		try (InputStream is = request.getInputStream()) {

			// JAXB unmarshal
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			Document doc = dbf.newDocumentBuilder().parse(is);

			// XPath to GetUpdatedObjects
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
			    @Override public String getPrefix(String uri) { return null; }
			    @Override public Iterator<String> getPrefixes(String uri) { return null; }
			});

			Node getUpdatedObjectsNode = (Node) xpath.evaluate(
			    "/soap:Envelope/soap:Body/birt:GetUpdatedObjects",
			    doc,
			    XPathConstants.NODE
			);

			return (GetUpdatedObjects) unmarshaller.unmarshal(getUpdatedObjectsNode);
		}
	}
}
