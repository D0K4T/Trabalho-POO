import java.io.Serializable;

public class CardapioItem implements Serializable {
    private String nome;
    private String descricao;

    public CardapioItem(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return nome + " - " + descricao;
    }
}
