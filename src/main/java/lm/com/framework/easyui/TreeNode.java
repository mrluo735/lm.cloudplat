package lm.com.framework.easyui;

import java.util.List;

/**
 * 树节点
 * 
 * @author Administrator
 *
 */
public class TreeNode {
    private String id;

    private String text;

    private Boolean checked;

    private Boolean disabled;

    private String iconCls;

    private String state;

    private String attributes;

    private List<TreeNode> children;

    private Boolean has;

    public String getId() {
	return null == id ? "" : id.trim();
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getText() {
	return null == text ? "" : text.trim();
    }

    public void setText(String text) {
	this.text = text;
    }

    public Boolean getChecked() {
	return checked;
    }

    public void setChecked(Boolean checked) {
	this.checked = checked;
    }

    public Boolean getDisabled() {
	return disabled;
    }

    public void setDisabled(Boolean disabled) {
	this.disabled = disabled;
    }

    public String getIconCls() {
	return null == iconCls ? "" : iconCls.trim();
    }

    public void setIconCls(String iconCls) {
	this.iconCls = iconCls;
    }

    public String getState() {
	return null == state ? "" : state.trim();
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getAttributes() {
	return null == attributes ? "" : attributes.trim();
    }

    public void setAttributes(String attributes) {
	this.attributes = attributes;
    }

    public List<TreeNode> getChildren() {
	return children;
    }

    public void setChildren(List<TreeNode> children) {
	this.children = children;
    }

    public Boolean getHas() {
	return has;
    }

    public void setHas(Boolean has) {
	this.has = has;
    }
}
