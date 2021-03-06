/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.siri.chouette.client.dao;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import irys.siri.chouette.client.adapter.InfoMessageAdapter;
import irys.siri.realtime.dao.InfoMessageDao;
import irys.siri.realtime.model.InfoMessageNeptune;
import irys.siri.realtime.model.Message;

import org.junit.Before;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import test.siri.chouette.client.factory.DomainObjectBuilder;

//@RunWith(SpringJUnit4ClassRunner.class)

/**
 *
 * @author marc
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:testContext.xml"})
@TransactionConfiguration(transactionManager="myTxManager", defaultRollback=false)
@Transactional
public class InfoMessageDaoTest  {
    private static final Logger logger = Logger.getLogger(InfoMessageDaoTest.class); 
	
	private InfoMessageAdapter adapter ;
    private InfoMessageDao gmDAO = null;
    private InfoMessageNeptune infoMessage;
    private List<Message> messages;
    private Message firstMessage;

    @Before
    public void cleanDB() {
        firstMessage = DomainObjectBuilder.aNew().message().build();
        messages = new ArrayList<Message>();
        messages.add( firstMessage);
        infoMessage = adapter.read( DomainObjectBuilder.aNew().infoMessage().
                        withMessages( messages).
                        build());
        this.gmDAO.deleteAll();
    }

    @Test
    public void testInstanceFoundAfterSave() throws Exception {
        this.gmDAO.save( infoMessage);
        
        InfoMessageNeptune retreiveData = this.gmDAO.get( infoMessage.getMessageId());
        assertNotNull("should have retreive persisted instance", retreiveData);
    }

    @Test
    public void testPropertiesPersistence() throws Exception {
        this.gmDAO.save( infoMessage);
        
        InfoMessageNeptune retreiveData = this.gmDAO.get( infoMessage.getMessageId());
        assertEquals("should have persisted ValidUntilTime", infoMessage.getValidUntilTime(), retreiveData.getValidUntilTime());
        assertEquals("should have persisted MessageVersion", infoMessage.getMessageVersion(), retreiveData.getMessageVersion());
        assertEquals("should have persisted RecordedAtTime", infoMessage.getRecordedAtTime(), retreiveData.getRecordedAtTime());
    }

    @Test
    public void testMessageListPersistence() throws Exception {
    	try
    	{
    	this.gmDAO.save( infoMessage);
    	}
    	catch (Exception e) 
    	{
			logger.error(e.getMessage(),e);
			Throwable e1 = e.getCause();
			while (e1 != null)
			{
				logger.error("caused by" + e1.getMessage(),e1);
				e1 = e1.getCause();
			}
			throw e;
		}
        
        InfoMessageNeptune retreiveData = this.gmDAO.get( infoMessage.getMessageId());
        assertEquals("should have persisted all messages", infoMessage.getMessages().size(), retreiveData.getMessages().size());
    }

    @Test
    public void testMessagePersistence() throws Exception {
        this.gmDAO.save( infoMessage);
        
        InfoMessageNeptune retreiveData = this.gmDAO.get( infoMessage.getMessageId());
        
        Message retrieveFirstMessage = retreiveData.getMessages().iterator().next();
        assertEquals("should have persisted text", firstMessage.getText(), retrieveFirstMessage.getText());
        assertEquals("should have persisted type", firstMessage.getType(), retrieveFirstMessage.getType());
        assertEquals("should have persisted lang", firstMessage.getLang(), retrieveFirstMessage.getLang());
    }
    
    @Autowired 
    public void setAdapter(InfoMessageAdapter adapter) {
        this.adapter = adapter;
    }
    
    @Autowired 
    public void setGmDAO(InfoMessageDao gmDAO) {
        this.gmDAO = gmDAO;
    }
}
