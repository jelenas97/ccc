package com.rent.service.impl;


import com.core.commands.ReserveCommand;
import com.rent.client.AdvertisementClient;
import com.rent.client.MessagesClient;
import com.rent.client.StatisticsClient;
import com.rent.client.UserClient;
import com.rent.dto.*;
import com.rent.enumerations.RentRequestStatus;
import com.rent.model.RentRequest;
import com.rent.model.RequestsHolder;
import com.rent.rabbitmq.ProducerRMQ;
import com.rent.service.RentRequestService;
import com.rent.service.RequestsHolderService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.rent.repository.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RentRequestImpl implements RentRequestService {

    @Autowired
    private RentRequestRepository rentRequestRepository;

    @Inject
    private transient CommandGateway commandGateway;

    @Autowired
    private AdvertisementClient advertisementClient;

    @Autowired
    private MessagesClient messagesClient;

    @Autowired
    private StatisticsClient statisticsClient;

    @Autowired
    private UserClient userClient;


    @Autowired
    private RequestsHolderService requestsHolderService;




    @Override
    @Transactional
    public void rent(RentRequest rentRequest) {
        if (rentRequest.getRentRequestStatus().equals(RentRequestStatus.PENDING)) {
            rentRequest.setRentRequestStatus(RentRequestStatus.RESERVED);
            System.out.println("Salje se komanda" + rentRequest.getId() + " sa" + rentRequest.getAdvertisementId());
            this.save(rentRequest);
            String rentAggregateId = UUID.randomUUID().toString();
            commandGateway.send(new ReserveCommand(rentAggregateId, rentRequest.getId(), rentRequest.getRentRequestStatus().toString(), rentRequest.getStartDateTime().toString(), rentRequest.getEndDateTime().toString(), rentRequest.getAdvertisementId()));
            System.out.println("Poslata je komanda");
        }
    }


    @Override
    public List<RentRequestDTO> getHistoryRentRequests(long id) {

        List<RentRequestDTO> historyList = new ArrayList<>();

        LocalDateTime dateTime = LocalDateTime.now();
        RentRequestStatus status = RentRequestStatus.PAID;
        List<RentRequest> historyListR = rentRequestRepository.findBySenderIdAndRentRequestStatusAndEndDateTimeLessThanEqual(id, status, dateTime);

        CommentAndRateDTO dto = statisticsClient.getCommentsAndRates(id);

        System.out.println(historyListR);
        for (RentRequest rr : historyListR) {

            String carClass= advertisementClient.getRentRequestsCarClass(rr.getAdvertisementId());
            historyList.add(new RentRequestDTO(rr, dto, carClass));
        }

        System.out.println(historyList);
        return historyList;
    }

    @Override
    public List<RentRequestDTO> getCancelableRentRequests(long id) {

        List<RentRequestDTO> cancelableList = new ArrayList<>();

        List<RentRequestStatus> statuses = new ArrayList<>();
        statuses.add(RentRequestStatus.PENDING);
        statuses.add(RentRequestStatus.RESERVED);
        List<RentRequest> cancelableListR = rentRequestRepository.findBySenderIdAndRentRequestStatusIn(id, statuses);

        System.out.println(cancelableListR);
        for (RentRequest rr : cancelableListR) {

            String carClass= advertisementClient.getRentRequestsCarClass(rr.getAdvertisementId());
            cancelableList.add(new RentRequestDTO(rr, 0, carClass));
        }

        return cancelableList;
    }

    @Override
    public List<RentRequestDTO> getPaidRentRequests(long id) {

        List<RentRequestDTO> paidList = new ArrayList<>();

        List<RentRequestStatus> statuses = new ArrayList<>();
        statuses.add(RentRequestStatus.PAID);
        List<RentRequest> paidListR = rentRequestRepository.findBySenderIdAndRentRequestStatusIn(id, statuses);

        for (RentRequest rr : paidListR) {

            String carClass= advertisementClient.getRentRequestsCarClass(rr.getAdvertisementId());
            paidList.add(new RentRequestDTO(rr, 0, carClass));
        }

        return paidList;
    }

    @Override
    public void changeStatus(Long id, String status) {
        RentRequest rentRequest = this.rentRequestRepository.find(id);
        rentRequest.setRentRequestStatus(RentRequestStatus.valueOf(status));
        this.rentRequestRepository.save(rentRequest);
    }

    @Override
    public List<RentRequest> findPending(Long id, LocalDateTime startDate, LocalDateTime endDate) {
        return this.rentRequestRepository.findPending(id, startDate, endDate);
    }


    @Override
    public RentRequest save(RentRequest rentRequest) {
        return this.rentRequestRepository.save(rentRequest);
    }

    @Override
    @Transactional
    //  @Scheduled(cron = "${every30sec.cron}")
    @Scheduled(cron = "${fourHours.cron}")
    public void cleanOldRequests() {
        System.out.println("Requests maintenance");
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        List<RentRequest> requests = this.rentRequestRepository.findOldRequests(yesterday);

        System.out.println("Found : " + requests);

        for (RentRequest r : requests) {
            r.setRentRequestStatus(RentRequestStatus.CANCELED);
            this.save(r);
        }
    }

    @Override
    public RentRequest physicalRent(RentRequestDTO rentDTO) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        System.out.println("Physical rent " + rentDTO);
        RequestsHolder holder = new RequestsHolder();
        holder.setBundle(false);
        RentRequest req1 = new RentRequest(rentDTO, rentDTO.getSenderId(), rentDTO.getAdvertisementId(), holder, RentRequestStatus.PENDING);
        this.save(req1);
        RentRequest req = this.rentRequestRepository.findById(req1.getId()).orElse(null);
        if (req != null) {
            this.rent(req);
        }
        //automatsko odbijanje
        List<RentRequest> rentRequests = this.findPending(rentDTO.getAdvertisementId(), rentDTO.getStartDateTime(), rentDTO.getEndDateTime());
        System.out.println("OVI SU VEC POSTOJALI: " + rentRequests);

        this.automaticRejection(rentRequests);
        return req;
    }

    @Override
    public RentRequestDTO cancelRentRequest(long id) {

        RentRequest rr = this.rentRequestRepository.find(id);
        rr.setRentRequestStatus(RentRequestStatus.CANCELED);
        this.rentRequestRepository.save(rr);
        String carClass= advertisementClient.getRentRequestsCarClass(rr.getAdvertisementId());
        RentRequestDTO rrDTO = new RentRequestDTO(rr, 0,carClass);

        return rrDTO;
    }

    @Override
    public RentRequestDTO payRentRequest(long id) {

        RentRequest rr = this.rentRequestRepository.find(id);
        rr.setRentRequestStatus(RentRequestStatus.PAID);
        this.rentRequestRepository.save(rr);
        String carClass= advertisementClient.getRentRequestsCarClass(rr.getAdvertisementId());
        RentRequestDTO rrDTO = new RentRequestDTO(rr, 0,carClass);

        return rrDTO;
    }

    @Override
    public List<RentRequest> getHolderRequests(Long hold) {
        return this.rentRequestRepository.getHolderRequests(hold);
    }

    @Override
    public void processRequest(String confirm, RentRequestDTO rentDTO) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        if (confirm.equals("YES")) {
            System.out.println(rentDTO);
            TermSearchDTO termSearchDTO = new TermSearchDTO(rentDTO.getAdvertisementId(), rentDTO.getStartDateTime(), rentDTO.getEndDateTime());
            List<TermDTO> term = this.advertisementClient.getTakenTerms(termSearchDTO);
            System.out.println("Zauzeti termini su " + term.toString());

            if (term.size() == 0) {
                System.out.println("NEMA TERMINA SA PREKLAPANJEM!!!!");
                RentRequest request = this.rentRequestRepository.findById(rentDTO.getId()).orElse(null);
                if (request != null) {
                    this.rent(request);
                    UserDTO userDTO = this.userClient.getUser(request.getSenderId().toString());
                    String accept = "Your request for reservation has been accepted";
                    EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                    new ProducerRMQ(emailMessage.toString());
                    List<RentRequest> rentRequests = this.findPending(rentDTO.getAdvertisementId(), rentDTO.getStartDateTime(), rentDTO.getEndDateTime());
                    System.out.println("Ove odbijam " + rentRequests);
                    this.automaticRejection(rentRequests);
                }
            } else {
                this.changeStatus(rentDTO.getId(), RentRequestStatus.CANCELED.toString());
                UserDTO userDTO = this.userClient.getUser(rentDTO.getSenderId().toString());
                String accept = "Your request for reservation has been rejected";
                EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                new ProducerRMQ(emailMessage.toString());
            }
        } else {
            this.changeStatus(rentDTO.getId(), RentRequestStatus.CANCELED.toString());
            UserDTO userDTO = this.userClient.getUser(rentDTO.getSenderId().toString());
            String accept = "Your request for reservation has been rejected";
            EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
            new ProducerRMQ(emailMessage.toString());
        }
    }

    @Override
    public void processRequestsBundle(String confirm, RequestsHolderDTO holderDTO) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        if (confirm.equals("YES")) {
            //true = nema preklapanja  u jednom terminu! Dodaj ih sve!
            //false = ima preklapanja u jednom/vise! Sve odbij!
            Boolean yes = true;
            for (RentRequestDTO rentDTO : holderDTO.getRentRequests()) {
                System.out.println(rentDTO);
                TermSearchDTO termSearchDTO = new TermSearchDTO(rentDTO.getAdvertisementId(), rentDTO.getStartDateTime(), rentDTO.getEndDateTime());
                List<TermDTO> term = this.advertisementClient.getTakenTerms(termSearchDTO);
                System.out.println("Zauzeti termini su " + term.toString());
                if (term.size() != 0) {
                    yes = false;
                }
            }
            if (yes) {
                RentRequestDTO dto = new RentRequestDTO();
                for (RentRequestDTO rentDTO : holderDTO.getRentRequests()) {
                    RentRequest request = this.rentRequestRepository.findById(rentDTO.getId()).orElse(null);
                    dto = rentDTO;
                    if (request != null) {
                        this.rent(request);
                        UserDTO userDTO = this.userClient.getUser(rentDTO.getSenderId().toString());
                        String accept = "Your request for bundle reservation has been accepted";
                        EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                        new ProducerRMQ(emailMessage.toString());
                    }
                }
                List<RentRequest> rentRequests = this.findPending(dto.getAdvertisementId(), dto.getStartDateTime(), dto.getEndDateTime());
                this.automaticRejection(rentRequests);
            } else {
                Long id = 1L;
                for (RentRequestDTO rentDTO : holderDTO.getRentRequests()) {
                    this.changeStatus(rentDTO.getId(), RentRequestStatus.CANCELED.toString());
                    id = rentDTO.getSenderId();
                }
                UserDTO userDTO = this.userClient.getUser(id.toString());
                String accept = "Your request for bundle reservation has been rejected";
                EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                new ProducerRMQ(emailMessage.toString());
            }
        } else {
            Long id = 1L;
            for (RentRequestDTO r : holderDTO.getRentRequests()) {
                this.changeStatus(r.getId(), RentRequestStatus.CANCELED.toString());
                id = r.getSenderId();
            }
            UserDTO userDTO = this.userClient.getUser(id.toString());
            String accept = "Your request for bundle reservation has been rejected";
            EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
            new ProducerRMQ(emailMessage.toString());
        }
    }

    @Override
    public void sendRequest(RequestsHolderDTO holderDTO) {
        System.out.println("Posal zahtjev " + holderDTO);
        Set<Long> usersIds = new HashSet<>();
        for (RentRequestDTO requestDTO : holderDTO.getRentRequests()) {
            AdvertisementDTO ad = this.advertisementClient.getAdvertisement(requestDTO.getAdvertisementId());
            usersIds.add(ad.getOwnerId());
        }
        System.out.println("Owners:" + usersIds);
        for (Long id : usersIds) {
            RequestsHolder rq = new RequestsHolder(holderDTO.getBundle());
            System.out.println("Vlasnik = " + id);

            for (RentRequestDTO requestDTO : holderDTO.getRentRequests()) {
                AdvertisementDTO ad = this.advertisementClient.getAdvertisement(requestDTO.getAdvertisementId());

                if (id.equals(ad.getOwnerId())) {
                    RentRequest rentRequest = new RentRequest(requestDTO, requestDTO.getSenderId(), requestDTO.getAdvertisementId(), rq);
                    this.save(rentRequest);
                }
            }
        }
    }

    @Override
    public RentRequestDTO getRentRequest(String id) {
        RentRequest rentRequest = this.findById(Long.parseLong(id));
        return new RentRequestDTO(rentRequest);
    }


    public void automaticRejection(List<RentRequest> rentRequests) throws NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        for (RentRequest request : rentRequests) {
            RequestsHolder holder = this.requestsHolderService.findById(request.getRequests().getId());
            if (holder.getBundle()) {
                List<RentRequest> listReq = this.rentRequestRepository.getHolderRequests(holder.getId());
                List<Long> listIds = listReq.stream()
                        .map(RentRequest::getId)
                        .collect(Collectors.toList());
                for (Long id : listIds) {
                    System.out.println("Ovo je bilo u bundle uklanjam!!!" + id);
                    this.changeStatus(id, "CANCELED");

                }
                UserDTO userDTO = this.userClient.getUser(request.getSenderId().toString());
                String accept = "Your request for bundle reservation has been rejected because other requests are accepted";
                EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                new ProducerRMQ(emailMessage.toString());
            } else {
                UserDTO userDTO = this.userClient.getUser(request.getSenderId().toString());
                String accept = "Your request for reservation has been rejected because other requests are accepted";
                EmailMessage emailMessage = new EmailMessage(userDTO.getEmail(), accept);
                new ProducerRMQ(emailMessage.toString());
                this.changeStatus(request.getId(), "CANCELED");
            }
        }
    }
    @Override
    public RentRequest findById(long id) {
        return rentRequestRepository.findById(id).orElse(null);
    }

    @Override
    public List<RentRequestDTO> getRentRequestReserved(long id) {
        try {

            List<RentRequestDTO> reserved = new ArrayList<>();

            List<RentRequest> listReserved = this.rentRequestRepository.findBySenderIdAndStatus(id);
            List<AdvertisementDTO> advertisementDTO = this.advertisementClient.getUserAdvertisements(id);
            List<Long> ids = new ArrayList<Long>();
            for(AdvertisementDTO ad : advertisementDTO)
            {
                ids.add(ad.getId());
            }
            List<RentRequest> listOwnerId = this.rentRequestRepository.findByOwnerIdAndStatus(ids);

            listReserved.addAll(listOwnerId);
            for (RentRequest rr : listReserved) {
                int numberOfUnseen = 0;
                List<MessageDTO> messageDTOS = new ArrayList<MessageDTO>();
                messageDTOS = this.messagesClient.getMessagesFromRentRequest(rr.getId().toString());
                for (MessageDTO m : messageDTOS) {
                    if (m.getRecepientId().equals(id) && !m.isSeen()) {
                        numberOfUnseen++;
                    }
                }
                reserved.add(new RentRequestDTO(rr, numberOfUnseen));
            }

            return reserved;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }



}
