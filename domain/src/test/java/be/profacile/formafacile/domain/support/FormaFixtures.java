package be.profacile.formafacile.domain.support;
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

public final class FormaFixtures {
 private FormaFixtures() {}
 public static final Class<?>[] ENTITES={Categorie.class,Formateur.class,Administrateur.class,Formation.class,Module.class,DemandeInscription.class,NoteInterne.class,MessageContact.class};
 public static Object valeur(Class<?> type,String valeur) {
  return Arrays.stream(type.getEnumConstants()).filter(v -> ((Enum<?>)v).name().equals(valeur)).findFirst()
   .orElseThrow(() -> new AssertionError("Ajouter "+valeur+" dans "+type.getSimpleName()));
 }
 public static Map<String,Object> creer() throws Exception {
  Map<String,Object> objets=new LinkedHashMap<>();
 objets.put("Categorie",nouveau(Categorie.class,Map.ofEntries(Map.entry("libelle","Developpement"))));
objets.put("Formateur",nouveau(Formateur.class,Map.ofEntries(Map.entry("nom","Exemple"),
Map.entry("prenom","Alice"),
Map.entry("email","trainer@example.test"),
Map.entry("bio","Formatrice"))));
objets.put("Administrateur",nouveau(Administrateur.class,Map.ofEntries(Map.entry("email","admin@example.test"),
Map.entry("motDePasse","hash-fictif"),
Map.entry("nom","Admin"))));
objets.put("Formation",nouveau(Formation.class,Map.ofEntries(Map.entry("titre","Java"),
Map.entry("slug","java-pratique"),
Map.entry("descriptionCourte","Initiation"),
Map.entry("descriptionDetaillee","Programme"),
Map.entry("niveau",valeur(Niveau.class,"DEBUTANT")),
Map.entry("dureeHeures",24),
Map.entry("format",valeur(Format.class,"EN_LIGNE")),
Map.entry("prixIndicatif",new BigDecimal("250.00")),
Map.entry("prerequis","Aucun"),
Map.entry("objectifs","Apprendre"),
Map.entry("publicCible","Debutants"),
Map.entry("statut",valeur(StatutPublication.class,"PUBLIEE")),
Map.entry("categorie",objets.get("Categorie")),
Map.entry("formateur",objets.get("Formateur")))));
objets.put("Module",nouveau(Module.class,Map.ofEntries(Map.entry("ordre",1),
Map.entry("titre","Introduction"),
Map.entry("description","Variables"),
Map.entry("dureeHeures",4),
Map.entry("formation",objets.get("Formation")))));
objets.put("DemandeInscription",nouveau(DemandeInscription.class,Map.ofEntries(Map.entry("nom","Exemple"),
Map.entry("prenom","Bob"),
Map.entry("email","learner@example.test"),
Map.entry("telephone","000000000"),
Map.entry("entreprise","Entreprise-fictive"),
Map.entry("nbParticipants",6),
Map.entry("formatSouhaite",valeur(Format.class,"INTRA_ENTREPRISE")),
Map.entry("periodeSouhaitee","Printemps"),
Map.entry("message","Demande-fictive"),
Map.entry("consentementRgpd",true),
Map.entry("dateDemande",LocalDateTime.of(2026,11,20,9,0)),
Map.entry("statut",valeur(StatutDemande.class,"NOUVELLE")),
Map.entry("formation",objets.get("Formation")))));
objets.put("NoteInterne",nouveau(NoteInterne.class,Map.ofEntries(Map.entry("contenu","Rappeler"),
Map.entry("dateCreation",LocalDateTime.of(2026,11,20,9,0)),
Map.entry("demande",objets.get("DemandeInscription")),
Map.entry("administrateur",objets.get("Administrateur")))));
objets.put("MessageContact",nouveau(MessageContact.class,Map.ofEntries(Map.entry("nom","Visiteur"),
Map.entry("email","visitor@example.test"),
Map.entry("sujet","Question"),
Map.entry("message","Information"),
Map.entry("dateEnvoi",LocalDateTime.of(2026,11,20,9,0)),
Map.entry("traite",false))));
 return objets;
 }
}
