
package com.rent.soap.code;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the soapkodenesto package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: soapkodenesto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PhysicalRentRequest }
     * 
     */
    public PhysicalRentRequest createPhysicalRentRequest() {
        return new PhysicalRentRequest();
    }

    /**
     * Create an instance of {@link PhysicalRentResponse }
     * 
     */
    public PhysicalRentResponse createPhysicalRentResponse() {
        return new PhysicalRentResponse();
    }

    /**
     * Create an instance of {@link RentRequests }
     * 
     */
    public RentRequests createRentRequests() {
        return new RentRequests();
    }

    /**
     * Create an instance of {@link RequestsHolder }
     * 
     */
    public RequestsHolder createRequestsHolder() {
        return new RequestsHolder();
    }
    /**
     * Create an instance of {@link GetRentRequestRequest }
     *
     */
    public GetRentRequestRequest createGetRentRequestRequest() {
        return new GetRentRequestRequest();
    }

    /**
     * Create an instance of {@link GetRentRequestResponse }
     *
     */
    public GetRentRequestResponse createGetRentRequestResponse() {
        return new GetRentRequestResponse();
    }

    /**
     * Create an instance of {@link RentRequest }
     *
     */
    public RentRequest createRentRequest() {
        return new RentRequest();
    }


}
