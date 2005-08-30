/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2004, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */

package org.eclipse.gmf.runtime.diagram.ui.internal.figures;

import org.eclipse.draw2d.geometry.Dimension;

import org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure;
import org.eclipse.gmf.runtime.draw2d.ui.figures.ConstrainedToolbarLayout;

/**
 * A figure to represent the nested resizable compartment figure. Extends 
 * ResizeableCompartmentFigure with the following differences
 * 1.  If the contents of the scroll pane is empty then the figure will not be visible
 * 2.  Provides a constructor that has the text pane text align to the left
 * 3.  Provides a constructor that doesn't have a title by default.
 * 
 * Used by List compartment figures that are nested within other resizable compartment 
 * @author choang
 * @canBeSeenBy org.eclipse.gmf.runtime.diagram.ui.*
 */
public class NestedResizableCompartmentFigure extends ResizableCompartmentFigure {

	/**
	 * Constructors a ResizeableComparmtmentFigure that has the text align to the left
	 * and the scrollpane 
	 */
	public NestedResizableCompartmentFigure() {
		super(null);
		setBorder(null);
		ConstrainedToolbarLayout layout = (ConstrainedToolbarLayout)getLayoutManager();
		layout.setMinorAlignment(ConstrainedToolbarLayout.ALIGN_TOPLEFT); //diff cause we want to align our title to the left top
		getScrollPane().getContents().setBorder(null);
	}
	
	/*
	 * Zero dimension
	 */
	private static Dimension ZERO_DIM = new Dimension(0,0);
	
	/* 
	 * Override as the min dimension is 0,0 since this compartment doesn't want to 
	 * leave any spaces if there are not contains
	 * @see org.eclipse.gmf.runtime.diagram.ui.figures.ResizableCompartmentFigure#getMinClientDimension()
	 */
	public Dimension getMinClientDimension() {		
		return ZERO_DIM;
	}

}
