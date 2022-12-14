package com.university.nounfaculties;

import java.util.ArrayList;

public class SocialSciencesFaculty {
    public static ArrayList<String> coursesList, creditUnitList;

    public SocialSciencesFaculty(){
        //initialize arrayList
        coursesList = new ArrayList<>();
        creditUnitList = new ArrayList<>();

        // 100 level first semester
        addCourse("POL124",3);
        addCourse("POL123",3);
        addCourse("POL111",3);
        addCourse("FMC113",2);
        addCourse("PHL103",3);
        addCourse("GST107",2);
        addCourse("GST105",2);
        addCourse("GST103",2);
        addCourse("GST101",2);
        addCourse("DES111",2);
        addCourse("CIT101",2);
        addCourse("CIT102",2);
        addCourse("JLS111",3);
        addCourse("TSM147",2);
        addCourse("TSM145",2);
        addCourse("TSM143",2);
        addCourse("ENG113",2);//
        addCourse("TSM141",2);
        addCourse("POL123",3);
        addCourse("POL121",3);
        addCourse("PCR115",3);
        addCourse("MAC121",3);
        addCourse("MAC117",2);
        addCourse("INR121",2);
        addCourse("INR111",2);
        addCourse("MAC115",2);
        addCourse("ENG114",2);//
        addCourse("MAC113",2);
        addCourse("MAC111",3);
        addCourse("PCR113",3);
        addCourse("PCR111",3);
        addCourse("POL111",3);
        addCourse("CRD124",2);
        addCourse("FRE101",2);
        addCourse("FRE102",2);
        addCourse("FRE111",2);
        addCourse("FRE122",2);
        addCourse("ENT101",2);
        addCourse("ENT121",2);
        addCourse("ENG121",2);
        addCourse("ENG122",2);
        addCourse("HCM131",2);
        addCourse("BUS105",2);
        addCourse("ECO153",3);
        addCourse("ECO121",3);
        addCourse("CSS134",3);
        addCourse("CSS133",3);
        addCourse("CSS131",3);
        addCourse("CSS121",3);
        addCourse("CSS111",3);
        // 100 level second semester.
        addCourse("FMC116",2);
        addCourse("PHL102",2);
        addCourse("MAC132",2);
        addCourse("TSM146",2);
        addCourse("GST102",2);
        addCourse("GST104",2);
        addCourse("TSM144",2);
        addCourse("TSM142",2);
        addCourse("ECO154",2);
        addCourse("PCR112",3);
        addCourse("POL124",3);
        addCourse("INR162",2);
        addCourse("INR142",2);
        addCourse("INR132",2);
        addCourse("INR122",2);
        addCourse("INR112",2);
        addCourse("MKT108",2);
        addCourse("BUS106",2);
        addCourse("MAC142",3);
        addCourse("MAC134",2);
        addCourse("MAC118",2);
        addCourse("MAC116",2);
        addCourse("JIL100",4);
        addCourse("PCR114",3);
        addCourse("ECO146",3);
        addCourse("ECO122",3);
        addCourse("CSS152",3);
        addCourse("POL126",3);
        addCourse("CSS136",3);
        addCourse("CSS132",3);
        addCourse("CSS112",3);
        // 200 level first semester.
        addCourse("POL215",3);
        addCourse("ECO292",2);
        addCourse("FMC217",2);
        addCourse("FMC221",2);
        addCourse("TSM221",2);
        addCourse("FMC215",2);
        addCourse("FMC212",2);
        addCourse("ACC203",3);
        addCourse("FSS211",2);
        addCourse("DES216",3);
        addCourse("DES215",3);
        addCourse("DES218",2);
        addCourse("DES112",2);
        addCourse("DES219",2);
        addCourse("DES213",2);
        addCourse("DES211",2);
        addCourse("DES220",2);
        addCourse("GST201",2);
        addCourse("GST204",2);
        addCourse("GST203",2);
        addCourse("PCR261",3);
        addCourse("INR261",2);
        addCourse("BUS205",3);
        addCourse("BUS207",3);
        addCourse("ENG223",2);
        addCourse("TSM243",2);
        addCourse("TSM241",2);
        addCourse("POL231",3);
        addCourse("POL221",2);
        addCourse("POL223",3);
        addCourse("POL211",3);
        addCourse("POL231",3);
        addCourse("BFN209",3);
        addCourse("INR251",2);
        addCourse("INR231",2);
        addCourse("INR211",2);
        addCourse("JIL211",4);
        addCourse("JIL212",4);
        addCourse("MAC225",2);
        addCourse("MAC223",3);
        addCourse("MAC221",2);
        addCourse("MAC213",2);
        addCourse("MAC211",3);
        addCourse("HCM237",2);
        addCourse("STT205",3);
        addCourse("PCR211",3);
        addCourse("ECO247",3);
        addCourse("ECO255",2);
        addCourse("ECO253",3);
        addCourse("ECO231",2);
        addCourse("CSS245",2);
        addCourse("PCR271",3);
        addCourse("CSS211",2);
        addCourse("CSS243",3);
        addCourse("CSS241",3);
        // 200 level second semester
        addCourse("DES222",2);
        addCourse("DES221",2);
        addCourse("TSM244",2);
        addCourse("TSM252",2);
        addCourse("GST202",2);
        addCourse("POL224",3);
        addCourse("PCR276",3);
        addCourse("POL228",3);
        addCourse("POL226",3);
        addCourse("POL216",3);
        addCourse("ACC204",3);
        addCourse("FRE222",3);
        addCourse("POL212",3);
        addCourse("FRE221",2);
        addCourse("INR262",2);
        addCourse("POL214",3);
        addCourse("ENG224",2);
        addCourse("CRD204",2);
        addCourse("CRD208",2);
        addCourse("HCM234",2);
        addCourse("STT206",3);
        addCourse("INR242",3);
        addCourse("INR232",3);
        addCourse("INR222",3);
        addCourse("INR212",3);
        addCourse("MAC246",2);
        addCourse("MAC242",3);
        addCourse("MAC232",3);
        addCourse("MAC214",2);
        addCourse("INR221",2);
        addCourse("ENT204",2);
        addCourse("MAC224",3);
        addCourse("ECO256",2);
        addCourse("ECO254",3);
        addCourse("ECO232",2);
        addCourse("CRS202",2);
        addCourse("PCR276",3);
        addCourse("MAC212",3);
        addCourse("CIT208",2);
        addCourse("PCR274",3);
        addCourse("PCR276",3);
        addCourse("PCR272",3);
        addCourse("CSS246",3);
        addCourse("CSS242",3);
        addCourse("CSS244",3);
        addCourse("CSS212",3);
        // 300 level firsts semester.
        addCourse("DES318",2);
        addCourse("DES317",3);
        addCourse("DES315",3);
        addCourse("DES313",2);
        addCourse("DES311",2);
        addCourse("DES309",2);
        addCourse("POL344",3);
        addCourse("POL345",2);
        addCourse("NSC301",3);
        addCourse("CSS380",2);
        addCourse("EHS319",3);
        addCourse("POL341",3);
        addCourse("POL343",3);
        addCourse("INR309",3);
        addCourse("CSS331",3);
        addCourse("MAC343",3);
        addCourse("ECO323",2);
        addCourse("CSS331",2);
        addCourse("ECO332",3);
        addCourse("MAC311",3);
        addCourse("MAC345",2);
        addCourse("MAC323",3);
        addCourse("MAC315",2);
        addCourse("POL322",3);
        addCourse("POL315",3);
        addCourse("TSM349",2);
        addCourse("TSM347",2);
        addCourse("PCR311",3);
        addCourse("PCR373",3);
        addCourse("PCR371",3);
        addCourse("PCR331",3);
        addCourse("POL337",3);
        addCourse("PUL341",4);
        addCourse("POL324",3);
        addCourse("POL317",3);
        addCourse("POL316",3);
        addCourse("INR341",2);
        addCourse("INR391",2);
        addCourse("POL311",3);
        addCourse("INR381",3);
        addCourse("INR371",2);
        addCourse("INR361",3);
        addCourse("INR351",2);
        addCourse("INR331",3);
        addCourse("INR321",3);
        addCourse("MAC341",3);
        addCourse("MAC333",3);
        addCourse("MAC331",3);
        addCourse("MAC313",2);
        addCourse("ECO311",3);
        addCourse("ECO329",3);
        addCourse("ECO347",2);
        addCourse("ECO355",3);
        addCourse("ECO343",3);
        addCourse("ECO341",3);
        addCourse("PCR375",3);
        addCourse("BUS325",3);
        addCourse("CRD305",2);
        addCourse("HCM343",2);
        addCourse("HCM313",2);
        addCourse("CSS343",3);
        addCourse("CSS381",3);
        addCourse("CSS361",3);
        addCourse("CSS351",3);
        addCourse("CSS371",3);
        addCourse("CSS341",3);
        // 300 level second semester
        addCourse("DES316",3);
        addCourse("DES314",2);
        addCourse("DES312",2);
        addCourse("POL312",3);
        addCourse("GST302",2);
        addCourse("INR393",2);
        addCourse("INR381",3);
        addCourse("MAC324",2);
        addCourse("INR302",2);
        addCourse("CRD324",3);
        addCourse("ECO344",3);
        addCourse("INR311",3);
        addCourse("PCR312",3);
        addCourse("HCM340",2);
        addCourse("TSM350",6);
        addCourse("TSM310",2);
        addCourse("TSM348",2);
        addCourse("TSM342",2);
        addCourse("TSM305",2);
        addCourse("PCR352",3);
        addCourse("PCR374",3);
        addCourse("PCR372",3);
        addCourse("PUL342",4);
        addCourse("PAD341",3);
        addCourse("BUS322",3);
        addCourse("FSM304",3);
        addCourse("POL318",3);
        addCourse("POL301",3);
        addCourse("POL312",3);
        addCourse("INR312",2);
        addCourse("INR372",2);
        addCourse("INR386",3);
        addCourse("INR382",2);
        addCourse("INR342",2);
        addCourse("INR352",2);
        addCourse("INR332",2);
        addCourse("INR322",3);
        addCourse("MAC312",3);
        addCourse("MAC314",2);
        addCourse("MAC316",2);
        addCourse("MAC318",4);
        addCourse("MAC334",2);
        addCourse("MAC332",2);
        addCourse("MAC322",3);
        addCourse("ECO346",2);
        addCourse("ECO314",2);
        addCourse("ECO348",3);
        addCourse("ECO356",3);
        addCourse("ECO324",3);
        addCourse("ECO342",3);
        addCourse("CRD334",2);
        addCourse("POL326",2);
        addCourse("PCR362",3);
        addCourse("CSS342",3);
        addCourse("CSS356",3);
        addCourse("CSS354",3);
        addCourse("CSS352",3);
        // 400 level first semester
        addCourse("POL444",2);
        addCourse("POL441",2);
        addCourse("POL431",2);
        addCourse("PAD406",3);
        addCourse("POL412",3);
        addCourse("INR471",2);
        addCourse("INR411",2);
        addCourse("CSS461",4);
        addCourse("CSS411",3);
        addCourse("CSS455",3);
        addCourse("PUL241",4);
        addCourse("INR481",2);
        addCourse("INR461",2);
        addCourse("POL452",3);
        addCourse("POL441",3);
        addCourse("POL401",6);
        addCourse("TSM447",2);
        addCourse("TSM441",2);
        addCourse("POL421",3);
        addCourse("PCR421",3);
        addCourse("PCR419",3);
        addCourse("PCR423",3);
        addCourse("PCR411",3);
        addCourse("INR491",3);
        addCourse("PCR415",3);
        addCourse("PCR417",3);
        addCourse("INR451",3);
        addCourse("INR441",2);
        addCourse("INR431",2);
        addCourse("INR421",2);
        addCourse("MAC427",2);
        addCourse("MAC441",3);
        addCourse("MAC425",3);
        addCourse("MAC423",3);
        addCourse("MAC413",2);
        addCourse("MAC421",3);
        addCourse("MAC411",3);
        addCourse("PUL242",4);
        addCourse("ECO415",6);
        addCourse("ECO459",2);
        addCourse("ECO449",2);
        addCourse("ECO443",2);
        addCourse("ECO441",2);
        addCourse("ECO447",2);
        addCourse("BUS419",2);
        addCourse("ECO427",2);
        addCourse("ECO445",2);
        addCourse("ECO453",2);
        addCourse("TSM403",2);
        addCourse("BUS427",3);
        addCourse("ECO431",2);
        addCourse("HCM439",2);
        addCourse("HCM435",2);
        addCourse("CSS443",3);
        addCourse("BFN421",2);
        addCourse("ENG453",3);
        addCourse("CSS431",6);
        addCourse("CSS491",3);
        addCourse("CSS441",3);
        // 400 level second semester
        addCourse("POL443",3);
        addCourse("INR462",2);
        addCourse("POL422",3);
        addCourse("ECO450",2);
        addCourse("INR482",3);
        addCourse("INR492",6);
        addCourse("INR452",2);
        addCourse("CSS462",4);
        addCourse("ECO440",2);
        addCourse("HCM434",2);
        addCourse("TSM450",6);
        addCourse("HCM434",2);
        addCourse("POL432",3);
        addCourse("POL411",3);
        addCourse("POL401",6);
        addCourse("POL444",3);
        addCourse("HCM436",2);
        addCourse("HCM438",2);
        addCourse("PCR424",3);
        addCourse("PCR426",3);
        addCourse("TSM444",2);
        addCourse("TSM442",2);
        addCourse("PCR422",3);
        addCourse("MAC412",3);
        addCourse("POL424",3);
        addCourse("POL431",2);
        addCourse("INR422",2);
        addCourse("INR432",2);
        addCourse("INR412",2);
        addCourse("MAC418",6);
        addCourse("MAC444",3);
        addCourse("MAC442",3);
        addCourse("MAC428",2);
        addCourse("MAC424",3);
        addCourse("MAC416",2);
        addCourse("MAC414",2);
        addCourse("PUL244",4);
        addCourse("ECO444",2);
        addCourse("ECO452",2);
        addCourse("ECO448",2);
        addCourse("ECO446",2);
        addCourse("ECO454",2);
        addCourse("ECO442",2);
        addCourse("CSS442",3);
        addCourse("CSS432",3);
        addCourse("PCR412",6);
        addCourse("CSS433",6);
        addCourse("CSS452",3);

    }

    private void addCourse(String course, int creditUnit){

        // adding data to the arrayList.
        coursesList.add(course);
        creditUnitList.add(Integer.toString(creditUnit));
    }
}
