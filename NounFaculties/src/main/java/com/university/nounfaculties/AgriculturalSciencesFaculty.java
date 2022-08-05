package com.university.nounfaculties;

import java.util.ArrayList;

public class AgriculturalSciencesFaculty {
    public static ArrayList<String> coursesList, creditUnitList;

    public AgriculturalSciencesFaculty(){
        //initialize arrayList
        coursesList = new ArrayList<>();
        creditUnitList = new ArrayList<>();

        // 100 level first semester
        addCourse("HCM147",2);
        addCourse("HCM135",2);
        addCourse("HCM133",2);
        addCourse("GST105",2);
        addCourse("GST104",2);
        addCourse("GST121",2);
        addCourse("GST101",2);
        addCourse("HCM131",2);
        addCourse("PHY191",1);
        addCourse("CIT101",2);
        addCourse("CHM191",1);
        addCourse("BIO191",1);
        addCourse("BIO101",2);
        addCourse("CHM101",2);
        addCourse("CHM131",2);
        addCourse("ESM112",2);
        // 100 level second semester
        addCourse("HCM134",2);
        addCourse("HCM142",2);
        addCourse("HCM136",2);
        addCourse("GST102",2);
        addCourse("PHY192",2);
        addCourse("CHM192",1);
        addCourse("ESM104",2);
        addCourse("ESM102",2);
        addCourse("CHM102",2);
        addCourse("BIO192",2);
        addCourse("BIO102",2);
        addCourse("MTH102",3);
        addCourse("STT102",2);
        addCourse("HCM146",2);
        addCourse("HCM145",2);
        addCourse("HCM144",2);
        addCourse("HCM141",2);
        // 200 level first semester
        addCourse("AGR207",2);
        addCourse("ANP207",2);
        addCourse("ANP201",2);
        addCourse("SMS207",2);
        addCourse("GST201",2);
        addCourse("GST203",2);
        addCourse("GST202",2);
        addCourse("HCM232",2);
        addCourse("HCM239",2);
        addCourse("HCM237",2);
        addCourse("HCM235",2);
        addCourse("HCM231",2);
        addCourse("ARD201",2);
        addCourse("ARD203",2);
        addCourse("ARD251",2);
        addCourse("SLM201",2);
        addCourse("AGR205",2);
        addCourse("FRM211",2);
        addCourse("SOS203",2);
        addCourse("SOS201",2);
        addCourse("AGR203",2);
        addCourse("AGR201",3);
        addCourse("AGE202",2);
        addCourse("AEM251",2);
        addCourse("AEM246",2);
        addCourse("AEM203",2);
        addCourse("AEM201",2);
        // 200 level second semester
        addCourse("AFS202",2);
        addCourse("AEM212",2);
        addCourse("HCM210",3);
        addCourse("HCM238",2);
        addCourse("HCM236",2);
        addCourse("HCM234",2);
        addCourse("ENT206",2);
        addCourse("CIT204",2);
        addCourse("ARD202",2);
        addCourse("ANP204",3);
        addCourse("ANP202",2);
        addCourse("AGR215",2);
        addCourse("AGR202",2);
        addCourse("AFS220",2);
        addCourse("FST202",3);
        addCourse("AEM202",2);
        addCourse("HCM244",2);
        addCourse("HCM243",2);
        addCourse("HCM241",2);
        // 300 level first semester
        addCourse("AEA308",2);
        addCourse("SLM307",2);
        addCourse("SLM301",2);
        addCourse("SLM308",2);
        addCourse("ANP308",2);
        addCourse("HCM349",2);
        addCourse("HCM305",2);
        addCourse("HCM304",2);
        addCourse("CRP301",2);
        addCourse("ANP309",2);
        addCourse("ANP303",2);
        addCourse("AGR309",2);
        addCourse("CRP305",2);
        addCourse("CRP308",3);
        addCourse("CRP303",2);
        addCourse("AGR305",2);
        addCourse("AFM305",2);
        addCourse("SLM310",2);
        addCourse("SLM305",3);
        addCourse("ACP301",2);
        addCourse("HCM345",2);
        addCourse("HCM343",2);
        addCourse("HCM339",2);
        addCourse("HCM333",2);
        addCourse("HCM313",2);
        addCourse("CRP313",2);
        addCourse("SOS301",2);
        addCourse("SLM306",2);
        addCourse("CRP311",2);
        addCourse("ANP313",2);
        addCourse("AGR307",2);
        addCourse("AEM311",2);
        addCourse("AEM303",3);
        addCourse("AEM301",2);
        addCourse("CRD301",2);
        addCourse("AEA303",2);
        addCourse("ACP305",2);
        addCourse("ACP303",2);
        addCourse("ANP301",2);
        addCourse("CRP309",2);
        // 300 level second semester
        addCourse("CRP302",2);
        addCourse("CRP312",2);
        addCourse("SLM309",2);
        addCourse("HCM348",2);
        addCourse("GST302",2);
        addCourse("ENT310",2);
        addCourse("AGR401",3);
        addCourse("ANP314",2);
        addCourse("CRP310",3);
        addCourse("AEM310",2);
        addCourse("AGR314",3);
        addCourse("ARD312",2);
        addCourse("ANP312",2);
        addCourse("ANP306",2);
        addCourse("ANP304",2);
        addCourse("AGR302",2);
        addCourse("CRP304",2);
        addCourse("AEA310",3);
        addCourse("AEA308",2);
        addCourse("AEA306",2);
        addCourse("ARD304",2);
        addCourse("ARD302",3);
        addCourse("AFM318",2);
        addCourse("AFM314",2);
        addCourse("AFM310",2);
        addCourse("HCM342",2);
        addCourse("HCM340",2);
        addCourse("HCM310",3);
        addCourse("HCM303",2);
        addCourse("ARD301",2);
        addCourse("SLM303",2);
        addCourse("CRP306",2);
        addCourse("ARD308",2);
        addCourse("ANP310",2);
        addCourse("ANP307",2);
        addCourse("ANP302",2);
        addCourse("AGM314",2);
        addCourse("AFM321",2);
        addCourse("AFM317",2);
        addCourse("AEM304",2);
        addCourse("AEM302",3);
        addCourse("AEC308",2);
        addCourse("COP318",2);
        addCourse("AEC306",2);
        addCourse("AEA304",2);
        addCourse("AEA302",2);
        addCourse("HCM347",2);
        // 400 level first semester.
        addCourse("HCM441",2);
        addCourse("HCM403",2);
        addCourse("AFM401",1);
        addCourse("SLM407",3);
        addCourse("AEM411",3);
        addCourse("CRP407",3);
        addCourse("ARD403",3);
        addCourse("AEM403",3);
        addCourse("AEC401",2);
        addCourse("SLM403",2);
        addCourse("SLM401",2);
        addCourse("CRP405",2);
        addCourse("CRP403",2);
        addCourse("ANP403",2);
        addCourse("ANP401",2);
        addCourse("CRP401",2);
        addCourse("CSP401",3);
        addCourse("HCM439",2);
        addCourse("HCM437",2);
        addCourse("HCM435",2);
        addCourse("HCM431",2);
        addCourse("AGB404",3);
        addCourse("AEM458",3);
        addCourse("AEM450",3);
        addCourse("AEA403",2);
        addCourse("AEM451",3);
        addCourse("AEC401",2);
        addCourse("SLM401",2);
        addCourse("AFS401",2);
        addCourse("CRP407",3);
        addCourse("ANP401",2);
        addCourse("CRP401",2);
        addCourse("ARD403",3);
        addCourse("ANP403",2);
        addCourse("CRP405",2);
        addCourse("CPS401",3);
        addCourse("AGB404",3);
        addCourse("HCM437",2);
        addCourse("AEM458",3);
        addCourse("AEM450",3);
        addCourse("CSP401",3);
        addCourse("HCM403",2);
        addCourse("AEM451",3);
        addCourse("CRP403",2);
        addCourse("HCM441",2);
        addCourse("HCM439",2);
        addCourse("AEM403",3);
        addCourse("SLM403",2);
        addCourse("HCM433",2);
        // 400 level second semester
        addCourse("ANP407",2);
        addCourse("HCM444",2);
        addCourse("AGR401",3);
        addCourse("ANP407",2);
        addCourse("HCM442",2);
        addCourse("HCM438",2);
        addCourse("AEM405",3);
        addCourse("HCM442",2);
        addCourse("HCM450",6);
        addCourse("HCM444",2);
        addCourse("AGM401",2);
        addCourse("HCM434",2);
        addCourse("AGM401",2);
        addCourse("HCM412",2);
        addCourse("AEM400",12);
        addCourse("ARD401",2);
        addCourse("AGR403",3);
        addCourse("SLM405",2);
        addCourse("AGM403",2);
        addCourse("AEC403",3);
        addCourse("AGR401",3);
        addCourse("AGM403",2);
        addCourse("AFS401",2);
        addCourse("AGR403",3);
        addCourse("SLM405",2);
        addCourse("AGM401",2);
        addCourse("ARD401",2);
        addCourse("HCM436",2);
        addCourse("HCM412",2);
        addCourse("AEM400",12);
        addCourse("HCM450",6);
        addCourse("HCM438",2);
        addCourse("HCM436",2);
        addCourse("AGE421",3);
        addCourse("AEM411",3);
        addCourse("AEM405",3);
        addCourse("AEC403",3);
        addCourse("HCM432",2);
        // 500 level first semester
        addCourse("AEA510",2);
        addCourse("CRP505",2);
        addCourse("SLM509",2);
        addCourse("SLM503",2);
        addCourse("CRP513",3);
        addCourse("CRP509",2);
        addCourse("CRP507",2);
        addCourse("ANP510",2);
        addCourse("SLM514",2);
        addCourse("SLM512",2);
        addCourse("SLM508",2);
        addCourse("SLM506",2);
        addCourse("SLM505",2);
        addCourse("ANP511",2);
        addCourse("ANP512",2);
        addCourse("ANP501",2);
        addCourse("ANP506",2);
        addCourse("CRP511",2);
        addCourse("AGR503",2);
        addCourse("SLM501",2);
        addCourse("SLM507",3);
        addCourse("ARD512",2);
        addCourse("AEA509",2);
        addCourse("ARD507",3);
        addCourse("ARD506",2);
        addCourse("ARD505",3);
        addCourse("ARD504",3);
        addCourse("ARD503",3);
        addCourse("AEA505",2);
        addCourse("ARD502",3);
        addCourse("ARD501",3);
        addCourse("CRP508",3);
        addCourse("ARD509",3);
        addCourse("AGR515",1);
        addCourse("AGR501",3);
        addCourse("SLM504",2);
        addCourse("CRP503",2);
        addCourse("AFM505",3);
        addCourse("AEM511",3);
        addCourse("AEM509",3);
        addCourse("AEM507",3);
        addCourse("AEM505",3);
        addCourse("AEM504",3);
        addCourse("AEM503",3);
        addCourse("AEM501",3);
        addCourse("AEA507",3);
        addCourse("AEA506",3);
        addCourse("AEA503",3);
        addCourse("AEA501",3);
        // 500 level second semester
        addCourse("CRP516",2);
        addCourse("CRP512",2);
        addCourse("CRP504",3);
        addCourse("AEA510",3);
        addCourse("ANP514",2);
        addCourse("ANP513",2);
        addCourse("ANP508",2);
        addCourse("ANP509",2);
        addCourse("ANP505",2);
        addCourse("ANP503",2);
        addCourse("ANP504",2);
        addCourse("ANP502",2);
        addCourse("SLM516",2);
        addCourse("SLM510",2);
        addCourse("CRP502",2);
        addCourse("ANP507",2);
        addCourse("ARD511",3);
        addCourse("ARD510",3);
        addCourse("ARD508",3);
        addCourse("AEM599",4);
        addCourse("CPT514",2);
        addCourse("ARD513",3);
        addCourse("AFM510",3);
        addCourse("AEM512",2);
        addCourse("AEM510",2);
        addCourse("AEM508",2);
        addCourse("AEM506",3);
        addCourse("AEM502",3);
    }

    private void addCourse(String course, int creditUnit){

        // adding data to the arrayList.
        coursesList.add(course);
        creditUnitList.add(Integer.toString(creditUnit));
    }
}