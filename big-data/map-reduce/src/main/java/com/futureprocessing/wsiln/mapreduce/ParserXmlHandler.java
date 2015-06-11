package com.futureprocessing.wsiln.mapreduce;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserXmlHandler extends DefaultHandler {

    private String body;
    private String tags;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (qName.equalsIgnoreCase("row")) {
            body = attributes.getValue("Body");
            tags = attributes.getValue("Tags");
        }
    }

    public String getBody() {
        return body;
    }

    public String getTags() {
        return tags;
    }
}
