package org.avmrus.fopds;

import java.util.HashMap;

public class DownloadTemplate {
    private static volatile DownloadTemplate instance;
    private HashMap<String, DownloadRecord> map;

    private DownloadTemplate() {
        super();
        map = new HashMap<>();
        fillRecords();
    }

    public static DownloadTemplate getInstance() {
        if (instance == null) {
            synchronized (DownloadTemplate.class) {
                if (instance == null) {
                    instance = new DownloadTemplate();
                }
            }
        }
        return instance;
    }

    public HashMap<String, DownloadRecord> getTemplates() {
        return map;
    }

    private void fillRecords() {
        map.put("application/fb2+zip", new DownloadRecord("application/fb2+zip", "fb2.zip", "FB2"));
        map.put("application/html+zip", new DownloadRecord("application/html+zip", "html.zip", "HTML"));
        map.put("application/txt+zip", new DownloadRecord("application/txt+zip", "txt.zip", "TEXT"));
        map.put("application/rtf+zip", new DownloadRecord("application/rtf+zip", "rtf.zip", "RTF"));
        map.put("application/epub+zip", new DownloadRecord("application/epub+zip", "epub", "EPUB"));
        map.put("application/pdf", new DownloadRecord("application/pdf", "pdf", "PDF"));
        map.put("application/djvu", new DownloadRecord("application/djvu", "djv", "DJVU"));
        map.put("application/docx", new DownloadRecord("application/docx", "docx", "DOCX"));
        map.put("application/doc", new DownloadRecord("application/doc", "doc", "DOC"));
        map.put("application/x-mobipocket-ebook", new DownloadRecord("application/x-mobipocket-ebook", "mobi", "MOBI"));
    }
}
