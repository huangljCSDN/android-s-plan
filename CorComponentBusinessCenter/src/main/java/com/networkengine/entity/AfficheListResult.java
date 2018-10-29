package com.networkengine.entity;

import java.util.List;

/**
 * Created by liuhao on 2018/4/16.
 */

public class AfficheListResult {


    private String code;
    private String message;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {


        private int groupId;
        private List<AfficheListBean> afficheList;

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public List<AfficheListBean> getAfficheList() {
            return afficheList;
        }

        public void setAfficheList(List<AfficheListBean> afficheList) {
            this.afficheList = afficheList;
        }

        public static class AfficheListBean {

            private String title;
            private String createName;
            private String createDatetime;
            private String content;
            private String id;
            private String useId;

            public void setId(String id) {
                this.id = id;
            }

            public void setUseId(String useId) {
                this.useId = useId;
            }

            public String getId() {
                return id;
            }

            public String getUseId() {
                return useId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getCreateName() {
                return createName;
            }

            public void setCreateName(String createName) {
                this.createName = createName;
            }

            public String getCreateDatetime() {
                return createDatetime;
            }

            public void setCreateDatetime(String createDatetime) {
                this.createDatetime = createDatetime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
