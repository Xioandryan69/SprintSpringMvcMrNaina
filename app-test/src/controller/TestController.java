package controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.WebApi;
import framework.util.ModelView;

import framework.annotation.UrlMapping;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.example.entreprise.service.EmployeService;
import com.example.entreprise.entity.*;

@Controller
public class TestController { 

    @Get("/api/status")
    @WebApi
    public Map<String, Object> getStatusApi() {
        Map<String, Object> status = new HashMap<>();
        status.put("code", 200);
        status.put("message", "API Fonctionnelle");
        return status;
    }

  
    @Get("/api/employes")
    @WebApi
    public ModelView getEmployesJson() {
        ModelView mv = new ModelView();
        mv.addItem("titre", "Liste API");
        mv.addItem("total", 2);
        return mv;
    }

    @Get("/employe-list")
    public ModelView listEmploye() {
        ModelView mv = new ModelView("employe-list.jsp");
        
        mv.addItem("titre", "Liste des Employés");
        mv.addItem("message", "Bienvenue sur la page de gestion des employés !");
        List<String> employes = EmployeService.listEmployes();
        mv.addItem("employes", employes);
        
        return mv;
    }

    @Get("/employe-add")
    public void addEmploye() {
    }

    @Get("/andrana1")
    public void andrana() {
    }
    @UrlMapping(value= "/andrana1",method ="POST")
    public void andrana3() {
    }


    @Get("/employe-form")
    public ModelView showForm() {
        return new ModelView("employe-form.jsp");
    }

    @Get("/employe-form-objet")
    public ModelView showForm1() {
        return new ModelView("employe-form-objet.jsp");
    }



    @Get("/employe-form-vector-map")
    public ModelView showForm2() {
        return new ModelView("employe-form-vector-map.jsp");
    }


    @UrlMapping(value = "/employe-save", method = "POST")
    public ModelView save(String nom, int age, double salaire, boolean actif) {
        ModelView mv = new ModelView("employe-result.jsp");
        mv.addItem("nom", nom);
        mv.addItem("age", age);
        mv.addItem("salaire", salaire);
        mv.addItem("actif", actif);
        return mv;
    }


    @UrlMapping(value = "/employe-save-objet", method = "POST")
    public ModelView saveEmploye(Employe employe, List<String> competences, Map<String, Object> allParams) {
        ModelView mv = new ModelView("employe-result1.jsp");
        // L'objet 'employe' est automatiquement instancié et rempli à partir des champs du formulaire
        mv.addItem("employe", employe);
        mv.addItem("competences", competences);
        return mv;
    }

    @UrlMapping(value = "/employe-save-vector-map", method = "POST")
    public ModelView saveVectorMap(java.util.Vector<String> competencesVector, Map<String, Object> allParams) {
        ModelView mv = new ModelView("employe-result2.jsp");
        mv.addItem("competencesVector", competencesVector);
        mv.addItem("metaData", allParams);
        return mv;
    }
    /*
    curl -X POST http://localhost:8080/employe-save-objet \
     -d "nom=Rabe" \
     -d "prenom=Jean" \
     -d "email=jean.rabe@example.com" \
     -d "poste=Développeur" \
     -d "salaire=2500.00" \
     -d "competences=Java" \
     -d "competences=Spring" \
     -d "competences=SQL"
     
     */


}