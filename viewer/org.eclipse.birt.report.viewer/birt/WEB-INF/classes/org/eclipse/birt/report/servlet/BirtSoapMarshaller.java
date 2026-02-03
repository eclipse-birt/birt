package org.eclipse.birt.report.servlet;

import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

public class BirtSoapMarshaller {

	/**
	 * Marshal GetUpdatedObjectsResponse into SOAP envelope and send
	 * response.
	 *
	 * @param responseObj GetUpdatedObjectsResponse
	 * @param httpResp    HttpServletResponse
	 * @throws Exception
	 */
	public static void marshalResponse(GetUpdatedObjectsResponse responseObj, HttpServletResponse httpResp)
			throws Exception {

		StringWriter buffer = new StringWriter();
		XMLOutputFactory xof = XMLOutputFactory.newFactory();
		XMLStreamWriter xsw = xof.createXMLStreamWriter(buffer);

		xsw.writeStartDocument("UTF-8", "1.0");

//		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"><soapenv:Body>
		xsw.writeStartElement("soapenv", "Envelope", "http://schemas.xmlsoap.org/soap/envelope/");
		xsw.writeNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		xsw.writeNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
		xsw.writeNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

		xsw.writeStartElement("soapenv", "Body", "http://schemas.xmlsoap.org/soap/envelope/");

		// JAXB marshall directly into XMLStreamWriter
		JAXBContext ctx = JAXBContext.newInstance(GetUpdatedObjectsResponse.class);
		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		marshaller.marshal(responseObj, xsw);

		// end of SOAP Body and Envelope
		xsw.writeEndElement(); // Body
		xsw.writeEndElement(); // Envelope
		xsw.writeEndDocument();

		xsw.flush();

		httpResp.reset();
		byte[] bytes = buffer.toString().getBytes(StandardCharsets.UTF_8);
		httpResp.setContentLength(bytes.length);
		httpResp.setCharacterEncoding("UTF-8");
		httpResp.setContentType("text/xml");

		try (OutputStream os = httpResp.getOutputStream()) {
			os.write(bytes);
			os.flush();
			httpResp.flushBuffer();
		}
	}
}
