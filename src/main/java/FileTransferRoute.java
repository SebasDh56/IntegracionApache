import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;

import java.time.LocalDateTime;

public class FileTransferRoute extends RouteBuilder {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.configure().addRoutesBuilder(new FileTransferRoute());
        main.run();
    }

    @Override
    public void configure() {

        from("file:input?noop=true")
                // Filtra solo archivos CSV
                .filter(header("CamelFileName").endsWith(".csv"))

                // Log con fecha y hora
                .log("Procesando archivo: ${file:name} a las " + LocalDateTime.now())

                // 🔥 SOLUCIÓN: convertir archivo a texto
                .convertBodyTo(String.class)

                // Transformación a MAYÚSCULAS
                .transform().simple("${body.toUpperCase()}")

                // Enviar a carpeta output
                .to("file:output")

                // Guardar copia en archived
                .to("file:archivede");
    }
}