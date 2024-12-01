package ru.varvara;

import javax.swing.*;
import java.awt.*;

/**
 * Класс GameTile представляет собой клетку на игровом поле.
 * Каждая клетка имеет свой тип, который определяет её цвет.
 * Наследует от JButton, чтобы быть кнопкой, и использует её функциональность.
 */
public class GameTile extends JButton {

    private int type; // Тип элемента (целое число, которое определяет цвет клетки)

    /**
     * Конструктор, который инициализирует тип клетки и задает начальные настройки.
     * @param type Тип клетки, который используется для определения её цвета.
     */
    public GameTile(int type) {
        this.type = type; // Сохраняем тип клетки
        setBackground(getColor(type)); // Устанавливаем фон клетки в зависимости от типа
        setOpaque(true); // Делаем клетку непрозрачной
        setBorderPainted(false); // Отключаем стандартную рамку кнопки
    }

    /**
     * Геттер для получения типа клетки.
     * @return Возвращает тип клетки.
     */
    public int getType() {
        return type;
    }

    /**
     * Сеттер для изменения типа клетки.
     * При изменении типа, также обновляется цвет клетки.
     * @param type Новый тип клетки.
     */
    public void setType(int type) {
        this.type = type; // Обновляем тип клетки
        setBackground(getColor(type)); // Обновляем фон клетки в зависимости от нового типа
    }

    /**
     * Метод для получения цвета клетки в зависимости от её типа.
     * @param type Тип клетки.
     * @return Цвет, соответствующий типу клетки.
     */
    private Color getColor(int type) {
        switch (type) { // В зависимости от типа клетки, возвращаем соответствующий цвет
            case 0: return new Color(251, 90, 0);   // #fb5a00
            case 1: return new Color(174, 239, 139); // #aeef8b
            case 2: return new Color(106, 148, 84);  // #6a9454
            case 3: return new Color(235, 176, 247); // #ebb0f7
            case 4: return new Color(171, 50, 195);  // #ab32c3
            default: return new Color(255, 194, 170); // #ffc2aa (цвет по умолчанию)
        }
    }
}
