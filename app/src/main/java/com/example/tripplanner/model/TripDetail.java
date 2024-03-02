/*
 * FILE          : PassengerInfo.java
 * AUTHORS       : Mher Keshishian
 * FIRST VERSION : January 27th
 * PURPOSE       : Passenger information.
 */

package com.example.tripplanner.model;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TripDetail {
    private DestinationModel location = null;
    private Date startDate = null;
    private Date endDate = null;
    private String buyerName = null;
    private int numberOfPassengers = 1;
    private boolean isInsuranceSelected;

    /**
     * FUNCTION      : getStartDate
     * PURPOSE       : Get the start date of the trip.
     * RETURN        : The start date.
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * FUNCTION      : setStartDate
     * PURPOSE       : Set the start date of the trip.
     * PARAMETER     : startDate - The start date to set.
     * RETURN        : void
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * FUNCTION      : getEndDate
     * PURPOSE       : Get the end date of the trip.
     * RETURN        : The end date.
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * FUNCTION      : setEndDate
     * PURPOSE       : Set the end date of the trip.
     * PARAMETER     : endDate - The end date to set.
     * RETURN        : void
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * FUNCTION      : getBuyerName
     * PURPOSE       : Get the name of the person who ordered the trip.
     * RETURN        : The buyer name.
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * FUNCTION      : setBuyerName
     * PURPOSE       : Set the name of the person who ordered the trip.
     * PARAMETER     : buyerName - The buyer's name to set.
     * RETURN        : void
     */
    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    /**
     * FUNCTION      : getNumberOfPassengers
     * PURPOSE       : Get the number of passengers for the trip.
     * RETURN        : The number of passengers.
     */
    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }

    /**
     * FUNCTION      : setNumberOfPassengers
     * PURPOSE       : Set the number of passengers for the trip.
     * PARAMETER     : numberOfPassengers - The number of passengers to set.
     * RETURN        : void
     */
    public void setNumberOfPassengers(int numberOfPassengers) {
        this.numberOfPassengers = numberOfPassengers;
    }


    /**
     * FUNCTION      : getDurationInDays
     * PURPOSE       : Returns the duration in days between start and end dates.
     * Minimum value is one.
     * RETURN        : The duration in days.
     */
    public long getDurationInDays() {
        long diffInMilliseconds = endDate.getTime() - startDate.getTime();
        return Math.max(1, TimeUnit.DAYS.convert(diffInMilliseconds, TimeUnit.MILLISECONDS));
    }

    /**
     * FUNCTION      : getLocation
     * PURPOSE       : Returns the destination city model.
     * RETURN        : The destination city model.
     */
    public DestinationModel getLocation() {
        return location;
    }

    /**
     * FUNCTION      : setLocation
     * PURPOSE       : Set the destination city model.
     * PARAMETER     : location - The destination city model to set.
     * RETURN        : void
     */
    public void setLocation(DestinationModel location) {
        this.location = location;
    }

    /**
     * FUNCTION      : getTotalPrice
     * PURPOSE       : Returns total price for the selected city and duration in days.
     * RETURN        : The total price.
     */
    public long getTotalPrice() {
        // Calculate the total price dynamically based on the current state
        return location.getPrice() * getDurationInDays() + (isInsuranceSelected() ? 100 : 0);
    }


    /**
     * FUNCTION      : isInsuranceSelected
     * PURPOSE       : Check if insurance is selected.
     * RETURN        : True if insurance is selected, false otherwise.
     */
    public boolean isInsuranceSelected() {
        return isInsuranceSelected;
    }

    /**
     * FUNCTION      : setInsuranceSelected
     * PURPOSE       : Set whether insurance is selected.
     * PARAMETER     : insuranceSelected - True if insurance is selected, false otherwise.
     * RETURN        : void
     */
    public void setInsuranceSelected(boolean insuranceSelected) {
        this.isInsuranceSelected = insuranceSelected;

    }
}