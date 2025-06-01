package org.example.util;

import java.util.HashMap;
import java.util.Map;

public class ColorToLineMapper {

    private static final Map<String, String> COLOR_TO_LINE = new HashMap<>();

    static {
        COLOR_TO_LINE.put("#a0a2a3", "Серпуховско-Тимирязевская линия");
        COLOR_TO_LINE.put("#bac8e8", "Бутовская линия");
        COLOR_TO_LINE.put("#b4d445", "Люблинско-Дмитровская линия");
        COLOR_TO_LINE.put("#82C0C0", "БКЛ");
        COLOR_TO_LINE.put("#FFCBDB", "Некрасовская линия");
        COLOR_TO_LINE.put("#03795F", "Троицкая линия");
        COLOR_TO_LINE.put("#03795f", "Троицкая линия");
        COLOR_TO_LINE.put("lightblue", "Филёвская линия");
        COLOR_TO_LINE.put("brown", "Кольцевая линия");
        COLOR_TO_LINE.put("red", "Сокольническая линия");
        COLOR_TO_LINE.put("yellow", "Солнцевская линия");
        COLOR_TO_LINE.put("green", "Замоскворецкая линия");
        COLOR_TO_LINE.put("blue", "Арбатско-Покровская линия");
        COLOR_TO_LINE.put("orange", "Калужско-Рижская линия");
        COLOR_TO_LINE.put("violet", "Таганско-Краснопресненская линия");
    }

    /**
     * Получить название линии по цвету. Если цвет неизвестен, вернуть "Неизвестная линия".
     *
     * @param colorRaw строка с цветом (возможно с несколькими цветами через ; без проблела).
     * @return название линии
     */
    public static String getLineNameFromColor(String colorRaw) {
        if (colorRaw == null || colorRaw.isBlank()) {
            return "Неизвестная линия";
        }

        String[] colors = colorRaw.split(";");
        String firstColor = colors[0].trim().toLowerCase();

        return COLOR_TO_LINE.getOrDefault(firstColor, "Неизвестная линия");
    }
}
