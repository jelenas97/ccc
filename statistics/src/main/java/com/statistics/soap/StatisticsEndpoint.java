package com.statistics.soap;

import com.statistics.dto.CommentDTO;
import com.statistics.service.CommentService;
import com.statistics.soap.code.*;
import com.statistics.soap.code.CommentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDate;
import java.util.List;

@Endpoint
public class StatisticsEndpoint {
    private static final String NAMESPACE_URI = "http://localhost:8090/microservices/statistics";

    @Autowired
    private CommentService commentService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "commentRequest")
    @ResponsePayload
    public CommentResponse commentResponse(@RequestPayload CommentRequest request) {
        System.out.println("Soap request");


        CommentResponse response = new CommentResponse();

        com.statistics.dto.CommentDTO commentDTO = new com.statistics.dto.CommentDTO();
        commentDTO.setAdvertisement_id(request.getAdvertisementId());

        LocalDate date = LocalDate.parse(request.getDate());

        commentDTO.setDate(date);
        commentDTO.setDateString(date.toString());
        commentDTO.setContent(request.getContent());
        commentDTO.setStatus(request.getStatus());
        commentDTO.setCommenter_id(request.getCommenterId());
        commentDTO.setCommenter(request.getCommenter());
        commentDTO.setRent_request_id(request.getRentRequestId());
        long id = this.commentService.addComment(commentDTO);

        response.setCommentId(id);

        System.out.println("zavrsio request");
        return response;

    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "commentOwnerRequest")
    @ResponsePayload
    public CommentOwnerResponse commentOwnerResponse(@RequestPayload CommentOwnerRequest request) {
        System.out.println("Soap request");

        CommentOwnerResponse response = new CommentOwnerResponse();

        com.statistics.dto.CommentDTO commentDTO = new com.statistics.dto.CommentDTO();
        commentDTO.setAdvertisement_id(request.getAdvertisementId());

        LocalDate date = LocalDate.parse(request.getDate());

        commentDTO.setDate(date);
        commentDTO.setDateString(date.toString());
        commentDTO.setContent(request.getContent());
        commentDTO.setStatus(request.getStatus());
        commentDTO.setCommenter_id(request.getCommenterId());
        commentDTO.setCommenter(request.getCommenter());
        long id = this.commentService.addCommentOwner(commentDTO);

        response.setCommentId(id);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCommentRequest")
    @ResponsePayload
    public GetCommentResponse getComments(@RequestPayload GetCommentRequest request) {
        System.out.println("Soap request");


        List<CommentDTO> comments = commentService.findProcessedAdvertisementComments(request.getAdvertisementId());
        GetCommentResponse response = new GetCommentResponse();

        for (CommentDTO dto : comments) {
            com.statistics.soap.code.CommentDTO soapComment = new com.statistics.soap.code.CommentDTO();
            soapComment.setId(dto.getId());
            soapComment.setContent(dto.getContent());
            soapComment.setDate(dto.getDateString());
            soapComment.setDateString(dto.getDate().toString());
            soapComment.setStatus(dto.getStatus());
            soapComment.setAdvertisementId(dto.getAdvertisement_id());
            soapComment.setCommenterId(dto.getCommenter_id());
            soapComment.setCommenterId(dto.getCommenter_id());
            soapComment.setCommenter(dto.getCommenter());
            soapComment.setRentRequestId(dto.getRent_request_id());
            response.getComment().add(soapComment);
        }


        return response;

    }
}
