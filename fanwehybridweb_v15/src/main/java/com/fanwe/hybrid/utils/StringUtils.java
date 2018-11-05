package com.fanwe.hybrid.utils;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sendtion on 2016/6/24.
 */
public class StringUtils
{


    /**
     * @param targetStr 要处理的字符串
     * @description 切割字符串，将文本和img标签碎片化，如"ab<img>cd"转换为"ab"、"<img>"、"cd"
     */
    public static List<String> cutStringByImgTag(String targetStr)
    {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find())
        {
            if (matcher.start() > lastIndex)
            {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length())
        {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    /**
     * 从html中按顺序截取出文本和图片 ,注:只能截取
     * @param html
     * @return
     */
    public static List<String> cutHtml(String html)
    {
        List<String> splitTextList = new ArrayList<String>();
        if (isValidHtml(html))
        {
            String[] tmp = html.split("<div class=\"review_unit\">");
            if (tmp != null && tmp.length > 0)
            {
                for (String s : tmp)
                {
                    splitTextList.add(getContent(s));
                }
            }

        }


        return splitTextList;
    }

    private static String getContent(String target)
    {
        if (TextUtils.isEmpty(target))
        {
            return "";
        } else
        {
            int start = 0;
            int end = 0;
            if (target.contains("<div class=\"text_edit\""))
            { //文字

                if (target.contains("<pre>"))
                {
                    start = target.indexOf("<pre>")+5;
                    end = target.indexOf("</pre>");

                } else
                {
                    start = target.indexOf("contenteditable=\"false\">")+24;
                    end = target.indexOf("</div>");
                }


            } else if (target.contains("<img class=\"u_img\""))
            {  //图片
                start = target.indexOf("src=\"")+5;
                end = target.indexOf("\"></div>");
                if (start == -1&&end==-1)
                { //图片url 可能单引号
                    start= target.indexOf("src=\'")+5;
                    end = target.indexOf("\'></div>");
                }
            }

            String ss = "";
            if (start < end)
            {
                ss = String.valueOf(target.subSequence(start, end));
            }
            return ss;

        }
    }

    /**
     * 获取img标签中的src值
     *
     * @param content
     * @return
     */
    public static String getImgSrc(String content)
    {
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img)
        {
            while (result_img)
            {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find())
                {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    /**
     * 关键字高亮显示
     *
     * @param target 需要高亮的关键字
     * @param text   需要显示的文字
     * @return spannable 处理完后的结果，记得不要toString()，否则没有效果
     * SpannableStringBuilder textString = TextUtilTools.highlight(item.getItemName(), KnowledgeActivity.searchKey);
     * vHolder.tv_itemName_search.setText(textString);
     */
    public static SpannableStringBuilder highlight(String text, String target)
    {
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);
        CharacterStyle span = null;

        Pattern p = Pattern.compile(target);
        Matcher m = p.matcher(text);
        while (m.find())
        {
            span = new ForegroundColorSpan(Color.parseColor("#EE5C42"));// 需要重复！
            spannable.setSpan(span, m.start(), m.end(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static boolean isValidHtml(String html)
    {
        if ("false".equals(html) || "null".equals(html) || TextUtils.isEmpty(html))
        {
            return false;
        }else
        {
            return true;
        }
    }

}
