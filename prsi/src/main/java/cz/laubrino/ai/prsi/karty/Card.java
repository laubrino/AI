package cz.laubrino.ai.prsi.karty;

/**
 * @author tomas.laubr on 9.10.2019.
 */
public enum Card {
    ZELENA_SEDMA(Color.ZELENY, Value.SEDMA),
    ZELENA_OSMA(Color.ZELENY, Value.OSMA),
    ZELENA_DEVITKA(Color.ZELENY, Value.DEVITKA),
    ZELENA_DESITKA(Color.ZELENY, Value.DESITKA),
    ZELENY_SPODEK(Color.ZELENY, Value.SPODEK),
    ZELENY_SVRSEK(Color.ZELENY, Value.SVRSEK),
    ZELENY_KRAL(Color.ZELENY, Value.KRAL),
    ZELENE_ESO(Color.ZELENY, Value.ESO),

    CERVENA_SEDMA(Color.CERVENY, Value.SEDMA),
    CERVENA_OSMA(Color.CERVENY, Value.OSMA),
    CERVENA_DEVITKA(Color.CERVENY, Value.DEVITKA),
    CERVENA_DESITKA(Color.CERVENY, Value.DESITKA),
    CERVENY_SPODEK(Color.CERVENY, Value.SPODEK),
    CERVENY_SVRSEK(Color.CERVENY, Value.SVRSEK),
    CERVENY_KRAL(Color.CERVENY, Value.KRAL),
    CERVENE_ESO(Color.CERVENY, Value.ESO),

    KULOVA_SEDMA(Color.KULE, Value.SEDMA),
    KULOVA_OSMA(Color.KULE, Value.OSMA),
    KULOVA_DEVITKA(Color.KULE, Value.DEVITKA),
    KULOVA_DESITKA(Color.KULE, Value.DESITKA),
    KULOVY_SPODEK(Color.KULE, Value.SPODEK),
    KULOVY_SVRSEK(Color.KULE, Value.SVRSEK),
    KULOVY_KRAL(Color.KULE, Value.KRAL),
    KOLOVE_ESO(Color.KULE, Value.ESO),

    ZALUDOVA_SEDMA(Color.ZALUDY, Value.SEDMA),
    ZALUDOVA_OSMA(Color.ZALUDY, Value.OSMA),
    ZALUDOVA_DEVITKA(Color.ZALUDY, Value.DEVITKA),
    ZALUDOVA_DESITKA(Color.ZALUDY, Value.DESITKA),
    ZALUDOVY_SPODEK(Color.ZALUDY, Value.SPODEK),
    ZALUDOVY_SVRSEK(Color.ZALUDY, Value.SVRSEK),
    ZALUDOVY_KRAL(Color.ZALUDY, Value.KRAL),
    ZALUDOVE_ESO(Color.ZALUDY, Value.ESO);
    
    Card(Color color, Value value) {
        this.value = value;
        this.color = color;
    }

    private Value value;
    private Color color;

    public Value getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }
}
