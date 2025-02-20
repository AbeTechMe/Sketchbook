package org.FipeApp;

import org.FipeApp.Model.Anos;
import org.FipeApp.Model.Marca;
import org.FipeApp.Model.Valor;
import org.FipeApp.Model.Modelos;
import org.FipeApp.Services.ApiRequest;
import org.FipeApp.Services.JsonToObject;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n🚗 Bem-vindo ao FIPE APP! 🚀\n");

        // Busca dos dados
        String url = "https://parallelum.com.br/fipe/api/v1/carros/marcas/";
        String json = ApiRequest.getData(url);
        List<Marca> marcas = JsonToObject.converterList(json, Marca.class);

        System.out.println("🔹 Marcas disponíveis:");
        marcas.forEach(m -> System.out.println("➡️ " + m.name()));

        // Interação marcas
        System.out.print("\n📝 Digite a marca desejada: ");
        String read = scanner.nextLine();

        Optional<String> idMarca = marcas.stream()
                .filter(m -> m.name().equalsIgnoreCase(read))
                .map(Marca::id)
                .findFirst();

        if (idMarca.isPresent()) {
            String modelsUrl = "https://parallelum.com.br/fipe/api/v1/carros/marcas/" + idMarca.get() + "/modelos/";
            json = ApiRequest.getData(modelsUrl);
            List<Modelos> modelos = JsonToObject.converterObjList(json, Modelos.class, "modelos");

            System.out.println("\n📌 Modelos disponíveis:");
            modelos.forEach(m -> System.out.println("➡️ " + m.name()));

            if (!modelos.isEmpty()) {
                System.out.print("\n📝 Digite o modelo desejado: ");
                String finalRead = scanner.nextLine();

                Optional<String> idModelo = modelos.stream()
                        .filter(m -> m.name().toLowerCase().contains(finalRead.toLowerCase()))
                        .map(Modelos::id)
                        .findFirst();

                if (idModelo.isPresent()) {
                    String anosUrl = modelsUrl + idModelo.get() + "/anos/";
                    json = ApiRequest.getData(anosUrl);
                    List<Anos> anos = JsonToObject.converterList(json, Anos.class);
                    List<String> codAno = anos.stream().map(Anos::id).toList();

                    System.out.println("\n📅 Anos disponíveis para " + finalRead + ":");
                    for (String s : codAno) {
                        json = ApiRequest.getData(anosUrl + s);
                        Valor valor = JsonToObject.converterObject(json, Valor.class);

                        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                        System.out.println("🛠️ Ano: " + s);
                        System.out.println("💰 Valor: " + valor.valor());
                        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    }
                } else {
                    System.out.println("\n❌ Modelo não encontrado! 😢");
                }
            } else {
                System.out.println("\n❌ Nenhum modelo encontrado para essa marca! 😢");
            }
        } else {
            System.out.println("\n❌ Marca não encontrada! 😢");
        }
    }
}
