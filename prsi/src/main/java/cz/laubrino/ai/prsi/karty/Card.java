package cz.laubrino.ai.prsi.karty;

/**
 * @author tomas.laubr on 9.10.2019.
 */
public enum Card {
    ZELENA_SEDMA(Color.ZELENY, Value.SEDMA, "L7"),
    ZELENA_OSMA(Color.ZELENY, Value.OSMA, "L8"),
    ZELENA_DEVITKA(Color.ZELENY, Value.DEVITKA, "L9"),
    ZELENA_DESITKA(Color.ZELENY, Value.DESITKA, "L0"),
    ZELENY_SPODEK(Color.ZELENY, Value.SPODEK, "LS"),
    ZELENY_SVRSEK(Color.ZELENY, Value.SVRSEK, "LF"),
    ZELENY_KRAL(Color.ZELENY, Value.KRAL, "LK"),
    ZELENE_ESO(Color.ZELENY, Value.ESO, "LE"),

    CERVENA_SEDMA(Color.CERVENY, Value.SEDMA, "C7"),
    CERVENA_OSMA(Color.CERVENY, Value.OSMA, "C8"),
    CERVENA_DEVITKA(Color.CERVENY, Value.DEVITKA, "C9"),
    CERVENA_DESITKA(Color.CERVENY, Value.DESITKA, "C0"),
    CERVENY_SPODEK(Color.CERVENY, Value.SPODEK, "CS"),
    CERVENY_SVRSEK(Color.CERVENY, Value.SVRSEK, "CF"),
    CERVENY_KRAL(Color.CERVENY, Value.KRAL, "CK"),
    CERVENE_ESO(Color.CERVENY, Value.ESO, "CE"),

    KULOVA_SEDMA(Color.KULE, Value.SEDMA, "K7"),
    KULOVA_OSMA(Color.KULE, Value.OSMA, "K8"),
    KULOVA_DEVITKA(Color.KULE, Value.DEVITKA, "K9"),
    KULOVA_DESITKA(Color.KULE, Value.DESITKA, "K0"),
    KULOVY_SPODEK(Color.KULE, Value.SPODEK, "KS"),
    KULOVY_SVRSEK(Color.KULE, Value.SVRSEK, "KF"),
    KULOVY_KRAL(Color.KULE, Value.KRAL, "KK"),
    KULOVE_ESO(Color.KULE, Value.ESO, "KE"),

    ZALUDOVA_SEDMA(Color.ZALUDY, Value.SEDMA, "Z7"),
    ZALUDOVA_OSMA(Color.ZALUDY, Value.OSMA, "Z8"),
    ZALUDOVA_DEVITKA(Color.ZALUDY, Value.DEVITKA, "Z9"),
    ZALUDOVA_DESITKA(Color.ZALUDY, Value.DESITKA, "Z0"),
    ZALUDOVY_SPODEK(Color.ZALUDY, Value.SPODEK, "ZS"),
    ZALUDOVY_SVRSEK(Color.ZALUDY, Value.SVRSEK, "ZF"),
    ZALUDOVY_KRAL(Color.ZALUDY, Value.KRAL, "ZK"),
    ZALUDOVE_ESO(Color.ZALUDY, Value.ESO, "ZE");
    
    Card(Color color, Value value, String string) {
        this.value = value;
        this.color = color;
        this.string = string;
    }

    private Value value;
    private Color color;
    private String string;

    public Value getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return string;
    }
}
