insert into requests_holder(id, bundle)
VALUES (1, true);
insert into requests_holder(id, bundle)
VALUES (2, true);
insert into requests_holder(id, bundle)
VALUES (3, false);

insert into requests_holder(id, bundle)
VALUES (4, false);

insert into requests_holder(id, bundle)
VALUES (5, false);

insert into requests_holder(id, bundle)
VALUES (6, false);


insert into rent_request(advertisement_id, sender_id, end_date_time, rent_request_status, start_date_time, requests_id,version, created)
values (2, 1, '2020-06-03 05:05:05', 'PENDING', '2020-06-15 04:04:04', 4, 1, '2020-06-18T12:28:09.063');
insert into rent_request(advertisement_id, sender_id, end_date_time, rent_request_status, start_date_time, requests_id,version, created)
values (2, 1, '2020-06-12 05:05:05', 'PENDING', '2020-06-22 04:04:04', 5,1, '2020-06-18T12:28:09.063');
insert into rent_request(advertisement_id, sender_id, end_date_time, rent_request_status, start_date_time, requests_id,version, created)
values (2, 1, '2020-05-25 05:05:05', 'PENDING', '2020-05-20 04:04:04', 6,1, '2020-06-18T12:28:09.063');
insert into rent_request(advertisement_id, sender_id, end_date_time, rent_request_status, start_date_time, requests_id,version, created)
values (2, 1, '2020-05-13 05:05:05', 'PENDING', '2020-05-19 04:04:04', 6,1, '2020-06-18T12:28:09.063');

insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-07-19T22:00', 'PENDING', '2020-06-17T22:00', 1, 1, 1, 1, '2020-06-18T12:28:09.063');
insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-07-21T22:00', 'PENDING', '2020-06-20T22:00', 1, 1, 1, 1, '2020-06-18T12:28:09.063');
insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-07-29T22:00', 'PENDING', '2020-06-27T22:00', 1, 1, 1, 1, '2020-06-18T12:28:09.063');



insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-07-19T22:00', 'PENDING', '2020-06-17T22:00', 1, 2, 2, 1, '2020-06-18T12:28:09.063');
insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-07-21T22:00', 'PENDING', '2020-06-20T22:00', 1, 2, 2, 1, '2020-06-18T12:28:09.063');
insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-08-04T22:00', 'PENDING', '2020-06-30T22:00', 1, 2, 2, 1, '2020-06-18T12:28:09.063');



insert into rent_request(end_date_time, rent_request_status, start_date_time, advertisement_id, requests_id, sender_id,
                         version, created)
values ('2020-08-26T22:00', 'PENDING', '2020-07-03T22:00', 1, 3, 2, 1, '2020-06-18T12:28:09.063');



