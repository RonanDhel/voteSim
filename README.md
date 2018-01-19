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
4. Vote uninominal à 1 tour - First past the post - plurality - PL1
5. Vote uninominal à 2 tours - 2-round plurality - PL2
6. Vote uninominal à 4 tours - 4-round plurality - PL4
7. Méthode Borda - Borda count - BC
8. Anti-plurality - APL
9. Electeur au hasard choisi le gagnant - Random Ballot - RB
10. Candidat au hasard - Random Winner - RW
11. Candidat avec la pire utilité choisi arbitrairement - Worst-summed-utility Winner - WW
12. Coombs - Coombs - CO
13. Bucklin - Bucklin - BUC
14. Méthode de Copeland - Copeland's method - CLM
15. Vote de Dabagh - Dabagh - DBH
16. Black (si pas de gagnant de Condorcet, on utilise la Méthode de Borda) - Black - BL
17. Méthode de Baldwin - Baldwin's method - BWM
18. Vote alternatif - Instant Run-off voting - IRV
19. Schulze - Schulze - SU
20. Scrutin de Condorcet randomisé - Randomized Condorcet System - RCS

## VOTEUR HONNÊTE ET UTILITE
L'utilité de chaque votant est un nombre réél entre 0 et 1 indiquant la satisfaction d'un votant pour un(e) candidat(e). La valeur 1 indiquant une utilité parfaite et donc une "satisfaction" parfaite du votant pour le(la) candidat(e) indiqué(e).  
Le "meilleur(e)" candidat(e) d'une éléction est donc celui (celle) qui maximise l'utilité totale.  
Si chaque votant était intéressé par l'avis de ses pairs, ce(tte) candidat(e) serait généralement celui (celle) qui est élu(e).

Un voteur honnête votera toujours préférentiellement pour le(la) candidat(e) qui maximise son utilité personnelle puis descendra dans l'ordre ; même si leurs candidat(e)s préféré(e)s ont peu de chances de gagner.  
Les voteurs stratégique (ou non-honnête) vote de manière à élire le(la) candidat(e) proche d'eux ayant le plus de chance de gagner. La stratégie adoptée par type de scrutin est détaillée ci-dessous :


## VOTE STRATEGIQUE

TODO

## COPYRIGHT

(c) 2017 Ronan Dhellemmes
