package com.xsimple.im.engine.protocol;


import com.xsimple.im.db.datatable.IMMessage;
import com.xsimple.im.db.datatable.IMSysMessage;

import java.util.ArrayList;
import java.util.List;

import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_ACTIVE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_ADD;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_AGREE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_APPLY;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_CANCLE_ADMIN;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_DEL;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_FORBIDDEN;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_REFUSE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_REMOVE;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_SET_ADMIN;
import static com.xsimple.im.db.datatable.IMMessage.FIXGROUP_UPDATE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_ADD;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_AGREE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_DEL;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_OWN;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_REFUSE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_REMOVE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_SET_ADMIN;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_UPDATE;
import static com.xsimple.im.db.datatable.IMMessage.GROUP_UPDATE_REMARK;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_READ_GROUP_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_READ_SINGLE_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_WITHDRAWAL_GROUP_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.MESSAGE_WITHDRAWAL_SINGLE_CHAT;
import static com.xsimple.im.db.datatable.IMMessage.SCAN_QRCODE_JOIN_GROUP;

/**
 * Created by liuhao on 2017/4/24.
 */

public class IMCommand {

    private static List<String> groupCmds = new ArrayList<>();
    private static List<String> fixgroupCmds = new ArrayList<>();
    private static List<String> singleCmds = new ArrayList<>();

    static {
        //讨论组命令
        groupCmds.add(GROUP_ADD);
        groupCmds.add(GROUP_AGREE);
        groupCmds.add(GROUP_REFUSE);
        groupCmds.add(GROUP_REMOVE);
        groupCmds.add(GROUP_DEL);
        groupCmds.add(GROUP_OWN);
        groupCmds.add(GROUP_UPDATE);
        groupCmds.add(GROUP_UPDATE_REMARK);
        groupCmds.add(GROUP_SET_ADMIN);
        groupCmds.add(SCAN_QRCODE_JOIN_GROUP);
        //群组命令
        fixgroupCmds.add(FIXGROUP_UPDATE);
        fixgroupCmds.add(FIXGROUP_DEL);
        fixgroupCmds.add(FIXGROUP_FORBIDDEN);
        fixgroupCmds.add(FIXGROUP_ACTIVE);
        fixgroupCmds.add(FIXGROUP_SET_ADMIN);
        fixgroupCmds.add(FIXGROUP_CANCLE_ADMIN);
        fixgroupCmds.add(FIXGROUP_ADD);
        fixgroupCmds.add(FIXGROUP_REMOVE);
        fixgroupCmds.add(FIXGROUP_APPLY);
        fixgroupCmds.add(FIXGROUP_AGREE);
        fixgroupCmds.add(FIXGROUP_REFUSE);
        singleCmds.add(MESSAGE_READ_SINGLE_CHAT);
        singleCmds.add(MESSAGE_WITHDRAWAL_SINGLE_CHAT);
        //群组讨论组公共命令
        groupCmds.add(MESSAGE_READ_GROUP_CHAT);
        groupCmds.add(MESSAGE_WITHDRAWAL_GROUP_CHAT);
        fixgroupCmds.add(MESSAGE_READ_GROUP_CHAT);
        fixgroupCmds.add(MESSAGE_WITHDRAWAL_GROUP_CHAT);
    }



    private String Type;

    private IMSysMessage imSysMessage;

    private List<IMMessage> imMessage;

    private String mActionId;

    public boolean isGroupCmd(){
        return groupCmds.contains(Type);
    }

    public IMCommand() {
    }

    public IMCommand(String Type, List<IMMessage> imMessage) {
        this.Type = Type;
        this.imMessage = imMessage;
    }

    public String getType() {
        return Type;
    }

    public IMCommand setType(String type) {
        this.Type = type;
        return this;
    }

    public List<IMMessage> getImMessage() {
        return imMessage;
    }

    public IMCommand setImMessage(List<IMMessage> imMessage) {
        this.imMessage = imMessage;
        return this;
    }

    public IMSysMessage getImSysMessage() {
        return imSysMessage;
    }

    public void setImSysMessage(IMSysMessage imSysMessage) {
        this.imSysMessage = imSysMessage;
    }

    public String getmActionId() {
        return mActionId;
    }

    public void setmActionId(String mActionId) {
        this.mActionId = mActionId;
    }

    @Override
    public String toString() {
        return "IMCommand{" +
                "Type='" + Type + '\'' +
                ", imSysMessage=" + imSysMessage +
                ", imMessage=" + imMessage +
                ", mActionId='" + mActionId + '\'' +
                '}';
    }
}
