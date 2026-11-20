# FormaFacile — PROFACILE SRL

Backend pédagogique du service de formations proposé depuis profacile.be.
Java 25, Spring Boot 4.1.2, Maven Wrapper 3.9.16.

## Utilisation
Installer JDK 25 et définir JAVA_HOME. Le wrapper fournit Maven.
- Compiler : .\mvnw.cmd -DskipTests package
- Vérifier le démarrage : .\mvnw.cmd -pl bootstrap -am test -Dtest=FormaFacileApplicationTest -Dsurefire.failIfNoSpecifiedTests=false
- Exercices : .\mvnw.cmd -pl domain test
- Validation finale : .\mvnw.cmd verify
Sous Linux, utiliser sh mvnw.

Le squelette compile et démarre sans serveur HTTP ni base de production.
Les 18 tests du domaine sont volontairement rouges tant que les classes sont vides.
[Consignes complètes, champs, relations, répartition et tests](docs/sprint-2.md).

## Structure
- domain/model : entités et enums à compléter.
- domain/repository : interfaces Spring Data à compléter.
- domain/service : emplacements réservés au futur sprint services.
- rest/dto/request et response : emplacements réservés à la future API.
- bootstrap : démarrage Spring Boot.
- domain/src/test : tests déjà implémentés, Hibernate/H2 et infrastructure fournis.

main et develop partent du même commit. Branches de ticket depuis develop, puis PR vers develop.
Les workflows GitHub séparent le socle technique (vert) des exercices (rouges au départ).
CODEOWNERS identifie les tests/configurations à faire réviser ; la protection bloquante des branches
reste à configurer selon les capacités du compte GitHub. Le partage aux étudiants sera réalisé séparément.
Utiliser uniquement des données fictives, aucun secret.
