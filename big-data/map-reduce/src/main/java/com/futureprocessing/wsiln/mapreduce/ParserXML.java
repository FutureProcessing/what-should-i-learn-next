package com.futureprocessing.wsiln.mapreduce;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

public class ParserXML {
    private String body;
    private String tags;

    public ParserXML(String value) throws ParserConfigurationException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        ParserXmlHandler handler = new ParserXmlHandler();
        try {
            InputSource inputSource = new InputSource(new StringReader(value));
            saxParser.parse(inputSource, handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Get Employees list
        body = handler.getBody();
        tags = handler.getTags();
        //print employee information
    }

    public String getBody() {
        return body;
    }

    public String getTags() {
        return tags;
    }
}
