import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CardapioDia implements Serializable {
    private List<CardapioItem> itens;

    public CardapioDia() {
        itens = new ArrayList<>();
    }

    public void adicionarItem(CardapioItem item) {
        itens.add(item);
    }

    public void removerItem(CardapioItem item) {
        itens.remove(item);
    }

    public List<CardapioItem> getItens() {
        return itens;
    }
}
