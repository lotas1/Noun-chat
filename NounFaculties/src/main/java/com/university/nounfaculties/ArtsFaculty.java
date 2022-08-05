package com.university.nounfaculties;

import java.util.ArrayList;

public class ArtsFaculty {
    public static ArrayList<String> coursesList, creditUnitList;

    public ArtsFaculty(){
        //initialize arrayList
        coursesList = new ArrayList<>();
        creditUnitList = new ArrayList<>();

        // 100 level first semester
        addCourse("ENG172",2);
        addCourse("CRS173",2);
        addCourse("CRS113",2);
        addCourse("CRS101",2);
        addCourse("HAU101",2);
        addCourse("HAU111",2);
        addCourse("HAU117",2);
        addCourse("HAU105",2);
        addCourse("PHL102",2);
        addCourse("HAU110",2);
        addCourse("HAU109",2);
        addCourse("IGB124",3);
        addCourse("IGB122",2);
        addCourse("IGB113",2);
        addCourse("IGB111",2);
        addCourse("YOR211",2);
        addCourse("YOR113",2);
        addCourse("YOR111",2);
        addCourse("ARA127",2);
        addCourse("ARA111",2);
        addCourse("INR113",3);
        addCourse("LIN111",2);
        addCourse("FRE141",2);
        addCourse("FRE131",2);
        addCourse("FRE121",2);
        addCourse("FRE111",2);
        addCourse("CTH101",2);
        addCourse("CTH113",2);
        addCourse("CTH131",2);
        addCourse("CRS111",2);
        addCourse("CTH141",2);
        addCourse("CTH173",2);
        addCourse("CRS151",2);
        addCourse("ENG141",3);
        addCourse("ISL121",3);
        addCourse("ISL113",2);
        addCourse("ISL101",2);
        addCourse("ENG211",2);
        addCourse("FRE101",2);
        addCourse("LIN111",2);
        addCourse("CRS131",2);
        addCourse("CSS111",3);
        addCourse("ENG181",2);
        addCourse("ENG161",2);
        addCourse("GST103",2);
        addCourse("GST105",2);
        addCourse("GST107",2);
        addCourse("GST101",2);
        addCourse("GST104",2);
        addCourse("ENG113",2);
        addCourse("ENG111",3);
        addCourse("CIT101",2);
        addCourse("POL121",3);
        addCourse("INR121",2);
        addCourse("INR111",2);
        addCourse("ENG121",2);
        addCourse("POL111",3);
        addCourse("ARA183",2);
        addCourse("ARA181",2);
        // 100 level second semester
        addCourse("CRS192",2);
        addCourse("CRS152",2);
        addCourse("CRS122",2);
        addCourse("CRS102",2);
        addCourse("HAU116",2);
        addCourse("HAU108",2);
        addCourse("POL126",3);
        addCourse("INR132",2);
        addCourse("INR122",2);
        addCourse("INR112",2);
        addCourse("INR132",2);
        addCourse("GST102",2);
        addCourse("CIT102",2);
        addCourse("CRS141",2);
        addCourse("CRS142",2);
        addCourse("PHL126",2);
        addCourse("HAU112",2);
        addCourse("YOR124",2);
        addCourse("YOR122",2);
        addCourse("PHL106",2);
        addCourse("PHL152",2);
        addCourse("PHL105",2);
        addCourse("ARA114",2);
        addCourse("ARA112",2);
        addCourse("FRE162",2);
        addCourse("FRE132",2);
        addCourse("FRE112",2);
        addCourse("FRE152",2);
        addCourse("FRE122",2);
        addCourse("ISL132",2);
        addCourse("CTH152",2);
        addCourse("CTH192",2);
        addCourse("CTH122",2);
        addCourse("CTH142",2);
        addCourse("CTH102",2);
        addCourse("INR142",2);
        addCourse("ARA182",2);
        addCourse("ISL172",3);
        addCourse("ISL142",2);
        addCourse("ISL136",2);
        addCourse("ISL102",2);
        addCourse("ISL111",2);
        addCourse("FRE102",2);
        addCourse("LIN112",2);
        addCourse("ENG151",2);
        addCourse("ENG122",2);
        addCourse("ENG114",2);
        addCourse("ENG162",2);
        // 200 level first semester.
        addCourse("CRS241",2);
        addCourse("HAU215",2);
        addCourse("HAU209",2);
        addCourse("ENG224",2);
        addCourse("CRD203",2);
        addCourse("CRD208",2);
        addCourse("PHL252",2);
        addCourse("PHL241",2);
        addCourse("PHL216",3);
        addCourse("PHL205",2);
        addCourse("PHL204",2);
        addCourse("PHL203",3);
        addCourse("PHL202",3);
        addCourse("PHL214",2);
        addCourse("PHL201",2);
        addCourse("ARA229",2);
        addCourse("ARA227",2);
        addCourse("CRS271",2);
        addCourse("CRS213",2);
        addCourse("HAU205",2);
        addCourse("HAU115",2);
        addCourse("FRE241",2);
        addCourse("HAU214",2);
        addCourse("HAU213",2);
        addCourse("HAU207",2);
        addCourse("HAU201",2);
        addCourse("IGB241",2);
        addCourse("IGB215",3);
        addCourse("IGB213",2);
        addCourse("YOR282",2);
        addCourse("YOR213",2);
        addCourse("PHL253",3);
        addCourse("PHL242",2);
        addCourse("ARA215",2);
        addCourse("ARA211",2);
        addCourse("ENG215",2);
        addCourse("ENG251",2);
        addCourse("FRE271",2);
        addCourse("FRE221",2);
        addCourse("FRE211",2);
        addCourse("FRE231",2);
        addCourse("CTH261",2);
        addCourse("CTH217",2);
        addCourse("CRS215",3);
        addCourse("CTH211",2);
        addCourse("CTH271",2);
        addCourse("CTH213",2);
        addCourse("CTH233",2);
        addCourse("GST203",2);
        addCourse("ARA283",2);
        addCourse("CRS233",2);
        addCourse("ISL271",2);
        addCourse("JLS111",3);
        addCourse("ISL245",1);
        addCourse("ISL231",2);
        addCourse("ISL213",2);
        addCourse("MAC212",3);
        addCourse("CRS217",2);
        addCourse("CRS261",2);
        addCourse("INR251",2);
        addCourse("INR231",2);
        addCourse("CSS211",3);
        addCourse("CTH215",3);
        addCourse("ENG241",3);
        addCourse("ENG223",2);
        addCourse("ENG221",2);
        addCourse("ARA281",2);
        //200 level second semester
        addCourse("ENG172",2);
        addCourse("CRS211",2);
        addCourse("HAU215",2);
        addCourse("HAU210",2);
        addCourse("CTH231",2);
        addCourse("INR221",2);
        addCourse("CRS214",2);
        addCourse("GST202",2);
        addCourse("PCR276",3);
        addCourse("CTH210",2);
        addCourse("INR242",3);
        addCourse("INR212",3);
        addCourse("INR232",3);
        addCourse("FRE222",2);
        addCourse("FRE232",2);
        addCourse("FRE251",2);
        addCourse("FRE242",2);
        addCourse("HAU211",2);
        addCourse("POL214",3);
        addCourse("HAU208",2);
        addCourse("HAU206",2);
        addCourse("HAU204",2);
        addCourse("IGB282",3);
        addCourse("IGB224",2);
        addCourse("IGB222",2);
        addCourse("IGB212",2);
        addCourse("IGB211",2);
        addCourse("YOR241",2);
        addCourse("YOR224",2);
        addCourse("YOR222",2);
        addCourse("YOR215",2);
        addCourse("YOR212",2);
        addCourse("ENG216",2);
        addCourse("FRE282",2);
        addCourse("FRE212",3);
        addCourse("FRE222",3);
        addCourse("CRS210",2);
        addCourse("CRS218",2);
        addCourse("CRS202",2);
        addCourse("CRS222",2);
        addCourse("CRS216",3);
        addCourse("CRS214",2);
        addCourse("CRS272",2);
        addCourse("CRS212",2);
        addCourse("CRS231",2);
        addCourse("ISL272",3);
        addCourse("ISL222",2);
        addCourse("ISL214",2);
        addCourse("ISL212",3);
        addCourse("ISL241",2);
        addCourse("ENG226",3);
        addCourse("ENG222",2);
        addCourse("ENG281",3);
        addCourse("ENG224",2);
        addCourse("ENG212",3);
        addCourse("ARA284",2);
        addCourse("ARA282",2);
        // 300 level first semester
        addCourse("ARA323",3);
        addCourse("ARA321",2);
        addCourse("ARA313",3);
        addCourse("ARA311",2);
        addCourse("ARA317",3);
        addCourse("CRS323",2);
        addCourse("CRS321",2);
        addCourse("ENG313",2);
        addCourse("ENG381",3);
        addCourse("ENG321",3);
        addCourse("FRE321",3);
        addCourse("FRE301",3);
        addCourse("FRE381",3);
        addCourse("FRE331",3);
        addCourse("CRS313",3);
        addCourse("CTH323",2);
        addCourse("CTH321",2);
        addCourse("CRS311",2);
        addCourse("ARA383",2);
        addCourse("ARA381",3);
        addCourse("ISL373",2);
        addCourse("ISL361",3);
        addCourse("ISL355",2);
        addCourse("ISL339",3);
        addCourse("ISL343",2);
        addCourse("ISL313",2);
        addCourse("CTH313",3);
        addCourse("ENG312",3);
        addCourse("CSS351",3);
        addCourse("POL311",3);
        addCourse("ENG311",3);
        addCourse("ENG353",3);
        addCourse("ENG351",3);
        addCourse("ENG321",3);
        addCourse("ENG355",3);
        addCourse("PCR375",3);
        addCourse("INR351",2);
        addCourse("INR331",3);
        addCourse("INR321",3);
        addCourse("FRE391",3);
        // 300 level second semester
        addCourse("ARA325",3);
        addCourse("ARA324",2);
        addCourse("CRS352",2);
        addCourse("CRS324",2);
        addCourse("CRS316",3);
        addCourse("CRS314",2);
        addCourse("GST301",2);
        addCourse("CRS302",2);
        addCourse("PCR362",3);
        addCourse("PCR352",3);
        addCourse("JIL100",3);
        addCourse("ARA381",2);
        addCourse("INR352",2);
        addCourse("INR342",2);
        addCourse("INR322",3);
        addCourse("ISL322",2);
        addCourse("INR312",2);
        addCourse("ARA383",2);
        addCourse("FRE341",2);
        addCourse("FRE371",2);
        addCourse("FRE361",2);
        addCourse("ENG362",3);
        addCourse("ENG372",3);
        addCourse("FRE372",2);
        addCourse("FRE322",2);
        addCourse("FRE382",3);
        addCourse("FRE392",3);
        addCourse("ENG316",2);
        addCourse("CTH316",3);
        addCourse("CTH314",2);
        addCourse("CTH302",2);
        addCourse("CTH352",2);
        addCourse("CTH324",2);
        addCourse("ISL330",2);
        addCourse("ISL304",2);
        addCourse("ISL302",2);
        addCourse("ISL374",2);
        addCourse("ISL372",2);
        addCourse("ISL332",2);
        addCourse("ISL312",2);
        addCourse("ARA382",2);
        addCourse("INR332",2);
        addCourse("ENG352",3);
        addCourse("ENG341",3);
        addCourse("ENG314",2);
        addCourse("ENG316",2);
        addCourse("ENG331",3);
        // 400 level first semester
        addCourse("CRS472",2);
        addCourse("CRS423",2);
        addCourse("CRS412",2);
        addCourse("FRE202",2);
        addCourse("ENG426",3);
        addCourse("ENG423",2);
        addCourse("ENG417",3);
        addCourse("ENG415",3);
        addCourse("ENG421",3);
        addCourse("FRE441",2);
        addCourse("FRE411",3);
        addCourse("FRE423",2);
        addCourse("FRE421",2);
        addCourse("FRE481",2);
        addCourse("CTH491",2);
        addCourse("CRS471",2);
        addCourse("CTH413",2);
        addCourse("CTH441",2);
        addCourse("CTH423",2);
        addCourse("ARA483",2);
        addCourse("ARA481",2);
        addCourse("ISL471",3);
        addCourse("ISL451",2);
        addCourse("ISL439",2);
        addCourse("ISL437",2);
        addCourse("ISL435",2);
        addCourse("ISL415",2);
        addCourse("ISL431",2);
        addCourse("ENG414",3);
        addCourse("ENG491",3);
        addCourse("ENG453",3);
        addCourse("ENG421",3);
        addCourse("ENG411",3);
        addCourse("PCR417",3);
        addCourse("INR441",2);
        addCourse("INR431",2);
        addCourse("INR421",2);
        addCourse("CSS491",3);
        // 400 level second semester
        addCourse("CRS491",2);
        addCourse("CRS441",2);
        addCourse("CRS422",2);
        addCourse("CRS413",2);
        addCourse("CTH441",2);
        addCourse("FRE401",2);
        addCourse("FRE402",2);
        addCourse("FRE424/INR442",6);
        addCourse("FRE472",2);
        addCourse("FRE422",2);
        addCourse("FRE482",2);
        addCourse("FRE471",2);
        addCourse("CSS432",3);
        addCourse("CSS452",3);
        addCourse("CRS474",4);
        addCourse("INR432",2);
        addCourse("INR422",2);
        addCourse("INR412",2);
        addCourse("CTH474",4);
        addCourse("CTH472",2);
        addCourse("CTH412",2);
        addCourse("CRS432",2);
        addCourse("CTH422",2);
        addCourse("ISL492",4);
        addCourse("ISL474",2);
        addCourse("ISL472",3);
        addCourse("ISL438",2);
        addCourse("ISL436",2);
        addCourse("ISL432",2);
        addCourse("ISL412",3);
        addCourse("ISL402",2);
        addCourse("ENG454",3);
        addCourse("ENG434",3);
        addCourse("ENG432",3);
        addCourse("ENG418",6);
        addCourse("ENG416",2);
    }

    private void addCourse(String course, int creditUnit){

        // adding data to the arrayList.
        coursesList.add(course);
        creditUnitList.add(Integer.toString(creditUnit));
    }
}

