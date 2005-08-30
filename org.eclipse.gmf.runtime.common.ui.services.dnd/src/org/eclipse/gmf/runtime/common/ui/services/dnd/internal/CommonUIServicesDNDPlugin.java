/*
 *+------------------------------------------------------------------------+
 *| Licensed Materials - Property of IBM                                   |
 *| (C) Copyright IBM Corp. 2005.  All Rights Reserved.                    |
 *|                                                                        |
 *| US Government Users Restricted Rights - Use, duplication or disclosure |
 *| restricted by GSA ADP Schedule Contract with IBM Corp.                 |
 *+------------------------------------------------------------------------+
 */
package org.eclipse.gmf.runtime.common.ui.services.dnd.internal;

import org.eclipse.core.runtime.Platform;

import org.eclipse.gmf.runtime.common.core.l10n.AbstractResourceManager;
import org.eclipse.gmf.runtime.common.ui.plugin.XToolsUIPlugin;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.DragDropListenerService;
import org.eclipse.gmf.runtime.common.ui.services.dnd.core.TransferAdapterService;
import org.eclipse.gmf.runtime.common.ui.services.dnd.internal.l10n.ResourceManager;

/**
 * The common UI plug-in.
 * 
 * @author wdiu, Wayne Diu
 *  
 */
public class CommonUIServicesDNDPlugin
	extends XToolsUIPlugin {

	/**
	 * Extension point name for drag and drop listener providers extension
	 * point.
	 */
	protected static final String DRAG_DROP_LISTENER_PROVIDERS_EXT_P_NAME = "dragDropListenerProviders"; //$NON-NLS-1$

	/**
	 * Extension point name for the transfer adapter providers extension point.
	 */
	protected static final String TRANSFER_ADAPTER_PROVIDERS_EXT_P_NAME = "transferAdapterProviders"; //$NON-NLS-1$

	/**
	 * This plug-in's shared instance.
	 */
	private static CommonUIServicesDNDPlugin plugin;

	/**
	 * Creates a new plug-in runtime object.
	 */
	public CommonUIServicesDNDPlugin() {
		super();

		plugin = this;
	}

	/**
	 * Retrieves this plug-in's shared instance.
	 * 
	 * @return This plug-in's shared instance.
	 */
	public static CommonUIServicesDNDPlugin getDefault() {
		return plugin;
	}

	/**
	 * Retrieves the unique identifier of this plug-in.
	 * 
	 * @return A non-empty string which is unique within the plug-in registry.
	 */
	public static String getPluginId() {
		return getDefault().getBundle().getSymbolicName();
	}

	/**
	 * Retrieves the resource manager for this plug-in.
	 * 
	 * @return The resource manager for this plug-in.
	 */
	public AbstractResourceManager getResourceManager() {
		return ResourceManager.getInstance();
	}

	/**
	 * Starts up this plug-in.
	 */
	protected void doStartup() {
		configureDragDropListenerProviders();
		configureTransferAdapterProviders();
	}

	/**
	 * Configures drag and drop listener providers based drag and drop listener
	 * providers extension configurations.
	 *  
	 */
	private void configureDragDropListenerProviders() {
		DragDropListenerService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				DRAG_DROP_LISTENER_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());

	}

	/**
	 * Configures transfer adapter providers based on marker navigation provider
	 * extension configurations.
	 *  
	 */
	private void configureTransferAdapterProviders() {
		TransferAdapterService.getInstance().configureProviders(
			Platform.getExtensionRegistry().getExtensionPoint(getPluginId(),
				TRANSFER_ADAPTER_PROVIDERS_EXT_P_NAME)
				.getConfigurationElements());
	}
}