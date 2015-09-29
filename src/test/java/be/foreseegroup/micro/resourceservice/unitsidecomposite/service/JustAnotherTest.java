package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import be.foreseegroup.micro.resourceservice.unitsidecomposite.model.Consultant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.*;

import java.net.URI;

/**
 * Created by Kaj on 29/09/15.
 */
public class JustAnotherTest {
    @InjectMocks
    private ConsultantCompositeIntegration consultantIntegration;

    @Mock
    private ContractCompositeIntegration contractIntegration;

    @Mock
    private ServiceUtils util;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    //@Mock
    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockServer = MockRestServiceServer.createServer(restTemplate);
        consultantIntegration.setRestTemplate(restTemplate);
    }
/*
    @Test
    public void lalala(){
        mockServer.expect(requestTo("http://google.com"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("resultSuccess", MediaType.TEXT_PLAIN));

        String result = consultantIntegration.getMessage();

        mockServer.verify();
        assertThat(result, allOf(containsString("SUCCESS"),
                containsString("resultSuccess")));
    }*/

    public String objectToJson(Object o) {
        String result = "";
        try {
            result = mapper.writeValueAsString(o);
        } catch (Exception e) {

        }
        return result;
    }

    @Test
    public void lal() {
        mockServer.expect(requestTo("http://www.google.com")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("resultSuccess", MediaType.TEXT_PLAIN));
        String result = consultantIntegration.getMessage();

        mockServer.verify();
        assertThat(result, allOf(containsString("SUCCESS"),
                containsString("resultSuccess")));
    }

    @Test
    public void objecttest() {
        Consultant c = new Consultant("1","Kaj","Van der Hallen","Willebroek");
        //String json = "{ \"firstname\" : \"Kaj\", \"lastname\" : \"Van der Hallen\", \"address\" : \"Willebroek\"}";

        String json = objectToJson(c);

        when(util.getServiceUrl("consultant")).thenReturn(URI.create(""));

        mockServer.expect(requestTo("/consultants/42")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        ResponseEntity<Consultant> consultantResponse = consultantIntegration.getConsultantById("42");
        Consultant consultant = consultantResponse.getBody();

        assertEquals("Firstname does not match", "Kaj", consultant.getFirstname());
        assertEquals("Lastname does not match", "Van der Hallen", consultant.getLastname());
        assertEquals("Address does not match", "Willebroek", consultant.getAddress());

    }

    /*
    @Test
    public void OneOfMyTests() {
        when(util.getServiceUrl("consultant")).thenReturn(URI.create("http://www.google.com"));




        mockServer.expect(requestTo("http://www.google.com/consultants")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{ \"id\" : \"42\", \"name\" : \"Holiday Inn\"}", MediaType.APPLICATION_JSON));
        ResponseEntity<Iterable<Consultant>> consultants = consultantIntegration.getAllConsultants();
    }*/
}
