package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaGpt;

import java.util.OptionalDouble;

public class Serie {

    private String sinopse;
    private String titulo;
    private String ano;
    private String classificacao;
    private String dataLancamento;
    private String escritor;
    private String idioma;
    private String pais;
    private String premios;
    private String duracao;
    private Integer totalTemporadas;
    private Double avaliacao;
    private Categoria genero;
    private String atores;
    private String poster;
    private String notaPublica;


    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.parseDouble(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poster = dadosSerie.poster();
        this.sinopse =  ConsultaGpt.obterTraducao(dadosSerie.sinopse());
        this.ano = dadosSerie.ano();
        this.classificacao = dadosSerie.classificacao();
        this.dataLancamento = dadosSerie.dataLancamento();
        this.escritor = dadosSerie.escritor();
        this.idioma = dadosSerie.idioma();
        this.pais = dadosSerie.pais();
        this.premios = dadosSerie.premios();
        this.duracao = dadosSerie.duracao();
        this.notaPublica = dadosSerie.getPrimeiraAvaliacao();
    }

    public Object getSinopse() {
        return sinopse;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAno() {
        return ano;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public String getEscritor() {
        return escritor;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getPais() {
        return pais;
    }

    public String getPremios() {
        return premios;
    }

    public String getDuracao() {
        return duracao;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public String getAtores() {
        return atores;
    }

    public String getPoster() {
        return poster;
    }

    public String getNotaPublica() {
        return notaPublica;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public void setEscritor(String escritor) {
        this.escritor = escritor;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setPremios(String premios) {
        this.premios = premios;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setNotaPublica(String notaPublica) {
        this.notaPublica = notaPublica;
    }

    @Override
    public String toString() {
        return String.format("""
                \uD83C\uDFAC \u001B[36m========== Detalhes da Série ========== \u001B[0m 

                \uD83D\uDCDA \u001B[34mTítulo:\u001B[0m %s
                📆 \u001B[32mAno:\u001B[0m %s
                🏆 \u001B[33mPrêmios:\u001B[0m %s
                \uD83D\uDCC5 \u001B[32mLançamento:\u001B[0m %s
                ⏳ \u001B[31mDuração:\u001B[0m %s
                🎭 \u001B[36mGênero:\u001B[0m %s
                📝 \u001B[35mEscritor:\u001B[0m %s
                🎭 \u001B[35mAtores:\u001B[0m %s
                \uD83D\uDCC8 \u001B[33mTemporadas:\u001B[0m %d
                ⭐ \u001B[35mAvaliação IMDb:\u001B[0m %s
                🎖️ \u001B[36mAvaliação Pública:\u001B[0m %s

                \uD83C\uDFA5 \u001B[36mSinopse:\u001B[0m 
                "%s"

                🌎 \u001B[33mIdioma:\u001B[0m %s | \uD83C\uDF0D País: %s

                🖼️ \u001B[34mPoster:\u001B[0m %s

                \uD83C\uDFAC \u001B[36m==================================== \u001B[0m 
                """,
                getTitulo(), getAno(), getPremios(), getDataLancamento(), getDuracao(), getGenero(), getEscritor(), getAtores(), getTotalTemporadas(),
                getAvaliacao(), getNotaPublica(), getSinopse(), getIdioma(), getPais(), getPoster());
    }
}
