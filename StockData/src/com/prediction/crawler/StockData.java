package com.prediction.crawler;
import java.util.Date;

import com.google.code.morphia.annotations.Id;


public class StockData {
	@Id
	Date time;
	String kaipan;
	String shoupan;
	String zhengfu;
	String zhangfu;
	String zuigao;
	String zuidi;
	String zongshou;
	String zongjin;
	String huanshou;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getKaipan() {
		return kaipan;
	}
	public void setKaipan(String kaipan) {
		this.kaipan = kaipan;
	}
	public String getShoupan() {
		return shoupan;
	}
	public void setShoupan(String shoupan) {
		this.shoupan = shoupan;
	}
	public String getZhengfu() {
		return zhengfu;
	}
	public void setZhengfu(String zhengfu) {
		this.zhengfu = zhengfu;
	}
	public String getZhangfu() {
		return zhangfu;
	}
	public void setZhangfu(String zhangfu) {
		this.zhangfu = zhangfu;
	}
	public String getZuigao() {
		return zuigao;
	}
	public void setZuigao(String zuigao) {
		this.zuigao = zuigao;
	}
	public String getZuidi() {
		return zuidi;
	}
	public void setZuidi(String zuidi) {
		this.zuidi = zuidi;
	}
	public String getZongshou() {
		return zongshou;
	}
	public void setZongshou(String zongshou) {
		this.zongshou = zongshou;
	}
	public String getZongjin() {
		return zongjin;
	}
	public void setZongjin(String zongjin) {
		this.zongjin = zongjin;
	}
	public String getHuanshou() {
		return huanshou;
	}
	public void setHuanshou(String huanshou) {
		this.huanshou = huanshou;
	}
	
	
}
