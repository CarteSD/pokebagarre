/**
 * @author Estéban DESESSARD
 * @date 20/02/2025
 * @description Réalisation de test pour la méthode estVainqueurContre() de la classe Pokemon
 *  - Test de la méthode estVainqueurContre() avec des pokemons ayant des attaques et des défenses différentes
 *  - Test de la méthode estVainqueurContre() avec des pokemons ayant des attaques identiques et des défenses différentes
 *  - Test de la méthode estVainqueurContre() avec des pokemons ayant des attaques et des défenses identiques
 */


package com.montaury.pokebagarre.metier;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    private String POKEMON1_WIN = "Le premier pokemon a gagné";
    private String POKEMON2_WIN = "Le deuxième pokemon a gagné";

    @Test
    void different_attack_pokemon1_better_attack_win() {
        // Given
        Pokemon pokemon1 = new Pokemon("Premier pokemon", null, new Stats(150, 150));
        Pokemon pokemon2 = new Pokemon("Deuxième pokemon", null, new Stats(100, 100));

        // When
        boolean resultat = pokemon1.estVainqueurContre(pokemon2);

        // Then
        assertThat(resultat).isEqualTo(true);
    }

    @Test
    void different_attack_pokemon2_better_attack_win() {
        Pokemon pokemon1 = new Pokemon("Premier pokemon", null, new Stats(100, 100));
        Pokemon pokemon2 = new Pokemon("Deuxième pokemon", null, new Stats(150, 150));
        final boolean resultat = pokemon1.estVainqueurContre(pokemon2);
        assertFalse(resultat, POKEMON2_WIN);
    }

    @Test
    void same_attack_different_defense_pokemon1_better_defense_win() {
        Pokemon pokemon1 = new Pokemon("Premier pokemon", null, new Stats(150, 150));
        Pokemon pokemon2 = new Pokemon("Deuxième pokemon", null, new Stats(150, 100));
        final boolean resultat = pokemon1.estVainqueurContre(pokemon2);
        assertTrue(resultat, POKEMON1_WIN);
    }

    @Test
    void same_attack_different_defense_pokemon2_better_defense_win() {
        Pokemon pokemon1 = new Pokemon("Premier pokemon", null, new Stats(150, 150));
        Pokemon pokemon2 = new Pokemon("Deuxième pokemon", null, new Stats(150, 200));
        final boolean resultat = pokemon1.estVainqueurContre(pokemon2);
        assertFalse(resultat, POKEMON2_WIN);
    }

    @Test
    void same_attack_same_defense_pokemon1_win() {
        Pokemon pokemon1 = new Pokemon("Premier pokemon", null, new Stats(150, 150));
        Pokemon pokemon2 = new Pokemon("Deuxième pokemon", null, new Stats(150, 150));
        final boolean resultat = pokemon1.estVainqueurContre(pokemon2);
        assertTrue(resultat, POKEMON2_WIN);
    }
}