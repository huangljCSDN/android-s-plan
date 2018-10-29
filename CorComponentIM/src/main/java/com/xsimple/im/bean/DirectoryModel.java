package com.xsimple.im.bean;

import android.text.TextUtils;


import com.xsimple.im.utils.FilePathUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lh on 17/4/14.
 */
public class DirectoryModel {

    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private boolean isChcek;
    private List<PhotoModel> photos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectoryModel)) return false;

        DirectoryModel directory = (DirectoryModel) o;

        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);

        if (hasId && otherHasId) {
            if (!TextUtils.equals(id, directory.id)) {
                return false;
            }

            return TextUtils.equals(name, directory.name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }

            return name.hashCode();
        }

        int result = id.hashCode();

        if (TextUtils.isEmpty(name)) {
            return result;
        }

        result = 31 * result + name.hashCode();
        return result;
    }

    public boolean isChcek() {
        return isChcek;
    }

    public void setChcek(boolean chcek) {
        isChcek = chcek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public List<PhotoModel> getPhotos() {
        return photos;
    }

    /**
     * 设置照片
     *
     * @param photos
     */
    public void setPhotos(List<PhotoModel> photos) {
        if (photos == null) return;
        for (int i = 0, j = 0, num = photos.size(); i < num; i++) {
            PhotoModel p = photos.get(j);
            if (p == null || !FilePathUtils.fileIsExists(p.getOriginalPath())) {
                photos.remove(j);
            } else {
                j++;
            }
        }
        this.photos = photos;
    }

    /**
     * 获取照片地址
     *
     * @return
     */
    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(photos.size());
        for (PhotoModel photo : photos) {
            paths.add(photo.getOriginalPath());
        }
        return paths;
    }


    /**
     * 添加photo
     *
     * @param photoModel
     */
    public void addPhoto(PhotoModel photoModel) {
        if (FilePathUtils.fileIsExists(photoModel.getOriginalPath())) {
            photos.add(photoModel);
        }
    }

    @Override
    public String toString() {
        return "DirectoryModel{" +
                "id='" + id + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", name='" + name + '\'' +
                ", dateAdded=" + dateAdded +
                ", photos=" + photos +
                '}';
    }
}
