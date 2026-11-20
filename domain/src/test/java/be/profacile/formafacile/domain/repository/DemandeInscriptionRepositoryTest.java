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
class DemandeInscriptionRepositoryTest {
 @Test void respecterLeDiagrammeJpa() {FormaContract.verifierDemandeInscription();}
 @Test void sauvegarderRelireEtRechercher() throws Exception {
  FormaContract.verifierDemandeInscription();
  FormaContract.enumerations();
  try(var base=new BaseJpa(FormaFixtures.ENTITES)) {
   var objets=FormaFixtures.creer();
   var repository=base.creerRepository(DemandeInscriptionRepository.class);
   base.entityManager.getTransaction().begin();
   for(var entree:objets.entrySet()) {
    if(entree.getKey().equals("DemandeInscription")) repository.saveAndFlush(entree.getValue());
    else base.entityManager.persist(entree.getValue());
   }
   base.entityManager.flush();
   Long id=(Long)lire(objets.get("DemandeInscription"),"id");assertNotNull(id);
   base.entityManager.clear();
   Object relu=repository.findById(id).orElseThrow();
   assertEquals(lire(objets.get("DemandeInscription"),"nom"),lire(relu,"nom"),"Valeur persistée de nom");
assertEquals(lire(objets.get("DemandeInscription"),"prenom"),lire(relu,"prenom"),"Valeur persistée de prenom");
assertEquals(lire(objets.get("DemandeInscription"),"email"),lire(relu,"email"),"Valeur persistée de email");
assertEquals(lire(objets.get("DemandeInscription"),"telephone"),lire(relu,"telephone"),"Valeur persistée de telephone");
assertEquals(lire(objets.get("DemandeInscription"),"entreprise"),lire(relu,"entreprise"),"Valeur persistée de entreprise");
assertEquals(lire(objets.get("DemandeInscription"),"nbParticipants"),lire(relu,"nbParticipants"),"Valeur persistée de nbParticipants");
assertEquals(lire(objets.get("DemandeInscription"),"formatSouhaite"),lire(relu,"formatSouhaite"),"Valeur persistée de formatSouhaite");
assertEquals(lire(objets.get("DemandeInscription"),"periodeSouhaitee"),lire(relu,"periodeSouhaitee"),"Valeur persistée de periodeSouhaitee");
assertEquals(lire(objets.get("DemandeInscription"),"message"),lire(relu,"message"),"Valeur persistée de message");
assertEquals(lire(objets.get("DemandeInscription"),"consentementRgpd"),lire(relu,"consentementRgpd"),"Valeur persistée de consentementRgpd");
assertEquals(lire(objets.get("DemandeInscription"),"dateDemande"),lire(relu,"dateDemande"),"Valeur persistée de dateDemande");
assertEquals(lire(objets.get("DemandeInscription"),"statut"),lire(relu,"statut"),"Valeur persistée de statut");
assertEquals(lire(objets.get("Formation"),"id"),lire(lire(relu,"formation"),"id"));
   Object resultat=rechercher(repository,"findByStatut",StatutDemande.class,FormaFixtures.valeur(StatutDemande.class,"NOUVELLE"));
   assertEquals(List.of(id), ((List<?>) resultat).stream().map(o -> assertDoesNotThrow(() -> lire(o,"id"))).toList());
   base.entityManager.getTransaction().commit();
  }
 }
}
