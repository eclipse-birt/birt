/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;

/**
 * @author Actuate Corporation
 *  
 */
public class FillChooserComposite extends Composite implements SelectionListener, MouseListener, DisposeListener,
    KeyListener
{

    private transient Composite cmpContentInner = null;

    private transient Composite cmpContentOuter = null;

    private transient Composite cmpDropDown = null;

    private transient FillCanvas cnvSelection = null;

    private transient Button btnDown = null;

    private transient Label lblCurrentTransparency = null;

    private transient Slider srTransparency = null;

    private transient Button btnCustom = null;

    private transient Button btnGradient = null;

    private transient Button btnImage = null;

    private static Color[] colorArray = null;

    private final String[] saImageTypes = new String[]
    {
        "*.gif", "*.jpg", "*.png"
    };

    private transient boolean bGradientEnabled = true;

    private transient boolean bImageEnabled = true;

    private transient Fill fCurrent = null;

    private transient boolean bTransparencyChanged = false;

    private transient int iTransparency = 0;

    private transient Vector vListeners = null;

    public static final int FILL_CHANGED_EVENT = 1;

    public static final int MOUSE_CLICKED_EVENT = 2;

    private transient int iSize = 20;

    /**
     * @param parent
     * @param style
     */
    public FillChooserComposite(Composite parent, int style, Fill fCurrent, boolean bEnableGradient,
        boolean bEnableImage)
    {
        super(parent, style);
        this.fCurrent = fCurrent;
        this.bGradientEnabled = bEnableGradient;
        this.bImageEnabled = bEnableImage;
        init();
        placeComponents();
    }

    /**
     *  
     */
    private void init()
    {
        this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
        Display display = Display.getDefault();
        colorArray = this.createColorMap(display);
        vListeners = new Vector();
    }

    /**
     *  
     */
    private void placeComponents()
    {
        // THE LAYOUT OF THIS COMPOSITE (FILLS EVERYTHING INSIDE IT)
        FillLayout flMain = new FillLayout();
        flMain.marginHeight = 0;
        flMain.marginWidth = 0;
        setLayout(flMain);

        // THE LAYOUT OF THE OUTER COMPOSITE (THAT GROWS VERTICALLY BUT ANCHORS
        // ITS CONTENT NORTH)
        cmpContentOuter = new Composite(this, SWT.NONE);
        GridLayout glContentOuter = new GridLayout();
        glContentOuter.verticalSpacing = 0;
        glContentOuter.horizontalSpacing = 0;
        glContentOuter.marginHeight = 0;
        glContentOuter.marginWidth = 0;
        glContentOuter.numColumns = 1;
        cmpContentOuter.setLayout(glContentOuter);
        GridData gdContentOuter = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentOuter.setLayoutData(gdContentOuter);

        // THE LAYOUT OF THE INNER COMPOSITE (ANCHORED NORTH AND ENCAPSULATES
        // THE CANVAS + BUTTON)
        cmpContentInner = new Composite(cmpContentOuter, SWT.BORDER);
        GridLayout glContentInner = new GridLayout();
        glContentInner.verticalSpacing = 0;
        glContentInner.horizontalSpacing = 0;
        glContentInner.marginHeight = 0;
        glContentInner.marginWidth = 0;
        glContentInner.numColumns = 2;
        cmpContentInner.setLayout(glContentInner);
        GridData gdContentInner = new GridData(GridData.FILL_HORIZONTAL);
        cmpContentInner.setLayoutData(gdContentInner);

        // THE CANVAS
        cnvSelection = new FillCanvas(cmpContentInner, SWT.NONE);
        GridData gdCNVSelection = new GridData(GridData.FILL_BOTH);
        gdCNVSelection.heightHint = iSize;
        cnvSelection.setLayoutData(gdCNVSelection);
        cnvSelection.setFill(fCurrent);
        cnvSelection.addMouseListener(this);

        // THE BUTTON
        btnDown = new Button(cmpContentInner, SWT.ARROW | SWT.DOWN);
        GridData gdBDown = new GridData(GridData.FILL);
        gdBDown.verticalAlignment = GridData.BEGINNING;
        gdBDown.widthHint = iSize;
        gdBDown.heightHint = iSize;
        btnDown.setLayoutData(gdBDown);
        btnDown.addSelectionListener(this);

        addDisposeListener(this);
    }

    private Color[] createColorMap(Display display)
    {
        return new Color[]
        {
            new Color(display, 0, 0, 0), new Color(display, 154, 50, 0), new Color(display, 51, 51, 0),
            new Color(display, 0, 50, 0), new Color(display, 0, 50, 100), new Color(display, 0, 0, 128),
            new Color(display, 51, 51, 153), new Color(display, 51, 51, 51),

            new Color(display, 128, 0, 0), new Color(display, 254, 102, 0), new Color(display, 124, 124, 0),
            new Color(display, 0, 128, 0), new Color(display, 0, 128, 128), new Color(display, 0, 0, 254),
            new Color(display, 102, 102, 153), new Color(display, 128, 128, 128),

            new Color(display, 254, 0, 0), new Color(display, 254, 153, 0), new Color(display, 154, 204, 0),
            new Color(display, 51, 153, 102), new Color(display, 51, 204, 204), new Color(display, 51, 102, 254),
            new Color(display, 128, 0, 128), new Color(display, 145, 145, 145),

            new Color(display, 254, 0, 254), new Color(display, 254, 204, 0), new Color(display, 254, 254, 0),
            new Color(display, 0, 254, 0), new Color(display, 0, 254, 254), new Color(display, 0, 204, 254),
            new Color(display, 154, 50, 102), new Color(display, 192, 192, 192),

            new Color(display, 253, 153, 204), new Color(display, 254, 204, 153), new Color(display, 254, 254, 153),
            new Color(display, 204, 254, 204), new Color(display, 204, 254, 254), new Color(display, 153, 204, 254),
            new Color(display, 204, 153, 254), new Color(display, 254, 254, 254)
        };
    }

    /**
     *  
     */
    private void createDropDownComponent(int iXLoc, int iYLoc)
    {
        int iShellHeight = 240;
        int iShellWidth = 160;
        // Reduce the height based on which buttons are to be shown.
        if (!bGradientEnabled)
        {
            iShellHeight = iShellHeight - 30;
        }
        if (!bImageEnabled)
        {
            iShellHeight = iShellHeight - 30;
        }
        Shell shell = new Shell(this.getShell(), SWT.NONE);
        shell.setLayout(new FillLayout());
        shell.setSize(iShellWidth, iShellHeight);
        shell.setLocation(iXLoc, iYLoc);

        cmpDropDown = new Composite(shell, SWT.NONE);
        GridLayout glDropDown = new GridLayout();
        glDropDown.marginHeight = 2;
        glDropDown.marginWidth = 2;
        glDropDown.horizontalSpacing = 1;
        glDropDown.verticalSpacing = 1;
        glDropDown.numColumns = 8;
        cmpDropDown.setLayout(glDropDown);
        cmpDropDown.addKeyListener(this);

        if (colorArray == null)
        {
            colorArray = createColorMap(getDisplay());
        }
        ColorSelectionCanvas cnv = new ColorSelectionCanvas(cmpDropDown, SWT.BORDER, colorArray);
        GridData gdCnv = new GridData(GridData.FILL_BOTH);
        gdCnv.horizontalSpan = 8;
        gdCnv.heightHint = 100;
        cnv.setLayoutData(gdCnv);
        cnv.addMouseListener(this);
        if (this.fCurrent instanceof ColorDefinition)
        {
            cnv.setColor(new Color(this.getDisplay(), ((ColorDefinition) fCurrent).getRed(),
                ((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue()));
        }

        // Layout for Transparency Composite
        GridLayout glTransparency = new GridLayout();
        glTransparency.numColumns = 2;
        glTransparency.horizontalSpacing = 5;
        glTransparency.verticalSpacing = 3;
        glTransparency.marginHeight = 0;
        glTransparency.marginWidth = 0;

        Composite cmpTransparency = new Composite(cmpDropDown, SWT.NONE);
        GridData gdTransparency = new GridData(GridData.FILL_BOTH);
        gdTransparency.heightHint = 40;
        gdTransparency.horizontalSpan = 8;
        cmpTransparency.setLayoutData(gdTransparency);
        cmpTransparency.setLayout(glTransparency);

        Label lblTransparency = new Label(cmpTransparency, SWT.NONE);
        GridData gdLBLTransparency = new GridData(GridData.FILL_HORIZONTAL);
        gdLBLTransparency.horizontalIndent = 2;
        lblTransparency.setLayoutData(gdLBLTransparency);
        lblTransparency.setText("Transparency:");

        lblCurrentTransparency = new Label(cmpTransparency, SWT.NONE);
        GridData gdLBLCurrentTransparency = new GridData();
        gdLBLCurrentTransparency.widthHint = 40;
        lblCurrentTransparency.setLayoutData(gdLBLCurrentTransparency);

        srTransparency = new Slider(cmpTransparency, SWT.HORIZONTAL);
        GridData gdTransparent = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
        gdTransparent.heightHint = 20;
        gdTransparent.horizontalSpan = 2;
        srTransparency.setLayoutData(gdTransparent);
        if (fCurrent == null)
        {
            srTransparency.setSelection(0);
            srTransparency.setEnabled(false);
        }
        else
        {
            int iValue = 0;
            if (fCurrent instanceof ColorDefinition)
            {
                iValue = ((ColorDefinition) fCurrent).getTransparency();
            }
            if (fCurrent instanceof Gradient)
            {
                iValue = ((Gradient) fCurrent).getTransparency();
            }
            srTransparency.setValues(iValue, 0, 256, 1, 1, 10);
        }
        lblCurrentTransparency.setText(String.valueOf(srTransparency.getSelection()));
        srTransparency.setToolTipText(String.valueOf(srTransparency.getSelection()));
        srTransparency.addSelectionListener(this);

        if (this.bGradientEnabled)
        {
            btnGradient = new Button(cmpDropDown, SWT.NONE);
            GridData gdGradient = new GridData(GridData.FILL_BOTH);
            gdTransparent.heightHint = 26;
            gdGradient.horizontalSpan = 8;
            btnGradient.setLayoutData(gdGradient);
            btnGradient.setText("Gradient...");
            btnGradient.addSelectionListener(this);
        }

        btnCustom = new Button(cmpDropDown, SWT.NONE);
        GridData gdCustom = new GridData(GridData.FILL_BOTH);
        gdTransparent.heightHint = 26;
        gdCustom.horizontalSpan = 8;
        btnCustom.setLayoutData(gdCustom);
        btnCustom.setText("Custom Color...");
        btnCustom.addSelectionListener(this);

        if (this.bImageEnabled)
        {
            btnImage = new Button(cmpDropDown, SWT.NONE);
            GridData gdImage = new GridData(GridData.FILL_BOTH);
            gdTransparent.heightHint = 26;
            gdImage.horizontalSpan = 8;
            btnImage.setLayoutData(gdImage);
            btnImage.setText("Image...");
            btnImage.addSelectionListener(this);
        }
        shell.layout();
        shell.open();
    }

    public void setFill(Fill fill)
    {
        fCurrent = fill;
        cnvSelection.setFill(fill);
        cnvSelection.redraw();
    }

    public Fill getFill()
    {
        if (fCurrent == null)
        {
            return ColorDefinitionImpl.TRANSPARENT();
        }
        return this.fCurrent;
    }

    public Point getPreferredSize()
    {
        return new Point(160, 24);
    }

    public void addListener(Listener listener)
    {
        vListeners.add(listener);
    }

    private void toggleDropDown()
    {
        if (cmpDropDown == null || cmpDropDown.isDisposed() || !cmpDropDown.isVisible())
        {
            Point pLoc = UIHelper.getScreenLocation(cnvSelection);
            createDropDownComponent(pLoc.x, pLoc.y + cnvSelection.getSize().y + 1);
        }
        else
        {
            cmpDropDown.getParent().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent e)
    {
        Object oSource = e.getSource();
        if (oSource.equals(btnDown))
        {
            fireHandleEvent(MOUSE_CLICKED_EVENT);
            toggleDropDown();
        }
        else if (oSource.equals(this.btnImage))
        {
            FileDialog fDlg = new FileDialog(this.getShell(), SWT.OPEN);
            cmpDropDown.getParent().dispose();
            fDlg.setFilterExtensions(saImageTypes);
            String sStartFolder = System.getProperty("user.dir");
            String sImageFile = "";
            if (fCurrent instanceof Image)
            {
                String sFullPath = ((Image) fCurrent).getURL().toString();
                sImageFile = sFullPath.substring(sFullPath.lastIndexOf("/") + 1);
                sStartFolder = sFullPath.substring(0, sFullPath.lastIndexOf("/"));
            }
            fDlg.setFilterPath(sStartFolder);
            fDlg.setFileName(sImageFile);
            String sImgPath = fDlg.open();

            // Do nothing if dialog was cancelled
            if (sImgPath == null)
            {
                return;
            }

            try
            {
                new URL(sImgPath);
            }
            catch (MalformedURLException e1 )
            {
                sImgPath = "file:///" + fDlg.getFilterPath() + File.separator + fDlg.getFileName();
            }
            if (sImgPath != null && sImgPath.trim().length() > 0)
            {
                Image imgFill = AttributeFactory.eINSTANCE.createImage();
                imgFill.setURL(sImgPath);
                this.setFill(imgFill);
                fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            }
        }
        else if (oSource.equals(this.btnCustom))
        {
            ColorDialog cDlg = new ColorDialog(this.getShell(), SWT.NONE);
            cmpDropDown.getParent().dispose();
            int iTrans = 0;
            if (fCurrent instanceof ColorDefinition)
            {
                iTransparency = ((ColorDefinition) fCurrent).getTransparency();
                cDlg.setRGB(new RGB(((ColorDefinition) this.fCurrent).getRed(), ((ColorDefinition) this.fCurrent)
                    .getGreen(), ((ColorDefinition) this.fCurrent).getBlue()));
            }
            cDlg.open();
            RGB rgb = cDlg.getRGB();
            if (rgb != null)
            {
                ColorDefinition cdNew = AttributeFactory.eINSTANCE.createColorDefinition();
                cdNew.set(rgb.red, rgb.green, rgb.blue);
                cdNew.setTransparency((bTransparencyChanged) ? this.iTransparency : iTrans);
                this.setFill(cdNew);
                fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            }
        }
        else if (oSource.equals(this.btnGradient))
        {
            GradientEditorDialog ged = null;
            cmpDropDown.getParent().dispose();
            if (fCurrent instanceof Gradient)
            {
                ged = new GradientEditorDialog(this.getShell(), (Gradient) fCurrent);
            }
            else
            {
                ged = new GradientEditorDialog(this.getShell(), null);
            }
            if (ged.getGradient() != null)
            {
                Fill fTmp = ged.getGradient();
                if (fCurrent == null || !(fCurrent.equals(fTmp)))
                {
                    this.setFill(fTmp);
                    fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
                }
            }
        }
        else if (oSource.equals(srTransparency))
        {
            iTransparency = srTransparency.getSelection();
            lblCurrentTransparency.setText(String.valueOf(iTransparency));
            bTransparencyChanged = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent e)
    {
    }

    private void fireHandleEvent(int iType)
    {
        for (int iL = 0; iL < vListeners.size(); iL++)
        {
            Event se = new Event();
            se.widget = this;
            se.data = (fCurrent != null) ? fCurrent : ColorDefinitionImpl.TRANSPARENT();
            se.type = iType;
            ((Listener) vListeners.get(iL)).handleEvent(se);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDoubleClick(MouseEvent e)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseDown(MouseEvent e)
    {
        fireHandleEvent(MOUSE_CLICKED_EVENT);
        if (e.getSource().equals(cnvSelection))
        {
            toggleDropDown();
        }
        else if (e.getSource() instanceof ColorSelectionCanvas)
        {
            ColorDefinition cTmp = AttributeFactory.eINSTANCE.createColorDefinition();
            Color clrTmp = ((ColorSelectionCanvas) e.getSource()).getColorAt(e.x, e.y);
            cTmp.set(clrTmp.getRed(), clrTmp.getGreen(), clrTmp.getBlue());
            int iTransparency = 0;
            if (fCurrent instanceof ColorDefinition)
            {
                iTransparency = (bTransparencyChanged) ? this.iTransparency : ((ColorDefinition) fCurrent)
                    .getTransparency();
            }
            cTmp.setTransparency(iTransparency);
            setFill(cTmp);
            fireHandleEvent(FillChooserComposite.FILL_CHANGED_EVENT);
            cmpDropDown.getShell().dispose();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
     */
    public void mouseUp(MouseEvent e)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.DisposeListener#widgetDisposed(org.eclipse.swt.events.DisposeEvent)
     */
    public void widgetDisposed(DisposeEvent e)
    {
        if (colorArray != null)
        {
            for (int iC = 0; iC < colorArray.length; iC++)
            {
                colorArray[iC].dispose();
            }
            colorArray = null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
        if (cmpDropDown != null && !cmpDropDown.getShell().isDisposed())
        {
            if (e.keyCode == SWT.ESC)
            {
                cmpDropDown.getShell().dispose();
            }
            else if (e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
            {
                this.iTransparency = srTransparency.getSelection();
                if (fCurrent instanceof ColorDefinition && bTransparencyChanged)
                {
                    ((ColorDefinition) fCurrent).setTransparency(this.iTransparency);
                }
                this.setFill(fCurrent);
                cmpDropDown.getShell().dispose();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
     */
    public void keyReleased(KeyEvent e)
    {
        // TODO Auto-generated method stub

    }
}

class ColorSelectionCanvas extends Canvas implements PaintListener
{

    Color[] colorMap = null;

    Color colorSelection = null;

    public ColorSelectionCanvas(Composite parent, int iStyle, Color[] colorMap)
    {
        super(parent, iStyle);
        this.addPaintListener(this);
        this.colorMap = colorMap;
    }

    public Color getColor()
    {
        return colorSelection;
    }

    public void setColor(Color color)
    {
        this.colorSelection = color;
    }

    public void paintControl(PaintEvent pe)
    {
        Color cBlack = new Color(this.getDisplay(), 0, 0, 0);
        Color cWhite = new Color(this.getDisplay(), 255, 255, 255);
        GC gc = pe.gc;
        gc.setForeground(cBlack);

        int iCellWidth = this.getSize().x / 8;
        int iCellHeight = this.getSize().y / 5;

        for (int iR = 0; iR < 5; iR++)
        {
            for (int iC = 0; iC < 8; iC++)
            {
                try
                {
                    gc.setBackground(colorMap[(iR * 8) + iC]);
                }
                catch (Throwable e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                gc.fillRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth, iCellHeight);
                // Hilight currently selected color if it exists in this list
                if (colorSelection != null && colorSelection.equals(colorMap[(iR * 8) + iC]))
                {
                    gc.drawRectangle(iC * iCellWidth, iR * iCellHeight, iCellWidth - 2, iCellHeight - 2);
                    gc.setForeground(cWhite);
                    gc.drawRectangle(iC * iCellWidth + 1, iR * iCellHeight + 1, iCellWidth - 3, iCellHeight - 3);
                    gc.setForeground(cBlack);
                }
            }
        }
        cBlack.dispose();
        cWhite.dispose();
        gc.dispose();
    }

    /**
     * This method assumes a color array of 40 colors arranged with equal sizes in a 8x5 grid.
     * 
     * @param x
     * @param y
     * @return
     */
    public Color getColorAt(int x, int y)
    {
        int iCellWidth = this.getSize().x / 8;
        int iCellHeight = this.getSize().y / 5;
        int iHCell = x / iCellWidth;
        int iVCell = y / iCellHeight;
        int iArrayIndex = ((iVCell) * 8) + iHCell;
        return this.colorMap[iArrayIndex];
    }
}