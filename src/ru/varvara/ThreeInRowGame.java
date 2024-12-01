package ru.varvara;

import javax.swing.*;

/**
 * Главный класс игры "Три в ряд".
 * Этот класс содержит метод main, который запускает приложение.
 */
public class ThreeInRowGame {

    /**
     * Точка входа в приложение.
     * Здесь создается и отображается пользовательский интерфейс игры.
     * @param args Массив аргументов командной строки (не используется).
     */
    public static void main(String[] args) {

        // Используется invokeLater для того, чтобы создать и отобразить GUI в потоке событий Swing.
        SwingUtilities.invokeLater(() -> {
            // Создаем экземпляр класса GameUI, который отвечает за интерфейс игры.
            GameUI gameUI = new GameUI();

            // Делаем интерфейс видимым, чтобы игрок мог начать игру.
            gameUI.setVisible(true);
        });
    }
}