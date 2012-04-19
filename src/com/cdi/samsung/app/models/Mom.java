package com.cdi.samsung.app.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.cdi.samsung.app.Manager;

public class Mom {

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	private String id;
	private String name, typicalSentence, smartMomTip, whyBeASmartMom;
	private String pic1, pic2, pic3;
	private int votes;
	private boolean is_ok = false;

	public Mom(JSONObject object) {
		try {
			this.setId(object.getString("id"));
			this.setName(object.getString("nombre_mama"));
			this.setTypicalSentence(object.getString("frase_tipica"));
			this.setSmartMomTip(object.getString("smart_favorito"));
			this.setWhyBeASmartMom(object.getString("frase_porque"));
			if (!object.isNull("votos")) {
				this.setVotes(Integer.parseInt(object.getString("votos")));
			}
			is_ok = true;
		} catch (JSONException e) {
			is_ok = false;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.pic1 = Manager.PHOTO_PATH + id + "_thumb1" + Manager.PHOTO_EXT;
		this.pic2 = Manager.PHOTO_PATH + id + "_thumb2" + Manager.PHOTO_EXT;
		this.pic3 = Manager.PHOTO_PATH + id + "_thumb3" + Manager.PHOTO_EXT;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTypicalSentence() {
		return typicalSentence;
	}

	public void setTypicalSentence(String typicalSentence) {
		this.typicalSentence = typicalSentence;
	}

	public String getSmartMomTip() {
		return smartMomTip;
	}

	public void setSmartMomTip(String smartMomTip) {
		this.smartMomTip = smartMomTip;
	}

	public String getWhyBeASmartMom() {
		return whyBeASmartMom;
	}

	public void setWhyBeASmartMom(String whyBeASmartMom) {
		this.whyBeASmartMom = whyBeASmartMom;
	}

	public String getPic1() {
		return pic1;
	}

	public String getPic2() {
		return pic2;
	}

	public String getPic3() {
		return pic3;
	}

	public boolean isOk() {
		return is_ok;
	}
}
