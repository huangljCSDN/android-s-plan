package com.markLove.Xplan.module.emoji;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.markLove.Xplan.R;

/**
 * Created by luoyunmin on 2017/8/8.
 */

public class EmojiUtils {

    public static SpannableString parseEmoji(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        Pattern pattern = Pattern.compile("\\[([\\u4e00-\\u9fa5]|[A-Z])+?\\]"); //用正则匹配出所有的[xxx]类型字段
        Matcher matcher = pattern.matcher(str);
        Integer drawableSrc = null;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            int position = -1;
            String group = matcher.group();
            for (int i = 0; i < emojiValues.length; i++) {
                if (emojiValues[i].equals(group)) {
                    position = i;
                    break;
                }
            }
            if (position >= 0 && position < emojiIcons.length)
                drawableSrc = emojiIcons[position];
            if (drawableSrc != null && drawableSrc > 0) {
                spannableString.setSpan(new ImageSpan(context, drawableSrc), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return spannableString;
    }

    public static final Integer[] emojiIcons = new Integer[]{
            R.mipmap.emoji_1,
            R.mipmap.emoji_2,
            R.mipmap.emoji_3,
            R.mipmap.emoji_4,
            R.mipmap.emoji_5,
            R.mipmap.emoji_6,
            R.mipmap.emoji_7,
            R.mipmap.emoji_8,
            R.mipmap.emoji_9,
            R.mipmap.emoji_10,
            R.mipmap.emoji_11,
            R.mipmap.emoji_12,
            R.mipmap.emoji_13,
            R.mipmap.emoji_14,
            R.mipmap.emoji_15,
            R.mipmap.emoji_16,
            R.mipmap.emoji_17,
            R.mipmap.emoji_19,
            R.mipmap.emoji_20,
            R.mipmap.emoji_21,
            R.mipmap.emoji_22,
            R.mipmap.emoji_23,
            R.mipmap.emoji_24,
            R.mipmap.emoji_25,
            R.mipmap.emoji_26,
            R.mipmap.emoji_27,
            R.mipmap.emoji_28,
            R.mipmap.emoji_29,
            R.mipmap.emoji_30,
            R.mipmap.emoji_31,
            R.mipmap.emoji_32,
            R.mipmap.emoji_33,
            R.mipmap.emoji_34,
            R.mipmap.emoji_35,
            R.mipmap.emoji_37,
            R.mipmap.emoji_38,
            R.mipmap.emoji_39,
            R.mipmap.emoji_40,
            R.mipmap.emoji_41,
            R.mipmap.emoji_42,
            R.mipmap.emoji_43,
            R.mipmap.emoji_45,
            R.mipmap.emoji_46,
            R.mipmap.emoji_47,
            R.mipmap.emoji_48,
            R.mipmap.emoji_49,
            R.mipmap.emoji_50,
            R.mipmap.emoji_51,
            R.mipmap.emoji_52,
            R.mipmap.emoji_53,
            R.mipmap.emoji_54,
            R.mipmap.emoji_55,
            R.mipmap.emoji_56,
            R.mipmap.emoji_57,
            R.mipmap.emoji_58,
            R.mipmap.emoji_59,
            R.mipmap.emoji_60,
            R.mipmap.emoji_61,
            R.mipmap.emoji_62,
            R.mipmap.emoji_63,
            R.mipmap.emoji_64,
            R.mipmap.emoji_65,
            R.mipmap.emoji_66,
            R.mipmap.emoji_67,
            R.mipmap.emoji_68,
            R.mipmap.emoji_69,
            R.mipmap.emoji_70,
            R.mipmap.emoji_71,
            R.mipmap.emoji_72,
            R.mipmap.emoji_73,
            R.mipmap.emoji_74,
            R.mipmap.emoji_75,
            R.mipmap.emoji_76,
            R.mipmap.emoji_77,
            R.mipmap.emoji_78,
            R.mipmap.emoji_79,
            R.mipmap.emoji_80,
            R.mipmap.emoji_81,
            R.mipmap.emoji_82,
            R.mipmap.emoji_83,
            R.mipmap.emoji_84,
            R.mipmap.emoji_85,
            R.mipmap.emoji_86,
            R.mipmap.emoji_87,
            R.mipmap.emoji_88,
            R.mipmap.emoji_89,
            R.mipmap.emoji_90,
            R.mipmap.emoji_91,
            R.mipmap.emoji_92,
            R.mipmap.emoji_93,
            R.mipmap.emoji_94,
            R.mipmap.emoji_95,
            R.mipmap.emoji_98,
            R.mipmap.emoji_101,
    };

    public static final String[] emojiValues = new String[]{
            "[微笑]",
            "[撇嘴]",
            "[色]",
            "[发呆]",
            "[得意]",
            "[流泪]",
            "[害羞]",
            "[闭嘴]",
            "[困]",
            "[大哭]",
            "[尴尬]",
            "[发怒]",
            "[调皮]",
            "[龇牙]",
            "[惊讶]",
            "[难过]",
            "[酷]",
            "[抓狂]",
            "[吐]",
            "[偷笑]",
            "[愉快]",
            "[白眼]",
            "[傲慢]",
            "[想吃]",
            "[黑眼圈]",
            "[惊恐]",
            "[流汗]",
            "[大笑]",
            "[悠闲]",
            "[奋斗]",
            "[咒骂]",
            "[疑问]",
            "[嘘]",
            "[晕]",
            "[衰]",
            "[骷髅]",
            "[敲打]",
            "[再见]",
            "[擦汗]",
            "[抠鼻]",
            "[鼓掌]",
            "[坏笑]",
            "[左哼哼]",
            "[右哼哼]",
            "[哈欠]",
            "[鄙视]",
            "[委屈]",
            "[悲伤]",
            "[奸笑]",
            "[亲亲]",
            "[瞪眼]",
            "[可怜]",
            "[菜刀]",
            "[西瓜]",
            "[啤酒]",
            "[篮球]",
            "[乒乓球]",
            "[咖啡]",
            "[饭]",
            "[猪头]",
            "[玫瑰]",
            "[凋谢]",
            "[嘴唇]",
            "[爱心]",
            "[心碎]",
            "[蛋糕]",
            "[闪电]",
            "[炸弹]",
            "[小刀]",
            "[足球]",
            "[瓢虫]",
            "[大便]",
            "[月亮]",
            "[太阳]",
            "[礼物]",
            "[拥抱]",
            "[强]",
            "[弱]",
            "[握手]",
            "[胜利]",
            "[抱拳]",
            "[勾引]",
            "[拳头]",
            "[小拇指]",
            "[喜欢你]",
            "[不对]",
            "[OK]",
            "[爱情]",
            "[喜欢你]",
            "[欢乐]",
            "[发抖]",
            "[大喊]",
            "[回头]",
            "[惊慌]",
    };

    /**
     * 判断给定的字符串是否能解析成表情
     *
     * @param str
     * @return
     */
    public static boolean containStr(String str) {
        for (int i = 0; i < emojiValues.length; i++) {
            if (str.equals(emojiValues[i])) {
                return true;
            }
        }
        return false;
    }
}
