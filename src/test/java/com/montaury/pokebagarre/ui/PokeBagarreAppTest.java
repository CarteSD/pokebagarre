package com.montaury.pokebagarre.ui;

import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(ApplicationExtension.class)
class PokeBagarreAppTest {

    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1 = "#nomPokemon1";
    private static final String IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2 = "#nomPokemon2";
    private static final String IDENTIFIANT_BOUTON_BAGARRE = ".button";

    @Start
    private void start(Stage stage) {
        new PokeBagarreApp().start(stage);
    }

    @Test
    void devrait_afficher_vainqueur_si_pokemon_existant(FxRobot robot) {
        // Saisie des noms de Pokémon
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Dracaufeu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du résultat
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String resultat = getResultatBagarre(robot);
            assertThat(resultat).isNotEmpty();
            assertThat(resultat).contains("vainqueur");
        });
    }

    @Test
    void devrait_afficher_erreur_impossible_recuperer_details_si_pokemon_inexsitant(FxRobot robot) {
        // Saisie d'un Pokémon invalide
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("PokemonInexistant");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Pikachu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du message d'erreur
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String messageErreur = getMessageErreur(robot);
            assertThat(messageErreur).isNotEmpty();
            assertThat(messageErreur).isEqualTo("Erreur: Impossible de recuperer les details sur 'PokemonInexistant'");
        });
    }

    @Test
    void devrait_afficher_erreur_non_renseigne_si_premier_pokemon_non_saisi(FxRobot robot) {
        // Saisie d'un seul Pokémon
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Pikachu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du message d'erreur
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String messageErreur = getMessageErreur(robot);
            assertThat(messageErreur).isNotEmpty();
            assertThat(messageErreur).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne");
        });
    }

    @Test
    void devrait_afficher_erreur_non_renseigne_si_second_pokemon_non_saisi(FxRobot robot) {
        // Saisie d'un seul Pokémon
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du message d'erreur
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String messageErreur = getMessageErreur(robot);
            assertThat(messageErreur).isNotEmpty();
            assertThat(messageErreur).isEqualTo("Erreur: Le second pokemon n'est pas renseigne");
        });
    }

    @Test
    void devrait_afficher_erreur_non_renseigne_si_aucun_pokemon_saisi(FxRobot robot) {
        // Saisie d'un seul Pokémon
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Pikachu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du message d'erreur
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String messageErreur = getMessageErreur(robot);
            assertThat(messageErreur).isNotEmpty();
            assertThat(messageErreur).isEqualTo("Erreur: Le premier pokemon n'est pas renseigne");
        });
    }

    @Test
    void devrait_afficher_erreur_pokemon_identiques_si_meme_pokemon(FxRobot robot) {
        // Saisie d'un seul Pokémon
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_1).write("Pikachu");
        robot.clickOn(IDENTIFIANT_CHAMP_DE_SAISIE_POKEMON_2).write("Pikachu");

        // Clic sur le bouton de bataille
        robot.clickOn(IDENTIFIANT_BOUTON_BAGARRE);

        // Vérification du message d'erreur
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            String messageErreur = getMessageErreur(robot);
            assertThat(messageErreur).isNotEmpty();
            assertThat(messageErreur).isEqualTo("Erreur: Impossible de faire se bagarrer un pokemon avec lui-meme");
        });
    }

    private static String getResultatBagarre(FxRobot robot) {
        return robot.lookup("#resultatBagarre").queryText().getText();
    }

    private static String getMessageErreur(FxRobot robot) {
        return robot.lookup("#resultatErreur").queryLabeled().getText();
    }
}