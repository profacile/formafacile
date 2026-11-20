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
class FormateurRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierFormateur();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierFormateur();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(FormateurRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Formateur")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Formateur"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Formateur"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");
assertEquals(lire(objets.get("Formateur"),"prenom"),lire(relu,"prenom"),"Valeur persistée de prenom");
assertEquals(lire(objets.get("Formateur"),"email"),lire(relu,"email"),"Valeur persistée de email");
assertEquals(lire(objets.get("Formateur"),"bio"),lire(relu,"bio"),"Valeur persistée de bio");

   Object resultat=rechercher(repository,"findByEmail",String.class,"trainer@example.test");
   assertEquals(id,lire(((Optional<?>) resultat).orElseThrow(),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
}
