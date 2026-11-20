package be.profacile.formafacile.domain.repository;
import be.profacile.formafacile.domain.model.*;
import be.profacile.formafacile.domain.model.Module;
import be.profacile.formafacile.domain.model.Format;
import be.profacile.formafacile.domain.repository.*;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static be.profacile.formafacile.domain.support.JpaExerciseSupport.*;

import be.profacile.formafacile.domain.support.*;
import org.junit.jupiter.api.Test;
class FormationRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierFormation();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierFormation();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(FormationRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Formation")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Formation"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Formation"),"titre"),lire(relu,"titre"),"Valeur persistée de titre");
assertEquals(lire(objets.get("Formation"),"slug"),lire(relu,"slug"),"Valeur persistée de slug");
assertEquals(lire(objets.get("Formation"),"descriptionCourte"),lire(relu,"descriptionCourte"),"Valeur persistée de descriptionCourte");
assertEquals(lire(objets.get("Formation"),"descriptionDetaillee"),lire(relu,"descriptionDetaillee"),"Valeur persistée de descriptionDetaillee");
assertEquals(lire(objets.get("Formation"),"niveau"),lire(relu,"niveau"),"Valeur persistée de niveau");
assertEquals(lire(objets.get("Formation"),"dureeHeures"),lire(relu,"dureeHeures"),"Valeur persistée de dureeHeures");
assertEquals(lire(objets.get("Formation"),"format"),lire(relu,"format"),"Valeur persistée de format");
assertEquals(lire(objets.get("Formation"),"prixIndicatif"),lire(relu,"prixIndicatif"),"Valeur persistée de prixIndicatif");
assertEquals(lire(objets.get("Formation"),"prerequis"),lire(relu,"prerequis"),"Valeur persistée de prerequis");
assertEquals(lire(objets.get("Formation"),"objectifs"),lire(relu,"objectifs"),"Valeur persistée de objectifs");
assertEquals(lire(objets.get("Formation"),"publicCible"),lire(relu,"publicCible"),"Valeur persistée de publicCible");
assertEquals(lire(objets.get("Formation"),"statut"),lire(relu,"statut"),"Valeur persistée de statut");
assertEquals(lire(objets.get("Categorie"),"id"),lire(lire(relu,"categorie"),"id"));
assertEquals(lire(objets.get("Formateur"),"id"),lire(lire(relu,"formateur"),"id"));
   Object resultat=rechercher(repository,"findByStatut",StatutPublication.class,FormaFixtures.valeur(StatutPublication.class,"PUBLIEE"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
