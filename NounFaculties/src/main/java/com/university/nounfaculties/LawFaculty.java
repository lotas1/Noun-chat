package com.university.nounfaculties;

import java.util.ArrayList;

public class LawFaculty {
    public static ArrayList<String> coursesList, creditUnitList;

    public LawFaculty(){
        //initialize arrayList
        coursesList = new ArrayList<>();
        creditUnitList = new ArrayList<>();
        // 100 level first semester
        addCourse("LED023",3);
        addCourse("LED021",3);
        addCourse("CSS132",3);
        addCourse("CSS134",3);
        addCourse("ENG114",2);
        addCourse("CSS112",3);
        addCourse("CSS111",3);
        addCourse("CTH131",2);
        addCourse("GST105",2);
        addCourse("GST107",2);
        addCourse("ENG113",2);
        addCourse("POL121",3);
        addCourse("POL123",3);
        addCourse("POL124",3);
        addCourse("CIT101",2);
        addCourse("CIT102",2);
        addCourse("CSS121",3);
        addCourse("GST102",2);
        addCourse("ECO121",3);
        addCourse("INR112",2);
        addCourse("CSS131",3);
        addCourse("JIL111",2);
        // 100 second semester.
        addCourse("JIL100",3);
        addCourse("JIL112",2);
        addCourse("LED029",3);
        addCourse("PCR112",3);
        addCourse("LED027",3);
        addCourse("LED025",3);
        // 200 level first semester.
        addCourse("CLL203",3);
        addCourse("LAW103",2);
        addCourse("CLL233",4);
        addCourse("CLL231",4);
        addCourse("JIL211",4);
        addCourse("PUL243",4);
        addCourse("PUL241",4);
        // 200 second semester.
        addCourse("CLL232",4);
        addCourse("JIL212",4);
        addCourse("GST201",2);
        addCourse("GST203",2);
        addCourse("PUL244",4);
        addCourse("PUL242",4);
        addCourse("CLL234",4);
        // 300 first semester.
        addCourse("PUL337",3);
        addCourse("CLL307",3);
        addCourse("PUL321",4);
        addCourse("LAW331",4);
        addCourse("PPL343",4);
        addCourse("CLL331",4);
        addCourse("PUL341",4);
        addCourse("PPL323",4);
        // 300 level second semester.
        addCourse("PPL344",4);
        addCourse("LAW332",4);
        addCourse("PPL324",4);
        addCourse("PUL322",4);
        addCourse("PUL342",4);
        // 400 level first semester.
        addCourse("JIL447",4);
        addCourse("PUL446",4);
        addCourse("PUL445",4);
        addCourse("PUL443",4);
        addCourse("JIL441",4);
        addCourse("PPL435",4);
        addCourse("PUL433",0);//TODO
        addCourse("CLL431",4);
        addCourse("PPL421",4);
        addCourse("BUS428",3);
        addCourse("PCR111",3);
        addCourse("PPL423",4);
        addCourse("PUL411",4);
        // 400 level second semester.
        addCourse("JIL448",4);
        addCourse("JIL442",4);
        addCourse("SMS201",3);
        addCourse("CLL432",4);
        addCourse("INR212",3);
        addCourse("PUL444",4);
        addCourse("PPL436",4);
        addCourse("PUL434",4);
        addCourse("PPL424",4);
        addCourse("PPL422",4);
        addCourse("PUL412",4);
    }

    private void addCourse(String course, int creditUnit){

        // adding data to the arrayList.
        coursesList.add(course);
        creditUnitList.add(Integer.toString(creditUnit));
    }
}
