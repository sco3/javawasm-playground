package scorta;

import org.teavm.jso.JSBody;

public class Frontend {

    public static void main(String[] args) {
        initApp();
    }

    @JSBody(
        script = """
            console.log("TeaVM Frontend инициализирован");

            const btn = document.getElementById("loadBtn");
            const output = document.getElementById("output");

            if (!btn || !output) {
                console.error("Элементы не найдены в DOM!");
                return;
            }

            btn.addEventListener("click", async () => {
                try {
                    console.log("Отправка запроса к бэкенду...");
                    const response = await fetch("/api/hello");
                    const text = await response.text();
                    output.textContent = text;
                    console.log("Ответ получен:", text);
                } catch (err) {
                    console.error("Ошибка запроса:", err);
                    output.textContent = "Ошибка связи с сервером";
                }
            });
        """
    )
    public static native void initApp();
}
