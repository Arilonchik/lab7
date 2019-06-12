package bundel;

import java.util.ListResourceBundle;

public class Lang_ru_RU extends ListResourceBundle {
    public Object[][] getContents() { return contents; }
    private Object[][] contents = {

            {"Hello brotishka!", "Прривет, братик!"},
            {"Authorization", "Авторизация"},
            {"Registration", "Регистрация"},
            {"Exit", "Выход"},
            {"Hmm, who're u?", "Хммм, ты кто по жизни?"},
            {"We didn't see u before...", "Мы таких не знаем. Представьтесь, пожалуйста."},
            {"Enter ur Email", "Введите вашу почту, пожалуйста."},
            {"sexLabel", "Пол:"},
            {"anySexRadio", "Любой"},
            {"mRadio", "М"},
            {"fRadio", "Ж"},
            {"colorLabel", "Цвет:"},
            {"levelLabel", "Уровень = "},
            {"expLabel", "Опыт = "},
            {"hpLabel", "ОЗ = "},
            {"staminaLabel", "Энергия = "},
            {"wNameLabel", "Имя:"},
            {"tLabel", "Температура: "},
            {"hilo.0", "ВЫШЕ ЧЕМ"},
            {"hilo.1", "НИЖЕ ЧЕМ"},
            {"distLabel", "Радиус = "},
            {"startButton", "СТАРТ"},
            {"stopButton", "СТОП"},
            {"drawPanel", "Карта"},
            {"cPanel", "Существа"},
            {"wPanel", "Погода"},
            {"areaPanel", "Область"},
            {"controlPanel", "Фильтр"}

    };
}
