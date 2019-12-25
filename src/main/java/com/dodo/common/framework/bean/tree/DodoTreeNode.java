package com.dodo.common.framework.bean.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoTreeNode implements Serializable {
    private static final long  serialVersionUID = 7134383827074530069L;
    private String             pId;
    private String             id;
    private String             name;
    private String             nameKey;
    private String             url;
    private String             target;
    private int                sortSeq;
    private String             extData          = "";

    @JsonIgnore
    private Object             data;
    @JsonIgnore
    private DodoTreeNode       parentNode;
    @JsonIgnore
    private List<DodoTreeNode> childList;
    @JsonIgnore
    private int                businessLevel;
    @JsonIgnore
    private int                maxBusinessLevel;

    public DodoTreeNode() {
        this.childList = new ArrayList<DodoTreeNode>();
    }

    public DodoTreeNode(String pId, String id, String name, String nameKey, Object data) {
        this();
        this.pId = pId;
        this.id = id;
        this.name = name;
        this.data = data;
        this.nameKey = nameKey;
    }

    @JsonIgnore
    public boolean isLeaf() {
        if (this.childList == null || this.childList.isEmpty()) {
            return true;
        }
        return false;
    }

    @JsonIgnore
    public boolean isRoot() {
        return getParentNode() == null;
    }

    public void addChildNode(DodoTreeNode treeNode) {
        this.childList.add(treeNode);
        treeNode.setParentNode(this);
    }

    @JsonIgnore
    public List<DodoTreeNode> getParentNodes() {
        List<DodoTreeNode> elderList = new ArrayList<DodoTreeNode>();
        DodoTreeNode parentNode = this.getParentNode();
        if (parentNode == null) {
            return elderList;
        } else {
            elderList.add(parentNode);
            elderList.addAll(parentNode.getParentNodes());
            return elderList;
        }
    }

    @JsonIgnore
    public List<DodoTreeNode> getChildrenNodes() {
        List<DodoTreeNode> childrenNodesList = new ArrayList<DodoTreeNode>();
        if (this.childList == null) {
            return childrenNodesList;
        } else {
            for (DodoTreeNode childNode : this.childList) {
                childrenNodesList.add(childNode);
                childrenNodesList.addAll(childNode.getChildrenNodes());
            }
            return childrenNodesList;
        }
    }

    public void deleteNode() {
        DodoTreeNode parentNode = this.getParentNode();
        if (parentNode != null) {
            parentNode.deleteChildNode(this.getId());
        }
    }

    public void deleteChildNode(String childId) {
        for (int i = 0, childNumber = this.childList.size(); i < childNumber; i++) {
            if (this.childList.get(i).getId().equals(childId)) {
                this.childList.remove(i);
                return;
            }
        }
    }

    public boolean insertTreeNode(DodoTreeNode treeNode) {
        if (this.getId().equals(treeNode.getpId())) {
            addChildNode(treeNode);
            return true;
        } else {
            for (DodoTreeNode childNode : this.childList) {
                if (childNode.insertTreeNode(treeNode) == true) {
                    return true;
                }
            }
            return false;
        }
    }

    public DodoTreeNode findDodoTreeNodeById(String id) {
        if (this.id.equals(id)) {
            return this;
        }
        if (childList.isEmpty()) {
            return null;
        } else {
            for (DodoTreeNode child : this.childList) {
                DodoTreeNode resultNode = child.findDodoTreeNodeById(id);
                if (resultNode != null) {
                    return resultNode;
                }
            }
            return null;
        }
    }

    public void setChildList(List<DodoTreeNode> childList) {
        for (DodoTreeNode node : childList) {
            this.addChildNode(node);
        }
    }

    public DodoTreeNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(DodoTreeNode parentNode) {
        if (this.getParentNode() != null) {
            this.getParentNode().deleteChildNode(this.getId());
        }
        this.parentNode = parentNode;
    }

    public String getpId() {
        return pId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public int getBusinessLevel() {
        return businessLevel;
    }

    public void setBusinessLevel(int businessLevel) {
        this.businessLevel = businessLevel;
        this.maxBusinessLevel = businessLevel;
    }

    public List<DodoTreeNode> getChildList() {
        return childList;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMaxBusinessLevel() {
        return maxBusinessLevel;
    }

    public void setMaxBusinessLevel(int maxBusinessLevel) {
        this.maxBusinessLevel = maxBusinessLevel;
    }

    public String getUrl() {
        return url;
    }

    public String getTarget() {
        return target;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getSortSeq() {
        return sortSeq;
    }

    public void setSortSeq(int sortSeq) {
        this.sortSeq = sortSeq;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DodoTreeNode other = (DodoTreeNode) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "DodoTreeNode [pId=" + pId + ", id=" + id + ", name=" + name + ", nameKey=" + nameKey + ", url=" + url
                + ", target=" + target + ", sortSeq=" + sortSeq + ", extData=" + extData + ", businessLevel="
                + businessLevel + ", maxBusinessLevel=" + maxBusinessLevel + "]";
    }
}
