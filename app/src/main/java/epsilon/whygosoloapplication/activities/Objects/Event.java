/*
    Author: Luke Collier
    Description: The local event object
    Date Created: N/A
    Last Edited by: Luke Colliers
    Date Edited: 07/05/2016
    Edit Note: Added comments
 */
package epsilon.whygosoloapplication.activities.objects;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Joker on 22/04/16.
 */
public class Event {
    private String name;
    private String location;
    private String category;
    private Calendar fromCalendar;
    private Calendar toCalendar;
    private String description;
    private int eventId;

    private int pictureId;

    public Event() {
        this.fromCalendar = new GregorianCalendar();
        this.toCalendar = new GregorianCalendar();
    }

    /**
     * Gets the name of the event
     * @return the name of the event
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the location of the event TODO: Update to another object
     * @return the location name
     */
    public String getLocation() {
        return location;
    }

    /**
     * Get the from from time for the calendar
     * @return the from calendar
     */
    public Calendar getFromCalendar() {
        return fromCalendar;
    }

    /**
     * Get the to time for the calendar
     * @return the to calender
     */
    public Calendar getToCalendar() {
        return toCalendar;
    }

    /**
     * The description of the event
     * @return the event description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the id for the picture TODO: Update to a link to a web address given by the API GUYS
     * @return an image ID
     */
    public int getPictureId() {
        return pictureId;
    }

    /**
     * Set the from date for the calendar
     * @param dayOfMonth the day
     * @param month the month
     * @param year the year
     */
    public void setFromDate(int dayOfMonth, int month, int year) {
        fromCalendar.set(Calendar.YEAR,year);
        fromCalendar.set(Calendar.MONTH,month);
        fromCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    /**
     * Sets the time for the calendar
     * @param hour the hour
     * @param minute the minutes
     */
    public void setFromTime(int hour, int minute) {
        fromCalendar.set(Calendar.MINUTE,minute);
        fromCalendar.set(Calendar.HOUR_OF_DAY,hour);
    }

    /**
     * Sets the date for the calendar
     * @param dayOfMonth the day
     * @param month the month
     * @param year the year
     */
    public void setToDate(int dayOfMonth, int month, int year) {
        toCalendar.set(Calendar.YEAR,year);
        toCalendar.set(Calendar.MONTH,month);
        toCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }

    /**
     * Sets the time for the calendar
     * @param hour the hour
     * @param minute the minute
     */
    public void setToTime(int hour, int minute) {
        toCalendar.set(Calendar.MINUTE,minute);
        toCalendar.set(Calendar.HOUR_OF_DAY,hour);
    }

    /**
     * Set the from calendar directly
     * @param date the new date to set it too
     */
    public void setFromCalendar(Date date) {
        fromCalendar.setTime(date);
    }

    /**
     * Set the to calendar directly
     * @param date the new date to set it too
     */
    public void setToCalendar(Date date) {
        toCalendar.setTime(date);
    }

    /**
     * Sets the name of the event
     * @param name the new event name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the event location name
     * @param location the new event location name
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the events category
     * @param category the new category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the event description
     * @param description the new description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the picture ID to a local resource in the project
     * @param id the local resource ID
     */
    public void setPictureId(int id) {
        this.pictureId = id;
    }

    /**
     * Gets the Events id for the server
     * @return the server id
     */
    public int getId() {
        return eventId;
    }

    /**
     * Sets the event server id
     * @param id the servers id for the event
     */
    public void setId(int id) {
        eventId = id;
    }
}
