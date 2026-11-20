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
class MessageContactRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierMessageContact();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierMessageContact();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(MessageContactRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("MessageContact")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("MessageContact"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("MessageContact"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");
assertEquals(lire(objets.get("MessageContact"),"email"),lire(relu,"email"),"Valeur persistée de email");
assertEquals(lire(objets.get("MessageContact"),"sujet"),lire(relu,"sujet"),"Valeur persistée de sujet");
assertEquals(lire(objets.get("MessageContact"),"message"),lire(relu,"message"),"Valeur persistée de message");
assertEquals(lire(objets.get("MessageContact"),"dateEnvoi"),lire(relu,"dateEnvoi"),"Valeur persistée de dateEnvoi");
assertEquals(lire(objets.get("MessageContact"),"traite"),lire(relu,"traite"),"Valeur persistée de traite");
assertNull(lire(relu,"administrateur"));
   Object resultat=rechercherSansParametre(repository,"findByTraiteFalse");
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
