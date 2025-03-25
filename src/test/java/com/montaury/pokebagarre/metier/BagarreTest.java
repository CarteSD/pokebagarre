package com.montaury.pokebagarre.metier;

import com.montaury.pokebagarre.erreurs.ErreurMemePokemon;
import com.montaury.pokebagarre.erreurs.ErreurPokemonNonRenseigne;
import com.montaury.pokebagarre.erreurs.ErreurRecuperationPokemon;
import com.montaury.pokebagarre.webapi.PokeBuildApi;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

class BagarreTest {

    @Test
    void exception_ErreurPokemonNonRenseigne_si_pokemon1_est_vide() {
        // Given
        Bagarre bagarre = new Bagarre();
        String nomPremierPokemon = "";
        String nomSecondPokemon = "Deuxième pokemon";

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(nomPremierPokemon, nomSecondPokemon));

        // Then
        assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void exception_ErreurPokemonNonRenseigne_si_pokemon2_est_vide() {
        // Given
        Bagarre bagarre = new Bagarre();
        String nomPremierPokemon = "Premier pokemon";
        String nomSecondPokemon = "";

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(nomPremierPokemon, nomSecondPokemon));

        // Then
        assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    @Test
    void exception_ErreurMemePokemon_si_pokemon1_et_pokemon2_identiques() {
        // Given
        Bagarre bagarre = new Bagarre();
        String nomPremierPokemon = "Premier pokemon";
        String nomSecondPokemon = "Premier pokemon";

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(nomPremierPokemon, nomSecondPokemon));

        // Then
        assertThat(thrown).isInstanceOf(ErreurMemePokemon.class).hasMessage("Impossible de faire se bagarrer un pokemon avec lui-meme");
    }

    @Test
    void exception_ErreurPokemonNonRenseigne_si_pokemon1_est_null() {
        // Given
        Bagarre bagarre = new Bagarre();
        String nomPremierPokemon = null;
        String nomSecondPokemon = "Deuxième pokemon";

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(nomPremierPokemon, nomSecondPokemon));

        // Then
        assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le premier pokemon n'est pas renseigne");
    }

    @Test
    void exception_ErreurPokemonNonRenseigne_si_pokemon2_est_null() {
        // Given
        Bagarre bagarre = new Bagarre();
        String nomPremierPokemon = "Premier pokemon";
        String nomSecondPokemon = null;

        // When
        Throwable thrown = catchThrowable(() -> bagarre.demarrer(nomPremierPokemon, nomSecondPokemon));

        // Then
        assertThat(thrown).isInstanceOf(ErreurPokemonNonRenseigne.class).hasMessage("Le second pokemon n'est pas renseigne");
    }

    private PokeBuildApi fakeApi = Mockito.mock(PokeBuildApi.class);

    @Test
    void exception_ErreurRecuperationPokemon_si_pokemon1_existe_pas() {
        // Given
        Bagarre bagarre = new Bagarre(fakeApi);
        String nomPremierPokemon = "Pokemon inexistant";
        String nomSecondPokemon = "Pikachu";
        Mockito.when(fakeApi.recupererParNom(nomPremierPokemon)).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("Pokemon inexistant")));
        Mockito.when(fakeApi.recupererParNom(nomSecondPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Pikachu", "url", new Stats(100, 100))));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer(nomPremierPokemon, nomSecondPokemon);

        // Then
        assertThat(futurVainqueur)
            .failsWithin(Duration.ofSeconds(2))
            .withThrowableOfType(ExecutionException.class)
            .havingCause()
            .isInstanceOf(ErreurRecuperationPokemon.class)
            .withMessage("Impossible de recuperer les details sur 'Pokemon inexistant'");
    }

    @Test
    void exception_ErreurRecuperationPokemon_si_pokemon2_existe_pas() {
        // Given
        Bagarre bagarre = new Bagarre(fakeApi);
        String nomPremierPokemon = "Pikachu";
        String nomSecondPokemon = "Pokemon inexistant";
        Mockito.when(fakeApi.recupererParNom(nomPremierPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Pikachu", "url", new Stats(100, 100))));
        Mockito.when(fakeApi.recupererParNom(nomSecondPokemon)).thenReturn(CompletableFuture.failedFuture(new ErreurRecuperationPokemon("Pokemon inexistant")));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer(nomPremierPokemon, nomSecondPokemon);

        // Then
        assertThat(futurVainqueur)
                .failsWithin(Duration.ofSeconds(2))
                .withThrowableOfType(ExecutionException.class)
                .havingCause()
                .isInstanceOf(ErreurRecuperationPokemon.class)
                .withMessage("Impossible de recuperer les details sur 'Pokemon inexistant'");
    }

    @Test
    void retourne_pokemon1_si_pokemon1_est_meilleur() {
        // Given
        Bagarre bagarre = new Bagarre(fakeApi);
        String nomPremierPokemon = "Pikachu";
        String nomSecondPokemon = "Bulbizarre";
        Mockito.when(fakeApi.recupererParNom(nomPremierPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Pikachu", "url", new Stats(100, 100))));
        Mockito.when(fakeApi.recupererParNom(nomSecondPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Bulbizarre", "url", new Stats(50, 50))));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer(nomPremierPokemon, nomSecondPokemon);

        // Then
        assertThat(futurVainqueur)
            .succeedsWithin(Duration.ofSeconds(2))
            .satisfies (pokemon -> {
                assertThat(pokemon.getNom())
                    .isEqualTo("Pikachu"); // autres assertions...
                }
            ) ;

    }

    @Test
    void retourne_pokemon2_si_pokemon2_est_meilleur() {
        // Given
        Bagarre bagarre = new Bagarre(fakeApi);
        String nomPremierPokemon = "Pikachu";
        String nomSecondPokemon = "Bulbizarre";
        Mockito.when(fakeApi.recupererParNom(nomPremierPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Pikachu", "url", new Stats(50, 50))));
        Mockito.when(fakeApi.recupererParNom(nomSecondPokemon)).thenReturn(CompletableFuture.completedFuture(new Pokemon("Bulbizarre", "url", new Stats(100, 100))));

        // When
        CompletableFuture<Pokemon> futurVainqueur = bagarre.demarrer(nomPremierPokemon, nomSecondPokemon);

        // Then
        assertThat(futurVainqueur)
                .succeedsWithin(Duration.ofSeconds(2))
                .satisfies (pokemon -> {
                            assertThat(pokemon.getNom())
                                    .isEqualTo("Bulbizarre"); // autres assertions...
                        }
                ) ;

    }
}