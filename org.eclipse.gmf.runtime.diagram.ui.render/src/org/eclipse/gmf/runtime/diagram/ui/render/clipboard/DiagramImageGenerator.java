/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002 - 2005.  All Rights Reserved.             |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.render.clipboard;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import org.eclipse.gmf.runtime.common.core.util.Trace;
import org.eclipse.gmf.runtime.diagram.ui.IPreferenceConstants;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.Images;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderDebugOptions;
import org.eclipse.gmf.runtime.diagram.ui.render.internal.DiagramUIRenderPlugin;
import org.eclipse.gmf.runtime.draw2d.ui.mapmode.MapMode;
import org.eclipse.gmf.runtime.draw2d.ui.render.image.ImageConverter;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.GraphicsToGraphics2DAdaptor;
import org.eclipse.gmf.runtime.draw2d.ui.render.internal.graphics.RenderedMapModeGraphics;

/**
 * Supports generation of AWT and SWT images of a diagram or a subset of
 * editparts on a diagram.
 * 
 * @author schafe / sshaw
 */
public class DiagramImageGenerator
	extends DiagramGenerator {

	/**
	 * Creates a new instance.
	 * @param dgrmEP
	 *            the diagram editpart
	 */
	public DiagramImageGenerator(DiagramEditPart dgrmEP) {
		super(dgrmEP);
	}

	private GC gc = null;

	private Image image = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.clipboard.DiagramGenerator#setUpGraphics(int,
	 *      int)
	 */
	protected Graphics setUpGraphics(int width, int height) {
		Display display = Display.getDefault();

		image = new Image(display, new Rectangle(0, 0, width, height));
		gc = new GC(image);
		SWTGraphics swtG = new SWTGraphics(gc);
		
		/*
		IPreferenceStore preferenceStore =
			(IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
		
		if (preferenceStore.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)) {
			swtG.setAntialias(SWT.ON);
		} else {
			swtG.setAntialias(SWT.OFF);
		}
		*/
		return swtG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.clipboard.DiagramGenerator#disposeGraphics(org.eclipse.draw2d.Graphics)
	 */
	protected void disposeGraphics(Graphics g) {
		super.disposeGraphics(g);

		image.dispose();

		if (gc != null)
			gc.dispose();
		gc = null;

	}

	/* (non-Javadoc)
	 * @see org.eclipse.gmf.runtime.diagram.ui.render.clipboard.DiagramGenerator#getImageDescriptor(org.eclipse.draw2d.Graphics)
	 */
	protected ImageDescriptor getImageDescriptor(Graphics g) {
		return new ImageDescriptor() {

			ImageData imgData = image.getImageData();

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.resource.ImageDescriptor#getImageData()
			 */
			public ImageData getImageData() {
				return imgData;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.internal.clipboard.DiagramGenerator#createAWTImageForParts(java.util.List)
	 */
	public java.awt.Image createAWTImageForParts(List selectedObjects) {
		org.eclipse.swt.graphics.Rectangle sourceRect = calculateImageRectangle(selectedObjects);

		BufferedImage awtImage = null;
		try {
			awtImage = new BufferedImage(MapMode.LPtoDP(sourceRect.width),
				MapMode.LPtoDP(sourceRect.height),
				BufferedImage.TYPE_4BYTE_ABGR_PRE);

			Graphics2D g2d = awtImage.createGraphics();
			g2d.setColor(Color.white);
			g2d.fillRect(0, 0, awtImage.getWidth(), awtImage.getHeight());

			// Check anti-aliasing preference
			IPreferenceStore preferenceStore =
				(IPreferenceStore) getDiagramEditPart().getDiagramPreferencesHint().getPreferenceStore();
			
			if (preferenceStore.getBoolean(IPreferenceConstants.PREF_ENABLE_ANTIALIAS)) {				
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			} else {
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
			}
			
			g2d.clip(new java.awt.Rectangle(0, 0, awtImage.getWidth(), awtImage
				.getHeight()));

			Graphics graphics = new GraphicsToGraphics2DAdaptor(g2d,
				new Rectangle(0, 0, MapMode.LPtoDP(sourceRect.width), MapMode
					.LPtoDP(sourceRect.height)));

			RenderedMapModeGraphics mapModeGraphics = new RenderedMapModeGraphics(
				graphics);

			renderToGraphics(mapModeGraphics, new Point(sourceRect.x, sourceRect.y), selectedObjects);

			graphics.dispose();
			g2d.dispose();
		} catch (Error e) {
			// log the Error but allow execution to continue
			Trace.catching(DiagramUIRenderPlugin.getInstance(),
				DiagramUIRenderDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"createAWTImageForParts() failed to generate image", //$NON-NLS-1$
				e);
			awtImage = ImageConverter.convert(Images.ICON_ERROR);

		} catch (Exception ex) {
			// log the Exception but allow execution to continue
			Trace.catching(DiagramUIRenderPlugin.getInstance(),
				DiagramUIRenderDebugOptions.EXCEPTIONS_THROWING, getClass(),
				"createAWTImageForParts() failed to generate image", //$NON-NLS-1$
				ex);
			awtImage = ImageConverter.convert(Images.ICON_ERROR);
		}

		return awtImage;
	}

}