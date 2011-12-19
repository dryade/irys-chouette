/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dryade.siri.chouette.client;

import net.dryade.siri.chouette.client.dao.DatedCallDao;
import net.dryade.siri.chouette.client.model.DatedCallNeptune;
import net.dryade.siri.chouette.client.model.DatedVehicleJourneyNeptune;
import net.dryade.siri.chouette.client.dao.DatedVehicleJourneyDao;
import net.dryade.siri.chouette.client.adapter.MonitoredVisitAdapter;
import net.dryade.siri.sequencer.model.MonitoredVisit;
import net.dryade.siri.sequencer.model.StopMonitoringNotificationResponse;
import net.dryade.siri.chouette.client.factory.DomainObjectBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 *
 * @author marc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/persistenceConfig.xml"})
public class StopMonitoringServiceTest {
    
    private StopMonitoringService smService = null;
    
    private StopMonitoringNotificationResponse sm = null;
    private MonitoredVisit visit = null;
    
    @Before
    public void setUp() {
        visit = DomainObjectBuilder.aNew().monitoredVisitBuilder().build();
        
        sm = new StopMonitoringNotificationResponse( "req", "res");
        //sm.addMonitoredVisit( visit );
        
    }

    @Test
    public void testUpdate() {
        // do the actual test 
        smService.update( sm); 
    }

    @Test
    public void retrieveDatedVehicleJourney() {
        //DatedCallDao mock = createMock(DatedCallDao.class);
        //smService.setDatedCallDao( mock);
        DatedVehicleJourneyNeptune dummyDVJ = (new MonitoredVisitAdapter()).read(visit);
        
        MonitoredVisitAdapter mock = createMock( MonitoredVisitAdapter.class);
        expect( mock.vehicleJourneyNeptuneRef( visit.getDatedVehicleJourneyRef())).
                andReturn( "vehicleRefNeptune");
        smService.setMonitoredVisitAdapter( mock);
        replay(mock);
        
        DatedVehicleJourneyDao mockDVJ = createMock( DatedVehicleJourneyDao.class);
        // TODO: use originAimedDepartreTime when it will be defined
        expect( mockDVJ.get( "vehicleRefNeptune", visit.getAimedDepartureTime())).andReturn( dummyDVJ);
        smService.setDatedVehicleJourneyDao(mockDVJ);
        replay(mockDVJ);

        // do the actual test 
        DatedVehicleJourneyNeptune result = smService.retrieveDatedVehicleJourney( visit);
        assertEquals( "should retrive using adpater ans dao", dummyDVJ, result); 
    }

    @Test
    public void retrieveDatedCall() {
        //DatedCallDao mock = createMock(DatedCallDao.class);
        //smService.setDatedCallDao( mock);
        DatedCallNeptune dummyCall = DomainObjectBuilder.aNew().datedCallBuilder().build();
        
        MonitoredVisitAdapter mock = createMock( MonitoredVisitAdapter.class);
        expect( mock.stopPointNeptuneRef( visit.getStopPointRef())).
                andReturn( "vehicleRefNeptune");
        smService.setMonitoredVisitAdapter( mock);
        replay(mock);
        
        DatedCallDao mockDC = createMock( DatedCallDao.class);
        expect( mockDC.get( 123L, "vehicleRefNeptune")).andReturn( dummyCall);
        smService.setDatedCallDao(mockDC);
        replay(mockDC);

        // do the actual test 
        DatedCallNeptune result = smService.retrieveDatedCall( 123L, visit);
        assertEquals( "should retrive using adpater ans dao", dummyCall, result); 
    }

    @Autowired 
    public void setSmService(StopMonitoringService smService) {
        this.smService = smService;
    }
    
}
