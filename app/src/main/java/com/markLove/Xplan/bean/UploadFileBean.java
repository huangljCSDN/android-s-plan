package com.markLove.Xplan.bean;

import java.util.ArrayList;

/**
 * 作者：created by huanglingjun on 2018/10/25
 * 描述：
 */
public class UploadFileBean {

    private ArrayList<FileBean> list;

    public class FileBean {
        String fullPath;
        String path;
        String group;

        public String getFullPath() {
            return fullPath;
        }

        public void setFullPath(String fullPath) {
            this.fullPath = fullPath;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return "UploadFileBean{" +
                    "fullPath='" + fullPath + '\'' +
                    ", path='" + path + '\'' +
                    ", group='" + group + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UploadFileBean{" +
                "list=" + list +
                '}';
    }
}
