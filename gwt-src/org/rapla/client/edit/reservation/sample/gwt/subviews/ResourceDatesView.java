package org.rapla.client.edit.reservation.sample.gwt.subviews;

import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.edit.reservation.sample.gwt.gfx.ImageImport;
import org.rapla.client.edit.reservation.sample.gwt.subviews.TerminList.DateSelected;
import org.rapla.client.gwt.components.DateTimeComponent;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.Widget;

public class ResourceDatesView
{

    private FlowPanel contentPanel;

    private final Presenter presenter;

    public ResourceDatesView(Presenter presenter, RaplaLocale raplaLocale)
    {
        this.presenter = presenter;
        contentPanel = new FlowPanel();
    }

    protected Presenter getPresenter()
    {
        return presenter;
    }

    public Widget provideContent()
    {
        return contentPanel;
    }

    public void createContent(final Reservation reservation)
    {
        contentPanel.clear();

        final FlowPanel buttonBar = new FlowPanel();
        buttonBar.setStyleName("datesButtonBar");

        final Image buttonPlus = new Image(ImageImport.INSTANCE.plusIcon());
        buttonPlus.setTitle("Termin erstellen");
        buttonPlus.setStyleName("buttonsResourceDates");

        buttonPlus.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                //                getPresenter().onButtonPlusClicked();
            }
        });

        final Image buttonGarbageCan = new Image(ImageImport.INSTANCE.crossGreyIcon());
        buttonGarbageCan.setStyleName("buttonsResourceDates");
        buttonGarbageCan.setTitle("Termin l\u00F6schen");
        buttonGarbageCan.addClickHandler(new ClickHandler()
        {
            public void onClick(ClickEvent e)
            {
                //                getPresenter().onGarbageCanButtonClicked();
            }
        });
        final Image buttonNextGap = new Image(ImageImport.INSTANCE.nextGreyIcon());
        buttonNextGap.setStyleName("buttonsResourceDates");

        //
        buttonBar.add(buttonPlus);
        buttonBar.add(buttonGarbageCan);
        buttonBar.add(buttonNextGap);

        final FlowPanel dateInfos = new FlowPanel();
        dateInfos.setStyleName("dateInfos");

        final DateTimeComponent begin = new DateTimeComponent("Beginn:");

        // initialize and declarate Panel and Elements for End Time and Date
        final DateTimeComponent end = new DateTimeComponent("Ende:");

        //creatin the checkbox for whole day and add a handler
        final CheckBox cbWholeDay = new CheckBox("ganzt\u00E4gig");
        cbWholeDay.setStyleName("allDay");
        begin.add(cbWholeDay);
        cbWholeDay.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                //                getPresenter().onWholeDaySelected();
            }

        });

        // Checkbox reccuring dates
        final FlowPanel repeat = new FlowPanel();
        final DisclosurePanel cbRepeatType = new DisclosurePanel("Wiederholen");
        cbRepeatType.setStyleName("dateInfoLineLeft");

        final RadioButton daily = new RadioButton("repeat", "t\u00E4glich");
        daily.addClickHandler(new RepeatClickHandler());
        final RadioButton weekly = new RadioButton("repeat", "w\u00F6chentlich");
        weekly.addClickHandler(new RepeatClickHandler());
        final RadioButton monthly = new RadioButton("repeat", "monatlich");
        monthly.addClickHandler(new RepeatClickHandler());
        final RadioButton year = new RadioButton("repeat", "j\u00E4hrlich");
        year.addClickHandler(new RepeatClickHandler());
        final RadioButton noReccuring = new RadioButton("repeat", "keine Wiederholung");
        noReccuring.addClickHandler(new RepeatClickHandler());

        //Setting for reccuring dates
        final ListBox repeatType = new ListBox();
        repeatType.addItem("Bis Datum");
        repeatType.addItem("x Mal");

        final Label repeatText = new Label("Beginn: ");
        repeatText.setStyleName("beschriftung");

        repeat.add(daily);
        repeat.add(weekly);
        repeat.add(monthly);
        repeat.add(year);
        repeat.add(noReccuring);

        cbRepeatType.add(repeat);

        //initializing the disclourePanel for the resources
        final DisclosurePanel addResources = new DisclosurePanel("Ressourcen hinzuf\u00FCgen");
        addResources.setStyleName("dateInfoLineComplete");

        //load chosen resources
        final FlowPanel chosenResources = new FlowPanel();
        chosenResources.setStyleName("dateInfoLineComplete");

        Label headerChosenRes = new Label("Ausgew\u00E4hlte Ressourcen:");
        headerChosenRes.setStyleName("beschriftung");

        chosenResources.setStyleName("dateInfoLineComplete");
        chosenResources.add(headerChosenRes);

        Label explainer2 = new Label("Es wurden bisher keine Ressourcen ausgewählt");
        explainer2.setStyleName("wildcard");

        FlowPanel chooseContainer = new FlowPanel();
        chooseContainer.setStyleName("chooseContainer");

        // Baumstruktur f\u00FCr verf\u00FCgbare Resourcen
        final Tree resourceTree = new Tree();

        // Filter
        final Image filter = new Image(ImageImport.INSTANCE.filterIcon());
        filter.setStyleName("buttonFilter");
        filter.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                //                getPresenter().onFilterClicked();
            }

        });

        final ListBox filterEintr = new ListBox();
        filterEintr.addItem("Verf\u00FCgbare Ressourcen");
        filterEintr.addItem("Nicht Verf\u00FCgbare Ressourcen");
        filterEintr.addItem("Kurse");
        filterEintr.addItem("R\u00E4ume");
        filterEintr.addItem("Professoren");
        filterEintr.setStyleName("filterWindow");
        filterEintr.setMultipleSelect(true);
        filterEintr.setVisible(false);

        // Suchfeld
        final FlowPanel suche = new FlowPanel();
        suche.setStyleName("suchfeld");
        MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        oracle.add("WWI12B1");
        oracle.add("K\u00FCstermann");
        oracle.add("Daniel");
        oracle.add("B343");

        SuggestBox searchField = new SuggestBox(oracle);
        searchField.setWidth("300px");
        searchField.setStyleName("searchInput");

        Image loupe = new Image(ImageImport.INSTANCE.loupeIcon());
        loupe.setStyleName("buttonLoupe");

        suche.add(searchField);
        suche.add(loupe);
        suche.add(filter);
        suche.add(filterEintr);

        chooseContainer.add(suche);
        chooseContainer.add(resourceTree);
        // chooseContainer.setWidth(width * 0.85 + "px");

        addResources.setContent(chooseContainer);

        final FlowPanel dateContentWrapper = new FlowPanel();
        dateContentWrapper.setStyleName("dateContent");
        dateContentWrapper.add(begin);
        dateContentWrapper.add(end);
        dateContentWrapper.add(cbRepeatType);
        //        dateContentWrapper.add(addDateWithLabel);
        dateInfos.add(dateContentWrapper);

        // dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
        dateContentWrapper.add(chosenResources);
        dateContentWrapper.add(addResources);

        final TerminList dateList = new TerminList(new DateSelected()
        {
            @Override
            public void selectDate(Appointment appointment)
            {
                begin.setDate(appointment.getStart());
                end.setDate(appointment.getEnd());
            }
        }, reservation.getAppointments());
        //The panel contains the Button to select all resources at the top of the datelist
        FlowPanel placeholderSetResourcesToAll = new FlowPanel();
        placeholderSetResourcesToAll.setStyleName("resourceButtonPanel");
        final Button setResourcesToAll = new Button("Ressourcen f\u00FCr alle \u00FCbernehmen");
        setResourcesToAll.setStyleName("resourceButton");
        setResourcesToAll.setVisible(false);
        placeholderSetResourcesToAll.add(setResourcesToAll);
        dateList.add(placeholderSetResourcesToAll);
        setResourcesToAll.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent event)
            {
                //                getPresenter().onSetResourcesToAllClicked();
            }
        });

        contentPanel.add(dateList);
        contentPanel.add(buttonBar);
        contentPanel.add(dateInfos);
    }

    private class RepeatClickHandler implements ClickHandler
    {
        @Override
        public void onClick(ClickEvent event)
        {
            //            getPresenter().onrepeatTypeClicked(event);
        }
    }

    public void clearContent()
    {
        contentPanel.clear();
    }

    public void update(Reservation reservation)
    {
        createContent(reservation);
    }

}
