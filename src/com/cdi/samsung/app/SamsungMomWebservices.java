package com.cdi.samsung.app;

import com.niucodeninja.Params;
import com.niucodeninja.webservices.RestClient.RequestMethod;
import com.niucodeninja.webservices.WebServices;

public class SamsungMomWebservices extends WebServices {

	public static final int VERIFY_IMEI_COMPLETE = 0x01;
	public static final int VERIFY_IMEI_ERROR = 0x02;
	public static final int REGISTER_COMPLETE = 0x3;
	public static final int REGISTER_ERROR = 0x4;
	public static final int MOMS_COMPLETE = 0x5;
	public static final int MOMS_ERROR = 0x6;
	public static final int RANKING_COMPLETE = 0x7;
	public static final int RANKING_ERROR = 0x8;

	public SamsungMomWebservices(String base) {
		super(base);
	}

	public void verifyIMEI(String imei, String password) {
		Params params = new Params();
		params.AddParam("imei", imei);
		params.AddParam("clave", password);
		call("?a=imei", params, RequestMethod.POST, VERIFY_IMEI_COMPLETE,
				VERIFY_IMEI_ERROR, true);
	}

	public void register(String name, String email, String phone, String imei,
			String password, String country, boolean information) {
		Params params = new Params();
		params.AddParam("nombre", name);
		params.AddParam("email", email);
		params.AddParam("celular", phone);
		params.AddParam("imei", imei);
		params.AddParam("id_pais", country);
		params.AddParam("clave", password);
		params.AddParam("recibir_info", information == true ? "1" : "0");
		call("?a=registrar", params, RequestMethod.POST, REGISTER_COMPLETE,
				REGISTER_ERROR, true);
	}

	public void getMoms(String imei, String password, String country) {
		Params params = new Params();
		params.AddParam("imei", imei);
		params.AddParam("id_pais", country);
		params.AddParam("clave", password);
		call("?a=madres", params, RequestMethod.POST, MOMS_COMPLETE,
				MOMS_ERROR, true);
	}

	public void getRanking(String imei, String password, String country) {
		Params params = new Params();
		params.AddParam("imei", imei);
		params.AddParam("id_pais", country);
		params.AddParam("clave", password);
		call("?a=ranking", params, RequestMethod.POST, RANKING_COMPLETE,
				RANKING_ERROR, true);
	}
}
