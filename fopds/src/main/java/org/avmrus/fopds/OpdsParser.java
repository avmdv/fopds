package org.avmrus.fopds;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class OpdsParser {
    private Document document;

    public OpdsParser(byte[] data) {
        super();
        this.document = initParser(data);
    }

    private Document initParser(byte[] data) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream(data));
        } catch (ParserConfigurationException | SAXException| IOException e) {
            e.printStackTrace();
        }
        return document;
    }

    public String getTitle() {
        Node node = ((Element) document.getElementsByTagName("feed").item(0)).getElementsByTagName("title").item(0);
        return node.getTextContent();
    }

    public int getBooksCount() {
        int count = 0;
        NodeList nodeList = document.getElementsByTagName("entry");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = ((Element) nodeList.item(i)).getElementsByTagName("id").item(0);
            if (node.getTextContent().equalsIgnoreCase("tag:search:new:books")) {
                Node node3 = ((Element) nodeList.item(i)).getElementsByTagName("content").item(0);
                count = Integer.parseInt(node3.getTextContent().split(" ")[0]);
                break;
            }
        }
        return count;
    }

    public ArrayList<Book> getBooksList() {
        ArrayList<Book> list = new ArrayList<>();
        NodeList entries = document.getElementsByTagName("entry");
        for (int i = 0; i < entries.getLength(); i++) {
            list.add(createBookFromOpds(entries.item(i)));
        }

        return list;
    }

    private Book createBookFromOpds(Node bookEntry) {
        Book book = new Book();
        NodeList entries = bookEntry.getChildNodes();
        for (int i = 0; i < entries.getLength(); i++) {
            parseOpdsItem(book, entries.item(i));
        }
        return book;
    }

    private void parseOpdsItem(Book book, Node node) {
        Element element;
        switch (node.getNodeName()) {
            case "id":
                String sid = node.getTextContent().split(":")[2];
                book.setId(sid);
                break;
            case "title":
                book.setTitle(node.getTextContent());
                break;
            case "author":
                parseAuthor(book, node);
                break;
            case "dc:language":
                book.setLanguage(node.getTextContent());
                break;
            case "category":
                element = (Element) node;
                book.getCategories().add(element.getAttribute("term"));
            case "content":
                book.setContent(node.getTextContent().replaceAll("<br/>", "\n").replaceAll("<p.+?>", "\n").replaceAll("</p>", ""));
                break;
            case "link":
                element = (Element) node;
                if (element.getAttribute("rel").equalsIgnoreCase("http://opds-spec.org/image")) {
                    book.setCoverUrl(element.getAttributeNode("href").getValue());
                } else if (element.getAttribute("rel").equalsIgnoreCase("http://opds-spec.org/acquisition/open-access")) {
                    HashMap<String, DownloadRecord> templates = DownloadTemplate.getInstance().getTemplates();
                    String type = element.getAttributeNode("type").getValue();
                    if (templates.containsKey(type)) {
                        DownloadRecord template = templates.get(type);
                        DownloadRecord record = new DownloadRecord(template);
                        record.setUrl(element.getAttributeNode("href").getValue());
                        book.getDownloadUrls().add(record);
                    }
                }
                break;
            default:
                break;
        }
    }

    public void parseAuthor(Book book, Node node) {
        Author author = new Author();
        NodeList entries = node.getChildNodes();
        for (int i = 0; i < entries.getLength(); i++) {
            switch (entries.item(i).getNodeName()) {
                case "uri":
                    author.setId(Integer.parseInt(entries.item(i).getTextContent().split("/")[2]));
                    break;
                case "name":
                    author.setName(entries.item(i).getTextContent());
                    break;
                default:
                    break;
            }
        }
        book.getAuthors().add(author);
    }

}
