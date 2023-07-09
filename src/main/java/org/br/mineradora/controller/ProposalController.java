package org.br.mineradora.controller;

import org.br.mineradora.dto.ProposalDetailsDTO;
import org.br.mineradora.message.KafkaEvent;
import org.br.mineradora.service.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/api/proposals")
public class ProposalController {

    private final Logger LOGGER = LoggerFactory.getLogger(ProposalController.class);

    @Inject
    private ProposalService proposalService;

    @GET
    @Path("/{id}")
    public ProposalDetailsDTO findDetailsProposal(@PathParam("id") Long id) {
        return proposalService.findFullProposal(id);
    }

    @POST
    public Response createProposal(ProposalDetailsDTO proposalDetailsDTO) {

        LOGGER.info("--- Recebendo proposta de compra --- ");

        try{
            proposalService.createNewProposal(proposalDetailsDTO);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removeProposal(@PathParam("id") Long id) {
        try {
            proposalService.removalProposal(id);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }

}
