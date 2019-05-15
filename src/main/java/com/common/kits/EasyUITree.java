package com.common.kits;

import java.util.ArrayList;
import java.util.List;

public class EasyUITree {

	public static String STATE_OPEN = "open"; 
    public static String STATE_CLOSED = "closed"; 
    private Integer id; 
    private String text; 
    private boolean checked; 
    private String state = STATE_OPEN; 
    private String attributes; 
    private List<EasyUITree> children; 
    
    public EasyUITree() { 
        this(null, null, STATE_OPEN); 
    } 

    public EasyUITree(Integer id, String text) { 
        this(id, text, STATE_OPEN); 
    } 

    public EasyUITree(Integer id, String text, String state) { 
        this.id = id; 
        this.text = text; 
        this.state = state; 
        this.children = new ArrayList<EasyUITree>(); 
    }

	public static String getSTATE_OPEN() {
		return STATE_OPEN;
	}

	public static void setSTATE_OPEN(String sTATE_OPEN) {
		STATE_OPEN = sTATE_OPEN;
	}

	public static String getSTATE_CLOSED() {
		return STATE_CLOSED;
	}

	public static void setSTATE_CLOSED(String sTATE_CLOSED) {
		STATE_CLOSED = sTATE_CLOSED;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public List<EasyUITree> getChildren() {
		return children;
	}

	public void setChildren(List<EasyUITree> children) {
		this.children = children;
	}
    
    
	
}
