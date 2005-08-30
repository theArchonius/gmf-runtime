/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.properties.sections.appearance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Image;

import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.properties.Properties;
import org.eclipse.gmf.runtime.diagram.ui.properties.internal.l10n.ResourceManager;
import org.eclipse.gmf.runtime.draw2d.ui.figures.FigureUtilities;
import com.ibm.xtools.notation.View;


public class DiagramColorsAndFontsPropertySection
	extends ShapeColorsAndFontsPropertySection {
	/**
	 * @return - an itertor object to iterate over the selected/input edit parts
	 */
	protected Iterator getInputIterator() {
		DiagramEditPart diagram = (DiagramEditPart) super.getSingleInput();
		return diagram != null ? diagram.getPrimaryEditParts().iterator()
			: Collections.EMPTY_LIST.iterator();

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xtools.uml.ui.diagram.internal.properties.sections.appearance.AbstractAppearancePropertySection#getSingleInput()
	 */
	public IGraphicalEditPart getSingleInput() {

		DiagramEditPart diagram = (DiagramEditPart) super.getSingleInput();
		if (diagram != null)
			return (IGraphicalEditPart) diagram.getPrimaryChildEditPart();
		return null;
	}

	/**
	 * Change fill color property value
	 */
	protected void changeFillColor() {
	
		// Update model in response to user
	
		if (fillColor != null) {
	
			List commands = new ArrayList();
			Iterator it = getInputIterator();
	
			while (it.hasNext()) {
				final IGraphicalEditPart ep = (IGraphicalEditPart) it.next();
				if (!(ep instanceof ConnectionNodeEditPart))
					commands.add(createCommand(FILL_COLOR_COMMAND_NAME,
						((View) ep.getModel()).eResource(), new Runnable() {
	
							public void run() {
								ep.setPropertyValue(Properties.ID_FILLCOLOR,
									FigureUtilities.RGBToInteger(fillColor));
							}
						}));
			}
	
			executeAsCompositeCommand(FILL_COLOR_COMMAND_NAME, commands);
			Image overlyedImage = new ColorOverlayImageDescriptor(
				ResourceManager.getInstance().getImage(FILL_COLOR_IMAGE_NAME)
					.getImageData(), fillColor).createImage();
			fillColorButton.setImage(overlyedImage);
		}
	}

	/**
	 * Adapt the object to an EObject - if possible
	 * 
	 * @param object
	 *            object from a diagram or ME
	 * @return EObject
	 */
	protected EObject adapt(Object object) {
		if (object instanceof IAdaptable) {
			if (object instanceof IGraphicalEditPart)// digram case
				return (EObject) ((IAdaptable) object).getAdapter(View.class);
			// ME case
			return (EObject) ((IAdaptable) object).getAdapter(EObject.class);
		}
	
		return null;
	}	
}
