insert into EmailTemplate (pk, subject, text, keyword) values (1, 'Convenio para emision de comprobantes electronicos', 'Agreement', 'agreement')
insert into Agreement(pk, status, token, duration, lastDate) values (1, 'SEND', '123', 36, sysdate)
insert into Agreement(pk, status, token, duration, lastDate) values (2, 'SEND', '1234', 0, sysdate)
insert into Customer (pk, id_Type, id, name, email) values (1, 'CEDULA', '1713652290', 'Jose Teran','iapazmino@gmail.com')
insert into Customer (pk, id_Type, id, name, email, agreement_pk) values (2, 'CEDULA', '1234567890', 'Perico de loa Palotes','perico@gmail.com', 1)
insert into Customer (pk, id_Type, id, name, email, agreement_pk) values (3, 'RUC', '1234567890001', 'Juana Salgado','jsalgado@gmail.com', 2)
commit