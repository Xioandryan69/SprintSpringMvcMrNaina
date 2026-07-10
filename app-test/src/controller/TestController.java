package controller;

import framework.annotation.Controller;
import framework.annotation.Get;
import framework.annotation.UrlMapping;
import framework.util.ModelView;

@Controller
public class TestController {
    @Get("/employe-list")
    public ModelView listEmploye() {
        ModelView mv = new ModelView("employe-list.jsp");
        
        // Ajout de données de test à transmettre à la JSP
        mv.addItem("titre", "Liste des Employés");
        mv.addItem("message", "Bienvenue sur la page de gestion des employés !");
        
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