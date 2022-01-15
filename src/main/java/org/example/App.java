package org.example;

import org.example.services.XmlParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        XmlParser xmlParser = null;
        Scanner in = new Scanner(System.in);
        String tempPath;

        while (true) {
            System.out.print("Importantly!!!\n" +
                    "Place the file for verification in the xml Source directory.\n" +
                    " Enter a filename to check -");
            tempPath = in.nextLine();

            try {
                xmlParser = new XmlParser("xmlSource/" + tempPath);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                System.out.println("\nIncorrect filename!\n");
                continue;
            }
            break;
        }

        LinkedList<Student> studentLinkedList = null;

        if (xmlParser != null) {
            studentLinkedList = xmlParser.reader();
        }
        if (studentLinkedList != null) {
            for (Student temp : studentLinkedList) {
                temp.calculationAverage();
            }
        }
        try {
            xmlParser.writer(studentLinkedList);
        } catch (TransformerException | FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Checking successful!");
    }
}
