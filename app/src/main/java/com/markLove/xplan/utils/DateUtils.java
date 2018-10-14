package com.markLove.xplan.utils;

/**
 * Created by Administrator on 2017/5/28.
 */

public class DateUtils {
    /**
     * 爱地图分类的数据
     */
    /*public static List<LoveMapSelectBean> getLoveMapSelectData() {
        List<LoveMapSelectBean> lists = new ArrayList<LoveMapSelectBean>();

        LoveMapSelectBean lmsb1 = new LoveMapSelectBean("美食", R.mipmap.love_map_select_1);
        LoveMapSelectBean lmsb2 = new LoveMapSelectBean("电影院", R.mipmap.love_map_select_2);
        LoveMapSelectBean lmsb3 = new LoveMapSelectBean("休闲", R.mipmap.love_map_select_3);
        LoveMapSelectBean lmsb4 = new LoveMapSelectBean("美妆", R.mipmap.love_map_select_4);
        LoveMapSelectBean lmsb5 = new LoveMapSelectBean("其他", R.mipmap.love_map_select_more);
        lists.add(lmsb1);
        lists.add(lmsb2);
        lists.add(lmsb3);
        lists.add(lmsb4);
        lists.add(lmsb5);
        return lists;
    }*/

//    /**
//     * 分享的选项到好友支付
//     */
//    public static List<ShareDialogBean> getSharePayBeanData() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//
//        ShareDialogBean sb2 = new ShareDialogBean(R.drawable.wx, "微信");
//        lists.add(sb2);
//        return lists;
//    }
//    /**
//     * 分享的选项
//     */
//    public static List<ShareDialogBean> getShareBeanData() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//
//        ShareDialogBean sb0 = new ShareDialogBean(R.drawable.qq_big, "QQ");
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.qq_friend_big, "朋友圈");
//        ShareDialogBean sb2 = new ShareDialogBean(R.drawable.wx, "微信");
//        lists.add(sb0);
//        lists.add(sb1);
//        lists.add(sb2);
//        return lists;
//    }

//    /**
//     * 分享的选项
//     */
//    public static List<ShareDialogBean> getMoreData() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//        ShareDialogBean sb0 = new ShareDialogBean(R.drawable.block_big, "拉黑");
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.report_big, "举报");
//        lists.add(sb0);
//        lists.add(sb1);
//        return lists;
//    }/**
//     * 分享的选项
//     */
//    public static List<ShareDialogBean> getMoreNoDackData() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.report_big, "举报");
//        lists.add(sb1);
//        return lists;
//    }
//    /**
//     * 恩爱圈的更多
//     */
//    public static List<ShareDialogBean> getMoreForCircleLoveData(boolean isSupporting) {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//        ShareDialogBean sb0 = new ShareDialogBean(R.drawable.block_big, "拉黑");
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.report_big, "举报");
//        lists.add(sb0);
//        lists.add(sb1);
//        if (isSupporting){
//            ShareDialogBean sb2 = new ShareDialogBean(R.drawable.go_out_circlr_love_big, "退出后援团");
//            lists.add(sb2);
//        }
//        return lists;
//    }
//    /**
//     * 分享的选项
//     */
//    public static List<ShareDialogBean> getMoreDataContainLoves() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//
//        ShareDialogBean sb0 = new ShareDialogBean(R.drawable.block_big, "拉黑");
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.report_big, "举报");
//        ShareDialogBean sb2 = new ShareDialogBean(R.drawable.loves_diss, "解除情侣关系");
//        lists.add(sb0);
//        lists.add(sb1);
//        lists.add(sb2);
//        return lists;
//    }
//    /**
//     * 分享的选项(有申请成为情侣)
//     */
//    public static List<ShareDialogBean> getMoreDataBecomeLoves() {
//        List<ShareDialogBean> lists = new ArrayList<ShareDialogBean>();
//
//        ShareDialogBean sb0 = new ShareDialogBean(R.drawable.block_big, "拉黑");
//        ShareDialogBean sb1 = new ShareDialogBean(R.drawable.report_big, "举报");
//        ShareDialogBean sb2 = new ShareDialogBean(R.drawable.loves_become, "申请成为情侣");
//        lists.add(sb0);
//        lists.add(sb1);
//        lists.add(sb2);
//        return lists;
//    }
//
//    /**
//     * 广场页面的tab
//     */
//    public static ArrayList<CustomTabEntity> getSquareTabData() {
//        ArrayList<CustomTabEntity> lists = new ArrayList<CustomTabEntity>();
//
//        SquareTabBean stb1 = new SquareTabBean("单身", 0, 0);
//        SquareTabBean stb2 = new SquareTabBean("热恋", 0, 0);
//        SquareTabBean stb3 = new SquareTabBean("婚姻", 0, 0);
//        SquareTabBean stb4 = new SquareTabBean("恩爱圈", 0, 0);
//
//        lists.add(stb1);
//        lists.add(stb2);
//        lists.add(stb3);
//        lists.add(stb4);
//        return lists;
//    }
//    /**
//     * 新商城的type界面的tab
//     */
//    public static ArrayList<CustomTabEntity> getStoreTypeTabData() {
//        ArrayList<CustomTabEntity> lists = new ArrayList<CustomTabEntity>();
//
//        SquareTabBean stb1 = new SquareTabBean("恋爱攻略", 0, 0);
//        SquareTabBean stb2 = new SquareTabBean("爱情萌物", 0, 0);
//        SquareTabBean stb3 = new SquareTabBean("浪漫礼盒", 0, 0);
//
//        lists.add(stb1);
//        lists.add(stb2);
//        lists.add(stb3);
//        return lists;
//    }
//    public static ArrayList<CustomTabEntity> getFriendsListTab() {
//        ArrayList<CustomTabEntity> lists = new ArrayList<CustomTabEntity>();
//        SquareTabBean stb1 = new SquareTabBean("相互关注的", 0, 0);
//        SquareTabBean stb2 = new SquareTabBean("我关注的", 0, 0);
//        SquareTabBean stb3 = new SquareTabBean("关注我的", 0, 0);
//        lists.add(stb1);
//        lists.add(stb2);
//        lists.add(stb3);
//        return lists;
//    }
//    /**获取所有的草稿*/
//    public static List<MyDraftBean> getAllDraft(Context context) {
//        List<MyDraftBean> lists=new ArrayList<MyDraftBean>();
//        String str = PreferencesUtils.getString(context, Constants.DRAFT_KEY, "");
//        if (!TextUtils.isEmpty(str)){
//            lists=new Gson().fromJson(str, new TypeToken<List<MyDraftBean>>(){}.getType());
//        }
//        return lists;
//    }


}
