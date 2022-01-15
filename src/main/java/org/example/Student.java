package org.example;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private String name;
    private String surname;
    private String groupName;

    private Double averageMark=0d;
    private Map<String, Integer> subjects = new HashMap<>();

    public void calculationAverage() {
        for (Map.Entry<String, Integer> subject : subjects.entrySet()) {
            averageMark += subject.getValue();
        }
        averageMark /= Double.parseDouble(String.valueOf(subjects.size()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Double getAverageMark() {
        return averageMark;
    }

    public Map<String, Integer> getSubjects() {
        return subjects;
    }

    public void addSubjects(String str, Integer i) {
        subjects.put(str, i);
    }
}
