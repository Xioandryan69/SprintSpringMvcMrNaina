

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

@Controller
public class TestController { 

    // Exemple 1 : Méthode renvoyant un objet/map en JSON
    @Get("/api/status")
    @WebApi
    public Map<String, Object> getStatusApi() {
        Map<String, Object> status = new HashMap<>();
        status.put("code", 200);
        status.put("message", "API Fonctionnelle");
        return status;
    }

    // Exemple 2 : Méthode renvoyant un ModelView transformé automatiquement en JSON
    @Get("/api/employes")
    //@WebApi
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



}