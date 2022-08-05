package com.university.nounfaculties;

import java.util.ArrayList;

public class Faculties {
    public static ArrayList<String> sciences,arts,socialSciences,managementSciences,healthSciences,law,agriculturalSciences,education;

    public Faculties(){
        sciences = new ArrayList<>(); arts = new ArrayList<>(); socialSciences = new ArrayList<>(); managementSciences = new ArrayList<>();
        healthSciences = new ArrayList<>(); law = new ArrayList<>(); agriculturalSciences = new ArrayList<>(); education = new ArrayList<>();

        // agricultural departments
        addAgriculturalSciences("B.Sc Agricultural Extension and Management");
        addAgriculturalSciences("B.Agric Agricultural Extension and Rural Development");
        addAgriculturalSciences("B.Agric Crop Science");
        addAgriculturalSciences("B.Agric Soil and Land Resources Management");
        addAgriculturalSciences("B.Agric Agricultural Economics and Agro-Business");
        addAgriculturalSciences("B.Agric (Aquaculture and Fisheries Management Option)");
        addAgriculturalSciences("B.Agric Animal Science");
        addAgriculturalSciences("B.Sc. Hotel and Catering Management");

        // add arts
        addArts("B.A. Islamic Studies");
        addArts("B.A. English");
        addArts("B.A. French");
        addArts("B.A. Christian Religious Studies");

        // add education
        addEducation("B.Sc.(ED) Agricultural Science");
        addEducation("B.Sc.(ED) Biology");
        addEducation("B.Sc.(ED) Business Education");
        addEducation("B.A.(ED) Early Childhood Education");
        addEducation("B.Sc.(ED) Chemistry");
        addEducation("B.Sc.(ED) Computer Science");
        addEducation("B.A.(ED) English");
        addEducation("B.A.(ED) French");
        addEducation("B.Sc.(ED) Integrated Science");
        addEducation("B.Sc.(ED) Mathematics");
        addEducation("B.Sc.(ED) Physics");
        addEducation(".A.(ED) Primary Education");

        // add health science
        addHealthSciences("B.Sc. Environmental Health Science");
        addHealthSciences("B.NSc. Nursing (Old)");
        addHealthSciences("B.Sc. Public Health");
        addHealthSciences("B.NSc. Nursing (New)");

        //add law
        addLaw("LLB Law");

        // add management
        addManagementSciences("B.Sc. Marketing");
        addManagementSciences("B.Sc. Public Administration");
        addManagementSciences("B.Sc. Business Administration");
        addManagementSciences("B.Sc. Accounting");
        addManagementSciences("B.Sc. Entrepreneurship");
        addManagementSciences("B.Sc. Cooperative and Rural Development");

        // add sciences
        addSciences("B.Sc. Biology");
        addSciences("B.Sc. Chemistry");
        addSciences("B.Sc. Computer Science");
        addSciences("B.Sc. DATA MANAGEMENT");
        addSciences("B.Sc. Physics");
        addSciences("B.Sc. Mathematics");
        addSciences("B.Sc. Maths and Computer Science");
        addSciences("B.Sc. Environmental Management and Toxicology");
        addSciences("B.Sc. Information Technology");

        // add social sciences
        addSocialSciences("B.Sc. Tourism Studies");
        addSocialSciences("B.Sc. Economics");
        addSocialSciences("B.Sc. Political Science");
        addSocialSciences("B.Sc. Mass Communication");
        addSocialSciences("B.Sc. Peace Studies and Conflict Resolution");
        addSocialSciences("B.Sc. Criminology and Security Studies");

    }
    public void addSciences(String department){
        sciences.add(department);
    }
    public void addArts(String department){
        arts.add(department);
    }
    public void addSocialSciences(String department){
        socialSciences.add(department);
    }
    public void addManagementSciences(String department){
        managementSciences.add(department);
    }
    public void addHealthSciences(String department){
        healthSciences.add(department);
    }
    public void addLaw(String department){
        law.add(department);
    }
    public void addAgriculturalSciences(String department){
        agriculturalSciences.add(department);
    }
    public void addEducation(String department){
        education.add(department);
    }

}
