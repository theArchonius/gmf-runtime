/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2003.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.diagram.ui.services.decorator;

import org.eclipse.gmf.runtime.diagram.ui.internal.services.decorator.IDecoratorProviderBase;

/**
 * Clients providing an extension to the DecoratorService need to create a
 * decorator provider class that implements the IDecoratorProvider interface.
 * 
 * <p>
 * IDecoratorProvider is the interface for providers of the decorator service. A
 * decorator provider is responsible for installing its decorators on the
 * decorator targets that it wishes to decorate.
 * </p>
 * 
 * <p>
 * The provides method determines whether this decorator provider supports
 * adding decorations to a given decoration target. The operation in the
 * provides method will be of type <code>CreateDecoratorsOperation</code> from
 * which the decorator target can be extracted.
 * </p>
 * 
 * <p>
 * Here is an example:
 * 
 * <pre>
 * 
 *  
 *    public boolean provides(IOperation operation) {
 *  		if (!(operation instanceof CreateDecoratorsOperation)) {
 *  			return false;
 *  		}
 *  
 *  		IAdaptable adapter = ((CreateDecoratorsOperation) operation)
 *  			.getDecoratorTarget();
 *  		Object element = adapter.getAdapter(org.eclipse.uml2.Element.class);
 *  		
 *  		return CHECK CONDITION ON ELEMENT;
 *  	}
 *  
 *    public void createDecorators(IDecoratorTarget decoratorTarget) {
 *  		Object element = decoratorTarget.getAdapter(org.eclipse.uml2.Element.class));
 *       if (CHECK CONDITION ON ELEMENT) {
 *   		decoratorTarget.installDecorator(MY_DECORATOR_ID,
 *  			new MyDecorator(decoratorTarget));
 *       }
 *   }
 *   
 *  
 * </pre>
 * 
 * </p>
 * 
 * @see IDecoratorTarget
 * @see IDecorator
 * 
 * @author cmahoney
 */
public interface IDecoratorProvider
	extends IDecoratorProviderBase {

	/**
	 * Creates the decorators and installs them on the decorator target. See
	 * example above.
	 * 
	 * @param decoratorTarget
	 *            the object to be decorated
	 */
	public void createDecorators(IDecoratorTarget decoratorTarget);
}