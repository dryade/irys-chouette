/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.chouette.client.dao;

import net.dryade.siri.chouette.client.model.DatedVehicleJourney;

/**
 *
 * @author marc
 */
public interface DatedVehicleJourneyDao {
       
    void save(DatedVehicleJourney datedVehicleJourney);

    DatedVehicleJourney get(String datedVehicleJourneyNeptuneRef);
 
    void deleteAll();
}
