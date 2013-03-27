package org.javarebel.research;

import java.net.URL;

import javax.faces.view.facelets.ResourceResolver;

public class RemoteTemplateResolver extends ResourceResolver {
	
	private ResourceResolver parent;
	 
    public RemoteTemplateResolver(ResourceResolver parent) {
        this.parent = parent;
    }

	@Override
	public URL resolveUrl(String resource) {
		System.out.println("resource ==> " + resource);
		if("/template.xhtml".equals(resource)) {
			URL resURL = RemoteTemplateResolver.class.getClassLoader().getResource("template.xhtml");
			if(resURL != null) return resURL;
		}
		return parent.resolveUrl(resource);
	}
}
