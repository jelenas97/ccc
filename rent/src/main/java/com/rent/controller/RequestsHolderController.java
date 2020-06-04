package com.rent.controller;

import com.rent.dto.RequestsHolderDTO;
import com.rent.service.RequestsHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "requestHolder")
@CrossOrigin("http://localhost:4200")
public class RequestsHolderController {

    @Autowired
    private RequestsHolderService requestsHolderService;

    @GetMapping(value = "/{id}", produces = "application/json")
    //    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_AGENT')")
    public ResponseEntity<?> getRequestHolders(@PathVariable Long id) {
        try {
            //dobavi ovde id-eve od oglasa od ovog id-a usera
            //imas listu id-eva oglasa od coveka
            //funkcija koja za query kasnije ima
            ////    @Query(value = "select c from RequestsHolder c inner join c.rentRequests req where req.id in lista id-eva od gore where req.rentRequestStatus='PENDING'  group by c.id")
            List<RequestsHolderDTO> holders = this.requestsHolderService.getAllPending(id);
            for (RequestsHolderDTO holder : holders) {
                if (holder.getRentRequests().size() <= 1) {
                    holder.setBundle(false);
                }
            }
            return new ResponseEntity(holders, HttpStatus.OK);
        } catch (NullPointerException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
