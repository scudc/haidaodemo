package com.justone.android.bean;

public class IndexBean {
	
	public String getIndexId() {
		return indexId;
	}
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getIndexTitle() {
		return indexTitle;
	}
	public void setIndexTitle(String indexTitle) {
		this.indexTitle = indexTitle;
	}
	private String indexId;
	public IndexBean(String indexId, String imageUrl, String indexTitle) {
		super();
		this.indexId = indexId;
		this.imageUrl = imageUrl;
		this.indexTitle = indexTitle;
	}
	public IndexBean() {
		// TODO Auto-generated constructor stub
	}
	private String imageUrl;
	private String indexTitle;

}
