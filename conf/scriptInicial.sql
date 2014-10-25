insert into facrole(pk, name) values (1, 'admin');
insert into facrole(pk, name) values (2, 'pos');
insert into facrole(pk, name) values (3, 'backoffice');

insert into facuser(pk, username, pwd, status, name, lastname) values (1, 'admin', 'ICy5YqxZB1uWSwcVLSNLcA==', 'ACT', 'Administrador', '');
insert into facuser(pk, username, pwd, status, name, lastname) values (2, 'pos', 'ICy5YqxZB1uWSwcVLSNLcA==', 'ACT', 'POS', '');
insert into facuserrol(userid, roleid) values(1, 1);
insert into facuserrol(userid, roleid) values(2, 2);

INSERT INTO Tax (pk, code, description, stateTax) VALUES (1, 1, 'RENTA', 'ACT');
INSERT INTO Tax (pk, code, description, stateTax) VALUES (2, 2, 'IVA', 'ACT');
INSERT INTO Tax (pk, code, description, stateTax) VALUES (3, 3, 'ICE', 'ACT');

INSERT INTO TaxValue (pk, description, endDate, rate, satartDate, tax_value_code, taxable, taxId_pk) VALUES (1, 'Honorarios profesionales y dietas', '2015-01-31', 1.00, '2013-01-01', 303, 0.00, 1);
INSERT INTO TaxValue (pk, description, endDate, rate, satartDate, tax_value_code, taxable, taxId_pk) VALUES (2, 'Por rendimientos financieros (No aplica para IFIs)', '2014-08-31', 2.00, '2012-12-02', 323, 0.00, 1);
INSERT INTO TaxValue (pk, description, endDate, rate, satartDate, tax_value_code, taxable, taxId_pk) VALUES (3, 'ICE \u2013 BEBIDAS GASEOSAS', '2013-07-31', 10.00, '2012-12-02', 3051, 0.00, 2);
INSERT INTO TaxValue VALUES (4,'IVA 12','2013-07-31','12.00','2012-12-02','2','0.00',2);
		
INSERT INTO EmailTemplate VALUES (1,'acuerdo-email','ACUERDO DE FACTURACION ELECTRONICA','El enlace a continuaci&oacute;n lo dirige al acuerdo de emisi&oacute;n de comprobantes electr&oacute;nicos, necesario para acceder al servicio.');
INSERT INTO EmailTemplate VALUES (2,'acuerdo','ACUERDO DE FACTURACION ELECTRONICA INTEGRAL DATA ','El presente es un acuerdo entre el cliente, y, la empresa Integral Data S.A.<br />Al aceptar el presente acuerdo el cliente permite que el vendedor entregue el(los) comprobante de la(s) transacci&oacute;n(es) con el cliente en formato electr&oacute;nico con la debida autorizaci&oacute; del Servicio de Rentas Internas.');

INSERT INTO Issuer (pk, addres, contactmail, description, socialreason, phonenumbre, ruc, name, accountingleads, emision, environment) VALUES (1,'Av. 10 de Agosto N37-288 y Villalengua','pzurita@integraldata.net.ec','PRESTACION DE SERVICIOS DE TELECOMUNICACION','INTEGRALDATA S.A.','02 2252-803','1791403711001','SERVICIOS DE TRANSMISION INFORMATICA S.A. INTEGRALDATA','SI','NORMAL','PRUEBAS');