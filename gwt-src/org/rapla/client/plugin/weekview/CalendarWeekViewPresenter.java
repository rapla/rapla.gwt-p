package org.rapla.client.plugin.weekview;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Provider;

import org.rapla.client.base.CalendarPlugin;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.plugin.weekview.CalendarWeekView.Presenter;
import org.rapla.components.calendarview.AbstractCalendar;
import org.rapla.components.calendarview.Block;
import org.rapla.components.calendarview.Builder;
import org.rapla.components.calendarview.html.AbstractHTMLView;
import org.rapla.components.util.DateTools;
import org.rapla.entities.domain.Reservation;
import org.rapla.facade.CalendarOptions;
import org.rapla.facade.CalendarSelectionModel;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;
import org.rapla.plugin.abstractcalendar.GroupAllocatablesStrategy;
import org.rapla.plugin.abstractcalendar.RaplaBuilder;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBlock;
import org.rapla.plugin.abstractcalendar.server.HTMLRaplaBuilder;

import com.google.web.bindery.event.shared.EventBus;

public class CalendarWeekViewPresenter<W> implements Presenter, CalendarPlugin
{

    private CalendarWeekView<W> view;
    @Inject
    private Logger logger;
    @Inject
    private EventBus eventBus;
    @Inject
    private CalendarSelectionModel model;

    @Inject
    ClientFacade facade;

    @Inject
    Provider<HTMLRaplaBuilder> builderProvider;

    @Inject
    private RaplaLocale raplaLocale;

    @Inject
    public CalendarWeekViewPresenter(CalendarWeekView view)
    {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public String getName()
    {
        return "week";
    }

    @Override
    public W provideContent()
    {
        return view.provideContent();
    }

    @Override
    public void selectReservation(Reservation selectedObject)
    {
        DetailSelectEvent event2 = new DetailSelectEvent(selectedObject);
        eventBus.fireEvent(event2);
        logger.info("selection changed");

    }

    @Override
    public void updateContent()
    {
        HTMLWeekViewPresenter weekview = new HTMLWeekViewPresenter(view);
        //            try {
        //                configureView();
        //            } catch (RaplaException ex) {
        //                logger.error("Can't configure view ", ex);
        //                throw new ServletException( ex );
        //            }
        weekview.setLocale(raplaLocale.getLocale());
        weekview.setTimeZone(raplaLocale.getTimeZone());
        weekview.setToDate(model.getSelectedDate());
        model.setStartDate(weekview.getStartDate());
        model.setEndDate(weekview.getEndDate());

        try
        {
            RaplaBuilder builder = createBuilder();
            weekview.rebuild(builder);
            //String calendarviewHTML = weekview.getHtml();
            //this.view.update(calendarviewHTML);
        }
        catch (RaplaException ex)
        {
            logger.error("Can't create builder ", ex);
        }
    }

    //    protected HTMLWeekView createCalendarView() {
    //        HTMLWeekView weekView = new HTMLWeekView()
    //        {
    //            public void rebuild() {
    //                setWeeknumber(MessageFormat.format(getString("calendarweek.abbreviation"), getStartDate()));
    //                super.rebuild();
    //            }
    //        };
    //        return weekView;
    //    }

    //    protected void configureView() {
    //        HTMLWeekView weekView = (HTMLWeekView) view;
    //        CalendarOptions opt = getCalendarOptions();
    //        weekView.setRowsPerHour( opt.getRowsPerHour() );
    //        weekView.setWorktimeMinutes(opt.getWorktimeStartMinutes(), opt.getWorktimeEndMinutes() );
    //        weekView.setFirstWeekday( opt.getFirstDayOfWeek());
    //        int days = getDays(opt);
    //        weekView.setDaysInView( days);
    //        Set<Integer> excludeDays = opt.getExcludeDays();
    //        if ( days <3)
    //        {
    //            excludeDays = new HashSet<Integer>();
    //        }
    //        weekView.setExcludeDays( excludeDays );
    //       
    //    }
    //    

    /** overide this for daily views*/
    protected int getDays(CalendarOptions calendarOptions)
    {
        return calendarOptions.getDaysInWeekview();
    }

    protected RaplaBuilder createBuilder() throws RaplaException
    {
        //RaplaBuilder builder = super.createBuilder();
        RaplaBuilder builder = builderProvider.get();
        Date startDate = facade.today();
        Date endDate = DateTools.addDays(startDate, 7);
        builder.setFromModel(model, startDate, endDate);
        builder.setNonFilteredEventsVisible(false);

        GroupAllocatablesStrategy strategy = new GroupAllocatablesStrategy(raplaLocale.getLocale());
        //        boolean compactColumns = getCalendarOptions().isCompactColumns() ||  builder.getAllocatables().size() ==0 ;
        //        strategy.setFixedSlotsEnabled( !compactColumns);
        strategy.setResolveConflictsEnabled(true);
        builder.setBuildStrategy(strategy);

        return builder;
    }

    public int getIncrementSize()
    {
        return Calendar.WEEK_OF_YEAR;
    }

    static public class HTMLWeekViewPresenter extends AbstractHTMLView
    {
        private CalendarWeekView view;

        private int endMinutes;
        private int minMinute;
        private int maxMinute;
        private int startMinutes;
        int m_rowsPerHour = 2;
        HTMLDaySlot[] daySlots;
        ArrayList<Block> blocks = new ArrayList<Block>();
        //ArrayList<Integer> blockStart = new ArrayList<Integer>();
        //ArrayList<Integer> blockSize = new ArrayList<Integer>();

        String weeknumber;

        public HTMLWeekViewPresenter(CalendarWeekView view)
        {
            this.view = view;
        }

        /** The granularity of the selection rows.
         * <ul>
         * <li>1:  1 rows per hour =   1 Hour</li>
         * <li>2:  2 rows per hour = 1/2 Hour</li>
         * <li>3:  3 rows per hour = 20 Minutes</li>
         * <li>4:  4 rows per hour = 15 Minutes</li>
         * <li>6:  6 rows per hour = 10 Minutes</li>
         * <li>12: 12 rows per hour =  5 Minutes</li>
         * </ul>
         * Default is 2.
         */
        public void setRowsPerHour(int rows)
        {
            m_rowsPerHour = rows;
        }

        public int getRowsPerHour()
        {
            return m_rowsPerHour;
        }

        public void setWorktime(int startHour, int endHour)
        {
            this.startMinutes = startHour * 60;
            this.endMinutes = endHour * 60;
        }

        public void setWorktimeMinutes(int startMinutes, int endMinutes)
        {
            this.startMinutes = startMinutes;
            this.endMinutes = endMinutes;
        }

        public void setToDate(Date weekDate)
        {
            calcMinMaxDates(weekDate);
        }

        public Collection<Block> getBlocks()
        {
            return blocks;
        }

        /** must be called after the slots are filled*/
        protected boolean isEmpty(int column)
        {
            return daySlots[column].isEmpty();
        }

        protected int getColumnCount()
        {
            return getDaysInView();
        }

        public void rebuild()
        {
            int columns = getColumnCount();
            blocks.clear();
            daySlots = new HTMLDaySlot[columns];

            String[] headerNames = new String[columns];

            for (int i = 0; i < columns; i++)
            {
                String headerName = createColumnHeader(i);
                headerNames[i] = headerName;
            }

            // calculate the blocks
            int start = startMinutes;
            int end = endMinutes;
            minuteBlock.clear();
            Iterator<Builder> it = builders.iterator();
            while (it.hasNext())
            {
                Builder b = it.next();
                b.prepareBuild(getStartDate(), getEndDate());
                start = Math.min(b.getMinMinutes(), start);
                end = Math.max(b.getMaxMinutes(), end);
                if (start < 0)
                    throw new IllegalStateException("builder.getMin() is smaller than 0");
                if (end > 24 * 60)
                    throw new IllegalStateException("builder.getMax() is greater than 24");
            }
            minMinute = start;
            maxMinute = end;
            for (int i = 0; i < daySlots.length; i++)
            {
                daySlots[i] = new HTMLDaySlot(2, headerNames[i]);
            }

            it = builders.iterator();
            while (it.hasNext())
            {
                Builder b = it.next();
                if (b.isEnabled())
                {
                    b.build(this);
                }
            }
            boolean useAM_PM = org.rapla.components.calendarview.AbstractCalendar.isAmPmFormat(locale);
            for (int minuteOfDay = minMinute; minuteOfDay < maxMinute; minuteOfDay++)
            {
                boolean isLine = (minuteOfDay) % (60 / m_rowsPerHour) == 0;
                if (isLine || minuteOfDay == minMinute)
                {
                    minuteBlock.add(minuteOfDay);
                }
            }
            
            List<HTMLDaySlot> daylist = new ArrayList<>();
            for (int i = 0; i < daySlots.length; i++)
            {
                if (isExcluded(i))
                    continue;
                daylist.add( daySlots[i]);
            }
            List<RowSlot> timelist = new ArrayList<>();
            
            int row = 0;
            for (Integer minuteOfDay : minuteBlock)
            {
                row++;
                if (minuteBlock.last().equals(minuteOfDay))
                {
                    break;
                }
                //System.out.println("Start row " + row / m_rowsPerHour  + ":" + row % m_rowsPerHour +" " + timeString );

                boolean fullHour = (minuteOfDay) % 60 == 0;
                boolean isLine = (minuteOfDay) % (60 / m_rowsPerHour) == 0;
                if (fullHour || minuteOfDay == minMinute)
                {
                    int rowspan = calcRowspan(minuteOfDay, ((minuteOfDay / 60) + 1) * 60);
                    String timeString = formatTime(minuteOfDay, useAM_PM);
                    timelist.add( new RowSlot( timeString, rowspan));
                }
                for (int day = 0; day < columns; day++)
                {
                    if (isExcluded(day))
                        continue;

                    for (int slotnr = 0; slotnr < daySlots[day].size(); slotnr++)
                    {
                        Slot slot = daySlots[day].getSlotAt(slotnr);
                        Block block = slot.getBlock(minuteOfDay);
                        if (block != null)
                        {
                            blockCalendar.setTime(block.getEnd());
                            int endMinute = Math.min(maxMinute, blockCalendar.get(Calendar.HOUR_OF_DAY) * 60 + blockCalendar.get(Calendar.MINUTE));
                            int rowspan = calcRowspan(minuteOfDay, endMinute);
                            if (block instanceof HTMLRaplaBlock)
                            {
                               ((HTMLRaplaBlock)block).setRowCount(rowspan);
                               ((HTMLRaplaBlock)block).setRow(row);
                            }
                            slot.setLastEnd(endMinute);
                        }
                    }
                }
            }
            view.update( daylist, timelist, weeknumber);
        }
        
        static public class RowSlot
        {
            public RowSlot(String rowname, int rowspan)
            {
                this.rowname = rowname;
                this.rowspan = rowspan;
            }
            String rowname;
            int rowspan;
            public String getRowname()
            {
                return rowname;
            }
            public int getRowspan()
            {
                return rowspan;
            }
            
        }

        private int calcRowspan(int start, int end)
        {
            if (start == end)
            {
                return 0;
            }
            SortedSet<Integer> tailSet = minuteBlock.tailSet(start);
            int col = 0;
            for (Integer minute : tailSet)
            {
                if (minute < end)
                {
                    col++;
                }
                else
                {
                    break;
                }
            }
            return col;
        }

        public String getWeeknumber()
        {
            return weeknumber;
        }

        public void setWeeknumber(String weeknumber)
        {
            this.weeknumber = weeknumber;
        }

        protected void printBlock(StringBuffer result, @SuppressWarnings("unused") int firstEventMarkerId, Block block)
        {
            String string = block.toString();
            result.append(string);
        }

        protected String createColumnHeader(int i)
        {
            blockCalendar.setTime(getStartDate());
            blockCalendar.add(Calendar.DATE, i);
            String headerName = AbstractCalendar.formatDayOfWeekDateMonth(blockCalendar.getTime(), locale, timeZone);
            return headerName;
        }

        SortedSet<Integer> minuteBlock = new TreeSet<Integer>();

        public void addBlock(Block block, int column, int slot)
        {
            checkBlock(block);
            HTMLDaySlot multiSlot = daySlots[column];
            blockCalendar.setTime(block.getStart());

            int startMinute = Math.max(minMinute, (blockCalendar.get(Calendar.HOUR_OF_DAY) * 60 + blockCalendar.get(Calendar.MINUTE)));
            blockCalendar.setTime(block.getEnd());
            int endMinute = (Math.min(maxMinute, blockCalendar.get(Calendar.HOUR_OF_DAY) * 60 + blockCalendar.get(Calendar.MINUTE)));
            blocks.add(block);
            //            startBlock.add( startMinute);
            //       endBlock.add( endMinute);
            minuteBlock.add(startMinute);
            minuteBlock.add(endMinute);
            multiSlot.putBlock(block, slot, startMinute);

        }

        private String formatTime(int minuteOfDay, boolean useAM_PM)
        {
            blockCalendar.set(Calendar.MINUTE, minuteOfDay % 60);
            int hour = minuteOfDay / 60;
            blockCalendar.set(Calendar.HOUR_OF_DAY, hour);
            SimpleDateFormat format = new SimpleDateFormat(useAM_PM ? "h:mm" : "H:mm", locale);
            format.setTimeZone(blockCalendar.getTimeZone());
            if (useAM_PM && hour == 12 && minuteOfDay % 60 == 0)
            {
                return format.format(blockCalendar.getTime()) + " PM";
            }
            else
            {
                return format.format(blockCalendar.getTime());
            }
        }

        static public class HTMLDaySlot extends ArrayList<Slot>
        {
            private static final long serialVersionUID = 1L;
            private boolean empty = true;
            String header;

            public HTMLDaySlot(int size, String header)
            {
                super(size);
                this.header = header;
            }

            public void putBlock(Block block, int slotnr, int startMinute)
            {
                while (slotnr >= size())
                {
                    addSlot();
                }
                getSlotAt(slotnr).putBlock(block, startMinute);
                empty = false;
            }

            public int addSlot()
            {
                Slot slot = new Slot();
                add(slot);
                return size();
            }

            public Slot getSlotAt(int index)
            {
                return get(index);
            }
            
            public String getHeader()
            {
                return header;
            }
            

            public boolean isEmpty()
            {
                return empty;
            }
        }

        public static class Slot
        {
            //            int[] EMPTY = new int[]{-2};
            //      int[] SKIP = new int[]{-1};
            int lastEnd = 0;
            HashMap<Integer, Block> map = new HashMap<Integer, Block>();

            public Slot()
            {
            }

            public void putBlock(Block block, int startMinute)
            {
                map.put(startMinute, block);
            }

            Block getBlock(Integer startMinute)
            {
                return map.get(startMinute);
            }

            public int getLastEnd()
            {
                return lastEnd;
            }

            public void setLastEnd(int lastEnd)
            {
                this.lastEnd = lastEnd;
            }

        }
    }

}
