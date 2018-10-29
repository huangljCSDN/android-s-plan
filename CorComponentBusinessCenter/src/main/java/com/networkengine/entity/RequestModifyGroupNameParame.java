package com.networkengine.entity;

/**
 * Created by liuhao on 2018/5/29.
 */

public class RequestModifyGroupNameParame {
    /**
     * 群组id
     */
    private String id;
    /**
     * 群名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
