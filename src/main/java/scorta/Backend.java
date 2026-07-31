package scorta;

import io.javalin.Javalin;

public class Backend {

    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(8080);

        app.get("/api/hello", ctx -> {
            ctx.contentType("text/plain; charset=utf-8");
            ctx.result("Привет из Javalin бэкенда!");
        });
    }
}
