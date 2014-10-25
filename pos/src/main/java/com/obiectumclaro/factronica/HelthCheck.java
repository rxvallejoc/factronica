package com.obiectumclaro.factronica;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/InvoiceService")
public class HelthCheck {

    @GET
    @Path("/HealthCheck")
    public Response getMsg() {

        String output = "Invoice Service is alive";

        return Response.status(200).entity(output).build();

    }
}
