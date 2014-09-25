package com.femtioprocent.fpd2.b.util.text;

import java.util.ArrayList;

import org.apache.commons.lang.StringEscapeUtils;

public class HTML {

    String[] xmlEntity = new String[] {
            "&amp;",
            "&quot;",
            "&apos;",
            "&lt;",
            "&gt;",
    };
    
    public String isXMLEntity(String s) {
        String lower = s.toLowerCase();
        for(String xe : xmlEntity) {
            if ( lower.startsWith(xe) )
                return xe;
        }
        return null;
    }
    
    public String cleanUp(String html_) {
        String html = replaceEntitiesExceptXMLEntities(html_);
        ArrayList<String> sArr = splitSpecial(html);
        StringBuilder sb = new StringBuilder();
        for(String s : sArr) {
            if ( isXMLEntity(s) != null )
                sb.append(s);
            else {
                sb.append(StringEscapeUtils.unescapeHtml(s));
            }
        }
        return sb.toString();
    }


    private String replaceEntitiesExceptXMLEntities(String html) {
        String h = html;
        h = h.replace("<strong>", "<b>");
        h = h.replace("</strong>", "</b>");
        h = h.replace("<em>", "<i>");
        h = h.replace("</em>", "</i>");
        return h;
    }

    private ArrayList<String> splitSpecial(String html) {
        ArrayList<String> sList = new ArrayList<String>();
        return splitSpecial(html, sList);
    }

    private ArrayList<String> splitSpecial(String html, ArrayList<String> sList) {
        int ix = html.indexOf('&');
        if ( ix == -1 ) {
            sList.add(html);
            return sList;
        }
        String s1 = html.substring(0, ix);
        String s2 = html.substring(ix);
        sList.add(s1);
        if ( s2.length() == 0 )
            return sList;
        String xmlE = isXMLEntity(s2);
        if ( xmlE == null ) {
            // starts with &
            int ix2 = s2.indexOf(';');
            if ( ix2 != -1 ) {
                sList.add(s2.substring(0, ix2+1));
                return splitSpecial(s2.substring(ix2+1), sList); 
            } else {
                System.err.println("This can't happen: " + s2);
                sList.add(s2.substring(0, 1));
                return splitSpecial(s2.substring(1), sList); 
            }
        }
        sList.add(xmlE);
        return splitSpecial(s2.substring(xmlE.length()), sList);
    }

}
