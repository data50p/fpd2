package com.femtioprocent.fpd2.b.util.text;

import com.femtioprocent.fpd2.b.ajax.LoggerFactory;
import java.io.StringReader;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class HTML2plain extends HTML {

    private static final Logger logger = LoggerFactory.getLogger(HTML2plain.class);
    
    public String convert(String htmlText) throws Throwable {
        try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            final StringBuilder sb = new StringBuilder();

            htmlText = htmlText.replace("\r", "");
            htmlText = htmlText.replace("\n", "");
            htmlText = cleanUp(htmlText);
            
            logger.info("the html is: " + htmlText);
            //        htmlText = htmlText.replace("&", "&amp;");

            String in = "<?xml version='1.0'?>\n<!DOCTYPE pdd [\n<!ENTITY nbsp \" \">\n]>\n<pdd>" + htmlText + "</pdd>";

            DefaultHandler handler = new DefaultHandler() {
                Stack<Integer> olStack = new Stack<Integer>();
                int olCnt = 0;

                String bl20 = "                    ";

                int indentBase = 0;
                
                String indent() {
                    return indent(olStack.size() * 2);
                }
                String indent(int width) {
                    if ( width == 0 )
                        return "";
                    if ( width < 20 )
                        return bl20.substring(0, width);
                    return bl20 + indent(width - 20);
                }

                String[] bullets = new String[] {
                        "",
                        "*",
                        "o",
                        "O",
                        "+",
                };
                String bullet() {
                    int index = olStack.size();
                    if ( index >= 5 )
                        if ( index-3 < 10)
                            return "**********".substring(0, index-3);
                        else
                            return "**********(" + (index-3+10) + ")";
                    return bullets[index];
                }

                public void characters(char[] ch, int start, int length) {
                    String s = new String(ch, start, length);
                    String sa[] = s.split("\n");
                    for(String s1 : sa) {
                        sb.append(s1);
                        sb.append("\n");
                    }
//                    sb.append(ch, start, length);
                }
                
                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if ( qName.equals("ol") ) {
                        olStack.push(olCnt);
                        olCnt = 1;
                        sb.append("\n");
                    } else if ( qName.equals("ul") ) {
                        olStack.push(olCnt);
                        olCnt = 0;
                        sb.append("\n");
                    } else if ( qName.equals("li") ) {
                        if ( olCnt > 0 ) {
                            sb.append(indent() + olCnt + ". ");
                            olCnt++;
                        } else {
                            // emit a bullet when olCnt == 0, don't increment
                            sb.append(indent() + bullet() + " ");
                        }
                    } else if ( qName.equals("br") ) {
                        sb.append("\n");
                    } else if ( qName.equals("p") ) {
                        String indSpec = attributes.getValue("style");
                        // expect: style="padding-left: 30px;"
                        int indent = parseIndent(indSpec);
                        indentBase = indent;
                        logger.info("indentBase: " + indentBase);
                    }
                }                
                private int parseIndent(String indSpec) {
                    if ( indSpec == null )
                        return 0;
                    int ix = indSpec.indexOf("padding-left:");
                    if ( ix == -1)
                        return 0;
                    String s = indSpec.substring(ix + 13).trim();
                    Pattern pat = Pattern.compile("[0-9]+");
                    Matcher mat = pat.matcher(s);
                    if ( mat.find() ) {
                        String ns = mat.group();
                        return Integer.parseInt(ns);
                    }
                    return 0;
                }
                
                public void endElement(String uri, String localName, String qName) {
                    if ( qName.equals("ol") ) {
                        olCnt = olStack.pop();
                    } else if ( qName.equals("ul") ) {
                        olCnt = olStack.pop();
                    } else if ( qName.equals("li") ) {
                        sb.append("\n");
                    } else if ( qName.equals("h1") ) {
                        sb.append("\n");
                    } else if ( qName.equals("h2") ) {
                        sb.append("\n");
                    } else if ( qName.equals("p") ) {
                        sb.append("\n" + indent(indentBase / 10));
                    }
                }
            };
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            xr.parse(new InputSource(new StringReader(in)));

            String plain = sb.toString();
            logger.info("the plain is: " + plain);
            return plain;
        } catch (Throwable err) {
            logger.info("the failed plain is: " + err + ' ' + htmlText);
            throw err;
        }
    }
}
