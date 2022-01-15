package org.example.services;

import org.example.Student;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class XmlParser {

    File inputFile;
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document;

    public XmlParser(String pathName) throws IOException, SAXException, ParserConfigurationException {
        inputFile = new File(pathName);
        factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        builder = factory.newDocumentBuilder();
        document = builder.parse(inputFile);
    }

    public LinkedList<Student> reader() {
        NodeList nList = document.getElementsByTagName("student");
        LinkedList<Student> students = new LinkedList<>();
        String tempSubjectName;
        Integer mark;

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                Student tempStudent = new Student();

                tempStudent.setSurname(eElement.getAttribute("lastname"));
                tempStudent.setGroupName(eElement.getAttribute("groupnumber"));
                tempStudent.setName(eElement.getAttribute("firstname"));

                NodeList fields = nNode.getChildNodes();

                for (int i = 0; i < fields.getLength(); i++) {
                    Node field = fields.item(i);

                    if (field.getNodeType() == Node.ELEMENT_NODE) {
                        if ("subject".equals(field.getNodeName())) {
                            mark = Integer.parseInt(field
                                    .getAttributes()
                                    .getNamedItem("mark")
                                    .getTextContent());
                            tempSubjectName = field
                                    .getAttributes()
                                    .getNamedItem("title")
                                    .getTextContent();
                            tempStudent.addSubjects(tempSubjectName, mark);
                        }
                    }
                }
                students.add(tempStudent);
            }
        }
        return students;
    }

    public void writer(LinkedList<Student> list) throws TransformerException, FileNotFoundException {
        NodeList nList = document.getElementsByTagName("student");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;

                eElement.setAttribute("lastname", list.get(temp).getSurname());
                eElement.setAttribute("groupnumber", list.get(temp).getGroupName());
                eElement.setAttribute("firstname", list.get(temp).getName());

                NodeList fields = nNode.getChildNodes();
                Iterator itr = list.get(temp).getSubjects().entrySet().iterator();

                for (int i = 0; i < fields.getLength(); i++) {
                    Node field = fields.item(i);
                    if (field.getNodeType() == Node.ELEMENT_NODE) {
                        if ("subject".equals(field.getNodeName())) {
                            Map.Entry<String, Integer> student = (Map.Entry<String, Integer>) itr.next();
                            field.getAttributes()
                                    .getNamedItem("title")
                                    .setTextContent(student.getKey());
                            field.getAttributes()
                                    .getNamedItem("mark")
                                    .setTextContent(String.valueOf(student.getValue()));
                        } else if ("average".equals(field.getNodeName())) {
                            field.getChildNodes().item(0).setTextContent(String.valueOf(list.get(temp).getAverageMark()));
                        }
                    }
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "xmlSource/group.dtd");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new FileOutputStream(inputFile));

        transformer.transform(source, result);
    }
}
