package scorta;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.plugin.OpenApiPlugin;

public class Backend {

	public static void main(String[] args) {
		var _ = Javalin.create(config -> {
			config.staticFiles.add(staticFiles -> {
				staticFiles.hostedPath = "/";
				staticFiles.directory = "/public";
				staticFiles.location = Location.CLASSPATH;
			});

			config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
				pluginConfig.withDefinitionConfiguration((_, definition) -> {
					definition.info(info -> info.title("Hello API"));
				});
			}));

			config.routes.get("/api/hello", HelloController::get);

		}).start(8080);
	}
}
