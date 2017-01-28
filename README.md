# voteSim

## INTRODUCTION

Projet ayant pour objectif de simuler différents système de vote et de comparer les résultats tout en appliquant autant que faire se peut les bonnes pratiques de la POO :
  * autant d'Objet immutable que possible
  * Single Responsability principle
  * Principe de substitution de Liskov
  * Inversion des dépendances
  * Bonne couverture par les TU et utilisation de tests par mutation
  
  
Projet inspiré du papier de Warren D. Smith comparant différent système de vote et réalisé en C.

L'un des objectifs de ce projet est de comparer le vote par valeur avec le Jugement Majoritaire.


## SYSTEMES DE VOTE SUPPORTES

A terme, voici les systèmes de vote devant être supportées :
1. Vote par valeur (0 à 2) - Range voting - RV
2. Vote par valeur (0 à 5) - Range voting - RV
3. Jugement Majoritaire - Majority Judgment - MJ
2. Vote uninominal à 1 tour - First past the post - plurality - PL1
3. Vote uninominal à 2 tours - 2-round plurality - PL2
4. Vote uninominal à 4 tours - 4-round plurality - PL4
5. Méthode Borda - Borda count - BC
6. Anti-plurality - APL
7. Electeur au hasard choisi le gagnant - Random Ballot - RB
8. Candidat au hasard - Random Winner - RW
9. Candidat avec la pire utilité choisi arbitrairement - Worst-summed-utility Winner - WW
10. Coombs - Coombs - CO
11. Bucklin - Bucklin - BUC
12. Méthode de Copeland - Copeland's method - CLM
13. Vote de Dabagh - Dabagh - DBH
14. Black (si pas de gagnant de Condorcet, on utilise la Méthode de Borda) - Black - BL
15. Méthode de Baldwin - Baldwin's method - BWM
16. Vote alternatif - Instant Run-off voting - IRV
17. Schulze - Schulze - SU


## VOTEUR HONNÊTE ET UTILITE
L'utilité de chaque votant est un nombre réél entre 0 et 1 indiquant la satisfaction d'un votant pour un candidat. La valeur 1 indiquant
une utilité parfaite et donc une satisfaction parfaite du votant pour le candidat indiqué.
Le meilleur candidat d'une éléction est donc celui qui maximise l'utilité totale.
Si chaque votant étaint intéressé par l'avis de ses pairs, ce candidat serait généralement celui qui est élu.
Un voteur honnête votera toujours préférentiellement pour le candidat qui maximise son utilité puis descendra dans l'ordre.


## COPYRIGHT

(c) 2017 Ronan Dhellemmes
