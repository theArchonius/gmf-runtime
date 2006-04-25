/******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation 
 ****************************************************************************/

package org.eclipse.gmf.runtime.emf.core.internal.resources;

import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.SAXWrapper;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class changes the behavior of the default XMILoader so that
 * UnresolvedReferenceExceptions are not thrown back.
 * 
 * @author rafikj
 * 
 * @deprecated Use the {@link org.eclipse.gmf.runtime.emf.core.resources.GMFLoad}
 *     class, instead
 */
public class GMFLoad
	extends org.eclipse.gmf.runtime.emf.core.resources.GMFLoad {

	/**
	 * Constructor.
	 */
	public GMFLoad(XMLHelper helper) {
		super(helper);
	}

	/**
	 * @see org.eclipse.emf.ecore.xmi.impl.XMLLoadImpl#makeDefaultHandler()
	 */
	protected DefaultHandler makeDefaultHandler() {
		return new SAXWrapper(new GMFHandler(resource, helper, options));
	}
}