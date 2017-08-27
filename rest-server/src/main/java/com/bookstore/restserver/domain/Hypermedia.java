package com.bookstore.restserver.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Renzo T.
 * @version 1.0
 */
@XmlRootElement
public class Hypermedia {

    private List<LinkResource> links = new ArrayList<>();

    public void addLink(LinkResource linkResource) {
        this.links.add(linkResource);
    }

    public List<LinkResource> getLinks() {
        return links;
    }

    public void setLinks(List<LinkResource> links) {
        this.links = links;
    }
}
