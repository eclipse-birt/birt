package org.apache.poi.xssf.usermodel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.xssf.util.EvilUnclosedBRFixingInputStream;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Node;

import schemasMicrosoftComOfficeOffice.CTIdMap;
import schemasMicrosoftComOfficeOffice.CTLock;
import schemasMicrosoftComOfficeOffice.CTShapeLayout;
import schemasMicrosoftComOfficeOffice.STConnectType;
import schemasMicrosoftComVml.CTFormulas;
import schemasMicrosoftComVml.CTImageData;
import schemasMicrosoftComVml.CTPath;
import schemasMicrosoftComVml.CTShape;
import schemasMicrosoftComVml.CTShapetype;
import schemasMicrosoftComVml.STExt;
import schemasMicrosoftComVml.STStrokeJoinStyle;
import schemasMicrosoftComVml.STTrueFalse;

public class XSSFVMLHeaderFooterImage extends POIXMLDocumentPart {

    private static final QName QNAME_SHAPE_LAYOUT = new QName("urn:schemas-microsoft-com:office:office", "shapelayout");
    private static final QName QNAME_SHAPE_TYPE = new QName("urn:schemas-microsoft-com:vml", "shapetype");
    private static final QName QNAME_SHAPE = new QName("urn:schemas-microsoft-com:vml", "shape");
    
    public enum ImageLocation{
	LEFT_HEADER ("LH"),
	CENTER_HEADER ("CH"),
	RIGHT_HEADER ("RH"),
	LEFT_FOOTER ("LF"),
	CENTER_FOOTER ("CF"),
	RIGHT_FOOTER ("RF"),
	;
	
	private String shapeId;
	
	private ImageLocation(String shapeId) {
	    this.shapeId = shapeId;
	}

	public String getShapeId() {
	    return shapeId;
	}
	
	public static ImageLocation getLeft(boolean isFooter) {
	    return isFooter? LEFT_FOOTER : LEFT_HEADER;
	}
	
	public static ImageLocation getCenter(boolean isFooter) {
	    return isFooter? CENTER_FOOTER : CENTER_HEADER;
	}
	
	public static ImageLocation getRight(boolean isFooter) {
	    return isFooter? RIGHT_FOOTER : RIGHT_HEADER;
	}
	
    }
    

    private List<QName> _qnames = new ArrayList<QName>();
    private List<XmlObject> _items = new ArrayList<XmlObject>();

    private String _shapeTypeId;
    private int _shapeId = 1024;
    private int _spId = 1024;

    private static final Pattern ptrn_shapeId = Pattern.compile("_x0000_s(\\d+)");

    public XSSFVMLHeaderFooterImage() {
	super();
	newHeaderFooterImageVMLDrawing();
    }

    public XSSFVMLHeaderFooterImage(PackagePart part, PackageRelationship rel) throws IOException, XmlException {
	super(part, rel);
	read(getPackagePart().getInputStream());
    }

    protected void read(InputStream is) throws IOException, XmlException {
	XmlObject root = XmlObject.Factory.parse(new EvilUnclosedBRFixingInputStream(is));

	_qnames = new ArrayList<QName>();
	_items = new ArrayList<XmlObject>();
	for (XmlObject obj : root.selectPath("$this/xml/*")) {
	    Node nd = obj.getDomNode();
	    QName qname = new QName(nd.getNamespaceURI(), nd.getLocalName());
	    if (qname.equals(QNAME_SHAPE_LAYOUT)) {
		_items.add(CTShapeLayout.Factory.parse(obj.xmlText()));
	    } else if (qname.equals(QNAME_SHAPE_TYPE)) {
		CTShapetype st = CTShapetype.Factory.parse(obj.xmlText());
		_items.add(st);
		_shapeTypeId = st.getId();
	    } else if (qname.equals(QNAME_SHAPE)) {
		CTShape shape = CTShape.Factory.parse(obj.xmlText());
		String id = shape.getId();
		if (id != null) {
		    Matcher m = ptrn_shapeId.matcher(id);
		    if (m.find())
			_shapeId = Math.max(_shapeId, Integer.parseInt(m.group(1)));
		}
		_items.add(shape);
	    } else {
		_items.add(XmlObject.Factory.parse(obj.xmlText()));
	    }
	    _qnames.add(qname);
	}
    }

    protected List<XmlObject> getItems() {
	return _items;
    }

    private void newHeaderFooterImageVMLDrawing() {
	createShapeLayout();
	createShapetype();
    }

    private void createShapetype() {
	CTShapetype shapetype = CTShapetype.Factory.newInstance();
	_shapeTypeId = "_x0000_t75";
	shapetype.setId(_shapeTypeId);
	shapetype.setCoordsize("21600,21600");
	shapetype.setSpt(75);
	shapetype.setPath2("m@4@5l@4@11@9@11@9@5xe");
	shapetype.setPreferrelative(schemasMicrosoftComOfficeOffice.STTrueFalse.T);
	shapetype.setFilled(STTrueFalse.F);
	shapetype.setStroked(STTrueFalse.F);

	addNewStroke(shapetype);
	addNewFormulas(shapetype);
	addNewPath(shapetype);
	addNewLock(shapetype);
	_items.add(shapetype);
	_qnames.add(QNAME_SHAPE_TYPE);
    }

    private void addNewLock(CTShapetype shapetype) {
	CTLock lock = shapetype.addNewLock();
	lock.setExt(STExt.EDIT);
	lock.setAspectratio(schemasMicrosoftComOfficeOffice.STTrueFalse.T);
    }

    private void addNewPath(CTShapetype shapetype) {
	CTPath path = shapetype.addNewPath();
	path.setGradientshapeok(STTrueFalse.T);
	path.setConnecttype(STConnectType.RECT);
	path.setExtrusionok(schemasMicrosoftComOfficeOffice.STTrueFalse.F);
    }

    private void addNewFormulas(CTShapetype shapetype) {
	CTFormulas formulas = shapetype.addNewFormulas();
	formulas.addNewF().setEqn("if lineDrawn pixelLineWidth 0");
	formulas.addNewF().setEqn("sum @0 1 0");
	formulas.addNewF().setEqn("sum 0 0 @1");
	formulas.addNewF().setEqn("prod @2 1 2");
	formulas.addNewF().setEqn("prod @3 21600 pixelWidth");
	formulas.addNewF().setEqn("prod @3 21600 pixelHeight");
	formulas.addNewF().setEqn("sum @0 0 1");
	formulas.addNewF().setEqn("prod @6 1 2");
	formulas.addNewF().setEqn("prod @7 21600 pixelWidth");
	formulas.addNewF().setEqn("sum @8 21600 0");
	formulas.addNewF().setEqn("prod @7 21600 pixelHeight");
	formulas.addNewF().setEqn("sum @10 21600 0");
    }

    private void addNewStroke(CTShapetype shapetype) {
	shapetype.addNewStroke().setJoinstyle(STStrokeJoinStyle.MITER);
    }

    private void createShapeLayout() {
	CTShapeLayout layout = CTShapeLayout.Factory.newInstance();
	layout.setExt(STExt.EDIT);
	addNewIdmap(layout);
	_items.add(layout);
	_qnames.add(QNAME_SHAPE_LAYOUT);
    }

    private void addNewIdmap(CTShapeLayout layout) {
	CTIdMap idmap = layout.addNewIdmap();
	idmap.setExt(STExt.EDIT);
	idmap.setData("1");
    }

    public CTShape newHeaderFooterImageShape(ImageLocation imageLocation, String imageStyle, String relationId) {
	CTShape shape = CTShape.Factory.newInstance();
	shape.setId(imageLocation.getShapeId());
	shape.setType("#" + _shapeTypeId);
	shape.setStyle(imageStyle);
	shape.setSpid("_x0000_s" + (++_spId));
	addImage(shape, relationId);
	addNewLock(shape);
	_items.add(shape);
	_qnames.add(QNAME_SHAPE);
	return shape;
    }

    private void addImage(CTShape shape, String relationId) {
	CTImageData image = shape.addNewImagedata();
	image.setRelid(relationId);
    }

    private void addNewLock(CTShape shape) {
	CTLock lock = shape.addNewLock();
	lock.setExt(STExt.EDIT);
	lock.setRotation(schemasMicrosoftComOfficeOffice.STTrueFalse.T);
    }

    @Override
    protected void commit() throws IOException {
	PackagePart part = getPackagePart();
	OutputStream out = part.getOutputStream();
	write(out);
	out.close();
    }

    protected void write(OutputStream out) throws IOException {
	XmlObject rootObject = XmlObject.Factory.newInstance();
	XmlCursor rootCursor = rootObject.newCursor();
	rootCursor.toNextToken();
	rootCursor.beginElement("xml");

	for (int i = 0; i < _items.size(); i++) {
	    XmlCursor xc = _items.get(i).newCursor();
	    rootCursor.beginElement(_qnames.get(i));
	    while (xc.toNextToken() == XmlCursor.TokenType.ATTR) {
		Node anode = xc.getDomNode();
		rootCursor.insertAttributeWithValue(anode.getLocalName(), anode.getNamespaceURI(), anode.getNodeValue());
	    }
	    xc.toStartDoc();
	    xc.copyXmlContents(rootCursor);
	    rootCursor.toNextToken();
	    xc.dispose();
	}
	rootCursor.dispose();

	XmlOptions xmlOptions = new XmlOptions(DEFAULT_XML_OPTIONS);
	xmlOptions.setSavePrettyPrint();
	HashMap<String, String> map = new HashMap<String, String>();
	map.put("urn:schemas-microsoft-com:vml", "v");
	map.put("urn:schemas-microsoft-com:office:office", "o");
	map.put("urn:schemas-microsoft-com:office:excel", "x");
	xmlOptions.setSaveSuggestedPrefixes(map);

	rootObject.save(out, xmlOptions);
    }

}
