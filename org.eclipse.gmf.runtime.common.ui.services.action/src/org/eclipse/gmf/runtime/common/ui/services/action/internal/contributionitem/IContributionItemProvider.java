/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2002, 2005.  All Rights Reserved.              |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.action.internal.contributionitem;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gmf.runtime.common.core.service.IProvider;
import org.eclipse.gmf.runtime.common.ui.util.IWorkbenchPartDescriptor;

/**
 * A provider interface to contribute to the action bars and/or
 * popup menus of workbench parts. A provider implementing this
 * interface will typically add contributions to the following
 * contribution managers: MainBar, ToolBar, GlobalActions and ContextMenu(s) of 
 * parts using the <code>ContributionItemService</code>
 * <P>
 * The information needed to perform the contribution can optionally
 * be described in XML along with the provider extension. In that case,
 * the provider implementing this interface has to also implement the package-protected
 * <code>IContributionDescriptorReader</code> interface , which ensures that
 * the provider gets a reference to its contribution description.
 * The <code>AbstractContributionItemProvider</code> provides an abstract
 * implementation of such provider
 * <P>
 * If a provider chose not to describe its contribution in its extension
 * then it has to implement this interface itself to do programatic
 * contributions to the managers.
 * 
 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.ContributionItemService
 * @see org.eclipse.gmf.runtime.common.ui.services.action.contributionitem.AbstractContributionItemProvider 
 * 
 * @author melaasar
 */
public interface IContributionItemProvider extends IProvider {

	/**
	 * Contributes to the given action bars that belong to a part 
	 * described with the given part descriptor.
	 * <P>
	 * This API is usually called from the <code>init()</code> method
	 * of a <code>EditorActionBarContributor</code> class for editors
	 * and from <code>init()</code> method of <code>IViewPart</code>
	 * implementation for views.
	 * 
	 * @param actionBars The target action bars
	 * @param workbenchPartDescriptor The context workbench part descriptor
	 */
	public void contributeToActionBars(
		IActionBars actionBars,
		IWorkbenchPartDescriptor workbenchPartDescriptor);

	/**
	 * Contributes to the given popup menu of the given part
	 * The popup menu id has to match the id used to register this
	 * menu with the part's site. Since the current selection could be
	 * a context to this API, a selection could be retrieved from the
	 * part's site's selection provider.
	 * <P>
	 * This API is usually called from <code>menuAboutToShow()</code> method
	 * of the main <code>IMenuListener</code> to a given context menu.
	 *
	 * @param popupMenu The target popup menu manager
	 * @param workbenchPart The context workbench part
	 */
	public void contributeToPopupMenu(
		IMenuManager popupMenu,
		IWorkbenchPart workbenchPart);

	/**
	 * Gives the provider a chance to clean up and dispose any cached contributions
	 * previously targeted at a part described by the given descriptor.
	 * <P>
	 * This API is usually called when the part contributor is disposing. This 
	 * would be form the <code>dispose</code> method of the 
	 * <code>EditorActioBarContributor</code> for editors and the <code>dispose</code>
	 * method of <code>IViewPart</code> for views. 
	 * 
	 * @param workbenchPartDescriptor The context workbench part descriptor
	 */
	public void disposeContributions(IWorkbenchPartDescriptor workbenchPartDescriptor);

}
