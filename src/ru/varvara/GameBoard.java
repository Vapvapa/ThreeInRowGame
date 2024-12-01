package ru.varvara;

import java.util.Random;

/**
 * Класс, представляющий игровое поле.
 * Содержит логику работы с клетками на поле: их инициализацию, проверку на совпадения,
 * удаление совпадений и заполнение пустых клеток.
 */
public class GameBoard {
    // Размер игрового поля (8x8)
    private static final int GRID_SIZE = 8;
    // Количество типов (цветов) клеток
    private static final int NUM_COLORS = 5;
    // Двумерный массив для хранения данных игрового поля
    private final int[][] board;
    // Объект для генерации случайных чисел (для случайного выбора цвета)
    private final Random random = new Random();

    /**
     * Конструктор, который инициализирует игровое поле.
     */
    public GameBoard() {
        board = new int[GRID_SIZE][GRID_SIZE];
        initializeBoard();
    }

    /**
     * Инициализация игрового поля случайными значениями типов клеток.
     * Каждой клетке на поле присваивается случайный тип от 0 до NUM_COLORS-1.
     */
    private void initializeBoard() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = random.nextInt(NUM_COLORS); // Выбор случайного типа клетки
            }
        }
    }

    /**
     * Получить тип клетки по её координатам.
     * @param x Координата по оси X
     * @param y Координата по оси Y
     * @return Тип клетки на указанной позиции
     */
    public int getType(int x, int y) {
        return board[x][y];
    }

    /**
     * Установить тип клетки на указанной позиции.
     * @param x Координата по оси X
     * @param y Координата по оси Y
     * @param type Новый тип клетки
     */
    public void setType(int x, int y, int type) {
        board[x][y] = type;
    }

    /**
     * Проверка на наличие совпадений (горизонтальных или вертикальных) для клетки.
     * Если вокруг клетки есть хотя бы два одинаковых типа клеток, то считаем, что совпадение найдено.
     * @param x Координата по оси X
     * @param y Координата по оси Y
     * @return true, если есть совпадение, иначе false
     */
    public boolean hasMatchAt(int x, int y) {
        int type = board[x][y];

        // Проверка горизонтальных совпадений
        if (x >= 2 && board[x - 1][y] == type && board[x - 2][y] == type) return true;
        if (x < GRID_SIZE - 2 && board[x + 1][y] == type && board[x + 2][y] == type) return true;

        // Проверка вертикальных совпадений
        if (y >= 2 && board[x][y - 1] == type && board[x][y - 2] == type) return true;
        if (y < GRID_SIZE - 2 && board[x][y + 1] == type && board[x][y + 2] == type) return true;

        return false;
    }

    /**
     * Помечает клетки для удаления, если они составляют совпадение.
     * Помечаются клетки, которые являются частью горизонтального или вертикального совпадения.
     * @param x Координата по оси X
     * @param y Координата по оси Y
     * @param toRemove Массив, в котором помечаются клетки для удаления
     */
    public void markForRemoval(int x, int y, boolean[][] toRemove) {
        int type = board[x][y];

        // Горизонтальные совпадения
        if (x >= 2 && board[x - 1][y] == type && board[x - 2][y] == type) {
            toRemove[x][y] = toRemove[x - 1][y] = toRemove[x - 2][y] = true;
        }

        // Вертикальные совпадения
        if (y >= 2 && board[x][y - 1] == type && board[x][y - 2] == type) {
            toRemove[x][y] = toRemove[x][y - 1] = toRemove[x][y - 2] = true;
        }
    }

    /**
     * Удаляет все клетки, помеченные для удаления.
     * Все помеченные клетки заменяются на -1 (что означает пустую клетку).
     * @param toRemove Массив, в котором помечены клетки для удаления
     */
    public void removeMatches(boolean[][] toRemove) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (toRemove[i][j]) {
                    board[i][j] = -1; // Удаляем клетку, заменяя её на -1
                }
            }
        }
    }

    /**
     * Заполняет пустые клетки (клетки с типом -1) новыми случайными значениями.
     * Клетки "падают" сверху, и новые значения заполняют пустые места.
     */
    public void fillEmptyTiles() {
        for (int j = 0; j < GRID_SIZE; j++) {
            for (int i = GRID_SIZE - 1; i >= 0; i--) {
                if (board[i][j] == -1) {  // Пустая клетка
                    // Сдвигаем все клетки ниже на одну позицию вниз
                    for (int k = i; k > 0; k--) {
                        board[k][j] = board[k - 1][j];
                    }
                    // Вставляем новую случайную клетку в верхнюю часть колонки
                    board[0][j] = random.nextInt(NUM_COLORS);
                }
            }
        }
    }
}