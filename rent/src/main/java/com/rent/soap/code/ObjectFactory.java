
package com.rent.soap.code;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.rent.soap.code package. 
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
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.rent.soap.code
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ConfirmRentRequestRequest }
     * 
     */
    public ConfirmRentRequestRequest createConfirmRentRequestRequest() {
        return new ConfirmRentRequestRequest();
    }

    /**
     * Create an instance of {@link RentRequest }
     * 
     */
    public RentRequest createRentRequest() {
        return new RentRequest();
    }

    /**
     * Create an instance of {@link ConfirmRequestHolderRequest }
     * 
     */
    public ConfirmRequestHolderRequest createConfirmRequestHolderRequest() {
        return new ConfirmRequestHolderRequest();
    }

    /**
     * Create an instance of {@link RequestsHolderDTO }
     * 
     */
    public RequestsHolderDTO createRequestsHolderDTO() {
        return new RequestsHolderDTO();
    }

    /**
     * Create an instance of {@link GetRequestHoldersRequest }
     * 
     */
    public GetRequestHoldersRequest createGetRequestHoldersRequest() {
        return new GetRequestHoldersRequest();
    }

    /**
     * Create an instance of {@link ConfirmRequestHolderResponse }
     * 
     */
    public ConfirmRequestHolderResponse createConfirmRequestHolderResponse() {
        return new ConfirmRequestHolderResponse();
    }

    /**
     * Create an instance of {@link GetRentRequestRequest }
     * 
     */
    public GetRentRequestRequest createGetRentRequestRequest() {
        return new GetRentRequestRequest();
    }

    /**
     * Create an instance of {@link ConfirmRentRequestResponse }
     * 
     */
    public ConfirmRentRequestResponse createConfirmRentRequestResponse() {
        return new ConfirmRentRequestResponse();
    }

    /**
     * Create an instance of {@link GetRequestHoldersResponse }
     * 
     */
    public GetRequestHoldersResponse createGetRequestHoldersResponse() {
        return new GetRequestHoldersResponse();
    }

    /**
     * Create an instance of {@link GetRentRequestResponse }
     * 
     */
    public GetRentRequestResponse createGetRentRequestResponse() {
        return new GetRentRequestResponse();
    }

}
