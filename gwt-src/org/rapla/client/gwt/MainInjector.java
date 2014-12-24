package org.rapla.client.gwt;

import org.rapla.client.Application;
import org.rapla.client.plugin.view.list.gwt.ListViewPluginGinModule;
import org.rapla.facade.ClientFacade;
import org.rapla.storage.StorageOperator;
import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value= { RaplaGWTModule.class, ListViewPluginGinModule.class},properties="extra.ginModules")
public interface MainInjector extends Ginjector {
    public Application getApplication();

    public RemoteServer getLoginService();
    
    public ClientFacade getFacade();

    public StorageOperator getOperator();
    
    
}