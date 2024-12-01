package ru.varvara;

import javax.swing.*;
import java.awt.*;

/**
 * Класс, представляющий графический интерфейс игры "Три в ряд".
 * Управляет игровым полем, взаимодействием с пользователем, обработкой совпадений и обновлением UI.
 */
public class GameUI extends JFrame {
    // Поле для хранения состояния игрового поля
    private final GameBoard gameBoard;
    // Двумерный массив для хранения клеток в UI
    private final GameTile[][] tiles;
    // Метка для отображения счета
    private final JLabel scoreLabel;
    // Переменная для хранения первой выбранной клетки при обмене
    private GameTile firstTile = null;
    // Счет игры
    private int score = 0;
    // Таймер для обработки совпадений
    private Timer gameTimer;

    /**
     * Конструктор UI, инициализирует окна, компоненты и события.
     */
    public GameUI() {
        setTitle("Три в ряд");  // Устанавливаем заголовок окна
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Закрытие приложения при закрытии окна
        setSize(500, 500);  // Размер окна
        setLayout(new BorderLayout());  // Устанавливаем компоновку

        // Инициализация игрового поля и клеток
        gameBoard = new GameBoard();
        tiles = new GameTile[8][8];

        // Панель для сетки 8x8
        JPanel gridPanel = new JPanel(new GridLayout(8, 8));
        // Создаем и добавляем клетки в панель
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new GameTile(gameBoard.getType(i, j));  // Создание клетки с заданным типом
                int x = i, y = j;
                // Добавление обработчика события для каждой клетки
                tiles[i][j].addActionListener(e -> onTileClick(x, y));
                gridPanel.add(tiles[i][j]);  // Добавляем клетку в панель
            }
        }
        add(gridPanel, BorderLayout.CENTER);  // Добавляем панель с клетками в центр окна

        // Метка для отображения счета
        scoreLabel = new JLabel("Счет: 0");
        add(scoreLabel, BorderLayout.NORTH);  // Добавляем метку в верхнюю часть окна

        // Проверка на наличие совпадений при загрузке
        checkInitialMatches();
    }

    /**
     * Обработчик клика по клетке. Реализует логику выбора двух клеток для обмена.
     * Если два клика приводят к обмену, проверяется, есть ли совпадения.
     */
    private void onTileClick(int x, int y) {
        if (firstTile == null) {
            // Первая выбранная клетка
            firstTile = tiles[x][y];
            firstTile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));  // Подсвечиваем клетку
        } else {
            // Вторая выбранная клетка
            firstTile.setBorder(null);  // Убираем подсветку с первой клетки
            GameTile secondTile = tiles[x][y];

            // Проверка, можно ли поменять местами клетки
            if (isAdjacent(firstTile, secondTile)) {
                swapTiles(firstTile, secondTile);  // Обмен клеток
                if (!hasMatches()) {
                    // Если нет совпадений, отменяем обмен
                    swapTiles(firstTile, secondTile);
                } else {
                    // Начинаем обработку совпадений
                    startMatchProcessing();
                }
            }
            firstTile = null;  // Сбрасываем первую клетку
        }
    }

    /**
     * Проверка, являются ли две клетки соседними (по горизонтали или вертикали).
     */
    private boolean isAdjacent(GameTile t1, GameTile t2) {
        Point p1 = getTilePosition(t1);
        Point p2 = getTilePosition(t2);
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) == 1;
    }

    /**
     * Получение позиции клетки на поле.
     */
    private Point getTilePosition(GameTile tile) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tiles[i][j] == tile) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Обмен типами двух клеток на поле.
     */
    private void swapTiles(GameTile t1, GameTile t2) {
        Point p1 = getTilePosition(t1);
        Point p2 = getTilePosition(t2);

        // Меняем местами типы клеток на игровом поле
        int temp = gameBoard.getType(p1.x, p1.y);
        gameBoard.setType(p1.x, p1.y, gameBoard.getType(p2.x, p2.y));
        gameBoard.setType(p2.x, p2.y, temp);

        // Обновляем UI
        updateUI();
    }

    /**
     * Проверка наличия совпадений на поле.
     * Если хотя бы одно совпадение найдено, возвращаем true.
     */
    private boolean hasMatches() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard.hasMatchAt(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Запуск таймера для обработки совпадений.
     */
    private void startMatchProcessing() {
        gameTimer = new Timer(100, e -> processMatches());
        gameTimer.start();
    }

    /**
     * Обработка совпадений.
     * Ищем все совпадения, удаляем их и заполняем пустые клетки.
     */
    private void processMatches() {
        boolean hasMatches;

        do {
            hasMatches = false;
            boolean[][] toRemove = new boolean[8][8];

            // Проверяем наличие совпадений
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (gameBoard.hasMatchAt(i, j)) {
                        gameBoard.markForRemoval(i, j, toRemove);
                        hasMatches = true;
                    }
                }
            }

            // Если есть совпадения, удаляем их и обновляем счет
            if (hasMatches) {
                gameBoard.removeMatches(toRemove);
                score += calculateScore(toRemove);
                scoreLabel.setText("Счет: " + score);
                gameBoard.fillEmptyTiles();
                updateUI(); // Обновляем интерфейс после изменения
            }

        } while (hasMatches); // Повторяем процесс, пока есть совпадения

        // Останавливаем таймер после завершения обработки
        gameTimer.stop();
    }

    /**
     * Расчет очков за удаленные клетки.
     * Каждая удаленная клетка дает 10 очков.
     */
    private int calculateScore(boolean[][] toRemove) {
        int points = 0;
        for (boolean[] row : toRemove) {
            for (boolean cell : row) {
                if (cell) {
                    points += 10;
                }
            }
        }
        return points;
    }

    /**
     * Обновление UI.
     * Перерисовывает все клетки в соответствии с состоянием игрового поля.
     */
    public void updateUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j].setType(gameBoard.getType(i, j));
            }
        }
    }

    /**
     * Проверка на наличие совпадений при начальной загрузке игры.
     * Если совпадения есть, сразу начинаем их обработку.
     */
    private void checkInitialMatches() {
        boolean hasMatches = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard.hasMatchAt(i, j)) {
                    hasMatches = true;
                    break;
                }
            }
        }

        if (hasMatches) {
            // Если есть совпадения, начинаем процесс удаления
            startMatchProcessing();
        }
    }
}