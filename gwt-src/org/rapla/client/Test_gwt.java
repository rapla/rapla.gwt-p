package org.rapla.client;

import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapla.entities.domain.internal.AllocatableImpl;
import org.rapla.entities.internal.CategoryImpl;
import org.rapla.entities.internal.UserImpl;
import org.rapla.facade.internal.ConflictImpl;
import org.rapla.framework.RaplaContext;
import org.rapla.framework.RaplaException;
import org.rapla.storage.UpdateEvent;
import org.rapla.storage.dbrm.FutureResult;
import org.rapla.storage.dbrm.RemoteJsonStorage;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwtjsonrpc.common.AsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Test_gwt implements EntryPoint {

	RemoteStorage service = GWT.create(RemoteStorage.class);
	{
		String address = GWT.getModuleBaseURL() + "../rapla/json/org.rapla.storage.dbrm.RemoteStorage";
		((ServiceDefTarget) service).setServiceEntryPoint(address);
	}
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RaplaGWTClient raplaGWTClient; 
		try {
			raplaGWTClient = new RaplaGWTClient();
		} catch (RaplaException e) {
			e.printStackTrace();
			return;
		}
		final RaplaContext context = raplaGWTClient.getContext();

		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("admin");
		final TextBox passwordField = new PasswordTextBox();
		passwordField.setText("");
		
		final Label errorLabel = new Label();
//		Container c = new TestContainer();
//		provide(c);
//		Test hallo = c.getContext().get( Test.class);
//		hallo.message();
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("passwordFieldContainer").add(passwordField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		final Logger logger = Logger.getLogger("componentClass");
		logger.log(Level.INFO, "GWT Applet started");

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});
		
		

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
				//Logger logger = Logger.getLogger("componentClass");
				//logger.log(Level.SEVERE, "hallo",new Exception("hallo"));
				
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
					
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				String password = passwordField.getText();


				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
//				try
//				{
//					ConnectInfo connectInfo = new ConnectInfo(textToServer, password.toCharArray());
//					ClientFacade facade = context.lookup(ClientFacade.class);
//					AppointmentFormater formater = context.lookup(AppointmentFormater.class);
//					facade.login(connectInfo);
//					Allocatable[] allocatables = facade.getAllocatables();
//					StringBuilder builder = new StringBuilder();
//					builder.append( "<h2>Ressources</h2>");
//					for ( Allocatable alloc:allocatables)
//					{
//						builder.append( alloc.getName( Locale.GERMANY));
//						builder.append( "<br/>");
//					}
//					builder.append( "<h2>Reservations</h2>");
//					
//					Reservation[] reservations = facade.getReservations( allocatables, new Date(), DateTools.addDays( new Date(), 5));
//					for (Reservation r: reservations)
//					{
//						builder.append( r.getName( Locale.GERMANY));
//						builder.append( "[");
//						for ( Appointment app:r.getAppointments())
//						{
//							String summary = formater.getSummary( app);
//							builder.append(summary);
//						}
//						builder.append( "]");
//						builder.append( "<br/>");
//					}
//					String result = builder.toString();
//					serverResponseLabel.setHTML(result);
//					
//				}
//				catch (Throwable ex) {
//					logger.log(Level.SEVERE, "hallo",ex);
//				}
				dialogBox.setText("Remote Procedure Call");
				serverResponseLabel
						.removeStyleName("serverResponseLabelError");
				dialogBox.center();
				closeButton.setFocus(true);
				
				FutureResult<UpdateEvent> resources = service.getResources();
				FutureResult<List<ConflictImpl>> conflicts = service.getConflicts();
				try {
					resources.get();
					conflicts.get();
				} catch (RaplaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				AsyncCallback<UserImpl> asyncCallback = new AsyncCallback<UserImpl>() {
//					public void onFailure(Throwable caught) {
//						// Show the RPC error message to the user
//						dialogBox
//								.setText("Remote Procedure Call - Failure");
//						serverResponseLabel
//								.addStyleName("serverResponseLabelError");
//						serverResponseLabel.setHTML(caught.getMessage());
//						dialogBox.center();
//						closeButton.setFocus(true);
//					}
//
//					public void onSuccess(UserImpl user) {
//						dialogBox.setText("Remote Procedure Call");
//						serverResponseLabel
//								.removeStyleName("serverResponseLabelError");
//						StringBuilder builder = new StringBuilder();
//						builder.append( "<h2>Ressources</h2>");
//						builder.append( user.getName( Locale.GERMANY));
//						builder.append( "<br/>");
//						serverResponseLabel.setHTML(builder.toString());
//						user.setEmail("christopher.kohlhaas@googlemail.com");
//						AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
//
//							public void onFailure(Throwable caught) {
//								// Show the RPC error message to the user
//								dialogBox
//										.setText("Remote Procedure Call - Failure");
//								serverResponseLabel
//										.addStyleName("serverResponseLabelError");
//								serverResponseLabel.setHTML(caught.getMessage());
//								dialogBox.center();
//								closeButton.setFocus(true);
//							}
//
//							@Override
//							public void onSuccess(Boolean result) {
//								
//							}
//						};
//						service.storeUser(user, callback);
//						
//					}
//				};
//				service.getUser("admin",asyncCallback);
				
//				AsyncCallback<CategoryImpl> callback2 = new AsyncCallback<CategoryImpl>() {
//
//					public void onFailure(Throwable caught) {
//						// Show the RPC error message to the user
//						dialogBox
//								.setText("Remote Procedure Call - Failure");
//						serverResponseLabel
//								.addStyleName("serverResponseLabelError");
//						serverResponseLabel.setHTML(caught.getMessage());
//						dialogBox.center();
//						closeButton.setFocus(true);
//					}
//
//					@Override
//					public void onSuccess(CategoryImpl result) {
//						System.out.println( result );
//						
//					}
//				};
//				service.getCategory( callback2);
				
//				AsyncCallback<List<AllocatableImpl>> callback2 = new AsyncCallback<List<AllocatableImpl>>() {
//
//					public void onFailure(Throwable caught) {
//						// Show the RPC error message to the user
//						dialogBox
//								.setText("Remote Procedure Call - Failure");
//						serverResponseLabel
//								.addStyleName("serverResponseLabelError");
//						serverResponseLabel.setHTML(caught.getMessage());
//						dialogBox.center();
//						closeButton.setFocus(true);
//					}
//
//					@Override
//					public void onSuccess(List<AllocatableImpl> result) {
//						
//					}
//				};
//				service.getResources( callback2);
				dialogBox.center();
				closeButton.setFocus(true);

			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}

	
//    private void provide(Container c) {
//    	Injector<Test> test = GWT.create(TestImpl.class);
//    	c.provide(Test.class, test);
//    }
//
//
//	private Injector<Test> GWT_create(Class<TestImpl> class1) {
//		return new Injector<Test>()
//			{
//				public Test create(Container cont) {
//					return new TestImpl( cont.getContext());
//				}
//			
//			};
//	}
}
