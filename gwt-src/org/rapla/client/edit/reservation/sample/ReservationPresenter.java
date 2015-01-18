package org.rapla.client.edit.reservation.sample;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.PermissionContainer;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.entities.dynamictype.internal.AttributeImpl;
import org.rapla.entities.dynamictype.internal.DynamicTypeImpl;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.client.edit.reservation.sample.InfoView;


public class ReservationPresenter implements ReservationController,Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;
    
    private ReservationView view;
    private InfoViewPresenter infoViewPresenter;
    
    private Reservation tempReservation;
	private ResourceDatesViewPresenter resourceDatesPresenter;
    
    @Inject
    public ReservationPresenter(ReservationView view, InfoViewPresenter infoViewPresenter, ResourceDatesViewPresenter resourceDatesPresenter) {
        this.view = view;
        view.setPresenter(this);
        this.infoViewPresenter = infoViewPresenter;
		this.resourceDatesPresenter = resourceDatesPresenter;
		
        view.addSubView( infoViewPresenter.getView());
        view.addSubView( resourceDatesPresenter.getView());
//       InfoView test2 =  (InfoView) infoViewPresenter.getView();
//       test2.hide();
        
    }

    Reservation event;
    boolean isNew;
    
    
    @Override
    public void edit(final Reservation event, boolean isNew) {
        tempReservation = event;
        this.isNew = isNew;
        infoViewPresenter.setReservation(event);
        //resourceViewPresenter.setReservation(event);	
        view.show(event);
        
    }

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        try {
   
        	 saveTemporaryChanges();
			 Classification classification = tempReservation.getClassification();
			 Attribute first =
			 classification.getType().getAttributes()[0];
	//		 String text = view.getTitelInput();
	//		 classification.setValue(first, text);       	
            facade.store(tempReservation);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        view.hide();
    }

    private void saveTemporaryChanges() {
    	Classification classificationTmp = tempReservation.getClassification();

//		if (currentView instanceof InfoViewInterface) {
//
//			reservationName = ((InfoViewInterface) currentView).getTitelInput()
//					.getText();

			// // DynamicType[] types;
			// types = ReservationController.this
			// .getFacade()
			// .getDynamicTypes(
			// DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

			// String text = reservationName;
			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();

			// Set new Titel/Name for reservationTmp
			Attribute first = classificationTmp.getType().getAttributes()[0];
		//	classificationTmp.setValue(first, reservationName);

			// Attribute first = classification.getType().getAttributes()[0];

			// List<DynamicType> eventTypes = new ArrayList<DynamicType>();
			// User user = getFacade().getUser();

			// String key = null;
			//
			// for (DynamicType type : types) {
			// if (PermissionContainer.Util.canCreate(type, user))
			// eventTypes.add(type);
			// }
			//
			// for (int i = 0; i < eventTypes.size(); i++) {
			// String name = eventTypes.get(i).getName(
			// GWTRaplaLocale.getLocale());
			//
			// if (name.equalsIgnoreCase(((InfoViewInterface) currentView)
			// .getSelectedEventType())) {
			// key = eventTypes.get(i).getKey();
			// }
			// }

//			// maybe useful for another case
//			for (Allocatable a : facade.getAllocatables()) {
//				if (a.getName(GWTRaplaLocale.getLocale()).equals(
//						((InfoViewInterface) currentView)
//								.getSelectedEventType())) {
//					reservationTmp.addAllocatable((Allocatable) a);
//				}
//			}

			// change reservation type
//			for (DynamicType type : facade
//					.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
//				if (!(type.getName(GWTRaplaLocale.getLocale())
//						.equals(classificationTmp.getType()))
//						&& type.getName(GWTRaplaLocale.getLocale()).equals(
//								((InfoViewInterface) currentView)
//										.getSelectedEventType())) {
////					Reservation event = facade.newReservation(type
//////							.newClassification());
////					reservationTmp = facade.edit(reservationTmp);
//					
//
////					Allocatable[] allocatables = reservationTmp
////							.getAllocatables();
////					Appointment[] appointments = reservationTmp
////							.getAppointments();
////					Collection<Permission> permissions = reservationTmp
////							.getPermissionList();
//					HashMap<Attribute, Object> attributes = new HashMap<Attribute, Object>();
//					for (Attribute a : classificationTmp.getAttributes()) {
//						attributes.put(a, classificationTmp.getValue(a));
//					}
////
//////					facade.remove(reservationTmp);
////					Classification classificationNew = event
////							.getClassification();
//					reservationTmp.setClassification(type
//					.newClassification());
//
//					classificationTmp = reservationTmp.getClassification();
////
////					for (Allocatable allocatable : allocatables) {
////						event.addAllocatable(allocatable.clone());
////					}
////					for (Appointment a : appointments) {
////						event.addAppointment(a.clone());
////					}
////					for (Permission a : permissions) {
////						event.addPermission(a.clone());
////					}
//					for (Attribute a : classificationTmp.getAttributes()) {
//						classificationTmp
//								.setValue(a, attributes.get(a));
//					}
////					reservationTmp = event;
////					classificationTmp = classificationNew;
//				}
//			}

			// Classification classificationTmp2 = facade.getDynamicType(key)
			// .newClassification();
			//
			// Allocatable resourceEventType = facade.newAllocatable(
			// classificationTmp2, user);
			//
			// reservationTmp.addAllocatable(resourceEventType);

			// classification.setType(((facade.getDynamicType((((InfoViewInterface)
			// currentView).getSelectedEventType())))));

			// reservationTmp.((InfoViewInterface) currentView).getResources();

			// reservationTmp.addAllocatable(new Allocatable());
			// facade.getDynamicType(((InfoViewInterface)
			// currentView).getSelectedEventType()).get;

			// ((InfoViewInterface) currentView).getVorlesungsStundenInput();
			// Attribute attrTmp = facade.newAttribute(AttributeType.INT);
			// Allocatable resourceDuration = facade.newResource();

			// set Vorlesungstunden
//			if (reservationTmp
//					.getClassification()
//					.getType()
//					.equals(facade
//							.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)[0])) {
//				Attribute second = classificationTmp.getType().getAttributes()[5];
//				classificationTmp.setValue(second, Integer
//						.parseInt(((InfoViewInterface) currentView)
//								.getVorlesungsStundenInput().getText()));
//			}

			// classificationTmp = facade
			// .getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
			// .newClassification();
			// classificationTmp.setValue("name", "Vorlesungsstunden");
			// classificationTmp.setValue("duration",
			// ((InfoViewInterface) currentView)
			// .getVorlesungsStundenInput().getText());
			//
			// Allocatable resourceDuration = facade.newAllocatable(
			// classificationTmp, user);
			// // facade.newPeriod();
			// resourceDuration.setAnn...
			// resourceDuration.setAnnotation("duration", ((InfoViewInterface)
			// currentView).getVorlesungsStundenInput().getText());
			// TO DO: look up real key for setAnnotation (is duration the
			// correct one?)
			//
			// reservationTmp.addAllocatable(resourceDuration);

			// ((InfoViewInterface) currentView).getStudiengangListBox();
			// Allocatable resourceCourse = facade.newResource();
			// := (entspricht)
			// facade.newAllocatable((facade.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0].newClassification()));
			// resourceDuration.
			// resourceCourse.setAnnotation("course", ((InfoViewInterface)
			// currentView).getStudiengangListBox().getSelectedItemText());
			// TO DO: look up real key for setAnnotation (is course the correct
			// one?)
			// ((InfoViewInterface) currentView).getStudiengangListBoxAuswahl();
			// classificationTmp = facade
			// .getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE)[0]
			// .newClassification();
			// classificationTmp.setValue("name", "Studiengang");
			// classificationTmp.setValue("course",
			// ((InfoViewInterface) currentView).getStudiengangListBox()
			// .getSelectedItemText());
			//
			//
			// Allocatable resourceCourse = facade.newAllocatable(
			// classificationTmp, user);
			//
			// reservationTmp.addAllocatable(resourceCourse);

//			// set Studiengang
//			Attribute third = classificationTmp.getType().getAttributes()[2];
//			Allocatable[] resources = facade.getAllocatables();
//			for (Allocatable a : resources) {
//				if (a.getName(this.GWTRaplaLocale.getLocale()).equals(
//						((InfoViewInterface) currentView)
//								.getStudiengangListBox().getSelectedItemText())) {
//					a.get ?
//					classificationTmp.setValue(third, a.getName(this.GWTRaplaLocale.getLocale()));
//					//was muss hier als value �bergeben werden??? Mal nachgucken
//				}
//			}

			// getCreateableDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);

//		} else {
//			currentView = (ResourceDatesInterface) currentView;
//		}

//		logger.log(Level.WARNING, "All Attributes - ");
//		for (Attribute a : classificationTmp.getType().getAttributes()) {
//			logger.log(
//					Level.WARNING,
//					"Allocatable: "
//							+ a.getName(this.GWTRaplaLocale.getLocale())
//							+ "-"
//							+ a.getName()
//							+ "-"
//							+ a.getKey()
//							+ "-"
//							+ classificationTmp.getValueAsString(a,
//									this.GWTRaplaLocale.getLocale()));
//		}
//


	}

	@Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove( event);
        } catch (RaplaException e1) {
            logger.error( e1.getMessage(), e1);
        }
        view.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        logger.info("cancel clicked");
        view.hide();
    }

    
    @Override
    public void changeEventName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = event.getClassification();
        Attribute first = classification.getType().getAttributes()[0];
        classification.setValue(first, newName);
    }
    
    
    @Override
    public boolean isDeleteButtonEnabled() 
    {
        return !isNew;
    }

	@Override
	public void onTabChanged(int selectedTab) {
	
		ReservationEditSubView tempView = infoViewPresenter.getView();
		
		if(selectedTab == 0){
			tempView = infoViewPresenter.getView();
		}else if(selectedTab == 1){
			tempView = resourceDatesPresenter.getView();
		}else{
			
		}
		
		view.update(tempView);
	}

	
	
	public List<DynamicType> getCreateableDynamicTypes(String classificationType)
			throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = facade.getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		return eventTypes;
	}

	public List<String> getResourceTypesNames() throws RaplaException {

		return getDynamicTypesNames(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESOURCE);
	}

	public List<String> getPersonTypesNames() throws RaplaException {

		return getDynamicTypesNames(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_PERSON);
	}

	public List<String> getDynamicTypesNames(String classificationType)
			throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = facade.getDynamicTypes(classificationType);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
		}

		return items;
	}

	public List<String> getEventTypes() throws RaplaException {

		DynamicType[] types;
		List<String> items = new ArrayList<String>();
		types = facade.getDynamicTypes(
				DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();
		for (DynamicType type : types) {
			if (PermissionContainer.Util.canCreate(type, user))
				eventTypes.add(type);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
		}
		;

		return items;
	}

	public Attribute[] getDynamicFields(String eventKey) throws RaplaException {

		AttributeImpl attr = new AttributeImpl();

		DynamicTypeImpl type = new DynamicTypeImpl();

		DynamicType[] types;
		types = facade.getDynamicTypes(
				DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);

		List<String> items = new ArrayList<String>();
		List<DynamicType> eventTypes = new ArrayList<DynamicType>();
		User user = facade.getUser();

		String key = eventKey;

		for (DynamicType typen : types) {
			if (PermissionContainer.Util.canCreate(typen, user))
				eventTypes.add(typen);
		}

		for (int i = 0; i < eventTypes.size(); i++) {
			String name = eventTypes.get(i).getName(raplaLocale.getLocale());
			items.add(name);
//			if (eventTypes
//					.get(i)
//					.getName(raplaLocale.getLocale())
//					.equalsIgnoreCase(view.getSelectedEventType())) {
//				key = eventTypes.get(i).getKey();
//			}
		}
//
//		type.setKey(key);
		Attribute[] attributes = { new AttributeImpl() };

		for (int i = 0; i < type.getAttributes().length; i++) {
			
			attributes[i] = type.getAttributes()[i];
			attributes[6] = new AttributeImpl();
		}
		
		return attributes;
	}

	public String[] getConstraintKeys(Attribute attribute)
			throws RaplaException {

		String[] constraintKeys = null;

		for (int i = 0; i < attribute.getConstraintKeys().length; i++) {
			constraintKeys[i] = attribute.getConstraintKeys()[i];
		}

		return constraintKeys;
	}

	protected void loadDataFromReservation(
			Reservation currentView2) { //to be double checked

//		Locale locale = this.GWTRaplaLocale.getLocale();
//		Classification classificationTmp = reservationTmp.getClassification();
//
//		Allocatable[] allocatables = reservationTmp.getAllocatables();
//
//		Allocatable[] resources = reservationTmp.getAllocatables();
//		Appointment[] appointments = reservationTmp.getAppointments();
//
//		// I just want to see all
//		// this.logAllElements();
//
////		logger.log(Level.WARNING, "All Attributes - ");
////		for (Attribute a : classificationTmp.getType().getAttributes()) {
////			logger.log(
////					Level.WARNING,
////					"Allocatable: " + a.getName(locale) + "-" + a.getName()
////							+ "-" + a.getKey() + "-"
////							+ classificationTmp.getValueAsString(a, locale));
////		}
//
//		if (currentView2 instanceof InfoViewInterface) {
//			if (reservationName != null) {
//				((InfoViewInterface) currentView).getTitelInput().setText(
//						reservationName);
//			}
//
//			((InfoViewInterface) currentView)
//					.setSelectedEventType(classificationTmp.getType().getName(
//							locale));
//
//			if (allocatables != null) {
//				for (int a = 0; a < allocatables.length; a++) {
//					DynamicType tmp = allocatables[a].getClassification()
//							.getType();
//					String tmp2 = tmp.toString();
//
//					if (tmp2.equals(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION)) {
//						((InfoViewInterface) currentView)
//								.setSelectedEventType(allocatables[a]
//										.getName(locale));
//					}
//		
//				}
//			}
//
//			for (Attribute a : classificationTmp.getAttributes()) {
//				String tmp = a.getName(locale);
//				String tmp2 = classificationTmp.getValueAsString(a, locale);
//				if (tmp.equals("Studiengang")) {
//					for (int i = 0; i < (((InfoViewInterface) currentView2)
//							.getStudiengangListBox().getItemCount()); i++) {
//						if (tmp2.equals(((InfoViewInterface) currentView2)
//								.getStudiengangListBox().getItemText(i))) {
//							((InfoViewInterface) currentView2)
//									.getStudiengangListBox()
//									.setSelectedIndex(i);
//						}
//					}
//
//				}
//				if (tmp.equals("Gepl. Vorlesungsstunden")) {
//					((InfoViewInterface) currentView2)
//							.getVorlesungsStundenInput().setText(tmp2);
//				}
//			}
//
//		} else {
//
//		}

	}

	private void logAllElements() {
		Classification classificationTmp = tempReservation.getClassification();
		Allocatable[] resources = tempReservation.getAllocatables();
		Appointment[] appointments = tempReservation.getAppointments();

		logger.warn( "All Allocatables - ");
		try {
			for (Allocatable a : facade.getAllocatables()) {

				logger.warn(
						"Allocatable: "
								+ a.getName(raplaLocale.getLocale()));
			}
		} catch (RaplaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.warn( "All Attributes - ");
		for (Attribute a : classificationTmp.getType().getAttributes()) {
			logger.warn(
					"Allocatable: "
							+ a.getName(raplaLocale.getLocale()) + "-"
							+ a.getName() + "-" + a.getKey());
		}

		{
			StringBuilder builder = new StringBuilder();
			for (Allocatable res : resources) {
				builder.append(res.getName(raplaLocale.getLocale()));
			}
			logger.warn( "Ressourcen: " + builder.toString());

		}
		{
			StringBuilder builder = new StringBuilder();
			for (Appointment app : appointments) {
				builder.append(app.toString());
			}
			logger.warn( "Termine: " + builder.toString());
		}

		logger.warn("reservationname: " + ((InfoView) infoViewPresenter.getView()).getTitelInput());

	}

}
