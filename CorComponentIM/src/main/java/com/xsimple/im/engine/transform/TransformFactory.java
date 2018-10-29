package com.xsimple.im.engine.transform;

import com.networkengine.engine.LogicEngine;
import com.networkengine.entity.GroupEntity;
import com.networkengine.entity.ResultGroupDetail;
import com.xsimple.im.db.datatable.IMGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Description：数据实体转换并映射到本地数据库
 */
public class TransformFactory {

    /**
     * 群组&讨论组实体转换, 用于数据库存储映射
     */
    public static IMGroup transformGroup(GroupEntity group, int type) {
        if (group == null) {
            return null;
        }
        IMGroup g = new IMGroup();
        g.setId(group.getId());
        g.setCurrUserId(LogicEngine.getInstance().getUser().getId());
        g.setName(group.getName());
        g.setType(type);
        g.setUpdate_time(group.getUpdate_time());
        g.setCreate_time(group.getCreate_time());
        g.setRemark(group.getRemark());
        g.setImportantFlag(group.getImportantFlag());
        return g;

    }

    public static List<IMGroup> transformGroups(List<GroupEntity> groups, int type) {
        List<IMGroup> groupList = new ArrayList<>();
        if (groups == null || groups.isEmpty()) {
            return groupList;
        }
        for (GroupEntity g : groups) {
            if (g == null)
                continue;
            IMGroup imGroup = transformGroup(g, type);
            if (imGroup == null)
                continue;
            groupList.add(imGroup);
        }
        return groupList;

    }

    public static IMGroup transformGroupsByDetail(ResultGroupDetail group, int type) {
        if (group == null) {
            return null;
        }
        IMGroup g = new IMGroup();
        g.setId(String.valueOf(group.getId()));
        g.setName(group.getName());
        g.setType(type);
        g.setCurrUserId(LogicEngine.getInstance().getUser().getId());
        // g.setUpdate_time(group.getUpdate_time());
        // g.setCreate_time(group.getCreate_time());
        //  g.setRemark(group.getRemark());
        g.setImportantFlag(group.getImportantFlag());
        return g;

    }


    public static List<IMGroup> transformGroupsByDetail(List<ResultGroupDetail> groups, int type) {
        List<IMGroup> groupList = new ArrayList<>();
        if (groups == null || groups.isEmpty()) {
            return groupList;
        }
        for (ResultGroupDetail g : groups) {
            if (g == null)
                continue;
            IMGroup imGroup = transformGroupsByDetail(g, type);
            if (imGroup == null)
                continue;
            groupList.add(imGroup);
        }
        return groupList;

    }

}
