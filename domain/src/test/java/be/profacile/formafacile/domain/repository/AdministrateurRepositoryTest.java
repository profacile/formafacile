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
class AdministrateurRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierAdministrateur();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierAdministrateur();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(AdministrateurRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Administrateur")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Administrateur"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Administrateur"),"email"),lire(relu,"email"),"Valeur persistée de email");
assertEquals(lire(objets.get("Administrateur"),"motDePasse"),lire(relu,"motDePasse"),"Valeur persistée de motDePasse");
assertEquals(lire(objets.get("Administrateur"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");

   Object resultat=rechercher(repository,"findByEmail",String.class,"admin@example.test");
   assertEquals(id,lire(((Optional<?>) resultat).orElseThrow(),"id"));
   base.entityManager.getTransaction().commit();
  }
 }
}
