package com.dodo.common.framework.bean.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dodo.utils.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * <p>
 * Dodo Framework. <a href="https://www.bydodo.com">https://www.bydodo.com</a>
 * 
 * @author uncle.code@bydodo.com
 * @author mingming@bydodo.com
 * @author dodo@bydodo.com
 * @version v 1.0
 */
public class DodoTree implements Serializable {
    private static final long         serialVersionUID = 8253967982964142057L;
    private List<DodoTreeNode>        rootNodes;
    private Set<DodoTreeNode>         tempNodes;
    private Map<String, DodoTreeNode> wholeTreeMap;
    private String                    rootPid;
    private String                    selectNodes;
    private Integer                   maxBusinessLevel;
    /**
     * 当主键类型为 long 或者 int的时候，需要在树的每层补一定数量的0，确保前台展示正确<br/>
     * <br/>
     * 这个属性表示 叶子节点的补0 字符串
     */
    private String                    lastAppend       = "";

    public DodoTree() {
        rootNodes = new ArrayList<DodoTreeNode>();
        tempNodes = new HashSet<DodoTreeNode>();
        wholeTreeMap = new HashMap<String, DodoTreeNode>();
    }

    public DodoTree(List<DodoTreeNode> treeNodeList, String rootPid) {
        this();
        DodoTreeNode tempNode = null;
        this.rootPid = rootPid;
        this.wholeTreeMap = new HashMap<String, DodoTreeNode>(treeNodeList.size());
        for (DodoTreeNode treeNode : treeNodeList) {
            wholeTreeMap.put(treeNode.getId(), treeNode);
            if ((this.rootPid == null && treeNode.getpId() == null)
                    || (this.rootPid != null && this.rootPid.equals(treeNode.getpId()))) {
                this.rootNodes.add(treeNode);
            }
        }
        String parentKeyId = null;
        for (DodoTreeNode treeNode : treeNodeList) {
            parentKeyId = treeNode.getpId();
            if (parentKeyId != null && wholeTreeMap.containsKey(parentKeyId)) {
                wholeTreeMap.get(parentKeyId).addChildNode(treeNode);
                tempNode = treeNode.getParentNode();
                while (true) {
                    if (tempNode == null || tempNode.getMaxBusinessLevel() >= treeNode.getMaxBusinessLevel()) {
                        break;
                    }
                    tempNode.setMaxBusinessLevel(treeNode.getMaxBusinessLevel());
                    tempNode = tempNode.getParentNode();
                }
            }
        }
    }

    public void addRootNode(DodoTreeNode rootNode) {
        rootNode.setParentNode(null);
        this.rootNodes.add(rootNode);
        this.rootPid = rootNode.getpId();
        wholeTreeMap.put(rootNode.getId(), rootNode);
    }

    public DodoTreeNode getDodoTreeNodeById(String id) {
        DodoTreeNode returnNode = null;
        for (DodoTreeNode rootNode : rootNodes) {
            if ((returnNode = rootNode.findDodoTreeNodeById(id)) != null) {
                break;
            }
        }
        return returnNode;
    }

    public boolean insertDodoTreeNode(DodoTreeNode treeNode) {
        DodoTreeNode tempNode = null;
        if ((tempNode = this.wholeTreeMap.get(treeNode.getpId())) != null) {
            if (tempNode.insertTreeNode(treeNode)) {
                this.wholeTreeMap.put(treeNode.getId(), treeNode);
                tempNode = treeNode.getParentNode();
                while (true) {
                    if (tempNode == null || tempNode.getMaxBusinessLevel() >= treeNode.getMaxBusinessLevel()) {
                        break;
                    }
                    tempNode.setMaxBusinessLevel(treeNode.getMaxBusinessLevel());
                    tempNode = tempNode.getParentNode();
                }

                return true;
            }
        } else {
            if (!tempNodes.contains(treeNode)) {
                tempNodes.add(treeNode);
            }
        }
        return false;
    }

    public List<DodoTreeNode> getRootNodes() {
        return rootNodes;
    }

    public String getWholeTreeJson() {
        List<DodoTreeNode> wholeTree = new ArrayList<DodoTreeNode>(this.wholeTreeMap.size());
        wholeTree.addAll(this.wholeTreeMap.values());
        Collections.sort(wholeTree, new DodoTreeNodeSortter());
        if (this.maxBusinessLevel == null) {
            try {
                return JacksonUtil.toJackson(wholeTree);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }
        List<DodoTreeNode> nodes = new ArrayList<DodoTreeNode>(wholeTree.size());
        for (DodoTreeNode node : wholeTree) {
            if (node.getMaxBusinessLevel() == this.maxBusinessLevel) {
                nodes.add(node);
            }
        }
        try {
            return JacksonUtil.toJackson(nodes);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Collection<DodoTreeNode> getValidTreeNode() {
        List<DodoTreeNode> wholeTree = new ArrayList<DodoTreeNode>(this.wholeTreeMap.size());
        wholeTree.addAll(this.wholeTreeMap.values());
        Collections.sort(wholeTree, new DodoTreeNodeSortter());
        if (this.maxBusinessLevel == null) {
            return wholeTree;
        }
        List<DodoTreeNode> nodes = new ArrayList<DodoTreeNode>(wholeTree.size());
        for (DodoTreeNode node : wholeTree) {
            if (node.getMaxBusinessLevel() == this.maxBusinessLevel) {
                nodes.add(node);
            }
        }
        return nodes;
    }

    public void addTempNodes() {
        boolean flag = false;
        Iterator<DodoTreeNode> it = tempNodes.iterator();
        DodoTreeNode dodoTreeNode = null;
        while (it.hasNext()) {
            dodoTreeNode = (DodoTreeNode) it.next();
            if (insertDodoTreeNode(dodoTreeNode)) {
                flag = true;
                it.remove();
            }
        }
        if (flag) {
            addTempNodes();
        }
        for (DodoTreeNode node : tempNodes) {
            node.setpId(String.valueOf(node.getBusinessLevel() * -1));
            addRootNode(node);
        }
    }

    public String getSelectNodes() {
        return selectNodes == null ? "[]" : selectNodes;
    }

    public void setSelectNodes(String selectNodes) {
        this.selectNodes = selectNodes;
    }

    public int getMaxBusinessLevel() {
        return maxBusinessLevel;
    }

    public String getLastAppend() {
        return lastAppend;
    }

    public void setMaxBusinessLevel(int maxBusinessLevel) {
        this.maxBusinessLevel = maxBusinessLevel;
    }

    public void setLastAppend(String lastAppend) {
        this.lastAppend = lastAppend;
    }

    public void sortTree() {
        if (rootNodes == null) {
            return;
        }
        for (DodoTreeNode treeNode : rootNodes) {
            _sortTree(treeNode);
        }
    }

    private void _sortTree(DodoTreeNode treeNode) {
        Collections.sort(treeNode.getChildList(), new DodoTreeNodeSortter());
        for (DodoTreeNode node : treeNode.getChildList()) {
            _sortTree(node);
        }
    }
}
