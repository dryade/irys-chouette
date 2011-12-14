/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.chouette.client.dao;

import java.util.List;
import net.dryade.siri.chouette.client.model.DatedVehicleJourney;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author marc
 */
public class DatedVehicleJourneyDaoImpl implements DatedVehicleJourneyDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public void save(DatedVehicleJourney datedVehicleJourney)
    {
        this.sessionFactory.getCurrentSession().save( datedVehicleJourney);
    }
    
    @Override
    public void deleteAll()
    {
        this.sessionFactory.getCurrentSession().createQuery( "delete DatedVehicleJourney");
    }

    @Override
    public DatedVehicleJourney get(String datedVehicleJourneyNeptuneRef)
    {
        List<DatedVehicleJourney> results = this.sessionFactory.getCurrentSession().createCriteria(DatedVehicleJourney.class).
                add( Restrictions.eq("datedVehicleJourneyNeptuneRef", datedVehicleJourneyNeptuneRef)).
                list();
        if ( results.isEmpty())
            return null;
        return results.iterator().next();
    }

}
