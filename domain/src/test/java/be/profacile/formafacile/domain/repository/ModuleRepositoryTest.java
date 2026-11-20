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
class ModuleRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierModule();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierModule();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(ModuleRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("Module")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("Module"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("Module"),"ordre"),lire(relu,"ordre"),"Valeur persistée de ordre");
assertEquals(lire(objets.get("Module"),"titre"),lire(relu,"titre"),"Valeur persistée de titre");
assertEquals(lire(objets.get("Module"),"description"),lire(relu,"description"),"Valeur persistée de description");
assertEquals(lire(objets.get("Module"),"dureeHeures"),lire(relu,"dureeHeures"),"Valeur persistée de dureeHeures");
assertEquals(lire(objets.get("Formation"),"id"),lire(lire(relu,"formation"),"id"));
   Object resultat=rechercher(repository,"findByFormationIdOrderByOrdreAsc",Long.class,lire(objets.get("Formation"),"id"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
