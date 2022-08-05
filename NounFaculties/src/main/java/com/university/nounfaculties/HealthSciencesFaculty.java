package com.university.nounfaculties;

import java.util.ArrayList;

public class HealthSciencesFaculty {
    public static ArrayList<String> coursesList, creditUnitList;


    public HealthSciencesFaculty(){
        //initialize arrayList
        coursesList = new ArrayList<>();
        creditUnitList = new ArrayList<>();

        // 100 level first semester.
        addCourse("PHL126",2);
        addCourse("GST103",2);
        addCourse("GST105",2);
        addCourse("GST107",2);
        addCourse("GST101",2);
        addCourse("GST104",2);
        addCourse("GST102",2);
        addCourse("POL111",3);
        addCourse("CSS111",3);
        // 100 level second semester

        // 200 level first semester.
        addCourse("NSC205",3);
        addCourse("NSC207",3);
        addCourse("EHS201",2);
        addCourse("EHS209",1);
        addCourse("EHS213",1);
        addCourse("EHS205",2);
        addCourse("EHS508",2);
        addCourse("EHS202",2);
        addCourse("EHS210",2);
        addCourse("GST203",2);
        addCourse("GST201",2);
        addCourse("CIT104",2);
        addCourse("PHY103",2);
        addCourse("BIO102",2);
        addCourse("MTH101",3);
        addCourse("CHM101",2);
        addCourse("PED221",2);
        addCourse("GST203",2);
        addCourse("CIT104",2);
        addCourse("BIO217",3);
        addCourse("NSS211",5);
        addCourse("NSC203",4);
        addCourse("NSS201",3);
        addCourse("PHS217",3);
        addCourse("NSC201",4);
        addCourse("PHS203",3);
        addCourse("PHS201",3);
        addCourse("NSC209",2);
        // 200 level second semester.
        addCourse("PHS204",3);
        addCourse("EHS203",2);
        addCourse("EHS211",3);
        addCourse("EHS204",2);
        addCourse("EHS206",2);
        addCourse("NSC204",2);
        addCourse("NSS214",3);
        addCourse("NSS213",3);
        addCourse("ANP204",3);
        addCourse("EHS212",3);
        addCourse("NSC207",3);
        addCourse("NSS217",2);
        addCourse("PHS202",3);
        addCourse("PHS210",3);
        addCourse("NSC210",3);
        addCourse("NSC104",4);
        addCourse("NSC208",2);
        addCourse("NSC202",3);
        //300 level first semester.
        addCourse("CHS314",3);
        addCourse("EHS316",2);
        addCourse("EHS302",3);
        addCourse("EHS304",2);
        addCourse("EHS310",3);
        addCourse("EHS305",2);
        addCourse("EHS303",2);
        addCourse("EHS301",2);
        addCourse("GST302",2);
        addCourse("NSS327",2);
        addCourse("NSS325",3);
        addCourse("NSS323",4);
        addCourse("NSC312",3);
        addCourse("NSC306",3);
        addCourse("NSC314",3);
        addCourse("NSS321",3);
        addCourse("NSS311",3);
        addCourse("NSC316",3);
        addCourse("NSC305",3);
        addCourse("NSS305",2);
        addCourse("NSS303",2);
        addCourse("NSS301",3);
        addCourse("NSC309",4);
        addCourse("NSC301",2);
        addCourse("NSC307",3);
        addCourse("NSC303",2);
        addCourse("NSC111",2);
        addCourse("NSC311",2);
        addCourse("EHS317",2);
        addCourse("EHS319",3);
        addCourse("PHS311",3);
        addCourse("PHS303",2);
        addCourse("PHS301",3);
        // 300 level second semester.
        addCourse("EHS322",2);
        addCourse("EHS320",2);
        addCourse("EHS315",3);
        addCourse("EHS321",2);
        addCourse("EHS314",2);
        addCourse("EHS313",2);
        addCourse("EHS318",2);
        addCourse("EHS311",2);
        addCourse("EHS312",2);
        addCourse("EHS308",3);
        addCourse("EHS306",2);
        addCourse("NSS311",3);
        addCourse("NSS324",4);
        addCourse("NSS312",3);
        addCourse("NSS316",3);
        addCourse("NSS322",3);
        addCourse("NSS306",2);
        addCourse("NSS302",3);
        addCourse("PHS326",3);
        addCourse("PHS322",3);
        addCourse("PHS318",3);
        addCourse("PHS312",2);
        addCourse("PHS308",3);
        addCourse("PHS302",3);
        addCourse("NSC206",2);
        addCourse("PHS305",2);
        // 400 level first semester.
        addCourse("EHS401",2);
        addCourse("EHS407",3);//TODO or 2
        addCourse("EHS415",2);
        addCourse("PHS401",2);
        addCourse("EHS417",2);
        addCourse("NCS403",2);
        addCourse("NSC402",4);
        addCourse("NSC401",4);
        addCourse("NSC403",3);
        addCourse("NSC411",3);
        addCourse("NSC407",2);
        addCourse("NSC409",2);
        addCourse("NSC412",3);
        addCourse("EHS405",2);
        addCourse("EHS403",3);
        addCourse("NSS409",2);
        addCourse("EHS411",3);
        addCourse("PHS421",3);
        addCourse("NSS403",4);
        addCourse("NSS401",4);
        addCourse("NSS413",4);
        addCourse("NSS411",3);
        addCourse("NSS407",3);
        addCourse("EHS409",2);
        addCourse("PHS403",2);
        // 400 level second semester.
        addCourse("EHS419",2);
        addCourse("EHS413",3);
        addCourse("NSC406",3);
        addCourse("NSC416",4);
        addCourse("NSS402",4);
        addCourse("EHS402",6);
        addCourse("PHS424",2);
        addCourse("PHS422",3);
        addCourse("BIO411",2);
        addCourse("NSS410",4);
        addCourse("NSS412",3);
        addCourse("PHS430",3);
        addCourse("PHS426",3);
        addCourse("PHS404",3);
        addCourse("PHS402",4);
        // 500 level first semester
        addCourse("PHS599",5);
        addCourse("PHS507",2);
        addCourse("PHS505",3);
        addCourse("NSS509",3);
        addCourse("NSS504",3);
        addCourse("NSS507",3);
        // 500 level second semester
        addCourse("PHS512",6);
        addCourse("PHS524",3);
        addCourse("PHS520",6);
        addCourse("NSS511",3);
        addCourse("NSS508",3);
        addCourse("NSS513",3);
    }


    private void addCourse(String course, int creditUnit){

        // adding data to the arrayList.
        coursesList.add(course);
        creditUnitList.add(Integer.toString(creditUnit));
    }
}
