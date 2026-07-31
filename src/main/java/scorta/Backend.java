package scorta;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Backend {

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

            config.routes.get("/api/hello", ctx -> {
                ctx.result("Hello from Java Backend!");
            });
        }).start(8080);
    }
}
