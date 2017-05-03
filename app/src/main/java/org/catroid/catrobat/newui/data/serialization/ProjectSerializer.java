package org.catroid.catrobat.newui.data.serialization;

import com.thoughtworks.xstream.XStream;

import org.catroid.catrobat.newui.data.ProjectItem;

public class ProjectSerializer {
    public String serializeProject(ProjectItem project) {

        XStream xStream = new XStream();

        return xStream.toXML(project);
    }

}
